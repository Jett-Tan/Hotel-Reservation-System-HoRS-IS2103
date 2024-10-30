/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import exception.RoomNotFoundException;
import exception.RoomRateNotFoundException;
import exception.RoomTypeNameAlreadyExistException;
import exception.RoomTypeNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class RoomTypeSessionBean implements RoomTypeSessionBeanRemote, RoomTypeSessionBeanLocal {

    @EJB(name = "RoomSessionBeanLocal")
    private RoomSessionBeanLocal roomSessionBeanLocal;

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public RoomType createNewRoomType(RoomType roomType) throws RoomTypeNameAlreadyExistException {
        Query query = em.createQuery("SELECT r FROM RoomType r WHERE r.name = :name")
                .setParameter("name", roomType.getName());
        if (query.getFirstResult() == 0) {
            em.persist(roomType);
            em.flush();
            return roomType;
        }else {
            throw new RoomTypeNameAlreadyExistException("RoomType with roomType number of " + roomType.getName() + " already exist!");
        }
    }

    @Override
    public List<RoomType> getRoomTypes() throws RoomTypeNotFoundException {
        try {
            List<RoomType> list = em.createQuery("SELECT r FROM RoomType r").getResultList();
            return list;
        } catch (NoResultException ex) {
            throw new RoomTypeNotFoundException("There is no roomType in the system");
        }
    }

    @Override
    public RoomType getRoomTypeById(Long roomTypeId) throws RoomTypeNotFoundException {
        RoomType roomType = em.find(RoomType.class,roomTypeId);
        if (roomType == null) {
            throw new RoomTypeNotFoundException("RoomType with Id " + roomTypeId + " not found!");
        }
        return roomType;
    }

    @Override
    public RoomType getRoomTypeByName(String roomTypeName) throws RoomTypeNotFoundException {
        Query query = em.createQuery("SELECT r FROM RoomType r WHERE r.name = :roomTypeName")
                .setParameter("roomTypeName", roomTypeName);
        try {
            RoomType roomType = (RoomType) query.getSingleResult();
            return roomType;
        } catch (NoResultException ex) {
            throw new RoomTypeNotFoundException("RoomType with roomType number " + roomTypeName + " not found!");
        }
    }

    @Override
    public RoomType updateStatusType(RoomType roomType) throws RoomTypeNotFoundException {
        RoomType emRoomType = getRoomTypeById(roomType.getRoomTypeId());
        emRoomType.setStatusType(roomType.getStatusType());
        return emRoomType;
    }

    @Override
    public boolean deleteRoomType(RoomType roomType) throws RoomTypeNotFoundException {
        RoomType emRoomType = getRoomTypeById(roomType.getRoomTypeId());
        em.remove(emRoomType);
        return true;
    }

    @Override
    public boolean deleteRoomTypeById(Long roomTypeId) throws RoomTypeNotFoundException {
        RoomType emRoomType = getRoomTypeById(roomTypeId);
        em.remove(emRoomType);
        return true;
    }

    @Override
    public RoomType updateName(RoomType roomType) throws RoomTypeNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public RoomType updateDescription(RoomType roomType) throws RoomTypeNotFoundException {
        RoomType emRoomType = getRoomTypeById(roomType.getRoomTypeId());
        emRoomType.setDescription(roomType.getDescription());
        return emRoomType;
    }

    @Override
    public RoomType updateSize(RoomType roomType) throws RoomTypeNotFoundException {
        RoomType emRoomType = getRoomTypeById(roomType.getRoomTypeId());
        emRoomType.setSize(roomType.getSize());
        return emRoomType;
    }

    @Override
    public RoomType updateBed(RoomType roomType) throws RoomTypeNotFoundException {
        RoomType emRoomType = getRoomTypeById(roomType.getRoomTypeId());
        emRoomType.setBed(roomType.getBed());
        return emRoomType;
    }

    @Override
    public RoomType updateCapacity(RoomType roomType) throws RoomTypeNotFoundException {
        RoomType emRoomType = getRoomTypeById(roomType.getRoomTypeId());
        emRoomType.setCapacity(roomType.getCapacity());
        return emRoomType;
    }

    @Override
    public RoomType updateAmenities(RoomType roomType) throws RoomTypeNotFoundException {
        RoomType emRoomType = getRoomTypeById(roomType.getRoomTypeId());
        emRoomType.setAmenities(roomType.getAmenities());
        return emRoomType;
    }

    @Override
    public RoomType updateRooms(RoomType roomType) throws RoomTypeNotFoundException {
        RoomType emRoomType = getRoomTypeById(roomType.getRoomTypeId());
        emRoomType.setRooms(roomType.getRooms());
        return emRoomType;
    }

    @Override
    public RoomType updateRoomRates(RoomType roomType) throws RoomTypeNotFoundException {
        RoomType emRoomType = getRoomTypeById(roomType.getRoomTypeId());
        emRoomType.setRoomRates(roomType.getRoomRates());
        return emRoomType;
    }

    @Override
    public RoomType updateRoomType(RoomType roomType) throws RoomTypeNotFoundException, RoomNotFoundException,RoomRateNotFoundException, RoomTypeNameAlreadyExistException {
        RoomType oldRoomType = getRoomTypeById(roomType.getRoomTypeId());
        
        try {
            getRoomTypeByName(roomType.getName());
            throw new RoomTypeNameAlreadyExistException("Room type with name of " + roomType.getName() + " already exist!");
        } catch (RoomTypeNotFoundException ex){
            oldRoomType.setName(roomType.getName());
        }
        oldRoomType.setDescription(roomType.getDescription());
        oldRoomType.setSize(roomType.getSize());    
        oldRoomType.setBed(roomType.getBed());
        oldRoomType.setCapacity(roomType.getCapacity());
        oldRoomType.setAmenities(roomType.getAmenities());
        oldRoomType.setStatusType(roomType.getStatusType());
        
        oldRoomType.setRoomRates(roomType.getRoomRates());
        List<Room> oldRooms = oldRoomType.getRooms();
        
        oldRooms.forEach(x -> x.setRoomRmType(null));
        ArrayList<Room> rooms = new ArrayList<>();
        roomType.getRooms().forEach(x -> rooms.add(x));
        for(Room room : roomType.getRooms()) {
            Room managedRoom = roomSessionBeanLocal.getRoomByNumber(room.getRoomNumber());
            managedRoom.setRoomRmType(oldRoomType);
            rooms.add(managedRoom);
        }
        oldRoomType.setRooms(roomType.getRooms());
        
        return oldRoomType;
    
    //List<RoomRate> roomRates;
    }

    @Override
    public RoomType getLoadedRoomType(RoomType roomType) throws RoomTypeNotFoundException, RoomTypeNameAlreadyExistException, RoomNotFoundException, RoomRateNotFoundException {
        RoomType returnRoomType = getRoomTypeByName(roomType.getName());
        returnRoomType.getRoomRates().size();
        returnRoomType.getRooms().size();
        return returnRoomType;
    }

}
