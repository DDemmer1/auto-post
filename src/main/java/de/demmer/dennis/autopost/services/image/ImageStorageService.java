package de.demmer.dennis.autopost.services.image;

import de.demmer.dennis.autopost.entities.Facebookpost;
import de.demmer.dennis.autopost.entities.ImageFile;
import de.demmer.dennis.autopost.entities.user.Facebookuser;
import de.demmer.dennis.autopost.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
public class ImageStorageService {

    @Autowired
    private ImageRepository imageRepository;

    public ImageFile storeFile(MultipartFile file, Facebookuser facebookuser) throws ImageStorageException {
        try {
            // Normalize file name
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new ImageStorageException("Filename contains invalid path sequence " + fileName);
            }

            ImageFile dbFile = new ImageFile(fileName, file.getContentType(), file.getSize() + "", file.getBytes());
            dbFile.setFacebookuser(facebookuser);

            return imageRepository.save(dbFile);

        } catch (IOException e) {
            throw new ImageStorageException("File exception");
        }

    }

    public ImageFile getFile(String fileId) throws ImageStorageException {
        return imageRepository.findById(fileId)
                .orElseThrow(() -> new ImageStorageException("File not found with id " + fileId));
    }


}
