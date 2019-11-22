package com.wingsmight.makeday.Growth.Skills;

public class Skill
{
    private int serialNumber;
    private String name;
    private boolean isChecked;

    public Skill(int serialNumber, String name, boolean isChecked)
    {
        this.serialNumber = serialNumber;
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

    public int getSerialNumber()
    {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber)
    {
        this.serialNumber = serialNumber;
    }
}
