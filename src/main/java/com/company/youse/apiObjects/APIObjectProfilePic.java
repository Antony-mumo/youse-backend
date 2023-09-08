package com.company.youse.apiObjects;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class APIObjectProfilePic {
    MultipartFile thumbnail;
    MultipartFile picture;
}
