package com.flume2d.graphics;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.flume2d.F2D;

public class Tilemap implements Graphic, Disposable
{
	
	public Tilemap(String filename, int tileWidth, int tileHeight, int columns, int rows)
	{
		this(filename, tileWidth, tileHeight, columns, rows, 0, 0);
	}
	
	public Tilemap(String filename, int tileWidth, int tileHeight, int columns, int rows, int spacing, int margin)
	{
		if (filename != null)
		{
			tileset = new Texture(filename);
			if(F2D.smooth)
			{
				tileset.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			}
			else
			{
				tileset.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
			}
		}
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.scale = 1;
		this.color = Color.WHITE.cpy();
		this.columns = columns;
		this.rows = rows;
		this.width = columns * tileWidth;
		this.height = rows * tileHeight;
		this.spacing = spacing;
		this.margin = margin;
		
		tiles = new int[columns][rows];
	}
	
	@Override
	public void dispose()
	{
		tileset.dispose();
	}
	
	@Override
	public void render(SpriteBatch spriteBatch, float x, float y)
	{
		if (tileset == null) return;
		
		drawBatch(spriteBatch, x, y);
	}
	
	private void drawBatch(SpriteBatch spriteBatch, float x, float y)
	{
		int tile, tileX, tileY;
		TextureRegion region = new TextureRegion(tileset);
		int tileCols = (tileset.getWidth() - margin * 2) / (tileWidth + spacing);
		
		Rectangle rect = F2D.GetRenderRectangle();
		int startX = (int)((rect.x - x) / (tileWidth * scale)) - 1;
		int startY = (int)((rect.y - y) / (tileHeight * scale)) - 1;
		int endX = (int)(rect.width / (tileWidth * scale)) + startX + 2;
		int endY = (int)(rect.height / (tileHeight * scale)) + startY + 2;
		
		spriteBatch.setColor(color);
		for (int tx = Math.max(0, startX); tx < Math.min(columns, endX); tx++)
		{
			for (int ty = Math.max(0, startY); ty < Math.min(rows, endY); ty++)
			{
				tile = tiles[tx][ty];
				if (tile == -1) continue; // skip empty tiles
				
				tileX = tile % tileCols;
				tileY = (int) Math.floor(tile / tileCols);
				
				region.setRegion(
						(tileX * tileWidth)  + (spacing * tileX) + margin,
						(tileY * tileHeight) + (spacing * tileY) + margin,
						tileWidth, tileHeight);
				region.flip(false, true);
				spriteBatch.draw(region, tx * tileWidth * scale + x, ty * tileHeight * scale + y, scale * tileWidth, scale * tileHeight);
			}
		}
	}

	public void setTile(int x, int y, int tile)
	{
		if (x >= columns || y >= rows) return;
		
		tiles[x][y] = tile;
	}
	
	public int getTile(int x, int y)
	{
		if (x >= columns || y >= rows) return -1;
		
		return tiles[x][y];
	}
	
	public void setRect(int x, int y, int width, int height, int tile)
	{
		for (int j = y; j < y + height; j++)
		{
			for (int i = x; i < x + width; i++)
			{
				setTile(i, j, tile);
			}
		}
	}
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	
	@Override public void update(float dt) { }
	@Override public boolean isActive() { return false; }
	@Override public boolean isVisible() { return true; }
	
	public int columns;
	public int rows;
	public Color color;
	public float scale;
	
	private int width;
	private int height;
	private int tileWidth;
	private int tileHeight;
	
	private int spacing;
	private int margin;
	
	private int[][] tiles;
	private Texture tileset;

}
