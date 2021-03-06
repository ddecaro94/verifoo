package it.polito.verigraph.scalability.tests;

/**
 * <p/>  Custom test  <p/>
 *  | A | <------> | FM1 | <------> | FM2 | <------> | FM2 |<------> | B |
 */

import java.util.ArrayList;
import java.util.HashMap;

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
import it.polito.verigraph.mcnet.netobjs.PolitoFieldModifier;

public class Test3FieldModifiers {

    public Checker check;
    public Context ctx;
    public PolitoEndHost a,b;
    public PolitoFieldModifier fm1, fm2, fm3;

    public  Test3FieldModifiers(){
        ctx = new Context();

        NetContext nctx = new NetContext (ctx,new String[]{"a", "b", "fm1","fm2","fm3"},
                                                new String[]{"ip_a", "ip_b", "ip_fm1","ip_fm2","ip_fm3"});
        Network net = new Network (ctx,new Object[]{nctx});

        a = new PolitoEndHost(ctx, new Object[]{nctx.nm.get("a"), net, nctx});
        b = new PolitoEndHost(ctx, new Object[]{nctx.nm.get("b"), net, nctx});
        fm1 = new PolitoFieldModifier(ctx, new Object[]{nctx.nm.get("fm1"), net, nctx});
        fm2 = new PolitoFieldModifier(ctx, new Object[]{nctx.nm.get("fm2"), net, nctx});
        fm3 = new PolitoFieldModifier(ctx, new Object[]{nctx.nm.get("fm3"), net, nctx});

        ArrayList<Tuple<NetworkObject,ArrayList<DatatypeExpr>>> adm = new ArrayList<Tuple<NetworkObject,ArrayList<DatatypeExpr>>>();
        ArrayList<DatatypeExpr> al1 = new ArrayList<DatatypeExpr>();
        ArrayList<DatatypeExpr> al2 = new ArrayList<DatatypeExpr>();
        ArrayList<DatatypeExpr> al3 = new ArrayList<DatatypeExpr>();
        ArrayList<DatatypeExpr> al4 = new ArrayList<DatatypeExpr>();
        ArrayList<DatatypeExpr> al5 = new ArrayList<DatatypeExpr>();
        al1.add(nctx.am.get("ip_a"));
        al2.add(nctx.am.get("ip_b"));
        al3.add(nctx.am.get("ip_fm1"));
        al4.add(nctx.am.get("ip_fm2"));
        al5.add(nctx.am.get("ip_fm3"));
        adm.add(new Tuple<>(a, al1));
        adm.add(new Tuple<>(b, al2));
        adm.add(new Tuple<>(fm1, al3));
        adm.add(new Tuple<>(fm2, al4));
        adm.add(new Tuple<>(fm3, al5));
        net.setAddressMappings(adm);

        ArrayList<Tuple<DatatypeExpr,NetworkObject>> rt1 = new ArrayList<Tuple<DatatypeExpr,NetworkObject>>();
        rt1.add(new Tuple<DatatypeExpr,NetworkObject>(nctx.am.get("ip_fm1"), fm1));
        rt1.add(new Tuple<DatatypeExpr,NetworkObject>(nctx.am.get("ip_fm2"), fm1));
        rt1.add(new Tuple<DatatypeExpr,NetworkObject>(nctx.am.get("ip_fm3"), fm1));
        rt1.add(new Tuple<DatatypeExpr,NetworkObject>(nctx.am.get("ip_b"), fm1));

        net.routingTable(a, rt1);

        ArrayList<Tuple<DatatypeExpr,NetworkObject>> rt2 = new ArrayList<Tuple<DatatypeExpr,NetworkObject>>();
        rt2.add(new Tuple<DatatypeExpr,NetworkObject>(nctx.am.get("ip_a"), fm3));
        rt2.add(new Tuple<DatatypeExpr,NetworkObject>(nctx.am.get("ip_fm1"), fm3));
        rt2.add(new Tuple<DatatypeExpr,NetworkObject>(nctx.am.get("ip_fm2"), fm3));
        rt2.add(new Tuple<DatatypeExpr,NetworkObject>(nctx.am.get("ip_fm3"), fm3));

        net.routingTable(b, rt2);

        ArrayList<Tuple<DatatypeExpr,NetworkObject>> rt3 = new ArrayList<Tuple<DatatypeExpr,NetworkObject>>();
        rt3.add(new Tuple<DatatypeExpr,NetworkObject>(nctx.am.get("ip_a"),a));
        rt3.add(new Tuple<DatatypeExpr,NetworkObject>(nctx.am.get("ip_fm2"),fm2));
        rt3.add(new Tuple<DatatypeExpr,NetworkObject>(nctx.am.get("ip_fm3"),fm2));
        rt3.add(new Tuple<DatatypeExpr,NetworkObject>(nctx.am.get("ip_b"),fm2));

        net.routingTable(fm1, rt3);

        ArrayList<Tuple<DatatypeExpr,NetworkObject>> rt4 = new ArrayList<Tuple<DatatypeExpr,NetworkObject>>();
        rt4.add(new Tuple<DatatypeExpr,NetworkObject>(nctx.am.get("ip_a"),fm1));
        rt4.add(new Tuple<DatatypeExpr,NetworkObject>(nctx.am.get("ip_fm1"),fm1));
        rt4.add(new Tuple<DatatypeExpr,NetworkObject>(nctx.am.get("ip_fm3"),fm3));
        rt4.add(new Tuple<DatatypeExpr,NetworkObject>(nctx.am.get("ip_b"),fm3));

        net.routingTable(fm2, rt4);

        ArrayList<Tuple<DatatypeExpr,NetworkObject>> rt5 = new ArrayList<Tuple<DatatypeExpr,NetworkObject>>();
        rt5.add(new Tuple<DatatypeExpr,NetworkObject>(nctx.am.get("ip_a"),fm2));
        rt5.add(new Tuple<DatatypeExpr,NetworkObject>(nctx.am.get("ip_fm1"),fm2));
        rt5.add(new Tuple<DatatypeExpr,NetworkObject>(nctx.am.get("ip_fm2"),fm2));
        rt5.add(new Tuple<DatatypeExpr,NetworkObject>(nctx.am.get("ip_b"),b));

        net.routingTable(fm3, rt5);

        net.attach(a, b, fm1, fm2, fm3);

        //Configuring middleboxes
  

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
        Test3FieldModifiers model = new Test3FieldModifiers();
        model.resetZ3();
        
        IsolationResult ret =model.check.checkIsolationProperty(model.a,model.b);
        model.printVector(ret.assertions);
        if (ret.result == Status.UNSATISFIABLE){
           System.out.println("UNSAT"); // Nodes a and b are isolated
        }else{
            System.out.println("SAT ");
            System.out.println(ret.model);
//            System.out.print( "Model -> ");model.printModel(ret.model);
//          System.out.println( "Violating packet -> " +ret.violating_packet);
//          System.out.println("Last hop -> " +ret.last_hop);
//          System.out.println("Last send_time -> " +ret.last_send_time);
//          System.out.println( "Last recv_time -> " +ret.last_recv_time);
        }
    }
}