package rs.ac.uns.ftn.onlybunsapp.service.impl;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.onlybunsapp.model.User;

import java.util.List;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("onlybuns.noreply@gmail.com");
        message.setTo(to);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);

        System.out.println("Mail sent successfully.");
    }

    public void sendAccountActivationEmail(User user) {
        sendEmail(user.getEmail(), "Activate Your OnlyBuns Account",
                "Hi " + user.getFirstName() + ",\n\n" +
                        "Welcome to OnlyBuns! Thank you for signing up. To get started, please activate your account by clicking the link below:\n\n" +
                        "http://localhost:4200/successfully-activated?userId=" + user.getId() + "\n\n" +
                        "Why activate?\n" +
                        "Activating your account helps keep your profile secure and gives you access to all the features that OnlyBuns has to offer.\n\n" +
                        "If you didn't sign up for an account, please ignore this email.\n\n" +
                        "Cheers,\n" +
                        "The OnlyBuns Team");
    }

}
