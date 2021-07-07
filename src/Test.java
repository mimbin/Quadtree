import java.util.ArrayList;
import java.util.Scanner;
//this is gonna be cooooooooooooooooooooool
class Quadtree {
    Quadtree northeast, northwest, southeast, southwest;
    int MAXIMUM_CAPACITY;
    Rectangle boundary;
    ArrayList<Point> includedPoints;
    boolean divided;

    public Quadtree(int MAXIMUM_CAPACITY, Rectangle boundary, ArrayList<Point> includedPoints2) {
        this.MAXIMUM_CAPACITY = MAXIMUM_CAPACITY;
        this.boundary = boundary;
        this.includedPoints = includedPoints2;
    }

    public Quadtree(int MAXIMUM_CAPACITY, Rectangle boundary) {
        this.MAXIMUM_CAPACITY = MAXIMUM_CAPACITY;
        this.boundary = boundary;
        this.includedPoints = new ArrayList<Point>();
    }

    void subdivide() {
        double x = this.boundary.x;
        double y = this.boundary.y;
        double h = this.boundary.height;
        double w = this.boundary.width;
        ArrayList<Point> tempPoints = new ArrayList<>();
        ArrayList<Point> tempPoints2 = new ArrayList<>();
        ArrayList<Point> tempPoints3 = new ArrayList<>();
        ArrayList<Point> tempPoints4 = new ArrayList<>();
//        ArrayList<Point> toRemove = new ArrayList<>();

//        ------------------------------------------------------------------------------------
        Rectangle ne = new Rectangle(x + w / 4.00, y + h / 4.00, h / 2.00, w / 2.00);
        for (Point p : this.includedPoints) {
            if (ne.contains(p)) {
                tempPoints.add(p); //those who must be transferred to children's PointList
//                toRemove.add(p); //those who are redundant in parent's PointList and are moved to children's
            }
        }
        if (tempPoints.size() > 0) {
            this.northeast = new Quadtree(this.MAXIMUM_CAPACITY, ne, tempPoints);
            this.includedPoints.removeAll(tempPoints);
            //tempPoints.clear();
        } else {
            this.northeast = new Quadtree(this.MAXIMUM_CAPACITY, ne);
        }
//        this.northeast.includedPoints.addAll(tempPoints);
//        toRemove.clear();

//        -----------------------------------------------------------------------------------------
        Rectangle nw = new Rectangle(x - w / 4.00, y + h / 4.00, h / 2.00, w / 2.00);
        for (Point p : this.includedPoints) {
            if (nw.contains(p)) {
                tempPoints2.add(p);
//                toRemove.add(p);
            }
        }
        if (tempPoints2.size() > 0) {
            this.northwest = new Quadtree(this.MAXIMUM_CAPACITY, nw, tempPoints2);
            this.includedPoints.removeAll(tempPoints2);
            //tempPoints2.clear();
        } else {
            this.northwest = new Quadtree(this.MAXIMUM_CAPACITY, nw);
        }
//        this.northwest.includedPoints.addAll(tempPoints2);
//        toRemove.clear();


//        ---------------------------------------------------------------------------------------------
        Rectangle se = new Rectangle(x + w / 4.00, y - h / 4.00, h / 2.00, w / 2.00);
        for (Point p : this.includedPoints) {
            if (se.contains(p)) {
                tempPoints3.add(p);
//                toRemove.add(p);
            }
        }
        if (tempPoints3.size() > 0) {
            this.southeast = new Quadtree(this.MAXIMUM_CAPACITY, se, tempPoints3);
            this.includedPoints.removeAll(tempPoints3);
            //tempPoints3.clear();

        }
        else {
            this.southeast = new Quadtree(this.MAXIMUM_CAPACITY, se);
        }
//        this.southeast.includedPoints.addAll(tempPoints3);
//        toRemove.clear();

//        ----------------------------------------------------------------------------------------------
        Rectangle sw = new Rectangle(x - w / 4.00, y - h / 4.00, h / 2.00, w / 2.00);
        for (Point p : this.includedPoints) {
            if (sw.contains(p)) {
                tempPoints4.add(p);
//                toRemove.add(p);
            }
        }
        if (tempPoints4.size() > 0) {
            this.southwest = new Quadtree(this.MAXIMUM_CAPACITY, sw, tempPoints4);
            this.includedPoints.removeAll(tempPoints4);
            //tempPoints4.clear();

        }
        else{
            this.southwest = new Quadtree(this.MAXIMUM_CAPACITY, sw);
        }
//        this.southwest.includedPoints.addAll(tempPoints4);
//        toRemove.clear();
        this.divided = true;
    }

    boolean search_point(Point tempPoint) {
        //is it even within the range?
        if (!this.boundary.contains(tempPoint))
            return false;
        else {
            if (!this.divided) { //if it has no children, search within parent's points
                for (Point p : this.includedPoints) {
                    if (p.x == tempPoint.x && p.y == tempPoint.y) {
                        return true;
                    }
                }
            } else {
                if (this.northeast.search_point(tempPoint))
                    return true;
                else if (this.northwest.search_point(tempPoint))
                    return true;
                else if (this.southeast.search_point(tempPoint))
                    return true;
                else if (this.southwest.search_point(tempPoint)){
                    return true;
                }
            }
        }
        return false;
    }

    int search_area(Rectangle queryArea , ArrayList<Point> count) {
        if (!this.boundary.intersects(queryArea)) {
            return count.size();
        } else {
            for (Point p : this.includedPoints) {
                if (queryArea.contains(p))
                    count.add(p);
            }
            if (this.divided) {
                this.northwest.search_area(queryArea , count);
                this.northeast.search_area(queryArea , count);
                this.southwest.search_area(queryArea , count);
                this.southeast.search_area(queryArea , count);
            }

        }
        return count.size();
    }

    boolean insert(Point point) {
        if (!this.boundary.contains(point))
            return false;

        if (this.includedPoints.size() < this.MAXIMUM_CAPACITY && !this.divided) {
            this.includedPoints.add(point);
            return true;
        } else {
            if (!this.divided) {
                this.subdivide();
            }
            if (this.northeast.insert(point)) {
                return true;
            } else if (this.northwest.insert(point)) {
                return true;
            } else if (this.southeast.insert(point)) {
                return true;
            } else {
                return (this.southwest.insert(point));
            }
        }
    }

}

class Point {
    double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
}

class Rectangle {
    double x, y, height, width, left, right, bottom, top;


    public Rectangle(double x, double y, double height, double width) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.left = x - (width / 2.00);
        this.right = x + (width / 2.00);
        this.bottom = y - (height / 2.00);
        this.top = y + (height / 2.00);
    }

    public boolean contains(Point point) {
        return (point.x >= this.left && point.x <= this.right &&
                point.y <= this.top && point.y >= this.bottom);
    }

    public boolean intersects(Rectangle range) {
        return !(
                this.right < range.left || range.right < this.left ||
                        this.bottom > range.top || range.bottom > this.top
        );
    }
}

public class Test {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Rectangle workArea = new Rectangle(0, 0, 200000, 200000);
        int x, y, x2, y2, capacity = 4;
        String input;
        Quadtree qtInitiate = new Quadtree(capacity, workArea);
        while (scanner.hasNext()) {
            input = scanner.next();
            switch (input) {
                case "Insert":
                    x = scanner.nextInt();
                    y = scanner.nextInt();
                    Point newPoint = new Point(x, y);
                    qtInitiate.insert(newPoint);
                    input = null;
                    break;

                case "Search":
                    x = scanner.nextInt();
                    y = scanner.nextInt();
                    Point tempPoint = new Point(x, y);
                    if (qtInitiate.search_point(tempPoint)) {
                        System.out.println("TRUE");
                    } else {
                        System.out.println("FALSE");
                    }
                    input = null;
                    break;
                case "Area":
                    x = scanner.nextInt();
                    y = scanner.nextInt();
                    x2 = scanner.nextInt();
                    y2 = scanner.nextInt();
                    ArrayList<Point> count = new ArrayList<Point>();
                    Rectangle area = new Rectangle(((x2 + x) / 2.00), ((y2 + y) / 2.00), Math.abs(y - y2), Math.abs(x - x2));
                    qtInitiate.search_area(area , count);
                    System.out.println(count.size());
                    input = null;
                    break;
                default:
                    System.out.println("undefined order");
            }
        }
    }
}