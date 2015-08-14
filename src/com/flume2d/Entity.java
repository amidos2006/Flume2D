package com.flume2d;

import java.util.*;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.flume2d.graphics.Backdrop;
import com.flume2d.graphics.Graphic;
import com.flume2d.masks.*;
import com.flume2d.tween.Tween;

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
		
		this.addedTweens = new LinkedList<Tween>();
		this.removedTweens = new LinkedList<Tween>();
		this.updateTweens = new LinkedList<Tween>();
	}
	
	public void AddTween(Tween t)
	{
		AddTween(t, false);
	}
	
	public void AddTween(Tween t, boolean start)
	{
		if (t.hasParent()) return;
		t.setParent(this);
		addedTweens.add(t);
	}
	
	public void RemoveTween(Tween t)
	{
		if(!t.hasParent()) return;
		removedTweens.add(t);
	}
	
	/**
	 * Checks for a collision against an Entity type.
	 * @param	type		The Entity type to check for.
	 * @param	x			Virtual x position to place this Entity.
	 * @param	y			Virtual y position to place this Entity.
	 * @return	The first Entity collided with, or null if none were collided.
	 */
	public Entity collide(String type, float x, float y)
	{
		if (world == null)
			return null;
		
		if(mask == null)
			return null;
		
		LinkedList<Entity> list = world.getTypes(type);
		if (list == null)
			return null;
		
		Iterator<Entity> it = list.iterator();
		while (it.hasNext())
		{
			Entity e = it.next();
			if(collideWith(e, x, y) != null)
			{
				return e;
			}
		}
		
		return null;
	}
	
	/**
	 * Checks for collision against multiple Entity types.
	 * @param	types		An Array or Vector of Entity types to check for.
	 * @param	x			Virtual x position to place this Entity.
	 * @param	y			Virtual y position to place this Entity.
	 * @return	The first Entity collided with, or null if none were collided.
	 */
	public Entity collide(String[] types, float x, float y)
	{
		for (int i=0; i < types.length; i++)
		{
			Entity e = collide(types[i], x, y);
			if(e != null)
			{
				return e;
			}
		}
		
		return null;
	}
	
	/**
	 * Checks if this Entity collides with a specific Entity.
	 * @param	e		The Entity to collide against.
	 * @param	x		Virtual x position to place this Entity.
	 * @param	y		Virtual y position to place this Entity.
	 * @return	The Entity if they overlap, or null if they don't.
	 */
	public Entity collideWith(Entity e, float x, float y)
	{
		if (world == null)
			return null;
		
		if(mask == null || e.mask == null || e == this)
			return null;
		
		float tempX = this.x;
		float tempY = this.y;
		
		this.x = x;
		this.y = y;
		
		if(mask.collide(e.mask))
		{
			this.x = tempX;
			this.y = tempY;
			return e;
		}
		
		this.x = tempX;
		this.y = tempY;
		return null;
	}
	
	/**
	 * Checks if this Entity overlaps the specified rectangle.
	 * @param	x			Virtual x position to place this Entity.
	 * @param	y			Virtual y position to place this Entity.
	 * @param	rX			X position of the rectangle.
	 * @param	rY			Y position of the rectangle.
	 * @param	rWidth		Width of the rectangle.
	 * @param	rHeight		Height of the rectangle.
	 * @return	If they overlap.
	 */
	public boolean collideRect(float x, float y, float rX, float rY, float rWidth, float rHeight)
	{
		F2D.testEntity.x = rX;
		F2D.testEntity.y = rY;
		//((AABB)F2D.testEntity.mask).bounds.width = rWidth;
		//((AABB)F2D.testEntity.mask).bounds.height = rHeight;
		
		return collideWith(F2D.testEntity, x, y) != null;
	}
	
	/**
	 * Checks if this Entity overlaps the specified position.
	 * @param	x			Virtual x position to place this Entity.
	 * @param	y			Virtual y position to place this Entity.
	 * @param	pX			X position.
	 * @param	pY			Y position.
	 * @return	If the Entity intersects with the position.
	 */
	public boolean collidePoint(float x, float y, float pX, float pY)
	{
		return collideRect(x, y, pX, pY, 1, 1);
	}
	
	/**
	 * Populates an array with all collided Entities of a type.
	 * @param	type		The Entity type to check for.
	 * @param	x			Virtual x position to place this Entity.
	 * @param	y			Virtual y position to place this Entity.
	 * @param	array		The Array or Vector object to populate.
	 * @return	The array, populated with all collided Entities.
	 */
	public Array<Entity> collideInto(String type, float x, float y)
	{
		if (world == null)
			return null;
		
		if(mask == null)
			return null;
		
		LinkedList<Entity> list = world.getTypes(type);
		if (list == null)
			return null;
		
		Array<Entity> results = new Array<Entity>();
		Iterator<Entity> it = list.iterator();
		while (it.hasNext())
		{
			Entity e = it.next();
			if(collideWith(e, x, y) != null)
			{
				results.add(e);
			}
		}
		
		return results;
	}
	
	/**
	 * Populates an array with all collided Entities of multiple types.
	 * @param	types		An array of Entity types to check for.
	 * @param	x			Virtual x position to place this Entity.
	 * @param	y			Virtual y position to place this Entity.
	 * @param	array		The Array or Vector object to populate.
	 * @return	The array, populated with all collided Entities.
	 */
	public Array<Entity> collideInto(String[] types, float x, float y)
	{
		Array<Entity> results = new Array<Entity>();
		for (int i = 0; i < types.length; i++)
		{
			Array<Entity> temp = collideInto(types[i], x, y);
			results.addAll(temp);
		}
		
		return results;
	}

	/**
	 * Updates the entity's graphic and mask
	 */
	public void update(float dt)
	{
		if (graphic != null && graphic.isActive()) graphic.update(dt);
		
		Iterator<Tween> it = updateTweens.iterator();
		while (it.hasNext())
		{
			Tween t = it.next();
			t.update(dt);
		}
		updateLists();
	}
	
	private void updateLists()
	{
		Object[] list;
		
		// add any new entities
		if (addedTweens.size() > 0)
		{
			// copy list to array for traversal
			list = addedTweens.toArray();
			addedTweens.clear();
			for (int i = 0; i < list.length; i++)
			{
				Tween t = (Tween) list[i];
				updateTweens.add(t);
			}
		}
		
		// remove any old entities
		if (removedTweens.size() > 0)
		{
			// copy list to array for traversal
			list = removedTweens.toArray();
			removedTweens.clear();
			for (int i = 0; i < list.length; i++)
			{
				Tween t = (Tween) list[i];
				updateTweens.remove(t);
			}
		}
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
	
	public void setMask(Mask mask)
	{
		if (mask == null) return;
		mask.parent = this;
		this.mask = mask;
	}
	
	public Mask getMask()
	{
		return mask;
	}
	
	public void setHitBox(float width, float height)
	{
		Hitbox hitbox = new Hitbox(width, height);
		setMask(hitbox);
	}
	
	public void setHitBox(float originX, float originY, float width, float height)
	{
		Hitbox hitbox = new Hitbox(originX, originY, width, height);
		setMask(hitbox);
	}
	
	public void setGraphic(Graphic graphic)
	{
		if(graphic == null) return;
		this.graphic = graphic;
	}
	
	public void dispose()
	{
		if (graphic != null) graphic.dispose();
		if (mask != null) mask.dispose();
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
	
	private LinkedList<Tween> addedTweens;
	private LinkedList<Tween> removedTweens;
	private LinkedList<Tween> updateTweens;
	
	protected World world;
	
}
