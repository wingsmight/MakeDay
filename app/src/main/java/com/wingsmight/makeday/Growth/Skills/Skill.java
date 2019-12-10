package com.wingsmight.makeday.Growth.Skills;

import java.io.Serializable;

public class Skill implements Serializable
{
    private String name;
    private boolean isChecked;

    public Skill(String name, boolean isChecked)
    {
        this.name = name;
        this.isChecked = isChecked;
    }

    public boolean isChecked()
    {
        return isChecked;
    }

    public void setChecked(boolean checked)
    {
        isChecked = checked;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
