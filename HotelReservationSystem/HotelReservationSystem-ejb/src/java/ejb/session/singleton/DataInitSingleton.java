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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
                employeeSessionBeanLocal.createNewEmployee(
                        new Employee("System", "Admin", "admin", "password", EmployeeTypeEnum.SYSTEM_ADMINISTRATOR));
                employeeSessionBeanLocal.createNewEmployee(new Employee("Guest", "Officer", "officer", "password",
                        EmployeeTypeEnum.GUEST_RELATION_OFFICER));
                employeeSessionBeanLocal
                        .createNewEmployee(new Employee("Sales", "Sales", "sales", "password", EmployeeTypeEnum.SALES));
                employeeSessionBeanLocal.createNewEmployee(new Employee("Operation", "Manager", "manager", "password",
                        EmployeeTypeEnum.OPERATION_MANAGER));
            } catch (EmployeeUsernameAlreadyExistException ex1) {
                System.out.println(ex1);
            }
        }
        loadRoomRateType();
        loadGuest();
    }
    public void loadRoomRateType() {
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
            } catch (ParseException | RoomRateNameAlreadyExistException | RoomTypeNameAlreadyExistException | RoomNumberAlreadyExistException | RoomTypeNotFoundException ex) {
                Logger.getLogger(DataInitSingleton.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    public void loadGuest(){
        if (em.find(Guest.class, 1l) == null ) {
            try {
                guestSessionBeanLocal.registerGuest(
                    new Guest("Guest1","guest1","password","T01234561A",new ArrayList<>()));
                guestSessionBeanLocal.registerGuest(
                    new Guest("Guest2","guest2","password","T01234562A",new ArrayList<>()));
                guestSessionBeanLocal.registerGuest(
                    new Guest("Guest3","guest3","password","T01234563A",new ArrayList<>()));
                guestSessionBeanLocal.registerGuest(
                    new Guest("Guest4","guest4","password","T01234564A",new ArrayList<>()));
            } catch (InvalidDataException ex) {
                Logger.getLogger(DataInitSingleton.class.getName()).log(Level.SEVERE, null, ex);
            } catch (GuestUsernameAlreadyExistException ex) {
                Logger.getLogger(DataInitSingleton.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
