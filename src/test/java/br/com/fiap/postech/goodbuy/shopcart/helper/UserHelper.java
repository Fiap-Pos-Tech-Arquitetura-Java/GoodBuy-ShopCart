package br.com.fiap.postech.goodbuy.shopcart.helper;

import br.com.fiap.postech.goodbuy.security.JwtService;
import br.com.fiap.postech.goodbuy.security.User;
import br.com.fiap.postech.goodbuy.security.UserDetailsImpl;
import br.com.fiap.postech.goodbuy.security.enums.UserRole;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

public class UserHelper {
    public static User getUser(String login, UserRole userRole) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return new User(
                login,
                encoder.encode("123456"),
                userRole
        );
    }

    public static String getToken(User user) {
        return "Bearer " + new JwtService().generateToken(user);
    }

    public static String getToken(String login, UserRole userRole) {
        return getToken(getUser(login, userRole));
    }

    public static UserDetails getUserDetails(User user) {
        return new UserDetailsImpl(user);
    }
}
