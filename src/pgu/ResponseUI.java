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
        responseUI.setLayout(new BoxLayout(responseUI, BoxLayout.PAGE_AXIS));

        if (null != response.exception) {
            addLabel("Exception", responseUI);
            addField(response.exception, responseUI);

        } else {
            addRow("Url  ", "" + config.url, responseUI);
            responseUI.add(Box.createRigidArea(new Dimension(0, 5)));

            addCodeAndMessage(response, responseUI);
            responseUI.add(Box.createRigidArea(new Dimension(0, 5)));

            addRow("Content-Type  ", response.contentType, responseUI);
            responseUI.add(Box.createRigidArea(new Dimension(0, 5)));

            addRow("Location  ", response.location, responseUI);
            responseUI.add(Box.createRigidArea(new Dimension(0, 5)));

            addLabel("Body", responseUI);
            final JTextArea responseArea = new JTextArea(100, 100);
            responseArea.setWrapStyleWord(true);
            responseArea.setText(response.body);

            final JScrollPane scroll = new JScrollPane(responseArea);
            responseUI.add(scroll);
        }

        responseUI.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        setTitle("Response for " + config.url);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setContentPane(responseUI);
    }

    private void addRow(final String label, final String value, final JPanel responseUI) {
        final Box box = Box.createHorizontalBox();
        box.add(new JLabel(label));
        final JTextField jtf = new JTextField(value);
        box.add(jtf);
        responseUI.add(box);
    }

    private void addCodeAndMessage(final ResponseResult response, final JPanel responseUI) {
        final Box box = Box.createHorizontalBox();

        box.add(new JLabel("Code  "));
        final JTextField code = new JTextField("" + response.code);
        box.add(code);

        box.add(new JLabel("  Msg  "));
        final JTextField codemsg = new JTextField(response.responseMsg);
        box.add(codemsg);

        responseUI.add(box);
    }

    private static void addField(final Object value, final JPanel responseUI) {
        final JTextField jtf = new JTextField(null == value ? "" : value.toString());
        responseUI.add(jtf);
    }

    private static void addLabel(final String label, final JPanel responseUI) {
        final JLabel jlabel = new JLabel(label);
        responseUI.add(jlabel);
    }

}
