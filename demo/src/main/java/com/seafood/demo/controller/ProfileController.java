package com.seafood.demo.controller;

import com.seafood.demo.entity.User;
import com.seafood.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String showProfile(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        
        // Fetch fresh data from DB
        userService.getUserById(loggedInUser.getId()).ifPresent(user -> {
            model.addAttribute("userProfile", user);
        });
        
        return "profile/index";
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute("userProfile") User profileDetails, 
                                HttpSession session, 
                                RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        // Only update specific allowed fields (cannot change role or username)
        userService.getUserById(loggedInUser.getId()).ifPresent(existing -> {
            existing.setFullName(profileDetails.getFullName());
            existing.setPhone(profileDetails.getPhone());
            existing.setAddress(profileDetails.getAddress());
            
            if (profileDetails.getPassword() != null && !profileDetails.getPassword().trim().isEmpty()) {
                existing.setPassword(profileDetails.getPassword().trim());
            }
            
            userService.updateUser(existing.getId(), existing);
            
            // Update session info
            session.setAttribute("loggedInUser", existing);
            session.setAttribute("user", existing.getUsername());
        });

        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin cá nhân thành công!");
        return "redirect:/profile";
    }
}
