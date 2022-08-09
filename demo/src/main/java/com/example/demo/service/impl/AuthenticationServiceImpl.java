package com.example.demo.service.impl;

import static com.example.demo.exception.DomainCode.CREDENTIALS_EXCEPTION;

import com.example.demo.controller.dto.AuthRequest;
import com.example.demo.controller.dto.AuthResponse;
import com.example.demo.exception.DomainBusinessException;
import com.example.demo.security.JwtTokenUtil;
import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private final CustomUserDetailsService userDetailsService;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenUtil jwtTokenUtil;

  @Override
  public AuthResponse authenticate(AuthRequest authRequest) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(authRequest.getUsername(),
              authRequest.getPassword()));

      final UserDetails userDetails = userDetailsService
          .loadUserByUsername(authRequest.getUsername());

      final String token = jwtTokenUtil.generateToken(userDetails);

      return AuthResponse.builder().token(token).build();
    } catch (AuthenticationException e) {
      throw new DomainBusinessException(CREDENTIALS_EXCEPTION);
    }
  }
}
