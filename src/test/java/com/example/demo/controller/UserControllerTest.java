package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.impl.KeyCloakService;
import com.example.demo.utils.UserUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private KeyCloakService keyCloakService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        UserCreateDto dto = new UserCreateDto();
        dto.setUsername("testuser");

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername("testuser");

        when(keyCloakService.creatUser(dto)).thenReturn(userRepresentation);

        UserRepresentation result = userController.createUser(dto);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(keyCloakService, times(1)).creatUser(dto);
    }

    @Test
    void testSignIn() {
        UserSignInDto dto = new UserSignInDto();
        dto.setUsername("user");
        dto.setPassword("pass");

        Map<String, String> tokens = Map.of(
                "access_token", "access123",
                "refresh_token", "refresh123"
        );

        when(keyCloakService.signIn(dto)).thenReturn(tokens);

        ResponseEntity<AuthResponseDto> response = userController.signIn(dto);

        assertNotNull(response);
        assertEquals("access123", response.getBody().getAccessToken());
        assertEquals("refresh123", response.getBody().getRefreshToken());
        verify(keyCloakService, times(1)).signIn(dto);
    }

    @Test
    void testRefreshToken() {
        RefreshTokenRequestDto dto = new RefreshTokenRequestDto();
        dto.setRefreshToken("refresh123");

        Map<String, String> tokens = Map.of(
                "access_token", "newAccess",
                "refresh_token", "newRefresh"
        );

        when(keyCloakService.refreshToken("refresh123")).thenReturn(tokens);

        ResponseEntity<AuthResponseDto> response = userController.refresh(dto);

        assertNotNull(response);
        assertEquals("newAccess", response.getBody().getAccessToken());
        assertEquals("newRefresh", response.getBody().getRefreshToken());
        verify(keyCloakService, times(1)).refreshToken("refresh123");
    }

    @Test
    void testChangePassword_Success() {
        UserChangePasswordDto dto = new UserChangePasswordDto();
        dto.setNewPassword("newPass");

        mockStatic(UserUtils.class);
        when(UserUtils.getCurrentUserName()).thenReturn("testuser");

        doNothing().when(keyCloakService).changePassword("testuser", "newPass");

        ResponseEntity<String> response = userController.changePassword(dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Password Changed", response.getBody());
        verify(keyCloakService, times(1)).changePassword("testuser", "newPass");

        clearAllCaches();
    }

    @Test
    void testChangePassword_Unauthorized() {
        UserChangePasswordDto dto = new UserChangePasswordDto();

        mockStatic(UserUtils.class);
        when(UserUtils.getCurrentUserName()).thenReturn(null);

        ResponseEntity<String> response = userController.changePassword(dto);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Coulnd't Identify User", response.getBody());
    }
}
