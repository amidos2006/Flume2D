package com.flume2d;

import java.util.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;
import com.flume2d.graphics.Graphic;

public class World implements Disposable
{
	public World()
	{
		added = new LinkedList<Entity>();
		removed = new LinkedList<Entity>();
		renderList = new LinkedList<Entity>();
		updateList = new LinkedList<Entity>();
		typeList = new HashMap<String, LinkedList<Entity>>();
	}
	
	@Override
	public void dispose()
	{
		Iterator<Entity> it = updateList.iterator();
		while (it.hasNext())
		{
			Entity e = it.next();
			e.dispose();
		}
	}
	
	public void add(Entity e)
	{
		if (e.hasWorld()) return;
		e.setWorld(this);
		added.add(e);
	}
	
	public void add(List<Entity> list)
	{
		Iterator<Entity> it = list.iterator();
		while (it.hasNext())
		{
			add(it.next());
		}
	}
	
	public void remove(Entity e)
	{
		if (!e.hasWorld()) return;
		removed.add(e);
	}
	
	public void remove(List<Entity> list)
	{
		Iterator<Entity> it = list.iterator();
		while (it.hasNext())
		{
			remove(it.next());
		}
	}
	
	public Entity addGraphic(Graphic graphic)
	{
		return addGraphic(graphic, 0, 0);
	}
	
	public Entity addGraphic(Graphic graphic, int x, int y)
	{
		Entity e = new Entity(x, y, graphic);
		add(e);
		return e;
	}
	
	public LinkedList<Entity> getTypes(String type)
	{
		return typeList.get(type);
	}
	
	public Entity findClosest(String type, float x, float y)
	{
		if (!typeList.containsKey(type)) return null;
		
		return findClosest(typeList.get(type), x, y);
	}
	
	public Entity findClosest(String[] types, float x, float y)
	{
		LinkedList<Entity> list = new LinkedList<Entity>();
		for (int i = 0; i < types.length; i++)
		{
			if (!typeList.containsKey(types[i])) continue;
			Iterator<Entity> it = typeList.get(types[i]).iterator();
			while (it.hasNext())
			{
				list.push(it.next());
			}
		}
		
		// check to make sure we actually populated the list
		if (list.size() == 0) return null;
		
		return findClosest(list, x, y);
	}
	
	public Entity findClosest(LinkedList<Entity> list, float x, float y)
	{
		double dist, maxDist = Double.MAX_VALUE;
		Entity closest = null;
		Iterator<Entity> it = list.iterator();
		do
		{
			Entity e = it.next();
			dist = (x - e.x) * (x - e.x) + (y - e.y) * (y - e.y);
			if (dist < maxDist)
			{
				maxDist = dist;
				closest = e;
			}
		} while(it.hasNext());
		
		return closest;
	}
	
	public Entity findAt(String type, int x, int y)
	{
		if (!typeList.containsKey(type)) return null;
		Iterator<Entity> it = typeList.get(type).iterator();
		while(it.hasNext())
		{
			Entity e = it.next();
			if (e.collidePoint(e.x, e.y, x, y)) return e;
		}
		return null;
	}
	
	public Entity getByName(String name)
	{
		Iterator<Entity> it = updateList.iterator();
		while (it.hasNext())
		{
			Entity e = it.next();
			if (e.tagName == name)
				return e;
		}
		return null;
	}

	public void update(float dt)
	{
		Iterator<Entity> it = updateList.iterator();
		while (it.hasNext())
		{
			Entity e = it.next();
			e.update(dt);
		}
		updateLists();
	}
	
	private void render(SpriteBatch spritebatch, List<Entity> list, Matrix4 projection)
	{
		spritebatch.setProjectionMatrix(projection);
		spritebatch.begin();
		
		for(int i=0; i < list.size(); i++)
		{
			list.get(i).render(spritebatch);
		}
		
		spritebatch.end();
	}
	
	
	public void render(SpriteBatch spritebatch)
	{
		spritebatch.setProjectionMatrix(F2D.camera.projection);
		spritebatch.begin();
		spritebatch.setColor(F2D.backColor);
		spritebatch.draw(F2D.background, -F2D.viewport.getWorldWidth() / 2, -F2D.viewport.getWorldHeight() / 2, 
				F2D.viewport.getWorldWidth(), F2D.viewport.getWorldHeight());
		spritebatch.setColor(Color.WHITE);
		spritebatch.end();
		
		Iterator<Entity> it = renderList.iterator();
		Entity e = null;
		List<Entity> ceRendering = new ArrayList<Entity>();
		List<Entity> heRendering = new ArrayList<Entity>();
		while(it.hasNext())
		{
			e = it.next();
			
			if(e.followCamera)
			{
				if(heRendering.size() > 0)
				{
					render(spritebatch, heRendering, F2D.camera.projection);
					heRendering.clear();
				}
				ceRendering.add(e);
			}
			else
			{
				if(ceRendering.size() > 0)
				{
					render(spritebatch, ceRendering, F2D.camera.combined);
					ceRendering.clear();
				}
				heRendering.add(e);
			}
		}
		
		render(spritebatch, heRendering, F2D.camera.projection);
		render(spritebatch, ceRendering, F2D.camera.combined);
	}
	
	
	private void addType(Entity e)
	{
		LinkedList<Entity> list;
		if (typeList.containsKey(e.collisionType))
		{
			list = typeList.get(e.collisionType);
		}
		else
		{
			list = new LinkedList<Entity>();
			typeList.put(e.collisionType, list);
		}
		list.add(e);
	}
	
	private void removeType(Entity e)
	{
		if (typeList.containsKey(e.collisionType))
		{
			typeList.get(e.collisionType).remove(e);
		}
	}
	
	private void updateLists()
	{
		Object[] list;
		
		// add any new entities
		if (added.size() > 0)
		{
			// copy list to array for traversal
			list = added.toArray();
			added.clear();
			for (int i = 0; i < list.length; i++)
			{
				Entity e = (Entity) list[i];
				updateList.add(e);
				renderList.add(e);
				addType(e);
				e.added();
			}
		}
		
		// remove any old entities
		if (removed.size() > 0)
		{
			// copy list to array for traversal
			list = removed.toArray();
			removed.clear();
			for (int i = 0; i < list.length; i++)
			{
				Entity e = (Entity) list[i];
				updateList.remove(e);
				renderList.remove(e);
				removeType(e);
				e.removed();
				e.setWorld(null);
			}
		}
		
		// sort render list by z-index
		Collections.sort(renderList, EntityZSort.getInstance());
		SortTypeList();
	}
	
	private void SortTypeList() 
	{
		Iterator<Entity> it;
		Iterator<String> keys;
		LinkedList<Entity> added = new LinkedList<Entity>();
		keys = typeList.keySet().iterator();
		while (keys.hasNext())
		{
			String key = keys.next();
			LinkedList<Entity> list = typeList.get(key);
			it = list.iterator();
			while (it.hasNext())
			{
				Entity e = it.next();
				// check that the entity is in the right type group
				if (key != e.collisionType)
				{
					it.remove();
					added.add(e);
				}
			}
			// remove an empty list
			if (list.size() == 0)
			{
				keys.remove();
			}
		}
		it = added.iterator();
		while (it.hasNext())
		{
			addType(it.next());
		}
	}
	
	/**
	 * Class that sorts entities by z-index
	 */
	private static class EntityZSort implements Comparator<Entity>
	{
		
		public static EntityZSort getInstance()
		{
			if (instance == null)
				instance = new EntityZSort();
			return instance;
		}

		@Override
		public int compare(Entity e1, Entity e2) {
			if (e1.layer > e2.layer) return 1;
			if (e1.layer < e2.layer) return -1;
			return 0;
		}
		
		private static EntityZSort instance;

	}

	private LinkedList<Entity> added;
	private LinkedList<Entity> removed;
	private LinkedList<Entity> renderList;
	private LinkedList<Entity> updateList;
	private HashMap<String, LinkedList<Entity>> typeList;
}
