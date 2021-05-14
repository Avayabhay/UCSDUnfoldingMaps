package module6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.geo.Location;
import parsing.ParseFeed;
import processing.core.PApplet;

/** An applet that shows airports (and routes)
 * on a world map.  
 * @author Abhay Sharma 
 *
 */
public class AirportMap extends PApplet {
	
	
	private CommonMarker lastSelected;  		//Addded by me
	private CommonMarker lastClicked;			//Addded by me
	Marker show;
	UnfoldingMap map;
	private List<Marker> airportList;
	List<Marker> routeList;
	
	public void setup() {
		// setting up PAppler
		size(800,600, OPENGL);
		
		// setting up map and default events
		map = new UnfoldingMap(this, 50, 50, 750, 550);
		MapUtils.createDefaultEventDispatcher(this, map);
		
		// get features from airport data
		List<PointFeature> features = ParseFeed.parseAirports(this, "airports.dat");
		
		// list for markers, hashmap for quicker access when matching with routes
		airportList = new ArrayList<Marker>();
		HashMap<Integer, Location> airports = new HashMap<Integer, Location>();
		
		// create markers from features
		for(PointFeature feature : features) {
			AirportMarker m = new AirportMarker(feature);
			System.out.println(feature.getProperties());
			m.setRadius(5);
			airportList.add(m);
			
			// put airport in hashmap with OpenFlights unique id for key
			airports.put(Integer.parseInt(feature.getId()), feature.getLocation());
		
		}
		
		
		// parse route data
		List<ShapeFeature> routes = ParseFeed.parseRoutes(this, "routes.dat");
		routeList = new ArrayList<Marker>();
		for(ShapeFeature route : routes) {
			
			// get source and destination airportIds
			int source = Integer.parseInt((String)route.getProperty("source"));
			int dest = Integer.parseInt((String)route.getProperty("destination"));
			
			// get locations for airports on route
			if(airports.containsKey(source) && airports.containsKey(dest)) {
				route.addLocation(airports.get(source));
				route.addLocation(airports.get(dest));
			}
			
			SimpleLinesMarker sl = new SimpleLinesMarker(route.getLocations(), route.getProperties());
		
			//System.out.println(sl.getProperty(3));
			
			//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
			routeList.add(sl);
			//map.addMarker(AirportMarker);  		//		edited by me
		}
		
		
		
		//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
		//map.addMarkers(routeList);
		
		map.addMarkers(airportList);
		
		
	}
	
	public void draw() {
		background(0);
		map.draw();
		
	}
	
	
	/* My Own Modification */
	public void mouseClicked() {
		if(lastClicked != null) {
			//unHIde all the Markers
			lastClicked = null;
			unhideAll();
			show = null;
		}
		else {
			showRoutesAroundAirport();
		}
	}
	
	private void unhideAll() {
		// TODO Auto-generated method stub
		for(Marker m: airportList) {
			m.setHidden(false);
			}
	}

	public  void showRoutesAroundAirport(){
		for(Marker m: airportList) {
			lastClicked = (CommonMarker)m;
			if(m.isInside(map, mouseX, mouseY)) {
				
				  for(Marker mar :routeList) {
					  
					  if(lastClicked.getProperty("country") != mar.getProperty("source")) {
						  show = mar;
						  map.addMarker(show);
					  }
					
				  }
				 
				hideAllAirports();
				m.setHidden(false);
			}
		}
	}

	private void hideAllAirports() {
		// TODO Auto-generated method stub
		for(Marker m :airportList) {
			m.setHidden(true);
		}
	}
	

}
