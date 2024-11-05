/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.AllocationReport;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Tan Jian Feng
 */
@Local
public interface AllocationReportSessionBeanLocal {

    public AllocationReport createNewAllocationReport(AllocationReport allocationReport);
    
    public List<AllocationReport> getAllAllocationReports();
}
