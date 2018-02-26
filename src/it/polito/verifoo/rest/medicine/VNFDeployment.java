package it.polito.verifoo.rest.medicine;

import java.util.List;

import it.polito.verifoo.rest.jaxb.Host;
import it.polito.verifoo.rest.jaxb.NodeRefType;


public class VNFDeployment {
	String placement;
	private List<Host> hosts;
	
	public VNFDeployment(List<Host> hosts) {
		this.hosts = hosts;
		CreatePlacement();
	}

	private void CreatePlacement(){
		placement = "class CustomPlacement(object):\n"
							+"\tdef place(self, nsd, vnfds, saps, dcs):\n";
		for(Host h:hosts){
			for(NodeRefType n : h.getNodeRef()){
				placement += "\t\tvnfds['"+n.getNode().toLowerCase()+"'][\"dc\"] = dcs['"+h.getName()+"']\n";
			}
			if(h.getFixedEndpoint() != null){
				placement += "\t\tvnfds['"+h.getFixedEndpoint().toLowerCase()+"'][\"dc\"] = dcs['"+h.getName()+"']\n";
			}
		}
		 placement +="\t\tsaps['ns_input'][\"dc\"] = dcs['hostA']\n"
				 	 +"\t\tsaps['ns_output'][\"dc\"] = dcs['hostB']\n";

	}
	public String getPlacementDescription(){
		return placement;
	}
}