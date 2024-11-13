/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Reservation;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import enumerations.ReservationTypeEnum;
import enumerations.RoomRateTypeEnum;
import enumerations.RoomStatusEnum;
import exception.RoomNotFoundException;
import exception.RoomTypeNotFoundException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Tan Jian Feng
 */
@Stateless
public class SearchRoomSessionBean implements SearchRoomSessionBeanRemote, SearchRoomSessionBeanLocal {

    @EJB(name = "ReservationSessionBeanLocal")
    private ReservationSessionBeanLocal reservationSessionBeanLocal;

    @EJB(name = "RoomSessionBeanLocal")
    private RoomSessionBeanLocal roomSessionBeanLocal;

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;


    
    @Override
    public List<Room> searchRooms(Date checkIndate, Date checkOutDate, RoomType roomType) throws RoomNotFoundException, RoomTypeNotFoundException {
        List<Room> rooms = roomSessionBeanLocal.getRooms();
        rooms.removeIf(x -> x.getRoomStatus().equals(RoomStatusEnum.UNAVAILABLE));
        rooms.removeIf(x -> {
            boolean free = false;
            List<Date> bookedDates = x.getBookedDates();
            Date checkin = checkIndate;
            Date checkout = checkOutDate;
            do {
                for(Date bookedDate : bookedDates) {
                    if(bookedDate.getYear() == (checkin.getYear()) &&
                       bookedDate.getMonth()== (checkin.getMonth()) &&
                       bookedDate.getDate()== (checkin.getDate()) ) {
                        return true;
                    }
                }
                checkin = addDays(checkin,1);
            } while (checkin.before(checkout) || checkin.equals(checkout));
            return free;
        });
        rooms.removeIf(x -> !x.getRoomRmType().getName().equals(roomType.getName()));
        return rooms;
    }
    
    @Override
    public Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }
    
    public int getNumberOfAvailableRoom(Date checkInDate, Date checkOutDate,RoomType roomType) throws RoomNotFoundException {
        List<Room> rooms = roomSessionBeanLocal.getRooms();
        
        rooms.removeIf(x -> !x.getRoomRmType().getName().equals(roomType.getName()));
        rooms.removeIf(x -> {
            boolean free = false;
            List<Date> bookedDates = x.getBookedDates();
            Date temp = checkInDate;
            do {
                for(Date bookedDate : bookedDates) {
                    if(bookedDate.getYear() == (temp.getYear()) &&
                       bookedDate.getMonth()== (temp.getMonth()) &&
                       bookedDate.getDate()== (temp.getDate()) ) {
                        return true;
                    }
                }
                temp = addDays(temp,1);
            } while (temp.before(checkOutDate));
//            } while (temp.before(checkOutDate) || temp.equals(checkOutDate));// Original code but has to remove equals
            return free;
        });
        rooms.removeIf(x -> x.getRoomStatus().equals(RoomStatusEnum.UNAVAILABLE));
        List<Reservation> reservations = reservationSessionBeanLocal.retrieveAllReservationWithinDates(checkInDate, checkOutDate,roomType);
        int numOfRooms = reservations.stream().filter(x -> !x.getAllocated()).map(x -> x.getNumOfRooms()).reduce(BigDecimal.ZERO,(x,y)->x.add(y)).intValue();
//        System.out.println(reservations);
//        System.out.println("numofRooms"+numOfRooms);
//        System.out.println("roomSize" + rooms.size());
        return rooms.size() - numOfRooms;
    }
    
    @Override
    public List<Reservation> generateReservationWalkIn(Date checkInDate,Date checkOutDate) throws RoomNotFoundException {
        List<Reservation> reservations = new ArrayList<>();
        List<Room> allRooms = roomSessionBeanLocal.getRooms();
        allRooms.forEach(x -> em.detach(x));
        allRooms.removeIf(x -> x.getRoomStatus().equals(RoomStatusEnum.UNAVAILABLE));
        allRooms.removeIf(x -> {
//            boolean free = false;
            List<Date> bookedDates = x.getBookedDates();
            Date checkin = checkInDate;
            Date checkout = checkOutDate;
            do {
                for(Date bookedDate : bookedDates) {
                    if(bookedDate.getYear() == (checkin.getYear()) &&
                       bookedDate.getMonth()== (checkin.getMonth()) &&
                       bookedDate.getDate()== (checkin.getDate()) ) {
                        return true;
                    }
                }
                checkin = addDays(checkin,1);
            } while (checkin.before(checkout));            
//            } while (checkin.before(checkout) || checkin.equals(checkout)); // Original code but has to remove equals

            return false;
        });
        List<Room> distinctRooms = new ArrayList<>();
        for(Room room : allRooms) {
            boolean add = true;
            for(Room distinctRoom : distinctRooms) {
                if (room.getRoomRmType().equals(distinctRoom.getRoomRmType())) {
                    add = false;
                    break;
                }
            }
            if(add) {
                distinctRooms.add(room);
            }            
        }
        System.out.println("SearchRoomSessionBean distinctRooms before filter: " + distinctRooms);
        distinctRooms.removeIf(x -> {
            try {
                return !(getNumberOfAvailableRoom(checkInDate,checkOutDate,x.getRoomRmType()) > 0);
            } catch (RoomNotFoundException ex) {
                return true;
            }
        });
        System.out.println("SearchRoomSessionBean distinctRooms after filter: " + distinctRooms);
        for(Room room : distinctRooms) {
            em.detach(room.getRoomRmType());
            Reservation current = new Reservation();
            current.setStartDate(checkInDate);
            current.setEndDate(checkOutDate);
            current.setNumOfRooms(new BigDecimal(getNumberOfAvailableRoom(checkInDate,checkOutDate,room.getRoomRmType())));
            current.setAllocated(Boolean.FALSE);
            current.setReservationType(ReservationTypeEnum.WALK_IN);
            current.setRoomType(room.getRoomRmType());
            List<RoomRate> roomRates = room.getRoomRmType().getRoomRates();
            System.out.println("SearchRoomSessionBean roomRates before filter for " +room.getRoomRmType().getName()+ " | "+ roomRates);
            roomRates.forEach(x -> em.detach(x));
            roomRates.removeIf(x -> !x.getRoomRateType().equals(RoomRateTypeEnum.PUBLISHED));
            System.out.println("SearchRoomSessionBean roomRates  after filter for " +room.getRoomRmType().getName()+ " | "+ roomRates);
            BigDecimal totalAmount = BigDecimal.ZERO;
            long diffInMillies = Math.abs(checkInDate.getTime() - checkOutDate.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            BigDecimal numOfDays = new BigDecimal(diff);
//            System.out.println(numOfDays);
            for(RoomRate x : roomRates) {
                totalAmount = totalAmount.add(x.getRate().multiply(numOfDays));
            }
            
            current.setAmountPerRoom(totalAmount);
            reservations.add(current);
        }
        
        return reservations;
    }

    @Override
    public List<Reservation> generateReservationOnline(Date checkInDate, Date checkOutDate) throws RoomNotFoundException {
        List<Reservation> reservations = new ArrayList<>();
        List<Room> allRooms = roomSessionBeanLocal.getRooms();
        allRooms.forEach(x -> em.detach(x));
        allRooms.removeIf(x -> x.getRoomStatus().equals(RoomStatusEnum.UNAVAILABLE));
        allRooms.removeIf(x -> {
//            boolean free = false;
            List<Date> bookedDates = x.getBookedDates();
            Date checkin = checkInDate;
            Date checkout = checkOutDate;
            do {
                for(Date bookedDate : bookedDates) {
                    if(bookedDate.getYear() == (checkin.getYear()) &&
                       bookedDate.getMonth()== (checkin.getMonth()) &&
                       bookedDate.getDate()== (checkin.getDate()) ) {
                        return true;
                    }
                }
                checkin = addDays(checkin,1);
            } while (checkin.before(checkout));
//            } while (checkin.before(checkout) || checkin.equals(checkout));

            return false;
        });
        List<Room> distinctRooms = new ArrayList<>();
        for(Room room : allRooms) {
            boolean add = true;
            for(Room distinctRoom : distinctRooms) {
                if (room.getRoomRmType().equals(distinctRoom.getRoomRmType())) {
                    add = false;
                    break;
                }
            }
            if(add) {
                distinctRooms.add(room);
            }            
        }
        
        System.out.println("SearchRoomSessionBean distinctRooms before filter: " + distinctRooms);
        distinctRooms.removeIf(x -> {
            try {
                return !(getNumberOfAvailableRoom(checkInDate,checkOutDate,x.getRoomRmType()) > 0);
            } catch (RoomNotFoundException ex) {
                return true;
            }
        });
        System.out.println("SearchRoomSessionBean distinctRooms after filter: " + distinctRooms);
        for(Room room : distinctRooms) {
            Reservation current = new Reservation();
            em.detach(room.getRoomRmType());
            current.setStartDate(checkInDate);
            current.setEndDate(checkOutDate);
            current.setNumOfRooms(new BigDecimal(getNumberOfAvailableRoom(checkInDate,checkOutDate,room.getRoomRmType())));
            current.setAllocated(Boolean.FALSE);
            current.setReservationType(ReservationTypeEnum.ONLINE);
            current.setRoomType(room.getRoomRmType());
            List<RoomRate> roomRates = room.getRoomRmType().getRoomRates();
            roomRates.forEach(x -> em.detach(x));
            roomRates.removeIf(x -> x.getRoomRateType().equals(RoomRateTypeEnum.PUBLISHED));
            BigDecimal totalAmount = BigDecimal.ZERO;
            Date tempDate = checkInDate;
            System.out.println("SearchRoomSessionBean roomRates for " +room.getRoomRmType().getName()+ " | "+ roomRates);
            do {
//                System.out.println("Looking at " + tempDate);
                RoomRate selectedRoomRate;
                if(roomRates.size() > 0) {
                    selectedRoomRate = roomRates.get(0);
                    for(RoomRate roomRate: roomRates) {
                        if(((tempDate.getYear() == roomRate.getEndDate().getYear() &&
                            tempDate.getMonth()== roomRate.getEndDate().getMonth() &&
                            tempDate.getDate()== roomRate.getEndDate().getDate()) 
                            || tempDate.before(roomRate.getEndDate())
                            )&& 
                            ((tempDate.getYear() == roomRate.getStartDate().getYear() &&
                                tempDate.getMonth()== roomRate.getStartDate().getMonth() &&
                                tempDate.getDate()== roomRate.getStartDate().getDate()) 
                                || tempDate.after(roomRate.getStartDate())
                            )){
//                            System.out.println("Current looking at " + roomRate);
                                switch (roomRate.getRoomRateType()){
                                    case NORMAL:
                                        if (!selectedRoomRate.getRoomRateType().equals(RoomRateTypeEnum.PROMOTION)) {
                                            if (!selectedRoomRate.getRoomRateType().equals(RoomRateTypeEnum.PEAK)) {
                                                selectedRoomRate = roomRate;
                                            }
                                        }
                                        break;
                                    case PEAK: 
                                        if (!selectedRoomRate.getRoomRateType().equals(RoomRateTypeEnum.PROMOTION)) {
                                            selectedRoomRate = roomRate;
                                        }
                                        break;
                                    case PROMOTION: 
                                        selectedRoomRate = roomRate;
                                        break;
                                } 
                        }
                    }
                    System.out.println("SearchRoomSessionBean Selected Room Rate: " + selectedRoomRate.getName());
                    totalAmount = totalAmount.add(selectedRoomRate.getRate());
                }
                tempDate = addDays(tempDate,1);
            } while (tempDate.before(checkOutDate));
            current.setAmountPerRoom(totalAmount);
            reservations.add(current);
        }
        
        return reservations;
    }


}
