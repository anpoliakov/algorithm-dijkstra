import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Решение задачи алгоритмом Дейкстры
 * */
public class Rover {
    static final String CELLSTART = "00";
    static final int INFINITY = 10000; //мнимая бесконечность в программе
    static int steps = 0;
    static int fuel = 0;

    static Map<String,Integer> distance = null; //значение растояния при переходе из точки А в точку B
    static Map<String,Boolean> visited = null; //память о том рассматривалась ли данная точка
    static HashMap<String, LinkedList<String>> neigbors = null; //содержит информацию о соседях каждой клетки
    static List<String> path = null; //путь по которому можно добраться из начальной клетки в конечную

    public static void main(String[] args) {
        calculateRoverPath(args);
    }

    public static void calculateRoverPath(int[][] map) {
        distance.createDistance(map);
        distance.replace("00", map[0][0]); //заполняем начальную точку default значением
        visited = createVisited(map);
        neigbors = scanNeighbors(map);
    }

    //создаёт изначально заполненную бесконечным мнимыми значением hashMap
    private static Map<String,Integer> createDistance(int[][] map){
        Map <String, Integer> distance = new HashMap<>();
        for (int i = 0; i < map.length; i++){
            for (int j = 0; j < map[0].length; j++){
                distance.put((i + "" + j), INFINITY);
            }
        }
        return distance;
    }

    //создаём hashMap и говорим о том, что ни одна из ячеек не осмотрена
    private static Map<String,Boolean> createVisited(int[][] map){
        Map <String, Boolean> visited = new HashMap<>();
        for (int i = 0; i < map.length; i++){
            for (int j = 0; j < map[0].length; j++){
                visited.put((i + "" + j), false);
            }
        }
        return visited;
    }

    private static HashMap<String,LinkedList<String>> scanNeighbors(int [][] map){
        HashMap<String,LinkedList<String>> neigbors = new HashMap<>();
        int rows = map.length;
        int columns = map[0].length;

        for(int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++){
                LinkedList<String> cell = new LinkedList<>();

                //если справа есть ячейка - добавляем
                if(j < columns - 1){
                    cell.add(i+""+(j+1));
                }

                //если снизу есть ячейка - добавляем
                if(i < rows - 1){
                    cell.add((i+1)+""+j);
                }

                //если сверху есть ячейка - добавляем
                if(i > 0){
                    cell.add((i-1)+""+j);
                }

                //если слева есть ячейка - добавляем
                if(j > 0){
                    cell.add(i+""+(j-1));
                }
                neigbors.put(i+""+j, cell);
            }
        }

        return neigbors;
    }
}
