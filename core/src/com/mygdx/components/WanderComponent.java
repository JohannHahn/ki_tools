package com.mygdx.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class WanderComponent extends Component{
	public int circleDistance = 10;
	public int circleRadius = 20;
	public float wanderAngle = 0f;
	public float angleChange = 10f;
	public Vector2 positionCircle;
	public Vector2 wanderVector;

}
