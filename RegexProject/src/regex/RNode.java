package regex;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;




class RNode {
	protected static int compteur; // Usefull to set ID 
	protected final int ID;
	protected Map<Integer, RNode> next;
	protected ArrayList<RNode> epsilon;
	
	public RNode() {
		RNode.compteur ++;
		this.next = new HashMap<Integer, RNode>();
		this.epsilon =new ArrayList<RNode>();
		ID = compteur;

	}
	
	@Override
	public String toString() {
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
	
	public void addLink(RNode nextNode, int value)
	{
		if (!next.containsKey(value))
			next.put(value, nextNode);
	}

	public void addEpsilon(RNode next)
	{
		if (! epsilon.contains(next))
			epsilon.add(next);
	}
}
