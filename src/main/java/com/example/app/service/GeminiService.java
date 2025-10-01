package com.example.app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    private final String apiKey;
    private final RestTemplate restTemplate;
    private final String model = "gemini-2.5-flash"; // bạn có thể đổi model nếu cần
    // endpoint (REST)
    private final String endpoint;

    public GeminiService(@Value("${google.api.key}") String apiKey) {
        this.apiKey = apiKey;
        this.restTemplate = new RestTemplate();
        this.endpoint = "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent";
    }

    /**
     * Gọi Gemini (synchronous) và trả về text thuần.
     */
    public String chat(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", this.apiKey);

        // body cấu trúc theo doc: { "contents":[ { "parts":[ { "text": "..." } ] } ] }
        Map<String, Object> part = Map.of("text", prompt);
        Map<String, Object> content = Map.of("parts", List.of(part));
        Map<String, Object> body = Map.of("contents", List.of(content));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(endpoint, request, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            try {
                Map<String, Object> map = response.getBody();

                // Thử parse "candidates" -> candidate.content.parts[].text
                Object cands = map.get("candidates");
                if (cands instanceof List) {
                    List candidates = (List) cands;
                    if (!candidates.isEmpty() && candidates.get(0) instanceof Map) {
                        Map first = (Map) candidates.get(0);
                        Object contentObj = first.get("content");
                        if (contentObj instanceof Map) {
                            Map contentMap = (Map) contentObj;
                            Object partsObj = contentMap.get("parts");
                            if (partsObj instanceof List) {
                                StringBuilder sb = new StringBuilder();
                                for (Object p : (List) partsObj) {
                                    if (p instanceof Map) {
                                        Object text = ((Map) p).get("text");
                                        if (text != null) sb.append(text.toString());
                                    }
                                }
                                String result = sb.toString().trim();
                                if (!result.isBlank()) return result;
                            }
                        }
                    }
                }

                // Fallback check: outputs -> first -> content -> parts
                Object outputs = map.get("outputs");
                if (outputs instanceof List) {
                    List outs = (List) outputs;
                    if (!outs.isEmpty() && outs.get(0) instanceof Map) {
                        Map firstOut = (Map) outs.get(0);
                        Object contentObj = firstOut.get("content");
                        if (contentObj instanceof Map) {
                            Map contentMap = (Map) contentObj;
                            Object partsObj = contentMap.get("parts");
                            if (partsObj instanceof List) {
                                StringBuilder sb = new StringBuilder();
                                for (Object p : (List) partsObj) {
                                    if (p instanceof Map) {
                                        Object text = ((Map) p).get("text");
                                        if (text != null) sb.append(text.toString());
                                    }
                                }
                                String result = sb.toString().trim();
                                if (!result.isBlank()) return result;
                            }
                        }
                    }
                }

                // Nếu không lấy được, trả Raw JSON string làm debug
                return map.toString();

            } catch (Exception ex) {
                throw new RuntimeException("Error parsing Gemini response", ex);
            }
        } else {
            throw new RuntimeException("Gemini API returned non-2xx: " + response.getStatusCode());
        }
    }
}
