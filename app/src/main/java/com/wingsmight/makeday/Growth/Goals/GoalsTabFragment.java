package com.wingsmight.makeday.Growth.Goals;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kyleduo.blurpopupwindow.library.BlurPopupWindow;
import com.wingsmight.makeday.R;
import com.wingsmight.makeday.SavingSystem.SaveLoad;
import com.wingsmight.makeday.TabName;

import java.util.ArrayList;

public class GoalsTabFragment extends Fragment
{
    TabName tabName = TabName.GOALS;
    RecyclerView recyclerView;
    GoalsTabAdapter goalsTabAdapter;
    ArrayList<Goal> goals;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_goals, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        recyclerView = view.findViewById(R.id.goalsRecyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        // = FillRecyclerView();
        goals = SaveLoad.load(tabName);

        goalsTabAdapter = new GoalsTabAdapter(view.getContext(), goals);
        recyclerView.setAdapter(goalsTabAdapter);

        FloatingActionButton addButton = view.findViewById(R.id.addGoalButton);
        addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AddGoal(view);
            }
        });
    }

    private void backgroundSave(){
        Thread backgroundThread = new Thread() {
            @Override
            public void run()
            {
                SaveLoad.save(tabName, goals);
            }
        };
        backgroundThread.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        backgroundSave();
    }

    View dialogView;
    AlertDialog popupDialog;
    private void AddGoal(View view)
    {
        //Show popup
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        dialogView = getLayoutInflater().inflate(R.layout.add_goal_dialog, null);
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
                AddGoalFromInput(inputEditText);
            }
        });

        //Enter button -> add goal
        final TextInputEditText inputEditText = dialogView.findViewById(R.id.newGoalInput);
        inputEditText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                TextInputEditText inputEditText = ((View)v.getParent()).findViewById(R.id.newGoalInput);
                AddGoalFromInput(inputEditText);

                return true;
            }
        });

        //Show keyboard
        new Handler().postDelayed(new Runnable() {
            public void run() {

                inputEditText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN , 0, 0, 0));
                inputEditText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP , 0, 0, 0));
            }
        }, 1000);
    }

    private void AddGoalFromInput(TextInputEditText inputEditText)
    {
        String newGoalName = inputEditText.getText().toString();

        if(!newGoalName.equals(""))
        {
            Goal newGoal = new Goal(goalsTabAdapter.getGoalsCount() + 1, newGoalName);
            goalsTabAdapter.addGoal(newGoal);
        }

        popupDialog.dismiss();
    }
}
