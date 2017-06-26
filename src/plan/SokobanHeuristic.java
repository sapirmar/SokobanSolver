package plan;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import strips.Clause;
import strips.Plannable;
import strips.Predicate;

public class SokobanHeuristic implements HueristicMethod {
/**
 * the huristic 
 * choose for each box the target
 */
	@Override
	public Clause heuristic(Plannable plannable) {
		Clause kb=plannable.getKnowledgebase();
		List<Predicate> allBoxes=new LinkedList<Predicate>();
		List<Predicate> allTargets=new LinkedList<Predicate>();
		Set<Predicate> box_cohsen=new HashSet<>();


		for (Predicate predicate : kb.getPredicates()) {
			if(predicate.getType().equals("boxAt")){
				allBoxes.add(predicate);
			}
			if(predicate.getType().equals("targetAt")){
				allTargets.add(predicate);
			}
		}

		for(Predicate target:allTargets)
		{
			int min=Integer.MAX_VALUE;
			for(Predicate box: allBoxes){

				int temp=computeDistance(box.getValue(), target.getValue());
				if(min>temp)
				{
					min=temp;
					Predicate boxC=box;
				}
			}


		}


		return null;
	}
	public int computeDistance(String boxPos,String targetPos)
	{
		String[] box=boxPos.split(",");
		String[] target=targetPos.split(",");
		int box_i=Integer.parseInt(box[0]);
		int box_j=Integer.parseInt(box[1]);
		int target_i=Integer.parseInt(target[0]);
		int target_j=Integer.parseInt(target[1]);
		int res=Math.abs(box_i-target_i)+Math.abs(box_j-target_j);
		return res;


	}
}
