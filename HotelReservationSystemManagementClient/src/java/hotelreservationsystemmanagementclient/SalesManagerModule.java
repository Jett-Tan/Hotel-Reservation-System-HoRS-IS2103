/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import entity.Employee;
import entity.RoomRate;
import enumerations.RoomRateTypeEnum;
import enumerations.RoomStatusEnum;
import exception.RoomRateNameAlreadyExistException;
import exception.RoomRateNotFoundException;
import exception.RoomTypeNotFoundException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author Tan Jian Feng
 */
public class SalesManagerModule {

    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    private Employee currentEmployee;
    private Scanner scanner = new Scanner(System.in);
    
    private ValidatorFactory validatorFactory;
    private Validator validator;

    public SalesManagerModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    public SalesManagerModule(
            EmployeeSessionBeanRemote employeeSessionBeanRemote, 
            Employee currentEmployee,
            RoomRateSessionBeanRemote roomRateSessionBeanRemote
    ) {
        this();
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.currentEmployee = currentEmployee;
        this.roomRateSessionBeanRemote = roomRateSessionBeanRemote;
    }

    void run() {
        int input = 0;
        do {
            System.out.println("===============================================================");
            System.out.println("==== Welcome to Hotel Reservation System Management Client ====");
            System.out.println("====                  Sales Manager Module                 ====");
            System.out.println("===============================================================");
            System.out.println("1. Create new Room Rate");
            System.out.println("2. View all room rates");
            System.out.println("3. Exit");
            System.out.print("Enter >> ");
            input = scanner.nextInt();
            scanner.nextLine();
            if (input == 3) {
                break;
            }
            switch(input) {
                case 1: {
                    doCreateNewRoomRate();
                    break;
                }
                case 2: {
                    doViewAllRoomRates();
                    break;
                }
                default : {
                    System.out.println("\nInvalid input");
                    break;
                }
            }
        } while (true);
    }

    private void doCreateNewRoomRate() {
        System.out.println("===============================================================");
        System.out.println("====              System Administration Module             ====");        
        System.out.println("====                 Create New Room Rate                  ====");
        System.out.println("===============================================================");
        RoomRate newRoomRate = new RoomRate();
        String name;
        BigDecimal rate;
        Date startDate = new Date(Long.MIN_VALUE);
        Date endDate = new Date(Long.MAX_VALUE);
        RoomStatusEnum rateStatus;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        System.out.print("Enter name >> ");
        newRoomRate.setName(scanner.nextLine());
        System.out.print("Enter rate >> ");    
        newRoomRate.setRate(new BigDecimal(scanner.nextLine().trim()));
        
        int option = 0;
        do{
            System.out.println("Select Rate Status");
            System.out.println("1. Available");
            System.out.println("2. Unavailable");
            System.out.print("Enter room status (1 - 2) >> ");
            option = scanner.nextInt();
            scanner.nextLine();
            if(option == 1) {
                newRoomRate.setRateStatus(RoomStatusEnum.AVAILABLE);
            } else if (option == 2) {
                newRoomRate.setRateStatus(RoomStatusEnum.UNAVAILABLE);
            }
        }while (option > 2 || option < 1);
        do{
            System.out.println("Select Rate Type");
            System.out.println("1. Published");
            System.out.println("2. Normal");
            System.out.println("3. Peak");
            System.out.println("4. Promotion");
            System.out.print("Enter >> ");
            option = scanner.nextInt();
            scanner.nextLine();
            if(option == 1) {
                newRoomRate.setRoomRateType(RoomRateTypeEnum.PUBLISHED);
            } else if (option == 2) {
                newRoomRate.setRoomRateType(RoomRateTypeEnum.NORMAL);
            } else if (option == 3) {
                newRoomRate.setRoomRateType(RoomRateTypeEnum.PEAK);
            } else if (option == 4) {
                newRoomRate.setRoomRateType(RoomRateTypeEnum.PROMOTION);
            }
        }while(option > 4 || option < 1);
        if(newRoomRate.getRoomRateType().equals(RoomRateTypeEnum.PROMOTION) || newRoomRate.getRoomRateType().equals(RoomRateTypeEnum.PEAK) ) {
            do {
                System.out.print("Enter start date (dd-mm-yyyy)>> ");
                try {
                    newRoomRate.setStartDate(sdf.parse(scanner.nextLine().trim()));
                    break;
                } catch (ParseException ex) {
                    System.out.println("Wrong date format");
                }            
            } while(true);
            do {
                System.out.print("Enter end date (dd-mm-yyyy)>> ");  
                try {
                    newRoomRate.setEndDate(sdf.parse(scanner.nextLine().trim()));
                    break;
                } catch (ParseException ex) {
                    System.out.println("Wrong date format!");
                }
            } while(true);
        } else {
            newRoomRate.setStartDate(startDate);
            newRoomRate.setEndDate(endDate);
        }
        Set<ConstraintViolation<RoomRate>> errorList = this.validator.validate(newRoomRate);
        if (errorList.isEmpty()) {
            System.out.println("");
            try {
                newRoomRate = roomRateSessionBeanRemote.createNewRoomRate(newRoomRate);
                System.out.println("New Room Rate created with name of " + newRoomRate.getName());
            } catch (RoomRateNameAlreadyExistException ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            System.out.println("");
            System.out.println("===============================================================");
            System.out.println("====             Error Creating New Room Rate              ====");
            errorList.forEach(x -> System.out.println(x.getPropertyPath() + " : " + x.getMessage().replace("size","input") + " length"));
            System.out.println("===============================================================");
        }
    }

    private void doViewAllRoomRates() {
        System.out.println("===============================================================");
        System.out.println("====                  Sales Manager Module                 ====");        
        System.out.println("====                  View All Room Rates                  ====");
        System.out.println("===============================================================");
         int EXITVALUE = 2;
        try {
            int option = 0;
            List<RoomRate> roomRates = roomRateSessionBeanRemote.getRoomRates();
            System.out.println(String.format("%s.%20s","No","Name"));
            do{
                for(int i = 1; i < roomRates.size () + 1; i++) {
                    RoomRate roomRate = roomRates.get(i - 1);
                    System.out.println(String.format("%d.%21s",i,roomRate.getName()));
                }
                System.out.println("===============================================================");
                if(roomRates.size() < 1) {
                    EXITVALUE = 1;
                    System.out.println("1. Exit");
                }
                if(roomRates.size() > 0) {
                    System.out.println("1. View room rate details");
                    System.out.println("2. Exit");
                }
                System.out.print("Enter >> ");
                option = scanner.nextInt();
                scanner.nextLine();
                if(option == EXITVALUE) {
                    break;
                }
                if(option > 0 && option < EXITVALUE) {
                    int id = 0;
                    do{
                        System.out.print("Enter room id >> ");
                        id = scanner.nextInt();
                        scanner.nextLine();
                    }while(id > roomRates.size() + 1 || id < 1);
                    RoomRate roomRate = roomRates.get(id - 1);
                    switch(option) {
                        case 1: 
                            doViewRoomRateDetail(roomRate);
                            break;
                    }
                }
            }while(!(option <= roomRates.size() + 1));
        } catch (RoomRateNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        
    }

    private void doViewRoomRateDetail(RoomRate roomRate) {
        int input = 0;
        final int EXITVALUE = 3;
        do {
            System.out.println("===============================================================");
            System.out.println("====                  Sales Manager Module                 ====");        
            System.out.println("====                 View Room Rate Detials                ====");
            System.out.println("===============================================================");    
            System.out.println("Name       : " + roomRate.getName());
            System.out.println("Rate       : " + roomRate.getRate());
            System.out.println("RateStatus : " + roomRate.getRateStatus());
            System.out.println("StartDate  : " + roomRate.getStartDate());
            System.out.println("EndDate    : " + roomRate.getEndDate());
            System.out.println("==============================================================="); 

            System.out.println("1. Update room rate details");
            System.out.println("2. Delete room rate");
            System.out.println("3. Exit");
            System.out.print("Enter >> ");
            input = scanner.nextInt();
            scanner.nextLine();
            
            if (input == 1) {
                doUpdateRoomRateDetail(roomRate);
            } else if (input == 2){
                doDeleteRoomRate(roomRate);
                break;
            } else if(input == EXITVALUE) {
                break;
            }
        } while (input > 0|| input < EXITVALUE);
    }

    private void doUpdateRoomRateDetail(RoomRate roomRate) {
        System.out.println("===============================================================");
        System.out.println("====                  Sales Manager Module                 ====");        
        System.out.println("====                Update Room Rate Detials               ====");
        System.out.println("===============================================================");
        String input = "";
        int inputInt = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        
        System.out.println("Name       : " + roomRate.getName());
        System.out.print("Enter (Leave black to skip) >> ");
        input = scanner.nextLine();
        if(!"".equals(input)) {
            roomRate.setName(input);
        }
        System.out.println("Rate       : " + roomRate.getRate());
        System.out.print("Enter (Leave black to skip) >> ");
        input = scanner.nextLine();
        if(!"".equals(input)) {
            roomRate.setRate(new BigDecimal(input));
        }
        do {
            System.out.println("RateStatus : " + roomRate.getRateStatus());
            System.out.println("1. Available");
            System.out.println("2. Unavailable");
            System.out.println("3. Skip");
            System.out.print("Enter (1 - 3) >> ");
            inputInt = scanner.nextInt();
            scanner.nextLine();
            if(inputInt == 1) {
                roomRate.setRateStatus(RoomStatusEnum.AVAILABLE);
                break;
            } else if(inputInt == 2) {
                roomRate.setRateStatus(RoomStatusEnum.UNAVAILABLE);
                break;
            } else if(inputInt == 3) {
                break;
            }
        } while(true);
        System.out.println("StartDate  : " + roomRate.getStartDate());
        System.out.print("Enter startDate (dd-mm-yyyy) (Leave blank to skip) >> ");
        input = scanner.nextLine().trim();
        if(!"".equals(input)) {
            try {
                roomRate.setStartDate(sdf.parse(input));
            } catch (ParseException ex) {
                System.out.println("Wrong date format");
                return;
            }
        }
        System.out.println("EndDate    : " + roomRate.getEndDate());
        System.out.print("Enter (dd-mm-yyyy) (Leave blank to skip) >> ");
        input = scanner.nextLine().trim();
        if(!"".equals(input)) {
            try {
                roomRate.setEndDate(sdf.parse(input));
            } catch (ParseException ex) {
                System.out.println("Wrong date format");
                return;
            }
        }
        
        // input validation
        Set<ConstraintViolation<RoomRate>> errorList = this.validator.validate(roomRate);
        if (errorList.isEmpty()) {
            System.out.println("");
            try {
                roomRateSessionBeanRemote.updateRoomRate(roomRate);
                System.out.println("Room rate updated!");
            } catch (RoomRateNotFoundException | RoomRateNameAlreadyExistException ex) {
                System.out.println(ex.getMessage());
            } 
        } else {
            System.out.println("");
            System.out.println("===============================================================");
            System.out.println("====             Error Creating New Room Rate              ====");
            errorList.forEach(x -> System.out.println(x.getPropertyPath() + " : " + x.getMessage().replace("size","input") + " length"));
            System.out.println("===============================================================");
        }
        
    }

    private void doDeleteRoomRate(RoomRate roomRate) {
        try{
            boolean isDeleted = roomRateSessionBeanRemote.deleteRoomRate(roomRate);
            if(isDeleted) {
                System.out.println("Room rate deleted !");
            }else {
                System.out.println("Room rate disabled !");
            }
        } catch (RoomRateNotFoundException | RoomTypeNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
}
