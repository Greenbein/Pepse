package pepse.world;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import java.awt.event.KeyEvent;

import pepse.PepseConstants.

/**
 * This class implements the game object avatar
 */
public class Avatar extends GameObject {
    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -650;
    private static final float GRAVITY = 1000;
    private static final float DEFAULT_SPRITE_HEIGHT = 50;
    private static final float MAX_ENERGY = 100.0f;
    private static final float MAX_UPDATE_ENERGY = 99.5f;
    private static final float MIN_MOVEMENT_ENERGY = 0.5f;
    private static final float MIN_JUMP_ENERGY = 10f;
    private static final float AMOUNT_OF_ENERGY_TO_ADD = 1f;
    //Pics for animations
    private static Renderable idleZero, idleOne, idleTwo,idleThree;
    private static Renderable jumpZero, jumpOne, jumpTwo, jumpThree;
    private static Renderable runZero, runOne, runTwo, runThree, runFour, runFive;
    private UserInputListener inputListener;
    private Renderable avatarRenderable;
    private float energy;
    private ImageReader imageReader;

    /**
     * constructor of Avatar class
     * @param topLeftCorner initial position of avatar
     * @param inputListener ability to track avatar's movement
     * @param imageReader reads the image we give it
     */
    public Avatar(Vector2 topLeftCorner, UserInputListener inputListener,
                  ImageReader imageReader) {
        super( new Vector2(0,
                topLeftCorner.y()*(2.0f/3.0f)-DEFAULT_SPRITE_HEIGHT),Vector2.ONES.mult(50),
                imageReader.readImage("assets/idle_0.png",
                        false));
        System.out.println("X "+topLeftCorner.x() + " Y "+
                (topLeftCorner.y()*(2.0f/3.0f)-DEFAULT_SPRITE_HEIGHT));
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
        this.imageReader = imageReader;
        this.energy = MAX_ENERGY;

    }

    /**
     *
     * @return the avatar's energy
     */
    public float getEnergy() {
        return energy;
    }

    @Override
    /**
     * this function would update the avatar movment and energy
     */
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT) &&
                 !(inputListener.isKeyPressed(KeyEvent.VK_RIGHT))){
            if(this.energy >= MIN_MOVEMENT_ENERGY){
                xVel -= VELOCITY_X;
                if(getVelocity().y() == 0) {
                    this.energy -= MIN_MOVEMENT_ENERGY;
                }
            }
        }

        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT) &&
                !(inputListener.isKeyPressed(KeyEvent.VK_LEFT))){
            if(this.energy >= MIN_MOVEMENT_ENERGY){
                xVel += VELOCITY_X;
                if(getVelocity().y() == 0) {
                    this.energy -= MIN_MOVEMENT_ENERGY;
                }
            }
        }
        transform().setVelocityX(xVel);
        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) &&
                getVelocity().y() == 0 && this.energy >= MIN_JUMP_ENERGY){
            this.energy -= MIN_JUMP_ENERGY;
            transform().setVelocityY(VELOCITY_Y);
        }

        if (getVelocity().y() == 0 && getVelocity().x()==0){
            if(this.energy < MAX_UPDATE_ENERGY){
                this.energy += AMOUNT_OF_ENERGY_TO_ADD;
            }
            else if(this.energy == MAX_UPDATE_ENERGY){
                this.energy = MAX_ENERGY;
            }
        }
        switchRenderable("idle");
    }

    /**
     * This function would be used later to add energy to avatar
     * @param amount amount of energy we would add to avavtar
     */
    public void addEnergy(float amount){
        this.energy += amount;
    }

    private void uploadImages(){
        idleZero = this.imageReader.readImage(IDLE_ZERO_PATH,false);
        idleOne = this.imageReader.readImage(IDLE_ONE_PATH, false);
        idleTwo = this.imageReader.readImage(IDLE_TWO_PATH, false);
        idleThree = this.imageReader.readImage(IDLE_THREE_PATH, false);

        jumpZero = this.imageReader.readImage(JUMP_ZERO_PATH,false);
        jumpOne = this.imageReader.readImage(JUMP_ONE_PATH, false);
        jumpTwo = this.imageReader.readImage(JUMP_TWO_PATH, false);
        jumpThree = this.imageReader.readImage(JUMP_THREE_PATH, false);

        runZero = this.imageReader.readImage(RUN_ZERO_PATH,false);
        runOne = this.imageReader.readImage(RUN_ONE_PATH, false);
        runTwo = this.imageReader.readImage(RUN_TWO_PATH, false);
        runThree = this.imageReader.readImage(RUN_THREE_PATH, false);
        runFour = this.imageReader.readImage(RUN_FOUR_PATH, false);
        runFive = this.imageReader.readImage(RUN_FIVE_PATH, false);




    }

    // switch rendarable animation
    private void switchRenderable(String state){
        Renderable IdleImage1 = this.imageReader.readImage("assets/idle_0.png",false);
        Renderable IdleImage2 = this.imageReader.readImage("assets/idle_1.png",false);
        Renderable IdleImage3 = this.imageReader.readImage("assets/idle_2.png",false);
        Renderable IdleImage4 = this.imageReader.readImage("assets/idle_3.png",false);
        switch(state){
            case "idle":
                Renderable [] animationsIdle = {IdleImage1, IdleImage2, IdleImage3, IdleImage4};
                AnimationRenderable animation = new AnimationRenderable(animationsIdle,10);
                this.renderer().setRenderable(animation);
            default:

        }
    }
}
