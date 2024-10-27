/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import entity.Employee;
import exception.EmployeeNotFoundException;
import java.util.Scanner;

/**
 *
 * @author Tan Jian Feng
 */
public class MainApp {
    private RoomSessionBeanRemote roomSessionBeanRemote;
    private PartnerSessionBeanRemote partnerSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private Employee currentEmployee;
    private Scanner scanner = new Scanner(System.in);
    
    MainApp(
            RoomSessionBeanRemote roomSessionBeanRemote, 
            PartnerSessionBeanRemote partnerSessionBeanRemote, 
            EmployeeSessionBeanRemote employeeSessionBeanRemote
    ) {
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
    }

    void run() {
        System.out.println("==== Welcome to Hotel Reservation System Management Client ====");
        int option = 0;
        do {
            System.out.println("1. Login");
            System.out.println("2. Exit");
            System.out.println(">");
            option = scanner.nextInt();
            scanner.nextLine();
            switch(option) {
                case 1: doLogin();
                        break;
                default: System.out.println("\nInvalid input");
            }
            if(option > 2){
                break;
            }
        } while (true);
    }

    private void doLogin() {
        System.out.print("Username >> ");
        String username = scanner.nextLine();
        System.out.print("Password >> ");
        String password = scanner.nextLine();
        try {
            Employee employee = employeeSessionBeanRemote.getEmployeeByUsername(username);
            if(employee.getPassword().equals(password)) {
                currentEmployee = employee;
            } else {
                System.out.println("Invalid Credentials");
            }
        } catch (EmployeeNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    
}
