package com.flume2d.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.flume2d.F2D;

public class Backdrop implements Graphic, Disposable
{
	public Backdrop(String filename)
	{
		background = new Texture(Gdx.files.internal(filename));
		background.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		if(F2D.smooth)
		{
			background.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		else
		{
			background.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		}
		imageWidth = background.getWidth();
		imageHeight = background.getHeight();
		
		scrollX = 0;
		scrollY = 0;
		color = Color.WHITE.cpy();
		scale = 1;
		parallex = 1;
	}
	
	@Override
	public void dispose() {
		background.dispose();
	}
	
	@Override
	public void update(float dt) { }
	
	@Override
	public void render(SpriteBatch spriteBatch, float x, float y) {
		int numHorizontal = (int)Math.ceil(F2D.viewport.getWorldWidth() / (imageWidth * scale));
		int numVertical = (int)Math.ceil(F2D.viewport.getWorldHeight() / (imageHeight * scale));
		
		spriteBatch.setColor(color);
		for (int i = 0; i < numVertical; i++)
		{
			for(int j = 0; j < numHorizontal; j++)
			{
				float currentX = x + j * imageWidth * scale - F2D.viewport.getWorldWidth() / 2;
				float currentY = y + i * imageHeight * scale - F2D.viewport.getWorldHeight() / 2;
				float u = (scrollX + F2D.camera.position.x) * parallex / imageWidth;
				float v = (scrollY + F2D.camera.position.y) * parallex / imageHeight;
				spriteBatch.draw(background, currentX, currentY, imageWidth * scale, imageHeight * scale, u, v, u + 1, v + 1);
			}
		}
	}

	@Override
	public boolean isActive() {
		return false;
	}
	
	@Override
	public boolean isVisible() {
		return true;
	}
	
	public float scrollX;
	public float scrollY;
	public Color color;
	public float scale;
	public float parallex;
	
	private Texture background;
	private int imageWidth;
	private int imageHeight;
}
