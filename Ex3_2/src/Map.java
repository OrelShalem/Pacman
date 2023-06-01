
import java.util.*;

/**
 * This class represents a 2D map as a "screen" or a raster matrix or maze over integers.
 *
 * @author boaz.benmoshe
 */
public class Map implements Map2D {
	private int[][] _map;
	private boolean _cyclicFlag = true;
	/**
	 * Constructs a w*h 2D raster map with an init value v.
	 *
	 * @param w
	 * @param h
	 * @param v
	 */
	public Map(int w, int h, int v) {
		init(w, h, v);
	}

	/**
	 * Constructs a square map (size*size).
	 *
	 * @param size
	 */
	public Map(int size) {
		this(size, size, 0);
	}

	/**
	 * Constructs a map from a given 2D array.
	 *
	 * @param data
	 */
	public Map(int[][] data) {
		init(data);
	}

	/**
	 * Initializes the map with a given width, height, and value.
	 *
	 * @param w The width of the map.
	 * @param h The height of the map.
	 * @param v The value to assign to each cell in the map.
	 */

	@Override
	public void init(int w, int h, int v) {
		_map = new int[h][w]; // Create a new array with dimensions w and h

		// Set each cell in the array to the value v
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				_map[i][j] = v;
			}
		}
	}
	/**
	 * Initializes the _map array with the values from the input 2D array.
	 *
	 * @param arr The input 2D array to initialize the _map array.
	 * @throws RuntimeException if the input array is null, empty, or not a ragged matrix.
	 */
	@Override
	public void init(int[][] arr) {
		// Check if the array is null or empty or if it is a ragged 2D array
		if (arr == null || arr.length == 0) {
			throw new RuntimeException("array is empty or a ragged 2D array");
		}

		// Check if the array has consistent row lengths
		int num = arr[0].length;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].length != num) {
				throw new RuntimeException("array is not ragged");
			}
		}

		// Create a new matrix with the same dimensions as arr
		_map = new int[arr.length][arr[0].length];

		// Deep copy the values from arr to _map
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				_map[i][j] = arr[i][j];
			}
		}
	}
	/**
	 * Returns a copy of the matrix representing the map.
	 *
	 * @return A 2D array representing the map.
	 */

	@Override
	public int[][] getMap() {
		int[][] ans = new int[this._map.length][this._map[0].length]; // Initializing an array with the size of the _map array
		for (int i = 0; i < this._map.length; i++) { // Nested loop that iterates and assigns the value of _map to each cell
			for (int j = 0; j < this._map[0].length; j++) {
				ans[i][j] = this._map[i][j];
			}
		}
		return ans;
	}
	/**
	 * Returns the width of the map.
	 *
	 * @return The number of rows in the matrix (width of the map).
	 */

	@Override
	public int getWidth() {
		return _map.length; // Returns the number of rows in the matrix
	}
	/**
	 * Returns the height of the map.
	 *
	 * @return The number of columns in the matrix (height of the map).
	 */

	@Override
	public int getHeight() {
		return _map[0].length;// Returns the number of col in the matrix
	}
	/**
	 * Returns the pixel value at the specified coordinates.
	 *
	 * @param x The row index.
	 * @param y The column index.
	 * @return The value located at cell [x][y] in the matrix.
	 */

	@Override
	public int getPixel(int x, int y) {
		return _map[x][y]; // Returns the value located at cell [x][y] in the matrix
	}
	/**
	 * Returns the pixel value at the coordinates specified by the given Pixel2D object.
	 *
	 * @param p The Pixel2D object representing the coordinates.
	 * @return The value located at the specified coordinates in the matrix.
	 */

	@Override
	public int getPixel(Pixel2D p) {
		return getPixel(p.getX(), p.getY()); // Returns the value located at the coordinates specified by the Pixel2D object
	}
	/**
	 * Sets the pixel value at the specified coordinates.
	 *
	 * @param x The row index.
	 * @param y The column index.
	 * @param v The new value to be assigned.
	 */
	@Override
	public void setPixel(int x, int y, int v) {
		_map[x][y] = v; // Assigns a new value to the cell [x][y] in the matrix
	}
	/**
	 * Sets the pixel value at the specified Pixel2D coordinates.
	 *
	 * @param p The Pixel2D object representing the coordinates.
	 * @param v The new value to be assigned.
	 */
	@Override
	public void setPixel(Pixel2D p, int v) {
		setPixel(p.getX(), p.getY(), v); // Assigns a new value to a specific pixel in the matrix
	}

	@Override
	/**
	 * Fills this map with the new color (new_v) starting from p.
	 * https://en.wikipedia.org/wiki/Flood_fill
	 *
	 *
	 *  * Fills the pixel at the given coordinates with a new value, using the current pixel value as the target value.
	 *  *
	 *  * @param xy The coordinates of the pixel to be filled.
	 *  * @param new_v The new value to assign to the pixel.
	 *  * @return The number of pixels filled.
	 *  */
	public int fill(Pixel2D xy, int new_v) {
		return fill(xy, new_v, getPixel(xy));

	}

	/**
	 * Fills the connected region starting from the given pixel (xy) with the new value (new_v).
	 * It recursively explores neighboring pixels and changes their values if they have the old value (old_v).
	 * The function returns the number of pixels that were filled.
	 *
	 * @param xy    The starting pixel to fill.
	 * @param new_v The new value to assign to the filled pixels.
	 * @param old_v The old value to be replaced by the new value.
	 * @return The number of pixels that were filled.
	 */

	private int fill(Pixel2D xy, int new_v, int old_v) {
		// Check if the current pixel is outside the map boundaries or has a different value
		if (!(isInside(xy)) || getPixel(xy) != old_v)
			return 0;

		// If the current pixel has the old value, update it to the new value
		if (old_v == getPixel(xy)) {
			setPixel(xy, new_v);

			// If the map is not cyclic
			if (this.isCyclic() == false) {
				// Define the neighboring pixels
				Index2D right = new Index2D(xy.getX() + 1, xy.getY());
				Index2D left = new Index2D(xy.getX() - 1, xy.getY());
				Index2D up = new Index2D(xy.getX(), xy.getY() - 1);
				Index2D down = new Index2D(xy.getX(), xy.getY() + 1);

				// Recursive calls to fill the neighboring pixels and count the number of filled pixels
				return 1 + fill(right, new_v, old_v) +
						fill(left, new_v, old_v) +
						fill(up, new_v, old_v) +
						fill(down, new_v, old_v);
			} else { // If the map is cyclic
				// Define the neighboring pixels considering the map's cyclic behavior

				// Right neighbor
				Index2D right = new Index2D(xy.getX() + 1, xy.getY());
				if (xy.getX() == this.getWidth() - 1) {
					// If the current pixel is at the rightmost edge, wrap around to the leftmost edge
					right = new Index2D(0, xy.getY());
				}

				// Left neighbor
				Index2D left = new Index2D(xy.getX() - 1, xy.getY());
				if (xy.getX() == 0) {
					// If the current pixel is at the leftmost edge, wrap around to the rightmost edge
					left = new Index2D(this.getWidth() - 1, xy.getY());
				}

				// Up neighbor
				Index2D up = new Index2D(xy.getX(), xy.getY() - 1);
				if (xy.getY() == 0) {
					// If the current pixel is at the topmost edge, wrap around to the bottommost edge
					up = new Index2D(xy.getX(), this.getHeight() - 1);
				}

				// Down neighbor
				Index2D down = new Index2D(xy.getX(), xy.getY() + 1);
				if (xy.getY() == this.getHeight() - 1) {
					// If the current pixel is at the bottommost edge, wrap around to the topmost edge
					down = new Index2D(xy.getX(), 0);
				}

				// Recursive calls to fill the neighboring pixels and count the number of filled pixels
				return 1 + fill(right, new_v, old_v) +
						fill(left, new_v, old_v) +
						fill(up, new_v, old_v) +
						fill(down, new_v, old_v);
			}
		}

		// If the current pixel doesn't have the old value, return 0 (no pixels filled)
		return 0;
	}


	@Override
	/**
	 * BFS like shortest the computation based on iterative raster implementation of BFS, see:
	 * https://en.wikipedia.org/wiki/Breadth-first_search
	 *
	 *Finds the shortest path between two pixels (p1 and p2) on the map,
	 * avoiding obstacles with the specified color (obsColor).
	 * It returns an array of pixels representing the shortest path.
	 *
	 * @param p1       The starting pixel.
	 * @param p2       The target pixel.
	 * @param obsColor The color of the obstacles to avoid.
	 * @return An array of pixels representing the shortest path.
	 */
	public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor) {


		// Create a distance map using the allDistance method
		Map2D map = this.allDistance(p1, obsColor);

		// Create an array to store the pixels of the shortest path
		Pixel2D[] ans = new Pixel2D[map.getPixel(p2) +1];

		// Check if the starting or target pixels are outside the map boundaries
		if (!isInside(p1) || !isInside(p2)) {
			return null;
		}

		// If the starting and target pixels are the same, return the starting pixel as the shortest path
		if (p1.equals(p2)) {
			ans[0] = new Index2D(p1.getX(), p1.getY());
			return ans;
		}

		int i = 1;
		ans[0] = p2;


		// Iterate until the entire shortest path is found
		while (i < ans.length) {

			// If the map is not cyclic
			if (this.isCyclic() == false) {
				// Define the neighboring pixels
				Index2D right = new Index2D(p2.getX() + 1, p2.getY());
				Index2D left = new Index2D(p2.getX() - 1, p2.getY());
				Index2D up = new Index2D(p2.getX(), p2.getY() - 1);
				Index2D down = new Index2D(p2.getX(), p2.getY() + 1);

				// Check if the neighboring pixels have a distance value that is one less than the current pixel
				if (map.getPixel(p2) - 1 == map.getPixel(right)) {
					ans[i] = right;
					i++;
					p2 = right;
					continue;
				}
				if (map.getPixel(p2) - 1 == map.getPixel(left)) {
					ans[i] = left;
					i++;
					p2 = left;
					continue;
				}
				if (map.getPixel(p2) - 1 == map.getPixel(up)) {
					ans[i] = up;
					i++;
					p2 = up;
					continue;
				}
				if (map.getPixel(p2) - 1 == map.getPixel(down)) {
					ans[i] = down;
					i++;
					p2 = down;
					continue;
				}
			} else { // If the map is cyclic
				// Define the neighboring pixels considering the map's cyclic behavior

				// Right neighbor
				Index2D right = new Index2D(p2.getX() + 1, p2.getY());
				if (p2.getX() == this.getWidth() - 1) {
					// If the current pixel is at the rightmost edge, wrap around to the leftmost edge
					right = new Index2D(0, p2.getY());
				}

				// Left neighbor
				Index2D left = new Index2D(p2.getX() - 1, p2.getY());
				if (p2.getX() == 0) {
					// If the current pixel is at the leftmost edge, wrap around to the rightmost edge
					left = new Index2D(this.getWidth() - 1, p2.getY());
				}

				// Up neighbor
				Index2D up = new Index2D(p2.getX(), p2.getY() - 1);
				if (p2.getY() == 0) {
					// If the current pixel is at the topmost edge, wrap around to the bottommost edge
					up = new Index2D(p2.getX(), this.getHeight() - 1);
				}

				// Down neighbor
				Index2D down = new Index2D(p2.getX(), p2.getY() + 1);
				if (p2.getY() == this.getHeight() - 1) {
					// If the current pixel is at the bottommost edge, wrap around to the topmost edge
					down = new Index2D(p2.getX(), 0);
				}

				// Check if the neighboring pixels have a distance value that is one less than the current pixel
				if (map.getPixel(p2) - 1 == map.getPixel(right)) {
					ans[i] = right;
					i++;
					p2 = right;
					continue;
				}
				if (map.getPixel(p2) - 1 == map.getPixel(left)) {
					ans[i] = left;
					i++;
					p2 = left;
					continue;
				}
				if (map.getPixel(p2) - 1 == map.getPixel(up)) {
					ans[i] = up;
					i++;
					p2 = up;
					continue;
				}
				if (map.getPixel(p2) - 1 == map.getPixel(down)) {
					ans[i] = down;
					i++;
					p2 = down;
					continue;
				}
			}
		}

		return ans;
	}
	/**
	 * Checks if a given pixel (p) is inside the map boundaries.
	 *
	 * @param p The pixel to check.
	 * @return True if the pixel is inside the map boundaries, false otherwise.
	 */

	@Override
	public boolean isInside(Pixel2D p) {
		// Create an ArrayList to store values (not used in the current implementation)
		ArrayList<Integer> a = new ArrayList<>();

		// Check if the x-coordinate and y-coordinate of the pixel are within the map boundaries
		if (p.getX() < getWidth() && p.getY() < getWidth() && p.getY() >= 0 && p.getX() >= 0) {
			return true;
		}

		return false;
	}

	/**
	 * Checks if the map is cyclic.
	 *
	 * @return True if the map is cyclic, false otherwise.
	 */
	@Override
	public boolean isCyclic() {
		// Check the value of the cyclicFlag
		if (_cyclicFlag == true) {
			return true;
		}

		return false;
	}

	/**
	 * Sets the cyclic flag of the map.
	 *
	 * @param cy The value indicating whether the map is cyclic or not.
	 */
	@Override
	public void setCyclic(boolean cy) {
		// Set the value of the cyclicFlag to the provided value
		_cyclicFlag = cy;
	}


	@Override
	/**
	 * Calculates the distance map from a given start position to all other positions on the map.
	 *
	 * @param start    The starting position for calculating the distance map.
	 * @param obsColor The color representing obstacles on the map.
	 * @return The distance map from the start position.
	 */
	public Map2D allDistance(Pixel2D start, int obsColor) {
		// Create a copy of the map
		Map distanceMap = new Map(getMap());

		// Check if the start position is outside the map
		if (isInside(start) == false) {
			// If the start position is outside the map, create a new distance map with all cells set to -1
			distanceMap = new Map(getWidth(), getHeight(), -1);
			return distanceMap;
		}

		// Check if the start position is an obstacle
		if (this.getPixel(start) == obsColor) {
			// If the start position is an obstacle, create a new distance map with all cells set to -1
			distanceMap = new Map(getWidth(), getHeight(), -1);
			return distanceMap;
		}

		// Set initial distances in the distance map
		for (int i = 0; i < distanceMap.getMap().length; i++) {
			for (int j = 0; j < distanceMap.getMap()[0].length; j++) {
				if (distanceMap.getPixel(i, j) == obsColor) {
					// If the cell is an obstacle, set the distance to -1
					distanceMap.setPixel(i, j, -1);
				} else {
					// Otherwise, set the distance to -2 as a placeholder value
					distanceMap.setPixel(i, j, -2);
				}
			}
		}

		// Calculate the distances using the helper method
		alldistHelp(distanceMap, start);

		// Set remaining placeholder values to -1
		for (int i = 0; i < distanceMap.getMap().length; i++) {
			for (int j = 0; j < distanceMap.getMap()[0].length; j++) {
				if (distanceMap.getPixel(i, j) == -2) {
					distanceMap.setPixel(i, j, -1);
				}
			}
		}

		return distanceMap;
	}

	/**
	 * Helper method for calculating the distances in the distance map.
	 *
	 * @param map The distance map.
	 * @param xy  The starting position.
	 */
	private void alldistHelp(Map map, Pixel2D xy) {
		// Create a queue to store the pixels for breadth-first search
		Queue<Pixel2D> temp = new LinkedList<>();

		// Set the distance of the starting position to 0 in the distance map
		map.setPixel(xy, 0);

		// Add the starting position to the queue
		temp.add(xy);
		// Perform breadth-first search to calculate distances
		while (temp.isEmpty() == false) {
			xy = temp.poll();
			int index = map.getPixel(xy) + 1;

			// Check if the map is cyclic or not
			if (this.isCyclic() == false) {
				Index2D right = new Index2D(xy.getX() + 1, xy.getY());  // What happens if you are at the leftmost side
				Index2D left = new Index2D(xy.getX() - 1, xy.getY());
				Index2D up = new Index2D(xy.getX(), xy.getY() - 1);
				Index2D down = new Index2D(xy.getX(), xy.getY() + 1);

				// Check if the right cell is inside the map
				if (map.isInside(right)) {
					if (map.getPixel(right) == -2) {
						map.setPixel(right, index);
						temp.add(right);
					}
				}

				// Check if the left cell is inside the map
				if (map.isInside(left)) {
					if (map.getPixel(left) == -2) {
						map.setPixel(left, index);
						temp.add(left);
					}
				}

				// Check if the up cell is inside the map
				if (map.isInside(up)) {
					if (map.getPixel(up) == -2) {
						map.setPixel(up, index);
						temp.add(up);
					}
				}

				// Check if the down cell is inside the map
				if (map.isInside(down)) {
					if (map.getPixel(down) == -2) {
						map.setPixel(down, index);
						temp.add(down);
					}
				}
			} else {
				Index2D right = new Index2D(xy.getX() + 1, xy.getY());
				Index2D left = new Index2D(xy.getX() - 1, xy.getY());
				Index2D up = new Index2D(xy.getX(), xy.getY() - 1);
				Index2D down = new Index2D(xy.getX(), xy.getY() + 1);

				// Check if the x-coordinate of xy is at the rightmost side of the map
				if (xy.getX() == this.getWidth() - 1) {
					right = new Index2D(0, xy.getY());
				}

				// Check if the x-coordinate of xy is at the leftmost side of the map
				if (xy.getX() == 0) {
					left = new Index2D(this.getWidth() - 1, xy.getY());
				}

				// Check if the y-coordinate of xy is at the topmost side of the map
				if (xy.getY() == 0) {
					down = new Index2D(xy.getX(), this.getHeight() - 1);
				}

				// Check if the y-coordinate of xy is at the bottommost side of the map
				if (xy.getY() == this.getHeight() - 1) {
					up = new Index2D(xy.getX(), 0);
				}

				// Update the distances for the neighboring cells
				if (map.getPixel(right) == -2) {
					map.setPixel(right, index);
					temp.add(right);
				}
				if (map.getPixel(up) == -2) {
					map.setPixel(up, index);
					temp.add(up);
				}
				if (map.getPixel(left) == -2) {
					map.setPixel(left, index);
					temp.add(left);
				}
				if (map.getPixel(down) == -2) {
					map.setPixel(down, index);
					temp.add(down);
				}
			}
		}
	}
	/**
	 * Populates an ArrayList with Pixel2D objects representing specific points on the map.
	 * @param allPoints The ArrayList to populate with the points.
	 */
	public void arraypoints(ArrayList<Pixel2D> allPoints) {
		for (int i = 0; i < this.getMap().length; i++) {
			for (int j = 0; j < this.getMap()[0].length; j++) {
				if (this.getPixel(i, j) == 3 || this.getPixel(i, j) == 5) {
					// Create a new Pixel2D object representing the current pixel
					Pixel2D point = new Index2D(i, j);

					// Add the point to the allPoints ArrayList
					allPoints.add(point);
				}
			}
		}
	}
	public Pixel2D nearPoint(Pixel2D xy, int obsColor){
		Map distanceMap = new Map(this._map);
		for (int i = 0; i < distanceMap.getMap().length; i++) {
			for (int j = 0; j < distanceMap.getMap()[0].length; j++) {
				if (distanceMap.getPixel(i, j) == obsColor) {
					// If the cell is an obstacle, set the distance to -1
					distanceMap.setPixel(i, j, -1);
				}
				else if(distanceMap.getPixel(i,j) == 3 ||distanceMap.getPixel(i,j) == 5){
					distanceMap.setPixel(i, j, -3);
				}
				else {
					// Otherwise, set the distance to -2 as a placeholder value
					distanceMap.setPixel(i, j, -2);
				}
			}
		}

		Queue<Pixel2D> temp = new LinkedList<>();

		// Set the distance of the starting position to 0 in the distance map
		distanceMap.setPixel(xy, 0);

		// Add the starting position to the queue
		temp.add(xy);
		// Perform breadth-first search to calculate distances
		while (temp.isEmpty() == false) {
			xy = temp.poll();
			int index = distanceMap.getPixel(xy) + 1;

			// Check if the map is cyclic or not
			if (this.isCyclic() == false) {
				Index2D right = new Index2D(xy.getX() + 1, xy.getY());  // What happens if you are at the leftmost side
				Index2D left = new Index2D(xy.getX() - 1, xy.getY());
				Index2D up = new Index2D(xy.getX(), xy.getY() - 1);
				Index2D down = new Index2D(xy.getX(), xy.getY() + 1);

				// Check if the right cell is inside the map
				if (distanceMap.isInside(right)) {
					if (distanceMap.getPixel(right) == -2) {
						distanceMap.setPixel(right, index);
						temp.add(right);
					}
					if (distanceMap.getPixel(right) == -3) {
						return right;
					}
				}

				// Check if the left cell is inside the map
				if (distanceMap.isInside(left)) {
					if (distanceMap.getPixel(left) == -2) {
						distanceMap.setPixel(left, index);
						temp.add(left);
					}
					if (distanceMap.getPixel(left) == -3) {
						return left;
					}
				}

				// Check if the up cell is inside the map
				if (distanceMap.isInside(up)) {
					if (distanceMap.getPixel(up) == -2) {
						distanceMap.setPixel(up, index);
						temp.add(up);
					}
					if (distanceMap.getPixel(up) == -3) {
						return up;
					}
				}

				// Check if the down cell is inside the map
				if (distanceMap.isInside(down)) {
					if (distanceMap.getPixel(down) == -2) {
						distanceMap.setPixel(down, index);
						temp.add(down);
					}
					if (distanceMap.getPixel(down) == -3) {
						return down;
					}
				}
			} else {
				Index2D right = new Index2D(xy.getX() + 1, xy.getY());
				Index2D left = new Index2D(xy.getX() - 1, xy.getY());
				Index2D up = new Index2D(xy.getX(), xy.getY() - 1);
				Index2D down = new Index2D(xy.getX(), xy.getY() + 1);

				// Check if the x-coordinate of xy is at the rightmost side of the map
				if (xy.getX() == this.getWidth() - 1) {
					right = new Index2D(0, xy.getY());
				}

				// Check if the x-coordinate of xy is at the leftmost side of the map
				if (xy.getX() == 0) {
					left = new Index2D(this.getWidth() - 1, xy.getY());
				}

				// Check if the y-coordinate of xy is at the topmost side of the map
				if (xy.getY() == 0) {
					down = new Index2D(xy.getX(), this.getHeight() - 1);
				}

				// Check if the y-coordinate of xy is at the bottommost side of the map
				if (xy.getY() == this.getHeight() - 1) {
					up = new Index2D(xy.getX(), 0);
				}

				// Update the distances for the neighboring cells
				if (distanceMap.getPixel(right) == -2) {
					distanceMap.setPixel(right, index);
					temp.add(right);
				}
				if (distanceMap.getPixel(right) == -3) {
					return right;
				}
				if (distanceMap.getPixel(up) == -2) {
					distanceMap.setPixel(up, index);
					temp.add(up);
				}
				if (distanceMap.getPixel(up) == -3) {
					return up;
				}
				if (distanceMap.getPixel(left) == -2) {
					distanceMap.setPixel(left, index);
					temp.add(left);
				}
				if (distanceMap.getPixel(left) == -3) {
					return left;
				}
				if (distanceMap.getPixel(down) == -2) {
					distanceMap.setPixel(down, index);
					temp.add(down);
				}
				if (distanceMap.getPixel(down) == -3) {
					return down;
				}
			}
		}
		return null;
	}
	public Pixel2D nearGreenPoint(Pixel2D xy, int obsColor){
		Map distanceMap = new Map(this._map);
		for (int i = 0; i < distanceMap.getMap().length; i++) {
			for (int j = 0; j < distanceMap.getMap()[0].length; j++) {
				if (distanceMap.getPixel(i, j) == obsColor) {
					// If the cell is an obstacle, set the distance to -1
					distanceMap.setPixel(i, j, -1);
				}
				else if(distanceMap.getPixel(i,j) == 5){
					distanceMap.setPixel(i, j, -3);
				}
				else {
					// Otherwise, set the distance to -2 as a placeholder value
					distanceMap.setPixel(i, j, -2);
				}
			}
		}

		Queue<Pixel2D> temp = new LinkedList<>();

		// Set the distance of the starting position to 0 in the distance map
		distanceMap.setPixel(xy, 0);

		// Add the starting position to the queue
		temp.add(xy);
		// Perform breadth-first search to calculate distances
		while (temp.isEmpty() == false) {
			xy = temp.poll();
			int index = distanceMap.getPixel(xy) + 1;

			// Check if the map is cyclic or not
			if (this.isCyclic() == false) {
				Index2D right = new Index2D(xy.getX() + 1, xy.getY());  // What happens if you are at the leftmost side
				Index2D left = new Index2D(xy.getX() - 1, xy.getY());
				Index2D up = new Index2D(xy.getX(), xy.getY() - 1);
				Index2D down = new Index2D(xy.getX(), xy.getY() + 1);

				// Check if the right cell is inside the map
				if (distanceMap.isInside(right)) {
					if (distanceMap.getPixel(right) == -2) {
						distanceMap.setPixel(right, index);
						temp.add(right);
					}
					if (distanceMap.getPixel(right) == -3) {
						return right;
					}
				}

				// Check if the left cell is inside the map
				if (distanceMap.isInside(left)) {
					if (distanceMap.getPixel(left) == -2) {
						distanceMap.setPixel(left, index);
						temp.add(left);
					}
					if (distanceMap.getPixel(left) == -3) {
						return left;
					}
				}

				// Check if the up cell is inside the map
				if (distanceMap.isInside(up)) {
					if (distanceMap.getPixel(up) == -2) {
						distanceMap.setPixel(up, index);
						temp.add(up);
					}
					if (distanceMap.getPixel(up) == -3) {
						return up;
					}
				}

				// Check if the down cell is inside the map
				if (distanceMap.isInside(down)) {
					if (distanceMap.getPixel(down) == -2) {
						distanceMap.setPixel(down, index);
						temp.add(down);
					}
					if (distanceMap.getPixel(down) == -3) {
						return down;
					}
				}
			} else {
				Index2D right = new Index2D(xy.getX() + 1, xy.getY());
				Index2D left = new Index2D(xy.getX() - 1, xy.getY());
				Index2D up = new Index2D(xy.getX(), xy.getY() - 1);
				Index2D down = new Index2D(xy.getX(), xy.getY() + 1);

				// Check if the x-coordinate of xy is at the rightmost side of the map
				if (xy.getX() == this.getWidth() - 1) {
					right = new Index2D(0, xy.getY());
				}

				// Check if the x-coordinate of xy is at the leftmost side of the map
				if (xy.getX() == 0) {
					left = new Index2D(this.getWidth() - 1, xy.getY());
				}

				// Check if the y-coordinate of xy is at the topmost side of the map
				if (xy.getY() == 0) {
					down = new Index2D(xy.getX(), this.getHeight() - 1);
				}

				// Check if the y-coordinate of xy is at the bottommost side of the map
				if (xy.getY() == this.getHeight() - 1) {
					up = new Index2D(xy.getX(), 0);
				}

				// Update the distances for the neighboring cells
				if (distanceMap.getPixel(right) == -2) {
					distanceMap.setPixel(right, index);
					temp.add(right);
				}
				if (distanceMap.getPixel(right) == -3) {
					return right;
				}
				if (distanceMap.getPixel(up) == -2) {
					distanceMap.setPixel(up, index);
					temp.add(up);
				}
				if (distanceMap.getPixel(up) == -3) {
					return up;
				}
				if (distanceMap.getPixel(left) == -2) {
					distanceMap.setPixel(left, index);
					temp.add(left);
				}
				if (distanceMap.getPixel(left) == -3) {
					return left;
				}
				if (distanceMap.getPixel(down) == -2) {
					distanceMap.setPixel(down, index);
					temp.add(down);
				}
				if (distanceMap.getPixel(down) == -3) {
					return down;
				}
			}
		}
		return null;
	}


}
