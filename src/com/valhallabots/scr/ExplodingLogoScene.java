package com.valhallabots.scr;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.ridhvl.HvlMath;
import com.osreboot.ridhvl.painter.painter2d.HvlPainter2D;

public class ExplodingLogoScene extends Scene {

	public class Particle {
		public Particle(Color color, float x, float y, float xVel, float yVel) {
			super();
			this.color = color;
			this.x = x;
			this.y = y;
			this.xVel = xVel;
			this.yVel = yVel;
		}

		public Color color;
		public float x, y;
		public float xVel, yVel;

		public void update(float delta) {
			xVel *= Math.pow(Math.E, 1.01 * delta);
			yVel *= Math.pow(Math.E, -0.5f * delta);
			
			x += xVel * delta;
			y += yVel * delta;
		}

		public void draw(float delta, float opacity) {
			HvlPainter2D.hvlDrawQuadc(x, y, 1, 1, new Color(color.r, color.g, color.b, color.a * opacity));
		}
	}

	private List<Particle> particles;

	public static final float size = 512;

	private float windBar;
	
	private boolean allOff;

	@Override
	public void initialize() {
		windBar = (Display.getWidth() / 2) + (size / 2);
		
		particles = new LinkedList<>();

		try {
			BufferedImage logo = ImageIO.read(new File("res/Logo.png"));

			for (int x = 0; x < logo.getWidth(); x++) {
				for (int y = 0; y < logo.getHeight(); y++) {
					int pixel = logo.getRGB(x, y);

					float alpha = (pixel >> 24) & 0xff;
					float red = (pixel >> 16) & 0xff;
					float green = (pixel >> 8) & 0xff;
					float blue = (pixel) & 0xff;

					if (alpha <= 0.05f)
						continue;

					particles.add(new Particle(new Color(red, green, blue, alpha),
							(Display.getWidth() / 2) + (((float) x / logo.getWidth()) * size / 2) - (size / 4),
							(Display.getHeight() / 2) + (((float) y / logo.getHeight()) * size / 2) - (size / 4), 0, 0));
				}
			}
		} catch (IOException e) {

		}
	}
	
	@Override
	public void update(float delta) {
		allOff = true;
		
		windBar -= (Display.getWidth() / 32) * delta;
		
		for (Particle p : particles)
		{
			if (p.x >= windBar && p.xVel == 0.0f)
			{
				p.xVel = 8.0f;
				p.yVel = p.y - (Display.getHeight() / 2);
				
				p.xVel *= HvlMath.randomFloatBetween(1f, 1.25f);
				p.yVel *= HvlMath.randomFloatBetween(-0.25f, 1.0f);
			}
			p.update(delta);
			
			if (p.x <= Display.getWidth()) allOff = false;
		}
	}

	@Override
	public void draw(float delta, float opacity) {
		for (Particle p : particles)
		{
			p.draw(delta, opacity);
		}
	}

	
	@Override
	public boolean isDone() {
		return allOff;
	}

}
