package net.nikonorov.lazerninja.logic;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;

/**
 * Created by vitaly on 10.04.16.
 */
public class Lazer {

    public ModelInstance instance;
    public float dx;
    public float dy;
    public float dz;
    public btCollisionShape collisionShape;
    public btCollisionObject collisionObject;

}
