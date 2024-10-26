/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import entity.Employee;
import enumeration.EmployeeTypeEnum;
import exception.EmployeeNotFoundException;
import exception.EmployeeUsernameAlreadyExistException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;

/**
 *
 * @author Tan Jian Feng
 */
@Singleton
@LocalBean
public class DataInitSingleton {

    @EJB(name = "EmployeeSessionBeanLocal")
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;
    
    
    public DataInitSingleton() {
    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PostConstruct
    public void run(){
        System.out.println("\n\n=========================");
        System.out.println("DEPLOYED");
        System.out.println("=========================\n\n");
        try{
            employeeSessionBeanLocal.getEmployees();
        } catch (EmployeeNotFoundException ex) {        
            System.out.println("Initialising Data");
            try {
                employeeSessionBeanLocal.createNewEmployee(new Employee("SYSTEMADMINISTRATOR","SYSTEMADMINISTRATOR", "admin", "password", EmployeeTypeEnum.SYSTEM_ADMINISTRATOR));
                employeeSessionBeanLocal.createNewEmployee(new Employee("GUESTRELATIONOFFICER","GUESTRELATIONOFFICER", "officer", "password", EmployeeTypeEnum.GUEST_RELATION_OFFICER));
                employeeSessionBeanLocal.createNewEmployee(new Employee("SALES","SALES", "sales", "password", EmployeeTypeEnum.SALES));
                employeeSessionBeanLocal.createNewEmployee(new Employee("OPERATIONMANAGER","OPERATIONMANAGER", "manager", "password", EmployeeTypeEnum.OPERATION_MANAGER));

            } catch (EmployeeUsernameAlreadyExistException ex1) {
                System.out.println(ex1);
            }
        }
    }
}
