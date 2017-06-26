package search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.data.Level2D;
import model.data.Position;
import model.items.Destination_Box;
import model.items.Space;

public class BoxState implements Searchable<Position> {

	private Level2D level;
	Position goal;
	Position current;

	public BoxState(Level2D level, Position goal, Position current) {

		this.level = level;
		this.goal = goal;
		this.current = current;


	}

	@Override
	public State<Position> getInitialState() {
		// Position p = new Position(level.getActors().get(0).getP());
		Position p = new Position(current);
		State<Position> initialState = new State<Position>(p);
		return initialState;

	}

	@Override
	public List<State<Position>> getGoalStates() {
		List<State<Position>> list = new ArrayList<State<Position>>();
		State<Position> goalState = new State<Position>(goal);
		list.add(goalState);
		return list;
	}
/**
 * get all the possible moves
 */
	@Override
	public HashMap<Action, State<Position>> getAllPossibleMoves(State<Position> state) {
		HashMap<Action, State<Position>> map = new HashMap<>();
		int i = state.getState().getI();
		int j = state.getState().getJ();
		
		// box move right or left need right and left to be clean
		if ((level.getRow() - 1 > j) && (j > 0)) {
			if ((level.getWarehouse()[i][j + 1].getClass().equals(Space.class)
					|| level.getWarehouse()[i][j + 1].getClass().equals(Destination_Box.class))
					&& (level.getWarehouse()[i][j - 1].getClass().equals(Space.class)
							|| level.getWarehouse()[i][j - 1].getClass().equals(Destination_Box.class))) {
				Position newpos = new Position(i, j + 1);
				Action act = new Action("move right");
				Action act2 = new Action("move left");
				map.put(act, new State<Position>(newpos));
				map.put(act2, new State<Position>(new Position(i, j-1)));
			}
		}
		if ((level.getColumn() - 1 > i) && (i > 0)) {
			if ((level.getWarehouse()[i - 1][j].getClass().equals(Space.class)
					|| level.getWarehouse()[i - 1][j].getClass().equals(Destination_Box.class))
					&& (level.getWarehouse()[i + 1][j].getClass().equals(Space.class)
							|| level.getWarehouse()[i + 1][j].getClass().equals(Destination_Box.class))) {
				Position newpos = new Position(i-1, j );
				Action act = new Action("move up");
				Action act2 = new Action("move down");
				map.put(act, new State<Position>(newpos));
				map.put(act2, new State<Position>(new Position(i+1, j)));
			}
		}
return map;
	}

}
