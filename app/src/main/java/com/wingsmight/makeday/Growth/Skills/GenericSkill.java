package com.wingsmight.makeday.Growth.Skills;

import com.wingsmight.makeday.Growth.Skills.Skill;

import java.util.ArrayList;

public class GenericSkill
{
    private int serialNumber;
    private String name;
    private boolean isChecked;
    private ArrayList<Skill> skills;

    public GenericSkill(int serialNumber, String name, boolean isChecked, ArrayList<Skill> skills)
    {
        this.serialNumber = serialNumber;
        this.name = name;
        this.isChecked = isChecked;
        this.skills = skills;
    }

    public int getSerialNumber()
    {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber)
    {
        this.serialNumber = serialNumber;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isChecked()
    {
        return isChecked;
    }

    public void setChecked(boolean checked)
    {
        isChecked = checked;
        for (Skill skill : skills)
        {
            skill.setChecked(checked);
        }
    }

    public ArrayList<Skill> getSkills()
    {
        return skills;
    }

    public void setSkills(ArrayList<Skill> skills)
    {
        this.skills = skills;
    }
}
