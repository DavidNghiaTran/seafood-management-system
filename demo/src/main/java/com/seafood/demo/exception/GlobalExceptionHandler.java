package com.seafood.demo.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MaxUploadSizeExceededException.class, org.springframework.web.multipart.MultipartException.class})
    public String handleMaxSizeException(Exception exc, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "Không nhập file nào khác ngoài .xlsx (Lỗi: File tải lên quá lớn hoặc không đúng định dạng)!");
        
        String referer = request.getHeader("referer");
        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        }
        return "redirect:/"; // fallback
    }
}
