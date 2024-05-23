package PaooGame.Misc;
import PaooGame.Character.CharacterList;

import java.util.*;
public class AStar {
    //implementare clasica al algoritmului A*
    //functie de costuri custom bazata pe ID-ul tile-ului in sine
    //foarte batuta in cap totusi, puteam face liste/colectii or whatever dar imi era lene
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
    public static int aStar(Integer[][] grid, int startX, int startY, int goalX, int goalY, CharacterList cl){
        Node start = new Node(startX,startY, tileMovementMultiplier(startX,startY,grid, cl),0);
        Node goal = new Node(goalX, goalY, tileMovementMultiplier(goalX,goalY,grid, cl),0);
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
                int tentGCost = gCost.get(current) + tileMovementMultiplier(neighbor.x, neighbor.y, grid, cl);
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

    private static int tileMovementMultiplier(int x, int y, Integer[][] grid, CharacterList cl){
        if(cl.containsEnemy(x,y)) return 9999;
        switch(grid[x][y]){
            case 1:
            case 4:
            case 5:
            case 13:
            case 45:
            case 69:
            case 70:
            case 81:
            case 97:
            case 101:
            case 102:
            case 112:
            case 141:
            case 149:
            case 170:
            case 171:
            case 266:
            case 297:
            case 298:
            case 299:
            case 324:
                return 10;
            case 781:
            case 847:
                return 20;
            case 351:
            case 418:
            case 458:
            case 464:
            case 490:
            case 492:
            case 496:
            case 557:
            case 559:
            case 567:
            case 580:
            case 590:
            case 593:
            case 597:
            case 612:
            case 615:
            case 617:
            case 621:
            case 624:
            case 630:
            case 658:
            case 663:
            case 673:
            case 674:
            case 678:
            case 679:
            case 703:
            case 704:
            case 743:
            case 744:
            case 746:
            case 749:
            case 770:
            case 774:
            case 777:
            case 779:
            case 806:
            case 807:
            case 808:
            case 839:
            case 840:
            case 841:
            case 850:
            case 851:
            case 852:
            case 870:
            case 871:
            case 872:
            case 882:
            case 883:
            case 884:
            case 914:
            case 915:
            case 916:
                return 9999;
        }
        return 20;
        //aceasta este o implementare foarte batuta in cap^^^^
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
