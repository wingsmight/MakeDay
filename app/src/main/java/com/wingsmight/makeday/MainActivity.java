package com.wingsmight.makeday;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.wingsmight.makeday.Growth.GrowthFragment;
import com.wingsmight.makeday.Menu.MenuFragment;
import com.wingsmight.makeday.MyDays.MyDaysFragment;
import com.wingsmight.makeday.SavingSystem.SaveLoad;
import com.wingsmight.makeday.Tracker.TrackerFragment;

public class MainActivity extends AppCompatActivity
{
    final Fragment fragment1 = new MyDaysFragment();
    final Fragment fragment2 = new TrackerFragment();
    final Fragment fragment3 = new GrowthFragment();
    final Fragment fragment4 = new MenuFragment();
    final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment active = fragment1;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_main);

        fragmentManager.beginTransaction().add(R.id.main_container, fragment4, "4").hide(fragment4).commit();
        fragmentManager.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit();
        fragmentManager.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit();
        fragmentManager.beginTransaction().add(R.id.main_container,fragment1, "1").commit();

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
                        switch (menuItem.getItemId()) {
                            case R.id.bottom_navigation_my_days0:
                                fragmentManager.beginTransaction().hide(active).show(fragment1).commit();
                                active = fragment1;
                                return true;

                            case R.id.bottom_navigation_tracker1:
                                fragmentManager.beginTransaction().hide(active).show(fragment2).commit();
                                active = fragment2;
                                return true;

                            case R.id.bottom_navigation_growth2:
                                fragmentManager.beginTransaction().hide(active).show(fragment3).commit();
                                active = fragment3;
                                return true;

                            case R.id.bottom_navigation_menu3:
                                fragmentManager.beginTransaction().hide(active).show(fragment4).commit();
                                active = fragment4;
                                return true;
                        }

                        return true;
                    }
                };

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationListener);
    }

    private static Context context;
    public static Context GetContext()
    {
        return context;
    }
}
