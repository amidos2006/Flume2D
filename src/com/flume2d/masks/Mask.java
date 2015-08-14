package com.flume2d.masks;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.flume2d.Entity;

public abstract class Mask implements Disposable
{
	public Entity parent;
	public float x;
	public float y;
	
	public Mask()
	{
		x = 0;
		y = 0;
		parent = null;
	}
	
	public boolean collide(Mask mask)
	{
		return getBounds().overlaps(mask.getBounds());
	}
	
	public abstract Rectangle getBounds();
}
