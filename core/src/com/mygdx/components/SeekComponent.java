package com.mygdx.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.Entities.PointOfInterestEntity;

public class SeekComponent extends Component {
	public Vector2 vectorSeek = new Vector2();	
	public Vector2 target = new Vector2();
	public PointOfInterestEntity entityTarget;
	
}
