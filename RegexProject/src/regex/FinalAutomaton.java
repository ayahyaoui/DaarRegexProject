package regex;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FinalAutomaton {
	int nbNode;
	final int SIZEOMEGA = 255;
	int [][]matrix;
	boolean []endsNode;
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	
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

	
	public int findPattern2(String text, int startIndex)
	{
		int currentNode = 1;
		int i = 0;
		
		for (i = startIndex; i < text.length() && !endsNode[currentNode] && currentNode > 0; i++)
		{
			if ((int)text.charAt(i) > 255)
				currentNode = 0;
			else
				currentNode = matrix[currentNode][(int)text.charAt(i)];
		}
		if (endsNode[currentNode])
			return i;
		return -1; // didn't find anything
		//return findPattern(text, startIndex + 1);
	}

	public void egrep(BufferedReader reader, String fileName) throws IOException
	{	
		String line = reader.readLine();
		int index;
		BufferedWriter writer;
		int indexName = 1;
		int nbFound = 0;

		if (RegEx.ONFILE)
		{
			while (new File(fileName + indexName).isFile())
				indexName++;
			writer = new BufferedWriter(new FileWriter(fileName + indexName));

		} 
		

		//line = new String(line.getBytes("ISO-8859-1"));
		while (line != null)
		{
			for(int i = 0; i < line.length(); i++)
			{
				index = findPattern2(line, i);
				if (index > 0)
				{
					if (RegEx.ONFILE)
						writer.write(line.substring(0, i) + ANSI_RED +  line.substring(i,index) + ANSI_RESET + line.substring(index));
					if (RegEx.DISPLAY)
						System.out.println(line.substring(0, i) + ANSI_RED +  line.substring(i,index) + ANSI_RESET + line.substring(index)); 
					nbFound++;
					break;
					
				}
			}
			line = reader.readLine();
	
			
			//byte[] bytes = line.getBytes("UTF-8");
			//line = new String(bytes);
			//line = new String(line.getBytes("ISO-8859-1"));
		}

		System.out.println("RESULT FOUND " + nbFound + " OCURRENCE.");
		reader.close();
		if (RegEx.ONFILE)
			writer.close();
	}
}


