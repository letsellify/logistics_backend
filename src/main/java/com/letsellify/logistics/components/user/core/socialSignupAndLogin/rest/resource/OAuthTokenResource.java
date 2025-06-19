package com.letsellify.logistics.components.user.core.socialSignupAndLogin.rest.resource;

/**
 * @author AHMAD BUBA
 * Date:6/19/25
 * Time:22:07
 */

public record OAuthTokenResource(
  String issuer,
  String accessToken,
  String refreshToken
) {}
