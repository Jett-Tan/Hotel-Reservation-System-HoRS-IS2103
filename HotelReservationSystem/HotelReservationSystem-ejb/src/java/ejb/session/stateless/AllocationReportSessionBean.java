/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AllocationReport;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Tan Jian Feng
 */
@Stateless
public class AllocationReportSessionBean implements AllocationReportSessionBeanRemote, AllocationReportSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public AllocationReport createNewAllocationReport(AllocationReport allocationReport){
        em.persist(allocationReport);
        em.flush();
        return allocationReport;
    }
    @Override
    public List<AllocationReport> getAllAllocationReports(){
        Query query = em.createQuery("SELECT a FROM AllocationReport a");
        List<AllocationReport> list = query.getResultList();
        return list;
    }
}
