package edu.dartmouth.cs.diningatdartmouth;
import java.text.NumberFormat;
import java.util.ArrayList;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.dds.R;

public class MoreTabFragment extends Fragment{
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.totals,container,false);
		drawChart(view);
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		return view;
	}
	
	private void drawChart(View view) {
		try
		{
			double maxValue = 0;
			double totalMoney = 0;
			
			SharedPreferences prefs = getActivity().getSharedPreferences("prefs",Context.MODE_PRIVATE);
			ArrayList<Float>tempList = new ArrayList<Float>();
			for(int i = 0; i < 24; i++)
			{
				if(i < 10)
				{
					tempList.add(prefs.getFloat("0"+i,(float)0.0));
				} 
				else
				{
					tempList.add(prefs.getFloat(i+"",(float)0.0));
				}
			}
			Float[] values = tempList.toArray(new Float[24]);
			
			CategorySeries series = new CategorySeries("When Do I Eat?");
			for (int i = 1; i < values.length; i++) {
				if(values[i-1] > 0)
				{
					series.add("Bar " + (i-1), values[i-1]);
				}
				else
				{
					series.add("Bar " + (i-1), -1);
				}
				totalMoney += values[i-1];
				if(values[i-1]>maxValue)
				{
					maxValue = values[i-1];
				}
			}
			XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
			dataset.addSeries(series.toXYSeries());
			
			// This is how the "Graph" itself will look like
			XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
//			mRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.VERTICAL);
			mRenderer.setXTitle("Hours of the day");
			mRenderer.setYTitle("Money spent");
			mRenderer.setLabelsColor(getResources().getColor(R.color.white));
			mRenderer.setMarginsColor(getResources().getColor(R.color.dark_green));
		    mRenderer.setXLabels(24);
		    mRenderer.setLabelsTextSize(25);
		    mRenderer.setYAxisMax(maxValue+(totalMoney*.1));
		    mRenderer.setYAxisMin(0);
		    mRenderer.setXAxisMax(24);
		    mRenderer.setXAxisMin(0);
		    mRenderer.setBarWidth(10);

			mRenderer.setZoomButtonsVisible(false);
			mRenderer.setClickEnabled(false);
			mRenderer.setPanEnabled(false);
		    // Customize bar 1
			XYSeriesRenderer renderer = new XYSeriesRenderer();
		    renderer.setDisplayChartValues(true);
		    renderer.setChartValuesSpacing((float)20);
			renderer.setChartValuesTextSize(25);
		    mRenderer.addSeriesRenderer(renderer);
		    mRenderer.setBarSpacing(50);
		    renderer.setShowLegendItem(false);
		    renderer.setColor(getResources().getColor(R.color.white));
		    
		    NumberFormat formatter = NumberFormat.getInstance();
		    formatter.setMaximumFractionDigits(2);
		    formatter.setMinimumIntegerDigits(1);
		    
		    mRenderer.setAxisTitleTextSize(30);
		    mRenderer.setChartTitleTextSize(70);
		    mRenderer.setYLabels(0);
		    mRenderer.setBarSpacing(5);
		    mRenderer.setChartTitle("When Do I Eat?");
			renderer.setChartValuesFormat(formatter);
	         
	        GraphicalView chartView;
	        chartView = ChartFactory.getBarChartView(getActivity(), dataset, mRenderer, Type.DEFAULT);
	        chartView.animate();
	        LinearLayout layout = (LinearLayout) view.findViewById(R.id.fragment_container42); 
	        layout.addView(chartView);
		}
		catch(Exception e)
		{
			Log.e("tag","error");
			Log.e("tag","error: "+e.toString());
		}
    }
}
