package it.polito.verifoo.rest.common;

import it.polito.verifoo.rest.jaxb.EType;
import it.polito.verifoo.rest.jaxb.Endhost;
import it.polito.verifoo.rest.jaxb.FunctionalTypes;
import it.polito.verifoo.rest.jaxb.ProtocolTypes;
import it.polito.verigraph.mcnet.components.NetContext;
import it.polito.verigraph.mcnet.netobjs.PacketModel;
/**
 * This class converts our EndHost Packet Model configuration in the verigraph one
 */
public class PacketWrapper extends PacketModel {

	/**
	 * This method converts our EndHost Packet Model configuration in the verigraph one
	 * @param eh EndHost Packet Model
	 * @param nctx Network Context.
	 * @throws BadGraphError Invalid Configuration
	 */
	public PacketWrapper(Endhost eh, NetContext nctx) throws BadGraphError {
		if(eh!=null){
			try {
				if(eh.getBody()!=null && !eh.getBody().isEmpty())
					this.setBody(String.valueOf(eh.getBody()).hashCode());
				if(eh.getEmailFrom()!=null && !eh.getEmailFrom().isEmpty())
					this.setEmailFrom(String.valueOf(eh.getEmailFrom()).hashCode());
				if(eh.getDestination()!=null && !eh.getDestination().isEmpty())
					this.setIp_dest(nctx.am.get(eh.getDestination()));
				if(eh.getOptions()!=null && !eh.getOptions().isEmpty())
					this.setOptions(String.valueOf(eh.getOptions()).hashCode());
				if(eh.getProtocol()!=null){
					if(eh.getProtocol().value().equals("HTTP_RESPONSE")){
						this.setProto(nctx.HTTP_REQUEST);
					}else if(eh.getProtocol().value().equals("HTTP_RESPONSE")){
						this.setProto(nctx.HTTP_RESPONSE);
					}else if(eh.getProtocol().value().equals("POP3_REQUEST")){
						this.setProto(nctx.POP3_REQUEST);
					}else if(eh.getProtocol().value().equals("POP3_RESPONSE")){
						this.setProto(nctx.POP3_RESPONSE);
					}else{
						throw new BadGraphError("Endhost Protocol " + eh.getProtocol().value() + " not supported",EType.INVALID_NODE_CONFIGURATION);
					}
				}
				if(eh.getSequence()!=null)
					this.setSeq(eh.getSequence().intValue());
				if(eh.getUrl()!=null && !eh.getUrl().isEmpty())
					this.setUrl(String.valueOf(eh.getUrl()).hashCode());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				throw new BadGraphError(e.getMessage(),EType.INVALID_NODE_CONFIGURATION);
			}
		}
	}

}
