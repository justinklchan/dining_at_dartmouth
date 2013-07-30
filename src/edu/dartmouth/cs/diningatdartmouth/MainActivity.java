package edu.dartmouth.cs.diningatdartmouth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.dds.R;

public class MainActivity extends Activity 
implements FragmentManager.OnBackStackChangedListener{

	private static final String TAB_INDEX_KEY = "tab_index";
	ArrayList<ArrayList<String>> masterData;
	private Handler mHandler = new Handler();
	private boolean mShowingBack = false;
	private Fragment mHoursFragment;
	private Fragment mSwipesFragment;
	private Fragment mTotalsFragment;
	private Fragment mMoreFragment;
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		context = this;

		SharedPreferences.Editor prefEdit = getSharedPreferences("prefs",Context.MODE_PRIVATE).edit();
		SharedPreferences prefs = getSharedPreferences("prefs",Context.MODE_PRIVATE);
		Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:d");
		String d1 = cal.get(Calendar.YEAR)+":01:1";
        String d2 = cal.get(Calendar.YEAR)+":"+(1+cal.get(Calendar.MONTH))+":"+cal.get(Calendar.DAY_OF_MONTH)+" ";
        try
        {
	        if(prefs.getString("from_date","").equals(""))
	        {
	        	prefEdit.putString("from_date",format.format(format.parse(d1)));
	        }
	        if(prefs.getString("to_date","").equals(""))
	        {
	        	prefEdit.putString("to_date",format.format(format.parse(d2)));
	        }
        }
        catch(Exception e)
        {
        	Log.e("tag",e.toString());
        }
		prefEdit.commit();
		// ActionBar
		ActionBar actionbar = getActionBar();
		
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// create new tabs and and set up the titles of the tabs
		ActionBar.Tab mHoursTab = actionbar.newTab().setText(
				getString(R.string.ui_tab_hours_title));
		ActionBar.Tab mMenuTab = actionbar.newTab().setText(
				getString(R.string.ui_tab_menu_title));
		ActionBar.Tab mSwipesTab = actionbar.newTab().setText(
				getString(R.string.ui_tab_swipes_title));
		ActionBar.Tab mTotalsTab = actionbar.newTab().setText(
				getString(R.string.ui_tab_totals_title));
		ActionBar.Tab mMoreTab = actionbar.newTab().setText(
				getString(R.string.ui_tab_more_title));
		
		// create the fragments
		mHoursFragment = new HoursTabFragment();
		Fragment mMenuFragment = new MenuTabFragment();
		mSwipesFragment = new SwipesTabFragment();
		mTotalsFragment = new TotalsTabFragment();
		mMoreFragment = new MoreTabFragment();

		// bind the fragments to the tabs - set up tabListeners for each tab
		mHoursTab.setTabListener(new TabListener(mHoursFragment,
				getApplicationContext()));
		mMenuTab.setTabListener(new TabListener(mMenuFragment,
				getApplicationContext()));
		mSwipesTab.setTabListener(new TabListener(mSwipesFragment,
				getApplicationContext()));
		mTotalsTab.setTabListener(new TabListener(mTotalsFragment,
				getApplicationContext()));
		mMoreTab.setTabListener(new TabListener(mMoreFragment,
				getApplicationContext()));

		// add the tabs to the action bar
		actionbar.addTab(mHoursTab);
		actionbar.addTab(mMenuTab);
		actionbar.addTab(mSwipesTab);
		actionbar.addTab(mTotalsTab);
		actionbar.addTab(mMoreTab);

		// restore to navigation
		if (savedInstanceState != null) {
			actionbar.setSelectedNavigationItem(savedInstanceState.getInt(
					TAB_INDEX_KEY, 0));
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TAB_INDEX_KEY, getActionBar()
                .getSelectedNavigationIndex());
	}


class TabListener implements ActionBar.TabListener {
	public Fragment fragment;
	public Context context;

	public TabListener(Fragment fragment, Context context) {
		this.fragment = fragment;
		this.context = context;
	}

	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
	}

	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		arg1.replace(R.id.fragment_container, fragment);
	}

	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		arg1.remove(fragment);
	}
}
	//CALL THIS METHOD ON REFRESH
	public void getData() 
	{
//		Log.e("tag","get data");
		SharedPreferences.Editor prefs = getSharedPreferences("prefs",Context.MODE_PRIVATE).edit();
		
		HashMap<String,Float>placesData = new HashMap<String,Float>();
		HashMap<String,Float>timeData = new HashMap<String,Float>();
		
		for(int i = 0; i < 24; i++)
		{
			if(i < 10)
			{
				timeData.put("0"+i, (float)0);
			}
			else
			{
				timeData.put(i+"", (float)0);
			}
		}
//		Log.e("tag","hours");
		try
		{
			DataRetriever dat = new DataRetriever();
			masterData = dat.doInBackground(getSharedPreferences("prefs",Context.MODE_PRIVATE));
			
			if(masterData == null)
			{
//				Toast.makeText(getApplicationContext(),"login failed", Toast.LENGTH_SHORT).show();
				MyDialogFragment frag = MyDialogFragment.newInstance(MyDialogFragment.DIALOG_ID_LOGIN_ERROR);
		  		frag.show(getFragmentManager(),"hi");
				return;
			}
			String[] names = {"'53 Commons", "Collis Cafe", "Novack Cafe", "Courtyard Cafe", "King Arthur Flour Coffee Bar",
			  "EW Snack Bar","Topside"};
			for(String s : names)
			{
				prefs.putFloat(s,0);
			}
			
//			Log.e("tag","got data");
//			Log.e("tag","size: "+masterData.size());
			float totalMoney = 0;
			for(int i = 0; i < masterData.size(); i++)
			{
//				Log.e("tag","got");
				ArrayList<String>data = masterData.get(i);
				float money = Float.parseFloat(data.get(2).substring(1,data.get(2).length()));
//				for(String s : data)
//				{
//					Log.e("tag","DATUM: "+s);
//				}
				
				totalMoney += money;
//				Log.e("tag",data.get(1));
				if(!placesData.containsKey(data.get(1)))
				{
//					Log.e("tag",data.get(1)+"->"+money);
//					money = round(money,2);
					placesData.put(data.get(1), money);
				}
				else
				{
//					Log.e("tag",data.get(1)+"->"+money+":"+placesData.get(data.get(1)));
//					money = round(money,2);
					placesData.put(data.get(1),placesData.get(data.get(1))+money);
//					Log.e("tag",placesData.get(data.get(1))+"");
				}
				
				String hour = data.get(0).split(" ")[1].split(":")[0];
//				Log.e("tag",i+"@"+hour+":"+money+"");
				if(money > 0)
				{
					timeData.put(hour+"",timeData.get(hour)+money);
				}
			}
			prefs.putString("total", totalMoney+"");
			for(String key : placesData.keySet())
			{
//				Log.e("tag",key+"");
//				Log.e("tag",placesData.get(key)+"");
				prefs.putFloat(key,placesData.get(key));
			}
			for(String key : timeData.keySet())
			{
//				Log.e("tag",key+"");
//				Log.e("tag",timeData.get(key)+"");
				prefs.putFloat(key,(float)timeData.get(key));
			}
			
		}
		catch(Exception e)
		{
			Log.e("tag","error1");
			Log.e("tag",e.toString());
		}
		prefs.commit();
	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        // Add either a "photo" or "finish" button to the action bar, depending on which page
        // is currently selected.
        MenuItem item = menu.add(Menu.NONE, R.id.action_flip, Menu.NONE,
        				R.string.ui_tab_preference_title);
        item.setIcon(R.drawable.ic_action_info);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        
        MenuItem item2 = menu.add(Menu.NONE, R.id.refresh, Menu.NONE,
	                	 R.string.refresh);
        item2.setIcon(R.drawable.stat_notify_sync);
        item2.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) 
        {
            case android.R.id.home:
                // Navigate "up" the demo structure to the launchpad activity.
                // See http://developer.android.com/design/patterns/navigation.html for more.
                NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
                return true;

            case R.id.action_flip:
            	Intent intent = new Intent(this, PreferenceActivity.class);
  	    	  	startActivity(intent);
                return true;
            case R.id.refresh:
            	getAllData();
	            break;
        }

        return super.onOptionsItemSelected(item);
    }
	
	public void getAllData()
	{
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            private ProgressDialog pd;
            @Override
            protected void onPreExecute() {
            	int currentOrientation = getResources().getConfiguration().orientation; 
            	   if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            	   }
            	   else {
            	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            	   }
                     pd = new ProgressDialog(context);
                     pd.setTitle("Downloading Dining Data...");
                     pd.setMessage("Please wait.");
                     pd.setCancelable(false);
                     pd.setIndeterminate(true);
                     pd.show();
            }
            @Override
            protected Void doInBackground(Void... arg0) {
            		refresh();
                    return null;
             }
             @Override
             protected void onPostExecute(Void result) {
            	 try
            	 {
                	 mHoursFragment = new HoursTabFragment();
                	 mTotalsFragment = new TotalsTabFragment();
                	 mMoreFragment = new MoreTabFragment();
                	 mSwipesFragment = new SwipesTabFragment();
                	 getFragmentManager().beginTransaction().replace(R.id.fragment_container,mHoursFragment).commit();
                	 getFragmentManager().beginTransaction().replace(R.id.fragment_container,mTotalsFragment).commit(); 
                	 getFragmentManager().beginTransaction().replace(R.id.fragment_container,mMoreFragment).commit();  
                	 getFragmentManager().beginTransaction().replace(R.id.fragment_container,mSwipesFragment).commit();    
            	 }
            	 catch(Exception e)
            	 {
            		 Log.e("tag",e.toString());
            	 }
            	 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                 pd.dismiss();
             }
        };
        if(isNetworkAvailable())
        {
            task.execute((Void[])null);
        }
        else
        {
			   MyDialogFragment frag = MyDialogFragment.newInstance(MyDialogFragment.DIALOG_ID_NO_NETWORK);
			   frag.show(getFragmentManager(),"");
        }
	}
	
	@Override
	public void onBackStackChanged() {
		
	}
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	public void refresh()
	{
		try
		{
		SharedPreferences prefs = getSharedPreferences("prefs",Context.MODE_PRIVATE);
  		SharedPreferences.Editor editor = getSharedPreferences("prefs",Context.MODE_PRIVATE).edit();
  		try 
  		{
  			DataRetriever2 dat = new DataRetriever2();
			ArrayList<String> openCloseData = dat.doInBackground();
			
			for(int i = 25; i < openCloseData.size()+25; i++)
			{
//				Log.e("tag",openCloseData.get(i-25));
				editor.putString(i+"",openCloseData.get(i-25));
			} 
			editor.commit(); 
		} catch (Exception e) {
			Log.e("tag","ok");
			Log.e("tag",e.toString());
		}
	    
//		Log.e("tag","email "+prefs.getString("email",""));
//		Log.e("tag","password "+prefs.getString("password",""));
		   if(prefs.getString("email","").equals("") || prefs.getString("password","").equals(""))
		   {
			   MyDialogFragment frag = MyDialogFragment.newInstance(MyDialogFragment.DIALOG_ID_NO_LOGIN_DETAILS);
			   frag.show(getFragmentManager(),"");
		   }
		   else
		   {
			  getData();
		   }
//		   Log.e("tag","done");
		}
		catch(Exception e)
		{
			Log.e("tag","WHAT");
			Log.e("tag",e.toString());
		}
	}
}

