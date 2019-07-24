package club.tempvs.image.util;

import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Component
public class MongoHelper {

    private static final String ID = "_id";
    private static final String BELONGS_TO_CRITERIA = "metadata.belongsTo";
    private static final String ENTITY_ID_CRITERIA = "metadata.entityId";

    public Query buildBulkQuery(String belongsTo, String entityId) {
        Criteria belongsToCriteria = Criteria.where(BELONGS_TO_CRITERIA).is(belongsTo);
        Criteria entityIdCriteria = Criteria.where(ENTITY_ID_CRITERIA).is(entityId);
        Criteria resultCriteria = new Criteria();
        resultCriteria.andOperator(belongsToCriteria, entityIdCriteria);
        return new Query(resultCriteria);
    }

    public Query buildIdQuery(String objectId) {
        return new Query(Criteria.where(ID).is(objectId));
    }

    public Query buildIdQuery(List<String> objectIds) {
        Criteria[] idCriteria = objectIds.stream()
                .map(Criteria.where(ID)::is)
                .toArray(Criteria[]::new);
        Criteria resultCriteria = new Criteria();
        resultCriteria.orOperator(idCriteria);
        return new Query(resultCriteria);
    }

    public List<GridFSFile> collectGridFSFiles(GridFSFindIterable gridFSFindIterable) {
        return StreamSupport.stream(gridFSFindIterable.spliterator(), false)
                .collect(toList());
    }
}
