package com.rmsi.mast.studio.dao;

import com.rmsi.mast.studio.domain.MutationType;

public interface MutationTypeDao extends GenericDAO<MutationType, Integer> {
    MutationType getTypeById(int id);
}
