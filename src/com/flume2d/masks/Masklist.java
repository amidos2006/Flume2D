package com.flume2d.masks;

import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.math.Rectangle;

public class Masklist extends Mask
{
	public Masklist()
	{
		masks = new LinkedList<Mask>();
	}
	
	public Masklist(Mask[] masks)
	{
		this();
		
		for	(int i = 0; i < masks.length; i++)
		{
			this.masks.add(masks[i]);
		}
	}
	
	@Override
	public void dispose() 
	{
		Iterator<Mask> it = masks.iterator();
		while(it.hasNext())
		{
			Mask m = it.next();
			m.dispose();
		}
	}
	
	public void add(Mask m)
	{
		masks.add(m);
	}
	
	public void remove(Mask m)
	{
		masks.remove(m);
	}
	
	@Override
	public boolean collide(Mask mask)
	{
		if(super.collide(mask))
		{
			Iterator<Mask> it = masks.iterator();
			while(it.hasNext())
			{
				Mask m = it.next();
				if(m.collide(mask))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public Rectangle getBounds() {
		float minX, minY, maxX, maxY;
		minX = minY = maxX = maxY = 0;
		boolean noMinX, noMinY, noMaxX, noMaxY;
		noMinX = noMinY = noMaxX = noMaxY = true;
		
		Iterator<Mask> it = masks.iterator();
		while(it.hasNext())
		{
			Rectangle bounds = it.next().getBounds();
			if(noMinX || minX > bounds.x)
			{
				noMinX = false;
				minX = bounds.x;
			}
			if(noMinY || minY > bounds.y)
			{
				noMinY = false;
				minY = bounds.y;
			}
			if(noMaxX || maxX < bounds.x + bounds.width)
			{
				noMaxX = false;
				maxX = bounds.x + bounds.width;
			}
			if(noMaxY || maxY < bounds.y + bounds.height)
			{
				noMaxY = false;
				maxY = bounds.y + bounds.height;
			}
		}
		return new Rectangle(minX, minY, maxX - minX, maxY - minY);
	}
	
	private LinkedList<Mask> masks;
}
