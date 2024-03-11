package com.ing.survivorbird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.ScreenUtils;

import java.awt.Shape;
import java.util.Random;

public class SurvivorBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture bird;
	Texture bee1;
	Texture bee2;
	Texture bee3;
	float birdX = 0;
	float birdY = 0;
	int gameState = 0;
	float velocity = 0;
	float gravity = 0.1f;
	float enemyVelocity = 2;
	Random random;
	int score = 0;
	int scoredEnemy = 0;
	BitmapFont font;//score font
	BitmapFont font2;//gameover font
	ShapeRenderer shapeRenderer;



	Circle birdCirle;



	int numberOfEnemies = 4;
	float [] enemyX = new float[numberOfEnemies]; // 4 farklı kordinat dizisinde spawn olacak enemyler
	float [] enemyOffset = new float[numberOfEnemies];
	float [] enemyOffset2 = new float[numberOfEnemies];
	float [] enemyOffset3 = new float[numberOfEnemies];

	Circle[] enemyCircles;
	Circle[] enemyCircles2;
	Circle[] enemyCircles3;

	float distance = 0;


	@Override
	public void create () {
		batch = new SpriteBatch(); // sprite'ları(öbjeleri) çizmemiz için lazım olan objeler batch'ler.
		background = new Texture("background.png");
		bird = new Texture("bird.png");
		bee1 = new Texture("bee.png");
		bee2 = new Texture("bee.png");
		bee3 = new Texture("bee.png");

		distance = Gdx.graphics.getWidth()/2;// iki arı arasında ekranın yarısı kadar fark olsun
		random = new Random();

		birdX = Gdx.graphics.getWidth()/2 - bird.getHeight()/2;
		birdY = Gdx.graphics.getHeight()/3;

		shapeRenderer = new ShapeRenderer();//circle çizdirme(collision)


		birdCirle = new Circle();
		enemyCircles = new Circle[numberOfEnemies];
		enemyCircles2 = new Circle[numberOfEnemies];
		enemyCircles3  = new Circle[numberOfEnemies];

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(4);

		font2 = new BitmapFont();
		font2.setColor(Color.WHITE);
		font2.getData().setScale(6);


		//initialize enemy
		for (int i = 0; i < numberOfEnemies; i++){
			//random numara ile Y ekseni oluşturuyoruz.
			enemyOffset[i] = (random.nextFloat() - 0.5f)*(Gdx.graphics.getHeight() - 200);// random- 0.5f çıkarma sonucu hem + hem - dönebileceğinden Y ekseni altı ve üstünde random oluşum sağlayacak
			enemyOffset2[i] = (random.nextFloat() - 0.5f)*(Gdx.graphics.getHeight() - 200);
			enemyOffset3[i] = (random.nextFloat() - 0.5f)*(Gdx.graphics.getHeight() - 200);

			enemyX[i] = Gdx.graphics.getWidth() - bee1.getWidth()/2 + i * distance;// her oluşan arı setinin ekseni kendi distance göre otomatik ayarlanır

			enemyCircles[i] = new Circle();
			enemyCircles2[i] = new Circle();
			enemyCircles3[i] = new Circle();

		}

	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


		if(gameState == 1){

			if(enemyX[scoredEnemy] < birdX) {// eğer bird enemy'e takılmadan geçmişse skoru 1 arttır
				score++;

				if(scoredEnemy < numberOfEnemies-1){ // skor 3 iken 4. seti kontrol edeceğinden buraya 3 verdik. numberofenemies 4 belirlediğimiz için.
					scoredEnemy++;
				}else{
					scoredEnemy = 0;
				}

			}

				if(Gdx.input.justTouched()){
					velocity = -7;
				}

				for(int i = 0; i < numberOfEnemies; i++){

// ekrandan çıktığında kaybolur ve loop tekrar eder
					if(enemyX[i] < 0) {
						enemyX[i] = enemyX[i] + numberOfEnemies * distance;

						//random numara ile Y ekseni oluşturuyoruz.
						enemyOffset[i] = (random.nextFloat() - 0.5f)*(Gdx.graphics.getHeight() - 200);// random- 0.5f çıkarma sonucu hem + hem - dönebileceğinden Y ekseni altı ve üstünde random oluşum sağlayacak
						enemyOffset2[i] = (random.nextFloat() - 0.5f)*(Gdx.graphics.getHeight() - 200);
						enemyOffset3[i] = (random.nextFloat() - 0.5f)*(Gdx.graphics.getHeight() - 200);

					} else {
						enemyX[i] = enemyX[i] - enemyVelocity;
					}
					batch.draw(bee1,enemyX[i],Gdx.graphics.getHeight()/2 + enemyOffset[i],Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10);
					batch.draw(bee2,enemyX[i],Gdx.graphics.getHeight()/2 + enemyOffset2[i],Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10);
					batch.draw(bee3,enemyX[i],Gdx.graphics.getHeight()/2 + enemyOffset3[i],Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10);

					enemyCircles[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth()/30, Gdx.graphics.getHeight()/2 + enemyOffset[i] + Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30  );
					enemyCircles2[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth()/30, Gdx.graphics.getHeight()/2 + enemyOffset2[i] + Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30  );
					enemyCircles3[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth()/30, Gdx.graphics.getHeight()/2 + enemyOffset3[i] + Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30  );

				}


			if(birdY > 0){
				velocity = velocity + gravity;
				birdY = birdY - velocity;
			} else {
				gameState = 2; // kuş aşağı düşerse, Y ekseninden çıkarsa oyun biter
			}

		} else if(gameState == 0) {
			if(Gdx.input.justTouched()) { // tıklandığında
				gameState = 1; // oyun başladı.
			}
		} else if (gameState == 2){

			font2.draw(batch,"Game Over! Tap To Play Again!",100,Gdx.graphics.getHeight()/2);

			if(Gdx.input.justTouched()) { // tıklandığında
				gameState = 1; // oyun başladı.

				birdY = Gdx.graphics.getHeight() / 3;

				for (int i = 0; i < numberOfEnemies; i++) {
					enemyOffset[i] = (random.nextFloat() - 0.5f)*(Gdx.graphics.getHeight() - 200);// random- 0.5f çıkarma sonucu hem + hem - dönebileceğinden Y ekseni altı ve üstünde random oluşum sağlayacak
					enemyOffset2[i] = (random.nextFloat() - 0.5f)*(Gdx.graphics.getHeight() - 200);
					enemyOffset3[i] = (random.nextFloat() - 0.5f)*(Gdx.graphics.getHeight() - 200);

					enemyX[i] = Gdx.graphics.getWidth() - bee1.getWidth()/2 + i * distance;// her oluşan arı setinin ekseni kendi distance göre otomatik ayarlanır

					enemyCircles[i] = new Circle();
					enemyCircles2[i] = new Circle();
					enemyCircles3[i] = new Circle();
				}

				velocity = 0; // çok yukarı çıktıysa baştan başlasın.
				score= 0; // oyun bitince sıfırla
				scoredEnemy = 0; // oyun bitince sıfırla
			}
		}




		batch.draw(bird,birdX,birdY, Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10);

		font.draw(batch, String.valueOf(score), 100,200);

		batch.end();

		birdCirle.set(birdX + Gdx.graphics.getWidth()/30,birdY + Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30 );

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.BLACK);
		//shapeRenderer.circle(birdCirle.x, birdCirle.y, birdCirle.radius);



		for(int i = 0; i < numberOfEnemies; i++) {
			//shapeRenderer.circle(enemyX[i] + Gdx.graphics.getWidth()/30, Gdx.graphics.getHeight()/2 + enemyOffset[i] + Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30);
			//shapeRenderer.circle(enemyX[i] + Gdx.graphics.getWidth()/30, Gdx.graphics.getHeight()/2 + enemyOffset2[i] + Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30);
			//shapeRenderer.circle(enemyX[i] + Gdx.graphics.getWidth()/30, Gdx.graphics.getHeight()/2 + enemyOffset3[i] + Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30);

			if(Intersector.overlaps(birdCirle, enemyCircles[i]) || Intersector.overlaps(birdCirle,enemyCircles2[i]) || Intersector.overlaps(birdCirle,enemyCircles3[i])) {
				gameState = 2;
			}
		}

		//shapeRenderer.end();
	}
	
	@Override
	public void dispose () {

	}
}
