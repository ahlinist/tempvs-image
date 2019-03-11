package club.tempvs.image.service.impl;

import club.tempvs.image.domain.Image;
import club.tempvs.image.service.ImageService;
import club.tempvs.image.dao.ImageDao;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private static final String DEFAULT_IMAGE_NAME = "default_image.gif";

    private final ImageDao imageDao;

    @Override
    public byte[] getImage(String id) {
        byte[] result = imageDao.get(id);
        return result != null ? result : getDefaultImage();
    }

    @Override
    public Image store(Image image) {
        return imageDao.save(image);
    }

    @Override
    public void deleteImage(String id) {
        imageDao.delete(id);
    }

    @Override
    public void deleteImages(List<Image> images) {
        images.stream().forEach(imageDao::delete);
    }

    private byte[] getDefaultImage() {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        try(InputStream inputStream = classloader.getResourceAsStream(DEFAULT_IMAGE_NAME)) {
            return IOUtils.toByteArray(inputStream);
        } catch (Exception e) {
            return new byte[]{};
        }
    }
}
