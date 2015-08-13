package com.flume2d.graphics;

import java.util.*;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.flume2d.callback.IVoidVoid;

public class Spritemap extends Image
{
	public Spritemap(String filename, int width, int height)
	{
		this(filename, width, height, null);
	}
	
	public Spritemap(String filename, int width, int height, IVoidVoid endFunction)
	{
		super(filename);
		frameWidth = width;
		frameHeight = height;
		regions = TextureRegion.split(image, frameWidth, frameHeight);
		cells = imageWidth / frameWidth;
		
		animations = new HashMap<String, Animation>();
		this.endFunction = endFunction;
		animationFinished = false;
		frameTime = 0;
		currentAnim = null;
	}
	
	public void add(String name, int[] frames)
	{
		add(name, frames, 0, true);
	}
	
	public void add(String name, int[] frames, float delay)
	{
		add(name, frames, delay, true);
	}
	
	public void add(String name, int[] list, float fps, boolean loop)
	{
		TextureRegion[] frames = new TextureRegion[list.length];
		for(int i=0; i<list.length;i++)
		{
			frames[i] = regions[list[i] / cells][list[i] % cells];
		}
		Animation a = new Animation(fps / list.length, frames);
		a.setPlayMode(Animation.PlayMode.NORMAL);
		if(loop)
		{
			a.setPlayMode(Animation.PlayMode.LOOP);
		}
		animations.put(name, a);
	}
	
	public void play(String name)
	{
		play(name, false);
	}
	
	public void play(String name, boolean reset)
	{
		if (reset || (currentAnim == null && name != currentAnim))
		{
			frameTime = 0;
			animationFinished = false;
		}
		currentAnim = name;
	}
	
	@Override
	public void update(float dt)
	{
		if (currentAnim != null)
		{
			// set the frame to display
			frameX = animations.get(currentAnim).getKeyFrame(frameTime).getRegionX();
			frameY = animations.get(currentAnim).getKeyFrame(frameTime).getRegionY();
			if(!animationFinished && animations.get(currentAnim).getPlayMode() == PlayMode.NORMAL
					&& animations.get(currentAnim).isAnimationFinished(frameTime))
			{
				animationFinished = true;
				if(endFunction != null)
				{
					endFunction.Execute();
				}
			}
			
			frameTime += dt;
		}
		super.update(dt);
	}
	
	@Override public boolean isActive() { return true; }
	
	private TextureRegion[][] regions;
	private HashMap<String, Animation> animations;
	private IVoidVoid endFunction;
	private String currentAnim;
	private float frameTime;
	private boolean animationFinished;
	private int cells; // horizontal cells

}
