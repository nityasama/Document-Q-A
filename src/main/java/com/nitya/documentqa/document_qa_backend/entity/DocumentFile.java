package com.nitya.documentqa.document_qa_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class DocumentFile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String fileName;

    private String fileType;

    @Lob // Marks this field as a Large Object (blob)
    private byte[] data;

    // Constructors
    public DocumentFile() {
    }

    public DocumentFile(String fileName, String fileType, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
