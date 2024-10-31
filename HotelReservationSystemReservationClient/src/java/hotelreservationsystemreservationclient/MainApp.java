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
import entity.Guest;
import entity.Reservation;
import entity.RoomType;
import enumerations.ReservationTypeEnum;
import exception.GuestNotFoundException;
import exception.InvalidLoginCredentialsException;
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

    public MainApp(RoomTypeSessionBeanRemote roomTypeSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, GuestSessionBeanRemote guestSessionBeanRemote) {
        this();

        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.guestSessionBeanRemote = guestSessionBeanRemote;
    }

    public void run() throws InvalidDataException, GuestNotFoundException, InvalidLoginCredentialsException, ParseException, RoomTypeNotFoundException {
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
                System.out.println("**** You have successfully logged in. Welcome back " + this.currentGuest.getName() + "! ****\n");

                System.out.println("Please select an option.");
                System.out.println("1: Search Hotel Room");
                System.out.println("2: View Reservation Details");
                System.out.println("3: View All Reservations");
                System.out.println("4: Logout\n");
                System.out.print("> ");

                int response = scanner.nextInt();
                switch (response) {
                    case 1:
                        doSearchHotelRoom();
                        break;
                    case 2:
                        doViewMyReservation(currentGuest);
                        break;
                    case 3:
                        doViewAllMyReservations(currentGuest);
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

        // Create a new Guest object
        Guest newGuest = new Guest(name, username, password,new ArrayList<>());

        Set<ConstraintViolation<Guest>> errorList = this.validator.validate(newGuest);

        if (errorList.isEmpty()) {
            guestSessionBeanRemote.registerGuest(newGuest);
            System.out.println("Registration successful! You can now log in as a guest.");
        } else {
            System.out.println("Registration failed!");
        }
    }

    private void doSearchHotelRoom() throws ParseException, RoomTypeNotFoundException {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");

        System.out.println("**** Guest Search Hotel Room ****\n");
        System.out.print("Enter check in date (dd/mm/yyyy) > ");
        Date checkInDate = inputDateFormat.parse(scanner.nextLine().trim());
        System.out.print("Enter check in date (dd/mm/yyyy) > ");
        Date checkOutDate = inputDateFormat.parse(scanner.nextLine().trim());
//        System.out.print("Enter number of rooms > ");
//        int numOfRooms = scanner.nextInt();

        //create and set calendars
//        Calendar calendar1 = Calendar.getInstance();
//        calendar1.set(Calendar.YEAR, checkInDate.getYear());
//        calendar1.set(Calendar.MONTH, checkInDate.getMonth());
//        calendar1.set(Calendar.DAY_OF_MONTH, checkInDate.getDay());
//        calendar1.set(Calendar.HOUR_OF_DAY, checkInDate.getHours());
//        calendar1.set(Calendar.MINUTE, checkInDate.getMinutes());
//        calendar1.set(Calendar.SECOND, checkInDate.getSeconds()); // to test after 2AM
//
//        Calendar calendar2 = Calendar.getInstance();
//        calendar2.set(Calendar.YEAR, checkOutDate.getYear());
//        calendar2.set(Calendar.MONTH, checkOutDate.getMonth());
//        calendar2.set(Calendar.DAY_OF_MONTH, checkOutDate.getDay());
//        calendar2.set(Calendar.HOUR_OF_DAY, checkOutDate.getHours());
//        calendar2.set(Calendar.MINUTE, checkOutDate.getMinutes());
//        calendar2.set(Calendar.SECOND, checkOutDate.getSeconds());
        List<RoomType> roomTypesList = roomTypeSessionBeanRemote.getRoomTypes();
        String[] roomNames = new String[roomTypesList.size() + 1];
        BigDecimal[] roomTypePrices = new BigDecimal[roomTypesList.size() + 1];
        BigDecimal[] noOfRoomsAvail = new BigDecimal[roomTypesList.size() + 1];

        if (isLoggedIn) {
            System.out.println("Choose your desired room type number from 1 to " + roomTypesList.size());
            int count = 1;
            for (RoomType roomType : roomTypesList) {
                roomNames[count] = roomType.getName();
                roomTypePrices[count] = roomTypeSessionBeanRemote.calculatePrice(roomType.getRoomTypeId(), checkInDate, checkOutDate, isWalkIn);
                noOfRoomsAvail[count] = roomTypeSessionBeanRemote.calculateRoomsAvail(roomType.getRoomTypeId(), checkInDate, checkOutDate);
                System.out.print(count + " ");
                System.out.print("Room Name: ");
                System.out.print(roomNames[count]);
                System.out.print("Room Price: ");
                System.out.print(roomTypePrices[count]);
                System.out.print("Rooms Available: ");
                System.out.println(noOfRoomsAvail[count]);
                count++;
            }

            System.out.println("Choose room > ");
            int roomIdx = scanner.nextInt();
            scanner.nextLine();

            BigDecimal noOfRooms = BigDecimal.ZERO;
            if (roomIdx > 0 && roomIdx <= roomTypesList.size() + 1) {
                System.out.println("Enter number of rooms > ");
                noOfRooms = scanner.nextBigDecimal();
                scanner.nextLine();
            } else {
                System.out.println("Invalid Option!\n");
                return;
            }

            BigDecimal totalAmt = roomTypePrices[roomIdx].multiply(noOfRooms);

            System.out.printf("Confirm %d %s at $%d? (Enter Y to confirm) > ", noOfRooms, roomNames[roomIdx], totalAmt);
            String cfmchkout = scanner.nextLine().trim();

            if (cfmchkout.equals("Y")) {
                //create new reservation
                if (isWalkIn) {
                    Reservation newReservation = new Reservation(totalAmt, checkInDate, checkOutDate, noOfRooms, ReservationTypeEnum.WALK_IN);
                } else {
                    Reservation newReservation = new Reservation(totalAmt, checkInDate, checkOutDate, noOfRooms, ReservationTypeEnum.ONLINE);
                }

            }
        } else {
            int count = 1;
            for (RoomType roomType : roomTypesList) {
                roomNames[count] = roomType.getName();
                roomTypePrices[count] = roomTypeSessionBeanRemote.calculatePrice(roomType.getRoomTypeId(), checkInDate, checkOutDate, isWalkIn);
                noOfRoomsAvail[count] = roomTypeSessionBeanRemote.calculateRoomsAvail(roomType.getRoomTypeId(), checkInDate, checkOutDate);
                System.out.print(count + " ");
                System.out.print("Room Name: ");
                System.out.print(roomNames[count]);
                System.out.print("Room Price: ");
                System.out.print(roomTypePrices[count]);
                System.out.print("Rooms Available: ");
                System.out.println(noOfRoomsAvail[count]);
                count++;
            }
            System.out.println("Press any key to return > ");
            scanner.nextLine();
            return;
        }
    }

    private void doViewMyReservation(Guest currentGuest) {
        System.out.println("**** View My Reservation ****\n");
        doViewAllMyReservations(currentGuest);
        System.out.println("Select the reservation id > ");
        Long reservationId = scanner.nextLong();
        scanner.nextLine();
        
        Reservation reservation = reservationSessionBeanRemote.retrieveReservationByReservationId(currentGuest, reservationId);
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        System.out.println("Reservation Id: " + reservation.getReservationId());
        System.out.println("Start Date: " + dateFormat.format(reservation.getStartDate()));
        System.out.println("End Date: " + dateFormat.format(reservation.getEndDate()));
        System.out.println("Room Type: " + reservation.getReservationTpy());
        System.out.println("Number of Rooms: " + reservation.getNumOfRooms());
        System.out.println("Total Amount: " + reservation.getTotalAmount());
        
        System.out.print("Press enter to continue > ");
        scanner.nextLine();
    }

    private void doViewAllMyReservations(Guest currentGuest) {
        System.out.println("**** View All My Reservations ****\n");

        List<Reservation> reservationList = reservationSessionBeanRemote.retrieveAllMyReservations(currentGuest);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

        System.out.println("Id    Reservation Start Date   Reservation End Date    Total Amount");

        for (Reservation reservation : reservationList) {
            System.out.println(reservation.getReservationId() + "      "
                    + dateFormat.format(reservation.getStartDate()) + "      "
                    + dateFormat.format(reservation.getEndDate()) + "      "
                    + reservation.getTotalAmount());

        }
        System.out.println();

        System.out.print("Press enter to continue > ");
        scanner.nextLine();
    }

}
