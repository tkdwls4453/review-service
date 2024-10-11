package com.example.reviewservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3Service {

    public String uploadImage(MultipartFile file){
        // 이미지 업로드 로직
        return file == null ? null : "/" + file.getOriginalFilename();
    }

}
