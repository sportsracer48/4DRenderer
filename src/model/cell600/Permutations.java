package model.cell600;

import java.util.ArrayList;

public class Permutations
{
	public static final float PHI=(float) ((1+Math.sqrt(5))/2f);
	
	public static void man()//turned off without []
	{
		printPM(.5f,.5f,.5f,.5f,true,true,true,true);
		for(int i=0;i<4;i++)
		{
			int[] vals=new int[4];
			vals[i]=1;
			printPM(vals[0],vals[1],vals[2],vals[3],vals[0]==1,vals[1]==1,vals[2]==1,vals[3]==1);
		}
		for(float[] perm:getEvenPermutations(.5f*PHI,.5f,.5f/PHI,0))
		{
			printPM(perm[0],perm[1],perm[2],perm[3],perm[0]!=0,perm[1]!=0,perm[2]!=0,perm[3]!=0);
		}
	}
	
	public static void printPM(float x, float y, float z, float w, boolean xFlag, boolean yFlag, boolean zFlag, boolean wFlag)
	{
		for(int xs=xFlag?-1:1;xs<=1;xs+=2)
		{
			for(int ys=yFlag?-1:1;ys<=1;ys+=2)
			{
				for(int zs=zFlag?-1:1;zs<=1;zs+=2)
				{
					for(int ws=wFlag?-1:1;ws<=1;ws+=2)
					{
						System.out.println("v "+xs*x+" "+ys*y+" "+zs*z+" "+ws*w);
					}
				}
			}
		}
	}
	
	static int[][] twoSwaps=
			{
				{0,2,3,1}, 
				{0,3,1,2},
				{1,0,3,2}, 
				{1,2,0,3}, 
				{1,3,2,0},
				{2,0,1,3}, 
				{2,1,3,0}, 
				{2,3,0,1}, 
				{3,0,2,1}, 
				{3,1,0,2},
				{3,2,1,0}
			};
	
	public static ArrayList<float[]> getEvenPermutations(float x, float y, float z, float w)
	{
		float[] orig={x,y,z,w};
		ArrayList<float[]> evenPermutations=new ArrayList<float[]>();
		evenPermutations.add(orig);
		for(int i=0;i<twoSwaps.length;i++)
		{
			float[] newPermutation={orig[twoSwaps[i][0]],
									orig[twoSwaps[i][1]],
									orig[twoSwaps[i][2]],
									orig[twoSwaps[i][3]]
			};
			
			if(!containsPermutation(evenPermutations,newPermutation))
			{
				evenPermutations.add(newPermutation);
			}
		}
		return evenPermutations;
	}

	public static boolean containsPermutation(ArrayList<float[]> evenPermutations, float[] newPermutation)
	{
		for(float[] permutation:evenPermutations)
		{
			if(permutation[0]==newPermutation[0] &&
			   permutation[1]==newPermutation[1] &&
			   permutation[2]==newPermutation[2] &&
			   permutation[3]==newPermutation[3])
			{
				return true;
			}
		}
		return false;
	}
}
