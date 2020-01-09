package spaceinvader;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author admin
 */
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.paint.ImagePattern;



public class AssetManager {
    static private Background backgroundImage = null;

    //static private ArrayList<ImagePattern> planets = new ArrayList<>();
    static private ImagePattern alienImage = null;
    static private ImagePattern alienImage2 = null;
    static private ImagePattern alienImage3 = null;
    static private ImagePattern alienImage4 = null;
    static private ImagePattern spaceshipImage = null;
    static private ImagePattern bulletImage = null;
    static private ImagePattern enemyBulletImage = null;
    static private ImagePattern shieldImage = null;
    
    static private Media backgroundMusic = null;
    static private AudioClip explosionSound = null;
    static private AudioClip shootingSound = null;
    static private AudioClip victorySound = null;
    static private AudioClip loseSound = null;
    
    
    static private String fileURL(String relativePath)
    {
        return new File(relativePath).toURI().toString();
    }
    
    static public void preloadAllAssets()
    {
        // Preload all images
        Image background = new Image(fileURL("./assets/images/back.jpg"));
        backgroundImage = new Background(
                            new BackgroundImage(background, 
                                                BackgroundRepeat.NO_REPEAT, 
                                                BackgroundRepeat.NO_REPEAT, 
                                                BackgroundPosition.DEFAULT,
                                                BackgroundSize.DEFAULT));
        
        
        bulletImage = new ImagePattern(new Image(fileURL("./assets/images/bullet.jpg")));
        alienImage = new ImagePattern(new Image(fileURL("./assets/images/alien.png")));
        alienImage2 = new ImagePattern(new Image(fileURL("./assets/images/alien2.png")));
        alienImage3 = new ImagePattern(new Image(fileURL("./assets/images/alien3.png")));
        alienImage4 = new ImagePattern(new Image(fileURL("./assets/images/alien4.png")));
        spaceshipImage = new ImagePattern(new Image(fileURL("./assets/images/spaceship.png")));
        enemyBulletImage = new ImagePattern(new Image(fileURL("./assets/images/enemyBullet.jpg")));
        shieldImage = new ImagePattern(new Image(fileURL("./assets/images/shield.png")));
        // Preload all music tracks
        backgroundMusic = new Media(fileURL("./assets/music/bgm.mp3"));

        // Preload all sound effects
        explosionSound = new AudioClip(fileURL("./assets/soundfx/explosion.wav"));
        shootingSound =  new AudioClip(fileURL("./assets/soundfx/shooting.wav"));
        victorySound = new AudioClip(fileURL("./assets/soundfx/victory.wav"));
        loseSound = new AudioClip(fileURL("./assets/soundfx/lose.wav"));
    }
    
    static public Background getBackgroundImage()
    {
        return backgroundImage;        
    }
    
    static public ImagePattern getAlienImage()
    {
        return alienImage;
    }
    
    static public ImagePattern getAlien2Image()
    {
        return alienImage2;
    }
    
    static public ImagePattern getAlien3Image()
    {
        return alienImage3;
    }
    
    static public ImagePattern getAlien4Image()
    {
        return alienImage4;
    }
    
    static public ImagePattern getBulletImage()
    {
        return bulletImage;
    }

    static public ImagePattern getSpaceshipImage()
    {
        return spaceshipImage;
    }
    
    static public ImagePattern getEnemyBulletImage()
    {
        return enemyBulletImage;
    }
    
    static public ImagePattern getShieldImage()
    {
        return shieldImage;
    }


    static public Media getBackgroundMusic()
    {
        return backgroundMusic;
    }
    
    static public AudioClip getExplosionSound()
    {
        return explosionSound;
    }
    
    static public AudioClip getShootingSound()
    {
        return shootingSound;
    }
    
    static public AudioClip getVictorySound()
    {
        return victorySound;
    }
    
    static public AudioClip getLoseSound()
    {
        return loseSound;
    }
}

