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

    }
}
