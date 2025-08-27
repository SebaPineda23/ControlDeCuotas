package GestionPagoMensual.club.ConfigurationMail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfiguration {

    @Bean
    public JavaMailSender getJavaMailSender() {
        String host = System.getenv("SPRING_MAIL_HOST");
        String portStr = System.getenv("SPRING_MAIL_PORT");
        String username = System.getenv("SPRING_MAIL_USERNAME");
        String password = System.getenv("SPRING_MAIL_PASSWORD");
        String auth = System.getenv("SPRING_MAIL_SMTP_AUTH");
        String starttls = System.getenv("SPRING_MAIL_STARTTLS_ENABLE");
        String timeout = System.getenv("SPRING_MAIL_TIMEOUT");

        // Si alguna variable falta, logueamos y devolvemos un JavaMailSender vacío
        if (host == null || portStr == null || username == null || password == null) {
            System.out.println("[WARN] No se encontraron todas las variables de entorno para MailConfiguration. JavaMailSender no estará funcional.");
            return new JavaMailSenderImpl(); // Devuelve un bean vacío para que Spring no falle
        }

        int port = Integer.parseInt(portStr);

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", auth != null ? auth : "true");
        props.put("mail.smtp.starttls.enable", starttls != null ? starttls : "true");
        props.put("mail.smtp.timeout", timeout != null ? timeout : "2000");

        System.out.println("[INFO] JavaMailSender configurado correctamente con host: " + host);
        return mailSender;
    }
}

