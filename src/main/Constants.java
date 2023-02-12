package main;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.swing.JFrame;
//import view.AlphabetPerspective;
//import view.MenuPerspective;
//import view.PetriNetsPerspective;
//import view.PlannerPerspective;
//import view.TracePerspective;

import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.PetrinetNode;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;

public class Constants {

	/**
	 * Vector that records the alphabet of activities that appear in the log traces.
	 */
	private static Vector<String> log_activities_repository_vector = new Vector<String>();	

	/**
	 * Vector that records all the traces (represented as java objects "Trace") of the log.	
	 */
	private static Vector<Trace> all_traces_vector = new Vector<Trace>();	
	
	/**
	 * Vector that records all the transitions (represented as java objects "PetriNetTransition") of the log.	
	 */
	private static Vector<PetrinetTransition> all_transitions_vector = new Vector<PetrinetTransition>();
	
	/**
	 * Vector that records all the places (represented as java Strings) of the log.	
	 */
	
	private static Vector<String> all_places_vector = new Vector<String>();
	private static Vector<String> training_traces = new Vector<String>();
	private static String activity_in_initial_marking_vector;
	private static String activity_in_final_marking_vector;	
	private static Vector<String> all_activities_vector = new Vector<String>();
	private static HashMap<String, Vector<String>> transitionGraph = new HashMap<String, Vector<String>>();
	private static Map<String, Double> activitiesCost = new HashMap<String, Double>();
	private static Petrinet net;

	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////// GETTERS AND SETTERS ///////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static Vector<String> getLogActivitiesRepositoryVector() {
		return log_activities_repository_vector;
	}
	public static void setLogActivitiesRepositoryVector(Vector<String> v) {
		log_activities_repository_vector = v;
	}
	public static Vector<Trace> getAllTracesVector() {
		return all_traces_vector;
	}
	public static void setAllTracesVector(Vector<Trace> all_traces_vector) {
		Constants.all_traces_vector = all_traces_vector;
	}
	public static Vector<PetrinetTransition> getAllTransitionsVector() {
		return all_transitions_vector;
	}
	public static void setAllTransitionsVector(Vector<PetrinetTransition> all_transitions_vector) {
		Constants.all_transitions_vector = all_transitions_vector;
	}
	public static Vector<String> getAllPlacesVector() {
		return all_places_vector;
	}
	public static void setAllPlacesVector(Vector<String> all_places_vector) {
		Constants.all_places_vector = all_places_vector;
	}
	public static Vector<String> getAllActivitiesVector() {
		return all_activities_vector;
	}
	public static void setAllActivitiesVector(Vector<String> all_activities_repository_vector) {
		Constants.all_activities_vector = all_activities_repository_vector;
	}
	public static HashMap<String, Vector<String>> getTransitionGraph() {
		return transitionGraph;
	}
	public static void setTransitionGraph(HashMap<String, Vector<String>> transitionGraph) {
		Constants.transitionGraph = transitionGraph;
	}
	public static String getActivity_in_initial_marking_vector() {
		return activity_in_initial_marking_vector;
	}
	public static void setActivity_in_initial_marking_vector(String activity_in_initial_marking_vector) {
		Constants.activity_in_initial_marking_vector = activity_in_initial_marking_vector;
	}
	public static String getActivity_in_final_marking_vector() {
		return activity_in_final_marking_vector;
	}
	public static void setActivity_in_final_marking_vector(String activity_in_final_marking_vector) {
		Constants.activity_in_final_marking_vector = activity_in_final_marking_vector;
	}
	public static Vector<String> getTraining_traces() {
		return training_traces;
	}
	public static void setTraining_traces(Vector<String> training_log_traces) {
		Constants.training_traces = training_log_traces;
	}
	public static Map<String, Double> getActivitiesCost() {
		return activitiesCost;
	}
	public static void setActivitiesCost(Map<String, Double> activitiesCost) {
		Constants.activitiesCost = activitiesCost;
	}
	public static Petrinet getNet() {
		return net;
	}
	public static void setNet(Petrinet net) {
		Constants.net = net;
	}	

}
