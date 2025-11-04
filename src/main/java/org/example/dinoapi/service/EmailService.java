package org.example.dinoapi.service;

import com.sendgrid.*;
import org.springframework.stereotype.Service;
import java.io.IOException;


@Service
public class EmailService {

    private static final String SENDGRID_API_KEY = "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";

    public void enviarCodigoVerificacion(String to, String codigo) throws IOException {
        Email from = new Email("dinoappverify@gmail.com");
        Email toEmail = new Email(to);
        String subject = "Código de verificación";
        Content content = new Content("text/plain", "Tu código de verificación es: " + codigo);
        Mail mail = new Mail(from, subject, toEmail, content);

        SendGrid sg = new SendGrid(SENDGRID_API_KEY);
        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sg.api(request);
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
        System.out.println(response.getHeaders());
    }
}

