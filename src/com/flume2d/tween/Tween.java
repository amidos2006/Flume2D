package com.flume2d.tween;

import com.flume2d.Entity;
import com.flume2d.tween.easing.IEasing;
import com.flume2d.tween.easing.Linear;

public class Tween 
{
	public enum TweenType
	{
		Presistent,
		OneShot,
		Looping
	}
	
	public TweenType type;
	
	protected double time;
	protected double value;
	protected boolean active;
	protected Entity parent;
	protected boolean isFinished;
	
	private IEasing ease;
	private double duration;
	
	public Tween(double duration)
	{
		this(duration, TweenType.Presistent);
	}
	
	public Tween(double duration, TweenType type)
	{
		this(duration, type, new Linear());
	}
	
	public Tween(double duration, TweenType type, IEasing ease)
	{
		this.type = type;
		this.ease = ease;
		this.duration = duration;
		this.time = 0;
		this.value = 0;
		this.active = false;
		this.isFinished = false;
	}
	
	public void start()
	{
		this.active = true;
		this.time = 0;
		this.value = 0;
	}
	
	public void stop()
	{
		this.active = false;
	}
	
	public double getPercentage()
	{
		return value;
	}
	
	public boolean isActive()
	{
		return active;
	}
	
	public void setParent(Entity e)
	{
		this.parent = e;
	}
	
	public boolean hasParent()
	{
		return parent != null;
	}
	
	public void update(float dt)
	{
		if(!active)
		{
			return;
		}
		
		isFinished = false;
		time += dt;
		value = ease.tick(time, 0, 1, duration);
		if(value > 1)
		{
			isFinished = true;
			switch(type)
			{
			case Presistent:
				stop();
				value = 1;
				break;
			case OneShot:
				stop();
				value = 1;
				parent.RemoveTween(this);
				parent = null;
				break;
			case Looping:
				time -= duration;
				value -= 1;
				break;
			}
		}
		
	}
}
