package com.example.demo.controller;

import com.example.demo.controller.dto.AuthRequest;
import com.example.demo.controller.dto.AuthResponse;
import com.example.demo.service.AuthenticationService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentications")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PostMapping
  public AuthResponse authentication(@RequestBody @Valid AuthRequest authRequest) {
    return authenticationService.authenticate(authRequest);
  }


}
