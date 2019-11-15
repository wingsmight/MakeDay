package com.wingsmight.makeday;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyDaysFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener =
            new BottomNavigationView.OnNavigationItemSelectedListener()
            {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
                {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId())
                    {
                        case R.id.bottom_navigation_my_days0:
                        {
                            selectedFragment = new MyDaysFragment();
                            break;
                        }
                        case R.id.bottom_navigation_tracker1:
                        {
                            selectedFragment = new TrackerFragment();
                            break;
                        }
                        case R.id.bottom_navigation_growth2:
                        {
                            selectedFragment = new GrowthFragment();
                            break;
                        }
                        case R.id.bottom_navigation_menu3:
                        {
                            selectedFragment = new MenuFragment();
                            break;
                        }
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };
}
