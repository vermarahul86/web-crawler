package com.web.crawler.service;

import com.web.crawler.entity.CrawlerStatus;
import com.web.crawler.entity.Movie;
import com.web.crawler.repository.CrawlerStatusRepository;
import com.web.crawler.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.Stack;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

@Service
@Slf4j
public class WebCrawlerServiceImpl implements WebCrawlerService {

    @Value("${web.crawler.target}")
    private String url;

    @Autowired
    private WebCrawler webCrawler;

    @Autowired
    private MovieCollector movieCollector;

    @Autowired
    private CrawlerStatusRepository crawlerStatusRepository;

    @Autowired
    private MovieRepository repository;

    Stack<String> linkStack = new Stack<>();

    @Override
    @Async
    public Boolean startCrawling() throws IOException, InterruptedException {
        Optional<CrawlerStatus> result = crawlerStatusRepository.findById(0); //will always be one entry in this.
        if(!result.isPresent() || "FINISHED".equals(result.get().getStatus())){
            Stack<String> linkStack = new Stack<>();
            linkStack.push(url);
            BlockingDeque<Element> queue = new LinkedBlockingDeque();
            webCrawler.startCrawling(linkStack, queue);
            /*start collecting all the movies from the elements identified.*/
            movieCollector.collectMovies(queue);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public CrawlerStatus getStatus() {
        return crawlerStatusRepository.findById(0).get();
    }

    @Override
    public Iterable<Movie> getMovies() {
        return repository.findAll();
    }
}
