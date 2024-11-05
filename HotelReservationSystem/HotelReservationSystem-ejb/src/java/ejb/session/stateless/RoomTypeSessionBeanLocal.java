/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomType;
import exception.RoomNotFoundException;
import exception.RoomRateNotFoundException;
import exception.RoomTypeNameAlreadyExistException;
import exception.RoomTypeNotFoundException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Tan Jian Feng
 */
@Local
public interface RoomTypeSessionBeanLocal {

    public RoomType createNewRoomType(RoomType roomType) throws RoomTypeNameAlreadyExistException;

    public List<RoomType> getRoomTypes() throws RoomTypeNotFoundException;

    public RoomType getRoomTypeById(Long roomTypeID) throws RoomTypeNotFoundException;

    public RoomType getRoomTypeByName(String roomTypeName) throws RoomTypeNotFoundException;

    public RoomType updateName(RoomType roomType) throws RoomTypeNotFoundException;

    public RoomType updateDescription(RoomType roomType) throws RoomTypeNotFoundException;

    public RoomType updateSize(RoomType roomType) throws RoomTypeNotFoundException;

    public RoomType updateBed(RoomType roomType) throws RoomTypeNotFoundException;

    public RoomType updateCapacity(RoomType roomType) throws RoomTypeNotFoundException;

    public RoomType updateAmenities(RoomType roomType) throws RoomTypeNotFoundException;

    public RoomType updateStatusType(RoomType roomType) throws RoomTypeNotFoundException;

    public RoomType updateRooms(RoomType roomType) throws RoomTypeNotFoundException;

    public RoomType updateRoomRates(RoomType roomType) throws RoomTypeNotFoundException;

    public RoomType updateRoomType(RoomType roomType) throws RoomTypeNotFoundException, RoomNotFoundException, RoomTypeNameAlreadyExistException, RoomRateNotFoundException;

    boolean deleteRoomType(RoomType roomType) throws RoomTypeNotFoundException;

    boolean deleteRoomTypeById(Long roomTypeId) throws RoomTypeNotFoundException;

}
