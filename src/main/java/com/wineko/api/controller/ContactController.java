package com.wineko.api.controller;

import com.wineko.api.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public String sendContactMessage(@RequestBody ContactForm contactForm) {
        String subject = "Message from " + contactForm.getName();
        String body = "Nom: " + contactForm.getName() + "\n"
                + "Prénom: " + contactForm.getPrenom() + "\n"
                + "Email: " + contactForm.getEmail() + "\n\n"
                + "Message: \n" + contactForm.getMessage();

        // Envoyer l'email à l'adresse de votre choix
        emailService.sendEmail("your-email@example.com", subject, body);

        return "Message sent successfully";
    }

    // Classe ContactForm définie dans le même fichier
    static class ContactForm {
        private String name;
        private String prenom;
        private String email;
        private String message;

        // Getters et setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrenom() {
            return prenom;
        }

        public void setPrenom(String prenom) {
            this.prenom = prenom;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}