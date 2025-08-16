package com.nitya.documentqa.document_qa_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("document")
public class DocumentUploadController {
    @GetMapping
    public String getResponse(){
        return "HELLO WORLD";
    }
}
