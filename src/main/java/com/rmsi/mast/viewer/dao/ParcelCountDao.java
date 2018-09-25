package com.rmsi.mast.viewer.dao;

import com.rmsi.mast.studio.dao.GenericDAO;
import com.rmsi.mast.studio.domain.ParcelCount;

public interface ParcelCountDao extends GenericDAO<ParcelCount, Integer> {
    public ParcelCount findParcelCountByTypeAndProjectName(String Type,String projectName);
}
