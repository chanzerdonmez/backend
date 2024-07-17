package com.wineko.api.controller;

import com.wineko.api.dto.IdentificationDto;
import com.wineko.api.dto.UserDto;
import com.wineko.api.manager.Aleatoire;
import com.wineko.api.manager.JwtTokenManager;
import com.wineko.api.manager.WsException;
import com.wineko.api.model.RecaptchaResponse;
import com.wineko.api.model.Role;
import com.wineko.api.model.Users;
import com.wineko.api.service.EmailService;
import com.wineko.api.service.UsersService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/open")
@CrossOrigin(origins = "http://localhost:4200")
public class IdentificationController {

    private static final Logger logger = Logger.getLogger(IdentificationController.class.getName());

    @Autowired
    private UsersService usersService;

//    @Autowired
//    private RoleService roleService;

    @Value("${recaptcha.secret}")
    private String recaptchaSecret;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private EmailService emailService;

    @PostMapping("/login")
    public Map<String, String> identification(@RequestBody IdentificationDto identificationDto, HttpServletResponse response) {
        String msgError = "L'email ou le mot de passe est incorrect";

        // Vérifier si l'email existe
        Users users = usersService.findByEmail(identificationDto.getEmail());
        if (users == null) {
            logger.info("User not found: " + identificationDto.getEmail());
            throw new WsException(HttpStatus.NOT_FOUND, msgError);
        }

        // Vérifier si le mot de passe correspond
        if (!this.bCryptPasswordEncoder.matches(identificationDto.getPassword(), users.getPassword())) {
            logger.info("Invalid password for user: " + identificationDto.getEmail());
            throw new WsException(HttpStatus.NOT_FOUND, msgError);
        }

        // Vérifier si l'utilisateur a confirmé son email
        if (!users.isEnabled()) {
            logger.info("Email not confirmed for user: " + identificationDto.getEmail());
            throw new WsException(HttpStatus.BAD_REQUEST, "Veuillez confirmer votre email pour activer votre compte.");
        }

        // Générer le token
        String token = JwtTokenManager.generateToken(users.getToken());

        // Définir le cookie
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // Assurez-vous que votre application utilise HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60); // 24 heures
        response.addCookie(cookie);

        logger.info("User logged in: " + users.getEmail());

        // Retourner le token pour utilisation dans le frontend (facultatif)
        return Map.of("token", token);
    }



    /**
     * La méthode qui permet d'enregister un nouvel utilisateur
     * @param userDto
     * @return
     * <ul>
     *     <li><b>Exception</b> si l'email existe ....</li>
     *     <li><b>token</b> si l'utilisateur et bien enregister</li>
     * </ul>
     */

//    @PostMapping("/register")
//    public Map<String, String> register(@RequestBody UserDto userDto) {
//        if (userDto.getPassword().length() <= 4){
//            throw new WsException(HttpStatus.BAD_REQUEST, "Le mot de passe doit contenir au moins 8 caractères");
//        }
//
//        Users users = usersService.findByEmail(userDto.getEmail());
//        if (users != null) {
//            throw new WsException(HttpStatus.BAD_REQUEST, "Cet email existe déja");
//        }
//
//        users = userDto.getUser();
//        users.setPassword(this.bCryptPasswordEncoder.encode(userDto.getPassword()));
//        users.setRole(Role.CLIENT); // ou Role.ADMIN ou Role.SUPERADMIN selon votre logique d'application
//
////        users.setRoles(List.of(roleService.save("USER")));
//
//        // générer un token user
//        do {
//            users.setToken(Aleatoire.getRandomStr(50));
//        }while (usersService.findByToken(users.getToken()) != null);
//
//
//        usersService.save(users);
//
//        return Map.of("token", JwtTokenManager.generateToken(users.getToken()));
//
//    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody UserDto userDto) {
        if (userDto.getPassword().length() <= 4) {
            throw new WsException(HttpStatus.BAD_REQUEST, "Le mot de passe doit contenir au moins 8 caractères");
        }

        Users users = usersService.findByEmail(userDto.getEmail());
        if (users != null) {
            throw new WsException(HttpStatus.BAD_REQUEST, "Cet email existe déjà");
        }

        users = userDto.getUser();
        users.setPassword(this.bCryptPasswordEncoder.encode(userDto.getPassword()));
        users.setRole(Role.CLIENT);

        do {
            users.setToken(Aleatoire.getRandomStr(50));
        } while (usersService.findByToken(users.getToken()) != null);

        usersService.save(users);

        String confirmationUrl = "http://localhost:8080/api/open/confirm?token=" + users.getToken();
        logger.info("Sending confirmation email to " + users.getEmail());
        emailService.sendEmail(
                users.getEmail(),
                "Email de confirmation",
                "Cliquez sur le lien pour confirmer votre inscription : " + confirmationUrl
        );

        return Map.of("token", JwtTokenManager.generateToken(users.getToken()));
    }

    @GetMapping("/confirm")
    public String confirmUserAccount(@RequestParam("token") String confirmationToken) {
        Users user = usersService.findByToken(confirmationToken);

        if (user == null) {
            logger.info("Token invalide: " + confirmationToken);
            throw new WsException(HttpStatus.BAD_REQUEST, "Token invalide");
        }

        user.setEnabled(true);
        usersService.save(user);

        logger.info("User enabled: " + user.getEmail());
        return "Compte confirmé avec succès";
    }

}
