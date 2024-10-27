/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Guest;
import exception.GuestNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author jovanfoo
 */
@Stateless
public class GuestSessionBean implements GuestSessionBeanRemote, GuestSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public Guest getGuestById(Long guestId) throws GuestNotFoundException {
        Guest guest = em.find(Guest.class, guestId);
        if (guest == null) {
            throw new GuestNotFoundException("Guest with Id " + guestId + " not found!");
        }
        return guest;
    }

    @Override
    public Guest getGuestByUsername(String guestUsername) throws GuestNotFoundException {
        Query query = em.createQuery("SELECT g FROM Guest g WHERE g.username = :userName")
                .setParameter("userName", guestUsername);
        Guest guest = (Guest) query.getSingleResult();
        if (guest == null) {
            throw new GuestNotFoundException("Guest with username " + guestUsername + " not found!");
        }
        return guest;
    }

    @Override
    public boolean isUsernameExist(String guestUsername) {
        List<Guest> guestList = retrieveAllGuest();
        
        for (Guest guest : guestList) {
            if (guest.getUniqueIdentifier().equalsIgnoreCase(guestUsername)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Guest registerGuest(Guest guest) {
        em.persist(guest);
        em.flush();
        return guest;
    }
    
    public List<Guest> retrieveAllGuest() {
        Query query = em.createQuery("SELECT g FROM Guest g");
        List<Guest> guestList = query.getResultList();
        
        if (guestList == null) {
            return new ArrayList<>();
        }

        return guestList;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
