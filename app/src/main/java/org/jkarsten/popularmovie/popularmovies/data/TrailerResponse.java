package org.jkarsten.popularmovie.popularmovies.data;

import java.util.List;

/**
 * Created by juankarsten on 8/14/17.
 */

public class TrailerResponse {
    private int id;
    private List<Trailer> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Trailer> getResults() {
        return results;
    }

    public void setResults(List<Trailer> results) {
        this.results = results;
    }
}
