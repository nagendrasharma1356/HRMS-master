package com.papaya.EventManagement.DTO;


import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDate;
import java.time.LocalTime;

public class EventDto {

    private Long id;

    private String imageName;


    private String eventName;

    private String location;  // 'Where'

    @Column(length = 1000)
    private String description;

    private LocalDate startOnDate;

    private LocalTime startOnTime;

    private LocalDate endOnDate;

    private LocalTime endOnTime;

    private String department;

    private Boolean status;

    private String filePath;

    private String eventLink;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDate.now();

    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartOnDate() {
        return startOnDate;
    }

    public void setStartOnDate(LocalDate startOnDate) {
        this.startOnDate = startOnDate;
    }

    public LocalTime getStartOnTime() {
        return startOnTime;
    }

    public void setStartOnTime(LocalTime startOnTime) {
        this.startOnTime = startOnTime;
    }

    public LocalDate getEndOnDate() {
        return endOnDate;
    }

    public void setEndOnDate(LocalDate endOnDate) {
        this.endOnDate = endOnDate;
    }

    public LocalTime getEndOnTime() {
        return endOnTime;
    }

    public void setEndOnTime(LocalTime endOnTime) {
        this.endOnTime = endOnTime;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getEventLink() {
        return eventLink;
    }

    public void setEventLink(String eventLink) {
        this.eventLink = eventLink;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
