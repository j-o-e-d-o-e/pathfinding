package net.joedoe;

public class GameInfo {
    public static final int WIDTH = 800; // in pixel
    public static final int HEIGHT = 500;// in pixel
    public static final int PIXEL = 8; // pixel width & height per tile
    public static final float SCALE = 2.5f; // scaling factor
    public static final int ONE_TILE = (int) (PIXEL * SCALE); // size of one
                                                              // scaled tile
    public static final float[] RED = new float[] { 217 / 255f, 100 / 255f, 89 / 255f };
    public static final float[] GREEN = new float[] { 151 / 255f, 206 / 255f, 104 / 255f };;
    public static final float[] BLUE = new float[] { 75 / 255f, 166 / 255f, 224 / 255f };
    public static final float[] GREY = new float[] { 105 / 255f, 105 / 255f, 105 / 255f };
    public static final String CONTROLS = "In the city:\nMove: arrow keys or w, a, s, d\n\n"
            + "In fight:\nMove: arrow keys\nShoot: w, a, s, d\nChange weapon: numbers\n"
            + "Reload: r\nEnd turn: e\n\nPause/Resume: p";
}