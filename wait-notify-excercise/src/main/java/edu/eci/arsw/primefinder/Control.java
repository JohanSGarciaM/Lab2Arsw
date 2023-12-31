/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.primefinder;

import java.util.Scanner;

/**
 *
 */
public class Control extends Thread {
    
    private final static int NTHREADS = 10;
    private final static int MAXVALUE = 50000;
    private final static int TMILISECONDS = 2000;

    private final int NDATA = MAXVALUE / NTHREADS;

    private PrimeFinderThread pft[];
    Scanner scanner = new Scanner(System.in);
    String cont="";
    
    private Control() {
        super();
        this.pft = new  PrimeFinderThread[NTHREADS];

        int i;
        for(i = 0;i < NTHREADS - 1; i++) {
            PrimeFinderThread elem = new PrimeFinderThread(i*NDATA, (i+1)*NDATA);
            pft[i] = elem;
        }
        pft[i] = new PrimeFinderThread(i*NDATA, MAXVALUE + 1);
    }
    
    public static Control newControl() {
        return new Control();
    }

    @Override
    public void run() {
        for(int i = 0;i < NTHREADS;i++ ) {
            pft[i].start();   
        }
        boolean dead=false;
        while(!dead) {
        	timeOfExe();
//        	Suspender cada t tiempo 
        	for (int i = 0;i < NTHREADS;i++) {
        		pft[i].suspendThread();
        	}
        	
//        	Solicita ingresar enter para continuar
        	try {
				Thread.sleep(TMILISECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	System.out.println("Ingrese Enter para continuar");
        	String space = new Scanner(System.in).nextLine();
        	if (space.equals("")) {
        		for (int i = 0; i < NTHREADS; i++) {
        			pft[i].resumeThread();
        		}
        	}

//        	Revisar si todos los hilos estan vivos
        	boolean live = false;
        	for(int i = 0;i < NTHREADS;i++) {
        		live = (!pft[i].isAlive())||(live);
        	}
    		dead = live;																			
        }System.out.println("Todos los primos entre "+Integer.toString(0)+" y "+Integer.toString(MAXVALUE)+" fueron hallados cada "+ Integer.toString(TMILISECONDS)+ " milisegundos");
    }
   
    
    // Método para dejar con "vida" T milisegundos los hilos
    public void timeOfExe() {
    	boolean flag = false;
    	long start = System.currentTimeMillis();
    	long end = 0;
    	while (!flag) {
    		end = System.currentTimeMillis() - start;
    		if (end >= TMILISECONDS) {
    			flag = true;
    		}		
    	}
    
    }
}   