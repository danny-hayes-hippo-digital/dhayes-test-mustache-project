package org.example.model;

import java.util.List;

public class MustacheDataModel {

    private final List<Person> people;
    private final boolean searchComplete;
    private final String errorMessage;
    private final String creationSuccessMessage;

    public MustacheDataModel(
            List<Person> people,
            boolean searchComplete,
            String errorMessage,
            String creationSuccessMessage) {
        this.people = people;
        this.searchComplete = searchComplete;
        this.errorMessage = errorMessage;
        this.creationSuccessMessage = creationSuccessMessage;
    }

    public List<Person> getPeople() {
        return people;
    }

    public boolean getSearchComplete() {
        return searchComplete;
    }

    public String errorMessage() {
        return errorMessage;
    }

    public String getCreationSuccessMessage() {
        return creationSuccessMessage;
    }
}
