package com.wingsmight.makeday.Growth.Skills;

import java.io.Serializable;
import java.util.ArrayList;

public class GenericSkill implements Serializable
{
    private String name;
    private SkillCheckType isChecked;
    private boolean isExpanded;
    private ArrayList<Skill> skills;

    public GenericSkill(String name, SkillCheckType isChecked, ArrayList<Skill> skills)
    {
        this.name = name;
        this.isChecked = isChecked;
        this.skills = skills;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public SkillCheckType getCheckType()
    {
        return isChecked;
    }

    public void setCheckType(SkillCheckType checked)
    {
        isChecked = checked;
        if(checked == SkillCheckType.ALLCHECK)
        {
            setChildrenChecked(true);
        }
        else if(checked == SkillCheckType.NOONECHECK)
        {
            setChildrenChecked(false);
        }
    }

    private void setChildrenChecked(boolean checked)
    {
        if(skills != null)
        {
            for (Skill skill : skills)
            {
                skill.setChecked(checked);
            }
        }
    }

    public void addChild(Skill skill)
    {
        if(skills == null)
        {
            skills = new ArrayList<>();
        }
        skills.add(skill);
    }

    public ArrayList<Skill> getSkills()
    {
        return skills;
    }

    public void setSkills(ArrayList<Skill> skills)
    {
        this.skills = skills;
    }

    public boolean getIsExpanded()
    {
        return isExpanded;
    }

    public void setIsExpanded(boolean isExpanded)
    {
        this.isExpanded = isExpanded;
    }
}

