package com.sid.gl.validators;

import org.springframework.web.multipart.MultipartFile;

public class DocumentValidator {
    public static boolean validateDocument(MultipartFile file){
        String fileName = file.getName().toUpperCase();
        return fileName.endsWith(".JPG") || fileName.endsWith(".JPEG");
    }
}
