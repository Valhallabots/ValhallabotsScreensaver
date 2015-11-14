package com.valhallabots.scr;

public abstract class Scene {
	public abstract void initialize();
	
	public abstract void update(float delta);
	
	public abstract void draw(float delta, float opacity);
	
	public abstract boolean isDone();
}
