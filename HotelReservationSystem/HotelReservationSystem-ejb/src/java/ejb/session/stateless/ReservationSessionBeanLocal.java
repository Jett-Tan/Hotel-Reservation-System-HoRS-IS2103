/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Guest;
import entity.Reservation;
import entity.RoomType;
import exception.ReservationNotFoundException;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jovanfoo
 */
@Local
public interface ReservationSessionBeanLocal {

    public List<Reservation> retrieveAllMyReservations(Guest guest);

    public Reservation retrieveReservationByReservationId(Guest guest, Long reservationId);
    
    public List<Reservation> retrieveAllReservationWithinDates(Date checkInDate,Date checkOutDate, RoomType roomType);

    public Reservation getLoadedReservation(Reservation reservation) throws ReservationNotFoundException;
    
    public List<Reservation> retrieveAllReseravtions();
}
