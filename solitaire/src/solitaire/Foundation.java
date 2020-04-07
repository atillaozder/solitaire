package solitaire;

import java.awt.Dimension;
import java.awt.Graphics;

public class Foundation extends Pile {

    public Foundation(Solitaire game) {
        super(game);
        setPreferredSize(new Dimension(Card.width, Card.height));
        setSize(Card.width, Card.height);
    }

    @Override
    public void paint(Graphics grphcs) {
        super.paint(grphcs);
    }
}
