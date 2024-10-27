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
import javax.persistence.NoResultException;
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
        try {
            getEmployeeByUsername(employee.getUsername());
            throw new EmployeeUsernameAlreadyExistException("Employee with username of " + employee.getUsername() + " already exist!");
        } catch (EmployeeNotFoundException ex) {
            em.persist(employee);
            em.flush();
            return employee;
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
    public Employee getEmployeeById(Long employeeId) throws EmployeeNotFoundException {
        Employee employee = em.find(Employee.class,employeeId);
        if (employee == null) {
            throw new EmployeeNotFoundException("Employee with Id " + employeeId + " not found!");
        }
        return employee;
    }

    @Override
    public Employee getEmployeeByUsername(String employeeUsername) throws EmployeeNotFoundException {
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.username = :userName")
                .setParameter("userName", employeeUsername);
        try{
             return (Employee) query.getSingleResult();
        } catch(NoResultException ex){
            throw new EmployeeNotFoundException("Employee with username " + employeeUsername + " not found!");
        }
    }
    
    @Override
    public Employee updateUsername(Employee employee) throws EmployeeNotFoundException, EmployeeUsernameAlreadyExistException {
        Employee emEmployee = getEmployeeById(employee.getEmployeeId());
        try{
            getEmployeeByUsername(employee.getUsername());
        } catch ( EmployeeNotFoundException ex) {
            emEmployee.setUsername(employee.getUsername());
            return emEmployee;
        }
        throw new EmployeeUsernameAlreadyExistException("Employee with username " + employee.getUsername() + " already exist!");
    }

    @Override
    public Employee updateFirstName(Employee employee) throws EmployeeNotFoundException {
        Employee emEmployee = getEmployeeById(employee.getEmployeeId());
        emEmployee.setFirstName(employee.getFirstName());
        return emEmployee;
    }

    @Override
    public Employee updateLastName(Employee employee) throws EmployeeNotFoundException {
        Employee emEmployee = getEmployeeById(employee.getEmployeeId());
        emEmployee.setLastName(employee.getLastName());
        return emEmployee;
    }

    @Override
    public Employee updatePassword(Employee employee) throws EmployeeNotFoundException {
        Employee emEmployee = getEmployeeById(employee.getEmployeeId());
        emEmployee.setPassword(employee.getPassword());
        return emEmployee;
    }

    @Override
    public boolean deleteEmployee(Employee employee) throws EmployeeNotFoundException {
        Employee emEmployee = getEmployeeById(employee.getEmployeeId());
        em.remove(emEmployee);
        return true;
    }

    @Override
    public boolean deleteEmployeeById(Long employeeId) throws EmployeeNotFoundException {
        Employee emEmployee = getEmployeeById(employeeId);
        em.remove(emEmployee);
        return true;
    }
}
