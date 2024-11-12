package rs.ac.uns.ftn.onlybunsapp.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.onlybunsapp.service.ImageService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ImageServiceImpl implements ImageService {

    String UPLOAD_DIR = Paths.get("uploads").toString();

    @Override
    public String saveImage(MultipartFile image, String username) {
        if (image.isEmpty()) {
            throw new IllegalArgumentException("Slika nije primljena ili je prazna.");
        }

        try {
            String userDir = UPLOAD_DIR + File.separator + username;
            Files.createDirectories(Paths.get(userDir));

            String postDir = userDir + File.separator + "post_" + countDirectories(userDir);
            Files.createDirectories(Paths.get(postDir));

            String imagePath = postDir + File.separator + generateUniqueFileName(image.getOriginalFilename());

            File imageFile = new File(imagePath);
            image.transferTo(imageFile.getAbsoluteFile());

            return postDir;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public List<String> getAllUncompressedImagePaths() throws IOException{
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);

        return Files.walk(Paths.get(UPLOAD_DIR))
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().matches(".*\\.(jpg|jpeg|png)"))
                .filter(p -> !p.toString().contains("compressed"))
                .filter(p -> {
                    try {
                        BasicFileAttributes attrs = Files.readAttributes(p, BasicFileAttributes.class);
                        LocalDateTime creationTime = LocalDateTime.ofInstant(attrs.creationTime().toInstant(), ZoneId.systemDefault());
                        return creationTime.isBefore(oneMonthAgo);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                })
                .map(Path::toString)
                .collect(Collectors.toList());
    }

    @Override
    public String toImageBase64(String folderPath) {
        try {
            // Assuming there's only one image in the specified folder
            File folder = new File(folderPath);
            File[] files = folder.listFiles();
            System.out.println(folderPath);

            if (files == null || files.length == 0) {
                return ""; // No image file found in the folder
            }

            // Select the first image file found in the folder
            File imageFile = files[0];

            // Convert image file to byte array
            byte[] fileContent = Files.readAllBytes(imageFile.toPath());

            // Encode byte array to Base64
            String base64String = Base64.getEncoder().encodeToString(fileContent);

            return base64String;
        } catch (IOException e) {
            e.printStackTrace();
            return ""; // Return an empty string in case of error
        }
    }

    private String generateUniqueFileName(String originalFilename) {
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        return UUID.randomUUID().toString() + extension;
    }
    private int countDirectories(String path) {
        try {
            return (int) Files.list(Paths.get(path))
                    .filter(Files::isDirectory)
                    .count();
        } catch (IOException e) {
            return 0;
        }
    }
}
