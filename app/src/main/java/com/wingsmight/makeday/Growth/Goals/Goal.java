package com.wingsmight.makeday.Growth.Goals;

public class Goal
{
    private String name;
    private boolean isChecked;

    public Goal(String name, boolean isChecked)
    {
        this.name = name;
        this.isChecked = isChecked;
    }

    public boolean getIsChecked()
    {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked)
    {
        this.isChecked = isChecked;
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
