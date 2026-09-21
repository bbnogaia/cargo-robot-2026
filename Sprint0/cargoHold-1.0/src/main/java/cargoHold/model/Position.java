package main.java.cargoHold.model;

public class Position {
	private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // restituisce il valore della coordinata X come intero
    public int getX() {
        return x;
    }

    // restituisce il valore della coordinata Y come intero
    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    // restituisce il valore delle coordinate come stringa (X,Y)
    public String toString() {
        return "(" + x + "," + y + ')';
    }

}
