package com.mygdx.components;

import com.badlogic.ashley.core.Component;

public class RessourceComponent extends Component{
	public int health = 100;
	public int fuel = 100;
	public int fuelThreshold = 40;
	public boolean wounded = false;
	public boolean lowOnFuel = false;
	
	public RessourceComponent(){}
	
	public RessourceComponent(int health, int fuel){
		this.health = health;
		this.fuel = fuel;
	}
}
