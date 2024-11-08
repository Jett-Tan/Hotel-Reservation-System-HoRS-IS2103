/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemclient;

import exception.GuestNotFoundException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
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
    private Boolean isManager = false;

    private ValidatorFactory validatorFactory;
    private Validator validator;

    public PartnerModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    PartnerModule(HotelReservationWebService hotelReservationWebService, Partner partner) {
        this.hotelReservationWebService = hotelReservationWebService;
        this.currentPartner = partner;
    }

    void run() {
        if (currentPartner.getEmployeeType().equals(PartnerEmployeeTypeEnum.EMPLOYEE)) {
            employeeView();
        } else if (currentPartner.getEmployeeType().equals(PartnerEmployeeTypeEnum.MANAGER)) {
            managerView();
        }
    }

    private void employeeView() {
        int inputInt = 0;
        String input;
        do {
            System.out.println("===============================================================");
            System.out.println("====    Welcome to Hotel Reservation System Management     ====");
            System.out.println("====                    Partner Employee                   ====");
            System.out.println("===============================================================");
            System.out.println("1. Search rooms");
            System.out.println("2. Exit");
            System.out.print("Enter >> ");
            inputInt = scanner.nextInt();
            scanner.nextLine();
            if (inputInt == 1) {
                doSearchRoom();
            } else if (inputInt == 2) {
                break;
            }
        } while (true);
    }

    private void managerView() {
        int inputInt = 0;
        String input;
        do {
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
            if (inputInt == 1) {
                doSearchRoom();
            } else if (inputInt == 2) {
                viewAllMyReservation();
            } else if (inputInt == 3) {
                break;
            }
        } while (true);
    }

    private void doSearchRoom() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date checkin, checkout;
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
                if (checkin.after(today)
                        || (checkin.getDate() == today.getDate()
                        && checkin.getYear() == today.getYear()
                        && checkin.getMonth() == today.getMonth())) {
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
                if (checkout.after(checkin)) {
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
            List<Reservation> reservations = this.hotelReservationWebService.generateReservation(xmlCheckInDate, xmlCheckOutDate);

            if (reservations.size() > 0) {
                do {
                    System.out.println("===============================================================");
                    System.out.println(String.format("No.%20s%20s", "Total Amount Per Room", "Room Type"));
                    for (int i = 1; i < reservations.size() + 1; i++) {
                        Reservation reservation = reservations.get(i - 1);
                        System.out.println(String.format("%d.%20s%20s", i, "$ " + reservation.getAmountPerRoom(), reservation.getRoomType().getName()));
                    }
                    System.out.println("===============================================================");
                    System.out.print("Enter >> ");
                    inputInt = scanner.nextInt();
                    scanner.nextLine();

                    if (inputInt > 0 && inputInt < reservations.size() + 1) {
                        do {
                            Reservation reservation = reservations.get(inputInt - 1);
                            System.out.println("1. Create reservation");
                            System.out.println("2. Exit");
                            System.out.print("Enter >> ");
                            inputInt = scanner.nextInt();
                            scanner.nextLine();
                            if (inputInt == 1) {
                                doReserve(reservation);
                                break;
                            } else if (inputInt == 2) {
                                break;
                            }

                        } while (true);
                        break;
                    }
                } while (true);
            } else {
                System.out.println("Currently there is not enough rooms");
            }
        } catch (DatatypeConfigurationException | RoomNotFoundException_Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void viewAllMyReservation() throws ReservationNotFoundException_Exception {
//        this.hotelReservationWebService.retrieveAllMyReservations(guest);
        System.out.println("================================================================");
        System.out.println("==== Welcome to Hotel Reservation System Reservation Client ====");
        System.out.println("====               View All My Reservations                 ====");
        System.out.println("================================================================");

        List<Reservation> reservationList = hotelReservationWebService.retrieveAllPartnerReservations(currentPartner);

        if (reservationList == null || reservationList.isEmpty()) {
            System.out.println("You currently have no reservations.");
            System.out.println();
            return;
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

        System.out.println("Id    Reservation Start Date   Reservation End Date    Total Amount");

        for (Reservation reservation : reservationList) {
            System.out.println(reservation.getReservationId() + "      "
                    + dateFormat.format(reservation.getStartDate()) + "      "
                    + dateFormat.format(reservation.getEndDate()) + "      "
                    + reservation.getAmountPerRoom());
        }
        System.out.println();
    }

    private void doReserve(Reservation reservation) {
        int inputInt;
        String input;
        System.out.println("===============================================================");
        System.out.println("==== Welcome to Hotel Reservation System Management Client ====");
        System.out.println("====                     Reserve Room                      ====");
        System.out.println("===============================================================");
        do {
            System.out.print("Enter number of rooms (1 - " + reservation.getNumOfRooms() + ") >> ");
            inputInt = scanner.nextInt();
            scanner.nextLine();
            if (inputInt > 0 && inputInt <= reservation.getNumOfRooms().intValue()) {
                reservation.setNumOfRooms(new BigDecimal(inputInt));
                break;
            }
        } while (true);

        //Set<ConstraintViolation<Reservation>> errorList = this.validator.validate(reservation);
        //if (errorList.isEmpty()) {
        boolean continueWith = true;
        do {
            System.out.println("Total amount: " + reservation.getNumOfRooms().multiply(reservation.getAmountPerRoom()));
            System.out.print("Enter confirmation (Y/N): ");
            input = scanner.nextLine().trim();
            if ("Y".equalsIgnoreCase(input)) {
                reservation = this.hotelReservationWebService.createNewReservation(reservation);
                break;
            } else if ("N".equalsIgnoreCase(input)) {
                continueWith = false;
                break;
            }
        } while (true);
        if (continueWith) {
            try {
                currentPartner = this.hotelReservationWebService.addReservation(this.currentPartner, reservation);
                System.out.println("Successfully create reservation!");
            } catch (PartnerNotFoundException_Exception | ReservationNotFoundException_Exception ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            System.out.println("CANCELLED");
        }
//        } else {
//            System.out.println("");
//            System.out.println("===============================================================");
//            System.out.println("====              Error Creating Reservation               ====");
//            errorList.forEach(x -> System.out.println(x.getPropertyPath() + " : " + x.getMessage().replace("size", "input") + " length"));
//            System.out.println("===============================================================");
//        }
    }

}
