package com.seafood.demo.repository;

import com.seafood.demo.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByOrderByWorkDateDesc();
    List<Attendance> findByEmployeeIdOrderByWorkDateDesc(Long employeeId);
}
