/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import enumerations.RoomRateTypeEnum;
import enumerations.RoomStatusEnum;
import exception.RoomNotFoundException;
import exception.RoomRateNotFoundException;
import exception.RoomTypeNameAlreadyExistException;
import exception.RoomTypeNotFoundException;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
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
        try{
            RoomType temp = (RoomType) query.getSingleResult();
            throw new RoomTypeNameAlreadyExistException(
                    "RoomType with roomType number of " + roomType.getName() + " already exist!");
        } catch (NoResultException ex){
            em.persist(roomType);
            em.flush();
            return roomType;
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
    public List<RoomType> getAvailableRoomTypes() throws RoomTypeNotFoundException {
        try {
            List<RoomType> list = em.createQuery("SELECT r FROM RoomType r WHERE r.statusType = :statusType").setParameter("statusType",RoomStatusEnum.AVAILABLE).getResultList();
            return list;
        } catch (NoResultException ex) {
            throw new RoomTypeNotFoundException("There is no roomType in the system");
        }
    }


    @Override
    public RoomType getRoomTypeById(Long roomTypeId) throws RoomTypeNotFoundException {
        RoomType roomType = em.find(RoomType.class, roomTypeId);
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
        return deleteRoomTypeById(roomType.getRoomTypeId());
    }

    @Override
    public boolean deleteRoomTypeById(Long roomTypeId) throws RoomTypeNotFoundException {
        RoomType emRoomType = getRoomTypeById(roomTypeId);
        boolean toRemove = true;
        if(emRoomType.getRooms().stream().anyMatch(x -> x.isIsCheckedIn())){
            System.out.println("HAVE CHECKIN ROOMS");
            emRoomType.setStatusType(RoomStatusEnum.UNAVAILABLE);
            emRoomType.getRooms().forEach(x -> {
                if(!x.isIsCheckedIn()) {
                    x.setRoomStatus(RoomStatusEnum.UNAVAILABLE);
                }
            });
            toRemove = false;
        }
        if(toRemove) {
            emRoomType.getRooms().forEach(x -> {
                x.setRoomRmType(null);
                x.setRoomStatus(RoomStatusEnum.UNAVAILABLE);
            });
            emRoomType.setRooms(null);
            emRoomType.setParentRoomType(null);
            List<RoomType> childrenRoomType = getRoomTypes();
            childrenRoomType.removeIf(x -> x.getParentRoomType()== null || !x.getParentRoomType().equals(emRoomType));
//                try {
//                    RoomType y = this.getLoadedRoomType(x);
//                    return !y.getParentRoomType().equals(emRoomType);
//                } catch (RoomTypeNotFoundException | RoomTypeNameAlreadyExistException | RoomNotFoundException | RoomRateNotFoundException ex) {
//                    System.out.println(ex.getMessage());
//                    return true;
//                }
//            });
            childrenRoomType.forEach(x -> x.setParentRoomType(null));
            emRoomType.setRoomRates(null);
            em.remove(emRoomType);
            return true;
        }
        return false;
    }

    @Override
    public RoomType updateName(RoomType roomType) throws RoomTypeNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
                                                                       // Tools | Templates.
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
    public RoomType updateRoomType(RoomType roomType) throws RoomTypeNotFoundException, RoomNotFoundException,
            RoomRateNotFoundException, RoomTypeNameAlreadyExistException {
        RoomType managedRoomType = getRoomTypeById(roomType.getRoomTypeId());

        try {
            
            RoomType ifExist = getRoomTypeByName(roomType.getName());
            if(managedRoomType.getRoomTypeId() == ifExist.getRoomTypeId()) {
                managedRoomType.setDescription(roomType.getDescription());
                managedRoomType.setSize(roomType.getSize());
                managedRoomType.setBed(roomType.getBed());
                managedRoomType.setCapacity(roomType.getCapacity());
                managedRoomType.setAmenities(roomType.getAmenities());
                managedRoomType.setStatusType(roomType.getStatusType());

                managedRoomType.setRoomRates(roomType.getRoomRates());
                List<Room> oldRooms = managedRoomType.getRooms();

                oldRooms.forEach(x -> x.setRoomRmType(null));
                ArrayList<Room> rooms = new ArrayList<>();
                roomType.getRooms().forEach(x -> rooms.add(x));
                for (Room room : roomType.getRooms()) {
                    Room managedRoom = roomSessionBeanLocal.getRoomByNumber(room.getRoomNumber());
                    managedRoom.setRoomRmType(managedRoomType);
                    rooms.add(managedRoom);
                }
                managedRoomType.setRooms(roomType.getRooms());

                return managedRoomType;
            }
            throw new RoomTypeNameAlreadyExistException(
                    "Room type with name of " + roomType.getName() + " already exist!");
        } catch (RoomTypeNotFoundException ex) {
            managedRoomType.setName(roomType.getName());
            managedRoomType.setDescription(roomType.getDescription());
            managedRoomType.setSize(roomType.getSize());
            managedRoomType.setBed(roomType.getBed());
            managedRoomType.setCapacity(roomType.getCapacity());
            managedRoomType.setAmenities(roomType.getAmenities());
            managedRoomType.setStatusType(roomType.getStatusType());

            managedRoomType.setRoomRates(roomType.getRoomRates());
            List<Room> oldRooms = managedRoomType.getRooms();

            oldRooms.forEach(x -> x.setRoomRmType(null));
            ArrayList<Room> rooms = new ArrayList<>();
            roomType.getRooms().forEach(x -> rooms.add(x));
            for (Room room : roomType.getRooms()) {
                Room managedRoom = roomSessionBeanLocal.getRoomByNumber(room.getRoomNumber());
                managedRoom.setRoomRmType(managedRoomType);
                rooms.add(managedRoom);
            }
            managedRoomType.setRooms(roomType.getRooms());

            return managedRoomType;
        }

        // List<RoomRate> roomRates;

        // List<RoomRate> roomRates;
    }

    @Override
    public BigDecimal calculatePrice(Long id, Date checkInDate, Date checkOutDate, Boolean isWalkIn) {
        RoomType roomType = null;
        try {
            roomType = getRoomTypeById(id);
        } catch (RoomTypeNotFoundException ex) {
            Logger.getLogger(RoomTypeSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal roomRate = BigDecimal.ZERO;

        Calendar checkIn = Calendar.getInstance();
        checkIn.setTime(checkInDate);
        Calendar checkOut = Calendar.getInstance();
        checkOut.setTime(checkInDate);

        while (checkIn.before(checkOut)) {
            Date date = checkIn.getTime();

            if (isWalkIn) {
                roomRate = getRoomRate(roomType, RoomRateTypeEnum.PUBLISHED);
                totalPrice = totalPrice.add(roomRate);
            } else {

            }

            checkIn.add(Calendar.DATE, 1);
        }

        return totalPrice;
    }

    private BigDecimal getRoomRate(RoomType roomType, RoomRateTypeEnum roomRateType) {
        for (RoomRate roomRate : roomType.getRoomRates()) {
            if (roomRate.getRateStatus().equals(roomRateType)) {
                return roomRate.getRate();
            }
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal calculateRoomsAvail(Long id, Date checkInDate, Date checkOutDate) {
        return BigDecimal.ZERO;
    }

    @Override
    public RoomType getLoadedRoomType(RoomType roomType) throws RoomTypeNotFoundException,
            RoomTypeNameAlreadyExistException, RoomNotFoundException, RoomRateNotFoundException {
        RoomType returnRoomType = getRoomTypeByName(roomType.getName());
        returnRoomType.getRoomRates().size();
        returnRoomType.getRooms().size();
        return returnRoomType;
    }

}
