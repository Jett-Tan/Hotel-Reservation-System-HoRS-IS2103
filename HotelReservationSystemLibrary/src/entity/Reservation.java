/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import enumerations.ReservationTypeEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Tan Jian Feng
 */
@Entity
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    
    @Column(nullable = false)
    private BigDecimal totalAmount;
    
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date startDate;
    
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date endDate;
    
     @Column(nullable = false)
    private BigDecimal numOfRooms;
    
    @Enumerated(EnumType.STRING)
    private ReservationTypeEnum reservationTpy;
    
//    private List<Room> room;

    public Reservation(BigDecimal totalAmount, Date startDate, Date endDate, BigDecimal numOfRooms, ReservationTypeEnum reservationTpy) {
        this.totalAmount = totalAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numOfRooms = numOfRooms;
        this.reservationTpy = reservationTpy;
    }
    
    
    
    public Long getReservationId() {
        return reservationId;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationId != null ? reservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reservationId fields are not set
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.reservationId == null && other.reservationId != null) || (this.reservationId != null && !this.reservationId.equals(other.reservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ReservationLine[ id=" + reservationId + " ]";
    }

    /**
     * @return the totalAmount
     */
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    /**
     * @param totalAmount the totalAmount to set
     */
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * @param reservationId the reservationId to set
     */
    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the reservationTpy
     */
    public ReservationTypeEnum getReservationTpy() {
        return reservationTpy;
    }

    /**
     * @param reservationTpy the reservationTpy to set
     */
    public void setReservationTpy(ReservationTypeEnum reservationTpy) {
        this.reservationTpy = reservationTpy;
    }

    /**
     * @return the numOfRooms
     */
    public BigDecimal getNumOfRooms() {
        return numOfRooms;
    }

    /**
     * @param numOfRooms the numOfRooms to set
     */
    public void setNumOfRooms(BigDecimal numOfRooms) {
        this.numOfRooms = numOfRooms;
    }
    
}
