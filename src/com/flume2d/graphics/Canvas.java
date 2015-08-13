package com.flume2d.graphics;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.flume2d.F2D;

public class Canvas implements Graphic
{	
	public Canvas(int width, int height)
	{
		canvas = new Pixmap(width, height, Pixmap.Format.RGB565);
		texture = new Texture(canvas);
		if(F2D.smooth)
		{
			Pixmap.setFilter(Pixmap.Filter.BiLinear);
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		else
		{
			Pixmap.setFilter(Pixmap.Filter.NearestNeighbour);
			texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		}
		clear(Color.BLACK);
		setColor(1, 1, 1, 1);
	}
	
	public void setColor(float r, float g, float b, float a)
	{
		canvas.setColor(r, g, b, a);
	}
	
	public void lineTo(int x1, int y1, int x2, int y2)
	{
		canvas.drawLine(x1, y1, x2, y2);
	}
	
	public void drawRect(int x, int y, int width, int height, boolean isOutline)
	{
		if(isOutline)
		{
			canvas.drawRectangle(x, y, width, height);
		}
		else
		{
			canvas.fillRectangle(x, y, width, height);
		}
	}
	
	public void drawCircle(int x, int y, int radius, boolean isOutline)
	{
		if(isOutline)
		{
			canvas.drawCircle(x, y, radius);
		}
		else
		{
			canvas.fillCircle(x, y, radius);
		}
	}
	
	public void setPixel(int x, int y)
	{
		canvas.drawPixel(x, y);
	}
	
	public void clear(Color clearColor)
	{
		setColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
		canvas.fillRectangle(0, 0, canvas.getWidth(), canvas.getHeight());
	}
	
	@Override
	public boolean isVisible()
	{
		return true;
	}

	@Override
	public void render(SpriteBatch spriteBatch, float x, float y)
	{
		spriteBatch.draw(texture, x, y);
	}

	@Override
	public boolean isActive()
	{
		return false;
	}

	@Override
	public void update(float dt)
	{
	}
	
	@Override
	public void dispose()
	{
		texture.dispose();
		canvas.dispose();
	}
	
	private Pixmap canvas;
	private Texture texture;
}
