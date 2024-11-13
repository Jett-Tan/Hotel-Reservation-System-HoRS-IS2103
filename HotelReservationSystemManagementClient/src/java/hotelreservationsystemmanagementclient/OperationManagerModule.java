package hotelreservationsystemmanagementclient;

import ejb.session.stateless.AllocationReportSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.AllocationReport;
import entity.Employee;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import enumerations.RoomRateTypeEnum;
import enumerations.RoomStatusEnum;
import exception.RoomNotFoundException;
import exception.RoomNumberAlreadyExistException;
import exception.RoomRateNotFoundException;
import exception.RoomTypeNameAlreadyExistException;
import exception.RoomTypeNotFoundException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.validation.Validator;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

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
    private RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    private AllocationReportSessionBeanRemote allocationReportSessionBeanRemote;
    private Employee currentEmployee;
    private Scanner scanner = new Scanner(System.in);
    private ValidatorFactory validatorFactory;
    private Validator validator;

    public OperationManagerModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
    public OperationManagerModule(
            RoomSessionBeanRemote roomSessionBeanRemote, 
            PartnerSessionBeanRemote partnerSessionBeanRemote, 
            EmployeeSessionBeanRemote employeeSessionBeanRemote, 
            Employee currentEmployee,
            RoomTypeSessionBeanRemote roomTypeSessionBeanRemote,
            RoomRateSessionBeanRemote roomRateSessionBeanRemote,
            AllocationReportSessionBeanRemote allocationReportSessionBeanRemote
    ) {
        this();
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.currentEmployee = currentEmployee;
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
        this.roomRateSessionBeanRemote = roomRateSessionBeanRemote;
        this.allocationReportSessionBeanRemote = allocationReportSessionBeanRemote;
    }
    
    public void run() {
        int input = 0;
        do {
            System.out.println("===============================================================");
            System.out.println("==== Welcome to Hotel Reservation System Management Client ====");
            System.out.println("====                Operation Manager Module               ====");
            System.out.println("===============================================================");
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
        System.out.println("===============================================================");
        System.out.println("====                Operation Manager Module               ====");        
        System.out.println("====                  Create New Room Type                 ====");
        System.out.println("===============================================================");

        RoomType roomType = new RoomType();
        List<String> amenities = new ArrayList<>();
        List<Room> rooms = new ArrayList<>();
        List<RoomRate> roomRates = new ArrayList<>();
        
        int inputInt = 0;
        System.out.print("Enter name >> ");
        roomType.setName(scanner.nextLine().trim());
        System.out.print("Enter description >> ");
        roomType.setDescription(scanner.nextLine().trim());
        System.out.print("Enter bed (string) >> ");
        roomType.setBed(scanner.nextLine().trim());
        System.out.print("Enter size (double) >> ");
        roomType.setSize(new BigDecimal(scanner.nextLine().trim()));
        System.out.print("Enter capacity (integer) >> ");
        roomType.setCapacity(new BigDecimal(scanner.nextLine().trim()));
        
        boolean isDone = false;
        System.out.println("Enter amentities >> ");
        do {
            System.out.print("Enter (Enter to skip) >> ");
            String value = scanner.nextLine().trim();
            if(!value.equals("")){
                amenities.add(value);
            }else {
                isDone = true;
            }
        }while(!isDone);
        roomType.setAmenities(amenities);
        do{
            System.out.println("Select Room Status: ");
            System.out.println("1. Available");
            System.out.println("2. Unavailable");
            System.out.print("Enter room status (1 - 2) >> ");
            inputInt = scanner.nextInt();
            scanner.nextLine();
            if(inputInt == 1) {
                roomType.setStatusType(RoomStatusEnum.AVAILABLE);
            } else if (inputInt == 2) {
                roomType.setStatusType(RoomStatusEnum.UNAVAILABLE);
            }
        }while (inputInt > 2 || inputInt < 1);
        inputInt = 0;
        try {
            System.out.println("Select room rates: ");
            List<RoomRate> roomRatesList = roomRateSessionBeanRemote.getAvailableRoomRates();
            System.out.println(String.format("%s%22s%8s%12s%12s%13s","No.","Name","Rate","Start Date","End Date","Rate Status"));
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            for(int i = 1; i< roomRatesList.size() + 1; i++) {
                RoomRate roomrate = roomRatesList.get(i - 1);
                System.out.println(String.format("%d.%23s%8s%12s%12s%13s",
                        i,
                        roomrate.getName(),
                        roomrate.getRate(),
                        dateFormat.format(roomrate.getStartDate()),
                        dateFormat.format(roomrate.getEndDate()),
                        roomrate.getRateStatus()));
            }
            do{
                System.out.print("Enter ("+(roomRatesList.size() + 1)+" to skip) >> ");
                inputInt = scanner.nextInt();
                scanner.nextLine();
                if (inputInt < roomRatesList.size() + 1 && inputInt > 0) {
                    roomRates.add(roomRatesList.get(inputInt - 1));
                } else if (roomRatesList.size() + 1 == inputInt) {
                    break;
                }
            }while(true);
        } catch (RoomRateNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        
        do{
            try{
                List<RoomType> roomTypes = roomTypeSessionBeanRemote.getAvailableRoomTypes();
                System.out.println("Select parent room type: ");
                int i = 1;
                for(RoomType roomtype : roomTypes) {
                    System.out.println(String.format("%d.%60s",i++,roomtype.getName()));
                }
                System.out.print("Enter ("+ i++ +" to skip) >> ");
                inputInt = scanner.nextInt();
                scanner.nextLine();
                if(inputInt == roomTypes.size() + 1){
                    break;
                }
                if(inputInt > 0 && inputInt < roomTypes.size() + 1) {
                    roomType.setParentRoomType(roomTypes.get(inputInt - 1));
                    break;
                }
            } catch(RoomTypeNotFoundException ex){
                break;
            }
        }while (true);
        roomType.setRoomRates(roomRates);
        Set<ConstraintViolation<RoomType>> errorList = this.validator.validate(roomType);
        if (errorList.isEmpty()) {
            System.out.println("");
            try {
                roomType = roomTypeSessionBeanRemote.createNewRoomType(roomType);
                System.out.println("New room type created with name of " + roomType.getName());
            } catch (RoomTypeNameAlreadyExistException ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            System.out.println("");
            System.out.println("===============================================================");
            System.out.println("====             Error Creating New Room Type              ====");
            errorList.forEach(x -> System.out.println(x.getPropertyPath() + " : " + x.getMessage().replace("size","input") + " length"));
            System.out.println("===============================================================");
        }
    }

    private void doCreateNewRoom() {
        System.out.println("===============================================================");
        System.out.println("====                Operation Manager Module               ====");        
        System.out.println("====                     Create New Room                   ====");
        System.out.println("===============================================================");
        Room room = new Room();
        String input;
        int inputInt;
        do{
            System.out.println("Room Number : ");
            System.out.print("Enter >> ");
            input = scanner.nextLine().trim();
            if(!"".equals(input)){
                room.setRoomNumber(input);
                break;
            }
        } while(true);
        List<Date> bookedDates = new ArrayList<>();
//        do{
//            System.out.println("Booked date : ");
//            System.out.print("Enter (dd-MM--yyyy) (Leave blank to skip) >> ");
//            input = scanner.nextLine().trim();
//            if(!"".equals(input)){
//                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//                try {
//                    bookedDates.add(sdf.parse(input));
//                } catch (ParseException ex) {
//                    System.out.println("Invalid date format!");
//                }
//            }
//            if("".equals(input)) {
                room.setBookedDates(bookedDates);
//                break;
//            }
//        } while(true);
//        do{
//            System.out.println("Currently checked in : ");
//            System.out.print("Enter (T/F) >> ");
//            input = scanner.nextLine().trim();
//            if(!"".equals(input)) {
//                if(input.toUpperCase().equals("T")){
//                    room.setIsCheckedIn(true);
//                    break;
//                } else if (input.toUpperCase().equals("F")) {
                    room.setIsCheckedIn(false);
//                    break;
//                } else {
//                    System.out.println("Wrong input");
//                }
//            }
//        }while(true);
        do{
            System.out.println("Room Status: ");
            System.out.println("1. Available");
            System.out.println("2. Unavailable");
            System.out.print("Enter >> ");
            inputInt = scanner.nextInt();
            scanner.nextLine();
            if(inputInt == 1) {
                room.setRoomStatus(RoomStatusEnum.AVAILABLE);
                break;
            } else if (inputInt == 2) {
                room.setRoomStatus(RoomStatusEnum.UNAVAILABLE);
                break;
            }
        }while(true);
        
        try {
            List<RoomType> roomTypes;
            roomTypes = roomTypeSessionBeanRemote.getAvailableRoomTypes();
            int option = 0;
            do{
                System.out.println("Room Type :");
                System.out.println(String.format("%s%33s%30s", "No.","Name", "Description"));
                for(int i = 1; i < roomTypes.size() + 1; i++) {
                    RoomType roomType = roomTypes.get(i - 1);
                    System.out.println(String.format("%d.%32s%30s", i,roomType.getName(), roomType.getDescription()));
                }
                System.out.println("===============================================================");
                System.out.print("Enter >> ");
                option = scanner.nextInt();
                scanner.nextLine();
                if (option > 0 && option < roomTypes.size()+ 1) {
                    room.setRoomRmType(roomTypes.get(option -1));
                    break;
                }
            }while (true);
        } catch (RoomTypeNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        
        Set<ConstraintViolation<Room>> errorList = this.validator.validate(room);
        if (errorList.isEmpty()) {
            System.out.println("");
            try {
                room = roomSessionBeanRemote.createNewRoom(room);
                System.out.println("New room created with room number of " + room.getRoomNumber());
            } catch (RoomNumberAlreadyExistException | RoomTypeNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            System.out.println("");
            System.out.println("===============================================================");
            System.out.println("====               Error Creating New Room                 ====");
            errorList.forEach(x -> System.out.println(x.getPropertyPath() + " : " + x.getMessage().replace("size","input") + " length"));
            System.out.println("===============================================================");
        }
    }

    private void doViewAllRoomTypes() {
        System.out.println("===============================================================");
        System.out.println("====                 Hotel Operation Module                ====");        
        System.out.println("====                  View All Room Types                  ====");
        System.out.println("===============================================================");
        int EXITVALUE = 2;
        try{
            List<RoomType> roomTypes = roomTypeSessionBeanRemote.getRoomTypes();
            int option = 0;
            do{
                System.out.println(String.format("%s%20s%40s", "No.","Name", "Description"));
                for(int i = 1; i < roomTypes.size() + 1; i++) {
                    RoomType roomType = roomTypes.get(i - 1);
                    String description = roomType.getDescription();
                    if(description.length()> 20) {
                        description = description.substring(0, 17).concat("...");
                    }
                    System.out.println(String.format("%d.%21s%40s", i,roomType.getName(), description));
                }
                System.out.println("===============================================================");
                if(roomTypes.size() < 1) {
                    System.out.println("1. Exit");
                    EXITVALUE = 1;
                } else {
                    System.out.println("1. View room type details");
                    System.out.println("2. Exit"); 
                    System.out.print("Enter >> ");
                }
                option = scanner.nextInt();
                scanner.nextLine();
                if(option == EXITVALUE) {
                    break;
                }
                if(option == 1) {
                    System.out.print("Select room type >> ");
                    RoomType roomType = roomTypes.get(scanner.nextInt() -1);
                    switch(option) {
                        case 1: {
                            doViewRoomType(roomType);
                            break;
                        }
                    }
                }
            }while (option > 2 || option < 1 );
        }catch(RoomTypeNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }

    private void doViewAllRooms() {
        int EXITVALUE = 2;
        try{
            System.out.println("===============================================================");
            System.out.println("====                 Hotel Operation Module                ====");        
            System.out.println("====                     View All Rooms                    ====");
            System.out.println("===============================================================");
            int option = 0;
            do{
                List<Room> rooms = roomSessionBeanRemote.getRooms();
                System.out.println(String.format("%s%20s%20s%20s", "No.","Room Number","Room Type", "Room Status"));
                for(int i = 1; i < rooms.size() + 1; i++) {
                    Room room = rooms.get(i - 1);
                    try {
                          RoomType roomType = new RoomType();
                          roomType.setName("NONE");
                        if(room.getRoomRmType() != null) {
                            roomType = roomTypeSessionBeanRemote.getLoadedRoomType(room.getRoomRmType());
                        }
                        System.out.println(String.format("%d.%21s%20s%20s", i,room.getRoomNumber(),roomType.getName(), room.getRoomStatus()));
                    } catch (RoomTypeNotFoundException | RoomTypeNameAlreadyExistException | RoomRateNotFoundException ex) {
                        System.out.println(ex.getMessage());
                    }
                    
                }
                System.out.println("===============================================================");
                if (rooms.size() > 0) {
                    System.out.println("1. View room");
                    System.out.println("2. Exit");
                } else {
                    System.out.println("1. Exit");
                    EXITVALUE = 1;
                }
                System.out.print("Enter >> ");
                option = scanner.nextInt();
                scanner.nextLine();
                if(option == EXITVALUE) {
                    break;
                }
                if(option == 1) {
                     System.out.print("Enter No. >> ");
                     Room room = rooms.get(scanner.nextInt() -1);
                     doViewRoom(room);
                }
            }while (true);
        }catch(RoomNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }

    private void doViewRoomAllocationExceptionReport() {
        System.out.println("===============================================================");
        System.out.println("====                 Hotel Operation Module                ====");        
        System.out.println("====              View Room Allocation Report              ====");
        System.out.println("===============================================================");
        List<AllocationReport> reports = allocationReportSessionBeanRemote.getAllAllocationReports();
        reports.removeIf(x -> x.isIsSettled());
        for(int i = 1; i < reports.size() + 1; i++) {
            AllocationReport temp = reports.get(i-1);
            System.out.println(String.format("%d.%s",i,temp.getType()));
        }
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }    

    private void doViewRoomType(RoomType roomType) {
        System.out.println("===============================================================");
        System.out.println("====                 Hotel Operation Module                ====");        
        System.out.println("====                     View Room Type                    ====");
        System.out.println("===============================================================");
        try {
            roomType = roomTypeSessionBeanRemote.getLoadedRoomType(roomType);
        } catch (RoomTypeNotFoundException | RoomTypeNameAlreadyExistException | RoomNotFoundException | RoomRateNotFoundException ex) {
            System.out.println("Error with loading room type !");
            return;
        }
        System.out.println("Name             : " +  roomType.getName());
        System.out.println("Bed              : " +  roomType.getBed());
        System.out.println("Capacity         : " +  roomType.getCapacity());
        System.out.println("Size             : " +  roomType.getSize());
        System.out.println("StatusType       : " +  roomType.getStatusType().toString());
        System.out.println("Description      : " +  roomType.getDescription());
        System.out.println("Parent Room Type : " + (roomType.getParentRoomType() == null ? "NONE" : roomType.getParentRoomType().getName()));
        System.out.println("Amenitities      : ");
        int i = 1;
        for(String amenity : roomType.getAmenities()) {// need to decide what to print
            System.out.println(String.format("%d.%20s", i++ , amenity));
        }
        System.out.println("Room Rate        : ");
        i = 1;
        for(RoomRate roomRate : roomType.getRoomRates()) {// need to decide what to print
            System.out.println(String.format("%d.%20s",i++ ,roomRate.getName()));
        }
        do{
            System.out.println("===============================================================");
            System.out.println("1. Update room type");
            System.out.println("2. Delete room type");
            System.out.println("3. Exit");
            System.out.println("Enter to exit >> ");
            int x = scanner.nextInt();
            scanner.nextLine();
            if(x == 3) {
                break;
            } else if (x == 1) {
                doUpdateRoomType(roomType);
                break;
            } else if (x == 2) {
                doDeleteRoomType(roomType);
                break;
            }
        } while(true);
    }
    
    private void doUpdateRoomType(RoomType roomType) {
        System.out.println("===============================================================");
        System.out.println("====                 Hotel Operation Module                ====");        
        System.out.println("====                    Update Room Type                   ====");
        System.out.println("===============================================================");
        String input;
        int inputInt= 0;
        List<String> amenities = roomType.getAmenities();
        List<RoomRate> roomRates = roomType.getRoomRates();
        
        System.out.println("Name            : " + roomType.getName());
        System.out.print("Enter (Leave blank to skip) >> ");
        input = scanner.nextLine();
        if (!"".equals(input)) {
            roomType.setName(input);
        }
        System.out.println("Description     : " + roomType.getDescription());
        System.out.print("Enter (Leave blank to skip) >> ");
        input = scanner.nextLine();
        if (!"".equals(input)) {
            roomType.setDescription(input);
        }
        System.out.println("StatusType      : " + roomType.getStatusType());
        System.out.println("1. Available");
        System.out.println("2. Unavailable");
        System.out.println("3. Skip");
        System.out.print("Enter >> ");
        inputInt = scanner.nextInt();
        scanner.nextLine();
        if(inputInt == 1) {
            roomType.setStatusType(RoomStatusEnum.AVAILABLE);
        } else if(inputInt == 2) {
            roomType.setStatusType(RoomStatusEnum.UNAVAILABLE);
        }
        System.out.println("Bed             : " + roomType.getBed());
        System.out.print("Enter (Leave blank to skip) >> ");
        input = scanner.nextLine();
        if (!"".equals(input)) {
            roomType.setBed(input);
        }
        System.out.println("Capacity        : " + roomType.getCapacity());
        System.out.print("Enter (Leave blank to skip) >> ");
        input = scanner.nextLine();
        if (!"".equals(input)) {
            roomType.setCapacity(new BigDecimal(input));
        }
        System.out.println("Size            : " + roomType.getSize());   
        System.out.print("Enter (Leave blank to skip) >> ");
        input = scanner.nextLine();
        if (!"".equals(input)) {
            roomType.setSize(new BigDecimal(input));
        }
        do {
            System.out.println("===============================================================");
            System.out.println("Amenities       : ");
            int i = 1;
            for (String amenity : amenities) {
                System.out.println(i + ". " + amenity);
                i++;
            }
            System.out.println("===============================================================");
            System.out.println("1. Add");
            System.out.println("2. Remove");
            System.out.println("3. Update");
            System.out.println("4. Skip");
            System.out.print("Enter >> ");
            inputInt = scanner.nextInt();
            scanner.nextLine();
            if (inputInt == 4) {
                roomType.setAmenities(amenities);
                break;
            } else if (inputInt == 1) {
                do {
                    System.out.print("Enter new amenity >> ");
                    input = scanner.nextLine();
                    if(!"".equals(input)) {
                        amenities.add(input);
                        break;
                    }
                }while(true);
            } else if (inputInt == 2) {
                do {
                    inputInt = 0;
                    System.out.print("Enter amenity number >> ");
                    inputInt = scanner.nextInt();
                    if (inputInt > 0 && inputInt < amenities.size() + 1) {
                        amenities.remove(inputInt - 1);
                        break;
                    }
                } while (true);
            } else if (inputInt == 3) {
                do {
                    inputInt = 0;
                    System.out.print("Enter amenity number >> ");
                    inputInt = scanner.nextInt();
                    scanner.nextLine();
                    if (inputInt > 0 && inputInt < amenities.size() + 1) {
                        do {
                            System.out.print("Enter updated amenity >> ");
                            input = scanner.nextLine().trim();
                            if (!"".equals(input)) {
                                amenities.set(inputInt - 1, input);
                                break;
                            }
                        } while(true);
                        break;
                    }
                } while (true);
            }
        } while (true);
        do {
            System.out.println("===============================================================");
            System.out.println("RoomRates       : ");
            int i = 1;
            for (RoomRate roomRate : roomRates) {
            System.out.println(i + ". " + roomRate.getName());
                i++;
            }
            System.out.println("===============================================================");
            System.out.println("1. Add");
            System.out.println("2. Remove");
            System.out.println("3. Skip");
            System.out.print("Enter >> ");
            inputInt = scanner.nextInt();
            scanner.nextLine();
            if (inputInt == 1) {
                try {
                    List<RoomRate> newRoomRates = roomRateSessionBeanRemote.getAvailableRoomRates();
                    newRoomRates.removeAll(roomRates);
                    i = 0;
                    for(RoomRate roomRate : newRoomRates) {
                        System.out.println(++i +". "+ roomRate.getName());
                    }
                    do{
                        System.out.print("Enter Room rate number >> ");
                        inputInt = scanner.nextInt();
                        scanner.nextLine();

                        if (inputInt > 0 && inputInt < newRoomRates.size() + 1) {
                            roomRates.add(newRoomRates.get(inputInt - 1));
                            break;
                        }
                    }while (true);
                } catch (RoomRateNotFoundException ex) {
                    System.out.println("No room rate to choose!");
                    break;
                }
            } else if (inputInt == 2) {
                do {
                    System.out.print("Enter Room rate number >> ");
                    inputInt = scanner.nextInt();
                    scanner.nextLine();
                    if (inputInt > 0 && inputInt < roomRates.size() + 1) {
                        roomRates.remove(inputInt - 1);
                        break;
                    }
                } while (true);
            } else if (inputInt == 3) {
                roomType.setRoomRates(roomRates);
                break;
            }
        } while (true);
        do {
            System.out.println("===============================================================");
            System.out.println("Parent Room Type : " + (roomType.getParentRoomType() == null ? "NONE" : roomType.getParentRoomType().getName()));
            System.out.println("===============================================================");
            System.out.println("1. "+(roomType.getParentRoomType() == null ? "Add" : "Change"));
            System.out.println("2. Remove");
            System.out.println("3. Skip");
            System.out.print("Enter >> ");
            inputInt = scanner.nextInt();
            scanner.nextLine();
            if (inputInt == 1) {
                try {
                    List<RoomType> newRoomTypes = roomTypeSessionBeanRemote.getRoomTypes();
                    newRoomTypes.remove(roomType);
                    int i = 0;
                    for(RoomType RoomType : newRoomTypes) {
                        System.out.println(++i +". "+ RoomType.getName());
                    }
                    do{
                        System.out.print("Enter Room rate number >> ");
                        inputInt = scanner.nextInt();
                        scanner.nextLine();

                        if (inputInt > 0 && inputInt < newRoomTypes.size() + 1) {
                            roomType.setParentRoomType(newRoomTypes.get(inputInt - 1));
                            break;
                        }
                    }while (true);
                } catch (RoomTypeNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
            } else if (inputInt == 2) {
                roomType.setParentRoomType(null);
            } else if (inputInt == 3) {
                
                break;
            }
        } while (true);
        
        System.out.println("");
        Set<ConstraintViolation<RoomType>> errorList = this.validator.validate(roomType);
        if (errorList.isEmpty()) {
            System.out.println("");
            try {
                roomType = roomTypeSessionBeanRemote.updateRoomType(roomType);
                System.out.println("Room type with name of " + roomType.getName()+" updated! ");
            } catch (RoomTypeNameAlreadyExistException | RoomNotFoundException |RoomRateNotFoundException| RoomTypeNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            System.out.println("");
            System.out.println("===============================================================");
            System.out.println("====             Error Creating New Room Type              ====");
            errorList.forEach(x -> System.out.println(x.getPropertyPath() + " : " + x.getMessage().replace("size","input") + " length"));
            System.out.println("===============================================================");
        }
        
    }
    
    private void doDeleteRoomType(RoomType roomType) {
        try {
            if(roomTypeSessionBeanRemote.deleteRoomType(roomType)){
                System.out.println("Room type successfully deleted !");
            }else {
                System.out.println("Romm type has been disabled !");
            }
        } catch (RoomTypeNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private void doViewRoom(Room room) {
        int inputInt = 0;
        try {
            do {
                RoomType roomType = new RoomType();
                roomType.setName("NONE");
                if(room.getRoomRmType() != null) {
                    roomType = roomTypeSessionBeanRemote.getLoadedRoomType(room.getRoomRmType());
                }
                System.out.println("===============================================================");
                System.out.println("====                 Hotel Operation Module                ====");        
                System.out.println("====                       View Room                       ====");
                System.out.println("===============================================================");
                System.out.println("Room Number : " + room.getRoomNumber());
                System.out.println("Room Type   : " + roomType.getName());
                System.out.println("Room Status : " + room.getRoomStatus());
                System.out.println("Booked Dates: ");
                int i = 0;
                for(Date date : room.getBookedDates()) {
                    System.out.println(++i + ". " + date);
                }
                System.out.println("1. Update Room");
                System.out.println("2. Delete Room");
                System.out.println("3. Exit");
                System.out.print("Enter >> ");
                inputInt = scanner.nextInt();
                scanner.nextLine();
                if(inputInt == 3) {
                    break;
                } else if (inputInt == 1) {
                    doUpdateRoom(room);
                } else if (inputInt == 2) {
                    doDeleteRoom(room);
                    break;
                }
            } while(true);
        } catch (RoomTypeNotFoundException | RoomNotFoundException | RoomTypeNameAlreadyExistException | RoomRateNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
            
    }
    
    private void doUpdateRoom(Room room) {
        String input;
        int inputInt = 0;
        List<RoomType> roomTypes;
        
        System.out.println("RoomNumber: " + room.getRoomNumber());
        System.out.print("Enter (Leave blank to skip) >> ");
        input = scanner.nextLine();
        if(!"".equals(input)) {
            room.setRoomNumber(input);
        }
        do {
            RoomType roomTypeTemp = new RoomType();
            roomTypeTemp.setName("NONE");
            if(room.getRoomRmType() != null) {
                try {
                    roomTypeTemp = roomTypeSessionBeanRemote.getLoadedRoomType(room.getRoomRmType());
                } catch (RoomTypeNotFoundException | RoomTypeNameAlreadyExistException | RoomNotFoundException | RoomRateNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            System.out.println("RoomRmType: " + roomTypeTemp.getName());
            System.out.println("1. Change");
            System.out.println("2. Skip");
            System.out.print("Enter >> ");
            inputInt = scanner.nextInt();
            scanner.nextLine();
            if(inputInt == 1) {
                do {
                    System.out.println("RoomTypes: ");
                    int i = 1;
                    try {
                        roomTypes = roomTypeSessionBeanRemote.getAvailableRoomTypes();
                        for(RoomType roomType : roomTypes) {
                            System.out.println(i + ". " + roomType.getName());
                            i++;
                        }
                        System.out.print("Enter RoomType number >> ");
                        inputInt = scanner.nextInt();
                        scanner.nextLine();
                        if(inputInt > 0 && inputInt < roomTypes.size() + 1) {
                            room.setRoomRmType(roomTypes.get(inputInt - 1));
                            break;
                        }
                    } catch (RoomTypeNotFoundException ex) {
                        break;
                    }
                }while(true);
                break;
            } else if(inputInt == 2) {
                break;
            } 
        } while (true);
        do {
            System.out.println("RoomStatus: " + room.getRoomStatus());
            System.out.println("1. Available");
            System.out.println("2. Unavailable");
            System.out.println("3. Skip");
            System.out.print("Enter >> ");
            inputInt = scanner.nextInt();
            scanner.nextLine();
            if(inputInt == 1) {
                room.setRoomStatus(RoomStatusEnum.AVAILABLE);
                break;
            } else if(inputInt == 2) {
                room.setRoomStatus(RoomStatusEnum.UNAVAILABLE);
                break;
            } else if(inputInt == 3) {
                break;
            }
        } while (true);
//        List<Date> bookedDates = room.getBookedDates();
//        do {
//            
//            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//            System.out.println("BookedDates: ");
//            int i = 0;
//            for(Date date : bookedDates) {
//                System.out.println(++i + "." + date);
//            }
//            System.out.println("1. Add date");
//            System.out.println("2. Update date");
//            System.out.println("3. Remove date");
//            System.out.println("4. Skip");
//            System.out.print("Enter >> ");
//            inputInt = scanner.nextInt();
//            scanner.nextLine();
//            if(inputInt == 1) {
//                do {
//                    System.out.print("Enter date (dd-MM-yyyy) >> ");
//                    input = scanner.nextLine();
//                    if(!"".equals(input)) {
//                        try {
//                            bookedDates.add(sdf.parse(input));
//                            break;
//                        } catch (ParseException ex) {
//                            System.out.println("Wrong date format!");
//                        }
//                    }
//                } while (true);
//            } else if(inputInt == 2) {
//                do {
//                    System.out.print("Enter date number >> ");
//                    inputInt = scanner.nextInt();
//                    scanner.nextLine();
//                    if(inputInt > 0 && inputInt < bookedDates.size() + 1) {
//                        do {
//                            System.out.print("Enter new date (yyyy-MM-dd) >> ");
//                            input = scanner.nextLine().trim();
//                            if(!"".equals(input)) {
//                                try {
//                                    bookedDates.set(inputInt - 1,sdf.parse(input));
//                                    break;
//                                } catch (ParseException ex) {
//                                    System.out.println("Wrong date format!");
//                                }
//                            }
//                        } while (true);
//                        break;
//                    }
//                } while (true);
//            } else if(inputInt == 3) {
//                do {
//                    System.out.print("Enter date number >> ");
//                    inputInt = scanner.nextInt();
//                    scanner.nextLine();
//                    if(inputInt > 0 && inputInt < bookedDates.size() + 1) {
//                        bookedDates.remove(inputInt - 1);
//                        break;
//                    }
//                } while (true);
//            } else if(inputInt == 4) {
//                room.setBookedDates(bookedDates);
//                break;
//            }
//        } while (true);
        
        
        Set<ConstraintViolation<Room>> errorList = this.validator.validate(room);
        if (errorList.isEmpty()) {
            System.out.println("");
            try {
                room = roomSessionBeanRemote.updateRoom(room);
            } catch (RoomNumberAlreadyExistException | RoomNotFoundException | RoomTypeNotFoundException ex) {
                System.out.println(ex.getMessage());
            } 
        } else {
            System.out.println("");
            System.out.println("===============================================================");
            System.out.println("====             Error Creating New Room Type              ====");
            errorList.forEach(x -> System.out.println(x.getPropertyPath() + " : " + x.getMessage().replace("size","input") + " length"));
            System.out.println("===============================================================");
        }
    }

    private void doDeleteRoom(Room room) {
        try {
            roomSessionBeanRemote.deleteRoom(room);
            System.out.println("Room successfully deleted !");
        } catch (RoomNotFoundException|RoomTypeNotFoundException ex) {
            System.out.println(ex.getMessage());
        } 
    }
}
