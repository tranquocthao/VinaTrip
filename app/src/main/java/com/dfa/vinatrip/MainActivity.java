package com.dfa.vinatrip;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

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

        // thay đổi màu status bar
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
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorStatusBar));
        }
    }

    public void setupActionBar() {
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
    }

    public void selectFragment(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.iconLocation:
                LocationFragment locationFragment = new LocationFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutContainer, locationFragment).commit();
                break;
            case R.id.iconPlan:
                PlanFragment planFragment = new PlanFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutContainer, planFragment).commit();
                break;
            case R.id.iconShare:
                ShareFragment shareFragment = new ShareFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutContainer, shareFragment).commit();
                break;
            case R.id.iconMemory:
                MemoryFragment memoryFragment = new MemoryFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutContainer, memoryFragment).commit();
                break;
        }
        selectedItemId = item.getItemId();
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }
}
