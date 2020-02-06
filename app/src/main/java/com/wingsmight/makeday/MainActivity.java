package com.wingsmight.makeday;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;

import com.wingsmight.makeday.Growth.GrowthFragment;
import com.wingsmight.makeday.Menu.MenuFragment;
import com.wingsmight.makeday.MyDays.MyDaysFragment;
import com.wingsmight.makeday.SavingSystem.SaveLoad;
import com.wingsmight.makeday.Tracker.TrackerFragment;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
{
    final Fragment fragment1 = new MyDaysFragment();
    final Fragment fragment2 = new TrackerFragment();
    final Fragment fragment3 = new GrowthFragment();
    final Fragment fragment4 = new MenuFragment();
    final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment active = fragment1;

    //public static boolean isAppInForeground;

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

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);

        int hourOfDay = Integer.valueOf(SaveLoad.loadString(SaveLoad.defaultSavingPath, "eveningTime_HoursOfDay", "22"));
        int minute =  Integer.valueOf(SaveLoad.loadString(SaveLoad.defaultSavingPath, "eveningTime_Minute", "00"));

        Calendar eveningDate = Calendar.getInstance();
        eveningDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
        eveningDate.set(Calendar.MINUTE, minute);
        eveningDate.set(Calendar.SECOND, 0);

        if(eveningDate.getTimeInMillis() < System.currentTimeMillis())
        {
            eveningDate.setTimeInMillis(eveningDate.getTimeInMillis() + 86400000);
        }

        GoalCheckingActivity.setEveningSkillNotify(this, eveningDate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);

        MenuItem itemEdit = menu.findItem(R.id.edit);
        if(itemEdit!=null)
        {
            itemEdit.setVisible(false);
        }
        MenuItem itemSave = menu.findItem(R.id.save);
        if(itemSave!=null)
        {
            itemSave.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    private void SetupBottomNavigationView()
    {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
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
        });
    }

    private static Context context;
    public static Context GetContext()
    {
        return context;
    }
}
