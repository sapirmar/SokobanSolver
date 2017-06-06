package search;

import model.data.Position;

public class SokobanState {

private Position box_pos;
private Position actor_pos;



public Position getBox_pos() {
	return box_pos;
}

public void setBox_pos(Position box_pos) {
	this.box_pos = box_pos;
}

public Position getActor_pos() {
	return actor_pos;
}

public void setActor_pos(Position actor_pos) {
	this.actor_pos = actor_pos;
}

public SokobanState(Position box_pos, Position actor_pos) {

	this.box_pos = box_pos;
	this.actor_pos = actor_pos;
}



@Override
public String toString() {
	return "SokobanState [box_pos=" + box_pos + ", actor_pos=" + actor_pos + "]";
}

@Override
public int hashCode() {
	return toString().hashCode();
}

@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	SokobanState other = (SokobanState) obj;
	if (actor_pos == null) {
		if (other.actor_pos != null)
			return false;
	} else if (!actor_pos.equals(other.actor_pos))
		return false;
	if (box_pos == null) {
		if (other.box_pos != null)
			return false;
	} else if (!box_pos.equals(other.box_pos))
		return false;
	return true;
}



}
