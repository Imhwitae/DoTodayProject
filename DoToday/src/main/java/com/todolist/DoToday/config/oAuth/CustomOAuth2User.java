//package com.todolist.DoToday.config.oAuth;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.Map;
//
//@Getter
//public class CustomOAuth2User extends DefaultOAuth2User {
//    private String registrationId;
//    private String email;
//
//    /**
//     * Constructs a {@code DefaultOAuth2User} using the provided parameters.
//     *
//     * @param authorities      the authorities granted to the user
//     * @param attributes       the attributes about the user
//     * @param nameAttributeKey the key used to access the user's &quot;name&quot; from
//     *                         {@link #getAttributes()}
//     */
//    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey,
//                            String email, String registrationId) {
//        super(authorities, attributes, nameAttributeKey);
//        this.email = email;
//        this.registrationId = registrationId;
//    }
//
//}
