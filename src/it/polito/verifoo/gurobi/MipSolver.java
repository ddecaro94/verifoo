package it.polito.verifoo.gurobi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBExpr;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBQuadExpr;
import gurobi.GRBVar;
import it.polito.verifoo.rest.jaxb.BandwidthConstraints.BandwidthMetrics;
import it.polito.verifoo.rest.jaxb.Connection;
import it.polito.verifoo.rest.jaxb.Host;
import it.polito.verifoo.rest.jaxb.NFV;
import it.polito.verifoo.rest.jaxb.Node;
import it.polito.verifoo.rest.jaxb.NodeConstraints.NodeMetrics;
import it.polito.verifoo.rest.jaxb.TypeOfHost;

public class MipSolver {
	public static class Edge {

		String startN;

		String endN;
		String firstV;
		String nextV;
		GRBVar var;

		public Edge(String host, String host2, String vnf, String vnf2, GRBVar temp) {
			startN = host;
			endN = host2;
			firstV = vnf;
			nextV = vnf2;
			var = temp;
		}
	}

	public static class MPlace {
		public String vnf;

		public String host;
		public GRBVar var;

		public MPlace(String vnfTemps, String nodeTemps, GRBVar temp) {
			var = temp;
			vnf = vnfTemps;
			host = nodeTemps;
		}

	}

	static GRBEnv env;
	static GRBModel model;
	static List<MPlace> placeVars = new ArrayList<>();
	static List<Edge> edgeM = new ArrayList<>();
	static List<GRBVar> hostVars = new ArrayList<>();
	static Map<String, Host> hostList = new HashMap<>();
	static Map<String, Node> nodeList = new HashMap<>();
	static List<String> nodeNames;

	static List<String> hostNames;
	static String client = "nodeA";
	static String server = "nodeB";
	static String clientHost = "e050caaa";
	static String serverHost = "c0286e30";

	static List<Connection> edge;

	static List<NodeMetrics> constraints;

	public static long optimizeIt() throws GRBException {
		long startTime = System.nanoTime();
		model.optimize();
		long endTime = System.nanoTime();
		long totalTime = endTime - startTime;

		System.err.println("############ Execution time Gurobi: " + totalTime / 1000000);
		return totalTime;

	}
	static int count=0;
	static int lat=0;
	public static int printThem() throws GRBException {
		model.write("debug.lp");
		
		if (model.get(GRB.IntAttr.Status) == GRB.Status.OPTIMAL) {
			//printObjs();
			hostVars.forEach(p -> {
				try {
					if (p.get(GRB.DoubleAttr.X) == 1) count++;
						//System.out.println("Hosts: " + p.get(GRB.StringAttr.VarName) + " " + p.get(GRB.DoubleAttr.X));
				} catch (GRBException e) {
					e.printStackTrace();
				}
			});
			System.out.println("Hosts: "+count);
			placeVars.forEach(p -> {
				try {
					double temp = p.var.get(GRB.DoubleAttr.X);
					if (temp == 1)
						System.out.println(
								"Placement: " + p.var.get(GRB.StringAttr.VarName) + " " + p.var.get(GRB.DoubleAttr.X));
				} catch (GRBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			
			edgeM.forEach(p -> {
				try {
					if (p.var.get(GRB.DoubleAttr.X) == 1){
						
							for (Connection edgeConnections : edge) {
								if(edgeConnections.getSourceHost().equals(p.startN) && edgeConnections.getDestHost().equals(p.endN)){
									 lat = lat+edgeConnections.getAvgLatency();
									 //System.out.println("Edges: " + p.var.get(GRB.StringAttr.VarName) + " " + p.var.get(GRB.DoubleAttr.X));
								}
							}
									
					}
						
				} catch (GRBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			System.out.println("Latency is: "+lat);
			// Dispose of model and environment
			model.dispose();
			env.dispose();
		} else {
			System.out.println("Infeasable");
			return -1;
		}
		return 0;

	}

	private static void printObjs() throws GRBException {
		int nSolutions = model.get(GRB.IntAttr.SolCount);
		System.out.println("Number of solutions found: " + nSolutions);

		// Print objective values of solutions
		if (nSolutions > 10)
			nSolutions = 10;
		System.out.println("Objective values for first " + nSolutions);
		System.out.println(" solutions:");
		for (int i = 0; i < 2; i++) {
			model.set(GRB.IntParam.ObjNumber, i);

			System.out.print("\tSet" + i);
			for (int e = 0; e < nSolutions; e++) {
				System.out.print(" ");
				model.set(GRB.IntParam.SolutionNumber, e);
				double val = model.get(GRB.DoubleAttr.ObjNVal);
				System.out.print("      " + val);
			}
			System.out.println();
		}
	}

	static List<BandwidthMetrics> latencyConstraint;

	public MipSolver(NFV root) throws GRBException {

		env = new GRBEnv("mip1.log");
		model = new GRBModel(env);
		// model.set(GRB.IntParam.OutputFlag, 0);
		model.set(GRB.IntParam.OutputFlag, 0);
		constraints = root.getConstraints().getNodeConstraints().getNodeMetrics();
		latencyConstraint = root.getConstraints().getBandwidthConstraints().getBandwidthMetrics();
		nodeNames = root.getGraphs().getGraph().get(0).getNode().stream().map(p -> {
			nodeList.put(p.getName(), p);
			return p.getName();
		}).collect(Collectors.toList());

		List<Node> all = new ArrayList<>();
		getOrderedNodes(root.getGraphs().getGraph().get(0).getNode().get(0), all);
		// nodeList = all.stream().collect(Collectors.toMap(x -> x.getName(), x
		// -> x));

		nodeNames = all.stream().map(p -> p.getName()).collect(Collectors.toList());

		hostNames = root.getHosts().getHost().stream().map(p -> {
			hostList.put(p.getName(), p);
			if (p.getType().compareTo(TypeOfHost.CLIENT) == 0) {
				clientHost = p.getName();
				client = p.getFixedEndpoint();
			}
			if (p.getType().compareTo(TypeOfHost.SERVER) == 0) {
				serverHost = p.getName();
				server = p.getFixedEndpoint();
			}
			return p.getName();
		}).collect(Collectors.toList());

		edge = root.getConnections().getConnection();

		generateVars();
		constraints();
	}
	
	private void getOrderedNodes(Node node, List<Node> all) {
		all.add(node);
		if(all.size()>100) {
			System.out.println("to many");
			return;
		}
		//System.out.println("Doing:  "+ node.getName());
		if (!node.getNeighbour().isEmpty()) {
			if(all.contains(nodeList.get(node.getNeighbour().get(0).getName()))){
				if(node.getNeighbour().size()!=2) return;
				getOrderedNodes(nodeList.get(node.getNeighbour().get(1).getName()), all);
				return;
			}else{
				getOrderedNodes(nodeList.get(node.getNeighbour().get(0).getName()), all);
				return;
			}
		}
		return;
	}

	

	private void constraints() throws GRBException {
	
		
/*		GRBLinExpr temps = new GRBLinExpr(); Optional<MPlace> tt =
				 placeVars.stream().filter(p -> p.host.equals("host12") &&
				  p.vnf.equals("MAC0")).findFirst(); temps.addTerm(1, tt.get().var);
				 model.addConstr(temps, GRB.EQUAL, 1, "twos");*/
				 
		// at least host + obj
		GRBLinExpr obj = new GRBLinExpr();

		hostList.values().forEach(p -> {
			try {
				// System.out.println("%%% For host:" + p);
				GRBVar hostVar = model.addVar(0, 1, 0, GRB.BINARY, p.getName());
				obj.addTerm(1, hostVar);

				hostVars.add(hostVar);

				GRBLinExpr storageExpr = new GRBLinExpr();
				GRBLinExpr maxExpr = new GRBLinExpr();
				placeVars.stream().filter(l -> l.host.equals(p.getName())).forEach(m -> {
					if (!(m.vnf.equals(nodeNames.get(0)) || m.vnf.equals(nodeNames.get(nodeNames.size() - 1)))) {
						GRBLinExpr atleast = new GRBLinExpr();
						atleast.addTerm(-1, hostVar);
						atleast.addTerm(1, m.var);
						// System.out.println("----" + m.vnf + "_" + m.host + "
						// less
						// or equal " + p);
						storageExpr.addTerm((int) getVnfMetric(m.vnf), m.var);
						maxExpr.addTerm(1, m.var);
						// System.out.println("------storage of VNF:" +
						// vnfStorage.get(m.vnf));
						try {
							model.addConstr(atleast, GRB.LESS_EQUAL, 0, "at least one");
						} catch (GRBException e) {
							e.printStackTrace();
						}
					}

				});
				// System.out.println("-Lessore than host storage:" +
				// nodeStorage.get(p));
				model.addConstr(storageExpr, GRB.LESS_EQUAL, hostList.get(p.getName()).getDiskStorage(), "storage___");
				model.addConstr(maxExpr, GRB.LESS_EQUAL,
						hostList.get(p.getName()).getMaxVNF() != null ? hostList.get(p.getName()).getMaxVNF() : 1,
						"maximum number");
			} catch (GRBException e) {
				e.printStackTrace();
			}
		});
		

		// at least one m to be true
		// System.out.println("Only one placement");
		for (String name : nodeNames) {
			GRBLinExpr expr = new GRBLinExpr();
			placeVars.stream().filter(p -> p.vnf.equals(name)).forEach(p -> {
				expr.addTerm(1, p.var);

				// System.out.print(p.vnf + "_" + p.host + "/");
			});
			// System.out.println();
			model.addConstr(expr, GRB.EQUAL, 1, "one");
		}

		// for each pair there is an edge
		for (int a = 0; a < nodeNames.size() - 1; a++) {
			final Integer innerMi = new Integer(a);
			for (MPlace string1n : placeVars.stream().filter(p -> p.vnf.equals(nodeNames.get(innerMi)))
					.collect(Collectors.toList())) {
				for (MPlace string2n : placeVars.stream().filter(p -> p.vnf.equals(nodeNames.get(innerMi + 1)))
						.collect(Collectors.toList())) {
					if (!string1n.host.equals(string2n.host)) {
						GRBVar temp = model.addVar(0, 1, 0, GRB.BINARY,
								string1n.vnf + string1n.host + string2n.vnf + string2n.host);
						// System.out.println("EDGEyes:" + string1n.vnf + string1n.host + string2n.vnf + string2n.host);
						edgeM.add(new Edge(string1n.host, string2n.host, string1n.vnf, string2n.vnf, temp));
					}
				}
			}
		}

		for (Edge edgesVar : edgeM) {
			MPlace st1 = placeVars.stream().filter(p -> p.vnf.equals(edgesVar.firstV) && p.host.equals(edgesVar.startN))
					.findFirst().get();
			MPlace st2 = placeVars.stream().filter(p -> p.vnf.equals(edgesVar.nextV) && p.host.equals(edgesVar.endN))
					.findFirst().get();
			GRBQuadExpr exp = new GRBQuadExpr();
			exp.addTerm(1, st1.var, st2.var);
			exp.addTerm(-1, edgesVar.var);
			model.addQConstr(exp, GRB.EQUAL, 0, "temp");
		}

		for (Edge edgesVar : edgeM) {
			boolean hush = edge.stream()
					.anyMatch(p -> p.getSourceHost().equals(edgesVar.startN) && p.getDestHost().equals(edgesVar.endN));
			if (!hush) {
				// System.out.println("edge is
				// zero:"+edgesVar.startN+"_"+edgesVar.endN);
				GRBLinExpr zero = new GRBLinExpr();
				zero.addTerm(1, edgesVar.var);
				model.addConstr(zero, GRB.EQUAL, 0, "edges");
			}
		}

		for (BandwidthMetrics constr : latencyConstraint) {
			for (Edge temp : edgeM) {
				if (temp.firstV.equals(constr.getSrc()) && temp.nextV.equals(constr.getDst())) {
					GRBLinExpr latReq = new GRBLinExpr();
					Connection hush = edge.stream()
							.filter(p -> p.getSourceHost().equals(temp.startN) && p.getDestHost().equals(temp.endN))
							.findFirst().orElse(null);
					if (hush == null)
						continue;
					latReq.addTerm(hush.getAvgLatency(), temp.var);
					model.addConstr(latReq, GRB.LESS_EQUAL, constr.getReqLatency(), "--------latReq");
				}
			}
		}
		/* ############################## */model.setObjectiveN(obj, 1, 1000, 1, 1,1, "maxO");
	}

	public void latencyObj() throws GRBException {
		GRBLinExpr obj = new GRBLinExpr();
		for (Connection connection : edge) {
			List<Edge> list = edgeM.stream()
					.filter(p -> p.startN.equals(connection.getSourceHost()) && p.endN.equals(connection.getDestHost()))
					.collect(Collectors.toList());
			list.forEach(p -> {
				obj.addTerm(connection.getAvgLatency(), p.var);
			});
		}
		/* ############################## */ model.setObjectiveN(obj, 100, 1001, 1, 1, 0.77739, "latencyO");
		// more of third integer is better
	}

	private void generateVars() throws GRBException {
		for (Node vnfs : nodeList.values()) {
			for (Host nodes : hostList.values()) {
				String nodeTemps = nodes.getName();
				String vnfTemps = vnfs.getName();
				String sring = vnfTemps + "_" + nodeTemps;
				if (sring.equals(client + "_" + clientHost) || sring.equals(server + "_" + serverHost)) {
					GRBVar temp = model.addVar(1, 1, 0, GRB.BINARY, sring);
					placeVars.add(new MPlace(vnfTemps, nodeTemps, temp));
					// System.out.println("Possibility1:" + sring);
					// continue;
				} else if (vnfTemps.equals(client) || vnfTemps.equals(server) || nodeTemps.equals(clientHost)
						|| nodeTemps.equals(serverHost)) {
					GRBVar temp = model.addVar(0, 0, 0, GRB.BINARY, sring);
					placeVars.add(new MPlace(vnfTemps, nodeTemps, temp));
					// System.out.println("Possibility0:" + sring);
					// continue;
				} else if (!nodes.getSupportedVNF().stream().anyMatch(p -> {
					// System.out.println("comparing "+p.getFunctionalType()+"
					// to "+vnfs.getFunctionalType());
					return p.getFunctionalType() == (vnfs.getFunctionalType());
				})) {
					GRBVar temp = model.addVar(0, 1, 0, GRB.BINARY, sring);
					placeVars.add(new MPlace(vnfTemps, nodeTemps, temp));
					GRBLinExpr types = new GRBLinExpr();
					types.addTerm(1, temp);
					model.addConstr(types, GRB.EQUAL, 0, "type");
					// System.out.println("cannot " + vnfTemps + " on " +
					// nodeTemps);
				} else {
					GRBVar temp = model.addVar(0, 1, 0, GRB.BINARY, sring);
					placeVars.add(new MPlace(vnfTemps, nodeTemps, temp));
					// System.out.println("can" + vnfTemps + " on " +
					// nodeTemps);
					// System.out.println("Possibility:" + sring);
				}
			}
		}

	}

	private double getVnfMetric(String vnf) {
		double storage = 0;

		for (NodeMetrics nodeMetrics : constraints) {
			if (nodeMetrics.getNode().equals(nodeNames.get(0))
					|| nodeMetrics.getNode().equals(nodeNames.get(nodeNames.size() - 1)))
				continue;
			String one = vnf;
			String two = nodeMetrics.getNode();
			// System.out.println("looking for"+one+ "now"+two+"sdfsdf");
			if (one.equals(two)) {
				storage = nodeMetrics.getReqStorage();
//				/System.out.println("for:" + vnf + " metric found:" + storage);
			} else {
				// System.out.println("not found for metric of:"+vnf);
			}
		}
		return storage;
	}

}
