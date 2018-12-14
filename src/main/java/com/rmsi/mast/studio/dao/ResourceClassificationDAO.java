package com.rmsi.mast.studio.dao;

import com.rmsi.mast.studio.domain.ResourceClassification;
import java.util.List;

public interface ResourceClassificationDAO extends GenericDAO<ResourceClassification, Integer> {

    ResourceClassification getById(Integer Id);
    
    List<ResourceClassification> getActiveClassifications();
}
