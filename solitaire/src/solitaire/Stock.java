package solitaire;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import javax.imageio.ImageIO;
import static solitaire.Card.dir;

public final class Stock extends Pile {

    private BufferedImage img;

    public Stock(Solitaire game) {
        super(game);
        setPreferredSize(new Dimension(Card.width, Card.height));
        setSize(Card.width, Card.height);
    }

    public void insertFirst(Card card) {
        card.addMouseListener(this);

        if (card.getPrev() != null) {
            card.getPrev().setNext(null);
        }

        if (isEmpty()) {
            head = card;
            tail = card;
            return;
        }

        card.setNext(head);
        card.setPrev(null);
        head.setPrev(card);
        head = card;
    }

    @Override
    public void insertLast(Card card) {
        super.insertLast(card);
        add(card, 0, 0);
    }

    public void shuffle() {
        if (isEmpty()) {
            return;
        }

        Random rand = new Random();
        int random = (int) (Math.random() * getListSize());
        Card card;

        for (int x = 0; x < random; x++) {
            for (int y = 1; y < 52; y++) {
                card = head;
                random = rand.nextInt(52 - y);
                for (int k = 0; k < random; k++) {
                    card = card.getNext();
                }

                remove(card);
                Card c = new Card(card.getRank(), card.getSuit(), game);
                insertLast(c);
            }
        }
    }

    public void setImg() {
        try {
            URL url = new URL(dir.toURI().toURL(), "background.png");
            img = ImageIO.read(url);
            setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
            setSize(img.getWidth(), img.getHeight());
            setOpaque(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paint(Graphics grphcs) {
        super.paint(grphcs);
        if (img != null) {
            grphcs.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), this);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Card.width, Card.height + 1);
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        super.mouseClicked(me);
        game.dealNext(me);
    }
}
