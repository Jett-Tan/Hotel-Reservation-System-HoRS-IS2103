/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hotelreservationsystemmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author Tan Jian Feng
 */
public class Main {

    @EJB(name = "RoomTypeSessionBeanRemote")
    private static RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;

    @EJB(name = "RoomSessionBeanRemote")
    private static RoomSessionBeanRemote roomSessionBeanRemote;

    @EJB(name = "PartnerSessionBeanRemote")
    private static PartnerSessionBeanRemote partnerSessionBeanRemote;

    @EJB(name = "EmployeeSessionBeanRemote")
    private static EmployeeSessionBeanRemote employeeSessionBeanRemote;
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MainApp app = new MainApp(
                roomSessionBeanRemote,
                partnerSessionBeanRemote,
                employeeSessionBeanRemote,
                roomTypeSessionBeanRemote
        );
        try{
            app.run();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
}
