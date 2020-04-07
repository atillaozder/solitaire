package solitaire;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;

public class Card extends JComponent {

    public static enum Suit {

        DIAMOND, SPADE, HEART, CLUB;

        @Override
        public String toString() {
            if (this.equals(DIAMOND)) {
                return "diamond";
            }

            if (this.equals(SPADE)) {
                return "spade";
            }

            if (this.equals(HEART)) {
                return "heart";
            }

            if (this.equals(CLUB)) {
                return "club";
            }

            return "";
        }

    }

    static final File dir = new File(System.getProperty("user.dir") + "/images/cards");
    public static int width = 100;
    public static int height = 150;

    private int screenX = 0;
    private int screenY = 0;
    private int locX = 0;
    private int locY = 0;
    int yOffset;

    private static Dimension cardSize;
    private int rank;
    private Suit suit;
    private boolean faceUp = false;
    private BufferedImage img;

    private Card next;
    private Card prev;

    private Solitaire game;

    public Card(int rank, Suit suit, Solitaire game) {
        this.rank = rank;
        this.suit = suit;
        this.game = game;
        setImgView();
        setOpaque(false);

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (next == null) {
                    if (!isFaceUp()) {
                        flip();
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent me) {
                if (isFaceUp()) {
                    screenX = me.getXOnScreen();
                    screenY = me.getYOnScreen();

                    locX = getX();
                    locY = getY();

                    // MARK: - To Move Selected Card via other Components... 
                    // MARK: - However, the starting point is different and after releasing it is not located in initial position.
                    
                    //game.getLayeredPane().setLayout(null);
                    //game.getLayeredPane().add(me.getComponent(), JLayeredPane.DRAG_LAYER);
                    //game.repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                if (!isFaceUp()) {
                    return;
                }

                Point newLoc = new Point();

                if (game.controlEvent(me)) {
                    newLoc.x = me.getXOnScreen();
                    newLoc.y = me.getYOnScreen();
                } else {
                    newLoc.x = locX;
                    newLoc.y = locY;
                }

                setLocation(newLoc);
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        });

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent me) {
                if (isFaceUp()) {
                    int deltaX = me.getXOnScreen() - screenX;
                    int deltaY = me.getYOnScreen() - screenY;
                    setLocation(locX + deltaX, locY + deltaY);
                }
            }

            @Override
            public void mouseMoved(MouseEvent me) {
            }
        });
    }

    public int getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Card getNext() {
        return next;
    }

    public Card getPrev() {
        return prev;
    }

    public boolean isFaceUp() {
        return faceUp;
    }

    public void setNext(Card next) {
        this.next = next;
    }

    public void setPrev(Card prev) {
        this.prev = prev;
    }

    private void setImgView() {
        try {
            URL url = new URL(dir.toURI().toURL(), getURL());
            if (!isFaceUp()) {
                url = new URL(dir.toURI().toURL(), "background.png");
            }

            img = ImageIO.read(url);
            width = img.getWidth();
            height = img.getHeight();
            cardSize = new Dimension(width, height);

            setPreferredSize(cardSize);
            setSize(cardSize);
            setOpaque(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isRed() {
        if (this.suit.equals(Suit.DIAMOND) || this.suit.equals(Suit.HEART)) {
            return true;
        }
        return false;
    }

    public boolean suitEquals(Card c) {
        if (this.suit.equals(c.getSuit())) {
            return true;
        }
        return false;
    }

    public void flip() {
        faceUp = !faceUp;
        this.setImgView();
    }

    private String rankStr() {
        if (this.rank == 11) {
            return "J";
        }
        if (this.rank == 12) {
            return "Q";
        }
        if (this.rank == 13) {
            return "K";
        }
        if (this.rank == 1) {
            return "A";
        }
        return Integer.toString(this.rank);
    }

    private String getURL() {
        return rankStr().toLowerCase() + "_of_" + this.suit.toString() + ".png";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Card) {
            Card c = (Card) o;
            return this.suit.equals(c.suit) && this.rank == c.rank;
        }
        return false;
    }

    @Override
    public String toString() {
        return suit + ", " + rank + ", " + faceUp;
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        grphcs.drawImage(img, 0, 0, width, height, this);
    }
}
