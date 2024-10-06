package com.loadone.saferealtor.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.loadone.saferealtor.exception.BaseException;
import com.loadone.saferealtor.exception.ErrorCode;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j2
@Component
public class FileUtil {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public FileUtil(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public List<String> uploadImages(List<MultipartFile> images) {
        // 스레드 풀 설정 (최대 10개의 스레드로 비동기 처리)
        ExecutorService executor = Executors.newFixedThreadPool(10);

        List<CompletableFuture<String>> futures = images.stream()
                .map(image -> CompletableFuture.supplyAsync(() -> {
                    try {
                        // 파일의 MIME 타입을 확인
                        Path filePath = Paths.get(image.getOriginalFilename());
                        String mimeType = Files.probeContentType(filePath);

                        // 허용된 이미지 MIME 타입 (jpg, png, gif, bmp)
                        List<String> allowedMimeTypes = Arrays.asList("image/jpeg", "image/png", "image/gif", "image/bmp");

                        // 이미지 파일(MIME 타입이 image로 시작하는지)인지 확인
                        if (mimeType != null && allowedMimeTypes.contains(mimeType)) {
                            // 이미지 파일이 맞다면 S3에 업로드
                            return uploadToS3(image);
                        } else {
                            log.error("Invalid file type: {}", image.getOriginalFilename());
                            throw new BaseException(ErrorCode.INVALID_FILE_TYPE);
                        }
                    } catch (IOException e) {
                        log.error("Error uploading image: {}", e.getMessage(), e);
                        throw new BaseException(ErrorCode.FILE_UPLOAD_ERROR);
                    }
                }, executor))
                .toList();

        return futures.stream()
                .map(CompletableFuture::join)
                .toList();
    }

    private String uploadToS3(MultipartFile image) {
        try {
            String filename = UUID.randomUUID().toString();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(image.getSize());
            metadata.setContentType(image.getContentType());

            amazonS3.putObject(bucketName, filename, image.getInputStream(), metadata);

            return amazonS3.getUrl(bucketName, filename).toString();
        } catch (IOException e) {
            log.error("Error uploading image to S3: {}", e.getMessage(), e);
            throw new BaseException(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }
}
