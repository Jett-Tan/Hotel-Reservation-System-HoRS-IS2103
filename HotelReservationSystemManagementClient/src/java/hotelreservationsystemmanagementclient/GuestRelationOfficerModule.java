package hotelreservationsystemmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Tan Jian Feng
 */
public class GuestRelationOfficerModule {
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    private Employee currentEmployee;
    private Scanner scanner = new Scanner(System.in);
    
    private ValidatorFactory validatorFactory;
    private Validator validator;

    public GuestRelationOfficerModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    public GuestRelationOfficerModule(
            EmployeeSessionBeanRemote employeeSessionBeanRemote, 
            Employee currentEmployee,
            RoomRateSessionBeanRemote roomRateSessionBeanRemote
    ) {
        this();
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.currentEmployee = currentEmployee;
        this.roomRateSessionBeanRemote = roomRateSessionBeanRemote;
    }
}
