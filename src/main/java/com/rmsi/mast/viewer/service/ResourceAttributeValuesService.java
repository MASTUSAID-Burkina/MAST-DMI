package com.rmsi.mast.viewer.service;

import java.util.List;

import com.rmsi.mast.studio.domain.ResourceAttributeValues;
import com.rmsi.mast.studio.mobile.transferobjects.SearchResult;

public interface ResourceAttributeValuesService {

    List<ResourceAttributeValues> getResourceAttributeValuesBylandId(Integer projectId, Integer Id);

    SearchResult searchResources(String project, int chartered, int tenureType, int classType, String owner, Integer startfrom);

    Integer getAllresouceCountByproject(String project);

    List<ResourceAttributeValues> getResourceAttributeValuesByMasterlandid(Integer Id);

    List<Object[]> getResourceAttributeValuesAndDatatypeBylandId(Integer projectId, Integer Id);

}
