/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Reservation;
import entity.Room;
import entity.RoomType;
import enumerations.ReservationTypeEnum;
import enumerations.RoomRateTypeEnum;
import exception.RoomNotFoundException;
import exception.RoomTypeNotFoundException;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Tan Jian Feng
 */
@Remote
public interface SearchRoomSessionBeanRemote {
    
    public List<Room> searchRooms(Date checkIndate,Date checkOutDate, RoomType roomType) throws RoomNotFoundException, RoomTypeNotFoundException;
    
    public List<Reservation> generateReservationWalkIn(Date checkInDate,Date checkOutDate) throws RoomNotFoundException;
    
    public List<Reservation> generateReservationOnline(Date checkInDate,Date checkOutDate) throws RoomNotFoundException;
  
}
