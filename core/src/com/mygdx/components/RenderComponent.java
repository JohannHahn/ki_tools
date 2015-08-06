package com.mygdx.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;

public class RenderComponent extends Component {
	// only objects width render components will be rendert
	// hold the information how it shoud be drawen

	private Texture texture = null;// if null then draw default Shape in Rendersystem
	private int width = 0;
	private int height = 0;

	public RenderComponent() {

	}

	public RenderComponent(Texture texture) {
		this();
		setHeight(texture.getHeight());
		setWidth(texture.getWidth());
		this.setTexture(texture);
	}

	public RenderComponent(Texture texture, int width, int height) {
		this(texture);
		setHeight(height);
		setWidth(width);
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
