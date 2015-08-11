package com.mygdx.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class VelocityComponent extends Component {
	public float maxVelocity = 2;
	public float maxForce = 1;
	public float maxSpeed = 3;
	public Vector2 vectorVelocity = new Vector2(0,0);
	public Vector2 direction = new Vector2(0,1);
}
