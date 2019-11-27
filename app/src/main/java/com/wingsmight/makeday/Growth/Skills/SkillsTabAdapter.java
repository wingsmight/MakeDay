package com.wingsmight.makeday.Growth.Skills;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.kyleduo.blurpopupwindow.library.BlurPopupWindow;
import com.wingsmight.makeday.AnimatedExpandableListView.AnimatedExpandableListAdapter;
import com.wingsmight.makeday.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
                switch(v.getId())
                {
                    case R.id.skillCheckBox:
                    {
                        CheckBox checkBox1 = v.findViewById(R.id.skillCheckBox);
                        int[] pos = (int[]) checkBox1.getTag();
                        ((Skill) getChild(pos[0], pos[1])).setChecked(((CheckBox) v).isChecked());
                        notifyDataSetChanged();
                        break;
                    }
                }
            }
        });
        checkBox.setChecked(skillChecked);

        ImageView skillInfo = convertView.findViewById(R.id.skillInfo);
        skillInfo.setOnClickListener(new View.OnClickListener()
        {
            BlurPopupWindow blurPopupWindow;
            @Override
            public void onClick(View view)
            {
                //Show blur popup
                blurPopupWindow = new BlurPopupWindow.Builder(view.getContext())
                        .setContentView(R.layout.skill_info_dialog)
                        .bindClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                blurPopupWindow.dismiss();
                            }
                        }, R.id.closeSkillInfo)
                        .setGravity(Gravity.CENTER_HORIZONTAL)
                        .setScaleRatio(0.2f)
                        .setBlurRadius(10)
                        .setTintColor(0x30000000)
                        .build();
                blurPopupWindow.show();
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
                switch(v.getId())
                {
                    case R.id.genericSkillCheckBox:
                    {
                        int pos = (Integer) v.findViewById(R.id.genericSkillCheckBox).getTag();
                        ((GenericSkill) getGroup(pos)).setChecked(((CheckBox) v).isChecked());
                        notifyDataSetChanged();
                        break;
                    }
                }
            }
        });

        //Set checkbox
        ArrayList<Skill> skills = currentGenericSkill.getSkills();
        boolean isAllChecked = true;
        if(skills != null && skills.size() != 0)
        {
            for (Skill skill : skills)
            {
                if(!skill.isChecked())
                {
                    isAllChecked = false;
                    break;
                }
            }

            if(isAllChecked)
            {
                currentGenericSkill.setChecked(true);
            }
        }


        checkBox.setChecked(isAllChecked);

        return convertView;
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
