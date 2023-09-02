package edu.eci.arsw.primefinder;

import java.util.LinkedList;
import java.util.List;

public class PrimeFinderThread extends Thread{

	
	int a,b;
	boolean suspend;
	
	private List<Integer> primes;
	
	public PrimeFinderThread(int a, int b) {
		super();
        this.primes = new LinkedList<>();
		this.a = a;
		this.b = b;
		this.suspend=false;
	}

        @Override
	public void run(){
		for (int i= a;i < b;i++){						
			if (isPrime(i)){
				primes.add(i);
				System.out.println(this.getName() +" " + Integer.toString(i));
			}
			synchronized (this){
				while(suspend) {
					try {		
						this.wait();
					}catch(InterruptedException e){
						throw new RuntimeException(e);
					}
				}
			}
		}
	}
        
        
        
        
	
	boolean isPrime(int n) {
	    boolean ans;
            if (n > 2) { 
                ans = n%2 != 0;
                for(int i = 3;ans && i*i <= n; i+=2 ) {
                    ans = n % i != 0;
                }
            } else {
                ans = n == 2;
            }
	    return ans;
	}

	public List<Integer> getPrimes() {
		return primes;
	}
	
	
	synchronized void resumeThread() {
		suspend=false;
		notify();
	}
	
	synchronized void suspendThread() {
		suspend=true;
	}
	
}
