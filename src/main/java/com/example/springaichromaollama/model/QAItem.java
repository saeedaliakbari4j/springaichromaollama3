package com.example.springaichromaollama.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QAItem {
    private String question;
    private String answer;
    private int rowNum; // Optional: for metadata
}