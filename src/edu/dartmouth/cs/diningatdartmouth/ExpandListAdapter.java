package edu.dartmouth.cs.diningatdartmouth;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.dds.R;

public class ExpandListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<ExpandListGroup> groups;
	public ExpandListAdapter(Context context, ArrayList<ExpandListGroup> groups) {
		this.context = context;
		this.groups = groups;
	}
	
	public void addItem(ExpandListChild item, ExpandListGroup group) {
		if (!groups.contains(group)) {
			groups.add(group);
		}
		int index = groups.indexOf(group);
		ArrayList<ExpandListChild> ch = groups.get(index).getItems();
		ch.add(item);
		groups.get(index).setItems(ch);
	}
	public Object getChild(int groupPosition, int childPosition) {
		ArrayList<ExpandListChild> chList = groups.get(groupPosition).getItems();
		return chList.get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view,
			ViewGroup parent) {
		ExpandListChild child = (ExpandListChild) getChild(groupPosition, childPosition);
		if (view == null) {
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			view = infalInflater.inflate(R.layout.expandlist_child_item, null);
		}
		TextView tv = (TextView) view.findViewById(R.id.tvChild);
		TextView tv2 = (TextView) view.findViewById(R.id.tvChild2);
		String names = child.getName().toString();
		tv.setText(names.substring(0,names.indexOf(":")));
		tv2.setText(names.substring(names.indexOf(":")+2,names.length()));
		return view;
	}

	public int getChildrenCount(int groupPosition) {
		ArrayList<ExpandListChild> chList = groups.get(groupPosition).getItems();

		return chList.size();

	}

	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	public int getGroupCount() {
		return groups.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isLastChild, View view,
			ViewGroup parent) {
		SharedPreferences prefs = context.getSharedPreferences("prefs",Context.MODE_PRIVATE);
		ExpandListGroup group = (ExpandListGroup) getGroup(groupPosition);
		
		LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		
		if(prefs.getInt("loc"+groupPosition,-1) == 1)
		{
			view = inf.inflate(R.layout.expandlist_group_item_enabled, null);
		}
		else
		{
			view = inf.inflate(R.layout.expandlist_group_item_disabled, null);
		}
		try
		{
			String[] names = group.getName().split("%");
			TextView tv = (TextView) view.findViewById(R.id.tvGroup);
			TextView tv2 = (TextView) view.findViewById(R.id.tvGroup2);
			tv.setText(names[0]);
			if(names.length > 1)
			{
				tv2.setText(names[1]);
			}
		}
		catch(Exception e)
		{
			Log.e("tag","gotcha");
			Log.e("tag",e.toString());
		}
		try
		{
			view.setPadding(20, 0, 0, 0);
		}
		catch(Exception e)
		{
			Log.e("tag",e.toString());
		}
		return view;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}
}


