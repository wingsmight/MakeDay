package com.wingsmight.makeday.Growth.Goals;

import android.content.Context;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.CompoundButtonCompat;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wingsmight.makeday.DragNDropExpandableListView.DropListener;
import com.wingsmight.makeday.DragNDropExpandableListView.RemoveListener;
import com.wingsmight.makeday.R;

import java.util.ArrayList;

public class GoalsTabAdapter extends BaseAdapter implements RemoveListener, DropListener
{
    public static ArrayList<Goal> goals;
    private NonCheckedGoalsTabAdapter nonCheckedGoalsTabAdapter;
    private Fragment fragment;
    private Context context;

    public GoalsTabAdapter(Context context, Fragment fragment, ArrayList<Goal> goals, NonCheckedGoalsTabAdapter nonCheckedGoalsTabAdapter)
    {
        this.goals = goals;
        this.fragment = fragment;
        this.nonCheckedGoalsTabAdapter = nonCheckedGoalsTabAdapter;
        this.context = context;
    }

    @Override
    public int getCount()
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

    @Override
    public Object getItem(int position)
    {
        return goals.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_goal, parent, false);
        }

        Goal currentGoal = (Goal)getItem(position);

        //Set name
        String GoalName = currentGoal.getName();

        if (GoalName.equals(""))
        {
            TextView textView = convertView.findViewById(R.id.goalName);
            textView.setText("");
            CheckBox checkBox = convertView.findViewById(R.id.goalCheckBox);
            setCheckBoxColor(checkBox, context.getResources().getColor(R.color.white));
            ImageView deleteGoal = convertView.findViewById(R.id.deleteGoal);
            deleteGoal.setImageDrawable(null);
            ImageView dragHandle = convertView.findViewById(R.id.dragHandleGoal);
            dragHandle.setImageDrawable(null);

            return convertView;
        }

        TextView textView = convertView.findViewById(R.id.goalName);
        textView.setText(GoalName);
        textView.setTextColor(context.getResources().getColor(android.R.color.tab_indicator_text));

        //Init checkboxes
        CheckBox checkBox = convertView.findViewById(R.id.goalCheckBox);
        checkBox.setTag(position);
        checkBox.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int pos = (Integer) v.findViewById(R.id.goalCheckBox).getTag();

                Goal goal = (Goal) getItem(pos);
                if(goal.getIsChecked())
                {
                    goal.setIsChecked(false);

                    goals.remove(goal);
                    nonCheckedGoalsTabAdapter.addGoal(goal);
                }
                else
                {
                    goal.setIsChecked(true);
                }

                notifyDataSetChanged();
            }
        });

        //Set checkbox
        int groupCheckColor;
        boolean isChecked = currentGoal.getIsChecked();
        if(isChecked)
        {
            currentGoal.setIsChecked(true);
            groupCheckColor = context.getResources().getColor(R.color.colorAccent);
            setCheckBoxColor(checkBox, groupCheckColor);
            checkBox.setChecked(true);
        }
        else
        {
            currentGoal.setIsChecked(false);
            groupCheckColor = Color.GRAY;
            setCheckBoxColor(checkBox, groupCheckColor);
            checkBox.setChecked(false);
        }

        ImageView deleteGoal = convertView.findViewById(R.id.deleteGoal);
        Drawable deleteGoalDrawable = context.getResources().getDrawable(R.drawable.ic_delete);
        deleteGoalDrawable.mutate().setColorFilter(context.getResources().getColor(R.color.redDelete), PorterDuff.Mode.SRC_IN);
        deleteGoal.setImageDrawable(deleteGoalDrawable);

        deleteGoal.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((GoalsTabFragment)fragment).remove(position);
                onRemove(position);
            }
        });

        ImageView dragHandle = convertView.findViewById(R.id.dragHandleGoal);
        Drawable dragHandleDrawable = context.getResources().getDrawable(R.drawable.ic_drag_handle);
        dragHandleDrawable.mutate().setColorFilter(context.getResources().getColor(R.color.dragHandle), PorterDuff.Mode.SRC_IN);
        dragHandle.setImageDrawable(dragHandleDrawable);

        if(GoalsTabFragment.isEditMode)
        {
            deleteGoal.setVisibility(View.VISIBLE);
            dragHandle.setVisibility(View.VISIBLE);
        }
        else
        {
            deleteGoal.setVisibility(View.GONE);
            dragHandle.setVisibility(View.GONE);
        }

        return convertView;
    }

    private void setCheckBoxColor(CheckBox checkBox, int groupCheckColor)
    {
        checkBox.setButtonDrawable(R.drawable.abc_btn_check_material);

        Drawable drawable = CompoundButtonCompat.getButtonDrawable(checkBox);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, groupCheckColor);
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
    }


    public void addGoal(Goal newGoal)
    {
        if(goals == null)
        {
            goals = new ArrayList<>();
        }
        goals.add(newGoal);
    }

    private int prevTo = -1;
    private int prevToChild = -1;
    private Goal fromGoal;

    @Override
    public void onSwap(int from, int to, ListView listView, int prevPosition)
    {
        if(prevTo != -1)
        {
            goals.remove(prevTo);
            prevTo = to;
        }
        else
        {
            fromGoal = goals.remove(from);
            prevTo = to;
        }
        goals.add(to, new Goal("", false));

        notifyDataSetChanged();
    }
    @Override
    public void onSwapChild(int from, int to, ListView listView, int prevPosition)
    {

    }

    @Override
    public void onStartDrag(int from, ListView listView)
    {
        prevTo = -1;
        prevToChild = -1;
    }

    @Override
    public void onDrop(int from, int to)
    {
        Goal removingGoal = goals.remove(to);
        Goal temp = fromGoal;

        if(temp != null)
        {
            goals.add(to,temp);

            notifyDataSetChanged();
            fromGoal = null;
        }
        else
        {
            goals.add(to, removingGoal);
        }
    }

    @Override
    public void onDropChild(int from, int to)
    {

    }

    @Override
    public void onRemove(int which)
    {
        if (which < 0 || which > goals.size()) return;
        goals.remove(which);
    }

    public void update()
    {
        notifyDataSetChanged();
    }
}
