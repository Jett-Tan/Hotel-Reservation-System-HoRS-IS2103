/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import enumerations.PartnerEmployeeTypeEnum;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Tan Jian Feng
 */
@Entity
public class Partner implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long PartnerId;
    
    @Column(nullable = false)
    @Size(min = 1, max = 10)
    @NotNull
    private String firstName;
    
    @Column(nullable = false)
    @Size(min = 1, max = 10)
    @NotNull
    private String lastName;
    
    @Column(nullable = false,unique = true)
    @Size(min = 1, max = 10)
    @NotNull
    private String username;
    
    @Column(nullable = false)
    @Size(min = 8)
    @NotNull
    private String password;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private PartnerEmployeeTypeEnum employeeType;
    
    @OneToMany
    private List<Reservation> reservationList;
    public Partner() {
    }
    
    public Partner(String firstName, String lastName, String username, String password, PartnerEmployeeTypeEnum employeeType, List<Reservation> reservationList) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.employeeType = employeeType;
        this.reservationList = reservationList;
    }

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public PartnerEmployeeTypeEnum getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(PartnerEmployeeTypeEnum employeeType) {
        this.employeeType = employeeType;
    }
    
    
    
    public Long getPartnerId() {
        return PartnerId;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (PartnerId != null ? PartnerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the PartnerId fields are not set
        if (!(object instanceof Partner)) {
            return false;
        }
        Partner other = (Partner) object;
        if ((this.PartnerId == null && other.PartnerId != null) || (this.PartnerId != null && !this.PartnerId.equals(other.PartnerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Partner[ id=" + PartnerId + " ]";
    }
    
}
