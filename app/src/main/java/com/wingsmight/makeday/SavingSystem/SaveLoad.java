package com.wingsmight.makeday.SavingSystem;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wingsmight.makeday.Growth.Goals.Goal;
import com.wingsmight.makeday.Growth.Skills.GenericSkill;
import com.wingsmight.makeday.MainActivity;
import com.wingsmight.makeday.TabName;
import com.wingsmight.makeday.Tracker.Emotions.EmotionEvent;
import com.wingsmight.makeday.Tracker.Event;
import com.wingsmight.makeday.Tracker.RowDayModel;
import com.wingsmight.makeday.Tracker.TimeTracking.TimeTrackingDay;

import java.util.ArrayList;
import java.util.List;

public class SaveLoad
{
    public static void save(TabName tabName, ArrayList tabArray)
    {
        Gson gson = new Gson();

        String json = gson.toJson(tabArray);
        saveJSON("TabArray", tabName.toString(), json);
    }

    public static ArrayList load(TabName tabName)
    {
        Gson gson = new Gson();

        String json = loadJSON("TabArray", tabName.toString());

        switch (tabName)
        {
            case MY_DAYS:
            {
                return gson.fromJson(json, new TypeToken<List<RowDayModel>>(){}.getType());
            }
            case TIME_TRACKING:
            {
                return gson.fromJson(json, new TypeToken<List<TimeTrackingDay<Event>>>(){}.getType());
            }
            case EMOTIONS:
            {
                return gson.fromJson(json, new TypeToken<List<TimeTrackingDay<EmotionEvent>>>(){}.getType());
            }
            case SKILLS:
            {
                return gson.fromJson(json, new TypeToken<List<GenericSkill>>(){}.getType());
            }
            case GOALS:
            {
                return gson.fromJson(json, new TypeToken<List<Goal>>(){}.getType());
            }
            case MENU:
            {
                return null;//return gson.fromJson(json, new TypeToken<List<TimeTrackingDay>>(){}.getType());
            }
            default:
            {
                return null;
            }
        }
    }

    private static void saveJSON(String fileName, String key, String value)
    {
        SharedPreferences sharedPreferences = MainActivity.GetContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorSave = sharedPreferences.edit();

        editorSave.putString(key, value);
        editorSave.apply();
    }

    private static String loadJSON(String fileName, String key)
    {
        SharedPreferences sharedPreferences = MainActivity.GetContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }
}
