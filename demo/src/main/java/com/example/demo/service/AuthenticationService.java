package com.example.demo.service;

import com.example.demo.controller.dto.AuthRequest;
import com.example.demo.controller.dto.AuthResponse;

public interface AuthenticationService {

  AuthResponse authenticate(AuthRequest authRequest);
}
