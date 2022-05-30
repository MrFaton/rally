package ua.ponarin.rally.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import ua.ponarin.rally.model.dto.UserStoryDto;
import ua.ponarin.rally.model.response.UserStoryResponse;

import java.util.List;
import java.util.Optional;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface UserStoryMapper {
    String ITERATION_DELIMITER = " ";

    @Named("iterationFullNameToPi")
    static Optional<String> iterationFullNameToPi(String iterationFullName) {
        return Optional.ofNullable(iterationFullName)
                .map(iterationFullNameInternal -> iterationFullNameInternal.split(ITERATION_DELIMITER)[0].substring(2));
    }

    @Named("iterationFullNameToIteration")
    static Optional<String> iterationFullNameToIteration(String iterationFullName) {
        return Optional.ofNullable(iterationFullName)
                .map(iterationFullNameInternal -> iterationFullNameInternal.split(ITERATION_DELIMITER)[1].substring(1));
    }

    default Optional<String> optionalWrapper(String value) {
        return Optional.ofNullable(value);
    }

    @Mapping(target = "pi", source = "iterationResponse.iterationFullName", qualifiedByName = "iterationFullNameToPi")
    @Mapping(target = "iteration", source = "iterationResponse.iterationFullName", qualifiedByName = "iterationFullNameToIteration")
    UserStoryDto toUserStoryDto(UserStoryResponse userStoryResponse);

    List<UserStoryDto> toUserStoryDtoList(List<UserStoryResponse> userStoryResponseList);


}
