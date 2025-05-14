package com.example.springaichromaollama.controller;

import com.example.springaichromaollama.service.DataIngestionService;
import com.example.springaichromaollama.service.RagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/qa")
public class QaController {

    private final RagService ragService;
    private final DataIngestionService dataIngestionService;

    public QaController(RagService ragService, DataIngestionService dataIngestionService) {
        this.ragService = ragService;
        this.dataIngestionService = dataIngestionService;
    }

    @PostMapping("/ask")
    public ResponseEntity<Map<String, String>> ask(@RequestBody QuestionRequest request) {
        if (request.getQuestion() == null || request.getQuestion().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Question cannot be empty."));
        }
        String answer = ragService.askQuestion(request.getQuestion());
        return ResponseEntity.ok(Map.of("question", request.getQuestion(), "answer", answer));
    }

    @PostMapping("/ingest-data")
    public ResponseEntity<String> triggerIngestion() {
        try {
            // This will call the public ingestData method, not the @PostConstruct one directly.
            // This is useful if you want to manually re-trigger.
            // Be aware of duplicate data if Chroma isn't cleared.
            dataIngestionService.ingestData();
            return ResponseEntity.ok("Data ingestion process triggered successfully. Note: this may create duplicates if run multiple times without clearing ChromaDB.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error during data ingestion: " + e.getMessage());
        }
    }

    // Simple DTO for the request body
    static class QuestionRequest {
        private String question;
        public String getQuestion() { return question; }
        public void setQuestion(String question) { this.question = question; }
    }
}