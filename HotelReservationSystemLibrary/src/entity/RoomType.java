/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import enumerations.RoomRateTypeEnum;
import enumerations.RoomStatusEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Tan Jian Feng
 */
@Entity
public class RoomType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomTypeId;

    @Column(nullable = false)
    @NotNull
    private String name;

    @Column(nullable = false)
    @Size(min = 1, max = 255)
    private String description;

    @Column(nullable = false)
    @Digits(integer = 7, fraction = 3)
    @DecimalMax("1000.000")
    private BigDecimal size;

    @Column(nullable = false)
    @NotNull
    @Size(min = 1, max = 32)
    private String bed;

    @Column(nullable = false)
    @NotNull
    @DecimalMax("6")
    @Digits(integer = 1, fraction = 0)
    private BigDecimal capacity;

    @NotEmpty(message = "Input list cannot be empty")
    private List<@Size(min = 1, max = 30) String> amenities;

    @Enumerated(EnumType.STRING)
    @NotNull
    private RoomStatusEnum statusType;

    @OneToMany(mappedBy = "roomRmType")
    private List<Room> rooms;

    @OneToMany
    private List<RoomRate> roomRates;

    public RoomType() {
    }

    public RoomType(String name, String description, BigDecimal size, String bed, BigDecimal capacity,
            List<String> amenities, RoomStatusEnum statusType, List<Room> rooms, List<RoomRate> roomRates) {
        this.name = name;
        this.description = description;
        this.size = size;
        this.bed = bed;
        this.capacity = capacity;
        this.amenities = amenities;
        this.statusType = statusType;
        this.rooms = rooms;
        this.roomRates = roomRates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getSize() {
        return size;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }

    public String getBed() {
        return bed;
    }

    public void setBed(String bed) {
        this.bed = bed;
    }

    public BigDecimal getCapacity() {
        return capacity;
    }

    public void setCapacity(BigDecimal capacity) {
        this.capacity = capacity;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    public RoomStatusEnum getStatusType() {
        return statusType;
    }

    public void setStatusType(RoomStatusEnum statusType) {
        this.statusType = statusType;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public void addRoom(Room room) {
        this.rooms.add(room);
    }

    public List<RoomRate> getRoomRates() {
        return roomRates;
    }

    public void setRoomRates(List<RoomRate> roomRates) {
        this.roomRates = roomRates;
    }

    public void addRoomRates(RoomRate roomRate) {
        this.roomRates.add(roomRate);
    }

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomTypeId != null ? roomTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomTypeId fields are
        // not set
        if (!(object instanceof RoomType)) {
            return false;
        }
        RoomType other = (RoomType) object;
        if ((this.roomTypeId == null && other.roomTypeId != null)
                || (this.roomTypeId != null && !this.roomTypeId.equals(other.roomTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomType[ id=" + roomTypeId + " ]";
    }

}
