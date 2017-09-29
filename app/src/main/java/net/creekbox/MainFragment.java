/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package net.creekbox;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.creekbox.interfaces.PanelService;
import net.creekbox.models.Category;
import net.creekbox.models.VODCategoryResponse;
import net.creekbox.models.VODResponse;
import net.creekbox.models.Video;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainFragment extends BrowseFragment {
    private static final String TAG = "MainFragment";

    private static final int BACKGROUND_UPDATE_DELAY = 300;
    private static final int GRID_ITEM_WIDTH = 200;
    private static final int GRID_ITEM_HEIGHT = 200;
//    private static final int NUM_ROWS = 6;
//    private static final int NUM_COLS = 15;

    private final Handler mHandler = new Handler();
    private ArrayObjectAdapter mRowsAdapter;
    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    private Timer mBackgroundTimer;
    private URI mBackgroundURI;
    private BackgroundManager mBackgroundManager;
//    private ProgressBar loadingicon;
    private ProgressBar progressBar;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onActivityCreated(savedInstanceState);
        prepareBackgroundManager();

        setupUIElements();

        loadRows();

        setupEventListeners();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (null != mBackgroundTimer)
        {
            Log.d(TAG, "onDestroy: " + mBackgroundTimer.toString());
            mBackgroundTimer.cancel();
        }
    }

    private void loadRows()
    {
        loadCategories();

    }

    private void displayContents()
    {
        Log.d("Paras","displayContents()");
        Activity activity = MainActivity.getMainActivity();
        if(!MatildaNewsApplication.getHmap().isEmpty())
        {
            progressBar = (ProgressBar)activity.findViewById(R.id.working);
            progressBar.setVisibility(View.VISIBLE);
            int nCategories = MatildaNewsApplication.getCategoryList().size();
            String strMsg = "Preparing to load categories with count = " + nCategories;
            Log.d("MainFragment::displayContents",strMsg);
//            msgBox(strMsg,"MainFragment");
//            loadingicon = (ProgressBar)activity.findViewById(R.id.loading);
            mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
            CardPresenter cardPresenter = new CardPresenter();
            int i=0;
            for(i=0;i<nCategories;i++)
            {
                Category category = MatildaNewsApplication.getCategoryList().get(i);
                Log.d("PARAS","parsing contents of "+category.getTitle());
                ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
                List<Movie> movieList = MatildaNewsApplication.getHmap().get(category.getTitle());
                if(null!=movieList)
                {
                    Log.d("PARAS","add movies to "+category.getTitle()+" row");
                    int nMovies = movieList.size();
                    for (int j = 0; j < nMovies; j++)
                    {
                        listRowAdapter.add(movieList.get(j));
                    }
                    Log.d("PARAS",movieList.size()+"movies added to "+category.getTitle()+" row");
                }
                else
                {
                    Log.d("PARAS","movieList of "+category.getTitle()+" is null");
                }
                HeaderItem header = new HeaderItem(i, category.getTitle());
                mRowsAdapter.add(new ListRow(header, listRowAdapter));
            }
            setAdapter(mRowsAdapter);
//            loadingicon.setEnabled(false);
            progressBar.setVisibility(View.GONE);
        }
        else
        {
            Log.d("PARAS","hashmap is empty");
        }

//        msgBox("Exiting","MainFragment:displayContents");

    }

    public void loadCategories()
    {
        Log.d("Paras","loadCategories()");
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MatildaNewsApplication.getEndPoint())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        PanelService service = retrofit.create(PanelService.class);

        Call<VODCategoryResponse> call =
                service.getVODCategories(MatildaNewsApplication.getUser(),MatildaNewsApplication.getPass());
        call.enqueue(new Callback<VODCategoryResponse>()
        {
            @Override
            public void onResponse(Call<VODCategoryResponse> call, Response<VODCategoryResponse> response)
            {
                if(null!=response.body())
                {
                    VODCategoryResponse vodCategoryResponse
                            = (VODCategoryResponse) response.body();
                    List<Category> categories = vodCategoryResponse.getCategories();
                    Category tempCategory=new Category();
                    tempCategory.setTitle("Live Streams");
                    tempCategory.setDescription("Lists all live streams");
                    tempCategory.setId("0");
                    categories.add(0,tempCategory);
                    MatildaNewsApplication.setCategoryList(categories);
                    for(Category category:categories)
                    {
                        loadVideosByCategory(category);
                    }
                    Log.d(TAG,"size" + categories.size());
                }

            }

            @Override
            public void onFailure(Call<VODCategoryResponse> call, Throwable t)
            {
                Log.d("Paras","Failure in MainFragment.");
            }
        });

    }
    public void loadVideosByCategory(final Category category)
    {
        Log.d("Paras","loadVideosByCategory() :"+category.getTitle());
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MatildaNewsApplication.getEndPoint())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        PanelService service = retrofit.create(PanelService.class);
        if(category.getId().equals("0"))
        {
            Call<VODResponse> call = service.getStreams(MatildaNewsApplication.getUser(),MatildaNewsApplication.getPass(),"0");
            call.enqueue(new Callback<VODResponse>()
            {
                @Override
                public void onResponse(Call<VODResponse> call, Response<VODResponse> response)
                {
                    if(null!=response.body())
                    {
                        VODResponse vodResponse = (VODResponse) response.body();
                        List<Video> videos = vodResponse.getVideos();
                        List <Movie> movieList = new ArrayList<Movie>();
                        for(Video video : videos)
                        {
                            movieList.add(
                                    MatildaNewsApplication.buildMovieInfo(
                                            category.getTitle(), //category
                                            video.getTitle(),//title
                                            video.getDescription(),//description
                                            " ",//studio
                                            video.getHlsStream(), //videoUrl
                                            video.getImage1(), //cardImage
                                            video.getImage1()//backgroundImage
                                            //getResources().getString(R.string.default_bg_url) //backgroundImage
                                    )
                            );
                        }
                        Log.d("PARAS",movieList.size()+" movies added to category : "+category.getTitle());
                        MatildaNewsApplication.getHmap().put(category.getTitle(),movieList);
                        displayContents();
                    }
                    else
                    {
                        Log.d("PARAS",response.raw().message());
                    }
                }

                @Override
                public void onFailure(Call<VODResponse> call, Throwable t) {

                }
            });
        }
        else
        {
            Call<VODResponse> call =
                    service.getVideosByVODCategory(MatildaNewsApplication.getUser(),MatildaNewsApplication.getPass(),category.getId());
            call.enqueue(new Callback<VODResponse>()
            {
                @Override
                public void onResponse(Call<VODResponse> call, Response<VODResponse> response)
                {
                    if(null!=response.body())
                    {
                        VODResponse vodResponse = (VODResponse) response.body();
                        List<Video> videos = vodResponse.getVideos();
                        List <Movie> movieList = new ArrayList<Movie>();
                        for(Video video : videos)
                        {
                            movieList.add
                            (
                                    MatildaNewsApplication.buildMovieInfo(
                                        category.getTitle(), //category
                                        video.getTitle(),//title
                                        video.getDescription(),//description
                                        " ",//studio
                                        video.getHlsStream(), //videoUrl
                                        video.getImage1(), //cardImage
                                        video.getImage1()//backgroundImage
                                        //getResources().getString(R.string.default_bg_url) //backgroundImage
                                    )
                            );
                        }
                        Log.d("PARAS",movieList.size()+" movies added to category : "+category.getTitle());
                        MatildaNewsApplication.getHmap().put(category.getTitle(),movieList);
                        displayContents();
                    }
                    else
                    {
                        Log.d("PARAS",response.raw().message());
                    }
                }

                @Override
                public void onFailure(Call<VODResponse> call, Throwable t)
                {
                    Log.d("Paras","Failure in MainFragment.");
                }

            });
        }

    }



    private void prepareBackgroundManager()
    {
        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mDefaultBackground = getResources().getDrawable(R.drawable.default_background);
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    private void setupUIElements()
    {
        // setBadgeDrawable(getActivity().getResources().getDrawable(
        // R.drawable.videos_by_google_banner));
        setTitle(getString(R.string.browse_title)); // Badge, when set, takes precedent
        // over title
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);

        // set fastLane (or headers) background color
        setBrandColor(getResources().getColor(R.color.fastlane_background));
        // set search icon color
        setSearchAffordanceColor(getResources().getColor(R.color.search_opaque));
    }

    private void setupEventListeners()
    {
        //commented to disable search
//        setOnSearchClickedListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getActivity(),SearchActivity.class));
//            }
//        });

        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }

    protected void updateBackground(String uri)
    {
        int width = mMetrics.widthPixels;
        int height = mMetrics.heightPixels;
        Glide.with(getActivity())
                .load(uri)
                //.load("http://tvstartup.biz/mng-channel/vpanel/uploads/MatildaNews_fire_tv_background.png")
                .centerCrop()
                .error(mDefaultBackground)
                .into(new SimpleTarget<GlideDrawable>(width, height) {
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable>
                                                        glideAnimation) {
                        mBackgroundManager.setDrawable(resource);
                    }
                });
        mBackgroundTimer.cancel();
    }

    private void startBackgroundTimer()
    {
        if (null != mBackgroundTimer)
        {
            mBackgroundTimer.cancel();
        }
        mBackgroundTimer = new Timer();
        mBackgroundTimer.schedule(new UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY);
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener
    {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row)
        {
            if (item instanceof Movie)
            {
                Movie movie = (Movie) item;

                Log.d(TAG, "Item: " + item.toString());
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.MOVIE, movie);
                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        ((ImageCardView) itemViewHolder.view).getMainImageView(),
                        DetailsActivity.SHARED_ELEMENT_NAME).toBundle();
                getActivity().startActivity(intent, bundle);
                Log.d("Y.Z.H","MainFragment ItemViewClickedListener onItemClicked: instance is Movie item.");
            }
            else if (item instanceof String)
            {
                if (((String) item).indexOf(getString(R.string.error_fragment)) >= 0)
                {
                    Intent intent = new Intent(getActivity(), BrowseErrorActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getActivity(), ((String) item), Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener
    {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                   RowPresenter.ViewHolder rowViewHolder, Row row)
        {
            if (item instanceof Movie)
            {
                mBackgroundURI = ((Movie) item).getBackgroundImageURI();
                startBackgroundTimer();
            }
        }
    }

    private class UpdateBackgroundTask extends TimerTask
    {
        @Override
        public void run()
        {
            mHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    if (mBackgroundURI != null)
                    {
                        updateBackground(mBackgroundURI.toString());
                    }
                }
            });
        }
    }

    private class GridItemPresenter extends Presenter
    {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent)
        {
            TextView view = new TextView(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT));
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setBackgroundColor(getResources().getColor(R.color.default_background));
            view.setTextColor(Color.WHITE);
            view.setGravity(Gravity.CENTER);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Object item)
        {
            ((TextView) viewHolder.view).setText((String) item);
        }

        @Override
        public void onUnbindViewHolder(ViewHolder viewHolder)
        {
        }
    }
}