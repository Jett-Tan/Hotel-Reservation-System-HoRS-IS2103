/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hotelreservationsystemclient;


/**
 * Holiday.com
 * @author Tan Jian Feng
 */
public class HotelReservationSystemClient {

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        // TODO code application logic here
        HotelReservationWebService_Service service = new HotelReservationWebService_Service();
        HotelReservationWebService hotelReservationWebService = service.getHotelReservationWebServicePort();
       
        MainApp app = new MainApp(hotelReservationWebService);
        app.run();
    }
    
}
