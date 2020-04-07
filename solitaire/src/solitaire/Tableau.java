package solitaire;

import java.awt.Dimension;
import java.awt.Graphics;

public final class Tableau extends Pile {

    public Tableau(Solitaire game) {
        super(game);
    }

    public Tableau(Solitaire game, int baseCard) {
        super(game, baseCard);
    }

    @Override
    protected void insertLast(Card card) {
        super.insertLast(card);
        add(card, 1, 0);
    }

    public void insertIndividually(Card card) {
        if (card.getNext() != null) {
            card.setNext(null);
        }
        insertLast(card);
    }

    @Override
    public void paint(Graphics grphcs) {
        super.paint(grphcs);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Card.width, Card.height * getListSize());
    }
}
