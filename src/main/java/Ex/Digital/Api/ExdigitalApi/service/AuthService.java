package Ex.Digital.Api.ExdigitalApi.service;

import Ex.Digital.Api.ExdigitalApi.dto.AuthResponse;
import Ex.Digital.Api.ExdigitalApi.dto.LoginRequest;
import Ex.Digital.Api.ExdigitalApi.dto.RegisterRequest;
import Ex.Digital.Api.ExdigitalApi.entity.Role;
import Ex.Digital.Api.ExdigitalApi.entity.User;
import Ex.Digital.Api.ExdigitalApi.repository.RoleRepository;
import Ex.Digital.Api.ExdigitalApi.repository.UserRepository;
import Ex.Digital.Api.ExdigitalApi.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Verificar si el email ya existe
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Buscar rol de usuario (por defecto "USER")
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));

        // Crear nuevo usuario
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(userRole);
        user.setIsActive(true);

        User savedUser = userRepository.save(user);

        // Generar token
        String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getRole().getName());

        return new AuthResponse(
                token,
                savedUser.getIdUser(),
                savedUser.getEmail(),
                savedUser.getName(),
                savedUser.getRole().getName()
        );
    }

    public AuthResponse login(LoginRequest request) {
        // Autenticar usuario
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Buscar usuario
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar si está activo
        if (!user.getIsActive()) {
            throw new RuntimeException("Usuario desactivado");
        }

        // Generar token
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().getName());

        return new AuthResponse(
                token,
                user.getIdUser(),
                user.getEmail(),
                user.getName(),
                user.getRole().getName()
        );
    }
}