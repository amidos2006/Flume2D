package com.flume2d.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.flume2d.F2D;

public class Text implements Graphic, Disposable
{
	protected Text(String text)
	{
		this.text = text;
		this.color = Color.WHITE.cpy();
		this.scale = 1;
		this.align = Align.center;
	}
	
	public Text(String fileName, String text)
	{
		this(fileName, text, 8);
	}
	
	public Text(String fileName, String text, int size)
	{
		this(fileName, text, size, 0);
	}
	
	public Text(String fileName, String text, int size, int borderSize)
	{
		this(fileName, text, size, borderSize, Color.WHITE, Color.BLACK, false);
	}
	
	public Text(String fileName, String text, int size, int borderSize, Color foreColor, Color borderColor, boolean roundedBorders)
	{
		this(text);
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fileName));
		FreeTypeFontParameter parameters = new FreeTypeFontParameter();
		parameters.size = size;
		parameters.color = foreColor;
		parameters.borderWidth = borderSize;
		parameters.borderColor = borderColor;
		parameters.borderStraight = !roundedBorders;
		parameters.flip = true;
		if(F2D.smooth)
		{
			parameters.minFilter = TextureFilter.MipMapLinearNearest;
			parameters.magFilter = TextureFilter.Linear;
		}
		else
		{
			parameters.minFilter = TextureFilter.Nearest;
			parameters.magFilter = TextureFilter.Nearest;
		}
		font = generator.generateFont(parameters);
		generator.dispose();
		
		layout = new GlyphLayout(font, text);
	}
	
	@Override
	public void dispose() {
		font.dispose();
	}
	
	@Override
	public void update(float dt) { }

	@Override
	public void render(SpriteBatch spriteBatch, float x, float y) 
	{
		layout.setText(font, text);
		font.getData().setScale(scale);
		font.setColor(color);
		font.draw(spriteBatch, text, x, y, 0, align, false);
	}

	@Override
	public boolean isVisible() {
		return true;
	}
	
	@Override
	public boolean isActive() {
		return false;
	}
	
	public float getWidth()
	{
		return layout.width;
	}
	
	public float getHeight()
	{
		return layout.height;
	}
	
	public Color color;
	public float scale;
	public String text;
	public int align;
	
	protected BitmapFont font;
	protected GlyphLayout layout;
}
