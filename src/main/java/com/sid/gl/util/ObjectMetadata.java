package com.sid.gl.util;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class ObjectMetadata {
    private String objectName;
    private String contentType;
    private long objectSize;
}
