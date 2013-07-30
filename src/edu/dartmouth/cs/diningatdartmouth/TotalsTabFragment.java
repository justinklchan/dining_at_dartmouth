package edu.dartmouth.cs.diningatdartmouth;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

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
import android.widget.LinearLayout;

import com.example.dds.R;

public class TotalsTabFragment extends Fragment {
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.totals,container,false);
		drawChart(view);
		return view;
	}
	
	private void drawChart(View view) {
//		MainActivity act = (MainActivity)getActivity();
//		act.getData();
        try 
        {
			Log.e("tag","drawing chart");
			SharedPreferences prefs = getActivity().getSharedPreferences("prefs",Context.MODE_PRIVATE);
			String[] names = {"53 Commons", "Collis Cafe", "Novack Cafe", "Courtyard Cafe", "King Arthur Flour Coffee Bar",
							  "EW Snack Bar"};
			String[] names2 = {"Foco", "Collis", "Novack", "The Hop", "KAF", "EW",
			  				   "EW Snack Bar"};

	        DecimalFormat df = new DecimalFormat("#.##");
			ArrayList<Double>tempValues = new ArrayList<Double>();
			for(String s : names)
			{
				tempValues.add(Double.parseDouble(df.format(prefs.getFloat(s,0))));
			}
			
			int[] colors = new int[] { getResources().getColor(R.color.c1),
									   getResources().getColor(R.color.c2),
									   getResources().getColor(R.color.c3),
									   getResources().getColor(R.color.c4),
									   getResources().getColor(R.color.c5),
									   getResources().getColor(R.color.c6)};
			
			DefaultRenderer renderer = new DefaultRenderer();
			CategorySeries series = new CategorySeries("Pie Graph");
			for (int i = 0; i < names.length; i++) {
				if(tempValues.get(i) > 0)
				{
					series.add(names2[i], tempValues.get(i));
					SimpleSeriesRenderer r = new SimpleSeriesRenderer();
					r.setColor(colors[i]);
					renderer.addSeriesRenderer(r);
				}
			}
			renderer.setShowLegend(true);
			renderer.setLabelsTextSize(30);
			renderer.setLegendTextSize(30);
			renderer.setFitLegend(true);
			renderer.setZoomButtonsVisible(false);
			renderer.setPanEnabled(false);
			renderer.setApplyBackgroundColor(true);
			renderer.setBackgroundColor(getResources().getColor(R.color.dark_green));
			renderer.setAntialiasing(true);
			renderer.setChartTitle("Where Do I Eat?");
			renderer.setLabelsColor(getResources().getColor(R.color.white));
			renderer.setChartTitleTextSize(70);
			renderer.setApplyBackgroundColor(true);
			renderer.setDisplayValues(true);
//			renderer.setShowLabels(true);
			renderer.setTextTypeface("sans_serif", Typeface.BOLD);
			NumberFormat formatter = NumberFormat.getInstance();
		    formatter.setMaximumFractionDigits(2);
		    formatter.setMinimumIntegerDigits(1);
	        
	        GraphicalView chartView;
	        chartView = ChartFactory.getPieChartView(getActivity(), series, renderer);
	        
	        LinearLayout layout = (LinearLayout) view.findViewById(R.id.fragment_container42); 
	        layout.addView(chartView);
        }
        catch(Exception e)
        {
        	Log.e("tag","error");
        	Log.e("tag",e.toString());
        }
    }
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}

	
}
