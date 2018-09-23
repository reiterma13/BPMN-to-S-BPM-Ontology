# BPMN-to-S-BPM-Ontology
This is a proposal for the transformation from BPMN to S-BPM OWL and contains a limited Set of BPMN elements, which are allowed. The prototype BPMN2SBPM is an executeable JAR file, which takes a BPMN file as input and generates a S-BPM OWL file. The following list shows the BPMN elements, which are included in this prototype:

- bpmn2:collaboration
- bpmn2:definitions
- bpmn2:endEvent
- bpmn2:exclusiveGateway
- bpmn2:incoming
- bpmn2:messageEventDefinition
- bpmn2:messageFlow
- bpmn2:outgoing
- bpmn2:participant
- bpmn2:process
- bpmn2:receiveTask
- bpmn2:sendTask
- bpmn2:sequenceFlow
- bpmn2:startEvent
- bpmn2:task
- bpmn2:userTask
- bpmndi:BPMNDiagram
- bpmndi:BPMNEdge
- bpmndi:BPMNLabel
- bpmndi:BPMNLabelStyle
- bpmndi:BPMNPlane
- bpmndi:BPMNShape
- dc:Bounds
- dc:Font
- di:waypoint

-----------------------------------------------------------------------------------------------------
Installation and Configuration 
-----------------------------------------------------------------------------------------------------

JDK
- http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
- jdk-8u151-windows-x64.exe
- Set the system variables: JAVA_HOME und Path 

Eclipse
- https://www.eclipse.org/downloads/download.php?file=/oomph/epp/oxygen/R/eclipse-inst-win64.exe
- Eclipse IDE for Java Developers

BPMN2-Modeler
- Eclipse Marketplace
- Eclipse BPMN2 Modeler 1.4.1

OWL API
- https://github.com/owlcs/owlapi
- Import existing maven project

OWL
- Create Java Project
- Configure Build Path -> Projects -> Add all owlapi projects
- Run Configuration -> Classpath -> JRE System Library [JavaSE-1.8] und OWL
- Download Folder 4 from https://github.com/owlcs/releases/tree/master/owlapi
- and add external JAR Files from releases-master version 4

JDOM
- http://www.jdom.org/downloads/
- Add external JAR Files

Protege
- https://protege.stanford.edu/products.php#desktop-protege
- Program to view ontologies.

Graphviz
- https://graphviz.gitlab.io/_pages/Download/Download_windows.html
- OWL Viz uses Ghraphviz to display classes.

OWL VIZ
- Is a Protege Plugin and depends on Graphviz.
- Protege: File -> Preferences -> OWL Viz -> Path to DOT ..\graphviz-2.38\release\bin\dot.exe

-----------------------------------------------------------------------------------------------------
Execution of BPMN2SBPM.jar
-----------------------------------------------------------------------------------------------------

1. Download the file BPMN2SBPM.jar.
2. Copy the BPMN file to the same filepath.
3. Use CMD to naviagte to the jar location: cd filepath
4. Use the following command to execute the jar file: java -jar BPMN2SBPM.jar
5. Enter the bpmn file name: test.bpmn
6. View Generated-S-BPM-OWL.owl in logs.

