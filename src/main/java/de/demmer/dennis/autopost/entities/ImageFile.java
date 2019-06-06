package de.demmer.dennis.autopost.entities;


import de.demmer.dennis.autopost.entities.user.Facebookuser;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;


@NoArgsConstructor
@Getter
@Setter
@Entity(name="image")
@Table(name = "images")
public class ImageFile {


    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String fileName;

    private String fileType;

    private String fileSize;

    @ManyToOne
    @JoinColumn(name = "facebookuser_id", nullable = false)
    private Facebookuser facebookuser;


    public ImageFile(String fileName, String fileType, String fileSize, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.data = data;
    }

    @Lob
    @Type(type = "org.hibernate.type.ImageType")
    private byte[] data;

    @OneToOne
    @JoinColumn(name ="facebookpost_id")
    private Facebookpost facebookpost;

}
