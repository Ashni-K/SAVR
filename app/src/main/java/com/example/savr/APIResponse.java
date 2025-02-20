package com.example.savr;

import java.util.List;

public class APIResponse {
    private List<Ingredient> results;

    public List<Ingredient> getResults() { // Changed return type
        return results;
    }

    public void setResults(List<Ingredient> results) {
        this.results = results;
    }
}