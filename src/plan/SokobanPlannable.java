package plan;

import java.util.HashMap;
import java.util.Set; 

import model.data.Level2D;
import strips.Action;
import strips.Clause;
import strips.Plannable;
import strips.Predicate;

public class SokobanPlannable implements Plannable {

	private Clause goal;
	private Clause kb;
	// Set<Action> satisfyingActions;
	// Action satisfyingAction;
	private Level2D level;
	private HashMap<String, String> mapBox = new HashMap<String, String>();// Key=id,Value=position
	// start

	public SokobanPlannable(Level2D level) {
		this.level = level;
	}

	@Override
	public Clause getGoal() {
		// Clause heuristic_goal=
		// היורסטי -שיחזיר מה הגול קלוס
		TempHeuristic huHeuristic = new TempHeuristic();
		this.goal = huHeuristic.heuristic(this);
		return goal;
	}

	@Override
	public Clause getKnowledgebase() {
		Clause kb = new Clause(null);
		int boxCount = 0;
		int targetCount = 0;
		for (int i = 0; i < level.getColumn(); i++) {
			for (int j = 0; j < level.getRow(); j++) {
				switch (level.getWarehouse()[i][j].getChar()) {
				case '#':
					kb.add(new Predicate("wallAt", "", i + "," + j));
					break;
				case ' ':
					kb.add(new Predicate("clearAt", "", i + "," + j));
					break;
				case 'A':
					kb.add(new Predicate("actorAt", "", i + "," + j));
					break;
				case '@': {
					boxCount++;
					kb.add(new Predicate("boxAt", "b" + boxCount, i + "," + j));
					mapBox.put("b" + boxCount, i + "," + j);
					break;
				}
				case 'o':
					targetCount++;
					kb.add(new Predicate("targetAt", "t" + targetCount, i + "," + j));
					break;
				}
			}
		}
		return kb;
	}

	@Override
	public Set<Action> getsatisfyingActions(Predicate top) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Action getsatisfyingAction(Predicate top) {
		Action satisfyAction = null;
		if (top.getType() == "boxAt") {
			satisfyAction = new MoveAction(top.getId(), mapBox.get(top.getId()), top.getValue());// (box_id,position
																									// start,position
																									// //
																									// goal)
		}
		

		return satisfyAction;
	}

	// get&set
	public Clause getKb() {
		return kb;
	}

	public void setKb(Clause kb) {
		this.kb = kb;
	}

	public Level2D getLevel() {
		return level;
	}

	public void setLevel(Level2D level) {
		this.level = level;
	}

	public HashMap<String, String> getMapBox() {
		return mapBox;
	}

	public void setMapBox(HashMap<String, String> mapBox) {
		this.mapBox = mapBox;
	}

	public void setGoal(Clause goal) {
		this.goal = goal;
	}

}
