package com.flume2d.tween;

import com.flume2d.callback.IVoidVoid;

public class Alarm extends Tween
{
	private IVoidVoid endFunction;
	
	public Alarm(double duration, IVoidVoid endFunction)
	{
		this(duration, endFunction, TweenType.Presistent);
	}
	
	public Alarm(double duration, IVoidVoid endFunction, TweenType type) 
	{
		super(duration, type);
		
		this.endFunction = endFunction;
	}
	
	@Override
	public void update(float dt)
	{
		if(!active)
		{
			return;
		}
		
		super.update(dt);
		if(isFinished && endFunction != null)
		{
			endFunction.Execute();
		}
	}
}
