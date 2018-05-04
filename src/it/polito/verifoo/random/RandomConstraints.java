package it.polito.verifoo.random;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import it.polito.verifoo.rest.jaxb.*;
import it.polito.verifoo.rest.jaxb.BandwidthConstraints.BandwidthMetrics;
import it.polito.verifoo.rest.jaxb.NodeConstraints.NodeMetrics;;

public class RandomConstraints {
	Random random;
	NodeConstraints nodeConstraints = new NodeConstraints();
	BandwidthConstraints bandwidthConstraints = new BandwidthConstraints();
	/**
	 * Creates a totally random generated set of constraints for Verifoo
	 * @param random a random number generator
	 * @param g a RandomGraph object describing the service graph
	 */
	public RandomConstraints(Random random, RandomGraph g) {
		this.random = random;
		createNodeConstraints(g);
		createBandwithConstraints(g);
	}
	
	private void createNodeConstraints(RandomGraph g) {
		for(Node n : g.getMiddle()){
			if(true)
				nodeConstraints.getNodeMetrics().add(randomNodeMetrics(n));
		}
	}

	private void createBandwithConstraints(RandomGraph g) {
		Map<Node,Node> set = new HashMap<>();
		for(Node src : g.getMiddle()){
			for(Node dst : g.getMiddle()){
				if(src.getNeighbour().stream().map(nei -> nei.getName()).collect(Collectors.toList()).contains(dst.getName())){
					if(random.nextBoolean()){
						if(set.containsKey(src)) if(set.get(src).equals(dst)) continue;
							bandwidthConstraints.getBandwidthMetrics().add(randomBandwidthMetrics(src,dst));
							set.put(dst,src);	
						
					}
						
				}
			}
		}
	}
	
	private NodeMetrics randomNodeMetrics(Node n){
		NodeMetrics m = new NodeMetrics();
		m.setNode(n.getName());
		if(random.nextBoolean())
			//m.setCores(random.nextInt(2)+1);
		if(random.nextBoolean()){
			//m.setNrOfOperations((long) (random.nextInt(100000)+1000));
			//m.setMaxNodeLatency(random.nextInt(1000)+10);
		}
		if(true)
			m.setReqStorage(random.nextInt(200)+1);
		//if(random.nextBoolean())
			//m.setMemory(random.nextInt(64)+1);
		return m;
	}
	


	private BandwidthMetrics randomBandwidthMetrics(Node src, Node dst) {
		BandwidthMetrics m = new BandwidthMetrics();
		m.setSrc(src.getName());
		m.setDst(dst.getName());
		m.setReqLatency(random.nextInt(1000)+1);
		return m;
	}
	/**
	 * 
	 * @return the node constraints
	 */
	public NodeConstraints getNodeConstraints() {
		return nodeConstraints;
	}

	/**
	 * 
	 * @return the bandwidth constraints
	 */
	public BandwidthConstraints getBandwidthConstraints() {
		return bandwidthConstraints;
	}

	/**
	 * 
	 * @return the node and bandwidth constraints
	 */
	public Constraints getConstraints(){
		Constraints c = new Constraints();
		c.setNodeConstraints(nodeConstraints);
		c.setBandwidthConstraints(bandwidthConstraints);
		return c;
	}

}
