package com.web.crawler.repository;

import com.web.crawler.entity.CrawlerStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrawlerStatusRepository extends CrudRepository<CrawlerStatus, Integer>
{

}
