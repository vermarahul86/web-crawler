package com.web.crawler.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "status")
public class CrawlerStatus {
    @Id
    private Integer id = 0;
    private String status;
    private Integer moviesCount;
}
