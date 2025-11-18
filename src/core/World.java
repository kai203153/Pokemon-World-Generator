package core;

import tileengine.TETile;
import tileengine.Tileset;
import utils.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    private boolean isConnected() {

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
