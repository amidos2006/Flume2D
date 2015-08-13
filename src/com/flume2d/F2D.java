package com.flume2d;

import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.flume2d.audio.Sfx;

public class F2D {
	private static float volume;
	private static LinkedList<Sfx> sfxCache;
	
	public static Engine engine;
	public static OrthographicCamera camera;
	public static Viewport viewport;
	public static int windowWidth;
	public static int windowHeight;
	public static Color backColor;
	public static Texture background;
	public static boolean smooth;
	
	public static void Initialize(Engine e)
	{
		engine = e;
		smooth = false;
		volume = 1;
		sfxCache = new LinkedList<Sfx>();
		windowWidth = Gdx.graphics.getWidth();
		windowHeight = Gdx.graphics.getHeight();
		camera = new OrthographicCamera();
		camera.setToOrtho(true);
		camera.position.set(0, 0, 0);
		if(viewport == null)
		{
			viewport = new FitViewport(F2D.windowWidth, F2D.windowHeight, F2D.camera);
		}
		else
		{
			viewport.setCamera(camera);
		}
		viewport.apply();
		backColor = new Color(0x222222FF);
		Pixmap temp = new Pixmap(1, 1, Pixmap.Format.RGB565);
		temp.drawPixel(0, 0, 0xFFFFFFFF);
		background = new Texture(temp);
		temp.dispose();
	}
	
	public static void AddSfx(Sfx sfx)
	{
		sfxCache.add(sfx);
	}
	
	public static void RemoveSfx(Sfx sfx)
	{
		sfxCache.remove(sfx);
	}
	
	public static void setVolume(float volume){
		F2D.volume = volume;
		
		Iterator<Sfx> it = sfxCache.iterator();
		while(it.hasNext())
		{
			Sfx sfx = it.next();
			sfx.SetVolume(sfx.GetVolume());
		}
	}
	
	public static float getVolume(){
		return volume;
	}
	
	public static void SetWorld(World newWorld){
		Engine.SetWorld(newWorld);
	}
	
	public static float getWidth()
	{
		return viewport.getWorldWidth();
	}
	
	public static float getHeight()
	{
		return viewport.getWorldHeight();
	}
	
	public static Rectangle GetRenderRectangle()
	{
		Vector3 firstPoint = camera.unproject(new Vector3(0, 0, 0));
		Vector3 secondPoint = camera.unproject(new Vector3(windowWidth, windowHeight, 0));
		Rectangle rect = new Rectangle(firstPoint.x, firstPoint.y, 
				Math.abs(secondPoint.x - firstPoint.x), Math.abs(secondPoint.y - firstPoint.y));
		
		return rect;
	}
}
