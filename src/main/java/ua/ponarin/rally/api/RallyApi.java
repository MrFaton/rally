package ua.ponarin.rally.api;

import com.google.gson.JsonObject;
import com.rallydev.rest.RallyRestApi;
import com.rallydev.rest.request.QueryRequest;
import com.rallydev.rest.request.UpdateRequest;
import com.rallydev.rest.response.QueryResponse;
import com.rallydev.rest.response.UpdateResponse;
import com.rallydev.rest.util.Fetch;
import com.rallydev.rest.util.QueryFilter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RallyApi {

    private final RallyRestApi rallyRestApi;
    @Value("${rally.team.projectUrl}")
    private String projectUrl;

    @SneakyThrows
    public QueryResponse findUserStoryByPi(String pi) {
        var queryRequest = new QueryRequest(RequestType.USER_STORY.getType());
        var queryFilter = new QueryFilter("Release.Name", "contains", "PI " + pi);
        var fetch = new Fetch("Name", "FormattedID", "Iteration", "c_PIPIteration");

        queryRequest.setProject(projectUrl);
        queryRequest.setQueryFilter(queryFilter);
        queryRequest.setFetch(fetch);

        return rallyRestApi.query(queryRequest);
    }

    @SneakyThrows
    public QueryResponse findIterationByName(String name) {
        var queryRequest = new QueryRequest(RequestType.ITERATION.getType());
        var queryFilter = new QueryFilter("Name", "=", name);

        queryRequest.setProject(projectUrl);
        queryRequest.setQueryFilter(queryFilter);

        return rallyRestApi.query(queryRequest);
    }

    @SneakyThrows
    public UpdateResponse updateUserStory(String userStoryRef, JsonObject updates) {
        var updateRequest = new UpdateRequest(userStoryRef, updates);
        return rallyRestApi.update(updateRequest);
    }

    enum RequestType {
        USER_STORY("hierarchicalrequirement"),
        ITERATION("iteration");

        private final String type;

        RequestType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }
}
