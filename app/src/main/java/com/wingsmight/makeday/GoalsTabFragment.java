package com.wingsmight.makeday;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class GoalsTabFragment extends Fragment
{
    RecyclerView recyclerView;
    ArrayList<Goal> goals = new ArrayList<>();
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

        goals = FillRecyclerView();
        goalsTabAdapter = new GoalsTabAdapter(view.getContext(), goals);
        recyclerView.setAdapter(goalsTabAdapter);
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
