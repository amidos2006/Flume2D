package com.flume2d.masks;

import com.badlogic.gdx.math.Rectangle;

public class Hitbox extends Mask
{
	public float originX;
	public float originY;
	public float width;
	public float height;
	
	public Hitbox(float width, float height)
	{
		this.originX = 0;
		this.originY = 0;
		this.width = width;
		this.height = height;
	}
	
	public Hitbox(float originX, float originY, float width, float height)
	{
		this.originX = originX;
		this.originY = originY;
		this.width = width;
		this.height = height;
	}
	
	public void centerOO()
	{
		this.originX = width/2;
		this.originY = height/2;
	}
	
	@Override
	public void dispose() 
	{
		
	}
	
	@Override
	public boolean collide(Mask mask)
	{
		if(super.collide(mask))
		{
			if(mask instanceof Grid)
			{
				return ((Grid)mask).collide(this);
			}
			
			if(mask instanceof Pixelmask)
			{
				return ((Masklist)mask).collide(this);
			}
			
			if(mask instanceof Hitbox)
			{
				return true;
			}
			
			if(mask instanceof Masklist)
			{
				return ((Masklist)mask).collide(this);
			}
		}
		
		return false;
	}
	
	@Override
	public Rectangle getBounds() {
		Rectangle rect = new Rectangle();
		rect.x = parent.x + x - originX;
		rect.y = parent.y + y - originY;
		rect.width = width;
		rect.height = height;
		
		return rect;
	}
}
