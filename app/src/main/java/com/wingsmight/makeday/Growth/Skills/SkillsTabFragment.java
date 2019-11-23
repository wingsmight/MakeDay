package com.wingsmight.makeday.Growth.Skills;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.kyleduo.blurpopupwindow.library.BlurPopupWindow;
import com.wingsmight.makeday.AnimatedExpandableListView.AnimatedExpandableListView;
import com.wingsmight.makeday.Growth.Goals.Goal;
import com.wingsmight.makeday.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SkillsTabFragment extends Fragment implements ExpandableListView.OnGroupClickListener, ExpandableListView.OnChildClickListener,
        ExpandableListView.OnGroupExpandListener
{
    private AnimatedExpandableListView mExpandableListView;
    private SkillsTabAdapter mExpandableListAdapter;
    private int lastExpandedPosition = -1;

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
        ArrayList<GenericSkill> genericSkills = initGenericSkills();

        mExpandableListView = view.findViewById(R.id.expandable_lst);
        mExpandableListAdapter = new SkillsTabAdapter(getContext(), genericSkills);
        mExpandableListView.setAdapter(mExpandableListAdapter);
        mExpandableListView.setOnGroupClickListener(this);
        mExpandableListView.setOnChildClickListener(this);
        mExpandableListView.setOnGroupExpandListener(this);
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        if (mExpandableListView.isGroupExpanded(groupPosition)) {
            mExpandableListView.collapseGroupWithAnimation(groupPosition);
        } else {
            mExpandableListView.expandGroupWithAnimation(groupPosition);
        }
        return true;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        // handle click based on child position
        return false;
    }

    /**
     * This is done in order to close the previously expanded group
     * when clicked on a new group
     *
     *@param groupPosition
     */
    @Override
    public void onGroupExpand(int groupPosition) {
        if (lastExpandedPosition != -1
                && groupPosition != lastExpandedPosition) {
            mExpandableListView.collapseGroup(lastExpandedPosition);
        }
        lastExpandedPosition = groupPosition;
    }

    private ArrayList<GenericSkill> initGenericSkills()
    {
        ArrayList<GenericSkill> genericSkills = new ArrayList<>();

        ArrayList<Skill> subSkills = new ArrayList<>();
        subSkills.add(new Skill(1, "Успехи", false));
        subSkills.add(new Skill(2, "Идеи", false));
        subSkills.add(new Skill(3, "Осознания / инсайты", false));
        genericSkills.add(new GenericSkill(1, "Дневник Успехов", false, subSkills));

        subSkills = new ArrayList<>();
        subSkills.add(new Skill(1, "Поднавык 1", false));
        subSkills.add(new Skill(2, "Поднавык 2", false));
        genericSkills.add(new GenericSkill(2, "Навык 2", false, subSkills));

        return genericSkills;
    }

    BlurPopupWindow blurPopupWindow;
    private void AddSkill(View view)
    {
        //Show blur popup
        blurPopupWindow = new BlurPopupWindow.Builder(view.getContext())
                .setContentView(R.layout.add_skill_dialog)
                .bindClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextInputEditText inputEditText = ((View)v.getParent()).findViewById(R.id.newGoalInput);
                        AddSkillFromInput(inputEditText);
                    }
                }, R.id.dialog_like_bt)
                .setGravity(Gravity.CENTER_HORIZONTAL)
                .setScaleRatio(0.2f)
                .setBlurRadius(10)
                .setTintColor(0x30000000)
                .build();
        blurPopupWindow.show();

        //Enter button -> add goal
        final TextInputEditText inputEditText = blurPopupWindow.findViewById(R.id.newGoalInput);
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
            GenericSkill newGenericSkill = new GenericSkill(mExpandableListAdapter.getGroupCount() + 1, newSkillName, false, null);
            //genericSkills.add(newGenericSkill);
            mExpandableListAdapter.addGenericSkill(newGenericSkill);
        }

        blurPopupWindow.dismiss();
    }
}
