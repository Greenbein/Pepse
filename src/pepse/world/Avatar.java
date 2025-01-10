package pepse.world;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;

import java.awt.event.KeyEvent;


/**
 * This class implements the game object avatar
 */
public class Avatar extends GameObject {
    //constants
    private static final String AVATAR_TAG = "avatar";
    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -650;
    private static final float GRAVITY = 1000;
    private static final float DEFAULT_SPRITE_HEIGHT = 50;
    private static final float MAX_ENERGY = 100.0f;
    private static final float MAX_UPDATE_ENERGY = 99.5f;
    private static final float MIN_MOVEMENT_ENERGY = 0.5f;
    private static final float MIN_JUMP_ENERGY = 10f;
    private static final float AMOUNT_OF_ENERGY_TO_ADD = 1f;
    private static final String IDLE = "idle";
    private static final String JUMP = "jump";
    private static final String RUN_RIGHT = "run right";
    private static final String RUN_LEFT = "run left";
    private static final String FRUIT = "fruit";
    private static final String GROUND = "ground";
    private static final String TRUNK = "trunk";
    private static final float FRUIT_AMOUNT_OF_ENERGY = 10.0F;
    //Pics for animations
    private static Renderable [] idleArray;
    private static Renderable [] jumpArray;
    private static Renderable [] runArray;
    // other privates
    private UserInputListener inputListener;
    private float energy;
    private ImageReader imageReader;
    private String currentState;
    private String newState;
    private PepseGameManager gameManager;

    /**
     * constructor of Avatar class
     * @param topLeftCorner initial position of avatar
     * @param inputListener ability to track avatar's movement
     * @param imageReader reads the image we give it
     */
    public Avatar(Vector2 topLeftCorner, UserInputListener inputListener,
                  ImageReader imageReader, PepseGameManager gameManager) {
        super( new Vector2(0,
                topLeftCorner.y()*(2.0f/3.0f)-DEFAULT_SPRITE_HEIGHT),Vector2.ONES.mult(50),
                imageReader.readImage("assets/idle_0.png",
                        false));
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
        this.imageReader = imageReader;
        this.energy = MAX_ENERGY;
        this.idleArray = createImageArrayIdle();
        this.jumpArray = createImageArrayJump();
        this.runArray = createImageArrayRun();
        this.currentState = IDLE;
        this.newState = IDLE;
        this.gameManager = gameManager;
        this.setTag(AVATAR_TAG);
    }

    /**
     * this function is a getter for avatar's energy
     * @return the avatar's energy
     */
    public float getEnergy() {
        return energy;
    }

    @Override
    /**
     * this function would update the avatar movement and energy
     */
    public void update(float deltaTime) {
        super.update(deltaTime);
        this.newState = this.currentState;
        if(!avatarRun() && !avatarJumps()){
            avatarIdle();
        }
        if (!this.currentState.equals(this.newState)) {
            switchRenderable(this.newState);
            this.currentState = this.newState;
        }
    }

    // this function handles the case the avatar runs
    private boolean avatarRun() {
        boolean runFlag = false;
        float xVel = 0;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT) &&
                !(inputListener.isKeyPressed(KeyEvent.VK_RIGHT))) {
            if (this.energy >= MIN_MOVEMENT_ENERGY) {
                runFlag = true;
                this.newState = RUN_LEFT;
                xVel -= VELOCITY_X;
                if (getVelocity().y() == 0) {
                    this.energy -= MIN_MOVEMENT_ENERGY;
                }
            }
        }

        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT) &&
                !(inputListener.isKeyPressed(KeyEvent.VK_LEFT))) {
            if (this.energy >= MIN_MOVEMENT_ENERGY) {
                runFlag = true;
                this.newState = RUN_RIGHT;
                xVel += VELOCITY_X;
                if (getVelocity().y() == 0) {
                    this.energy -= MIN_MOVEMENT_ENERGY;
                }
            }
        }
        transform().setVelocityX(xVel);
        return runFlag;
    }

    // this function handles the case the avatar jumps and
    // handles the functionality of rain if we are jumping
    private boolean avatarJumps(){
        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) &&
                getVelocity().y() == 0){
            if(this.energy >= MIN_JUMP_ENERGY){
                this.newState = JUMP;
                this.energy -= MIN_JUMP_ENERGY;
                transform().setVelocityY(VELOCITY_Y);
                this.gameManager.generateRain();
                return true;
            }
        }
        return false;
    }

    // this function handles the case the avatar is idle
    private void avatarIdle(){
        if ((inputListener.isKeyPressed(KeyEvent.VK_RIGHT) &&
                !(inputListener.isKeyPressed(KeyEvent.VK_LEFT))) ||
                (getVelocity().y() == 0 && getVelocity().x() == 0)){
            newState = IDLE;
            if(this.energy < MAX_UPDATE_ENERGY){
                this.energy += AMOUNT_OF_ENERGY_TO_ADD;
            }
            else{
                this.energy = MAX_ENERGY;
            }
        }
    }

    /**
     * This function would be used later to add energy to avatar
     * @param amount amount of energy we would add to avavtar
     */
    public void addEnergy(float amount){
        this.energy += amount;
    }

    // creates Image array of idle
    private Renderable[] createImageArrayIdle(){
        Renderable idleImage1 = this.imageReader.readImage("assets/idle_0.png",false);
        Renderable idleImage2 = this.imageReader.readImage("assets/idle_1.png",false);
        Renderable idleImage3 = this.imageReader.readImage("assets/idle_2.png",false);
        Renderable idleImage4 = this.imageReader.readImage("assets/idle_3.png",false);
        Renderable [] animationsIdle = {idleImage1, idleImage2, idleImage3, idleImage4};
        return animationsIdle;
    }

    /**
     * this func is a get function of idle array
     * @return idle image array
     */
    public Renderable[] getIdleArray(){
        return this.idleArray;
    }

    // creates Image array of jump
    private Renderable[] createImageArrayJump(){
        Renderable jumpImage1 = this.imageReader.readImage
                ("assets/jump_0.png",false);
        Renderable jumpImage2 = this.imageReader.readImage
                ("assets/jump_1.png",false);
        Renderable jumpImage3 = this.imageReader.readImage
                ("assets/jump_2.png",false);
        Renderable jumpImage4 = this.imageReader.readImage
                ("assets/jump_3.png",false);
        Renderable [] animationsJump = {jumpImage1, jumpImage2, jumpImage3, jumpImage4};
        return animationsJump;
    }

    /**
     * this func is a get function of jump array
     * @return jump image array
     */
    public Renderable[] getjumpArray(){
        return this.jumpArray;
    }

    // creates Image array of run
    private Renderable[] createImageArrayRun(){
        Renderable runImage1 = this.imageReader.readImage("assets/run_0.png",false);
        Renderable runImage2 = this.imageReader.readImage("assets/run_1.png",false);
        Renderable runImage3 = this.imageReader.readImage("assets/run_2.png",false);
        Renderable runImage4 = this.imageReader.readImage("assets/run_3.png",false);
        Renderable runImage5 = this.imageReader.readImage("assets/run_4.png",false);
        Renderable runImage6 = this.imageReader.readImage("assets/run_5.png",false);
        Renderable [] animationsRun = {runImage1, runImage2, runImage3, runImage4,runImage5,runImage6};
        return animationsRun;
    }

    /**
     *
     * @return run image array
     */
    public Renderable[] getRunArray(){
        return this.runArray;
    }

    // switch renderable animation
    private void switchRenderable(String state){
        Renderable [] imageArray;
        AnimationRenderable animation;
        switch(state){
            case IDLE:
                animation = new AnimationRenderable(getIdleArray(),2);
                this.renderer().setRenderable(animation);
                break;
            case JUMP:
                animation = new AnimationRenderable(getjumpArray(),2);
                this.renderer().setRenderable(animation);
                break;
            case RUN_LEFT:
                animation = new AnimationRenderable(getRunArray(),1);
                this.renderer().setRenderable(animation);
                this.renderer().setIsFlippedHorizontally(true);
                break;
            case RUN_RIGHT:
                animation = new AnimationRenderable(getRunArray(),1);
                this.renderer().setRenderable(animation);
                this.renderer().setIsFlippedHorizontally(false);
                break;
        }
    }

    @Override
    /**
     * this function defines which objects the avatar
     * should collide with
     */
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals(FRUIT)||other.getTag().equals(GROUND)
                ||other.getTag().equals(TRUNK);
    }

    @Override
    /**
     * this function defines
     */
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        switch (other.getTag()){
            case GROUND:
                this.transform().setVelocityY(0);
                break;
            case TRUNK:
                this.transform().setVelocityX(0);
                break;
            case FRUIT:
                if(this.energy + FRUIT_AMOUNT_OF_ENERGY > MAX_ENERGY){
                    this.energy=MAX_ENERGY;
                }
                else{
                    this.energy += FRUIT_AMOUNT_OF_ENERGY;
                }
                this.gameManager.removeObject(other, Layer.DEFAULT);
                break;
            default:
                break;

        }
    }
}
