/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomRate;
import exception.RoomRateNameAlreadyExistException;
import exception.RoomRateNotFoundException;
import java.util.List;
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
public class RoomRateSessionBean implements RoomRateSessionBeanRemote, RoomRateSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public RoomRate getRoomRateByName(RoomRate roomRate) throws RoomRateNotFoundException {
        try{
            Query query = em.createQuery("SELECT rr FROM RoomRate rr WHERE rr.name = :roomRateName")
                    .setParameter("roomRateName",roomRate.getName());
            RoomRate roomrate = (RoomRate) query.getSingleResult();
            return roomrate;
        } catch (NoResultException ex){
            throw new RoomRateNotFoundException("Room rate with id of " + roomRate.getName() + " do not exist!");
        }
    }

    @Override
    public RoomRate getRoomRateById(Long roomRateId) throws RoomRateNotFoundException {
        try{
            RoomRate roomRate = em.find(RoomRate.class,roomRateId);
            if(roomRate != null){
                return roomRate;
            }
            throw new RoomRateNotFoundException("Room rate with id of " + roomRateId + " do not exist!");
        } catch (NoResultException ex){
            throw new RoomRateNotFoundException("Room rate with id of " + roomRateId + " do not exist!");
        }
    }

    @Override
    public List<RoomRate> getRoomRates() throws RoomRateNotFoundException {
        try{
            Query query = em.createQuery("SELECT rr FROM RoomRate rr");
            List<RoomRate> roomrates = query.getResultList();
            return roomrates;
        } catch (NoResultException ex){
            throw new RoomRateNotFoundException("There is no room rates in the system" );
        }
    }

    @Override
    public RoomRate updateRoomRate(RoomRate roomRate) throws RoomRateNotFoundException, RoomRateNameAlreadyExistException {
        RoomRate managedRoomRate = getRoomRateById(roomRate.getRoomRateId());
        try {
            RoomRate ifExist = getRoomRateByName(roomRate);
            if(managedRoomRate.getRoomRateId() == ifExist.getRoomRateId()) {
                managedRoomRate.setRate(roomRate.getRate());
                managedRoomRate.setRateStatus(roomRate.getRateStatus());
                managedRoomRate.setStartDate(roomRate.getStartDate());
                managedRoomRate.setEndDate(roomRate.getEndDate());
                return managedRoomRate;
            }
            throw new RoomRateNameAlreadyExistException("Room rate with name of " + roomRate.getName() + " already exists!");
        } catch (RoomRateNotFoundException ex) {
            managedRoomRate.setName(roomRate.getName());
            managedRoomRate.setRate(roomRate.getRate());
            managedRoomRate.setRateStatus(roomRate.getRateStatus());
            managedRoomRate.setStartDate(roomRate.getStartDate());
            managedRoomRate.setEndDate(roomRate.getEndDate());
            return managedRoomRate;
        }
        
    }


    @Override
    public boolean deleteRoomRate(RoomRate roomRate) throws RoomRateNotFoundException {
        RoomRate oldRoomRate = getRoomRateByName(roomRate);
        em.remove(oldRoomRate);
        em.flush();
        return true;
    }  

    @Override
    public boolean deleteRoomRateById(Long roomRateId) throws RoomRateNotFoundException {
        try {
            RoomRate roomRate = em.find(RoomRate.class,roomRateId);
            em.remove(roomRate);
            em.flush();
            return true;
        }catch(NoResultException ex) {
            throw new RoomRateNotFoundException("Room rate with id of " + roomRateId + " do not exist!");
        }
    }

    @Override
    public RoomRate createNewRoomRate(RoomRate roomRate) throws RoomRateNameAlreadyExistException {
        try {
            getRoomRateByName(roomRate);
            throw new RoomRateNameAlreadyExistException("Room rate with name of " + roomRate.getName() + " already exist!");
        } catch (RoomRateNotFoundException ex) {
            em.persist(roomRate);
            em.flush();
            return roomRate;
        }
    }


}
