package pgu;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class AppUI extends JFrame {

    private static final String URL_BASE         = "http://localhost:8081/wikeo-core";

    private final JTextField    fieldUrlBase     = new JTextField();
    private final JTextField    fieldUrl         = new JTextField();
    private final JButton       btnGet           = new JButton();
    private final JButton       btnPost          = new JButton();
    private final JButton       btnPut           = new JButton();
    private final JTextArea     fieldBody        = new JTextArea(100, 500);

    private final JTextField    fieldUser        = new JTextField();
    private final JTextField    fieldPassword    = new JTextField();
    private final JTextField    fieldContentType = new JTextField();

    private enum RequestAction {
        GET, PUT, POST
    }

    public AppUI() {
        buildAppUI();
    }

    private static final int WIDTH       = 500;
    private static final int HEIGHT_LINE = 20;

    private void buildAppUI() {
        setTitle("Pgu Client Rest");
        setSize(WIDTH, 600);
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
        addUIGet(btns);
        addUIPost(btns);
        addUIPut(btns);
        requestUI.add(btns);
        requestUI.add(Box.createRigidArea(new Dimension(0, 20)));

        addUIUserPassword(requestUI);
        requestUI.add(Box.createRigidArea(new Dimension(0, 5)));

        addUIContentType(requestUI);
        requestUI.add(Box.createRigidArea(new Dimension(0, 5)));

        requestUI.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        return requestUI;
    }

    private void addUIContentType(final JPanel requestUI) {
        fieldContentType.setSize(WIDTH, HEIGHT_LINE);
        fieldContentType.setText("application/xml");

        final Box box = Box.createHorizontalBox();
        box.add(new JLabel("Content-Type "));
        box.add(fieldContentType);
        requestUI.add(box);
    }

    private void addUIUserPassword(final JPanel requestUI) {
        fieldUser.setSize(WIDTH, HEIGHT_LINE);
        fieldPassword.setSize(WIDTH, HEIGHT_LINE);

        fieldUser.setText("wikeo");
        fieldPassword.setText("oekiw");

        Box box = Box.createHorizontalBox();
        box.add(new JLabel("User  "));
        box.add(fieldUser);
        requestUI.add(box);

        box = Box.createHorizontalBox();
        box.add(new JLabel("Pwd   "));
        box.add(fieldPassword);
        requestUI.add(box);
    }

    private void addUIBody(final JPanel requestUI) {
        fieldBody.setSize(WIDTH, 400);
        fieldBody.setText("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        final JScrollPane scroll = new JScrollPane(fieldBody);
        requestUI.add(scroll);
    }

    private void addUIPut(final JPanel panel) {
        btnPut.setSize(WIDTH, HEIGHT_LINE * 2);
        btnPut.setAction(new AbstractAction(RequestAction.PUT.toString()) {

            @Override
            public void actionPerformed(final ActionEvent e) {
                sendRequest(RequestAction.PUT);
            }
        });
        panel.add(btnPut);
    }

    private void addUIPost(final JPanel panel) {
        btnPost.setSize(WIDTH, HEIGHT_LINE * 2);
        btnPost.setAction(new AbstractAction(RequestAction.POST.toString()) {

            @Override
            public void actionPerformed(final ActionEvent e) {
                sendRequest(RequestAction.POST);
            }
        });
        panel.add(btnPost);
    }

    private void addUIGet(final JPanel panel) {
        btnGet.setSize(WIDTH, HEIGHT_LINE * 2);
        btnGet.setAction(new AbstractAction(RequestAction.GET.toString()) {

            @Override
            public void actionPerformed(final ActionEvent e) {
                sendRequest(RequestAction.GET);
            }

        });
        panel.add(btnGet);
    }

    private void addUIUrl(final JPanel requestUI) {
        fieldUrl.setSize(WIDTH, HEIGHT_LINE);
        fieldUrl.setText("");
        requestUI.add(fieldUrl);
    }

    private void addUIUrlBase(final JPanel requestUI) {
        fieldUrlBase.setSize(WIDTH, HEIGHT_LINE);
        fieldUrlBase.setText(URL_BASE);
        requestUI.add(fieldUrlBase);
    }

    private RequestConfig getRequestConfig() {

        final RequestConfig config = new RequestConfig();
        config.url = fieldUrlBase.getText() + fieldUrl.getText();
        config.username = fieldUser.getText();
        config.password = fieldPassword.getText();
        config.body = fieldBody.getText();
        config.contentType = fieldContentType.getText();
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
