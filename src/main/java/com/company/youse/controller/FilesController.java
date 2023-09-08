package com.company.youse.controller;

import com.company.youse.apiObjects.APIObjectDocument;
import com.company.youse.apiObjects.APIObjectProfilePic;
import com.company.youse.models.User;
import com.company.youse.services.AvatarService;
import com.company.youse.services.DocumentService;
import com.company.youse.services.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.zip.DataFormatException;

/**
 *
 */
@RestController
@Transactional
@RequiredArgsConstructor
public class FilesController {

    private final AvatarService avatarService;

    private final DocumentService documentService;

    private final Util util;

    @RequestMapping(value = "/api/avatar/{imageName}" , method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getProfilePicture(@PathVariable("imageName") String imageName) throws IOException, DataFormatException {

        return avatarService.getAvatarByName(imageName);
    }

    @RequestMapping(value = "/api/avatar/thumbnail/{imageName}" , method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getLightProfilePicture(@PathVariable("imageName") String imageName) throws IOException, DataFormatException {

        return avatarService.getMiniAvatarByName(imageName);
    }

    @RequestMapping(value = "/api/avatar/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createAvatar(HttpServletRequest request, @ModelAttribute APIObjectProfilePic apiObjectProfilePic) {
        User user = util.getUserFromToken(request.getHeader("Authorization"));
        return avatarService.createAvatar(user, apiObjectProfilePic);
    }

    @RequestMapping(value = "/api/file/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createDocument(HttpServletRequest request, @ModelAttribute APIObjectDocument apiObjectDocument) {
        User user = util.getUserFromToken(request.getHeader("Authorization"));
        return documentService.createDocument(user.getServiceProvider(), apiObjectDocument);
    }

    @RequestMapping(value = "/api/file/get/{imageName}" , method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getDocument(HttpServletRequest request, @PathVariable("imageName") String imageName) {
        return documentService.getDocumentByName(imageName);
    }

}
