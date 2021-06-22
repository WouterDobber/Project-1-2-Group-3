The structure of the code includes the implementation of the interfaces given in Phase 1 and a Genetic Algorithm (GA.java),  as well as all solvers implemented in phase 2 (EulerSolver.java , RungeKuttaSolver.java and VerletSolver.java)
and the fuel system (FuelSolver.java, SolarSystemSolver.java). 
For this final phase, an Open Loop controller  (related files: Lander.java, OpenLoop.java, PhysicsEngine.java and 
RunLanderGuiOpenLoop.java) and a Feedback Controller were implemented for coordinating the landing of the lander
(related files: Thruster.java, LandSystem.java, PID.java, RunLanderGuiClosedLoop.java ), a wind model for simulating real conditions (Wind.java) and a 
 Newton Raphson (NewtonRaphson.java) to improve the results from phase 2. 

If you want to run the code from command line follow the next steps, otherwise, if running from an IDE, the path name of the images needs to be change in the class that you will be running, for instance, if you want to run RunLaderGuiOpenLoop.java go to the respective lines were the images are being open and change them to be "src/main/java/solarimages/lander.png" instead of "solarimages/lander.png". 

To run the code that shows the GUI, there are different options or classes that could be executed based on what is wanted:
 Traverse to the correct folder in your command window throw: src/main/java
1. Solar system simulation:
    Compile RunSolarSystemSimulation.java ("javac RunSolarSystemSimulation.java"). This should compile all of the necessary files as well.
    From there on, running the file ("java RunSolarSystemSimulation") should open the 2D window and show the rocket's trajectory to Titan and back.

2. Landing open loop controller:
    Compile RunLanderGuiOpenLoop.java ("javac RunLanderGuiOpenLoop.java"). This should compile all of the necessary files as well.
    From there on, running the file ("java RunLanderGuiOpenLoop") should open the 2D animation and show the landers trajectory.

3. Landing feedback controller:
   Compile RunLanderGuiFeedbackController.java ("javac RunLanderGuiFeedbackController.java"). This should compile all of the necessary files as well.
   From there on, running the file ("java RunLanderGuiFeedbackController") should open the 2D animation and show the landers trajectory.


As part of the requirements for phase 2, test classes were created and can be found following the Gradle structure. 
Finally, 2 classes that test the accuracy of our ODEsolver with the correct results of coordinates from NASA can be found under the names Test_SaturnCoordinates.java and Test_EarthCoordinates.java,
to change which solver is being tested, comment in the one you prefer in class State.java lines 58-66.

Enjoy :)
Group 3.
