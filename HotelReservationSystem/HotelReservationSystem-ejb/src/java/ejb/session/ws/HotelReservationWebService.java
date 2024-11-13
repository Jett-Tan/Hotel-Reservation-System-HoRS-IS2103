/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/EjbWebService.java to edit this template
 */
package ejb.session.ws;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ejb.session.stateless.GuestSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.ReservationSessionBeanLocal;
import ejb.session.stateless.RoomRateSessionBeanLocal;
import ejb.session.stateless.RoomSessionBeanLocal;
import ejb.session.stateless.RoomTypeSessionBeanLocal;
import ejb.session.stateless.SearchRoomSessionBeanLocal;
import entity.Guest;
import entity.Partner;
import entity.Reservation;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import exception.GuestNotFoundException;
import exception.InvalidLoginCredentialsException;
import exception.PartnerNotFoundException;
import exception.ReservationNotFoundException;
import exception.RoomNotFoundException;
import exception.RoomNumberAlreadyExistException;
import exception.RoomRateNotFoundException;
import exception.RoomTypeNotFoundException;

/**
 *
 * @author Tan Jian Feng
 */
@WebService(serviceName = "HotelReservationWebService")
@Stateless()
public class HotelReservationWebService {

    @EJB(name = "GuestSessionBeanLocal")
    private GuestSessionBeanLocal guestSessionBeanLocal;

    @EJB(name = "SearchRoomSessionBeanLocal")
    private SearchRoomSessionBeanLocal searchRoomSessionBeanLocal;

    @EJB(name = "PartnerSessionBeanLocal")
    private PartnerSessionBeanLocal partnerSessionBeanLocal;

    @EJB(name = "ReservationSessionBeanLocal")
    private ReservationSessionBeanLocal reservationSessionBeanLocal;

    @EJB(name = "RoomTypeSessionBeanLocal")
    private RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;

    @EJB(name = "RoomRateSessionBeanLocal")
    private RoomRateSessionBeanLocal roomRateSessionBeanLocal;

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @EJB(name = "RoomSessionBeanLocal")
    private RoomSessionBeanLocal roomSessionBeanLocal;

    @WebMethod(operationName = "getPartners")
    public List<Partner> getPartners() throws PartnerNotFoundException {
        return partnerSessionBeanLocal.getPartners();
    }

    @WebMethod(operationName = "getPartnerByUsername")
    public Partner getPartnerByUsername(@WebParam(name = "partnerUsername") String partnerUsername)
            throws PartnerNotFoundException {
        Partner p = partnerSessionBeanLocal.getPartnerByUsername(partnerUsername);
        em.detach(p);
        p.getReservationList().forEach(x -> x.getRoomList().forEach(y -> y.getRoomRmType().setRooms(new ArrayList<>())));
        p.getReservationList().forEach(x -> x.getRoomType().setRooms(new ArrayList<>()));
        p.getReservationList().forEach(x -> x.getRoomType().setParentRoomType(null));
        
        return p;
    }

    // RoomRate
    @WebMethod(operationName = "getRoomRateByName")
    public RoomRate getRoomRateByName(@WebParam(name = "roomRate") RoomRate roomRate) throws RoomRateNotFoundException {
        return roomRateSessionBeanLocal.getRoomRateByName(roomRate);
    }

    @WebMethod(operationName = "getRoomRateById")
    public RoomRate getRoomRateById(@WebParam(name = "roomRateId") Long roomRateId) throws RoomRateNotFoundException {
        return roomRateSessionBeanLocal.getRoomRateById(roomRateId);
    }

    @WebMethod(operationName = "getRoomRates")
    public List<RoomRate> getRoomRates() throws RoomRateNotFoundException {
        return roomRateSessionBeanLocal.getRoomRates();
    }

    // RoomType
    @WebMethod(operationName = "getRoomTypes")
    public List<RoomType> getRoomTypes() throws RoomTypeNotFoundException {
        return roomTypeSessionBeanLocal.getRoomTypes();
    }

    @WebMethod(operationName = "getRoomTypeById")
    public RoomType getRoomTypeById(@WebParam(name = "roomTypeID") Long roomTypeID) throws RoomTypeNotFoundException {
        return roomTypeSessionBeanLocal.getRoomTypeById(roomTypeID);
    }

    @WebMethod(operationName = "getRoomTypeByName")
    public RoomType getRoomTypeByName(@WebParam(name = "roomTypeName") String roomTypeName)
            throws RoomTypeNotFoundException {
        return roomTypeSessionBeanLocal.getRoomTypeByName(roomTypeName);
    }

    // Room
    @WebMethod(operationName = "getRooms")
    public List<Room> getRooms() throws RoomNotFoundException {
        List<Room> rooms = roomSessionBeanLocal.getRooms();

        rooms.forEach(x -> em.detach(x));
        rooms.forEach(x -> x.getRoomRmType().setRooms(new ArrayList<>()));
        return rooms;
    }

    @WebMethod(operationName = "getRoomById")
    public Room getRoomById(@WebParam(name = "roomID") Long roomID) throws RoomNotFoundException {
        return roomSessionBeanLocal.getRoomById(roomID);
    }

    @WebMethod(operationName = "getRoomByNumber")
    public Room getRoomByNumber(@WebParam(name = "roomNumber") String roomNumber) throws RoomNotFoundException {
        return roomSessionBeanLocal.getRoomByNumber(roomNumber);
    }

    @WebMethod(operationName = "updateRoom")
    public Room updateRoom(@WebParam(name = "room") Room room)
            throws RoomNotFoundException, RoomNumberAlreadyExistException, RoomTypeNotFoundException {
        return roomSessionBeanLocal.updateRoom(room);
    }

    // Reservation
    @WebMethod(operationName = "retrieveAllPartnerReservations")
    public List<Reservation> retrieveAllPartnerReservations(@WebParam(name = "partner") Partner partner) throws ReservationNotFoundException {
        return reservationSessionBeanLocal.retrieveAllPartnerReservations(partner);
    }

    @WebMethod(operationName = "retrieveReservationByReservationId")
    public Reservation retrieveReservationByReservationId(@WebParam(name = "guest") Guest guest,
            @WebParam(name = "reservationId") Long reservationId) {
        return reservationSessionBeanLocal.retrieveReservationByReservationId(guest, reservationId);
    }

    @WebMethod(operationName = "retrieveAllReservationWithinDates")
    public List<Reservation> retrieveAllReservationWithinDates(@WebParam(name = "checkInDate") Date checkInDate,
            @WebParam(name = "checkOutDate") Date checkOutDate, @WebParam(name = "roomType") RoomType roomType) {
        return reservationSessionBeanLocal.retrieveAllReservationWithinDates(checkInDate, checkOutDate, roomType);
    }

    @WebMethod(operationName = "getLoadedReservation")
    public Reservation getLoadedReservation(@WebParam(name = "reservation") Reservation reservation)
            throws ReservationNotFoundException {
        return reservationSessionBeanLocal.getLoadedReservation(reservation);
    }

    @WebMethod(operationName = "retrieveAllReseravtions")
    public List<Reservation> retrieveAllReseravtions() {
        return reservationSessionBeanLocal.retrieveAllReseravtions();
    }

    // searchRoom
    @WebMethod(operationName = "searchRooms")
    public List<Room> searchRooms(@WebParam(name = "checkIndate") Date checkIndate,
            @WebParam(name = "checkOutDate") Date checkOutDate, @WebParam(name = "roomType") RoomType roomType)
            throws RoomNotFoundException, RoomTypeNotFoundException {
        return searchRoomSessionBeanLocal.searchRooms(checkIndate, checkOutDate, roomType);
    }

    @WebMethod(operationName = "generateReservation")
    public List<Reservation> generateReservation(@WebParam(name = "checkInDate") Date checkInDate,
            @WebParam(name = "checkOutDate") Date checkOutDate)
            throws RoomNotFoundException {
        List<Reservation> reservations = searchRoomSessionBeanLocal.generateReservationOnline(checkInDate, checkOutDate);
//        for (Reservation r : reservations) {
//            em.detach(r);
//            r.getRoomList().forEach(x -> {
//                em.detach(x);
//                x.setRoomRmType(null);
//            });
//            em.detach(r.getRoomType());
//            r.getRoomType().setRooms(new ArrayList<>());
//        }
//        reservations.forEach(x -> x.getRoomList().forEach(y -> y.getRoomRmType().setRooms(new ArrayList<>())));
        reservations.forEach(x -> x.getRoomType().setRooms(new ArrayList<>()));
        reservations.forEach(x -> x.getRoomType().setParentRoomType(null));
        reservations.forEach(x -> x.getRoomType());
        return reservations;
    }

    @WebMethod(operationName = "loginGuest")
    public Guest loginGuest(@WebParam(name = "username") String username, @WebParam(name = "password") String password) throws GuestNotFoundException, InvalidLoginCredentialsException {
        return guestSessionBeanLocal.loginGuest(username, password);
    }

    @WebMethod(operationName = "createNewReservation")
    public Reservation createNewReservation(@WebParam(name = "reservation") Reservation reservation) throws RoomTypeNotFoundException {
        RoomType roomtype = roomTypeSessionBeanLocal.getRoomTypeByName(reservation.getRoomType().getName());
        reservation.setRoomType(roomtype);
        Reservation r = reservationSessionBeanLocal.createNewReservation(reservation);
        em.detach(r);
        em.detach(r.getRoomType());

        r.setRoomList(new ArrayList<>());
        r.setRoomType(null);
        return r;
    }

    @WebMethod(operationName = "addReservation")
    public Partner addReservation(@WebParam(name = "partner") Partner partner, @WebParam(name = "reservation") Reservation reservation) throws PartnerNotFoundException, ReservationNotFoundException {
        partner = partnerSessionBeanLocal.getPartnerByUsername(partner.getUsername());
        Reservation mreservation = reservationSessionBeanLocal.getLoadedReservation(reservation);
        partner.getReservationList().forEach(x -> System.out.println("RESERVATION: " + x.getRoomType()));
        System.out.println("NEW RESERVATION: " + mreservation.getRoomType());
        partner.getReservationList().add(mreservation);
        partner.getReservationList().size();
        
        em.flush();
        em.detach(partner);
        partner.getReservationList().forEach(x -> em.detach(x));
        partner.getReservationList().forEach(x -> em.detach(x.getRoomType()));
        partner.getReservationList().forEach(x -> x.getRoomList().forEach(y -> em.detach(y)));
        
        partner.getReservationList().forEach(x -> x.getRoomType());
        partner.getReservationList().forEach(x -> x.getRoomList()
                .forEach(y -> y.getRoomRmType()
                        .setRooms(new ArrayList<>())));
        partner.getReservationList().forEach(x -> x.getRoomType().setRooms(new ArrayList<>()));
        partner.getReservationList().forEach(x -> x.getRoomType().setParentRoomType(null));
        
        return partner;
    }
}
