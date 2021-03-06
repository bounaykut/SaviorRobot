package app;

import java.io.Serializable;

public class Cell implements Serializable{

int x,y;
int count = 0;
boolean isDone = false;
Main.Color color = Main.Color.WHITE;

float[] distance = {0,0,0,0}; //{up,left,down,right}
int wallCount = 0;

Cell parent;
int g,h = 0;

Cell left;
Cell right;
Cell up;
Cell bottom;

public Cell() {
	
}

public Cell clone() {
	
	Cell result = new Cell();
	result.x = this.x;
	result.y = this.y;
	result.up = this.up;
	result.left = this.left;
	result.bottom = this.bottom;
	result.right = this.right;
	
	return result;
}


public int getX() {
	return x;
}
public void setX(int x) {
	this.x = x;
}
public int getY() {
	return y;
}
public void setY(int y) {
	this.y = y;
}
public int getCount() {
	return count;
}
public void setCount(int count) {
	this.count = count;
}
public boolean isDone() {
	return isDone;
}
public void setDone(boolean isDone) {
	this.isDone = isDone;
}
public Main.Color getColor() {
	return color;
}
public void setColor(Main.Color color) {
	this.color = color;
}
public float[] getDistance() {
	return distance;
}
public void setDistance(float[] distance) {
	this.distance = distance;
}
public Cell getLeft() {
	return left;
}
public void setLeft(Cell left) {
	this.left = left;
}
public Cell getRight() {
	return right;
}
public void setRight(Cell right) {
	this.right = right;
}
public Cell getUp() {
	return up;
}
public void setUp(Cell up) {
	this.up = up;
}
public Cell getBottom() {
	return bottom;
}
public void setBottom(Cell bottom) {
	this.bottom = bottom;
}



public int getWallCount() {
	return wallCount;
}



public void setWallCount(int wallCount) {
	this.wallCount = wallCount;
}






}
