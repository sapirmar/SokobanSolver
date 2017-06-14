package plan;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import strips.Clause;
import strips.Plannable;
import strips.Predicate;

public class TempHeuristic implements HueristicMethod {

	@Override
	public Clause heuristic(Plannable plannable) {
		Clause kb=plannable.getKnowledgebase();
		List<Predicate> allBoxes=new LinkedList<Predicate>();
		List<Predicate> allTargets=new LinkedList<Predicate>();
		//Set<Predicate> box_cohsen=new HashSet<>();
		Clause goalstate=new Clause(null);

		for (Predicate predicate : kb.getPredicates()) {
			if(predicate.getType().equals("boxAt")){
				allBoxes.add(predicate);
			}
			if(predicate.getType().equals("targetAt")){
				allTargets.add(predicate);
			}
		}

		for (int i = 0; i < allBoxes.size(); i++) {
			Predicate predicate=new SokPredicate("boxAt",allBoxes.get(i).getId(),allTargets.get(i).getValue());
			goalstate.add(predicate);

		}

		return goalstate;
	}

}
