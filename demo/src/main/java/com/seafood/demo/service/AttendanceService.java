package com.seafood.demo.service;

import com.seafood.demo.entity.Attendance;

import java.util.List;
import java.util.Optional;

public interface AttendanceService {
    List<Attendance> getAllAttendances();
    List<Attendance> getAttendancesByEmployee(Long employeeId);
    Optional<Attendance> getAttendanceById(Long id);
    Attendance saveAttendance(Attendance attendance);
    void deleteAttendance(Long id);
}
