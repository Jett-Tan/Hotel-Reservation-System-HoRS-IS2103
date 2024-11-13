/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateless.AllocationReportSessionBeanLocal;
import ejb.session.stateless.ReservationSessionBeanLocal;
import ejb.session.stateless.RoomSessionBeanLocal;
import ejb.session.stateless.SearchRoomSessionBeanLocal;
import entity.AllocationReport;
import entity.Reservation;
import entity.Room;
import entity.RoomType;
import enumerations.AllocationTypeEnum;
import exception.AllocationException;
import exception.ReservationNotFoundException;
import exception.RoomNotFoundException;
import exception.RoomTypeNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author Tan Jian Feng
 */
@Singleton
public class AllocationSingleton implements AllocationSingletonRemote, AllocationSingletonLocal {

    @EJB(name = "AllocationReportSessionBeanLocal")
    private AllocationReportSessionBeanLocal allocationReportSessionBeanLocal;

    @EJB(name = "SearchRoomSessionBeanLocal")
    private SearchRoomSessionBeanLocal searchRoomSessionBeanLocal;

    @EJB(name = "RoomSessionBeanLocal")
    private RoomSessionBeanLocal roomSessionBeanLocal;

    @EJB(name = "ReservationSessionBeanLocal")
    private ReservationSessionBeanLocal reservationSessionBeanLocal;
    
    
    @Override
    @Schedule(hour = "2")
    public void allocateRoom() {
        Date today = new Date();
        allocateRoom(today);
    }
    
    @Override
    public void allocateRoom(Date currentDate) {
        Date today = currentDate;
        System.out.println("Allocating Room reservation for "  + today);
        List<Reservation> reservations = reservationSessionBeanLocal.retrieveAllReseravtions();
        reservations.removeIf(x ->!(x.getStartDate().getDate() == today.getDate() &&
                            x.getStartDate().getYear() == today.getYear()&& 
                            x.getStartDate().getMonth()== today.getMonth()));
        System.out.println("Allocating Room for Reservations ");
        reservations.forEach(x -> System.out.println(x));
        reservations.forEach(x -> {
            if(x.getAllocated()) {
                try {
                    Reservation managedReservation = reservationSessionBeanLocal.getLoadedReservation(x);
                    BigDecimal numOfRooms = managedReservation.getNumOfRooms();
                    List<Room> reservationRooms = managedReservation.getRoomList();
                    Date checkIndate = managedReservation.getStartDate();
                    Date checkOutDate = managedReservation.getEndDate();
                    RoomType roomType = managedReservation.getRoomType();
                    RoomType parentRoomType = roomType.getParentRoomType();
                    List<Room> availableRooms = new ArrayList<>();
                    AllocationReport allocationReport = new AllocationReport();
                    allocationReport.setReservation(managedReservation);
                    if (!managedReservation.getAllocated()) {
                        try {
                            boolean needToCreateAllocationReport = false;
                            boolean needToHaltAllocation = false;
                            
                            availableRooms = searchRoomSessionBeanLocal.searchRooms(checkIndate, checkOutDate, roomType);
                            while (numOfRooms.intValue() > 0 && availableRooms.size() > 0) {
                                Room room = availableRooms.get(0);
                                availableRooms.remove(0);
                                reservationRooms.add(room);
                                numOfRooms = numOfRooms.subtract(BigDecimal.ONE);
                            }
                            if (numOfRooms.intValue() > 0) {
                                availableRooms = searchRoomSessionBeanLocal.searchRooms(checkIndate, checkOutDate, parentRoomType);
                                while (numOfRooms.intValue() > 0 && availableRooms.size() > 0) {
                                    Room room = availableRooms.get(0);
                                    availableRooms.remove(0);
                                    reservationRooms.add(room);
                                    numOfRooms = numOfRooms.subtract(BigDecimal.ONE);
                                }
                                allocationReport.setType(AllocationTypeEnum.UPGRADED);
                                needToCreateAllocationReport = true;
                            }
                            if (numOfRooms.intValue() > 0) {
                                allocationReport.setType(AllocationTypeEnum.NO_UPGRADE);
                                needToCreateAllocationReport = true;
                                needToHaltAllocation = true;
                            }
                            if (needToCreateAllocationReport) {
                                ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
                                Validator validator = validatorFactory.getValidator();
                                Set<ConstraintViolation<AllocationReport>> errorList = validator.validate(allocationReport);
                                if (errorList.isEmpty()) {
                                    allocationReport = allocationReportSessionBeanLocal.createNewAllocationReport(allocationReport);
                                }
                            }
                        } catch (RoomNotFoundException | RoomTypeNotFoundException ex) {
                            System.out.println(ex.getMessage());
                        }
                        
                        managedReservation.setAllocated(Boolean.TRUE);
                        managedReservation.setRoomList(reservationRooms);
                        for (Room room : reservationRooms) {
                            Date temp = checkIndate;
                            while (temp.before(checkOutDate) || temp.equals(checkOutDate)) {
                                room.getBookedDates().add(temp);
                                temp = searchRoomSessionBeanLocal.addDays(temp, 1);
                            }
                        }
                    }
                } catch (ReservationNotFoundException ex) {
                    Logger.getLogger(AllocationSingleton.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public Reservation manualAllocateRooms(Reservation reservation) throws ReservationNotFoundException, AllocationException {
        System.out.println("RUNNING MANUAL ALLOCATE ROOMS");
        Reservation managedReservation = reservationSessionBeanLocal.getLoadedReservation(reservation);
        BigDecimal numOfRooms = managedReservation.getNumOfRooms();
        List<Room> reservationRooms = managedReservation.getRoomList();
        Date checkIndate = managedReservation.getStartDate();
        Date checkOutDate = managedReservation.getEndDate();
        RoomType roomType = managedReservation.getRoomType();
        RoomType parentRoomType = roomType.getParentRoomType();
        List<Room> availableRooms = new ArrayList<>();
        AllocationReport allocationReport = new AllocationReport();
        allocationReport.setReservation(managedReservation);
        if(!managedReservation.getAllocated()){
            try {
                boolean needToCreateAllocationReport = false;
                boolean needToHaltAllocation = false;

                availableRooms = searchRoomSessionBeanLocal.searchRooms(checkIndate, checkOutDate, roomType);
                while(numOfRooms.intValue() > 0 && availableRooms.size() > 0){
                    Room room = availableRooms.get(0);
                    availableRooms.remove(0);
                    reservationRooms.add(room);
                    numOfRooms = numOfRooms.subtract(BigDecimal.ONE);
                }
                if(numOfRooms.intValue() > 0) {
                    availableRooms = searchRoomSessionBeanLocal.searchRooms(checkIndate, checkOutDate, parentRoomType);
                    while(numOfRooms.intValue() > 0 && availableRooms.size() > 0){
                        Room room = availableRooms.get(0);
                        availableRooms.remove(0);
                        reservationRooms.add(room);
                        numOfRooms = numOfRooms.subtract(BigDecimal.ONE);
                    }
                    allocationReport.setType(AllocationTypeEnum.UPGRADED);
                    needToCreateAllocationReport = true;
                }
                if(numOfRooms.intValue() > 0) {
                    allocationReport.setType(AllocationTypeEnum.NO_UPGRADE);
                    needToCreateAllocationReport = true;
                    needToHaltAllocation = true;
                }
                if(needToCreateAllocationReport) {
                    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
                    Validator validator = validatorFactory.getValidator();
                    Set<ConstraintViolation<AllocationReport>> errorList = validator.validate(allocationReport);
                    if(errorList.isEmpty()){
                        allocationReport = allocationReportSessionBeanLocal.createNewAllocationReport(allocationReport);
                    }
                    if(needToHaltAllocation) {
                        throw new AllocationException("Error allocating rooms");
                    }
                }
            } catch (RoomNotFoundException | RoomTypeNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
            
            managedReservation.setAllocated(Boolean.TRUE);
            managedReservation.setRoomList(reservationRooms);
            for(Room room : reservationRooms){
                Date temp = checkIndate;
                while(temp.before(checkOutDate) || temp.equals(checkOutDate)){
                    room.getBookedDates().add(temp);
//                    room.setIsCheckedIn(true);
                    temp = searchRoomSessionBeanLocal.addDays(temp, 1);
                }
            }
        } 
        managedReservation.getRoomList().size();
        return managedReservation;
    }
    

}
