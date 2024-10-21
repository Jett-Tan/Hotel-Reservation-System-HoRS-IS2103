/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import exception.EmployeeNotFoundException;
import exception.EmployeeUsernameAlreadyExistException;
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
public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public Employee createNewEmployee(Employee employee) throws EmployeeUsernameAlreadyExistException {
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.username = :username")
                .setParameter("username", employee.getUsername());
        if (query.getFirstResult() == 0) {
            em.persist(employee);
            em.flush();
            return employee;
        }else {
            throw new EmployeeUsernameAlreadyExistException("Employee with username of " + employee.getUsername() + " already exist!");
        }
    }

    @Override
    public List<Employee> getEmployees() throws EmployeeNotFoundException {
        List<Employee> list = em.createQuery("SELECT e FROM Employee e").getResultList();
        if(list.isEmpty()) {
            throw new EmployeeNotFoundException("There is no employee in the system");
        }
        return list;
    }

    @Override
    public Employee getEmployeeById(Long EmployeeID) throws EmployeeNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Employee getEmployeeByUsername(Long EmployeeID) throws EmployeeNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Employee updateUsername(Employee employee) throws EmployeeNotFoundException, EmployeeUsernameAlreadyExistException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Employee updateFirstName(Employee employee) throws EmployeeNotFoundException, EmployeeUsernameAlreadyExistException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Employee updateLastName(Employee employee) throws EmployeeNotFoundException, EmployeeUsernameAlreadyExistException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Employee updatePassword(Employee employee) throws EmployeeNotFoundException, EmployeeUsernameAlreadyExistException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deleteEmployee(Employee employee) throws EmployeeNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deleteEmployeeById(Long employeeId) throws EmployeeNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
