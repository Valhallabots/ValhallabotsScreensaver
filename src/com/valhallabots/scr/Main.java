package com.valhallabots.scr;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.osreboot.ridhvl.display.collection.HvlDisplayModeDefault;
import com.osreboot.ridhvl.template.HvlTemplateInteg2D;

public class Main extends HvlTemplateInteg2D {
	
	public static final float fadeTime = 1.5f;
	
	private List<Scene> scenes;
	
	private int currentScene;
	
	private float opacity = 0.0f;
	
	private int fadeDir = 1;
	
	private boolean prevNext, currNext;
	
	public Main() {
		super(60, 1366, 768, "Team 3268 - Valhallabots", new HvlDisplayModeDefault());
	}
	
	public static void main(String[] args) {
		new Main();
	}

	@Override
	public void initialize() {
		getTextureLoader().loadResource("Logo");
		
		scenes = new ArrayList<Scene>();
		
		scenes.add(new ExplodingLogoScene());
	}

	@Override
	public void update(float delta) {
		// Next slide key update
		prevNext = currNext;
		currNext = Keyboard.isKeyDown(Keyboard.KEY_RIGHT);
		
		// Fade to next slide (manual)
		if (!currNext && prevNext)  {
			fadeDir = -1;
		}
		
		// Fade to next slide (auto)
		if (scenes.get(currentScene).isDone())
			fadeDir = -1;
		
		// Fade update
		opacity += delta * fadeDir / fadeTime;
		
		// Fade in finish
		if (opacity >= 1.0f) {
			opacity = 1.0f;
			fadeDir = 0;
		}
		
		// Fade out finish
		if (opacity <= 0.0f) {
			opacity = 0.0f;
			fadeDir = 1;
			currentScene++;
			
			// Scene wrap (once slideshow finishes, go to beginning)
			if (currentScene >= scenes.size()) currentScene = 0;
			
			scenes.get(currentScene).initialize();
		}
		
		scenes.get(currentScene).update(delta);
		
		// Draw the scene
		scenes.get(currentScene).draw(delta, opacity);
	}
}
