package regex;
import java.util.ArrayList;

public class Automate {
	protected RNode firstNode;
	protected RNode endNode;
	

	public Automate(int code) {
		this.firstNode = new RNode();
		this.endNode = new RNode();
		  if (code != RegEx.DOT)
		  	firstNode.addLink(endNode, code);
		  else 
			  for (int i = 1; i < 255; i++)
			  	firstNode.addLink(endNode, i);
	}
	
	public Automate() {
		this.firstNode = new RNode();
		this.endNode = new RNode();
		
	}

		
	@Override
	public String toString() {
		String res = "NodeStart: " + firstNode.ID + "\n";
		res += "NodEnd: " + endNode.ID + "\n_______________\n";
		res += firstNode.afficheString(new ArrayList<Integer>() );
	
		return res;
	}	
}