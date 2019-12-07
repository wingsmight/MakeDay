package com.wingsmight.makeday.Growth.Skills;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.kyleduo.blurpopupwindow.library.BlurPopupWindow;
import com.wingsmight.makeday.AnimatedExpandableListView.AnimatedExpandableListAdapter;
import com.wingsmight.makeday.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.CompoundButtonCompat;

public class SkillsTabAdapter extends AnimatedExpandableListAdapter
{
    Context context;
    List<GenericSkill> listGroup;
    //HashMap<int, List<Skill>> listItem;



    public SkillsTabAdapter(Context context, List<GenericSkill> listGroup)
    {
        this.context = context;
        this.listGroup = listGroup;
    }

    View dialogView;
    AlertDialog popupDialog;
    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.row_skill, null);
        }

        String skillName = ((Skill)getChild(groupPosition, childPosition)).getName();

        TextView textView = convertView.findViewById(R.id.skillName);
        textView.setText(skillName);

        boolean skillChecked = ((Skill)getChild(groupPosition, childPosition)).isChecked();
        CheckBox checkBox = convertView.findViewById(R.id.skillCheckBox);
        checkBox.setTag(new int[]{groupPosition, childPosition});
        checkBox.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CheckBox checkBox1 = v.findViewById(R.id.skillCheckBox);
                int[] pos = (int[]) checkBox1.getTag();
                ((Skill) getChild(pos[0], pos[1])).setChecked(((CheckBox) v).isChecked());
                notifyDataSetChanged();
            }
        });
        checkBox.setChecked(skillChecked);

        ImageView skillInfo = convertView.findViewById(R.id.skillInfo);
        skillInfo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Show popup
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.skill_info_dialog, null);
                builder.setView(dialogView);
                popupDialog = builder.create();
                popupDialog.show();

                ImageView sendButton = dialogView.findViewById(R.id.closeSkillInfo);
                sendButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        popupDialog.dismiss();
                    }
                });
            }
        });

        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition)
    {
        ArrayList<Skill> skills = listGroup.get(groupPosition).getSkills();
        if(skills == null)
        {
            return 0;
        }
        else
        {
            return skills.size();
        }
    }

    @Override
    public int getGroupCount()
    {
        if(listGroup == null)
        {
            return 0;
        }
        else
        {
            return listGroup.size();
        }
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return listGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return listGroup.get(groupPosition).getSkills().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.row_generic_skill, null);
        }

        GenericSkill currentGenericSkill = (GenericSkill)getGroup(groupPosition);

        //Set name
        String genericSkillName = currentGenericSkill.getName();
        TextView textView = convertView.findViewById(R.id.genericSkillName);
        textView.setText(genericSkillName);

        //Init checkboxes
        CheckBox checkBox = convertView.findViewById(R.id.genericSkillCheckBox);
        checkBox.setTag(groupPosition);
        checkBox.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int pos = (Integer) v.findViewById(R.id.genericSkillCheckBox).getTag();

                GenericSkill skill = (GenericSkill) getGroup(pos);
                if(skill.isChecked() == SkillCheckType.ALLCHECK)
                {
                    skill.setChecked(SkillCheckType.NOONECHECK);
                }
                else if(skill.isChecked() == SkillCheckType.NOONECHECK)
                {
                    skill.setChecked(SkillCheckType.ALLCHECK);
                }
                else
                {
                    skill.setChecked(SkillCheckType.ALLCHECK);
                }

                notifyDataSetChanged();
            }
        });

        //Set checkbox
        ArrayList<Skill> skills = currentGenericSkill.getSkills();
        boolean isAllChecked = true;
        boolean isNoneChecked = true;
        int groupCheckColor;
        if(skills != null && skills.size() != 0)
        {
            for (Skill skill : skills)
            {
                if(!skill.isChecked())
                {
                    isAllChecked = false;
                }
                else
                {
                    isNoneChecked = false;
                }
            }
        }

        if(isAllChecked)
        {
            currentGenericSkill.setChecked(SkillCheckType.ALLCHECK);
            groupCheckColor = context.getResources().getColor(R.color.colorAccent);
            setCheckBoxColor(checkBox, groupCheckColor);
            checkBox.setChecked(true);
        }
        else if(isNoneChecked)
        {
            currentGenericSkill.setChecked(SkillCheckType.NOONECHECK);
            groupCheckColor = Color.GRAY;
            setCheckBoxColor(checkBox, groupCheckColor);
            checkBox.setChecked(false);
        }
        else
        {
            currentGenericSkill.setChecked(SkillCheckType.SOMECHECK);
            groupCheckColor = Color.GRAY;
            setCheckBoxColor(checkBox, groupCheckColor);
            checkBox.setChecked(true);
        }


        return convertView;
    }

    private void setCheckBoxColor(CheckBox checkBox, int groupCheckColor)
    {
        Drawable drawable =  CompoundButtonCompat.getButtonDrawable(checkBox);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, groupCheckColor);
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }

    public void addGenericSkill(GenericSkill newGenericSkill)
    {
        if(listGroup == null)
        {
            listGroup = new ArrayList<>();
        }
        listGroup.add(newGenericSkill);
    }
}
