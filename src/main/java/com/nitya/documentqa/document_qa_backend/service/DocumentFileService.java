package com.nitya.documentqa.document_qa_backend.service;

import com.nitya.documentqa.document_qa_backend.entity.DocumentFile;
import com.nitya.documentqa.document_qa_backend.repository.DocumentFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class DocumentFileService {
    @Autowired
    private DocumentFileRepository repository;

    public DocumentFile storeFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String fileType = file.getContentType();
        byte[] data = file.getBytes();

        DocumentFile documentFile = new DocumentFile(fileName, fileType, data);
        return repository.save(documentFile);
    }

    public DocumentFile getFile(String id) {
        return repository.findById(id).orElse(null);
    }
}
