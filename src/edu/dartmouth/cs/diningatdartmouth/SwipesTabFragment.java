package edu.dartmouth.cs.diningatdartmouth;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dds.R;

public class SwipesTabFragment extends Fragment {
	private View view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }
      
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.swipes,container,false);
    	this.view = view;
        populate();
		return view;
	}
    
    public void populate()
    {
    	Log.e("tag","populate");
    	SharedPreferences prefs = getActivity().getSharedPreferences("prefs",Context.MODE_PRIVATE);
        TextView swipes = (TextView) view.findViewById(R.id.swipes);
        TextView dba = (TextView) view.findViewById(R.id.dba);
        swipes.setTypeface(null,Typeface.BOLD);
        dba.setTypeface(null,Typeface.BOLD);
        Log.e("tag","before");
        try
        {
        	try
        	{
        		swipes.setText(Integer.parseInt(prefs.getString("swipes", "0"))+"");
        	}
        	catch(NumberFormatException e)
        	{
        		swipes.setText("0");
        	}
        	try
        	{
        		double temp = Double.parseDouble(prefs.getString("dba", "0"));
        		dba.setText("$"+String.format("%.2f",temp));
        	}
        	catch(NumberFormatException e)
        	{
        		dba.setText("$0.00");
        	}
        }
        catch(Exception e)
        {
        	Log.e("tag","swipes");
        	Log.e("tag",e.toString());
        }
    }
}
