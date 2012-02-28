package pgu;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class AppUI extends JFrame {

    private static final String URL_BASE     = "http://localhost:8081/wikeo-core";

    private final JTextField    fieldUrlBase = new JTextField();
    private final JTextField    fieldUrl     = new JTextField();
    private final JButton       btnGet       = new JButton();
    private final JButton       btnPost      = new JButton();
    private final JButton       btnPut       = new JButton();
    private final JTextArea     fieldBody    = new JTextArea(10, 10);

    private enum RequestAction {
        GET, PUT, POST
    }

    public AppUI() {
        buildAppUI();
    }

    private void buildAppUI() {
        setTitle("Rest");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setContentPane(buildRequestUI());
    }

    private Container buildRequestUI() {
        final JPanel requestUI = new JPanel();
        requestUI.setLayout(new BoxLayout(requestUI, BoxLayout.PAGE_AXIS));

        addUIUrlBase(requestUI);
        requestUI.add(Box.createRigidArea(new Dimension(0, 5)));

        addUIUrl(requestUI);
        requestUI.add(Box.createRigidArea(new Dimension(0, 5)));

        addUIBody(requestUI);
        requestUI.add(Box.createRigidArea(new Dimension(0, 5)));

        final JPanel btns = new JPanel();
        btns.setLayout(new FlowLayout());

        addUIGet(btns);
        addUIPost(btns);
        addUIPut(btns);
        requestUI.add(btns);

        requestUI.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        return requestUI;
    }

    private void addUIBody(final JPanel requestUI) {
        fieldBody.setText("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        final JScrollPane scroll = new JScrollPane(fieldBody);
        requestUI.add(scroll);
    }

    private void addUIPut(final JPanel panel) {
        btnPut.setAction(new AbstractAction(RequestAction.PUT.toString()) {

            @Override
            public void actionPerformed(final ActionEvent e) {
                sendRequest(RequestAction.PUT);
            }
        });
        panel.add(btnPut);
    }

    private void addUIPost(final JPanel panel) {
        btnPost.setAction(new AbstractAction(RequestAction.POST.toString()) {

            @Override
            public void actionPerformed(final ActionEvent e) {
                sendRequest(RequestAction.POST);
            }
        });
        panel.add(btnPost);
    }

    private void addUIGet(final JPanel panel) {
        btnGet.setAction(new AbstractAction(RequestAction.GET.toString()) {

            @Override
            public void actionPerformed(final ActionEvent e) {
                sendRequest(RequestAction.GET);
            }

        });
        panel.add(btnGet);
    }

    private void addUIUrl(final JPanel requestUI) {
        fieldUrl.setText("");
        requestUI.add(fieldUrl);
    }

    private void addUIUrlBase(final JPanel requestUI) {
        fieldUrlBase.setText(URL_BASE);
        requestUI.add(fieldUrlBase);
    }

    private RequestConfig getRequestConfig() {

        final String url = fieldUrlBase.getText() + fieldUrl.getText();
        // TODO PGU  validation des valeurs
        // TODO PGU add fields for user/password
        // TODO PGU add fields for content type

        final RequestConfig config = new RequestConfig();
        config.url = url;
        config.username = "wikeo";
        config.password = "oekiw";
        config.body = fieldBody.getText();
        return config;
    }

    private void sendRequest(final RequestAction action) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                final RequestConfig config = getRequestConfig();
                ResponseResult response = null;

                if (RequestAction.GET == action) {
                    response = ClientRest.get(config);

                } else if (RequestAction.POST == action) {
                    response = ClientRest.post(config);

                } else if (RequestAction.PUT == action) {
                    response = ClientRest.put(config);

                } else {
                    throw new IllegalArgumentException("action unknown: " + action);
                }

                final ResponseUI responseUI = new ResponseUI(response, config);
                responseUI.setVisible(true);
            }
        });
    }
}
