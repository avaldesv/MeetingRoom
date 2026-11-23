package com.meetingroom.room.infrastructure.persistence.models;

import com.meetingroom.common.persistence.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "room")
public class RoomModel extends BaseModel {

    @Column(nullable = false, length = 120)
    private String name;

    @Column(length = 255)
    private String location;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private boolean active = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
