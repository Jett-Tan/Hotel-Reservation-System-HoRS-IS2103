/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AllocationReport;
import entity.Guest;
import entity.Reservation;
import entity.Room;
import entity.RoomType;
import exception.ReservationNotFoundException;
import exception.RoomNotFoundException;
import exception.RoomTypeNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

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

    public List<Reservation> retrieveAllMyReservations(Guest guest) {
        long guestId = guest.getGuestId();
        List<Reservation> reservationList = em.createQuery("SELECT r FROM Reservation r WHERE r.guest.guestId = :inGuestId")
                .setParameter("inGuestId", guestId).getResultList();

        return reservationList;
    }

    public Reservation retrieveReservationByReservationId(Guest guest, Long reservationId) {
        long guestId = guest.getGuestId();
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.reservationId = :reservationId AND r.guest.guestId = :guestId")
                .setParameter("reservationId", reservationId)
                .setParameter("guestId", guestId);
        return (Reservation) query.getSingleResult();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

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
        Query query =  em.createQuery("SELECT r FROM Reservation r WHERE r.roomType == :roomType")
                .setParameter("roomType", roomType);
        return query.getResultList();
    }
}
