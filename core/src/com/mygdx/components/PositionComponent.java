package com.mygdx.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class PositionComponent extends Component {
	public Vector2 position = null;
	public PositionComponent() {
		position = new Vector2(0, 0);
	}
	public PositionComponent(float x, float y) {
		position = new Vector2(x,y);
	}
}
