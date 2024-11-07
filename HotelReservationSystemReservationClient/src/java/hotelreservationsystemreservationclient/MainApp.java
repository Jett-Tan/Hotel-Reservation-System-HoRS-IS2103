/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemreservationclient;

import exception.InvalidDataException;
import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.ReservationLineSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import ejb.session.stateless.SearchRoomSessionBeanRemote;
import entity.Guest;
import entity.Reservation;
import entity.Room;
import entity.RoomType;
import enumerations.ReservationTypeEnum;
import enumerations.RoomRateTypeEnum;
import enumerations.RoomStatusEnum;
import exception.GuestNotFoundException;
import exception.GuestUsernameAlreadyExistException;
import exception.InvalidLoginCredentialsException;
import exception.ReservationNotFoundException;
import exception.RoomNotFoundException;
import exception.RoomTypeNotFoundException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.Validator;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

/**
 *
 * @author jovanfoo
 */
public class MainApp {

    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private GuestSessionBeanRemote guestSessionBeanRemote;
    private SearchRoomSessionBeanRemote searchRoomSessionBeanRemote;
    private Guest currentGuest;
    private Scanner scanner = new Scanner(System.in);
    private ValidatorFactory validatorFactory;
    private Validator validator;
    private Boolean isLoggedIn = false;
    private Boolean isWalkIn = true;

    public MainApp() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    public MainApp(RoomTypeSessionBeanRemote roomTypeSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, SearchRoomSessionBeanRemote searchRoomSessionBeanRemote, GuestSessionBeanRemote guestSessionBeanRemote) {
        this();

        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.guestSessionBeanRemote = guestSessionBeanRemote;
        this.searchRoomSessionBeanRemote = searchRoomSessionBeanRemote;

    }

    public void run() throws InvalidDataException, GuestNotFoundException, InvalidLoginCredentialsException, ParseException, RoomTypeNotFoundException, RoomNotFoundException {
        System.out.println("==== Welcome to Hotel Reservation System Client ====");
        int option = 0;

        while (true) {

            System.out.println("1. Guest Login");
            System.out.println("2. Register as Guest");
            System.out.println("3. Search Hotel Rooms");
            System.out.println("4. Exit");
            System.out.print("> ");
            option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    if (!isLoggedIn) {
                        doLogin();
                        break;
                    } else {
                        System.out.println("**** You have successfully logged in. Welcome back " + this.currentGuest.getName() + "! ****\n");
                    }
                    break;
                case 2:
                    doRegister();
                    break;
                case 3:
                    doSearchHotelRoom();
                    break;
                default:
                    System.out.println("\nInvalid input");
            }

            while (isLoggedIn) {
                System.out.println("**** Welcome back " + this.currentGuest.getName() + "! ****\n");

                System.out.println("Please select an option.");
                System.out.println("1: Search Hotel Room");
                System.out.println("2: View Reservation Details");
                System.out.println("3: View All Reservations");
                System.out.println("4: Logout\n");
                System.out.print("> ");

                int response = scanner.nextInt();
                scanner.nextLine();
                switch (response) {
                    case 1:
                        doSearchHotelRoom();
                        break;
                    case 2:
                        doViewMyReservation(currentGuest);
                        break;
                    case 3:
                        doViewAllMyReservations(currentGuest);
                        System.out.print("Press enter to continue > ");
                        scanner.nextLine();
                        break;
                    case 4:
                        isLoggedIn = false;
                        run();
                        break;
                    default:
                        System.out.println("\nInvalid input");
                }
            }
        }
    }

    private void doLogin() throws GuestNotFoundException, InvalidLoginCredentialsException {
        System.out.print("Username >> ");
        String username = scanner.nextLine();
        System.out.print("Password >> ");
        String password = scanner.nextLine();

        // Check if the username exists
        if (!guestSessionBeanRemote.isUsernameExist(username)) {
            System.out.println("No account found with that username. Please register first.");
            return;
        }

        currentGuest = guestSessionBeanRemote.loginGuest(username, password);
        System.out.println("Login successful! Welcome to Merlion Hotel!");
        isLoggedIn = true;
        isWalkIn = false;
    }

    private void doRegister() throws InvalidDataException {
        System.out.print("Name >> ");
        String name = scanner.nextLine();
        System.out.print("Username >> ");
        String username = scanner.nextLine();
        System.out.print("Password >> ");
        String password = scanner.nextLine();
        System.out.print("Passport number >> ");
        String passportNumber = scanner.nextLine();
        // Create a new Guest object
        Guest newGuest = new Guest(name, username, password, passportNumber, new ArrayList<>());

        Set<ConstraintViolation<Guest>> errorList = this.validator.validate(newGuest);

        if (errorList.isEmpty()) {
            try {
                guestSessionBeanRemote.registerGuest(newGuest);
                System.out.println("Registration successful! You can now log in as a guest.");
            } catch (GuestUsernameAlreadyExistException ex) {
                System.out.println("Registration failed! The username '" + username + "' is already taken. Please choose a different username.");
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("Registration failed!");
        }
    }

    private void doSearchHotelRoom() throws ParseException, RoomTypeNotFoundException, RoomNotFoundException {
        if (isLoggedIn) {
            doLoggedInSearchRoom();
        } else {
            // if not loggedIn, can only view
            doWalkInSearchRoom();
        }
    }

    private void doViewMyReservation(Guest currentGuest) {
        List<Reservation> reservationList = currentGuest.getReservationList();
        if (reservationList == null || reservationList.isEmpty()) {
            System.out.println("You currently have no reservations.");
            System.out.println();
            return;
        }
        
        doViewAllMyReservations(currentGuest);
        System.out.print("Select the reservation id > ");
        int reservationId = scanner.nextInt();
        scanner.nextLine();

        Reservation reservation = reservationList.get(reservationId - 1);

        System.out.println("================================================================");
        System.out.println("==== Welcome to Hotel Reservation System Reservation Client ====");
        System.out.println("====                  View My Reservation                   ====");
        System.out.println("================================================================");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        System.out.println("Reservation Id: " + reservation.getReservationId());
        System.out.println("Reservation Type: " + reservation.getReservationType().name());
        System.out.println("Room Type: " + reservation.getRoomType().getName());
        System.out.println("Number of Rooms: " + reservation.getNumOfRooms());
        System.out.println("Start Date: " + dateFormat.format(reservation.getStartDate()));
        System.out.println("End Date: " + dateFormat.format(reservation.getEndDate()));
        System.out.println("Total Amount: " + reservation.getAmountPerRoom().multiply(reservation.getNumOfRooms()));

        System.out.print("Press enter to continue > ");
        scanner.nextLine();
    }

    private void doViewAllMyReservations(Guest currentGuest) {
        System.out.println("================================================================");
        System.out.println("==== Welcome to Hotel Reservation System Reservation Client ====");
        System.out.println("====               View All My Reservations                 ====");
        System.out.println("================================================================");

        List<Reservation> reservationList = currentGuest.getReservationList();

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

    private void doWalkInSearchRoom() throws RoomTypeNotFoundException, RoomNotFoundException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date checkin, checkout;
        int inputInt;
        System.out.println("================================================================");
        System.out.println("==== Welcome to Hotel Reservation System Reservation Client ====");
        System.out.println("====                      Search Room                       ====");
        System.out.println("================================================================");
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
                if (checkout.after(checkin) || checkout.equals(checkin)) {
                    break;
                } else {
                    System.out.println("Wrong date input!");
                }
            } catch (ParseException ex) {
                System.out.println("Wrong date format!");
            }
        } while (true);

        List<Reservation> reservations = searchRoomSessionBeanRemote.generateReservationOnline(checkin, checkout);
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
                scanner.nextLine();
                break;
            } while (true);
        } else {
            System.out.println("Currently there is not enough rooms");
        }
    }

    private void doLoggedInSearchRoom() throws RoomTypeNotFoundException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date checkin, checkout;
        int inputInt;
        System.out.println("================================================================");
        System.out.println("==== Welcome to Hotel Reservation System Reservation Client ====");
        System.out.println("====                      Search Room                       ====");
        System.out.println("================================================================");
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
                if (checkout.after(checkin) || checkout.equals(checkin)) {
                    break;
                } else {
                    System.out.println("Wrong date input!");
                }
            } catch (ParseException ex) {
                System.out.println("Wrong date format!");
            }
        } while (true);

        try {

            List<Reservation> reservations = new ArrayList<>();
            if (isWalkIn) {
                reservations = searchRoomSessionBeanRemote.generateReservationWalkIn(checkin, checkout);
            } else {
                reservations = searchRoomSessionBeanRemote.generateReservationOnline(checkin, checkout);
            }

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

                    // create reservation
                    Reservation reservation = reservations.get(inputInt - 1);
                    doReserve(reservation);
                    break;
                } while (true);
            } else {
                System.out.println("Currently there is not enough rooms");
            }

        } catch (RoomNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void doReserve(Reservation reservation) {
        int inputInt;
        String input;
        System.out.println("===============================================================");
        System.out.println("==== Welcome to Hotel Reservation System Reservation Client ====");
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
        do {
            Set<ConstraintViolation<Reservation>> errorList = this.validator.validate(reservation);
            if (errorList.isEmpty()) {
                boolean continueWith = true;
                do {
                    System.out.println("Total amount: " + reservation.getNumOfRooms().multiply(reservation.getAmountPerRoom()));
                    System.out.print("Enter confirmation (Y/N): ");
                    input = scanner.nextLine().trim();
                    if ("Y".equalsIgnoreCase(input)) {
                        reservation = reservationSessionBeanRemote.createNewReservation(reservation);
                        break;
                    } else if ("N".equalsIgnoreCase(input)) {
                        continueWith = false;
                        break;
                    }
                } while (true);
                if (continueWith) {
                    try {
                        currentGuest = guestSessionBeanRemote.addReservation(currentGuest, reservation);
                        //reservation.setGuest(currentGuest);
                        System.out.println("Successfully create reservation!");
                    } catch (GuestNotFoundException ex) {
                        System.out.println(ex.getMessage());
                    } catch (ReservationNotFoundException ex) {
                        System.out.println(ex.getMessage());
                    }
                    break;
                } else {
                    System.out.println("CANCELLED");
                    break;
                }
            } else {
                System.out.println("");
                System.out.println("===============================================================");
                System.out.println("====              Error Creating Reservation               ====");
                errorList.forEach(x -> System.out.println(x.getPropertyPath() + " : " + x.getMessage().replace("size", "input") + " length"));
                System.out.println("===============================================================");
                break;
            }
        } while (true);
    }

}
