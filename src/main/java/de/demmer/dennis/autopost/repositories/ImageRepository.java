package de.demmer.dennis.autopost.repositories;

import de.demmer.dennis.autopost.entities.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageFile, String> {
}
