package NewLand;

import java.util.Random;
import java.util.ArrayList;

public class Map {
	private final int VARIATION = 8;

	private int[][] terrain;
    private int[][] discovered;
    private int t_width;
    private int t_height;

    public Map(int width, int height) {
    	t_width = width;
    	t_height = height;

        terrain = generateTerrain();

    	// Create map overlay
        discovered = new int[width][];
        for (int i = 0; i < t_width; i++) {
            discovered[i] = new int[t_height];
            for (int j = 0; j < t_height; j++) {
                discovered[i][j] = -1;
            }
        }

        // Place HQ
        discovered[(int)(t_width / 2)][(int)(t_height / 2)] = 101;
        discover((int)(t_width / 2), (int)(t_height / 2));
    }

    private int[][] generateTerrain() {
    	int[][] output = new int[t_width][];
        Random rand = new Random();

    	for(int i = 0; i < t_width; i++) {
    		output[i] = new int[t_height];
    		for(int j = 0; j < t_height; j++) {
                int total = 0;
                int count = 0;

                if (i > 0) {
                    total += output[i-1][j];
                    count++;

                    if (j > 0) {
                        total += output[i-1][j-1];
                        count++;
                    }

                    if (j < (t_height - 1)) {
                        total += output[i-1][j+1];
                        count++;
                    }
                }

                if (j > 0) {
                    total += output[i][j-1];
                    count++;
                }

                if (count > 0) {
                    output[i][j] = (int)(total / count) + rand.nextInt((VARIATION * 2) + 1) - VARIATION;
                    if (output[i][j] < 0) {
                        output[i][j] = 0;
                    } else if (output[i][j] > 100) {
                        output[i][j] = 100;
                    }
                } else {
                    output[i][j] = (int)(Math.random() * 100) + 1;
                }
    		}
    	}

    	return output;
    }

    public int getDiscovered(int x, int y) {
    	return discovered[x][y];
    }

    public double getRequiredEnergyToMove(int x1, int y1, int x2, int y2) {
        if (x1 >= 0 && x1 < discovered.length && y1 >= 0 && y1 < discovered[0].length && x2 >= 0 && x2 < discovered.length && y2 >= 0 && y2 < discovered[0].length) {
            if (discovered[x2][y2] >= 0 && Math.abs(x1 - x2) <= 1 && Math.abs(y1 - y2) <= 1) {
                double output = Math.sqrt(Math.abs(x1 - x2) + Math.abs(y1 - y2)) + ((terrain[x2][y2] - terrain[x1][y1]) / VARIATION);
                if (output < 0.2) {
                    return 0.2;
                } else {
                    return output;
                }
            }
        }

    	return 100;
    }

    public int getWidth() {
        return t_width;
    }

    public int getHeight() {
        return t_height;
    }

    public ArrayList getShortestPath(int x1, int y1, int x2, int y2) {
        int[] origin = new int[2];
    	ArrayList path = new ArrayList();
    	ArrayList<ArrayList> connections = new ArrayList();
        ArrayList<Integer> position;
        int[] pos = new int[2];
    	boolean connected = false;

    	double[][] values = new double[t_width][];
    	for (int i = 0; i < t_width; i++) {
    		values[i] = new double[t_height];
    		for (int j = 0; j < t_height; j++) {
    			values[i][j] = 100;
    		}
    	}

        origin[0] = x1;
        origin[1] = y1;

    	// shotest path to the same inicial point is 0
    	values[x1][y1] = 0;

    	while(!connected) {
    		double value;
    		int[] connection = new int[3];

    		if (x1 > 0) {
    			if (y1 > 0) {
    				value = values[x1][y1] + getRequiredEnergyToMove(x1, y1, x1 - 1, y1 - 1);
    				if (value < values[x1-1][y1-1]) {
    					values[x1-1][y1-1] = value;
    					connections = addConnectionToList(connections, value, x1 - 1, y1 - 1);
    				}
    			}
    			value = values[x1][y1] + getRequiredEnergyToMove(x1, y1, x1 - 1, y1);
				if (value < values[x1-1][y1]) {
					values[x1-1][y1] = value;
					connections = addConnectionToList(connections, value, x1 - 1, y1);
				}
				if (y1 < (t_height - 1)) {
    				value = values[x1][y1] + getRequiredEnergyToMove(x1, y1, x1 - 1, y1 + 1);
    				if (value < values[x1-1][y1+1]) {
    					values[x1-1][y1+1] = value;
    					connections = addConnectionToList(connections, value, x1 - 1, y1 + 1);
    				}
    			}
    		}

    		if (y1 > 0) {
				value = values[x1][y1] + getRequiredEnergyToMove(x1, y1, x1, y1 - 1);
				if (value < values[x1][y1-1]) {
					values[x1][y1-1] = value;
					connections = addConnectionToList(connections, value, x1, y1 - 1);
				}
			}
			if (y1 < (t_height - 1)) {
				value = values[x1][y1] + getRequiredEnergyToMove(x1, y1, x1, y1 + 1);
				if (value < values[x1][y1+1]) {
					values[x1][y1+1] = value;
					connections = addConnectionToList(connections, value, x1, y1 + 1);
				}
			}

			if (x1 < (t_width - 1)) {
    			if (y1 > 0) {
    				value = values[x1][y1] + getRequiredEnergyToMove(x1, y1, x1 + 1, y1 - 1);
    				if (value < values[x1+1][y1-1]) {
    					values[x1+1][y1-1] = value;
    					connections = addConnectionToList(connections, value, x1 + 1, y1 - 1);
    				}
    			}
    			value = values[x1][y1] + getRequiredEnergyToMove(x1, y1, x1 + 1, y1);
				if (value < values[x1+1][y1]) {
					values[x1+1][y1] = value;
					connections = addConnectionToList(connections, value, x1 + 1, y1);
				}
				if (y1 < (t_height - 1)) {
    				value = values[x1][y1] + getRequiredEnergyToMove(x1, y1, x1 + 1, y1 + 1);
    				if (value < values[x1+1][y1+1]) {
    					values[x1+1][y1+1] = value;
    					connections = addConnectionToList(connections, value, x1 + 1, y1 + 1);
    				}
    			}
    		}

            position = connections.get(0);
            connections.remove(0);
            x1 = position.get(1);
            y1 = position.get(2);

            if (x1 == x2 && y1 == y2) {
                /*for (int i = 0; i < t_width; i++) {
                    for (int j = 0; j < t_height; j++) {
                        System.out.print(values[i][j] + " ");
                    }
                    System.out.println();
                }
                System.exit(0);*/
                connected = true;
                pos[0] = x1;
                pos[1] = y1;

                do {
                    path.add(0, pos);
                    pos = getPreviousStep(values, pos[0], pos[1]);
                } while(origin[0] != pos[0] || origin[1] != pos[1]);
            }
    	}

        return path;
    }

    public ArrayList getShortestPathToUndiscovered(int x1, int y1) {
        int[] origin = new int[2];
        ArrayList path = new ArrayList();
        ArrayList<ArrayList> connections = new ArrayList();
        ArrayList<Integer> position;
        int[] pos = new int[2];
        boolean connected = false;

        double[][] values = new double[t_width][];
        for (int i = 0; i < t_width; i++) {
            values[i] = new double[t_height];
            for (int j = 0; j < t_height; j++) {
                values[i][j] = 100;
            }
        }

        origin[0] = x1;
        origin[1] = y1;

        // shotest path to the same inicial point is 0
        values[x1][y1] = 0;

        while(!connected) {
            double value;
            int[] connection = new int[3];

            if (x1 > 0) {
                if (y1 > 0) {
                    if (discovered[x1-1][y1-1] > -1) {
                        value = values[x1][y1] + getRequiredEnergyToMove(x1, y1, x1 - 1, y1 - 1);
                        if (value < values[x1-1][y1-1]) {
                            values[x1-1][y1-1] = value;
                            connections = addConnectionToList(connections, value, x1 - 1, y1 - 1);
                        }
                    } else {
                        value = values[x1][y1];
                        connections = addConnectionToList(connections, value, x1 - 1, y1 - 1);
                    }
                }
                if (discovered[x1-1][y1] > -1) {
                    value = values[x1][y1] + getRequiredEnergyToMove(x1, y1, x1 - 1, y1);
                    if (value < values[x1-1][y1]) {
                        values[x1-1][y1] = value;
                        connections = addConnectionToList(connections, value, x1 - 1, y1);
                    }
                } else {
                    value = values[x1][y1];
                    connections = addConnectionToList(connections, value, x1 - 1, y1);
                }
                if (y1 < (t_height - 1)) {
                    if (discovered[x1-1][y1+1] > -1) {
                        value = values[x1][y1] + getRequiredEnergyToMove(x1, y1, x1 - 1, y1 + 1);
                        if (value < values[x1-1][y1+1]) {
                            values[x1-1][y1+1] = value;
                            connections = addConnectionToList(connections, value, x1 - 1, y1 + 1);
                        }
                    } else {
                        value = values[x1][y1];
                        connections = addConnectionToList(connections, value, x1 - 1, y1 + 1);
                    }
                }
            }

            if (y1 > 0) {
                if (discovered[x1][y1-1] > -1) {
                    value = values[x1][y1] + getRequiredEnergyToMove(x1, y1, x1, y1 - 1);
                    if (value < values[x1][y1-1]) {
                        values[x1][y1-1] = value;
                        connections = addConnectionToList(connections, value, x1, y1 - 1);
                    }
                } else {
                    value = values[x1][y1];
                    connections = addConnectionToList(connections, value, x1, y1 - 1);
                }
            }
            if (y1 < (t_height - 1)) {
                if (discovered[x1][y1+1] > -1) {
                    value = values[x1][y1] + getRequiredEnergyToMove(x1, y1, x1, y1 + 1);
                    if (value < values[x1][y1+1]) {
                        values[x1][y1+1] = value;
                        connections = addConnectionToList(connections, value, x1, y1 + 1);
                    }
                } else {
                    value = values[x1][y1];
                    connections = addConnectionToList(connections, value, x1, y1 + 1);
                }
            }

            if (x1 < (t_width - 1)) {
                if (y1 > 0) {
                    if (discovered[x1+1][y1-1] > -1) {
                        value = values[x1][y1] + getRequiredEnergyToMove(x1, y1, x1 + 1, y1 - 1);
                        if (value < values[x1+1][y1-1]) {
                            values[x1+1][y1-1] = value;
                            connections = addConnectionToList(connections, value, x1 + 1, y1 - 1);
                        }
                    } else {
                        value = values[x1][y1];
                        connections = addConnectionToList(connections, value, x1 + 1, y1 - 1);
                    }
                }
                if (discovered[x1+1][y1] > -1) {
                    value = values[x1][y1] + getRequiredEnergyToMove(x1, y1, x1 + 1, y1);
                    if (value < values[x1+1][y1]) {
                        values[x1+1][y1] = value;
                        connections = addConnectionToList(connections, value, x1 + 1, y1);
                    }
                } else {
                    value = values[x1][y1];
                    connections = addConnectionToList(connections, value, x1 + 1, y1);
                }
                if (y1 < (t_height - 1)) {
                    if (discovered[x1+1][y1+1] > -1) {
                        value = values[x1][y1] + getRequiredEnergyToMove(x1, y1, x1 + 1, y1 + 1);
                        if (value < values[x1+1][y1+1]) {
                            values[x1+1][y1+1] = value;
                            connections = addConnectionToList(connections, value, x1 + 1, y1 + 1);
                        }
                    } else {
                        value = values[x1][y1];
                        connections = addConnectionToList(connections, value, x1 + 1, y1 + 1);
                    }
                }
            }

            position = connections.get(0);
            connections.remove(0);
            x1 = position.get(1);
            y1 = position.get(2);

            if (discovered[x1][y1] == -1) {
                /*for (int i = 0; i < t_width; i++) {
                    for (int j = 0; j < t_height; j++) {
                        System.out.print(values[i][j] + " ");
                    }
                    System.out.println();
                }
                System.exit(0);*/
                connected = true;
                pos[0] = x1;
                pos[1] = y1;

                do {
                    path.add(0, pos);
                    pos = getPreviousStep(values, pos[0], pos[1]);
                } while(origin[0] != pos[0] || origin[1] != pos[1]);
            }
        }

        return path;
    }

    private ArrayList addConnectionToList(ArrayList list, double value, int x, int y) {
    	ArrayList connection = new ArrayList();
    	connection.add(value);
    	connection.add(x);
    	connection.add(y);

    	for (int i = 0; i < list.size(); i++) {
            ArrayList conn = (ArrayList)list.get(i);
    		if ((double)conn.get(0) > value) {
    			list.add(i, connection);
    			return list;
    		}
    	}

    	list.add(connection);
    	return list;
    }

    private int[] getPreviousStep(double[][] values, int x, int y) {
        double value = 100;
        int[] previous = new int[2];

        if (x > 0) {
            if (y > 0) {
                if (values[x-1][y-1] < value) {
                    value = values[x-1][y-1];
                    previous[0] = x - 1;
                    previous[1] = y - 1;
                }
            }
            if (values[x-1][y] < value) {
                value = values[x-1][y];
                previous[0] = x - 1;
                previous[1] = y;
            }
            if (y < (t_width - 1)) {
                if (values[x-1][y+1] < value) {
                    value = values[x-1][y+1];
                    previous[0] = x - 1;
                    previous[1] = y + 1;
                }
            }
        }

        if (y > 0) {
            if (values[x][y-1] < value) {
                value = values[x][y-1];
                previous[0] = x;
                previous[1] = y - 1;
            }
        }
        if (y < (t_width - 1)) {
            if (values[x][y+1] < value) {
                value = values[x][y+1];
                previous[0] = x;
                previous[1] = y + 1;
            }
        }

        if (x < (t_height - 1)) {
            if (y > 0) {
                if (values[x+1][y-1] < value) {
                    value = values[x+1][y-1];
                    previous[0] = x + 1;
                    previous[1] = y - 1;
                }
            }
            if (values[x+1][y] < value) {
                value = values[x+1][y];
                previous[0] = x + 1;
                previous[1] = y;
            }
            if (y < (t_width - 1)) {
                if (values[x+1][y+1] < value) {
                    value = values[x+1][y+1];
                    previous[0] = x + 1;
                    previous[1] = y + 1;
                }
            }
        }

        return previous;
    }

    public int discover(int x, int y) {
        int cost = 0;

        if (x > 0) {
            if (y > 0) {
                if (discovered[x-1][y-1] == -1) {
                    discovered[x-1][y-1] = terrain[x-1][y-1];
                    cost++;
                }
            }
            if (discovered[x-1][y] == -1) {
                discovered[x-1][y] = terrain[x-1][y];
                cost++;
            }
            if (y < (t_height - 1)) {
                if (discovered[x-1][y+1] == -1) {
                    discovered[x-1][y+1] = terrain[x-1][y+1];
                    cost++;
                }
            }
        }

        if (y > 0) {
            if (discovered[x][y-1] == -1) {
                discovered[x][y-1] = terrain[x][y-1];
                cost++;
            }
        }
        if (discovered[x][y] == -1) {
            discovered[x][y] = terrain[x][y];
            cost++;
        }
        if (y < (t_height - 1)) {
            if (discovered[x][y+1] == -1) {
                discovered[x][y+1] = terrain[x][y+1];
                cost++;
            }
        }

        if (x < (t_width - 1)) {
            if (y > 0) {
                if (discovered[x+1][y-1] == -1) {
                    discovered[x+1][y-1] = terrain[x+1][y-1];
                    cost++;
                }
            }
            if (discovered[x+1][y] == -1) {
                discovered[x+1][y] = terrain[x+1][y];
                cost++;
            }
            if (y < (t_height - 1)) {
                if (discovered[x+1][y+1] == -1) {
                    discovered[x+1][y+1] = terrain[x+1][y+1];
                    cost++;
                }
            }
        }

        return cost;
    }
}