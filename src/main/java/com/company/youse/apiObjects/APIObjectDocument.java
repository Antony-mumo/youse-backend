package com.company.youse.apiObjects;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class APIObjectDocument {
    String documentName;
    MultipartFile data;
}
