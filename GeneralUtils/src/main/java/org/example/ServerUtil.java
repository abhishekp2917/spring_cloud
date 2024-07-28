package org.example;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;

/**
 * Utility class for server-related functions.
 * Provides methods to handle common tasks such as extracting cookies from HTTP requests.
 */
public class ServerUtil {

    /**
     * Extracts the value of a specified cookie from the HttpServletRequest.
     *
     * This method iterates through all cookies in the request and returns the value of the cookie
     * with the given name. If no matching cookie is found, the method returns null.
     *
     * @param request The HttpServletRequest object containing the cookies.
     * @param cookieName The name of the cookie to extract.
     * @return The value of the specified cookie, or null if the cookie is not present.
     */
    public static String extractCookieFromHttpRequest(HttpServletRequest request, String cookieName) {
        String cookieValue = null;
        // Retrieve all cookies from the HTTP request
        Cookie[] cookies = request.getCookies();
        // Check if cookies are present
        if (cookies != null) {
            // Iterate over the cookies
            for (Cookie cookie : cookies) {
                // Check if the current cookie matches the specified name
                if (cookie.getName().equals(cookieName)) {
                    // Extract the value of the matching cookie
                    cookieValue = cookie.getValue();
                    break;  // Exit the loop once the cookie is found
                }
            }
        }
        return cookieValue;  // Return the cookie value or null if not found
    }

    /**
     * Extracts the value of a specified cookie from the ServerHttpRequest.
     *
     * This method uses Spring's reactive WebFlux framework to retrieve the cookies from the request.
     * It returns the value of the first cookie with the given name. If no matching cookie is found,
     * the method returns null.
     *
     * @param request The ServerHttpRequest object containing the cookies.
     * @param cookieName The name of the cookie to extract.
     * @return The value of the specified cookie, or null if the cookie is not present.
     */
    public static String extractCookieFromHttpRequest(ServerHttpRequest request, String cookieName) {
        String cookieValue = null;
        // Retrieve all cookies from the reactive HTTP request
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        // Check if a cookie with the provided name is present
        if (cookies != null && cookies.containsKey(cookieName)) {
            // Extract the value of the first cookie in the list with the given name
            cookieValue = cookies.getFirst(cookieName).getValue();
        }
        return cookieValue;  // Return the cookie value or null if not found
    }
}
