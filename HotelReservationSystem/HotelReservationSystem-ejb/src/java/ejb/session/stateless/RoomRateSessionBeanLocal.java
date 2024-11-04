/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomRate;
import exception.RoomRateNameAlreadyExistException;
import exception.RoomRateNotFoundException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Tan Jian Feng
 */
@Local
public interface RoomRateSessionBeanLocal {
    public RoomRate createNewRoomRate(RoomRate roomRate) throws RoomRateNameAlreadyExistException;
    public RoomRate getRoomRateByName(RoomRate roomRate) throws RoomRateNotFoundException;
    public RoomRate getRoomRateById(Long roomRateId) throws RoomRateNotFoundException;
    public List<RoomRate> getRoomRates() throws RoomRateNotFoundException;
    public RoomRate updateRoomRate(RoomRate roomRate) throws RoomRateNotFoundException, RoomRateNameAlreadyExistException;
    public boolean deleteRoomRate(RoomRate roomRate) throws RoomRateNotFoundException;
    public boolean deleteRoomRateById(Long roomRateId) throws RoomRateNotFoundException;

}
