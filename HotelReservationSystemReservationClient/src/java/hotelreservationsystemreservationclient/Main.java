/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hotelreservationsystemreservationclient;

import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.ReservationLineSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import exception.GuestNotFoundException;
import exception.InvalidDataException;
import exception.InvalidLoginCredentialsException;
import javax.ejb.EJB;

/**
 *
 * @author Tan Jian Feng
 */
public class Main {

    
    @EJB
    private static ReservationLineSessionBeanRemote reservationLineSessionBeanRemote;
    
    @EJB
    private static ReservationSessionBeanRemote reservationSessionBeanRemote;
    
    @EJB
    private static GuestSessionBeanRemote guestSessionBeanRemote;
    
    public static void main(String[] args) throws InvalidDataException, GuestNotFoundException, InvalidLoginCredentialsException {
         MainApp mainApp = new MainApp(reservationLineSessionBeanRemote, reservationSessionBeanRemote, guestSessionBeanRemote);
        mainApp.run();
    }
    
}
