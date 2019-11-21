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

import java.util.ArrayList;
import java.util.List;


public class SkillsTabFragment extends Fragment
{
    private RecyclerView recyclerView;
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

        FloatingActionButton addButton = view.findViewById(R.id.addSkillButton);
        addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AddSkill();
            }
        });

        genericSkills = InitGenericSkills();
        recyclerView = view.findViewById(R.id.genericSkillRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        SkillsTabAdapter skillsTabAdapter = new SkillsTabAdapter(genericSkills);
        recyclerView.setAdapter(skillsTabAdapter);
    }

    private ArrayList<GenericSkill> InitGenericSkills()
    {
        ArrayList<GenericSkill> skills = new ArrayList<>();

        ArrayList<Skill> subSkills = new ArrayList<>();
        subSkills.add(new Skill(0, "Успехи", false));
        subSkills.add(new Skill(1, "Идеи", false));
        subSkills.add(new Skill(2, "Осознания / инсайты", false));
        skills.add(new GenericSkill(0, "Дневник Успехов", false, subSkills));

        subSkills = new ArrayList<>();
        subSkills.add(new Skill(0, "Поднавык 1", false));
        subSkills.add(new Skill(1, "Поднавык 2", false));
        skills.add(new GenericSkill(1, "Навык 2", false, subSkills));

        return  skills;
    }

    private void AddSkill()
    {

    }
}
