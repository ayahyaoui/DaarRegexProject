package regex;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
//import java.util.Set;



class RNode {
	//protected boolean isStart;
	//protected boolean isEnd;
	protected static int compteur;
	protected final int ID;
	protected Map<Integer, RNode> next;
	protected ArrayList<RNode> epsilon;
	
	public RNode() {
		RNode.compteur ++;
		this.next = new HashMap<Integer, RNode>();
		this.epsilon =new ArrayList<RNode>();
		ID = compteur;
		System.out.println(ID);	
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "" + ID;
	}
    
		
	public String afficheString(ArrayList<Integer> seen) {
		String res= "" + this.ID + ": \n";
		if (seen.contains(ID))
			return "";
		for (Map.Entry<Integer, RNode> entry : next.entrySet()) {
			res += "     --"+ entry.getKey() + "-->" + entry.getValue().ID + "\n";
			
		}
		if( epsilon.size() > 0)
			res += "Epsilon:"  ;
		for (RNode node : epsilon){
			res += node.ID + "  //";
		}
		res += "=============\n";
		seen.add(ID);
		for (Map.Entry<Integer, RNode> entry : next.entrySet()) {
			res += entry.getValue().afficheString(seen) + "\n";
		}
		for (RNode node : epsilon){
			res += node.afficheString(seen) + "\n";
		}
		
		return res;
	}
	
	public ArrayList<RNode> getAllDirectNode(ArrayList<RNode> res) {
		for (RNode node : this.epsilon) {
			if (!res.contains(node))
			{
				res.add(node);
				res = node.getAllDirectNode(res);
			}
		}
		return res;
	}
	
	/*
	
	
	public RNode(boolean isStart, boolean isEnd, Map<Integer, RNode> next, ArrayList<RNode> epsilon) {
		this.isStart = isStart;
		this.isEnd = isEnd;
		this.next = next;
		this.epsilon = epsilon;
	}
// Cf@InSt@-$tUd3nT
	public RNode(boolean isStart, boolean isEnd, Map<Integer, RNode> next) {
		this.isStart = isStart;
		this.isEnd = isEnd;
		this.next = next;
		this.epsilon = new ArrayList<RNode>();
	}

	public RNode(boolean isStart, boolean isEnd) {
		this.isStart = isStart;
		this.isEnd = isEnd;
		this.next = new HashMap<Integer, RNode>();
		this.epsilon =new ArrayList<RNode>();
	}
	*/
}
