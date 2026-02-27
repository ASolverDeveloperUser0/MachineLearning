import java.util.ArrayList;
import java.util.Random;

public class Main {
    private double [] W;
    private double B;
    private double l = 0.01;
    private double n = 0.01;
    private ArrayList<Point> list = new ArrayList<>();
    ArrayList<Point> d = new ArrayList<>();

    public static void main(String[] args) {
        Main main = new Main();
    }
    // сделаю так, что проверяет ошибку, если она довольно маленькая - остановить.
    public Main(){
        for (int i = 0; i < 100; i++){
            d.add(new Point(i, (double) i /2));
        }

        Function ds = FRnd(1);
        System.out.println(err(ds, d));
        for (Double w : W){
            System.out.println(w);
        }
        for (int i = 0; i < 1000; i++) {
            ds = step(d, ds);
            if (err(ds, d) > 0 && err(ds,d) < 0.35) {
                System.out.println(err(ds, d));
                System.out.println("W: " + W[0]);
            }
        }
        System.out.println("Итоговая ошибка: " + err(ds, d) + " Итоговый вес: " + W[0]);
    }

    public ArrayList<Point> randomList(ArrayList<Point> data){
        Random random = new Random();
        for (int i = data.size(); i > 0; i--) {
            int j = random.nextInt(i + 1);
            Point t = data.get(i);
            data.set(i, data.get(j));
            data.set(j, t);
        }
        return data;
    }

    private Function step(ArrayList<Point> data, Function function) {
        int j = W.length; // количество w напрямую зависит от степени случайной ф-ции
        double sum = 0;
        double s = 0;
        for (Point p: data){
            sum += n * (sgn(p.y - function.f(p.x))) * p.x; // посмотреть что будет если выбирать случайный x
            s += n * (sgn(p.y - function.f(p.x)));
           if (j > 0){
               j--;
           }
        }
        W[j]+= (sum/ data.size()); // пока что перевожу в int
        B+= s;

       // W[j]-= (l * sgn(W[j]));
        W[j] *= (1 - (2 * l));
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

    private double sgn(double x){
        return Math.abs(x) / x;
    }

    private Function FRnd(int power){
        Random random = new Random();
        double[] w = new double[power];
        for (int i = 0; i < power; i++) {
            w[i] = random.nextInt(1000);
        }
        W = w.clone();
        double b = random.nextDouble(1000);
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

    public void setN(int n) {
        this.n = n;
    }

    public void setL(double l) {
        this.l = l;
    }

    public double err(Function function, ArrayList<Point> data){
        double sum = 0;
        for (Point point : data) {
            sum += Math.abs(point.y - function.f(point.x));
        }
        return (sum/ data.size());
    }

    private double L1(){
        double result = 0;
        for (Double w : W){
            result += Math.abs(w);
        }
        return result;
    }

    private double L2(){
        double result = 0;
        for (Double w : W){
            result += Math.pow(w, 2);
        }
        return result;
    }


}

