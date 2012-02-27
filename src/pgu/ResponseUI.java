package pgu;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class ResponseUI extends JFrame {

    private final JTextField fieldUrl = new JTextField();
    private final JTextField fieldCode = new JTextField();
    private final JTextField fieldContentType = new JTextField();
    private final JTextField fieldLocation = new JTextField();
    private final JTextArea responseArea = new JTextArea(100, 100);

    public ResponseUI(final ResponseResult response, final RequestConfig config) {

        final JPanel responseUI = new JPanel();
        responseUI.setLayout(new BoxLayout(responseUI, BoxLayout.PAGE_AXIS));

        fieldUrl.setText("Url: " + config.url);
        responseUI.add(fieldUrl);
        responseUI.add(Box.createRigidArea(new Dimension(0, 5)));

        fieldCode.setText("Code: " + response.code);
        responseUI.add(fieldCode);
        responseUI.add(Box.createRigidArea(new Dimension(0, 5)));

        fieldContentType.setText("Content-Type: " + response.contentType);
        responseUI.add(fieldContentType);
        responseUI.add(Box.createRigidArea(new Dimension(0, 5)));

        fieldLocation.setText("Location: " + response.location);
        responseUI.add(fieldLocation);
        responseUI.add(Box.createRigidArea(new Dimension(0, 5)));

        responseArea.setEditable(false);
        responseArea.setWrapStyleWord(true);
        responseArea.setText(response.body);
        responseUI.add(new JScrollPane(responseArea));

        responseUI.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        setTitle("Response for " + config.url);
        setSize(320, 500);
        setLocationRelativeTo(null);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setContentPane(responseUI);

    }
}
