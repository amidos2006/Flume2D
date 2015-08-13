package com.flume2d.graphics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

public interface Graphic extends Disposable
{
	public boolean isVisible();
	public void render(SpriteBatch spriteBatch, float x, float y);
	
	public boolean isActive();
	public void update(float dt);
	public void dispose();
}
