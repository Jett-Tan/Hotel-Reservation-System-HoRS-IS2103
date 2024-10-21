/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import exception.EmployeeNotFoundException;
import exception.EmployeeUsernameAlreadyExistException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Tan Jian Feng
 */
@Local
public interface EmployeeSessionBeanLocal {
   //CRUD
    Employee createNewEmployee(Employee employee) throws EmployeeUsernameAlreadyExistException;
    
    List<Employee> getEmployees() throws EmployeeNotFoundException;
    
    Employee getEmployeeById(Long EmployeeID) throws EmployeeNotFoundException;
    
    Employee getEmployeeByUsername(Long EmployeeID) throws EmployeeNotFoundException;
    
    Employee updateUsername(Employee employee) throws EmployeeNotFoundException, EmployeeUsernameAlreadyExistException;
    
    Employee updateFirstName(Employee employee) throws EmployeeNotFoundException, EmployeeUsernameAlreadyExistException;
    
    Employee updateLastName(Employee employee) throws EmployeeNotFoundException, EmployeeUsernameAlreadyExistException;
    
    Employee updatePassword(Employee employee) throws EmployeeNotFoundException, EmployeeUsernameAlreadyExistException;
    
    boolean deleteEmployee(Employee employee) throws EmployeeNotFoundException;
    
    boolean deleteEmployeeById(Long employeeId) throws EmployeeNotFoundException;
}
