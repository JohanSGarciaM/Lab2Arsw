package snakepackage;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import enums.GridSize;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;




/**
 * @author jd-
 *
 */
public class SnakeApp {

    private static SnakeApp app;
    public static final int MAX_THREADS = 8;
    Snake[] snakes = new Snake[MAX_THREADS];
    private static final Cell[] spawn = {
        new Cell(1, (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(GridSize.GRID_WIDTH - 2,
        3 * (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(3 * (GridSize.GRID_WIDTH / 2) / 2, 1),
        new Cell((GridSize.GRID_WIDTH / 2) / 2, GridSize.GRID_HEIGHT - 2),
        new Cell(1, 3 * (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(GridSize.GRID_WIDTH - 2, (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell((GridSize.GRID_WIDTH / 2) / 2, 1),
        new Cell(3 * (GridSize.GRID_WIDTH / 2) / 2,
        GridSize.GRID_HEIGHT - 2)};
    private JFrame frame;
    private JButton start;
    private JButton resume;
    private JButton pause;
    private int deadSnake = -1;
    private static Board board;
    int nr_selected = 0;
    Thread[] thread = new Thread[MAX_THREADS];
    public SnakeApp() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        frame = new JFrame("The Snake Race");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setSize(618, 640);
        frame.setSize(GridSize.GRID_WIDTH * GridSize.WIDTH_BOX + 17,
                GridSize.GRID_HEIGHT * GridSize.HEIGH_BOX + 40);
        frame.setLocation(dimension.width / 2 - frame.getWidth() / 2,
                dimension.height / 2 - frame.getHeight() / 2);
        board = new Board();
        
        
        frame.add(board,BorderLayout.CENTER);
        
        JPanel actionsBPabel=new JPanel();
        actionsBPabel.setLayout(new FlowLayout());
        start = new JButton("Iniciar");
        pause = new JButton("Pausar");
        resume = new JButton("Reanudar");
        actionsBPabel.add(start);
        actionsBPabel.add(pause);
        actionsBPabel.add(resume);
        frame.add(actionsBPabel,BorderLayout.SOUTH);
        prepareActionButtons();

    }
    
    private void prepareActionButtons() {
    	start.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent ev) {
    			resumeOption();
    			start.setEnabled(false);
    		}
    	});
    	
    	pause.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent ev) {
    			try {
    				pauseOption();
    				preparePausePanel();
    			}catch(InterruptedException e) {
    				throw new RuntimeException(e);
    			}
    		}
    	});
    	
    	resume.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent ev) {
    			resumeOption();
    		}
    	});
    }
    
    private void preparePausePanel() {
    	Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame frame1 = new JFrame("Game paused");
        JPanel snakesInfo=new JPanel();
        frame1.setVisible(true);
        frame1.setSize(300, 200);
        frame1.setLocation(dimension.width / 2 - frame.getWidth() / 3,dimension.height / 2 - frame.getHeight() / 4);
        frame1.setLayout(new BorderLayout());
        snakesInfo.setLayout(new FlowLayout());
        JLabel bigSnake = new JLabel("longest snake: "+getBiggest());
        snakesInfo.add(bigSnake);
        JLabel worstSnake = new JLabel("Worst snake: "+getFirstSnake());
        snakesInfo.add(worstSnake);
        frame1.add(snakesInfo);
    }

    private void pauseOption() throws InterruptedException{
    	for(int i = 0; i != MAX_THREADS; i++) {
    		snakes[i].stopThread();
    	}
    }
    
    private void resumeOption() {
    	for(int i = 0; i != MAX_THREADS; i++) {
    		snakes[i].restartThread();
    	}
    }
    
    public static void main(String[] args) {
        app = new SnakeApp();
        app.init();
    }

    private void init() {
        
        
        
        for (int i = 0; i != MAX_THREADS; i++) {
            
            snakes[i] = new Snake(i + 1, spawn[i], i + 1);
            snakes[i].addObserver(board);
            thread[i] = new Thread(snakes[i]);
            thread[i].start();
        }

        frame.setVisible(true);

            
        while (true) {
            int x = 0;
            for (int i = 0; i != MAX_THREADS; i++) {
                if (snakes[i].isSnakeEnd() == true) {
                	if(deadSnake == -1) {
                		deadSnake = i+1;
                	}
                    x++;
                }
            }
            if (x == MAX_THREADS) {
                break;
            }
        }


        System.out.println("Thread (snake) status:");
        for (int i = 0; i != MAX_THREADS; i++) {
            System.out.println("["+i+"] :"+thread[i].getState());
        }
        

    }

    public static SnakeApp getApp() {
        return app;
    }
    
    public int getBiggest(){
        int maxValue = 0;
        int position = 0;
        for (int i = 0; i != MAX_THREADS; i++) {
            if (snakes[i].getSnakeBody() > maxValue && !snakes[i].getSnakeEnd()){
                maxValue = snakes[i].getSnakeBody();
                position = i;
            }
        }
        return position+1;
    }

    public int getFirstSnake() {
        return deadSnake;
    }

}
