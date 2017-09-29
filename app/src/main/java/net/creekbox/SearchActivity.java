package net.creekbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by opnchaudhary on 6/7/16.
 */
public class SearchActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }
    @Override
    public boolean onSearchRequested() {
        startActivity(new Intent(this, SearchActivity.class));
        return true;
    }
}
