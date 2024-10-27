package hotelreservationsystemmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.Employee;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import enumerations.RoomRateTypeEnum;
import enumerations.RoomStatusEnum;
import exception.RoomNotFoundException;
import exception.RoomTypeNotFoundException;
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
        System.out.println("");
        System.out.println("==== Welcome to Hotel Reservation System Management Client ====");
        System.out.println("====                 Hotel Operation Module                ====");
        System.out.println("");
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
        System.out.println("");
        System.out.println("====                 Hotel Operation Module                ====");        
        System.out.println("====                  Create New Room Type                 ====");
        System.out.println("");
        RoomType roomType = new RoomType();
        List<String> amenities = new ArrayList<String>();
        List<Room> rooms = new ArrayList<Room>();
        List<RoomRate> roomRates = new ArrayList<RoomRate>();
        
        int option = 0;
        System.out.print("Enter name >> ");
        roomType.setName(scanner.nextLine().trim());
        System.out.print("Enter description >> ");
        roomType.setDescription(scanner.nextLine().trim());
        System.out.print("Enter bed >> ");
        roomType.setBed(scanner.nextLine().trim());
        System.out.print("Enter size >> ");
        roomType.setSize(scanner.nextDouble());
//        scanner.nextLine(); // Not sure if there is a need for nextLine()
        System.out.print("Enter capacity >> ");
        roomType.setCapacity(scanner.nextInt());
        scanner.nextLine();
        
        boolean isDone = false;
        System.out.println("Enter amentities >>");
        do {
            System.out.print("Enter >> ");
            String value = scanner.nextLine().trim();
            if(!value.equals("")){
                amenities.add(value);
            }else {
                isDone = true;
            }
        }while(!isDone);
        roomType.setAmenities(amenities);
        do{
            System.out.println("Select Room Status");
            System.out.println("1. Available");
            System.out.println("2. Unavailable");
            System.out.print("Enter room status (1 - 2)");
            option = scanner.nextInt();
            scanner.nextLine();
            if(option == 1) {
                roomType.setStatusType(RoomStatusEnum.AVAILABLE);
            } else if (option == 2) {
                roomType.setStatusType(RoomStatusEnum.UNAVAILABLE);
            }
        }while (option > 2 || option < 1);
        option = 0;
//        do{
//            System.out.println("Select Room Rate Types");
//            System.out.println("1. Normal");
//            System.out.println("2. Peak");
//            System.out.println("3. Promotion");
//            System.out.println("4. Published");
//            System.out.print("Enter room rate types (1 - 4)");
//            option = scanner.nextInt();
//            scanner.nextLine();
//            switch (option) {
//                case 1:
//                    roomType.setRateType(RoomRateTypeEnum.NORMAL);
//                    break;
//                case 2:
//                    roomType.setRateType(RoomRateTypeEnum.PEAK);
//                    break;
//                case 3:
//                    roomType.set(RoomRateTypeEnum.PROMOTION);
//                    break;
//                case 4:
//                    roomType.setRateType(RoomRateTypeEnum.PUBLISHED);
//                    break;
//                default:
//                    break;
//            }
//        }while (option > 4 || option < 1);
        
                
//       roomTypeSessionBeanRemote.createNewRoomType(roomType);
    
    }


    private void doCreateNewRoom() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doViewAllRoomTypes() {
        System.out.println("");
        System.out.println("====                 Hotel Operation Module                ====");        
        System.out.println("====                  View All Room Types                  ====");
        System.out.println("===============================================================");
        try{
            List<RoomType> roomTypes = roomTypeSessionBeanRemote.getRoomTypes();
            int option = 0;
            do{
                for(int i = 1; i < roomTypes.size() + 1; i++) {
                    RoomType roomType = roomTypes.get(i - 1);
                    System.out.println(String.format("%d.%10s%10s", i,roomType.getName(), roomType.getDescription()));
                }
                System.out.println("1. View room type details");
                System.out.println("2. Update room type");
                System.out.println("3. Delete room type");
                System.out.println("4. Exit"); 
                System.out.print("Enter >> ");
                option = scanner.nextInt();
                scanner.nextLine();
                if(option == 4) {
                    break;
                }
                System.out.print("Select room type >>");
                RoomType roomType = roomTypes.get(scanner.nextInt() -1);
                switch(option) {
                    case 1: {
                        doViewRoomType(roomType);
                        break;
                    }
                            
                    case 2: {
                        doUpdateRoomType(roomType);
                        break;
                    }
                    case 3: {
                        doDeleteRoomType(roomType);
                        break;
                    }
                }
            }while (option > 4 || option < 1 );
        }catch(RoomTypeNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }

    private void doViewAllRooms() {
        System.out.println("");
        System.out.println("====                 Hotel Operation Module                ====");        
        System.out.println("====                     View All Rooms                    ====");
        System.out.println("===============================================================");
        try{
            List<Room> rooms = roomSessionBeanRemote.getRooms();
            int option = 0;
            do{
                for(int i = 1; i < rooms.size() + 1; i++) {
                    Room room = rooms.get(i - 1);
                    System.out.println(String.format("%d.%10s%30s%30s", i,room.getRoomNumber(),room.getRoomRmType(), room.getRoomStatus()));
                }
                System.out.println("1. View room type details");
                System.out.println("2. Update room type");
                System.out.println("3. Delete room type");
                System.out.println("4. Exit"); 
                System.out.print("Enter >> ");
                option = scanner.nextInt();
                scanner.nextLine();
                if(option == 4) {
                    break;
                }
                System.out.print("Select room type >>");
                Room room = rooms.get(scanner.nextInt() -1);
                switch(option) {
                    case 1: {
                        doViewRoom(room);
                        break;
                    }
                            
                    case 2: {
                        doUpdateRoom(room);
                        break;
                    }
                    case 3: {
                        doDeleteRoom(room);
                        break;
                    }
                }
            }while (option > 4 || option < 1 );
        }catch(RoomNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }

    private void doViewRoomAllocationExceptionReport() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doDeleteRoomType(RoomType roomType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doUpdateRoomType(RoomType roomType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doViewRoomType(RoomType roomType) {
        System.out.println("");
        System.out.println("====                 Hotel Operation Module                ====");        
        System.out.println("====                     View Room Type                    ====");
        System.out.println("===============================================================");
        
        System.out.println(String.format("%10s%20s", "Name: ", roomType.getName()));
        System.out.println(String.format("%10s%20s", "Bed: ", roomType.getBed()));
        System.out.println(String.format("%10s%20s", "Capacity: ", roomType.getCapacity()));
        System.out.println(String.format("%10s%20s", "Size: ", roomType.getSize()));
        System.out.println(String.format("%10s%20s", "StatusType: ", roomType.getStatusType().toString()));
        System.out.println(String.format("%10s%20s", "Description: ", roomType.getDescription()));
        for(String amenity : roomType.getAmenities()) {// need to decide what to print
            System.out.println(String.format("%10s%20s", "Amenity: " , amenity));
        }
        for(RoomRate roomRate : roomType.getRoomRates()) {// need to decide what to print
//            System.out.println("Room rate: " + roomRate.getName() + " Room start date: " + roomRate.getStartDate());
        }
        
        System.out.println("Enter to exit >>");
        String exit = scanner.nextLine();
    }

    private void doUpdateRoom(Room room) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doDeleteRoom(Room room) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doViewRoom(Room room) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
