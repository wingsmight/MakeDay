package com.wingsmight.makeday.Growth.Goals;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wingsmight.makeday.R;

import java.util.ArrayList;

public class GoalsTabAdapter extends RecyclerView.Adapter<GoalsTabAdapter.MyDaysViewHolder>
{
    private ArrayList<Goal> goals;
    private Context context;

    public GoalsTabAdapter(Context context, ArrayList<Goal> goals)
    {
        this.goals = goals;
        this.context = context;
    }

    @NonNull
    @Override
    public MyDaysViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_goal, viewGroup, false);
        MyDaysViewHolder viewHolder = new MyDaysViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyDaysViewHolder viewHolder, int i)
    {
        Goal goal = goals.get(i);

        viewHolder.name.setText(goal.getName());
        viewHolder.serialNumber.setText(goal.getSerialNumber() + ". ");
    }

    @Override
    public int getItemCount() {
        if(goals == null)
        {
            return 0;
        }
        else
        {
            return goals.size();
        }
    }

    public class MyDaysViewHolder extends  RecyclerView.ViewHolder
    {
        TextView name, serialNumber;

        public MyDaysViewHolder(@NonNull View itemView)
        {
            super(itemView);

            name = itemView.findViewById(R.id.goalName);
            serialNumber = itemView.findViewById(R.id.goalSerialNumber);
        }
    }

    public void addGoal(Goal newGoal)
    {
        if(goals == null)
        {
            goals = new ArrayList<>();
        }
        goals.add(newGoal);
    }

    public int getGoalsCount()
    {
        if(goals == null)
        {
            return 0;
        }
        else
        {
            return goals.size();
        }
    }
}
