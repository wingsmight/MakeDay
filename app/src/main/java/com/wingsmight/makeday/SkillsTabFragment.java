package com.wingsmight.makeday;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.wingsmight.makeday.AnimatedExpandableListView.AnimatedExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SkillsTabFragment extends Fragment implements ExpandableListView.OnGroupClickListener, ExpandableListView.OnChildClickListener,
        ExpandableListView.OnGroupExpandListener
{
    private AnimatedExpandableListView mExpandableListView;
    private SkillsTabAdapter mExpandableListAdapter;
    private int lastExpandedPosition = -1;

    ArrayList<GenericSkill> genericSkills = new ArrayList<>();
    HashMap<GenericSkill, List<Skill>> listItem = new HashMap<>();

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
            public void onClick(View v)
            {
                AddSkill();
            }
        });

        //Expandable list
        InitGenericSkills();

        mExpandableListView = view.findViewById(R.id.expandable_lst);
        mExpandableListAdapter = new SkillsTabAdapter(getContext(), genericSkills, listItem);
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

    private void InitGenericSkills()
    {
        ArrayList<Skill> subSkills = new ArrayList<>();
        subSkills.add(new Skill(0, "Успехи", false));
        subSkills.add(new Skill(1, "Идеи", false));
        subSkills.add(new Skill(2, "Осознания / инсайты", false));
        genericSkills.add(new GenericSkill(0, "Дневник Успехов", false, subSkills));

        subSkills = new ArrayList<>();
        subSkills.add(new Skill(0, "Поднавык 1", false));
        subSkills.add(new Skill(1, "Поднавык 2", false));
        genericSkills.add(new GenericSkill(1, "Навык 2", false, subSkills));



        listItem.put(genericSkills.get(0), genericSkills.get(0).getSkills());
        listItem.put(genericSkills.get(1), genericSkills.get(1).getSkills());
    }

    private void AddSkill()
    {

    }
}
