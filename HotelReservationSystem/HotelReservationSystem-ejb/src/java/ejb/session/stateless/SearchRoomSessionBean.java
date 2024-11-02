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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
    public List<Room> searchRoomsByDate(Date checkIndate, Date checkOutDate) throws RoomNotFoundException {
        List<Room> rooms = roomSessionBeanLocal.getRooms();
//        rooms.removeIf(x -> x.getRoomStatus().equals(RoomStatusEnum.UNAVAILABLE));
//        rooms.removeIf(x -> {
//            boolean free = false;
//            List<Date> bookedDates = x.getBookedDates();
//            Date checkin = checkIndate;
//            Date checkout = checkOutDate;
//            do {
//                for(Date bookedDate : bookedDates) {
//                    if(bookedDate.getYear() == (checkin.getYear()) &&
//                       bookedDate.getMonth()== (checkin.getMonth()) &&
//                       bookedDate.getDate()== (checkin.getDate()) ) {
//                        return true;
//                    }
//                }
//                checkin = addDays(checkin,1);
//            } while (checkin.before(checkout) || checkin.equals(checkout));
//            return free;
//        });
        return rooms;
    }
    
    @Override
    public List<Room> searchRoomsByType(RoomType roomType) throws RoomNotFoundException, RoomTypeNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
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
    
    public Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }
    
    public int getNumberOfAvailableRoom(Date checkInDate, Date checkOutDate,RoomType roomType) throws RoomNotFoundException {
        List<Room> rooms = roomSessionBeanLocal.getRooms();
        rooms.removeIf(x -> x.getRoomStatus().equals(RoomStatusEnum.UNAVAILABLE));
        rooms.removeIf(x -> x.getRoomRmType().getName().equals(roomType.getName()));
        rooms.removeIf(x -> {
            boolean free = false;
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
            } while (checkin.before(checkout) || checkin.equals(checkout));
            return free;
        });
        
        List<Reservation> reservations = reservationSessionBeanLocal.retrieveAllReservationWithinDates(checkInDate, checkOutDate,roomType);
        int numOfRooms = reservations.stream().map(x -> x.getNumOfRooms()).reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO).intValue();
        return rooms.size() - numOfRooms;
    }

    @Override
    public List<Reservation> convertSearchToReservation(List<Room> rooms, Date checkInDate, Date checkOutDate) throws RoomNotFoundException, RoomTypeNotFoundException {
        List<Reservation> reservations = new ArrayList<>();
//        List<RoomType> roomTypesAvailable = rooms.stream().map(x -> x.getRoomRmType()).distinct().collect(Collectors.toList());
//        rooms.replaceAll(x -> {
//            try {
//                return roomSessionBeanLocal.getRoomByNumber(x.getRoomNumber());
//            } catch (RoomNotFoundException ex) {
//                Logger.getLogger(SearchRoomSessionBean.class.getName()).log(Level.SEVERE, null, ex);
//                return null;
//            }
//        });
//        for(Room room : rooms) {
//            Reservation current = new Reservation();
//            current.setStartDate(checkInDate);
//            current.setEndDate(checkOutDate);
//            current.setNumOfRooms(BigDecimal.ONE);
//            current.setAllocated(Boolean.FALSE);
//            current.setReservationType(ReservationTypeEnum.WALK_IN);
//            List<RoomRate> roomRates = room.getRoomRmType().getRoomRates();
//            roomRates.removeIf(x -> !x.getRoomRateType().equals(RoomRateTypeEnum.PUBLISHED));
//            roomRates.removeIf(x -> !x.getStartDate().before(checkInDate));
//            roomRates.removeIf(x -> !x.getEndDate().after(checkOutDate));   
//            roomRates.forEach(x -> System.out.println(x.getName()));
//            BigDecimal totalAmount = BigDecimal.ZERO;
//            roomRates.forEach(x -> {
//                 totalAmount.add(x.getRate());
//            });
//            current.setTotalAmount(totalAmount);
//            reservations.add(current);
//        }
        return reservations;
    }
    
    @Override
    public List<Reservation> generateReservation(Date checkInDate,Date checkOutDate) throws RoomNotFoundException {
        List<Reservation> reservations = new ArrayList<>();
        List<Room> allRooms = roomSessionBeanLocal.getRooms();
        
        //maybe need add in a way to add reservations and check if got enuf rooms
        allRooms.removeIf(x -> x.getRoomStatus().equals(RoomStatusEnum.UNAVAILABLE));
        allRooms.removeIf(x -> {
            boolean free = false;
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
            } while (checkin.before(checkout) || checkin.equals(checkout));
            return free;
        });
        
        List<RoomType> allRoomTypes = allRooms.stream().map(x -> x.getRoomRmType()).distinct()
                .filter(x -> {
                    try {
                        return getNumberOfAvailableRoom(checkInDate,checkOutDate,x) > 0;
                    } catch (RoomNotFoundException ex) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
        List<Room> distinctRooms = new ArrayList<Room>();
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
        distinctRooms.removeIf(x -> {
            try {
                return !(getNumberOfAvailableRoom(checkInDate,checkOutDate,x.getRoomRmType()) > 0);
            } catch (RoomNotFoundException ex) {
                return true;
            }
        });
        for(Room room : distinctRooms) {
            Reservation current = new Reservation();
            current.setStartDate(checkInDate);
            current.setEndDate(checkOutDate);
            current.setNumOfRooms(new BigDecimal(getNumberOfAvailableRoom(checkInDate,checkOutDate,room.getRoomRmType())));
            current.setAllocated(Boolean.FALSE);
            current.setReservationType(ReservationTypeEnum.WALK_IN);
            current.setRoomType(room.getRoomRmType());
            List<RoomRate> roomRates = room.getRoomRmType().getRoomRates();
            roomRates.removeIf(x -> !x.getRoomRateType().equals(RoomRateTypeEnum.PUBLISHED));
//            roomRates.forEach(x -> System.out.println(x.getName()));
            BigDecimal totalAmount = BigDecimal.ZERO;
            long diffInMillies = Math.abs(checkInDate.getTime() - checkOutDate.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            BigDecimal numOfDays = new BigDecimal(diff+1);
            for(RoomRate x : roomRates) {
                totalAmount = totalAmount.add(x.getRate().multiply(numOfDays));
            }
            
            current.setTotalAmount(totalAmount);
            reservations.add(current);
        }
        
        return reservations;
    }
}
