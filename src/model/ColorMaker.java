package model;

public class ColorMaker
{
	final float rc,bc,gc;
	final float rr,br,gr;
	float alpha=1;
	public ColorMaker(float rc, float rr,
					  float gc, float gr,
					  float bc, float br
			)
	{
		this.rr=rr;
		this.rc=rc;
		this.bc=bc;
		this.br=br;
		this.gc=gc;
		this.gr=gr;
	}
	
	public void setAlpha(float alpha)
	{
		this.alpha=alpha;
	}
	public float getRed()
	{
		return rc+ (float)(Math.random()-.5f)*rr;
	}
	public float getGreen()
	{
		return gc+ (float)(Math.random()-.5f)*gr;
	}
	public float getBlue()
	{
		return bc+ (float)(Math.random()-.5f)*br;
	}
	public float getAlpha()
	{
		return alpha;
	}
}
