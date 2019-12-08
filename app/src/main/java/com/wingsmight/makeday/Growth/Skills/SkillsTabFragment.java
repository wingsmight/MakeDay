package com.wingsmight.makeday.Growth.Skills;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wingsmight.makeday.AnimatedExpandableListView.AnimatedExpandableListView;
import com.wingsmight.makeday.R;
import com.wingsmight.makeday.SavingSystem.SaveLoad;
import com.wingsmight.makeday.TabName;

import java.util.ArrayList;


public class SkillsTabFragment extends Fragment implements ExpandableListView.OnGroupExpandListener
{
    TabName tabName = TabName.SKILLS;
    private ExpandableListView expandableListView;
    private SkillsTabAdapter expandableListAdapter;
    private int lastExpandedPosition = -1;
    ArrayList<GenericSkill> genericSkills;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_skills, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        //Add button
        FloatingActionButton addButton = view.findViewById(R.id.addSkillButton);
        addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AddSkill(view);
            }
        });

        //Expandable list
        genericSkills = SaveLoad.load(tabName);//initGenericSkills();

        expandableListView = view.findViewById(R.id.expandable_lst);
        expandableListAdapter = new SkillsTabAdapter(getContext(), genericSkills);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(this);
    }

    private void backgroundSave(){
        Thread backgroundThread = new Thread() {
            @Override
            public void run()
            {
                SaveLoad.save(tabName, genericSkills);
            }
        };
        backgroundThread.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        backgroundSave();
    }

//    @Override
//    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//        if (expandableListView.isGroupExpanded(groupPosition)) {
//            //expandableListView.collapseGroupWithAnimation(groupPosition);
//        } else {
//            //expandableListView.expandGroupWithAnimation(groupPosition);
//        }
//        return true;
//    }

//    @Override
//    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//        // handle click based on child position
//        return false;
//    }

    @Override
    public void onGroupExpand(int groupPosition) {
        if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition)
        {
            expandableListView.collapseGroup(lastExpandedPosition);
        }
        lastExpandedPosition = groupPosition;
    }


    View dialogView;
    AlertDialog popupDialog;
    private void AddSkill(View view)
    {
        //Show popup
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        dialogView = getLayoutInflater().inflate(R.layout.add_skill_dialog, null);
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
                AddSkillFromInput(inputEditText);
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
                AddSkillFromInput(inputEditText);

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

    private void AddSkillFromInput(TextInputEditText inputEditText)
    {
        String newSkillName = inputEditText.getText().toString();

        if(!newSkillName.equals(""))
        {
            GenericSkill newGenericSkill = new GenericSkill(expandableListAdapter.getGroupCount() + 1, newSkillName, SkillCheckType.NOONECHECK, null);
            //genericSkills.add(newGenericSkill);
            expandableListAdapter.addGenericSkill(newGenericSkill);
        }

        popupDialog.dismiss();
    }
}
