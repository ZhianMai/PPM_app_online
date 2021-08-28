package io.johnston.ppmtool.security;

import io.johnston.ppmtool.domain.User;
import io.johnston.ppmtool.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static io.johnston.ppmtool.security.SecurityConstants.HEADER_STRING;
import static io.johnston.ppmtool.security.SecurityConstants.TOKEN_PREFIX;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  private CustomUserDetailsService customUserDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                  HttpServletResponse httpServletResponse,
                                  FilterChain filterChain) throws ServletException, IOException {

    try {
      String jwt = getJWTFromRequest(httpServletRequest);

      if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
        // Get user id from JWT
        Long userId = jwtTokenProvider.getUserIdFromJWT(jwt);
        User userDetails = customUserDetailsService.loadUserById(userId);

        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(userDetails, null, Collections.emptyList());

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    } catch (Exception exception) {
      logger.error("Could not set user authentication in security context", exception);
    }

    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }

  private String getJWTFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader(HEADER_STRING);

    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
      return bearerToken.substring(TOKEN_PREFIX.length(), bearerToken.length());
    }

    return null;
  }
}
