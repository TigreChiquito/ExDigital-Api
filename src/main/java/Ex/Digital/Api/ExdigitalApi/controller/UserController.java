package Ex.Digital.Api.ExdigitalApi.controller;

import Ex.Digital.Api.ExdigitalApi.dto.ApiResponse;
import Ex.Digital.Api.ExdigitalApi.dto.UpdateUserRequest;
import Ex.Digital.Api.ExdigitalApi.dto.UserDto;
import Ex.Digital.Api.ExdigitalApi.security.CustomUserDetails;
import Ex.Digital.Api.ExdigitalApi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        try {
            List<UserDto> users = userService.getAllUsers();
            return ResponseEntity.ok(ApiResponse.success("Usuarios obtenidos exitosamente", users));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.userId")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Integer id) {
        try {
            UserDto user = userService.getUserById(id);
            return ResponseEntity.ok(ApiResponse.success("Usuario obtenido exitosamente", user));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserDto>> getProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            UserDto user = userService.getUserById(userDetails.getUserId());
            return ResponseEntity.ok(ApiResponse.success("Perfil obtenido exitosamente", user));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.userId")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateUserRequest request) {
        try {
            UserDto updatedUser = userService.updateUser(id, request);
            return ResponseEntity.ok(ApiResponse.success("Usuario actualizado exitosamente", updatedUser));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deactivateUser(@PathVariable Integer id) {
        try {
            userService.deactivateUser(id);
            return ResponseEntity.ok(ApiResponse.success("Usuario desactivado exitosamente", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> activateUser(@PathVariable Integer id) {
        try {
            userService.activateUser(id);
            return ResponseEntity.ok(ApiResponse.success("Usuario activado exitosamente", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}