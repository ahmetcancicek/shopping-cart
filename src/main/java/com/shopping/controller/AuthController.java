package com.shopping.controller;

import com.shopping.config.jwt.JwtTokenUtil;
import com.shopping.domain.dto.*;
import com.shopping.domain.model.User;
import com.shopping.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@AllArgsConstructor
@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final CustomerService customerService;

    @PostMapping("/api/auth/login")
    public ResponseEntity<ApiResponse<AuthToken>> login(@Valid @RequestBody AuthRequest request) throws Exception {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                ));

        final User user = (User) authenticate.getPrincipal();
        final String token = jwtTokenUtil.generateToken(user);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<AuthToken>(HttpStatus.OK.value(), "Authentication is successful!", new AuthToken(token, user.getUsername())));
    }

    @PostMapping("/api/auth/register")
    public ResponseEntity<ApiResponse<AuthToken>> register(@Valid @RequestBody RegistrationRequest request) throws Exception {
        customerService.save(request);
        return login(AuthRequest.builder().username(request.getUsername()).password(request.getPassword()).build());

    }
}
