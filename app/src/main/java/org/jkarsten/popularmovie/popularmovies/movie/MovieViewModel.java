package org.jkarsten.popularmovie.popularmovies.movie;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.util.ImageUtil;

/**
 * Created by juankarsten on 8/15/17.
 */

public class MovieViewModel extends BaseObservable{
    private Movie mMovie;

    public MovieViewModel(Movie movie) {
        mMovie = movie;
    }

    @Bindable
    public String getTitle() {
        return mMovie.getTitle();
    }

    @Bindable
    public String getYear() {
        return mMovie.getYear();
    }

    @Bindable
    public String getTopAverage() {
        return mMovie.getTopAverage();
    }

    @Bindable
    public String getOverview() {
        return mMovie.getOverview();
    }

    @Bindable
    public boolean getMarkAsFavorite() {
        return mMovie.getMarkAsFavorite();
    }

    @Bindable
    public String getImagePath() {
        return mMovie.getPosterPath();
    }

    @BindingAdapter({"imagePath"})
    public static void setImagePath(ImageView view, String imagePath) {
        String path = ImageUtil.buildImageUri(imagePath, view.getContext());
        Picasso.with(view.getContext()).load(path).into(view);
    }
}
