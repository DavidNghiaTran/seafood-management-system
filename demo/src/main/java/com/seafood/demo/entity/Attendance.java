package com.seafood.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendances")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User employee;

    @Column(nullable = false)
    private LocalDate workDate;

    // e.g., "Sáng", "Chiều", "Tối"
    @Column(nullable = false)
    private String shift;

    // e.g., "CÓ MẶT", "VẮNG MẶT", "TRỄ"
    @Column(nullable = false)
    private String status;

    private LocalDateTime checkInTime;
    
    private String notes;

    public Attendance() {
    }

    public Attendance(User employee, LocalDate workDate, String shift, String status) {
        this.employee = employee;
        this.workDate = workDate;
        this.shift = shift;
        this.status = status;
        if ("CÓ MẶT".equals(status) || "TRỄ".equals(status)) {
            this.checkInTime = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getEmployee() {
        return employee;
    }

    public void setEmployee(User employee) {
        this.employee = employee;
    }

    public LocalDate getWorkDate() {
        return workDate;
    }

    public void setWorkDate(LocalDate workDate) {
        this.workDate = workDate;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
