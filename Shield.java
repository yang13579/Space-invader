/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceinvader;

/**
 *
 * @author Yutong Yang
 */
public class Shield extends GameObject{
    public Shield(Vector2D position, float radius){
        super(position, new Vector2D(0,0), new Vector2D(0,0), radius);
        circle.setFill(AssetManager.getShieldImage());
    }
    
}
