package net.nikonorov.lazerninja.ui;

/**
 * Created by vitaly on 20.03.16.
 */
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.CardBoardAndroidApplication;
import com.badlogic.gdx.backends.android.CardBoardApplicationListener;
import com.badlogic.gdx.backends.android.CardboardCamera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.UBJsonReader;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;



public class Game extends CardBoardAndroidApplication implements CardBoardApplicationListener{

    private CardboardCamera cam;
    private ModelInstance[] troopers = new ModelInstance[4];
    private ModelInstance saber;
    private ModelInstance scene;
    private ModelBatch batch;
    private Stage stage;
    private Label label;
    private BitmapFont font;
    private Environment environment;
    private static final float Z_NEAR = 0.1f;
    private static final float Z_FAR = 1000.0f;
    private static final float CAMERA_Z = 0;//.1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(this, config);
    }

    @Override
    public void create() {
        cam = new CardboardCamera();
        cam.position.set(0f, 3f, CAMERA_Z);
        cam.lookAt(0,3f,-5f);
        cam.near = Z_NEAR;
        cam.far = Z_FAR;

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


        Model trooperModel = modelLoader.loadModel(Gdx.files.internal("trooper/trooper.g3db"));

        saber = new ModelInstance(modelLoader.loadModel(Gdx.files.internal("saber/saber.g3db")));

        saber.transform.scl(0.2f);



        troopers[0] = new ModelInstance(trooperModel);
        troopers[0].transform.translate(0, 0, -5);
        troopers[0].transform.scl(0.01f);



        troopers[1] = new ModelInstance(trooperModel);
        troopers[1].transform.translate(0, 0, 5);
        troopers[1].transform.scl(0.01f);

        troopers[2] = new ModelInstance(trooperModel);
        troopers[2].transform.translate(-5, 0, 0);
        troopers[2].transform.scl(0.01f);


        troopers[3] = new ModelInstance(trooperModel);
        troopers[3].transform.translate(5, 0, 0);
        troopers[3].transform.scl(0.01f);

        saber.transform.translate(0, 10f, -1.7f);

        stage = new Stage();
        font = new BitmapFont();
        label = new Label(" TEXT ", new Label.LabelStyle(font, Color.RED));
        label.setPosition(Gdx.graphics.getWidth() / 2 - 3, Gdx.graphics.getHeight() / 2 - 9);
        stage.addActor(label);
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

        for (int i = 0; i < 4; i++){
            troopers[i].transform.rotate(0, 1, 0, Gdx.graphics.getDeltaTime() * 30);

            troopers[i].transform.rotate(0, 1, 0, Gdx.graphics.getDeltaTime() * 30);
        }

        Vector3 direction = cam.direction;


        //saber.transform.ro

        Log.i("GAme", cam.toString());



        //saber.transform.trn(cam.combined);

        //saber.transform.translate(cam.direction);

        //saber.transform.translate(0.0f, 0.01f, 0.0f);

        //saber.transform.rotate(((App) getApplication()).getXPosition(), 1, 0, - Gdx.graphics.getDeltaTime() * 30);

        //saber.transform.translate(((App)getApplication()).getXPosition(), 5, -3);
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

        batch.render(saber, environment);
        batch.render(scene, environment);

        batch.end();

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