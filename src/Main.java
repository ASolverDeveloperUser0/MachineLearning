import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class Main {
    private double [] W; // массив весов W
    private double B;
    private final double l = 0.01; // скорость регуляризации
    private final double n = 0.001; // скорость обучения
    private ArrayList<Point> list = new ArrayList<>();
    private ArrayList<Point> testData;
    private ArrayList<Point> controlData;
    ArrayList<Point> d = new ArrayList<>(); // временный список данных
    HashMap<Integer, Double> toChart = new HashMap<>();
    HashMap<Integer, Double> toPointChart = new HashMap<>();

    public static void main(String[] args) {
        Main main = new Main();
    }
    // Сделаю так, что проверяет ошибку, если она довольно маленькая - остановить.
    // Ошибка - второй вес не меняется
    public Main(){
        for (int i = 0; i < 100; i++){
            d.add(new Point(i, (double) Math.pow(i, 3))); // временное заполнение списка данных
            //toPointChart.put(i, Math.pow(i, 3));
        }
        testData = getData(10, d);
        controlData = getData(10, d);

        Function ds = FRnd(3);
        double temp = err(ds, d);
        int index = 0;
        for (int i = 0; i < 2000; i++) {
            ds = step(d, ds, Type.L2);
            toChart.put(index, err(ds, d)); // записываю в коллекцию значения о данных
            toPointChart.put(index, err(ds, testData));
            if (temp > err(ds, d)){
                temp = err(ds, d);
                System.out.println(err(ds, testData) + " ошибка на тестовых данных " + err(ds, d) + " не на тестовых");
            }
                System.out.println(temp + " temp " + Arrays.toString(W) + " " + B);

            index++;
        } // реализовать запись конечной функции и возможность решать, опираясь на контрольный набор, на сколько ф-ця хороша
        PaintChart chart = new PaintChart(800,800,toPointChart,4, 4000);
        PaintChart chart1 = new PaintChart(800,800,toPointChart,2, 10);
    }

    private Function step(ArrayList<Point> data, Function function, Type L) { // шаг градиентного спуска
        int power = W.length; // количество w напрямую зависит от степени power случайной ф-ции метод Frnd
        double sum = 0;
        double s = 0;
        for (Point p: data){
            sum += n * (sgn(p.y - function.f(p.x))) * p.x; // посмотреть что будет если выбирать случайный x
            s += n * (sgn(p.y - function.f(p.x))); // возможно, ошибка в алгоритме, потому что самую низкую ошибку пропускает
        }
        Random random = new Random();
        int index = random.nextInt(power);
        W[index]+= (sum/ data.size()); // пока что перевожу в int
        B+= s;
        if (L == Type.L1) {
            W[index]-= (l * sgn(W[index]));
        }
        if (L == Type.L2){
            W[index] *= (1 - (2 * l)); // норма регуляризации L2
        }
        return (x -> {
            double y = 0;
            int pow = W.length;
            for (Double w: W) {
                y += w * Math.pow(x, pow);
                pow--;
            }
            return y + B;
        });
    }

    private double sgn(double x){ // знаковая функция
        return Math.abs(x) / x;
    }

    private Function FRnd(int power){ // создает многочлен со случайными весами степени power
        Random random = new Random();
        double[] w = new double[power];
        for (int i = 0; i < power; i++) {
            w[i] = random.nextInt(100);
        }
        W = w.clone();
        double b = random.nextDouble(100);
        B = b;
        Function f = (x ->{
            double y = 0;
            for (int pow = power; pow >= 1; pow--) {
                y += w[pow - 1] * Math.pow(x, pow);
            }
            return y + b;
        });
        return f;
    }

    public double err(Function function, ArrayList<Point> data){ // функция ошибки
        double sum = 0;
        for (Point point : data) {
            sum += Math.abs(point.y - function.f(point.x));
        }
        return (sum/ data.size());
    }

    private Function createFunction(Double[] w, double b, int power){
        return (x ->{
            double y = 0;
            for (int pow = power; pow >= 1; pow--) {
                y += w[pow - 1] * Math.pow(x, pow);
            }
            return y + b;
        });
    }

    private ArrayList<Point> getData(int percent, ArrayList<Point> data){ // Здесь будут формироваться тествые данные
        int numberOfPoint = Math.round((float) (data.size() * percent) / 100);
        int size = data.size();
        boolean[] bol = new boolean[size];
        ArrayList<Point> array = new ArrayList<>();
        Random random = new Random();
        for(int i = 0; i < numberOfPoint; i++) {
            int rnd = random.nextInt(size);
            while (bol[rnd]){
                rnd = random.nextInt(size);
            }

            bol[rnd] = true;
            array.add(data.get(rnd));
            data.remove(rnd);
        }

        return array; // возникает ошибка с длиной массива - разобраться
    }

    /*
    * Тестовые данные предназначены для определения, на сколько хороша наша модель.
    * Именно по тестовым данным я принимаю решение(программа), создавать ли новую функцию более высокой степени.
    * Контрольные данные нужны для, непосредственно, участии в обучении модели*/
}

