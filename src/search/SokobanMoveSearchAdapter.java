package search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.data.Level2D;
import model.data.Position;
import model.items.Destination_Box;

import model.items.Space;

public class SokobanMoveSearchAdapter implements Searchable<Position> {

	private Level2D level;
	Position goal;
	Position current;

	public SokobanMoveSearchAdapter(Level2D level, Position goal,Position current) {
		this.level = level;
		this.goal = goal;
		this.current=current;

	}

	@Override
	public State<Position> getInitialState() {

		Position p=new Position(current);
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
 * get al the possible moves
 */
	@Override
	public HashMap<Action, State<Position>> getAllPossibleMoves(State<Position> state) {
		HashMap<Action, State<Position>> map = new HashMap<>();
		int i = state.getState().getI();
		int j = state.getState().getJ();
		if (i > 0) {
			if (level.getWarehouse()[i - 1][j].getClass().equals(Space.class)
					|| level.getWarehouse()[i - 1][j].getClass().equals(Destination_Box.class)) {
				Position newpos = new Position(i - 1, j);
				Action act = new Action("move up");
				map.put(act, new State<Position>(newpos));
			}
		}
		if (level.getColumn() - 1 > i) {

			if (level.getWarehouse()[i + 1][j].getClass().equals(Space.class)
					|| level.getWarehouse()[i + 1][j].getClass().equals(Destination_Box.class)) {
				Position newpos = new Position(i + 1, j);
				Action act = new Action("move down");
				map.put(act, new State<Position>(newpos));
			}
		}
		if (level.getRow() - 1 > j) {
			if (level.getWarehouse()[i][j + 1].getClass().equals(Space.class)
					|| level.getWarehouse()[i][j + 1].getClass().equals(Destination_Box.class)) {
				Position newpos = new Position(i, j + 1);
				Action act = new Action("move right");
				map.put(act, new State<Position>(newpos));
			}
		}
		if (j > 0) {
			if (level.getWarehouse()[i][j - 1].getClass().equals(Space.class)
					|| level.getWarehouse()[i][j - 1].getClass().equals(Destination_Box.class)) {
				Position newpos = new Position(i, j - 1);
				Action act = new Action("move left");
				map.put(act, new State<Position>(newpos));
			}
		}

		return map;
	}

}
