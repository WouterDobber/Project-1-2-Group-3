import java.util.Random;

public class GA {

	public static final int POPSIZE = 21;
	public static final int ELITESIZE = 7;

	public static void main(String[] args) {

		Probe[] population = new Probe[POPSIZE];

		// we initialize the population with random characters
		for (int i = 0; i < POPSIZE; i++) {
			population[i] = new Probe();
			double randomX = (Math.random() * 2) - 1; // generates a random number between -1 and 1.
			double randomY = (Math.random() * 2) - 1;
			double randomZ = (Math.random() * 2) - 1;
			Vector v = new Vector(randomX, randomY, randomZ);
			v.normalize();
			population[i].getTrajectory().setNormalized(v);
		}

		// Vector [][] launchingCoordinates =
		// ProbeCoordinates2.getCoordinates(solarsystem.bodies.get(1)); //1= Earth

		Random rand = new Random();

		int generation = 0;
		GeneticAlgorithm(generation, population, rand);
	}

	/**
	 * In this method we recursively go through all 3 Genetic Algorithm methods to
	 * receive a newly evolved generation. We use recursion to repeat the method
	 * untill we have found our TARGET string in one of our individuals, which is
	 * our base case
	 *
	 * @return we return in our base case by looping through the new generation and
	 *         checking if any of the individuals match the TARGET string.
	 * @param counter       we loop through every individual in the generation and
	 *                      count each chromosome that matches the TARGET in the
	 *                      right spot.
	 * @param fitnessLevel  we take the amount of matching chromosomes (counter) and
	 *                      divide it over the length to get the percentage of the
	 *                      chromosome that matches. we then pass this double as
	 *                      each individual's fitness value using
	 *                      setFitness(fitnessLevel)
	 * @param selectionPop  receive our elitist selection from our selection method
	 *                      by passing our population
	 * @param newPopulation return our new population of the next generation by
	 *                      passing our elitist selection into our crossover method
	 *                      with the randomizer
	 */

	public static void GeneticAlgorithm(int generation, Probe[] population, Random rand) {
		// Base case: loop through the recursively passed generation and check every
		// individual to see if we have found one that matches TARGET
		double counter = 0;
		double fitnessLevel = 0;

		SolarSystem solarsystem = new SolarSystem();

		for (int i = 0; i < population.length; i++) {

			// assign location and velocity
			assign(population);

			// Calculate final distance as fitness
			double minimum = launch(population[i], solarsystem);
			population[i].getTrajectory().setFitness(minimum);
			if (population[i].getTrajectory().getFitness() == 0) {
				System.out.println("Landed on Titan!");
				System.out.println("Launch Position " + population[i].getTrajectory().getLaunchPos().toString());
				System.out.println("Launch Velocity " + population[i].getTrajectory().getFinalVelocity().toString());
				return;
			}
		}

		for (int i = 0; i < population.length; i++) {
			// System.out.println(population[i].getTrajectory().getFitness());
		}

		System.out.println();
		// System.out.println();

		System.out.println("Selection Population:");

		// Get 5 closest probes
		Probe[] selectionPop = selection(population);

		for (int i = 0; i < selectionPop.length; i++) {
			System.out.println(selectionPop[i].getTrajectory().getFitness());
		}
		System.out
				.println("Best Trajectory: Velocity = " + selectionPop[0].getTrajectory().getFinalVelocity().toString()
						+ ", Launch Position = " + selectionPop[0].getTrajectory().getLaunchPos().toString());

		// System.out.println();
		// System.out.println();

		// System.out.println("Cross Over Population:");

		// CROSS OVER
		Probe[] newPopulation = crossover(selectionPop);

		for (int i = 0; i < newPopulation.length; i++) {
			// System.out.println(newPopulation[i].getTrajectory().getNormalized().toString());
		}

		// System.out.println();
		// System.out.println();

		// MUTATION
		mutation(newPopulation, rand);

		// Print and increase the current generation
		System.out.println("Generation: " + generation++);

		// REPEAT GA
		GeneticAlgorithm(generation, newPopulation, rand);

	}

	public static double launch(Probe rocket, SolarSystem solarsystem) {
		Vector[][] launchingCoordinates = rocket.getTrajectory().getVelocity();
		double min;
		double trueMin = rocket.closestDistanceProbeTitan(launchingCoordinates[0][0], launchingCoordinates[0][1],
				31556952, 100);
		rocket.getTrajectory().setLaunchPos(launchingCoordinates[0][0]);
		rocket.getTrajectory().setFinalVelocity(launchingCoordinates[0][1]);
		for (int i = 1; i < launchingCoordinates.length; i++) {
			// System.out.println(launchingCoordinates[i][0]);
			// System.out.println( launchingCoordinates[i][1]);
			min = rocket.closestDistanceProbeTitan(launchingCoordinates[i][0], launchingCoordinates[i][1], 31556952,
					100);
			if (min < trueMin) {
				trueMin = min;
				rocket.getTrajectory().setLaunchPos(launchingCoordinates[i][0]);
				rocket.getTrajectory().setFinalVelocity(launchingCoordinates[i][1]);
			}
			// System.out.println(" MIN " + min);
		}
		// System.out.println("TRUE MIN " + trueMin);
		double finalMin = trueMin - 2575.5e3;
		System.out.println("FINAL MIN " + finalMin);

		return finalMin;
	}

	/**
	 * In the mutation method we loop through our population and randomly assign a
	 * new character if the random chance is met
	 * 
	 * @param chance randomize a number between 1 and 11. If the chance is equal to
	 *               1, which has an arbitrary 1/11 chance of happening, we apply
	 *               mutation
	 */

	public static void mutation(Probe[] population, Random rand) {
		for (int i = 0; i < population.length; i++) {
			int chance = rand.nextInt(5);
			if (chance == 1) {
				// This part basically repeats the randomisation that was pre-written in the
				// file

				double randomX = (Math.random() * 2) - 1; // generates a random number between -1 and 1.
				double randomY = (Math.random() * 2) - 1;
				double randomZ = (Math.random() * 2) - 1;
				Vector v = new Vector(randomX, randomY, randomZ);

				Vector together = (Vector) population[i].getTrajectory().getNormalized().add(v);
				population[i].getTrajectory().setNormalized(together.normalize());

			}
		}
	}

	/**
	 * In this method we take the 8 fittest individuals and have them reproduce with
	 * all of the individuals that come after, untill 17, leaving exactly 100 new
	 * individuals
	 *
	 * @param chromosome1 is the first parent's chromosome, which is always one of
	 *                    the 8 fittest individuals
	 * @param chromosome2 is the second parent's chromosome. We never repeat 2
	 *                    parents or clone with the same parent
	 * @param finalChrom  is the chromosome our child will have, which gets filled
	 *                    by the 2 parent's chromosomes
	 * @param popCounter  assigns the spot for each children, increasing by 1
	 *                    whenever we have a new child generated to fill all 100
	 *                    spots
	 * @return newPopulation is the new population of the same size, containing all
	 *         100 generated children
	 */

	public static Probe[] crossover(Probe[] population) {
		Probe[] newPopulation = new Probe[POPSIZE];
		int tempcounter = 0;
		int popCounter = 0;

		// Loop through all 5 parents for the 1st vector
		for (int i = 0; i < population.length; i++) {
			Vector v1 = population[i].getTrajectory().getNormalized();
			for (int j = i + 1; j < population.length; j++) {
				Vector v2 = population[j].getTrajectory().getNormalized();

				Vector newVector = (Vector) v1.add(v2); // new Vector(v1.getX() + v2.getX(), v1.getY() + v2.getY(),
														// v1.getZ() + v2.getZ());
				Vector newVector2 = (Vector) newVector.normalize();

				try {
					newPopulation[popCounter] = new Probe();
					newPopulation[popCounter].getTrajectory().setNormalized(newVector2);
				} catch (ArrayIndexOutOfBoundsException e) {
				}
				popCounter++;
			}
		}
		return newPopulation;
	}

	/**
	 * In this method we take the elitist selection of our population
	 *
	 * @param selectionPop is the elitist selection of our population, containing
	 *                     the top 17 people. We explained why in our report
	 * @return we return the elitist selection of our population to be used for
	 *         reproduction
	 */

	public static Probe[] selection(Probe[] population) {
		Probe[] selectionPop = new Probe[ELITESIZE];
		int counter = 0;
		int counter2 = 0;
		int counter3 = 0;

		while (counter3 < ELITESIZE) {
			// System.out.println("Looping...");
			for (int i = 0; i < population.length; i++) {
				counter2 = 0;
				// System.out.println(population[i].getTrajectory().getFitness());
				for (int j = 0; j < population.length; j++) {
					if (population[i].getTrajectory().getFitness() > population[j].getTrajectory().getFitness()) {
						counter2++;
					}
				}
				if (counter2 == counter) {
					counter++;
					System.out.println("FOUND " + counter + " which is number " + counter3);
					selectionPop[counter3] = population[i];
					counter3++;
					break;
				}
			}
		}
		System.out.println("Loop done");
		return selectionPop;
	}

	public static void assign(Probe[] population) {
		for (int i = 0; i < population.length; i++) {
			Vector position = (Vector) population[i].getTrajectory().getNormalized().mul(6371e3);
			position = (Vector) position
					.add(new Vector(-1.471922101663588e+11, -2.860995816266412e+10, 8.278183193596080e+06));
			population[i].getTrajectory().setLaunchPos(position);

			Vector velocity;
			Vector[][] positionsVelocities = new Vector[5][];

			int p = 0;

			for (int j = 20; j < 61; j += 10) {
				velocity = (Vector) population[i].getTrajectory().getNormalized().mul(j * 1000);
				velocity = (Vector) velocity
						.add(new Vector(5.427193405797901e+03, -2.931056622265021e+04, 6.575428158157592e-01)); // adding
																												// velocity
																												// of
																												// planet
				Vector[] positionVel = new Vector[2];
				positionVel[0] = population[i].getTrajectory().getLaunchPos();
				positionVel[1] = velocity;
				positionsVelocities[p] = positionVel;
				p++;
			}
			population[i].getTrajectory().setVelocity(positionsVelocities);
			/*
			 * for (int x = 0; x < positionsVelocities.length; x++){ for (int s = 0; s <
			 * positionsVelocities[x].length; s++){
			 * System.out.print(positionsVelocities[x][s] + " "); } System.out.println(); }
			 * System.out.println();
			 */
		}
	}
}