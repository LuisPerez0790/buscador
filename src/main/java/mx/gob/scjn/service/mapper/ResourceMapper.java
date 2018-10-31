package mx.gob.scjn.service.mapper;

import mx.gob.scjn.domain.*;
import mx.gob.scjn.service.dto.ResourceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Resource and its DTO ResourceDTO.
 */
@Mapper(componentModel = "spring", uses = {SentenceMapper.class})
public interface ResourceMapper extends EntityMapper<ResourceDTO, Resource> {

    @Mapping(source = "sentence.id", target = "sentenceId")
    ResourceDTO toDto(Resource resource);

    @Mapping(source = "sentenceId", target = "sentence")
    Resource toEntity(ResourceDTO resourceDTO);

    default Resource fromId(Long id) {
        if (id == null) {
            return null;
        }
        Resource resource = new Resource();
        resource.setId(id);
        return resource;
    }
}
