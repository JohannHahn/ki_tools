package com.mygdx.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class VelocityComponent extends Component {
	public float maxVelocity = 1;
	public float maxForce = 1;
	public float maxSpeed = 1;
	public Vector2 vectorVelocity = new Vector2(0,0);
}
