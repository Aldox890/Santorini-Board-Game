package project.client.GUI;

import javax.swing.*;

public class CellButton extends JButton {

    private int row;
    private int column;

    public CellButton(int r, int c) {
        this.row = r;
        this.column = c;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
