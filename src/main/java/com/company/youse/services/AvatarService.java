package com.company.youse.services;

import com.company.youse.repositrories.AvatarRepository;
import com.company.youse.apiObjects.APIObjectProfilePic;
import com.company.youse.errorHandler.MissingException;
import com.company.youse.models.Avatar;
import com.company.youse.models.User;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
@RequiredArgsConstructor
public class AvatarService {


    private final AvatarRepository avatarRepository;


    /**
     * gets an avatar/profile picture from db, decompresses the bytes then returns an avatar/profile picture
     * @param name
     * @return
     */
    public byte[] getAvatarByName(String name){
        Avatar avatar = avatarRepository.findTopByName(name);
        if(avatar!=null){
            return decompressBytes(avatar.getPicByte());
        }else
            throw new MissingException("avatar not found");
    }

    /**
     * gets a mini avatar/profile picture from db, decompresses the bytes then returns an avatar/profile picture
     * @param name
     * @return
     */
    public byte[] getMiniAvatarByName(String name){
        Avatar avatar = avatarRepository.findTopByName(name);
        if(avatar!=null){
            return decompressBytes(avatar.getMiniByte());
        }else
            throw new MissingException("avatar not found");
    }

    public ResponseEntity<?> createAvatar(User user,  APIObjectProfilePic apiObjectProfilePic){
        if (user == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        try {
            if (user.getAvatar() == null) {
                Avatar avatar = new Avatar(  /* create an avatar object */
                        generateAvatarName(user.getfName()), /* get the first name to use in creating the avatar name */
                        apiObjectProfilePic.getPicture().getContentType(), /* get the content type of the multipart file */
                        compressBytes(apiObjectProfilePic.getPicture().getBytes()), /* the main bytes is the picture */
                        compressBytes(apiObjectProfilePic.getThumbnail().getBytes()) /* the mini bytes is the thumbnail */
                );

                avatar.setUser(user);
                avatarRepository.save(avatar);
            }
            else {
                Avatar avatar = user.getAvatar();
                avatar.setMiniByte(compressBytes(apiObjectProfilePic.getThumbnail().getBytes()));
                avatar.setPicByte(compressBytes(apiObjectProfilePic.getPicture().getBytes()));
                avatarRepository.save(avatar);
            }

        }catch(IOException e){

        }
        return new ResponseEntity<>(HttpStatus.OK);

    }

    /**
     * compress the image bytes before storing it in the database
     * @param data
     * @return
     */
    private static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }
//        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }


    /**
     * uncompress the image bytes before returning it to the angular application
     * @param data
     * @return
     */
    private static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException ioe) {
        } catch (DataFormatException e) {
        }
        return outputStream.toByteArray();
    }

    /**
     * generates a unique name for each avatar
     * @param name
     * @return
     */
    private String generateAvatarName(String name){
        String rndchars = RandomStringUtils.randomAlphanumeric(10);
        long millis = System.currentTimeMillis();

        return name+"-"+rndchars+"_"+millis+".jpg";
    }

}
