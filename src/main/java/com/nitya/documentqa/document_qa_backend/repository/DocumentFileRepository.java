package com.nitya.documentqa.document_qa_backend.repository;

import com.nitya.documentqa.document_qa_backend.entity.DocumentFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentFileRepository extends JpaRepository<DocumentFile, String> {
}
