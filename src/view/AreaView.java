package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import model.world.World;
import model.world.area.Area;
import model.world.area.EntityArea;
import model.world.area.HydroArea;
import model.world.area.ReliefArea;

public class AreaView extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final int SCALE = 10;
    private World world;

    private AreaSlicedPanel areaPanel;
    private JPanel buttonPanel;
    private JCheckBox hydroCB, entityCB, reliefCB;

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
        this.buttonPanel.setBackground(Color.green);
        this.buttonPanel.setLayout(new BoxLayout(this.buttonPanel,
                BoxLayout.PAGE_AXIS));

        this.add(this.areaPanel, BorderLayout.NORTH);
        this.add(this.buttonPanel, BorderLayout.SOUTH);

        this.reliefCB = new JCheckBox("relief");
        this.reliefCB.addActionListener(new CBListener("relief"));
        this.reliefCB.setSelected(true);
        this.hydroCB = new JCheckBox("hydrometrie");
        this.hydroCB.addActionListener(new CBListener("hydro"));
        this.entityCB = new JCheckBox("entity");
        this.entityCB.addActionListener(new CBListener("entity"));

        this.buttonPanel.add(reliefCB);
        this.buttonPanel.add(hydroCB);
        this.buttonPanel.add(entityCB);
    }

    public class AreaSlicedPanel extends JPanel {
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
            this.entityBool = this.hydroBool = false;
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
                    if (message.equals("hydro")) {
                        areaPanel.hydroBool = true;
                    }
                    if (message.equals("entity")) {
                        areaPanel.entityBool = true;
                    }
                    if (message.equals("relief")) {
                        areaPanel.reliefBool = true;
                    }
                } else {
                    if (message.equals("hydro")) {
                        areaPanel.hydroBool = false;
                    }
                    if (message.equals("entity")) {
                        areaPanel.entityBool = false;
                    }
                    if (message.equals("relief")) {
                        areaPanel.reliefBool = false;
                    }
                }
                areaPanel.repaint();
            }
        }

    }

}
