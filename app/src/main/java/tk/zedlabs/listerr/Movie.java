package tk.zedlabs.listerr;

public class Movie {

    private String mMovieName;
    private double mRating;
    private String mPosterUrl;

    public Movie(String movieName, double rating, String posterUrl){

        mMovieName = movieName;
        mRating = rating;
        mPosterUrl = posterUrl;

    }

    public double getRating() {
        return mRating;
    }

    public String getMovieName() {
        return mMovieName;
    }

    public String getPosterUrl() {
        return mPosterUrl;
    }
}
