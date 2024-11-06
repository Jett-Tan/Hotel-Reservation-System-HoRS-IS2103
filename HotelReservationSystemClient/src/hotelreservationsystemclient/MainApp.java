/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemclient;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tan Jian Feng
 */
public class MainApp {
    
    private HotelReservationWebService hotelReservationWebService;
    private Scanner scanner = new Scanner(System.in);
    
    MainApp(HotelReservationWebService hotelReservationWebService) {
        this.hotelReservationWebService = hotelReservationWebService;
    }
    
    public void run() {
        int inputInt;
        String input;
        do{
            System.out.println("====      Welcome to Hotel Reservation System Client       ====");
            System.out.println("1. Login");
            System.out.println("2. Exit");
            System.out.print("Enter >> ");
            inputInt = scanner.nextInt();
            scanner.nextLine();
            if(inputInt == 1){
                doLogin();
            }else if (inputInt == 2) {
                break;
            }
        }while(true);
    }
    
    public void doLogin() {
        String username, password;
        System.out.println("===============================================================");
        System.out.println("====      Welcome to Hotel Reservation System Client       ====");
        System.out.println("====                    Partner Log In                     ====");
        System.out.println("===============================================================");
        System.out.print("Enter username >> ");
        username = scanner.nextLine();
        
        System.out.print("Enter password >> ");
        password = scanner.nextLine();
        
        try {
            Partner partner = hotelReservationWebService.getPartnerByUsername(username);
            if(partner.getPassword().equals(password)) {
                PartnerModule app = new PartnerModule(hotelReservationWebService,partner);
                app.run();
            }
        } catch (PartnerNotFoundException_Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
