/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
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
    private RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    private Employee currentEmployee;
    private Scanner scanner = new Scanner(System.in);

    public HotelOperationModule(
            RoomSessionBeanRemote roomSessionBeanRemote, 
            PartnerSessionBeanRemote partnerSessionBeanRemote, 
            EmployeeSessionBeanRemote employeeSessionBeanRemote, 
            Employee currentEmployee, 
            RoomTypeSessionBeanRemote roomTypeSessionBeanRemote,
            RoomRateSessionBeanRemote roomRateSessionBeanRemote
    ) {
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.currentEmployee = currentEmployee;
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
        this.roomRateSessionBeanRemote = roomRateSessionBeanRemote;
    }
    
    public void run() {
        
        if (null != currentEmployee.getEmployeeType()) {
            switch (currentEmployee.getEmployeeType()) {
                case OPERATION_MANAGER:{
                    OperationManagerModule app = new OperationManagerModule(
                            roomSessionBeanRemote,
                            partnerSessionBeanRemote,
                            employeeSessionBeanRemote,
                            currentEmployee,
                            roomTypeSessionBeanRemote,
                            roomRateSessionBeanRemote
                    );      app.run();
                        break;
                    }
                case SALES:{
                    SalesManagerModule app = new SalesManagerModule(
                            employeeSessionBeanRemote,
                            currentEmployee,
                            roomRateSessionBeanRemote
                    );      app.run();
                        break;
                    }
                case GUEST_RELATION_OFFICER:
                    break;
                default:
                    break;
            }
        }
    }
    
}
