package conf_checker_ppddl_gen;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.models.connections.GraphLayoutConnection;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.PetrinetEdge;
import org.processmining.models.graphbased.directed.petrinet.PetrinetNode;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.graphbased.directed.petrinet.impl.PetrinetFactory;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.pnml.Pnml;

import main.Constants;
import main.PetrinetTransition;
import main.PnmlImportUtils;
import main.Trace;
import main.XLogReader;

import java.awt.Color;

public class WB_Main {

	protected static final boolean String = false;
	private JFrame frmL;
	private JLabel lbStep1;
	private JLabel lbStep2;
	private JLabel lbStep3;
	private JLabel lbStep4;
	private JLabel lbStartEvent;
	private JLabel lbEndEvent;
	
	private JButton btStep1Next; 	
	private JButton btStep2Prev;
	private JButton btStep2Next;
	private JButton btStep3Prev;
	private JButton btStep3Next;
	private JButton btStep4Prev;
	private JButton btStep4Next;	
	private JButton btImportEventLog;
	private JButton btImportTrainingEventLog;
	private JComboBox<String> cbStartEvent;
	private JComboBox<String> cbEndEvent;	
	private JComboBox<String> cbEstimator;
	private int petriNetImported = -1;
	private int trainingEventLogImported = -1;
	private int eventLogImported = -1;
	
	
	private JButton btImportPetriNet;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WB_Main window = new WB_Main();
					window.frmL.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public WB_Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmL = new JFrame();
		frmL.setResizable(false);
		frmL.setTitle("Conf. checking PPDDL generator");
		frmL.setBounds(100, 100, 404, 328);
		frmL.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmL.getContentPane().setLayout(null);
		
		JButton btGeneratePpddl = new JButton("Generate PPDDL");
		btGeneratePpddl.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btGeneratePpddl.setEnabled(false);
		btGeneratePpddl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				var number_of_traces_to_check_from = 1;
				var number_of_traces_to_check_to = Constants.getAllTracesVector().size();
			
				for(int k=number_of_traces_to_check_from-1;k<number_of_traces_to_check_to;k++) {

					Trace trace = Constants.getAllTracesVector().elementAt(k);
					
					StringBuffer sb_domain = Utilities.createPropositionalDomain(trace);
					StringBuffer sb_problem = Utilities.createPropositionalProblem(trace);
					
					int trace_real_number = k + 1;

					Utilities.writeFile("PPDDLfiles/domain" + trace_real_number + ".pddl", sb_domain);
					Utilities.writeFile("PPDDLfiles/problem" + trace_real_number + ".pddl", sb_problem);
					
				}
				
				
			}
		});
		btGeneratePpddl.setBounds(106, 261, 161, 23);
		frmL.getContentPane().add(btGeneratePpddl);
		
		JPanel pnStep1 = new JPanel();
		pnStep1.setBorder(new LineBorder(new Color(192, 192, 192)));
		pnStep1.setToolTipText("");
		pnStep1.setBounds(5, 11, 383, 39);
		frmL.getContentPane().add(pnStep1);
		pnStep1.setLayout(null);
		
		lbStep1 = new JLabel("STEP 1 : Import Petri net");
		lbStep1.setBounds(0, 16, 152, 13);
		lbStep1.setVerticalAlignment(SwingConstants.BOTTOM);
		pnStep1.add(lbStep1);
		lbStep1.setFont(new Font("Arial", Font.BOLD, 10));
		
		btStep1Next = new JButton(">");
		btStep1Next.setBounds(319, 11, 52, 22);
		pnStep1.add(btStep1Next);
		
		btImportPetriNet = new JButton("Import");
		btImportPetriNet.setBounds(162, 11, 80, 22);
		pnStep1.add(btImportPetriNet);
		btImportPetriNet.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btImportPetriNet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();

				FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter("Petri Net Markup Language (*.pnml)", "pnml");

				fileChooser.setDialogTitle("Import a Petri Net");
				fileChooser.setAcceptAllFileFilterUsed(false);
				fileChooser.setFileFilter(xmlfilter);

				petriNetImported = fileChooser.showOpenDialog(null);
				
				if (petriNetImported != JFileChooser.APPROVE_OPTION)
				{
			        JOptionPane.showMessageDialog(null, "No Petri net was imported", "WARNING", JOptionPane.INFORMATION_MESSAGE);
			        return;					
				}
				
				File f = fileChooser.getSelectedFile();
				
				try {
					// create Pnml object from .pnml file
					PnmlImportUtils ut = new PnmlImportUtils();
					InputStream input = new FileInputStream(f);
					Pnml pnml = ut.importPnmlFromStream(input);
	
					// create Petri Net from Pnml object
					Petrinet net = PetrinetFactory.newPetrinet(pnml.getLabel() + " (imported from " + f.getName() + ")");
					Marking marking = new Marking();								  // only needed for Petrinet initialization
					pnml.convertToNet(net, marking, new GraphLayoutConnection(net));  // initialize Petrinet
	
					Constants.setNet(net);
					Utilities.generateTransitionGraph(net);
					Collection<Place> places = net.getPlaces();
					Collection<Transition> transitions = net.getTransitions();
					
					Constants.setAllTransitionsVector(new Vector<PetrinetTransition>());
					Constants.setAllPlacesVector(new Vector<String>());
					
					//Feed the vector of places with the places imported from the Petri Net.
					//Determine which places compose the initial and final markings.
					for (Place place : places) {
						String placeName = place.getLabel();
						placeName = getCorrectFormatting(placeName);

						Constants.getAllPlacesVector().addElement(placeName.toLowerCase());
					}
					
					//get all transitions labels
					Vector<String> transitionLabels = new Vector<String>();					
					for (Transition transition : transitions) 
					{
						if(!Utilities.isInvisible(transition.getLabel()))
							transitionLabels.add(transition.getLabel());
					}
					transitionLabels.add("modelEnd");
					
					Constants.setAllActivitiesVector(transitionLabels);			
					
					for (Transition transition : transitions) 
					{
						//To get OUTGOING edges from a transition
						Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> transitionOutEdgesCollection = net.getOutEdges(transition);

						//To get INGOING edges to a transition
						Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> transitionInEdgesCollection = net.getInEdges(transition);

						Vector<Place> transitionOutPlacesVector = new Vector<Place>();
						Vector<Place> transitionInPlacesVector = new Vector<Place>();
						Iterator<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> transitionInEdgesIterator = transitionInEdgesCollection.iterator();
						Iterator<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> transitionOutEdgesIterator = transitionOutEdgesCollection.iterator();

						while(transitionInEdgesIterator.hasNext()) {
							PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> edge = transitionInEdgesIterator.next();									
							transitionInPlacesVector.addElement((Place) edge.getSource());
						}
						
						while(transitionOutEdgesIterator.hasNext()) {
							PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> edge = transitionOutEdgesIterator.next();
							transitionOutPlacesVector.addElement((Place) edge.getTarget());									
						}
						
						String activityName = transition.getLabel();
						activityName = getCorrectFormatting(activityName);

						PetrinetTransition petriNetTransition = new PetrinetTransition(activityName.toLowerCase(), transitionInPlacesVector, transitionOutPlacesVector);
						Constants.getAllTransitionsVector().addElement(petriNetTransition);
					}
					
					//
					// Check if a transition with a specific label appears multiple times in a Petri Net		
					// If so, create a specific alias for the transition 
					//
					for(int ixc=0;ixc<Constants.getAllTransitionsVector().size();ixc++)  {

						PetrinetTransition pnt = Constants.getAllTransitionsVector().elementAt(ixc);
						int occurrences = 0;

						if(!pnt.isMultiple()) {

							for(int j=ixc+1;j<Constants.getAllTransitionsVector().size();j++)  {

								PetrinetTransition pnt2 = Constants.getAllTransitionsVector().elementAt(j);

								if(pnt2.getName().equalsIgnoreCase(pnt.getName())) {
									if(!pnt.isMultiple()) {
										pnt.setMultiple(true);
										pnt.setAlias(pnt.getName() + "0");
									}
									occurrences ++;
									pnt2.setAlias(pnt.getName() + occurrences);
									pnt2.setMultiple(true);
								}
							}
						}
					}
					
					JOptionPane.showMessageDialog(null, "Petri net imported successfully", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception a) {
					
				}
		}});
		
			//region actions
			btStep1Next.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					if (petriNetImported != JFileChooser.APPROVE_OPTION)
					{
				        JOptionPane.showMessageDialog(null, "No Petri net was imported", "WARNING", JOptionPane.INFORMATION_MESSAGE);
				        return;					
					}
					
					configStage1(false);
					configStage2(true);
					
					cbStartEvent.setModel(new DefaultComboBoxModel<String>(Constants.getAllActivitiesVector()));	
					cbEndEvent.setModel(new DefaultComboBoxModel<String>(Constants.getAllActivitiesVector()));
				}
			});
		
		JPanel pnStep3 = new JPanel();
		pnStep3.setBorder(new LineBorder(new Color(192, 192, 192)));
		pnStep3.setBounds(5, 140, 383, 68);
		frmL.getContentPane().add(pnStep3);
		pnStep3.setLayout(null);
		
		btStep3Next = new JButton(">");
		btStep3Next.setEnabled(false);
		btStep3Next.setBounds(319, 35, 52, 22);
		pnStep3.add(btStep3Next);
		
		btStep3Prev = new JButton("<");
		btStep3Prev.setEnabled(false);
		btStep3Prev.setBounds(319, 9, 52, 22);
		pnStep3.add(btStep3Prev);
		
		btImportTrainingEventLog = new JButton("Import");
		btImportTrainingEventLog.setEnabled(false);
		btImportTrainingEventLog.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btImportTrainingEventLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				importTrainingEventLog();
				
				String selected = cbEstimator.getSelectedItem().toString();
				
				if(selected.equals("Frequency"))
				{
					var activitiesFrequency = generateActivitiesFrequency();
					generateFrequencyEstimator(activitiesFrequency);
				}
				
			}
			
			private void generateFrequencyEstimator(Map<String, Integer> activitiesFrequency) {				
				Vector<PetrinetTransition> transitionsActivities = Constants.getAllTransitionsVector();		
				var transitionGraph = Constants.getTransitionGraph();
				
				Map<String, Double> activityCosts = new HashMap<String, Double>();
				for(var transition : transitionsActivities)
				{
					var name = transition.getName();
					
					if(Utilities.isInvisible(name))
						continue;
					
					var activities = transitionGraph.get(name);
					double totalSum = 0.0;
					
					//get total frequency of activities
					for(String activity : activities) 
						if(activity.equals("modelend"))
							totalSum += 1;
						else
							totalSum += activitiesFrequency.getOrDefault(activity, 0);	
					
					for(String activity : activities) {
						double currentActivityCost;
						if(activity.equals("modelend"))
							currentActivityCost = 1;
						else
							currentActivityCost = activitiesFrequency.getOrDefault(activity, 0) / totalSum;
						
						activityCosts.put(name+activity, currentActivityCost);
					}
					
				}
				Constants.setActivitiesCost(activityCosts);
			}
			
			
			private Map<String, Integer> generateActivitiesFrequency() {
				Vector<String> training_traces = Constants.getTraining_traces();
				Map<String, Integer> activitiesFrequency = new HashMap<String, Integer>();
				
				activitiesFrequency.put("sum", 0);
				
				for(String trace : training_traces){
					var traceAsArray = Arrays.asList(trace.split(""));
					
					for(String activity : traceAsArray)
					{
						int activityFrequency = Collections.frequency(traceAsArray, activity);
						
						if(activitiesFrequency.containsKey(activity))
						{
							int storedActivityFrequency = activitiesFrequency.get(activity);
							activitiesFrequency.replace(activity, activityFrequency + storedActivityFrequency);
						}else {
							activitiesFrequency.put(activity, activityFrequency);
						}
						
						int totalSumActivities = activitiesFrequency.get("sum");
						activitiesFrequency.replace("sum", activityFrequency + totalSumActivities);
					}
				}
				return activitiesFrequency;
			}
			
			private void importTrainingEventLog() {
				JFileChooser fileChooser = new JFileChooser();

				FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter("Extensible Event Stream (*.xes)", "xes");

				fileChooser.setDialogTitle("Import a training event log");
				fileChooser.setAcceptAllFileFilterUsed(false);
				fileChooser.setFileFilter(xmlfilter);

				trainingEventLogImported = fileChooser.showOpenDialog(null);
				
				if (trainingEventLogImported != JFileChooser.APPROVE_OPTION)
				{
			        JOptionPane.showMessageDialog(null, "No training event log was imported", "WARNING", JOptionPane.INFORMATION_MESSAGE);
			        return;					
				}
				
				File selectedFile = fileChooser.getSelectedFile();
				
				try {
					XLog log = XLogReader.openLog(selectedFile.getAbsolutePath());
					Vector<String> trainingTraces = new Vector<String>();

					for(XTrace trace:log){
						String current_trace = "";
						for(XEvent event : trace){
							String activityName = XConceptExtension.instance().extractName(event).toLowerCase();
							activityName = getCorrectFormatting(activityName);
							current_trace += activityName;
						}						
						trainingTraces.add(current_trace);					
					}
					
					Constants.setTraining_traces(trainingTraces);
					
					JOptionPane.showMessageDialog(null, "training event log imported successfully", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
					
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
			
			
		});
		btImportTrainingEventLog.setSelectedIcon(null);
		btImportTrainingEventLog.setIcon(null);
		btImportTrainingEventLog.setBounds(162, 35, 80, 22);
		pnStep3.add(btImportTrainingEventLog);
		
		lbStep3 = new JLabel("");
		lbStep3.setBounds(0, 8, 337, 23);
		pnStep3.add(lbStep3);
		lbStep3.setText("<html> STEP 3 : Choose estimator and import a . XES file to train it </html>");
		lbStep3.setEnabled(false);
		lbStep3.setFont(new Font("Arial", Font.BOLD, 10));
		
		cbEstimator = new JComboBox<String>();
		cbEstimator.setEnabled(false);
		cbEstimator.setBounds(5, 35, 140, 22);
		cbEstimator.addItem("Frequency");
		pnStep3.add(cbEstimator);
		
		JPanel pnStep4 = new JPanel();
		pnStep4.setLayout(null);
		pnStep4.setBorder(new LineBorder(new Color(192, 192, 192)));
		pnStep4.setBounds(5, 219, 383, 31);
		frmL.getContentPane().add(pnStep4);
		
		btStep4Next = new JButton(">");
		btStep4Next.setEnabled(false);
		btStep4Next.setBounds(319, 5, 52, 22);
		pnStep4.add(btStep4Next);
		
		btStep4Prev = new JButton("<");
		btStep4Prev.setEnabled(false);
		btStep4Prev.setBounds(262, 5, 52, 22);
		pnStep4.add(btStep4Prev);
		
		lbStep4 = new JLabel("STEP 4 : Import an event log");
		lbStep4.setEnabled(false);
		lbStep4.setFont(new Font("Arial", Font.BOLD, 10));
		lbStep4.setBounds(0, 7, 142, 14);
		pnStep4.add(lbStep4);
		
		btImportEventLog = new JButton("Import");
		btImportEventLog.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btImportEventLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();

				FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter("Extensible Event Stream (*.xes)", "xes");

				fileChooser.setDialogTitle("Import an event log");
				fileChooser.setAcceptAllFileFilterUsed(false);
				fileChooser.setFileFilter(xmlfilter);

				eventLogImported = fileChooser.showOpenDialog(null);
				
				if (eventLogImported != JFileChooser.APPROVE_OPTION)
				{
			        JOptionPane.showMessageDialog(null, "No event log was imported", "WARNING", JOptionPane.INFORMATION_MESSAGE);
			        return;					
				}
				
				File selectedFile = fileChooser.getSelectedFile();
				
				try {
					XLog log = XLogReader.openLog(selectedFile.getAbsolutePath());
					
					// Vector used to record the complete alphabet of activities used in the log
					Vector<String> logAlphabetVector = new Vector<String>();

					// Vector used to record the activities of a specific trace of the log
					Vector<String> traceActivitiesVector = new Vector<String>();

					Vector<Trace> all_traces_vector = new Vector<Trace>();
					
					int traceId = 0;

					for(XTrace trace:log){

						traceId++;

						Trace t = new Trace("Trace#" + traceId);

						t.setTraceAlphabet(new Vector<String>());
						traceActivitiesVector = new Vector<String>();

						for(XEvent event : trace){
							String activityName = XConceptExtension.instance().extractName(event).toLowerCase();
							activityName = getCorrectFormatting(activityName);

							traceActivitiesVector.addElement(activityName);

							if(!t.getTraceAlphabet().contains(activityName))
								t.getTraceAlphabet().addElement(activityName);

							// add activity name to log alphabet (if not already present)
							if(!logAlphabetVector.contains(activityName))
								logAlphabetVector.addElement(activityName);
						}

						// Update the single trace of the log						

						for(int j=0;j<traceActivitiesVector.size();j++) {
							String string = (String) traceActivitiesVector.elementAt(j);
							t.getTraceContentVector().addElement(string);

							t.getTraceTextualContent().append(string);
							if(j<traceActivitiesVector.size()-1)
								t.getTraceTextualContent().append(",");
						}
						
						// Insert trace into vector of traces
						all_traces_vector.add(t);
						
						Constants.setLogActivitiesRepositoryVector(logAlphabetVector);
					}
					
					Constants.setAllTracesVector(all_traces_vector);
					JOptionPane.showMessageDialog(null, "training event log imported successfully", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
					
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}});
		btImportEventLog.setEnabled(false);
		btImportEventLog.setBounds(162, 5, 80, 22);
		pnStep4.add(btImportEventLog);
		
		JPanel pnStep2 = new JPanel();
		pnStep2.setBorder(new LineBorder(new Color(192, 192, 192)));
		pnStep2.setForeground(new Color(0, 0, 0));
		pnStep2.setBounds(5, 61, 383, 68);
		frmL.getContentPane().add(pnStep2);
		pnStep2.setLayout(null);
		
		lbStep2 = new JLabel("STEP 2: Set a start and end event");
		lbStep2.setEnabled(false);
		lbStep2.setFont(new Font("Arial", Font.BOLD, 10));
		lbStep2.setBounds(0, 11, 180, 14);
		pnStep2.add(lbStep2);
		
		lbStartEvent = new JLabel("Start Event");
		lbStartEvent.setEnabled(false);
		lbStartEvent.setFont(new Font("Arial", Font.PLAIN, 10));
		lbStartEvent.setBounds(0, 36, 55, 14);
		pnStep2.add(lbStartEvent);
		
		cbStartEvent = new JComboBox<String>();
		cbStartEvent.setEnabled(false);
		cbStartEvent.setBounds(55, 32, 85, 22);
		pnStep2.add(cbStartEvent);
		
		lbEndEvent = new JLabel("End Event");
		lbEndEvent.setEnabled(false);
		lbEndEvent.setFont(new Font("Arial", Font.PLAIN, 10));
		lbEndEvent.setBounds(157, 36, 62, 14);
		pnStep2.add(lbEndEvent);
		
		cbEndEvent = new JComboBox<String>();
		cbEndEvent.setBounds(211, 32, 85, 22);
		pnStep2.add(cbEndEvent);
		cbEndEvent.setEnabled(false);
		
		btStep2Next = new JButton(">");
		btStep2Next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				configStage2(false);
				configStage3(true);
				
				Constants.setActivity_in_initial_marking_vector((String)cbStartEvent.getSelectedItem());
				Constants.setActivity_in_final_marking_vector((String)cbEndEvent.getSelectedItem());
			}
		});
		btStep2Next.setEnabled(false);
		btStep2Next.setBounds(319, 35, 52, 22);
		pnStep2.add(btStep2Next);
		
		btStep2Prev = new JButton("<");
		btStep2Prev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				configStage1(true);
				configStage2(false);
			}
		});
		btStep2Prev.setEnabled(false);
		btStep2Prev.setBounds(319, 7, 52, 22);
		pnStep2.add(btStep2Prev);
		
		btStep3Prev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				configStage2(true);
				configStage3(false);
			}
		});
		
		btStep3Next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {								
				configStage2(false);
				configStage4(true);
			}
		});
				
		btStep4Prev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				configStage4(false);
				configStage2(true);
			}
		});
		
		btStep4Next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(eventLogImported == -1)
				{
					JOptionPane.showMessageDialog(null, "No event log was imported", "WARNING", JOptionPane.INFORMATION_MESSAGE);
			        return;	
				}
				
				configStage4(false);
				btGeneratePpddl.setEnabled(true);
			}
		});
		//endregion
	}
	
	private void configStage1(Boolean value) {
		lbStep1.setEnabled(value);
		btImportPetriNet.setEnabled(value);
		btStep1Next.setEnabled(value);
	}
	
	private void configStage2(Boolean value)
	{
		btStep2Next.setEnabled(value);
		btStep2Prev.setEnabled(value);
		lbStep2.setEnabled(value);
		lbStartEvent.setEnabled(value);
		lbEndEvent.setEnabled(value);
		cbStartEvent.setEnabled(value);
		cbEndEvent.setEnabled(value);
	}
	
	private void configStage3(Boolean value)
	{
		btStep3Next.setEnabled(value);
		btStep3Prev.setEnabled(value);
		lbStep3.setEnabled(value);
		cbEstimator.setEnabled(value);
		btImportTrainingEventLog.setEnabled(value);		
	}
	
	private void configStage4(Boolean value)
	{
		lbStep4.setEnabled(value);
		btImportEventLog.setEnabled(value);
		btStep4Prev.setEnabled(value);
		btStep4Next.setEnabled(value);
	}
	
	private String getCorrectFormatting(String string)  {

		if(string.contains(" "))
			string = string.replaceAll(" ", "");

		if(string.contains("/"))
			string = string.replaceAll("\\/", "");

		if(string.contains("("))
			string = string.replaceAll("\\(", "");

		if(string.contains(")"))
			string = string.replaceAll("\\)", "");

		if(string.contains("<"))
			string = string.replaceAll("\\<", "");

		if(string.contains(">"))
			string = string.replaceAll("\\>", "");

		if(string.contains("."))
			string = string.replaceAll("\\.", "");

		if(string.contains(","))
			string = string.replaceAll("\\,", "_");

		if(string.contains("+"))
			string = string.replaceAll("\\+", "_");

		if(string.contains("-"))
			string = string.replaceAll("\\-", "_");

		return string;
	}
}
