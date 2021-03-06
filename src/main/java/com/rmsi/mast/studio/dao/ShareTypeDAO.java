package com.rmsi.mast.studio.dao;

import com.rmsi.mast.studio.domain.ShareType;

public interface ShareTypeDAO extends GenericDAO<ShareType, Integer> {

    ShareType getShareTypeByAttributeId(int attrId);
    ShareType getShareTypeById(int attrId);

}
