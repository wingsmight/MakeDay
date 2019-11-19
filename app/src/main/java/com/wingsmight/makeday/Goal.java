package com.wingsmight.makeday;

public class Goal
{
    private int serialNumber;
    private String name;

    public Goal(int serialNumber, String name)
    {
        this.serialNumber = serialNumber;
        this.name = name;
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
