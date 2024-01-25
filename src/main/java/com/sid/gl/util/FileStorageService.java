package com.sid.gl.util;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import com.sid.gl.dto.BookRequestDto;
import com.sid.gl.exceptions.FileStorageException;
import com.sid.gl.exceptions.UploadException;
import com.sid.gl.models.Book;
import com.sid.gl.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@Service
@Slf4j
public class FileStorageService {
    private final Path fileStorageLocation;
    @Autowired
    private BookRepository bookRepository;



    public FileStorageService(Path fileStorageLocation) {
        this.fileStorageLocation = fileStorageLocation;
    }

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        log.info("create directory instance ....");
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }

    }

    public Book createBook(BookRequestDto bookRequestDto, Optional<MultipartFile> file) {
       if(file.isPresent()){
           MultipartFile fileBook =  file.get();
           String fileName = StringUtils.cleanPath(fileBook.getOriginalFilename());
           String fileDownloadUri="";
           try {
               if(fileName.contains("..")) {
                   throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
               }
               Path targetLocation = this.fileStorageLocation.resolve(fileName);
               Files.copy(fileBook.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

               fileDownloadUri= ServletUriComponentsBuilder.fromCurrentContextPath()
                       .path("api/v1/book/downloadImageUri/")
                       .path(fileName)
                       .toUriString();

               bookRequestDto.setFilebook(fileDownloadUri);
               bookRequestDto.setFileName(fileName);
               System.out.println(" null object "+BookMappers.jsonObjectToString(bookRequestDto));
               return bookRepository.save(BookMappers.convertToBook(bookRequestDto));
           }catch (IOException ex) {
               throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
           }
       }else{
           throw new UploadException("Cannot upload file image for book");
       }

    }


}
