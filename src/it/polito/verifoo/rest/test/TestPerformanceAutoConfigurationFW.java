/**
 * 
 */
package it.polito.verifoo.rest.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.microsoft.z3.Status;

import it.polito.verifoo.rest.common.BadGraphError;
import it.polito.verifoo.rest.common.Translator;
import it.polito.verifoo.rest.common.VerifooProxy;
import it.polito.verifoo.rest.common.VerifooSerializer;
import it.polito.verifoo.rest.jaxb.FunctionalTypes;
import it.polito.verifoo.rest.jaxb.Graph;
import it.polito.verifoo.rest.jaxb.Host;
import it.polito.verifoo.rest.jaxb.NFV;
import it.polito.verifoo.rest.jaxb.Path;
import it.polito.verifoo.rest.jaxb.Property;
import it.polito.verifoo.rest.jaxb.TypeOfHost;
import it.polito.verigraph.mcnet.components.IsolationResult;
/**
 * 
 * This class runs some tests to collect some data about the performance of the tool 
 *
 */
public class TestPerformanceAutoConfigurationFW {
	private long condTime = 0, checkTimeSAT = 0, checkTimeUNSAT = 0, totTime = 0;
	private long maxCondTime = 0, maxCheckTimeSAT = 0, maxCheckTimeUNSAT = 0, maxTotTime = 0;
	private int nSAT = 0, nUNSAT = 0, i = 0, err = 0, nrOfConditions = 0, maxNrOfConditions = 0;
	NFV root;
	private List<Host> pastClients = new ArrayList<>(), pastServers = new ArrayList<>();
	private Logger logger = LogManager.getLogger("mylog");
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	private NFV init(NFV root) throws JAXBException, SAXException, FileNotFoundException{
		List<Path> paths = null;
		if(root.getNetworkForwardingPaths() != null)
			paths = root.getNetworkForwardingPaths().getPath();
        for(Graph g:root.getGraphs().getGraph()){
        	long beginVP=System.currentTimeMillis();
        	List<Property> prop = root.getPropertyDefinition().getProperty().stream().filter(p -> p.getGraph()==g.getId()).collect(Collectors.toList());
        	VerifooProxy test = new VerifooProxy(g, root.getHosts(), root.getConnections(),root.getConstraints(), prop, paths);
        	long endVP=System.currentTimeMillis();
        	condTime += (endVP-beginVP);
        	maxCondTime = maxCondTime<(endVP-beginVP)? (endVP-beginVP) : condTime;
            //System.out.println("Graph " + g.getId() + ": creating condition -> " + ((endVP-beginVP)) + "ms");
        	IsolationResult res=test.checkNFFGProperty();
        	nrOfConditions += test.getNrOfConditions();
        	maxNrOfConditions = maxNrOfConditions<test.getNrOfConditions()? test.getNrOfConditions() : maxNrOfConditions;
        	long endCheck=System.currentTimeMillis();
            //System.out.println("Graph " + g.getId() + ": checking property -> " + ((endCheck-endVP)) + "ms");
        	if(res.result != Status.UNSATISFIABLE){
            	checkTimeSAT += (endCheck-endVP);
            	maxCheckTimeSAT = maxCheckTimeSAT<(endCheck-endVP)? (endCheck-endVP) : maxCheckTimeSAT;
        		nSAT++;
        		new Translator(res.model.toString(),root,g).convert();
        	}
        	else{
        		checkTimeUNSAT += (endCheck-endVP);
        		maxCheckTimeUNSAT = maxCheckTimeUNSAT<(endCheck-endVP)? (endCheck-endVP) : maxCheckTimeUNSAT;
        		nUNSAT++;
        	}
        	root.getPropertyDefinition().getProperty().stream().filter(p->p.getGraph()==g.getId()).forEach(p -> p.setIsSat(res.result!=Status.UNSATISFIABLE)); 
        	//long endT=System.currentTimeMillis();
            //System.out.println(g.getId() + ": translating model -> " + ((endT-endCheck)/1000) + "s");
        }
		return root;
		
	}
	
	private void testCoarse(NFV root) throws Exception{
		//System.out.println("===========FILE " + file + "===========");
		long beginAll=System.currentTimeMillis();
		VerifooSerializer test = new VerifooSerializer(root);
		long endAll=System.currentTimeMillis();
		 if(test.isSat()){
			nSAT++;
			maxTotTime = maxTotTime<(endAll-beginAll)? (endAll-beginAll) : maxTotTime;
			System.out.print("time: " + (endAll-beginAll) + "ms;");
			totTime += (endAll-beginAll);
		 }
	 	else{
	 		System.out.print("UNSAT");	
			nUNSAT++;
	 	}
		
        return;
	}
	
	private void test(NFV root) throws Exception{
        //System.out.println("===========FILE " + file + "===========");
		long beginAll=System.currentTimeMillis();
		NFV rootTest = init(root);
		long endAll=System.currentTimeMillis();
        //System.out.println("Total time -> " + ((endAll-beginAll)/1000) + "s");
		Property p = rootTest.getPropertyDefinition().getProperty().get(0);
    	if(p.isIsSat()){
			maxTotTime = maxTotTime<(endAll-beginAll)? (endAll-beginAll) : maxTotTime;
			System.out.print("time: " + (endAll-beginAll) + "ms;");
			totTime += (endAll-beginAll);
    	}
        return;
	}
	
	private Host changeFixedClient(List<Host> hosts, String client){
		Host currClient = hosts.stream().filter(h -> h.getType().equals(TypeOfHost.CLIENT) && h.getFixedEndpoint().equals(client) ).findAny().orElse(null);

		pastClients.add(currClient);
		Host newClient = hosts.stream().filter(h -> !pastClients.contains(h) && h.getType().equals(TypeOfHost.MIDDLEBOX)).findAny().orElse(null);
		if(newClient != null){
			currClient.setType(TypeOfHost.MIDDLEBOX);
			currClient.setFixedEndpoint(null);
			//System.out.println("Host client changed to " + newClient.getName());
			newClient.setType(TypeOfHost.CLIENT);
			newClient.setFixedEndpoint(client);
		}
		return newClient;
	}
	
	private Host changeEndpoints(List<Host> hosts, String client, String server){
		Host tmp = changeFixedClient(hosts, client);
		if(tmp != null)
			return tmp;
		pastClients.clear();
		tmp = changeFixedClient(hosts, client);
		Host currServer = hosts.stream().filter(h -> h.getType().equals(TypeOfHost.SERVER) && h.getFixedEndpoint().equals(server) ).findAny().orElse(null);
		pastServers.add(currServer);
		currServer.setType(TypeOfHost.MIDDLEBOX);
		currServer.setFixedEndpoint(null);
		Host newServer = hosts.stream().filter(h -> !pastServers.contains(h) && h.getType().equals(TypeOfHost.MIDDLEBOX)).findAny().orElse(null);
		if(newServer != null){
			//System.out.println("Host server changed to " + newServer.getName());
			newServer.setType(TypeOfHost.SERVER);
			newServer.setFixedEndpoint(server);
		}
		return newServer;
	}
	@Test
	public void testBGPerformance(){
		try {
			List<String> files = new ArrayList<>();
			
			files.add("./testfile/FWAutoconfiguration/3FW2P.xml");
			files.add("./testfile/FWAutoconfiguration/3FW3P.xml");
			files.add("./testfile/FWAutoconfiguration/3FW4P.xml");
			files.add("./testfile/FWAutoconfiguration/3FW5P.xml");
			files.add("./testfile/FWAutoconfiguration/4FW2P.xml");
			files.add("./testfile/FWAutoconfiguration/4FW3P.xml");
			files.add("./testfile/FWAutoconfiguration/4FW4P.xml");
			files.add("./testfile/FWAutoconfiguration/4FW5P.xml");
			files.add("./testfile/FWAutoconfiguration/5FW2P.xml");
			files.add("./testfile/FWAutoconfiguration/5FW3P.xml");
			files.add("./testfile/FWAutoconfiguration/5FW4P.xml");
			files.add("./testfile/FWAutoconfiguration/5FW5P.xml");
			files.add("./testfile/FWAutoconfiguration/6FW2P.xml");
			files.add("./testfile/FWAutoconfiguration/6FW3P.xml");
			files.add("./testfile/FWAutoconfiguration/6FW4P.xml");
			files.add("./testfile/FWAutoconfiguration/6FW5P.xml");
			files.add("./testfile/FWAutoconfiguration/7FW2P.xml");
			files.add("./testfile/FWAutoconfiguration/7FW3P.xml");
			files.add("./testfile/FWAutoconfiguration/7FW4P.xml");
			files.add("./testfile/FWAutoconfiguration/7FW5P.xml");
			files.add("./testfile/FWAutoconfiguration/8FW2P.xml");
			files.add("./testfile/FWAutoconfiguration/8FW3P.xml");
			files.add("./testfile/FWAutoconfiguration/8FW4P.xml");
			files.add("./testfile/FWAutoconfiguration/8FW5P.xml");
			
			for(String f : files){
				condTime = 0;
				checkTimeSAT = 0;
				checkTimeUNSAT = 0;
				totTime = 0;
				maxCondTime = 0;
				maxCheckTimeSAT = 0;
				maxCheckTimeUNSAT = 0;
				maxTotTime = 0;
				nSAT = 0;
				nUNSAT = 0;
				i = 0;
				err = 0;
				System.out.println("===========FILE " + f + "===========");
				logger.debug("===========FILE " + f + "===========");
				// create a JAXBContext capable of handling the generated classes
				//long beginAll=System.currentTimeMillis();
		        JAXBContext jc = JAXBContext.newInstance( "it.polito.verifoo.rest.jaxb" );
		        // create an Unmarshaller
		        Unmarshaller u = jc.createUnmarshaller();
		        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); 
		        Schema schema = sf.newSchema( new File( "./xsd/nfvSchema.xsd" )); 
		        u.setSchema(schema);
		        // unmarshal a document into a tree of Java content objects
		        root = (NFV) u.unmarshal( new FileInputStream( f ) );
		        String clientName = root.getGraphs().getGraph().stream()
		        		.flatMap(g -> g.getNode().stream())
		        		.filter(n -> n.getFunctionalType().equals(FunctionalTypes.WEBCLIENT) 
		        				|| n.getFunctionalType().equals(FunctionalTypes.MAILCLIENT)
		        				|| n.getFunctionalType().equals(FunctionalTypes.ENDHOST))
		        		.map(n -> n.getName())
		        		.findFirst().get();
		        String serverName = root.getGraphs().getGraph().stream()
		        		.flatMap(g -> g.getNode().stream())
		        		.filter(n -> n.getFunctionalType().equals(FunctionalTypes.WEBSERVER) 
		        				|| n.getFunctionalType().equals(FunctionalTypes.MAILSERVER))
		        		.map(n -> n.getName())
		        		.findFirst().get();
		        
				do{
					System.out.print("Simulation nr " + i+" ");
					Thread t = new Thread(){
						public void run(){
							try {
								root = (NFV) u.unmarshal( new FileInputStream( f ) );
								testCoarse(root);
								i++;
								if(i%50 == 0) System.out.println("");
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								err++;
							}
						}
					};
					t.start();
					//avoid deadlock
					t.join(6000000);
					if(t.isAlive()){
						if(root.getHosts() != null){
							Host currClient = root.getHosts().getHost().stream().filter(h -> h.getType().equals(TypeOfHost.CLIENT)).findAny().orElse(null);
							Host currServer = root.getHosts().getHost().stream().filter(h -> h.getType().equals(TypeOfHost.SERVER)).findAny().orElse(null);
							System.out.println("Simulation " + i + " has deadlock with client on " + currClient.getName() + " and server on " + currServer.getName());
						}
						throw new BadGraphError();
					}
				//}while(changeEndpoints(root.getHosts().getHost(), clientName, serverName) != null);
				}while(i<10);
				System.out.println("");
				System.out.println("Simulations -> " + i + " / Errors -> " + err);
				logger.debug("Simulations -> " + i + " / Errors -> " + err);
				//System.out.println("AVG Nr of Conditions -> " + (nrOfConditions/(i)) + " / MAX Nr Of Conditions -> " + maxNrOfConditions);
				//System.out.println("AVG creating condition -> " + (condTime/(i-err)) + "ms");
				//System.out.println("MAX creating condition -> " + (maxCondTime) + "ms");
				logger.debug("AVG creating condition -> " + (condTime/(i-err)) + "ms");
				logger.debug("MAX creating condition -> " + (maxCondTime) + "ms");
				if(nSAT > 0){
					//System.out.println("AVG checking property when SAT -> " + (checkTimeSAT/nSAT) + "ms");
					//System.out.println("MAX checking property when SAT -> " + (maxCheckTimeSAT) + "ms");
					logger.debug("AVG checking property when SAT -> " + (checkTimeSAT/nSAT) + "ms");
					logger.debug("MAX checking property when SAT -> " + (maxCheckTimeSAT) + "ms");
				}
				if(nUNSAT > 0){
					//System.out.println("AVG checking property when UNSAT-> " + (checkTimeUNSAT/nUNSAT) + "ms");
					//System.out.println("MAX checking property when UNSAT-> " + (maxCheckTimeUNSAT) + "ms");
					logger.debug("AVG checking property when UNSAT-> " + (checkTimeUNSAT/nUNSAT) + "ms");
					logger.debug("MAX checking property when UNSAT-> " + (maxCheckTimeUNSAT) + "ms");
				}
				System.out.println("AVG total time -> " + (totTime/nSAT) + "ms");
				System.out.println("MAX total time -> " + (maxTotTime) + "ms");
				System.out.println("=====================================");
				logger.debug("AVG total time -> " + (totTime/nSAT) + "ms");
				logger.debug("MAX total time -> " + (maxTotTime) + "ms");
				logger.debug("=====================================");

			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
}
