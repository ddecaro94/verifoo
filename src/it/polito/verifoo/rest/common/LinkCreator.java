package it.polito.verifoo.rest.common;

import java.util.ArrayList;
import java.util.HashMap;

import static java.util.Comparator.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.polito.verifoo.rest.autoconfiguration.FWAutoconfigurationManager;
import it.polito.verifoo.rest.jaxb.FunctionalTypes;
import it.polito.verifoo.rest.jaxb.Node;
import it.polito.verifoo.rest.jaxb.Path;
/**
 * Creates the links from the node's neighbours
 *
 */
public class LinkCreator {
	private Logger logger = LogManager.getLogger("mylog");
	private List<Link> links = new ArrayList<>();
	private List<Node> nodes;
	private List<Path> paths;
	private FWAutoconfigurationManager FWmanager;
	private Map<Integer, List<Link>> pathMap = new HashMap<>();
	/**
	 * 
	 * @param ns the list of the nodes in the network service
	 */
	public LinkCreator(List<Node> ns){
		nodes = ns;
		FWmanager = null;
	}
	/**
	 * 
	 * @param ns the list of the nodes in the network service
	 * @param FWmanager the class to manage firewall autoconfiguration
	 */
	public LinkCreator(List<Node> ns, FWAutoconfigurationManager FWmanager){
		nodes = ns;
		this.FWmanager = FWmanager;
	}
	/**
	 * 
	 * @param ns the list of the nodes in the network service
	 * @param ps the list of the paths that the packet flows will follow 
	 */
	public LinkCreator(List<Node> ns, List<Path> ps){
		nodes = ns;
		paths = ps;
//		for(Path p : ps){
//			List<Link> links = new ArrayList<>();
//			List<PathNode> pathNodes = p.getPathNode();
//			for(int i = 1; i < pathNodes.size(); i++){
//				Link l = new Link(pathNodes.get(i-1).getName(), pathNodes.get(i).getName());
//				links.add(l);
//			}
//			pathMap.put(p.getId(), links);
//		}
	}
	
	/**
	 * Retrives the links of the service graph exploring the node's neighbours
	 * @return the links between nodes in the service graph
	 */
	public List<Link> getLinks(){
		List<Node> clients = nodes.stream().filter(n -> {return n.getFunctionalType().equals(FunctionalTypes.MAILCLIENT) || n.getFunctionalType().equals(FunctionalTypes.WEBCLIENT)|| n.getFunctionalType().equals(FunctionalTypes.ENDHOST);}).collect(Collectors.toList());
        List<Node> servers = nodes.stream().filter(n -> {return n.getFunctionalType().equals(FunctionalTypes.MAILSERVER) || n.getFunctionalType().equals(FunctionalTypes.WEBSERVER);}).collect(Collectors.toList());
        for(Node client:clients){
			for(Node server:servers){
				List<String> neighbours = client.getNeighbour().stream()
												.map(n -> n.getName())
												.collect(Collectors.toList());
				//logger.debug("Found neighbours of " + client.getName() + " ("+ neighbours + ")");
		        for(String neighbour : neighbours){
		        	Node next = nodes.stream().filter(n -> n.getName().equals(neighbour)).findFirst().get();
		        	//logger.debug("Creating path from client " + client.getName() + " to "+ next.getName() +" towards server "+server.getName());
		        	createLink(client, next, client, server, new ArrayList<>(), new ArrayList<>());
		        	//logger.debug("New Link from " + client.getName() + " to "+ next.getName() +" towards server "+server.getName());
		        }
				//links.add(new Link(client.getName(), next.getName()));
			}
		}
        
		List<Link> orderedLinks = links.stream()
										.sorted(comparing(Link::getSourceNode).thenComparing(Link::getDestNode))
										.distinct()
										.collect(Collectors.toList());
		logger.debug("Unique links:");
		orderedLinks.forEach(l -> logger.debug(l.getSourceNode()+"->"+l.getDestNode()));
		return orderedLinks;
	}
	/**
	 * Creates the links from the nodes' neighbours exploring the various paths, ensuring to add a link only once
	 * @param prec previous node in the chain
	 * @param current current node in the chain
	 * @param server the node that is the server of the chain
	 * @param converting the list of nodes on the current path, to avoid infinite loops
	 * @param converted the nodes from which all the neighbours are already been explored
	 * @throws BadGraphError
	 */
	private boolean createLink(Node prec, Node current, Node client, Node server, List<String> converting, List<String> converted) throws BadGraphError{
		if(current.getName().equals(server.getName())){
			//logger.debug("Found neighbours of " + prec.getName() + " ("+ current.getName() + ") that reaches the server " + server.getName());
			//logger.debug("New Link from " + prec.getName() + " to "+ current.getName() +" towards server "+server.getName());
			links.add(new Link(prec.getName(), current.getName()));
			return true;
		}
		if(current.getFunctionalType().equals(FunctionalTypes.MAILCLIENT) || current.getFunctionalType().equals(FunctionalTypes.WEBCLIENT)|| current.getFunctionalType().equals(FunctionalTypes.ENDHOST)){
			//logger.debug("Link from " + prec.getName() + " to "+ current.getName() +" reaches client "+client.getName());
			return false;
		}
		if(converted.contains(current.getName())){
			links.add(new Link(prec.getName(), current.getName()));
			return true;
		}
		
		boolean found = false;
		List<String> neighbours = current.getNeighbour().stream()
										.filter(n -> !(n.getName().equals(prec.getName())))
										.map(n -> n.getName())
										.collect(Collectors.toList());
		converting.add(current.getName());
		//logger.debug("From " + prec.getName() + " converting neighbours of " + current.getName() + " " + neighbours +" into links");
		
		for(String neighbour : neighbours){
			if(!converting.contains(neighbour)){
				Node next = nodes.stream().filter(n -> n.getName().equals(neighbour)).findFirst().get();
				//If the neighbour reaches the server or reaches a node that reaches the server then... 
				if(createLink(current, next, client, server, converting, converted) ){
					//logger.debug("Found neighbours of " + prec.getName() + " ("+ current.getName() + ") that reaches the server " + server.getName());
					Link l = new Link(prec.getName(), current.getName());
					//logger.debug("New Link from " + prec.getName() + " to "+ current.getName() +" towards server "+server.getName());
					links.add(l);
					converted.add(current.getName());
					found = true;
					
					if(current.getFunctionalType()== FunctionalTypes.FIREWALL && current.getConfiguration().getFirewall().getElements().isEmpty() && FWmanager != null) {
						FWmanager.setPolicy(current, client, server);
					}
				}
				else{
					//logger.debug("Neighbour from " + current.getName() + " (" + neighbour +") don't reach the server " + server.getName());
				}
			}
		}
		converting.remove(current.getName());
		return found;
	}
}
