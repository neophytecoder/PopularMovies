package org.jkarsten.popularmovie.popularmovies.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by juankarsten on 6/24/17.
 */

public class Movie implements Serializable {
    ////"vote_count":1945,"id":297762,"video":false,"vote_average":7,"title":"Wonder Woman","popularity":113.999005,"poster_path":"\/gfJGlDaHuWimErCr5Ql0I8x9QSy.jpg","original_language":"en","original_title":"Wonder Woman","genre_ids":[28,12,14,878],"backdrop_path":"\/hA5oCgvgCxj5MEWcLpjXXTwEANF.jpg","adult":false,"overview":"An Amazon princess comes to the world of Man to become the greatest of the female superheroes.","release_date":"2017-05-30"

    @SerializedName("vote_count")
    private int voteCount;

    private int id;

    private boolean video;

    private String title;

    @SerializedName("vote_average")
    private double voteAverage;

    private double popularity;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("genre_ids")
    private int[] genreIds;

    private String overview;

    @SerializedName("release_date")
    private Date releaseDate;

    @SerializedName("backdrop_path")
    private String backdropPath;

    private String year;
    private String topAverage;

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public int[] getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(int[] genreIds) {
        this.genreIds = genreIds;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getYear() {
        return new SimpleDateFormat("yyyy").format(getReleaseDate()).toString();
    }

    public String getTopAverage() {
        return voteAverage+"/10.0";
    }

    @Override
    public String toString() {
        return "Movie{" +
                "voteCount=" + voteCount +
                ", id=" + id +
                ", video=" + video +
                ", title='" + title + '\'' +
                ", popularity=" + popularity +
                ", posterPath='" + posterPath + '\'' +
                ", originalLanguage='" + originalLanguage + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", genreIds='" + genreIds + '\'' +
                ", overview='" + overview + '\'' +
                ", releaseDate=" + releaseDate +
                ", backdropPath='" + backdropPath + '\'' +
                '}';
    }


}
