import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class PaintChart extends JComponent {
    private final JFrame frame;
    private final int kx;
    private final int ky;
    private final HashMap<Integer, Double> chart_point;
    private final int w;
    private final int h;
    public Color color = Color.BLACK;

    public PaintChart(int w, int h, HashMap<Integer, Double> chart_point, int kx, int ky){
        JFrame frame = new JFrame();
        frame.setSize(w, h);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setTitle("x: " + kx + " y: " + ky);
        this.frame = frame;
        this.kx = kx;
        this.ky = ky;
        this.chart_point = chart_point;
        this.w = w;
        this.h = h;

        frame.add(this);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(color);
        for (Integer iteration : chart_point.keySet()){
            g.fillOval(50 + Math.round((float) iteration / kx),
                    (h - 100) - (int) Math.round(chart_point.get(iteration) / ky), 3, 3);
        }
    }

    public JFrame getFrame() {
        return frame;
    }
}
