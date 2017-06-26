package plan;

import strips.Action;
import strips.Clause;

public class MoveAction extends Action {
	String from;
	String goal;
	String who;

	public MoveAction(String who, String from, String goal) {
		super(who, from, goal);
		this.who = who;
		this.from = from;
		this.goal = goal;
		updatePreconditions();
		updatePostEffect();

	}
	
	public void updatePreconditions() {
		SokPredicate boxAt = new SokPredicate("boxAt", who, from);
		SokPredicate targetAt = new SokPredicate("targetAt", "?", goal);
		SokPredicate clear = new SokPredicate("clear", "?", goal);
		SokPredicate clearPath = new ClearPathPrediacte(from, goal);
		Clause preconditions = new Clause(boxAt, targetAt, clear, clearPath);

		setPreconditions(preconditions);
		getPreconditions().add(boxAt);
		getPreconditions().add(targetAt);
		getPreconditions().add(clear);
		getPreconditions().add(clearPath);
	}

	public void updatePostEffect()
	{
		SokPredicate boxAt=new SokPredicate("boxAt", who, goal);
		Clause postCondtions=new Clause(boxAt);
		setEffects(postCondtions);

	}

	@Override
	public String toString() {
		return "MoveAction [from=" + from + ", goal=" + goal + ", who=" + who + "]";

	}

}
