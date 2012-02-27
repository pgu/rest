package pgu;

import javax.swing.SwingUtilities;

public class Run {

    public static void main(final String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                final AppUI appUI = new AppUI();
                appUI.setVisible(true);
            }
        });

        while (true) {
            // wait...
        }
    }

}
