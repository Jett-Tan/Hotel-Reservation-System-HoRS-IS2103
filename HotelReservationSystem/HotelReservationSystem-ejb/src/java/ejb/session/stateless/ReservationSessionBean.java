/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AllocationReport;
import entity.Guest;
import entity.Partner;
import entity.Reservation;
import entity.Room;
import entity.RoomType;
import exception.ReservationNotFoundException;
import exception.RoomNotFoundException;
import exception.RoomTypeNotFoundException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author jovanfoo
 */
@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @EJB(name = "RoomSessionBeanLocal")
    private RoomSessionBeanLocal roomSessionBeanLocal;

    @EJB(name = "SearchRoomSessionBeanLocal")
    private SearchRoomSessionBeanLocal searchRoomSessionBeanLocal;

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public List<Reservation> retrieveAllMyReservations(Guest guest) {
        long guestId = guest.getGuestId();
        List<Reservation> reservationList = em.createQuery("SELECT r FROM Reservation r WHERE r.guest.guestId = :inGuestId")
                .setParameter("inGuestId", guestId).getResultList();
        reservationList.forEach(x -> x.getRoomList().size());
        return reservationList;
    }
    
    @Override
    public List<Reservation> retrieveAllPartnerReservations(Partner partner) {
        long partnerId = partner.getPartnerId();
        List<Reservation> reservationList = em.createQuery("SELECT r FROM Reservation r WHERE r.partner.partnerId = :inPartnerId")
                .setParameter("inPartnerId", partnerId).getResultList();
        reservationList.forEach(x -> x.getRoomList().size());
        return reservationList;
    }

    @Override
    public Reservation retrieveReservationByReservationId(Guest guest, Long reservationId) {
        long guestId = guest.getGuestId();
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.reservationId = :reservationId AND r.guest.guestId = :guestId")
                .setParameter("reservationId", reservationId)
                .setParameter("guestId", guestId);
        return (Reservation) query.getSingleResult();
    }
    
    @Override
    public Reservation createNewReservation(Reservation reservation) {
        em.persist(reservation);
        em.flush();
        return reservation;
    }
    @Override
    public Reservation allocateReservation(Reservation reservation) throws ReservationNotFoundException {
        try {
            AllocationReport report = new AllocationReport();
            BigDecimal numOfRooms = reservation.getNumOfRooms();
            List<Room> rooms = searchRoomSessionBeanLocal.searchRooms(reservation.getStartDate(), reservation.getEndDate(), reservation.getRoomType());
            if (rooms.isEmpty()) {
                
            }
            Date startDate = reservation.getStartDate();
            Date endDate = reservation.getEndDate();
            do {
                startDate = searchRoomSessionBeanLocal.addDays(startDate, 1);
            } while(startDate.before(endDate) || startDate.equals(endDate));
        } catch (RoomNotFoundException ex) {
            //create exceptionReport
        } catch (RoomTypeNotFoundException ex) {
            Logger.getLogger(ReservationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return reservation;
    }

    @Override
    public List<Reservation> retrieveAllReservationWithinDates(Date checkInDate, Date checkOutDate, RoomType roomType) {
        Query query =  em.createQuery("SELECT r FROM Reservation r");
        List<Reservation> list =  query.getResultList();
        list.removeIf(x -> {
            Date temp = x.getStartDate();
            Date end = x.getEndDate();
            while(temp.before(end)|| temp.equals(end)) {
                if((temp.getMonth() == checkInDate.getMonth()&& 
                    temp.getDate() == checkInDate.getDate() &&
                    temp.getYear() == checkInDate.getYear() 
                    ) || (
                    temp.getMonth() == checkOutDate.getMonth()&& 
                    temp.getDate() == checkOutDate.getDate() &&
                    temp.getYear() == checkOutDate.getYear() 
                        )){
                    return false;
                }
                temp = searchRoomSessionBeanLocal.addDays(temp, 1);
            }
            return true;
        });
        
        list.removeIf(x -> x.getRoomType().getName() != roomType.getName());
        System.out.println(list);
        return list;
    }

    @Override
    public Reservation getLoadedReservation(Reservation reservation) throws ReservationNotFoundException{
        // get managed reservation
        Reservation mReservation = em.find(Reservation.class,reservation.getReservationId());
        if(mReservation == null) {
            throw new ReservationNotFoundException("Reservation not found!");
        }
        // load all the rooms
        mReservation.getRoomList().size();
        mReservation.getRoomList().forEach(x -> x.getRoomRmType().getRoomRates().size());
        return mReservation;
    }

    @Override
    public List<Reservation> retrieveAllReseravtions() {
        Query query =  em.createQuery("SELECT r FROM Reservation r");
        List<Reservation> list =  query.getResultList();
        return list;
    }
}
