/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import java.util.Date;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

/**
 *
 * @author Tan Jian Feng
 */
@Singleton
public class AllocationSessionBean implements AllocationSessionBeanRemote, AllocationSessionBeanLocal {

    @Override
    @Schedule(hour = "2")
    public void allocateRoom() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public void manualAllocateRooms(Date date) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
