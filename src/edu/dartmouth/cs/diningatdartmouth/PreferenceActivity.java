package edu.dartmouth.cs.diningatdartmouth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dds.R;

public class PreferenceActivity extends ListActivity {
	public static final int LIST_ITEM_EMAIL = 0;
	public static final int LIST_ITEM_PASSWORD = 1;
	public static final int LIST_ITEM_FROM_DATE = 2;
	public static final int LIST_ITEM_TO_DATE = 3;
	private SimpleDateFormat date;
	private SimpleDateFormat date2;
	
	private String tempEmail = "";
	private String tempPassword = "";
	private String tempFromDate = "";
	private String tempToDate = "";
	private View view;
	private ArrayAdapter<String> adapter;
	private ArrayList<String>options = new ArrayList<String>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		try
		{
			super.onCreate(savedInstanceState);
//			Log.e("tag","ON CREATE");
			setContentView(R.layout.preferences);
			date = new SimpleDateFormat("yyyy:MM:dd");
			date2 = new SimpleDateFormat("MM/dd/yyyy");
			SharedPreferences prefs = getSharedPreferences("prefs",Context.MODE_PRIVATE);
			options.add("Email");
			options.add("Password");
			options.add("From date");
			options.add("To date");
	  		String temp = prefs.getString("email", "");
	  		if(options.get(0).equals("Email") && temp.length() > 0)
	  		{
	  			options.set(0,options.get(0) + "\n" + prefs.getString("email", ""));
	  		}
	  		if(!tempEmail.equals(""))
	  		{
//	  			Log.e("tag","look here1");
	  			options.set(0,options.get(0) + "\n" + tempEmail);
	  		}
	  		temp = prefs.getString("password", "");
	  		if(options.get(1).equals("Password") && temp.length() > 0)
	  		{
	  			options.set(1,options.get(1) + "\n");
	  			for(int i = 0; i < temp.length(); i++)
		  		{
	  				options.set(1,options.get(1) + "*");
		  		}
	  		};
	  		if(!tempPassword.equals(""))
	  		{
//	  			Log.e("tag","look here1");
	  			options.set(1,options.get(1) + "\n" + tempPassword);
	  		}
	  		try
	  		{
	  			options.set(2,options.get(2) + "\n" + date2.format(date.parse(prefs.getString("from_date", ""))));
	  		}
	  		catch(Exception e)
	  		{
//	  			options[2] = "From date";
	  		}
	  		try
	  		{
	  			options.set(3,options.get(3) + "\n" + date2.format(date.parse(prefs.getString("to_date", ""))));
	  		}
	  		catch(Exception e)
	  		{
//	  			options[3] = "To date";
	  		}
//	  		Log.e("tag","options");
//	  		Log.e("tag",options[0]);
//	  		Log.e("tag",options[1]);
//	  		Log.e("tag",options[2]);
//	  		Log.e("tag",options[3]);
	  		
			adapter = new ArrayAdapter<String>(this, R.layout.simple_list_view, options);
			getListView().setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
		catch(Exception e)
		{
			Log.e("tag",e.toString());
		}
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		Log.e("tag","RESUME");
	}
	
	@Override
  	protected void onListItemClick(ListView l, View v, int position, long id) {
  		super.onListItemClick(l, v, position, id);
  		view = v;
  		int dialogId=0;
  		// Figuring out what dialog to show based on the position clicked
//  		Log.e("tag","onlistitem");
//  		Log.e("tag","position"+position);
  		switch (position) {
  		case LIST_ITEM_EMAIL:
//  			Log.e("tag","email");
  			dialogId = MyDialogFragment.DIALOG_ID_EMAIL;
  			break;
  			
  		case LIST_ITEM_PASSWORD:
//  			Log.e("tag","password");
  			dialogId = MyDialogFragment.DIALOG_ID_PASSWORD;
  			break;
  			
  		case LIST_ITEM_FROM_DATE:
//  			Log.e("tag","from date");
  			dialogId = MyDialogFragment.DIALOG_ID_FROM_DATE;
  			break;
  			
  		case LIST_ITEM_TO_DATE:
  			Log.e("tag","to date");
  			dialogId = MyDialogFragment.DIALOG_ID_TO_DATE;
  			break;
  		}

  		displayDialog(dialogId);
  	}
	// Display dialog based on id. See MyRunsDialogFragment for details 
	  	public void displayDialog(int id) {
	  		Log.e("tag","id"+id);
	  		MyDialogFragment frag = MyDialogFragment.newInstance(id);
	  		frag.show(getFragmentManager(),"");
	  	}
	  	
	public void onSaveInputClicked(View v) {
		try
		{
		SharedPreferences.Editor prefs = getSharedPreferences("prefs",Context.MODE_PRIVATE).edit();
		if(!tempEmail.equals(""))
		{
			prefs.putString("email", tempEmail);
		}
		if(!tempPassword.equals(""))
		{
			prefs.putString("password", tempPassword);
		}
		if(!tempFromDate.equals(""))
		{
			Log.e("tag","commit "+tempFromDate);
			prefs.putString("from_date", tempFromDate);
		}
		if(!tempToDate.equals(""))
		{
			Log.e("tag","commit "+tempToDate);
			prefs.putString("to_date", tempToDate);
		}
		prefs.commit();
		
		
		
		finish();
		}
		catch(Exception e)
		{
			Log.e("tag",e.toString());
		}
	}
	
	public void onCancelClicked(View v) {
		finish();
	}
	
	public void onDeleteClicked(View v)
	{
		MyDialogFragment frag = MyDialogFragment.newInstance(MyDialogFragment.DIALOG_ID_DELETE_LOGIN_DETAILS);
  		frag.show(getFragmentManager(),"hi");
	}
	
	public void setTempEmail(String email) {
		this.tempEmail = email;
		if(!tempEmail.equals(""))
		{
			Log.e("tag","SET EMAIL");
			options.set(0, "Email\n"+tempEmail);
			adapter.notifyDataSetChanged();
		}
	}
	
	public void setTempPassword(String password) {
		this.tempPassword = password;
		String hiddenPass = "";
		if(!tempPassword.equals(""))
		{
			for(int i = 0; i < tempPassword.length(); i++)
			{
				hiddenPass += "*";
			}
		}
		options.set(1, "Password\n"+hiddenPass);
		adapter.notifyDataSetChanged();
	}

	public void setTempFromDate(String tempFromDate) throws ParseException {
		this.tempFromDate = tempFromDate;
		String s = date2.format(date.parse(tempFromDate));
		Log.e("tag","DATE" +s);
		options.set(2,"From date\n"+s);
		adapter.notifyDataSetChanged();
	}

	public void setTempToDate(String tempToDate) throws ParseException {
		this.tempToDate = tempToDate;
		String s = date2.format(date.parse(tempToDate));
		options.set(3, "To date\n"+s);
		adapter.notifyDataSetChanged();
	}
	public String getTempEmail() {
		return tempEmail;
	}
	public String getTempPassword() {
		return tempPassword;
	}
	public String getTempFromDate() {
		return tempFromDate;
	}
	public String getTempToDate() {
		return tempToDate;
	}
    public void deleteLoginInfo()
    {
    	tempEmail = "";
    	tempPassword = "";
    	options.set(0, "Email");
    	options.set(1, "Password");
		adapter.notifyDataSetChanged();
    }
}
