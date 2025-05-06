package com.letsellify.logistics.components.user.restController;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.components.user.core.authorizationToken.AuthorizationTokenManager;
import com.letsellify.logistics.components.user.core.logisticUser.UserManager;
import com.letsellify.logistics.components.user.core.logisticUser.data.LogisticsAppUser;
import com.letsellify.logistics.components.user.core.logisticUser.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:2/15/25
 * Time:16:52
 */

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class OAuth2Controller {
    private final UserManager userManager;
    private final AuthorizationTokenManager tokenManager;

    /**
     * Show the role selection form using Thymeleaf
     */
    @GetMapping
    public String showRoleSelectionPage(@RequestParam final String token, final Model model) {
        final String username = this.tokenManager.getUsernameFromToken(token);
        try {
            final LogisticsAppUser user = this.userManager.getUserByEmail(username);
            model.addAttribute("userName", user.getName());
            model.addAttribute("userEmail", user.getEmail());
        } catch (final UserNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Exclude ADMIN role
        final List<LogisticAppRole> availableRoles = Arrays.stream(LogisticAppRole.values())
                                                           .filter(role -> role != LogisticAppRole.ADMIN)
                                                           .toList();

        model.addAttribute("roles", availableRoles);
        model.addAttribute("token", token);

        return "role-selection";
    }

    /**
     * Handle Role Assignment
     */
    @PostMapping("/assign-role")
    public String assignRole(@RequestParam final String token, @RequestParam final LogisticAppRole role, final RedirectAttributes redirectAttributes) {
        try {
            final String username = this.tokenManager.getUsernameFromToken(token);
            this.userManager.assignRoleForAuthUser(username, role);
            redirectAttributes.addFlashAttribute("success", "Congratulations, " + username + "! Your role is assigned.");
            return "redirect:/dashboard/success";
        } catch (final Exception e) {
            redirectAttributes.addFlashAttribute("error", "Something went wrong.");
            return "redirect:/dashboard?token=" + token;
        }
    }

    /**
     * Success Page
     */
    @GetMapping("/success")
    public String showSuccessPage() {
        return "role-success";
    }

}
