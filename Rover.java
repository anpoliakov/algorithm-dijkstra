package com;

import java.io.*;
import java.util.*;

/**
 * Решение задачи алгоритмом Дейкстры
 * */
public class Rover {
    static final String CELLSTART = "00";
    static final int INFINITY = 10000; //мнимая бесконечность в программе
    static int steps = 0;
    static int fuel = 0;
    static int [][] map;

    static Map<String,Integer> distance = null; //значение растояния при переходе из точки А в точку B
    static Map<String,Boolean> visited = null; //память о том рассматривалась ли данная точка
    static HashMap<String, LinkedList<String>> neigbors = null; //содержит информацию о соседях каждой клетки
    static List<String> path = null; //путь по которому можно добраться из начальной клетки в конечную

    public static void main(String[] args) {
        map = args[0];
        Arrays.binarySearch(args[0]);
        calculateRoverPath(map);
    }

    //основной метод по заданию
    public static void calculateRoverPath(int[][] map) {
        //инициализирую основные hashMap с которыми буду работать
        distance = createDistance(map);
        distance.replace("00", map[0][0]); //заполняем начальную точку default значением
        visited = createVisited(map);
        neigbors = scanNeighbors(map);

        final int ROWS = map.length;
        final int COLUMNS = map[0].length;
        final String CELLFINISH = (ROWS-1) + "" + (COLUMNS-1);

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

        int endValueDistance = distance.get(CELLFINISH); //последнее значение distance c которым работаю
        String endCell = CELLFINISH; // последняя клетка с которой работаю

        //инициализируем список для хранения пути движения ровера
        path = new LinkedList<>();
        addPath(endCell); // добавляем конечную точку и с неё начинаем обратный путь

        //двигаемся от конечной точки к начальной (по алг.Дейкстры) и проверяем откуда был совершён переход
        while(endValueDistance != distance.get(CELLSTART)){
            LinkedList<String> allNeighborsCell = neigbors.get(endCell);

            for(String neighbor : allNeighborsCell){
                int valueFromMap = getValueFromMap(endCell);
                int prevValueDistance = endValueDistance - valueFromMap;

                //if true => шли через этого соседа
                if(prevValueDistance == distance.get(neighbor)){
                    steps++;
                    fuel += Math.abs(getValueFromMap(endCell) - getValueFromMap(neighbor));
                    addPath(neighbor);
                    endValueDistance -= valueFromMap;
                    endCell = neighbor;
                    break; //если хоть 1 сосед подошёл выходим из цикла (если есть 2 пути до этой клетки)
                }
            }

        }

        //переворачиваем коллекцию
        Collections.reverse(path);
        String resultPath = "";

        for (int i = 0; i < path.size(); i++){
            if(i == path.size() - 1) {
                resultPath += path.get(i);
            }else{
                resultPath += path.get(i) + "->";
            }
        }

        writeData(resultPath + System.lineSeparator());
        writeData("steps: " + steps + System.lineSeparator());
        writeData("fuel: " + (fuel + steps));
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

    //метод который оформляет ячейку пути перед помещением в финальный список
    private static void addPath(String cell){
        int charOne = Character.digit(cell.charAt(0), 10);
        int charTwo = Character.digit(cell.charAt(1), 10);
        path.add("["+charOne+"]["+charTwo+"]");
    }

    //получаю по String названию значение высоты с ячейки реальной карты
    private static int getValueFromMap(String cell){
        int row = Character.digit(cell.charAt(0), 10);
        int column = Character.digit(cell.charAt(1), 10);
        return map[row][column];
    }

    //метод для записи данных в файл
    private static void writeData(String data){
        File planFile = new File("path-plan.txt");
        Writer writer = null;
        try {
            writer = new FileWriter(planFile, true);
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


//TODO: исправить максимально допустимый получаемый размер двухмерного массива (сейчас он от 1 до 10)
//TODO: дописать исключения и проверки
//Задание: https://gitlab.com/SyberryAcademy/syberry-academy-e07-test-task
