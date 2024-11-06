/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemclient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
/**
 *
 * @author Tan Jian Feng
 */
public class PartnerModule {
    private HotelReservationWebService hotelReservationWebService;
    private Partner currentPartner;
    private Scanner scanner = new Scanner(System.in);
    
    PartnerModule(HotelReservationWebService hotelReservationWebService, Partner partner) {
        this.hotelReservationWebService = hotelReservationWebService;
        this.currentPartner = partner;
    }

    void run() {
        if(currentPartner.getEmployeeType().equals(PartnerEmployeeTypeEnum.EMPLOYEE)) {
            employeeView();
        }else if (currentPartner.getEmployeeType().equals(PartnerEmployeeTypeEnum.MANAGER)){
            managerView();
        }
    }
    
    private void employeeView() {
        int inputInt = 0;
        String input;
        do{
            System.out.println("===============================================================");
            System.out.println("====    Welcome to Hotel Reservation System Management     ====");
            System.out.println("====                    Partner Employee                   ====");
            System.out.println("===============================================================");
            System.out.println("1. Search rooms");
            System.out.println("2. Exit");
            System.out.print("Enter >> ");
            inputInt = scanner.nextInt();
            scanner.nextLine();
            if(inputInt == 1) {
                doSearchRoom();
            } else if (inputInt == 2) {
                break;
            }
        }while(true);
    }

    private void managerView() {
        int inputInt = 0;
        String input;
        do{
            System.out.println("===============================================================");
            System.out.println("====    Welcome to Hotel Reservation System Management     ====");
            System.out.println("====                    Partner Manager                    ====");
            System.out.println("===============================================================");
            System.out.println("1. Search rooms");
            System.out.println("2. View all my reservations");
            System.out.println("3. Exit");
            System.out.print("Enter >> ");
            inputInt = scanner.nextInt();
            scanner.nextLine();
            if(inputInt == 1) {
                doSearchRoom();
            } else if (inputInt == 2) {
                viewAllMyReservation();
            }else if(inputInt == 3) {
                break;
            }
        }while(true);
    }

    private void doSearchRoom() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date checkin,checkout;
        RoomType roomType = new RoomType(); 
        int inputInt;
        System.out.println("===============================================================");
        System.out.println("==== Welcome to Hotel Reservation System Management Client ====");
        System.out.println("====                      Search Room                      ====");
        System.out.println("===============================================================");
        Date today = new Date();
        do {
            System.out.print("Enter check in date (dd-MM-yyyy) >> ");
            try {
                checkin = sdf.parse(scanner.nextLine().trim());
                if(checkin.after(today)||
                            (checkin.getDate() == today.getDate() &&
                            checkin.getYear() == today.getYear()&& 
                            checkin.getMonth()== today.getMonth()) ){
                    break;
                } else {
                    System.out.println("Wrong date input!");
                }
            } catch (ParseException ex) {
                System.out.println("Wrong date format!");
            }
        } while (true);
        do {
            System.out.print("Enter check out date (dd-MM-yyyy) >> ");
            try {
                checkout = sdf.parse(scanner.nextLine().trim());
                if(checkout.after(checkin) || checkout.equals(checkin)){
                    break;
                } else {
                    System.out.println("Wrong date input!");
                }
            } catch (ParseException ex) {
                System.out.println("Wrong date format!");
            }
        } while (true);
        XMLGregorianCalendar xmlCheckInDate = null;
        XMLGregorianCalendar xmlCheckOutDate = null;
        GregorianCalendar gcIn = new GregorianCalendar();
        GregorianCalendar gcOut = new GregorianCalendar();
        gcIn.setTime(checkin);
        gcOut.setTime(checkout);
        try {
            xmlCheckOutDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcOut);
            xmlCheckInDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcIn);
            this.hotelReservationWebService.generateReservation(xmlCheckInDate, xmlCheckOutDate, RoomRateTypeEnum.PEAK);
        } catch (DatatypeConfigurationException | RoomNotFoundException_Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void viewAllMyReservation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
