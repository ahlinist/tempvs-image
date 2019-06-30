package club.tempvs.image.service.impl;

import club.tempvs.image.domain.Image;
import club.tempvs.image.service.ImageService;
import club.tempvs.image.dao.ImageDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageDao imageDao;

    @Override
    public byte[] getImage(String id) {
        return imageDao.get(id);
    }

    @Override
    public Image store(Image image) {
        return imageDao.save(image);
    }

    @Override
    public void delete(List<String> objectIds) {
        objectIds.stream().forEach(imageDao::delete);
    }
}
