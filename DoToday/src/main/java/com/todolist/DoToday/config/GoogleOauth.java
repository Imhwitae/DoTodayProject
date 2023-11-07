//package com.todolist.DoToday.config;
//
//import lombok.Getter;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//@Component
//@Getter
//public class GoogleOauth {
//
//    @Value("${app.google.clientId}")
//    private String clientId;
//    @Value("${app.google.secret}")
//    private String clientSecret;
//    @Value("${app.google.redirect}")
//    private String googleRedirectUrl;
//
//    private final String TRY_GOOGLE_LOGIN_URL = "https://accounts.google.com/o/oauth2/auth?" +
//            "client_id=" + clientId +
//            "&redirect_uri=" + googleRedirectUrl +
//            "&response_type=code" +
//            "&scope=email profile" +
//            "&access_type=offline";
//
//    private final String GOOGLE_REQUEST_TOKEN_URL = "https://oauth2.googleapis.com/token";
//
//    private final String GOOGLE_GET_USER_INFO_URL = "https://www.googleapis.com/userinfo/v2/me";
//}
