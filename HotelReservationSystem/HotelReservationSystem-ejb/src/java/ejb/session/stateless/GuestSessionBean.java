/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Guest;
import entity.Reservation;
import exception.GuestNotFoundException;
import exception.GuestUsernameAlreadyExistException;
import exception.InvalidDataException;
import exception.InvalidLoginCredentialsException;
import exception.ReservationNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

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
        try {
            Guest guest = (Guest) query.getSingleResult();
            guest.getReservationList().size();
            return guest;
        } catch (NoResultException ex) {
            throw new GuestNotFoundException("Guest with username of " + guestUsername + " not found!");
        }
    }
    @Override
    public Guest getGuestByName(String guestName) throws GuestNotFoundException {
        Query query = em.createQuery("SELECT g FROM Guest g WHERE g.name = :name")
                .setParameter("name", guestName);
        try {
            Guest guest = (Guest) query.getSingleResult();
            guest.getReservationList().size();
            return guest;
        } catch (NoResultException ex) {
            throw new GuestNotFoundException("Guest with name of " + guestName + " not found!");
        }
    }

    @Override
    public boolean isUsernameExist(String guestUsername) {
        List<Guest> guestList = retrieveAllGuest();
        if (guestList.isEmpty()) {
            return false;
        }
        for (Guest guest : guestList) {
            if (guest.getUsername().equalsIgnoreCase(guestUsername)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Guest loginGuest(String username, String password) throws GuestNotFoundException, InvalidLoginCredentialsException {
        try {
            Guest guest = getGuestByUsername(username);
            if (guest.getPassword().equals(password)) {
                guest.getReservationList().size();
                return guest;
            } else {
                throw new InvalidLoginCredentialsException("You have entered the wrong password!");
            }
        } catch (NoResultException ex) {
             throw new InvalidLoginCredentialsException("This guest does not exist!");
        }
    }

    @Override
    public Guest registerGuest(Guest guest) throws InvalidDataException, GuestUsernameAlreadyExistException {
        if (isUsernameExist(guest.getUsername())) {
            System.out.println("Duplicate Username!");
        }

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        Set<ConstraintViolation<Guest>> errorList = validator.validate(guest);

        if (errorList.isEmpty()) {
            if(!isUsernameExist(guest.getUsername())) {
                em.persist(guest);
                em.flush();
                return guest;
            }else {
                throw new GuestUsernameAlreadyExistException("Guest with username of " + guest.getUsername() + " already exist!");
            }
        } else {
            throw new InvalidDataException("Register failed");
        }
    }
    
    @Override
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

    @Override
    public Guest addReservation(Guest guest,Reservation reservation) throws GuestNotFoundException, ReservationNotFoundException {
        Guest managedGuest = getGuestById(guest.getGuestId());
        managedGuest.getReservationList().add(reservation);
        managedGuest.getReservationList().size();
        return managedGuest;
    }

    @Override
    public Guest getGuestByPassportNumber(String passportNumber) throws GuestNotFoundException {
        Query query = em.createQuery("SELECT g FROM Guest g WHERE g.passportNumber = :passportNumber")
            .setParameter("passportNumber", passportNumber);
        try {
            Guest guest = (Guest) query.getSingleResult();
            guest.getReservationList().size();
            return guest;
        } catch (NoResultException ex) {
            throw new GuestNotFoundException("Guest with passport number of " + passportNumber + " not found!");
        }
    }
}
