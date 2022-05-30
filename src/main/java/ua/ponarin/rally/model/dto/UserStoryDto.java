package ua.ponarin.rally.model.dto;

import lombok.Data;

import java.util.Optional;

@Data
public class UserStoryDto {


    private String name;
    private String formattedId;
    private String ref;
    private Optional<String> pi;
    private Optional<String> iteration;
    private Optional<String> pipIteration;

    public boolean hasDifferentIterations() {
        return !iteration.equals(pipIteration);
    }
}
