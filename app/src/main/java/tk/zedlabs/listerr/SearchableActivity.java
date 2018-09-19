package tk.zedlabs.listerr;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearchableActivity extends AppCompatActivity
                     implements LoaderCallbacks<List<Movie>>{

    private static final String LOG_TAG = SearchableActivity.class.getName();

    private String YTS_REQUEST_URL_SEARCH = "https://yts.am/api/v2/list_movies.json?query_term=";

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_view);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
             YTS_REQUEST_URL_SEARCH = YTS_REQUEST_URL_SEARCH + query.trim().toLowerCase();

        }
        ListView movieListView = (ListView) findViewById(tk.zedlabs.listerr.R.id.list2);
        mEmptyStateTextView    = (TextView) findViewById(tk.zedlabs.listerr.R.id.empty_view2);

        movieListView.setEmptyView(mEmptyStateTextView);

        mAdapter = new MovieAdapter(this, new ArrayList<Movie>());

        movieListView.setAdapter(mAdapter);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(MOVIE_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(tk.zedlabs.listerr.R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText("no connection");
        }

        //
    }


    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {
        return new MovieLoader(this, YTS_REQUEST_URL_SEARCH);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        View loadingIndicator = findViewById(tk.zedlabs.listerr.R.id.loading_indicator2);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText("No Movies!");

        if (movies != null && !movies.isEmpty()) {
            mAdapter.addAll(movies);
            //updateUi(earthquakes);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

        mAdapter.clear();
    }
}
