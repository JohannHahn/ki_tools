package com.mygdx.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

public class PursuitComponent extends Component{
	public Entity target;
	public Vector2 vectorPersuit;

}
