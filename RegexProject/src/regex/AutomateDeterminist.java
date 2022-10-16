package regex;


import java.util.HashMap;
import java.util.Map;

import javax.swing.text.Position;

import java.util.*;//Set;
import java.util.ArrayList;

public class AutomateDeterminist {
	// todo change name to groupement
	Automate todetermine;
	ArrayList<ArrayList<RNode>> pileGroups;
	HashMap<String,Integer> idGroups;
	ArrayList<Integer> endNodesArrayList;
	ArrayList<HashMap<Integer, Integer>> matrix;
	
	final int BUFFER_SIZE = 1024;
	int nbNewNode;
	final int idStart;
	final int idEnd;
	
	public AutomateDeterminist(Automate todetermine) {
		 this.pileGroups = new ArrayList<ArrayList<RNode>>();
		 this.idGroups = new HashMap<String, Integer>();
		 this.endNodesArrayList = new ArrayList<Integer>();
		 this.idStart = todetermine.firstNode.ID;
		 this.idEnd = todetermine.endNode.ID;
		 this.matrix  = new ArrayList<HashMap<Integer, Integer>>();
		 ArrayList<RNode> newNodes = new ArrayList<RNode>();
			
		 newNodes.add(todetermine.firstNode);
		 // TODO check why is not newNodes.addAll(todete...getAllDi...)
		 todetermine.firstNode.getAllDirectNode(newNodes);	
		 this.addNodeToPile(newNodes);
		 matrix.add(new HashMap<Integer, Integer>());
	}
	 

	private int addNodeToPile(ArrayList<RNode> newNodes)
	{
		String node_name;
	
		newNodes.sort((o1, o2) -> (o1.ID - o2.ID));
		node_name = newNodes.toString();
		System.out.print("tente ajout nouveau" + node_name);
	
		if(!idGroups.containsKey(node_name))
		{
			idGroups.put(node_name, nbNewNode);
			
			endNodesArrayList.add(0);
			pileGroups.add(newNodes);
			for (RNode n1: newNodes)
			{
				if (n1.ID == idEnd)
					endNodesArrayList.set(endNodesArrayList.size() - 1, 1);
			} 
			System.out.println("  Sucess numero" + nbNewNode);
			
			this.nbNewNode++;
			return this.nbNewNode-1;
	}
	System.out.println("  Failure");
	return idGroups.get(node_name);
}

	public  Automate AutomateTransition(Automate aust) {
	List<RNode>[] tab = new List[255];
	ArrayList<RNode> newNodes;
	int i; 
	int idOrigineNode; 

	for (i = 0; i < 255; i++)
		tab[i] = new ArrayList<RNode>();
	
	System.out.println("Determinastion, debut boucle");
	while(pileGroups.size() > 0) // tant qu'il y'a de nouveaux noeud non trait�
	{
		newNodes = pileGroups.remove(0);
		newNodes.sort((o1, o2) -> (o1.ID - o2.ID));
		idOrigineNode = this.idGroups.get(newNodes.toString());
		System.out.println("pop pile liste " + newNodes.size() );
		for (i = 0; i < 255; i++)
			tab[i].clear();
		for (RNode node : newNodes) {
			System.out.println("    Node " + node.ID);
			
			for (Map.Entry<Integer, RNode> entry : node.next.entrySet()) {
				tab[entry.getKey()].add(entry.getValue());
				tab[entry.getKey()].addAll(entry.getValue().getAllDirectNode(new ArrayList<RNode>()));
				System.out.println("        voisin case " + entry.getKey() + "nb: " + tab[entry.getKey()].size() );
			}
		}
		
		for (i = 0; i < 255; i++) {
			if (!tab[i].isEmpty())
			{
				newNodes = new ArrayList<RNode>();
				newNodes.addAll(tab[i]);
				
				int pos = addNodeToPile(newNodes);
				if (pos == nbNewNode - 1)
					matrix.add(new HashMap<Integer, Integer>());
				matrix.get(idOrigineNode).put(i, pos);
			}
		}
	}
	printDeterministAutomaton();
	
	
	return null;
}
	
	void printDeterministAutomaton()
	{
		int i;
		String s;
		for (Map.Entry<String, Integer> entry : idGroups.entrySet()) {
			String isStart,isEnd;
			isStart = entry.getValue() == 0 ? "Etat initial" : "";
			isEnd = endNodesArrayList.get(entry.getValue()) > 0 ? "Etat final" : "";
			System.out.println("Node new : " + entry.getKey() + " -> " + entry.getValue() + isStart +" "+ isEnd);
		}
		i = 0;
		for (HashMap<Integer, Integer> next : this.matrix) {
			System.out.println(i + ": " );
			for (Map.Entry<Integer, Integer> entry : next.entrySet())
				System.out.println("     --" +  entry.getKey() + "-->" + entry.getValue() );
			System.out.println();	
			i++;
		}
/*		for (int j = 0; j < this.nbNewNode; j++) {
			System.out.print("____");
		}
		System.out.println("____");
		for(int i = 0; i< this.nbNewNode; i++)
		{
			System.out.print("  |");
			for (int j = 0; j < this.nbNewNode; j++) {
				s = this.matrix[i][j] == 0 ? "00" : "" +this.matrix[i][j] ;   
				System.out.print(s + "  ");
				
			}
			System.out.println("|");
				
		}
		for (int j = 0; j < this.nbNewNode; j++) {
			System.out.print("---");
		}
		System.out.println("____");
	*/	
	}
}

