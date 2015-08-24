package com.mygdx.Entities;

import com.badlogic.ashley.core.Entity;

public class PointOfInterestEntity extends Entity {
	String name = "";

	public PointOfInterestEntity(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}
