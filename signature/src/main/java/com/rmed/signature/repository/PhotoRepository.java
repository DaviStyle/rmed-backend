package com.rmed.signature.repository;

import com.rmed.signature.model.Photo;
import com.rmed.signature.model.PhotoType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByCategory(String category);

    List<Photo> findByType(PhotoType type);
}