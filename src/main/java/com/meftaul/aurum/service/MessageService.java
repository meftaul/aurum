package com.meftaul.aurum.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

@Service
@Transactional
public class MessageService {

    String username = "meftaul";
    String hash_token = "5d32ee322e2ef7dc9da709f9da789ae1";

    private final Logger log = LoggerFactory.getLogger(MessageService.class);

    public String sendSms() {
        String to = "01670645784";
        String output;

        //Your message to send, Add URL encoding here.
        String textmessage = "my message is here";

        URLConnection myURLConnection = null;
        URL myURL = null;
        BufferedReader reader = null;

        // encode the message content
        String encoded_message = URLEncoder.encode(textmessage);
        String apiUrl="http://alphasms.biz/index.php?app=ws&op=pv";

        StringBuilder sgcPostContent= new StringBuilder(apiUrl);
        sgcPostContent.append("u="+username);
        sgcPostContent.append("h="+hash_token);
        sgcPostContent.append("&to="+to);
        sgcPostContent.append("&msg="+encoded_message);

        apiUrl = sgcPostContent.toString();

        try
        {
            //prepare connection
            myURL = new URL(apiUrl);
            myURLConnection = myURL.openConnection();
            myURLConnection.connect();
            reader= new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));

            //read the output

            while ((output = reader.readLine()) != null)
                //print output
                log.debug("OUTPUT", ""+output);

            //Close connection
            reader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return "test";
    }

}
