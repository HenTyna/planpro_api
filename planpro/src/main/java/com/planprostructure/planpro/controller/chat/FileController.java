package com.planprostructure.planpro.controller.chat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {
  private final Path uploadDir;
  public FileController(@Value("${file.server-path}") String dir) throws IOException { this.uploadDir = Path.of(dir); Files.createDirectories(uploadDir); }

  @PostMapping(consumes = {"multipart/form-data"})
  public ResponseEntity<?> upload(@RequestPart("file") MultipartFile file) throws IOException {
    String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
    String key = UUID.randomUUID() + (ext != null ? ("." + ext) : "");
    Path dest = uploadDir.resolve(key);
    file.transferTo(dest.toFile());
    return ResponseEntity.ok(new UploadResponse(key, Files.size(dest)));
  }

  record UploadResponse(String objectKey, long sizeBytes) {}
} 