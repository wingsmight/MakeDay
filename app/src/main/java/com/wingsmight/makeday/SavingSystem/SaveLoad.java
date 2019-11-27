package com.wingsmight.makeday.SavingSystem;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wingsmight.makeday.Growth.Goals.Goal;
import com.wingsmight.makeday.Growth.Skills.GenericSkill;
import com.wingsmight.makeday.MainActivity;
import com.wingsmight.makeday.SplashActivity;
import com.wingsmight.makeday.TabName;
import com.wingsmight.makeday.TabInitialization;
import com.wingsmight.makeday.Tracker.Emotions.EmotionEvent;
import com.wingsmight.makeday.Tracker.Event;
import com.wingsmight.makeday.Tracker.RowDayModel;
import com.wingsmight.makeday.Tracker.TimeTracking.TimeTrackingDay;

import java.util.ArrayList;
import java.util.List;

public class SaveLoad
{
    private static final String defaultSavingPath = "defaultSavingPath";

    private static ArrayList<RowDayModel> list0;
    private static ArrayList<TimeTrackingDay<Event>> list1;
    private static ArrayList<TimeTrackingDay<EmotionEvent>> list2;
    private static ArrayList<GenericSkill> list3;
    private static ArrayList<Goal> list4;
    //private static List<> list5;

    public static void testInit(TabName tabName)
    {
        switch (tabName)
        {
            case MY_DAYS:
            {
                list0 = TabInitialization.initMyDays();
                break;
            }
            case TIME_TRACKING:
            {
                list1 = TabInitialization.initTimeTrackingTab();
                break;
            }
            case EMOTIONS:
            {
                list2 = TabInitialization.initEmotionsTab();
                break;
            }
            case SKILLS:
            {
                list3 = TabInitialization.initGenericSkills();
                break;
            }
            case GOALS:
            {
                list4 = TabInitialization.initGoals();
                break;
            }
            case MENU:
            {
                break;
            }
            default:
            {
                break;
            }
        }
    }

    public static void save(TabName tabName, ArrayList tabArray)
    {
        Gson gson = new Gson();

        String json = gson.toJson(tabArray);
        saveString("TabArray", tabName.toString(), json);
    }

    public static void preLoad(TabName tabName)
    {
        Gson gson = new Gson();

        String json = loadString("TabArray", tabName.toString());

        switch (tabName)
        {
            case MY_DAYS:
            {
                list0 = gson.fromJson(json, new TypeToken<List<RowDayModel>>(){}.getType());
                break;
            }
            case TIME_TRACKING:
            {
                list1 = gson.fromJson(json, new TypeToken<List<TimeTrackingDay<Event>>>(){}.getType());
                break;
            }
            case EMOTIONS:
            {
                list2 = gson.fromJson(json, new TypeToken<List<TimeTrackingDay<EmotionEvent>>>(){}.getType());
                break;
            }
            case SKILLS:
            {
                list3 = gson.fromJson(json, new TypeToken<List<GenericSkill>>(){}.getType());
                break;
            }
            case GOALS:
            {
                list4 = gson.fromJson(json, new TypeToken<List<Goal>>(){}.getType());
                break;
            }
            case MENU:
            {
                //list5 = gson.fromJson(json, new TypeToken<List<TimeTrackingDay>>(){}.getType());
                break;
            }
        }

        return;
    }

    public static ArrayList load(TabName tabName)
    {
        switch (tabName)
        {
            case MY_DAYS:
            {
                if(list0 == null)
                {
                    return TabInitialization.initMyDays();
                }
                else
                {
                    return list0;
                }
            }
            case TIME_TRACKING:
            {
                if(list1 == null)
                {
                    return TabInitialization.initTimeTrackingTab();
                }
                else
                {
                    return list1;
                }
            }
            case EMOTIONS:
            {
                if(list2 == null)
                {
                    return TabInitialization.initEmotionsTab();
                }
                else
                {
                    return list2;
                }
            }
            case SKILLS:
            {
                if(list3 == null)
                {
                    return TabInitialization.initGenericSkills();
                }
                else
                {
                    return list3;
                }
            }
            case GOALS:
            {
                if(list4 == null)
                {
                    return TabInitialization.initGoals();
                }
                else
                {
                    return list4;
                }
            }
            case MENU:
            {
                return null;
                //return list5;
            }
            default:
            {
                return null;
            }
        }
    }


    public static boolean isFirstLoad()
    {
        return loadBoolean(defaultSavingPath, "isFirstLoad");
    }

    public static void setIsFirstLoad()
    {
        saveBoolean(defaultSavingPath, "isFirstLoad", true);
    }

    private static void saveString(String fileName, String key, String value)
    {
        SharedPreferences sharedPreferences = getActualContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorSave = sharedPreferences.edit();

        editorSave.putString(key, value);
        editorSave.apply();
    }

    private static String loadString(String fileName, String key)
    {
        SharedPreferences sharedPreferences = getActualContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    private static void saveBoolean(String fileName, String key, boolean value)
    {
        SharedPreferences sharedPreferences = getActualContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorSave = sharedPreferences.edit();

        editorSave.putBoolean(key, value);
        editorSave.apply();
    }

    private static boolean loadBoolean(String fileName, String key)
    {
        SharedPreferences sharedPreferences = getActualContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    private static Context getActualContext()
    {
        if(MainActivity.GetContext() == null)
        {
            return SplashActivity.getContext();
        }
        else
        {
            return MainActivity.GetContext();
        }
    }
}
