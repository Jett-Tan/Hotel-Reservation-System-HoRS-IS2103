/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Guest;
import exception.GuestNotFoundException;
import exception.InvalidDataException;
import exception.InvalidLoginCredentialsException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author jovanfoo
 */
@Remote
public interface GuestSessionBeanRemote {

    public Guest getGuestById(Long guestId) throws GuestNotFoundException;

    public Guest getGuestByUsername(String guestUsername) throws GuestNotFoundException;

    public boolean isUsernameExist(String guestUsername);

    public Guest registerGuest(Guest guest) throws InvalidDataException;

    public List<Guest> retrieveAllGuest();

    public Guest loginGuest(String username, String password) throws GuestNotFoundException, InvalidLoginCredentialsException;

}
