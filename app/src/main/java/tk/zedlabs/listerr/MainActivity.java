package tk.zedlabs.listerr;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderCallbacks<List<Movie>> {

    private static final String LOG_TAG = MainActivity.class.getName();
    private String YTS_REQUEST_URL = "https://yts.am/api/v2/list_movies.json?query_term=";
    private static final int MOVIE_LOADER_ID = 1;
    private MovieAdapter mAdapter;
    private TextView mEmptyStateTextView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            YTS_REQUEST_URL = YTS_REQUEST_URL + query.trim().toLowerCase();
            }

        ListView movieListView = (ListView) findViewById(tk.zedlabs.listerr.R.id.list);
        mEmptyStateTextView = (TextView) findViewById(tk.zedlabs.listerr.R.id.empty_view);
        movieListView.setEmptyView(mEmptyStateTextView);
        mAdapter = new MovieAdapter(this, new ArrayList<Movie>());

        movieListView.setAdapter(mAdapter);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(MOVIE_LOADER_ID, null, this);
        } else {

            View loadingIndicator = findViewById(tk.zedlabs.listerr.R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);


            // Update empty state with no connection error message
            mEmptyStateTextView.setText("no connection");
        }

        Button search =  findViewById(R.id.browse_movie);

        search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SearchableActivity.class);
                startActivity(i);
            }
        });

        Button top =  findViewById(R.id.top_movies);

        top.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, TopMovies.class);
                startActivity(i);
            }
        });
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {
        return new MovieLoader(this, YTS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {

        View loadingIndicator = findViewById(tk.zedlabs.listerr.R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        mEmptyStateTextView.setText("No Movies!");

        if (movies != null && !movies.isEmpty()) {
            mAdapter.addAll(movies);
            //updateUi(movies);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

        mAdapter.clear();
    }
}


