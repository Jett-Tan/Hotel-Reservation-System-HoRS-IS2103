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
import java.util.Scanner;

/**
 *
 * @author Tan Jian Feng
 */
public class HotelOperationModule {
    private RoomSessionBeanRemote roomSessionBeanRemote;
    private PartnerSessionBeanRemote partnerSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    private Employee currentEmployee;
    private Scanner scanner = new Scanner(System.in);

    public HotelOperationModule(
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
        
        if (EmployeeTypeEnum.OPERATION_MANAGER.equals(currentEmployee.getEmployeeType())) {
            OperationManagerModule app = new OperationManagerModule(
                roomSessionBeanRemote,
                partnerSessionBeanRemote,
                employeeSessionBeanRemote,
                currentEmployee,
                    roomTypeSessionBeanRemote
            );
            app.run();
        } else if(EmployeeTypeEnum.SALES.equals(currentEmployee.getEmployeeType())) {
            SalesManagerModule app = new SalesManagerModule(
                roomSessionBeanRemote,
                partnerSessionBeanRemote,
                employeeSessionBeanRemote,
                currentEmployee
            );
            app.run();
        } else if(EmployeeTypeEnum.GUEST_RELATION_OFFICER.equals(currentEmployee.getEmployeeType())) {
            
        }
        
    }
    
}
