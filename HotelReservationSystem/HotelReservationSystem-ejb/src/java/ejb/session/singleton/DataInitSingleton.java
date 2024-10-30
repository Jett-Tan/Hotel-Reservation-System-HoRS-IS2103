/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import entity.Employee;
import enumerations.EmployeeTypeEnum;
import exception.EmployeeNotFoundException;
import exception.EmployeeUsernameAlreadyExistException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Tan Jian Feng
 */
@Singleton
@LocalBean
@Startup
public class DataInitSingleton {

    @EJB(name = "EmployeeSessionBeanLocal")
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;
    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    
    public DataInitSingleton() {
    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PostConstruct
    public void run(){
        System.out.println("\n\n=========================");
        System.out.println("DEPLOYED");
        System.out.println("=========================\n\n");
        Employee employee = em.find(Employee.class, 1L);
        if(employee == null) {
            System.out.println("Initialising Data");
            try {
                employeeSessionBeanLocal.createNewEmployee(new Employee("System","Admin", "admin", "password", EmployeeTypeEnum.SYSTEM_ADMINISTRATOR));
                employeeSessionBeanLocal.createNewEmployee(new Employee("Guest","Officer", "officer", "password", EmployeeTypeEnum.GUEST_RELATION_OFFICER));
                employeeSessionBeanLocal.createNewEmployee(new Employee("Sales","Sales", "sales", "password", EmployeeTypeEnum.SALES));
                employeeSessionBeanLocal.createNewEmployee(new Employee("Operation","Manager", "manager", "password", EmployeeTypeEnum.OPERATION_MANAGER));

            } catch (EmployeeUsernameAlreadyExistException ex1) {
                System.out.println(ex1);
            }
        }
    }
}
