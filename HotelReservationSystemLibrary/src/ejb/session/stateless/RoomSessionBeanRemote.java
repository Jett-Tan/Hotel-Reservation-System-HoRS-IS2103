/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Room;
import entity.RoomType;
import exception.RoomNotFoundException;
import exception.RoomNumberAlreadyExistException;
import exception.RoomRateNotFoundException;
import exception.RoomTypeNameAlreadyExistException;
import exception.RoomTypeNotFoundException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Tan Jian Feng
 */
@Remote
public interface RoomSessionBeanRemote {
    Room createNewRoom(Room room) throws RoomNumberAlreadyExistException, RoomTypeNotFoundException;
        
    List<Room> getRooms() throws RoomNotFoundException;

    Room getRoomById(Long roomID) throws RoomNotFoundException;

    Room getRoomByNumber(String roomNumber) throws RoomNotFoundException;

//    Room updateNumber(Room room) throws RoomNotFoundException, RoomNumberAlreadyExistException;
//
//    Room updateBookedDates(Room room) throws RoomNotFoundException;
//    
//    Room updateRoomStatus(Room room) throws RoomNotFoundException;
//    
//    Room updateIsCheckIn(Room room) throws RoomNotFoundException;
//    
//    Room updateRoomType(Room room) throws RoomNotFoundException;
    
    public Room getLoadedRoom(Room room) throws RoomTypeNotFoundException, RoomNumberAlreadyExistException,RoomNotFoundException;
    
    public Room updateRoom(Room room) throws  RoomTypeNotFoundException, RoomNumberAlreadyExistException, RoomNotFoundException;
    
    boolean deleteRoom(Room room) throws RoomNotFoundException,RoomTypeNotFoundException;

    boolean deleteRoomById(Long roomId) throws RoomNotFoundException,RoomTypeNotFoundException;
}
