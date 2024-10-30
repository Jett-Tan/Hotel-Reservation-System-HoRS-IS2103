/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import enumerations.ReservationTypeEnum;
import java.io.Serializable;
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
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;

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
    @Temporal(TemporalType.DATE)
    @Future
    private Date startDate;
    
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @Future
    private Date endDate;
    
    @Enumerated(EnumType.STRING)
    private ReservationTypeEnum reservationTpye;

    @NotEmpty(message = "Input list should not be empty")
    @OneToMany(fetch = FetchType.LAZY)
    private List<Room> roomList;
    
    @Column(nullable = false)
    private Boolean allocated;
    
    public Long getReservationId() {
        return reservationId;
    }

    public Reservation() {
    }

    public Reservation(Date startDate, Date endDate, ReservationTypeEnum reservationTpye, List<Room> roomList, Boolean allocated) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.reservationTpye = reservationTpye;
        this.roomList = roomList;
        this.allocated = allocated;
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

    public ReservationTypeEnum getReservationTpye() {
        return reservationTpye;
    }

    public void setReservationTpye(ReservationTypeEnum reservationTpye) {
        this.reservationTpye = reservationTpye;
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }

    public Boolean getAllocated() {
        return allocated;
    }

    public void setAllocated(Boolean allocated) {
        this.allocated = allocated;
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
    
}
