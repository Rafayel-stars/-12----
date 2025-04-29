package org.example.bookmanagesystem01.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class SpaceApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int applicationId;

    private int spaceId;
    private String applicantName;
    private Date appointmentTime;
    private int duration;
    private int status;

    public SpaceApplication() {
    }

    public SpaceApplication(int spaceId, String applicantName, Date appointmentTime, int duration, int status) {
        this.spaceId = spaceId;
        this.applicantName = applicantName;
        this.appointmentTime = appointmentTime;
        this.duration = duration;
        this.status = status;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public int getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(int spaceId) {
        this.spaceId = spaceId;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public Date getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Date appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "SpaceApplication{" +
                "applicationId=" + applicationId +
                ", spaceId=" + spaceId +
                ", applicantName='" + applicantName + '\'' +
                ", appointmentTime=" + appointmentTime +
                ", duration=" + duration +
                ", status=" + status +
                '}';
    }
}