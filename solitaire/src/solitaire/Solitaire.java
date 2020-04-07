package solitaire;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import solitaire.Card.Suit;

public class Solitaire extends JFrame {

    int numOfCards = 0;
    private final JPanel gamePanel = new JPanel(new BorderLayout());
    private final JLayeredPane centerPanel = new JLayeredPane();
    private final JPanel botPanel = new JPanel(new BorderLayout());
    private final JPanel topPanel = new JPanel(new BorderLayout());
    private final JPanel stockPanel;
    private final JLabel cardCount = new JLabel();

    private final Stock stockPile = new Stock(this);
    private Tableau tableauPile;

    private final JPanel foundationsPanel;

    private Solitaire() {
        gamePanel.setSize(1000, 750);

        GridLayout gridLayout = new GridLayout(1, 7);
        gridLayout.setHgap(20);
        centerPanel.setLayout(gridLayout);

        FlowLayout flowLayout = new FlowLayout(FlowLayout.RIGHT);
        flowLayout.setHgap(20);
        foundationsPanel = new JPanel(flowLayout);

        stockPanel = new JPanel(flowLayout);
        Stock customDeck = new Stock(this);
        customDeck.setImg();
        stockPanel.add(customDeck);
        stockPanel.add(new Stock(this));

        dealCards();

        JPanel statPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statPanel.add(new JLabel("Cards: ", JLabel.LEFT));
        statPanel.add(cardCount);

        botPanel.setBorder(BorderFactory.createEtchedBorder());
        botPanel.add(statPanel, BorderLayout.EAST);
        gamePanel.add(botPanel, BorderLayout.SOUTH);

        topPanel.add(foundationsPanel, BorderLayout.EAST);
        topPanel.add(stockPanel, BorderLayout.WEST);
        gamePanel.add(topPanel, BorderLayout.NORTH);
        gamePanel.add(centerPanel, BorderLayout.CENTER);

        numOfCards += stockPile.getListSize();
        cardCount.setText(Integer.toString(numOfCards));

        add(gamePanel, BorderLayout.CENTER);
    }

    private void dealCards() {
        initStock();

        for (int i = 0; i < 4; i++) {
            Foundation f = new Foundation(this);
            foundationsPanel.add(f);
        }

        for (int i = 1; i <= 7; i++) {
            tableauPile = new Tableau(this, 13);
            centerPanel.add(tableauPile);

            for (int j = 1; j <= i; j++) {
                numOfCards++;
                Card card = stockPile.getHead();
                stockPile.remove(card);

                if (j == i) {
                    card.flip();
                }

                card.yOffset = (Card.height / 4) * j;
                card.setBounds(tableauPile.getX() + 13, card.yOffset, Card.width, Card.height);
                tableauPile.insertIndividually(card);
            }
        }
    }

    public void dealNext(MouseEvent me) {
        if (me.getComponent().equals(stockPanel.getComponent(0))) {
            if (stockPile.isEmpty()) {
                return;
            }

            Stock openedCardStock = (Stock) stockPanel.getComponent(1);
            Card card = stockPile.getTail();
            card.flip();
            openedCardStock.add(card, 0, 0);

            stockPile.remove(card);
            Card addedAgain = new Card(card.getRank(), card.getSuit(), this);
            stockPile.insertFirst(addedAgain);
        }
    }

    private void initStock() {
        for (int i = 0; i < 4; i++) {
            for (int j = 1; j <= 13; j++) {
                Suit suit = null;

                if (i == 0) {
                    suit = Card.Suit.DIAMOND;
                }
                if (i == 1) {
                    suit = Card.Suit.HEART;
                }
                if (i == 2) {
                    suit = Card.Suit.SPADE;
                }
                if (i == 3) {
                    suit = Card.Suit.CLUB;
                }

                Card card = new Card(j, suit, this);
                stockPile.insertFirst(card);
            }
        }

        stockPile.shuffle();
    }

    public boolean controlEvent(MouseEvent me) {
        if (gamePanel.getComponentAt(me.getPoint()).equals(centerPanel)) {
            if (centerPanel.getComponentAt(me.getPoint()) instanceof Tableau) {
                Tableau pile = (Tableau) me.getComponent();
                return toTableau(pile, (Card) me.getComponent());
            }
        } else {
            if (topPanel.getComponentAt(me.getPoint()) instanceof Foundation) {
                Foundation pile = (Foundation) me.getComponent();
                return toFoundation(pile, (Card) me.getComponent());
            }
        }

        return false;
    }

    private void setCardCount() {
        numOfCards = 0;
        for (int i = 0; i < foundationsPanel.getComponentCount(); i++) {
            if (foundationsPanel.getComponent(i) instanceof Foundation) {
                Foundation f = (Foundation) foundationsPanel.getComponent(i);
                numOfCards += f.getListSize();
            }
        }

        cardCount.setText(Integer.toString(numOfCards));
    }

    private boolean toTableau(Pile pile, Card card) {
        if (pile != null) {
            if (pile.isEmpty()) {
                return pile.isBaseCard(card);
            }

            Card temp = pile.getHead();
            if (temp.isRed() != card.isRed() && temp.getRank() == card.getRank() + 1) {
                pile.insertAll(card);
                return true;
            }
        }

        return false;
    }

    private boolean toFoundation(Pile pile, Card card) {
        if (pile != null) {
            if (pile.isEmpty()) {
                return pile.isBaseCard(card);
            }

            Card temp = pile.getHead();
            if (temp.suitEquals(card) && temp.getRank() == card.getRank() + 1) {
                pile.insertLast(card);
                return true;
            }
        }

        setCardCount();
        if (numOfCards == 0) {
            int option = JOptionPane.showConfirmDialog(
                    null,
                    "Do you want to play again?",
                    "Choose",
                    JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                gameStart();
            } else {
                setVisible(false);
            }
        }

        return false;
    }

    private static void gameStart() {
        Solitaire s = new Solitaire();
        s.setTitle("Solitaire");
        s.setSize(1000, 750);
        s.setLocationRelativeTo(null);
        s.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        s.setResizable(false);
        s.setVisible(true);
    }

    public static void main(String[] args) {
        gameStart();
    }
}
