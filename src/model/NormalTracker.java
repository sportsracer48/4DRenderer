package model;

public class NormalTracker
{
	float[] sum=new float[4];
	int num;
	
	public NormalTracker()
	{
		
	}
	
	public void add(float[] cellNorm)
	{
		for(int i=0;i<4;i++)
		{
			sum[i]+=cellNorm[i];
		}
		num++;
	}
	
	public float[] getNormal()
	{
		float[] norm=new float[4];
		for(int i=0;i<4;i++)
		{
			norm[i]=sum[i]/num;
		}
		
		return norm;
	}
}
