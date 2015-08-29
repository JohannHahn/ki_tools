package com.mygdx.Entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.components.PositionComponent;
import com.mygdx.components.RenderComponent;

public class PointOfInterestEntity extends Entity {
	public String name = "";


	public PointOfInterestEntity(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
	
	public static PointOfInterestEntity createTargetEntity(int windowWidth, int windowHeight){
		
		PointOfInterestEntity result = new PointOfInterestEntity("target");
		final String PATH_TO_SKIN = "smiley.png";
		result.add(new RenderComponent(new Texture(PATH_TO_SKIN), 50, 50));
		result.add(new PositionComponent(MathUtils.random(0, (float)windowWidth), MathUtils.random(0, (float)windowHeight)));
		
		return result;
	}
}
