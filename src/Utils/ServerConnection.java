package Utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

public class ServerConnection {

    public void postToServer(String url, String gsonString) {
        StringEntity postString;
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);

        try {
            postString = new StringEntity(gsonString);
            post.setEntity(postString);
            post.setHeader("Content-type", "application/json");
            HttpResponse response = httpClient.execute(post);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
