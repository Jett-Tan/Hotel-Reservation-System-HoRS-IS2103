/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Partner;
import exception.PartnerNotFoundException;
import exception.PartnerUsernameAlreadyExistException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Tan Jian Feng
 */
@Stateless
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    
    @Override
    public Partner createNewPartner(Partner partner) throws PartnerUsernameAlreadyExistException {
        try{
            getPartnerByUsername(partner.getUsername());
            throw new PartnerUsernameAlreadyExistException("Partner with username of " + partner.getUsername() + " already exist!");
        } catch (PartnerNotFoundException ex) {
            em.persist(partner);
            em.flush();
            return partner;
        }
    }

    @Override
    public List<Partner> getPartners() throws PartnerNotFoundException {
        List<Partner> list = em.createQuery("SELECT e FROM Partner e").getResultList();
        if(list.isEmpty()) {
            throw new PartnerNotFoundException("There is no partner in the system");
        }
        return list;
    }

    @Override
    public Partner getPartnerById(Long partnerId) throws PartnerNotFoundException {
        Partner partner = em.find(Partner.class,partnerId);
        if (partner == null) {
            throw new PartnerNotFoundException("Partner with Id " + partnerId + " not found!");
        }
        return partner;
    }

    @Override
    public Partner getPartnerByUsername(String partnerUsername) throws PartnerNotFoundException {
        Query query = em.createQuery("SELECT e FROM Partner e WHERE e.username = :userName")
                .setParameter("userName", partnerUsername);
        try{
             return (Partner) query.getSingleResult();
        } catch(NoResultException ex){
            throw new PartnerNotFoundException("Partner with username " + partnerUsername + " not found!");
        }
        
    }

    @Override
    public Partner updateUsername(Partner partner) throws PartnerNotFoundException, PartnerUsernameAlreadyExistException {
        Partner emPartner = getPartnerById(partner.getPartnerId());
        try{
            getPartnerByUsername(partner.getUsername());
        } catch ( PartnerNotFoundException ex) {
            emPartner.setUsername(partner.getUsername());
            return emPartner;
        }
        throw new PartnerUsernameAlreadyExistException("Partner with username " + partner.getUsername() + " already exist!");
    }

    @Override
    public Partner updateFirstName(Partner partner) throws PartnerNotFoundException {
        Partner emPartner = getPartnerById(partner.getPartnerId());
        emPartner.setFirstName(partner.getFirstName());
        return emPartner;
    }

    @Override
    public Partner updateLastName(Partner partner) throws PartnerNotFoundException {
        Partner emPartner = getPartnerById(partner.getPartnerId());
        emPartner.setLastName(partner.getLastName());
        return emPartner;}

    @Override
    public Partner updatePassword(Partner partner) throws PartnerNotFoundException {
        Partner emPartner = getPartnerById(partner.getPartnerId());
        emPartner.setPassword(partner.getPassword());
        return emPartner;
    }

    @Override
    public boolean deletePartner(Partner partner) throws PartnerNotFoundException {
        Partner emPartner = getPartnerById(partner.getPartnerId());
        em.remove(emPartner);
        return true;
    }

    @Override
    public boolean deletePartnerById(Long partnerId) throws PartnerNotFoundException {
        Partner emPartner = getPartnerById(partnerId);
        em.remove(emPartner);
        return true;
    }

}
