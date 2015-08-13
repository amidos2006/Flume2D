package com.flume2d;

import java.util.*;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.flume2d.graphics.Backdrop;
import com.flume2d.graphics.Graphic;
import com.flume2d.masks.*;
import com.flume2d.math.*;

public class Entity implements Disposable
{
	
	public float x;
	public float y;
	
	public Entity()
	{
		this(0, 0, null, null);
	}
	
	public Entity(int x, int y)
	{
		this(x, y, null, null);
	}
	
	public Entity(int x, int y, Graphic graphic)
	{
		this(x, y, graphic, null);
	}
	
	public Entity(int x, int y, Graphic graphic, Mask mask)
	{
		this.x = x;
		this.y = y;
		setGraphic(graphic);
		setMask(mask);
		this.layer = 0;
		this.followCamera = true;
	}
	
	/**
	 * Collision checking that separates objects on an axis
	 * @param type the type to check collisions against
	 * @return the entity we collided with
	 */
	public Entity collide(String type)
	{
		return collide(type, 1.0f);
	}
	
	/**
	 * Collision checking that separates objects on an axis
	 * @param type the type to check collisions against
	 * @param ratio the ratio to push away from the object (should always be less than 1)
	 * @return the entity we collided with
	 */
	public Entity collide(String type, float ratio)
	{
		if (world == null)
			return null;
		
		LinkedList<Entity> list = world.getTypes(type);
		if (list == null)
			return null;
		
		Iterator<Entity> it = list.iterator();
		while (it.hasNext())
		{
			Entity e = it.next();
			Vector2 result = null;
			result = mask.collide(e.mask);
			if (result != null)
			{
				if (ratio > 1) ratio = 1;
				x += result.x * ratio;
				y += result.y * ratio;
				return e;
			}
		}
		return null;
	}
	
	/**
	 * Faster collision checking without calculating the separating axis
	 * @param type the type to check for overlapping
	 * @return the entity we overlap with, if any
	 */
	public Entity overlaps(String type)
	{
		if (world == null)
			return null;
		
		LinkedList<Entity> list = world.getTypes(type);
		if (list == null)
			return null;
		
		Iterator<Entity> it = list.iterator();
		while (it.hasNext())
		{
			Entity e = it.next();
			if (mask.overlaps(e.mask))
			{
				return e;
			}
		}
		return null;
	}

	/**
	 * Updates the entity's graphic and mask
	 */
	public void update(float dt)
	{
		if (graphic != null && graphic.isActive()) graphic.update(dt);
	}
	
	/**
	 * Draws the current graphic
	 * @param spriteBatch batching object to draw sprites
	 */
	public void render(SpriteBatch spriteBatch)
	{
		if (graphic == null) return;
		if(graphic.getClass() == Backdrop.class && followCamera)
		{
			graphic.render(spriteBatch, x + F2D.camera.position.x, y + F2D.camera.position.y);
		}
		else
		{
			graphic.render(spriteBatch, x, y);
		}
	}
	
	/**
	 * Calculates the distance from an entity
	 * @param e the entity to calculate distance
	 * @return the distance from an entity
	 */
	public double distanceFrom(Entity e)
	{
		return Math.sqrt((x - e.x) * (x - e.x) + (y - e.y) * (y - e.y));
	}
	
	/**
	 * Calculates the distance from a point
	 * @param px the point's x-axis value
	 * @param py the point's y-axis value
	 * @return the distance from an entity
	 */
	public double distanceFromPoint(float px, float py)
	{
		return Math.sqrt((x - px) * (x - px) + (y - py) * (y - py));
	}
	
	/**
	 * Checks if the entity's mask collides with a point
	 * @param x the point's x-axis value
	 * @param y the point's y-axis value
	 * @return whether we collided with the entity
	 */
	public boolean collideAt(int x, int y)
	{
		if (mask == null)
			return false;
		
		return mask.collideAt(x, y);
	}
	
	public void setMask(Mask mask)
	{
		if (mask == null) return;
		mask.parent = this;
		this.mask = mask;
	}
	
	public void setGraphic(Graphic graphic)
	{
		if(graphic == null) return;
		this.graphic = graphic;
	}
	
	public void dispose()
	{
		if (graphic != null) graphic.dispose();
	}
	
	public void setWorld(World world) { this.world = world; }
	public boolean hasWorld() { return (world != null); }

	public void added() { }
	public void removed() { }
	
	public String tagName;
	public String collisionType;
	public int layer;
	public boolean followCamera;

	private Graphic graphic;
	private Mask mask;
	
	protected World world;
	
}
