package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import view.tools.WELogger;

import measure.ThreadsEfficiency;
import model.world.World;
import model.world.area.Area;
import model.world.area.EntityArea;
import model.world.area.HydroArea;
import model.world.area.ReliefArea;

public class AreaView extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final int SCALE = 10;
    private static final String RELIEF = "relief";
    private static final String HYDRO = "hydro";
    private static final String ENTITY = "entity";

    private World world;

    private AreaSlicedPanel areaPanel;
    private JPanel buttonPanel;
    private JCheckBox hydroCB, entityCB, reliefCB;
    private JLabel efficiencyLab;
    private JLabel nuageLab, undergroundWaterLab, uppergroundWaterLab,
            totalWaterLab;

    public AreaView(World world) {
        this.world = world;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setSize(SCALE * Area.SIZE + 150, SCALE * Area.SIZE);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.LINE_AXIS));

        this.areaPanel = new AreaSlicedPanel();
        this.areaPanel.setSize(SCALE * Area.SIZE, SCALE * Area.SIZE);
        this.areaPanel.setBackground(Color.red);

        this.buttonPanel = new JPanel();
        this.buttonPanel.setSize(150, SCALE * Area.SIZE);
        this.buttonPanel.setLayout(new BoxLayout(this.buttonPanel,
                BoxLayout.PAGE_AXIS));

        this.add(this.areaPanel, BorderLayout.NORTH);
        this.add(this.buttonPanel, BorderLayout.SOUTH);

        this.reliefCB = new JCheckBox("relief");
        this.reliefCB.addActionListener(new CBListener(RELIEF));
        this.reliefCB.setSelected(true);
        this.hydroCB = new JCheckBox("hydrometrie");
        this.hydroCB.addActionListener(new CBListener(HYDRO));
        this.hydroCB.setSelected(true);
        this.entityCB = new JCheckBox("entity");
        this.entityCB.addActionListener(new CBListener(ENTITY));
        this.entityCB.setSelected(true);

        this.buttonPanel.add(reliefCB);
        this.buttonPanel.add(hydroCB);
        this.buttonPanel.add(entityCB);

        this.efficiencyLab = new JLabel("Efficiency : "
                + ThreadsEfficiency.getEfficiency());
        this.nuageLab = new JLabel("Nuage : "
                + world.getCentralArea().getNuage());
        this.undergroundWaterLab = new JLabel("Underground : "
                + world.getCentralArea().getNuage());
        this.uppergroundWaterLab = new JLabel("Upperground : "
                + world.getCentralArea().getNuage());
        this.totalWaterLab = new JLabel("Total : "
                + world.getCentralArea().getNuage());

        this.buttonPanel.add(this.efficiencyLab);
        this.buttonPanel.add(this.nuageLab);
        this.buttonPanel.add(this.undergroundWaterLab);
        this.buttonPanel.add(this.uppergroundWaterLab);
        this.buttonPanel.add(this.totalWaterLab);
    }

    public void updateLabels() {
        this.efficiencyLab.setText("Efficiency : "
                + ThreadsEfficiency.getEfficiency());
        double atmo = world.getCentralArea().getAtmosphereWater();
        double underG = world.getCentralArea().getUndergroundWater();
        double upperG = world.getCentralArea().getUppergroundWater();
        double total = atmo + underG + upperG;
        this.nuageLab.setText("Atmos : "
                + Double.toString(atmo).split("\\.")[0]);
        this.undergroundWaterLab.setText("Underground : "
                + Double.toString(underG).split("\\.")[0]);
        this.uppergroundWaterLab.setText("Upperground : "
                + Double.toString(upperG).split("\\.")[0]);
        this.totalWaterLab.setText("Total : "
                + Double.toString(total).split("\\.")[0]);
    }

    public class AreaSlicedPanel extends JPanel implements Runnable {
        private static final long serialVersionUID = 1L;
        private EntityArea entityArea;
        private HydroArea hydroArea;
        private ReliefArea reliefArea;
        private boolean entityBool, hydroBool, reliefBool;

        public AreaSlicedPanel() {
            this.entityArea = (EntityArea) world.getCentralArea();
            this.hydroArea = (HydroArea) this.entityArea;
            this.reliefArea = (ReliefArea) this.hydroArea;
            this.setSize(SCALE * Area.SIZE, SCALE * Area.SIZE);
            this.entityBool = true;
            this.hydroBool = true;
            this.reliefBool = true;
        }

        public void paint(Graphics g) {
            g.setColor(Color.white);
            g.clearRect(0, 0, SCALE * Area.SIZE, SCALE * Area.SIZE);
            if (reliefBool) {
                reliefArea.drawRelief(g);
            }
            if (hydroBool) {
                hydroArea.drawHydro(g);
            }
            if (entityBool) {
                entityArea.drawEntities(g);
            }
        }

        @Override
        public void run() {
            while (true) {
                updateLabels();
                this.repaint();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    WELogger.log(e);
                }
            }
        }
    }

    public class CBListener implements ActionListener {
        private String message;

        public CBListener(String message) {
            this.message = message;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JCheckBox) {
                if (((JCheckBox) e.getSource()).isSelected()) {
                    if (message.equals(HYDRO)) {
                        areaPanel.hydroBool = true;
                    }
                    if (message.equals(ENTITY)) {
                        areaPanel.entityBool = true;
                    }
                    if (message.equals(RELIEF)) {
                        areaPanel.reliefBool = true;
                    }
                } else {
                    if (message.equals(HYDRO)) {
                        areaPanel.hydroBool = false;
                    }
                    if (message.equals(ENTITY)) {
                        areaPanel.entityBool = false;
                    }
                    if (message.equals(RELIEF)) {
                        areaPanel.reliefBool = false;
                    }
                }
                areaPanel.repaint();
            }
        }

    }

    public void start() {
        new Thread(this.areaPanel).start();
    }

}
