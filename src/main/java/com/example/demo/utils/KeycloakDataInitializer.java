package com.example.demo.utils;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class KeycloakDataInitializer {

    private final Keycloak keycloak;

    @PostConstruct
    public void init() {
        log.info("Init KeycloakDataInitializer");
        String realmName = "bitlab-realm";
        RealmResource realm = keycloak.realm(realmName);

        createRoleIfNotExists(realm, "ADMIN");
        createRoleIfNotExists(realm, "TEACHER");
        createRoleIfNotExists(realm, "USER");

        createAdminUser(realm);
    }


    private void createRoleIfNotExists(RealmResource realm, String roleName) {
        try {
            realm.roles().get(roleName).toRepresentation();
            log.info("Role already exists: {}", roleName);
        } catch (javax.ws.rs.NotFoundException e) {
            RoleRepresentation role = new RoleRepresentation();
            role.setName(roleName);
            realm.roles().create(role);
            log.info("Created role: {}", roleName);

        }
    }


    private void createAdminUser(RealmResource realm) {

        UserRepresentation adminUser = realm.users()
                .search("admin").stream()
                .filter(u -> "admin".equals(u.getUsername()))
                .findFirst()
                .orElse(null);

        log.info("Find admin: {}", adminUser);
        if (adminUser != null) {
            log.info("Admin user already exists");
            return;
        }

        log.info("Creating admin user");
        UserRepresentation admin = new UserRepresentation();
        admin.setUsername("admin");
        admin.setEnabled(true);
        admin.setFirstName("Super");
        admin.setLastName("Admin");
        admin.setEmail("admin@example.com");
        admin.setEmailVerified(true);
        admin.setEnabled(true);

        javax.ws.rs.core.Response response = realm.users().create(admin);
        String userId = CreatedResponseUtil.getCreatedId(response);

        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setTemporary(false);
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue("your_secure_initial_password");

        realm.users().get(userId).resetPassword(cred);

        RoleRepresentation adminRole = realm.roles().get("ADMIN").toRepresentation();
        realm.users().get(userId).roles().realmLevel().add(Collections.singletonList(adminRole));

        assignBuiltInAdminRoles(realm, userId);

        log.info("Admin user created with ROLE_ADMIN + Keycloak built-in roles.");
    }


    private void assignBuiltInAdminRoles(RealmResource realm, String userId) {

        String clientId = realm.clients()
                .findByClientId("realm-management")
                .getFirst()
                .getId();

        String[] builtInRoles = {
                "manage-realm",
                "manage-users",
                "view-users",
                "create-client"
        };

        var clientRolesResource = realm.clients().get(clientId).roles();

        List<RoleRepresentation> roleReps = java.util.Arrays.stream(builtInRoles)
                .map(roleName -> clientRolesResource.get(roleName).toRepresentation())
                .toList();

        realm.users().get(userId)
                .roles()
                .clientLevel(clientId)
                .add(roleReps);

        log.info("Assigned Keycloak built-in admin roles: manage-realm, manage-users, view-users, create-client");
    }
}


