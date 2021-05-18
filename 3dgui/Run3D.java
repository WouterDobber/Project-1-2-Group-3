import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;


//3D Gui in JavaFX
//TODO: Change sizes of planets+probe to be more accurate
//TODO: Add rocket PhongMaterial/possibly change to Cylinder instead of Sphere

public class Run3D extends Application {

    private double anchorX, anchorY;
    public StateInterface[] arr;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);
    PerspectiveCamera cam;

    public SolarSystem solarSystem = new SolarSystem();
    static State state;
    public Sphere sun, earth, venus, mars, jupiter, saturn, titan, neptune, moon, uranus, mercury, probe;
    public int j = 0;

    private int numStepsPerUpdate = 10;
    SolarSystemSolver solver = new SolarSystemSolver();
    NewtonsFunction function = new NewtonsFunction();


    @Override
    public void start(Stage primaryStage) throws Exception {
        state = new State(solarSystem.bodies);
        CelestialBody Probe = new CelestialBody("rocket", null, 115000, -1.471886208478151E11, -2.861522074209465E10, 8170057.668900404, 27962.61762782645, -62349.24395947284, -666.7403073700751);
        solarSystem.bodies.add(Probe);

        //Assigns materials to the spheres(planets).
        PhongMaterial matSun = new PhongMaterial();
        PhongMaterial matEarth = new PhongMaterial();
        PhongMaterial matVenus = new PhongMaterial();
        PhongMaterial matMars = new PhongMaterial();
        PhongMaterial matJupiter = new PhongMaterial();
        PhongMaterial matSaturn = new PhongMaterial();
        PhongMaterial matTitan = new PhongMaterial();
        PhongMaterial matNeptune = new PhongMaterial();
        PhongMaterial matMoon = new PhongMaterial();
        PhongMaterial matUranus = new PhongMaterial();
        PhongMaterial matMercury = new PhongMaterial();
        PhongMaterial matProbe = new PhongMaterial();

        //Set materials
        matSun.setDiffuseMap(new javafx.scene.image.Image("/textures/sun.jpeg"));
        matEarth.setDiffuseMap(new javafx.scene.image.Image("/textures/earth.jpeg"));
        matVenus.setDiffuseMap(new javafx.scene.image.Image("/textures/venus.jpeg"));
        matMars.setDiffuseMap(new javafx.scene.image.Image("/textures/mars.jpeg"));
        matJupiter.setDiffuseMap(new javafx.scene.image.Image("/textures/jupiter.jpeg"));
        matSaturn.setDiffuseMap(new javafx.scene.image.Image("/textures/saturn.jpeg"));
        matTitan.setDiffuseMap(new javafx.scene.image.Image("/textures/titan.jpeg"));
        matNeptune.setDiffuseMap(new javafx.scene.image.Image("/textures/neptune.jpeg"));
        matMoon.setDiffuseMap(new javafx.scene.image.Image("/textures/moon.jpeg"));
        matUranus.setDiffuseMap(new javafx.scene.image.Image("/textures/uranus.jpeg"));
        matMercury.setDiffuseMap(new javafx.scene.image.Image("/textures/mercury.jpeg"));

        //Initialising spheres, with radius and material
        sun = new Sphere(30);
        sun.setMaterial(matSun);
        earth = new Sphere(5);
        earth.setMaterial(matEarth);
        venus = new Sphere(5);
        venus.setMaterial(matVenus);
        mars = new Sphere(25);
        mars.setMaterial(matMars);
        jupiter = new Sphere(25);
        jupiter.setMaterial(matJupiter);
        saturn = new Sphere(25);
        saturn.setMaterial(matSaturn);
        titan = new Sphere(100);
        titan.setMaterial(matTitan);
        neptune = new Sphere(20);
        neptune.setMaterial(matNeptune);
        moon = new Sphere(1);
        moon.setMaterial(matMoon);
        uranus = new Sphere(75);
        uranus.setMaterial(matUranus);
        mercury = new Sphere(10);
        mercury.setMaterial(matMercury);
        probe = new Sphere(15);

        //Paint initial state
        paint();

        cam = new PerspectiveCamera();

        //Group planets and probe together
        Group gr = new Group();
        gr.getChildren().add(sun);
        gr.getChildren().add(earth);
        gr.getChildren().add(venus);
        gr.getChildren().add(mars);
        gr.getChildren().add(jupiter);
        gr.getChildren().add(saturn);
        gr.getChildren().add(titan);
        gr.getChildren().add(neptune);
        gr.getChildren().add(moon);
        gr.getChildren().add(uranus);
        gr.getChildren().add(mercury);
        gr.getChildren().add(probe);

        //Initialise scene with the group, and size of frame. 600x600
        Scene scene = new Scene(gr, 600, 600, true);
        scene.setFill(Color.BLACK);
        scene.setCamera(cam);

        //Initialise the mouse control. Allows for scroll zoom-in and click-drag to look through 3D space.
        initMouseControl(gr, scene, primaryStage);

        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.setRenderScaleX(100000);
        primaryStage.show();

        //Initialising all the states.
        arr = solver.solve(function, state, 31556952, 100);

        Timeline[] timelines = new Timeline[1000];
        SequentialTransition sq = new SequentialTransition();

        //onMouseClick, animation starts
        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                int x=1000; //The states which transition to
                for(int i=0; i*x<arr.length-1;i++){
                    timelines[i] = newtl(x*i);  //Timeline array of all timelines that are transitioned to.
                    sq.getChildren().add(timelines[i]);

                }

                sq.play();
            }
        });
    }

    /**
     * Creating timeline at a point in time
     * @param x the point in time at which the Timeline is created, in seconds from initial state. e.g. 0=initial state, 1 is second state. Time is based on step size in array solve.
     * @return the timeline at point x in time
     */

    private Timeline newtl(int x){
        Timeline timeline;
        timeline = new Timeline(new KeyFrame(Duration.millis(100),
                new KeyValue(sun.translateXProperty(),1+300),
                new KeyValue(sun.translateYProperty(),1+300),
                new KeyValue(sun.translateZProperty(),1),
                new KeyValue(earth.translateXProperty(),arr[x].getbodies().get(1).getLocation().getX()/arr[x].getbodies().get(0).getLocation().getX() +300),
                new KeyValue(earth.translateYProperty(), arr[x].getbodies().get(1).getLocation().getY()/arr[x].getbodies().get(0).getLocation().getY()+300),
                new KeyValue(earth.translateZProperty(), arr[x].getbodies().get(1).getLocation().getZ()/arr[x].getbodies().get(0).getLocation().getZ()),
                new KeyValue(venus.translateXProperty(),arr[x].getbodies().get(2).getLocation().getX()/arr[x].getbodies().get(0).getLocation().getX()+300),
                new KeyValue(venus.translateYProperty(), arr[x].getbodies().get(2).getLocation().getY()/arr[x].getbodies().get(0).getLocation().getY()+300),
                new KeyValue(venus.translateZProperty(), arr[x].getbodies().get(2).getLocation().getZ()/arr[x].getbodies().get(0).getLocation().getZ()),
                new KeyValue(mars.translateXProperty(),arr[x].getbodies().get(3).getLocation().getX()/arr[x].getbodies().get(0).getLocation().getX()+300),
                new KeyValue(mars.translateYProperty(), arr[x].getbodies().get(3).getLocation().getY()/arr[x].getbodies().get(0).getLocation().getY()+300),
                new KeyValue(mars.translateZProperty(), arr[x].getbodies().get(3).getLocation().getZ()/arr[x].getbodies().get(0).getLocation().getZ()),
                new KeyValue(jupiter.translateXProperty(),arr[x].getbodies().get(4).getLocation().getX()/arr[x].getbodies().get(0).getLocation().getX()+300),
                new KeyValue(jupiter.translateYProperty(), arr[x].getbodies().get(4).getLocation().getY()/arr[x].getbodies().get(0).getLocation().getY()+300),
                new KeyValue(jupiter.translateZProperty(), arr[x].getbodies().get(4).getLocation().getZ()/arr[x].getbodies().get(0).getLocation().getZ()),
                new KeyValue(saturn.translateXProperty(),arr[x].getbodies().get(5).getLocation().getX()/arr[x].getbodies().get(0).getLocation().getX()+300),
                new KeyValue(saturn.translateYProperty(), arr[x].getbodies().get(5).getLocation().getY()/arr[x].getbodies().get(0).getLocation().getY()+300),
                new KeyValue(saturn.translateZProperty(), arr[x].getbodies().get(5).getLocation().getZ()/arr[x].getbodies().get(0).getLocation().getZ()),
                new KeyValue(titan.translateXProperty(),arr[x].getbodies().get(6).getLocation().getX()/arr[x].getbodies().get(0).getLocation().getX()+300),
                new KeyValue(titan.translateYProperty(), arr[x].getbodies().get(6).getLocation().getY()/arr[x].getbodies().get(0).getLocation().getY()+300),
                new KeyValue(titan.translateZProperty(), arr[x].getbodies().get(6).getLocation().getZ()/arr[x].getbodies().get(0).getLocation().getZ()),
                new KeyValue(neptune.translateXProperty(),arr[x].getbodies().get(7).getLocation().getX()/arr[x].getbodies().get(0).getLocation().getX()+300),
                new KeyValue(neptune.translateYProperty(), arr[x].getbodies().get(7).getLocation().getY()/arr[x].getbodies().get(0).getLocation().getY() +300),
                new KeyValue(neptune.translateZProperty(), arr[x].getbodies().get(7).getLocation().getZ()/arr[x].getbodies().get(0).getLocation().getZ()),
                new KeyValue(moon.translateXProperty(),arr[x].getbodies().get(8).getLocation().getX()/arr[x].getbodies().get(0).getLocation().getX()+300),
                new KeyValue(moon.translateYProperty(), arr[x].getbodies().get(8).getLocation().getY()/arr[x].getbodies().get(0).getLocation().getY()+300),
                new KeyValue(moon.translateZProperty(), arr[x].getbodies().get(8).getLocation().getZ()/arr[x].getbodies().get(0).getLocation().getZ()),
                new KeyValue(uranus.translateXProperty(),arr[x].getbodies().get(9).getLocation().getX()/arr[x].getbodies().get(0).getLocation().getX()+300),
                new KeyValue(uranus.translateYProperty(), arr[x].getbodies().get(9).getLocation().getY()/arr[x].getbodies().get(0).getLocation().getY()+300),
                new KeyValue(uranus.translateZProperty(), arr[x].getbodies().get(9).getLocation().getZ()/arr[x].getbodies().get(0).getLocation().getZ()),
                new KeyValue(mercury.translateXProperty(),arr[x].getbodies().get(10).getLocation().getX()/arr[x].getbodies().get(0).getLocation().getX()+300),
                new KeyValue(mercury.translateYProperty(), arr[x].getbodies().get(10).getLocation().getY()/arr[x].getbodies().get(0).getLocation().getY()+300),
                new KeyValue(mercury.translateZProperty(), arr[x].getbodies().get(10).getLocation().getZ()/arr[x].getbodies().get(0).getLocation().getZ()),
                new KeyValue(probe.translateXProperty(),arr[x].getbodies().get(11).getLocation().getX()/arr[x].getbodies().get(0).getLocation().getX()+300),
                new KeyValue(probe.translateYProperty(), arr[x].getbodies().get(11).getLocation().getY()/arr[x].getbodies().get(0).getLocation().getY()+300),
                new KeyValue(probe.translateZProperty(), arr[x].getbodies().get(11).getLocation().getZ()/arr[x].getbodies().get(0).getLocation().getZ())


        ));
        return timeline;
    }

    /**
     * Creating the mouse control: zooming and dragging to look at 3D space
     * @param group the group of nodes (planets) that are in the scene
     * @param scene the scene on which to apply it
     * @param stage the stage on which to apply it
     */
    private void initMouseControl(Group group, Scene scene, Stage stage) {
        Rotate xRotate;
        Rotate yRotate;
        group.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(mouseEvent -> {
                    anchorX = mouseEvent.getSceneX();
                    anchorY = mouseEvent.getSceneY();
                    anchorAngleX = angleX.get();
                    anchorAngleY = angleY.get();
                }
        );
        scene.setOnMouseDragged(mouseEvent -> {
            angleX.set((anchorAngleX - (anchorY - mouseEvent.getSceneY())));
            angleY.set((anchorAngleY + anchorX - mouseEvent.getSceneX()));
        });

        stage.addEventHandler(ScrollEvent.SCROLL, mouseEvent -> {
            double movement = mouseEvent.getDeltaY(); //Only Y as only y rolls
            group.translateZProperty().set(group.getTranslateZ() + movement);
        });
    }


    private void paint() {

        state = (State) solver.step(function, j * numStepsPerUpdate, state, numStepsPerUpdate);
        double sunX = state.celestialBodies.get(0).getLocation().getX();
        double sunY = state.celestialBodies.get(0).getLocation().getY();
        double sunZ = state.celestialBodies.get(0).getLocation().getZ();

        sun.translateXProperty().set(1+ 300);
        sun.translateYProperty().set(1+ 300);
        sun.translateZProperty().set(1);
        earth.translateXProperty().set((state.celestialBodies.get(1).getLocation().getX() / state.celestialBodies.get(0).getLocation().getX()+ 300));
        earth.translateYProperty().set((state.celestialBodies.get(1).getLocation().getY() / state.celestialBodies.get(0).getLocation().getY()+ 300));
        earth.translateZProperty().set((state.celestialBodies.get(1).getLocation().getZ() / state.celestialBodies.get(0).getLocation().getZ()));
        venus.translateXProperty().set((state.celestialBodies.get(2).getLocation().getX() / sunX+ 300));
        venus.translateYProperty().set((state.celestialBodies.get(2).getLocation().getY() / sunY+ 300));
        venus.translateZProperty().set((state.celestialBodies.get(2).getLocation().getZ() / sunZ));
        mars.translateXProperty().set((state.celestialBodies.get(3).getLocation().getX() / sunX+ 300));
        mars.translateYProperty().set((state.celestialBodies.get(3).getLocation().getY() / sunY+ 300));
        mars.translateZProperty().set((state.celestialBodies.get(3).getLocation().getZ() / sunZ));
        jupiter.translateXProperty().set((state.celestialBodies.get(4).getLocation().getX() / sunX+ 300));
        jupiter.translateYProperty().set((state.celestialBodies.get(4).getLocation().getY() / sunY+ 300));
        jupiter.translateZProperty().set((state.celestialBodies.get(4).getLocation().getZ() / sunZ));
        saturn.translateXProperty().set((state.celestialBodies.get(5).getLocation().getX() / sunX+ 300));
        saturn.translateYProperty().set((state.celestialBodies.get(5).getLocation().getY() / sunY+ 300));
        saturn.translateZProperty().set((state.celestialBodies.get(5).getLocation().getZ() / sunZ));
        titan.translateXProperty().set((state.celestialBodies.get(6).getLocation().getX() / sunX+ 150));
        titan.translateYProperty().set((state.celestialBodies.get(6).getLocation().getY() / sunY+ 150));
        titan.translateZProperty().set((state.celestialBodies.get(6).getLocation().getZ() / sunZ));
        neptune.translateXProperty().set((state.celestialBodies.get(7).getLocation().getX() / sunX+ 300));
        neptune.translateYProperty().set((state.celestialBodies.get(7).getLocation().getY() / sunY+ 300));
        neptune.translateZProperty().set((state.celestialBodies.get(7).getLocation().getZ() / sunZ));
        moon.translateXProperty().set((state.celestialBodies.get(8).getLocation().getX() / sunX+ 305));
        moon.translateYProperty().set((state.celestialBodies.get(8).getLocation().getY() / sunY+ 305));
        moon.translateZProperty().set((state.celestialBodies.get(8).getLocation().getZ() / sunZ));
        uranus.translateXProperty().set((state.celestialBodies.get(9).getLocation().getX() / sunX+ 300));
        uranus.translateYProperty().set((state.celestialBodies.get(9).getLocation().getY() / sunY)+ 300);
        uranus.translateZProperty().set((state.celestialBodies.get(9).getLocation().getZ() / sunZ));
        mercury.translateXProperty().set((state.celestialBodies.get(10).getLocation().getX() / sunX)+ 300);
        mercury.translateYProperty().set((state.celestialBodies.get(10).getLocation().getY() / sunY)+ 300);
        mercury.translateZProperty().set((state.celestialBodies.get(10).getLocation().getZ() / sunZ));
        probe.translateXProperty().set((state.celestialBodies.get(11).getLocation().getX()/sunX)+300);
        probe.translateYProperty().set((state.celestialBodies.get(11).getLocation().getY()/sunY)+300);
        probe.translateZProperty().set((state.celestialBodies.get(11).getLocation().getZ()/sunZ));

    }


    public static void main (String[]args){
        launch(args);
    }
}