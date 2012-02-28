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

        final ResponseResult result = new ResponseResult();

        HttpURLConnection connection = null;
        try {
            setAuthenticationBasic(config);

            final URL url = newUrl(config);

            connection = newConnection(url);
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

            result.code = responseCode;
            result.contentType = contentType;
            result.location = location;
            result.body = sb.toString();

        } catch (final Throwable t) {
            result.exception = t.getMessage();

        } finally {
            if (null != connection) {
                connection.disconnect();
            }
        }
        return result;
    }

    private static StringBuilder getResponseBody(final HttpURLConnection connection) throws IOException {
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

    private static void flushOutputStream(final OutputStream os) throws IOException {
        os.flush();
    }

    private static void writeRequestBody(final RequestConfig config, final OutputStream os) throws IOException {
        os.write(config.body.getBytes());
    }

    private static OutputStream getOutputStream(final HttpURLConnection connection) throws IOException {
        return connection.getOutputStream();
    }

    private static String getLine(final BufferedReader reader) throws IOException {
        return reader.readLine();
    }

    private static BufferedReader getReader(final HttpURLConnection connection) throws IOException {
        return new BufferedReader(new InputStreamReader(connection.getInputStream()));
    }

    private static int getResponseCode(final HttpURLConnection connection) throws IOException {
        return connection.getResponseCode();
    }

    private static void setRequestMethod(final ReqMethod reqMethod, final HttpURLConnection connection)
            throws ProtocolException {
        connection.setRequestMethod(reqMethod.toString());
    }

    private static HttpURLConnection newConnection(final URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }

    private static URL newUrl(final RequestConfig config) throws MalformedURLException {
        return new URL(config.url);
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
