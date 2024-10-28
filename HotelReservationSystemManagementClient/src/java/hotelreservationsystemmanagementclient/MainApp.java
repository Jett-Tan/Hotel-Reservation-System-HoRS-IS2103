/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.Employee;
import enumerations.EmployeeTypeEnum;
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
    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    private Scanner scanner = new Scanner(System.in);
    
    MainApp(
            RoomSessionBeanRemote roomSessionBeanRemote, 
            PartnerSessionBeanRemote partnerSessionBeanRemote, 
            EmployeeSessionBeanRemote employeeSessionBeanRemote,
            RoomTypeSessionBeanRemote roomTypeSessionBeanRemote
    ) {
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
    }

    void run() {
        System.out.println("==== Welcome to Hotel Reservation System Management Client ====");
        int option = 0;
        do {
            System.out.println("1. Login");
            System.out.println("2. Exit");
            System.out.print(">> ");
            option = scanner.nextInt();
            scanner.nextLine();
            switch(option) {
                case 1: doLogin();
                        break;                        
            }
            if(option > 1){
                break;
            }
        }while (true);
    }

    private void doLogin() {
        System.out.println("");
        System.out.print("Username >> ");
        String username = scanner.nextLine();
        System.out.print("Password >> ");
        String password = scanner.nextLine();
        try {
            Employee employee = employeeSessionBeanRemote.getEmployeeByUsername(username);
            System.out.println(employee);
            if(employee.getPassword().equals(password)) {
                currentEmployee = employee;
                switch (employee.getEmployeeType()) {
                    case SYSTEM_ADMINISTRATOR:
                        {
                            SystemAdministrationModule app = new SystemAdministrationModule(
                                    partnerSessionBeanRemote,
                                    employeeSessionBeanRemote,
                                    currentEmployee
                            );      app.run();
                            break;
                        }
                    case GUEST_RELATION_OFFICER:
                        {

                            break;
                        }
                    case OPERATION_MANAGER:
                    case SALES:
                        {
                            HotelOperationModule app = new HotelOperationModule(
                                    roomSessionBeanRemote,
                                    partnerSessionBeanRemote,
                                    employeeSessionBeanRemote,
                                    currentEmployee,
                                    roomTypeSessionBeanRemote
                            );      app.run();
                            break;
                        }
                }
            } else {
                System.out.println("Invalid Credentials");
            }
        }catch (EmployeeNotFoundException ex) {
            System.out.println("Invalid Credentials");
//            System.out.println(ex.getMessage());
        }
    }
    
    
}
