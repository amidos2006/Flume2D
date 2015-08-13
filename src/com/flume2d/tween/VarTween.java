package com.flume2d.tween;

import com.flume2d.tween.easing.IEasing;
import com.flume2d.tween.easing.Linear;

public class VarTween extends Tween
{
	private float begin;
	private float end;
	
	public VarTween(double begin, double end, double duration)
	{
		this(begin, end, duration, TweenType.Presistent);
	}
	
	public VarTween(double begin, double end, double duration, TweenType type)
	{
		this(begin, end, duration, type, new Linear());
	}
	
	public VarTween(double begin, double end, double duration, TweenType type, IEasing ease)
	{
		super(duration, type, ease);
	}
	
	public float getValue()
	{
		return (float)(value * (end - begin) + begin);
	}
}
