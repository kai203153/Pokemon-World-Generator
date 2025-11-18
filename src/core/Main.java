package core;

import tileengine.TERenderer;
import tileengine.TETile;

public class Main {
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(67,67);
        World w = new World(12345);
        TETile[][] world = w.generate();
        ter.renderFrame(world);
    }
}
