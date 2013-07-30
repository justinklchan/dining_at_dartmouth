package edu.dartmouth.cs.diningatdartmouth;

import java.text.ParseException;
import java.util.Calendar;

import com.example.dds.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;

// Ref: http://developer.android.com/reference/android/app/DialogFragment.html
// The difference from the sample code is that we use Items, not OK/Cancel buttons


// Handling all the customized dialog boxes in our project.
// Differentiated by dialog id.
public class MyDialogFragment extends DialogFragment {
	
	// Different dialog IDs
	public static final int DIALOG_ID_EMAIL = 0;
	public static final int DIALOG_ID_PASSWORD = 1;
	public static final int DIALOG_ID_FROM_DATE = 2;
	public static final int DIALOG_ID_TO_DATE = 3;
	public static final int DIALOG_ID_LOGIN_ERROR = 4;
	public static final int DIALOG_ID_NO_LOGIN_DETAILS = 5;
	public static final int DIALOG_ID_DELETE_LOGIN_DETAILS = 6;
	public static final int DIALOG_ID_NO_NETWORK = 7;

	private static final String DIALOG_ID_KEY = "dialog_id";

	public static MyDialogFragment newInstance(int dialog_id) {
		MyDialogFragment frag = new MyDialogFragment();
		Bundle args = new Bundle();
		args.putInt(DIALOG_ID_KEY, dialog_id);
		frag.setArguments(args);
		return frag;
	}

	//skeleton
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int dialog_id = getArguments().getInt(DIALOG_ID_KEY);
		
		final Activity parent = getActivity();

		// For initializing date/time related dialogs
		final Calendar now;
		now = Calendar.getInstance();
		
		int year, month, day;
//		Date minDate = new Date(now.get(Calendar.YEAR), 1, 1);
//		Date maxDate = new Date(now.get(Calendar.YEAR)-1, 12, 31);
		
		// For text input field
		final EditText textEntryView;
		Log.e("tag","dialogid"+dialog_id);
  		SharedPreferences prefs = getActivity().getSharedPreferences("prefs",Context.MODE_PRIVATE);
		// Setup dialog appearance and onClick Listeners
		switch (dialog_id) {
		case DIALOG_ID_EMAIL:
			Log.e("tag","email2");
			textEntryView = new EditText(parent);
			PreferenceActivity temp = (PreferenceActivity)parent;
			if(temp.getTempEmail().equals(""))
			{
				textEntryView.setText(prefs.getString("email", ""));
			}
			else
			{
				textEntryView.setText(temp.getTempEmail());
			}
			textEntryView.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS );
			return new AlertDialog.Builder(parent)
			.setView(textEntryView)
			.setTitle("Enter Email")
			.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // FIRE ZE MISSILES!
                	   try
                	   {
	                	   Log.e("tag","distance+");
	                	   Log.e("tag",textEntryView.getText().toString());
	                	   PreferenceActivity temp = (PreferenceActivity)parent;
	                	   temp.setTempEmail(textEntryView.getText().toString());
                	   }
                	   catch(Exception e)
                	   {
                		   Log.e("tag",e.toString());
                	   }
                   }
               })
               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                	   Log.e("tag","distance-");
                   }
               }).create();
		case DIALOG_ID_PASSWORD:
			Log.e("tag","password2");
			textEntryView = new EditText(parent);
			PreferenceActivity temp2 = (PreferenceActivity)parent;
			if(temp2.getTempPassword().equals(""))
			{
				textEntryView.setText(prefs.getString("password", ""));
			}
			else
			{
				textEntryView.setText(temp2.getTempPassword());
			}
			textEntryView.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
			textEntryView.setTransformationMethod(PasswordTransformationMethod.getInstance());
			return new AlertDialog.Builder(parent)
			.setView(textEntryView)
			.setTitle("Enter Password")
			.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // FIRE ZE MISSILES!
                	   Log.e("tag","distance+");
                	   Log.e("tag",textEntryView.getText().toString());
                	   PreferenceActivity temp = (PreferenceActivity)parent;
                	   temp.setTempPassword(textEntryView.getText().toString());
                   }
               })
               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                	   Log.e("tag","distance-");
                   }
               }).create();
		case DIALOG_ID_FROM_DATE:
			Log.e("tag","date2");
			PreferenceActivity temp3 = (PreferenceActivity)parent;
			String date = "";
			if(temp3.getTempFromDate().equals(""))
			{
				date = (prefs.getString("from_date", ""));
			}
			else
			{
				date = (temp3.getTempFromDate());
			}

			year = now.get(Calendar.YEAR);
			month = 0;
			day = 1;
			
			if(!date.equals(""))
			{
				String[] dates = date.split(":");
				year = Integer.parseInt(dates[0]);
				month = Integer.parseInt(dates[1])-1;
				day = Integer.parseInt(dates[2]);
			}
			DatePickerDialog picker = new DatePickerDialog(getActivity(), 
				   new DatePickerDialog.OnDateSetListener()
				   {
						public void onDateSet(DatePicker view, int year, int month, int day) {
					        // Do something with the date chosen by the user
							String dateString = year+":";
							if(month+1 < 10)
							{
								dateString += "0"+(month+1)+":";
							}
							dateString += day;
							Log.e("tag",dateString);
	                	   PreferenceActivity temp = (PreferenceActivity)parent;
	                	   try {
							temp.setTempFromDate(dateString);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					    }
				   }, year, month, day);
				return picker;
		case DIALOG_ID_TO_DATE:
			Log.e("tag","date2");
			PreferenceActivity temp4 = (PreferenceActivity)parent;
			date = "";
			if(temp4.getTempToDate().equals(""))
			{
				date = (prefs.getString("to_date", ""));
			}
			else
			{
				date = (temp4.getTempToDate());
			}
			year = now.get(Calendar.YEAR);
			month = now.get(Calendar.MONTH);
			day = now.get(Calendar.DAY_OF_MONTH);
			if(!date.equals(""))
			{
				String[] dates = date.split(":");
				year = Integer.parseInt(dates[0]);
				month = Integer.parseInt(dates[1])-1;
				day = Integer.parseInt(dates[2]);
			}
			return new DatePickerDialog(getActivity(), 
				   new DatePickerDialog.OnDateSetListener()
				   {
						public void onDateSet(DatePicker view, int year, int month, int day) {
					        // Do something with the date chosen by the user
							String dateString = year+":";
							if(month+1 < 10)
							{
								dateString += "0"+(month+1)+":";
							}
							dateString += day;
							Log.e("tag",dateString);
	                	   PreferenceActivity temp = (PreferenceActivity)parent;
	                	   try {
							temp.setTempToDate(dateString);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					    }
				   }, year, month, day);
		case DIALOG_ID_LOGIN_ERROR:
			return new AlertDialog.Builder(parent)
			.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // FIRE ZE MISSILES!
                }
            })
            .setNegativeButton("Visit ManageMyId", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // FIRE ZE MISSILES!
                	try
                	{
                	String url = "https://dartmouth.managemyid.com/student/login.php";
                	Intent i = new Intent(Intent.ACTION_VIEW);
                	i.setData(Uri.parse(url));
                	startActivity(i);
                	}
                	catch(Exception e)
                	{
                		Log.e("tag",e.toString());
                	}
                }
            })
			.setMessage("Invalid login")
			.setTitle("Login Error")
			.create();
			
		case DIALOG_ID_NO_LOGIN_DETAILS:
			return new AlertDialog.Builder(parent)
			.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            })
			.setMessage("Please Enter Details in Preference Screen")
			.setTitle("No Login Details")
			.create();
		case DIALOG_ID_DELETE_LOGIN_DETAILS:
			Log.e("tag","password2");
			return new AlertDialog.Builder(parent)
			.setTitle("Delete all login info")
			.setMessage("Are you sure?")
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // FIRE ZE MISSILES!
           			PreferenceActivity parentPref = (PreferenceActivity)parent;
                	   SharedPreferences.Editor prefs = getActivity().getSharedPreferences("prefs",Context.MODE_PRIVATE).edit();
	               		prefs.putString("email", "");
	               		prefs.putString("password", "");
	               		parentPref.deleteLoginInfo();
	               		prefs.commit();
                   }
               })
               .setNegativeButton("No", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                	   Log.e("tag","distance-");
                   }
               }).create();
		case DIALOG_ID_NO_NETWORK:
			return new AlertDialog.Builder(parent)
			.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // FIRE ZE MISSILES!
                }
            })
			.setMessage("No network connection")
			.setTitle("Network error")
			.create();
		default:
			return null;
		}
	}
}
