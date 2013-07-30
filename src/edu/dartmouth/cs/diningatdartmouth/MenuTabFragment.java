package edu.dartmouth.cs.diningatdartmouth;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.dds.R;

public class MenuTabFragment extends Fragment{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		try
		{
		View view = inflater.inflate(R.layout.menu,container,false);
		TextView t1 = (TextView) view.findViewById(R.id.textView2);
		t1.setMovementMethod(LinkMovementMethod.getInstance());
		t1 = (TextView) view.findViewById(R.id.textView3);
		t1.setMovementMethod(LinkMovementMethod.getInstance());
		t1 = (TextView) view.findViewById(R.id.textView4);
		t1.setMovementMethod(LinkMovementMethod.getInstance());
		t1 = (TextView) view.findViewById(R.id.textView5);
		t1.setMovementMethod(LinkMovementMethod.getInstance());
		
		return view;
		}
		catch(Exception e)
		{
			Log.e("tag",e.toString());
		}
		return null;
	}
}
