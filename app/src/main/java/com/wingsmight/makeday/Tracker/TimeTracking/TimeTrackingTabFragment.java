package com.wingsmight.makeday.Tracker.TimeTracking;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.kyleduo.blurpopupwindow.library.BlurPopupWindow;
import com.wingsmight.makeday.SavingSystem.SaveLoad;
import com.wingsmight.makeday.TabName;
import com.wingsmight.makeday.R;

import java.util.ArrayList;

public class TimeTrackingTabFragment extends Fragment
{
    TabName tabName = TabName.TIME_TRACKING;
    RecyclerView recyclerView;
    ArrayList<TimeTrackingDay> timeTrackingDays = new ArrayList<>();
    TimeTrackingTabAdapter timeTrackingTabAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_time_tracking_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        recyclerView = view.findViewById(R.id.timeTrackerRecyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        timeTrackingDays = SaveLoad.load(tabName);
        timeTrackingTabAdapter = new TimeTrackingTabAdapter(timeTrackingDays);
        recyclerView.setAdapter(timeTrackingTabAdapter);

        //Button setLaunchSettings
        ExtendedFloatingActionButton launchButton = view.findViewById(R.id.launchButton);
        launchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                setLaunchSettings(view);
            }
        });
    }

    View dialogView;
    AlertDialog popupDialog;
    private void setLaunchSettings(View view)
    {
        //Show popup
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        dialogView = getLayoutInflater().inflate(R.layout.launch_tracking_dialog, null);
        builder.setView(dialogView);
        popupDialog = builder.create();
        popupDialog.show();

        ImageButton sendButton = dialogView.findViewById(R.id.dialog_like_bt);
        sendButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TextInputEditText inputEditText = ((View)v.getParent()).findViewById(R.id.newGoalInput);
                launch(inputEditText);
            }
        });
    }

    private void launch(TextInputEditText inputEditText)
    {

    }

    private void backgroundSave(){
        Thread backgroundThread = new Thread() {
            @Override
            public void run()
            {
                SaveLoad.save(tabName, timeTrackingDays);
            }
        };
        backgroundThread.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        backgroundSave();
    }
}
