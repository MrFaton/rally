package ua.ponarin.rally.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class UserStoryResponse {
    @JsonProperty("Name")
    private String name;

    @JsonProperty("FormattedID")
    private String formattedId;

    @JsonProperty("_ref")
    private String ref;

    @JsonProperty("c_PIPIteration")
    private String pipIteration;

    @JsonProperty("Iteration")
    private IterationResponse iterationResponse;
}
