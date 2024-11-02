package hotelreservationsystemmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import ejb.session.stateless.SearchRoomSessionBeanRemote;
import entity.Employee;
import entity.Guest;
import entity.Reservation;
import entity.Room;
import entity.RoomType;
import exception.GuestNotFoundException;
import exception.InvalidDataException;
import exception.InvalidLoginCredentialsException;
import exception.ReservationNotFoundException;
import exception.RoomNotFoundException;
import exception.RoomNumberAlreadyExistException;
import exception.RoomTypeNotFoundException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Tan Jian Feng
 */
public class GuestRelationOfficerModule {
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    private RoomTypeSessionBeanRemote    roomTypeSessionBeanRemote;
    private RoomSessionBeanRemote    roomSessionBeanRemote;
    private ReservationSessionBeanRemote    reservationSessionBeanRemote;
    private GuestSessionBeanRemote    guestSessionBeanRemote;
    private SearchRoomSessionBeanRemote    searchRoomSessionBeanRemote;
    private Employee currentEmployee;
    private Scanner scanner = new Scanner(System.in);
    
    private ValidatorFactory validatorFactory;
    private Validator validator;

    public GuestRelationOfficerModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    public GuestRelationOfficerModule(
            EmployeeSessionBeanRemote employeeSessionBeanRemote, 
            Employee currentEmployee,
            RoomRateSessionBeanRemote roomRateSessionBeanRemote,
            RoomTypeSessionBeanRemote    roomTypeSessionBeanRemote,
            RoomSessionBeanRemote    roomSessionBeanRemote,
            ReservationSessionBeanRemote    reservationSessionBeanRemote,
            GuestSessionBeanRemote    guestSessionBeanRemote,
            SearchRoomSessionBeanRemote    searchRoomSessionBeanRemote
    ) {
        this();
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.currentEmployee = currentEmployee;
        this.roomRateSessionBeanRemote = roomRateSessionBeanRemote;
        this.roomTypeSessionBeanRemote  =  roomTypeSessionBeanRemote;
        this.roomSessionBeanRemote  =  roomSessionBeanRemote;
        this.reservationSessionBeanRemote   = reservationSessionBeanRemote;
        this.guestSessionBeanRemote =   guestSessionBeanRemote;
        this.searchRoomSessionBeanRemote  = searchRoomSessionBeanRemote;
    }
    
    public void run() {
        
        int option = 0;
        do {
            System.out.println("===============================================================");
            System.out.println("==== Welcome to Hotel Reservation System Management Client ====");
            System.out.println("====             Guest Relation Officer Module             ====");
            System.out.println("===============================================================");
            System.out.println("1. Walk-in search room");
            System.out.println("2. Check-in guest");
            System.out.println("3. Check-out guest");
            System.out.println("4. Exit");
            System.out.print("Enter >> ");
            option = scanner.nextInt();
            scanner.nextLine();
            if(4 == option) {
                break;
            }
            if(1 == option) {
                doWalkInSearchRoom();
            } else if (2 == option) {
                doCheckInGuest();
            } else if (3 == option) {
                doCheckoutGuest();
            }
        } while(true);
        
    }

    private void doWalkInSearchRoom() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date checkin,checkout;
        RoomType roomType = new RoomType(); 
        int inputInt;
        System.out.println("===============================================================");
        System.out.println("==== Welcome to Hotel Reservation System Management Client ====");
        System.out.println("====                      Search Room                      ====");
        System.out.println("===============================================================");
        do {
            System.out.print("Enter check in date (dd-MM-yyyy) >> ");
            try {
                checkin = sdf.parse(scanner.nextLine().trim());
                break;
            } catch (ParseException ex) {
                System.out.println("Wrong date format!");
            }
        } while (true);
        do {
            System.out.print("Enter check in date (dd-MM-yyyy) >> ");
            try {
                checkout = sdf.parse(scanner.nextLine().trim());
                break;
            } catch (ParseException ex) {
                System.out.println("Wrong date format!");
            }
        } while (true);
        
        try {
            List<Reservation> reservations = searchRoomSessionBeanRemote.generateReservation(checkin, checkout);
            do {
                System.out.println("===============================================================");
                System.out.println(String.format("No.%20s%20s","Total Amount","Room Type"));
                for(int i = 1; i < reservations.size() + 1; i++) {
                    Reservation reservation = reservations.get(i - 1);
                    System.out.println(String.format("%d.%20s%20s",i,"$ "+reservation.getTotalAmount(),reservation.getRoomType().getName()));
                }
                System.out.println("===============================================================");
                System.out.print("Enter >> ");
                inputInt = scanner.nextInt();
                scanner.nextLine();
                if (inputInt > 0 && inputInt < reservations.size()+1) {
                    do{
                        Reservation reservation = reservations.get(inputInt - 1);
                        System.out.println("1. Create check in");
                        System.out.println("2. Create reservation");
                        System.out.print("Enter >> ");
                        inputInt = scanner.nextInt();
                        scanner.nextLine();
                        if (inputInt == 1) {
                            doCheckInRn(reservation);
                            break;
                        }else if (inputInt == 2) {
                            doReserve(reservation);
                            break;
                        }
                            
                    } while(true);
                    break;
                }
            } while(true);
            
        } catch (RoomNotFoundException ex) {
            Logger.getLogger(GuestRelationOfficerModule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void doCheckoutGuest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doCheckInGuest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doCheckInRn(Reservation reservation) {
        int inputInt;
        String input;
        System.out.println("===============================================================");
        System.out.println("==== Welcome to Hotel Reservation System Management Client ====");
        System.out.println("====                    Check In Room                      ====");
        System.out.println("===============================================================");
        do {
            System.out.print("Enter number of rooms (1 - "+ reservation.getNumOfRooms()+ ") >> ");
            inputInt = scanner.nextInt();
            scanner.nextLine();
            if(inputInt > 0 && inputInt <= reservation.getNumOfRooms().intValue()) {
                reservation.setNumOfRooms(new BigDecimal(inputInt));
                break;
            }
        } while(true);
        Guest guest = new Guest();
        String name,username,password;
        boolean continueOn = false;
        do {
            System.out.println("1. Registered guest");
            System.out.println("2. Unregiistered guest");
            System.out.print("Enter >> ");
            inputInt = scanner.nextInt();
            scanner.nextLine();
            if(inputInt == 1) {
                do {
                    System.out.println("1. Login");
                    System.out.println("2. Exit");
                    System.out.print("Enter >> ");
                    inputInt = scanner.nextInt();
                    scanner.nextLine();
                    if (inputInt == 1) {
                        System.out.print("Enter username >> ");
                        username = scanner.nextLine().trim();
                        System.out.print("Enter password >> ");
                        password = scanner.nextLine().trim();
                        try {
                            guest = guestSessionBeanRemote.loginGuest(username, password);
                            continueOn = true;
                            break;
                        } catch (InvalidLoginCredentialsException ex) {
                            System.out.println("Invalid Credentials");
                        } catch (GuestNotFoundException ex) {
                            System.out.println("Guest not found");
                        }
                    } else if(inputInt == 2) {
                        break;
                    }
                } while(true);
            }else if(inputInt == 2) {
                do {
                    System.out.print("Enter name >> ");
                    name = scanner.nextLine().trim();
                    System.out.print("Enter username >> ");
                    username = scanner.nextLine().trim();
                    System.out.print("Enter password >> ");
                    password = scanner.nextLine().trim();
                    try {
                        Guest newGuest = new Guest(name, username, password,new ArrayList<>());
                        Set<ConstraintViolation<Guest>> errorList = this.validator.validate(newGuest);
                        if (errorList.isEmpty()) {
                            guest = guestSessionBeanRemote.registerGuest(newGuest);
                            System.out.println("Registration successful!");
                            continueOn = true;
                            break;
                        } else {
                            System.out.println("Registration failed!");
                            System.out.println("===============================================================");
                            System.out.println("====                 Error Creating Guest                  ====");
                            errorList.forEach(x -> System.out.println(x.getPropertyPath() + " : " + x.getMessage().replace("size", "input") + " length"));
                            System.out.println("===============================================================");
                        }
                    } catch (InvalidDataException ex) {
                        Logger.getLogger(GuestRelationOfficerModule.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } while(true);
            }
            if (continueOn) {
                Set<ConstraintViolation<Reservation>> errorList = this.validator.validate(reservation);
                if (errorList.isEmpty()) {
                    reservation = reservationSessionBeanRemote.createNewReservation(reservation);
                    try {
                        guest = guestSessionBeanRemote.addReservation(guest, reservation);
                        System.out.println("Successfully create reservation!");
                    } catch (GuestNotFoundException | ReservationNotFoundException ex) {
                        Logger.getLogger(GuestRelationOfficerModule.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                } else {
                    System.out.println("");
                    System.out.println("===============================================================");
                    System.out.println("====              Error Creating Reservation               ====");
                    errorList.forEach(x -> System.out.println(x.getPropertyPath() + " : " + x.getMessage().replace("size", "input") + " length"));
                    System.out.println("===============================================================");
                    break;
                }
            }
        }while (true);
    }
    
    private void doReserve(Reservation reservation) {
        int inputInt;
        String input;
        System.out.println("===============================================================");
        System.out.println("==== Welcome to Hotel Reservation System Management Client ====");
        System.out.println("====                     Reserve Room                      ====");
        System.out.println("===============================================================");
        do {
            System.out.print("Enter number of rooms (1 - "+ reservation.getNumOfRooms()+ ") >> ");
            inputInt = scanner.nextInt();
            scanner.nextLine();
            if(inputInt > 0 && inputInt <= reservation.getNumOfRooms().intValue()) {
                reservation.setNumOfRooms(new BigDecimal(inputInt));
                break;
            }
        } while(true);
        Guest guest = new Guest();
        String name,username,password;
        do {
            System.out.println("1. Registered guest");
            System.out.println("2. Unregiistered guest");
            System.out.print("Enter >> ");
            inputInt = scanner.nextInt();
            scanner.nextLine();
            if(inputInt == 1) {
                do {
                    System.out.print("Enter username >> ");
                    username = scanner.nextLine().trim();
                    System.out.print("Enter password >> ");
                    password = scanner.nextLine().trim();
                    try {
                        guest = guestSessionBeanRemote.loginGuest(username, password);
                        break;
                    } catch (InvalidLoginCredentialsException ex) {
                        System.out.println("Invalid Credentials");
                    } catch (GuestNotFoundException ex) {
                        System.out.println("Guest not found");
                    }
                } while(true);
            }else if(inputInt == 2) {
                do {
                    System.out.print("Enter name >> ");
                    name = scanner.nextLine().trim();
                    System.out.print("Enter username >> ");
                    username = scanner.nextLine().trim();
                    System.out.print("Enter password >> ");
                    password = scanner.nextLine().trim();
                    try {
                        Guest newGuest = new Guest(name, username, password,new ArrayList<>());
                        Set<ConstraintViolation<Guest>> errorList = this.validator.validate(newGuest);
                        if (errorList.isEmpty()) {
                            guest = guestSessionBeanRemote.registerGuest(newGuest);
                            break;
                        } else {
                            System.out.println("Registration failed!");
                        }
                    } catch (InvalidDataException ex) {
                        Logger.getLogger(GuestRelationOfficerModule.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } while(true);
            }
            reservation = reservationSessionBeanRemote.createNewReservation(reservation);
            try {
                guest = guestSessionBeanRemote.addReservation(guest, reservation);
                break;
            } catch (GuestNotFoundException | ReservationNotFoundException ex) {
                Logger.getLogger(GuestRelationOfficerModule.class.getName()).log(Level.SEVERE, null, ex);
            }
        }while (true);
    }
}
