package com.passwordchecker.controller;

import com.passwordchecker.dto.PasswordReport;
import com.passwordchecker.dto.PasswordRequest;
import com.passwordchecker.service.PasswordService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * MVC Controller that handles password validation HTTP requests.
 *
 * <p>Exposes two endpoints:</p>
 * <ul>
 *   <li>{@code GET /} — Renders the password input form</li>
 *   <li>{@code POST /check} — Processes the form and renders the validation report</li>
 * </ul>
 *
 * <p>Dependencies are injected via constructor injection only.
 * The controller delegates all business logic to the {@link PasswordService}.</p>
 *
 * @author Password Checker Team
 * @version 1.0.0
 */
@Controller
public class PasswordController {

    private static final Logger logger = LoggerFactory.getLogger(PasswordController.class);

    private final PasswordService passwordService;

    /**
     * Constructs the controller with the required service dependency.
     *
     * @param passwordService the password validation service
     */
    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    /**
     * Displays the password validation form.
     *
     * @param model the Spring MVC model
     * @return the Thymeleaf template name
     */
    @GetMapping("/")
    public String showForm(Model model) {
        logger.info("Rendering password validation form");
        model.addAttribute("passwordRequest", new PasswordRequest());
        return "index";
    }

    /**
     * Processes the password validation form submission.
     *
     * <p>Receives the form data, delegates validation to the service layer,
     * and adds the resulting report to the model for Thymeleaf rendering.</p>
     *
     * @param passwordRequest the form data bound from the POST body
     * @param model           the Spring MVC model
     * @return the Thymeleaf template name
     */
    @PostMapping("/check")
    public String checkPassword(@ModelAttribute("passwordRequest") PasswordRequest passwordRequest,
                                 Model model) {
        logger.info("Received password check request for user: {}", passwordRequest.getUsername());

        PasswordReport report = passwordService.validatePassword(
                passwordRequest.getPassword(),
                passwordRequest.getUsername());

        model.addAttribute("passwordRequest", passwordRequest);
        model.addAttribute("report", report);

        logger.info("Returning validation report: score={}, strength={}",
                report.getScore(), report.getStrength().getLabel());

        return "index";
    }
}
