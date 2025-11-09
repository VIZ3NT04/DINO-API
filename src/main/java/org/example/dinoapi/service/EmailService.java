package org.example.dinoapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCodigoVerificacion(String to, String codigo) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom("dinoappverify@gmail.com");
        mensaje.setTo(to);
        mensaje.setSubject("Código de verificación");
        mensaje.setText("Tu código de verificación es: " + codigo);
        mailSender.send(mensaje);
    }

    @Scheduled(cron = "0 0 12 1 * ?")
    public void correoMantenimiento() {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom("dinoappverify@gmail.com");
        mensaje.setTo("dinoappverify@gmail.com"); // Se lo envías a ti mismo
        mensaje.setSubject("Ping de mantenimiento");
        mensaje.setText("Este correo es solo para mantener la cuenta activa.");
        mailSender.send(mensaje);
    }
}
