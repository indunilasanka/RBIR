package aztec.rbir_backend.clustering;

import java.io.Serializable;

/**
 * Created by asankai on 03/08/2017.
 */
public class Vector implements Serializable{

    private final int n;         // length of the vector
    private double[] data;       // array of vector's components

    // create the zero vector of length n
    public Vector(int n) {
        this.n = n;
        this.data = new double[n];
    }

    // create a vector from an array
    public Vector(double[] data) {
        n = data.length;

        // defensive copy so that client can't alter our copy of data[]
        this.data = new double[n];
        for (int i = 0; i < n; i++)
            this.data[i] = data[i];
    }

    // return the length of the vector
    public int length() {
        return n;
    }

    // return the inner product of this Vector and that
    public double dot(Vector that) {
        if (this.length() != that.length())
            throw new IllegalArgumentException("dimensions disagree");
        double sum = 0.0;
        for (int i = 0; i < n; i++)
            sum = sum + (this.data[i] * that.data[i]);
        return sum;
    }

    // return the Euclidean norm of this Vector
    public double magnitude() {
        return Math.sqrt(this.dot(this));
    }

    // return the Euclidean distance between this and that
    public double distanceTo(Vector that) {
        if (this.length() != that.length())
            throw new IllegalArgumentException("dimensions disagree");
        return this.minus(that).magnitude();
    }

    // return this - that
    public Vector minus(Vector that) {
        if (this.length() != that.length())
            throw new IllegalArgumentException("dimensions disagree");
        Vector c = new Vector(n);
        for (int i = 0; i < n; i++)
            c.data[i] = this.data[i] - that.data[i];
        return c;
    }

    // return the corresponding coordinate
    public double cartesian(int i) {
        return data[i];
    }

    // create and return a new object whose value is (this * factor)
    @Deprecated
    public Vector times(double factor) {
        Vector c = new Vector(n);
        for (int i = 0; i < n; i++)
            c.data[i] = factor * data[i];
        return c;
    }

    // create and return a new object whose value is (this * factor)
    public Vector scale(double factor) {
        Vector c = new Vector(n);
        for (int i = 0; i < n; i++)
            c.data[i] = factor * data[i];
        return c;
    }


    // return the corresponding unit vector
    public Vector direction() {
        if (this.magnitude() == 0.0)
            throw new ArithmeticException("zero-vector has no direction");
        return this.times(1.0 / this.magnitude());
    }

    //this + that
    public void plus(Vector that) {
        if (this.length() != that.length())
            throw new IllegalArgumentException("dimensions disagree");
        for (int i = 0; i < n; i++)
            data[i] = data[i] + that.data[i];
    }

    public void divide(double factor) {
        for (int i = 0; i < n; i++)
            data[i] = data[i]/factor;
    }

    public void invert(){
        for (int i = 0; i < n; i++) {
            if (data[i] > 0)
                data[i] = 1 / data[i];
        }
    }

    public void multiply(double factor){
        for (int i = 0; i < n; i++)
            data[i] = factor * data[i];
    }

    public void log(){
        for (int i = 0; i < n; i++) {
            if (data[i] > 0)
                data[i] = Math.log(data[i]);
        }
    }

    public void logFrequency(){
        for (int i = 0; i < n; i++){
            if(data[i] > 0)
                data[i] = 1 + Math.log(data[i]);
        }
    }

    public void increment(int index){
        data[index]++;
    }

    public double get(int index){
        return data[index];
    }

    // return a string representation of the vector
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append('(');
        for (int i = 0; i < n; i++) {
            s.append(data[i]);
            if (i < n-1) s.append(", ");
        }
        s.append(')');
        return s.toString();
    }

}
