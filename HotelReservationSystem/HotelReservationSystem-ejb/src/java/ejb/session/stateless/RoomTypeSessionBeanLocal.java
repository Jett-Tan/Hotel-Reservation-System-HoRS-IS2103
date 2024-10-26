/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomType;
import exception.RoomTypeNotFoundException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Tan Jian Feng
 */
@Local
public interface RoomTypeSessionBeanLocal {
    RoomType createNewRoomType(RoomType roomType);
        
    List<RoomType> getRoomTypes() throws RoomTypeNotFoundException;

    RoomType getRoomTypeById(Long roomTypeID) throws RoomTypeNotFoundException;

    RoomType getRoomTypeByNumber(String roomTypeNumber) throws RoomTypeNotFoundException;

    RoomType updateNumber(RoomType roomType) throws RoomTypeNotFoundException;

    RoomType updateBookedDates(RoomType roomType) throws RoomTypeNotFoundException;
    
    RoomType updateRoomTypeStatus(RoomType roomType) throws RoomTypeNotFoundException;
    
    RoomType updateIsCheckIn(RoomType roomType) throws RoomTypeNotFoundException;
    
    RoomType updateRoomTypeType(RoomType roomType) throws RoomTypeNotFoundException;
    
    boolean deleteRoomType(RoomType roomType) throws RoomTypeNotFoundException;

    boolean deleteRoomTypeById(Long roomTypeId) throws RoomTypeNotFoundException;
}
