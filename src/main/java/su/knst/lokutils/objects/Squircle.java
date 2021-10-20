package su.knst.lokutils.objects;

public class Squircle {
    public final Point position;
    public final Size size;
    public final double m;
    public final double n;
    public final double precision;

    public Squircle(Point position, Size size, float round) {
        this.position = position;
        this.size = size;
        round = Math.max(10 * (1 - round), 2f);
        this.m = round * Math.max(size.width / size.height, 1);
        this.n = round * Math.max(size.height / size.width, 1);
        this.precision = 0.02;
    }

    public Squircle(Rect rect, float round) {
        this(rect.position, rect.size, round);
    }

    public Squircle(Rect rect) {
        this(rect, 0.8f);
    }
}
