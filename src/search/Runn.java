package search;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import model.data.Level;
import model.data.Level2D;
import model.data.MyTextLevelLoader;
import model.data.Position;

public class Runn {

	public static void main(String[] args) {

		/*
		MyTextLevelLoader load= new MyTextLevelLoader();
		try {
			Level level=load.load_level(new FileInputStream(new File("level3.txt")) );
			Position current=level.getActors().get(0).getP();
			SokobanMoveSearchAdapter s=new SokobanMoveSearchAdapter((Level2D)level, new Position(3,16),current);

			BFS<Position> bfs = new BFS<>();

				Solution sol = bfs.search(s);			//	Solution sol=dj.search(adapter);
				System.out.println("the solution:\n"+sol);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	*/

		MyTextLevelLoader load= new MyTextLevelLoader();
		try {
			Level level=load.load_level(new FileInputStream(new File("y.txt")) );
			Position current=level.getActors().get(0).getP();
			Position box = level.getBoxes().get(0).getP();
			Position goal = level.getDest_boxes().get(0).getP();
			SokobanPushSearchAdapter push = new SokobanPushSearchAdapter((Level2D)level, goal, box);
			//SokobanMoveSearchAdapter push=new SokobanMoveSearchAdapter((Level2D)level, goal, current);
			BFS<SokobanState> bfs = new BFS<>();
			//BFS<Position> bfs=new BFS<>();
			Solution sol = bfs.search(push);
			System.out.println("the solution:\n"+sol);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}




}
