package edu.dartmouth.cs.diningatdartmouth;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class DataRetriever2 {

	protected ArrayList<String> doInBackground()
	{
		HashMap<String,String>map = new HashMap<String,String>();
		map.put("'53 Commons", "Foco");
		map.put("Courtyard Cafe", "The Hop");
		map.put("Collis Cafe", "Collis");
		map.put("Novack Cafe", "Novack");
		map.put("EW Snack Bar", "EW Snack Bar");
		map.put("Topside", "Topside");
		map.put("King Arthur Flour", "KAF");
		
		ArrayList<String> masterData = new ArrayList<String>();
		try
		{
			String rep = "";
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://www.dartmouth.edu/dining/menus/weekly.html");
			HttpResponse response = httpclient.execute(httppost);
			 String line = "";
		     StringBuilder contentBuilder = new StringBuilder();
		     BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		     while ((line = rd.readLine()) != null) { 
		     	contentBuilder.append(line);
		     }
		     String bigLine = contentBuilder.toString();
		     int i1 = bigLine.indexOf("<table id=\"fullwidth\"");
		     int i2 = bigLine.indexOf("</table>",i1);
		     String table = bigLine.substring(i1,i2);
		     int start = 0;
		     int start2 = 0;
		     int dayCounter = 1;
		     while(true)
		     { 
			     int i3 = table.indexOf("<td>",start);
			     int temp = table.indexOf("<td class=\"today\"",start);
			     if(Math.abs(start-temp) < Math.abs(start-i3))
			     {
			    	 i3 = temp;
			     }
			     if(i3 == -1)
			     {
			    	 break;
			     }
			     int i4 = table.indexOf("</td>",i3);
			     String currentData = table.substring(i3,i4);
			     start2 = 0; 
			     while(true)
			     {
				     int i5 = currentData.indexOf("<h2><",start2);
				     if(i5 == -1)
				     {
				    	 break;
				     }
				     int i6 = currentData.indexOf("</h2>",i5);
				     String currentHeader = currentData.substring(i5,i6);
				     int i7 = currentHeader.indexOf("<a href");
				     int i8 = currentHeader.indexOf(">",i7);
				     int i9 = currentHeader.indexOf("</a>",i8);

				     String location = currentHeader.substring(i8+1,i9);
				     location = map.get(location);
				     
				     rep += location + ";" + dayCounter + ";";
				     int i10 = currentData.indexOf("<h2><a href",i6);
				     String currentTimes;
				     if (i10 == -1)
				     {
				    	 currentTimes = currentData.substring(i6,currentData.indexOf("</address>",i6));
				     }
				     else
				     {
				    	 currentTimes = currentData.substring(i6,i10);
				     } 
				     rep += patternMatcher("[0-9]{1,}:[0-9]{2} [ap]m (-|to) [0-9]{1,}:[0-9]{2} [ap]m",currentTimes);
				     start2 = i6;
				     masterData.add(rep);
				     rep = "";
			     }
			     start = i4;
			     dayCounter += 1;
		     }
		}
		catch(Exception e)
		{
			Log.e("tag","BOO");
			Log.e("tag",e.toString());
		}
		getTopsideData(masterData);
//		for(String s : masterData)
//		{
//			Log.e("tag",s);
//		}
		return masterData;
	}
	
	public void getTopsideData(ArrayList<String> masterData)
	{
		try
		{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://www.dartmouth.edu/dining/locations/");
			HttpResponse response = httpclient.execute(httppost);
			 String line = "";
		     StringBuilder contentBuilder = new StringBuilder();
		     BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		     while ((line = rd.readLine()) != null) { 
		     	contentBuilder.append(line);
		     }
		     String bigLine = contentBuilder.toString();
		     int i1 = bigLine.indexOf("<h2>Collis Market");
		     int i2 = bigLine.indexOf("</tr>",i1);
		     String row = bigLine.substring(i1,i2);
//		     Log.e("tag",row);
		     int start = 0;
		     int counter = 0;
		     
		     while(true)
		     {
			     int i3 = row.indexOf("<td",start);
			     if(i3 == -1)
			     {
			    	 break;
			     }
			     int i4 = row.indexOf("<h3>",i3)+4;
			     int i5 = row.indexOf("</h3>",i4);
			     String time = row.substring(i4,i5);
			     
			     start = i5+5;
			     switch(counter)
			     {
			     	case 0:
			     		masterData.add("Topside"+";"+"7;"+time);
//			     		Log.e("tag","Topside"+";"+"7;"+time);
			     		break;
			     	case 1:
			     		masterData.add("Topside"+";"+"1;"+time);
			     		masterData.add("Topside"+";"+"2;"+time);
			     		masterData.add("Topside"+";"+"3;"+time);
			     		masterData.add("Topside"+";"+"4;"+time);
//			     		Log.e("tag","Topside"+";"+"1;"+time);
//			     		Log.e("tag","Topside"+";"+"2;"+time);
//			     		Log.e("tag","Topside"+";"+"3;"+time);
//			     		Log.e("tag","Topside"+";"+"4;"+time);
			     		break;
			     	case 2:
			     		masterData.add("Topside"+";"+"5;"+time);
//			     		Log.e("tag","Topside"+";"+"5;"+time);
			     		break;
			     	case 3:
			     		masterData.add("Topside"+";"+"6;"+time);
//			     		Log.e("tag","Topside"+";"+"6;"+time);
			     		break;
			     }
			     counter += 1;
		     }
		}
		catch(Exception e)
		{
			Log.e("tag","topside");
			Log.e("tag",e.toString());
		}
	}
	
	public String patternMatcher(String regex, String source)
	{
		Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
//        Log.e("tag","in matcher");
        String rep = "";
        while (matcher.find()) 
        {
        	String match = matcher.group();
//        	Log.e("tag",match);
        	rep += match+";";
        }
        return rep;
	}
}
