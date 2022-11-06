package com.sid.gl.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sid.gl.dto.ErrorDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ApiResponse<T> {
    private String status;
    private List<ErrorDTO> errorDTOS;
    private T results;
}
