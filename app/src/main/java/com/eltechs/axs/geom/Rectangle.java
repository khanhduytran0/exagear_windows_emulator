package com.eltechs.axs.geom;

public final class Rectangle {
    public final int height;
    public final int width;
    public final int x;
    public final int y;

    public Rectangle(int i, int i2, int i3, int i4) {
        this.x = i;
        this.y = i2;
        this.width = i3;
        this.height = i4;
    }

    public boolean containsPoint(int i, int i2) {
        return this.x <= i && this.y <= i2 && this.x + this.width > i && this.y + this.height > i2;
    }

    public boolean containsPoint(Point point) {
        return containsPoint(point.x, point.y);
    }

    public boolean containsInnerPoint(int i, int i2) {
        return i >= 0 && i2 >= 0 && i < this.width && i2 < this.height;
    }

    public boolean containsInnerPoint(Point point) {
        return containsInnerPoint(point.x, point.y);
    }

    public boolean containsRectangle(Rectangle rectangle) {
        if (!containsPoint(rectangle.x, rectangle.y) || !containsPoint((rectangle.x + rectangle.width) - 1, (rectangle.y + rectangle.height) - 1)) {
            return false;
        }
        return true;
    }

    public boolean containsInnerRectangle(Rectangle rectangle) {
        if (!containsInnerPoint(rectangle.x, rectangle.y) || !containsInnerPoint((rectangle.x + rectangle.width) - 1, (rectangle.y + rectangle.height) - 1)) {
            return false;
        }
        return true;
    }

    public static Rectangle getIntersection(Rectangle rectangle, Rectangle rectangle2) {
        if (rectangle.containsRectangle(rectangle2)) {
            return rectangle2;
        }
        if (rectangle2.containsRectangle(rectangle)) {
            return rectangle;
        }
        int i = rectangle.x;
        int i2 = rectangle.y;
        int i3 = (rectangle.width + i) - 1;
        int i4 = (rectangle.height + i2) - 1;
        if (i >= rectangle2.x + rectangle2.width || i2 >= rectangle2.y + rectangle2.height || i3 < rectangle2.x || i4 < rectangle2.y) {
            return null;
        }
        if (i < rectangle2.x) {
            i = rectangle2.x;
        }
        if (i2 < rectangle2.y) {
            i2 = rectangle2.y;
        }
        if (i3 >= rectangle2.width) {
            i3 = rectangle2.width - 1;
        }
        if (i4 >= rectangle2.height) {
            i4 = rectangle2.height - 1;
        }
        return new Rectangle(i, i2, (i3 - i) + 1, (i4 - i2) + 1);
    }
}
