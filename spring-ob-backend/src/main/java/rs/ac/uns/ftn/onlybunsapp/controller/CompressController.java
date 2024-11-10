package rs.ac.uns.ftn.onlybunsapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.onlybunsapp.service.ImageService;
import rs.ac.uns.ftn.onlybunsapp.service.PostService;
import rs.ac.uns.ftn.onlybunsapp.util.ImageCompressor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
public class CompressController {

    @Autowired
    private ImageService imageService;

    @Scheduled(cron = "0 * * * * ?")
    public void CompressImagesDaily() {
        try {
            List<String> imagesPaths = imageService.getAllUncompressedImagePaths();
            for (String imagePath : imagesPaths) {
                String compressedImagePath = imagePath.substring(0, imagePath.lastIndexOf('.')) + "_compressed" + imagePath.substring(imagePath.lastIndexOf('.'));
                ImageCompressor.compressImage(imagePath, compressedImagePath, 0.5f);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
