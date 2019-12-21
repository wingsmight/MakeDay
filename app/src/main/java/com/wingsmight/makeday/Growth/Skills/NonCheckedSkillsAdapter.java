package com.wingsmight.makeday.Growth.Skills;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.wingsmight.makeday.DragNDropExpandableListView.DropListener;
import com.wingsmight.makeday.DragNDropExpandableListView.RemoveListener;
import com.wingsmight.makeday.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.CompoundButtonCompat;
import androidx.fragment.app.Fragment;

public class NonCheckedSkillsAdapter extends BaseExpandableListAdapter implements RemoveListener, DropListener
{
    Context context;
    Fragment fragment;
    List<GenericSkill> listGroup;
    SkillsTabAdapter skillsTabAdapter;

    public NonCheckedSkillsAdapter(Context context, Fragment fragment, List<GenericSkill> listGroup)
    {
        this.context = context;
        this.fragment = fragment;
        this.listGroup = listGroup;
    }

    public void addSkillTabAdapter(SkillsTabAdapter skillsTabAdapter)
    {
        this.skillsTabAdapter = skillsTabAdapter;
    }

    public void addNonCheckedSkill(GenericSkill newSkill)
    {
        listGroup.add(0, newSkill);
        notifyDataSetChanged();
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
    public int getChildrenCount(int groupPosition)
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
    public Object getGroup(int groupPosition)
    {
        return listGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        ArrayList skills = listGroup.get(groupPosition).getSkills();
        if(skills == null) return null;
        if(skills.size() != 0 && childPosition < skills.size())
        {
            return skills.get(childPosition);
        }
        else
        {
            return null;
        }
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
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.row_generic_skill, null);
        }

        final GenericSkill currentGenericSkill = (GenericSkill)getGroup(groupPosition);

        if(currentGenericSkill == null)
        {
            return convertView;
        }

        //Set name
        String genericSkillName = currentGenericSkill.getName();

        if (genericSkillName.equals(""))
        {
            TextView textView = convertView.findViewById(R.id.genericSkillName);
            textView.setText("");
            CheckBox checkBox = convertView.findViewById(R.id.genericSkillCheckBox);
            setCheckBoxColor(checkBox, context.getResources().getColor(R.color.white));
            ImageView deleteGenericSkill = convertView.findViewById(R.id.deleteGenericSkill);
            deleteGenericSkill.setImageDrawable(null);
            ImageView dragHandle = convertView.findViewById(R.id.dragHandleSkill);
            dragHandle.setImageDrawable(null);

            return convertView;
        }

        TextView textView = convertView.findViewById(R.id.genericSkillName);
        textView.setText(genericSkillName);
        textView.setTextColor(context.getResources().getColor(android.R.color.tab_indicator_text));

        //Init checkboxes
        CheckBox checkBox = convertView.findViewById(R.id.genericSkillCheckBox);
        checkBox.setTag(groupPosition);
        checkBox.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                currentGenericSkill.setCheckType(SkillCheckType.ALLCHECK);
                skillsTabAdapter.addGenericSkill(currentGenericSkill);
                skillsTabAdapter.update();
                listGroup.remove(currentGenericSkill);
                update();

//                int pos = (Integer) v.findViewById(R.id.genericSkillCheckBox).getTag();
//
//                GenericSkill skill = (GenericSkill) getGroup(pos);
//                if(skill.getCheckType() == SkillCheckType.ALLCHECK)
//                {
//                    skill.setCheckType(SkillCheckType.NOONECHECK);
//                }
//                else if(skill.getCheckType() == SkillCheckType.NOONECHECK)
//                {
//                    skill.setCheckType(SkillCheckType.ALLCHECK);
//                }
//                else if(skill.getCheckType() == SkillCheckType.SOMECHECK)
//                {
//                    skill.setCheckType(SkillCheckType.ALLCHECK);
//                }
//
//                notifyDataSetChanged();
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

            if(isAllChecked)
            {
                currentGenericSkill.setCheckType(SkillCheckType.ALLCHECK);
                groupCheckColor = context.getResources().getColor(R.color.colorAccent);
                setCheckBoxColor(checkBox, groupCheckColor);
                checkBox.setChecked(true);
            }
            else if(isNoneChecked)
            {
                currentGenericSkill.setCheckType(SkillCheckType.NOONECHECK);
                groupCheckColor = Color.GRAY;
                setCheckBoxColor(checkBox, groupCheckColor);
                checkBox.setChecked(false);
            }
            else
            {
                currentGenericSkill.setCheckType(SkillCheckType.SOMECHECK);
                groupCheckColor = Color.GRAY;
                setCheckBoxColor(checkBox, groupCheckColor);
                checkBox.setChecked(true);
            }
        }
        else
        {
            SkillCheckType checkType = currentGenericSkill.getCheckType();
            if(checkType == SkillCheckType.ALLCHECK || checkType == SkillCheckType.SOMECHECK)
            {
                currentGenericSkill.setCheckType(SkillCheckType.ALLCHECK);
                groupCheckColor = context.getResources().getColor(R.color.colorAccent);
                setCheckBoxColor(checkBox, groupCheckColor);
                checkBox.setChecked(true);
            }
            else
            {
                currentGenericSkill.setCheckType(SkillCheckType.NOONECHECK);
                groupCheckColor = Color.GRAY;
                setCheckBoxColor(checkBox, groupCheckColor);
                checkBox.setChecked(false);
            }
        }

        ImageView deleteGenericSkill = convertView.findViewById(R.id.deleteGenericSkill);
        ImageView dragHandle = convertView.findViewById(R.id.dragHandleSkill);
        //deleteGenericSkill.setVisibility(View.GONE);
        dragHandle.setVisibility(View.GONE);

        Drawable deleteGenericSkillDrawable = context.getResources().getDrawable(R.drawable.ic_delete);
        deleteGenericSkillDrawable.mutate().setColorFilter(context.getResources().getColor(R.color.redDelete), PorterDuff.Mode.SRC_IN);
        deleteGenericSkill.setImageDrawable(deleteGenericSkillDrawable);

        deleteGenericSkill.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((SkillsTabFragment)fragment).removeNonChecked(groupPosition);
                onRemove(groupPosition);
            }
        });

        return convertView;
    }

    View dialogView;
    AlertDialog popupDialog;
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.row_skill, null);
        }

        View addSubSkillLayout = convertView.findViewById(R.id.addSubSkillLayout);
        addSubSkillLayout.setVisibility(View.GONE);

        Skill skill = (Skill)getChild(groupPosition, childPosition);
        if(skill == null) return convertView;

        String skillName = skill.getName();

        TextView textView = convertView.findViewById(R.id.skillName);
        textView.setText(skillName);
        textView.setTextColor(context.getResources().getColor(android.R.color.tab_indicator_text));

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

        ImageView deleteSkill = convertView.findViewById(R.id.deleteSkill);
        ImageView dragHandle = convertView.findViewById(R.id.dragHandleSkill);

        //deleteSkill.setVisibility(View.GONE);
        dragHandle.setVisibility(View.GONE);

        Drawable deleteGenericSkillDrawable = context.getResources().getDrawable(R.drawable.ic_delete);
        deleteGenericSkillDrawable.mutate().setColorFilter(context.getResources().getColor(R.color.redDelete), PorterDuff.Mode.SRC_IN);
        deleteSkill.setImageDrawable(deleteGenericSkillDrawable);

        deleteSkill.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((SkillsTabFragment)fragment).removeNonCheckedChild(groupPosition, childPosition);
                onRemoveChild(groupPosition, childPosition
                );
            }
        });

        return convertView;
    }

    private void setCheckBoxColor(CheckBox checkBox, int groupCheckColor)
    {
        checkBox.setButtonDrawable(R.drawable.abc_btn_check_material);

        Drawable drawable = CompoundButtonCompat.getButtonDrawable(checkBox);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, groupCheckColor);
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }

    @Override
    public boolean isEmpty()
    {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition)
    {

    }

    @Override
    public void onGroupCollapsed(int groupPosition)
    {

    }

    public void addGenericSkill(GenericSkill newGenericSkill)
    {
        if(listGroup == null)
        {
            listGroup = new ArrayList<>();
        }
        listGroup.add(newGenericSkill);
    }

    private int prevTo = -1;
    private int prevToChild = -1;
    private GenericSkill fromSkill;
    private Skill fromSkillChild;

    @Override
    public void onSwap(int from, int to, ExpandableListView listView, int prevPosition)
    {
        int[] fromTo = convertToNormalFromTo(from, to);
        from = fromTo[0];
        to = fromTo[1];

        if(prevTo != -1)
        {
            listGroup.remove(prevTo);
            prevTo = to;
        }
        else
        {
            fromSkill = listGroup.remove(from);
            prevTo = to;
        }
        listGroup.add(to,new GenericSkill("", SkillCheckType.NOONECHECK, null));

        notifyDataSetChanged();
    }
    @Override
    public void onSwapChild(int from, int to, ExpandableListView listView, int prevPosition)
    {
        int[] fromTo = convertToNormalFromToChild(from, to);
        from = fromTo[0];
        to = fromTo[1];
        int groupPos = fromTo[2];

        ArrayList<Skill> skills = listGroup.get(groupPos).getSkills();

        if(prevToChild != -1)
        {
            skills.remove(prevToChild);
            prevToChild = to;
        }
        else
        {
            fromSkillChild = skills.remove(from);
            prevToChild = to;
        }
        skills.add(to,new Skill("", false));

        notifyDataSetChanged();
    }

    @Override
    public void onStartDrag(int from, ExpandableListView listView)
    {
        prevTo = -1;
        prevToChild = -1;
    }

    @Override
    public void onDrop(int from, int to)
    {
        int[] fromTo = convertToNormalFromTo(from, to);
        from = fromTo[0];
        to = fromTo[1];

        listGroup.remove(to);
        GenericSkill temp = fromSkill;
        temp.setIsExpanded(false);

        listGroup.add(to,temp);
        listGroup.get(from).setIsExpanded(true);

        notifyDataSetChanged();
        fromSkill = null;
    }

    @Override
    public void onDropChild(int from, int to)
    {
        int[] fromTo = convertToNormalFromToChild(from, to);
        from = fromTo[0];
        to = fromTo[1];
        int groupPos = fromTo[2];

        ArrayList<Skill> skills = listGroup.get(groupPos).getSkills();
        skills.remove(to);
        Skill temp = fromSkillChild;

        skills.add(to,temp);

        notifyDataSetChanged();
        fromSkillChild = null;
    }

    @Override
    public void onRemove(int which)
    {
        if (which < 0 || which > listGroup.size()) return;
        listGroup.remove(which);
    }

    public void onRemoveChild(int groupPosition, int childPosition)
    {
        if (groupPosition < 0 || groupPosition > listGroup.size() || childPosition < 0 || childPosition > listGroup.get(groupPosition).getSkills().size()) return;

        listGroup.get(groupPosition).getSkills().remove(childPosition);
    }

    public void update()
    {
        notifyDataSetChanged();
    }

    private int[] convertToNormalFromTo(int from, int to)
    {
        for (int i = 0; i < listGroup.size() && i < to && i < from; i++)
        {
            GenericSkill genericSkill = listGroup.get(i);
            if(genericSkill.getIsExpanded())
            {
                ArrayList<Skill> skills = genericSkill.getSkills();
                if(skills == null) continue;

                if(skills.size() < to)
                {
                    to -= skills.size();
                }
                if(skills.size() < from)
                {
                    from -= skills.size();
                }
            }
        }

        return new int[]{from, to};
    }

    private int[] convertToNormalFromToChild(int from, int to)
    {
        int groupPos = 0;
        while (!listGroup.get(groupPos).getIsExpanded() || listGroup.get(groupPos).getSkills().size() == 0)
        {
            groupPos++;
        }

        from -= (groupPos + 1);
        to -= (groupPos + 1);

        ArrayList<Skill> skills = listGroup.get(groupPos).getSkills();
        if(to < 0)
        {
            to = 0;
        }
        else if(to >= skills.size())
        {
            to = skills.size() - 1;
        }

        return new int[]{from, to, groupPos};
    }
}
