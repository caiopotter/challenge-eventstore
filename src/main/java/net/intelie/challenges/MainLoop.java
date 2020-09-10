package net.intelie.challenges;

import java.util.Scanner;

public class MainLoop {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		EventStore main = new EventStoreMain();
		main.insert(new Event("Event", 1));
		main.insert(new Event("Event", 4));
		main.insert(new Event("Event", 3));
		main.insert(new Event("Even2t", 2));
		main.insert(new Event("Even2t", 6));
		main.insert(new Event("Even2t", 5));
		main.insert(new Event("Even2t", 8));
		main.insert(new Event("Event", 7));
		int option = -1;
		while(option != 0) {
			System.out.println("1 para trazer o próximo item");
			option = sc.nextInt();
			if(option == 1) {
				main.printAll();
			}else if(option == 2) {
				main.removeAll("Event");
			}else if(option == 3) {
				EventIterator query = main.query("Event", 3, 7);
			}
		}
		sc.close();
		
	}
}
