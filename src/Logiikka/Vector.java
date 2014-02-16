package Logiikka;

/**
 *
 * @author delma
 */
public class Vector {

    private double x;
    private double y;
    
    public Vector() {
        this.x = 0;
        this.y = 0;
    }
    
    public Vector(Vector vector) {
        this.x = vector.x;
        this.y = vector.y;
    }

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

}
