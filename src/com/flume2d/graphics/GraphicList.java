package com.flume2d.graphics;

import java.util.*;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GraphicList implements Graphic
{
	
	public GraphicList()
	{
		graphics = new LinkedList<Graphic>();
	}
	
	public GraphicList(Graphic[] g)
	{
		graphics = new LinkedList<Graphic>();
		for (int i = 0; i < g.length; i++)
		{
			add(g[i]);
		}
	}
	
	public void add(Graphic g)
	{
		graphics.add(g);
	}
	
	public void remove(Graphic g)
	{
		graphics.remove(g);
	}

	@Override
	public boolean isVisible()
	{
		return true;
	}

	@Override
	public void render(SpriteBatch spriteBatch)
	{
		Iterator<Graphic> it = graphics.iterator();
		while(it.hasNext())
		{
			Graphic graphic = (Graphic) it.next();
			if (graphic.isVisible()) graphic.render(spriteBatch);
			if (graphic.isActive()) graphic.update();
		}
	}

	@Override
	public boolean isActive()
	{
		return false;
	}

	@Override
	public void update()
	{
	}
	
	private LinkedList<Graphic> graphics;

}
