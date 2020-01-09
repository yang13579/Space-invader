/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceinvader;

/**
 *
 * @author cstuser
 */
public class SpaceShip extends GameObject{
    public SpaceShip(){
        super(new Vector2D(0,0), new Vector2D(0,0), new Vector2D(0,0), 20.0);
        circle.setFill(AssetManager.getSpaceshipImage());
    }
    
    @Override
    public void update(double dt){
        super.update(dt);
        
        circle.toFront();
    }
    
}
