package com.example.demo.security;

import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String tokenHeader = request.getHeader("Authorization");
    String username = null;
    String token = null;
    if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
      token = tokenHeader.substring(7);
      try {
        username = jwtTokenUtil.getUsernameFromToken(token);
      } catch (IllegalArgumentException e) {
        System.out.println("Unable to get JWT Token");
      } catch (ExpiredJwtException e) {
        System.out.println("JWT Token has expired");
      }
    } else {
      System.out.println("Bearer String not found in token");
    }
    if (null != username && SecurityContextHolder.getContext().getAuthentication() == null) {
      if (jwtTokenUtil.validateToken(token, username)) {
        var roles = (List<Map<String, String>>) jwtTokenUtil.getClaimFromToken(token, (claims -> claims.get("roles")));
        UsernamePasswordAuthenticationToken
            authenticationToken = new UsernamePasswordAuthenticationToken(null,null, extractRoles(roles));
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      }
    }
    filterChain.doFilter(request, response);
  }

  private Collection<? extends GrantedAuthority> extractRoles(List<Map<String, String>> roles) {
    return roles.stream().map(item -> new SimpleGrantedAuthority(item.get("authority"))).collect(
        Collectors.toList());
  }

}
