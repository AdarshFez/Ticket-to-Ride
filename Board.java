import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.Stack;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Collections;
import java.util.Random;

/**
 * This represents how the game functions as a whole
 *
 * @author Bradley, Noah, Rajshree, Fez, Dan, David
 * @version 4/29/18
 */
public class Board extends JPanel 
implements MouseListener, MouseMotionListener
{
    private ArrayList<Player> playerlist;
    TrainDeck deck;
    DestDeck destDeck; 
    int curPlays;

    ArrayList<Colors> colors;
    Image image;
    Image background, intro,playerbg;
    Image beginningImage;
    Image redMeep,blackMeep,
    greenMeep,yellowMeep,
    blueMeep, whiteMeep,
    shortDest,longDest,viewDest;

    Image yellowTrain, blueTrain, greenTrain, redTrain,
    purpleTrain, blackTrain, orangeTrain, 
    whiteTrain, locoTrain, destButt;

    int arrayCount;
    int imageWidth, imageHeight;
    String temp;
    Vertex tempVertex;
    boolean hover;
    int xHov,yHov;
    boolean startClick;
    boolean firstCity, secondCity;
    boolean routeCapture;
    //MusicLoopPlayer loop;
    static JButton start = new JButton();
    static JFrame f = new JFrame();
    TrainCard[] tableCards;

    Vertex firstClick = null;
    Vertex secondClick = null;

    PointsAround[] userPoints;

    static Vertex[] vertices;
    Toolkit toolkit;
    Image back;

    Player currentPlayer;
    boolean done;
    int claims;
    int locCard;
    int tCards;
    int playerIndex;
    int firstTurn;
    ArrayList<TrainCard> discard;
    int endTemp;
    DestDeck dest;
    boolean clickDest=false;

    //used for destturn
    int shortDestCount = 0;
    int longDestCount = 0;
    int totalDestCount = 0;
    DestCard destTemp[] = new DestCard[4];
    int press = 0;
    int counter = 0;
    Player player = null;

    boolean mustClaim = false;
    boolean firstTrain = false;
    /**
     * This constructor esablishes the default settings for
     * the board at the start
     */
    public Board() {

        dest = new DestDeck();

        toolkit = Toolkit.getDefaultToolkit();
        playerIndex = 0;
        endTemp = -1;
        hover = false;
        tempVertex = null;
        temp = "";
        vertices = new Vertex[40];
        playerlist = new ArrayList<Player>();
        colors = new ArrayList<Colors>();
        startClick = false;
        firstTurn = 0;
        start.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    introS(e);
                    colors.add(Colors.RED);
                    colors.add(Colors.WHITE);
                    colors.add(Colors.YELLOW);
                    colors.add(Colors.PURPLE);
                    colors.add(Colors.BLACK);
                    colors.add(Colors.GREEN);

                    f.getContentPane().remove(start);
                    String[] choices = { "2", "3", "4", "5" };
                    JComboBox<String> cb =
                        new JComboBox<String>(choices);

                    int s = JOptionPane.showOptionDialog
                        (new JFrame(), cb, "Number of Players"
                        , JOptionPane.YES_NO_OPTION,
                            JOptionPane.DEFAULT_OPTION, 
                            null, new Object[] 
                            { "OK", "Close" }, JOptionPane.CLOSED_OPTION);

                    if (s != 0) {
                        System.exit(s);
                    }

                    JOptionPane.showMessageDialog(null, 
                        "Please make sure the first"
                        +" person is the most experienced",
                        "Message", JOptionPane.ERROR_MESSAGE);
                    String level = (String) cb.getSelectedItem();
                    playerlist = new ArrayList<Player>
                    (Integer.parseInt(level));

                    String start = "";
                    for (int i = 0; i < Integer.parseInt(level); i++) {
                        playerlist.add
                        (new Player
                            (pickPlayersName(), pickPlayersColor()));
                        //currentPlayer = playerlist.get(i);

                    }
                    // }
                    currentPlayer = playerlist.get(0);

                    repaint();

                }
            });

        start.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        start.setOpaque(true);

        start.setContentAreaFilled(false);
        start.setBorderPainted(false);

        deck = new TrainDeck();
        destDeck = new DestDeck();
        discard = new ArrayList<>();
        loadImage();
        addMouseListener(this);
        addMouseMotionListener(this);
        imageWidth = image.getWidth(this);
        imageHeight = image.getHeight(this);
        //loop = new MusicLoopPlayer();
        //loop.intro();
        //loadBeg();

        //randomly sets the front facing cards at first
        tableCards = new TrainCard[5];
        for(int i = 0; i < 5; i ++)
        {
            deck.shuffle();
            tableCards[i] = deck.getCard();

        }
        //initializes the array for the points that go around the board
        userPoints = new PointsAround[101];
        int xoff= 24;
        int yO = -1;
        for(int i = 0; i < 20; i++){
            userPoints[i] = new PointsAround(xoff,yO);
            xoff+=24;
        }
        xoff-=24;
        yO += 29;

        for(int j= 20; j < 49; j++){
            userPoints[j] = new PointsAround(xoff,yO);
            yO+=24;
        }
        yO += 5;

        for(int k= 49; k < 70; k++){
            userPoints[k] = new PointsAround(xoff,yO);
            xoff-=24;
        }

        xoff+=24;
        yO -= 29;

        for(int l= 70; l < 100; l++){
            userPoints[l] = new PointsAround(xoff,yO);
            yO-=24;
        }

        claims = 0;
        locCard = 0;
        tCards = 0;
    }

    /**
     * Initalzies starting choices for dest
     * cards
     */
    public void destinationStart(){
        DestCard[] available = new DestCard[4];

        available[0] = (dest.shortDestDeck.get(0));
        available[1] = (dest.shortDestDeck.get(1));
        available[2] = (dest.longDestDeck.get(0));
        available[3] = (dest.longDestDeck.get(1));
        dest.destTurn(available[0],available[1]
        ,available[2],available[3], currentPlayer, 0);
    }

    /**
     * Main runnable for the game, runs method that starts the
     * system up
     * 
     * @param args, none needed
     */
    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    createAndShowGUI();
                }
            });
    }

    /**
     * Creates Gui and starts up JPanel for use
     */
    protected static void createAndShowGUI() {
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        start.setBounds(400,550,260,83);

        f.add(start);
        f.setTitle("Ticket to Ride: Germany");
        Board b = new Board();
        setBoard();
        setMeeples();
        f.getContentPane().add(b);
        f.setResizable(false);
        f.setSize(1000,950);

        f.setVisible(true);
    }

    /**
     * Loads main imaghes into the game, find them in the images folder
     * in main folder
     */
    private void loadImage() {
        back = toolkit.getImage("images/TrainCardBack.jpg");
        intro = toolkit.getImage("images/intro.png");
        image = toolkit.getImage("images/board.jpg");
        background = toolkit.getImage("images/background.jpg");
        playerbg = toolkit.getImage("images/playerbg.jpg");
        redMeep = toolkit.getImage("images/redMeeple.png");
        blackMeep = toolkit.getImage("images/blackMeeple.png");
        greenMeep = toolkit.getImage("images/greenMeeple.png");
        yellowMeep = toolkit.getImage("images/yellowMeeple.png");
        blueMeep = toolkit.getImage("images/blueMeeple.png");
        whiteMeep = toolkit.getImage("images/whiteMeeple.png");

        yellowTrain = toolkit.getImage("images/YellowCard.JPG");
        blueTrain = toolkit.getImage("images/BlueCard.JPG");
        greenTrain = toolkit.getImage("images/GreenCard.JPG");
        purpleTrain = toolkit.getImage("images/PurpleCard.JPG"); 
        orangeTrain = toolkit.getImage("images/OrangeCard.JPG");
        blackTrain = toolkit.getImage("images/BlackCard.JPG");
        whiteTrain = toolkit.getImage("images/WhiteCard.JPG");
        locoTrain = toolkit.getImage("images/RainbowCard.JPG");
        redTrain = toolkit.getImage("images/RedCard.JPG");

        shortDest = toolkit.getImage("images/BlueDest.JPG");
        longDest = toolkit.getImage("images/OrangeDest.JPG");
        viewDest = toolkit.getImage("images/dest.png");

    }

    /**
     * Allows for the game to start on the intro screen
     * @param e, a mouse click on the start button
     */
    public void introS(ActionEvent e){
        //loop.stop();
        //loop.music();
        startClick = true;

        repaint();
    }

    /**
     * Creates a JOptionPane to allow a player to input a name
     * 
     * @param The players name
     */
    public String pickPlayersName()
    {
        String s = JOptionPane.showInputDialog
            (new JFrame(), "Please Enter a player's Name");
        while (s.isEmpty() || s.length() > 10) {
            JOptionPane.showMessageDialog
            (null, "Player's name can not"
                +" be empty or exceed 10 characters");
            s = JOptionPane.showInputDialog
            (new JFrame(), "Please re-enter a player's name");
        }
        for (Player pl : playerlist) {
            String playerName = pl.getName();
            while (playerName.equalsIgnoreCase(s)) {
                JOptionPane.showMessageDialog
                (null, "The selected name already exists");
                s = JOptionPane.showInputDialog
                (new JFrame(), "Please enter a different name");
            }
        }
        return s;
    }

    /**
     * Creates a JComboBox to choose the players color
     * 
     * @return players color
     */
    public Colors pickPlayersColor()
    {        
        JComboBox<Colors> cb = new JComboBox<Colors>();

        cb.setModel(new DefaultComboBoxModel(colors.toArray()));

        int s = JOptionPane.showOptionDialog(new JFrame(), cb, 
                "Please Select a Color", JOptionPane.YES_NO_OPTION,
                JOptionPane.DEFAULT_OPTION, null, new Object[] 
                { "OK", "Close" }, JOptionPane.CLOSED_OPTION);

        if (s != 0) {
            System.exit(s);
        }

        Colors color = (Colors) cb.getSelectedItem();

        for(int i =0; i < colors.size(); i++){
            if(colors.get(i).equals(color)){
                colors.remove(i);   
            }
        }

        return color;
    } 

    /**
     * draws graphics onto the board and handles most 
     * changes to board in the game
     * 
     * @param g the internal graphics being adjusted
     */
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if(!startClick){
            g2.drawImage(intro,0,0,getWidth(),getHeight(),this);
        }
        else{
            // draws the board
            int w = getWidth();
            int h = getHeight();

            int x = (w - imageWidth) / 2;
            int y = (h - imageHeight) / 2;
            g2.drawImage(background,0,0,getWidth(),getHeight(),this);
            g2.drawImage(playerbg,539,634,500,300,this);

            g2.drawImage(image, 0, 0, this);

            g2.drawImage(redMeep,690,29,60,60,this);
            g2.drawImage(yellowMeep,690,93,60,60,this);
            g2.drawImage(greenMeep,690,157,60,60,this);
            g2.drawImage(blackMeep,690,221,60,60,this);
            g2.drawImage(blueMeep,690,285,60,60,this);
            g2.drawImage(whiteMeep,690,349,60,60,this);

            g2.drawImage(yellowTrain,579,667,100,60,this);
            g2.drawImage(blueTrain,720,667,100,60,this);
            g2.drawImage(greenTrain,850,667,100,60,this);
            g2.drawImage(purpleTrain,579,740,100,60,this);
            g2.drawImage(redTrain,720,740,100,60,this);
            g2.drawImage(blackTrain,850,740,100,60,this);
            g2.drawImage(orangeTrain,579,820,100,60,this);
            g2.drawImage(whiteTrain,720,820,100,60,this);
            g2.drawImage(locoTrain,850,820,100,60,this);

            g2.drawImage(tableCards[0].getImage(), 550,45,this);
            g2.drawImage(tableCards[1].getImage(), 550,125,this);
            g2.drawImage(tableCards[2].getImage(), 550,205,this);
            g2.drawImage(tableCards[3].getImage(), 550,285,this);
            g2.drawImage(tableCards[4].getImage(), 550,365,this);

            g2.drawImage(back,550,460,this);
            g2.drawImage(shortDest, 710, 460, this);
            g2.drawImage(longDest, 850, 460, this);
            g2.drawImage(viewDest, 710, 560,this );

            g2.drawImage(playerbg, 753,5,300,440,this);
            if(currentPlayer != null)
            {
                ArrayList<DestCard> fixDuplicates = new ArrayList<>();
                for(DestCard d: currentPlayer.destination)
                {
                    if(!fixDuplicates.contains(d))
                    {
                        fixDuplicates.add(d);
                    }
                }
                currentPlayer.destination.clear();
                for(DestCard d: fixDuplicates)
                {
                    currentPlayer.destination.add(d);
                }
            }
            if(deck.deck.size() <= 7)
            {
                deck.deck.addAll(discard);
                discard.clear();
                if(discard.size() == 0)
                {
                    mustClaim = true;
                }
            }
            else
            {
                mustClaim = false;
            }

            int yoff = 40;
            for(Player play: playerlist){
                g2.setColor(play.getColor2());

                if(play.getPoints() > 0 && play.getPoints() <101){
                    g2.fillOval(userPoints[play.getPoints()-1].getX(),
                        userPoints[play.getPoints()-1].getY(),
                        userPoints[play.getPoints()-1].getWidth(),
                        userPoints[play.getPoints()-1].getHeight());
                }
                else if(play.getPoints() > 100){
                    g2.fillOval(userPoints[99].getX(),
                        userPoints[99].getY(),
                        userPoints[99].getWidth(),
                        userPoints[99].getHeight());
                }

                if (play.equals(currentPlayer)) {
                    g2.setStroke(new BasicStroke(3));
                    g2.drawRect(770, yoff - 25, 210, 55);
                }
                g2.setFont(new Font("Arial", Font.BOLD, 18)); 
                g2.drawString(play.getName()+": "
                    + play.getPoints()+ " points",802,yoff);
                g2.drawString("Num Trains: "
                    + play.getTrainCount(), 802, yoff+20);
                yoff+= 80;

                if(firstTurn == 0)
                {
                    g2.setFont(new Font("Arial", Font.BOLD, 26));
                    g2.drawString(
                        playerlist.get(playerIndex).getName()
                        + "'s Turn", 200, 800);

                    firstTurn++;
                }
            }

            for(int i = 0 ; i < vertices.length; i++){
                ArrayList<Edge> aEd = new ArrayList<Edge>();
                aEd = vertices[i].getEdge();

                for(int j = 0 ; j < aEd.size(); j++){
                    if(aEd.get(j).getOwner() != null){

                        g2.setStroke(new BasicStroke(7));
                        g2.setColor(aEd.get(j).getOwner().getColor2());
                        ArrayList<Integer> points
                        = aEd.get(j).getRouteNumbers();

                        if(points.size() < 5){
                            g2.drawLine
                            (points.get(0),points.get(1),
                                points.get(2),points.get(3));
                        }
                        else if(points.size() < 9){

                            g2.drawLine(points.get(0),
                                points.get(1),points.get(2)
                            ,points.get(3));
                            g2.drawLine(points.get(4),
                                points.get(5),points.get(6)
                            ,points.get(7));
                        }
                        else if(points.size() < 13){
                            g2.drawLine(points.get(0),
                                points.get(1),points.get(2)
                            ,points.get(3));
                            g2.drawLine(points.get(4),
                                points.get(5),points.get(6)
                            ,points.get(7));
                            g2.drawLine(points.get(8),
                                points.get(9),points.get(10)
                            ,points.get(11));

                        }
                    }
                }
            }

            if(counter < playerlist.size() && player != currentPlayer){
                destinationStart();
                counter++;
                player = currentPlayer;
            }
            if(firstTurn > 0)
            {
                g2.setFont(new Font("Arial", Font.BOLD, 26));
                g2.drawImage(playerbg,150,770,300,50,this);
                g2.setColor(playerlist.get(playerIndex).getColor2());
                g2.drawString(playerlist.get
                    (playerIndex).getName() + "'s Turn", 160, 800);

                g2.setColor(Color.BLACK);
                int num = currentPlayer.totalRedMeep();
                g2.drawString(""+num, 721,66);
                num = currentPlayer.totalYellowMeep();
                g2.drawString(""+num, 715,129);
                num = currentPlayer.totalGreenMeep();
                g2.drawString(""+num, 717,195);
                num = currentPlayer.totalBlackMeep();
                g2.drawString(""+num, 718,255);
                num = currentPlayer.totalBlueMeep();
                g2.drawString(""+num, 718,321);
                num = currentPlayer.totalWhiteMeep();
                g2.setColor(Color.BLACK);
                g2.drawString(""+num, 715,390);

                g2.drawImage(yellowTrain,579,667,100,60,this);
                g2.drawImage(blueTrain,720,667,100,60,this);
                g2.drawImage(greenTrain,850,667,100,60,this);
                g2.drawImage(purpleTrain,579,740,100,60,this);
                g2.drawImage(redTrain,720,740,100,60,this);
                g2.drawImage(blackTrain,850,740,100,60,this);
                g2.drawImage(orangeTrain,579,820,100,60,this);
                g2.drawImage(whiteTrain,720,820,100,60,this);
                g2.drawImage(locoTrain,850,820,100,60,this);

                g2.setColor(Color.WHITE);
                g2.fillRect(619,679,30,30);
                g2.fillRect(760,679,30,30);
                g2.fillRect(887,679,30,30); 
                g2.fillRect(618,757,30,30); 
                g2.fillRect(762,757,30,30); 
                g2.fillRect(889,757,30,30); 

                g2.fillRect(614,834,30,30);

                g2.fillRect(887,834,30,30);

                g2.setColor(Color.BLACK);
                g2.fillRect(760,834,30,30);

                num = currentPlayer.getTotalColor(Colors.YELLOW);
                g2.drawString(""+num,625,704);
                num = currentPlayer.getTotalColor(Colors.BLUE);
                g2.drawString(""+num,770,704);
                num = currentPlayer.getTotalColor(Colors.GREEN);
                g2.drawString(""+num,895,704);
                num = currentPlayer.getTotalColor(Colors.PURPLE);
                g2.drawString(""+num,625,780);
                num = currentPlayer.getTotalColor(Colors.RED);
                g2.drawString(""+num,770,780);
                num = currentPlayer.getTotalColor(Colors.BLACK);
                g2.drawString(""+num,895,780);
                num = currentPlayer.getTotalColor(Colors.ORANGE);
                g2.drawString(""+num,625,860);

                num = currentPlayer.getTotalColor(Colors.LOCO);
                g2.drawString(""+num,895,860);
                g2.setColor(Color.WHITE);
                num = currentPlayer.getTotalColor(Colors.WHITE);
                g2.drawString(""+num,770,860);

                g2.setFont(new Font("Lucida", Font.PLAIN, 13));
                g2.setStroke(new BasicStroke(2));
                g2.setColor(Color.BLACK);

                g2.drawRect(619,679,30,30);
                g2.drawRect(760,679,30,30);
                g2.drawRect(887,679,30,30); 
                g2.drawRect(618,757,30,30); 
                g2.drawRect(762,757,30,30); 
                g2.drawRect(889,757,30,30); 

                g2.drawRect(614,834,30,30);

                g2.drawRect(887,834,30,30);

                g2.setColor(Color.WHITE);
                g2.drawRect(760,834,30,30);

            }
            if(hover){
                int length = 15 * tempVertex.getNumMeeples();
                g2.setFont(new Font("Lucida", Font.PLAIN, 13));
                g2.setStroke(new BasicStroke(2));
                g2.setColor(Color.WHITE);
                g2.fillRect(xHov,yHov,150,50 + length);
                g2.setColor(Color.BLACK);
                g2.drawRect(xHov,yHov,150,50 + length);
                g2.drawString(temp,xHov + 30, yHov + 15);
                hover = false;
                temp = "";
                if(tempVertex != null)
                {
                    int yOffset = 45;
                    for(Meeple m : tempVertex.meeples)
                    {
                        g2.drawString(m.getColor() 
                            + "", xHov + 30,yHov+yOffset);
                        yOffset += 15;
                    }
                }
            }

        }
        //garbage collection
        System.gc();
    }

    /**
     * Adds all cities to the board in their respective spots,
     * and adds all necesarry features to them
     */
    public static void setBoard()
    {
        ArrayList<Integer> list = new ArrayList<Integer>();
        vertices[0] = new Vertex(Cities.DENMARK,"Denmark",191,240,11,60);

        list.add(199);
        list.add(32);
        list.add(178);
        list.add(53);
        list.add(178);
        list.add(53);
        list.add(162);
        list.add(95);
        list.add(162);
        list.add(95);
        list.add(165);
        list.add(143);
        vertices[0].addEdge(
            new Edge(Colors.GREEN,5,0,2,list));
        list.clear();

        list.add(244);
        list.add(73);
        list.add(255);
        list.add(47);
        list.add(255);
        list.add(47);
        list.add(234);
        list.add(35);
        vertices[0].addEdge(
            new Edge(Colors.GRAY,2,0,3,list));
        list.clear();

        vertices[1] =
        new Vertex(Cities.EMDEN,"Emden",74,94,159,179);
        list.add(87);
        list.add(158);
        list.add(121);
        list.add(138);
        list.add(121);
        list.add(138);
        list.add(154);
        list.add(146);
        vertices[1].addEdge(
            new Edge(Colors.GRAY,3,1,2,list));
        list.clear();

        list.add(96);
        list.add(174);
        list.add(160);
        list.add(197);
        vertices[1].addEdge(
            new Edge(Colors.BLUE,3,1,7,list));
        list.clear();

        list.add(86);
        list.add(183);
        list.add(110);
        list.add(278);
        vertices[1].addEdge(
            new Edge(Colors.RED,4,1,9,list));
        list.clear();

        list.add(40);
        list.add(218);
        list.add(72);
        list.add(180);
        vertices[1].addEdge(
            new Edge(Colors.WHITE,2,1,8,list));
        list.clear();

        vertices[2] = new Vertex(
            Cities.BREMERHAVEN,"Bremerhaven",155,175,146,166);
        list.add(87);
        list.add(158);
        list.add(121);
        list.add(138);
        list.add(121);
        list.add(138);
        list.add(154);
        list.add(146);
        vertices[2].addEdge(
            new Edge(Colors.GRAY,3,2,1,list));
        list.clear();

        list.add(199);
        list.add(32);
        list.add(178);
        list.add(53);
        list.add(178);
        list.add(53);
        list.add(162);
        list.add(95);
        list.add(162);
        list.add(95);
        list.add(165);
        list.add(143);
        vertices[2].addEdge(
            new Edge(Colors.GREEN,5,2,0,list));
        list.clear();

        list.add(170);
        list.add(144);
        list.add(229);
        list.add(93);
        vertices[2].addEdge(
            new Edge(Colors.GRAY,3,2,3,list));
        list.clear();

        list.add(178);
        list.add(154);
        list.add(205);
        list.add(128);
        list.add(205);
        list.add(128);
        list.add(242);
        list.add(151);
        vertices[2].addEdge(
            new Edge(Colors.GRAY,3,2,5,list));
        list.clear();

        list.add(167);
        list.add(168);
        list.add(168);
        list.add(189);
        vertices[2].addEdge(
            new Edge(Colors.WHITE,1,2,7,list));
        list.clear();

        list.add(170);
        list.add(144);
        list.add(229);
        list.add(93);
        vertices[3] = new Vertex(
            Cities.KIEL,"Kiel",233,253,78,98);
        vertices[3].addEdge(
            new Edge(Colors.GRAY,3,3,2,list));
        list.clear();

        list.add(244);
        list.add(73);
        list.add(255);
        list.add(47);
        list.add(255);
        list.add(47);
        list.add(234);
        list.add(35);
        vertices[3].addEdge(
            new Edge(Colors.GRAY,2,3,0,list));
        list.clear();

        list.add(252);
        list.add(85);
        list.add(301);
        list.add(73);
        list.add(301);
        list.add(73);
        list.add(353);
        list.add(85);
        vertices[3].addEdge(
            new Edge(Colors.ORANGE,4,3,4,list));
        list.clear();

        list.add(255);
        list.add(94);
        list.add(296);
        list.add(115);
        list.add(296);
        list.add(115);
        list.add(314);
        list.add(138);
        vertices[3].addEdge(
            new Edge(Colors.YELLOW,3,3,6,list));
        list.clear();

        list.add(246);
        list.add(101);
        list.add(256);
        list.add(147);
        vertices[3].addEdge(
            new Edge(Colors.PURPLE,2,3,5,list));
        list.clear();

        list.add(237);
        list.add(101);
        list.add(246);
        list.add(148);
        vertices[3].addEdge(
            new Edge(Colors.BLACK,2,3,5,list));
        list.clear();

        vertices[4] = new Vertex(
            Cities.ROSTOCK,"Rostock",350,370,81,101);
        list.add(252);
        list.add(85);
        list.add(301);
        list.add(73);
        list.add(301);
        list.add(73);
        list.add(353);
        list.add(85);
        vertices[4].addEdge(
            new Edge(Colors.ORANGE,4,4,3,list));
        list.clear();

        list.add(326);
        list.add(140);
        list.add(356);
        list.add(101);
        vertices[4].addEdge(
            new Edge(Colors.RED,2,4,6,list));
        list.clear();

        list.add(369);
        list.add(96);
        list.add(420);
        list.add(150);
        list.add(420);
        list.add(150);
        list.add(434);
        list.add(233);
        vertices[4].addEdge(
            new Edge(Colors.PURPLE,6,4,12,list));
        list.clear();

        vertices[5] = new Vertex(
            Cities.HAMBURG,"Hamburg",244,264,148,168);
        list.add(178);
        list.add(154);
        list.add(205);
        list.add(128);
        list.add(205);
        list.add(128);
        list.add(242);
        list.add(151);
        vertices[5].addEdge(
            new Edge(Colors.GRAY,3,5,2,list));
        list.clear();

        list.add(237);
        list.add(101);
        list.add(246);
        list.add(148);
        vertices[5].addEdge(
            new Edge(Colors.BLACK,2,5,3,list));
        list.clear();

        list.add(246);
        list.add(101);
        list.add(256);
        list.add(147);
        vertices[5].addEdge(
            new Edge(Colors.PURPLE,2,5,3,list));
        list.clear();

        list.add(260);
        list.add(149);
        list.add(285);
        list.add(136);
        list.add(285);
        list.add(136);
        list.add(311);
        list.add(146);
        vertices[5].addEdge(
            new Edge(Colors.GREEN,2,5,6,list));
        list.clear();

        list.add(267);
        list.add(158);
        list.add(423);
        list.add(238);
        vertices[5].addEdge
        (new Edge(Colors.BLUE,7,5,12,list));
        list.clear();

        list.add(263);
        list.add(165);
        list.add(415);
        list.add(247);
        vertices[5].addEdge
        (new Edge(Colors.YELLOW,7,5,12,list));
        list.clear();

        list.add(232);
        list.add(261);
        list.add(253);
        list.add(169);
        vertices[5].addEdge
        (new Edge(Colors.WHITE,4,5,10,list));
        list.clear();

        list.add(246);
        list.add(165);
        list.add(223);
        list.add(259);
        vertices[5].addEdge(
            new Edge(Colors.RED,4,5,10,list));
        list.clear();

        list.add(178);
        list.add(200);
        list.add(227);
        list.add(192);
        list.add(231);
        list.add(186);
        list.add(238);
        list.add(160);
        vertices[5].addEdge(
            new Edge(Colors.ORANGE,3,5,7,list));
        list.clear();

        vertices[6] = new Vertex(
            Cities.SCHWERIN,"Schwerin",310,330,138,158);

        list.add(260);
        list.add(149);
        list.add(285);
        list.add(136);
        list.add(285);
        list.add(136);
        list.add(311);
        list.add(146);
        vertices[6].addEdge(
            new Edge(Colors.GREEN,2,6,5,list));
        list.clear();

        list.add(255);
        list.add(94);
        list.add(296);
        list.add(115);
        list.add(296);
        list.add(115);
        list.add(314);
        list.add(138);
        vertices[6].addEdge(
            new Edge(Colors.YELLOW,3,6,3,list));
        list.clear();

        list.add(326);
        list.add(140);
        list.add(356);
        list.add(101);
        vertices[6].addEdge(
            new Edge(Colors.RED,2,6,4,list));
        list.clear();

        list.add(332);
        list.add(150);
        list.add(377);
        list.add(173);
        list.add(377);
        list.add(173);
        list.add(411);
        list.add(207);
        list.add(411);
        list.add(207);
        list.add(425);
        list.add(232);
        vertices[6].addEdge(
            new Edge(Colors.WHITE,5,6,12,list));
        list.clear();

        vertices[7] = new Vertex(
            Cities.BREMEN,"Bremen",159,179,190,210);
        list.add(167);
        list.add(168);
        list.add(168);
        list.add(189);
        vertices[7].addEdge(
            new Edge(Colors.WHITE,1,7,2,list));
        list.clear();

        list.add(178);
        list.add(200);
        list.add(227);
        list.add(192);
        list.add(231);
        list.add(186);
        list.add(238);
        list.add(160);
        vertices[7].addEdge(
            new Edge(Colors.ORANGE,3,7,5,list));
        list.clear();

        list.add(172);
        list.add(212);
        list.add(190);
        list.add(257);
        list.add(190);
        list.add(257);
        list.add(218);
        list.add(260);
        vertices[7].addEdge(
            new Edge(Colors.YELLOW,3,7,10,list));
        list.clear();

        list.add(120);
        list.add(273);
        list.add(162);
        list.add(212);
        vertices[7].addEdge(
            new Edge(Colors.BLACK,3,7,9,list));
        list.clear();

        list.add(96);
        list.add(174);
        list.add(160);
        list.add(197);
        vertices[7].addEdge(
            new Edge(Colors.BLUE,3,7,1,list));
        list.clear();

        vertices[8] = new Vertex(
            Cities.LUXEMBORG,"Luxemborg",14,67,214,265);
        list.add(40);
        list.add(218);
        list.add(72);
        list.add(180);
        vertices[8].addEdge(
            new Edge(Colors.WHITE,2,8,1,list));
        list.clear();

        list.add(99);
        list.add(284);
        list.add(54);
        list.add(267);
        vertices[8].addEdge(
            new Edge(Colors.ORANGE,2,8,9,list));
        list.clear();

        list.add(31);
        list.add( 275);
        list.add(56);
        list.add(343);
        vertices[8].addEdge(
            new Edge(Colors.PURPLE,3,8,13,list));
        list.clear();

        vertices[9] = new Vertex(
            Cities.MUNSTER,"Munster",97,117,277,297);

        list.add(99);
        list.add(284);
        list.add(54);
        list.add(267);
        vertices[9].addEdge(
            new Edge(Colors.ORANGE,2,9,8,list));
        list.clear();

        list.add(86);
        list.add(183);
        list.add(110);
        list.add(278);
        vertices[9].addEdge(
            new Edge(Colors.RED,4,9,1,list));
        list.clear();

        list.add(120);
        list.add(273);
        list.add(162);
        list.add(212);
        vertices[9].addEdge(
            new Edge(Colors.BLACK,3,9,7,list));
        list.clear();

        list.add(118);
        list.add(288);
        list.add(144);
        list.add(295);
        list.add(144);
        list.add(295);
        list.add(190);
        list.add(273);
        list.add(190);
        list.add(273);
        list.add(215);
        list.add(270);
        vertices[9].addEdge(
            new Edge(Colors.PURPLE,4,9,10,list));
        list.clear();

        list.add(103);
        list.add(300);
        list.add(96);
        list.add(323);
        list.add(111);
        list.add(301);
        list.add(108);
        list.add(324);
        vertices[9].addEdge(
            new Edge(Colors.GRAY,1,9,14,list));
        list.clear();

        vertices[10] = new Vertex(
            Cities.HANNOVER,"Hannover",216,236,259,279);

        list.add(118);
        list.add(288);
        list.add(144);
        list.add(295);
        list.add(144);
        list.add(295);
        list.add(190);
        list.add(273);
        list.add(190);
        list.add(273);
        list.add(215);
        list.add(270);
        vertices[10].addEdge(
            new Edge(Colors.PURPLE,4,10,9,list));
        list.clear();

        list.add(172);
        list.add(212);
        list.add(190);
        list.add(257);
        list.add(190);
        list.add(257);
        list.add(218);
        list.add(260);
        vertices[10].addEdge(
            new Edge(Colors.YELLOW,3,10,7,list));
        list.clear();

        list.add(246);
        list.add(165);
        list.add(223);
        list.add(259);
        vertices[10].addEdge(
            new Edge(Colors.RED,4,10,5,list));
        list.clear();

        list.add(232);
        list.add(261);
        list.add(253);
        list.add(169);
        vertices[10].addEdge(
            new Edge(Colors.WHITE,4,10,5,list));
        list.clear();

        list.add(240);
        list.add(262);
        list.add(414);
        list.add(256);
        vertices[10].addEdge(
            new Edge(Colors.BLACK,7,10,12,list));
        list.clear();

        list.add(240);
        list.add(271);
        list.add(285);
        list.add(297);
        list.add(285);
        list.add(297);
        list.add(336);
        list.add(291);
        vertices[10].addEdge(
            new Edge(Colors.BLUE,4,10,11,list));
        list.clear();

        list.add(233);
        list.add(275);
        list.add(297);
        list.add(381);
        vertices[10].addEdge(
            new Edge(Colors.GREEN,5,10,16,list));
        list.clear();

        list.add(224);
        list.add(280);
        list.add(290);
        list.add(385);
        vertices[10].addEdge(
            new Edge(Colors.ORANGE,5,10,16,list));
        list.clear();

        list.add(207);
        list.add(279);
        list.add(207);
        list.add(351);
        list.add(217);
        list.add(279);
        list.add(217);
        list.add(351);
        vertices[10].addEdge(
            new Edge(Colors.GRAY,3,10,15,list));
        list.clear();

        vertices[11] = new Vertex(
            Cities.MAGDEBURG,"Magdeburg",335,355,276,296);
        list.add(240);
        list.add(271);
        list.add(285);
        list.add(297);
        list.add(285);
        list.add(297);
        list.add(336);
        list.add(291);
        vertices[11].addEdge(
            new Edge(Colors.BLUE,4,11,10,list));
        list.clear();

        list.add(355);
        list.add(286);
        list.add(403);
        list.add(278);
        list.add(403);
        list.add(278);
        list.add(423);
        list.add(258);
        vertices[11].addEdge(
            new Edge(Colors.RED,3,11,12,list));
        list.clear();

        list.add(350);
        list.add(294);
        list.add(369);
        list.add(337);
        vertices[11].addEdge(
            new Edge(Colors.YELLOW,2,11,17,list));
        list.clear();

        vertices[12] = new Vertex(
            Cities.BERLIN,"Berlin",421,441,234,254);
        list.add(369);
        list.add(96);
        list.add(420);
        list.add(150);
        list.add(420);
        list.add(150);
        list.add(434);
        list.add(233);
        vertices[12].addEdge(
            new Edge(Colors.PURPLE,6,12,4,list));
        list.clear();

        list.add(332);
        list.add(150);
        list.add(377);
        list.add(173);
        list.add(377);
        list.add(173);
        list.add(411);
        list.add(207);
        list.add(411);
        list.add(207);
        list.add(425);
        list.add(232);
        vertices[12].addEdge(
            new Edge(Colors.WHITE,5,12,6,list));
        list.clear();

        list.add(267);
        list.add(158);
        list.add(423);
        list.add(238);
        vertices[12].addEdge(
            new Edge(Colors.BLUE,7,12,5,list));
        list.clear();

        list.add(263);
        list.add(165);
        list.add(415);
        list.add(247);
        vertices[12].addEdge(
            new Edge(Colors.YELLOW,7,12,5,list));
        list.clear();

        list.add(240);
        list.add(262);
        list.add(414);
        list.add(256);
        vertices[12].addEdge(
            new Edge(Colors.BLACK,7,12,10,list));
        list.clear();

        list.add(355);
        list.add(286);
        list.add(403);
        list.add(278);
        list.add(403);
        list.add(278);
        list.add(423);
        list.add(258);
        vertices[12].addEdge(
            new Edge(Colors.RED,3,12,11,list));
        list.clear();

        list.add(380);
        list.add(339);
        list.add(429);
        list.add(283);
        list.add(429);
        list.add(283);
        list.add(432);
        list.add(253);
        vertices[12].addEdge(
            new Edge(Colors.ORANGE,4,12,17,list));
        list.clear();

        list.add(438);
        list.add(253);
        list.add(460);
        list.add(295);
        list.add(460);
        list.add(295);
        list.add(463);
        list.add(347);
        list.add(463);
        list.add(347);
        list.add(462);
        list.add(371);
        vertices[12].addEdge(
            new Edge(Colors.GREEN,5,12,19,list));
        list.clear();

        vertices[13] = new Vertex(
            Cities.DUSSELDORF,"Dusseldorf",52,72,345,365);

        list.add(31);
        list.add( 275);
        list.add(56);
        list.add(343);
        vertices[13].addEdge(
            new Edge(Colors.PURPLE,3,13,8,list));
        list.clear();

        list.add(65);
        list.add(337);
        list.add(85);
        list.add(326);
        list.add(68);
        list.add(344);
        list.add(91);
        list.add(337);
        list.add(72);
        list.add(357);
        list.add(97);
        list.add(346);
        vertices[13].addEdge(
            new Edge(Colors.GRAY,1,13,14,list));
        list.clear();

        list.add(44);
        list.add(385);
        list.add(44);
        list.add(364);
        list.add(54);
        list.add(385);
        list.add(54);
        list.add(364);
        list.add(64);
        list.add(385);
        list.add(64);
        list.add(364);
        vertices[13].addEdge(
            new Edge(Colors.GRAY,1,13,20,list));
        list.clear();

        vertices[14] = new Vertex(
            Cities.DORTMUND,"Dortmund",93,113,324,344);

        list.add(65);
        list.add(337);
        list.add(85);
        list.add(326);
        list.add(68);
        list.add(344);
        list.add(91);
        list.add(337);
        list.add(72);
        list.add(357);
        list.add(97);
        list.add(346);
        vertices[14].addEdge(
            new Edge(Colors.GRAY,1,14,13,list));
        list.clear();

        list.add(103);
        list.add(300);
        list.add(96);
        list.add(323);
        list.add(111);
        list.add(301);
        list.add(108);
        list.add(324);
        vertices[14].addEdge(
            new Edge(Colors.GRAY,1,14,9,list));
        list.clear();

        list.add(104);
        list.add(343);
        list.add(150);
        list.add(367);
        list.add(150);
        list.add(367);
        list.add(203);
        list.add(357);

        vertices[14].addEdge(
            new Edge(Colors.GREEN,4,14,15,list));

        vertices[15] = new Vertex(
            Cities.KASSEL,"Kassel",208,228,352,372);
        vertices[15].addEdge(new Edge(Colors.GREEN,4,15,14,list));
        list.clear();

        list.add(207);
        list.add(279);
        list.add(207);
        list.add(351);
        list.add(217);
        list.add(279);
        list.add(217);
        list.add(351);
        vertices[15].addEdge(new Edge(Colors.GRAY,3,15,10,list));
        list.clear();

        list.add(222);
        list.add(367);
        list.add(241);
        list.add(397);
        list.add(248);
        list.add(397);
        list.add(286);
        list.add(395);
        vertices[15].addEdge(new Edge(Colors.GRAY,3,15,16,list));
        list.clear();

        list.add(171);
        list.add(457);
        list.add(210);
        list.add(420);
        list.add(210);
        list.add(420);
        list.add(215);
        list.add(372);
        vertices[15].addEdge(new Edge(Colors.WHITE,4,15,22,list));
        list.clear();

        list.add(205);
        list.add(367);
        list.add(199);
        list.add(417);
        list.add(199);
        list.add(417);
        list.add(161);
        list.add(449);
        vertices[15].addEdge(new Edge(Colors.BLUE,4,15,22,list));
        list.clear();

        vertices[16] = new Vertex(
            Cities.ERFURT,"Erfurt",289,309,382,402);

        list.add(222);
        list.add(367);
        list.add(241);
        list.add(397);
        list.add(248);
        list.add(397);
        list.add(286);
        list.add(395);
        vertices[16].addEdge(new Edge(Colors.GRAY,3,16,15,list));
        list.clear();

        list.add(224);
        list.add(280);
        list.add(290);
        list.add(385);
        vertices[16].addEdge(new Edge(Colors.ORANGE,5,16,10,list));
        list.clear();

        list.add(233);
        list.add(275);
        list.add(297);
        list.add(381);
        vertices[16].addEdge(new Edge(Colors.GREEN,5,16,10,list));
        list.clear();

        list.add(307);
        list.add(382);
        list.add(318);
        list.add(357);
        list.add(318);
        list.add(357);
        list.add(336);
        list.add(349);
        list.add(336);
        list.add(349);
        list.add(364);
        list.add(347);
        vertices[16].addEdge(new Edge(Colors.RED,3,16,17,list));
        list.clear();

        list.add(311);
        list.add(393);
        list.add(408);
        list.add(401);
        vertices[16].addEdge(new Edge(Colors.BLACK,4,16,18,list));
        list.clear();

        list.add(312);
        list.add(404);
        list.add(365);
        list.add(453);
        list.add(365);
        list.add(453);
        list.add(384);
        list.add(548);
        vertices[16].addEdge(new Edge(Colors.WHITE,7,16,33,list));
        list.clear();

        list.add(302);
        list.add(400);
        list.add(306);
        list.add(498);
        vertices[16].addEdge(new Edge(Colors.PURPLE,4,16,24,list));
        list.clear();

        list.add(292);
        list.add(402);
        list.add(293);
        list.add(499);
        vertices[16].addEdge(new Edge(Colors.YELLOW,4,16,24,list));
        list.clear();

        vertices[17] = new Vertex(
            Cities.LEIPZIG,"Leipzig",365,385,340,360);
        list.add(307);
        list.add(382);
        list.add(318);
        list.add(357);
        list.add(318);
        list.add(357);
        list.add(336);
        list.add(349);
        list.add(336);
        list.add(349);
        list.add(364);
        list.add(347);
        vertices[17].addEdge(new Edge(Colors.RED,3,17,16,list));
        list.clear();

        list.add(350);
        list.add(294);
        list.add(369);
        list.add(337);
        vertices[17].addEdge(new Edge(Colors.YELLOW,2,17,11,list));
        list.clear();

        list.add(380);
        list.add(339);
        list.add(429);
        list.add(283);
        list.add(429);
        list.add(283);
        list.add(432);
        list.add(253);
        vertices[17].addEdge(new Edge(Colors.ORANGE,4,17,12,list));
        list.clear();

        list.add(386);
        list.add(352);
        list.add(454);
        list.add(374);
        vertices[17].addEdge(new Edge(Colors.BLACK,3,17,19,list));
        list.clear();

        list.add(380);
        list.add(363);
        list.add(413);
        list.add(395);
        vertices[17].addEdge(new Edge(Colors.BLUE,2,17,18,list));
        list.clear();

        vertices[18] = new Vertex(
            Cities.CHEMNITZ,"Chemnitz",410,430,393,413);

        list.add(311);
        list.add(393);
        list.add(408);
        list.add(401);
        vertices[18].addEdge(new Edge(Colors.BLACK,4,18,16,list));
        list.clear();

        list.add(380);
        list.add(363);
        list.add(413);
        list.add(395);
        vertices[18].addEdge(new Edge(Colors.BLUE,2,18,17,list));
        list.clear();

        list.add(428);
        list.add(394);
        list.add(450);
        list.add(386);
        vertices[18].addEdge(new Edge(Colors.YELLOW,1,18,19,list));
        list.clear();

        list.add(393);
        list.add(550);
        list.add(395);
        list.add(431);
        list.add(395);
        list.add(431);
        list.add(394);
        list.add(425);
        list.add(394);
        list.add(425);
        list.add(412);
        list.add(409);
        vertices[18].addEdge(new Edge(Colors.PURPLE,6,18,33,list));
        list.clear();

        vertices[19] = new Vertex(
            Cities.DRESDEN,"Dresden",452,472,374,394);

        list.add(459);
        list.add(396);
        list.add(435);
        list.add(492);
        list.add(435);
        list.add(492);
        list.add(419);
        list.add(537);
        list.add(419);
        list.add(537);
        list.add(404);
        list.add(558);
        vertices[19].addEdge(new Edge(Colors.RED,7,19,33,list));
        list.clear();

        list.add(428);
        list.add(394);
        list.add(450);
        list.add(386);
        vertices[19].addEdge(new Edge(Colors.YELLOW,1,19,18,list));
        list.clear();

        list.add(386);
        list.add(352);
        list.add(454);
        list.add(374);
        vertices[19].addEdge(new Edge(Colors.BLACK,3,19,17,list));
        list.clear();

        list.add(438);
        list.add(253);
        list.add(460);
        list.add(295);
        list.add(460);
        list.add(295);
        list.add(463);
        list.add(347);
        list.add(463);
        list.add(347);
        list.add(462);
        list.add(371);
        vertices[19].addEdge(new Edge(Colors.GREEN,5,19,12,list));
        list.clear();

        vertices[20] = new Vertex(Cities.KOLN,"Koln",48,68,390,410);
        list.add(44);
        list.add(385);
        list.add(44);
        list.add(364);
        list.add(54);
        list.add(385);
        list.add(54);
        list.add(364);
        list.add(64);
        list.add(385);
        list.add(64);
        list.add(364);
        vertices[20].addEdge(new Edge(Colors.GRAY,1,20,13,list));
        list.clear();

        list.add(68);
        list.add(405);
        list.add(151);
        list.add(455);
        list.add(73);
        list.add(396);
        list.add(156);
        list.add(447);
        vertices[20].addEdge(new Edge(Colors.GRAY,4,20,22,list));
        list.clear();

        list.add(54);
        list.add(439);
        list.add(49);
        list.add(417);
        list.add(65);
        list.add(437);
        list.add(60);
        list.add(414);
        vertices[20].addEdge(new Edge(Colors.GRAY,1,20,21,list));

        vertices[21] = new Vertex(
            Cities.KOBLENZ,"Koblenz",58,78,440,460);

        vertices[21].addEdge(new Edge(Colors.GRAY,1,21,20,list));
        list.clear();

        list.add(112);
        list.add(486);
        list.add(73);
        list.add(460);
        list.add(119);
        list.add(479);
        list.add(80);
        list.add(451);
        vertices[21].addEdge(new Edge(Colors.GRAY,2,21,25,list));
        list.clear();

        list.add(50);
        list.add(531);
        list.add(59);
        list.add(460);
        vertices[21].addEdge(new Edge(Colors.GRAY,3,21,26,list));
        list.clear();

        vertices[22] = new Vertex(
            Cities.FRANKFURT,"Frankfurt",155,175,455,475);

        list.add(68);
        list.add(405);
        list.add(151);
        list.add(455);
        list.add(73);
        list.add(396);
        list.add(156);
        list.add(447);
        vertices[22].addEdge(new Edge(Colors.GRAY,4,22,20,list));
        list.clear();

        list.add(205);
        list.add(367);
        list.add(199);
        list.add(417);
        list.add(199);
        list.add(417);
        list.add(161);
        list.add(449);
        vertices[22].addEdge(new Edge(Colors.BLUE,4,22,15,list));
        list.clear();

        list.add(171);
        list.add(457);
        list.add(210);
        list.add(420);
        list.add(210);
        list.add(420);
        list.add(215);
        list.add(372);
        vertices[22].addEdge(new Edge(Colors.WHITE,4,22,15,list));
        list.clear();

        list.add(176);
        list.add(475);
        list.add(220);
        list.add(486);
        list.add(178);
        list.add(463);
        list.add(223);
        list.add(479);
        vertices[22].addEdge(new Edge(Colors.GRAY,2,22,23,list));
        list.clear();

        list.add(152);
        list.add(522);
        list.add(162);
        list.add(476);
        list.add(164);
        list.add(524);
        list.add(172);
        list.add(479);
        vertices[22].addEdge(new Edge(Colors.GRAY,2,22,27,list));
        list.clear();

        list.add(127);
        list.add(477);
        list.add(150);
        list.add(462);
        list.add(135);
        list.add(485);
        list.add(154);
        list.add(473);
        vertices[22].addEdge(new Edge(Colors.GRAY,1,22,25,list));
        list.clear();

        vertices[23] = new Vertex(
            Cities.WURZBURG,"Wurzburg",222,242,477,497);

        list.add(176);
        list.add(475);
        list.add(220);
        list.add(486);
        list.add(178);
        list.add(463);
        list.add(223);
        list.add(479);
        vertices[23].addEdge(new Edge(Colors.GRAY,2,23,22,list));
        list.clear();

        list.add(237);
        list.add(496);
        list.add(285);
        list.add(511);
        list.add(243);
        list.add(489);
        list.add(289);
        list.add(504);
        vertices[23].addEdge(new Edge(Colors.GRAY,2,23,24,list));

        vertices[24] = new Vertex(
            Cities.NURNBERG,"Nurnberg",292,312,500,520);
        vertices[24].addEdge(new Edge(Colors.GRAY,2,24,23,list));
        list.clear();

        list.add(292);
        list.add(402);
        list.add(293);
        list.add(499);
        vertices[24].addEdge(new Edge(Colors.YELLOW,4,24,16,list));
        list.clear();

        list.add(302);
        list.add(400);
        list.add(306);
        list.add(498);
        vertices[24].addEdge(new Edge(Colors.PURPLE,4,24,16,list));
        list.clear();

        list.add(311);
        list.add(504);
        list.add(376);
        list.add(549);
        vertices[24].addEdge(new Edge(Colors.GREEN,3,24,33,list));
        list.clear();

        list.add(310);
        list.add(514);
        list.add(361);
        list.add(626);
        vertices[24].addEdge(new Edge(Colors.BLACK,5,24,32,list));
        list.clear();

        list.add(301);
        list.add(518);
        list.add(349);
        list.add(631);
        vertices[24].addEdge(new Edge(Colors.BLUE,5,24,32,list));
        list.clear();

        list.add(289);
        list.add(517);
        list.add(283);
        list.add(616);
        vertices[24].addEdge(new Edge(Colors.ORANGE,4,24,31,list));
        list.clear();

        vertices[25] = new Vertex(
            Cities.MAINZ,"Mainz",115,135,480,500);

        list.add(123);
        list.add(502);
        list.add(132);
        list.add(521);
        list.add(133);
        list.add(497);
        list.add(144);
        list.add(518);
        vertices[25].addEdge(new Edge(Colors.GRAY,1,25,27,list));
        list.clear();

        list.add(127);
        list.add(477);
        list.add(150);
        list.add(462);
        list.add(135);
        list.add(485);
        list.add(154);
        list.add(473);
        vertices[25].addEdge(new Edge(Colors.GRAY,1,25,22,list));
        list.clear();

        list.add(112);
        list.add(486);
        list.add(73);
        list.add(460);
        list.add(119);
        list.add(479);
        list.add(80);
        list.add(451);
        vertices[25].addEdge(new Edge(Colors.GRAY,2,25,21,list));
        list.clear();

        list.add(55);
        list.add(534);
        list.add(115);
        list.add(496);
        vertices[25].addEdge(new Edge(Colors.GRAY,3,25,26,list));
        list.clear();

        vertices[26] = new Vertex(
            Cities.SAARBRUCKEN,"Saarbrucken",37,57,534,554);

        list.add(50);
        list.add(531);
        list.add(59);
        list.add(460);
        vertices[26].addEdge(new Edge(Colors.GRAY,3,26,21,list));
        list.clear();

        list.add(55);
        list.add(534);
        list.add(115);
        list.add(496);
        vertices[26].addEdge(new Edge(Colors.GRAY,3,26,25,list));
        list.clear();

        list.add(58);
        list.add(545);
        list.add(132);
        list.add(533);
        vertices[26].addEdge(new Edge(Colors.GRAY,3,26,27,list));
        list.clear();

        list.add(55);
        list.add(552);
        list.add(90);
        list.add(571);
        list.add(90);
        list.add(571);
        list.add(125);
        list.add(572);
        vertices[26].addEdge(new Edge(Colors.GRAY,3,26,28,list));
        list.clear();

        list.add(47);
        list.add(555);
        list.add(42);
        list.add(579);
        vertices[26].addEdge(new Edge(Colors.GREEN,1,26,34,list));
        list.clear();

        vertices[27] = new Vertex(
            Cities.MANNHEIM,"Mannheim",133,153,522,542);

        list.add(58);
        list.add(545);
        list.add(132);
        list.add(533);
        vertices[27].addEdge(new Edge(Colors.GRAY,3,27,26,list));
        list.clear();

        list.add(123);
        list.add(502);
        list.add(132);
        list.add(521);
        list.add(133);
        list.add(497);
        list.add(144);
        list.add(518);
        vertices[27].addEdge(new Edge(Colors.GRAY,1,27,25,list));
        list.clear();

        list.add(152);
        list.add(522);
        list.add(162);
        list.add(476);
        list.add(164);
        list.add(524);
        list.add(172);
        list.add(479);
        vertices[27].addEdge(new Edge(Colors.GRAY,2,27,22,list));
        list.clear();

        list.add(151);
        list.add(541);
        list.add(178);
        list.add(577);
        list.add(158);
        list.add(534);
        list.add(188);
        list.add(569);
        vertices[27].addEdge(new Edge(Colors.GRAY,2,27,29,list));
        list.clear();

        list.add(142);
        list.add(546);
        list.add(141);
        list.add(567);
        vertices[27].addEdge(new Edge(Colors.BLUE,1,27,28,list));

        vertices[28] = new Vertex(
            Cities.KARLSRUHE,"Karlsruhe",129,149,564,584);
        vertices[28].addEdge(
            new Edge(Colors.BLUE,1,28,27,list));
        list.clear();

        list.add(148);
        list.add(578);
        list.add(177);
        list.add(585);
        vertices[28].addEdge(new Edge(Colors.PURPLE,1,28,29,list));
        list.clear();

        list.add(115);
        list.add(654);
        list.add(141);
        list.add(585);
        vertices[28].addEdge(new Edge(Colors.WHITE,3,28,35,list));
        list.clear();

        list.add(79);
        list.add(609);
        list.add(130);
        list.add(577);
        vertices[28].addEdge(new Edge(Colors.BLACK,2,28,34,list));
        list.clear();

        list.add(55);
        list.add(552);
        list.add(90);
        list.add(571);
        list.add(90);
        list.add(571);
        list.add(125);
        list.add(572);
        vertices[28].addEdge(new Edge(Colors.GRAY,3,28,26,list));
        list.clear();

        vertices[29] = new Vertex(
            Cities.STUTTGART,"Stuttgart", 178,198,576,596);

        list.add(151);
        list.add(541);
        list.add(178);
        list.add(577);
        list.add(158);
        list.add(534);
        list.add(188);
        list.add(569);
        vertices[29].addEdge(new Edge(Colors.GRAY,2,29,27,list));
        list.clear();

        list.add(198);
        list.add(594);
        list.add(235);
        list.add(623);
        list.add(199);
        list.add(584);
        list.add(241);
        list.add(615);
        vertices[29].addEdge(new Edge(Colors.GRAY,2,29,30,list));
        list.clear();

        list.add(185);
        list.add(599);
        list.add(185);
        list.add(670);
        vertices[29].addEdge(new Edge(Colors.GREEN,3,29,37,list));
        list.clear();

        list.add(127);
        list.add(652);
        list.add(176);
        list.add(597);
        vertices[29].addEdge(new Edge(Colors.GRAY,3,29,35,list));
        list.clear();

        list.add(148);
        list.add(578);
        list.add(177);
        list.add(585);
        vertices[29].addEdge(new Edge(Colors.PURPLE,1,29,28,list));
        list.clear();

        vertices[30] = new Vertex(Cities.ULM,"Ulm",234,254,614,634);

        list.add(198);
        list.add(594);
        list.add(235);
        list.add(623);
        list.add(199);
        list.add(584);
        list.add(241);
        list.add(615);
        vertices[30].addEdge(new Edge(Colors.GRAY,2,30,29,list));
        list.clear();

        list.add(254);
        list.add(620);
        list.add(276);
        list.add(621);
        list.add(252);
        list.add(627);
        list.add(278);
        list.add(631);
        vertices[30].addEdge(new Edge(Colors.GRAY,2,30,31,list));
        list.clear();

        list.add(239);
        list.add(636);
        list.add(228);
        list.add(679);
        vertices[30].addEdge(new Edge(Colors.RED,2,30,38,list));
        list.clear();

        vertices[31] = new Vertex(
            Cities.AUGSBURG,"Augsburg",279,299,619,639);

        list.add(254);
        list.add(620);
        list.add(276);
        list.add(621);
        list.add(252);
        list.add(627);
        list.add(278);
        list.add(631);
        vertices[31].addEdge(new Edge(Colors.GRAY,1,31,30,list));
        list.clear();

        list.add(289);
        list.add(517);
        list.add(283);
        list.add(616);
        vertices[31].addEdge(new Edge(Colors.ORANGE,4,31,24,list));
        list.clear();

        list.add(298);
        list.add(635);
        list.add(342);
        list.add(646);
        list.add(298);
        list.add(626);
        list.add(346);
        list.add(639);
        vertices[31].addEdge(new Edge(Colors.GRAY,2,31,32,list));
        list.clear();

        vertices[32] = new Vertex(
            Cities.MUNCHEN,"Munchen",347,367,634,654);

        list.add(298);
        list.add(635);
        list.add(342);
        list.add(646);
        list.add(298);
        list.add(626);
        list.add(346);
        list.add(639);
        vertices[32].addEdge(new Edge(Colors.GRAY,2,32,31,list));
        list.clear();

        list.add(301);
        list.add(518);
        list.add(349);
        list.add(631);
        vertices[32].addEdge(new Edge(Colors.BLUE,5,32,24,list));
        list.clear();

        list.add(310);
        list.add(514);
        list.add(361);
        list.add(626);
        vertices[32].addEdge(new Edge(Colors.BLACK,5,32,24,list));
        list.clear();

        list.add(366);
        list.add(632);
        list.add(388);
        list.add(624);
        list.add(388);
        list.add(624);
        list.add(399);
        list.add(599);
        list.add(399);
        list.add(599);
        list.add(393);
        list.add(569);
        vertices[32].addEdge(new Edge(Colors.ORANGE,3,32,33,list));
        list.clear();

        list.add(367);
        list.add(652);
        list.add(419);
        list.add(702);
        vertices[32].addEdge(new Edge(Colors.RED,3,32,39,list));
        list.clear();

        list.add(240);
        list.add(685);
        list.add(307);
        list.add(681);
        list.add(307);
        list.add(680);
        list.add(353);
        list.add(652);
        vertices[32].addEdge(new Edge(Colors.GRAY,5,32,38,list));
        list.clear();

        vertices[33] = new Vertex(
            Cities.REGENSBURG,"Regensburg",380,400,552,572);

        list.add(311);
        list.add(504);
        list.add(376);
        list.add(549);
        vertices[33].addEdge(new Edge(Colors.GREEN,3,33,24,list));
        list.clear();

        list.add(312);
        list.add(404);
        list.add(365);
        list.add(453);
        list.add(365);
        list.add(453);
        list.add(384);
        list.add(548);
        vertices[33].addEdge(new Edge(Colors.WHITE,7,33,16,list));
        list.clear();

        list.add(393);
        list.add(550);
        list.add(395);
        list.add(431);
        list.add(395);
        list.add(431);
        list.add(394);
        list.add(425);
        list.add(394);
        list.add(425);
        list.add(412);
        list.add(409);
        vertices[33].addEdge(new Edge(Colors.PURPLE,6,33,18,list));
        list.clear();

        list.add(459);
        list.add(396);
        list.add(435);
        list.add(492);
        list.add(435);
        list.add(492);
        list.add(419);
        list.add(537);
        list.add(419);
        list.add(537);
        list.add(404);
        list.add(558);
        vertices[33].addEdge(new Edge(Colors.RED,7,33,19,list));
        list.clear();

        list.add(402);
        list.add(566);
        list.add(458);
        list.add(646);
        vertices[33].addEdge(new Edge(Colors.YELLOW,4,33,39,list));
        list.clear();

        list.add(366);
        list.add(632);
        list.add(388);
        list.add(624);
        list.add(388);
        list.add(624);
        list.add(399);
        list.add(599);
        list.add(399);
        list.add(599);
        list.add(393);
        list.add(569);
        vertices[33].addEdge(new Edge(Colors.ORANGE,3,33,32,list));
        list.clear();

        vertices[34] = new Vertex(Cities.FRANCE,"France",10,80,580,650);

        list.add(47);
        list.add(555);
        list.add(42);
        list.add(579);
        vertices[34].addEdge(new Edge(Colors.GREEN,1,34,26,list));
        list.clear();

        list.add(79);
        list.add(609);
        list.add(130);
        list.add(577);
        vertices[34].addEdge(new Edge(Colors.BLACK,2,34,28,list));
        list.clear();

        list.add(55);
        list.add(648);
        list.add(104);
        list.add(664);
        vertices[34].addEdge(new Edge(Colors.YELLOW,2,34,35,list));

        vertices[35] = new Vertex(
            Cities.FREIBURG,"Freiburg",104,124,656,676);
        vertices[35].addEdge(new Edge(Colors.YELLOW,2,35,34,list));
        list.clear();

        list.add(115);
        list.add(654);
        list.add(141);
        list.add(585);
        vertices[35].addEdge(new Edge(Colors.WHITE,3,35,28,list));
        list.clear();

        list.add(127);
        list.add(652);
        list.add(176);
        list.add(597);
        vertices[35].addEdge(new Edge(Colors.GRAY,3,35,29,list));
        list.clear();

        list.add(127);
        list.add(666);
        list.add(173);
        list.add(678);
        vertices[35].addEdge(new Edge(Colors.BLACK,2,35,37,list));
        list.clear();

        list.add(108);
        list.add(675);
        list.add(91);
        list.add(694);
        list.add(91);
        list.add(694);
        list.add(112);
        list.add(720);
        vertices[35].addEdge(new Edge(Colors.ORANGE,2,35,36,list));

        vertices[36] = new Vertex(
            Cities.SWITZERLAND,"Switzerland",115,165,688,736);
        vertices[36].addEdge(new Edge(Colors.ORANGE,2,36,35,list));
        list.clear();

        list.add(156);
        list.add(702);
        list.add(176);
        list.add(687);
        vertices[36].addEdge(new Edge(Colors.WHITE,1,36,37,list));
        list.clear();

        list.add(175);
        list.add(718);
        list.add(201);
        list.add(716);
        list.add(201);
        list.add(716);
        list.add(221);
        list.add(699);
        vertices[36].addEdge(new Edge(Colors.BLUE,2,36,38,list));
        list.clear();

        vertices[37] = new Vertex(
            Cities.KONSTANZ,"Konstanz",175,195,670,690);

        list.add(127);
        list.add(666);
        list.add(173);
        list.add(678);
        vertices[37].addEdge(new Edge(Colors.BLACK,2,37,35,list));
        list.clear();

        list.add(185);
        list.add(599);
        list.add(185);
        list.add(670);
        vertices[37].addEdge(new Edge(Colors.GREEN,3,37,29,list));
        list.clear();

        list.add(194);
        list.add(681);
        list.add(216);
        list.add(688);
        vertices[37].addEdge(new Edge(Colors.YELLOW,1,37,38,list));
        list.clear();

        list.add(156);
        list.add(702);
        list.add(176);
        list.add(687);
        vertices[37].addEdge(new Edge(Colors.WHITE,1,37,36,list));
        list.clear();

        vertices[38] = new Vertex(Cities.LINDAU,"Lindau",217,237,679,699);
        list.add(194);
        list.add(681);
        list.add(216);
        list.add(688);
        vertices[38].addEdge(new Edge(Colors.YELLOW,1,38,37,list));
        list.clear();

        list.add(239);
        list.add(636);
        list.add(228);
        list.add(679);
        vertices[38].addEdge(new Edge(Colors.RED,2,38,30,list));
        list.clear();

        list.add(240);
        list.add(685);
        list.add(307);
        list.add(681);
        list.add(307);
        list.add(680);
        list.add(353);
        list.add(652);
        vertices[38].addEdge(new Edge(Colors.GRAY,5,38,32,list));
        list.clear();

        list.add(237);
        list.add(695);
        list.add(284);
        list.add(711);
        vertices[38].addEdge(new Edge(Colors.PURPLE,2,38,39,list));
        list.clear();

        list.add(175);
        list.add(718);
        list.add(201);
        list.add(716);
        list.add(201);
        list.add(716);
        list.add(221);
        list.add(699);
        vertices[38].addEdge(new Edge(Colors.BLUE,2,38,36,list));
        list.clear();

        vertices[39] = new Vertex(
            Cities.AUSTRIA,"Austria",405,480,650,725);
        list.add(237);
        list.add(695);
        list.add(284);
        list.add(711);
        vertices[39].addEdge(new Edge(Colors.PURPLE,2,39,38,list));
        list.clear();

        list.add(367);
        list.add(652);
        list.add(419);
        list.add(702);
        vertices[39].addEdge(new Edge(Colors.RED,3,39,32,list));
        list.clear();

        list.add(402);
        list.add(566);
        list.add(458);
        list.add(646);
        vertices[39].addEdge(new Edge(Colors.YELLOW,4,39,33,list));
        list.clear();
    }

    /**
     * Tests if a destination ticket has been fulfilled
     * 
     * @param src,dst the vertices visited
     * @return a boolean representing a success or failure
     */
    protected boolean completedDest(int src, int dst)
    {
        boolean[] visited = new boolean[vertices.length];

        Stack<Integer> stack = new Stack<>(); 
        stack.push(src);
        int s;
        while(!stack.isEmpty())
        {
            s = stack.pop();
            visited[s] = true;
            if(s == dst)
            {
                return true;
            }

            for(Edge e : vertices[src].getEdge())
            {
                if(!visited[e.dst] && e.getOwner() == currentPlayer)
                {
                    stack.push(e.dst);
                }
            }
        }
        return false;
    }

    /**
     * Sets meeples up on the board
     */
    protected static void setMeeples()
    {
        ArrayList<Meeple> meeples = new ArrayList<>();
        for(int i = 0; i < 10; i++)
        {
            meeples.add(new Meeple(Colors.RED));
            meeples.add(new Meeple(Colors.BLACK));
            meeples.add(new Meeple(Colors.GREEN));
            meeples.add(new Meeple(Colors.WHITE));
            meeples.add(new Meeple(Colors.BLUE));
            meeples.add(new Meeple(Colors.YELLOW));
        }
        Collections.shuffle(meeples);

        for(Vertex v: vertices)
        {
            if(v.getCityName() == Cities.LEIPZIG 
            || v.getCityName() == Cities.STUTTGART)
            {
                v.addMeeple(meeples.remove(0));
                v.addMeeple(meeples.remove(0));
                v.addMeeple(meeples.remove(0));
            }
            else if(v.getCityName() == Cities.FRANKFURT 
            || v.getCityName() == Cities.KOLN 
            || v.getCityName() == Cities.MUNCHEN
            || v.getCityName() == Cities.HAMBURG)
            {
                v.addMeeple(meeples.remove(0));
                v.addMeeple(meeples.remove(0));
                v.addMeeple(meeples.remove(0));
                v.addMeeple(meeples.remove(0));
            }
            else if(v.getCityName() == Cities.BERLIN)
            {
                v.addMeeple(meeples.remove(0));
                v.addMeeple(meeples.remove(0));
                v.addMeeple(meeples.remove(0));
                v.addMeeple(meeples.remove(0));
                v.addMeeple(meeples.remove(0));
            }
            else
            {
                v.addMeeple(meeples.remove(0));
            }
        }
    }

    /**
     * Switches between active players on board
     */
    protected void switchTurn(){
        int locCounter = 0;
        for(int i = 0; i < 5; i++)
        {
            if(tableCards[i].getColor() == Colors.LOCO)
            {
                locCounter++;
            }
        }
        if(locCounter > 2)
        {

            for(int i = 0; i < 5; i++)
            {
                deck.add(tableCards[i]);
            }
            deck.shuffle();
            int locoC2 = 0;

            for(int i = 0; i < 5; i++)
            {
                tableCards[i] = deck.getCard();
            }
            int counter = 0;
            while(counter < 10){
                locoC2=0;
                for(int i = 0; i < 5; i++)
                {
                    if(tableCards[i].getColor() == Colors.LOCO)
                    {
                        locoC2++;
                    }
                }
                if(locoC2 > 2){
                    deck.shuffle();
                }
                else{break;}
                for(int i = 0; i < 5; i++)
                {
                    tableCards[i] = deck.getCard();
                }
                counter++;

            }
            if(counter > 10){
                deck.deck.addAll(discard);
                discard.clear();
                deck.shuffle();
            }
            repaint();
        }
        if(totalDestCount == 4)
        {
            if(playerIndex == playerlist.size() - 1)
            {
                playerIndex = 0;
            }
            else
            {
                playerIndex++;
            }
            currentPlayer = playerlist.get(playerIndex);

            JOptionPane.showMessageDialog(new JFrame(),
                "It's " +currentPlayer.getName() + "'s turn"
            , "Message", JOptionPane.INFORMATION_MESSAGE);
        }
        if(claims == 1)
        {
            claims = 0;
            if(playerIndex == playerlist.size() - 1)
            {
                playerIndex = 0;
            }
            else
            {
                playerIndex++;
            }
            currentPlayer = playerlist.get(playerIndex);

            JOptionPane.showMessageDialog(new JFrame(),
                "It's " +currentPlayer.getName() + "'s turn"
            , "Message", JOptionPane.INFORMATION_MESSAGE);  
        }
        else if(locCard == 1)
        {
            locCard = 0;
            if(playerIndex == playerlist.size() - 1)
            {
                playerIndex = 0;
            }
            else
            {
                playerIndex++;
            }
            currentPlayer = playerlist.get(playerIndex);

            JOptionPane.showMessageDialog(new JFrame(),
                "It's " +currentPlayer.getName() + "'s turn"
            , "Message", JOptionPane.INFORMATION_MESSAGE);  
        }
        else if(tCards == 2)
        {
            tCards = 0;
            firstTrain = false;
            if(playerIndex == playerlist.size() - 1)
            {
                playerIndex = 0;
            }
            else
            {
                playerIndex++;
            }
            currentPlayer = playerlist.get(playerIndex);
            JOptionPane.showMessageDialog(new JFrame(),
                "It's " +currentPlayer.getName() 
                + "'s turn", "Message",
                JOptionPane.INFORMATION_MESSAGE);
        }

    }

    /**
     * Due to the implemented event handlers, this code needed
     * to be here, HOWEVER due to a lack of necessity the method
     * is blank
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Due to the implemented event handlers, this code needed
     * to be here, HOWEVER due to a lack of necessity the method
     * is blank
     */
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Due to the implemented event handlers, this code needed
     * to be here, HOWEVER due to a lack of necessity the method
     * is blank
     */
    public void mousePressed(MouseEvent e) {

    }

    /**
     * Due to the implemented event handlers, this code needed
     * to be here, HOWEVER due to a lack of necessity the method
     * is blank
     */
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * Handles how every mouse click dependent on where it was
     * clicked.
     * @param e the mouseclick at a given point
     */
    public void mouseClicked(MouseEvent e) {

        if(xHov < getWidth() && yHov < getHeight())
        {
            if(firstClick == null){
                firstClick = findVertex(e.getX(), e.getY());

            }
            else if (secondClick == null){

                secondClick = findVertex(e.getX(), e.getY());

                if(firstClick == null || secondClick == null){
                    firstClick = null;
                    secondClick = null;
                }
                else{
                    legalCapture(firstClick,secondClick);
                }
            }
        }

        if((e.getX() > 550 && e.getX() < 675)
        && (e.getY() > 45 && e.getY() <120))
        {
            TrainCard c = tableCards[0];
            if(totalDestCount > 0)
            {
                JOptionPane.showMessageDialog(
                    null, "Must finish choosing destination cards "
                , "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            else if((c.getColor() != Colors.LOCO
                || tCards == 0)&& !mustClaim)
            {
                currentPlayer.addCard(c);
                tableCards[0] = deck.getCard();

                if(c.getColor() == Colors.LOCO)
                {
                    locCard++;
                }
                else
                {
                    tCards++;
                }

                if(endTemp>0)
                    endTemp--;

                repaint();
                switchTurn();

            }
            else if (!mustClaim)
            {
                JOptionPane.showMessageDialog
                (null, "Cannot choose a faceup Locomotive "
                , "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            else
            {

                JOptionPane.showMessageDialog
                (null, "Must claim a route, no cards left "
                , "ERROR", JOptionPane.ERROR_MESSAGE); 
            }
        }
        if((e.getX() > 550 && e.getX() < 675)
        && (e.getY() > 125 && e.getY() < 205))
        {
            TrainCard c = tableCards[1];
            if(totalDestCount > 0)
            {
                JOptionPane.showMessageDialog
                (null, "Must finish choosing destination cards "
                , "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            else if((c.getColor() != Colors.LOCO
                || tCards == 0) && !mustClaim)
            {
                currentPlayer.addCard(c);
                tableCards[1] = deck.getCard();

                if(c.getColor() == Colors.LOCO)
                {
                    locCard++;
                }
                else
                {
                    tCards++;
                }

                if(endTemp>0)
                    endTemp--;

                repaint();
                switchTurn();

            }
            else if(!mustClaim)
            {

                JOptionPane.showMessageDialog
                (null, "Cannot choose a faceup Locomotive "
                , "ERROR", JOptionPane.ERROR_MESSAGE); 
            }
            else
            {

                JOptionPane.showMessageDialog
                (null, "Must claim a route or"
                    +" destination card, no cards left "
                , "ERROR", JOptionPane.ERROR_MESSAGE); 
            }
        }
        if((e.getX() > 550 && e.getX() < 675)
        && (e.getY() > 205 && e.getY() <280))
        {
            TrainCard c = tableCards[2];
            if(totalDestCount > 0)
            {
                System.out.println(totalDestCount);
                JOptionPane.showMessageDialog
                (null, "Must finish choosing destination cards "
                , "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            else if((c.getColor() != Colors.LOCO
                || tCards == 0) && !mustClaim)
            {
                currentPlayer.addCard(c);
                tableCards[2] = deck.getCard();

                if(c.getColor() == Colors.LOCO)
                {
                    locCard++;
                }
                else
                {
                    tCards++;
                }

                if(endTemp>0)
                    endTemp--;

                repaint();    
                switchTurn();

            }
            else if(!mustClaim)
            {
                JOptionPane.showMessageDialog
                (null, "Cannot choose a faceup Locomotive "
                , "ERROR", JOptionPane.ERROR_MESSAGE); 
            }
            else
            {

                JOptionPane.showMessageDialog
                (null, "Must claim a route or"
                    +" destination card, no cards left "
                , "ERROR", JOptionPane.ERROR_MESSAGE); 
            }
        }
        if((e.getX() > 550 && e.getX() < 675)
        && (e.getY() > 285 && e.getY() <360))
        {
            TrainCard c = tableCards[3];
            if(totalDestCount > 0)
            {
                JOptionPane.showMessageDialog
                (null, "Must finish choosing destination cards "
                , "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            else if((c.getColor() != Colors.LOCO
                || tCards == 0) && !mustClaim)
            {
                currentPlayer.addCard(c);
                tableCards[3] = deck.getCard();
                if(c.getColor() == Colors.LOCO)
                {
                    locCard++;
                }
                else
                {
                    tCards++;
                }

                if(endTemp>0)
                    endTemp--;

                repaint();    
                switchTurn();

            }
            else if(!mustClaim)
            {
                JOptionPane.showMessageDialog
                (null, "Cannot choose a faceup Locomotive "
                , "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            else
            {

                JOptionPane.showMessageDialog
                (null, "Must claim a route or"
                    +" destination card, no cards left "
                , "ERROR", JOptionPane.ERROR_MESSAGE); 
            }
        }
        if((e.getX() > 550 && e.getX() < 675)
        && (e.getY() > 365 && e.getY() <440))
        {
            TrainCard c = tableCards[4];
            if(totalDestCount > 0)
            {
                JOptionPane.showMessageDialog
                (null, "Must finish choosing destination cards "
                , "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            else if((c.getColor() != Colors.LOCO
                || tCards == 0) && !mustClaim)
            {
                currentPlayer.addCard(c);
                tableCards[4] = deck.getCard();
                if(c.getColor() == Colors.LOCO)
                {
                    locCard++;
                }
                else
                {
                    tCards++;
                }

                if(endTemp>0)
                    endTemp--;

                repaint();    
                switchTurn();

            }
            else if (!mustClaim)
            {
                JOptionPane.showMessageDialog
                (null, "Cannot choose a faceup Locomotive "
                , "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            else
            {

                JOptionPane.showMessageDialog
                (null, "Must claim a route or"
                    +" destination card, no cards left "
                , "ERROR", JOptionPane.ERROR_MESSAGE); 
            }
        }
        if((e.getX() > 550 && e.getX() < 675)
        && (e.getY() > 460 && e.getY() <535 ) )
        {
            if(totalDestCount > 0)
            {
                JOptionPane.showMessageDialog
                (null, "Must finish choosing destination cards "
                , "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            else if(!mustClaim)
            {
                TrainCard c = deck.getCard();
                currentPlayer.addCard(c);
                tCards++;

                if(endTemp>0)
                    endTemp--;

                repaint();    
                switchTurn();

            }
            else
            {

                JOptionPane.showMessageDialog
                (null, "Must claim a route or"
                    +" destination card, no cards left "
                , "ERROR", JOptionPane.ERROR_MESSAGE); 
            }
        }

        //blue dest and orange dest deck buttons" blue is short 
        ArrayList<DestCard> tempShortList
        = new ArrayList<DestCard>(dest.shortDestDeck);
        ArrayList<DestCard> tempLongList 
        = new ArrayList<DestCard>(dest.longDestDeck);

        if((e.getX() > 700 && e.getX() < 965)
        && (e.getY() > 460 && e.getY() < 535)) {

            // g2.drawImage(shortDest, 710, 460, this);
            // g2.drawImage(longDest, 850, 460, this);

            //blue short clicked
            //needed to not modify the actual deck
            //only used to get the cards able to be selected

            if(tCards == 0)
            {
                if((e.getX() > 710 && e.getX() < 710+ 120 )
                && (e.getY() > 460 && e.getY()< 460 + 70)
                && totalDestCount < 4){
                    destTemp[totalDestCount] =
                    dest.shortDestDeck.get(shortDestCount);
                    shortDestCount ++;
                    totalDestCount ++;
                }     
                else if((e.getX() > 850 && e.getX() < 850 + 120)
                && (e.getY() > 460 && e.getY() < 460 + 70)
                && totalDestCount < 4){
                    destTemp[totalDestCount] = 
                    dest.longDestDeck.get(longDestCount);
                    longDestCount ++;
                    totalDestCount ++;
                }

                if(totalDestCount == 4){
                    dest.destTurn(destTemp[0],
                        destTemp[1], 
                        destTemp[2],
                        destTemp[3],
                        currentPlayer, 1);
                    clickDest = true;
                    totalDestCount = 0;
                    shortDestCount = 0;
                    longDestCount = 0;

                    switchTurn();
                }

            }

        }
        if((e.getX() > 710 && e.getX() < 710 + 150)
        && (e.getY() > 560 && e.getY() < 560 + 52)){
            DestHandPanel panel = new DestHandPanel();
            panel.showDestHand(currentPlayer);
        }
        else if (tCards == 1 && firstTrain)
        {

            JOptionPane.showMessageDialog(null,
                "Must take another train card "
            , "ERROR", JOptionPane.ERROR_MESSAGE);

        }
        else if(tCards == 1 && !firstTrain)
        {
            firstTrain = true;
        }

        if(endGame() && endTemp == -1)
        {
            endTemp = playerlist.size();

            Player tempPlayer = playerlist.get(0);          
            for(Player player : playerlist)
            {
                if(player.getPoints() > tempPlayer.getPoints())
                {
                    tempPlayer = player;
                }                
            }

        }
        if(endTemp == 0)
        {   

            for(Player p : playerlist)
            {
                currentPlayer = p;
                for(DestCard c: p.destination)
                {
                    int start = 0;
                    int end = 0;
                    for(int i = 0; i < vertices.length; i++)
                    {
                        if(c.startCity.equalsIgnoreCase(
                            vertices[i].stringName))
                        {
                            start = i;
                        }
                        else if(c.endCity.equalsIgnoreCase(
                            vertices[i].stringName))
                        {
                            end = i;
                        }
                    }

                    if(completedDest(start,end))
                    {
                        p.addPoints(c.points);
                        p.totalRoutes++;
                    }
                    else
                    {
                        p.subPoints(c.points);
                    }
                }
            }

            int tempIndex = 0;
            ArrayList<Integer> tempList = new ArrayList<>();
            tempList.add(0);
            for(int i = 1; i < playerlist.size(); i++)
            {
                if(playerlist.get(i).totalRoutes
                > playerlist.get(tempIndex).totalRoutes)
                {
                    tempIndex = i;
                    tempList.set(0,i);
                }
                if(playerlist.get(i).totalRoutes
                == playerlist.get(tempIndex).totalRoutes)
                {
                    tempList.add(i);
                }
            }
            if(tempList.size() > 1)
            {
                for(int i : tempList)
                {
                    playerlist.get(i).addPoints(15);
                }
            }
            else
            {
                playerlist.get(tempIndex).addPoints(15);
            }

            int firstRed = 0; int firstBlue = 0; int firstGreen = 0;
            int firstBlack = 0; int firstWhite = 0; int firstYellow = 0;

            Player tempPlayer = playerlist.get(0);

            for(int i = 1; i < playerlist.size(); i++)
            {
                Player p = playerlist.get(i);
                if(p.totalRedMeep() > tempPlayer.totalRedMeep())
                {
                    firstRed = i;
                }
                if(p.totalBlueMeep() > tempPlayer.totalBlueMeep())
                {
                    firstBlue = i;
                }
                if(p.totalGreenMeep() > tempPlayer.totalGreenMeep())
                {
                    firstGreen = i;
                }
                if(p.totalYellowMeep() > tempPlayer.totalYellowMeep())
                {
                    firstYellow = i;
                }
                if(p.totalBlackMeep() > tempPlayer.totalBlackMeep())
                {
                    firstBlack = i;
                }
                if(p.totalWhiteMeep() > tempPlayer.totalWhiteMeep())
                {
                    firstWhite = i;
                }
            }
            playerlist.get(firstRed).addPoints(20);
            playerlist.get(firstGreen).addPoints(20);
            playerlist.get(firstBlue).addPoints(20);
            playerlist.get(firstYellow).addPoints(20);
            playerlist.get(firstWhite).addPoints(20);
            playerlist.get(firstBlack).addPoints(20);
            boolean tieFirst = false;
            for(int i = 0; i < playerlist.size(); i++)
            {
                Player p = playerlist.get(i);
                if(p.totalRedMeep()
                == playerlist.get(firstRed).totalRedMeep()
                && i != firstRed)
                {
                    playerlist.get(i).addPoints(20);
                    tieFirst = true;
                }
                if(p.totalBlueMeep()
                == playerlist.get(firstBlue).totalBlueMeep()
                && i != firstBlue){
                    playerlist.get(i).addPoints(20);
                    tieFirst = true;
                }
                if(p.totalGreenMeep()
                == playerlist.get(firstGreen).totalGreenMeep()
                && i != firstGreen){
                    playerlist.get(i).addPoints(20);
                    tieFirst = true;
                }
                if(p.totalYellowMeep()
                == playerlist.get(firstYellow).totalYellowMeep()
                && i != firstYellow){
                    playerlist.get(i).addPoints(20);
                    tieFirst = true;
                }
                if(p.totalBlackMeep()
                == playerlist.get(firstBlack).totalBlackMeep()
                && i != firstBlack){
                    playerlist.get(i).addPoints(20);
                    tieFirst = true;
                }
                if(p.totalWhiteMeep()
                == playerlist.get(firstWhite).totalWhiteMeep()
                && i != firstWhite){
                    playerlist.get(i).addPoints(20);
                    tieFirst = true;
                }
            }

            int secondRed = 0;
            int secondBlue = 0;
            int secondGreen = 0;
            int secondBlack = 0;
            int secondWhite = 0;
            int secondYellow = 0;

            int iSecondRed = 0;
            int iSecondBlue = 0;
            int iSecondGreen = 0;
            int iSecondBlack = 0;
            int iSecondWhite = 0;
            int iSecondYellow = 0;
            for(int i = 0; i < playerlist.size(); i++)
            {
                Player p = playerlist.get(i);

                if(p.totalRedMeep() > secondRed
                && p.totalRedMeep() <
                playerlist.get(firstRed).totalRedMeep())
                {
                    secondRed = p.totalRedMeep();
                }
                if(p.totalGreenMeep() > secondGreen
                && p.totalGreenMeep() 
                < playerlist.get(firstGreen).totalGreenMeep())
                {
                    secondGreen = p.totalGreenMeep();
                }
                if(p.totalBlueMeep() > secondBlue
                && p.totalBlueMeep() <
                playerlist.get(firstBlue).totalBlueMeep())
                {
                    secondBlue = p.totalBlueMeep();
                }
                if(p.totalYellowMeep() > secondYellow
                && p.totalYellowMeep() <
                playerlist.get(firstYellow).totalYellowMeep())
                {
                    secondYellow = p.totalYellowMeep();
                }
                if(p.totalWhiteMeep() >
                secondWhite && p.totalWhiteMeep()
                < playerlist.get(firstWhite).totalWhiteMeep())
                {
                    secondWhite = p.totalWhiteMeep();
                }
                if(p.totalBlackMeep() > secondBlack &&
                p.totalBlackMeep() <
                playerlist.get(firstBlack).totalBlackMeep())
                {
                    secondBlack = p.totalBlackMeep();
                }
            }
            if(!tieFirst)
            {
                for(Player p : playerlist)
                {
                    if(p.totalRedMeep() == secondRed)
                    {
                        p.addPoints(10);
                    }
                    if(p.totalBlueMeep() == secondBlue)
                    {
                        p.addPoints(10);
                    }
                    if(p.totalGreenMeep() == secondGreen)
                    {
                        p.addPoints(10);
                    }
                    if(p.totalYellowMeep() == secondYellow)
                    {
                        p.addPoints(10);
                    }
                    if(p.totalBlackMeep() == secondBlack)
                    {
                        p.addPoints(10);
                    }
                    if(p.totalWhiteMeep() == secondWhite)
                    {
                        p.addPoints(10);
                    }
                }
            }

            //return winning player and end
            Stack<Integer> maxPoints = new Stack<Integer> ();
            maxPoints.push(0);

            int i = 0;
            for(i = 1; i < playerlist.size(); i++ )
            {

                if(playerlist.get(i).getPoints()
                > playerlist.get(maxPoints.peek()).getPoints()) 
                {
                    maxPoints =  new Stack<Integer> ();
                    maxPoints.push(i);
                }
                else if (playerlist.get(i).getPoints()
                == playerlist.get(maxPoints.peek()).getPoints())
                {
                    maxPoints.push(i);
                }
            }
            repaint();
            Player winner = playerlist.get(maxPoints.pop());
            String endG = "" + winner.getName();

            while(!maxPoints.isEmpty())
            {
                endG += ", " + playerlist.get(maxPoints.pop()).getName();                
            }
            repaint();
            JOptionPane.showMessageDialog(null,
                "Game Over: " + endG + " wins!", 
                "Message", JOptionPane.ERROR_MESSAGE);

            endTemp = 237;
            System.exit(0);

        }
    }

    /**
     * Checks to see if the mouse is moved
     * 
     * @param e the event that is handled 
     * by the mouse listener and mouse adapter
     */
    public void mouseMoved(MouseEvent e){
        xHov = e.getX();
        yHov = e.getY();

        for(int i = 0; i < 40; i++)
        {
            if(xHov >= vertices[i].xLower && xHov <= vertices[i].xUpper
            && yHov >= vertices[i].yLower && yHov <= vertices[i].yUpper)
            {
                temp = vertices[i].stringName;
                tempVertex = vertices[i];
            }
            else if(xHov >= 285 && xHov <= 325
            && yHov >= 695 && yHov <= 730)
            {
                temp = "Austria";
                tempVertex = vertices[39];
            }
        }

        if(!temp.equals("")){
            hover = true;   
        }
        repaint();
    }

    /**
     * Due to the implemented event handlers, this code needed
     * to be here, HOWEVER due to a lack of necessity the method
     * is blank
     */
    public void mouseDragged(MouseEvent e) {}

    /**
     * Checks to see if it is the end of the game 
     */
    protected boolean endGame(){
        for(Player play: playerlist){
            if(play.getTrainCount() <= 2){
                return true;
            }
        }
        return false;
    }

    /**
     * Finds the vertex the user clicked on
     * 
     * @param x the x coordinate to look for
     * @param y the y coordinate to look for
     * @return Vertex the vertex that was found
     */
    protected Vertex findVertex(int x, int y){
        Vertex v = null;
        for(int i =0; i < vertices.length; i++){
            if(x <=  vertices[i].getxUpper() 
            && x >= vertices[i].getxLower()
            &&y <=  vertices[i].getyUpper() 
            && y >= vertices[i].getyLower()){
                v = vertices[i];
                break;
            }
            else if((x >= 285 && x <= 325 && y >= 695 && y <= 730)){
                v = vertices[39];
            }
        }

        return v;
    }

    /**
     * Returns what the index of the vertex that was clicked on
     * 
     * @param v the vertex being gotten
     * @return index an integer that represents the vertex that it is at
     */
    protected int getIndex(Vertex v){
        int index = -1;
        for(int i =0; i < vertices.length; i++){
            if(vertices[i].getCityName() == v.getCityName()){
                index = i;   
            }
        }
        return index;
    }

    /**
     * Checks to see if two mouse clicked 
     * points were valid to claim the route 
     * 
     * @param a,b the vertices being captures
     * @return a boolean true if successful, 
     * false otherwise
     */
    protected boolean legalCapture(Vertex a, Vertex b){
        int errorCount = 0;
        boolean enoughTrains = false;
        boolean valid = false;
        if(tCards != 1)
        {
            for(int i =0; i < vertices.length; i++){
                ArrayList<Edge> aEd = new ArrayList<Edge>();
                aEd = vertices[i].getEdge();
                int tries = 0;
                for(int j = 0 ; j < aEd.size(); j++){
                    if((aEd.get(j).getSource() == getIndex(a) && 
                        aEd.get(j).getDest() == getIndex(b)))
                    {
                        Edge toClaim = aEd.get(j);
                        valid = true;
                        int trainCount = 0;
                        ArrayList<Edge> checkAlts 
                        = multipleRoutes(aEd.get(j));
                        if(checkAlts.size() > 1 
                        && toClaim.owner == null && tries == 0)
                        {
                            ArrayList<String> routeOptions
                            = new ArrayList<>();
                            for(Edge e: checkAlts)
                            {
                                routeOptions.add(e.getColor() + "");
                            }
                            JComboBox<String> routes 
                            = new JComboBox<String>
                                (routeOptions.toArray(
                                        new String[routeOptions.size()]));
                            int s = JOptionPane.showOptionDialog(
                                    new JFrame(), routes,
                                    "Choose a route color to claim", 
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.DEFAULT_OPTION,
                                    null, new Object[] { "OK" }, 
                                    JOptionPane.CLOSED_OPTION);

                            String choice = 
                                (String)routes.getSelectedItem();
                            int mIndex = 0;

                            for(Edge e : checkAlts)
                            {
                                if(((e.getColor() + "")).equals(choice))
                                {
                                    toClaim = e;
                                    break;
                                }
                            }
                            tries++;
                        }
                        for(TrainCard t: currentPlayer.trainC)
                        {
                            if(t.getColor() == toClaim.getColor() 
                            || t.getColor() == Colors.LOCO)
                            {
                                trainCount++;
                            }
                        }
                        boolean grayLegal = false;
                        if(toClaim.getColor() == Colors.GRAY)
                        {
                            trainCount = 0;
                        }
                        if(currentPlayer.maxCardType() >= toClaim.length)
                        {
                            grayLegal = true;
                        }
                        if(trainCount >= toClaim.getLength()
                        || (toClaim.getColor() == Colors.GRAY && grayLegal))
                        {
                            enoughTrains = true;
                            if(toClaim.owner == null)
                            {
                                JOptionPane.showMessageDialog(
                                    null,"Route captured from "
                                    + a.getCityName() + " to " 
                                    + b.getCityName(),"Success!",
                                    JOptionPane.DEFAULT_OPTION);    
                                firstClick = null;
                                secondClick = null;
                                ArrayList<TrainCard> temp
                                = new ArrayList<>();
                                for(TrainCard t: currentPlayer.trainC)
                                {
                                    if(t.getColor() == toClaim.getColor())
                                    {
                                        discard.add(
                                            new TrainCard(
                                                t.getImage(),t.getColor()));
                                        temp.add(t);
                                        trainCount--;
                                    }
                                    if(trainCount <= 0)
                                    {
                                        break; 
                                    }
                                }
                                if(toClaim.getColor() == Colors.GRAY)
                                {
                                    Colors[] colors = 
                                        {Colors.RED,Colors.GREEN,
                                            Colors.YELLOW,Colors.ORANGE,
                                            Colors.BLUE, Colors.WHITE,
                                            Colors.BLACK,Colors.PURPLE};

                                    ArrayList<String> tempColors 
                                    = new ArrayList<>();
                                    for(int k = 0; k < colors.length; k++)
                                    {
                                        int sum = 0;
                                        for(TrainCard t 
                                        : currentPlayer.trainC)
                                        {
                                            if(t.getColor() == colors[k]
                                            || t.getColor() == Colors.LOCO)
                                            {
                                                sum++;
                                            }
                                        }
                                        if(sum >= toClaim.length)
                                        {
                                            tempColors.add(colors[k] + "");
                                        }
                                    }

                                    JComboBox<String> choices = new
                                        JComboBox<String>(
                                            tempColors.toArray(
                                                new String[tempColors.size()]));
                                    int s = JOptionPane.showOptionDialog
                                        (new JFrame(), choices,
                                            "Which color cards"+
                                            " would you like to use", 
                                            JOptionPane.YES_NO_OPTION,
                                            JOptionPane.DEFAULT_OPTION,
                                            null, new Object[] { "OK" }, 
                                            JOptionPane.CLOSED_OPTION);

                                    String choice = 
                                        (String)choices.getSelectedItem();
                                    int count = toClaim.length;
                                    for(TrainCard t: currentPlayer.trainC)
                                    {
                                        if((t.getColor() + "").
                                        equals(choice))
                                        {
                                            discard.add(
                                                new TrainCard(
                                                    t.getImage(),
                                                    t.getColor()));
                                            t.setColor(Colors.GRAY);
                                            count--;
                                        }
                                        if(count == 0)
                                        {
                                            break;
                                        }
                                    }
                                    if(count > 0)
                                    {
                                        for(TrainCard t:
                                        currentPlayer.trainC)
                                        {
                                            if(t.getColor() == Colors.LOCO)
                                            {
                                                discard.add(
                                                    new TrainCard
                                                    (t.getImage()
                                                    ,t.getColor()));
                                                t.setColor(Colors.GRAY);
                                                count--;
                                            }
                                            if(count == 0)
                                            {
                                                break;
                                            }
                                        }
                                    }
                                }
                                if(trainCount > 0)
                                {
                                    for(TrainCard t: currentPlayer.trainC)
                                    {
                                        if(t.getColor() == Colors.LOCO)
                                        {
                                            temp.add(t);
                                            discard.add(new TrainCard
                                                (t.getImage(),Colors.LOCO));

                                            trainCount--;
                                        }
                                        if(trainCount <= 0)
                                        {
                                            break;
                                        }
                                    }
                                }
                                int tCount = toClaim.length;
                                for(TrainCard t: temp)
                                {
                                    tCount--;
                                    currentPlayer.trainC.remove(t);
                                    if(tCount== 0)
                                    {
                                        break;
                                    }
                                }
                                claimRoute
                                (currentPlayer,toClaim.src,toClaim.dst);
                                currentPlayer.addPoints
                                (currentPlayer.addTrainPoints
                                    (toClaim.getLength()));
                                currentPlayer.setTrainCount
                                (toClaim.getLength());

                                claims++;
                                if(a.getNumMeeples() > 0)
                                {
                                    ArrayList<String> mList
                                    = new ArrayList<String>();
                                    for(Meeple m: a.meeples)
                                    {
                                        mList.add(m.getColor() + "");
                                    }
                                    JComboBox<String> meeples = 
                                        new JComboBox<String>
                                        (mList.toArray(
                                                new String[mList.size()]));
                                    int s = JOptionPane.showOptionDialog
                                        (new JFrame(), meeples
                                        , "Choose a Meeple from "
                                            + a.stringName, 
                                            JOptionPane.YES_NO_OPTION,
                                            JOptionPane.DEFAULT_OPTION,
                                            null, 
                                            new Object[] { "OK" },
                                            JOptionPane.CLOSED_OPTION);

                                    String choice =
                                        (String)meeples.getSelectedItem();
                                    int mIndex = 0;

                                    for(Meeple m : a.meeples)
                                    {
                                        if(((m.getColor() + ""))
                                        .equals(choice))
                                        {
                                            currentPlayer.addMeeple
                                            (a.meeples.remove(
                                                    mIndex).getColor());
                                            break;
                                        }
                                        mIndex++;
                                    }

                                }
                                if(b.getNumMeeples() > 0)
                                {
                                    ArrayList<String> mList 
                                    = new ArrayList<String>();
                                    for(Meeple m: b.meeples)
                                    {
                                        mList.add(m.getColor() + "");
                                    }
                                    JComboBox<String> meeples = 
                                        new JComboBox<String>(
                                            mList.toArray
                                            (new String[mList.size()]));
                                    int s = JOptionPane.showOptionDialog
                                        (new JFrame(), meeples, 
                                            "Choose a meeple from " 
                                            + b.stringName,JOptionPane.
                                            YES_NO_OPTION, JOptionPane.
                                            DEFAULT_OPTION, null, 
                                            new Object[] { "OK" }, 
                                            JOptionPane.CLOSED_OPTION);

                                    String choice = (String)meeples
                                        .getSelectedItem();
                                    int mIndex = 0;

                                    for(Meeple m : b.meeples)
                                    {
                                        if(((m.getColor() + ""))
                                        .equals(choice))
                                        {
                                            currentPlayer
                                            .addMeeple(b.meeples
                                                .remove(mIndex).getColor());
                                            break;
                                        }
                                        mIndex++;
                                    }

                                }
                                switchTurn();
                                return true;
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(null,
                                    "Route is already owned!",
                                    "ERROR", JOptionPane.ERROR_MESSAGE);
                                errorCount++;
                                break;
                            }
                        }
                    }
                }   
            }
        }
        if(!enoughTrains && valid)
        {
            JOptionPane.showMessageDialog(null,
                "Not enough train cards"
            , "ERROR", JOptionPane.ERROR_MESSAGE); 
        }
        else if(tCards == 1)
        {
            JOptionPane.showMessageDialog(null,
                "Must choose a second card."
            , "ERROR", JOptionPane.ERROR_MESSAGE); 
        }
        else if(errorCount == 0)
        {
            JOptionPane.showMessageDialog(null,
                "Invalid Route from " 
                + a.getCityName() + " to " + b.getCityName()
            , "ERROR", JOptionPane.ERROR_MESSAGE);    
        }
        firstClick = null;
        secondClick = null;

        return false;
    }

    /**
     * Claims a route for a plyer
     * @param p the player claiming
     * @param src the vertex being affected
     * @param the destination vertex
     */
    public void claimRoute(Player p, int src, int dst)
    {
        for(Edge e: vertices[src].getEdge())
        {
            if(e.dst == dst)
            {
                e.owner = p;
            }
        }
        for(Edge e: vertices[dst].getEdge())
        {
            if(e.dst == src)
            {
                e.owner = p;
            }
        }
    }

    /**
     * Handles instances of multple routes
     * 
     * @param e the edge being checked
     */
    public ArrayList<Edge> multipleRoutes(Edge e)
    {
        ArrayList<Edge> toReturn = new ArrayList<>();
        Vertex v = vertices[e.src];
        for(Edge edge : v.getEdge())
        {
            if(edge.dst == e.dst)
            {
                toReturn.add(edge);
            }
        }
        return toReturn;
    }
}
