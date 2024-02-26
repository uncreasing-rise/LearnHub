package com.example.learnhub.Controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

public class CustomError implements ErrorController {

    
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Get the error attributes
        Map<String, Object> errorAttributes = (Map<String, Object>) request.getAttribute(
                RequestDispatcher.ERROR_REQUEST_URI);

        // Add custom error handling logic here, if needed
        model.addAttribute("status", errorAttributes.get("status"));
        model.addAttribute("error", errorAttributes.get("error"));

        // Return the custom error view
        return "error"; // Create an "error.html" template in your "src/main/resources/templates" directory
    }

    
    public String getErrorPath() {
        return "/error";
    }
}
