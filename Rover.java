package NewLand;

import java.util.ArrayList;

public class Rover {
	// Energy when the rover leaves HQ
	public final double MAX_ENERGY = 100;
	// Energy limit for the rover to start going back home
	public final double MIN_ENERGY = 50;

	private int[] origin = new int[2];
	private int[] position = new int[2];
	private int[] destination = new int[2];
	private Map map;
	private ArrayList path;
	private double energy = 0;
	private double energy_waste = 0;
	private boolean going_home = false;
	private int trips = 0;
	private int discovered = 1;
	private int discovered_total = 0;


	public Rover(int x, int y, Map terrain) {
		origin[0] = x;
		origin[1] = y;
		position[0] = x;
		position[1] = y;
		destination[0] = x;
		destination[1] = y;
		map = terrain;
		path = map.getShortestPathToUndiscovered(position[0], position[1]);
	}

	public void move() {
		int[] pos;

		if (position[0] == origin[0] && position[1] == origin[1]) {
			refuel();
		}

		if (!going_home && energy < MIN_ENERGY) {
			goHome();
		}

		if (energy > 0) {
			if (path.size() == 0) {
				path = map.getShortestPathToUndiscovered(position[0], position[1]);
			}

			pos = (int[]) path.get(0);
			path.remove(0);

			energy -= map.getRequiredEnergyToMove(position[0], position[1], pos[0], pos[1]);
			position = pos;
			int d = map.discover(position[0], position[1]);
			discovered += d;
			discovered_total += d;
			energy -= d;
		}
	}

	public int getX() {
		return position[0];
	}

	public int getY() {
		return position[1];
	}

	public double getEnergy() {
		return Math.round(energy * 100.0) / 100.0;
	}

	public double getEnergyWaste() {
		return Math.round(energy_waste * 100.0) / 100.0;
	}

	public double getEnergyWasteAverage() {
		return Math.round((energy_waste / trips) * 100.0) / 100.0;
	}

	public int getTrips() {
		return trips;
	}

	public int getDiscovered() {
		return discovered;
	}

	public int getDiscoveredTotal() {
		return discovered_total;
	}

	public double getDiscoveredAverage() {
		return Math.round((discovered_total * 1.0 / trips) * 100.0) / 100.0;
	}

	public boolean isAlive() {
		return (energy > 0);
	}

	private void goHome() {
		destination[0] = origin[0];
		destination[1] = origin[1];
		going_home = true;
		path = map.getShortestPath(position[0], position[1], destination[0], destination[1]);
	}

	public boolean atHome() {
		if (position[0] == origin[0] && position[1] == origin[1]) {
			return true;
		} else {
			return false;
		}
	}

	private void refuel() {
		if (discovered > 0) {
			energy_waste += energy;
			energy = MAX_ENERGY;
			going_home = false;
			trips++;
			discovered = 0;
		} else {
			energy = 0;
		}
	}

	/*public int countUndiscovered() {
		int count = 0;

		if (pos_x > 0) {
			if (pos_y > 0) {
				if (map[pos_x - 1][pos_y - 1] == -1) {
					count++;
				}
			}
			if (map[pos_x - 1][pos_y] == -1) {
				count++;
			}
			if (pos_y < (map.length - 1)) {
				if (map[pos_x - 1][pos_y + 1] == -1) {
					count++;
				}
			}
		}

		if (pos_y > 0) {
			if (map[pos_x][pos_y - 1] == -1) {
				count++;
			}
		}
		if (map[pos_x][pos_y] == -1) {
			count++;
		}
		if (pos_y < (map.length - 1)) {
			if (map[pos_x][pos_y + 1] == -1) {
				count++;
			}
		}

		if (pos_x < (map[0].length - 1)) {
			if (pos_y > 0) {
				if (map[pos_x + 1][pos_y - 1] == -1) {
					count++;
				}
			}
			if (map[pos_x + 1][pos_y] == -1) {
				count++;
			}
			if (pos_y < (map.length - 1)) {
				if (map[pos_x + 1][pos_y + 1] == -1) {
					count++;
				}
			}
		}

		return count;
	}*/
}