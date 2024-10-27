/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
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
    private Employee currentEmployee;
    private Scanner scanner = new Scanner(System.in);

    public HotelOperationModule(RoomSessionBeanRemote roomSessionBeanRemote, PartnerSessionBeanRemote partnerSessionBeanRemote, EmployeeSessionBeanRemote employeeSessionBeanRemote, Employee currentEmployee) {
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }
    
    public void run() {
        
        if (EmployeeTypeEnum.OPERATION_MANAGER.equals(currentEmployee.getEmployeeType())) {
            OperationManagerModule app = new OperationManagerModule(
                roomSessionBeanRemote,
                partnerSessionBeanRemote,
                employeeSessionBeanRemote,
                currentEmployee
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
        }
        
    }
    
}
