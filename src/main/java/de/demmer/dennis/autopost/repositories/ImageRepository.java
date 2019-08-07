package de.demmer.dennis.autopost.repositories;

import de.demmer.dennis.autopost.entities.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<ImageFile, String> {


    List<ImageFile> findAllByFacebookpost_Id(int id);
}
