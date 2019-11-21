package com.wingsmight.makeday;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class SkillsTabAdapter extends RecyclerView.Adapter<SkillsTabAdapter.GenericSkillViewHolder>
{
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private ArrayList<GenericSkill> genericSkills;
    private SkillAdapter subItemAdapter;


    public SkillsTabAdapter(ArrayList<GenericSkill> genericSkills)
    {
        this.genericSkills = genericSkills;
    }

    @NonNull
    @Override
    public GenericSkillViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_generic_skill, viewGroup, false);
        GenericSkillViewHolder viewHolder = new GenericSkillViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GenericSkillViewHolder viewHolder, int i)
    {
        final GenericSkill genericSkill = genericSkills.get(i);
        viewHolder.name.setText(genericSkill.getName());

        // Create layout manager with initial prefetch item count
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                viewHolder.subSkills.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(genericSkill.getSkills().size());

        // Create sub item view adapter
        subItemAdapter = new SkillAdapter(genericSkill.getSkills());

        viewHolder.subSkills.setLayoutManager(layoutManager);
        viewHolder.subSkills.setAdapter(subItemAdapter);
        viewHolder.subSkills.setRecycledViewPool(viewPool);

        boolean isExpanded = genericSkill.isChecked();
        viewHolder.subSkills.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return genericSkills.size();
    }

    public class GenericSkillViewHolder extends  RecyclerView.ViewHolder
    {
        private TextView name;
        private RecyclerView subSkills;
        private LinearLayout skillsHeader;
        private CheckBox checkBox;
        //private LinearLayout expandableLayout;

        public GenericSkillViewHolder(@NonNull View itemView)
        {
            super(itemView);

            name = itemView.findViewById(R.id.genricSkillName);
            subSkills = itemView.findViewById(R.id.skillsRecyclerView);
            skillsHeader = itemView.findViewById(R.id.skillsHeader);
            checkBox = itemView.findViewById(R.id.genericSkillCheckBox);

            skillsHeader.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    GenericSkill clickedSkill = genericSkills.get(getAdapterPosition());
                    clickedSkill.setChecked(!clickedSkill.isChecked());
                    notifyItemChanged(getAdapterPosition());
                }
            });

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    GenericSkill clickedSkill = genericSkills.get(getAdapterPosition());
                    clickedSkill.setChecked(isChecked);
                    notifyItemChanged(getAdapterPosition());

                    ArrayList<Skill> clickedSubskills = clickedSkill.getSkills();
                    for (Skill skill : clickedSubskills)
                    {
                        subItemAdapter.Update();
                    }
                }
            });
        }
    }
}
