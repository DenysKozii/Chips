package degradation.mapper;

import degradation.dto.GameRequestDto;
import degradation.entity.GameRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface GameRequestMapper {
    GameRequestMapper INSTANCE = Mappers.getMapper(GameRequestMapper.class);

    GameRequestDto mapToDto(GameRequest gameRequest);
}
