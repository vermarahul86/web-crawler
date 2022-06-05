package com.web.crawler.service;

import com.web.crawler.entity.CrawlerStatus;
import com.web.crawler.entity.Movie;
import com.web.crawler.repository.CrawlerStatusRepository;
import com.web.crawler.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.BlockingDeque;

@Component
@Slf4j
public class MovieCollector {

    @Autowired
    private MovieRepository repository;

    @Value("${web.crawler.target}")
    private String url;

    @Autowired
    private CrawlerStatusRepository crawlerStatusRepository;

    @Async("asyncExecutor")
    public void collectMovies(BlockingDeque<Element> queue) throws InterruptedException, IOException {
        int count = 0;
        while (true) {
            Element movieElement = queue.take();
            try {
                Elements elements = movieElement.getElementsByTag("img");
                Element img = null;
                if(null != elements && elements.size() > 0) {
                    img = elements.get(0);
                }
                if(null != img) {
                    String movieImage =  img.attr("src");
                    String title = movieElement.attr("title");
                    Connection.Response response = Jsoup.connect(url + movieImage).ignoreContentType(true).execute();

                    /*FileOutputStream outputStream = new FileOutputStream(new File("C://data//projects//working//files//"+title+".jpeg"));
                    outputStream.write(response.bodyAsBytes());*/

                    Movie movie = new Movie(title, response.bodyAsBytes());
                    log.debug("Persisting movie {}", movie);
                    repository.save(movie);
                    CrawlerStatus status = crawlerStatusRepository.findById(0).get();
                    status.setMoviesCount(status.getMoviesCount() + 1);
                    crawlerStatusRepository.save(status);
                }
            } catch (Exception ex) {
                log.error("Failed to persist movie due to exception ", ex);
            }
        }
    }

}
