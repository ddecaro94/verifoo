package it.polito.verigraph.scalability.tests;

/**
 * <p/>  Custom test  <p/>
 *  | A | <------> | CACHE | <------> | CACHE |<------> | B |
 */

import java.util.ArrayList;
import java.util.HashMap;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.DatatypeExpr;
import com.microsoft.z3.FuncDecl;
import com.microsoft.z3.Model;
import com.microsoft.z3.Status;
import com.microsoft.z3.Z3Exception;

import it.polito.verifoo.components.RoutingTable;
import it.polito.verigraph.mcnet.components.Checker;
import it.polito.verigraph.mcnet.components.Checker.Prop;
import it.polito.verigraph.mcnet.components.IsolationResult;
import it.polito.verigraph.mcnet.components.NetContext;
import it.polito.verigraph.mcnet.components.Network;
import it.polito.verigraph.mcnet.components.NetworkObject;
import it.polito.verigraph.mcnet.components.Tuple;
import it.polito.verigraph.mcnet.netobjs.PolitoEndHost;
import it.polito.verigraph.mcnet.netobjs.PolitoNat;
import it.polito.verigraph.mcnet.netobjs.AclFirewall;
import it.polito.verigraph.mcnet.netobjs.AclFirewallAuto;
import it.polito.verigraph.mcnet.netobjs.PacketModel;


//| a| ---- | FW1 | ---- | NAT | ---- | FW2 | ---- | b |		<p/>
public class TestAutoPlacement {

    public Checker check;
    public Context ctx;
    public PolitoEndHost a,b;
    AclFirewall fw1;
	public AclFirewall fw2;
    public PolitoNat nat;
    
    public  BoolExpr y1;
   	
   	
   	public  BoolExpr x11;
   	public  BoolExpr x21;
   	public  BoolExpr x31;
   	public  BoolExpr fw1Used, fw1NotUsed;
    public  BoolExpr fw2Used, fw2NotUsed;
    public  TestAutoPlacement(){
        ctx = new Context();
        
        x11 = ctx.mkBoolConst("x11");
		x21 = ctx.mkBoolConst("x21");
		x31 = ctx.mkBoolConst("x31");

		y1 = ctx.mkBoolConst("y1");
		

		
        NetContext nctx  = new NetContext (ctx,new String[]{"a", "b", "nat", "fw2","fw1"},
                                                new String[]{"ip_a", "ip_b","ip_nat", "ip_fw2", "ip_fw1"});
        Network net = new Network (ctx,new Object[]{nctx});
        a = new PolitoEndHost(ctx, new Object[]{nctx.nm.get("a"), net, nctx});
        b = new PolitoEndHost(ctx, new Object[]{nctx.nm.get("b"), net, nctx});
        nat = new PolitoNat(ctx, new Object[]{nctx.nm.get("nat"), net, nctx});
        fw1= new AclFirewall(ctx, new Object[]{nctx.nm.get("fw1"), net, nctx,1});
        fw2= new AclFirewall(ctx, new Object[]{nctx.nm.get("fw2"), net, nctx,1});
    
        fw1Used = fw1.isUsed();
		fw1NotUsed = ctx.mkNot(fw1Used);
		fw2Used = fw2.isUsed();
		fw2NotUsed = ctx.mkNot(fw2Used);
        
        ArrayList<Tuple<NetworkObject,ArrayList<DatatypeExpr>>> adm = new ArrayList<Tuple<NetworkObject,ArrayList<DatatypeExpr>>>();
        ArrayList<DatatypeExpr> al1 = new ArrayList<DatatypeExpr>();
        ArrayList<DatatypeExpr> al2 = new ArrayList<DatatypeExpr>();
        ArrayList<DatatypeExpr> al3 = new ArrayList<DatatypeExpr>();
        ArrayList<DatatypeExpr> al4 = new ArrayList<DatatypeExpr>();
        ArrayList<DatatypeExpr> al5 = new ArrayList<DatatypeExpr>();
        al1.add(nctx.am.get("ip_a"));
        al2.add(nctx.am.get("ip_b"));
        al3.add(nctx.am.get("ip_nat"));
        al4.add(nctx.am.get("ip_fw2"));
        al5.add(nctx.am.get("ip_fw1"));
        adm.add(new Tuple<>(a, al1));
        adm.add(new Tuple<>(b, al2));
        adm.add(new Tuple<>(nat, al3));
        adm.add(new Tuple<>(fw2, al4));
        adm.add(new Tuple<>(fw1, al5));
        net.setAddressMappings(adm);

        
        ArrayList<RoutingTable> rta = new ArrayList<RoutingTable>();
        //rta.add(new RoutingTable(nctx.am.get("ip_b"), fw1,10,x11));
        //rta.add(new RoutingTable(nctx.am.get("ip_b"), nat,10,ctx.mkAnd(x21,ctx.mkNot(x11))));
        //rta.add(new RoutingTable(nctx.am.get("ip_b"), fw1,10,ctx.mkAnd(x11, ctx.mkNot(optFw1))));
        rta.add(new RoutingTable(nctx.am.get("ip_b"), fw1,10,ctx.mkOr(x11,fw1NotUsed)));
        rta.add(new RoutingTable(nctx.am.get("ip_b"), nat,10,x21));
        net.routingOptimizationSG(a, rta, null);
        
        ArrayList<RoutingTable> rtfw1 = new ArrayList<RoutingTable>();
        //rtfw1.add(new RoutingTable(nctx.am.get("ip_b"), nat,0,ctx.mkAnd(x11,x21)));
        rtfw1.add(new RoutingTable(nctx.am.get("ip_b"), nat,0,ctx.mkAnd(ctx.mkOr(x11, fw1NotUsed),x21)));
        net.routingOptimizationSG(fw1, rtfw1, null);

        ArrayList<RoutingTable> rtnat = new ArrayList<RoutingTable>();
        //rtnat.add(new RoutingTable(nctx.am.get("ip_b"), fw2,0,ctx.mkAnd(x31,x21)));
        //rtnat.add(new RoutingTable(nctx.am.get("ip_b"), b,10,ctx.mkAnd(x21,ctx.mkNot(x31))));
        rtnat.add(new RoutingTable(nctx.am.get("ip_b"), fw2,0,ctx.mkAnd(ctx.mkOr(x31, fw2NotUsed),x21)));
        rtnat.add(new RoutingTable(nctx.am.get("ip_b"), b,10,x21));
        net.routingOptimizationSG(nat, rtnat, null);
        
        ArrayList<RoutingTable> rtfw2 = new ArrayList<RoutingTable>();
        //rtfw2.add(new RoutingTable(nctx.am.get("ip_b"), b,10,ctx.mkAnd(x31,x21)));
        rtfw2.add(new RoutingTable(nctx.am.get("ip_b"), b,10,ctx.mkOr(x31, fw2NotUsed)));
        net.routingOptimizationSG(fw2, rtfw2, null);       
        
        
        ArrayList<RoutingTable> rtb = new ArrayList<RoutingTable>();
        net.routingOptimizationSG(b, rtb, null); 
        
        net.attach(a, b, nat,fw2,fw1);
        
        ArrayList<DatatypeExpr> ia = new ArrayList<DatatypeExpr>();
	    ia.add(nctx.am.get("ip_a"));
	    nat.natModel(nctx.am.get("ip_nat"));
	    nat.setInternalAddress(ia);
	    
	    PacketModel packet1  = new PacketModel();
	    a.installEndHost(packet1);
	    b.installEndHost(null);

        setConditions(ctx,nctx);
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


    public static void main(String[] args) throws Z3Exception
    {
        TestAutoPlacement model = new TestAutoPlacement();
        model.resetZ3();
        
        //IsolationResult ret =model.check.checkRealIsolationProperty(model.a,model.b);
        model.check.propertyAdd(model.a, model.b, Prop.ISOLATION);
        IsolationResult ret= model.check.propertyCheck();
        if (ret.result == Status.UNSATISFIABLE){
           System.out.println("UNSAT"); // Nodes a and b are isolated
        }else{
            System.out.println("SAT ");
            System.out.println(ret.model);
        }
    }
    private void setConditions(Context ctx, NetContext nctx) {
	  	y1 = ctx.mkBoolConst("y1");
		
		nctx.softConstraints.add(new Tuple<BoolExpr, String>(ctx.mkNot(y1), "servers"));
		
	  
    	int capacity_x1 = 10;
    	int capacity_x2 = 10;
    	int capacity_x3 = 10;
    	
    	int capacity_y1 = 1000;
		
		nctx.softConstrAutoPlace.add(new Tuple<BoolExpr, String>(ctx.mkEq(nctx.bool_to_int(fw1NotUsed), ctx.mkInt(1)), "servers1"));
		nctx.constraints.add(ctx.mkEq(ctx.mkAdd(nctx.bool_to_int(x11), nctx.bool_to_int(fw1NotUsed)), ctx.mkInt(1)));
		nctx.constraints.add(ctx.mkEq(nctx.bool_to_int(x21), ctx.mkInt(1)));
		nctx.constraints.add(ctx.mkEq(ctx.mkAdd(nctx.bool_to_int(x31), nctx.bool_to_int(fw2NotUsed)), ctx.mkInt(1)));
		nctx.softConstrAutoPlace.add(new Tuple<BoolExpr, String>(ctx.mkEq(nctx.bool_to_int(fw2NotUsed), ctx.mkInt(1)), "servers1"));
		
		nctx.constraints.add(ctx.mkOr(ctx.mkImplies(y1, x11),ctx.mkImplies(y1, x21),ctx.mkImplies(y1, x31)));
		
	
		ArithExpr leftSide = 
			ctx.mkAdd(ctx.mkMul(ctx.mkInt(capacity_x1), nctx.bool_to_int(x11)),
					ctx.mkMul(ctx.mkInt(capacity_x2), nctx.bool_to_int(x21)),
					ctx.mkMul(ctx.mkInt(capacity_x3), nctx.bool_to_int(x31))
					);
		nctx.constraints.add(ctx.mkLe(leftSide, ctx.mkMul(ctx.mkInt(capacity_y1), nctx.bool_to_int(y1))));
	}
}