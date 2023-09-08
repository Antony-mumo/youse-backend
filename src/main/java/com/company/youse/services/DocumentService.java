package com.company.youse.services;

import com.company.youse.repositrories.DocumentRepository;
import com.company.youse.apiObjects.APIObjectDocument;
import com.company.youse.errorHandler.MissingException;
import com.company.youse.models.Document;
import com.company.youse.models.ServiceProvider;
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
public class DocumentService {

    private final DocumentRepository documentRepository;


    /**
     * gets an avatar/profile picture from db, decompresses the bytes then returns an avatar/profile picture
     * @param name
     * @return
     */
    public byte[] getDocumentByName(String name){
        Document document = documentRepository.findTopByDocumentName(name);
        if(document != null){
            return  decompressBytes(document.getBytes());
        }else
            throw new MissingException("Document not found");
    }


    public ResponseEntity<?> createDocument(ServiceProvider serviceProvider, APIObjectDocument apiObjectDocument){
        if (serviceProvider == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            Document document = new Document(
                    serviceProvider,
                    generateDocumentName(apiObjectDocument.getDocumentName()+"_"+serviceProvider.getId()),
                    apiObjectDocument.getData().getContentType(),
                    compressBytes(apiObjectDocument.getData().getBytes())
            );
                documentRepository.save(document);
        }catch(IOException e){

        }
        return  new ResponseEntity<>(HttpStatus.OK);
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
     * generates a unique name for each Document
     * @param name
     * @return
     */
    private String generateDocumentName(String name){
        String rndchars = RandomStringUtils.randomAlphanumeric(10);
        long millis = System.currentTimeMillis();

        return name+"-"+rndchars+"_"+millis+".jpg";
    }

}
