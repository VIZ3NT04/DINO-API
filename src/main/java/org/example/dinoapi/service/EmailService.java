package org.example.dinoapi.service;

import com.resend.*;
import com.resend.services.emails.model.SendEmailRequest;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final Resend resend = new Resend("re_4MGGDcgj_2gbx7czHSAiErRNatctbwUNT");

    public void enviarCodigoVerificacion(String to, String codigo) throws Exception {
        SendEmailRequest params = SendEmailRequest.builder()
                .from("DinoApp <dinoappverify@gmail.com>")
                .to(to)
                .subject("Código de verificación")
                .text("Tu código de verificación es: " + codigo)
                .build();

        resend.emails().send(params);
    }
}
