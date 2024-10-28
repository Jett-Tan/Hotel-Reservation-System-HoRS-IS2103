/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemreservationclient;

import exception.InvalidDataException;
import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.ReservationLineSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import entity.Guest;
import exception.GuestNotFoundException;
import exception.InvalidLoginCredentialsException;
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

    private ReservationLineSessionBeanRemote reservationLineSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private GuestSessionBeanRemote guestSessionBeanRemote;
    private Guest currentGuest;
    private Scanner scanner = new Scanner(System.in);
    private ValidatorFactory validatorFactory;
    private Validator validator;

    public MainApp() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    public MainApp(ReservationLineSessionBeanRemote reservationLineSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, GuestSessionBeanRemote guestSessionBeanRemote) {
        this();

        this.reservationLineSessionBeanRemote = reservationLineSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.guestSessionBeanRemote = guestSessionBeanRemote;
    }

    public void run() throws InvalidDataException, GuestNotFoundException, InvalidLoginCredentialsException {
        System.out.println("==== Welcome to Hotel Reservation System Client ====");
        int option = 0;
        Boolean isLoggedIn = false;
        while (true) {

            System.out.println("1. Guest Login");
            System.out.println("2. Register as Guest");
            System.out.println("3. Exit");
            System.out.print("> ");
            option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    if (!isLoggedIn) {
                        doLogin();
                        isLoggedIn = true;
                    } else {
                        System.out.println("You are already logged in.");
                    }
                    break;
                case 2:
                    doRegister();
                    break;
                case 3:
                    isLoggedIn = false;
                    run();
                    break;
                default:
                    System.out.println("\nInvalid input");
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

    }

    private void doRegister() throws InvalidDataException {
        System.out.print("Name >> ");
        String name = scanner.nextLine();
        System.out.print("Username >> ");
        String username = scanner.nextLine();
        System.out.print("Password >> ");
        String password = scanner.nextLine();

        // Create a new Guest object
        Guest newGuest = new Guest(name, username, password);

        Set<ConstraintViolation<Guest>> errorList = this.validator.validate(newGuest);

        if (errorList.isEmpty()) {
            guestSessionBeanRemote.registerGuest(newGuest);
            System.out.println("Registration successful! You can now log in as a guest.");
        } else {
            System.out.println("Registration failed!");
        }
    }

}
