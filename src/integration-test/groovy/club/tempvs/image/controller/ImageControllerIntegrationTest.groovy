package club.tempvs.image.controller

import club.tempvs.image.service.ImageService

import static org.hamcrest.Matchers.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

import club.tempvs.image.domain.Image
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

@SpringBootTest
@AutoConfigureMockMvc
class ImageControllerIntegrationTest extends Specification {

    private static final String BASE_64_ENCODED_STRING = 'YmFzZTY0IGVuY29kZWQgc3RyaW5n'
    private static final String AUTHORIZATION_HEADER = "Authorization"
    private static final String TOKEN = "df41895b9f26094d0b1d39b7bdd9849e" //security_token as MD5

    @Autowired
    private MockMvc mvc

    @Autowired
    private ImageService imageService

    def "401 is returned due to missing token"() {
        expect:
        mvc.perform(get("/image/source/1"))
                .andExpect(status().isUnauthorized())
    }

    def "exsting image is returned as base64 encoded string"() {
        given:
        Image image = new Image(null, null, '1', 'source', BASE_64_ENCODED_STRING, 'file.jpg')
        imageService.store(image)

        expect:
        mvc.perform(get("/image/source/1")
                .header(AUTHORIZATION_HEADER, TOKEN))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$', hasSize(1)))
                    .andExpect(jsonPath('$[0].objectId', notNullValue(String)))
                    .andExpect(jsonPath('$[0].entityId', is('1')))
                    .andExpect(jsonPath('$[0].belongsTo', is('source')))
                    .andExpect(jsonPath('$[0].content', is(BASE_64_ENCODED_STRING)))
                    .andExpect(jsonPath('$[0].fileName', is('file.jpg')))
    }
}
