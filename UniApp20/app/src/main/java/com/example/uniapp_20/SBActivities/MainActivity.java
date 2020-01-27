package com.example.uniapp_20.SBActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import com.example.uniapp_20.R;
import com.example.uniapp_20.fragments.DirectoryFragment;
import com.example.uniapp_20.fragments.MapFragment;
import com.example.uniapp_20.fragments.MensaFragment;
import com.example.uniapp_20.fragments.MoreFragment;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.uniapp_20.fragments.NewsFragment;
import com.example.uniapp_20.utils.BottomNavigationBehavior;
import com.example.uniapp_20.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity {


    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    String fragmentName;
    // tags used to attach the fragments
    public static final String TAG_HOME = "News Feed";
    public static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_HOME;

    ProgressBar progressBar;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        mHandler = new Handler();
        progressBar = findViewById(R.id.news_progressbar);

        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // attaching bottom sheet behaviour - hide / show on scroll
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        if(SessionManager.getInstance(getBaseContext()).getScreen().equals("MensaFragment")) {
            navigation.setSelectedItemId(R.id.navigation_mensa);
            loadFragment(new MensaFragment());
        }
        else if(SessionManager.getInstance(getBaseContext()).getScreen().equals("MapFragment")) {
            navigation.setSelectedItemId(R.id.navigation_map);
            loadFragment(new MapFragment());
        }
        else if(SessionManager.getInstance(getBaseContext()).getScreen().equals("MoreFragment"))  {
            navigation.setSelectedItemId(R.id.navigation_more);

            loadFragment(new MoreFragment());
        }
        else if(SessionManager.getInstance(getBaseContext()).getScreen().equals("DirectoryFragment"))  {
            navigation.setSelectedItemId(R.id.navigation_directory);

            loadFragment(new DirectoryFragment());
        }
        else {
            navigation.setSelectedItemId(R.id.navigation_news_feed);
            loadFragment(new NewsFragment());
        }

    }
    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadFragment(final Fragment fragment) {


        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame_container, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_news_feed:
                    toolbar.setTitle("News + Events");
                    fragmentName = "NewsFragment";
                    loadNewFragment(new NewsFragment());
                    return true;
                case R.id.navigation_mensa:
                    toolbar.setTitle("Mensa Menu");
                    fragmentName = "MensaFragment";
                    loadNewFragment(new MensaFragment());
                    return true;
                case R.id.navigation_map:
                    toolbar.setTitle("Map");
                    fragmentName = "MapFragment";
                    loadFragment(new MapFragment());
                    return true;
                case R.id.navigation_directory:
                    toolbar.setTitle("Directory");

                    fragmentName = "DirectoryFragment";

                    loadNewFragment(new DirectoryFragment());
                    return true;
                case R.id.navigation_more:
                    toolbar.setTitle("More");
                    fragmentName = "MoreFragment";
                    loadNewFragment(new MoreFragment());
                    return true;
            }
            return false;
        }
    };


    private void loadNewFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
