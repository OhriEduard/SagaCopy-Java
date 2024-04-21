package Main;

import Frames.AdminFrame;
import Frames.MainFrame;
import Repository.EntityManagerFactoryCreator;
import Repository.MyRepository;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {
	public static void main(String[] args) {
		EntityManagerFactoryCreator.obtainOrCreateEntityManagerFactory();
		MainFrame mainFrame = new MainFrame();
	}
}
