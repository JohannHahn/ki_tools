package com.mygdx.Entities;

import com.badlogic.ashley.core.Entity;

enum Team {
    RED, GREEN
}
public class BoidEntity extends Entity {

	private Team team;
	public BoidEntity(Team team)
	{
		this.team= team;
	}
}
