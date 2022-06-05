package com.web.crawler.controller;


import com.web.crawler.entity.CrawlerStatus;
import com.web.crawler.entity.Movie;
import com.web.crawler.service.WebCrawlerService;
import io.netty.handler.codec.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
/**
 * MovieCrawlerController offers following 3 operations.
 * /crawler/start -> Start crawling.
 * /crawler/status -> Status of crawled data.
 * /crawler/movies -> Movies crawled so far.
 * */
@RestController
@RequestMapping("/crawler")
public class MovieCrawlerController  {

    @Autowired
    private WebCrawlerService webCrawlerService;

    @GetMapping("/start")
    public ResponseEntity<HttpResponse> startCrawler() throws IOException, InterruptedException {
        webCrawlerService.startCrawling();
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/status")
    public ResponseEntity<CrawlerStatus> getCrawlerStatus() {
        return new ResponseEntity<>(webCrawlerService.getStatus(), HttpStatus.OK);
    }

    @GetMapping("/movies")
    public ResponseEntity<Iterable<Movie>> getMovies() {
        return new ResponseEntity<>(webCrawlerService.getMovies(), HttpStatus.OK);
    }


}
