package com.wingsmight.makeday;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.wingsmight.makeday.Growth.GrowthFragment;
import com.wingsmight.makeday.Menu.MenuFragment;
import com.wingsmight.makeday.MyDays.MyDaysFragment;
import com.wingsmight.makeday.SavingSystem.SaveLoad;
import com.wingsmight.makeday.Tracker.TrackerFragment;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_main);

        SetupBottomNavigationView();
    }

    private void SetupBottomNavigationView()
    {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        BottomNavigationView.OnNavigationItemSelectedListener navigationListener =
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


        bottomNavigationView.setOnNavigationItemSelectedListener(navigationListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyDaysFragment()).commit();
    }

    private static Context context;
    public static Context GetContext()
    {
        return context;
    }
}
