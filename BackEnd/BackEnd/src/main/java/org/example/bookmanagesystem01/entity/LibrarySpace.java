package org.example.bookmanagesystem01.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class LibrarySpace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int spaceId;

    private String spaceName;
    private int capacity;
    private String location;
    private int status;

    public LibrarySpace() {
    }

    public LibrarySpace(String spaceName, int capacity, String location, int status) {
        this.spaceName = spaceName;
        this.capacity = capacity;
        this.location = location;
        this.status = status;
    }

    public int getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(int spaceId) {
        this.spaceId = spaceId;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LibrarySpace{" +
                "spaceId=" + spaceId +
                ", spaceName='" + spaceName + '\'' +
                ", capacity=" + capacity +
                ", location='" + location + '\'' +
                ", status=" + status +
                '}';
    }
}