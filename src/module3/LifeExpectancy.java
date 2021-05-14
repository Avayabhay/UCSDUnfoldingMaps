package module3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;

/**
 * @author abhay
 *
 */
public class LifeExpectancy extends PApplet{
	private UnfoldingMap map;
	private Map<String, Float> lifeExpectancy;
	private List<Feature> countries = new ArrayList<Feature>();
	private List<Marker> countryMarkers = new ArrayList<Marker>();
	
	public void setup() {
		
		 Map<String,Float> LE = new HashMap<String,Float>() ;
		
		size(900,700,OPENGL);
		map = new UnfoldingMap(this, 200,50,650,550, new Google.GoogleMapProvider());
		LE = lifeExpectancyFromCSVFile("LifeExpectancyWorldBankModule3.csv");
		countries = GeoJSONReader.loadData(this,"countries.geo.json");
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		System.out.println(LE);
		
	}
	
	public void draw() {
		map.draw();
		
	}
	
	
	
	private Map<String, Float> lifeExpectancyFromCSVFile(String fileName) {
		lifeExpectancy = new HashMap<String, Float>();

		String rows[] = loadStrings(fileName);
		System.out.println(rows);
		//System.out.println(rows);
		for (String row : rows) {
			String coloumns[] = row.split(",");
			//System.out.println(coloumns[3]+"   "+coloumns[5]);
			float value;
			try {	
				value = Float.parseFloat(coloumns[5]);
			}
			catch(Exception e){
				value =0;
			}
			lifeExpectancy.put(coloumns[3], value);

		}

		return lifeExpectancy;
	}
	 
	
}
