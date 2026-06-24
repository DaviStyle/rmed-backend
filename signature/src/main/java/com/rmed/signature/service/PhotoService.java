package com.rmed.signature.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.rmed.signature.model.Photo;
import com.rmed.signature.model.PhotoType;
import com.rmed.signature.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private Cloudinary cloudinary;

    public List<Photo> getAllPhotos() {
        return photoRepository.findAll();
    }

    public List<Photo> getPhotosByCategory(String category) {
        return photoRepository.findByCategory(category);
    }

    public List<Photo> getPhotosByType(PhotoType type) {
        return photoRepository.findByType(type);
    }

    public Photo addPhoto(Photo photo) {
        return photoRepository.save(photo);
    }

    public void deletePhoto(Long id) {
        photoRepository.deleteById(id);
    }

    @SuppressWarnings("unchecked")
    public Photo uploadPhoto(MultipartFile file, String title, String category,
            PhotoType type, Double price, String secretCode) throws IOException {

        // Upload the file to Cloudinary
        Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        String imageUrl = (String) uploadResult.get("secure_url");

        // Create and save the Photo record
        Photo photo = new Photo();
        photo.setTitle(title);
        photo.setCategory(category);
        photo.setType(type);
        photo.setPrice(price);
        photo.setImageUrl(imageUrl);
        photo.setSecretCode(secretCode);

        return photoRepository.save(photo);
    }

    public ResponseEntity<String> verifyPayment(String reference) {
        String secretKey = "sk_test_0011de5cac7080a192ad6d7add37ae9a79986222";
        String url = "https://api.paystack.co/transaction/verify/" + reference;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verification failed");
        }
    }
}