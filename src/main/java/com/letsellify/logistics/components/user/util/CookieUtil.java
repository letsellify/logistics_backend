package com.letsellify.logistics.components.user.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author AHMAD BUBA
 * Date:6/19/25
 * Time:21:36
 */

public class CookieUtil {
    public static void addCookie(final HttpServletResponse response, final String domain, final String name, final String value, final int maxAge) {
        final Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(!domain.equalsIgnoreCase("localhost"));
        cookie.setPath("/");
        cookie.setDomain(domain);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
}
