package org.jkarsten.popularmovie.popularmovies.data.source.remote;

import org.jkarsten.popularmovie.popularmovies.data.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;

/**
 * Created by juankarsten on 6/24/17.
 */

public interface MovieDatabaseService {
    //https://api.themoviedb.org/3/movie/popular?api_key=<<api_key>>&language=en-US&page=1

    @GET("movie/popular?language=en-US&&api_key=" + APIKey.API_KEY)
    Call<List<Movie>> listMovies(@Field("page") int page);
}
