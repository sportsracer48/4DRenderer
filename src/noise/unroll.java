package noise;

public class unroll
{
	public static void main(String[] args)
	{
//		for(int i=0; i<6;i++)
//		{
//			System.out.println(
//					  "if(!has3)\n"
//					+ "{\n"
//					+ "edge=vPos[end"+i+"]-vPos[start"+i+"];\n"
//					+ "u=-vPos[start"+i+"].w/edge.w;\n"
//					+ "if(u>=0 && u<=1)\n"
//					+ "{");
//			
//			for(int j=0;j<4;j++)
//			{
//				System.out.println(
//						  (j==0?"":"else ")
//						+ "if(!has"+j+")"
//						+ "{"
//						+ "has"+j+"=true;"
//						+ "edge"+j+"="+i+";"
//						+ "p"+j+"=vPos[start"+i+"]+u*edge;"
//						+ "c"+j+"=vColor[start"+i+"]*(1-u)+vColor[end"+i+"]*u;"
//						+ "n"+j+"=vNorm[start"+i+"]*(1-u)+vNorm[end"+i+"]*u;"
//						+ "}");
//			}
//			
//			System.out.println(
//					  "}\n"
//					+ "}\n");
//		}
		
		for(int j=0;j<2;j++)
		{
			for(int i=0;i<6;i++)
			{
				System.out.println(
						"if(edge"+j+"=="+i+" && edge"+(j+1)+"==noSearch"+i+")"
						+"{"
						+"temp=edge"+(j+1)+";"
						+"edge"+(j+1)+"=edge"+(j+2)+";"
						+"edge"+(j+2)+"=temp;"
						+"tempP=p"+(j+1)+";"
						+"p"+(j+1)+"=p"+(j+2)+";"
						+"p"+(j+2)+"=tempP;"
						+"tempC=c"+(j+1)+";"
						+"c"+(j+1)+"=c"+(j+2)+";"
						+"c"+(j+2)+"=tempC;"
						+"tempN=n"+(j+1)+";"
						+"n"+(j+1)+"=n"+(j+2)+";"
						+"n"+(j+2)+"=tempN;"
						+"}"
						);
			}
		}

	}
	
}
