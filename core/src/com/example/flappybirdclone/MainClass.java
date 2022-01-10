package com.example.flappybirdclone;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainClass extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture topTube;
	Texture bottomTube;
	Texture[] birds;
	Texture gameOver;
	Circle birdShape;
	BitmapFont font;
	Rectangle topPipe[];
	Rectangle bottomPipe[];
	boolean pipeCrossed[];
	//ShapeRenderer shapeRenderer;
	int flapState;
	long time;
	float velocity;
	float birdY;
	float topTubeY[];
	float tubeX[];
	float gap;
	int score;
	String gameState;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		gameOver = new Texture("gameover.png");
		font = new BitmapFont();
		font.setColor(Color.YELLOW);
		font.getData().setScale(10);
		birdShape = new Circle();
		topPipe = new Rectangle[4];
		bottomPipe = new Rectangle[4];
		pipeCrossed = new boolean[4];
		//shapeRenderer = new ShapeRenderer();
		flapState = 0;
		gameState = "stopped";
		velocity = 0;
		gap = 400;
		birdY = Gdx.graphics.getHeight()/2-birds[flapState].getHeight()/2;
		topTubeY = new float[4];
		tubeX = new float[4];
		tubeX[0] = Gdx.graphics.getWidth()-bottomTube.getWidth();
		score = 0;
		for(int i = 0;i<4;i++) {
			topTubeY[i] = (float)(Math.random()*((Gdx.graphics.getHeight()-200)-(gap+200))+(gap+200));
			topPipe[i] = new Rectangle();
			bottomPipe[i] = new Rectangle();
			pipeCrossed[i] = false;
			if(i>0)
				tubeX[i] = tubeX[i-1] + Gdx.graphics.getWidth()/2 + 200;
		}
		time = System.currentTimeMillis();
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		if(Gdx.input.justTouched()){
			if(gameState.equals("stopped")){
				gameState = "running";
				background = new Texture("bg.png");
				birds = new Texture[2];
				birds[0] = new Texture("bird.png");
				birds[1] = new Texture("bird2.png");
				topTube = new Texture("toptube.png");
				bottomTube = new Texture("bottomtube.png");
				birdShape = new Circle();
				topPipe = new Rectangle[4];
				bottomPipe = new Rectangle[4];
				pipeCrossed = new boolean[4];
				//shapeRenderer = new ShapeRenderer();
				flapState = 0;
				velocity = 0;
				gap = 400;
				birdY = Gdx.graphics.getHeight()/2-birds[flapState].getHeight()/2;
				topTubeY = new float[4];
				tubeX = new float[4];
				tubeX[0] = Gdx.graphics.getWidth()-bottomTube.getWidth();
				score = 0;
				for(int i = 0;i<4;i++) {
					topTubeY[i] = (float)(Math.random()*((Gdx.graphics.getHeight()-200)-(gap+200))+(gap+200));
					topPipe[i] = new Rectangle();
					bottomPipe[i] = new Rectangle();
					pipeCrossed[i] = false;
					if(i>0)
						tubeX[i] = tubeX[i-1] + Gdx.graphics.getWidth()/2 + 200;
				}
				time = System.currentTimeMillis();
			} else {
				velocity -= 20;
			}
		}
		if(gameState.equals("running")) {
			for(int i=0;i<4;i++) {
				if(tubeX[i]< -topTube.getWidth()){
					tubeX[i] = tubeX[(i-1+4)%4] + Gdx.graphics.getWidth()/2 + 200;
					topTubeY[i] = (float)(Math.random()*((Gdx.graphics.getHeight()-200)-(gap+200))+(gap+200));
					pipeCrossed[i] = false;
				}
				if(tubeX[i]<Gdx.graphics.getWidth()/2-topTube.getWidth() && !pipeCrossed[i]){
					pipeCrossed[i] = true;
					score+=1;
					System.out.println("Score:- "+score);
				}
				batch.draw(topTube, tubeX[i], topTubeY[i]);
				batch.draw(bottomTube, tubeX[i], topTubeY[i] - gap - bottomTube.getHeight());
				tubeX[i] -= 8;
			}
			if (System.currentTimeMillis() > time + 100) {
				flapState = 1 - flapState;
				time = System.currentTimeMillis();
			}
			velocity += 1;
			birdY -= velocity;
			if(birdY<0){
				birdY = birds[flapState].getHeight();
				gameState = "stopped";
			}
			if(birdY>Gdx.graphics.getHeight() - birds[flapState].getHeight()){
				birdY = Gdx.graphics.getHeight() - birds[flapState].getHeight();
				velocity = 0;
			}
		}
		batch.draw(birds[flapState], Gdx.graphics.getWidth()/2-birds[flapState].getWidth()/2, birdY);
		font.draw(batch,String.valueOf(score),100,200);

		birdShape.set(Gdx.graphics.getWidth()/2,birdY+birds[flapState].getHeight()/2,birds[flapState].getWidth()/2);
		for(int i = 0;i<4;i++){
			topPipe[i].set(tubeX[i],topTubeY[i],topTube.getWidth(),topTube.getHeight());
			bottomPipe[i].set(tubeX[i],topTubeY[i] - gap - bottomTube.getHeight(),bottomTube.getWidth(),bottomTube.getHeight());
		}
		/*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.BLUE);
		shapeRenderer.circle(birdShape.x,birdShape.y,birdShape.radius);
		shapeRenderer.end();
		for (int i=0;i<4;i++) {
			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			shapeRenderer.setColor(Color.CORAL);
			shapeRenderer.rect(topPipe[i].x, topPipe[i].y, topPipe[i].width, topPipe[i].height);
			shapeRenderer.end();
			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			shapeRenderer.setColor(Color.CHARTREUSE);
			shapeRenderer.rect(bottomPipe[i].x, bottomPipe[i].y, bottomPipe[i].width, bottomPipe[i].height);
			shapeRenderer.end();
		}*/
		for (int i=0;i<4;i++){
			if(Intersector.overlaps(birdShape,topPipe[i]) || Intersector.overlaps(birdShape,bottomPipe[i])){
				gameState = "stopped";
				batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				batch.draw(birds[flapState], Gdx.graphics.getWidth()/2-birds[flapState].getWidth()/2, Gdx.graphics.getHeight()/2-birds[flapState].getHeight()/2);
				font.draw(batch,String.valueOf(score),Gdx.graphics.getWidth()/2 - 30,Gdx.graphics.getHeight()/2-200);
				batch.draw(gameOver,Gdx.graphics.getWidth()/2 - gameOver.getWidth()/2,Gdx.graphics.getHeight()-gameOver.getHeight());
			}
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
		birds[0].dispose();
		birds[1].dispose();
	}
}
