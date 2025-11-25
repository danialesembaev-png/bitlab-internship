package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.impl.KeyCloakService;
import com.example.demo.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {

    private final KeyCloakService keyCloakService;


    @PostMapping(value = "/create")
    @PreAuthorize("hasRole('ADMIN')")
    public UserRepresentation createUser(@RequestBody UserCreateDto userCreateDto) {
        return keyCloakService.creatUser(userCreateDto);

    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponseDto> signIn(@RequestBody UserSignInDto userSignInDto) {
        Map<String, String> tokens = keyCloakService.signIn(userSignInDto);
        AuthResponseDto response = AuthResponseDto.builder()
                .accessToken(tokens.get("access_token"))
                .refreshToken(tokens.get("refresh_token"))
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(@RequestBody RefreshTokenRequestDto refreshDto) {

        Map<String, String> tokens = keyCloakService.refreshToken(refreshDto.getRefreshToken());

        AuthResponseDto response = AuthResponseDto.builder()
                .accessToken(tokens.get("access_token"))
                .refreshToken(tokens.get("refresh_token"))
                .build();

        return ResponseEntity.ok(response);
    }


    @PostMapping(value = "/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> changePassword(@RequestBody UserChangePasswordDto userChangePasswordDto) {

        String currentUserName = UserUtils.getCurrentUserName();
        if (currentUserName == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Coulnd't Identify User");
        }

        try {

            keyCloakService.changePassword(currentUserName, userChangePasswordDto.getNewPassword());
            return ResponseEntity.ok("Password Changed");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error on changing password");
        }

    }

}
