package io.johnston.ppmtool.security;

public class SecurityConstants {

  public static final String SIGN_UP_URLS = "/api/users/**";
  public static final String H2_URL = "h2-console/**";
  public static final String SECRET = "SecrectKeyToGenJWTs";
  public static final String TOKEN_PREFIX = "Bearer "; // Need tailing space
  public static final String HEADER_STRING = "Authorization";
  public static final long EXPIRATION_TIME = 3000_000;
}
