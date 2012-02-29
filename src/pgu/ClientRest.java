package pgu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
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

            final URL url = new URL(config.url);

            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(30 * 1000);
            connection.setUseCaches(false);
            connection.setRequestMethod(reqMethod.toString());
            connection.setRequestProperty("Accept", config.accept);

            if (ReqMethod.POST == reqMethod //
                    || ReqMethod.PUT == reqMethod // 
            ) {
                connection.setDoOutput(true);
                connection.setInstanceFollowRedirects(false);
                connection.setRequestProperty("Content-Type", config.contentType);

                final OutputStream os = connection.getOutputStream();
                os.write(config.body.getBytes());
                os.flush();
                os.close();
            }

            /////// response 

            final int responseCode = connection.getResponseCode();
            final String responseMessage = connection.getResponseMessage();
            final String contentType = connection.getContentType();
            final String location = connection.getHeaderField("Location");
            final StringBuilder sb = getResponseBody(connection);

            result.code = responseCode;
            result.responseMsg = responseMessage;
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
        final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        final StringBuilder sb = new StringBuilder();
        String line = reader.readLine();
        while (line != null) {
            sb.append(line);
            sb.append("\n");

            line = reader.readLine();
        }
        return sb;
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
