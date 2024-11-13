/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Reservation;
import entity.Room;
import entity.RoomType;
import enumerations.RoomStatusEnum;
import exception.RoomNotFoundException;
import exception.RoomNumberAlreadyExistException;
import exception.RoomTypeNotFoundException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Tan Jian Feng
 */
@Stateless
public class RoomSessionBean implements RoomSessionBeanRemote, RoomSessionBeanLocal {

    @EJB(name = "ReservationSessionBeanLocal")
    private ReservationSessionBeanLocal reservationSessionBeanLocal;

    @EJB(name = "RoomTypeSessionBeanLocal")
    private RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Room createNewRoom(Room room) throws RoomNumberAlreadyExistException, RoomTypeNotFoundException {
        Query query = em.createQuery("SELECT r FROM Room r WHERE r.roomNumber = :roomNumber")
                .setParameter("roomNumber", room.getRoomNumber());
        if (query.getFirstResult() == 0) {
            em.persist(room);
            RoomType roomType = room.getRoomRmType();
            roomType = roomTypeSessionBeanLocal.getRoomTypeByName(roomType.getName());
            roomType.addRoom(room);
            em.flush();
            return room;
        }else {
            throw new RoomNumberAlreadyExistException("Room with room number of " + room.getRoomNumber() + " already exist!");
        }
    }

    @Override
    public List<Room> getRooms() throws RoomNotFoundException {
        try {
            List<Room> list = em.createQuery("SELECT r FROM Room r").getResultList();
            return list;
        } catch (NoResultException ex) {
            throw new RoomNotFoundException("There is no room in the system");
        }
    }

    @Override
    public Room getRoomById(Long roomId) throws RoomNotFoundException {
        Room room = em.find(Room.class,roomId);
        if (room == null) {
            throw new RoomNotFoundException("Room with Id " + roomId + " not found!");
        }
        return room;
    }

    @Override
    public Room getRoomByNumber(String roomNumber) throws RoomNotFoundException {
        Query query = em.createQuery("SELECT r FROM Room r WHERE r.roomNumber = :roomNumber")
                .setParameter("roomNumber", roomNumber);
        try {
            Room room = (Room) query.getSingleResult();
            return room;
        } catch (NoResultException ex) {
            throw new RoomNotFoundException("Room with room number " + roomNumber + " not found!");
        }
    }

    @Override
    public Room updateNumber(Room room) throws RoomNotFoundException, RoomNumberAlreadyExistException {
        Room emRoom = getRoomById(room.getRoomId());
        try{
            getRoomByNumber(room.getRoomNumber());
        } catch ( RoomNotFoundException ex) {
            emRoom.setRoomNumber(room.getRoomNumber());
            return emRoom;
        }
        throw new RoomNumberAlreadyExistException("Room with room number " + room.getRoomNumber() + " already exist!");
    }
    
    @Override
    public Room updateBookedDates(Room room) throws RoomNotFoundException {
        Room emRoom = getRoomById(room.getRoomId());
        emRoom.setBookedDates(room.getBookedDates());
        return emRoom;
    }

    @Override
    public Room updateRoomStatus(Room room) throws RoomNotFoundException {
        Room emRoom = getRoomById(room.getRoomId());
        emRoom.setBookedDates(room.getBookedDates());
        return emRoom;
    }

    @Override
    public Room updateIsCheckIn(Room room) throws RoomNotFoundException {
        Room emRoom = getRoomById(room.getRoomId());
        emRoom.setIsCheckedIn(room.isIsCheckedIn());
        return emRoom;
    }

    @Override
    public Room updateRoomType(Room room) throws RoomNotFoundException {
        Room emRoom = getRoomById(room.getRoomId());
        emRoom.setRoomRmType(room.getRoomRmType());
        return emRoom;
    }
 

    @Override
    public boolean deleteRoom(Room room) throws RoomNotFoundException,RoomTypeNotFoundException {
        return deleteRoomById(room.getRoomId());
    }

    @Override
    public boolean deleteRoomById(Long roomId) throws RoomNotFoundException, RoomTypeNotFoundException {
        Room emRoom = getRoomById(roomId);
        em.refresh(emRoom);
        List<Reservation> reservations = reservationSessionBeanLocal.retrieveAllReseravtions();
        reservations.removeIf(x -> !x.getAllocated());
        reservations.removeIf(x -> !x.getRoomList().contains(emRoom));
        if(!emRoom.isIsCheckedIn() && !(reservations.size() > 0)){
            RoomType emRoomType = roomTypeSessionBeanLocal.getRoomTypeById(emRoom.getRoomRmType().getRoomTypeId());
            emRoomType.getRooms().remove(emRoom);
            emRoom.setRoomRmType(null);
            em.remove(emRoom);
            return true;
        }
        emRoom.setRoomStatus(RoomStatusEnum.UNAVAILABLE);
        return false;
    }

    @Override
    public Room updateRoom(Room room) throws RoomNotFoundException, RoomNumberAlreadyExistException, RoomTypeNotFoundException {
        Room managedRoom = getRoomById(room.getRoomId());
        try {
            Room ifExist = getRoomByNumber(room.getRoomNumber());
            if(managedRoom.getRoomId() == ifExist.getRoomId()) {
                managedRoom.setBookedDates(room.getBookedDates());
                managedRoom.setRoomStatus(room.getRoomStatus());
                managedRoom.setRoomRmType(room.getRoomRmType());
                managedRoom.setIsCheckedIn(room.isIsCheckedIn());
                RoomType roomtype = roomTypeSessionBeanLocal.getRoomTypeByName(room.getRoomRmType().getName());
                for (Room room1 : roomtype.getRooms()) {
                    if (room1.getRoomId().equals(managedRoom.getRoomId())) {
                        return managedRoom;
                    }
                }
                roomtype.addRoom(managedRoom);
                return managedRoom;
            }
            throw new RoomNumberAlreadyExistException("Room with room number " + room.getRoomNumber() + " already exist!");
        } catch (RoomNotFoundException ex){
            managedRoom.setRoomNumber(room.getRoomNumber());
            managedRoom.setBookedDates(room.getBookedDates());
            managedRoom.setRoomStatus(room.getRoomStatus());
            managedRoom.setRoomRmType(room.getRoomRmType());
                managedRoom.setIsCheckedIn(room.isIsCheckedIn());
            RoomType roomtype = roomTypeSessionBeanLocal.getRoomTypeByName(room.getRoomRmType().getName());
            for (Room room1 : roomtype.getRooms()) {
                if (room1.getRoomId().equals(managedRoom.getRoomId())) {
                    return managedRoom;
                }
            }
            roomtype.addRoom(managedRoom);
            return managedRoom;
        }
        
    }

    @Override
    public Room getLoadedRoom(Room room) throws RoomTypeNotFoundException, RoomNumberAlreadyExistException, RoomNotFoundException {
        Room oldRoom = getRoomByNumber(room.getRoomNumber());
        oldRoom.getRoomRmType().getRoomRates();
        return oldRoom;
    }
    
    
    

}
