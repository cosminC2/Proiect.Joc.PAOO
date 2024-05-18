package PaooGame.Misc;
import java.util.*;
public class AStar {
    public static class Node implements Comparable<Node>{
        int x, y, cost, fCost;
        Node(int x, int y, int cost, int fCost){
            this.x=x;
            this.y=y;
            this.cost=cost;
            this.fCost=fCost;
        }
        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.fCost, other.fCost);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Node node = (Node) obj;
            return x == node.x && y == node.y;
        }
        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
    public static int aStar(Integer[][] grid, int startX, int startY, int goalX, int goalY){
        Node start = new Node(startX,startY, tileMovementMultiplier(startX,startY,grid),0);
        Node goal = new Node(goalX, goalY, tileMovementMultiplier(goalX,goalY,grid),0);
        int rows = grid.length;
        int cols = grid[0].length;
        PriorityQueue<Node> openList = new PriorityQueue<>();
        Set<Node> clossedList = new HashSet<Node>();
        Map<Node, Integer> gCost = new HashMap<>();
        start.fCost = heuristic(start, goal);
        openList.add(start);
        gCost.put(start, 0);
        while(!openList.isEmpty())
        {
            Node current = openList.poll();
            if(current.equals(goal))
            {
                return gCost.get(current);
            }
            clossedList.add(current);
            for(Node neighbor: getNeighbors(current, rows, cols, grid))
            {
                if(clossedList.contains((neighbor))) continue;
                int tentGCost = gCost.get(current) + tileMovementMultiplier(neighbor.x, neighbor.y, grid);
                if(!gCost.containsKey(neighbor) || tentGCost < gCost.get(neighbor))
                {
                    gCost.put(neighbor, tentGCost);
                    neighbor.fCost = tentGCost + heuristic(neighbor, goal);
                    openList.add(neighbor);
                }
            }
        }
        return -1;
    }
    private static int heuristic(Node a, Node b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y); // Manhattan distance
    }

    private static int tileMovementMultiplier(int x, int y, Integer[][] grid){
        switch(grid[x][y]){
            case 1://grass
                return 10;
            case 2://water
                return 9999;
            case 3://forest
                return 14;
        }
        return 20;
    }
    private static List<Node> getNeighbors(Node node, int rows, int cols, Integer[][] grid) {
        List<Node> neighbors = new ArrayList<>();
        int x = node.x;
        int y = node.y;

        if (x > 0) neighbors.add(new Node(x - 1, y, grid[x - 1][y], 0));
        if (x < rows - 1) neighbors.add(new Node(x + 1, y, grid[x + 1][y], 0));
        if (y > 0) neighbors.add(new Node(x, y - 1, grid[x][y - 1], 0));
        if (y < cols - 1) neighbors.add(new Node(x, y + 1, grid[x][y + 1], 0));

        return neighbors;
    }
}
