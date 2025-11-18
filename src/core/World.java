package core;

import tileengine.TETile;
import tileengine.Tileset;
import utils.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class World {
    private TETile[][] world;
    private Random random;
    List<Room> rooms;

    public World(long seed) {
        random = new Random(seed);
        world = new TETile[67][67];
    }

    public TETile[][] generate() {
        fillWithNothing();
        int numberOfRooms = RandomUtils.uniform(random, 8,20);
        rooms = new ArrayList<>();
        for (int i=0; i < numberOfRooms; i++) {
            int cx= RandomUtils.uniform(random, 10, 57);
            int cy = RandomUtils.uniform(random, 10, 57);
            int height = RandomUtils.uniform(random, 3, 9) | 1;
            int width = RandomUtils.uniform(random, 3, 9) | 1;
            Room room = new Room(cx, cy, height, width);
            rooms.add(room);
            carveRoom(room);
        }
        generateHallways();
        generateWalls();
        return world;
    }

    private void fillWithNothing() {
        for (int x = 0; x < 67; x++) {
            for (int y = 0; y < 67; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    private void carveRoom(Room r) {
        int left = r.center_x - (r.width - 1) / 2;
        int right = r.center_x + (r.width - 1) / 2;
        int top = r.center_y + (r.height - 1) / 2;
        int bottom = r.center_y - (r.height -1) / 2;
        for (int i = left; i <= right; i++) {
            for (int j = bottom; j <= top; j++) {
                world[i][j] = Tileset.FLOOR;
            }
        }
    }

    private void generateHallways() {
        boolean[] connected = new boolean[rooms.size()];
        connected[0] = true;
        int connectedCount = 1;
        while (connectedCount < rooms.size()) {
            int bestdist = 67;
            Room bestA = null;
            Room bestB = null;
            for (int i = 0; i < rooms.size(); i++) {
                if (connected[i] == true) {
                    for (int j = 0; j < rooms.size(); j++) {
                        if (connected[j] == false) {
                            int d = dist(rooms.get(i),rooms.get(j));
                            if (d < bestdist) {
                                bestdist = d;
                                bestA = rooms.get(i);
                                bestB = rooms.get(j);
                            }
                        }
                }
                }
            }
            connectRooms(bestA, bestB);
            connected[rooms.indexOf(bestB)] = true;
            connectedCount += 1;
        }
    }

    private void connectRooms(Room a, Room b) {
        for (int i = min(a.center_x, b.center_x); i <= max(a.center_x, b.center_x); i++) {
            world[i][a.center_y] = Tileset.FLOOR;
        }
        for (int j = min(a.center_y, b.center_y); j <= max(a.center_y, b.center_y); j++) {
            world[b.center_x][j] = Tileset.FLOOR;
        }
    }

    private int dist(Room a, Room b) {
        int dist_x = Math.abs(a.center_x - b.center_x);
        int dist_y = Math.abs(a.center_y - b.center_y);
        return dist_x + dist_y;
    }

    private void generateWalls() {
        for (int i = 0; i < 67; i++) {
            for (int j = 0; j < 67; j++) {
                if (world[i][j] == Tileset.FLOOR) {
                    for (int x = i-1; x <= i+1; x++) {
                        for (int y = j -1; y <= j + 1; y++) {
                            if (x >= 0 && x < 67 && y >= 0 && y < 67 && world[x][y] == Tileset.NOTHING) {
                                world[x][y] = Tileset.WALL;
                            }
                        }
                    }
                }
            }
        }
    }

    private static class Room {
        int center_x;
        int center_y;
        int height;
        int width;

        Room(int cx, int cy, int h, int w) {
            this.center_x = cx;
            this.center_y = cy;
            this.height = h;
            this.width = w;
        }
    }

    // build your own world!

}
