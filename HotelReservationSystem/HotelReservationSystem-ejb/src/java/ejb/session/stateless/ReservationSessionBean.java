/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Guest;
import entity.Reservation;
import java.util.List;
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
}
