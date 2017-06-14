package solver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import model.data.Level;
import model.data.Level2D;
import model.data.MyTextLevelLoader;
import search.Solution;

public class TestSolver {

	public static void main(String[] args) {

			//Level level=load.load_level(new FileInputStream(new File("y.txt")) );
			SokobanSolver solver=new SokobanSolver();
			Level2D level=solver.readLevel(args[0]);
			Solution solution=solver.solve((Level2D)level);

			if(solution!=null){
			solver.writeToFile(args[1]);
			}
			else{
				System.err.println("no Solution for this level");
			}
	}

}
