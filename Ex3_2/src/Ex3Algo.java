

import java.awt.*;
import java.util.ArrayList;

import exe.ex3.game.Game;
import exe.ex3.game.GhostCL;
import exe.ex3.game.PacManAlgo;
import exe.ex3.game.PacmanGame;

/**
 * This is the major algorithmic class for Ex3 - the PacMan game:
 *
 * This code is a very simple example (random-walk algorithm).
 * Your task is to implement (here) your PacMan algorithm.
 */
public class Ex3Algo implements PacManAlgo {
	private int _count;

	public Ex3Algo() {
		_count = 0;
	}

	@Override
	/**
	 *  Add a short description for the algorithm as a String.
	 */
	public String getInfo() {

		return " sdkjsgc";
	}

	@Override
	/**
	 * This ia the main method - that you should design, implement and test.
	 */
	public int move(PacmanGame game) {

		if (_count == 0 || _count == 300) {
			int code = 0;
			int[][] board = game.getGame(0);
			printBoard(board);
			int blue = Game.getIntColor(Color.BLUE, code);
			int pink = Game.getIntColor(Color.PINK, code);
			int black = Game.getIntColor(Color.BLACK, code);
			int green = Game.getIntColor(Color.GREEN, code);
			System.out.println("Blue=" + blue + ", Pink=" + pink + ", Black=" + black + ", Green=" + green);
			String pos = game.getPos(code).toString();
			System.out.println("Pacman coordinate: " + pos);
			GhostCL[] ghosts = game.getGhosts(code);
			printGhosts(ghosts);
			int up = Game.UP, left = Game.LEFT, down = Game.DOWN, right = Game.RIGHT;
		}

		int dir = randomDir();
		int[][] board = game.getGame(0);
		String pos = game.getPos(0);
		GhostCL[] ghosts = game.getGhosts(0);
		Pixel2D pacman = new Index2D(pos);
		Map map = new Map(board);
		Pixel2D minGhost = closeGhost(map, ghosts, pacman);
		if (ghosts[0].remainTimeAsEatable(0) > 1) {
			if (pacman.equals(new Index2D(11, 13))) {
				return 0;
			}
			dir = eatGhost(map, pacman, minGhost);

			return dir;
		}
		int distanceToGhost = map.shortestPath(pacman, minGhost, 1).length;

		if (distanceToGhost>7) {
			dir = eatPoints(map, pacman);
		} else {
			dir = run(map,pacman,ghosts);
		}
		_count++;

		return dir;
	}

	private int run(Map map, Pixel2D pacman, GhostCL[] gs) {
       Pixel2D nearGhost = closeGhost(map, gs,pacman);
	   Pixel2D point1 = new Index2D(1,1);
		Pixel2D point2 = new Index2D(21,1);
		Pixel2D point3 = new Index2D(1,19);
		Pixel2D point4 = new Index2D(21,21);
//		Pixel2D point5 = new Index2D(22,18);

//		Pixel2D [] distantPoints = {point1,point2,point3,point4};
		ArrayList<Pixel2D> distantPoints = new ArrayList<>();
		distantPoints.add(point1);
		distantPoints.add(point2);
		distantPoints.add(point3);
		distantPoints.add(point4);
//		distantPoints.add(point5);
		int max = 0;
		int index = 0;
		Pixel2D maxPixel = null;
		for (int i = 0; i < 2 ; i++) {
			Pixel2D[] shortPath = map.shortestPath(nearGhost, distantPoints.get(i), 1);
			if (shortPath.length>max){
				max = shortPath.length;
				maxPixel = shortPath[i];
				index = i;
				System.out.println(shortPath[i]);
			}
		}

			while (map.shortestPath(pacman,nearGhost,1).length - 1 + map.shortestPath(nearGhost,maxPixel,1).length == map.shortestPath(pacman,maxPixel,1).length){
				max = 0;
				distantPoints.remove(index);
				System.out.println("baba");
				for (int i = 0; i < distantPoints.size()-1 ; i++) {
					Pixel2D[] shortPath = map.shortestPath(nearGhost, distantPoints.get(i), 1);
					if (shortPath.length>max){
						max = shortPath.length;
						maxPixel = shortPath[i];
						index = i;
						System.out.println(i);
					}
				}
			}
//		Pixel2D maxPixel = map.nearGreenPoint(pacman,1);
//		if (maxPixel == null){
//			return eatPoints(map,pacman);
//		}
		Pixel2D[] shortPath = map.shortestPath(maxPixel,pacman, 1);
		Pixel2D dir = shortPath[1];
		if(shortPath.length<2) {
			dir = shortPath[0];
		}
            if (map.isCyclic() == false) {
                if (pacman.getX() == dir.getX()) {
                    if (pacman.getY() > dir.getY()) {
                        return Game.DOWN;
                    } else {
                        return Game.UP;
                    }
                } else {
                    if (pacman.getX() > dir.getX()) {
                        return Game.LEFT;
                    } else {
                        return Game.RIGHT;
                    }
                }
            } else {
                if (pacman.getX() == dir.getX()) {
                    if (pacman.getY() > dir.getY()) {
                        if (pacman.getY() == map.getHeight() - 1 && dir.getY() == 0) {
                            return Game.UP;
                        }
                        return Game.DOWN;
                    } else {
                        if (pacman.getY() == 0 && dir.getY() == map.getHeight() - 1) {
                            return Game.DOWN;
                        }
                        return Game.UP;
                    }
                } else {
                    if (pacman.getX() > dir.getX()) {
                        if (pacman.getX() == map.getWidth() - 1 && dir.getX() == 0) {
                            return Game.RIGHT;
                        }
                        return Game.LEFT;
                    } else {
                        if (pacman.getX() == 0 && dir.getX() == map.getWidth() - 1) {
                            return Game.LEFT;
                        }
                        return Game.RIGHT;
                    }
                }
            }
        }

	private int eatPoints(Map map,Pixel2D pacman){
		ArrayList<Pixel2D> allPoints=new ArrayList<>();
		//map.arraypoints(allPoints);
		Pixel2D closePoint=map.nearPoint(pacman,1);
		int min=Integer.MAX_VALUE;
//		for (int i = 0; i <allPoints.size() ; i++) {
//			Pixel2D [] shortPathPoint=map.shortestPath(pacman,allPoints.get(i),1);
//			if(shortPathPoint.length<min){
//				min=shortPathPoint.length;
//				closePoint=allPoints.get(i);
//			}
//		}
		Pixel2D [] shortPath=map.shortestPath(closePoint,pacman,1);
		Pixel2D dir=shortPath[1];
		if(map.isCyclic()==false){
			if (pacman.getX() == dir.getX()) {
				if (pacman.getY() > dir.getY()) {
					return Game.DOWN;
				} else {
					return Game.UP;
				}
			} else {
				if (pacman.getX() > dir.getX()) {
					return Game.LEFT;
				} else {
					return Game.RIGHT;
				}
			}
		}
		else {
			if (pacman.getX() == dir.getX()) {
				if (pacman.getY() > dir.getY()) {
					if (pacman.getY() == map.getHeight() - 1 && dir.getY() == 0) {
						return Game.UP;
					}
					return Game.DOWN;
				} else {
					if (pacman.getY() == 0 && dir.getY() == map.getHeight() - 1) {
						return Game.DOWN;
					}
					return Game.UP;
				}
			} else {
				if (pacman.getX() > dir.getX()) {
					if (pacman.getX() == map.getWidth() - 1 && dir.getX() == 0) {
						return Game.RIGHT;
					}
					return Game.LEFT;
				} else {
					if (pacman.getX() == 0 && dir.getX() == map.getWidth() - 1) {
						return Game.LEFT;
					}
					return Game.RIGHT;
				}
			}
		}


	}



	private Pixel2D closeGhost(Map map,GhostCL[] gs,Pixel2D pacman){
		GhostCL g = gs[0];
		Pixel2D ghost = new Index2D(g.getPos(0));
		Pixel2D[] positionGhost = map.shortestPath(pacman,ghost,1 );
		Pixel2D minGhost=ghost;
		int min=positionGhost.length;
		int index = 0;
		for(int i=1;i<gs.length;i++) {
			 g = gs[i];
			 ghost = new Index2D(g.getPos(0));
			 positionGhost = map.shortestPath(pacman,ghost,1 );
			 if(min > positionGhost.length){
				 min = positionGhost.length;
				 minGhost = ghost;
				 index = i;
			 }
		}
		System.out.println("The closet ghost is :" + index);
		return minGhost;

	}
	private int eatGhost (Map map,Pixel2D pacman,Pixel2D ghost){

		Pixel2D[] shortPosition = map.shortestPath(ghost,pacman,1);
//		if (shortPosition.length == 1){
//			return Game.DOWN;
//		}
		Pixel2D dir=shortPosition[1];

		if(map.isCyclic()==false){
			if (pacman.getX() == dir.getX()) {
				if (pacman.getY() > dir.getY()) {
					return Game.DOWN;
				} else {
					return Game.UP;
				}
			} else {
				if (pacman.getX() > dir.getX()) {
					return Game.LEFT;
				} else {
					return Game.RIGHT;
				}
			}
		}
		else{
			if (pacman.getX() == dir.getX()) {
				if (pacman.getY() > dir.getY()) {
					if(pacman.getY()== map.getHeight()-1 && dir.getY()== 0){
						return Game.UP;
					}
					return Game.DOWN;
				} else {
					if(pacman.getY()== 0 && dir.getY()== map.getHeight()-1){
						return Game.DOWN;
					}
					return Game.UP;
				}
			} else {
				if (pacman.getX() > dir.getX()) {
					if(pacman.getX()==map.getWidth()-1&&dir.getX()==0){
						return Game.RIGHT;
					}
					return Game.LEFT;
				} else {
					if(pacman.getX()==0&&dir.getX()==map.getWidth()-1){
						return Game.LEFT;
					}
					return Game.RIGHT;
				}
			}

		}

	}

	private static void printBoard(int[][] b) {
		for(int y =0;y<b[0].length;y++){
			for(int x =0;x<b.length;x++){
				int v = b[x][y];
				System.out.print(v+"\t");
			}
			System.out.println();
		}
	}
	private static void printGhosts(GhostCL[] gs) {
		for(int i=0;i<gs.length;i++){
			GhostCL g = gs[i];
			System.out.println(i+") status: "+g.getStatus()+",  type: "+g.getType()+",  pos: "+g.getPos(0)+",  time: "+g.remainTimeAsEatable(0));
		}
	}
	private static int randomDir() {
		int[] dirs = {Game.UP, Game.LEFT, Game.DOWN, Game.RIGHT};
		int ind = (int)(Math.random()*dirs.length);
		return dirs[ind];
	}
}