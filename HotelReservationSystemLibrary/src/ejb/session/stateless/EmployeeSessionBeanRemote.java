/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import exception.EmployeeNotFoundException;
import exception.EmployeeUsernameAlreadyExistException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Tan Jian Feng
 */
@Remote
public interface EmployeeSessionBeanRemote {
    //CRUD
    Employee createNewEmployee(Employee employee) throws EmployeeUsernameAlreadyExistException;
    
    List<Employee> getEmployees() throws EmployeeNotFoundException;
    
    Employee getEmployeeById(Long EmployeeID) throws EmployeeNotFoundException;
    
    Employee getEmployeeByUsername(String EmployeeUsername) throws EmployeeNotFoundException;
    
    Employee updateUsername(Employee employee) throws EmployeeNotFoundException, EmployeeUsernameAlreadyExistException;
    
    Employee updateFirstName(Employee employee) throws EmployeeNotFoundException;
    
    Employee updateLastName(Employee employee) throws EmployeeNotFoundException;
    
    Employee updatePassword(Employee employee) throws EmployeeNotFoundException;
    
    boolean deleteEmployee(Employee employee) throws EmployeeNotFoundException;
    
    boolean deleteEmployeeById(Long employeeId) throws EmployeeNotFoundException;
}
