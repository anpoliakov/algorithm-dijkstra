import java.util.*;

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

    //основной метод по заданию
    public static void calculateRoverPath(int[][] map) {
        //инициализирую основные hashMap с которыми буду работать
        distance.createDistance(map);
        distance.replace("00", map[0][0]); //заполняем начальную точку default значением
        visited = createVisited(map);
        neigbors = scanNeighbors(map);

        final int ROWS = map.length;
        final int COLUMNS = map[0].length;
        final String CELLFINISH = (rows-1) + "" + (columns-1);

        //очередь обработки ячеек
        Queue<String> cellsToProcess = new ArrayDeque<>();
        cellsToProcess.offer("00");

        //пока конечная точка карты не обработана (идём вперёд по алг. Дейкстры)
        while(visited.get(CELLFINISH) != true){
            String specificСell = cellsToProcess.poll(); //получаю ячейку из очереди на обработку
            LinkedList<String> allNeighborsCell = neigbors.get(specificСell); //получаю список всех соседей конкретной ячейки
            int distanceToCell = distance.get(specificСell); //дистанция от начальной точки до текущей

            //проходим по всем соседям рассматриваемой ячеки
            for(String neighbor : allNeighborsCell){
                //если соседняя ячейка не рассмотрена - анализируем её соседей и расстояния до них
                if(visited.get(neighbor) != true){
                    int distanceToNeighbor = distanceToCell + getValueFromMap(neighbor);

                    //если найден менее затратный путь - то distance до соседа перезаписываем
                    if(distanceToNeighbor <= distance.get(neighbor)){
                        distance.replace(neighbor,distanceToNeighbor);
                    }

                    //если в стеке ещё нет ячейки для дальнейшей обработки - добавляем
                    if(!cellsToProcess.contains(neighbor)){
                        cellsToProcess.add(neighbor);
                    }
                }
            }

            //говорим что рассматриваемая ячейка проверена и больше её трогать не нужно
            visited.replace(specificСell, true);
        }
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

    //состовляет списки соседей для каждой ячейки полученного массива
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
