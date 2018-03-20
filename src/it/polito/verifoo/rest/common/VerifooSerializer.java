package it.polito.verifoo.rest.common;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.xml.bind.Marshaller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.microsoft.z3.Status;

import it.polito.verifoo.rest.jaxb.EType;
import it.polito.verifoo.rest.jaxb.Graph;
import it.polito.verifoo.rest.jaxb.NFV;
import it.polito.verifoo.rest.jaxb.Property;
import it.polito.verigraph.mcnet.components.IsolationResult;

/**
 * This class separates the Verifoo classes implementation from the actual input
 *  * @author Antonio
 *
 */
public class VerifooSerializer {
	private NFV nfv;
	private boolean sat = false;
	private Logger logger = LogManager.getLogger("mylog");
	public VerifooSerializer(NFV root) {
		this.nfv = root;
		try{
			for(Graph g:root.getGraphs().getGraph()){
	        	List<Property> prop = root.getPropertyDefinition().getProperty().stream().filter(p -> p.getGraph()==g.getId()).collect(Collectors.toList());
	        	if(prop.size() == 0)
					throw new BadGraphError("No property defined for the Graph "+g.getId(),EType.INVALID_PROPERTY_DEFINITION);
	        	VerifooProxy test = new VerifooProxy(g, root.getHosts(), root.getConnections(), root.getConstraints(), prop);
	        	IsolationResult res=test.checkNFFGProperty();
	        	if(res.result != Status.UNSATISFIABLE){
	        		new Translator(res.model.toString(),root, g).convert();
	        		sat = true;
	        	}
	        	else{
	        		sat = false;
	        	}
	        	root.getPropertyDefinition().getProperty().stream().filter(p->p.getGraph()==g.getId()).forEach(p -> p.setIsSat(res.result!=Status.UNSATISFIABLE)); 
	        }
	    } catch (BadGraphError e) {
			//logger.error("Graph semantically incorrect");
			//System.out.println("Graph semantically incorrect");
	    	logger.error(e);
	    	throw e;
	    }
	}
	

	/**
	 * @return the nfv object
	 */
	public NFV getNfv() {
		return nfv;
	}


	/**
	 * @return if the z3 model is sat
	 */
	public boolean isSat() {
		return sat;
	}

}