package com.greedyraccoon.fake_image_detector.service;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageDetectionService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    // Using Gemini 1.5 Flash - it is extremely fast and multimodal
    private final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=";

    public String analyzeImage(MultipartFile file) {
        try {
            // 1. Convert the raw image into a Base64 String
            byte[] imageBytes = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            
            String mimeType = file.getContentType();
            if (mimeType == null) mimeType = "image/jpeg";

            // 2. Build the image payload
            Map<String, Object> inlineData = new HashMap<>();
            inlineData.put("mime_type", mimeType);
            inlineData.put("data", base64Image);

            Map<String, Object> imagePart = new HashMap<>();
            imagePart.put("inline_data", inlineData);

            // 3. Give Gemini strict instructions so it formats the output for your frontend
            Map<String, Object> textPart = new HashMap<>();
            textPart.put("text", "You are an expert AI image detector. Analyze this image for AI watermarks (like the Craiyon logo), weird fingers, or illogical geometry. Respond ONLY with a raw JSON array. Calculate a realistic score between 0.50 and 0.99. If it is AI, use the exact label 'artificial'. If it is a real photograph, use 'human'. Add a short, punchy 1-sentence 'reason' explaining exactly what gave it away. Use EXACTLY this format: [{\"label\": \"artificial\", \"score\": 0.98, \"reason\": \"The glowing neon lines are structurally illogical and there is a visible Craiyon watermark in the bottom right corner.\"}] Do NOT use markdown. Reply with ONLY the JSON array.");

            // 4. Package it all up into the final JSON structure Google expects
            Map<String, Object> content = new HashMap<>();
            content.put("parts", List.of(textPart, imagePart));

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("contents", List.of(content));

            // 5. Fire the request to Google's servers
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    GEMINI_API_URL + geminiApiKey,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            // 6. Extract the text response from Gemini's nested JSON reply
            Map<String, Object> responseBody = response.getBody();
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");
            Map<String, Object> contentMap = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) contentMap.get("parts");
            String geminiTextOutput = (String) parts.get(0).get("text");

            return geminiTextOutput;

        } catch (Exception e) {
            e.printStackTrace();
            // Sanitize the error message so it doesn't break our JSON format
            String safeError = e.getMessage() != null ? e.getMessage().replace("\"", "'").replace("\n", " ") : "Unknown Error";
            return "{\"error\": \"Gemini Crash: " + safeError + "\"}";
        }

    }
}
