/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import enumerations.RoomStatusEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Tan Jian Feng
 */
@Entity
public class RoomRate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomRateId;
    
    @Column(nullable = false)
    @NotNull
    @Size(min = 1, max = 20)
    private String name;
    
    @Column(nullable = false, scale = 2)
    @NotNull
    @Digits(integer = 4, fraction = 2)
    private BigDecimal rate;
    
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date startDate;
    
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date endDate;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private RoomStatusEnum rateStatus;

    
    public RoomRate() {
    }

    public RoomRate(Long roomRateId, String name, BigDecimal rate, Date startDate, Date endDate, RoomStatusEnum rateStatus) {
        this.roomRateId = roomRateId;
        this.name = name;
        this.rate = rate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rateStatus = rateStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public RoomStatusEnum getRateStatus() {
        return rateStatus;
    }

    public void setRateStatus(RoomStatusEnum rateStatus) {
        this.rateStatus = rateStatus;
    }
    
    public Long getRoomRateId() {
        return roomRateId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomRateId != null ? roomRateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomRateId fields are not set
        if (!(object instanceof RoomRate)) {
            return false;
        }
        RoomRate other = (RoomRate) object;
        if ((this.roomRateId == null && other.roomRateId != null) || (this.roomRateId != null && !this.roomRateId.equals(other.roomRateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomRate[ id=" + roomRateId + " ]";
    }
    
}
