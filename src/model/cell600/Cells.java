package model.cell600;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Cells
{
	public static final float len=1/Permutations.PHI;
	static ArrayList<Vertex> verts=new ArrayList<Vertex>();
	public static void main(String[] args) throws IOException
	{
		File f=new File("600cell.obj4D");
		BufferedReader in=new BufferedReader(new FileReader(f));
		String line;
		

		int nextId=0;
		
		for(line=in.readLine();line!=null;line=in.readLine())
		{
			if(line.startsWith("v"))
			{
				verts.add(new Vertex(line,nextId));
				nextId++;
			}
		}
		in.close();
		
		for(Vertex v:verts)
		{
			v.addAllNearby(verts, len);
		}
		
		HashSet<Cell> allCells=new HashSet<Cell>();
		
		for(Vertex v:verts)
		{
			allCells.addAll(v.getAllCells());
		}
		
		for(Cell c:allCells)
		{
			System.out.println(c);
		}
	}
}
