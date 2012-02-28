package pgu;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class ResponseUI extends JFrame {

    public ResponseUI(final ResponseResult response, final RequestConfig config) {

        final JPanel responseUI = new JPanel();
        responseUI.setAlignmentX(LEFT_ALIGNMENT);
        responseUI.setLayout(new BoxLayout(responseUI, BoxLayout.PAGE_AXIS));

        addLabel("Url", responseUI);
        addField(config.url, responseUI);
        responseUI.add(Box.createRigidArea(new Dimension(0, 5)));

        addLabel("Code", responseUI);
        addField(response.code, responseUI);
        responseUI.add(Box.createRigidArea(new Dimension(0, 5)));

        addLabel("Content-Type", responseUI);
        addField(response.contentType, responseUI);
        responseUI.add(Box.createRigidArea(new Dimension(0, 5)));

        addLabel("Location", responseUI);
        addField(response.location, responseUI);
        responseUI.add(Box.createRigidArea(new Dimension(0, 5)));

        addLabel("Body", responseUI);
        final JTextArea responseArea = new JTextArea(100, 100);
        responseArea.setEditable(false);
        responseArea.setWrapStyleWord(true);
        responseArea.setText(response.body);

        final JScrollPane scroll = new JScrollPane(responseArea);
        scroll.setAlignmentX(LEFT_ALIGNMENT);
        responseUI.add(scroll);

        responseUI.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        setTitle("Response for " + config.url);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setContentPane(responseUI);
    }

    private static void addField(final Object value, final JPanel responseUI) {
        final JTextField jtf = new JTextField(null == value ? "" : value.toString());
        jtf.setAlignmentX(LEFT_ALIGNMENT);
        responseUI.add(jtf);
    }

    private static void addLabel(final String label, final JPanel responseUI) {
        final JLabel jlabel = new JLabel(label);
        jlabel.setAlignmentX(LEFT_ALIGNMENT);
        responseUI.add(jlabel);
    }

}
