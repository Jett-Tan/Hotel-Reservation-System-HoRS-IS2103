/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.AllocationReport;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Tan Jian Feng
 */
@Remote
public interface AllocationReportSessionBeanRemote {
    public List<AllocationReport> getAllAllocationReports();
}
