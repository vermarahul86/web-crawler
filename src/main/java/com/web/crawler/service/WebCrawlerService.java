package com.web.crawler.service;

import com.web.crawler.entity.CrawlerStatus;
import com.web.crawler.entity.Movie;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Stack;

@Service
public interface WebCrawlerService {

    Boolean startCrawling() throws IOException, InterruptedException;

    CrawlerStatus getStatus();

    Iterable<Movie> getMovies();

}
