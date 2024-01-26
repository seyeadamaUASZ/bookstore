package com.sid.gl.advisors;

import com.sid.gl.dto.ErrorDTO;
import com.sid.gl.exceptions.BooknotFoundException;
import com.sid.gl.exceptions.BusinessValidationException;
import com.sid.gl.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class BookServiceExceptionHandler {
    private static final String FAILED="FAILED";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleMethodArgumentException(MethodArgumentNotValidException exception){
       ApiResponse<?> apiResponse = new ApiResponse<>();
       List<ErrorDTO> errors = new ArrayList<>();
       exception.getBindingResult().getFieldErrors()
               .forEach(fieldError -> {
                   ErrorDTO errorDTO = new ErrorDTO(fieldError.getField(), fieldError.getDefaultMessage());
                   errors.add(errorDTO);
               });

       apiResponse.setStatus(FAILED);
       apiResponse.setErrorDTOS(errors);
       return apiResponse;
   }

   @ExceptionHandler(BooknotFoundException.class)
   public ApiResponse<?> handleServiceException(BooknotFoundException exception){
        ApiResponse<?> response = new ApiResponse<>();
        response.setErrorDTOS(Collections.singletonList(new ErrorDTO("", exception.getMessage())));
        response.setStatus(FAILED);
        return response;
   }

    @ExceptionHandler(BusinessValidationException.class)
    public ApiResponse<?> handleServiceBussnessValidatorException(BusinessValidationException exception){
        ApiResponse<?> response = new ApiResponse<>();
        response.setErrorDTOS(Collections.singletonList(new ErrorDTO("", exception.getMessage())));
        response.setStatus(FAILED);
        return response;
    }

}
