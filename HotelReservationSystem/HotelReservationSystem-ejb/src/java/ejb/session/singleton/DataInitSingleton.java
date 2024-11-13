/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.GuestSessionBeanLocal;
import ejb.session.stateless.RoomRateSessionBeanLocal;
import ejb.session.stateless.RoomSessionBeanLocal;
import ejb.session.stateless.RoomTypeSessionBeanLocal;
import entity.Employee;
import entity.Guest;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import enumerations.EmployeeTypeEnum;
import enumerations.RoomRateTypeEnum;
import enumerations.RoomStatusEnum;
import exception.EmployeeUsernameAlreadyExistException;
import exception.GuestUsernameAlreadyExistException;
import exception.InvalidDataException;
import exception.RoomNumberAlreadyExistException;
import exception.RoomRateNameAlreadyExistException;
import exception.RoomTypeNameAlreadyExistException;
import exception.RoomTypeNotFoundException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Tan Jian Feng
 */
@Singleton
@LocalBean
@Startup
public class DataInitSingleton {

    @EJB(name = "GuestSessionBeanLocal")
    private GuestSessionBeanLocal guestSessionBeanLocal;

    @EJB(name = "RoomSessionBeanLocal")
    private RoomSessionBeanLocal roomSessionBeanLocal;

    @EJB(name = "RoomRateSessionBeanLocal")
    private RoomRateSessionBeanLocal roomRateSessionBeanLocal;

    @EJB(name = "RoomTypeSessionBeanLocal")
    private RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;

    
    @EJB(name = "EmployeeSessionBeanLocal")
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;
    
    

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    // public DataInitSingleton() {
    // }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @PostConstruct
    public void run() {
        System.out.println("\n\n=========================");
        System.out.println("DEPLOYED");
        System.out.println("=========================\n\n");

        if (em.find(Employee.class, 1l) == null) {
            try {
                employeeSessionBeanLocal.createNewEmployee(new Employee("System", "Admin", "sysadmin", "password", EmployeeTypeEnum.SYSTEM_ADMINISTRATOR));
                employeeSessionBeanLocal.createNewEmployee(new Employee("Guest", "Officer", "guestrelo", "password",EmployeeTypeEnum.GUEST_RELATION_OFFICER));
                employeeSessionBeanLocal.createNewEmployee(new Employee("Sales", "Sales", "salesmanager", "password", EmployeeTypeEnum.SALES));
                employeeSessionBeanLocal.createNewEmployee(new Employee("Operation", "Manager", "opmanager", "password",EmployeeTypeEnum.OPERATION_MANAGER));
            } catch (EmployeeUsernameAlreadyExistException ex1) {
                System.out.println(ex1);
            }
        }
        loadRoomRateType();
//        loadGuest();
    }
    public void loadRoomRateType() {
        if (em.find(RoomRate.class, 1l) == null && em.find(RoomType.class, 1l) == null && em.find(Room.class, 1l) == null ) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
//                Long longDate1 = Long.MAX_VALUE;
//                Long longDate2 = Long.MIN_VALUE;
//                Date maxDate = new Date(longDate1);
//                Date minDate = new Date(longDate2);
//                
//                maxDate = sdf.parse(df.format(maxDate));
//                minDate = sdf.parse(df.format(minDate));

                Date minDate = sdf.parse("01-01-1970");
                Date maxDate = sdf.parse("01-01-2999");
                
                System.out.println("LOADING ROOM RATE");
                
                RoomRate DeluxeRoomPublished = roomRateSessionBeanLocal.createNewRoomRate(
                        new RoomRate("Deluxe Room Published",new BigDecimal("100.00"),minDate,maxDate,RoomStatusEnum.AVAILABLE,RoomRateTypeEnum.PUBLISHED));
                RoomRate DeluxeRoomNormal = roomRateSessionBeanLocal.createNewRoomRate(
                        new RoomRate("Deluxe Room Normal",new BigDecimal("50.00"),minDate,maxDate,RoomStatusEnum.AVAILABLE,RoomRateTypeEnum.NORMAL));
                RoomRate PremierRoomPublished = roomRateSessionBeanLocal.createNewRoomRate(
                        new RoomRate("Premier Room Published",new BigDecimal("200.00"),minDate,maxDate,RoomStatusEnum.AVAILABLE,RoomRateTypeEnum.PUBLISHED));
                RoomRate PremierRoomNormal = roomRateSessionBeanLocal.createNewRoomRate(
                        new RoomRate("Premier Room Normal",new BigDecimal("100.00"),minDate,maxDate,RoomStatusEnum.AVAILABLE,RoomRateTypeEnum.NORMAL));
                RoomRate FamilyRoomPublished = roomRateSessionBeanLocal.createNewRoomRate(
                        new RoomRate("Family Room Published", new BigDecimal("300.00"),minDate,maxDate, RoomStatusEnum.AVAILABLE, RoomRateTypeEnum.PUBLISHED));
                RoomRate FamilyRoomNormal = roomRateSessionBeanLocal.createNewRoomRate(
                        new RoomRate("Family Room Normal", new BigDecimal("150.00"),minDate,maxDate, RoomStatusEnum.AVAILABLE, RoomRateTypeEnum.NORMAL));
                RoomRate JuniorSuitePublished = roomRateSessionBeanLocal.createNewRoomRate(
                        new RoomRate("Junior Suite Published", new BigDecimal("400.00"),minDate,maxDate, RoomStatusEnum.AVAILABLE, RoomRateTypeEnum.PUBLISHED));
                RoomRate JuniorSuiteNormal = roomRateSessionBeanLocal.createNewRoomRate(
                        new RoomRate("Junior Suite Normal", new BigDecimal("200.00"),minDate,maxDate, RoomStatusEnum.AVAILABLE, RoomRateTypeEnum.NORMAL));
                RoomRate GrandSuitePublished = roomRateSessionBeanLocal.createNewRoomRate(
                        new RoomRate("Grand Suite Published", new BigDecimal("500.00"),minDate,maxDate, RoomStatusEnum.AVAILABLE, RoomRateTypeEnum.PUBLISHED));
                RoomRate GrandSuiteNormal = roomRateSessionBeanLocal.createNewRoomRate(
                        new RoomRate("Grand Suite Normal", new BigDecimal("250.00"),minDate,maxDate, RoomStatusEnum.AVAILABLE, RoomRateTypeEnum.NORMAL));

                List<String> anemities = new ArrayList();
                anemities.add("SOAP");
                anemities.add("SHAMPOO");
                anemities.add("TOWEL");
                
                List<RoomRate> deluxeRoomRoomRates = new ArrayList();
                deluxeRoomRoomRates.add(DeluxeRoomPublished);
                deluxeRoomRoomRates.add(DeluxeRoomNormal);
                List<RoomRate> premierRoomRoomRates = new ArrayList();
                premierRoomRoomRates.add(PremierRoomPublished);
                premierRoomRoomRates.add(PremierRoomNormal);
                List<RoomRate> familyRoomRoomRates = new ArrayList();
                familyRoomRoomRates.add(FamilyRoomPublished);
                familyRoomRoomRates.add(FamilyRoomNormal);
                List<RoomRate> juniorSuiteRoomRates = new ArrayList();
                juniorSuiteRoomRates.add(JuniorSuitePublished);
                juniorSuiteRoomRates.add(JuniorSuiteNormal);
                List<RoomRate> grandSuiteRoomRates = new ArrayList();
                grandSuiteRoomRates.add(GrandSuitePublished);
                grandSuiteRoomRates.add(GrandSuiteNormal);
                
                
                RoomType roomTypeGrandSuite = roomTypeSessionBeanLocal.createNewRoomType(
                        new RoomType("Grand Suite","Grand Suite",new BigDecimal("240"),"Grand Suite",new BigDecimal("2"),anemities,RoomStatusEnum.AVAILABLE,new ArrayList(),grandSuiteRoomRates,null));
                RoomType roomTypeJuniorSuite = roomTypeSessionBeanLocal.createNewRoomType(
                        new RoomType("Junior Suite","Junior Suite",new BigDecimal("120"),"Junior Suite",new BigDecimal("1"),anemities,RoomStatusEnum.AVAILABLE,new ArrayList(),juniorSuiteRoomRates,roomTypeGrandSuite));
                RoomType roomTypeFamilyRoom = roomTypeSessionBeanLocal.createNewRoomType(
                        new RoomType("Family Room","Family Room",new BigDecimal("240"),"Family Room",new BigDecimal("2"),anemities,RoomStatusEnum.AVAILABLE,new ArrayList(),familyRoomRoomRates,roomTypeJuniorSuite));
                RoomType roomTypePremierRoom = roomTypeSessionBeanLocal.createNewRoomType(
                        new RoomType("Premier Room","Premier Room",new BigDecimal("240"),"Premier Room",new BigDecimal("2"),anemities,RoomStatusEnum.AVAILABLE,new ArrayList(),premierRoomRoomRates,roomTypeFamilyRoom));
                RoomType roomTypeDeluxeRoom = roomTypeSessionBeanLocal.createNewRoomType(
                        new RoomType("Deluxe Room","Deluxe Room",new BigDecimal("240"),"Deluxe Room",new BigDecimal("2"),anemities,RoomStatusEnum.AVAILABLE,new ArrayList(),deluxeRoomRoomRates,roomTypePremierRoom));
                
                Room room0101 = roomSessionBeanLocal.createNewRoom(
                        new Room("0101",new ArrayList(),RoomStatusEnum.AVAILABLE,false,roomTypeDeluxeRoom));
                Room room0201 = roomSessionBeanLocal.createNewRoom(
                        new Room("0201",new ArrayList(),RoomStatusEnum.AVAILABLE,false,roomTypeDeluxeRoom));
                Room room0301 = roomSessionBeanLocal.createNewRoom(
                        new Room("0301",new ArrayList(),RoomStatusEnum.AVAILABLE,false,roomTypeDeluxeRoom));
                Room room0401 = roomSessionBeanLocal.createNewRoom(
                        new Room("0401",new ArrayList(),RoomStatusEnum.AVAILABLE,false,roomTypeDeluxeRoom));
                Room room0501 = roomSessionBeanLocal.createNewRoom(
                        new Room("0501",new ArrayList(),RoomStatusEnum.AVAILABLE,false,roomTypeDeluxeRoom));
                
                Room room0102 = roomSessionBeanLocal.createNewRoom(
                        new Room("0102",new ArrayList(),RoomStatusEnum.AVAILABLE,false,roomTypePremierRoom));
                Room room0202 = roomSessionBeanLocal.createNewRoom(
                        new Room("0202",new ArrayList(),RoomStatusEnum.AVAILABLE,false,roomTypePremierRoom));
                Room room0302 = roomSessionBeanLocal.createNewRoom(
                        new Room("0302",new ArrayList(),RoomStatusEnum.AVAILABLE,false,roomTypePremierRoom));
                Room room0402 = roomSessionBeanLocal.createNewRoom(
                        new Room("0402",new ArrayList(),RoomStatusEnum.AVAILABLE,false,roomTypePremierRoom));
                Room room0502 = roomSessionBeanLocal.createNewRoom(
                        new Room("0502",new ArrayList(),RoomStatusEnum.AVAILABLE,false,roomTypePremierRoom));
                
                Room room0103 = roomSessionBeanLocal.createNewRoom(
                        new Room("0103",new ArrayList(),RoomStatusEnum.AVAILABLE,false,roomTypeFamilyRoom));
                Room room0203 = roomSessionBeanLocal.createNewRoom(
                        new Room("0203",new ArrayList(),RoomStatusEnum.AVAILABLE,false,roomTypeFamilyRoom));
                Room room0303 = roomSessionBeanLocal.createNewRoom(
                        new Room("0303",new ArrayList(),RoomStatusEnum.AVAILABLE,false,roomTypeFamilyRoom));
                Room room0403 = roomSessionBeanLocal.createNewRoom(
                        new Room("0403",new ArrayList(),RoomStatusEnum.AVAILABLE,false,roomTypeFamilyRoom));
                Room room0503 = roomSessionBeanLocal.createNewRoom(
                        new Room("0503",new ArrayList(),RoomStatusEnum.AVAILABLE,false,roomTypeFamilyRoom));
                
                Room room0104 = roomSessionBeanLocal.createNewRoom(
                        new Room("0104",new ArrayList(),RoomStatusEnum.AVAILABLE,false,roomTypeJuniorSuite));
                Room room0204 = roomSessionBeanLocal.createNewRoom(
                        new Room("0204",new ArrayList(),RoomStatusEnum.AVAILABLE,false,roomTypeJuniorSuite));
                Room room0304 = roomSessionBeanLocal.createNewRoom(
                        new Room("0304",new ArrayList(),RoomStatusEnum.AVAILABLE,false,roomTypeJuniorSuite));
                Room room0404 = roomSessionBeanLocal.createNewRoom(
                        new Room("0404",new ArrayList(),RoomStatusEnum.AVAILABLE,false,roomTypeJuniorSuite));
                Room room0504 = roomSessionBeanLocal.createNewRoom(
                        new Room("0504",new ArrayList(),RoomStatusEnum.AVAILABLE,false,roomTypeJuniorSuite));
                
                Room room0105 = roomSessionBeanLocal.createNewRoom(
                        new Room("0105",new ArrayList(),RoomStatusEnum.AVAILABLE,false,roomTypeGrandSuite));
                Room room0205 = roomSessionBeanLocal.createNewRoom(
                        new Room("0205",new ArrayList(),RoomStatusEnum.AVAILABLE,false,roomTypeGrandSuite));
                Room room0305 = roomSessionBeanLocal.createNewRoom(
                        new Room("0305",new ArrayList(),RoomStatusEnum.AVAILABLE,false,roomTypeGrandSuite));
                Room room0405 = roomSessionBeanLocal.createNewRoom(
                        new Room("0405",new ArrayList(),RoomStatusEnum.AVAILABLE,false,roomTypeGrandSuite));
                Room room0505 = roomSessionBeanLocal.createNewRoom(
                        new Room("0505",new ArrayList(),RoomStatusEnum.AVAILABLE,false,roomTypeGrandSuite));
               
            } catch (RoomRateNameAlreadyExistException | RoomTypeNameAlreadyExistException | RoomNumberAlreadyExistException | RoomTypeNotFoundException ex) {
                Logger.getLogger(DataInitSingleton.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(DataInitSingleton.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void loadRoomRateTypeOld() {
        if (em.find(RoomRate.class, 1l) == null && em.find(RoomType.class, 1l) == null && em.find(Room.class, 1l) == null ) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                
                System.out.println("LOADING ROOM RATE");
                RoomRate roomRate1 = roomRateSessionBeanLocal.createNewRoomRate(
                        new RoomRate("WALKIN-SINGLE",new BigDecimal("100.00"),sdf.parse("10-10-2020"),sdf.parse("10-10-2030"),RoomStatusEnum.AVAILABLE,RoomRateTypeEnum.PUBLISHED));
                RoomRate roomRate2 = roomRateSessionBeanLocal.createNewRoomRate(
                        new RoomRate("NORMAL-SINGLE",new BigDecimal("100.00"),sdf.parse("10-10-2025"),sdf.parse("10-10-2025"),RoomStatusEnum.AVAILABLE,RoomRateTypeEnum.NORMAL));
                RoomRate roomRate3 = roomRateSessionBeanLocal.createNewRoomRate(
                        new RoomRate("PEAK-SINGLE",new BigDecimal("200.00"),sdf.parse("01-12-2025"),sdf.parse("01-12-2025"),RoomStatusEnum.AVAILABLE,RoomRateTypeEnum.PEAK));
                RoomRate roomRate4 = roomRateSessionBeanLocal.createNewRoomRate(
                        new RoomRate("PROMOTION-SINGLE",new BigDecimal("50.00"),sdf.parse("10-10-2025"),sdf.parse("10-10-2025"),RoomStatusEnum.AVAILABLE,RoomRateTypeEnum.PROMOTION));
                RoomRate roomRate5 = roomRateSessionBeanLocal.createNewRoomRate(
                        new RoomRate("WALKIN-DOUBLE", new BigDecimal("200.00"), sdf.parse("10-10-2020"), sdf.parse("10-10-2030"), RoomStatusEnum.AVAILABLE, RoomRateTypeEnum.PUBLISHED));
                RoomRate roomRate6 = roomRateSessionBeanLocal.createNewRoomRate(
                        new RoomRate("NORMAL-DOUBLE", new BigDecimal("200.00"), sdf.parse("10-10-2020"), sdf.parse("10-10-2030"), RoomStatusEnum.AVAILABLE, RoomRateTypeEnum.NORMAL));
                RoomRate roomRate7 = roomRateSessionBeanLocal.createNewRoomRate(
                        new RoomRate("PEAK-DOUBLE", new BigDecimal("400.00"),sdf.parse("01-12-2025"),sdf.parse("01-12-2025"), RoomStatusEnum.AVAILABLE, RoomRateTypeEnum.PEAK));
                RoomRate roomRate8 = roomRateSessionBeanLocal.createNewRoomRate(
                        new RoomRate("PROMOTION-DOUBLE", new BigDecimal("100.00"),sdf.parse("10-10-2025"),sdf.parse("10-10-2025"), RoomStatusEnum.AVAILABLE, RoomRateTypeEnum.PROMOTION));
                
                List<String> anemities = new ArrayList();
                anemities.add("SOAP");
                anemities.add("SHAMPOO");
                anemities.add("TOWEL");
                List<RoomRate> roomRateSingle = new ArrayList();
                roomRateSingle.add(roomRate1);
                roomRateSingle.add(roomRate2);
                roomRateSingle.add(roomRate3);
                roomRateSingle.add(roomRate4);
                List<RoomRate> roomRateDouble = new ArrayList();
                roomRateDouble.add(roomRate5);
                roomRateDouble.add(roomRate6);
                roomRateDouble.add(roomRate7);
                roomRateDouble.add(roomRate8);

                RoomType roomTypeDouble = roomTypeSessionBeanLocal.createNewRoomType(
                        new RoomType("DOUBLE","DOUBLE",new BigDecimal("240"),"DOUBLE",new BigDecimal("2"),anemities,RoomStatusEnum.AVAILABLE,new ArrayList(),roomRateDouble,null));
                RoomType roomTypeSingle = roomTypeSessionBeanLocal.createNewRoomType(
                        new RoomType("SINGLE","SINGLE",new BigDecimal("120"),"SINGLE",new BigDecimal("1"),anemities,RoomStatusEnum.AVAILABLE,new ArrayList(),roomRateSingle,roomTypeDouble));
                
                Room room0101 = roomSessionBeanLocal.createNewRoom(
                        new Room("0101",new ArrayList(),RoomStatusEnum.AVAILABLE,false,roomTypeSingle));
                Room room0102 = roomSessionBeanLocal.createNewRoom(
                        new Room("0102",new ArrayList(),RoomStatusEnum.AVAILABLE,false,roomTypeSingle));
                Room room0103 = roomSessionBeanLocal.createNewRoom(
                        new Room("0103",new ArrayList(),RoomStatusEnum.AVAILABLE,false,roomTypeSingle));
                Room room0104 = roomSessionBeanLocal.createNewRoom(
                        new Room("0104",new ArrayList(),RoomStatusEnum.AVAILABLE,false,roomTypeSingle));
                Room room0201 = roomSessionBeanLocal.createNewRoom(
                        new Room("0201",new ArrayList(),RoomStatusEnum.AVAILABLE,false,roomTypeDouble));
                Room room0202 = roomSessionBeanLocal.createNewRoom(
                        new Room("0202",new ArrayList(),RoomStatusEnum.AVAILABLE,false,roomTypeDouble));
                Room room0203 = roomSessionBeanLocal.createNewRoom(
                        new Room("0203",new ArrayList(),RoomStatusEnum.AVAILABLE,false,roomTypeDouble));
                Room room0204 = roomSessionBeanLocal.createNewRoom(
                        new Room("0204",new ArrayList(),RoomStatusEnum.AVAILABLE,false,roomTypeDouble));
                
               
            } catch (RoomRateNameAlreadyExistException | RoomTypeNameAlreadyExistException | RoomNumberAlreadyExistException | RoomTypeNotFoundException | ParseException ex) {
                Logger.getLogger(DataInitSingleton.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    public void loadGuest(){
//        if (em.find(Guest.class, 1l) == null ) {
//            try {
//                guestSessionBeanLocal.registerGuest(
//                    new Guest("Guest1","guest1","password","T01234561A",new ArrayList<>()));
//                guestSessionBeanLocal.registerGuest(
//                    new Guest("Guest2","guest2","password","T01234562A",new ArrayList<>()));
//                guestSessionBeanLocal.registerGuest(
//                    new Guest("Guest3","guest3","password","T01234563A",new ArrayList<>()));
//                guestSessionBeanLocal.registerGuest(
//                    new Guest("Guest4","guest4","password","T01234564A",new ArrayList<>()));
//            } catch (InvalidDataException ex) {
//                Logger.getLogger(DataInitSingleton.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (GuestUsernameAlreadyExistException ex) {
//                Logger.getLogger(DataInitSingleton.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
    }
    
    
    
}
