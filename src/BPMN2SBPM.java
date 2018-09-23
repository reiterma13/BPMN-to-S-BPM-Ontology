import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class BPMN2SBPM {

	@SuppressWarnings({})
	public static void main(String fileProcessName) throws ParserConfigurationException, SAXException, IOException,
			OWLOntologyCreationException, TransformerException {

		try {
			
			System.out.println("\r\nCreate BPMN S-BPM:");
			System.out.println("--------------------------------------------------------");
			System.out.println("Process Name: "+fileProcessName);
			System.out.println("--- Loading Generated-BPMN-OWL.owl");

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document document = null;
			String src = "logs/Generated-BPMN-OWL.owl";

			// Create SBPM.owl
			PrintWriter writer = new PrintWriter("logs/Generated-S-BPM-OWL.owl", "UTF-8");

			// XML Version
			writer.println("<?xml version=\"1.0\"?>\r\n");

			// DOCTYPE
			String doctypeNode = "<!DOCTYPE rdf:RDF [ \r\n" + 
					"    <!ENTITY owl \"http://www.w3.org/2002/07/owl#\" >\r\n" + 
					"    <!ENTITY xsd \"http://www.w3.org/2001/XMLSchema#\" >\r\n" + 
					"    <!ENTITY rdfs \"http://www.w3.org/2000/01/rdf-schema#\" >\r\n" + 
					"    <!ENTITY abstract-pass-ont \"http://www.imi.kit.edu/abstract-pass-ont#\" >\r\n" + 
					"    <!ENTITY standard-pass-ont \"http://www.imi.kit.edu/standard-pass-ont#\" >\r\n" + 
					"    <!ENTITY rdf \"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" >\r\n" + 
					"]>\r\n";
			writer.println(doctypeNode);
			
			// RDF
			String rdfNode = "<rdf:RDF xmlns:abstract-pass-ont=\"http://www.imi.kit.edu/abstract-pass-ont#\" xmlns:standard-pass-ont=\"http://www.imi.kit.edu/standard-pass-ont#\" xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:owl=\"http://www.w3.org/2002/07/owl#\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema#\" xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\" xmlns=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"\">";
			writer.println(rdfNode);

			try {
				document = docBuilder.parse(new File(src));
			} catch (Exception e) {
				System.out.println("--- Could not parse your file: " + src);
			}

			// create node list
			NodeList nodeList = document.getElementsByTagName("*");

			// parse the BPMN OWL nodes = named individuals
			System.out.println("--- Parsing Generated-BPMN-OWL.owl");

			Set<String> idMappingSet = new HashSet<String>();
			Set<String> participantSet = new HashSet<String>();
			Set<String> processSet = new HashSet<String>();
			Set<String> sendTaskSet = new HashSet<String>();
			Set<String> receiveTaskSet = new HashSet<String>();
			Set<String> messageFlowSet = new HashSet<String>();
			Set<String> initiatingProcessSet = new HashSet<String>();
			Set<String> startEventSet = new HashSet<String>();
			Set<String> taskSet = new HashSet<String>();
			Set<String> sequenceFlowSet = new HashSet<String>();
			Set<String> endEventSet = new HashSet<String>();
			Set<String> gatewaySet = new HashSet<String>();
			Set<String> processParticipantSet = new HashSet<String>();
			Set<String> bpmnProcessICSet = new HashSet<String>();
			Set<String> sendActorSet = new HashSet<String>();
			Set<String> receiveActorSet = new HashSet<String>();
			Set<String> sendAndReceivePointsSet = new HashSet<String>();
			Set<String> messageConnectorSet = new HashSet<String>();
			Set<String> messageSet = new HashSet<String>();
			Set<String> actorSet = new HashSet<String>();
			Set<String> processICisActorXSet = new HashSet<String>();
			Set<String> sendStateAndMessageSet = new HashSet<String>();
			Set<String> receiveStateAndMessageSet = new HashSet<String>();
			Set<String> functionStateAndMessageSet = new HashSet<String>();
			Set<String> sourceAndTargetOfSendTransition = new HashSet<String>();
			Set<String> sourceAndTargetOfReceiveTransition = new HashSet<String>();
			Set<String> sourceAndTargetOfStandardTransition = new HashSet<String>();
			Set<String> sourceStateSendTaskAndMessageText = new HashSet<String>();
			Set<String> sourceStateReceiveTaskAndMessageText = new HashSet<String>();
			Set<String> participantICAndMessageID = new HashSet<String>();
			Set<String> stateICandIDmapping = new HashSet<String>();
			Set<String> ssstamtSet = new HashSet<String>();
			Set<String> componentsForSBDSet = new HashSet<String>();
			Set<String> statesAndTransitionsForSBDSet = new HashSet<String>();
			Set<String> startEventSet1 = new HashSet<String>();

			for (int i = 0; i < nodeList.getLength(); i++) {
				String id = "";
				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					String nodeName = node.getNodeName();

					if (nodeName == "owl:NamedIndividual") {
						String individualAttributeValue = "";

						NamedNodeMap attributeList = node.getAttributes();
						for (int a = 0; a < attributeList.getLength(); a++) {
							individualAttributeValue = attributeList.item(a).toString().split("\\=")[1].replace("\"",
									"");
						}

						NodeList childList = node.getChildNodes();
						for (int c = 0; c < childList.getLength(); c++) {
							Node childNode = childList.item(c);
							String childName = childNode.getNodeName();

							if (childName == "rdf:type") {
								NamedNodeMap childAttributeList = childNode.getAttributes();
								for (int a = 0; a < childAttributeList.getLength(); a++) {
									String attributeValue = childAttributeList.item(a).toString().split("\\=")[1]
											.replace("\"", "");
									String type = attributeValue.toString().split("\\#")[1].trim();

									if (type.startsWith("bpmn2:participant")) {
										NodeList siblings = childNode.getParentNode().getChildNodes();
										for (int s = 0; s < siblings.getLength(); s++) {
											Node siblingNode = siblings.item(s);
											String siblingName = siblingNode.getNodeName();
											String participantName = "";

											if (siblingName == "name") {
												participantName = siblingNode.getTextContent();
												participantSet.add(individualAttributeValue + "_HH_" + participantName);
											}
											// for PROCESS and PARTICIPANT MAPPING
											if (siblingName == "processRef") {
												String processRef = siblingNode.getTextContent();
												processParticipantSet
														.add(individualAttributeValue + "_HH_" + processRef);
											}
										}
									}

									// PROCESS and PARTICIPANT MAPPING
									if (type.startsWith("bpmn2:process")) {
										String bpmn2_process_IC = individualAttributeValue.split("generated/")[1]
												.split("-belongsTo-")[0];
										NodeList siblings = childNode.getParentNode().getChildNodes();
										for (int s = 0; s < siblings.getLength(); s++) {
											Node siblingNode = siblings.item(s);
											String siblingName = siblingNode.getNodeName();
											if (siblingName == "id") {
												String processID = siblingNode.getTextContent();
												for (String p : processParticipantSet) {
													bpmnProcessICSet.add(bpmn2_process_IC + "==" + processID);
												}
											}
										}
									}

									if (type.startsWith("bpmn2:sendTask")) {
										String sendTaskName = "";
										NodeList siblings = childNode.getParentNode().getChildNodes();
										for (int s = 0; s < siblings.getLength(); s++) {
											Node siblingNode = siblings.item(s);
											String siblingName = siblingNode.getNodeName();
											if (siblingName == "name") {
												sendTaskName = siblingNode.getTextContent();
											}
											if (siblingName == "id") {
												id = siblingNode.getTextContent();
											}
											if (id.isEmpty() || sendTaskName.isEmpty()) {
											} else {
												sendTaskSet.add(
														individualAttributeValue + "_HH_" + sendTaskName + "_ID_" + id);
											}
										}
										sourceStateSendTaskAndMessageText.add(id + "_sourceMessage_" + sendTaskName);
									}

									if (type.startsWith("bpmn2:receiveTask")) {
										String receiveTaskName = "";
										NodeList siblings = childNode.getParentNode().getChildNodes();
										for (int s = 0; s < siblings.getLength(); s++) {
											Node siblingNode = siblings.item(s);
											String siblingName = siblingNode.getNodeName();
											String processRef = "";
											if (siblingName == "name") {
												receiveTaskName = siblingNode.getTextContent();
											}
											if (siblingName == "id") {
												id = siblingNode.getTextContent();
											}
											if (siblingName == "id") {
												processRef = siblingNode.getTextContent();
												processSet.add(individualAttributeValue + "_PR_" + processRef);
											}
											if (id.isEmpty() || receiveTaskName.isEmpty()) {
											} else {
												receiveTaskSet.add(individualAttributeValue + "_HH_" + receiveTaskName
														+ "_ID_" + id);
											}
										}
										sourceStateReceiveTaskAndMessageText
												.add(id + "_sourceMessage_" + receiveTaskName);
									}

									if (type.startsWith("bpmn2:messageFlow")) {
										NodeList siblings = childNode.getParentNode().getChildNodes();
										String messageFlowFrom = "";
										String messageFlowTo = "";
										for (int s = 0; s < siblings.getLength(); s++) {
											Node siblingNode = siblings.item(s);
											String siblingName = siblingNode.getNodeName();
											if (siblingName == "sourceRef") {
												messageFlowFrom = siblingNode.getTextContent();
											}
											if (siblingName == "targetRef") {
												messageFlowTo = siblingNode.getTextContent();
												messageFlowSet.add(individualAttributeValue + "_HH_" + "from-"
														+ messageFlowFrom + "_to-" + messageFlowTo);
											}
											if (siblingName == "id") {
												id = siblingNode.getTextContent();
											}
										}
									}

									String startEvent = "";
									if (type.startsWith("bpmn2:process")) {
										NodeList siblings = childNode.getParentNode().getChildNodes();
										for (int s = 0; s < siblings.getLength(); s++) {
											Node siblingNode = siblings.item(s);
											String siblingName = siblingNode.getNodeName();
											if (siblingName == "name") {
												startEvent = siblingNode.getTextContent();
												// get initiating process
												if (startEvent.startsWith("Initiating Process")) {
													// get initiating process id
													String initiatingProcessId = "";
													NodeList siblings2 = childNode.getParentNode().getChildNodes();
													for (int s2 = 0; s2 < siblings2.getLength(); s2++) {
														Node siblingNode2 = siblings.item(s2);
														String siblingName2 = siblingNode2.getNodeName();
														if (siblingName2 == "id") {
															initiatingProcessId = siblingNode2.getTextContent();
														}
													}
													initiatingProcessSet.add(
															individualAttributeValue + "_HH_" + initiatingProcessId);
												} else {
													String nonInitiatingProcessId = "";
													NodeList siblings2 = childNode.getParentNode().getChildNodes();
													for (int s2 = 0; s2 < siblings2.getLength(); s2++) {
														Node siblingNode2 = siblings.item(s2);
														String siblingName2 = siblingNode2.getNodeName();
														if (siblingName2 == "id") {
															nonInitiatingProcessId = siblingNode2.getTextContent();
															String processRef = siblingNode.getTextContent();
															processSet.add(
																	individualAttributeValue + "_PR_" + processRef);
														}
													}
													startEventSet.add(
															individualAttributeValue + "_HH_" + nonInitiatingProcessId);
													
												}
											}
										}
									}

									String userTask = "";
									String userTaskID = "";
									if (type.startsWith("bpmn2:userTask") | type.startsWith("bpmn2:task")) {
										NodeList siblings = childNode.getParentNode().getChildNodes();
										for (int s = 0; s < siblings.getLength(); s++) {
											Node siblingNode = siblings.item(s);
											String siblingName = siblingNode.getNodeName();
											if (siblingName == "name") {
												userTask = siblingNode.getTextContent();
												//taskSet.add(individualAttributeValue + "_HH_" + userTask);
											}
											if (siblingName == "id") {
												userTaskID = siblingNode.getTextContent();
											}
											if (userTask.isEmpty() || userTaskID.isEmpty()) {
											} else {
												taskSet.add(individualAttributeValue + "_HH_" + userTask + "_ID_"+userTaskID);
											}
										}
									}
									
									String startEvent1 = "";
									String startEvent1ID = "";
									String messageEventDefinition ="";
									if (type.startsWith("bpmn2:startEvent")) {
										NodeList siblings = childNode.getParentNode().getChildNodes();

										for (int s = 0; s < siblings.getLength(); s++) {
											Node siblingNode = siblings.item(s);
											String siblingName = siblingNode.getNodeName();
											if (siblingName == "name") {
												startEvent1 = siblingNode.getTextContent();
											}
											if (siblingName == "id") {
												startEvent1ID = siblingNode.getTextContent();
											}
											if (startEvent1.isEmpty() || startEvent1ID.isEmpty()) {
											} else {
												startEventSet1.add(individualAttributeValue + "_HH_" + startEvent1 + "_ID_"
														+ startEvent1ID);
												
												//TODO if bpmn2:messageEventDefinition add to receiveTaskSet																				
												receiveTaskSet.add(individualAttributeValue + "_HH_" + startEvent1 + "_ID_"
														+ startEvent1ID);
											}
										}
									}
									
									if (type.startsWith("bpmn2:sequenceFlow")) {
										String sequenceFlowFrom = "";
										String sequenceFlowTo = "";
										NodeList siblings = childNode.getParentNode().getChildNodes();
										for (int s = 0; s < siblings.getLength(); s++) {
											Node siblingNode = siblings.item(s);
											String siblingName = siblingNode.getNodeName();
											if (siblingName == "sourceRef") {
												sequenceFlowFrom = siblingNode.getTextContent();
											}
											if (siblingName == "targetRef") {
												sequenceFlowTo = siblingNode.getTextContent();
												sequenceFlowSet.add(individualAttributeValue + "_HH_" + "from-"
														+ sequenceFlowFrom + "_to-" + sequenceFlowTo);
											}
										}
										if (sequenceFlowFrom.contains("SendTask")) {
											sourceAndTargetOfSendTransition
													.add(sequenceFlowFrom + "_transitionTo_" + sequenceFlowTo);
										}
										else if (sequenceFlowFrom.contains("ReceiveTask")) {//TODO or start event 
											sourceAndTargetOfReceiveTransition
													.add(sequenceFlowFrom + "_transitionTo_" + sequenceFlowTo);
										}
										else {
											sourceAndTargetOfStandardTransition
											.add(sequenceFlowFrom + "_transitionTo_" + sequenceFlowTo);
										}
									}

									if (type.startsWith("bpmn2:endEvent")) {
										String endEvent = "";
										String endEventID = "";
										NodeList siblings = childNode.getParentNode().getChildNodes();
										for (int s = 0; s < siblings.getLength(); s++) {
											Node siblingNode = siblings.item(s);
											String siblingName = siblingNode.getNodeName();
											if (siblingName == "name") {
												endEvent = siblingNode.getTextContent();
											}
											if (siblingName == "id") {
												endEventID = siblingNode.getTextContent();
											}

											if (endEventID.isEmpty() || endEvent.isEmpty()) {
											} else {
												endEventSet.add(individualAttributeValue + "_HH_" + endEvent + "_ID_"
														+ endEventID);
											}
										}
									}

									String gateway = "";
									String gwID = "";
									if (type.startsWith("bpmn2:exclusiveGateway")) {
										NodeList siblings = childNode.getParentNode().getChildNodes();
										for (int s = 0; s < siblings.getLength(); s++) {
											Node siblingNode = siblings.item(s);
											String siblingName = siblingNode.getNodeName();
											if (siblingName == "name") {
												gateway = siblingNode.getTextContent();
											}
											if (siblingName == "id") {
												gwID = siblingNode.getTextContent();
											}
											if (gwID.isEmpty() || gateway.isEmpty()) {
											} else {
												gatewaySet.add(
														individualAttributeValue + "_HH_" + gateway + "_ID_" + gwID);
											}
										}
									}
								}
							}
						}
					}
				}
			}

			System.out.println("--- Transforming BPMN OWL into S-BPM OWL");

			String ontologyNode = "	<owl:Ontology rdf:about=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"\">\r\n"
					+ "		<owl:imports rdf:resource=\"http://www.imi.kit.edu/abstract-pass-ont\">" + "</owl:imports>\r\n"
					+ "		<owl:imports rdf:resource=\"http://www.imi.kit.edu/standard-pass-ont\">" + "</owl:imports>\r\n"
					+ "	</owl:Ontology>";

			writer.println(ontologyNode);

//			System.out.println("The process contains " + initiatingProcessSet.size() + " initiating process");
//			System.out.println("The process contains " + participantSet.size() + " participants");
//			System.out.println("The process contains " + sendTaskSet.size() + " send tasks");
//			System.out.println("The process contains " + receiveTaskSet.size() + " receive tasks");
//			System.out.println("The process contains " + messageFlowSet.size() + " message flows");
//			System.out.println("The process contains " + startEventSet.size() + " start events");
//			System.out.println("The process contains " + taskSet.size() + " tasks");
//			System.out.println("The process contains " + sequenceFlowSet.size() + " sequence flows");
//			System.out.println("The process contains " + endEventSet.size() + " end events");
//			System.out.println("The process contains " + gatewaySet.size() + " gateways\n");
			System.out.println("--- Create the PASSProcessModel");

			String actors = "";
			Integer actorNumber = 1;
			for (String participant : participantSet) {
				idMappingSet.add(participant + "_ID_SID_0_StandardActor_" + actorNumber);
				componentsForSBDSet.add("SBD_" + actorNumber + "_hasName_" + participant.split("_HH_")[1]
						+ "_hasNumber_" + actorNumber);
				actors = new StringBuilder(actors).append(
						"	<owl:NamedIndividual rdf:about=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#SID_0_StandardActor_"
								+ actorNumber + "\" >\r\n"
								+ "		<rdf:type rdf:resource=\"&standard-pass-ont;SingleActor\" ></rdf:type>\r\n"
								+ "		<standard-pass-ont:hasModelComponentID rdf:datatype=\"&xsd;string\" >SID_0_StandardActor_"
								+ actorNumber + "</standard-pass-ont:hasModelComponentID>\r\n"
								+ "		<standard-pass-ont:hasModelComponentLable xml:lang=\"en\" >"
								+ participant.split("_HH_")[1] + "</standard-pass-ont:hasModelComponentLable>\r\n"
								+ "		<standard-pass-ont:hasBehavior rdf:resource=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#SBD_"
								+ actorNumber + "_SID_0_StandardActor_" + actorNumber
								+ "\" ></standard-pass-ont:hasBehavior>\r\n" + "	</owl:NamedIndividual>\r\n")
						.toString();
				actorNumber++;
			}
			String messages = "";
			Integer messageNumber = 1;
			for (String sendTask : sendTaskSet) {
				idMappingSet.add(sendTask + "_ID_message_" + messageNumber);
				messageSet.add("_ID_message_" + messageNumber);
				participantICAndMessageID.add(sendTask.split("-belongsTo-")[1].split("_HH_")[0] + "_actorHasMessage_"
						+ "message_" + messageNumber);
				messages = new StringBuilder(messages)
						.append("	<owl:NamedIndividual rdf:about=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#message_"
								+ messageNumber + "\" >\r\n"
								+ "		<rdf:type rdf:resource=\"&standard-pass-ont;MessageSpec\" ></rdf:type>\r\n"
								+ "		<standard-pass-ont:hasModelComponentID rdf:datatype=\"&xsd;string\" >message_"
								+ messageNumber + "</standard-pass-ont:hasModelComponentID>\r\n"
								+ "		<standard-pass-ont:hasModelComponentLable xml:lang=\"en\" >"
								+ sendTask.split("_HH_")[1].split("_ID_")[0]
								+ "</standard-pass-ont:hasModelComponentLable>\r\n" + "	</owl:NamedIndividual>\r\n")
						.toString();
				messageNumber++;
			}

			Set<String> receivePointsSet = new HashSet<String>();
			Set<String> sendPointsSet = new HashSet<String>();
			for (String messageFlow : messageFlowSet) {
				receivePointsSet.add(messageFlow.split("_to-")[1]);
				String bpmnSenderId = messageFlow.split("_to-")[0].split("_from-")[1];
				sendPointsSet.add(bpmnSenderId);
			}

			// search in the message FlowSet for the receive points and get the send points
			String messageConnectors = "";
			Integer messageConnectorNumber = 1;
			for (String rPS : receivePointsSet) {
				sendAndReceivePointsSet.add(rPS);
			}
			for (String sPS : sendPointsSet) {
				sendAndReceivePointsSet.add(sPS);
			}

			String bpmn2_process_IC = "";
			String pRef = "";
			String bpmn2ParticipantIC = "";
			String processParticipant = "";
			String receiverActor = "";
			String senderActor = "";

			// get actors of send points
			for (String spoints : sendPointsSet) {
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node node = nodeList.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						String nodeName = node.getNodeName();
						if (nodeName == "owl:NamedIndividual") {
							NodeList childList = node.getChildNodes();
							for (int c = 0; c < childList.getLength(); c++) {
								Node childNode = childList.item(c);
								String childName = childNode.getNodeName();
								if (childName == "id") {
									if (spoints.equals(childNode.getTextContent())) {
										Node parentNode = childNode.getParentNode();
										NamedNodeMap parentAttributeList = parentNode.getAttributes();
										for (int a = 0; a < parentAttributeList.getLength(); a++) {
											bpmn2_process_IC = parentAttributeList.item(a).toString().split("\\=")[1]
													.split("-belongsTo-")[1].replaceAll("\"", "");

											// get process ref of bpmn2_process_IC
											for (String pp : bpmnProcessICSet) {
												if (pp.startsWith(bpmn2_process_IC)) {
													pRef = pp.split("==")[1];
													bpmn2ParticipantIC = pp.split("==")[0];
												}
											}
											// participant with process ref
											for (String h : processParticipantSet) {
												if (h.endsWith(pRef)) {
													processParticipant = h.split("generated/")[1]
															.split("-belongsTo-")[0];
												}
											}
											// get actor
											for (String idms : idMappingSet) {
												if (idms.contains("generated/" + processParticipant)) {
													senderActor = idms.split("_ID_")[1];
													sendActorSet.add(spoints + "_SA_" + senderActor);
													actorSet.add(senderActor);
													processICisActorXSet.add(bpmn2ParticipantIC + "_is_" + senderActor);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

			// get actors of receive points
			for (String rpoints : receivePointsSet) {
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node node = nodeList.item(i);
					String bpmn2ParticipantIC2 = "idk";
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						String nodeName = node.getNodeName();
						if (nodeName == "owl:NamedIndividual") {
							NodeList childList = node.getChildNodes();
							for (int c = 0; c < childList.getLength(); c++) {
								Node childNode = childList.item(c);
								String childName = childNode.getNodeName();
								if (childName == "id") {
									if (rpoints.equals(childNode.getTextContent())) {
										Node parentNode = childNode.getParentNode();
										NamedNodeMap parentAttributeList = parentNode.getAttributes();
										for (int a = 0; a < parentAttributeList.getLength(); a++) {
											bpmn2_process_IC = parentAttributeList.item(a).toString().split("\\=")[1]
													.split("-belongsTo-")[1].replaceAll("\"", "");
											// get process ref of bpmn2_process_IC
											for (String pp : bpmnProcessICSet) {
												if (pp.startsWith(bpmn2_process_IC)) {
													pRef = pp.split("==")[1];
													bpmn2ParticipantIC2 = pp.split("==")[0];
												}
											}
											// participant with process ref
											for (String h : processParticipantSet) {
												if (h.endsWith(pRef)) {
													processParticipant = h.split("generated/")[1]
															.split("-belongsTo-")[0];
												}
											}
											// get actor
											for (String idms : idMappingSet) {
												if (idms.contains("generated/" + processParticipant)) {
													receiverActor = idms.split("_ID_")[1];
													receiveActorSet.add(rpoints + "_SA_" + receiverActor);
													actorSet.add(receiverActor);
													processICisActorXSet
															.add(bpmn2ParticipantIC2 + "_is_" + receiverActor);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

			for (String messageFlow : receivePointsSet) {
				for (String messageFlow2 : messageFlowSet) {
					String originalId = messageFlow2.split("generated/")[1];
					String thing = messageFlow2.split("_from-")[1];
					String sender = thing.split("_to-")[0];
					String receiver = thing.split("_to-")[1];
					for (int i = 0; i < nodeList.getLength(); i++) {
						Node node = nodeList.item(i);
						if (node.getNodeType() == Node.ELEMENT_NODE) {
							String nodeName = node.getNodeName();
							if (nodeName == "owl:NamedIndividual") {
								NodeList childList = node.getChildNodes();
								for (int c = 0; c < childList.getLength(); c++) {
									Node childNode = childList.item(c);
									String childName = childNode.getNodeName();
									if (childName == "id") {
										if (receiver.equals(childNode.getTextContent())) {
											Node parentNode = childNode.getParentNode();
											NamedNodeMap parentAttributeList = parentNode.getAttributes();
											for (int a = 0; a < parentAttributeList.getLength(); a++) {
												bpmn2_process_IC = parentAttributeList.item(a).toString()
														.split("\\=")[1].split("-belongsTo-")[1].replaceAll("\"", "");
												// get process ref of bpmn2_process_IC
												for (String pp : bpmnProcessICSet) {
													if (pp.startsWith(bpmn2_process_IC)) {
														pRef = pp.split("==")[1];
													}
												}
												// participant with process ref
												for (String huch : processParticipantSet) {
													if (huch.endsWith(pRef)) {
														processParticipant = huch.split("generated/")[1]
																.split("-belongsTo-")[0];
													}
												}
												// get actor
												for (String idms : idMappingSet) {
													if (idms.contains("generated/" + processParticipant)) {
														receiverActor = idms.split("_ID_")[1];

													}
												}
											}
										}
									}
								}
							}
						}
					}

					if (receiver.startsWith(messageFlow)) {
						String to = "";
						for (String rAs : receiveActorSet) {
							if (rAs.startsWith(receiver)) {
								to = rAs.split("_SA_")[1];
							}
						}
						String from = "";
						for (String sAs : sendActorSet) {
							if (sAs.startsWith(sender)) {
								from = sAs.split("_SA_")[1];
							}
						}
						idMappingSet.add(messageFlow2 + "_ID_message_" + messageConnectorNumber);
						String ic = originalId.split("-belongsTo-")[0];
						String sbpmId = "idk";
						String messageText = "idk";
						for (String m : idMappingSet) {
							if (m.contains(ic)) {
								sbpmId = m.split("_ID_")[1];
							}
						}
						for (String m : idMappingSet) {
							if (m.contains("generated/bpmn2_sendTask_IC")
									& m.contains("message_" + messageConnectorNumber)) {
								messageText = m.split("_HH_")[1].split("_ID_")[0];
							}
						}
						messageConnectorSet
								.add("SID_0_StandardMessageConnector_" + messageConnectorNumber + "_" + sbpmId);
						// TODO message content
						messageConnectors = new StringBuilder(messageConnectors).append(
								"	<owl:NamedIndividual rdf:about=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#"
										+ "SID_0_StandardMessageConnector_" + messageConnectorNumber + "_" + sbpmId
										+ "\" >\r\n"
										+ "		<rdf:type rdf:resource=\"&standard-pass-ont;StandardMessageExchange\" ></rdf:type>\r\n"
										+ "		<standard-pass-ont:hasModelComponentID rdf:datatype=\"&xsd;string\" >"
										+ "SID_0_StandardMessageConnector_" + messageConnectorNumber + "_" + sbpmId
										+ "</standard-pass-ont:hasModelComponentID>\r\n"
										+ "		<standard-pass-ont:hasModelComponentLable xml:lang=\"en\" >\r\n"
										+ "		Message: " + messageText + ": " + from + " To: " + to
										+ "</standard-pass-ont:hasModelComponentLable>\r\n"
										+ "		<standard-pass-ont:hasSender rdf:resource=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#"
										+ from + "\" ></standard-pass-ont:hasSender>\r\n"
										+ "		<standard-pass-ont:hasReceiver rdf:resource=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#"
										+ to + "\" ></standard-pass-ont:hasReceiver>\r\n"
										+ "		<standard-pass-ont:hasMessageType rdf:resource=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#"
										+ sbpmId + "\" ></standard-pass-ont:hasMessageType>\r\n"
										+ "	</owl:NamedIndividual>\r\n")
								.toString();
						messageConnectorNumber++;
					}
				}
			}

			////////////////////////////////////////////////////////
			/////// PASSProcessModel ////////
			////////////////////////////////////////////////////////

			System.out.println("--- Create PASSProcessModel");
			String PASSProcessModel = "	<owl:NamedIndividual rdf:about=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#"
					+ fileProcessName + "\" >\r\n"
					+ "		<rdf:type rdf:resource=\"&standard-pass-ont;PASSProcessModel\" ></rdf:type>\r\n"
					+ "		<standard-pass-ont:hasModelComponentID rdf:datatype=\"&xsd;string\" >Layer_0</standard-pass-ont:hasModelComponentID>\r\n"
					+ "		<standard-pass-ont:hasModelComponentLable xml:lang=\"en\" >" + fileProcessName
					+ "</standard-pass-ont:hasModelComponentLable>\r\n"
					+ "		<standard-pass-ont:hasModelComponent rdf:resource=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#SID1\" ></standard-pass-ont:hasModelComponent>\r\n";

			// actors
			String PASSProcessModelAndSID = "";
			String standardActorComponent = "idk";
			Integer sbdCounter = 1;
			for (String node : actorSet) {
				standardActorComponent = node;
				// actor
				PASSProcessModelAndSID = new StringBuilder(PASSProcessModelAndSID)
						.append("		<standard-pass-ont:hasModelComponent "
								+ "rdf:resource=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#" + standardActorComponent+"\"></standard-pass-ont:hasModelComponent>\r\n")
						.toString();
				sbdCounter++;
			}

			// message components
			String messageComponent = "idk";
			for (String node : messageSet) {
				messageComponent = node.split("_ID_")[1];
				PASSProcessModelAndSID = new StringBuilder(PASSProcessModelAndSID)
						.append("		<standard-pass-ont:hasModelComponent rdf:resource=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#" + messageComponent+"\"></standard-pass-ont:hasModelComponent>\r\n")
						.toString();

			}

			// message connectors
			String messageConnectorComponent = "idk";
			for (String node : messageConnectorSet) {
				messageConnectorComponent = node;
				PASSProcessModelAndSID = new StringBuilder(PASSProcessModelAndSID)
						.append("		<standard-pass-ont:hasModelComponent rdf:resource=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#"+messageConnectorComponent + "\" ></standard-pass-ont:hasModelComponent>\r\n")
						.toString();
			}

			// sbd actors
			String standardActorComponent2 = "idk";
			Integer sbdCounter2 = 1;
			for (String node : actorSet) {
				standardActorComponent2 = node;
				// actor sbd
				PASSProcessModelAndSID = new StringBuilder(PASSProcessModelAndSID)
						.append("		<standard-pass-ont:hasModelComponent rdf:resource=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#" + "SBD_" + sbdCounter2+ "_" + standardActorComponent2 + "\" ></standard-pass-ont:hasModelComponent>\r\n")
						.toString();
				sbdCounter2++;
			}

			////////////////////////////////////////////////////////
			/////// SID ////////
			////////////////////////////////////////////////////////

			String SID1 = "	<owl:NamedIndividual rdf:about=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#SID1\" >\r\n"
					+ "		<rdf:type rdf:resource=\"&standard-pass-ont;Layer\" ></rdf:type>\r\n"
					+ "		<standard-pass-ont:hasModelComponentID rdf:datatype=\"&xsd;string\" >SID1</standard-pass-ont:hasModelComponentID>\r\n"
					+ "		<standard-pass-ont:hasModelComponentLable xml:lang=\"en\" >SID1</standard-pass-ont:hasModelComponentLable>\r\n";

			PASSProcessModel = new StringBuilder(PASSProcessModel).append(PASSProcessModelAndSID).toString();
			PASSProcessModel = new StringBuilder(PASSProcessModel).append("	</owl:NamedIndividual>").toString();
			writer.println(PASSProcessModel);

			SID1 = new StringBuilder(SID1).append(PASSProcessModelAndSID).toString();
			SID1 = new StringBuilder(SID1).append("	</owl:NamedIndividual>").toString();
			writer.println(SID1);

			writer.println(actors);
			writer.println(messages);
			writer.println(messageConnectors);

			////////////////////////////////////////////////////////
			/////// endStates ////////
			////////////////////////////////////////////////////////
			System.out.println("--- Create SBD for each actor with all states and transitions");
			String functionState = "";
			Integer functionStateNumber = 1;
			for (String ees : endEventSet) {
				String sbpmActorId = "idk";
				String bpmn2processIC = ees.split("-belongsTo-")[1].split("_HH_")[0];
				for (String bpis : processICisActorXSet) {
					if (bpis.startsWith(bpmn2processIC)) {
						sbpmActorId = bpis.split("_is_")[1].split("_StandardActor_")[1];
					}
				}
				String endEventText = (ees.split("_HH_")[1].split("_ID_")[0]);
				String endEventID = (ees.split("_HH_")[1].split("_ID_")[1]);
				functionState = new StringBuilder(functionState)
						.append("	<owl:NamedIndividual rdf:about=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#" + "SBD_"
								+ sbpmActorId + "_FunctionState_" + functionStateNumber + "\"" + " >\r\n"
								+ "		<rdf:type rdf:resource=\"&standard-pass-ont;FunctionState\" ></rdf:type>\r\n"
								+ "		<standard-pass-ont:hasModelComponentID rdf:datatype=\"&xsd;string\" >" + "SBD_"
								+ sbpmActorId + "_FunctionState_" + functionStateNumber
								+ "</standard-pass-ont:hasModelComponentID>\r\n"
								+ "		<standard-pass-ont:hasModelComponentLable xml:lang=\"en\" >" + endEventText
								+ "</standard-pass-ont:hasModelComponentLable>\r\n" + "	</owl:NamedIndividual>\r\n")
						.toString();

				stateICandIDmapping.add(
						endEventID + "_stateMapping_" + "SBD_" + sbpmActorId + "_FunctionState_" + functionStateNumber);
				statesAndTransitionsForSBDSet.add("SBD_" + sbpmActorId + "_FunctionState_" + functionStateNumber);
				functionStateNumber++;
			}
			writer.println(functionState);

			////////////////////////////////////////////////////////
			/////// gatewayStates ////////
			////////////////////////////////////////////////////////
			String gatewayState = "";
			for (String gs : gatewaySet) {
				String sbpmActorId = "idk";
				String bpmn2processIC = gs.split("-belongsTo-")[1].split("_HH_")[0];
				for (String bpis : processICisActorXSet) {
					if (bpis.startsWith(bpmn2processIC)) {
						sbpmActorId = bpis.split("_is_")[1].split("_StandardActor_")[1];
					}
				}
				String gwText = (gs.split("_HH_")[1].split("_ID_")[0]);
				String gwID = (gs.split("_HH_")[1].split("_ID_")[1]);
				functionStateAndMessageSet
						.add("SBD_" + sbpmActorId + "_FunctionState_" + functionStateNumber + "_hasMessage_" + gwText);
				idMappingSet.add(gs + "SBD_" + sbpmActorId + "_FunctionState_" + functionStateNumber);
				gatewayState = new StringBuilder(gatewayState)
						.append("	<owl:NamedIndividual rdf:about=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#" + "SBD_"
								+ sbpmActorId + "_FunctionState_" + functionStateNumber + "\"" + " >\r\n"
								+ "		<rdf:type rdf:resource=\"&standard-pass-ont;FunctionState\" ></rdf:type>\r\n"
								+ "		<standard-pass-ont:hasModelComponentID rdf:datatype=\"&xsd;string\" >" + "SBD_"
								+ sbpmActorId + "_FunctionState_" + functionStateNumber
								+ "</standard-pass-ont:hasModelComponentID>\r\n"
								+ "		<standard-pass-ont:hasModelComponentLable xml:lang=\"en\" >" + gwText
								+ "</standard-pass-ont:hasModelComponentLable>\r\n" + "	</owl:NamedIndividual>\r\n")
						.toString();
				stateICandIDmapping
						.add(gwID + "_stateMapping_" + "SBD_" + sbpmActorId + "_FunctionState_" + functionStateNumber);
				statesAndTransitionsForSBDSet.add("SBD_" + sbpmActorId + "_FunctionState_" + functionStateNumber);
				functionStateNumber++;
			}
			writer.println(gatewayState);
			
			//taskStates
			
			////////////////////////////////////////////////////////
			/////// gatewayStates ////////
			////////////////////////////////////////////////////////

			String taskState = "";
			for (String ts : taskSet) {
				String sbpmActorId = "idk";
				String bpmn2processIC = ts.split("-belongsTo-")[1].split("_HH_")[0];
				for (String bpis : processICisActorXSet) {
					if (bpis.startsWith(bpmn2processIC)) {
						sbpmActorId = bpis.split("_is_")[1].split("_StandardActor_")[1];
					}
				}
				String gwText = (ts.split("_HH_")[1].split("_ID_")[0]);
				String gwID = (ts.split("_HH_")[1].split("_ID_")[1]);
				functionStateAndMessageSet
						.add("SBD_" + sbpmActorId + "_FunctionState_" + functionStateNumber + "_hasMessage_" + gwText);
				idMappingSet.add(ts + "SBD_" + sbpmActorId + "_FunctionState_" + functionStateNumber);
				taskState = new StringBuilder(taskState)
						.append("	<owl:NamedIndividual rdf:about=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#" + "SBD_"
								+ sbpmActorId + "_FunctionState_" + functionStateNumber + "\"" + " >\r\n"
								+ "		<rdf:type rdf:resource=\"&standard-pass-ont;FunctionState\" ></rdf:type>\r\n"
								+ "		<standard-pass-ont:hasModelComponentID rdf:datatype=\"&xsd;string\" >" + "SBD_"
								+ sbpmActorId + "_FunctionState_" + functionStateNumber
								+ "</standard-pass-ont:hasModelComponentID>\r\n"
								+ "		<standard-pass-ont:hasModelComponentLable xml:lang=\"en\" >" + gwText
								+ "</standard-pass-ont:hasModelComponentLable>\r\n" + "	</owl:NamedIndividual>\r\n")
						.toString();
				stateICandIDmapping
						.add(gwID + "_stateMapping_" + "SBD_" + sbpmActorId + "_FunctionState_" + functionStateNumber);
				statesAndTransitionsForSBDSet.add("SBD_" + sbpmActorId + "_FunctionState_" + functionStateNumber);
				functionStateNumber++;
			}
			writer.println(taskState);
			
			////////////////////////////////////////////////////////
			/////// sendStates ////////
			////////////////////////////////////////////////////////
			String sendStates = "";
			Integer sendStateNumber = 1;
			for (String sts : sendTaskSet) {
				String sbpmActorId = "idk";
				String bpmn2processIC = sts.split("-belongsTo-")[1].split("_HH_")[0];
				for (String bpis : processICisActorXSet) {
					if (bpis.startsWith(bpmn2processIC)) {
						sbpmActorId = bpis.split("_is_")[1].split("_StandardActor_")[1];
					}
				}
				String sendText = (sts.split("_HH_")[1].split("_ID_")[0]);
				String sendID = (sts.split("_HH_")[1].split("_ID_")[1]);
				sendStateAndMessageSet
						.add("SBD_" + sbpmActorId + "_SendState_" + sendStateNumber + "_hasMessage_" + sendText);
				sendStates = new StringBuilder(sendStates)
						.append("	<owl:NamedIndividual rdf:about=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#" + "SBD_"
								+ sbpmActorId + "_SendState_" + sendStateNumber + "\" >\r\n"
								+ "		<rdf:type rdf:resource=\"&standard-pass-ont;SendState\" ></rdf:type>\r\n"
								+ "		<standard-pass-ont:hasModelComponentID rdf:datatype=\"&xsd;string\" >" + "SBD_"
								+ sbpmActorId + "_SendState_" + sendStateNumber
								+ "</standard-pass-ont:hasModelComponentID>\r\n"
								+ "		<standard-pass-ont:hasModelComponentLable xml:lang=\"en\" >" + sendText
								+ "</standard-pass-ont:hasModelComponentLable>\r\n" + "	</owl:NamedIndividual>\r\n")
						.toString();
				stateICandIDmapping
						.add(sendID + "_stateMapping_" + "SBD_" + sbpmActorId + "_SendState_" + sendStateNumber);
				statesAndTransitionsForSBDSet.add("SBD_" + sbpmActorId + "_SendState_" + sendStateNumber);
				sendStateNumber++;

			}
			writer.println(sendStates);

			////////////////////////////////////////////////////////
			/////// receiveStates ////////
			////////////////////////////////////////////////////////

			String receiveState = "";
			Integer receiveStateNumber = 1;
			for (String rts : receiveTaskSet) {
				String sbpmActorId = "idk";
				String bpmn2processIC = rts.split("-belongsTo-")[1].split("_HH_")[0];
				for (String bpis : processICisActorXSet) {
					if (bpis.startsWith(bpmn2processIC)) {
						sbpmActorId = bpis.split("_is_")[1].split("_StandardActor_")[1];
					}
				}
				String receiveText = (rts.split("_HH_")[1].split("_ID_")[0]);
				String receiveID = (rts.split("_HH_")[1].split("_ID_")[1]);
				String receiveStateID = "SBD_" + sbpmActorId + "_ReceiveState_" + receiveStateNumber;
				receiveStateAndMessageSet.add(
						"SBD_" + sbpmActorId + "_ReceiveState_" + receiveStateNumber + "_hasMessage_" + receiveText);
				receiveState = new StringBuilder(receiveState)
						.append("	<owl:NamedIndividual rdf:about=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#"
								+ "SBD_" + sbpmActorId + "_ReceiveState_" + receiveStateNumber + "\" >\r\n"
								+ "		<rdf:type rdf:resource=\"&standard-pass-ont;ReceiveState\" ></rdf:type>\r\n"
								+ "		<standard-pass-ont:hasModelComponentID rdf:datatype=\"&xsd;string\" >" + "SBD_"
								+ sbpmActorId + "_ReceiveState_" + receiveStateNumber
								+ "</standard-pass-ont:hasModelComponentID>\r\n"
								+ "		<standard-pass-ont:hasModelComponentLable xml:lang=\"en\" >" + receiveText
								+ "</standard-pass-ont:hasModelComponentLable>\r\n"
								+ "		<rdf:type rdf:resource=\"&standard-pass-ont;InitialState\" ></rdf:type>\r\n"
								+ "	</owl:NamedIndividual>\r\n")
						.toString();
				stateICandIDmapping.add(receiveID + "_stateMapping_" + receiveStateID);
				statesAndTransitionsForSBDSet.add(receiveStateID);
				receiveStateNumber++;
			}
			writer.println(receiveState);

			///////////////////////////////////////////////////////////////////////////////////////
			//////////// SEND TRANSITION //////////////////////////////////////////////////////////
			///////////////////////////////////////////////////////////////////////////////////////
			String currentSendTransition = "";
			for (String stst : sourceAndTargetOfSendTransition) {
				String hasSourceState = stst.split("_transitionTo_")[0];
				String hasTargetState = stst.split("_transitionTo_")[1];
				for (String ims : idMappingSet) {
					if (ims.contains("_from-")) {
						String hasSourceState2 = ims.split("_from-")[1].split("_to-")[0];
						if (hasSourceState.equals(hasSourceState2)) {
							String refersTo = ims.split("_ID_")[1];
							for (String ssstamt : sourceStateSendTaskAndMessageText) {
								String source = ssstamt.split("_sourceMessage_")[0];
								String message1 = ssstamt.split("_sourceMessage_")[1];
								if (hasSourceState.equals(source)) {
									for (String mcs : messageConnectorSet) {
										if (mcs.endsWith(refersTo)) {
											for (String ssms : sendStateAndMessageSet) {
												if (ssms.contains(message1)) {
													String message12 = message1;
													String fromSource = source;
													String toTarget = hasTargetState;
													String connector = mcs;
													String sendTransition = "";
													Integer sendTransitionNumber = 1;
													for (String ssmss : sendStateAndMessageSet) {
														String hasModelComponentID = ssmss.split("_SendState_")[0]
																+ "_SendTransition_" + sendTransitionNumber;
														String to = ssmss.split("_SendState_")[0];
														String msg = ssmss.split("_hasMessage_")[1];
														if (msg.equals(message12)) {
															for (String siaim : stateICandIDmapping) {
																// find id of state with id of bpmn element
																String currentBPMNelementID = siaim
																		.split("_stateMapping_")[0];
																String currentStateID = siaim
																		.split("_stateMapping_")[1];
																if (fromSource.equals(currentBPMNelementID)) {
																	fromSource = currentStateID;
																}
																if (toTarget.equals(currentBPMNelementID)) {
																	toTarget = currentStateID;
																}
															}
															currentSendTransition = new StringBuilder(sendTransition)
																	.append("	<owl:NamedIndividual rdf:about=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#"
																			+ hasModelComponentID + "\" >\r\n"
																			+ "		<rdf:type rdf:resource=\"&standard-pass-ont;SendTransition\" ></rdf:type>\r\n"
																			+ "		<standard-pass-ont:hasModelComponentID rdf:datatype=\"&xsd;string\" >"
																			+ hasModelComponentID
																			+ "</standard-pass-ont:hasModelComponentID>\r\n"
																			+ "		<standard-pass-ont:hasModelComponentLable xml:lang=\"en\" >To: "
																			+ to + "Msg: " + message12
																			+ " (Min: ) (Max: )</standard-pass-ont:hasModelComponentLable>\r\n"
																			+ "		<standard-pass-ont:hasSourceState rdf:resource=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#"
																			+ fromSource
																			+ "\" ></standard-pass-ont:hasSourceState>\r\n"
																			+ "		<standard-pass-ont:hasTargetState rdf:resource=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#"
																			+ toTarget
																			+ "\" ></standard-pass-ont:hasTargetState>\r\n"
																			+ "		<standard-pass-ont:hasAlternativePriorityNumber >0</standard-pass-ont:hasAlternativePriorityNumber>\r\n"
																			+ "		<standard-pass-ont:refersTo rdf:resource=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#"
																			+ connector
																			+ "\" ></standard-pass-ont:refersTo>\r\n"
																			+ "	</owl:NamedIndividual>\r\n")
																	.toString();
															writer.println(currentSendTransition);
														}

														statesAndTransitionsForSBDSet.add(hasModelComponentID);
														sendTransitionNumber++;
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

			///////////////////////////////////////////////////////////////////////////////////////
			//////////// Receive TRANSITION
			/////////////////////////////////////////////////////////////////////////////////////// //////////////////////////////////////////////////////////
			///////////////////////////////////////////////////////////////////////////////////////
			String currentReceiveTransition = "";
			for (String stst : sourceAndTargetOfReceiveTransition) {
				String hasSourceState = stst.split("_transitionTo_")[0];
				String hasTargetState = stst.split("_transitionTo_")[1];
				for (String ims : idMappingSet) {
					if (ims.contains("ReceiveTask")) {
						String hasSourceState2 = ims.split("_to-")[1].split("_ID_")[0];
						if (hasSourceState.equals(hasSourceState2)) {
							String refersTo = ims.split("_ID_")[1];
							for (String ssstamt : sourceStateReceiveTaskAndMessageText) {
								String source = ssstamt.split("_sourceMessage_")[0];
								String message1 = ssstamt.split("_sourceMessage_")[1];
								if (hasSourceState.equals(source)) {
									for (String mcs : messageConnectorSet) {
										if (mcs.endsWith(refersTo)) {
											for (String ssms : receiveStateAndMessageSet) {
												if (ssms.contains(message1)) {
													String message12 = message1;
													String fromSource = source;
													String toTarget = hasTargetState;
													String connector = mcs;
													String sendTransition = "";
													Integer sendTransitionNumber = 1;
													for (String ssmss : receiveStateAndMessageSet) {
														String hasModelComponentID = ssmss.split("_ReceiveState_")[0]
																+ "_ReceiveTransition_" + sendTransitionNumber;
														String msg = ssmss.split("_hasMessage_")[1];
														if (msg.equals(message12)) {
															for (String siaim : stateICandIDmapping) {
																// find id of state with id of bpmn element
																String currentBPMNelementID = siaim
																		.split("_stateMapping_")[0];
																String currentStateID = siaim
																		.split("_stateMapping_")[1];
																if (fromSource.equals(currentBPMNelementID)) {
																	fromSource = currentStateID;
																}
																if (toTarget.equals(currentBPMNelementID)) {
																	toTarget = currentStateID;
																}
															}
															currentReceiveTransition = new StringBuilder(sendTransition)
																	.append("	<owl:NamedIndividual rdf:about=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#"
																			+ hasModelComponentID + "\" >\r\n"
																			+ "		<rdf:type rdf:resource=\"&standard-pass-ont;ReceiveTransition\" ></rdf:type>\r\n"
																			+ "		<standard-pass-ont:hasModelComponentID rdf:datatype=\"&xsd;string\" >"
																			+ hasModelComponentID
																			+ "</standard-pass-ont:hasModelComponentID>\r\n"
																			+ "		<standard-pass-ont:hasModelComponentLable xml:lang=\"en\" >From: "+ "Msg: " + message12
																			+ " (Min: ) (Max: )</standard-pass-ont:hasModelComponentLable>\r\n"
																			+ "		<standard-pass-ont:hasSourceState rdf:resource=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#"
																			+ fromSource
																			+ "\" ></standard-pass-ont:hasSourceState>\r\n"
																			+ "		<standard-pass-ont:hasTargetState rdf:resource=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#"
																			+ toTarget
																			+ "\" ></standard-pass-ont:hasTargetState>\r\n"
																			+ "		<standard-pass-ont:hasAlternativePriorityNumber >0</standard-pass-ont:hasAlternativePriorityNumber>\r\n"
																			+ "		<standard-pass-ont:refersTo rdf:resource=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#"
																			+ connector
																			+ "\" ></standard-pass-ont:refersTo>\r\n"
																			+ "	</owl:NamedIndividual>\r\n")
																	.toString();
															writer.println(currentReceiveTransition);
														}
														statesAndTransitionsForSBDSet.add(hasModelComponentID);
														sendTransitionNumber++;
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			//////////////////////////////////////////////////////////////////////////////////////
			//////////// Standard
			////////////////////////////////////////////////////////////////////////////////////// TRANSITION//////////////////////////////////////////////////////
			//////////////////////////////////////////////////////////////////////////////////////
			String currentStanardTransition = "";
			Integer standardTransitionNumber = 1;
		
			for (String we : sourceAndTargetOfStandardTransition) {
				System.out.println("we "+we);
				String hasModelComponentID = "_StandardTransition_" + standardTransitionNumber;
				String label = "";
				String fromSource = we.split("_transitionTo_")[0];
				String toTarget = we.split("_transitionTo_")[1];
				for (String xx : stateICandIDmapping) {
					if (xx.startsWith(fromSource)) {
						fromSource = xx.split("_stateMapping_")[1];
					}
					if (xx.contains(toTarget)) {
						toTarget = xx.split("_stateMapping_")[1];
					}
				}
				// TODO add label
				currentStanardTransition = new StringBuilder(currentStanardTransition)
						.append("	<owl:NamedIndividual rdf:about=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#"
								+ hasModelComponentID + "\" >\r\n"
								+ "		<rdf:type rdf:resource=\"&standard-pass-ont;StandardTransition\" ></rdf:type>\r\n"
								+ "		<standard-pass-ont:hasModelComponentID rdf:datatype=\"&xsd;string\" >"
								+ hasModelComponentID + "</standard-pass-ont:hasModelComponentID>\r\n"
								+ "		<standard-pass-ont:hasModelComponentLable xml:lang=\"en\" >" + label
								+ "</standard-pass-ont:hasModelComponentLable>\r\n"
								+ "		<standard-pass-ont:hasSourceState rdf:resource=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#"
								+ fromSource + "\" ></standard-pass-ont:hasSourceState>\r\n"
								+ "		<standard-pass-ont:hasTargetState rdf:resource=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#"
								+ toTarget + "\" ></standard-pass-ont:hasTargetState>\r\n"
								+ "		<standard-pass-ont:hasAlternativePriorityNumber >0</standard-pass-ont:hasAlternativePriorityNumber>\r\n"
								+ "	</owl:NamedIndividual>\r\n")
						.toString();
				statesAndTransitionsForSBDSet.add(hasModelComponentID);
				standardTransitionNumber++;
			}
			writer.println(currentStanardTransition);

			//////////////////////////////////////////////////////////////////////////////////////
			//////////// Sbd
			////////////////////////////////////////////////////////////////////////////////////// /////////////////////////////////////////////////////////////////////
			//////////////////////////////////////////////////////////////////////////////////////
			String SBDstringBuilder = "";
			for (String sbd : componentsForSBDSet) {
				if (sbd.startsWith("SBD")) {
					String hasName = sbd.split("_hasName_")[1].split("_hasNumber_")[0];
					String hasNumber = sbd.split("_hasName_")[1].split("_hasNumber_")[1];
					String hasModelComponentID = "SBD_" + hasNumber + "_SID_0_StandardActor_" + hasNumber;
					String hasModelComponentLable ="SBD: "+hasName;
					SBDstringBuilder = new StringBuilder(SBDstringBuilder).append("	<owl:NamedIndividual rdf:about=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#"+hasModelComponentID+"\" >\r\n" + 
							"		<rdf:type rdf:resource=\"&standard-pass-ont;Behavior\" ></rdf:type>\r\n" + 
							"		<standard-pass-ont:hasModelComponentID rdf:datatype=\"&xsd;string\" >"+hasModelComponentID+"</standard-pass-ont:hasModelComponentID>\r\n" + 
							"		<standard-pass-ont:hasModelComponentLable xml:lang=\"en\" >"+hasModelComponentLable+"</standard-pass-ont:hasModelComponentLable>\r\n").toString();;
					for (String st : statesAndTransitionsForSBDSet) {
						if (st.startsWith("SBD_" + hasNumber)) {
							
							//TODO find initial state
							if (st.contains("Initial")) {
								String hasInitialState = "idk";
								SBDstringBuilder = new StringBuilder(SBDstringBuilder).append("		<standard-pass-ont:hasInitialState rdf:resource=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#"+hasInitialState+"\" ></standard-pass-ont:hasInitialState>\r\n").toString();;
							}
							if (st.contains("Transition")) {
								String hasEdge = st;
								SBDstringBuilder = new StringBuilder(SBDstringBuilder).append("		<standard-pass-ont:hasEdge rdf:resource=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#"+hasEdge+"\" ></standard-pass-ont:hasEdge>\r\n").toString();;
							}
							if (st.contains("State")) {
								String hasState = st;
								SBDstringBuilder = new StringBuilder(SBDstringBuilder).append("		<standard-pass-ont:hasState rdf:resource=\"http://www.imi.kit.edu/s-bpm/"+fileProcessName+"#"+hasState+"\" ></standard-pass-ont:hasState>\r\n").toString();;
							}
						}
					}
					SBDstringBuilder = new StringBuilder(SBDstringBuilder).append("	</owl:NamedIndividual>\r\n").toString();;
				}
			}
			writer.println(SBDstringBuilder);
			writer.println("</rdf:RDF>\r\n");
			writer.close();
			System.out.println("--- S-BPM Ontology created");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}