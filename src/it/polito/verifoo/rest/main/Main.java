package it.polito.verifoo.rest.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;
import com.microsoft.z3.Status;

import gurobi.GRB;
import gurobi.GRBException;
import it.polito.verifoo.rest.jaxb.*;
import it.polito.verifoo.rest.medicine.MedicineSimulator;
import it.polito.verigraph.mcnet.components.IsolationResult;
import it.polito.verifoo.gurobi.MipSolver;
import it.polito.verifoo.random.RandomInputGenerator;
import it.polito.verifoo.rest.common.*;

/**
 * 
 * This is the main class only for testing the VerifooProxy
 *
 */
public class Main {
	public static void main(String[] args) throws MalformedURLException {
		System.setProperty("log4j.configuration", new File("resources", "log4j2.xml").toURI().toURL().toString());
		Logger logger = LogManager.getLogger("mylog");
		try {

			JAXBContext jc;
			// create a JAXBContext capable of handling the generated classes
			synchronized (logger) {
				jc = JAXBContext.newInstance("it.polito.verifoo.rest.jaxb");
			}
			// create an Unmarshaller
			Unmarshaller u = jc.createUnmarshaller();
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = sf.newSchema(new File("./xsd/nfvSchema.xsd"));
			u.setSchema(schema);
			RandomInputGenerator r = null;
			boolean exit = false;
			int sat = 0;

			int isRandom =2;
			NFV root = null;
			Marshaller m =null;
			while (!exit) {
				try {
					if (isRandom==1) {
						 m = jc.createMarshaller();
						m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
						m.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "./xsd/nfvSchema.xsd");
						int maxClients = 1, maxServers = 1, maxInternalNodes = 10, maxProperty = 1, maxHosts = 25;
						boolean isRAN=true;
						r = new RandomInputGenerator(maxClients, maxServers, maxInternalNodes, maxProperty, maxHosts,isRAN);
						 root = r.getRandomInput();
						 Graph temp = root.getGraphs().getGraph().get(0);
							System.err.println("VNFs:" + temp.getNode().size() + "   Hosts:" + root.getHosts().getHost().size()
									+ "   Links:" + root.getConnections().getConnection().size());

						//if(root.getGraphs().getGraph().get(0).getNode().size()!=7) continue;
						//if(root.getHosts().getHost().size()!=24) continue;
						if(root.getConnections().getConnection().size()>1000) continue;
						MipSolver mip = new MipSolver(root);
						mip.latencyObj();
						mip.optimizeIt();
						OutputStream out = new FileOutputStream("./testfile/Random/current.xml");
						m.marshal(root, out);
						if (mip.printThem() == -1) {
							return;
						} else {
							//mip.printThem();
						}

						// exit = true;
						
					} else {

						// unmarshal a document into a tree of Java content
						// objects
						 root = (NFV) u.unmarshal(new FileInputStream("./testfile/RAN/sg3nodes12.xml"));
						 Graph temp = root.getGraphs().getGraph().get(0);
							System.err.println("VNFs:" + temp.getNode().size() + "   Hosts:" + root.getHosts().getHost().size()
									+ "   Links:" + root.getConnections().getConnection().size());

				
						MipSolver mip = new MipSolver(root);
						mip.latencyObj();
						mip.optimizeIt();
						mip.printThem();
						
					}
					//OutputStream out = new FileOutputStream("./testfile/Performance/bgAS.xml");
					//m.marshal(root, out);
					System.out.println("-------------");
					VerifooSerializer test = new VerifooSerializer(root);
					
					printVerifoo(root);

					if (test.isSat()) {
						//sound();
						System.out.println("SAT");
						sat++;
						if (sat > 0)
							exit = true;
						// create a Marshaller and marshal to output
						m = jc.createMarshaller();
						m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
						m.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "./xsd/nfvSchema.xsd");
						// m.marshal( root, System.out );
					} else {
						System.out.println("UNSAT");
						if (r == null)
							exit = true;
					}
					
					// MedicineSimulator sim = new MedicineSimulator(root);
					// sim.printAll();
					// m.marshal( sim.getPhysicalTopology(), System.out );
					// sim.stopSimulation();*/
				} catch (BadGraphError | FileNotFoundException e) {
					// logger.error("Graph semantically incorrect");
					// System.out.println("Graph semantically incorrect");
					logger.error(e);
					if (r == null)
						exit = true;
				} catch (GRBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (JAXBException je) {
			logger.error("Error while unmarshalling or marshalling");
			logger.error(je);
			System.exit(1);
		} catch (ClassCastException cce) {
			logger.error("Wrong data type found in XML document");
			logger.error(cce);
			System.exit(1);
		} catch (BadGraphError e) {
			logger.error("Graph semantically incorrect");
			logger.error(e);
			System.exit(1);
		} catch (SAXException e) {
			logger.error(e);
			System.exit(1);
		}
	}

	private static void printVerifoo(NFV root) {
		// (System.out::println)
		Set<Host> hostsSet = new HashSet<>();
		root.getHosts().getHost().forEach(p -> {
			if(!p.getNodeRef().isEmpty()) hostsSet.add(p);
			p.getNodeRef()
					.forEach(l -> System.out.println("PlacementV: " + l.getNode() + "_" + p.getName()));
		});
		System.out.println("Hosts: "+(hostsSet.size()-1));
		
	}

	private static void sound() {
		byte[] buf = new byte[1];
		;
		AudioFormat af = new AudioFormat((float) 44100, 8, 1, true, false);
		SourceDataLine sdl;
		try {
			sdl = AudioSystem.getSourceDataLine(af);
			sdl.open();
			sdl.start();
			for (int i = 0; i < 1000 * (float) 44100 / 1000; i++) {
				double angle = i / ((float) 44100 / 440) * 2.0 * Math.PI;
				buf[0] = (byte) (Math.sin(angle) * 100);
				sdl.write(buf, 0, 1);
			}
			sdl.drain();
			sdl.stop();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
