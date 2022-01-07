package com.travel.staybooking.dao;

import com.travel.staybooking.entity.Location;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 *CRUDrepo => ES repo => location repo
 * lots we need to implement
 * ES repo provides basic methods
 *
 */
public interface LocationDao extends ElasticsearchRepository<Location,Long>, CustomLocationRepository{

}
