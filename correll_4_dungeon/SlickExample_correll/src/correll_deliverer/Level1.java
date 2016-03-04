package correll_deliverer;

import static correll_deliverer.Level2.blueb;
import static correll_deliverer.Level2.counter;
import static correll_deliverer.Level2.damage;
import static correll_deliverer.Level2.player;
import static correll_deliverer.Level2.redb;
import static correll_deliverer.Level2.yellowb;
import java.util.ArrayList;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.tiled.TiledMap;

public class Level1 extends BasicGameState{
    
    public GoodPortal gportal;
    public Special special;
    
    public boolean reveal = false;
    public boolean pass = false;
    
    public ArrayList<GoodPortal> gp = new ArrayList();
    public ArrayList<Item> stuff = new ArrayList();
    public ArrayList<Special> item = new ArrayList();

    private static TiledMap grassMap2;
    private static AppGameContainer app;
    private static Camera camera;
    private static final int SIZE = 32;
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 750;
    
    public static Player player;

    public Level1(int xSize, int ySize) {

    }

    public void init(GameContainer gc, StateBasedGame sbg)
            throws SlickException {

        gc.setTargetFrameRate(60);
        gc.setShowFPS(false);
        grassMap2 = new TiledMap("res/level1.tmx");
        camera = new Camera(gc, grassMap2);
        Blocked2.blocked2 = new boolean[grassMap2.getWidth()][grassMap2.getHeight()];
        
        for (int xAxis = 0; xAxis < grassMap2.getWidth(); xAxis++) {
            for (int yAxis = 0; yAxis < grassMap2.getHeight(); yAxis++) {
                int tileID = grassMap2.getTileId(xAxis, yAxis, 0);
                String value = grassMap2.getTileProperty(tileID,
                        "blocked2", "false");
                if ("true".equals(value)) {
                    Blocked2.blocked2[xAxis][yAxis] = true;
                    
                }
            }
        }
        
        for (int xAxis = 0; xAxis < grassMap2.getWidth(); xAxis++) {
            for (int yAxis = 0; yAxis < grassMap2.getHeight(); yAxis++) {
                int xBlock = (int) xAxis;
                int yBlock = (int) yAxis;
                if (!Blocked2.blocked2[xBlock][yBlock]) {
                    if (yBlock % 7 == 0 && xBlock % 15 == 0) {

                    }
                }
            }
        }

        for (int xAxis = 0; xAxis < grassMap2.getWidth(); xAxis++) {
            for (int yAxis = 0; yAxis < grassMap2.getHeight(); yAxis++) {
                int xBlock = (int) xAxis;
                int yBlock = (int) yAxis;
                if (!Blocked2.blocked2[xBlock][yBlock]) {
                    if (xBlock % 9 == 0 && yBlock % 25 == 0) {

                    }
                }
            }
        }

        player = new Player();
        player.gameState = 1;
        
        gportal = new GoodPortal(55, 670);
        special = new Special(180, 705);
        
        gp.add(gportal);
        item.add(special);

    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
            throws SlickException {

        camera.centerOn((int) player.x, (int) player.y);
        camera.drawMap();
        camera.translateGraphics();
        player.sprite.draw((int) player.x, (int) player.y);
                //coordinates
		g.drawString("x: " + (int)player.x + "y: " +(int)player.y , player.x, player.y - 10);
                
            for (GoodPortal p : gp) {
                if (p.isvisible) {
                
                    p.currentImage.draw(p.x, p.y);
                    //g.draw(s.hitbox);

            }
        }
                
            for (Special s : item) {
                if (s.isvisible) {
                
                    s.currentImage.draw(s.x, s.y);
                    //g.draw(s.hitbox);

            }
        }

            if (reveal) {
                g.drawString("Press R to turn on the Portal!" + 50, camera.cameraX + 10, camera.cameraY + 15);
        }
            
        }

    public void update(GameContainer gc, StateBasedGame sbg, int delta)
            throws SlickException {

        Input input = gc.getInput();
        float fdelta = delta * player.speed;
        player.setpdelta(fdelta);
        double rightlimit = (grassMap2.getWidth() * SIZE) - (SIZE * 0.75);
        float projectedright = player.x + fdelta + SIZE;
        boolean cangoright = projectedright < rightlimit;
        
        if (input.isKeyDown(Input.KEY_UP)) {
            player.setDirection(0);
            player.sprite = player.up;
            float fdsc = (float) (fdelta - (SIZE * .15));

            if (!(isBlocked2(player.x, player.y + 5 -fdelta) || isBlocked2((float) (player.x + SIZE + 1.5), player.y + fdelta))) {
                player.sprite.update(delta);
                player.y -= fdelta;
                
            }
        } else if (input.isKeyDown(Input.KEY_DOWN)) {
            player.setDirection(2);
            player.sprite = player.down;

            if (!isBlocked2(player.x, player.y + SIZE + fdelta)
                    || !isBlocked2(player.x + SIZE - 1, player.y + SIZE + fdelta)) {
                player.sprite.update(delta);
                player.y += fdelta;

            }
        } else if (input.isKeyDown(Input.KEY_LEFT)) {
            player.setDirection(3);
            player.sprite = player.left;

            if ((player.getPlayersX() > 5) && (!(isBlocked2(player.x - fdelta, player.y) || isBlocked2(player.x
                    - fdelta, player.y + SIZE - 1)) || isBlocked2(player.x - SIZE / 2 - fdelta, player.y
                            + SIZE - 1))) {
                player.sprite.update(delta);
                player.x -= fdelta;

            }
        } else if (input.isKeyDown(Input.KEY_RIGHT)) {
            player.setDirection(1);
            player.sprite = player.right;

            if (cangoright
                    && (!(isBlocked2(player.x + SIZE + fdelta,
                            player.y) || isBlocked2(player.x + SIZE + fdelta, player.y
                            + SIZE - 1)))) {
			//player.sprite.update(delta);
                player.x += fdelta;

            }
        
        } else if (input.isKeyDown(Input.KEY_I)) {     
            
            sbg.enterState(5, new FadeOutTransition(Color.white), new FadeInTransition(Color.white));
            
        } else if (input.isKeyDown(Input.KEY_R) && (reveal)) {     
            
            pass = true;
            
        }

        Level1.player.rect.setLocation(Level1.player.getPlayershitboxX(),
                Level1.player.getPlayershitboxY() + 50);
        
        for (Special s : item) {
            if (Level1.player.rect.intersects(s.hitbox)) {
                if (s.isvisible) {
                    
                    reveal = true;
                    s.isvisible = false;
                    
                }
            }
        }
        
        for (GoodPortal p : gp) {
            if (Level1.player.rect.intersects(p.hitbox)) {
                if (p.isvisible) {
                    if(pass) {
                    
                    sbg.enterState(2, new FadeOutTransition(Color.white), new FadeInTransition(Color.white));
                    
                }
            }
        }
    }
    }

    public int getID() {
        
        return 1;

    }

    public void makevisible() {
        for (Item i : stuff) {
            i.isvisible = true;
            
        }
    }

    private boolean isBlocked2(float tx, float ty) {
        int xBlock = (int) tx / SIZE;
        int yBlock = (int) ty / SIZE;
        return Blocked2.blocked2[xBlock][yBlock];

    }
}
