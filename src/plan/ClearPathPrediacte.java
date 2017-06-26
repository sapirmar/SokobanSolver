package plan;

import model.data.Level2D;
import model.data.Position;
import search.BFS;

import search.SokobanPushSearchAdapter;
import search.SokobanState;
import search.Solution;
import strips.Predicate;

public class ClearPathPrediacte extends SokPredicate {
	Level2D level;
	Position goal;
	Position box;
	public ClearPathPrediacte(String from, String value) {
		super("clearPath", from, value);
		// TODO Auto-generated constructor stub
	}

	@Override//if there is maslul it can be true
	public boolean satisfies(Predicate p) {
		if(ifPath(level, goal, box)){
		return super.satisfies(p);
		}
		return false;
	}
	
	/**
	 * check if their is path by the BFS algorithem
	 * @param level 
	 * @param goal the target of the box
	 * @param box the box we want to move
	 * @return true or false
	 */
	public boolean ifPath(Level2D level,Position goal,Position box){
		this.level=level;
		this.box=box;
		this.goal=goal;
		SokobanPushSearchAdapter search=new SokobanPushSearchAdapter( level,goal,box);
		BFS<SokobanState> bfs=new BFS<>();
		Solution solution;
		solution=bfs.search(search);
		if(solution!=null)
		{
			return true;
		}
		else
			return false;
	}

}
