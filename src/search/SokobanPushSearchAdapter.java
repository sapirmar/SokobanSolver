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

//dont forget to check if there is a maslul from box to goal

public class SokobanPushSearchAdapter implements Searchable<SokobanState> {
	private Level2D level;
	private Position goal;
	private Position box;
	private SokobanMoveSearchAdapter move;

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
		return copy;
	}

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

	public void removeBoxFromCopyBoard(Level2D level2d, Position box) {
		if (level2d.getWarehouse()[box.getI()][box.getJ()].getClass().equals(Box.class)) {
			boolean flag = level2d.getWarehouse()[box.getI()][box.getJ()].ifOnDestination();
			if (flag == true) {
				level2d.getWarehouse()[box.getI()][box.getJ()] = new Destination_Box(
						new Position(box.getI(), box.getJ()));
			} else {
				level2d.getWarehouse()[box.getI()][box.getJ()] = new Space(new Position(box.getI(), box.getJ()));
			}

		}
		if(level2d.getWarehouse()[level.getActors().get(0).getP().getI()][level.getActors().get(0).getP().getJ()].getClass().equals(Actor.class)){

			boolean flag = level2d.getWarehouse()[level.getActors().get(0).getP().getI()][level.getActors().get(0).getP().getJ()].ifOnDestination();
			if (flag == true) {
				level2d.getWarehouse()[level.getActors().get(0).getP().getI()][level.getActors().get(0).getP().getJ()] = new Destination_Box(
						new Position(level.getActors().get(0).getP().getI(), level.getActors().get(0).getP().getJ()));
			} else {
				level2d.getWarehouse()[level.getActors().get(0).getP().getI()][level.getActors().get(0).getP().getJ()] = new Space(new Position(level.getActors().get(0).getP().getI(), level.getActors().get(0).getP().getJ()));
			}
		}

	}

	@Override
	public List<State<SokobanState>> getGoalStates() {
		List<State<SokobanState>> goalStates = new ArrayList<State<SokobanState>>();
		// box = goal;
		Level2D copylevel = copyLevel(level);
		Position boxOnTarget = goal;
		removeBoxFromCopyBoard(copylevel, box);
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
			// System.out.println("i: "+ i+" j:" +j);
		}

		return goalStates;

	}

	public boolean ifPossibleToPush(int i_actorNew,int j_actorNew ,int i_boxNew,int j_boxNew, String direction, HashMap<Action, State<SokobanState>> map)
	{

			if ((level.getWarehouse()[i_boxNew][j_boxNew].getClass().equals(Space.class))
					|| level.getWarehouse()[i_boxNew][j_boxNew].getClass().equals(Destination_Box.class)) {

				SokobanState s = new SokobanState(new Position(i_boxNew, j_boxNew),
						new Position(i_actorNew, j_actorNew));
				State<SokobanState> sokobanstate = new State<SokobanState>(s);

				Action act = new Action(direction);
				map.put(act, sokobanstate);
				return true ;

		}

		return false;
	}


	public void moveActorNearBoxByBfs(Position actorOld ,Position boxOld,int i_actorNew,int j_actorNew , HashMap<Action, State<SokobanState>> map)
	{
		if ((level.getWarehouse()[i_actorNew][j_actorNew].getClass().equals(Space.class))
				|| (level.getWarehouse()[i_actorNew][j_actorNew].getClass()
						.equals(Destination_Box.class))) {
			Solution solution;
			Position newpos = new Position(i_actorNew ,j_actorNew);

			move = new SokobanMoveSearchAdapter(level, newpos, actorOld);
			Searcher<Position> bfs = new BFS<Position>();
			solution = bfs.search(move);
			if (solution != null) {
				Action lastAction = solution.getActions().remove(solution.getActions().size()-1);

				//System.out.println("the last"+lastAction.getName());
				lastAction.setHistory(solution.getActions());
				SokobanState s = new SokobanState(new Position(boxOld),
						new Position(i_actorNew, j_actorNew));
				State<SokobanState> sokobanstate = new State<SokobanState>(s);
				map.put(lastAction, sokobanstate);

			}
		}

	}





	@Override
	public HashMap<Action, State<SokobanState>> getAllPossibleMoves(State<SokobanState> state) {
		HashMap<Action, State<SokobanState>> map = new HashMap<>();
		Position actor = state.getState().getActor_pos();
		Position boxP = state.getState().getBox_pos();

		//the actor left next to box
		if ((actor.getJ() + 1 == boxP.getJ()) && (actor.getI() == boxP.getI())){

		boolean res=ifPossibleToPush(actor.getI(),actor.getJ()+1,boxP.getI(), boxP.getJ()+1,"move right", map);
		if(res!=true)
		{
			//bfs to up to box
			moveActorNearBoxByBfs(actor, boxP, boxP.getI()-1, boxP.getJ(), map);
			//bfs to down to box
			moveActorNearBoxByBfs(actor, boxP, boxP.getI()+1, boxP.getJ(), map);
		}

		}
		//the actor right next to box
		 if(((actor.getJ() - 1 == boxP.getJ()) && (actor.getI() == boxP.getI())))
		{
			boolean res=ifPossibleToPush(actor.getI(),actor.getJ()-1,boxP.getI(), boxP.getJ()-1,"move left", map);
			if (res!=true)
			{
				//bfs to up to box
				moveActorNearBoxByBfs(actor, boxP, boxP.getI()-1, boxP.getJ(), map);
				//bfs to down to box
				moveActorNearBoxByBfs(actor, boxP, boxP.getI()+1, boxP.getJ(), map);
			}
		}
		//the actor down to the box
		if (((actor.getJ() == boxP.getJ()) && (actor.getI() == boxP.getI()+1)))
		{
			boolean res=ifPossibleToPush(actor.getI()-1,actor.getJ(),boxP.getI()-1, boxP.getJ(),"move up", map);
			if (res!=true)
			{
				//bfs right to box
				moveActorNearBoxByBfs(actor, boxP, boxP.getI(), boxP.getJ()+1, map);
				//bfs left to box
				moveActorNearBoxByBfs(actor, boxP, boxP.getI(), boxP.getJ()-1, map);

			}
		}
		//the actor up to the box
		if (((actor.getJ() == boxP.getJ()) && (actor.getI() == boxP.getI()-1)))
		{
			boolean res=ifPossibleToPush(actor.getI()+1,actor.getJ(),boxP.getI()+1, boxP.getJ(),"move down", map);
			if (res!=true)
			{
				//bfs right to box
				moveActorNearBoxByBfs(actor, boxP, boxP.getI(), boxP.getJ()+1, map);
				//bfs left to box
				moveActorNearBoxByBfs(actor, boxP, boxP.getI(), boxP.getJ()-1, map);
			}

		}
/*


		{

			// state:the actor and box move on step right
			if ((level.getWarehouse()[boxP.getI()][boxP.getJ() + 1].getClass().equals(Space.class))
					|| level.getWarehouse()[boxP.getI()][boxP.getJ() + 1].getClass().equals(Destination_Box.class)) {

				SokobanState s = new SokobanState(new Position(boxP.getI(), boxP.getJ() + 1),
						new Position(actor.getI(), actor.getJ() + 1));
				State<SokobanState> sokobanstate = new State<SokobanState>(s);

				Action act = new Action("move right");
				map.put(act, sokobanstate);
			} else {// אם הקופסה לא יכולה לזוז ימינה נזיז את השחקן למעלה או למטה
					// בעזרת בי אפ אס וניצור סטייט חדש עם עידכון היסטוריית
					// הצעדים מהבי אפ אס
					// השחקן יכול להיות מעל הקופסה
				if ((level.getWarehouse()[boxP.getI() - 1][boxP.getJ()].getClass().equals(Space.class))
						|| (level.getWarehouse()[boxP.getI() - 1][boxP.getJ()].getClass()
								.equals(Destination_Box.class))) {
					Solution solution;
					Position newpos = new Position(boxP.getI() - 1, boxP.getJ());
					move = new SokobanMoveSearchAdapter(level, newpos, actor);
					Searcher<Position> bfs = new BFS<Position>();
					solution = bfs.search(move);
					if (solution != null) {
						Action lastAction = solution.getActions().remove(0);
						lastAction.setHistory(solution.getActions());
						SokobanState s = new SokobanState(new Position(boxP.getI(), boxP.getJ()),
								new Position(boxP.getI() - 1, boxP.getJ()));
						State<SokobanState> sokobanstate = new State<SokobanState>(s);
						map.put(lastAction, sokobanstate);

					}

				}
				// השחקן יכול להיות מתחת לקופסא
				if ((level.getWarehouse()[boxP.getI() + 1][boxP.getJ()].getClass().equals(Space.class))
						|| (level.getWarehouse()[boxP.getI() + 1][boxP.getJ()].getClass()
								.equals(Destination_Box.class))) {
					Solution solution;
					Position newpos = new Position(boxP.getI() + 1, boxP.getJ());
					move = new SokobanMoveSearchAdapter(level, newpos, actor);
					Searcher<Position> bfs = new BFS<Position>();
					solution = bfs.search(move);

					if (solution != null) {
						Action lastAction = solution.getActions().remove(0);
						lastAction.setHistory(solution.getActions());
						SokobanState s = new SokobanState(new Position(boxP.getI(), boxP.getJ()),
								new Position(boxP.getI() + 1, boxP.getJ()));
						State<SokobanState> sokobanstate = new State<SokobanState>(s);
						map.put(lastAction, sokobanstate);

					}
				}

			}
		}
*/
		return map;
	}


}
