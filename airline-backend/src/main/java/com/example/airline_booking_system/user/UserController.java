package com.example.airline_booking_system.user;

import com.example.airline_booking_system.common.response.ApiResponse;
import com.example.airline_booking_system.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/authorization")
    public ResponseEntity<Void> updateUserAuthorization(@PathVariable("id") Long userId,
                                                        @RequestParam Role role){
        userService.updateUserAuthorization(userId, role);
        return ResponseEntity.noContent().build();
    }

}
