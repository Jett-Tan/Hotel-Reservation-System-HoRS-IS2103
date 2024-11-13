/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.singleton;

import entity.Reservation;
import exception.AllocationException;
import exception.ReservationNotFoundException;
import java.util.Date;
import javax.ejb.Remote;

/**
 *
 * @author Tan Jian Feng
 */
@Remote
public interface AllocationSingletonRemote {
    
    public void allocateRoom();
    public void allocateRoom(Date currentDate);
    public Reservation manualAllocateRooms(Reservation reservation) throws ReservationNotFoundException,AllocationException;
    public Reservation manualAllocateRoomsWithCheckin(Reservation reservation) throws ReservationNotFoundException,AllocationException;

}
