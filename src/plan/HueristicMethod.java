package plan;

import strips.Clause;
import strips.Plannable;


public interface HueristicMethod {
	public Clause heuristic(Plannable plannable);
}
