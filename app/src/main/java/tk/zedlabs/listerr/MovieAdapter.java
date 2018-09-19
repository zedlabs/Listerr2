package tk.zedlabs.listerr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.List;

public class MovieAdapter extends ArrayAdapter<Movie> {



    public MovieAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    tk.zedlabs.listerr.R.layout.list_item_view, parent, false);
        }

       Movie currentMovie = getItem(position);

        TextView titleView = (TextView) listItemView.findViewById(tk.zedlabs.listerr.R.id.movie_name);

        titleView.setText(currentMovie.getMovieName());

        TextView ratingsView = (TextView) listItemView.findViewById(tk.zedlabs.listerr.R.id.rating);

        String formattedRating = formatRating(currentMovie.getRating());

        ratingsView.setText(formattedRating);

        ImageView posterView = (ImageView) listItemView.findViewById(R.id.poster);
        String ss = currentMovie.getPosterUrl();
        new  DownloadImageTask(posterView).execute(ss);

        return listItemView;
    }

    private String formatRating(double rating) {
        DecimalFormat ratingFormat = new DecimalFormat("0.0");
        return ratingFormat.format(rating);
    }
}
