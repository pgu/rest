package pgu;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class ResponseUI extends JFrame {

    public ResponseUI(final String response, final RequestConfig config) {

        final JTextArea responseArea = new JTextArea(50, 50);
        responseArea.setEditable(false);
        responseArea.setWrapStyleWord(true);
        responseArea.setText(response);

        final JPanel responseUI = new JPanel();
        responseUI.setLayout(new BoxLayout(responseUI, BoxLayout.PAGE_AXIS));
        responseUI.add(new JScrollPane(responseArea));

        final JFrame resp = new JFrame();
        resp.setTitle("Response for " + config.url);
        resp.setSize(320, 240);
        resp.setLocationRelativeTo(null);
        resp.setResizable(true);
        resp.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        resp.setContentPane(responseUI);

    }

}
