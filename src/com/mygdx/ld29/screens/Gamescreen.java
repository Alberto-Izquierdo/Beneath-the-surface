package com.mygdx.ld29.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.ld29.LD29;
import com.mygdx.ld29.handlers.MyContactListener;
import com.mygdx.ld29.handlers.MyInput;
import com.mygdx.ld29.player.Player1;
import com.mygdx.ld29.player.Player1.State;
import com.mygdx.ld29.player.PlayerShoot;

public class Gamescreen implements Screen {
	
	private LD29 game;
	private World world;
	
	static final float BOX_STEP=1/120f;
	static final int  BOX_VELOCITY_ITERATIONS=8;
	static final int BOX_POSITION_ITERATIONS=3;
	float accumulator;
	float stateTime = 3;

	OrthographicCamera cam;
	OrthographicCamera cam2;
	SpriteBatch batch;
	
	SpriteBatch batch2;
	private MyContactListener cl;
	
	Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	
	Body body;
	Body staticBody;
	
	private Vector2[] spawns = new Vector2[2];
	
	private Player1 player1;
	private Player1 player2;
	
	private String thislevel;
	
	private Array<Vector2> candles = new Array<Vector2>();
	Array<Body> blocks = new Array<Body>();
	
	private Array<PlayerShoot> shoots;
	
	private TiledMap tileMap;
	private OrthogonalTiledMapRenderer tmr;
	
	private float tileSize;
	
	Texture texture_background = new Texture("background/background-colours.png");
	Texture texture_player = new Texture("player/player.png");
	Texture texture_bullet_player = new Texture("player/bullet.png");
	Texture texture_candle = new Texture("background/candle.png");
	TextureRegion [] region_bullet;
	TextureRegion [] region_player;
	TextureRegion region_player_jumping;
	TextureRegion region_player_falling;
	TextureRegion [] region_candle;
	
	private int numTiles;
	
	Animation animation_player;
	Animation animation_bullet;
	Animation animation_candle;
	
	
	public Gamescreen(LD29 game, String level){
		thislevel = level;
		this.game = game;
		this.world = new World(new Vector2(0, -150), true);
		World.setVelocityThreshold(0f);
		cl = new MyContactListener();
		cl.setGame(game);
		world.setContactListener(cl);
		batch = new SpriteBatch();
		batch2 = new SpriteBatch();
		cam2 = new OrthographicCamera();
		cam = new OrthographicCamera();
		cam2.setToOrtho(false, 480, 720);
		cam.setToOrtho(false, 480/10, 720/10);

		cam.position.x = 240/10;
		
		cam2.position.y = 360;
		cam.position.y = 360/80;
		cam.update();
		cam2.update();
		batch.getProjectionMatrix().set(cam2.combined);
		batch2.getProjectionMatrix().set(cam.combined);
		
		shoots = new Array<PlayerShoot>();
		
		//tiled map
		tileMap = new TmxMapLoader().load("C://BTS/"+level);
		tmr = new OrthogonalTiledMapRenderer(tileMap);
		tmr.setView(cam2);
		
		TiledMapTileLayer layer = (TiledMapTileLayer) tileMap.getLayers().get("Floor");
		tileSize = layer.getTileWidth();
		numTiles = layer.getWidth();
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		for(int row = 0; row < layer.getHeight(); row++) {
			for(int col = 0; col < layer.getWidth(); col++) {
				
				// get cell
				Cell cell = layer.getCell(col, row);
				
				// check if cell exists
				if(cell == null) continue;
				if(cell.getTile() == null) continue;
				
				// create a body + fixture from cell
				bdef.type = BodyType.StaticBody;
				bdef.position.set(
					(col + 0.5f) * tileSize / 10,
					(row + 0.5f) * tileSize / 10-320/10
				);
				
				ChainShape cs = new ChainShape();
				Vector2[] v = new Vector2[5];
				v[0] = new Vector2(
						(-tileSize+1) / 2 / 10, (-tileSize+1) / 2 / 10);
				v[1] = new Vector2(
						(-tileSize+1) / 2 / 10, (tileSize-1) / 2 / 10);
				v[2] = new Vector2(
					(tileSize-1) / 2 / 10, (tileSize-1) / 2 / 10);
				v[3] = new Vector2(
						(tileSize-1) / 2 / 10, (-tileSize+1) / 2 / 10);
				v[4] = new Vector2(
					(-tileSize+1) / 2 / 10, (-tileSize+1) / 2 / 10);
				cs.createChain(v);
				fdef.friction = 0;
				fdef.shape = cs;
				fdef.isSensor = false;
				world.createBody(bdef).createFixture(fdef).setUserData("Floor");
			}
			
		}
		layer = (TiledMapTileLayer) tileMap.getLayers().get("Spikes");
		for(int row = 0; row < layer.getHeight(); row++) {
			for(int col = 0; col < layer.getWidth(); col++) {
				
				// get cell
				Cell cell = layer.getCell(col, row);
				
				// check if cell exists
				if(cell == null) continue;
				if(cell.getTile() == null) continue;
				
				// create a body + fixture from cell
				bdef.type = BodyType.StaticBody;
				bdef.position.set(
					(col + 0.5f) * tileSize / 10,
					(row + 0.5f) * tileSize / 10-320/10
				);
				
				ChainShape cs = new ChainShape();
				Vector2[] v = new Vector2[5];
				v[0] = new Vector2(
					-tileSize / 2 / 10, -tileSize / 2 / 10);
				v[1] = new Vector2(
					-tileSize / 2 / 10, tileSize / 2 / 10);
				v[2] = new Vector2(
					tileSize / 2 / 10, tileSize / 2 / 10);
				v[3] = new Vector2(
					tileSize / 2 / 10, -tileSize / 2 / 10);
				v[4] = new Vector2(
						-tileSize / 2 / 10, -tileSize / 2 / 10);
				cs.createChain(v);
				fdef.friction = 0;
				fdef.shape = cs;
				fdef.isSensor = false;
				world.createBody(bdef).createFixture(fdef).setUserData("spike");
			}
		}
		
		layer = (TiledMapTileLayer) tileMap.getLayers().get("player");
		int k = 0;
		for(int row = 0; row < layer.getHeight(); row++) {
			for(int col = 0; col < layer.getWidth(); col++) {
				Cell cell = layer.getCell(col, row);
				if(cell == null) continue;
				if(cell.getTile() == null) continue;
				spawns[k] = new Vector2(col, row);
				k++;
			}
		}
		
		layer = (TiledMapTileLayer) tileMap.getLayers().get("candles");
		for(int row = 0; row < layer.getHeight(); row++) {
			for(int col = 0; col < layer.getWidth(); col++) {
				Cell cell = layer.getCell(col, row);
				if(cell == null) continue;
				if(cell.getTile() == null) continue;
				Vector2 spawn = new Vector2((col*tileSize+2)/10, (row*tileSize+2-320)/10);
				candles.add(spawn);
			}
		}
		
		layer = (TiledMapTileLayer) tileMap.getLayers().get("levelend");
		for(int row = 0; row < layer.getHeight(); row++) {
			for(int col = 0; col < layer.getWidth(); col++) {
				
				// get cell
				Cell cell = layer.getCell(col, row);
				
				// check if cell exists
				if(cell == null) continue;
				if(cell.getTile() == null) continue;
				
				// create a body + fixture from cell
				bdef.type = BodyType.StaticBody;
				bdef.position.set(
					(col + 0.5f) * tileSize / 10,
					(row + 0.5f) * tileSize / 10-320/10
				);
				
				ChainShape cs = new ChainShape();
				Vector2[] v = new Vector2[5];
				v[0] = new Vector2(
					-tileSize / 2 / 10, -tileSize / 2 / 10);
				v[1] = new Vector2(
					-tileSize / 2 / 10, tileSize / 2 / 10);
				v[2] = new Vector2(
					tileSize / 2 / 10, tileSize / 2 / 10);
				v[3] = new Vector2(
					tileSize / 2 / 10, -tileSize / 2 / 10);
				v[4] = new Vector2(
						-tileSize / 2 / 10, -tileSize / 2 / 10);
				cs.createChain(v);
				fdef.friction = 0;
				fdef.shape = cs;
				fdef.isSensor = true;
				world.createBody(bdef).createFixture(fdef).setUserData("Finish");
			}
		}
		
		layer = (TiledMapTileLayer) tileMap.getLayers().get("switches");
		for(int row = 0; row < layer.getHeight(); row++) {
			for(int col = 0; col < layer.getWidth(); col++) {
				
				// get cell
				Cell cell = layer.getCell(col, row);
				
				// check if cell exists
				if(cell == null) continue;
				if(cell.getTile() == null) continue;
				
				// create a body + fixture from cell
				bdef.type = BodyType.StaticBody;
				bdef.position.set(
					(col + 0.5f) * tileSize / 10,
					(row + 0.5f) * tileSize / 10-320/10
				);
				
				ChainShape cs = new ChainShape();
				Vector2[] v = new Vector2[5];
				v[0] = new Vector2(
					-tileSize / 2 / 10, -tileSize / 2 / 10);
				v[1] = new Vector2(
					-tileSize / 2 / 10, tileSize / 2 / 10);
				v[2] = new Vector2(
					tileSize / 2 / 10, tileSize / 2 / 10);
				v[3] = new Vector2(
					tileSize / 2 / 10, -tileSize / 2 / 10);
				v[4] = new Vector2(
						-tileSize / 2 / 10, -tileSize / 2 / 10);
				cs.createChain(v);
				fdef.friction = 0;
				fdef.shape = cs;
				fdef.isSensor = true;
				world.createBody(bdef).createFixture(fdef).setUserData("Switch");
			}
		}
		
		layer = (TiledMapTileLayer) tileMap.getLayers().get("switchblocks");
		for(int row = 0; row < layer.getHeight(); row++) {
			for(int col = 0; col < layer.getWidth(); col++) {
				
				// get cell
				Cell cell = layer.getCell(col, row);
				
				// check if cell exists
				if(cell == null) continue;
				if(cell.getTile() == null) continue;
				
				// create a body + fixture from cell
				bdef.type = BodyType.StaticBody;
				bdef.position.set(
					(col + 0.5f) * tileSize / 10,
					(row + 0.5f) * tileSize / 10-320/10
				);
				
				ChainShape cs = new ChainShape();
				Vector2[] v = new Vector2[5];
				v[0] = new Vector2(
						(-tileSize+1) / 2 / 10, (-tileSize+1) / 2 / 10);
				v[1] = new Vector2(
						(-tileSize+1) / 2 / 10, (tileSize-1) / 2 / 10);
				v[2] = new Vector2(
					(tileSize-1) / 2 / 10, (tileSize-1) / 2 / 10);
				v[3] = new Vector2(
						(tileSize-1) / 2 / 10, (-tileSize+1) / 2 / 10);
				v[4] = new Vector2(
					(-tileSize+1) / 2 / 10, (-tileSize+1) / 2 / 10);
				cs.createChain(v);
				fdef.friction = 0;
				fdef.shape = cs;
				fdef.isSensor = true;
				Body body = world.createBody(bdef);
				blocks.add(body);
				body.createFixture(fdef).setUserData("Block");
			}
		}
		
		cl.setBlocks(blocks);
		
		
		player1 = new Player1(new Vector2((spawns[0].x*tileSize+12)/10, (spawns[0].y*tileSize+12-320)/10), world, "1");
		player2 = new Player1(new Vector2((spawns[1].x*tileSize+12)/10, (spawns[1].y*tileSize+12-320)/10), world, "2");
		
		region_player = new TextureRegion[4];
		for(int i = 0; i<4; i++){
			region_player[i] = new TextureRegion(texture_player, i*24, 0, 24, 28);
		}
		region_player_jumping = new TextureRegion(texture_player, 4*24, 0, 24, 28);
		region_player_falling = new TextureRegion(texture_player, 5*24, 0, 24, 28);
		
		animation_player = new Animation(0.2f, region_player);
		animation_player.setPlayMode(PlayMode.LOOP);
		region_bullet = new TextureRegion[2];
		region_bullet[0] = new TextureRegion(texture_bullet_player, 0, 0, 9, 9);
		region_bullet[1] = new TextureRegion(texture_bullet_player, 9, 0, 9, 9);
		animation_bullet = new Animation(0.2f, region_bullet);
		animation_bullet.setPlayMode(PlayMode.LOOP);
		
		region_candle = new TextureRegion[3];
		for(int i = 0; i<3; i++){
			region_candle[i] = new TextureRegion(texture_candle, i*24, 0, 24, 24);
		}
		animation_candle = new Animation(0.2f, region_candle);
		animation_candle.setPlayMode(PlayMode.LOOP);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		posicionCamara(delta);
		cam2.position.x = cam.position.x*10;
		cam2.update();
		cam.update();
		batch.begin();
		batch.draw(texture_background, 0, 0);
		batch.end();
		tmr.setView(cam2);
		if(cl.blockOff())
			tmr.render(new int[]{0,1,4,5,8});
		else
			tmr.render(new int[]{0,1,2,4,5,8});
//		debugRenderer.render(world, cam.combined);
		batch2.begin();
		TextureRegion currentFrame;
		currentFrame = animation_candle.getKeyFrame(stateTime);
		for(int i = 0; i<candles.size; i++){
			batch2.draw(currentFrame, candles.get(i).x, candles.get(i).y, currentFrame.getRegionWidth()/10, currentFrame.getRegionHeight()/10);
		}
		batch2.setProjectionMatrix(cam.combined);
		State st = player1.getState();
		if(st==State.moving){
			currentFrame = animation_player.getKeyFrame(stateTime);
		}
		else if(st==State.stop){
			currentFrame = region_player[0];
		}
		else if(st==State.going_up){
			currentFrame = region_player_jumping;
		}
		else{
			currentFrame = region_player_falling;
		}
		if(player1.isFacingRight())
			batch2.draw(currentFrame, player1.getX()-12/10, player1.getY()-13/10, currentFrame.getRegionWidth()/10, currentFrame.getRegionHeight()/10);
		else
			batch2.draw(currentFrame, player1.getX()-12/10+24/10, player1.getY()-13/10, -currentFrame.getRegionWidth()/10, currentFrame.getRegionHeight()/10);
		st = player2.getState();
		if(st==State.moving){
			currentFrame = animation_player.getKeyFrame(stateTime);
		}
		else if(st==State.stop){
			currentFrame = region_player[0];
		}
		else if(st==State.going_up){
			currentFrame = region_player_jumping;
		}
		else{
			currentFrame = region_player_falling;
		}
		if(player2.isFacingRight())
			batch2.draw(currentFrame, player2.getX()-12/10, player2.getY()-13/10, currentFrame.getRegionWidth()/10, currentFrame.getRegionHeight()/10);
		else
			batch2.draw(currentFrame, player2.getX()-12/10+24/10, player2.getY()-13/10, -currentFrame.getRegionWidth()/10, currentFrame.getRegionHeight()/10);
		currentFrame = animation_bullet.getKeyFrame(stateTime);
		for(int i = 0; i<shoots.size; i++){
			if(shoots.get(i).getRight()){
				batch2.draw(currentFrame, shoots.get(i).getX()-6/10, shoots.get(i).getY()-0.7f, currentFrame.getRegionWidth()/5, currentFrame.getRegionHeight()/5);
			}
			else{
				batch2.draw(currentFrame, shoots.get(i).getX()-6/10+9/10, shoots.get(i).getY()-0.7f, -currentFrame.getRegionWidth()/5, currentFrame.getRegionHeight()/5);
			}
		}
		batch2.end();
		accumulator+=delta;
		stateTime += delta;
		game.timer+=delta;
		while(accumulator>BOX_STEP){
			update(delta);
			if(cl.isPlayerDead()){
				die();
			}
			if(Gdx.input.isKeyPressed(Keys.O)){
				die();
			}
			world.step(BOX_STEP, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS);
			accumulator=0;
		}
		if(player1.getY()<-400/10&&player2.getY()<-400/10 || (cl.getFinish1() && cl.getFinish2())){
			goNextLevel();
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	private void update(float dt){
		player1.update();
		player2.update();
		if(MyInput.isPressed(MyInput.BUTTON3)) {
			if(cl.isPlayer1OnGround()) {
				if(player1.jump())
					game.jumping.play();
			}
			if(cl.isPlayer2OnGround()) {
				if(player2.jump())
					game.jumping.play();
			}
		}
		if(MyInput.isDown(MyInput.BUTTON2)){
			player1.moveRight();
			player2.moveRight();
		}
		if(MyInput.isDown(MyInput.BUTTON1)){
			player1.moveLeft();
			player2.moveLeft();
		}
		if(MyInput.isDown(MyInput.BUTTON4)){
			game.laser.play();
			player1.shoot(shoots, world, stateTime);
			player2.shoot(shoots, world, stateTime);
		}
		for(int i = 0; i<shoots.size; i++){
			if(!shoots.get(i).update(dt)){
				shoots.get(i).destroyBody(world);
				shoots.removeIndex(i);
				i--;
			}
		}
		
	}
	
	public void die(){
		game.death.play();
		game.deaths+=1;
//		player1.setPosition((spawns[0].x*tileSize+12)/10, (spawns[0].y*tileSize+12-320)/10);
//		player2.setPosition((spawns[1].x*tileSize+12)/10, (spawns[1].y*tileSize+12-320)/10);
//		for(Body b: blocks){
//			b.getFixtureList().first().setSensor(true);
//		}
//		cl.blockOn();
		game.setScreen(new Gamescreen(game, thislevel));
	}
	
	private void posicionCamara(float delta){
		float distanciax = (player1.getX()+player2.getX())/2-this.cam.position.x;
		this.cam.translate(distanciax*10*delta, 0);
		if(this.cam.position.x<240/10){
			this.cam.position.x=240/10;
		}
		if(this.cam.position.x>tileSize*numTiles/10-240/10){
			this.cam.position.x = tileSize*numTiles/10 -240/10;
		}
	}
	
	private void goNextLevel(){
		int a = game.screens.indexOf(thislevel, true);
		if(a < game.screens.size-1)
			game.setScreen(new Gamescreen(game, game.screens.get(game.screens.indexOf(thislevel, true)+1)));
		else
			game.setScreen(new mainScreen(game));
	}
}
