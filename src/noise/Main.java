package noise;

public class Main
{
	public static void main(String[] args)
	{
		int size=32;
		SimplexNoise simplexNoise=new SimplexNoise(size,.01,(int) (
				Integer.MAX_VALUE*(
						(double)System.currentTimeMillis()/Long.MAX_VALUE
						)
				));
		int width=size;
		int height=size;
		int depth=size;

	    double[][] result=new double[width][height*depth];

	    for(int x=0;x<width;x++)
	    {
	    	for(int y=0;y<width;y++)
		    {
	    		for(int z=0;z<depth;z++)
	    		{
	    			result[x][z*height+y]=simplexNoise.getNoise(x, y, z);
	    		}
		    }
			System.out.println(x);
	    }
	    
	    ImageWriter.greyWriteImage(result);

	}
}
