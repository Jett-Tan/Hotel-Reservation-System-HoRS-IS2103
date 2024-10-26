package hotelreservationsystemmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.Employee;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import enumeration.RoomRateTypeEnum;
import enumeration.RoomStatusEnum;
import java.util.ArrayList;
import java.util.List;
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
    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    private Employee currentEmployee;
    private Scanner scanner = new Scanner(System.in);

    public OperationManagerModule(
            RoomSessionBeanRemote roomSessionBeanRemote, 
            PartnerSessionBeanRemote partnerSessionBeanRemote, 
            EmployeeSessionBeanRemote employeeSessionBeanRemote, 
            Employee currentEmployee,
            RoomTypeSessionBeanRemote roomTypeSessionBeanRemote
    ) {
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.currentEmployee = currentEmployee;
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
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
        System.out.println("====                 Hotel Operation Module                ====");        
        System.out.println("====                  Create New Room Type                 ====");
        String name, description, bed;
        List<String> amenities = new ArrayList<String>();
        Double size;
        Integer capacity;
        RoomStatusEnum statusType;
        RoomRateTypeEnum rateType;
        List<Room> rooms = new ArrayList<Room>();
        List<RoomRate> roomRates = new ArrayList<RoomRate>();
        
        int option = 0;
        System.out.print("Enter name >> ");
        name = scanner.nextLine().trim();
        System.out.print("Enter description >> ");
        description = scanner.nextLine();
        System.out.print("Enter bed >> ");
        bed = scanner.nextLine();
        System.out.print("Enter size >> ");
        size = scanner.nextDouble();
        System.out.print("Enter capacity >> ");
        capacity = scanner.nextInt();
        scanner.nextLine();
        
        do{
            System.out.println("Select Room Status");
            System.out.println("1. Available");
            System.out.println("2. Unavailable");
            System.out.print("Enter room status (1 - 2)");
            option = scanner.nextInt();
            scanner.nextLine();
            if(option == 1) {
                statusType = RoomStatusEnum.AVAILABLE;
            } else if (option == 2) {
                statusType = RoomStatusEnum.UNAVAILABLE;
            }
        }while (option > 2 || option < 1);
        option = 0;
        do{
            System.out.println("Select Room Rate Types");
            System.out.println("1. Normal");
            System.out.println("2. Peak");
            System.out.println("3. Promotion");
            System.out.println("4. Published");
            System.out.print("Enter room rate types (1 - 4)");
            option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    rateType = RoomRateTypeEnum.NORMAL;
                    break;
                case 2:
                    rateType = RoomRateTypeEnum.PEAK;
                    break;
                case 3:
                    rateType = RoomRateTypeEnum.PROMOTION;
                    break;
                case 4:
                    rateType = RoomRateTypeEnum.PUBLISHED;
                    break;
                default:
                    break;
            }
        }while (option > 4 || option < 1);
    
       roomTypeSessionBeanRemote.createNewRoomType(new RoomType(
               
       ));
    
    
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
