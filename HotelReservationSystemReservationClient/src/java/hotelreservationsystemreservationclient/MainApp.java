/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemreservationclient;

import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.ReservationLineSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import entity.Employee;
import entity.Guest;
import exception.EmployeeNotFoundException;
import exception.GuestNotFoundException;
import java.util.Scanner;

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

    public MainApp(ReservationLineSessionBeanRemote reservationLineSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, GuestSessionBeanRemote guestSessionBeanRemote) {
        this.reservationLineSessionBeanRemote = reservationLineSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.guestSessionBeanRemote = guestSessionBeanRemote;
    }

    public void run() {
        System.out.println("==== Welcome to Hotel Reservation System Client ====");
        int option = 0;
        do {
            System.out.println("1. Guest Login");
            System.out.println("2. Register as Guest");
            System.out.println("3. Exit");
            System.out.print("> ");
            option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    doLogin();
                    break;
                case 2:
                    doRegister();
                    break;
                default:
                    System.out.println("\nInvalid input");
            }
            if (option > 2) {
                break;
            }
        } while (true);
    }

    private void doLogin() {
        System.out.print("Username >> ");
        String username = scanner.nextLine();

        // Check if the username exists before asking for a password
        try {
            if (!guestSessionBeanRemote.isUsernameExist(username)) {
                System.out.println("No account found with that username. Please register first.");
                return;
            }
        } catch (Exception e) {
            System.out.println("Error checking username: " + e.getMessage());
            return;
        }

        System.out.print("Password >> ");
        String password = scanner.nextLine();
        try {
            Guest guest = guestSessionBeanRemote.getGuestByUsername(username);
            if (guest.getPassword().equals(password)) {
                currentGuest = guest;
                System.out.println("Login successful");
            } else {
                System.out.println("Invalid Credentials");
            }
        } catch (GuestNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void doRegister() {
        System.out.print("Name >> ");
        String name = scanner.nextLine();
        System.out.print("Username >> ");
        String username = scanner.nextLine();
        System.out.print("Password >> ");
        String password = scanner.nextLine();

        // Check if the username already exists
        try {
            if (guestSessionBeanRemote.isUsernameExist(username)) {
                System.out.println("Username is already taken. Please choose another one.");
                return;
            }
        } catch (Exception e) {
            System.out.println("Error checking username: " + e.getMessage());
            return; // Exit if there's an error
        }

        // Create a new Guest object
        Guest newGuest = new Guest(name, username, password);

        // Register the guest using the session bean
        try {
            guestSessionBeanRemote.registerGuest(newGuest);
            System.out.println("Registration successful! You can now log in as a guest.");
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
        }

    }

}
