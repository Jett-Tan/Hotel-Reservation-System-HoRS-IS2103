/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.singleton;

import javax.ejb.Local;

/**
 *
 * @author Tan Jian Feng
 */
@Local
public interface AllocationSingletonLocal {
    public void allocateRoom();
}
