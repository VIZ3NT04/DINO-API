package org.example.dinoapi.service;

import com.resend.*;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final Resend resend = new Resend(System.getenv("RESEND_API_KEY"));

    public void enviarCodigoVerificacion(String to, String codigo) throws Exception {
        EmailParams params = EmailParams.builder()
                .from("DinoApp <dinoappverify@gmail.com>")
                .to(to)
                .subject("Código de verificación")
                .text("Tu código de verificación es: " + codigo)
                .build();

        resend.emails().send(params);
    }
}
