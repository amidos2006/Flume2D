package com.flume2d.tween;

import com.flume2d.callback.IVoidVoid;
import com.flume2d.tween.easing.IEasing;
import com.flume2d.tween.easing.Linear;

public class MotionTween extends Tween
{
	private float sX;
	private float sY;
	private float eX;
	private float eY;
	private IVoidVoid endFunction;
	
	public MotionTween(float sX, float sY, float eX, float eY, float duration)
	{
		this(sX, sY, eX, eY, duration, null);
	}
	
	public MotionTween(float sX, float sY, float eX, float eY, float duration, IVoidVoid endFunction)
	{
		this(sX, sY, eX, eY, duration, endFunction, TweenType.Presistent);
	}
	
	public MotionTween(float sX, float sY, float eX, float eY, float duration, IVoidVoid endFunction, TweenType type)
	{
		this(sX, sY, eX, eY, duration, endFunction, TweenType.Presistent, new Linear());
	}
	
	public MotionTween(float sX, float sY, float eX, float eY, float duration, IVoidVoid endFunction, TweenType type, IEasing ease)
	{
		super(duration, type, ease);
		
		this.sX = sX;
		this.sY = sY;
		this.eX = eX;
		this.eY = eY;
		
		this.endFunction = endFunction;
	}
	
	public float getX()
	{
		return (float)(value * (eX - sX) + sX);
	}
	
	public float getY()
	{
		return (float)(value * (eY - sY) + sY);
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
