package com.wingsmight.makeday.Growth.Goals;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.kyleduo.blurpopupwindow.library.BlurPopupWindow;
import com.wingsmight.makeday.MainActivity;
import com.wingsmight.makeday.R;

import java.util.ArrayList;

public class GoalsTabFragment extends Fragment
{
    RecyclerView recyclerView;
    GoalsTabAdapter goalsTabAdapter;

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

        ArrayList<Goal> goals = FillRecyclerView();
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

    BlurPopupWindow blurPopupWindow;
    private void AddGoal(View view)
    {
        blurPopupWindow = new BlurPopupWindow.Builder(view.getContext())
                .setContentView(R.layout.add_goal_dialog)
                .bindClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextInputEditText inputEditText = ((View)v.getParent()).findViewById(R.id.newGoalInput);
                        AddGoalFromInput(inputEditText);
                    }
                }, R.id.dialog_like_bt)
                .setGravity(Gravity.CENTER_HORIZONTAL)
                .setScaleRatio(0.2f)
                .setBlurRadius(10)
                .setTintColor(0x30000000)
                .build();

        blurPopupWindow.show();

        TextInputEditText inputEditText = blurPopupWindow.findViewById(R.id.newGoalInput);
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
    }

    private void AddGoalFromInput(TextInputEditText inputEditText)
    {
        String newGoalName = inputEditText.getText().toString();

        if(!newGoalName.equals(""))
        {
            Goal newGoal = new Goal(goalsTabAdapter.getGoalsCount() + 1, newGoalName);
            goalsTabAdapter.addGoal(newGoal);
        }

        blurPopupWindow.dismiss();
    }

    private ArrayList<Goal> FillRecyclerView()
    {
        ArrayList<Goal> goals = new ArrayList<>();
        for (int i = 1; i < 12; i++)
        {
            goals.add(new Goal(i, "Цель..."));
        }

        return goals;
    }
}
