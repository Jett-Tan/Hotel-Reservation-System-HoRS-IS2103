/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Guest;
import entity.Reservation;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author jovanfoo
 */
@Remote
public interface ReservationSessionBeanRemote {

    public List<Reservation> retrieveAllMyReservations(Guest guest);

    public Reservation retrieveReservationByReservationId(Guest guest, Long reservationId);

}
