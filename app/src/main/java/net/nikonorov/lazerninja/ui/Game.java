package net.nikonorov.lazerninja.ui;

/**
 * Created by vitaly on 20.03.16.
 */
import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.CardBoardAndroidApplication;
import com.badlogic.gdx.backends.android.CardBoardApplicationListener;
import com.badlogic.gdx.backends.android.CardboardCamera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.CollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btCollisionAlgorithm;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btConvexHullShape;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcherInfo;
import com.badlogic.gdx.physics.bullet.collision.btManifoldResult;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.SharedLibraryLoader;
import com.badlogic.gdx.utils.UBJsonReader;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;

import net.nikonorov.lazerninja.App;
import net.nikonorov.lazerninja.logic.Lazer;
import net.nikonorov.lazerninja.logic.Saber;

import java.util.ArrayList;
import java.util.Random;


public class Game extends CardBoardAndroidApplication implements CardBoardApplicationListener{

    private CardboardCamera cam;
    private ModelInstance[] troopers = new ModelInstance[4];
    private Saber saber;
    private ModelInstance scene;
    private ModelBatch batch;
    private Stage stage;
    private Label label;
    private BitmapFont font;
    private Environment environment;
    private static final float Z_NEAR = 0.1f;
    private static final float Z_FAR = 1000.0f;
    private static final float CAMERA_Z = 0;//.1f;
    private ArrayList<Lazer> lazers = new ArrayList<>();
    private Model bulletModel;
    private Random random;

    private int hp = 20;

    private long lastTime = 0;

    private btCollisionDispatcher dispatcher;
    private btDefaultCollisionConfiguration collisionConfig;

    static {
        new SharedLibraryLoader().load("gdx-bullet");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(this, config);
    }

    private boolean checkCollision(btCollisionObject obj0, btCollisionObject obj1) {
        CollisionObjectWrapper co0 = new CollisionObjectWrapper(obj0);
        CollisionObjectWrapper co1 = new CollisionObjectWrapper(obj1);

        btCollisionAlgorithm algorithm = dispatcher.findAlgorithm(co0.wrapper, co1.wrapper);

        btDispatcherInfo info = new btDispatcherInfo();
        btManifoldResult result = new btManifoldResult(co0.wrapper, co1.wrapper);

        algorithm.processCollision(co0.wrapper, co1.wrapper, info, result);

        boolean r = result.getPersistentManifold().getNumContacts() > 0;

        dispatcher.freeCollisionAlgorithm(algorithm.getCPointer());
        result.dispose();
        info.dispose();
        co1.dispose();
        co0.dispose();

        return r;

    }

    @Override
    public void create() {
        Bullet.init();
        cam = new CardboardCamera();
        cam.position.set(0f, 3f, CAMERA_Z);
        cam.lookAt(0,3f,-5f);
        cam.near = Z_NEAR;
        cam.far = Z_FAR;

        random = new Random();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

//        ModelBuilder modelBuilder = new ModelBuilder();
//        model = modelBuilder.createBox(5f, 5f, 5f,
//                new Material(ColorAttribute.createDiffuse(Color.GREEN)),
//                Usage.Position | Usage.Normal);
//        instance = new ModelInstance(model);
//        instance.transform.translate(0, 0, -50);


        batch = new ModelBatch();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));

        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 10f, 10f, 10f, 10f));
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 100f, 100f, 100f, 100f));
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, -1f, -1f, -1f, 1f));
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));


        environment.add(new DirectionalLight().set(Color.WHITE, -100f, -100f, -100f));
        environment.add(new DirectionalLight().set(Color.WHITE, -100f, -100f, 100f));
        environment.add(new DirectionalLight().set(Color.WHITE, -100f, 100f, -100f));
        environment.add(new DirectionalLight().set(Color.WHITE, -100f, 100f, 100f));
        environment.add(new DirectionalLight().set(Color.WHITE, 100f, -100f, -100f));
        environment.add(new DirectionalLight().set(Color.WHITE, 100f, -100f, 100f));
        environment.add(new DirectionalLight().set(Color.WHITE, 100f, 100f, -100f));
        environment.add(new DirectionalLight().set(Color.WHITE, 100f, 100f, 100f));


        environment.add(new DirectionalLight().set(Color.WHITE, -10f, -10f, -10f));
        environment.add(new DirectionalLight().set(Color.WHITE, -10f, -10f, 10f));
        environment.add(new DirectionalLight().set(Color.WHITE, -10f, 10f, -10f));
        environment.add(new DirectionalLight().set(Color.WHITE, -10f, 10f, 10f));
        environment.add(new DirectionalLight().set(Color.WHITE, 10f, -10f, -10f));
        environment.add(new DirectionalLight().set(Color.WHITE, 10f, -10f, 10f));
        environment.add(new DirectionalLight().set(Color.WHITE, 10f, 10f, -10f));
        environment.add(new DirectionalLight().set(Color.WHITE, 10f, 10f, 10f));


        environment.add(new DirectionalLight().set(Color.RED, -1f, -1f, 0f));

        environment.add(new DirectionalLight().set(Color.BLUE, 1f, 1f, 0f));
        environment.add(new DirectionalLight().set(Color.RED, -1f, 1f, 0f));


        ObjLoader loader = new ObjLoader();

        scene = new ModelInstance(loader.loadModel(Gdx.files.internal("scene/scene.obj")));


        UBJsonReader reader = new UBJsonReader();
        G3dModelLoader modelLoader = new G3dModelLoader(reader);

//        JsonReader redr = new JsonReader();
//        G3dModelLoader jLoader = new G3dModelLoader(redr);
//        saber = new ModelInstance(modelLoader.loadModel(Gdx.files.internal("newsaber/LightSaber.g3db")));
//
//        saber.transform.scl(0.2f);
//        scene.transform.scl(0.002f);


        Model trooperModel = modelLoader.loadModel(Gdx.files.internal("storm/trooper.g3db"));
//        Model trooperModel = loader.loadModel(Gdx.files.internal("storm/sittedtrooper.obj"));


        ///////Saber

        Model saberModel = modelLoader.loadModel(Gdx.files.internal("ssaber/saber.g3db"));
        saber = new Saber();
        saber.instance = new ModelInstance(saberModel);
        saber.instance.transform.translate(2, 1, -2);

        Mesh saberMesh = saberModel.meshes.get(0);
        saber.collisionShape = new btConvexHullShape(saberMesh.getVerticesBuffer(), saberMesh.getNumVertices(), saberMesh.getVertexSize());
        saber.collisionObject = new btCollisionObject();

        saber.collisionObject.setCollisionShape(saber.collisionShape);

        saber.collisionObject.setWorldTransform(saber.instance.transform);
        ///////////////



        troopers[0] = new ModelInstance(trooperModel);
        troopers[0].transform.translate(0, 0, -5);
//        troopers[0].transform.setToRotation(1, 0, 1, -90);

//        troopers[0].transform.scl(0.01f);
//        troopers[0].transform.scl(0.1f);



        troopers[1] = new ModelInstance(trooperModel);
        troopers[1].transform.translate(0, 0, 5);
//        troopers[0].transform.rotate(0, 0, 1, 90);
//        troopers[1].transform.scl(0.01f);

        troopers[2] = new ModelInstance(trooperModel);
        troopers[2].transform.translate(-5, 0, 0);
//        troopers[0].transform.rotate(0, 0, 1, -90);
//        troopers[2].transform.scl(2f);


        troopers[3] = new ModelInstance(trooperModel);
        troopers[3].transform.translate(5, 0, 0);
//        troopers[0].transform.rotate(1, 0, 0, -90);
//        troopers[3].transform.scl(10.f);

        stage = new Stage();
        font = new BitmapFont();

//        ModelBuilder modelBuilder = new ModelBuilder();
//        Model bulletModel = modelBuilder.createCylinder(0.5f, 1f, 0.5f, 3, new Material(ColorAttribute.createDiffuse(Color.BLUE)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        bulletModel = loader.loadModel(Gdx.files.internal("lazer/sphere.obj"));
        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void render() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public void onNewFrame(HeadTransform paramHeadTransform) {

//        for (int i = 0; i < 4; i++){
//            troopers[i].transform.rotate(0, 1, 0, Gdx.graphics.getDeltaTime() * 30);
//
//            troopers[i].transform.rotate(0, 1, 0, Gdx.graphics.getDeltaTime() * 30);
//        }

        Quaternion q = new Quaternion(((App)getApplication()).getQuaternion());

        saber.instance.transform.set(q);
        saber.collisionObject.setWorldTransform(saber.instance.transform);

        long curTime = System.currentTimeMillis();

        for (int i = 0; i < lazers.size(); i++){
            lazers.get(i).instance.transform.translate(lazers.get(i).dx, lazers.get(i).dz, lazers.get(i).dy);

            lazers.get(i).collisionObject.setWorldTransform(lazers.get(i).instance.transform);

            Vector3 location = new Vector3();
            lazers.get(i).instance.transform.getTranslation(location);

            if (checkCollision(saber.collisionObject, lazers.get(i).collisionObject)) {
                lazers.get(i).dy = -2.0f;
                lazers.get(i).dx -= lazers.get(i).dx;
                lazers.get(i).dz -= lazers.get(i).dz;
            }

            if (location.z > 0){
                lazers.remove(i);
                --hp;
            } else if (location.z < -10){
                lazers.remove(i);
            }

        }

        if( curTime - lastTime > 3000 ){
            lastTime = curTime;
            ModelInstance bulletInstance = new ModelInstance(bulletModel);
            bulletInstance.transform.translate(-0.25f, 2.5f, -4);
            bulletInstance.transform.scl(0.05f);

            Lazer lazer = new Lazer();
            lazer.instance = bulletInstance;
            lazer.dx = (float) (random.nextInt() % 100) / 1000 + 0.15f;
            lazer.dz = (float) (random.nextInt() % 100) / 1000;
            lazer.dy = 2.0f;

            Mesh bulletMesh = bulletModel.meshes.get(0);
            btCollisionShape bulletShape = new btConvexHullShape(bulletMesh.getVerticesBuffer(), bulletMesh.getNumVertices(), bulletMesh.getVertexSize());
            btCollisionObject bulletCollisionObject = new btCollisionObject();

            bulletCollisionObject.setCollisionShape(bulletShape);
            bulletCollisionObject.setWorldTransform(lazer.instance.transform);

            lazer.collisionObject = bulletCollisionObject;
            lazer.collisionShape = bulletShape;

            lazers.add(lazer);

        }
    }

    @Override
    public void onDrawEye(Eye eye) {
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        cam.setEyeViewAdjustMatrix(new Matrix4(eye.getEyeView()));

        float[] perspective = eye.getPerspective(Z_NEAR, Z_FAR);
        cam.setEyeProjection(new Matrix4(perspective));
        cam.update();

        batch.begin(cam);

        for (int i = 0; i < 4; i++){
            batch.render(troopers[i], environment);
        }

        batch.render(saber.instance, environment);
        batch.render(scene, environment);
        for (int i = 0; i < lazers.size(); i++){
            batch.render(lazers.get(i).instance, environment);
        }

        batch.end();

        String caption;
        if (hp > 0){
            caption = " HP " + hp;
        }else {
            caption = "You are lose";
        }

        stage.clear();
        label = new Label(caption, new Label.LabelStyle(font, Color.CYAN));

        label.setPosition(Gdx.graphics.getWidth() / 2 - 3, Gdx.graphics.getHeight() / 2 - 9);
        stage.addActor(label);
        stage.draw();
    }

    @Override
    public void onFinishFrame(Viewport paramViewport) {

    }

    @Override
    public void onRendererShutdown() {

    }

    @Override
    public void onCardboardTrigger() {

    }
}