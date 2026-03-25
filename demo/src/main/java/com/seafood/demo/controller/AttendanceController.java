package com.seafood.demo.controller;

import com.seafood.demo.entity.Attendance;
import com.seafood.demo.entity.Role;
import com.seafood.demo.service.AttendanceService;
import com.seafood.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/attendances")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listAttendances(Model model) {
        model.addAttribute("attendances", attendanceService.getAllAttendances());
        return "attendances/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        Attendance attendance = new Attendance();
        attendance.setWorkDate(LocalDate.now());
        
        model.addAttribute("attendance", attendance);
        // Load all employees to select who to mark attendance for
        model.addAttribute("employees", userService.getUsersByRole(Role.EMPLOYEE));
        return "attendances/form";
    }

    @PostMapping("/save")
    public String saveAttendance(@ModelAttribute("attendance") Attendance attendance) {
        if (attendance.getId() == null && ("CÓ MẶT".equals(attendance.getStatus()) || "TRỄ".equals(attendance.getStatus()))) {
            attendance.setCheckInTime(LocalDateTime.now());
        }
        attendanceService.saveAttendance(attendance);
        return "redirect:/attendances";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        attendanceService.getAttendanceById(id).ifPresent(attendance -> {
            model.addAttribute("attendance", attendance);
            model.addAttribute("employees", userService.getUsersByRole(Role.EMPLOYEE));
        });
        return "attendances/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteAttendance(@PathVariable("id") Long id) {
        attendanceService.deleteAttendance(id);
        return "redirect:/attendances";
    }
}
