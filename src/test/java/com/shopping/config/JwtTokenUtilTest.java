package com.shopping.config;


import com.shopping.config.jwt.JwtTokenUtil;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class JwtTokenUtilTest {

    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    void setUp() {
        jwtTokenUtil = new JwtTokenUtil("EXFYH2J3K5N6P7R9SATCVDWEYGZH2J4M5N6Q8R9SBUCVDXFYGZJ3K4M6P7");
    }


    @Test
    public void it_should_validate_token() {
        // given
        UserDetails userDetails = mock(UserDetails.class);
        given(userDetails.getUsername()).willReturn("stevecar");

        // when
        String token = jwtTokenUtil.generateToken(userDetails);
        boolean tokenValidated = jwtTokenUtil.validateToken(token, userDetails);

        // then
        assertTrue(tokenValidated);
    }

    @Test
    public void it_should_throw_exception_when_changed_signature_token() {
        // given
        UserDetails userDetails = mock(UserDetails.class);
        given(userDetails.getUsername()).willReturn("stevecar");

        // when
        String token = jwtTokenUtil.generateToken(userDetails).concat("test");

        // then
        assertThrows(SignatureException.class, () -> jwtTokenUtil.validateToken(token, userDetails));

    }

    @Test
    public void it_should_invalid_token(){
        // given
        UserDetails authenticationUser = mock(UserDetails.class);
        given(authenticationUser.getUsername()).willReturn("stevecar");
        UserDetails fakeUser = mock(UserDetails.class);
        given(fakeUser.getUsername()).willReturn("sTevecar");

        // when
        String token = jwtTokenUtil.generateToken(authenticationUser);
        boolean tokenValidated = jwtTokenUtil.validateToken(token,fakeUser);

        // then
        assertFalse(tokenValidated);
    }


}
