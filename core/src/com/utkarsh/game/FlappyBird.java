package com.utkarsh.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	ShapeRenderer shapeRenderer;
	Circle birdCircle;
	Rectangle bottomTubeRectangle[];
	Rectangle topTubeRectangle[];

	BitmapFont font;
	Texture bird[];
	int flapState=0;
	float birdY=0;
	float velocity=0;
	int gameState=0;
	float gravity=2;
	Texture bottomTube;
	Texture topTube;
	Texture gameOverScreen;
	Texture gameLogo;
	Texture start;
	float gap=400;
	float maxTubeOffset;
	Random random;
	int score=0;
	int scoringTube=0;

	int numberOfTubes=4;
	float[] tubeX=new float[numberOfTubes];
    float[] tubeOffset=new float[numberOfTubes];
    float distBetweenTubes;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		shapeRenderer=new ShapeRenderer();
		birdCircle=new Circle();

		font=new BitmapFont();
		font.setColor(Color.BLACK);
		font.getData().setScale(10);

		bottomTubeRectangle=new Rectangle[numberOfTubes];
		topTubeRectangle=new Rectangle[numberOfTubes];
		background=new Texture("bgday.png");
		bird=new Texture[2];
		bird[0]=new Texture("birdup.png");
		bird[1]=new Texture("birddown.png");
		gameOverScreen=new Texture("gameover.png");
		bottomTube=new Texture("bottomtube.png");
		topTube=new Texture("toptube.png");
		start=new Texture("play.png");
		gameLogo=new Texture("logo.png");
		maxTubeOffset=Gdx.graphics.getHeight()/2 - gap/2 - 100;
		random=new Random();

		distBetweenTubes=Gdx.graphics.getWidth() * 3/4 ;
		gameStart();

	}

	public void gameStart(){
		birdY=Gdx.graphics.getHeight()/2 - bird[flapState].getHeight()/2;
		for(int i=0;i<numberOfTubes;i++){
			tubeX[i]=Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i*distBetweenTubes;
			tubeOffset[i]=(random.nextFloat() - 0.5f)*(Gdx.graphics.getHeight() - gap - 200);

			bottomTubeRectangle[i]=new Rectangle();
			topTubeRectangle[i]=new Rectangle();
		}
	}

	@Override
	public void render () {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

	    if(gameState==1) {

	    	if(tubeX[scoringTube]<Gdx.graphics.getWidth()/2){
	    		score++;
	    		Gdx.app.log("Score",Integer.toString(score));
	    		if(scoringTube<numberOfTubes-1){
	    			scoringTube++;
				}else{
	    			scoringTube=0;
				}
			}

	        if(Gdx.input.justTouched()){
	            velocity=-30;
            }

	        for(int i=0;i<numberOfTubes;i++) {
	            if(tubeX[i]< -topTube.getWidth()){
	                tubeX[i]=tubeX[i] + numberOfTubes*distBetweenTubes;
					tubeOffset[i]=(random.nextFloat() - 0.5f)*(Gdx.graphics.getHeight() - gap - 200);
                }else{
                    tubeX[i] = tubeX[i] - 4;
                }
                batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
                batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

			}
			if(birdY>0) {
                velocity = velocity + gravity;
                birdY = birdY - velocity;
            }else{
				gameState=2;
			}
		}
	    else if(gameState==0){
	    	batch.draw(gameLogo,Gdx.graphics.getWidth()/2-gameLogo.getWidth()/2,Gdx.graphics.getHeight()*3/4);
			if(Gdx.input.justTouched()){
				//Gdx.app.log("Touched:","Yeh!");
				gameState=1;
			}
		}
	    else if(gameState==2){
	    	batch.draw(gameOverScreen,Gdx.graphics.getWidth()/2-(gameOverScreen.getWidth()*3/2)/2,Gdx.graphics.getHeight()/2-(gameOverScreen.getHeight()*2)/2,gameOverScreen.getWidth()*3/2,gameOverScreen.getHeight()*2);
			if(Gdx.input.justTouched()){
				gameState=1;
				gameStart();
				score=0;
				scoringTube=0;
				velocity=0;
			}

		}

	    if(gameState!=2) {
			font.draw(batch,Integer.toString(score),100,200);
			if (flapState == 0) {
				flapState = 1;
			} else {
				flapState = 0;
			}
			batch.draw(bird[flapState], Gdx.graphics.getWidth() / 2 - bird[flapState].getWidth() / 2, birdY);
		}else{
			font.draw(batch,Integer.toString(score),Gdx.graphics.getWidth()/2-40,Gdx.graphics.getHeight()/2-200);
		}

		batch.end();

		birdCircle.set(Gdx.graphics.getWidth()/2,birdY+bird[flapState].getHeight()/2,bird[flapState].getWidth()/2);

		//Following commented code is used to display the shapes for coder to see whether they are accurate or not

		/*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);
		shapeRenderer.end(); */

		for(int i=0;i<numberOfTubes;i++){
			if(gameState==1){

				bottomTubeRectangle[i].set(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i],bottomTube.getWidth(),bottomTube.getHeight());
				topTubeRectangle[i].set(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());

				/*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
				shapeRenderer.setColor(Color.BLUE);
				shapeRenderer.rect(bottomTubeRectangle[i].x,bottomTubeRectangle[i].y,bottomTubeRectangle[i].width,bottomTubeRectangle[i].height);
				shapeRenderer.rect(topTubeRectangle[i].x,topTubeRectangle[i].y,topTubeRectangle[i].width,topTubeRectangle[i].height);
				shapeRenderer.end(); */

				if(Intersector.overlaps(birdCircle,bottomTubeRectangle[i]) || Intersector.overlaps(birdCircle,topTubeRectangle[i])){
					gameState=2;
				}
			}
		}

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
