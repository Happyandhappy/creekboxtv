package net.creekbox;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.SearchFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.SpeechRecognitionCallback;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by opnchaudhary on 6/7/16.
 */
public class CustomSearchFragment extends SearchFragment implements SearchFragment.SearchResultProvider {
    private static final String TAG = SearchFragment.class.getSimpleName();

    private static final int REQUEST_SPEECH = 0x00000010;
    private ArrayObjectAdapter mRowsAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());

        setSearchResultProvider(this);

        if (!MatildaNewsApplication.hasPermission(getActivity(), Manifest.permission.RECORD_AUDIO)) {
            // SpeechRecognitionCallback is not required and if not provided recognition will be handled
            // using internal speech recognizer, in which case you must have RECORD_AUDIO permission
            setSpeechRecognitionCallback(new SpeechRecognitionCallback() {
                @Override
                public void recognizeSpeech() {
                    Log.v(TAG, "recognizeSpeech");
                    try {
                        startActivityForResult(getRecognizerIntent(), REQUEST_SPEECH);
                    } catch (ActivityNotFoundException e) {
                        Log.e(TAG, "Cannot find activity for speech recognizer", e);
                    }
                }
            });
        }

    }

    @Override
    public ObjectAdapter getResultsAdapter() {
        Log.d(TAG, "getResultsAdapter");
        Log.d(TAG, mRowsAdapter.toString());

        // It should return search result here,
        // but static Movie Item list will be returned here now for practice.
        //ArrayList<Movie> mItems = MovieProvider.getMovieItems();
        Random rn = new Random();
        List<Movie> mItems = MatildaNewsApplication.getHmap().get(MatildaNewsApplication.categoryList.get(rn.nextInt(MatildaNewsApplication.categoryList.size())).getTitle());
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());
        listRowAdapter.addAll(0, mItems);
        HeaderItem header = new HeaderItem("Search results");
        mRowsAdapter.add(new ListRow(header, listRowAdapter));

        return mRowsAdapter;
    }

    @Override
    public boolean onQueryTextChange(String newQuery){
        Log.i(TAG, String.format("Search Query Text Change %s", newQuery));
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.i(TAG, String.format("Search Query Text Submit %s", query));
        return true;
    }
}
