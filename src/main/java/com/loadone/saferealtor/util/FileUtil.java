package com.loadone.saferealtor.util;

import com.loadone.saferealtor.exception.BaseException;
import com.loadone.saferealtor.exception.ErrorCode;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileUtil {
    @Value("${file.upload-path}")
    private String uploadPath;

    // 최대 파일 크기
    private long MAX_SIZE = 52428800; // 50MB

    public List<String> uploadImages(List<MultipartFile> images) throws IOException {
        List<String> imagePaths = new ArrayList<>();

        // 이미지 파일이 5개 이상이면 예외 발생
        if(images.size() > 5) {
            throw new BaseException(ErrorCode.INVALID_FILE_SIZE);
        }

        // 이미지 파일 크기가 50MB 이상이면 예외 발생
        if(images.stream().anyMatch(image -> image.getSize() > MAX_SIZE)) {
            throw new BaseException(ErrorCode.INVALID_FILE_SIZE);
        }

        for (MultipartFile image : images) {
            // 파일명 생성
            String filename = System.currentTimeMillis() + "_" + image.getOriginalFilename();

            // 실제 파일경로
            Path filePath = Paths.get(uploadPath + "images/" + filename);

            // 디렉토리 생성
            Files.createDirectories(filePath.getParent());

            // 파일 저장
            image.transferTo(filePath.toFile());

            // 파일의 상대 경로 URL 반환
            String imageUrl = "/uploads/images/" + filename;
            imagePaths.add(imageUrl); // 저장된 파일 경로를 리스트에 추가
        }

        return imagePaths;
    }
}
