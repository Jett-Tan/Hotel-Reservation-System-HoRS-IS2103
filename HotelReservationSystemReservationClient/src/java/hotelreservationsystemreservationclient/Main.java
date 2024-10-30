/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hotelreservationsystemreservationclient;

import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import exception.GuestNotFoundException;
import exception.InvalidDataException;
import exception.InvalidLoginCredentialsException;
import exception.RoomTypeNotFoundException;
import java.text.ParseException;
import javax.ejb.EJB;

/**
 *
 * @author Tan Jian Feng
 */
public class Main {

    
    @EJB
    private static RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    
    @EJB
    private static ReservationSessionBeanRemote reservationSessionBeanRemote;
    
    @EJB
    private static GuestSessionBeanRemote guestSessionBeanRemote;
    
    public static void main(String[] args) throws InvalidDataException, GuestNotFoundException, InvalidLoginCredentialsException, ParseException, RoomTypeNotFoundException {
         MainApp mainApp = new MainApp(roomTypeSessionBeanRemote, reservationSessionBeanRemote, guestSessionBeanRemote);
        mainApp.run();
    }
    
}
