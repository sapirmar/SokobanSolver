package solver;

import java.beans.XMLEncoder;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import model.data.ILevelCreator;

import model.data.Level2D;

import model.data.ObjectLevelCreator;

import model.data.Position;
import model.data.TextLevelCreator;

import model.data.XmlLevelCreator;

import model.items.Box;
import model.items.Destination_Box;

import model.items.Space;
import plan.MoveAction;
import plan.SokobanPlannable;
import search.BFS;
import search.SokobanPushSearchAdapter;
import search.SokobanState;
import search.Solution;
import strips.Action;
import strips.Strips;

public class SokobanSolver {
	private LinkedList<search.Action> allActions = new LinkedList<search.Action>();

	private HashMap<String, ILevelCreator> hm_loader;

	public SokobanSolver() {
		hm_loader = new HashMap<String, ILevelCreator>();
		hm_loader.put("txt", new TextLevelCreator());
		hm_loader.put("xml", new XmlLevelCreator());
		hm_loader.put("obj", new ObjectLevelCreator());

	}

	public Level2D readLevel(String path) {
		String type;
		// give us the 3 last letters indicate the type of file
		type = path.substring(path.length() - 3);
		// load according to the type
		Level2D level = (Level2D) hm_loader.get(type).create(path);
		return level;

	}

	public void writeToFile(String path) {
		String type;
		// give us the 3 last letters indicate the type of file
		type = path.substring(path.length() - 3);
		try {

			switch (type) {
			case "txt": {
				BufferedWriter writer = new BufferedWriter(new FileWriter(path));
				for (search.Action a : allActions) {
					writer.write(a.getName());
					writer.newLine();

				}
				writer.close();
				break;
			}
			case "xml": {
				XMLEncoder xml = new XMLEncoder(new FileOutputStream(path));
				xml.writeObject(allActions);
				xml.close();

				break;
			}
			case "obj": {
				ObjectOutputStream oi = new ObjectOutputStream(new FileOutputStream(path));
				oi.writeObject(allActions);
				oi.close();

				break;
			}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void updateLevelAfterChange(Level2D copylevel, int box_i, int box_j, int boxGoal_i, int boxGoal_j,
			int actorOld_i, int actorOld_j, search.Action last) {
		String moveup = "move up";
		String movedown = "move down";
		String moveleft = "move left";
		String moveright = "move right";
		//// delete actor and box old positions
		boolean actorOldFlag = copylevel.getWarehouse()[actorOld_i][actorOld_j].getP().getFlagDestination();

		if (actorOldFlag == true) {
			copylevel.getWarehouse()[actorOld_i][actorOld_j] = new Destination_Box(
					new Position(actorOld_i, actorOld_j));

		} else {
			copylevel.getWarehouse()[actorOld_i][actorOld_j] = new Space(new Position(actorOld_i, actorOld_j));
		}
		boolean boxOldFlag = copylevel.getWarehouse()[box_i][box_j].getP().getFlagDestination();
		if (boxOldFlag == true) {
			copylevel.getWarehouse()[box_i][box_j] = new Destination_Box(new Position(box_i, box_j));

		} else {
			copylevel.getWarehouse()[actorOld_i][actorOld_j] = new Space(new Position(box_i, box_j));
		}

		// update box new position
		for (Box b : copylevel.boxes) {
			if ((b.getP().getI() == box_i) && (b.getP().getJ() == box_j)) {
				b.setP(new Position(boxGoal_i, boxGoal_j));
				// the box on target->flag=true
				b.getP().setFlagDestination(true);
			}
		}
		//
		//// update the player position after move
		if (last.getName().toLowerCase().contains(moveup.toLowerCase())) {
			// update actor new place down to box
			copylevel.getActors().get(0).setP(new Position(boxGoal_i + 1, boxGoal_j));

		} else if (last.getName().toLowerCase().contains(movedown.toLowerCase())) {
			// update actor new place up to box
			copylevel.getActors().get(0).setP(new Position(boxGoal_i - 1, boxGoal_j));
		} else if (last.getName().toLowerCase().contains(moveright.toLowerCase())) {
			// update actor new left place to box
			copylevel.getActors().get(0).setP(new Position(boxGoal_i, boxGoal_j - 1));
		} else if (last.getName().toLowerCase().contains(moveleft.toLowerCase())) {
			// update actor new place right to box
			copylevel.getActors().get(0).setP(new Position(boxGoal_i, boxGoal_j + 1));

		}

	}

	public Solution solve(Level2D level) {
		SokobanPlannable plannable = new SokobanPlannable((Level2D) level);
		Level2D copylevel = level.copyLevel(level);

		Strips strips = new Strips();
		List<Action> actions = strips.plan(plannable);

		for (Action action : actions) {
			if (action instanceof MoveAction) {

				String[] posGoal = action.getValue().split(",");
				String boxPositionFromap = plannable.getMapBox().get(action.getType());

				String[] posBox = boxPositionFromap.split(",");
				int boxGoal_i = Integer.parseInt(posGoal[0]);
				int boxGoal_j = Integer.parseInt(posGoal[1]);
				int actorOld_i = copylevel.getActors().get(0).getP().getI();
				int actorOld_j = copylevel.getActors().get(0).getP().getJ();
				int box_i = Integer.parseInt(posBox[0]);
				int box_j = Integer.parseInt(posBox[1]);
				Position boxPosition = new Position(box_i, box_j);
				Position goalPosition = new Position(boxGoal_i, boxGoal_j);
				SokobanPushSearchAdapter push = new SokobanPushSearchAdapter(copylevel, goalPosition, boxPosition);
				BFS<SokobanState> bfs = new BFS<>();
				Solution sol = bfs.search(push);
				if(sol!=null){
				allActions.addAll(sol.getActions());
				search.Action last = sol.getActions().get(sol.getActions().size() - 1);

				updateLevelAfterChange(copylevel, box_i, box_j, boxGoal_i, boxGoal_j, actorOld_i, actorOld_j, last);
				}// System.out.println(sol);
				else//solution =null
				{
					return null;
				}

				// System.out.println("last "+last.getName());

			}
		}
		Solution sol=new Solution();
		sol.setActions(allActions);

		return sol;
		}



}
