package com.example.demo.service;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.example.demo.model.Event;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ImageService{

    private static final Path IMAGES_FOLDER = Paths.get(System.getProperty("user.dir"), "static");
    @Value("/src/main/resources/static")
    private String uploadDir;
    private ConcurrentHashMap<Long, String> filess = new ConcurrentHashMap<>();


    public String createImage(MultipartFile multiPartFile) {

        String originalName = multiPartFile.getOriginalFilename();

        if(!originalName.matches(".*\\.(jpg|jpeg|gif|png|pdf)")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The url is not an image resource");
        }

        String fileName = originalName;

        Path imagePath = IMAGES_FOLDER.resolve(fileName);
        try {
            multiPartFile.transferTo(imagePath);
        } catch (Exception ex) {
            System.err.println(ex);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't save image locally", ex);
        }

        return fileName;
    }
    public Path createFilePath(long imageId, Path folder) {
        return folder.resolve(  imageId + ".pdf");
    }

    public Resource getImage(String imageName) {
        Path imagePath = IMAGES_FOLDER.resolve(imageName);
        try {
            return new UrlResource(imagePath.toUri());
        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't get local image");
        }
    }

    public void deleteImage(String image_url) {
        String[] tokens = image_url.split("/");
        String image_name = tokens[tokens.length -1 ];

        try {
            IMAGES_FOLDER.resolve(image_name).toFile().delete();
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't delete local image");
        }
    }
    public void saveImage(String folderName, long imageId, MultipartFile image) throws IOException {

        Path folder = IMAGES_FOLDER.resolve(folderName);

        Files.createDirectories(folder);

        Path newFile = createFilePath(imageId, folder);

        image.transferTo(newFile);
    }

    public void deleteImage1(String folder, Long id) throws IOException {
        String imagePath = folder + "/" + id + ".jpg";
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
               imageFile.delete();
        }
    }
    public Blob convertMultiparttoBlob (MultipartFile multipartFile){
        try{
            byte[] bytes = multipartFile.getBytes();
            return new javax.sql.rowset.serial.SerialBlob(bytes);
        } catch (IOException | SQLException e){
            e.printStackTrace();
            return null;
        }
    }
    public void savefile(String foldername,MultipartFile file,String filemame) throws IOException {
        Path folder = Paths.get(uploadDir,foldername);
        if (!Files.exists(folder)){
            Files.createDirectories(folder);
        }
        Path newfile=folder.resolve(filemame);
        Files.copy(file.getInputStream(),newfile);
    }
    public void savePdf(MultipartFile pdfFile, Long id) throws IOException {
        String originalName = pdfFile.getOriginalFilename();
        Path folder = IMAGES_FOLDER.resolve(id.toString());
        Files.createDirectories(folder);

        // Crear un archivo en el directorio con el nombre original del archivo
        Path filePath = folder.resolve(originalName);

        try (InputStream inputStream = pdfFile.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
        filess.put(id,originalName);
    }
    public String getHashMap(long id) {
        String mine=filess.get(id);

        return mine;
    }
}
