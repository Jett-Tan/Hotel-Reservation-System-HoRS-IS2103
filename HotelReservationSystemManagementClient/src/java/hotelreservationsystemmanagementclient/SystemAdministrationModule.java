/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import entity.Employee;
import entity.Partner;
import enumerations.EmployeeTypeEnum;
import enumerations.PartnerEmployeeTypeEnum;
import exception.EmployeeNotFoundException;
import exception.EmployeeUsernameAlreadyExistException;
import exception.PartnerNotFoundException;
import exception.PartnerUsernameAlreadyExistException;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Tan Jian Feng
 */
public class SystemAdministrationModule {
    private PartnerSessionBeanRemote partnerSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private Employee currentEmployee;
    private Scanner scanner = new Scanner(System.in);

    public SystemAdministrationModule(
            PartnerSessionBeanRemote partnerSessionBeanRemote, 
            EmployeeSessionBeanRemote employeeSessionBeanRemote, 
            Employee currentEmployee
    ) {
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }
    
    public void run(){
        
        System.out.println("==== Welcome to Hotel Reservation System Management Client ====");
        System.out.println("====              System Administration Module             ====");
        int input = 0;
        do {
            System.out.println("1. Create new Employee");
            System.out.println("2. View all employees");
            System.out.println("3. Create new Partner");
            System.out.println("4. View all partners");
            System.out.println("5. Exit");
            System.out.print("Enter >> ");
            input = scanner.nextInt();
            scanner.nextLine();
            if (input == 5) {
                break;
            }
            switch(input) {
                case 1: {
                    doCreateNewEmployee();
                    break;
                }
                case 2: {
                    doViewAllEmployee();
                    break;
                }
                case 3: {
                    doCreateNewPartner();
                    break;
                }
                case 4: {
                    doViewAllPartners();
                    break;
                }
                default : {
                    System.out.println("\nInvalid input");
                    break;
                }
            }
        } while (true);
    }

    private void doCreateNewEmployee() {
        System.out.println("====              System Administration Module             ====");        
        System.out.println("====                  Create New Employee                  ====");
        String username, firstname, lastname, password;
        EmployeeTypeEnum employeeType = EmployeeTypeEnum.SALES;
        
        System.out.print("Enter first name >> ");
        firstname = scanner.nextLine();
        System.out.print("Enter last name >> ");        
        lastname = scanner.nextLine();
        System.out.print("Enter username >> ");        
        username = scanner.nextLine();
        System.out.print("Enter password >> ");       
        password = scanner.nextLine();
        
        int option = 0;
        do{
            System.out.println("\nSelect Employee Type");
            System.out.println("1. Sales");
            System.out.println("2. System Administrator");
            System.out.println("3. Operation Manager");
            System.out.println("4. Guest Relation Officer");
            System.out.print("Enter Employee Type (1 - 4) >> ");
            option = scanner.nextInt();
            scanner.nextLine();
            switch(option) {
                case 1: employeeType = EmployeeTypeEnum.SALES;
                        break;
                case 2: employeeType = EmployeeTypeEnum.SYSTEM_ADMINISTRATOR;
                        break;
                case 3: employeeType = EmployeeTypeEnum.OPERATION_MANAGER;
                        break;
                case 4: employeeType = EmployeeTypeEnum.GUEST_RELATION_OFFICER;
                        break;
            }
        }while(option > 4 || option < 1);
        Employee newEmployee = new Employee(firstname,lastname,username,password,employeeType);
        try {
            newEmployee = employeeSessionBeanRemote.createNewEmployee(newEmployee);
            System.out.println("New employee created with username of " + newEmployee.getUsername() );
        } catch (EmployeeUsernameAlreadyExistException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void doViewAllEmployee() {
        System.out.println("====              System Administration Module             ====");        
        System.out.println("====                   View All Employees                  ====");
        try {
            List<Employee> employees = employeeSessionBeanRemote.getEmployees();
            System.out.println("=======================================");
            for(int i = 1; i < employees.size()+1; i++) {
                Employee employee = employees.get(i-1);
                System.out.println(String.format("%2d.%10s%10s ",i ,employee.getFirstName() + employee.getLastName(), employee.getEmployeeType()));
            }
        } catch (EmployeeNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void doCreateNewPartner() {
        System.out.println("====              System Administration Module             ====");        
        System.out.println("====                   Create New Partner                  ====");
        String username, firstname, lastname, password;
        PartnerEmployeeTypeEnum employeeType = PartnerEmployeeTypeEnum.EMPLOYEE;
        
        System.out.print("Enter first name >> ");
        firstname = scanner.nextLine();
        System.out.print("Enter last name >> ");        
        lastname = scanner.nextLine();
        System.out.print("Enter username >> ");        
        username = scanner.nextLine();
        System.out.print("Enter password >> ");       
        password = scanner.nextLine();
        
        int option = 0;
        do{
            System.out.println("\nSelect Partner Employee Type");
            System.out.println("1. Employee");
            System.out.println("2. Manager");
            System.out.print("Enter Employee Type (1 - 2) >> ");
            option = scanner.nextInt();
            scanner.nextLine();
            switch(option) {
                case 1: employeeType = PartnerEmployeeTypeEnum.EMPLOYEE;
                        break;
                case 2: employeeType = PartnerEmployeeTypeEnum.MANAGER;
                        break;
            }
        }while(option > 2 || option < 1);
        Partner newPartner = new Partner(firstname,lastname,username,password,employeeType);
        try {
            newPartner = partnerSessionBeanRemote.createNewPartner(newPartner);
            System.out.println("New partner created with username of " + newPartner.getUsername() );
        } catch (PartnerUsernameAlreadyExistException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void doViewAllPartners() { 
        System.out.println("====              System Administration Module             ====");        
        System.out.println("====                    View All Partners                  ====");
        try {
            List<Partner> partners = partnerSessionBeanRemote.getPartners();
            System.out.println("=======================================");
            for(int i = 1; i < partners.size()+1; i++) {
                Partner partner = partners.get(i-1);
                System.out.println(String.format("%2d.%10s%10s ",i ,partner.getFirstName() + partner.getLastName(), partner.getEmployeeType()));
            }
        } catch (PartnerNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
