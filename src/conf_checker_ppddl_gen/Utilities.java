package conf_checker_ppddl_gen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Pattern;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.extension.std.XLifecycleExtension;
import org.deckfour.xes.extension.std.XTimeExtension;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.out.XesXmlSerializer;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.PetrinetEdge;
import org.processmining.models.graphbased.directed.petrinet.PetrinetNode;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;

import main.Constants;
import main.PetrinetTransition;
import main.Trace;

public class Utilities {

	public static StringBuffer createPropositionalDomain(Trace trace) {

		StringBuffer PPDDL_domain_buffer = new StringBuffer();

		PPDDL_domain_buffer.append("(define (domain Mining)\n");
		PPDDL_domain_buffer.append("(:requirements :typing :negative-preconditions :conditional-effects :probabilistic-effects :rewards)\n");
		PPDDL_domain_buffer.append("(:types node event)\n\n");

		PPDDL_domain_buffer.append("(:predicates\n");	
		PPDDL_domain_buffer.append("(activity ?p - node)\n");			
		PPDDL_domain_buffer.append("(tracePointer ?e - event)\n");		
		PPDDL_domain_buffer.append(")\n\n");			

		for(int i=0;i<Constants.getAllTransitionsVector().size();i++) {

			PetrinetTransition ith_transition = Constants.getAllTransitionsVector().elementAt(i);

			generateMoveSync(PPDDL_domain_buffer, trace, ith_transition);
			generateMoveModel(PPDDL_domain_buffer, ith_transition);
		}
		
		generateMovesLog(PPDDL_domain_buffer, trace);

		PPDDL_domain_buffer.append(")");
		return PPDDL_domain_buffer;
	}

	private static void generateMovesLog(StringBuffer PPDDL_domain_buffer, Trace trace) {
		for(int itr=0;itr<trace.getTraceContentVector().size();itr++) {

			String activity_related_to_the_event = trace.getTraceContentVector().elementAt(itr);

			int index_current_trace = itr + 1;
			int index_next_trace = itr + 2;

			String current_event = "ev" + index_current_trace;

			String next_event;
			if(index_current_trace==trace.getTraceContentVector().size())
				next_event = "evEND";
			else
				next_event = "ev" + index_next_trace;

			PPDDL_domain_buffer.append("(:action moveInTheLog#" + activity_related_to_the_event + "#" + current_event + "-" + next_event + "\n");
			PPDDL_domain_buffer.append(":precondition (tracePointer " + current_event  + ")\n");
			PPDDL_domain_buffer.append(":effect (and (not (tracePointer " + current_event  + ")) (tracePointer " + next_event  + ")");
			PPDDL_domain_buffer.append(" (decrease (reward) 1) ");
			PPDDL_domain_buffer.append(")");
			PPDDL_domain_buffer.append(")\n\n");	
		}
	}

	private static void generateMoveModel(StringBuffer PPDDL_domain_buffer, PetrinetTransition ith_transition) {
		var transitionGraph = Constants.getTransitionGraph();
		var activitiesCost = Constants.getActivitiesCost();
		String transitionName = ith_transition.getName(); 
		
		if(isInvisible(transitionName))
			return;

		PPDDL_domain_buffer.append("(:action moveInTheModel" + "#" + ith_transition.getName() + "\n");
		PPDDL_domain_buffer.append(":precondition (activity " + transitionName + ")\n");			
		PPDDL_domain_buffer.append(":effect (probabilistic ");
			
		Vector<String> nextAtivities = transitionGraph.get(transitionName.toLowerCase());
		for(String nextNode : nextAtivities)
		{
			String activityCostLabel = transitionName+nextNode;
			var activityCost = activitiesCost.get(activityCostLabel) == null ? 1.0 : activitiesCost.get(activityCostLabel);
			PPDDL_domain_buffer.append(activityCost + " (and (not (activity " + transitionName + "))");
			PPDDL_domain_buffer.append(" (activity " + nextNode + ") (decrease (reward) 1)) \n");
		}
			
		PPDDL_domain_buffer.append("))\n\n");
	}
	private static void generateMoveSync(StringBuffer PPDDL_domain_buffer, Trace trace, PetrinetTransition ith_transition) {
		var transitionGraph = Constants.getTransitionGraph();
		var activitiesCost = Constants.getActivitiesCost();
		
		for(int k=0;k<trace.getTraceContentVector().size();k++) {

			String elem_of_the_trace = trace.getTraceContentVector().elementAt(k);
			int ev_curr_index = k + 1;
			int ev_next_index = k + 2;
			String curr_event = "ev" + ev_curr_index;
			
			if(elem_of_the_trace.equalsIgnoreCase(ith_transition.getName())) {
				PPDDL_domain_buffer.append("(:action moveSync" + "#" + ith_transition.getName() + "#" + curr_event + "\n");
				PPDDL_domain_buffer.append(":precondition (and ");
							
				String transitionName = ith_transition.getName();
				
				PPDDL_domain_buffer.append(" (activity " + transitionName + ")");
				PPDDL_domain_buffer.append(" (tracePointer "+ curr_event + "))\n");

				PPDDL_domain_buffer.append(":effect (and");

				String next_event;
				if(ev_curr_index==trace.getTraceContentVector().size())
					next_event = "evEND";
				else
					next_event = "ev" + ev_next_index;

				PPDDL_domain_buffer.append(" (not (tracePointer "+ curr_event + ")) (tracePointer "+ next_event + ")");
				PPDDL_domain_buffer.append("(probabilistic ");
				
				Vector<String> nextActivities = transitionGraph.get(transitionName.toLowerCase());
				for(String nextNode : nextActivities)
				{
					String activityCostLabel = transitionName+nextNode;
					var activityCost = activitiesCost.get(activityCostLabel) == null ? 1.0 : activitiesCost.get(activityCostLabel);
					PPDDL_domain_buffer.append(activityCost + " (and (not (activity " + transitionName + "))");
					PPDDL_domain_buffer.append(" (activity " + activityCost + "))) \n");
				}				
				PPDDL_domain_buffer.append("))\n\n");
			}

		}
	}
	public static void generateTransitionGraph(Petrinet net) {
		HashMap<String, Vector<String>> transitionGraph = new HashMap<String, Vector<String>>();
		Collection<Transition> transitions = net.getTransitions();
		
		for (Transition transition : transitions) 
		{
			if(isInvisible(transition.getLabel()))
				continue;
			
			Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> transitionOutEdgesCollection = net.getOutEdges(transition);
		
			for(PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> node : transitionOutEdgesCollection)
			{
				PetrinetNode nodeLabel = node.getTarget();
				
				Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> placeOutEdgesCollection = net.getOutEdges(nodeLabel);
				
				Vector<String> adjacentNodes = new Vector<String>();
				for(var edge : placeOutEdgesCollection)
				{
					PetrinetNode adjacentNode = edge.getTarget();
					
					if(isInvisible(adjacentNode.getLabel()))
						getAdjacentNodes(net, adjacentNode, adjacentNodes);	
					else 
						adjacentNodes.add(adjacentNode.getLabel().toLowerCase());
				}
			
				if(adjacentNodes.size() == 0) 
				{
					adjacentNodes.add("modelend");
					transitionGraph.put(transition.getLabel().toLowerCase(), adjacentNodes);
				} else {
					transitionGraph.put(transition.getLabel().toLowerCase(), adjacentNodes);
				}
					
			}
		}
		Constants.setTransitionGraph(transitionGraph);
	}
	
	private static void getAdjacentNodes(Petrinet net, PetrinetNode adjacentNode, Vector<String> adjacentNodes) 
	{
		Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> transitionOutEdgesCollection = net.getOutEdges(adjacentNode);

		for(PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> node : transitionOutEdgesCollection)
		{
			PetrinetNode nodeLabel = node.getTarget();
			
			Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> placeOutEdgesCollection = net.getOutEdges(nodeLabel);
			for(var edge : placeOutEdgesCollection)
			{
				PetrinetNode adjacentActivity = edge.getTarget();						
				adjacentNodes.add(adjacentActivity.getLabel());
			}		
		}
	}
	
	public static Boolean isInvisible(String transition) {
		String transitionLowerCase = transition.toLowerCase(); 
		String regex = "i\\d";
		return transitionLowerCase.contains("inv") || Pattern.matches(regex, transitionLowerCase); 
	}
	
	public static StringBuffer createPropositionalProblem(Trace trace) {

		StringBuffer PPDDL_problem_buffer = new StringBuffer();	
		String initial_marking = Constants.getActivity_in_initial_marking_vector();
		String final_marking = Constants.getActivity_in_final_marking_vector();
		var activities = Constants.getAllActivitiesVector();
		
		PPDDL_problem_buffer.append("(define (problem Align) (:domain Mining)\n");
		PPDDL_problem_buffer.append("(:objects\n");	
		
		for(String activity : activities)
			PPDDL_problem_buffer.append(activity + " - node\n");		
		
		for(int ev=0;ev<trace.getTraceContentVector().size();ev++) 	
			PPDDL_problem_buffer.append("ev" + (ev+1) + " - event\n");	
		
		PPDDL_problem_buffer.append("evEND - event)\n\n");	
		
		PPDDL_problem_buffer.append("(:init\n");
		PPDDL_problem_buffer.append("(tracePointer ev1)\n");
		PPDDL_problem_buffer.append("(activity "+ initial_marking +")");
		PPDDL_problem_buffer.append(")\n\n");
		
		
		PPDDL_problem_buffer.append("(:goal\n");
		PPDDL_problem_buffer.append("(and\n");

		
		for(String activity : activities)
		{
			if(activity.equals(final_marking))
				PPDDL_problem_buffer.append("(activity " + activity + ")\n");
			else
				PPDDL_problem_buffer.append("(not (activity " + activity + "))\n");
		}
		
		PPDDL_problem_buffer.append("(tracePointer evEND)))\n\n");
		PPDDL_problem_buffer.append("(:metric maximize (reward))\n");
		PPDDL_problem_buffer.append(")\n");
				
		return PPDDL_problem_buffer;
	}

	public static File writeFile(String nomeFile, StringBuffer buffer) {

		File file = null;
		FileWriter fw = null;

		try {
			file = new File(nomeFile);
			file.setExecutable(true);

			fw = new FileWriter(file);
			fw.write(buffer.toString());
			fw.close();

			//fw.flush();
			//fw.close();
			
			return file;
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
//	
//	/**
//	 * Check whether the OS is 64 bits.
//	 * 
//	 * @return true if OS is 64 bits.
//	 */
//	public static boolean is64bitsOS() {
//		String osArch = System.getProperty("os.arch");
//		String winArch = System.getenv("PROCESSOR_ARCHITECTURE");
//		String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");
//		
//		return osArch != null && osArch.endsWith("64") || winArch != null && winArch.endsWith("64") || wow64Arch != null && wow64Arch.endsWith("64");
//	}
//
//	public static String getCorrectFormatting(String string)  {
//
//		if(string.contains(" "))
//			string = string.replaceAll(" ", "");
//
//		if(string.contains("/"))
//			string = string.replaceAll("\\/", "");
//
//		if(string.contains("("))
//			string = string.replaceAll("\\(", "");
//
//		if(string.contains(")"))
//			string = string.replaceAll("\\)", "");
//
//		if(string.contains("<"))
//			string = string.replaceAll("\\<", "");
//
//		if(string.contains(">"))
//			string = string.replaceAll("\\>", "");
//
//		if(string.contains("."))
//			string = string.replaceAll("\\.", "");
//
//		if(string.contains(","))
//			string = string.replaceAll("\\,", "_");
//
//		if(string.contains("+"))
//			string = string.replaceAll("\\+", "_");
//
//		if(string.contains("-"))
//			string = string.replaceAll("\\-", "_");
//
//		return string;
//	}
//
//	public static boolean isInteger(String s) {
//		try { 
//			Integer.parseInt(s); 
//		} catch(NumberFormatException e) { 
//			return false; 
//		} catch(NullPointerException e) {
//			return false;
//		}
//		// only got here if we didn't return false
//		return true;
//	}
//
//	/**
//	 * Method that creates a XES file starting from a XLog passed as input.
//	 */
//	public static File createXESFile(XLog eventLog, File outFile) throws IOException {
//		OutputStream outStream = new FileOutputStream(outFile);
//		new XesXmlSerializer().serialize(eventLog,outStream);
//		return outFile;
//	}
//
//	/**
//	 * Method that returns the current Timestamp.
//	 */
//	public static Timestamp getCurrentTimestamp() {
//		java.util.Date date = new java.util.Date();
//		return new Timestamp(date.getTime());
//	}
//
//	public static void createXLog() throws IOException {
//
//		//---------------------------------------------------------------//
//		XFactory factory = XFactoryRegistry.instance().currentDefault();
//		XLog log = factory.createLog();
//		//---------------------------------------------------------------//
//
//		File folder = new File("fast-downward/src/plans_found/hmax/62/30NOISE");
//		File[] listOfFiles = folder.listFiles();
//
//		Arrays.sort(listOfFiles);
//		int traces_noised_int = 0;
//		int total_noise_steps = 0;
//
//		for (int i = 0; i < listOfFiles.length; i++) {
//
//			File file = listOfFiles[i];
//
//			//---------------------------------------------------------------//
//			XTrace trace = factory.createTrace();
//			XConceptExtension.instance().assignName(trace, "id"+i);
//			//---------------------------------------------------------------//		
//
//			boolean trace_with_noise = false;
//
//			if (file.isFile()) {
//
//				System.out.println("FILE NAME : " + file.getName());
//				BufferedReader br = new BufferedReader(new FileReader(file));           
//
//				try {
//					String line = br.readLine();
//
//					trace_with_noise = false;
//
//					int noise_steps = 0;
//
//					while (line != null) {
//
//						boolean event_found = false;
//
//						System.out.println(line);
//
//						if(line.contains("(movesync-")) {
//							line = line.replace("(movesync-", "");
//							int index = line.lastIndexOf("-");
//							line = line.substring(0,index);
//							event_found = true;
//						}
//
//						if(line.contains("(moveinthemodel-")) {
//							line = line.replace("(moveinthemodel-", "");
//							event_found = true;
//
//
//							if(!line.contains("generatedinv")) {
//								trace_with_noise = true;
//								noise_steps++;
//							}
//
//						}
//
//						if(line.contains("(moveinthelog-")) {
//							trace_with_noise = true;
//							noise_steps++;
//						}
//
//						if(event_found) {
//							XEvent event = factory.createEvent();
//							XConceptExtension.instance().assignName(event, line);
//
//							Timestamp tm = Utilities.getCurrentTimestamp();
//
//							XLifecycleExtension.instance().assignTransition(event, "complete");
//							XTimeExtension.instance().assignTimestamp(event, tm.getTime());
//
//							trace.add(event);
//						}
//
//						line = br.readLine();
//
//
//					}
//					System.out.println("NOISE STEPS : " + noise_steps);
//					total_noise_steps += noise_steps;
//					System.out.println("TOTAL NOISE STEPS : " + total_noise_steps);
//
//				} finally {
//					br.close();
//				}
//
//				log.add(trace);
//
//				if(trace_with_noise)
//					traces_noised_int++;
//
//			} 
//
//			System.out.println("Number of traces with noise : " + traces_noised_int);
//
//		}
//
//
//		File file_for_log = new File("aligned_logs/" + Utilities.getCurrentTimestamp().getTime() + ".xes");
//		try {
//			Utilities.createXESFile(log,file_for_log);
//		} 
//		catch (IOException e) {
//			e.printStackTrace();
//		}	
//
//	}
//
//	public static boolean isUpperCase(String str){
//
//		for(int i=0; i<str.length(); i++){
//			char c = str.charAt(i);
//
//			if(Character.isUpperCase(c))
//				return true;
//		}
//		return false;
//
//	}
//
//	public static void deleteFolderContents(File folder) {
//		File[] files = folder.listFiles();
//		if(files!=null) { 
//			for(File f: files) {
//				if(f.isDirectory()) {
//					deleteFolderContents(f);
//				} else {
//					f.delete();
//				}
//			}
//		}
//	}
//
//	public static String getCostOfActivity(String activityName, String type_of_cost) {
//
//		for(int index=0;index<Constants.getActivitiesCostVector().size();index++) {							
//			Vector<String> v = Constants.getActivitiesCostVector().elementAt(index);
//			if(v.elementAt(0).equalsIgnoreCase(activityName)) {
//				if(type_of_cost == "move_in_the_model")
//					return(v.elementAt(1));
//				else if(type_of_cost == "move_in_the_log")
//					return(v.elementAt(2));
//			}
//		}
//		return "";
//	}

}

