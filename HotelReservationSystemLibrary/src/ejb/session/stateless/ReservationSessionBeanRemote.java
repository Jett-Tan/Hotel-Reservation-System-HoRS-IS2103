/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Guest;
import entity.Partner;
import entity.Reservation;
import exception.ReservationNotFoundException;
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

    public Reservation createNewReservation(Reservation reservation);

    public Reservation allocateReservation(Reservation reservation) throws ReservationNotFoundException;

    public List<Reservation> retrieveAllReseravtions();

    public List<Reservation> retrieveAllPartnerReservations(Partner partner);

}
