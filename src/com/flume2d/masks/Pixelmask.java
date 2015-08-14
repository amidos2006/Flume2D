package com.flume2d.masks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.Rectangle;
import com.flume2d.F2D;

public class Pixelmask extends Hitbox
{
	public Pixelmask(String fileName)
	{
		super(0, 0);
		Texture texture = new Texture(Gdx.files.internal(fileName));
		if(F2D.smooth)
		{
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		else
		{
			texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		}
		
		if (!texture.getTextureData().isPrepared()) 
		{
			texture.getTextureData().prepare();
        }
		pixelmap = texture.getTextureData().consumePixmap();
		threshold = 1;
	}
	
	@Override
	public void dispose()
	{
		pixelmap.dispose();
	}
	
	@Override
	public boolean collide(Mask mask)
	{
		if(super.collide(mask))
		{
			if(mask instanceof Pixelmask)
			{
				return collide((Pixelmask) mask);
			}
			
			if(mask instanceof Hitbox)
			{
				return collide((Hitbox) mask);
			}
			
			if(mask instanceof Masklist)
			{
				return ((Masklist)mask).collide(this);
			}
		}
		
		return false;
	}
	
	private boolean collide(Pixelmask mask)
	{
		Rectangle hRect = mask.getBounds();
		Rectangle pRect = this.getBounds();
		
		for (int x = (int)(hRect.x); x < (int)Math.ceil(hRect.x + hRect.width); x++)
		{
			for (int y = (int)(hRect.y); y < (int)Math.ceil(hRect.y + hRect.height); y++)
			{
				int offsetX = (int)(x - pRect.x);
				int offsetY = (int)(y - pRect.y);
				
				if(offsetX >= 0 && offsetX < pRect.width && offsetY >= 0 && offsetY < pRect.height)
				{
					int alpha1 = pixelmap.getPixel(offsetX, offsetY) & 0xFF;
					int alpha2 = mask.pixelmap.getPixel(x, y) & 0xFF;
					if(alpha1 > threshold && alpha2 > threshold)
					{
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	private boolean collide(Hitbox mask)
	{
		Rectangle hRect = mask.getBounds();
		Rectangle pRect = this.getBounds();
		
		for (int x = (int)(hRect.x); x < (int)Math.ceil(hRect.x + hRect.width); x++)
		{
			for (int y = (int)(hRect.y); y < (int)Math.ceil(hRect.y + hRect.height); y++)
			{
				int offsetX = (int)(x - pRect.x);
				int offsetY = (int)(y - pRect.y);
				
				if(offsetX >= 0 && offsetX < pRect.width && offsetY >= 0 && offsetY < pRect.height)
				{
					int alpha = pixelmap.getPixel(offsetX, offsetY) & 0xFF;
					if(alpha > threshold)
					{
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	private Pixmap pixelmap;
	private int threshold;
}
