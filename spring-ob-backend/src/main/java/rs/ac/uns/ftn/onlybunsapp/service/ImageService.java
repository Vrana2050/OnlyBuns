package rs.ac.uns.ftn.onlybunsapp.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    public String saveImage(MultipartFile file,String username);
    public List<String> getAllUncompressedImagePaths() throws IOException;
}
