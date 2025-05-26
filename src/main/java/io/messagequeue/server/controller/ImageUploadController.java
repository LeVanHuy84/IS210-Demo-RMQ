package io.messagequeue.server.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("api/v1/upload")
public class ImageUploadController {

    @Value("${server.port}")
    private String serverPort;
    @Value("${server.schema}")
    private String serverSchema;
    @Value("${server.name}")
    private String serverName;

    @PostMapping
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String folder = "uploads/";
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path path = Paths.get(folder + fileName);

            // Tạo thư mục nếu chưa có
            Files.createDirectories(path.getParent());

            // Lưu file
            Files.write(path, file.getBytes());

            String imageUrl = serverSchema + "://" +   // http
                              serverName +       // localhost
                              ":" + serverPort +              // 8080
                              "/images/" + fileName;

            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lưu file");
        }
    }

}
