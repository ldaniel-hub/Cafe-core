package br.edu.graosapi.controller;

import br.edu.graosapi.dto.LoginRequest;
import br.edu.graosapi.dto.LoginResponse;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtEncoder jwtEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).build();
        }
        Instant now = Instant.now();
        long duration = 3600;
        String scopes = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .filter(authority -> authority.startsWith("SCOPE_"))
            .map(authority -> authority.substring(6))
            .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("graosapi-local")
            .subject(authentication.getName())
            .issuedAt(now)
            .expiresAt(now.plusSeconds(duration))
            .claim("scope", scopes)
            .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
        return ResponseEntity.ok(new LoginResponse(token, "Bearer", duration));
    }
}
