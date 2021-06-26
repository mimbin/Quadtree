import java.util.ArrayList;
import java.util.Scanner;

class Quadtree {
    Quadtree northeast, northwest, southeast, southwest;
    int MAXIMUM_CAPACITY;
    Rectangle boundary;
    ArrayList<Point> includedPoints;
    boolean divided;


    public Quadtree(int MAXIMUM_CAPACITY, Rectangle boundary) {
        this.MAXIMUM_CAPACITY = MAXIMUM_CAPACITY;
        this.boundary = boundary;
        this.includedPoints = new ArrayList<Point>(MAXIMUM_CAPACITY);
    }

    void subdivide() {
        double x = this.boundary.x;
        double y = this.boundary.y;
        double h = this.boundary.height ;
        double w = this.boundary.width  ;

        Rectangle ne = new Rectangle(x + w / 4.00, y + h / 4.00, h/2.00 , w/2.00 );
        this.northeast = new Quadtree(this.MAXIMUM_CAPACITY, ne);
        Rectangle nw = new Rectangle(x - w / 4.00, y + h / 4.00, h/2.00 , w/2.00 );
        this.northwest = new Quadtree(this.MAXIMUM_CAPACITY, nw);
        Rectangle se = new Rectangle(x + w / 4.00, y - h / 4.00, h/2.00 , w/2.00 );
        this.southeast = new Quadtree(this.MAXIMUM_CAPACITY, se);
        Rectangle sw = new Rectangle(x - w / 4.00, y - h / 4.00, h/2.00 , w/2.00 );
        this.southwest = new Quadtree(this.MAXIMUM_CAPACITY, sw);
        this.divided = true;


    }

    boolean search_point(Point tempPoint) {
        //is it even within the range?
        if (!this.boundary.contains(tempPoint))
            return false;
        else {
            if (!this.divided) {
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
                else {
                    return this.southwest.search_point(tempPoint);
                }
            }
            //return (this.boundary.contains(tempPoint))
        }
        return false;
    }

    int search_area(Rectangle queryArea) {
        int count = 0;
        // check if two areas even intersect or not
        if (!this.boundary.intersects(queryArea))
            return 0;
        else {
            if (this.divided) {
                this.northwest.search_area(queryArea);
                this.northeast.search_area(queryArea);
                this.southwest.search_area(queryArea);
                this.southeast.search_area(queryArea);
            }
            for (Point p : this.includedPoints) {
                if (queryArea.contains(p))
                    ++count;
            }
        }
        return count;
    }

    boolean insert(Point point) {
        if (!this.boundary.contains(point))
            return false;

        if (this.includedPoints.size() < this.MAXIMUM_CAPACITY) {
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
                return (this.southeast.insert(point));
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
//        System.out.println("enter maximum capacity per cell:");
//        capacity = scanner.nextInt();
        Quadtree qtInitiate = new Quadtree(capacity, workArea);
        while (scanner.hasNext()){
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
                    Point tempPoint = new Point(x , y);
                    if (qtInitiate.search_point(tempPoint)){
                        System.out.println("TRUE");
                    } else{
                        System.out.println("FALSE"); }
                    input = null;
                    break;
                case "Area":
                    x = scanner.nextInt();
                    y = scanner.nextInt();
                    x2 = scanner.nextInt();
                    y2 = scanner.nextInt();
                    Rectangle area = new Rectangle((x2 - x) / 2.00, (y2 - y) / 2.00, Math.abs(y - y2), Math.abs(x - x2));
                    System.out.println(qtInitiate.search_area(area));
                    input = null;
                    break;
                default:
                    System.out.println("undefined order");
            }
        }
        scanner.close();
        System.exit(0);
    }
}