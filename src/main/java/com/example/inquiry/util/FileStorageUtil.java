package com.example.inquiry.util;

import com.example.inquiry.exception.InvalidFileTypeException;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Set;
import java.util.UUID;

@Component
public class FileStorageUtil {

    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
        "image/png", "image/jpeg", "image/webp"
    );
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("png", "jpg", "jpeg", "webp");
    private static final Tika TIKA = new Tika();

    @Value("${app.upload.dir}")
    private String uploadDir;

    public String store(MultipartFile file, Long ticketId) throws IOException {
        validateFile(file);

        String originalFilename = file.getOriginalFilename();
        String ext = getExtension(originalFilename);
        String storedName = UUID.randomUUID() + "." + ext;

        Path dir = Paths.get(uploadDir, String.valueOf(ticketId));
        Files.createDirectories(dir);

        Path target = dir.resolve(storedName);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return target.toString();
    }

    public byte[] load(String path) throws IOException {
        return Files.readAllBytes(Paths.get(path));
    }

    public void delete(String path) {
        try {
            Files.deleteIfExists(Paths.get(path));
        } catch (IOException e) {
            // ログに記録するが例外は握りつぶす
        }
    }

    private void validateFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new InvalidFileTypeException("ファイル名が不正です");
        }

        String ext = getExtension(originalFilename).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            throw new InvalidFileTypeException("許可されていないファイル形式です（png/jpg/webp のみ）");
        }

        String mimeType = TIKA.detect(file.getInputStream());
        if (!ALLOWED_MIME_TYPES.contains(mimeType)) {
            throw new InvalidFileTypeException("ファイルの内容が画像ではありません");
        }
    }

    private String getExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        if (lastDot < 0) return "";
        return filename.substring(lastDot + 1);
    }

    public String detectMimeType(MultipartFile file) throws IOException {
        return TIKA.detect(file.getInputStream());
    }
}
