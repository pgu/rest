package pgu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.ProtocolException;
import java.net.URL;

public class ClientRest {

    private enum ReqMethod {
        GET, POST, PUT
    }

    public static ResponseResult get(final RequestConfig config) {
        return send(config, ReqMethod.GET);
    }

    public static ResponseResult put(final RequestConfig config) {
        return send(config, ReqMethod.PUT);
    }

    public static ResponseResult post(final RequestConfig config) {
        return send(config, ReqMethod.POST);
    }

    private static ResponseResult send(final RequestConfig config, final ReqMethod reqMethod) {

        setAuthenticationBasic(config);

        final URL url = newUrl(config);

        final HttpURLConnection connection = newConnection(url);
        connection.setUseCaches(false);
        setRequestMethod(reqMethod, connection);
        connection.setRequestProperty("Accept", config.accept);

        if (ReqMethod.POST == reqMethod //
                || ReqMethod.PUT == reqMethod // 
        ) {
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestProperty("Content-Type", config.contentType);

            final OutputStream os = getOutputStream(connection);
            writeRequestBody(config, os);
            flushOutputStream(os);
        }

        /////// response 

        final int responseCode = getResponseCode(connection);
        final String contentType = connection.getContentType();
        final String location = connection.getHeaderField("Location");
        final StringBuilder sb = getResponseBody(connection);

        connection.disconnect();

        final ResponseResult result = new ResponseResult();
        result.code = responseCode;
        result.contentType = contentType;
        result.location = location;
        result.body = sb.toString();

        return result;
    }

    private static StringBuilder getResponseBody(final HttpURLConnection connection) {
        final BufferedReader reader = getReader(connection);
        final StringBuilder sb = new StringBuilder();
        String line = getLine(reader);
        while (line != null) {
            sb.append(line);
            sb.append("\n");

            line = getLine(reader);
        }
        return sb;
    }

    private static void flushOutputStream(final OutputStream os) {
        try {
            os.flush();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeRequestBody(final RequestConfig config, final OutputStream os) {
        try {
            os.write(config.body.getBytes());
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private static OutputStream getOutputStream(final HttpURLConnection connection) {
        OutputStream os = null;
        try {
            os = connection.getOutputStream();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return os;
    }

    private static String getLine(final BufferedReader reader) {
        try {
            return reader.readLine();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static BufferedReader getReader(final HttpURLConnection connection) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return reader;
    }

    private static int getResponseCode(final HttpURLConnection connection) {
        try {
            return connection.getResponseCode();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static void setRequestMethod(final ReqMethod reqMethod, final HttpURLConnection connection) {
        try {
            connection.setRequestMethod(reqMethod.toString());
        } catch (final ProtocolException e) {
            e.printStackTrace();
        }
    }

    private static HttpURLConnection newConnection(final URL url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private static URL newUrl(final RequestConfig config) {
        URL url = null;
        try {
            url = new URL(config.url);
        } catch (final MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static void setAuthenticationBasic(final RequestConfig config) {
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(config.username, config.password.toCharArray());
            }
        });
    }

}
