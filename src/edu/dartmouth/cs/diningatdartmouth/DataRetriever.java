package edu.dartmouth.cs.diningatdartmouth;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class DataRetriever {

	protected ArrayList<ArrayList<String>> doInBackground(SharedPreferences prefs) {
	    HttpClient httpclient = new DefaultHttpClient();
		String bigLine = login(prefs,httpclient);
//		if(params[1] == null)
//		{
//			return null;
//		}
		if(bigLine.equals(""))
		{
			return null;
		}

		SharedPreferences.Editor editor = prefs.edit();
		
		ArrayList<ArrayList<String>>masterData = new ArrayList<ArrayList<String>>();
//		Log.e("tag","async");
	    // Create a new HttpClient and Post Header
	    HttpPost httppost2 = new HttpPost("https://dartmouth.managemyid.com/student/svc_history_view.php");

	    try {
//	        Log.e("tag",bigLine);
//	        Log.e("tag","done");
	        String searchStart = "<td colspan=\"2\" >";
	        String searchEnd = "</td>";
	        int indexStart = bigLine.indexOf(searchStart) + searchStart.length();
	        int indexEnd = bigLine.indexOf(searchEnd,indexStart);
//	        data.put("swipes", (double)Integer.parseInt(bigLine.substring(indexStart, indexEnd)));
	        editor.putString("swipes",bigLine.substring(indexStart, indexEnd));
	        
	        searchStart = "Dining DBA";
	        searchEnd = "</td>";
	        indexStart = bigLine.indexOf(searchStart) + searchStart.length();
	        searchStart = "<td>$";
	        indexStart = bigLine.indexOf(searchStart,indexStart) + searchStart.length(); 
	        indexEnd = bigLine.indexOf(searchEnd,indexStart);
	        editor.putString("dba",bigLine.substring(indexStart, indexEnd));
	        editor.commit();
	        Date date1 = new Date();
	        Date date2 = new Date();
	        SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:d");
	        SimpleDateFormat format2 = new SimpleDateFormat("MM/d/yyyy");
	        Calendar cal = Calendar.getInstance();
	        String d1 = cal.get(Calendar.YEAR)+":01:1";
	        String d2 = cal.get(Calendar.YEAR)+":"+(1+cal.get(Calendar.MONTH))+":"+cal.get(Calendar.DAY_OF_MONTH)+" ";
	        
//			Log.e("tag","from_date "+prefs.getString("from_date", ""));
//			Log.e("tag","to_date "+prefs.getString("to_date", ""));
			
			String[] fromString = prefs.getString("from_date", format.format(format.parse(d1))).split(":");
			String[] toString = prefs.getString("to_date", format.format(format.parse(d2))).split(":");
			
			
//			Log.e("tag","FROM "+format.format(format.parse(d1)));
//			Log.e("tag","TO "+format.format(format.parse(d2)));
			
	        // Add data
	        List<NameValuePair> nameValuePairs2 = new ArrayList<NameValuePair>(7);
	        
	        nameValuePairs2.add(new BasicNameValuePair("FromMonth", fromString[1]));
	        nameValuePairs2.add(new BasicNameValuePair("FromDay", fromString[2]));
	        nameValuePairs2.add(new BasicNameValuePair("FromYear", fromString[0]));
	        
	        nameValuePairs2.add(new BasicNameValuePair("ToMonth", toString[1]));
	        nameValuePairs2.add(new BasicNameValuePair("ToDay", toString[2]));
	        nameValuePairs2.add(new BasicNameValuePair("ToYear", toString[0]));
	        
	        nameValuePairs2.add(new BasicNameValuePair("plan", "S32"));
	        httppost2.setEntity(new UrlEncodedFormEntity(nameValuePairs2));

	        // Execute HTTP Post Request
	        HttpResponse response2 = httpclient.execute(httppost2);
	        String line2 = "";
	        StringBuilder contentBuilder2 = new StringBuilder();
	        BufferedReader rd2 = new BufferedReader(new InputStreamReader(response2.getEntity().getContent()));
	        while ((line2 = rd2.readLine()) != null) { 
	        	contentBuilder2.append(line2);
	        }
	        String bigLine2 = contentBuilder2.toString();
	        try
	        {
		        ArrayList<String>tempDataString = new ArrayList<String>();
		        String start = "<tr class";
		        String end = "</tr>";
		        int startIndex = bigLine2.indexOf(start);
		        int endIndex = bigLine2.indexOf(end,startIndex);
		        tempDataString.add(bigLine2.substring(startIndex,endIndex));
		        while((startIndex = bigLine2.indexOf(start,startIndex+1)) != -1)
		        {
		        	endIndex = bigLine2.indexOf(end,startIndex);
		        	tempDataString.add(bigLine2.substring(startIndex,endIndex));
		        }
		        for(String row : tempDataString)
		        {
		        	endIndex = 0;
		        	ArrayList<String>info = new ArrayList<String>();
		        	for(int i = 0; i < 5; i++)
		        	{
			        	startIndex = row.indexOf("<td",endIndex);
			        	endIndex = row.indexOf(">",startIndex);
			        	if(i != 2 && i != 3)
		        		{
			        		info.add(row.substring(endIndex+1,row.indexOf("</td>",endIndex)));
		        		}
		        	}
//		        	Log.e("tag	","info: "+info+"");
		        	masterData.add(info);
		        }
		        
		        Log.e("tag","done");
	        }
	        catch(Exception e)
	        {
		    	Log.e("tag","ERROR1");
	        	Log.e("tag","error: " + e.toString());
	        }
	    } 
	    catch(Exception e)
	    {
	    	Log.e("tag","ERROR");
	    	Log.e("tag",e.toString());
	    }
	    return masterData;
	}
	
	public String login(SharedPreferences prefs, HttpClient httpclient)
	{
//		Log.e("tag","login");
		try
		{
		    HttpPost httppost = new HttpPost("https://dartmouth.managemyid.com/student/login.php");
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//			Log.e("tag",prefs.getString("email",""));
//			Log.e("tag",prefs.getString("password",""));
	        nameValuePairs.add(new BasicNameValuePair("user", prefs.getString("email","")));
	        nameValuePairs.add(new BasicNameValuePair("pwd", prefs.getString("password","")));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        String line = "";
	        StringBuilder contentBuilder = new StringBuilder();
	        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	        while ((line = rd.readLine()) != null) { 
	        	contentBuilder.append(line); 
	        }
	        String bigLine = contentBuilder.toString();
//	        Log.e("tag",bigLine);
	        if(bigLine.indexOf("Invalid username or password") != -1 ||
	           bigLine.indexOf("Account is locked") != -1 ||
	           bigLine.indexOf("Patron not found") != -1 ||
	           bigLine.indexOf("Login failure (error ") != -1)
	        {
	        	Log.e("tag",bigLine.indexOf("Invalid username or password")+"");
	        	Log.e("tag",bigLine.indexOf("Account is locked")+"");
	        	Log.e("tag",bigLine.indexOf("Patron not found")+"");
	        	return "";
	        }
	        return bigLine;
		}
		catch(Exception e)
		{
			Log.e("tag","error");
			Log.e("tag",e.toString());
		}
		return "";
	}
}
