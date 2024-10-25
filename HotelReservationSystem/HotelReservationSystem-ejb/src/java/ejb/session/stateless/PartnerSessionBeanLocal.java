/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Partner;
import exception.PartnerNotFoundException;
import exception.PartnerUsernameAlreadyExistException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Tan Jian Feng
 */
@Local
public interface PartnerSessionBeanLocal {
    Partner createNewPartner(Partner partner) throws PartnerUsernameAlreadyExistException;
        
    List<Partner> getPartners() throws PartnerNotFoundException;
    
    Partner getPartnerById(Long partnerID) throws PartnerNotFoundException;
    
    Partner getPartnerByUsername(String partnerUsername) throws PartnerNotFoundException;
    
    Partner updateUsername(Partner partner) throws PartnerNotFoundException, PartnerUsernameAlreadyExistException;
    
    Partner updateFirstName(Partner partner) throws PartnerNotFoundException;
    
    Partner updateLastName(Partner partner) throws PartnerNotFoundException;
    
    Partner updatePassword(Partner partner) throws PartnerNotFoundException;
    
    boolean deletePartner(Partner partner) throws PartnerNotFoundException;
    
    boolean deletePartnerById(Long partnerId) throws PartnerNotFoundException;
}
