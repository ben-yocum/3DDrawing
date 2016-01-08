package cs355.controller;

import cs355.GUIFunctions;
import cs355.model.SimpleModel;
import cs355.model.drawing.*;
import cs355.model.drawing.Rectangle;
import cs355.model.drawing.Shape;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by ben on 9/3/15.
 */
public class ShapeFactory {

    private SimpleModel model;

    private Shape shapeInProgress;
    private Point2D.Double anchor; // used to remember the starting point of squares and rectangles
    private Color color;
    ButtonEnum shapeType;
    private Point2D.Double a;
    private Point2D.Double b;
    private Point2D.Double c;
    private int numTrianglePoints = 0;

    public ShapeFactory(SimpleModel model){
        this.model = model;
        this.color = Color.WHITE;
        this.shapeType = ButtonEnum.LINE;
    }

    public void setColor(Color c){
        this.color = c;
    }

    public void setShapeType(ButtonEnum shape){
        this.shapeType = shape;
    }

    /**
     * Clicks will only be considered if the object being drawn is a triangle.
     * @param p the location of the click.
     */
    public Shape click(Point2D.Double p){
        if(shapeType == ButtonEnum.TRIANGLE){
            if(numTrianglePoints == 0){
                startShape(p);
                GUIFunctions.printf("Place triangle second point");
                return null;
            }
            else{
                return nextTrianglePoint(p);
            }
        }
        return null;
    }

    /**
     * A press indicates the start of a shape. It will be ignored if it'shapeInProgress a triangle.
     * Otherwise, it will be considered the first point.
     * @param p
     */
    public void press(Point2D.Double p){
        switch(shapeType){
            case TRIANGLE:
                break;
            case CIRCLE:
                anchor = (Point2D.Double)p.clone();
                // add the circle to the list of shapes in progress
                shapeInProgress = new Circle(color, p, 0);
                model.addShapeInProgress(shapeInProgress);
                break;
            case ELLIPSE:
                anchor = (Point2D.Double)p.clone();
                shapeInProgress = new Ellipse(color, p, 0, 0);
                model.addShapeInProgress(shapeInProgress);
                break;
            case LINE:
                shapeInProgress = new Line(color, p, ((Point2D.Double)p.clone()));
                model.addShapeInProgress(shapeInProgress);
                break;
            case SQUARE:
                anchor = (Point2D.Double)p.clone();
                shapeInProgress = new Square(color, ((Point2D.Double)p.clone()), 0);
                model.addShapeInProgress(shapeInProgress);
                break;
            case RECTANGLE:
                anchor = (Point2D.Double)p.clone();
                shapeInProgress = new Rectangle(color, ((Point2D.Double)p.clone()), 0, 0);
                model.addShapeInProgress(shapeInProgress);
        }
    }

    public void drag(Point2D.Double p){
        switch(shapeType){
            case TRIANGLE:
                break;
            case CIRCLE:
                Circle cir = (Circle)shapeInProgress;

                // find the new size. Set by height first, but by width if width would be inside square.
                cir.setRadius(Math.abs(anchor.getY() - p.getY()) / 2);
                if(Math.abs(anchor.getX() - p.getX()) < cir.getRadius() * 2){
                    cir.setRadius(Math.abs(anchor.getX() - p.getX()) / 2);
                }

                // set offset of upper left corner from original anchor point
                double newXC = anchor.getX() + cir.getRadius();
                double newYC = anchor.getY() + cir.getRadius();
                if(p.getY() < anchor.getY()){
                    newYC = anchor.getY() - cir.getRadius();
                }
                if(p.getX() < anchor.getX()){
                    newXC = anchor.getX() - cir.getRadius();
                }
                cir.setCenter(new Point2D.Double(newXC, newYC));

                break;
            case ELLIPSE:
                Ellipse ell = (Ellipse)shapeInProgress;

                // find the new height and width
                ell.setWidth(Math.abs(anchor.getX() - p.getX()));
                ell.setHeight(Math.abs(anchor.getY() - p.getY()));

                // set offset of upper left corner from original anchor point
                double newXE = anchor.getX() + ell.getWidth() / 2;
                double newYE = anchor.getY() + ell.getHeight() / 2;
                if(p.getX() < anchor.getX()){
                    newXE = anchor.getX() - ell.getWidth() / 2;
                }
                if(p.getY() < anchor.getY()){
                    newYE = anchor.getY() - ell.getHeight() / 2;
                }
                ell.setCenter(new Point2D.Double(newXE, newYE));
                break;
            case LINE:
                Line l = ((Line)shapeInProgress);
                l.getEnd().setLocation(p);
                break;
            case SQUARE:
                Square s = (Square)shapeInProgress;

                // find the new size. Set by height first, but by width if width would be inside square.
                s.setSize(Math.abs(anchor.getY() - p.getY()));
                if(Math.abs(anchor.getX() - p.getX()) < s.getSize()){
                    s.setSize(Math.abs(anchor.getX() - p.getX()));
                }

                // set offset of center from original anchor point
                // Start with bottom left (x and y both increasing)
                double newX = anchor.getX() + s.getSize() / 2;
                double newY = anchor.getY() + s.getSize() / 2;
                if(p.getY() < anchor.getY()){
                    newY = anchor.getY() - s.getSize() / 2;
                }
                if(p.getX() < anchor.getX()){
                    newX = anchor.getX() - s.getSize() / 2;
                }
                s.setCenter(new Point2D.Double(newX, newY));

                break;
            case RECTANGLE:
                Rectangle r = (Rectangle)shapeInProgress;

                // find the new height and width
                r.setWidth(Math.abs(anchor.getX() - p.getX()));
                r.setHeight(Math.abs(anchor.getY() - p.getY()));

                // set offset of upper left corner from original anchor point
                newX = anchor.getX() + r.getWidth() / 2;
                newY = anchor.getY() + r.getHeight() / 2;
                if(p.getX() < anchor.getX()){
                    newX = anchor.getX() - r.getWidth() / 2;
                }
                if(p.getY() < anchor.getY()){
                    newY = anchor.getY() - r.getHeight() / 2;
                }
                r.setCenter(new Point2D.Double(newX, newY));

                break;

        }
        model.quickNotify();

    }

    /**
     * A release indicates the end of a shape. It will be ignored if it'shapeInProgress a triangle.
     * Otherwise, it will be considered the last point, and the shape will be drawn.
     * @param p
     */
    public Shape release(Point2D.Double p){
        if(shapeType == ButtonEnum.TRIANGLE){
            return null;
        }
        else{
            model.clearShapesInProgress();

            // add the circle to the list of shapes
            model.addShape(shapeInProgress);

            // reset shapeInProgress for the next shape
            shapeInProgress = null;

            return shapeInProgress;
        }
    }

    /**
     * This function is used to start drawing any shape.
     * It is called when the mouse was clicked and a triangle is being drawn,
     * or when the mouse was pressed and anything besides a triangle is being drawn.
     */
    public void startShape(Point2D.Double point){
        // Triangle is drawn differently from everything else, so it gets its own strategy.
        if(shapeType == ButtonEnum.TRIANGLE){
            a = point;
            numTrianglePoints = 1;
        }
    }

    /**
     * This function adds the next point to the triangle.
     * @return
     */
    public Shape nextTrianglePoint(Point2D.Double point){
        if(numTrianglePoints == 1){
            b = point;
            numTrianglePoints = 2;
            GUIFunctions.printf("Place triangle third point");

            return null;
        }
        else if(numTrianglePoints == 2){
            c = point;
            numTrianglePoints = 0;
            GUIFunctions.printf("Triangle done! Start another");

            Point2D.Double center = new Point2D.Double((a.getX() + b.getX() + c.getX()) / 3,
                    (a.getY() + b.getY() + c.getY()) / 3);

            a.setLocation(a.getX() - center.getX(), a.getY() - center.getY());
            b.setLocation(b.getX() - center.getX(), b.getY() - center.getY());
            c.setLocation(c.getX() - center.getX(), c.getY() - center.getY());

            return new Triangle(color, center, a, b, c);
        }
        else{
            System.out.println("ERROR IN NEXTTRIANGLEPOINT");
            return null;
        }
    }
}
