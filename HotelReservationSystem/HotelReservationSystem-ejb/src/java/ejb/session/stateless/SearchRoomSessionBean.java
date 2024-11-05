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
            } while (temp.before(checkOutDate) || temp.equals(checkOutDate));
            return free;
        });
        rooms.removeIf(x -> x.getRoomStatus().equals(RoomStatusEnum.UNAVAILABLE));
        List<Reservation> reservations = reservationSessionBeanLocal.retrieveAllReservationWithinDates(checkInDate, checkOutDate,roomType);
        int numOfRooms = reservations.stream().filter(x -> !x.getAllocated()).map(x -> x.getNumOfRooms()).reduce(BigDecimal.ZERO,(x,y)->x.add(y)).intValue();
        System.out.println(reservations);
        System.out.println("numofRooms"+numOfRooms);
        System.out.println("roomSize" + rooms.size());
        return rooms.size() - numOfRooms;
    }
    
    @Override
    public List<Reservation> generateReservation(Date checkInDate,Date checkOutDate,RoomRateTypeEnum rateType) throws RoomNotFoundException {
        List<Reservation> reservations = new ArrayList<>();
        List<Room> allRooms = roomSessionBeanLocal.getRooms();
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
            } while (checkin.before(checkout) || checkin.equals(checkout));
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
        
        distinctRooms.removeIf(x -> {
            try {
                return !(getNumberOfAvailableRoom(checkInDate,checkOutDate,x.getRoomRmType()) > 0);
            } catch (RoomNotFoundException ex) {
                return true;
            }
        });
        System.out.println("Distinct Rooms" + distinctRooms);
        for(Room room : distinctRooms) {
            Reservation current = new Reservation();
            current.setStartDate(checkInDate);
            current.setEndDate(checkOutDate);
            current.setNumOfRooms(new BigDecimal(getNumberOfAvailableRoom(checkInDate,checkOutDate,room.getRoomRmType())));
            current.setAllocated(Boolean.FALSE);
            current.setReservationType(ReservationTypeEnum.WALK_IN);
            current.setRoomType(room.getRoomRmType());
            List<RoomRate> roomRates = room.getRoomRmType().getRoomRates();
            roomRates.removeIf(x -> !x.getRoomRateType().equals(rateType));
            BigDecimal totalAmount = BigDecimal.ZERO;
            long diffInMillies = Math.abs(checkInDate.getTime() - checkOutDate.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            BigDecimal numOfDays = new BigDecimal(diff+1);
            for(RoomRate x : roomRates) {
                totalAmount = totalAmount.add(x.getRate().multiply(numOfDays));
            }
            
            current.setAmountPerRoom(totalAmount);
            reservations.add(current);
        }
        
        return reservations;
    }

}
