package edu.dartmouth.cs.diningatdartmouth;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.dds.R;

public class HoursTabFragment extends Fragment{
	private ArrayList<ArrayList<String>> masterData;
	private ExpandListAdapter ExpAdapter;
	private View view;
	
	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
	}
	
	public ArrayList<ExpandListGroup> SetStandardGroups() {
		Calendar cal = Calendar.getInstance();
    	ArrayList<ExpandListGroup> list = new ArrayList<ExpandListGroup>();
    	ArrayList<ExpandListChild> list2 = new ArrayList<ExpandListChild>();

		SharedPreferences prefs = getActivity().getSharedPreferences("prefs",Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = getActivity().getSharedPreferences("prefs",Context.MODE_PRIVATE).edit();
		
    	String[] array = {"X","Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
        ExpandListGroup gru1 = new ExpandListGroup(); //group for location
        gru1.setName("    KAF");
        gru1.setItems(new ArrayList<ExpandListChild>());
        ExpandListGroup gru2 = new ExpandListGroup(); //group for location
        gru2.setName("    The Hop");
        gru2.setItems(new ArrayList<ExpandListChild>());
        ExpandListGroup gru3 = new ExpandListGroup(); //group for location
        gru3.setName("    Foco");
        gru3.setItems(new ArrayList<ExpandListChild>());
        ExpandListGroup gru4 = new ExpandListGroup(); //group for location
        gru4.setName("    Collis");
        gru4.setItems(new ArrayList<ExpandListChild>());
        ExpandListGroup gru5 = new ExpandListGroup(); //group for location
        gru5.setName("    Novack");
        gru5.setItems(new ArrayList<ExpandListChild>());
        ExpandListGroup gru6 = new ExpandListGroup(); //group for location
        gru6.setName("    EW Snack Bar");
        gru6.setItems(new ArrayList<ExpandListChild>());
        ExpandListGroup gru7 = new ExpandListGroup(); //group for location
        gru7.setName("    Topside");
        gru7.setItems(new ArrayList<ExpandListChild>());
        list.add(gru1);
        list.add(gru2);
        list.add(gru3);
        list.add(gru4);
        list.add(gru5);
        list.add(gru6);
        list.add(gru7);
        
        int counter = 25;
    	while(true)
    	{
    		String pref = prefs.getString(counter+"","");
//    		Log.e("tag",counter+" "+pref);
    		if(pref.equals(""))
    		{
    			break;
    		}
    		counter++; 
    		String[] text = pref.split(";");
    		String finalRep = "";
    		for(ExpandListGroup group : list)
    		{
    			String groupName = group.getName().trim();
//				Log.e("tag",groupName + ":" + text[0]);
    			if(text[0].equals(groupName))
    			{
    				finalRep += array[Integer.parseInt(text[1])] + ": ";
    				for(int i = 2; i < text.length; i++)
    				{
    					text[i]=text[i].replace(" to ", "-");
    					text[i]=text[i].replace(" - ","-");
    					text[i]=text[i].replace(" a", "a");
    					text[i]=text[i].replace(" p", "p");
    					finalRep += text[i] + "\n";
    				}
    				finalRep = finalRep.trim();
    				if(finalRep.charAt(finalRep.length()-1) == ',')
    				{
    					finalRep = finalRep.substring(0,finalRep.length()-1);
    				}
//    				Log.e("tag",finalRep);
        			ExpandListChild ch1_1 = new ExpandListChild();
            		ch1_1.setName(finalRep);
            		ch1_1.setTag(null);
            		group.getItems().add(ch1_1);
    				break;
    			}
    		}
    	}
        
    	if(gru1.getItems().size() == 0)
    	{
    		for(int i = 0; i < 5; i++)
    		{
		        ExpandListChild ch1_1 = new ExpandListChild();  //group for time/date of day
		        ch1_1.setName(array[i+1]+": 8am-1am");
		        ch1_1.setTag(null);
		        list2.add(ch1_1);
    		}
    		for(int i = 5; i < 7; i++)
    		{
		        ExpandListChild ch1_1 = new ExpandListChild();  //group for time/date of day
		        ch1_1.setName(array[i+1]+": 10am-1am");
		        ch1_1.setTag(null);
		        list2.add(ch1_1);
    		}
	        gru1.setItems(list2);
	        list2 = new ArrayList<ExpandListChild>();
        }
    	
    	if(gru2.getItems().size() == 0)
    	{
    		for(int i = 0; i < 7; i++)
    		{
		        ExpandListChild ch2_1 = new ExpandListChild();
//		        ch2_1.setName(array[i+1]+": 11am-12:30am");
		        ch2_1.setName(array[i+1]+": Closed");
		        ch2_1.setTag(null);
		        list2.add(ch2_1);
    		}
	        gru2.setItems(list2);
	        list2 = new ArrayList<ExpandListChild>();
    	}
    	
    	if(gru3.getItems().size() == 0)
    	{
    		for(int i = 0; i < 7; i++)
    		{
    			ExpandListChild ch3_1 = new ExpandListChild();
//    	        ch3_1.setName(array[i+1]+": 7:30am-3pm\n5pm-8:30am");
		        ch3_1.setName(array[i+1]+": Closed");
    	        ch3_1.setTag(null);
    	        list2.add(ch3_1);
    		}
	        gru3.setItems(list2);
	        list2 = new ArrayList<ExpandListChild>();
    	}
    	
    	if(gru4.getItems().size() == 0)
    	{
    		for(int i = 0; i < 7; i++)
    		{
		        ExpandListChild ch4_1 = new ExpandListChild();
//		        ch4_1.setName(array[i+1]+": 7am-8pm\n9:30pm-1:30am");
		        ch4_1.setName(array[i+1]+": Closed");
		        ch4_1.setTag(null);
		        list2.add(ch4_1);
    		}
	        gru4.setItems(list2);
	        list2 = new ArrayList<ExpandListChild>();
		}
    	
    	if(gru5.getItems().size() == 0)
    	{
    		for(int i = 0; i < 7; i++)
    		{
		        ExpandListChild ch5_1 = new ExpandListChild();
//		        ch5_1.setName(array[i+1]+": 7:30am-2am");
		        ch5_1.setName(array[i+1]+": Closed");
		        ch5_1.setTag(null);
		        list2.add(ch5_1);
    		}
	        gru5.setItems(list2);
	        list2 = new ArrayList<ExpandListChild>();
    	}
    	
    	if(gru6.getItems().size() == 0)
    	{
    		for(int i = 0; i < 7; i++)
    		{
		        ExpandListChild ch6_1 = new ExpandListChild();
		        ch6_1.setName(array[i+1]+": 8am-11:00am\n8pm-2am");
		        ch6_1.setTag(null);
		        list2.add(ch6_1);
    		}
	        gru6.setItems(list2);
	        list2 = new ArrayList<ExpandListChild>();
    	}
    	
    	if(gru7.getItems().size() == 0)
    	{
    		for(int i = 0; i < 7; i++)
    		{
		        ExpandListChild ch7_1 = new ExpandListChild();
//		        ch7_1.setName(array[i+1]+": 11am-11:30pm");
		        ch7_1.setName(array[i+1]+": Closed");
		        ch7_1.setTag(null);
		        list2.add(ch7_1);
    		}
	        gru7.setItems(list2);
	        list2 = new ArrayList<ExpandListChild>();
    	}

		DateFormat parser0 = new SimpleDateFormat("E");
		DateFormat parser1 = new SimpleDateFormat("h:mma");
		DateFormat parser2 = new SimpleDateFormat("ha");
		DateFormat parser3 = new SimpleDateFormat("yyyy/MM/dd hh:mma");
		DateFormat parser4 = new SimpleDateFormat("yyyy/MM/dd hha");
		DateFormat parser5 = new SimpleDateFormat("yyyy/MM/dd h:mma");
		String d1,d2;
		if(cal.get(Calendar.HOUR_OF_DAY) >= 0 && cal.get(Calendar.HOUR_OF_DAY) <= 3)
		{
			d1 = cal.get(Calendar.YEAR)+"/"+(1+cal.get(Calendar.MONTH))+"/"+(1-cal.get(Calendar.DAY_OF_MONTH))+" ";
			d2 = cal.get(Calendar.YEAR)+"/"+(1+cal.get(Calendar.MONTH))+"/"+(cal.get(Calendar.DAY_OF_MONTH))+" ";
		}
		else
		{
			d1 = cal.get(Calendar.YEAR)+"/"+(1+cal.get(Calendar.MONTH))+"/"+cal.get(Calendar.DAY_OF_MONTH)+" ";
			d2 = cal.get(Calendar.YEAR)+"/"+(1+cal.get(Calendar.MONTH))+"/"+(1+cal.get(Calendar.DAY_OF_MONTH))+" ";
		}
		Date todayDate = new Date();
		int childNumber = 0;
    	for(ExpandListGroup group : list)
        {
        	ArrayList<ExpandListChild> children = group.getItems();
        	for(ExpandListChild text : children)
        	{
        		String day = text.getName().substring(0,3);
        		if(day.equals(parser0.format(todayDate)))
        		{
        			String[] timePairs = new String[1];
        			timePairs = text.getName().substring(5,text.getName().length()).split("\n");
        			if(timePairs.length == 0)
        			{
        				timePairs[0] = text.getName().substring(5,text.getName().length());
        			}
        			if(timePairs[0].equals("Closed"))
        			{
//    					Log.e("tag","put "+group.getName()+" "+childNumber+" 0");
        				editor.putInt("loc"+childNumber,0);
        				continue;
        			}
        			for(int i = 0; i < timePairs.length; i++)
        			{  
        				try 
        				{ 
        					String start = timePairs[i].substring(0,timePairs[i].indexOf("-"));
        					String end = timePairs[i].substring(timePairs[i].indexOf("-")+1,timePairs[i].length());
        					
							Date openingDate;
	        				Date closingDate;
	        				try
	        				{
	        					if((start.contains("am")||start.contains("AM")) &&
	        					   (start.startsWith("12:") || start.startsWith("1:") || start.startsWith("2:")))
	        					{
		        					openingDate = parser3.parse(d2+start);
	        					}
	        					else
	        					{
	        						openingDate = parser3.parse(d1+start);
	        					}
	        				}
	        				catch(Exception e)
	        				{
	        					if(start.startsWith("12a") || start.startsWith("1a") || start.startsWith("2a"))
	        					{
		        					openingDate = parser4.parse(d2+start);
	        					}
	        					else
	        					{
	        						openingDate = parser4.parse(d1+start);
	        					}
	        				}
	        				try
	        				{
	        					if((end.contains("am")||end.contains("AM")) &&
        						   (end.startsWith("12:") || end.startsWith("1:") || end.startsWith("2:")))
	        					{
		        					closingDate = parser3.parse(d2+end);
	        					}
	        					else
	        					{
	        						closingDate = parser3.parse(d1+end);
	        					}
	        				}
	        				catch(Exception e)
	        				{
	        					if(end.startsWith("12a") || end.startsWith("1a") || end.startsWith("2a"))
	        					{
		        					closingDate = parser4.parse(d2+end);
	        					}
	        					else
	        					{
	        						closingDate = parser4.parse(d1+end);
	        					}
	        				}
	        				 
	        				if(todayDate.after(openingDate) && todayDate.before(closingDate))
	        				{
	        					group.setName(group.getName()+"%until "+parser1.format(closingDate));
	        					editor.putInt("loc"+childNumber,1);
//	        					Log.e("tag","put "+group.getName()+" "+childNumber+" 1");
//	        					Log.e("tag","opening: "+parser3.format(openingDate));
//	        					Log.e("tag","closing: "+parser3.format(closingDate));
	        					break;
	        				} 
	        				else
	        				{
	        					editor.putInt("loc"+childNumber,0);
//	        					Log.e("tag","put "+group.getName()+" "+childNumber+" 0");
//	        					Log.e("tag","opening: "+parser3.format(openingDate));
//	        					Log.e("tag","closing: "+parser3.format(closingDate));
	        				}
						} catch (Exception e) {
//        					Log.e("tag","exception 3");
							Log.e("tag","ERROR "+e.toString());
						}
        			}
            		break;
        		}
        	}
        	childNumber += 1;
        }
    	editor.commit();
        return list;
    }
	
	public void update()
	{
		try
		{
			ArrayList<ExpandListGroup> ExpListItems = SetStandardGroups();
			ExpAdapter = new ExpandListAdapter(getActivity(), ExpListItems);
			ExpAdapter.notifyDataSetChanged();
			ExpAdapter.notifyDataSetInvalidated();
		}
		catch(Exception e)
		{
			Log.e("tag","eek");
			Log.e("tag",e.toString());
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) { 
		view = inflater.inflate(R.layout.hours,container,false);
		ExpandableListView ExpandList = (ExpandableListView) view.findViewById(R.id.ExpList);
		ArrayList<ExpandListGroup> ExpListItems = SetStandardGroups();
		ExpAdapter = new ExpandListAdapter(getActivity(), ExpListItems);
        ExpandList.setAdapter(ExpAdapter);
			
		return view;
	}
}
