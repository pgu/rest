package pgu;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class ResponseUI extends JFrame {

    public ResponseUI(final ResponseResult response, final RequestConfig config) {

        final JTextArea responseArea = new JTextArea(100, 100);
        responseArea.setEditable(false);
        responseArea.setWrapStyleWord(true);
        responseArea.setText(response.body);

        final JPanel responseUI = new JPanel();
        responseUI.setLayout(new BoxLayout(responseUI, BoxLayout.PAGE_AXIS));
        responseUI.add(new JScrollPane(responseArea));

        setTitle("Response for " + config.url);
        setSize(320, 240);
        setLocationRelativeTo(null);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setContentPane(responseUI);

    }

}
