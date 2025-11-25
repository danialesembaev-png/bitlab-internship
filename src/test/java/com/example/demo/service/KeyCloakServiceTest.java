package com.example.demo.service;

import com.example.demo.dto.UserSignInDto;
import com.example.demo.service.impl.KeyCloakService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class KeyCloakServiceTest {

    @Mock
    private Keycloak keycloak;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private KeyCloakService keyCloakService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(keyCloakService, "url", "http://localhost:8080");
        ReflectionTestUtils.setField(keyCloakService, "realm", "test-realm");
        ReflectionTestUtils.setField(keyCloakService, "client", "client-id");
        ReflectionTestUtils.setField(keyCloakService, "clientSecret", "secret");
    }


    @Test
    void testSignIn_success() {

        UserSignInDto dto = new UserSignInDto();
        dto.setUsername("john");
        dto.setPassword("1234");

        Map<String, String> fakeResponse = Map.of(
                "access_token", "ACCESS_TOKEN_HERE",
                "refresh_token", "REFRESH_TOKEN_HERE"
        );

        ResponseEntity<Map> responseEntity =
                new ResponseEntity<>(fakeResponse, HttpStatus.OK);

        when(restTemplate.postForEntity(
                anyString(),
                any(HttpEntity.class),
                eq(Map.class)
        )).thenReturn(responseEntity);

        Map<String, String> tokens = keyCloakService.signIn(dto);

        assertEquals("ACCESS_TOKEN_HERE", tokens.get("access_token"));
        assertEquals("REFRESH_TOKEN_HERE", tokens.get("refresh_token"));

        verify(restTemplate).postForEntity(
                eq("http://localhost:8080/realms/test-realm/protocol/openid-connect/token"),
                any(HttpEntity.class),
                eq(Map.class)
        );
    }

    @Test
    void testRefreshToken_success() {

        Map<String, Object> fakeResponse = Map.of(
                "access_token", "NEW_ACCESS",
                "refresh_token", "NEW_REFRESH"
        );

        ResponseEntity<Map> response = new ResponseEntity<>(fakeResponse, HttpStatus.OK);

        when(restTemplate.postForEntity(
                anyString(),
                any(HttpEntity.class),
                eq(Map.class)
        )).thenReturn(response);

        Map<String, String> tokens = keyCloakService.refreshToken("OLD_REFRESH");

        assertEquals("NEW_ACCESS", tokens.get("access_token"));
        assertEquals("NEW_REFRESH", tokens.get("refresh_token"));

        verify(restTemplate).postForEntity(
                eq("http://localhost:8080/realms/test-realm/protocol/openid-connect/token"),
                any(HttpEntity.class),
                eq(Map.class)
        );
    }
}
