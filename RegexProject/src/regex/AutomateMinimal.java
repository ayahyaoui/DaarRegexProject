package regex;

import java.awt.GradientPaint;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.event.IIOReadWarningListener;
import javax.print.CancelablePrintJob;

public class AutomateMinimal {
	ArrayList<HashMap<Integer, Integer>> matrix;
	//HashMap<Integer, ArrayList<Integer>> voisinMap;
	//ArrayList<ArrayList<Integer>> groups;// useless
	ArrayList<Integer> endNodes; // list of end nodes
	ArrayList<String> groupCode; // identity of each group (path in cummun)
	ArrayList<Integer> groupChef; // leader (position in matrix) of each group

	int []groups2;
	final int SIZEOMEGA = 255;
	int nbGroup;
	int oldSize;
	
	public AutomateMinimal(AutomateDeterminist aut) {
		if (RegEx.DISPLAY)
		System.out.println("Minininimal" + aut.nbNewNode + " " + aut.matrix.size());
		this.matrix = aut.matrix;
		this.endNodes = aut.endNodesArrayList;
		this.oldSize = aut.nbNewNode;
		this.groups2 = new int[aut.nbNewNode];
		this.groupChef = new ArrayList<Integer>();
		this.groupCode = new ArrayList<String>();
		//this.groups = new ArrayList<>();
		this.groupCode.add(neighborsCode(matrix.get(0)));
		this.groupChef.add(0);
		//this.groups.add(new ArrayList<>());
		//this.groups.add(new ArrayList<>());
		
		boolean isFirst = true;
		
		// TODO encoder les premiers voisin (noeud de depart) et un noeud d'arriver
		for (int i = 0; i < this.oldSize; i++)
			if(this.endNodes.get(i) == 1) 
			{
				if (isFirst)
				{
					isFirst = false;

					this.groupChef.add(i);
					this.groupCode.add(neighborsCode(matrix.get(i)));
				}
				this.groups2[i] = 1;
			//	this.groups.get(1).add(i);
			}
			else
			{
				//this.groups.get(0).add(i);
				this.groups2[i] = 0;				
			}
		nbGroup = 2;
	}
	
	public String neighborsCode(HashMap<Integer, Integer> voisins)
	{
		
		ArrayList<Integer> nextsNodesArrayList = new ArrayList<Integer>();
		
		for (Map.Entry<Integer, Integer> voisin : voisins.entrySet()) {
			nextsNodesArrayList.add(voisin.getKey() + this.groups2[voisin.getValue()] * 255);					
		}
		nextsNodesArrayList.sort((o1, o2) -> (o1 - o2));
		return (nextsNodesArrayList.toString());
	}
	
	private void afficheDebug(String[] nextNodesStrings)
	{
		int i;

		System.out.println(">>>>>>>>>>>>>>>>>>>");
		System.out.println("Resume State groups:" + this.nbGroup );
		for (i = 0; i< this.nbGroup;i++) {
			System.out.print("< ");
			for(int j = 0; j < this.oldSize; j++)
			{
				if (this.groups2[j] == i)
					System.out.print(j + " ");
			//	else {
				//	System.out.print("esel[" + this.groups2[j] + "] ");
				//}
			}
			System.out.print(">, ");
		}
		System.out.println();
		for (i = 0; i< this.oldSize; i++) {
			System.out.println(i + " ===> " + nextNodesStrings[i]);
		}
		System.out.println("<<<<<<<<<<<<<<<<<<");
	}
	
	public FinalAutomaton minimizeAut()
	{
		boolean change;
		String[] nextsNodeStrings;
		int i; 
		
		nextsNodeStrings = new String[this.oldSize + 10];
		change = true;
		int limitToRemove = 10;
		while (change && limitToRemove > 0)
		{
			i = 0;
			change = false;
			for (HashMap<Integer, Integer> voisins : matrix)
			{
				System.out.println(i + " " + this.oldSize);
				
				nextsNodeStrings[i] = neighborsCode(voisins);
				i++;
			}
			if (RegEx.DISPLAY)
			afficheDebug(nextsNodeStrings);
			
			// refresh chefgroup code
			for (i = 0; i< this.nbGroup; i++)
			{
				if (this.groupCode.get(i).compareTo(nextsNodeStrings[this.groupChef.get(i)]) != 0)
					this.groupCode.set(i, nextsNodeStrings[this.groupChef.get(i)]);
			}
			
			for(i = 0 ; i < this.oldSize; i++)
			{
				int index = groupCode.indexOf(nextsNodeStrings[i]);
				if (index < 0)
				{
					groupCode.add(nextsNodeStrings[i]);
					this.groupChef.add(i);
					groups2[i] = this.nbGroup;
					this.nbGroup++;
					change = true;
				}
				else if (groups2[i] != index) // change to an existing group
				{
					groups2[i] = index;
					change = true;
				}
				// else stay in the same group (do nothing)
			}
			limitToRemove--;
		}
		
		int [][]finalMatrix = new int[nbGroup + 1][SIZEOMEGA];
		boolean []finalEndsNode = new boolean[nbGroup + 1];
		finalEndsNode[0] = false;
		
		System.out.println(matrix.size());


		i = 0;
		for (HashMap<Integer, Integer> voisins : matrix)
		{
			if(i >= oldSize)
				break;
				if (RegEx.DISPLAY)
			System.out.println("before" + i + "/" + nbGroup + " " + oldSize);
			if (endNodes.get(i) == 1)
				finalEndsNode[this.groups2[i] + 1] = true; 
			for (Map.Entry<Integer, Integer> voisin : voisins.entrySet()) {
				finalMatrix[this.groups2[i] + 1][voisin.getKey()] = groups2[voisin.getValue()] + 1 ;
				if (RegEx.DISPLAY)
				System.out.println(" => " + voisin.getKey() + "=> " + (voisin.getValue() + 1));
				//nextsNodesArrayList.add(voisin.getKey() + this.groups2[voisin.getValue()] * 255);					
			}
			if (RegEx.DISPLAY)
			System.out.println("after" + i + "/" + nbGroup + " " + oldSize);
			i++;
		}
		if (RegEx.DISPLAY)
		System.out.println("end" + i + "/" + nbGroup + " " + oldSize);
		FinalAutomaton testAutomaton = new FinalAutomaton(nbGroup + 1, finalMatrix, finalEndsNode);
		if (RegEx.DISPLAY)
		System.out.println("\n\n<<<<=======LAST STEP=======>");
		if (RegEx.DISPLAY)
		testAutomaton.displayAutomaton();
		return testAutomaton;
	}
}
