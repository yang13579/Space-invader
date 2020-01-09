/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceinvader;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;

/**
 *
 * @author cstuser
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    AnchorPane pane;

    @FXML
    Label resultLabel;

    @FXML
    Label livesLabel;

    @FXML
    Label scoreLabel;

    private MediaPlayer mediaPlayer = null;
    private AudioClip endGameClip = null;
    private boolean gameFinished = false;
    private double lastFrameTime = 0.0;
    private SpaceShip cursor = null;
    private Projectile enemyBullet = null;
    //private Projectile bullet = null;
    private ArrayList<Projectile> bulletList = new ArrayList<>();
    private ArrayList<GameObject> enemyList = new ArrayList<>();
    private ArrayList<Vector2D> enemyVelocityList = new ArrayList<>();
    private ArrayList<Shield> shieldList = new ArrayList<>();
    private final int BULLET_VELOCITY = 450;
    private int lives = 3;
    private int score = 0;
    private double ENEMY_SHOOT_TIME_DELAY = 4.5;
    private final double RESPOND_TIME = 3.0;
    private double respondDelay = RESPOND_TIME;
    private double enemyShootDelay = ENEMY_SHOOT_TIME_DELAY;
    private boolean isDead = false;
    private final Vector2D INITIAL_POSITION = new Vector2D(431, 780);
    private Vector2D ENEMY_VELOCITY = new Vector2D(20, 3);
    private final float SHIELD_RADIUS = 45;
    private final double VELOCITY_CHANGE = 0.01;

    public void addToPane(Node node) {
        pane.getChildren().add(node);
    }

    public void removeFromPane(Node node) {
        pane.getChildren().remove(node);
    }

    @FXML
    public void onMouseMoved(MouseEvent e) {
        //System.out.println("Move: " + e.getX() + ", " + e.getY());
        cursor.setPosition(new Vector2D(e.getX(), 780));
    }

    @FXML
    public void onMouseClicked(MouseEvent e) {
        //System.out.println("CLICK");
        if (isDead == false) {
            Vector2D position = new Vector2D(e.getX(), 780);
            Vector2D velocity = new Vector2D(0, -BULLET_VELOCITY);
            Projectile bullet = new Projectile(position, velocity, 5);
            addToPane(bullet.getCircle());
            bulletList.add(bullet);

            AudioClip clip = new AudioClip(new File("./assets/soundfx/shooting.wav").toURI().toString());
            clip.play();
        }
    }

    public void enemyShoot() {
        Random rand = new Random();
        int value = rand.nextInt(enemyList.size());
        if (enemyList.get(value) != null) {
            GameObject obj = enemyList.get(value);
            Vector2D position = new Vector2D(obj.getCircle().getCenterX(), obj.getCircle().getCenterY());
            Vector2D velocity = new Vector2D(0, BULLET_VELOCITY);
            enemyBullet = new Projectile(position, velocity, 5);
            addToPane(enemyBullet.getCircle());
            enemyBullet.getCircle().setFill(AssetManager.getEnemyBulletImage());

            AudioClip clip = new AudioClip(new File("./assets/soundfx/shooting.wav").toURI().toString());
            clip.play();
        }
    }

    public void gameWin() {
        resultLabel.setText("VICTORY!");
        mediaPlayer.pause();

        endGameClip = new AudioClip(new File("./assets/soundfx/victory.wav").toURI().toString());
        endGameClip.play();
        
        for(int i = 0; i < shieldList.size(); i++){
            removeFromPane(shieldList.get(i).getCircle());
        }
    }

    public void gameLose() {
        resultLabel.setText("Game Lose");
        mediaPlayer.pause();

        endGameClip = new AudioClip(new File("./assets/soundfx/lose.wav").toURI().toString());
        endGameClip.play();

        // to remove all gameObjects
        for (int i = 0; i < enemyList.size(); i++) {
            //enemyList.get(i).setVelocity(new Vector2D(0, 0));
            removeFromPane(enemyList.get(i).getCircle());
        }
        removeFromPane(cursor.getCircle());
        for(int i = 0; i < shieldList.size(); i++){
            removeFromPane(shieldList.get(i).getCircle());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        livesLabel.setText("LIVES   " + lives);
        scoreLabel.setText("SCORE " + score);

        lastFrameTime = 0.0f;
        long initialTime = System.nanoTime();

        AssetManager.preloadAllAssets();

        cursor = new SpaceShip();

        // to set the initial positiion for the spaceship
        cursor.setPosition(INITIAL_POSITION);

        addToPane(cursor.getCircle());

        Shield shield1 = new Shield(new Vector2D(190, 620), SHIELD_RADIUS);
        Shield shield2 = new Shield(new Vector2D(430, 620), SHIELD_RADIUS);
        Shield shield3 = new Shield(new Vector2D(670, 620), SHIELD_RADIUS);
        Shield shield4 = new Shield(new Vector2D(910, 620), SHIELD_RADIUS);

        shieldList.add(shield1);
        shieldList.add(shield2);
        shieldList.add(shield3);
        shieldList.add(shield4);

        for (int i = 0; i < shieldList.size(); i++) {
            addToPane(shieldList.get(i).getCircle());
        }

        //set the background image and background music
        Image backgroundImage = new Image(new File("./assets/images/back.jpg").toURI().toString());
        Background background = new Background(new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT));
        pane.setBackground(background);

        String musicFile = "./assets/music/bgm.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();

        // add the enemies
        int y = 10;
        for (int row = 0; row < 4; row++) {
            int x = 10;
            for (int column = 0; column < 15; column++) {
                int radius = 5;
                Circle c = new Circle(0, 0, radius);
                c.setFill(AssetManager.getAlienImage());
                c.setCenterX(x);
                c.setCenterY(y);
                Vector2D position = new Vector2D(x, y);
                //GameObject e;
                if (row == 0) {
                    GameObject e = new Enemy(position, ENEMY_VELOCITY, 20);
                    enemyList.add(e);
                    addToPane(e.getCircle());
                }
                if (row == 1) {
                    GameObject e = new Enemy(position, ENEMY_VELOCITY, 20);
                    enemyList.add(e);
                    e.getCircle().setFill(AssetManager.getAlien2Image());
                    addToPane(e.getCircle());
                }
                if (row == 2) {
                    GameObject e = new Enemy(position, ENEMY_VELOCITY, 20);
                    enemyList.add(e);
                    e.getCircle().setFill(AssetManager.getAlien3Image());
                    addToPane(e.getCircle());
                }
                if (row == 3) {
                    GameObject e = new Enemy(position, ENEMY_VELOCITY, 20);
                    enemyList.add(e);
                    e.getCircle().setFill(AssetManager.getAlien4Image());
                    addToPane(e.getCircle());
                }

                x += 55;
            }
            y += 45;
        }

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                double currentTime = (now - initialTime) / 1000000000.0;
                double frameDeltaTime = currentTime - lastFrameTime;
                lastFrameTime = currentTime;

                enemyShootDelay -= frameDeltaTime;

                if (enemyShootDelay < 0.0) {
                    // shoot
                    if (enemyList.size() != 0 && lives != 0) {
                        enemyShoot();
                        
                        if(ENEMY_SHOOT_TIME_DELAY > 1.5){
                            ENEMY_SHOOT_TIME_DELAY -= 0.2;
                        }
                    }
                    enemyShootDelay += ENEMY_SHOOT_TIME_DELAY;

                    // make the spaceship response if it's dead
                    if (isDead == true) {
                        addToPane(cursor.getCircle());
                        isDead = false;
                    }

                }
                /*
                if (isDead == true) {
                    respondDelay -= frameDeltaTime;
                    if (respondDelay < 0.0) {
                        addToPane(cursor.getCircle());
                        isDead = false;
                    }
                }
                 */

                for (GameObject obj : enemyList) {
                    obj.update(frameDeltaTime);
                }
                cursor.update(frameDeltaTime);
                //bullet.update(frameDeltaTime);
                for (GameObject obj : bulletList) {
                    obj.update(frameDeltaTime);
                }

                if (enemyBullet != null) {
                    enemyBullet.update(frameDeltaTime);
                }

                if (enemyList.size() != 0) {
                    for (int i = 0; i < enemyList.size(); i++) {
                        GameObject ob = enemyList.get(i);
                        Circle c = ob.getCircle();
                        Vector2D position = new Vector2D(c.getCenterX(), c.getCenterY());
                        Vector2D v = ob.getVelocity();
                        position = position.add(v.mult(frameDeltaTime));
                        c.setCenterX(position.getX());
                        c.setCenterY(position.getY());

                        if (c.getCenterX() - c.getRadius() < 0) {
                            //v.setX(Math.abs(v.getX()));
                            for (int x = 0; x < enemyList.size(); x++) {

                                enemyList.get(x).getVelocity().setX(Math.abs(enemyList.get(x).getVelocity().getX()));

                            }

                        }

                        if (c.getCenterX() + c.getRadius() > pane.getWidth()) {
                            //v.setX(-Math.abs(v.getX()));
                            for (int x = 0; x < enemyList.size(); x++) {

                                enemyList.get(x).getVelocity().setX(-Math.abs(enemyList.get(x).getVelocity().getX()));

                            }

                        }
                    }

                    // detect if an enemy hit the spaceship
                    for (int i = 0; i < enemyList.size(); i++) {
                        Circle circle1 = enemyList.get(i).getCircle();
                        Circle circle2 = cursor.getCircle();

                        Vector2D c1 = new Vector2D(circle1.getCenterX(), circle1.getCenterY());
                        Vector2D c2 = new Vector2D(circle2.getCenterX(), circle2.getCenterY());

                        Vector2D n = c2.sub(c1);
                        double distance = n.magnitude();

                        if (isDead == false && gameFinished == false) {
                            if (distance < circle1.getRadius() + circle2.getRadius()) {
                                AudioClip clip = new AudioClip(new File("./assets/soundfx/explosion.wav").toURI().toString());
                                clip.play();
                                removeFromPane(cursor.getCircle());
                                //System.out.println("You are dead!");
                                --lives;
                                livesLabel.setText("LIVES   " + lives);
                                isDead = true;

                            }
                        }
                    }
                }

                // detect collision between bullet from the spaceship and enemy
                if (bulletList.size() != 0) {
                    for (int i = 0; i < bulletList.size(); i++) {
                        for (int j = 0; j < enemyList.size(); j++) {

                            if (bulletList.get(i) != null || enemyList.get(j) != null) {
                                Circle circle1 = bulletList.get(i).getCircle();
                                Circle circle2 = enemyList.get(j).getCircle();

                                Vector2D c1 = new Vector2D(circle1.getCenterX(), circle1.getCenterY());
                                Vector2D c2 = new Vector2D(circle2.getCenterX(), circle2.getCenterY());

                                Vector2D n = c2.sub(c1);
                                double distance = n.magnitude();

                                if (distance < circle1.getRadius() + circle2.getRadius()) {
                                    //AudioClip clip = new AudioClip(new File("./assets/soundfx/explosion.wav").toURI().toString());
                                    endGameClip = new AudioClip(new File("./assets/soundfx/explosion.wav").toURI().toString());
                                    endGameClip.play();
                                    //clip.play();

                                    score++;
                                    scoreLabel.setText("SCORE " + score);
                                    //System.out.println("Your score is: " + score);
                                    
                                    removeFromPane(circle1);
                                    removeFromPane(circle2);
                                    bulletList.remove(i);
                                    enemyList.remove(j);

                                    j = enemyList.size();

                                }
                            }
                        }

                    }
                }
                
                // detect if bullet hits the shield
                if(bulletList != null){
                    for(int i = 0; i< bulletList.size(); i++){
                        for (int x = 0; x < shieldList.size(); x++) {
                            if (bulletList != null && lives != 0) {
                                Circle circle1 = bulletList.get(i).getCircle();
                                Circle circle2 = shieldList.get(x).getCircle();

                                Vector2D c1 = new Vector2D(circle1.getCenterX(), circle1.getCenterY());
                                Vector2D c2 = new Vector2D(circle2.getCenterX(), circle2.getCenterY());

                                Vector2D n = c2.sub(c1);
                                double distance = n.magnitude();
                                if (distance < circle1.getRadius() + circle2.getRadius()) {
                                    bulletList.remove(i);
                                    removeFromPane(circle1);
                                    x = shieldList.size();
                                }
                            }
                        }
                    }
                }

                
                // detect collision between spaceship and enemy bullet
                if (enemyBullet != null) {
                    Circle circle1 = enemyBullet.getCircle();
                    Circle circle2 = cursor.getCircle();

                    Vector2D c1 = new Vector2D(circle1.getCenterX(), circle1.getCenterY());
                    Vector2D c2 = new Vector2D(circle2.getCenterX(), circle2.getCenterY());

                    Vector2D n = c2.sub(c1);
                    double distance = n.magnitude();

                    if (isDead == false) {
                        if (distance < circle1.getRadius() + circle2.getRadius()) {
                            //AudioClip clip = new AudioClip(new File("./assets/soundfx/explosion.wav").toURI().toString());
                            //clip.play();
                            endGameClip = new AudioClip(new File("./assets/soundfx/explosion.wav").toURI().toString());
                            endGameClip.play();
                            
                            removeFromPane(cursor.getCircle());
                            //System.out.println("You are dead!");
                            --lives;
                            livesLabel.setText("LIVES   " + lives);
                            //System.out.println("Your live is " + lives);
                            isDead = true;
                        }
                    }
                    
                    //detect if the enemyBullet hits the shield
                    for(int x = 0; x < shieldList.size(); x++){
                        Circle circle3 = shieldList.get(x).getCircle();
                        Vector2D c3 = new Vector2D(circle3.getCenterX(), circle3.getCenterY());

                                Vector2D n1 = c3.sub(c1);
                                double distance1 = n1.magnitude();
                                if (distance1 < circle1.getRadius() + circle3.getRadius()) {
                                    removeFromPane(circle1);
                                    enemyBullet = null;
                                }
                    }
                }
                
                // the enemy acclerates while the game continues
                if(enemyList.size() != 0){
                    for(int i = 0; i<enemyList.size(); i++){
                        GameObject obj = enemyList.get(i);
                        double xVelocity = obj.getVelocity().getX();
                        double yVelocity = obj.getVelocity().getY();
                        if(xVelocity > 0 && xVelocity < 100)
                            xVelocity += VELOCITY_CHANGE;
                        
                        else if(xVelocity < 0 && xVelocity > -100)
                            xVelocity -= VELOCITY_CHANGE;
                        
                        yVelocity += 0.0004;
                        obj.setVelocity(new Vector2D(xVelocity, yVelocity));
                    }
                }

                if (enemyList.size() == 0 && gameFinished == false) {
                    gameWin();
                    gameFinished = true;
                }

                // to check if the game is lost or not
                if (lives == 0 && gameFinished == false) {
                    gameLose();
                    gameFinished = true;
                }
                if (enemyList.size() != 0) {
                    for (int i = 0; i < enemyList.size(); i++) {
                        GameObject obj = enemyList.get(i);
                        if (obj.getCircle().getCenterY() > cursor.getCircle().getCenterY()) {
                            lives = 0;
                            livesLabel.setText("LIVES   " + lives);
                        }
                    }

                }

                scoreLabel.toFront();
                livesLabel.toFront();
                resultLabel.toFront();
            }
        }.start();

    }

}
