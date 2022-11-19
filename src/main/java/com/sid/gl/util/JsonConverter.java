package com.sid.gl.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sid.gl.dto.BookRequestDto;

public class JsonConverter {

    public static BookRequestDto convertToBookRequest(String str){
        ObjectMapper mapper =
                new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        BookRequestDto dto = null;
        try{
            dto = mapper.readValue(str, BookRequestDto.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return dto;
    }
}
