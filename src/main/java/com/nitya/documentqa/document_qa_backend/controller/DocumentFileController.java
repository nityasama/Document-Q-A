package com.nitya.documentqa.document_qa_backend.controller;

import com.nitya.documentqa.document_qa_backend.entity.DocumentFile;
import com.nitya.documentqa.document_qa_backend.service.DocumentFileService;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DocumentFileController {
    private final Tika tika = new Tika();
    @Autowired
    private DocumentFileService documentFileService;

    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok().body("OKAY");
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
        final List<String> ALLOWED_MIME_TYPES = List.of("text/csv", "text/plain", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(new Error("Empty file."));
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                    .body(new Error("File size exceeds the 5MB limit."));
        }

        try {
            System.out.println("ContentType" + file.getContentType());
            InputStream inputStream = file.getInputStream();
            String mimeType = tika.detect(inputStream);

            boolean allowed = ALLOWED_MIME_TYPES.stream()
                    .anyMatch(allowedType -> allowedType.equalsIgnoreCase(mimeType));

            if (!allowed) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                        .body(new Error("Unsupported file type: " + mimeType));
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Error("Error while detecting file type: " + e.getMessage()));
        }

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());

        if (originalFilename.contains("..")) {
            return ResponseEntity.badRequest().body(new Error("Invalid file name."));
        }

        try {
            DocumentFile savedFile = documentFileService.storeFile(file);
            return ResponseEntity.ok("File uploaded successfully with id: " + savedFile.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Error("Could not upload file: " + e.getMessage()));
        }
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<?> getFile(@PathVariable String id) {
        DocumentFile documentFile = documentFileService.getFile(id);
        if (documentFile == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header("Content-Type", documentFile.getFileType())
                .header("Content-Disposition", "attachment; filename=\"" + documentFile.getFileName() + "\"")
                .body(documentFile.getData());
    }

}