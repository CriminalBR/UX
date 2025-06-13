package equipe.hackathon.gui;

import java.awt.*;

public class GuiUtils {
    public GridBagConstraints montarConstraints(int x, int y) {
        return montarConstraints(x, y, 1, 1);
    }

    public GridBagConstraints montarConstraints(int x, int y, int width, int height) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = width;
        constraints.gridheight = height;
        return constraints;
    }
}