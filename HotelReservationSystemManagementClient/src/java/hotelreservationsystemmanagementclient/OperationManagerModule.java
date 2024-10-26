package hotelreservationsystemmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Tan Jian Feng
 */
public class OperationManagerModule {
    private RoomSessionBeanRemote roomSessionBeanRemote;
    private PartnerSessionBeanRemote partnerSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private Employee currentEmployee;
    private Scanner scanner = new Scanner(System.in);

    public OperationManagerModule(
            RoomSessionBeanRemote roomSessionBeanRemote, 
            PartnerSessionBeanRemote partnerSessionBeanRemote, 
            EmployeeSessionBeanRemote employeeSessionBeanRemote, 
            Employee currentEmployee
    ) {
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }
    
    public void run() {
        System.out.println("==== Welcome to Hotel Reservation System Management Client ====");
        System.out.println("====                 Hotel Operation Module                ====");
        int input = 0;
        do {
            System.out.println("1. Create new Room Type");
            System.out.println("2. View all room types");
            System.out.println("3. Create new room");
            System.out.println("4. View all rooms");
            System.out.println("5. View room allocation exception report");
            System.out.println("6. Exit");
            System.out.print("Enter >> ");
            input = scanner.nextInt();
            scanner.nextLine();
            if (input == 6) {
                break;
            }
            switch(input) {
                case 1: {
                    doCreateNewRoomType();
                    break;
                }
                case 2: {
                    doViewAllRoomTypes();
                    break;
                }
                case 3: {
                    doCreateNewRoom();
                    break;
                }
                case 4: {
                    doViewAllRooms();
                    break;
                }
                case 5: {
                    doViewRoomAllocationExceptionReport();
                    break;
                }
                default : {
                    System.out.println("\nInvalid input");
                    break;
                }
            }
        } while (true);
    }

    private void doCreateNewRoomType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doCreateNewRoom() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doViewAllRoomTypes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doViewAllRooms() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doViewRoomAllocationExceptionReport() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
