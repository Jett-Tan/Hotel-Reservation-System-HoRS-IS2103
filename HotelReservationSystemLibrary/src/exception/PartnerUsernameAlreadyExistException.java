/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package exception;

/**
 *
 * @author Tan Jian Feng
 */
public class PartnerUsernameAlreadyExistException extends Exception{

    /**
     * Creates a new instance of <code>EmployeeNotFoundException</code> without
     * detail message.
     */
    public PartnerUsernameAlreadyExistException() {
    }

    /**
     * Constructs an instance of <code>EmployeeNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public PartnerUsernameAlreadyExistException(String msg) {
        super(msg);
    }
}
