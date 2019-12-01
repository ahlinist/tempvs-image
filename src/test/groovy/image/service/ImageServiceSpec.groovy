package image.service

import club.tempvs.image.dao.ImageDao
import club.tempvs.image.domain.Image
import club.tempvs.image.service.ImageService
import club.tempvs.image.service.impl.ImageServiceImpl
import spock.lang.Specification
import spock.lang.Subject

class ImageServiceSpec extends Specification {

    ImageDao imageDao = Mock ImageDao

    @Subject
    ImageService imageService = new ImageServiceImpl(imageDao)

    Image image = Mock Image

    def "replace image"() {
        given:
        String belongsTo = 'profile'
        String entityId = '1'
        String content = 'content'
        String fileName = 'test.jpg'
        String imageInfo = 'imageInfo'
        Map<String, String> metaDataMap = Map.of(
                'imageInfo', imageInfo,
                'entityId', entityId,
                'belongsTo', belongsTo
        )

        when:
        imageService.replace(image)

        then:
        2 * image.belongsTo >> belongsTo
        2 * image.entityId >> entityId
        1 * image.content >> content
        1 * image.fileName >> fileName
        1 * image.imageInfo >> imageInfo
        1 * imageDao.delete(belongsTo, entityId)
        1 * imageDao.save(content, fileName, metaDataMap)
        0 * _
    }
}
