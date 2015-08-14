package com.flume2d.masks;

import com.badlogic.gdx.math.Rectangle;

public class Grid extends Mask
{
	public Grid(int width, int height, float tileWidth, float tileHeight)
	{
		grid = new boolean[height][width];
		test = new Hitbox(tileWidth, tileHeight);
	}
	
	public void loadCSV(String csv)
	{
		String[] values = csv.split(",");
		int index = 0;
		for (int i = 0; i < grid.length; i++)
		{
			for (int j = 0; j < grid[0].length; j++)
			{
				if(Integer.parseInt(values[index]) == 0)
				{
					clearTile(j, i);
				}
				else
				{
					setTile(j, i);
				}
				
				index++;
			}
		}
	}
	
	public void setTile(int xTile, int yTile)
	{
		grid[yTile][xTile] = true;
	}
	
	public void clearTile(int xTile, int yTile)
	{
		grid[yTile][xTile] = false;
	}
	
	@Override
	public void dispose() 
	{
		
	}
	
	@Override
	public boolean collide(Mask mask)
	{
		if(super.collide(mask))
		{
			Rectangle bounds = getBounds();
			for (int i = 0; i < grid.length; i++)
			{
				for (int j = 0; j < grid[0].length; j++)
				{
					if(grid[i][j])
					{
						test.x = bounds.x + j * tileWidth;
						test.y = bounds.y + i * tileHeight;
						test.parent = parent;
						
						if(test.collide(mask))
						{
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	@Override
	public Rectangle getBounds() 
	{
		Rectangle rect = new Rectangle();
		rect.x = parent.x + x;
		rect.y = parent.y + y;
		rect.width = grid[0].length * tileWidth;
		rect.height = grid.length * tileHeight;
		
		return rect;
	}
	
	private boolean[][] grid;
	private float tileWidth;
	private float tileHeight;
	private Hitbox test;
}
