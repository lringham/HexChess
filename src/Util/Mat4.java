package Util;

public class Mat4
{
	private float[] elements;
		
	public Mat4(float[] elements)
	{		
		
		if(elements.length != 16)
		{
			Util.DebugLog.writeError("Invalid number of elements: "+elements.length);
			System.exit(1);
		}
		else
		{
			this.setElements(elements);
		}	
	}
	
	public Mat4()
	{
		toIdentity();
	}

	public void toIdentity()
	{
		elements = new float[] {
			1f, 0f, 0f, 0f,
			0f, 1f, 0f, 0f,
			0f, 0f, 1f, 0f,
			0f, 0f, 0f, 1f				
		};
	}
	
	public float[] getElements()
	{
		return elements;
	}
	
	public void setElements(float[] elements)
	{
		this.elements = elements;
	}
	

	
	public float[] toFloats()
	{
		return elements;	
	}

	public float get(int i)
	{
		return elements[i];
	}
	
	static public Mat4 position(Point point)
	{
		return new Mat4( 
				new float[] {
						1f,			0,			0,			0,
						0, 			1f,			0,			0,
						0, 			0,			1f,			0,
						point.x,	point.y,	point.z,	1f
				});
	}	
	
	static public Mat4 scale(Vector3 scale)
	{
		return new Mat4( 
				new float[] {
						scale.x, 	0,			0,			0,
						0, 			scale.y,	0,			0,
						0, 			0,			scale.z,	0,
						0, 			0,			0,			1f
				});
	}

	static public Mat4 rotateZ(float rotation)
	{

		float sinTheta = (float) Math.sin(rotation);
		float cosTheta = (float) Math.cos(rotation);
		
		return new Mat4( 
				new float[] {
						cosTheta, 	-sinTheta,	0,		0,
						sinTheta, 	cosTheta,	0,		0,
						0, 			0,			1f,		0,
						0, 			0,			0,		1f
				});	
	}

	public void add(Mat4 matrix)
	{
		for(int i=0; i<16; i++)
			elements[i] = elements[i] + matrix.get(i);		
	}
	
	public void sub(Mat4 matrix)
	{
		for(int i=0; i<16; i++)
			elements[i] = elements[i] - matrix.get(i);		
	}
	
	public void mul(Mat4 matrix)
	{
		float[] newElements = new float[16];
		for(int i=0;i<16;i++)
		{
			float[] row = getRow(i / 4);
			float[] col = getCol(i % 4);
			
			newElements[i] = row[0]*col[0] + row[1]*col[1] + row[2]*col[2] + row[3]*col[3];							
		}
		elements = newElements;
	}
	
	public float[] getRow(int row)
	{
		row = row*4;
		return new float[] {elements[row], elements[row+1], elements[row+2], elements[row+3]};
	}
	
	public float[] getCol(int col)
	{
		return new float[] {elements[col], elements[col+4], elements[col+8], elements[col+12]};
	}
	
	public static Mat4 add(Mat4 matrix1, Mat4 matrix2)
	{
		float[] sum = new float[16];
		
		for(int i=0; i<16; i++)
			sum[i] = matrix1.get(i) + matrix2.get(i);
		
		return new Mat4(sum);			
	}	

	public static Mat4 sub(Mat4 matrix1, Mat4 matrix2)
	{
		float[] difference = new float[16];
		
		for(int i=0; i<16; i++)
			difference[i] = matrix1.get(i) - matrix2.get(i);
		
		return new Mat4(difference);		
	}
	
	public static Mat4 mul(Mat4 matrix1, Mat4 matrix2)
	{
		float[] elements = new float[16];
		for(int i=0;i<16;i++)
		{
			float[] row = matrix1.getRow(i/4);
			float[] col = matrix2.getCol(i%4);
			
			elements[i] = row[0]*col[0] + row[1]*col[1] + row[2]*col[2] + row[3]*col[3];							
		}
		return new Mat4(elements);
	}		
	
	public static Mat4 orthoProjRowMajor(float left, float right, float bottom,  float top, float near, float far)
	{
		return new Mat4(
				new float[]
				{2f/(right-left),	0f,						0f, 				(-(right+left))/(right-left),
				0f,					2f/(top-bottom),		0f, 				(-(top+bottom))/(top-bottom),
				0f,					0f,						-2f/(far-near),		(-(far+near))/(far-near),
				0f,					0f,						0f,					1f						  });
	}	
	
	public static Mat4 orthoProjColumnMajor(float left, float right, float bottom,  float top, float near, float far)
	{
		return new Mat4(
				new float[]
				{2f/(right-left),				0f,								0f,							0f,				
				0f,								2f/(top-bottom),				0f,							0f,			
				0f,								0f,								-2f/(far-near),				0f,				
				(-(right+left))/(right-left),	(-(top+bottom))/(top-bottom),	(-(far+near))/(far-near),	1f});
	}
	
	public void print()
	{
		for(int i=0;i<16;i++)
		{
			if(i%4 == 0)
				System.out.print("\n");
			
			System.out.print("\t"+elements[i]);
			
			if((i+1)%4 != 0)
				System.out.print(",");
		}
	}
}
