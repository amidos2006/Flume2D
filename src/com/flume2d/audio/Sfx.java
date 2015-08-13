package com.flume2d.audio;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import com.flume2d.F2D;

public class Sfx implements Disposable
{
	private Sound sfx;
	private Music music;
	private long sfxId;
	private float volume;
	
	public Sfx(String filename)
	{
		sfxId = -1;
		volume = 1;
		
		FileHandle handle = Gdx.files.internal(filename);
		String ext = handle.extension();
		
		if (ext.equalsIgnoreCase("mp3") ||
			ext.equalsIgnoreCase("ogg") ||
			ext.equalsIgnoreCase("wav"))
		{
			music = Gdx.audio.newMusic(handle);
		}
		else
		{
			sfx = Gdx.audio.newSound(handle);
		}
		F2D.AddSfx(this);
	}
	
	public void Play()
	{
		Play(1);
	}
	
	public void Play(float volume)
	{
		Play(volume, false);
	}
	
	public void Play(float volume, boolean looping)
	{
		this.volume = volume;
		if (sfx == null)
		{
			music.stop();
			music.setVolume(F2D.getVolume() * volume);
			music.setLooping(looping);
			music.play();
		}
		else
		{
			sfx.stop();
			sfxId = sfx.play(F2D.getVolume() * volume);
			sfx.setLooping(sfxId, looping);
		}
	}
	
	public void Pause()
	{
		if (sfx == null)
		{
			music.pause();
		}
		else
		{
			sfx.pause();
		}
	}
	
	public void Resume()
	{
		if (sfx == null)
		{
			music.play();
		}
		else
		{
			sfx.resume();
		}
	}
	
	public void Stop()
	{
		if (sfx == null)
		{
			music.stop();
		}
		else if(sfxId > -1)
		{
			sfx.stop();
		}
		sfxId = -1;
	}
	
	public void SetVolume(float volume)
	{
		this.volume = volume;
		if(sfx == null)
		{
			if(music.isPlaying())
			{
				music.setVolume(F2D.getVolume() * volume);
			}
		}
		else
		{
			if(sfxId > -1)
			{
				sfx.setVolume(sfxId, F2D.getVolume() * volume);
			}
		}
	}
	
	public float GetVolume()
	{
		return this.volume;
	}
	
	@Override
	public void dispose() 
	{
		F2D.RemoveSfx(this);
		if (sfx == null)
			music.dispose();
		else
			sfx.dispose();
	}
	
}
