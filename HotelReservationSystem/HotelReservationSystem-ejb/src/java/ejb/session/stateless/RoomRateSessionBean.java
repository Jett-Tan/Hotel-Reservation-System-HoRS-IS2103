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
            throw new RoomRateNotFoundException("Room rate with id of " + roomRate.getName());
        }
    }

    @Override
    public RoomRate getRoomRateById(Long roomRateId) throws RoomRateNotFoundException {
        try{
            RoomRate roomRate = em.find(RoomRate.class,roomRateId);
            return roomRate;
        } catch (NoResultException ex){
            throw new RoomRateNotFoundException("Room rate with id of " + roomRateId);
        }
    }

    @Override
    public List<RoomRate> getRoomRates() throws RoomRateNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public RoomRate updateRoomRate() throws RoomRateNotFoundException, RoomRateNameAlreadyExistException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deleteRoomRate() throws RoomRateNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public void persist(Object object) {
        em.persist(object);
    }
}
