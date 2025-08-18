package com.letsellify.logistics.common.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

/**
 * @author AHMAD BUBA
 * Date:6/19/25
 * Time:21:36
 */

public class CookieHandler {
    public static void addCookie(final HttpServletResponse response, final String domain, final String name, final String value, final int maxAge) {
        final String encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8);
        final StringBuilder cookieBuilder = new StringBuilder();
        cookieBuilder.append(name).append("=").append(encodedValue).append(";");
        cookieBuilder.append(" Max-Age=").append(maxAge).append(";");
        cookieBuilder.append(" Path=/;");

        final boolean isLocalhost = "localhost".equalsIgnoreCase(domain);

        if (!isLocalhost) {
            cookieBuilder.append(" Domain=").append(domain).append(";");
            cookieBuilder.append(" Secure;");
            cookieBuilder.append(" SameSite=None;");
        } else {
            cookieBuilder.append(" SameSite=Lax;");
            // Do NOT set Secure for localhost, or the cookie won't be saved in HTTP
        }

        cookieBuilder.append(" HttpOnly;");

        response.addHeader("Set-Cookie", cookieBuilder.toString());
    }

    public static void clearCookie(final HttpServletResponse httpServletResponse, final String domain, final String name) {
        Cookie cookie = new Cookie(name, StringUtils.EMPTY);
        cookie.setHttpOnly(true);
        cookie.setSecure("localhost".equalsIgnoreCase(domain));
        cookie.setPath("/");
        if (!"localhost".equalsIgnoreCase(domain)) {
            cookie.setDomain(domain);
        }
        cookie.setMaxAge(0);
        httpServletResponse.addCookie(cookie);
    }
//    public static void addCookie(final HttpServletResponse response, final String domain, final String name, final String value, final int maxAge) {
//        final Cookie cookie = new Cookie(name, value);
//        cookie.setHttpOnly(true);
//        cookie.setSecure(!domain.equalsIgnoreCase("localhost"));
//        cookie.setPath("/");
//        cookie.setMaxAge(maxAge);
//        if (!"localhost".equalsIgnoreCase(domain)) {
//            cookie.setDomain(domain);
//        }
//        response.addCookie(cookie);
//    }
}
