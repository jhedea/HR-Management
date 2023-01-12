package nl.tudelft.sem.notification.microservice.services;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfig {
    @Value("${mail.host}")
    private transient String host;
    @Value("${mail.port}")
    private transient int port;
    @Value("${mail.username}")
    private transient String username;
    @Value("${mail.password}")
    private transient String password;
    @Value("${mail.protocol}")
    private transient String protocol = "smtp";
    @Value("${mail.smtp.auth}")
    private transient String auth = "true";
    @Value("${mail.smtp.starttls.enable}")
    private transient String starttls = "true";
    @Value("${mail.debug}")
    private transient String debug = "true";

    /**
     * Create a new JavaMailSender.
     *
     * @return a JavaMailSender bean.
     */
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(this.host);
        mailSender.setPort(this.port);
        mailSender.setUsername(this.username);
        mailSender.setPassword(this.password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", this.protocol);
        props.put("mail.smtp.auth", this.auth);
        props.put("mail.smtp.starttls.enable", this.starttls);
        props.put("mail.debug", this.debug);

        return mailSender;
    }
}
