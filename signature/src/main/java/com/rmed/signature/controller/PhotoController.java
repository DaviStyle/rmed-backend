package com.rmed.signature.controller;

import com.rmed.signature.model.Photo;
import com.rmed.signature.model.PhotoType;
import com.rmed.signature.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/photos")
@CrossOrigin(origins = "http://localhost:5173")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @GetMapping
    public List<Photo> getAllPhotos() {
        return photoService.getAllPhotos();
    }

    @GetMapping("/category/{category}")
    public List<Photo> getByCategory(@PathVariable String category) {
        return photoService.getPhotosByCategory(category);
    }

    @GetMapping("/type/{type}")
    public List<Photo> getByType(@PathVariable PhotoType type) {
        return photoService.getPhotosByType(type);
    }

    @GetMapping("/verify/{reference}")
    public ResponseEntity<String> verifyPayment(@PathVariable String reference) {
        return photoService.verifyPayment(reference);
    }

    @PostMapping
    public Photo addPhoto(@RequestBody Photo photo) {
        return photoService.addPhoto(photo);
    }

    @PostMapping("/upload")
    public Photo uploadPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("category") String category,
            @RequestParam("type") PhotoType type,
            @RequestParam(value = "price", required = false) Double price,
            @RequestParam(value = "secretCode", required = false) String secretCode) throws IOException {
        return photoService.uploadPhoto(file, title, category, type, price, secretCode);
    }

    @DeleteMapping("/{id}")
    public void deletePhoto(@PathVariable Long id) {
        photoService.deletePhoto(id);
    }
}