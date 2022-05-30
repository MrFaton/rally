package ua.ponarin.rally.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class IterationResponse {
    @JsonProperty("Name")
    private String iterationFullName;
}
