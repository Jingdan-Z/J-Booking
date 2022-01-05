package com.travel.staybooking.dao;

import com.travel.staybooking.entity.Location;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 *CRUDrepo => ES repo => location repo
 * lots we need to implement
 *
 */
public interface LocationDao extends ElasticsearchRepository<Location,Long>, CustomLocationRepository{
    List<Long> searchByDistance(double lat, double lon, String distance);
}
