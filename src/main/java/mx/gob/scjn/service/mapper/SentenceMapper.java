package mx.gob.scjn.service.mapper;

import mx.gob.scjn.domain.*;
import mx.gob.scjn.service.dto.SentenceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Sentence and its DTO SentenceDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SentenceMapper extends EntityMapper<SentenceDTO, Sentence> {


    @Mapping(target = "resources", ignore = true)
    Sentence toEntity(SentenceDTO sentenceDTO);

    default Sentence fromId(Long id) {
        if (id == null) {
            return null;
        }
        Sentence sentence = new Sentence();
        sentence.setId(id);
        return sentence;
    }
}
