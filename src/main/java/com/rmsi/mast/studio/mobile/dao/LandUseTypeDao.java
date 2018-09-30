/**
 *
 */
package com.rmsi.mast.studio.mobile.dao;

import com.rmsi.mast.studio.dao.GenericDAO;
import com.rmsi.mast.studio.domain.LandUseType;
import java.util.List;

public interface LandUseTypeDao extends GenericDAO<LandUseType, Integer> {

    LandUseType getLandUseTypeById(int landUseTypeId);

    LandUseType getLandUseTypeBylandusetypeId(int landUseTypeId);

    List<LandUseType> findEntriesById(String existingUse);
}
