/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import enumerations.RoomStatusEnum;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Tan Jian Feng
 */
@Entity
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    
    @Column(nullable = false,unique = true)
    @NotNull
    @Size(min = 1, max = 10)
    private String roomNumber;
    
    private List<Date> bookedDates;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private RoomStatusEnum roomStatus;
   
    @Column(nullable = false)
    @NotNull
    private boolean isCheckedIn;
    
    @ManyToOne
    @JoinColumn(name = "room_roomtype")
    private RoomType roomRmType;
    
    public Room() {
    }

    public Room(String roomNumber, List<Date> bookedDates, RoomStatusEnum roomStatus, boolean isCheckedIn, RoomType roomRmType) {
        this.roomNumber = roomNumber;
        this.bookedDates = bookedDates;
        this.roomStatus = roomStatus;
        this.isCheckedIn = isCheckedIn;
        this.roomRmType = roomRmType;
    }
    
    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public List<Date> getBookedDates() {
        return bookedDates;
    }

    public void setBookedDates(List<Date> bookedDates) {
        this.bookedDates = bookedDates;
    }

    public RoomStatusEnum getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(RoomStatusEnum roomStatus) {
        this.roomStatus = roomStatus;
    }

    public boolean isIsCheckedIn() {
        return isCheckedIn;
    }

    public void setIsCheckedIn(boolean isCheckedIn) {
        this.isCheckedIn = isCheckedIn;
    }

    public RoomType getRoomRmType() {
        return roomRmType;
    }

    public void setRoomRmType(RoomType roomRmType) {
        this.roomRmType = roomRmType;
    }
    
    
    
    public Long getRoomId() {
        return roomId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomId != null ? roomId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomId fields are not set
        if (!(object instanceof Room)) {
            return false;
        }
        Room other = (Room) object;
        if ((this.roomId == null && other.roomId != null) || (this.roomId != null && !this.roomId.equals(other.roomId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Room[ id=" + roomId + " ]";
    }
    
}
