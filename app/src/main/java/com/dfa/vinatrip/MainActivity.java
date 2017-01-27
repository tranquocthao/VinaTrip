package com.dfa.vinatrip;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private LocationFragment locationFragment;
    private PlanFragment planFragment;
    private ShareFragment shareFragment;
    private MemoryFragment memoryFragment;
    private BottomNavigationView bottomNavigationView;
    private boolean doubleBackPress = false;
    private int selectedItemId;
    private Toolbar toolbar;
    private android.support.v7.app.ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupActionBar();

        changeColorStatusBar();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        // Khi lớn hơn 3 icon sẽ xảy ra ShiftMode, dùng để chặn ShiftMode này
        StopShiftModeBottomNavView.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                // true thì item của bottom bar mới chuyển đổi qua lại được
                return true;
            }
        });

        addNewFragments();

        // load fragment_location lên đầu tiên
        MenuItem selectedItem;
        if (savedInstanceState != null) {
            selectedItem = bottomNavigationView.getMenu().findItem(savedInstanceState.getInt("arg_selected_item", 0));
        } else {
            selectedItem = bottomNavigationView.getMenu().getItem(0);
        }
        selectFragment(selectedItem);
    }

    public void changeColorStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.BLACK);
        }
    }

    public void setupActionBar() {
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    public void selectFragment(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.iconLocation:
                getSupportFragmentManager().beginTransaction().show(locationFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(planFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(shareFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(memoryFragment).commit();
                break;
            case R.id.iconPlan:
                getSupportFragmentManager().beginTransaction().hide(locationFragment).commit();
                getSupportFragmentManager().beginTransaction().show(planFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(shareFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(memoryFragment).commit();
                break;
            case R.id.iconShare:
                getSupportFragmentManager().beginTransaction().hide(locationFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(planFragment).commit();
                getSupportFragmentManager().beginTransaction().show(shareFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(memoryFragment).commit();
                break;
            case R.id.iconMemory:
                getSupportFragmentManager().beginTransaction().hide(locationFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(planFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(shareFragment).commit();
                getSupportFragmentManager().beginTransaction().show(memoryFragment).commit();
                break;
        }
        selectedItemId = item.getItemId();
    }

    public void addNewFragments() {
        locationFragment = new LocationFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayoutContainer, locationFragment).commit();
        planFragment = new PlanFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayoutContainer, planFragment).commit();
        shareFragment = new ShareFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayoutContainer, shareFragment).commit();
        memoryFragment = new MemoryFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayoutContainer, memoryFragment).commit();
    }

    @Override
    public void onBackPressed() {
        MenuItem homeItem = bottomNavigationView.getMenu().getItem(0);
        if (selectedItemId != homeItem.getItemId()) {
            selectFragment(homeItem);
            // nếu không có, bottom bar sẽ không quay lại icon home
            for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
                MenuItem menuItem = bottomNavigationView.getMenu().getItem(i);
                menuItem.setChecked(menuItem.getItemId() == homeItem.getItemId());
            }
        } else {
            if (doubleBackPress) {
                super.onBackPressed();
            } else {
                doubleBackPress = true;
                Toast.makeText(MainActivity.this, "Nhấn BACK thêm một lần nữa để thoát", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackPress = false;
                    }
                    // không nhấn nhanh thì phải nhấn 2 lần lại
                }, 2000);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        // Expand searchView, nếu không nó chỉ thu gọn lại thành icon
        searchView.setIconifiedByDefault(false);

        searchView.setQueryHint("Tìm kiếm...");

        return true;
    }
}

