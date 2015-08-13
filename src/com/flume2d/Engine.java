package com.flume2d;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * The game engine singleton class
 * @author matt.tuttle
 */
public class Engine implements ApplicationListener
{	
	@Override
	public void create()
	{
		spritebatch = new SpriteBatch();
		
		F2D.Initialize(this);
		
		//Gdx.input.setInputProcessor(new Input());
		running = true;
	}

	@Override
	public void dispose()
	{
		spritebatch.dispose();
		F2D.background.dispose();
		
		if (currentWorld != null)
			currentWorld.dispose();
		
		if(newWorld != null)
			newWorld.dispose();
	}

	@Override
	public void pause()
	{
		running = false;
	}

	@Override
	public void render()
	{
		if (newWorld != null)
		{
			currentWorld.dispose();
			currentWorld = newWorld;
			newWorld = null;
		}
		
		if(currentWorld != null)
		{
			currentWorld.update(Gdx.graphics.getDeltaTime());
		}
		
//		Input.update();
		F2D.viewport.apply();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(currentWorld != null)
		{
			currentWorld.render(spritebatch);
		}
	}

	@Override
	public void resize(int width, int height)
	{
		F2D.windowWidth = width;
		F2D.windowHeight = height;
		F2D.viewport.update(width, height);
	}

	@Override
	public void resume()
	{
		running = true;
	}
	
	public boolean IsRunning()
	{
		return running;
	}
	
	public static void SetWorld(World newWorld)
	{
		if (Engine.currentWorld == null)
			Engine.currentWorld = newWorld;
		else
			Engine.newWorld = newWorld;
	}
	
	private boolean running;
	private static World currentWorld;
	private static World newWorld;
	
	private SpriteBatch spritebatch;
}
