package com.flume2d.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BitmapText extends Text
{
	public BitmapText(String fontFilename, String pictureFilename, String text)
	{
		super(text);
		Texture texture = new Texture(Gdx.files.internal(pictureFilename));
		texture.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear);
		font = new BitmapFont(Gdx.files.internal(fontFilename), new TextureRegion(texture), true);
	}
}
