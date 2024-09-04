package com.wineko.api.service;

import com.wineko.api.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;


@Service
public class EmailService {

    private static final Logger logger = Logger.getLogger(EmailService.class.getName());

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        try {
            javaMailSender.send(message);
            logger.info("Email sent successfully to " + to);
        } catch (Exception e) {
            logger.severe("Failed to send email to " + to + ": " + e.getMessage());
        }
    }


    public void sendConfirmationUpdateEmail(String oldEmail, String newEmail, String firstName) {
        String subject = "Confirmation de changement d'adresse email";
        String message = String.format("Bonjour %s,\n\nVotre adresse email a été changée de %s à %s.\n\nCordialement,\nL'équipe de notre site.", firstName, oldEmail, newEmail);

        // Envoyer un email à l'ancienne adresse email pour l'informer du changement
        sendEmail(oldEmail, subject, message);

        // Envoyer un email à la nouvelle adresse email pour confirmation
        sendEmail(newEmail, subject, message);
    }

    // Méthode pour envoyer la confirmation de changement de mot de passe
    public void sendConfirmationUpdatePassword(Users user) {
        String subject = "Confirmation de changement de mot de passe";
        String message = String.format("Bonjour %s,\n\nVotre mot de passe a été changé avec succès.\n\nCordialement,\nL'équipe de notre site.", user.getFirstName());

        // Envoyer un email à l'utilisateur pour confirmer que le mot de passe a été changé
        sendEmail(user.getEmail(), subject, message);
    }
}
