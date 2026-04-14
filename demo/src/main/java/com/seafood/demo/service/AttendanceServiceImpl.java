package com.seafood.demo.service;

import com.seafood.demo.entity.Attendance;
import com.seafood.demo.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Xử lý logic điểm danh thông qua gọi trực tiếp đối tượng DB (Repository).
 */
@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    /**
     * Lấy toàn bộ lịch sử điểm danh và sắp xếp theo ngày làm việc giảm dần (Gần nhất đưa lên đầu).
     */
    @Override
    public List<Attendance> getAllAttendances() {
        return attendanceRepository.findByOrderByWorkDateDesc();
    }

    /**
     * Lấy lịch sử điểm danh của riêng một nhân viên.
     */
    @Override
    public List<Attendance> getAttendancesByEmployee(Long employeeId) {
        return attendanceRepository.findByEmployeeIdOrderByWorkDateDesc(employeeId);
    }

    @Override
    public Optional<Attendance> getAttendanceById(Long id) {
        return attendanceRepository.findById(id);
    }

    @Override
    public Attendance saveAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    @Override
    public void deleteAttendance(Long id) {
        attendanceRepository.deleteById(id);
    }
}
