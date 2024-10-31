/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import enumerations.AllocationTypeEnum;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author Tan Jian Feng
 */
@Entity
public class AllocationReport implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long allocationReportId;

    private AllocationTypeEnum type;
    
    @OneToOne
    private Reservation reservation;
    
    
    public AllocationReport() {
    }

    
    
    public Long getAllocationReportId() {
        return allocationReportId;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (allocationReportId != null ? allocationReportId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the allocationReportId fields are not set
        if (!(object instanceof AllocationReport)) {
            return false;
        }
        AllocationReport other = (AllocationReport) object;
        if ((this.allocationReportId == null && other.allocationReportId != null) || (this.allocationReportId != null && !this.allocationReportId.equals(other.allocationReportId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AllocationReport[ id=" + allocationReportId + " ]";
    }
    
}
