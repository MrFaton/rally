package ua.ponarin.rally.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.ponarin.rally.api.RallyApi;
import ua.ponarin.rally.mapper.UserStoryMapper;
import ua.ponarin.rally.model.dto.UserStoryDto;
import ua.ponarin.rally.model.response.UserStoryResponse;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class RallyService {
    private static final String FULL_ITERATION_NAME_TEMPLATE = "PI%s I%s";
    private static final String FULL_INNOVATION_ITERATION_NAME_TEMPLATE = "PI%s I%s (IP Sprint)";
    private static final String INNOVATION_ITERATION_PREFIX = "6";

    private final RallyApi rallyApi;
    private final ObjectMapper objectMapper;
    private final UserStoryMapper userStoryMapper;
    @Value("${rally.team.pi}")
    private String defaultPi;

    @SneakyThrows
    public List<UserStoryDto> findByPi(String pi) {
        log.info("Collecting UserStories from PI {}", pi);
        var response = rallyApi.findUserStoryByPi(pi);
        var userStoryResponseList = objectMapper
                .readValue(response.getResults().toString(), new TypeReference<List<UserStoryResponse>>() {
                });
        var userStoryDtoList = userStoryMapper.toUserStoryDtoList(userStoryResponseList);
        log.info("Found {} UserStories", userStoryDtoList.size());
        return userStoryDtoList;
    }

    public void printUserStoriesWithDifferentIterations() {
        var userStories = findByPi(defaultPi);
        var userStoriesWithDifferentIterations = userStories.stream()
                .filter(UserStoryDto::hasDifferentIterations)
                .collect(Collectors.toList());
        log.info("Found {} UserStories with different iterations:", userStoriesWithDifferentIterations.size());
        userStoriesWithDifferentIterations
                .forEach(userStory -> log.info(buildDifferenceInUserStoryIterationLogMessage(userStory)));
    }

    public void propagateIterationToPipIteration() {
        var userStories = findByPi(defaultPi);
        log.info("Start propagating Iteration to PIP Iteration for {} UserStories", userStories.size());
        Predicate<UserStoryDto> filter = userStoryDto ->
                userStoryDto.getIteration().isPresent() &&
                        userStoryDto.hasDifferentIterations();
        Function<UserStoryDto, JsonObject> updatesFunction = userStory -> {
            var updates = new JsonObject();
            updates.addProperty("c_PIPIteration", userStory.getIteration().get());
            return updates;
        };
        propagateIteration(userStories, filter, updatesFunction);
    }

    public void propagatePipIterationToIteration() {
        var userStories = findByPi(defaultPi);
        log.info("Start propagating PIP Iteration to Iteration for {} UserStories", userStories.size());
        Predicate<UserStoryDto> filter = userStoryDto ->
                userStoryDto.getPipIteration().isPresent() &&
                        userStoryDto.hasDifferentIterations();

        Function<UserStoryDto, JsonObject> updatesFunction = userStory -> {
            var pipIteration = userStory.getPipIteration().get();
            var iterationResponse = rallyApi
                    .findIterationByName(buildFullIterationName(userStory.getPi().orElse(defaultPi), pipIteration));
            var updates = new JsonObject();
            updates.add("Iteration", iterationResponse.getResults().get(0));
            return updates;
        };

        propagateIteration(userStories, filter, updatesFunction);
    }

    private void propagateIteration(List<UserStoryDto> userStories, Predicate<UserStoryDto> updatesFilterPredicate, Function<UserStoryDto, JsonObject> updatesFunction) {
        var userStoriesToUpdate = userStories.stream()
                .filter(updatesFilterPredicate)
                .collect(Collectors.toList());
        log.info("{} UserStories passed filter and will be updated", userStoriesToUpdate.size());
        log.info("Starting update process...");
        userStoriesToUpdate.forEach(userStory -> {
            log.info(buildDifferenceInUserStoryIterationLogMessage(userStory));
            var updates = updatesFunction.apply(userStory);
            update(userStory, updates);
        });
        log.info("Update process completed");
    }

    private void update(UserStoryDto userStory, JsonObject updates) {
//        log.info(String.format("Updating UserStory: %s - %-70s", userStory.getFormattedId(), StringUtils.abbreviate(userStory.getName(), 70)));
        rallyApi.updateUserStory(userStory.getRef(), updates);
    }

    private String buildFullIterationName(String pi, String iteration) {
        return String.format(
                iteration.startsWith(INNOVATION_ITERATION_PREFIX) ?
                        FULL_INNOVATION_ITERATION_NAME_TEMPLATE :
                        FULL_ITERATION_NAME_TEMPLATE,
                pi, iteration);
    }

    private String buildDifferenceInUserStoryIterationLogMessage(UserStoryDto userStory) {
        return String.format("Difference in iterations: %s - %-70s | Iteration: %-4s | PIP Iteration: %s",
                userStory.getFormattedId(),
                StringUtils.abbreviate(userStory.getName(), 70),
                userStory.getIteration().orElse("null"),
                userStory.getPipIteration().orElse("null"));
    }
}
