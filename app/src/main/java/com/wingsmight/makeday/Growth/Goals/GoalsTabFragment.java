package com.wingsmight.makeday.Growth.Goals;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wingsmight.makeday.DragNDropExpandableListView.DragListener;
import com.wingsmight.makeday.DragNDropExpandableListView.DragNDropGoalListView;
import com.wingsmight.makeday.DragNDropExpandableListView.DropListener;
import com.wingsmight.makeday.DragNDropExpandableListView.RemoveListener;
import com.wingsmight.makeday.MainActivity;
import com.wingsmight.makeday.R;
import com.wingsmight.makeday.SavingSystem.SaveLoad;
import com.wingsmight.makeday.TabName;

import java.util.ArrayList;

public class GoalsTabFragment extends Fragment
{
    TabName tabName = TabName.GOALS;
    ListView listView;
    ListView nonCheckedListView;
    GoalsTabAdapter goalsTabAdapter;
    NonCheckedGoalsTabAdapter nonCheckedGoalsTabAdapter;
    FloatingActionButton addButton;
    ArrayList<Goal> goals;
    ArrayList<Goal> nonCheckedGoals = new ArrayList();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_goals, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        listView = view.findViewById(android.R.id.list);

        goals = SaveLoad.load(tabName);
        for(int i = 0; i < goals.size(); i++)
        {
            if (!goals.get(i).getIsChecked())
            {
                nonCheckedGoals.add(goals.remove(i));
                i--;
            }
        }

        addButton = view.findViewById(R.id.addGoalButton);
        addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AddGoal(view);
            }
        });

        //ActionBar
        setHasOptionsMenu(true);

        //Drag and drop
        nonCheckedGoalsTabAdapter = new NonCheckedGoalsTabAdapter(getActivity(), this, nonCheckedGoals);

        listView = view.findViewById(android.R.id.list);
        goalsTabAdapter = new GoalsTabAdapter(getActivity(), this, goals, nonCheckedGoalsTabAdapter);
        listView.setAdapter(goalsTabAdapter);

        if (listView instanceof DragNDropGoalListView)
        {
            ((DragNDropGoalListView) listView).setDropListener(mDropListener);
            ((DragNDropGoalListView) listView).setRemoveListener(mRemoveListener);
            ((DragNDropGoalListView) listView).setDragListener(mDragListener);
        }

        nonCheckedListView = view.findViewById(R.id.NonCheckedList);
        nonCheckedListView.setVisibility(View.GONE);
        nonCheckedGoalsTabAdapter.addGoalsTabAdapter(goalsTabAdapter);
        nonCheckedListView.setAdapter(nonCheckedGoalsTabAdapter);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        final MenuItem itemEdit = menu.findItem(R.id.edit);
        final MenuItem itemSave = menu.findItem(R.id.save);
        if(itemEdit!=null)
        {
            itemEdit.setVisible(!isEditMode);
            itemEdit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
            {
                @Override
                public boolean onMenuItemClick(MenuItem item)
                {
                    itemEdit.setVisible(false);
                    itemSave.setVisible(true);

                    startEditMode();

                    return true;
                }
            });
        }

        if(itemSave!=null)
        {
            itemSave.setVisible(isEditMode);
            itemSave.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
            {
                @Override
                public boolean onMenuItemClick(MenuItem item)
                {
                    itemEdit.setVisible(true);
                    itemSave.setVisible(false);

                    saveEditMode();

                    return true;
                }
            });
        }
    }


    private DropListener mDropListener =
            new DropListener() {
                public void onDrop(int from, int to) {

                    if (goalsTabAdapter instanceof GoalsTabAdapter) {
                        ((GoalsTabAdapter)goalsTabAdapter).onDrop(from, to);
                        listView.invalidateViews();
                    }
                }

                public void onDropChild(int from, int to) {

                    if (goalsTabAdapter instanceof GoalsTabAdapter) {
                        ((GoalsTabAdapter)goalsTabAdapter).onDropChild(from, to);
                        listView.invalidateViews();
                    }
                }

                @Override
                public void onSwap(int from, int to, ListView listView, int prevPosition)
                {

                    if (goalsTabAdapter instanceof GoalsTabAdapter) {
                        ((GoalsTabAdapter)goalsTabAdapter).onSwap(from, to, listView, prevPosition);
                        listView.invalidateViews();
                    }
                }

                @Override
                public void onSwapChild(int from, int to, ListView listView, int prevPosition)
                {

                    if (goalsTabAdapter instanceof GoalsTabAdapter) {
                        ((GoalsTabAdapter)goalsTabAdapter).onSwapChild(from, to, listView, prevPosition);
                        listView.invalidateViews();
                    }
                }

                @Override
                public void onStartDrag(int from, ListView listView)
                {
                    if (goalsTabAdapter instanceof GoalsTabAdapter) {
                        ((GoalsTabAdapter)goalsTabAdapter).onStartDrag(from, listView);
                        listView.invalidateViews();
                    }
                }
            };

    private RemoveListener mRemoveListener =
            new RemoveListener() {
                public void onRemove(int which) {
                    if (goalsTabAdapter instanceof GoalsTabAdapter) {
                        ((GoalsTabAdapter)goalsTabAdapter).onRemove(which);
                        listView.invalidateViews();
                    }
                }
            };

    public void remove(final int index)
    {
        Animation anim = AnimationUtils.loadAnimation(
                getActivity(), android.R.anim.slide_out_right
        );
        anim.setDuration(350);

        listView.getChildAt(index - listView.getFirstVisiblePosition()).startAnimation(anim);

        new Handler().postDelayed(new Runnable() {

            public void run() {

//                FavouritesManager.getInstance().remove(
//                        FavouritesManager.getInstance().getTripManagerAtIndex(index)
//                );
                //populateList();
                goalsTabAdapter.update();

            }

        }, anim.getDuration());
    }

    public void removeNonChecked(final int index)
    {
        Animation anim = AnimationUtils.loadAnimation(
                getActivity(), android.R.anim.slide_out_right
        );
        anim.setDuration(350);

        nonCheckedListView.getChildAt(index - nonCheckedListView.getFirstVisiblePosition()).startAnimation(anim);

        new Handler().postDelayed(new Runnable() {

            public void run()
            {
                nonCheckedGoalsTabAdapter.update();

            }

        }, anim.getDuration());
    }
    int prevEmptySpacePosition = -1;
    private DragListener mDragListener =
            new DragListener() {

                int backgroundColor = MainActivity.GetContext().getResources().getColor(R.color.colorBackground);
                int defaultBackgroundColor;

                public void onDrag(int x, int y, ListView listView)
                {

                }

                public void onStartDrag(View itemView)
                {
                    prevEmptySpacePosition = -1;
//                for(int i = 0; i < goals.size(); i++)
//                {
//                    getExpandableListView().collapseGroup(i);
//                }

                    defaultBackgroundColor = itemView.getDrawingCacheBackgroundColor();
                    itemView.setBackgroundColor(backgroundColor);

                    int layoutId = itemView.getId();

                    CheckBox checkBox = itemView.findViewById(R.id.goalCheckBox);
                    if(checkBox != null)
                    {
                        checkBox.setVisibility(View.VISIBLE);
                    }

                    TextView goalName = itemView.findViewById(R.id.goalName);
                    if(goalName != null)
                    {
                        goalName.setVisibility(View.VISIBLE);
                        goalName.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));
                    }

                    ImageView deleteGoal = itemView.findViewById(R.id.deleteGoal);
                    if(deleteGoal != null)
                    {
                        deleteGoal.setVisibility(View.GONE);
                    }
                }

                public void onStopDrag(View itemView)
                {
                    if(itemView == null) return;

                    itemView.setVisibility(View.VISIBLE);
                    itemView.setBackgroundColor(defaultBackgroundColor);

                    CheckBox checkBox = itemView.findViewById(R.id.goalCheckBox);
                    checkBox.setVisibility(View.VISIBLE);

                    TextView goalName = itemView.findViewById(R.id.goalName);
                    goalName.setVisibility(View.VISIBLE);
                    goalName.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));

                    ImageView deleteGoal = itemView.findViewById(R.id.deleteGoal);
                    if(deleteGoal != null)
                    {
                        deleteGoal.setVisibility(View.VISIBLE);
                    }
                }

            };

    public static boolean isEditMode = false;
    private void startEditMode()
    {
        isEditMode = true;
        goalsTabAdapter.update();
        nonCheckedListView.setVisibility(View.VISIBLE);
        addButton.hide();
    }

    private void saveEditMode()
    {
        isEditMode = false;
        goalsTabAdapter.update();
        nonCheckedListView.setVisibility(View.GONE);

        Toast.makeText(getActivity(), "Измения сохранены", Toast.LENGTH_SHORT);
        addButton.show();
    }

    private void backgroundSave(){
        Thread backgroundThread = new Thread() {
            @Override
            public void run()
            {
                goals.addAll(nonCheckedGoals);
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
        }, 500);
    }

    private void AddGoalFromInput(TextInputEditText inputEditText)
    {
        String newGoalName = inputEditText.getText().toString();

        if(!newGoalName.equals(""))
        {
            Goal newGoal = new Goal(newGoalName, true);
            goalsTabAdapter.addGoal(newGoal);
        }

        popupDialog.dismiss();
    }
}
