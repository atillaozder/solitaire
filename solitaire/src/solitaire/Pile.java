package solitaire;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLayeredPane;

public class Pile extends JLayeredPane implements MouseListener {

    protected Solitaire game;
    protected int baseCard = 1;

    protected Card head;
    protected Card tail;

    public Pile() {
        addMouseListener(this); // MARK: - In order to listen stock pile and deal the next Card
        head = null;
        tail = null;
    }

    public Pile(Solitaire game) {
        this();
        this.game = game;

    }

    public Pile(Solitaire game, int baseCard) {
        this();
        this.game = game;
        this.baseCard = baseCard;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public Card getHead() {
        return head;
    }

    public Card getTail() {
        return tail;
    }

    public int getListSize() {
        int count = 0;
        Card temp = head;
        while (temp != null) {
            count++;
            temp = temp.getNext();
        }

        return count;
    }

    protected void insertLast(Card card) {
        if (isEmpty()) {
            head = card;
            tail = card;
            return;
        }

        tail.setNext(card);
        card.setPrev(tail);
        tail = card;
    }

    protected void insertAll(Card card) {
        if (card.getPrev() != null) {
            card.getPrev().setNext(null);
        }

        while (card != null) {
            insertLast(card);
            card = card.getNext();
        }
    }

    protected Card remove(Card c) {
        if (isEmpty()) {
            return null;
        }

        if (head == c) {
            head = c.getNext();
        } else {
            if (c.getNext() != null) {
                c.getNext().setPrev(c.getPrev());
            }

            if (c.getPrev() != null) {
                c.getPrev().setNext(c.getNext());
                if (c == tail) {
                    tail = c.getPrev();
                }
            }
        }

        return c;
    }

    public void removeAll() {
        while (head != null) {
            if (head.getNext() != null) {
                head.getNext().setPrev(null);
            }
            head = head.getNext();
        }

        head = null;
        tail = null;
    }

    public void display() {
        Card card = this.head;
        while (card != null) {
            System.out.println(card.toString() + " ");
            card = card.getNext();
        }
    }

    public boolean isBaseCard(Card card) {
        return baseCard == card.getRank();
    }

    @Override
    public void paint(Graphics grphcs) {
        super.paint(grphcs);
        if (isEmpty()) {
            grphcs.drawRect(0, 0, Card.width - 1, Card.height - 1);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return super.getPreferredSize();
    }

    @Override
    public void mouseClicked(MouseEvent me) {

    }

    @Override
    public void mousePressed(MouseEvent me) {

    }

    @Override
    public void mouseReleased(MouseEvent me) {

    }

    @Override
    public void mouseEntered(MouseEvent me) {

    }

    @Override
    public void mouseExited(MouseEvent me) {

    }
}
