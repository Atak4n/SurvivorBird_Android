package com.br0ke.survivorbird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

import java.util.Random;

public class SurvivorBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture bird;
	Texture enemy;
	float birdX = 0;
	float birdY = 0;
	int gameState = 0;
	float velocity = 0;
	float gravity = 0.3f;
	float enemyVelocity = 8;
	Random random;
	int score = 0;
	int scoredEnemy = 0;
	BitmapFont scoreFont;
	BitmapFont gameOverFont;
	Circle birdCircle;

	//ShapeRenderer shapeRenderer;

	int numberOfEnemies = 4;
	float [] enemyX = new float[numberOfEnemies];
	float [] enemyOffset = new float[numberOfEnemies];
	float [] enemyOffset1 = new float[numberOfEnemies];
	float [] enemyOffset2 = new float[numberOfEnemies];
	float distance = 0;


	Circle[] enemyCircle;
	Circle[] enemyCircle1;
	Circle[] enemyCircle2;



	@Override
	public void create () { //screen when first opened
		batch = new SpriteBatch();
		background = new Texture("background.png");
		bird = new Texture("bird.png");
		enemy = new Texture("enemy.png");

		distance = Gdx.graphics.getWidth()/2;
		random = new Random();

		birdX = Gdx.graphics.getWidth()/10;
		birdY = Gdx.graphics.getHeight()/3;

		//shapeRenderer = new ShapeRenderer();

		birdCircle = new Circle();
		enemyCircle = new Circle[numberOfEnemies];
		enemyCircle1 = new Circle[numberOfEnemies];
		enemyCircle2 = new Circle[numberOfEnemies];

		scoreFont = new BitmapFont();
		scoreFont.setColor(Color.RED);
		scoreFont.getData().setScale(5);

		gameOverFont = new BitmapFont();
		gameOverFont.setColor(Color.RED);
		gameOverFont.getData().setScale(6);



		for (int i=0; i<numberOfEnemies; i++){

			enemyOffset[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight() -200);
			enemyOffset1[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight() -200);
			enemyOffset2[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight() -200);

			enemyX[i] = Gdx.graphics.getWidth() - enemy.getWidth()/2 + i * distance;

			enemyCircle[i] = new Circle();
			enemyCircle1[i] = new Circle();
			enemyCircle2[i] = new Circle();

		}

	}

	@Override
	public void render () { //view or action that will continue
		batch.begin();
		batch.draw(background,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		if (gameState==1){ //when the game starts

			if ( enemyX[scoredEnemy] < Gdx.graphics.getWidth()/10){
				score++;
				if(scoredEnemy < numberOfEnemies -1) {
					scoredEnemy++;
				} else {
					scoredEnemy = 0;
				}
			}

			if (Gdx.input.justTouched()){
				velocity = -10;
			}

			for (int i = 0; i<numberOfEnemies; i++) {

				if (enemyX[i] < 0){ // endless cycle of enemies
					enemyX[i] = enemyX[i] + numberOfEnemies * distance;

					enemyOffset[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight() -200);
					enemyOffset1[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight() -200);
					enemyOffset2[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight() -200);

				} else {
					enemyX[i] = enemyX[i] - enemyVelocity;
				}


				batch.draw(enemy,enemyX[i],Gdx.graphics.getHeight()/2 + enemyOffset[i],Gdx.graphics.getWidth()/12,Gdx.graphics.getHeight()/8); //drawing enemies on the screen
				batch.draw(enemy,enemyX[i],Gdx.graphics.getHeight()/2 + enemyOffset1[i],Gdx.graphics.getWidth()/12,Gdx.graphics.getHeight()/8);
				batch.draw(enemy,enemyX[i],Gdx.graphics.getHeight()/2 + enemyOffset2[i],Gdx.graphics.getWidth()/12,Gdx.graphics.getHeight()/8);



				enemyCircle[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth()/24, Gdx.graphics.getHeight()/2 + enemyOffset[i] + Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/34); //circle of enemies
				enemyCircle1[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth()/24, Gdx.graphics.getHeight()/2 + enemyOffset1[i] + Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/34);
				enemyCircle2[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth()/24, Gdx.graphics.getHeight()/2 + enemyOffset2[i] + Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/34);
			}


			if (birdY > 0 && birdY < 1100) { //for the game to end when you touch the bottom and top of the screen
				velocity = velocity + gravity;
				birdY = birdY - velocity;
			} else {
				gameState = 2;
			}
		} else if (gameState == 0) {
			if (Gdx.input.justTouched()){
				gameState = 1;
			}
		} else if (gameState == 2) { //when the game over
			gameOverFont.draw(batch,"             GAME OVER! \n Click the screen to play again",485,Gdx.graphics.getHeight()/2);
			if (Gdx.input.justTouched()){
				gameState = 1;

				birdY = Gdx.graphics.getHeight()/3;

				for (int i=0; i<numberOfEnemies; i++){

					enemyOffset[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight() -200);
					enemyOffset1[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight() -200);
					enemyOffset2[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight() -200);

					enemyX[i] = Gdx.graphics.getWidth() - enemy.getWidth()/2 + i * distance;

					enemyCircle[i] = new Circle();
					enemyCircle1[i] = new Circle();
					enemyCircle2[i] = new Circle();

				}
				velocity = 0;
				scoredEnemy =0;
				score =0;
			}

		}
		batch.draw(bird, birdX, birdY,Gdx.graphics.getWidth()/12,Gdx.graphics.getHeight()/8); //drawing a bird on the screen
		scoreFont.draw(batch,"Score: " + String.valueOf(score),100,1030); //Writing the score to the screen
		batch.end();

		birdCircle.set(birdX + Gdx.graphics.getWidth()/24 , birdY + Gdx.graphics.getHeight()/16 ,Gdx.graphics.getWidth()/32); //bird's circle
		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.BLACK);
		//shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

		for (int i=0; i<numberOfEnemies; i++){
			/*if(Intersector.overlaps(sharkCircle[i],whaleCircle[i]) || Intersector.overlaps(sharkCircle[i],octopusCircle[i]) || Intersector.overlaps(octopusCircle[i],whaleCircle[i])){

				enemyOffset[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight() -200);
				enemyOffset1[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight() -200);
				enemyOffset2[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight() -200);
				enemyX[i] = Gdx.graphics.getWidth() - octopus.getWidth()/2 + i * distance;
			}*/

			//shapeRenderer.circle(enemyX[i] + Gdx.graphics.getWidth()/20, Gdx.graphics.getHeight()/2 + enemyOffset[i] + Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/34); //circle concrete drawing
			//shapeRenderer.circle(enemyX[i] + Gdx.graphics.getWidth()/20, Gdx.graphics.getHeight()/2 + enemyOffset1[i] + Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/34);
			//shapeRenderer.circle(enemyX[i] + Gdx.graphics.getWidth()/20, Gdx.graphics.getHeight()/2 + enemyOffset2[i] + Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/34);

			if(Intersector.overlaps(birdCircle, enemyCircle[i]) || Intersector.overlaps(birdCircle,enemyCircle1[i]) || Intersector.overlaps(birdCircle,enemyCircle2[i])){ // finish the game when they touch each other
				gameState = 2;
			}
		}
		//shapeRenderer.end();
	}
}
