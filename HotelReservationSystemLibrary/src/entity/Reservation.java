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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
    private BigDecimal amountPerRoom;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Column(nullable = false)
    private BigDecimal numOfRooms;

    @Enumerated(EnumType.STRING)
    private ReservationTypeEnum reservationType;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Room> roomList;

    @Column(nullable = false)
    private Boolean allocated;
    // private List<Room> room;
    
    @ManyToOne
    @JoinColumn()
    private RoomType roomType;
    
//    @ManyToOne(optional = true, cascade = {}, fetch = FetchType.LAZY)
//    private Guest guest;
    
    public Long getReservationId() {
        return reservationId;
    }

    public Reservation() {
    }

    public Reservation(BigDecimal totalAmount, Date startDate, Date endDate, BigDecimal numOfRooms, ReservationTypeEnum reservationType, List<Room> roomList, Boolean allocated, RoomType roomType) {
        this.amountPerRoom = totalAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numOfRooms = numOfRooms;
        this.reservationType = reservationType;
        this.roomList = roomList;
        this.allocated = allocated;
        this.roomType = roomType;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public ReservationTypeEnum getReservationType() {
        return reservationType;
    }

    public void setReservationType(ReservationTypeEnum reservationType) {
        this.reservationType = reservationType;
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
        // TODO: Warning - this method won't work in the case the reservationId fields
        // are not set
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.reservationId == null && other.reservationId != null)
                || (this.reservationId != null && !this.reservationId.equals(other.reservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ReservationLine[ id=" + reservationId + " ]";
    }

    /**
     * @return the amountPerRoom
     */
    public BigDecimal getAmountPerRoom() {
        return amountPerRoom;
    }

    /**
     * @param amountPerRoom the amountPerRoom to set
     */
    public void setAmountPerRoom(BigDecimal amountPerRoom) {
        this.amountPerRoom = amountPerRoom;
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
