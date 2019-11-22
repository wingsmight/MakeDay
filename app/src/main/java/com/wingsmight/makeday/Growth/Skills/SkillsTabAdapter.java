package com.wingsmight.makeday.Growth.Skills;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wingsmight.makeday.AnimatedExpandableListView.AnimatedExpandableListAdapter;
import com.wingsmight.makeday.R;

import java.util.HashMap;
import java.util.List;

public class SkillsTabAdapter extends AnimatedExpandableListAdapter
{
    Context context;
    List<GenericSkill> listGroup;
    HashMap<GenericSkill, List<Skill>> listItem;



    public SkillsTabAdapter(Context context, List<GenericSkill> listGroup, HashMap<GenericSkill, List<Skill>> listItem)
    {
        this.context = context;
        this.listGroup = listGroup;
        this.listItem = listItem;
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

        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition)
    {
        return listItem.get(listGroup.get(groupPosition)).size();
    }

    @Override
    public int getGroupCount()
    {
        return listGroup.size();
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return listGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return listItem.get(listGroup.get(groupPosition)).get(childPosition);
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

        String group = ((GenericSkill)getGroup(groupPosition)).getName();
        TextView textView = convertView.findViewById(R.id.genericSkillName);
        textView.setText(group);

        /////////////////////
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
        checkBox.setChecked(((GenericSkill) getGroup(groupPosition)).isChecked());
        ////////////////////

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }
}
