
public class Index2D implements Pixel2D{
    private int _x, _y;
    public Index2D() {this(0,0);}
    public Index2D(int x, int y) {_x=x;_y=y;}
    public Index2D(Pixel2D t) {this(t.getX(), t.getY());}
    public Index2D(String s) {
        String []s2 = s.split(",");
        _x = Integer.parseInt(s2[0]);
        _y = Integer.parseInt(s2[1]);
    }
    @Override
    public int getX() {
        return _x;
    }
    @Override
    public int getY() {
        return _y;
    }
    /**
     * Calculates the 2D distance between two Pixel2D points.
     * @param t The second point to calculate the distance to.
     * @return The calculated 2D distance between the two points.
     * @throws RuntimeException if the second point is null.
     */
    public double distance2D(Pixel2D t) {
        double ans = 0;
        if(t == null){//checks if t is null
            throw new RuntimeException("The second point is null!");
        }//if
        double deltaX = Math.abs(this.getX() - t.getX());//Calculates the distance between the two X's at the two points
        double deltaY = Math.abs(this.getY() - t.getY());//Calculates the distance between the two y's at the two points
        ans = Math.sqrt(Math.pow(deltaX,2)+Math.pow(deltaY,2));//distance formula
        return ans;
    }
    @Override
    public String toString() {
        return getX()+","+getY();
    }
    /**
     * Checks if this Pixel2D object is equal to the specified object.
     * Two Pixel2D objects are considered equal if their 2D distance is zero.
     * @param t The object to compare this Pixel2D object to.
     * @return true if the objects are equal, false otherwise.
     */

    @Override
    public boolean equals(Object t) {
        boolean ans = false;
        // Checking if t is not null and its a type of Pixel2D
        if(t != null && t instanceof Pixel2D){
            Pixel2D p = (Pixel2D) t;// Casting the Object parameter to Pixel2D type
            ans = this.distance2D(p) == 0;// Checking if the distance between this pixel and p is 0
        }

        return ans;
    }
}
