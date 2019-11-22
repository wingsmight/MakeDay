package com.wingsmight.makeday.Growth.Skills;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.wingsmight.makeday.R;

import java.util.ArrayList;

public class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.SkillViewHolder>
{
    private ArrayList<Skill> skills;

    public SkillAdapter(ArrayList<Skill> skills)
    {
        this.skills = skills;
    }

    @NonNull
    @Override
    public SkillViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_skill, viewGroup, false);

        return new SkillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillViewHolder viewHolder, int i)
    {
        final Skill skill = skills.get(i);

        viewHolder.skillName.setText(skill.getName());
        viewHolder.skillName.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //ShowDialog
            }
        });
    }

    @Override
    public int getItemCount() {
        return skills.size();
    }

    public void Update()
    {
    }

    public class SkillViewHolder extends  RecyclerView.ViewHolder
    {
        TextView skillName;
        ImageView skillInfo;
        CheckBox skillCheckBox;

        public SkillViewHolder(@NonNull View itemView)
        {
            super(itemView);

            skillName = itemView.findViewById(R.id.skillName);
            skillInfo = itemView.findViewById(R.id.skillInfo);
            skillCheckBox = itemView.findViewById(R.id.skillCheckBox);

            skillCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    skills.get(getAdapterPosition()).setChecked(isChecked);
                }
            });
        }
    }
}
