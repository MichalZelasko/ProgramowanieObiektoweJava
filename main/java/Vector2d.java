import java.util.Objects;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

public class Vector2d {
    private int x;
    private int y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }

    public boolean precedes(Vector2d other) {
        return this.x > other.x || this.y > other.y;
    }

    public boolean follows(Vector2d other) {
        return this.x < other.x || this.y < other.y;
    }

    public Vector2d upperRight(Vector2d other) {
        return new Vector2d(max(this.x, other.x), max(this.y, other.y));
    }

    public Vector2d lowerLeft(Vector2d other) {
        return new Vector2d(min(this.x, other.x), min(this.y, other.y));
    }

    public Vector2d add(Vector2d other) {
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Vector2d))
            return false;
        Vector2d that = (Vector2d) other;
        return that.x == this.x && that.y == this.y;
    }

    public int hashCode() {
        return Objects.hash(x, y);
    }

    public Vector2d opposite() {
        return new Vector2d(-this.x, -this.y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Vector2d multiplyByScalar(int a) {
        return new Vector2d(x * a, y * a);
    }

    public MapDirection toMapDirection() {
        if (this.x == 0 && this.y == 1) {
            return MapDirection.NORTH;
        }
        if (this.x == 1 && this.y == 1) {
            return MapDirection.NORTHEAST;
        }
        if (this.x == 1 && this.y == 0) {
            return MapDirection.EAST;
        }
        if (this.x == 1 && this.y == -1) {
            return MapDirection.SOUTHEAST;
        }
        if (this.x == 0 && this.y == -1) {
            return MapDirection.SOUTH;
        }
        if (this.x == -1 && this.y == -1) {
            return MapDirection.SOUTHWEST;
        }
        if (this.x == -1 && this.y == 0) {
            return MapDirection.WEST;
        }
        return MapDirection.NORTHWEST;
    }

    public Vector2d toVisualization(int mapSize) {
        return new Vector2d(x, mapSize - 1 - y);
    }
}
