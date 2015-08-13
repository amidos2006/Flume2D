package com.flume2d.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.flume2d.F2D;

public class Image implements Graphic
{
	public float originX, originY;
	public int frameX, frameY;
	public int frameWidth, frameHeight;
	public int imageWidth, imageHeight;
	
	public Color color;
	public float angle;
	public float scale;
	
	public Image(String filename)
	{
		this(filename, null);
	}
	
	public Image(String filename, Rectangle rectangle)
	{
		image = new Texture(Gdx.files.internal(filename));
		if(F2D.smooth)
		{
			image.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		else
		{
			image.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		}
		imageWidth = frameWidth = image.getWidth();
		imageHeight = frameHeight = image.getHeight();
		frameX = frameY = 0;
		if(rectangle != null)
		{
			frameX = (int)rectangle.x;
			frameY = (int)rectangle.y;
			frameWidth = (int)rectangle.width;
			frameHeight = (int)rectangle.height;
		}
		
		color = Color.WHITE.cpy();
		originX = originY = 0;
		scale = 1;
	}
	
	public void setOrigin(int x, int y)
	{
		originX = x;
		originY = y;
	}
	
	public void CenterOO()
	{
		originX = frameWidth / 2;
		originY = frameHeight / 2;
	}

	@Override
	public void render(SpriteBatch spriteBatch, float x, float y)
	{
		TextureRegion region = new TextureRegion(image, frameX, frameY, frameWidth, frameHeight);
		region.flip(false, true);
		spriteBatch.setColor(color);
		spriteBatch.draw(region, x - originX, y - originY, originX, originY, frameWidth, frameHeight, scale, scale, angle);
	}
	
	@Override
	public void dispose()
	{
		image.dispose();
	}

	@Override public void update(float dt) { }
	@Override public boolean isActive() { return false; }
	@Override public boolean isVisible() { return true; }
	
	protected Texture image;

}
