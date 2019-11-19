package RenderingSystem;


public class DrawLayer 
{
	public VertexBuffer vertBuff = null;
	public ElementBuffer elemBuff = null;
	public DrawLayer(VertexBuffer vertbuff, ElementBuffer eleBuff)
	{
		this.vertBuff = vertbuff;
		this.elemBuff = eleBuff;
	}
}
