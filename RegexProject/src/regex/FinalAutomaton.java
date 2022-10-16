package regex;

import java.io.BufferedReader;
import java.io.IOException;

public class FinalAutomaton {
	int nbNode;
	final int SIZEOMEGA = 255;
	int [][]matrix;
	boolean []endsNode;
	
	public FinalAutomaton(int nbNode, int[][] matrix, boolean []endsNode) {
		this.nbNode = nbNode;
		this.matrix = matrix;
		this.endsNode = endsNode;
		System.out.println(nbNode + " " + matrix.length + " " + endsNode.length);
	}
	
	public void displayAutomaton()
	{
		int i;
		
		System.out.println("final automaton (size=" + (nbNode - 1) + "), (nodeStart=1)");
		System.out.print("Final node:");// + endsNode.toString());
		for (i = 0; i < nbNode; i++)
			if (this.endsNode[i])
				System.out.print(i+ " ");
		System.out.println();
		for (i = 1 ; i < nbNode ; i++)
		{
			System.out.println("Node " + i + ": ");
			for (int j = 0; j < SIZEOMEGA; j++)
			{
				if(matrix[i][j] > 0)
				{
					if (j > 64 && j <= 90 || (j > 96 && j <= 122))
						System.out.print("    == " + (char)j + " ==>");
					else
						System.out.print("    == " + (char)j + " ==>");
					System.out.println(matrix[i][j]);
				}
			}
			System.out.println("\n");
		}
	}
	
	public int findPattern(String text, int startIndex)
	{
		int currentNode = 1;
		int i;
		
		if (startIndex == text.length())
			return -1; // didn't find anything
		for (i = startIndex; i < text.length() && currentNode > 0 && !endsNode[currentNode]; i++)
			currentNode = matrix[currentNode][(int)text.charAt(i)];
		if (endsNode[currentNode])
			return i;
		return findPattern(text, startIndex + 1);
	}

	public void egrep(BufferedReader reader) throws IOException
	{	
		String line = reader.readLine();
		Boolean found;
		int index;
		int nbLine = 1;

		while (line != null)
		{
			found = false;
			for(int i = 0; i < line.length(); i++)
			{
				index = findPattern(line, i);
				if (index > 0)
				{
					System.out.println(line.substring(i,index)); // TODO show entire line with pattern in red
					found = true;
					break;

				}
			}
			if (found)
				System.out.println("number line " + nbLine + ": " + line);
			line = reader.readLine();
			nbLine++;
		}
		reader.close();
	}
}

