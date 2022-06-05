package com.web.crawler.client;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
@Slf4j
public class WebCrawlerClient {

    public Document getDocument(String url) throws IOException {
        try {
            return Jsoup.connect(url).get();
        } catch (Exception ex) {
            log.error("Caught exception for link {}",url);
        }
        return null;
    }

}
