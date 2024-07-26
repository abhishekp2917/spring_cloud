package org.example.utility;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class GeneralUtil {

    // function to extract Cookie from HTTP request
    public static String extractCookieFromRequest(HttpServletRequest request, String cookieName) {
        String cookieValue = null;
        // getting the list of cookies present in request
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            // iterating over cookie list
            // if the cookie name matches with what it is for JWT token, extract the cookie value
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    cookieValue = cookie.getValue();
                    break;
                }
            }
        }
        return cookieValue;
    }
}
