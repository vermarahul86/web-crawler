package com.web.crawler.service;

import com.web.crawler.client.WebCrawlerClient;
import com.web.crawler.entity.CrawlerStatus;
import com.web.crawler.repository.CrawlerStatusRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class WebCrawler {

    @Autowired
    private WebCrawlerClient webCrawlerClient;

    @Autowired
    private CrawlerStatusRepository crawlerStatusRepository;

    @Value("${web.crawler.target}")
    private String url;

    private Set<String> links = new HashSet<>();

    @Async("asyncExecutor")
    public void startCrawling(Stack<String> linkStack, BlockingDeque<Element> queue) throws IOException {
        crawlerStatusRepository.save(new CrawlerStatus(0, "IN PROGRESS", 0));
        AtomicInteger count = new AtomicInteger(0);
        crawl(linkStack, queue, count);
        crawlerStatusRepository.save(new CrawlerStatus(0, "FINISHED", count.get()));
    }

    private void crawl(Stack<String> linkStack, BlockingDeque<Element> queue, AtomicInteger count ) throws IOException {
        if(linkStack.empty()) {
            return ;
        }
        /*We always have one url to crawled and, we take it out for processing.*/
        Document document = webCrawlerClient.getDocument(linkStack.remove(0));
        if(null != document) {

            log.debug(document.html());
            Elements linksOnPage = document.select("a[href]");
            log.debug("Found {} links", linksOnPage.size());

            linksOnPage.forEach(element -> {
                String href = element.attr("href");
                String title = element.attr("title");
                if(href.startsWith("/movie") && StringUtils.hasLength(title)) {
                    log.debug("Found  movie {}", element);
                    count.getAndIncrement();
                    queue.push(element); //Push the element to queue which holds movies details.
                } else {
                    /*We make entry to the stack for next iteration of parsing of new url identified.*/
                    if(!"/".equals(href) && !"#".equals(href) && !href.startsWith("http")) {
                        if(links.add(href)) {
                            linkStack.push( url + href);
                        }
                    }
                }
            });
        }
        crawl(linkStack, queue, count);
    }
}
