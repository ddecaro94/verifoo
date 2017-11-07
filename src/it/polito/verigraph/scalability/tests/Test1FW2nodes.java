package it.polito.verigraph.scalability.tests;

/**
 * <p/>  Custom test  <p/>
 *  | A | <------> | CACHE |<------> | B |
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.microsoft.z3.Context;
import com.microsoft.z3.DatatypeExpr;
import com.microsoft.z3.FuncDecl;
import com.microsoft.z3.Model;
import com.microsoft.z3.Status;
import com.microsoft.z3.Z3Exception;

import it.polito.verigraph.mcnet.components.Checker;
import it.polito.verigraph.mcnet.components.IsolationResult;
import it.polito.verigraph.mcnet.components.NetContext;
import it.polito.verigraph.mcnet.components.Network;
import it.polito.verigraph.mcnet.components.NetworkObject;
import it.polito.verigraph.mcnet.components.Tuple;
import it.polito.verigraph.mcnet.netobjs.PolitoEndHost;
import it.polito.verigraph.mcnet.netobjs.AclFirewall;
import it.polito.verigraph.mcnet.netobjs.AclFirewallAuto;
import it.polito.verigraph.mcnet.netobjs.PolitoCache;

public class Test1FW2nodes {

    public Checker check;
    public Context ctx;
    public PolitoEndHost a,b,c;
    public AclFirewallAuto fw;

    public  Test1FW2nodes(){
        ctx = new Context();

        NetContext nctx = new NetContext (ctx,new String[]{"a", "b","c", "fw"},
                                                new String[]{"ip_a", "ip_b","ip_c", "ip_fw"});
        Network net = new Network (ctx,new Object[]{nctx});

        a = new PolitoEndHost(ctx, new Object[]{nctx.nm.get("a"), net, nctx});
        b = new PolitoEndHost(ctx, new Object[]{nctx.nm.get("b"), net, nctx});
        fw = new AclFirewallAuto(ctx, new Object[]{nctx.nm.get("fw"), net, nctx});
        c = new PolitoEndHost(ctx, new Object[]{nctx.nm.get("c"), net, nctx});

        ArrayList<Tuple<NetworkObject,ArrayList<DatatypeExpr>>> adm = new ArrayList<Tuple<NetworkObject,ArrayList<DatatypeExpr>>>();
        ArrayList<DatatypeExpr> al1 = new ArrayList<DatatypeExpr>();
        ArrayList<DatatypeExpr> al2 = new ArrayList<DatatypeExpr>();
        ArrayList<DatatypeExpr> al3 = new ArrayList<DatatypeExpr>();
        ArrayList<DatatypeExpr> al4 = new ArrayList<DatatypeExpr>();
        al1.add(nctx.am.get("ip_a"));
        al2.add(nctx.am.get("ip_b"));
        al4.add(nctx.am.get("ip_c"));
        al3.add(nctx.am.get("ip_fw"));
        adm.add(new Tuple<>(a, al1));
        adm.add(new Tuple<>(b, al2));
        adm.add(new Tuple<>(c, al4));
        adm.add(new Tuple<>(fw, al3));
        net.setAddressMappings(adm);

        ArrayList<Tuple<DatatypeExpr,NetworkObject>> rt1 = new ArrayList<Tuple<DatatypeExpr,NetworkObject>>();
        rt1.add(new Tuple<DatatypeExpr,NetworkObject>(nctx.am.get("ip_fw"), fw));
        rt1.add(new Tuple<DatatypeExpr,NetworkObject>(nctx.am.get("ip_b"), fw));
        rt1.add(new Tuple<DatatypeExpr,NetworkObject>(nctx.am.get("ip_a"), a));
        net.routingTable(a, rt1);

        ArrayList<Tuple<DatatypeExpr,NetworkObject>> rt2 = new ArrayList<Tuple<DatatypeExpr,NetworkObject>>();
        rt2.add(new Tuple<DatatypeExpr,NetworkObject>(nctx.am.get("ip_a"), fw));
        rt2.add(new Tuple<DatatypeExpr,NetworkObject>(nctx.am.get("ip_fw"), fw));
        rt2.add(new Tuple<DatatypeExpr,NetworkObject>(nctx.am.get("ip_b"), b));
        net.routingTable(b, rt2);

        ArrayList<Tuple<DatatypeExpr,NetworkObject>> rt3 = new ArrayList<Tuple<DatatypeExpr,NetworkObject>>();
        rt3.add(new Tuple<DatatypeExpr,NetworkObject>(nctx.am.get("ip_a"),a));
        rt3.add(new Tuple<DatatypeExpr,NetworkObject>(nctx.am.get("ip_c"),c));
        rt3.add(new Tuple<DatatypeExpr,NetworkObject>(nctx.am.get("ip_b"),b));
        rt3.add(new Tuple<DatatypeExpr,NetworkObject>(nctx.am.get("ip_fw"),fw));
        net.routingTable(fw, rt3);

        ArrayList<Tuple<DatatypeExpr,NetworkObject>> rtc= new ArrayList<Tuple<DatatypeExpr,NetworkObject>>();
        rtc.add(new Tuple<DatatypeExpr,NetworkObject>(nctx.am.get("ip_a"),fw));
        rtc.add(new Tuple<DatatypeExpr,NetworkObject>(nctx.am.get("ip_b"),fw));
        net.routingTable(c, rtc);
        
        net.attach(a, b,c, fw);

        ArrayList<Tuple<DatatypeExpr,DatatypeExpr>> acl = new ArrayList<Tuple<DatatypeExpr,DatatypeExpr>>();
        //acl.add(new Tuple<DatatypeExpr,DatatypeExpr>(nctx.am.get("ip_fw"),nctx.am.get("ip_a")));
        //acl.add(new Tuple<DatatypeExpr,DatatypeExpr>(nctx.am.get("ip_a"),nctx.am.get("ip_b")));
        fw.addAcls(acl);
        
        check = new Checker(ctx,nctx,net);
}
    
    public void resetZ3() throws Z3Exception{
        HashMap<String, String> cfg = new HashMap<String, String>();
        cfg.put("model", "true");
         ctx = new Context(cfg);
    }
    
    public void printVector (Object[] array){
        int i=0;
        System.out.println( "*** Printing vector ***");
        for (Object a : array){
            i+=1;
            System.out.println( "#"+i);
            System.out.println(a);
            System.out.println(  "*** "+ i+ " elements printed! ***");
        }
    }
    
    public void printModel (Model model) throws Z3Exception{
        for (FuncDecl d : model.getFuncDecls()){
            System.out.println(d.getName() +" = "+ d.toString());
              System.out.println("");
        }
    }

    private static  void parsing(String model) {
		String[] lines =model.split("\\n");
		Pattern pattern = Pattern.compile("\\(\\) Bool *true");
		for (int i = 0; i < lines.length&&lines.length>1; i++) {
			String compare = lines[i++]+lines[i];
			Matcher matcher = pattern.matcher(compare);
			if (matcher.find()) {
			    System.out.println(compare);
			} 
		}
	}
    
    
    
    public static void main(String[] args) throws Z3Exception
    {
        Test1FW2nodes model = new Test1FW2nodes();
        model.resetZ3();
        
        //IsolationResult ret =model.check.checkRealIsolationProperty(model.a,model.b);
        
        model.check.propertyAdd(model.a, model.b, Checker.Prop.ISOLATION);
        model.check.propertyAdd(model.c, model.b, Checker.Prop.REACHABILITY);
        IsolationResult ret = model.check.propertyCheck();
        
        //IsolationResult ret =model.check.checkIsolationProperty(model.a,model.b);
        //model.printVector(ret.assertions);
        if (ret.result == Status.UNSATISFIABLE){
           System.out.println("UNSAT"); // Nodes a and b are isolated
           
        }else{
            System.out.println("SAT ");
            System.out.println(ret.model);
           // parsing(ret.model+"");
//            System.out.print( "Model -> ");model.printModel(ret.model);
//          System.out.println( "Violating packet -> " +ret.violating_packet);
//          System.out.println("Last hop -> " +ret.last_hop);
//          System.out.println("Last send_time -> " +ret.last_send_time);
//          System.out.println( "Last recv_time -> " +ret.last_recv_time);
        }
    }
}