/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hotelreservationsystemmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import ejb.session.stateless.SearchRoomSessionBeanRemote;

import javax.ejb.EJB;
import ejb.session.singleton.AllocationSingletonRemote;
import ejb.session.stateless.AllocationReportSessionBeanRemote;

/**
 *
 * @author Tan Jian Feng
 */
public class Main {

    @EJB(name = "AllocationReportSessionBeanRemote")
    private static AllocationReportSessionBeanRemote allocationReportSessionBeanRemote;

    @EJB(name = "AllocationSingletonRemote")
    private static AllocationSingletonRemote allocationSingletonRemote;
    
    @EJB(name = "ReservationSessionBeanRemote")
    private static ReservationSessionBeanRemote reservationSessionBeanRemote;
    
    @EJB(name = "GuestSessionBeanRemote")
    private static GuestSessionBeanRemote guestSessionBeanRemote;

    @EJB(name = "SearchRoomSessionBeanRemote")
    private static SearchRoomSessionBeanRemote searchRoomSessionBeanRemote;

    @EJB(name = "RoomRateSessionBeanRemote")
    private static RoomRateSessionBeanRemote roomRateSessionBeanRemote;

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
                roomTypeSessionBeanRemote,
                roomRateSessionBeanRemote,
                reservationSessionBeanRemote,
                guestSessionBeanRemote,
                searchRoomSessionBeanRemote,
                allocationSingletonRemote,
                allocationReportSessionBeanRemote
        );
        try{
            app.run();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
}
