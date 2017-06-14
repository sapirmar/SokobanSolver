package search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.data.Level2D;
import model.data.Position;
import model.items.Actor;
import model.items.Box;
import model.items.Destination_Box;
import model.items.Items;
import model.items.Space;
import model.items.Wall;



public class SokobanPushSearchAdapter implements Searchable<SokobanState> {
	private Level2D level;
	private Position goal;
	private Position box;

/*
	public Level2D copyLevel(Level2D level) {
		HashMap<Character, Items> hm;
		hm = new HashMap<Character, Items>();
		hm.put('A', new Actor());
		hm.put('@', new Box());
		hm.put('#', new Wall());
		hm.put(' ', new Space());
		hm.put('o', new Destination_Box());
		Level2D copy = new Level2D();
		Items[][] items = new Items[level.getColumn()][level.getRow()];

		char[][] ch = new char[level.getColumn()][level.getRow()];

		for (int i = 0; i < level.getColumn(); i++) {
			for (int j = 0; j < level.getRow(); j++) {
				ch[i][j] = level.getWarehouse()[i][j].getChar();

				items[i][j] = hm.get(ch[i][j]);
				items[i][j].setP(new Position(i, j));

			}

		}
		copy.setRow(level.getRow());
		copy.setColumn(level.getColumn());
		copy.setWarehouse(items);
		copy.actors=new ArrayList<Actor>();
		copy.getActors().add(new Actor(new Position(level.getActors().get(0).getP())));

		return copy;
	}
*/
	public SokobanPushSearchAdapter(Level2D level, Position goal, Position box) {
		this.level = level;
		this.box = box;
		this.goal = goal;
	}

	@Override
	public State<SokobanState> getInitialState() {
		SokobanState state = new SokobanState(box, level.getActors().get(0).getP());
		return new State<SokobanState>(state);
	}

	public void removeBoxAndActorFromCopyBoard(Level2D level2d, Position box,Position actor) {
		if (level2d.getWarehouse()[box.getI()][box.getJ()].getClass().equals(Box.class)) {
			boolean flag = level2d.getWarehouse()[box.getI()][box.getJ()].ifOnDestination();
			if (flag == true) {
				level2d.getWarehouse()[box.getI()][box.getJ()] = new Destination_Box(
						new Position(box.getI(), box.getJ()));
			} else {
				level2d.getWarehouse()[box.getI()][box.getJ()] = new Space(new Position(box.getI(), box.getJ()));
			}

		}
		if (level2d.getWarehouse()[actor.getI()][actor.getJ()].getClass().equals(Actor.class)) {
			boolean flag = level2d.getWarehouse()[actor.getI()][actor.getJ()].ifOnDestination();
			if (flag == true) {
				level2d.getWarehouse()[actor.getI()][actor.getJ()] = new Destination_Box(
						new Position(actor.getI(), actor.getJ()));
			} else {
				level2d.getWarehouse()[actor.getI()][actor.getJ()] = new Space(new Position(actor.getI(), actor.getJ()));
			}

		}

	}

	@Override
	public List<State<SokobanState>> getGoalStates() {
		List<State<SokobanState>> goalStates = new ArrayList<State<SokobanState>>();

		//Level2D copylevel = copyLevel(level);
		Level2D copylevel = level.copyLevel(level);
		Position boxOnTarget = goal;
		removeBoxAndActorFromCopyBoard(copylevel, box,level.getActors().get(0).getP());
		int i = boxOnTarget.getI();
		int j = boxOnTarget.getJ();
		if (i > 0) {
			if (copylevel.getWarehouse()[i - 1][j].getClass().equals(Space.class)
					|| copylevel.getWarehouse()[i - 1][j].getClass().equals(Destination_Box.class)) {
				goalStates.add(new State<SokobanState>(new SokobanState(boxOnTarget, new Position(i - 1, j))));
			}
		}
		if (copylevel.getColumn() - 1 > i) {

			if (copylevel.getWarehouse()[i + 1][j].getClass().equals(Space.class)
					|| copylevel.getWarehouse()[i + 1][j].getClass().equals(Destination_Box.class)) {
				goalStates.add(new State<SokobanState>(new SokobanState(boxOnTarget, new Position(i + 1, j))));
			}
		}

		if (copylevel.getRow() - 1 > j) {
			if (copylevel.getWarehouse()[i][j + 1].getClass().equals(Space.class)
					|| copylevel.getWarehouse()[i][j + 1].getClass().equals(Destination_Box.class)) {
				goalStates.add(new State<SokobanState>(new SokobanState(boxOnTarget, new Position(i, j + 1))));
			}
		}

		if (j > 0) {
			if (copylevel.getWarehouse()[i][j - 1].getClass().equals(Space.class)
					|| copylevel.getWarehouse()[i][j - 1].getClass().equals(Destination_Box.class)) {
				goalStates.add(new State<SokobanState>(new SokobanState(boxOnTarget, new Position(i, j - 1))));
			}

		}

		return goalStates;

	}

	public void ifPossibleToPush(int i_actorNew, int j_actorNew, int i_boxNew, int j_boxNew, String direction,
			HashMap<Action, State<SokobanState>> map) {
		//Level2D copylevel = copyLevel(level);
		Level2D copylevel =level.copyLevel(level);


		removeBoxAndActorFromCopyBoard(copylevel,box, level.getActors().get(0).getP());

		if ((copylevel.getWarehouse()[i_boxNew][j_boxNew].getClass().equals(Space.class))
				|| copylevel.getWarehouse()[i_boxNew][j_boxNew].getClass().equals(Destination_Box.class)) {

			SokobanState s = new SokobanState(new Position(i_boxNew, j_boxNew), new Position(i_actorNew, j_actorNew));
			State<SokobanState> sokobanstate = new State<SokobanState>(s);

			Action act = new Action(direction);
			Action actNew=ifMapContainsKey(act, map);

			sokobanstate.setCost(sokobanstate.getCost()+1);
			map.put(actNew, sokobanstate);


		}


	}

	public void moveActorNearBoxByBfs(Position actorOld, Position boxOld, int i_actorNew, int j_actorNew,
			HashMap<Action, State<SokobanState>> map ) {
		Action lastAction= new Action();
		SokobanMoveSearchAdapter move;

		Level2D copylevel=level.copyLevel(level);//////////////////////////////////////////////////////
		removeBoxAndActorFromCopyBoard(copylevel, box, level.getActors().get(0).getP());
		copylevel.getWarehouse()[boxOld.getI()][boxOld.getJ()]=new Box(new Position(boxOld.getI(), boxOld.getJ()));
		if ((copylevel.getWarehouse()[i_actorNew][j_actorNew].getClass().equals(Space.class))
				|| (copylevel.getWarehouse()[i_actorNew][j_actorNew].getClass().equals(Destination_Box.class))) {
			Solution solution;
			Position newpos = new Position(i_actorNew, j_actorNew);
			Position actorNew=new Position(i_actorNew, j_actorNew);

			move = new SokobanMoveSearchAdapter(copylevel, newpos, actorOld);
			Searcher<Position> bfs = new BFS<Position>();
			solution=new Solution();
			solution = bfs.search(move);
			if(solution!=null)
			if (solution.getActions() != null) {



				lastAction = solution.getActions().remove(0);


				lastAction.setHistory(solution.getActions());
				Action actNew=ifMapContainsKey(lastAction, map);

				actNew.setHistory(solution.getActions());


				SokobanState s = new SokobanState(new Position(boxOld), new Position(i_actorNew, j_actorNew));
				State<SokobanState> sokobanstate = new State<SokobanState>(s);


				sokobanstate.setCost(solution.getActions().size());


				map.put(actNew, sokobanstate);


			}
		}

	}
	public Action ifMapContainsKey(Action act, HashMap<Action, State<SokobanState>> AllPossibleMoves)
	{
		if(AllPossibleMoves.containsKey(act)){
			Action a=act;
			int i=0;
			while(true){
				Action c= new Action(act.getName()+" "+i);

				if(!AllPossibleMoves.containsKey(c))
				{

					return c;


				}


				i++;

			}
		}
		else {
			return act;
		}
	}
	@Override
	public HashMap<Action, State<SokobanState>> getAllPossibleMoves(State<SokobanState> state) {
		HashMap<Action, State<SokobanState>> map = new HashMap<>();
		Position actor = state.getState().getActor_pos();
		Position boxP = state.getState().getBox_pos();
		System.out.println("the States: " + state);
		// the actor left next to box
		if ((actor.getJ() + 1 == boxP.getJ()) && (actor.getI() == boxP.getI())) {
			 ifPossibleToPush(actor.getI(), actor.getJ() + 1, boxP.getI(), boxP.getJ() + 1, "move right",
					map);
			// bfs to up to box
			moveActorNearBoxByBfs(actor, boxP, boxP.getI() - 1, boxP.getJ(), map);
			// bfs to down to box
			moveActorNearBoxByBfs(actor, boxP, boxP.getI() + 1, boxP.getJ(), map);


		}
		// the actor right next to box
		else if (((actor.getJ() - 1 == boxP.getJ()) && (actor.getI() == boxP.getI()))) {
			 ifPossibleToPush(actor.getI(), actor.getJ() - 1, boxP.getI(), boxP.getJ() - 1, "move left",
					map);
			// bfs to up to box
			moveActorNearBoxByBfs(actor, boxP, boxP.getI() - 1, boxP.getJ(), map);
			// bfs to down to box
			moveActorNearBoxByBfs(actor, boxP, boxP.getI() + 1, boxP.getJ(), map);

		}
		// the actor down to the box
		else if (((actor.getJ() == boxP.getJ()) && (actor.getI() == boxP.getI() + 1))) {
			 ifPossibleToPush(actor.getI() - 1, actor.getJ(), boxP.getI() - 1, boxP.getJ(), "move up",
					map);
			// bfs right to box
			moveActorNearBoxByBfs(actor, boxP, boxP.getI(), boxP.getJ() + 1, map);
			// bfs left to box
			moveActorNearBoxByBfs(actor, boxP, boxP.getI(), boxP.getJ() - 1, map);

		}
		// the actor up to the box
		else if (((actor.getJ() == boxP.getJ()) && (actor.getI() == boxP.getI() - 1))) {
			 ifPossibleToPush(actor.getI() + 1, actor.getJ(), boxP.getI() + 1, boxP.getJ(), "move down",
					map);
			// bfs right to box
			moveActorNearBoxByBfs(actor, boxP, boxP.getI(), boxP.getJ() + 1, map);
			// bfs left to box
			moveActorNearBoxByBfs(actor, boxP, boxP.getI(), boxP.getJ() - 1, map);


		}
		else{// if the actor doesn't near the box
			BoxState boxState=new BoxState(level, goal, boxP);
			BFS<Position> bfs=new BFS<>();
			Solution sol=bfs.search(boxState);
			if(sol!=null){
			Action first=sol.getActions().get(0);
			String moveup="move up";
			String movedown="move down";
			String moveleft="move left";
			String moveright="move right";

			if(first.getName().toLowerCase().contains(moveup.toLowerCase())){
				// bfs to down to box
				moveActorNearBoxByBfs(actor, boxP, boxP.getI() + 1, boxP.getJ(), map);
			}
			else if(first.getName().toLowerCase().contains(movedown.toLowerCase())){
				// bfs to up to box
				moveActorNearBoxByBfs(actor, boxP, boxP.getI() - 1, boxP.getJ(), map);
			}
			else if(first.getName().toLowerCase().contains(moveright.toLowerCase())){
				// bfs left to box
				moveActorNearBoxByBfs(actor, boxP, boxP.getI(), boxP.getJ() - 1, map);
			}
			else if(first.getName().toLowerCase().contains(moveleft.toLowerCase())){
				// bfs right to box
				moveActorNearBoxByBfs(actor, boxP, boxP.getI(), boxP.getJ() + 1, map);
			}
			}

		}


		return map;
	}

}
