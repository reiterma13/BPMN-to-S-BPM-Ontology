import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdom2.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Reiter
 *
 */
public class bpmn_element_validation {

	/**
	 * bpmn_element_validation reads a single bpmn input file
	 * and checks if it only contains allowed bpmn elements 
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	
	public static void main(NodeList nodeList) throws ParserConfigurationException, SAXException, IOException{
	
		//PrintWriter
		PrintWriter writer = new PrintWriter("logs/ValidationOfLimitedBpmnElements.txt", "UTF-8");
		writer.println("The BPMN diagram contains the following nodes:\r\n");
		
		//Current BPMN Nodes
	    ArrayList<String> currentElements = new ArrayList<String>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {								
				if(currentElements.contains(node.getNodeName())) {
				}else {
					currentElements.add(node.getNodeName());
				}
			}
		}
		sortStringSet(currentElements).forEach(writer::println);

		//Allowed BPMN Subset
		writer.println("\r\n The following BPMN elements are allowed: \r\n");
		
		String element1 = "bpmn2:collaboration";
		String element2 = "bpmn2:definitions";
		String element3 = "bpmn2:endEvent";
		String element4 = "bpmn2:exclusiveGateway";
		String element5 = "bpmn2:incoming";
		String element6 = "bpmn2:messageEventDefinition";
		String element7 = "bpmn2:messageFlow";
		String element8 = "bpmn2:outgoing";
		String element9 = "bpmn2:participant";
		String element10 = "bpmn2:process";
		String element11 = "bpmn2:receiveTask";
		String element12 = "bpmn2:sendTask";
		String element13 = "bpmn2:sequenceFlow";
		String element14 = "bpmn2:startEvent";
		String element15 = "bpmn2:task";
		String element16 = "bpmn2:userTask";
		String element17 = "bpmndi:BPMNDiagram";
		String element18 = "bpmndi:BPMNEdge";
		String element19 = "bpmndi:BPMNLabel";
		String element20 = "bpmndi:BPMNLabelStyle";
		String element21 = "bpmndi:BPMNPlane";
		String element22 = "bpmndi:BPMNShape";
		String element23 = "dc:Bounds";
		String element24 = "dc:Font";
		String element25 = "di:waypoint";

	    ArrayList<String> allowedElements = new ArrayList<String>();
	    
	    allowedElements.add(element1);
	    allowedElements.add(element2);
	    allowedElements.add(element3);
	    allowedElements.add(element4);
	    allowedElements.add(element5);
	    allowedElements.add(element6);
	    allowedElements.add(element7);
	    allowedElements.add(element8);
	    allowedElements.add(element9);
	    allowedElements.add(element10);
	    allowedElements.add(element11);
	    allowedElements.add(element12);
	    allowedElements.add(element13);
	    allowedElements.add(element14);
	    allowedElements.add(element15);
	    allowedElements.add(element16);
	    allowedElements.add(element17);
	    allowedElements.add(element18);
	    allowedElements.add(element19);
	    allowedElements.add(element20);
	    allowedElements.add(element21);
	    allowedElements.add(element22);
	    allowedElements.add(element23);
	    allowedElements.add(element24);
	    allowedElements.add(element25);

		writer.println("\r\nThe following elements are allowed:\r\n");
		sortStringSet(allowedElements).forEach(writer::println);

		//List of illegal nodes
		
	    ArrayList<String> illegalElements = new ArrayList<String>();
	    for(String s:currentElements) {
			if(allowedElements.contains(s)) {
			}
			else {
				illegalElements.add(s);
			}
	    }

		writer.println("\r\nThe following elements are not allowed: \r\n");
		sortStringSet(illegalElements).forEach(writer::println);
		
		//close writer
		writer.close();
		
		//TODO: only transform owls if illegalElements is null

		
	}	
	
	private static List<String> sortStringSet(Collection<String> unsortedList) {
		List<String> sortedList = new ArrayList<String>(unsortedList);
		Collections.sort(sortedList);
		return sortedList;
	}
	
}
