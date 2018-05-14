/*
 * Copyright 2016 Politecnico di Torino
 * Authors:
 * Project Supervisor and Contact: Riccardo Sisto (riccardo.sisto@polito.it)
 * 
 * This file is part of Verigraph.
 * 
 * Verigraph is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * Verigraph is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public
 * License along with Verigraph.  If not, see
 * <http://www.gnu.org/licenses/>.
 */
package it.polito.verifoo.test;

import java.util.HashMap;

import com.microsoft.z3.Context;
import com.microsoft.z3.FuncDecl;
import com.microsoft.z3.Model;
import com.microsoft.z3.Status;
import com.microsoft.z3.Z3Exception;

import it.polito.verigraph.mcnet.components.IsolationResult;
/**
 * 
 * @author Giacomo Costantini
 *
 */
public class Polito_Multipath{

	Context ctx;
	
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
    	Polito_Multipath p = new Polito_Multipath();
    	IsolationResult ret;
    	
    	p.resetZ3(); 	
    	//PolitoMultipathTest model = new PolitoMultipathTest(p.ctx);
    	Polito3nodes3hosts model = new Polito3nodes3hosts(p.ctx);
    	//Polito5nodes1hostSG model = new Polito5nodes1hostSG(p.ctx);
    	
    	
//    	ret = model.check.CheckIsolationProperty(model.politoErrFunction, model.politoMailClient);
//    	ret = model.check.CheckIsolationProperty(model.politoMailClient, model.politoMailServer);
//    	ret = model.check.CheckIsolationProperty(model.politoMailServer, model.politoErrFunction);
    	long startTime = System.currentTimeMillis();
    	ret = model.check.checkIsolationProperty(model.a,model.b );
		long endTime = System.currentTimeMillis();
		long totalTime = (endTime - startTime);
		System.out.println("Total execution time: " + totalTime + " ms");
    	
    	
    
    	
    	//p.printVector(ret.assertions);
    	if (ret.result == Status.UNSATISFIABLE){
     	   System.out.println("UNSAT"); // Nodes a and b are isolated
    	}else{
     		System.out.println("SAT ");
     		System.out.print( ""+ret.model); //p.printModel(ret.model);
     	}
    }

    
}
