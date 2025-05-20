package org.example.dinoapi.utils;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Component
public class SupabaseUploader {

    private final String SUPABASE_URL = "https://twsfgasbtdnymjoticwk.supabase.co";
    private final String BUCKET = "uploads";
    private final String SERVICE_ROLE_KEY = "Bearer eyJhbGciOi..."; // ⚠️ reemplaza en producción

    public String subir(MultipartFile archivo, String nombreArchivo) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.set("Authorization", SERVICE_ROLE_KEY);
            headers.set("x-upsert", "true"); // permite sobrescribir si ya existe

            HttpEntity<byte[]> requestEntity = new HttpEntity<>(archivo.getBytes(), headers);

            String url = SUPABASE_URL + "/storage/v1/upload/object/" + BUCKET + "/" + nombreArchivo;

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Error al subir archivo a Supabase: " + response.getBody());
            }

            return SUPABASE_URL + "/storage/v1/object/public/" + BUCKET + "/" + nombreArchivo;

        } catch (Exception e) {
            throw new RuntimeException("Error subiendo a Supabase: " + e.getMessage(), e);
        }
    }
}
