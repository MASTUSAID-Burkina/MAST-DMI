package com.rmsi.mast.studio.dao.hibernate;

import com.rmsi.mast.studio.dao.TitleTypeDao;
import com.rmsi.mast.studio.domain.TitleType;
import org.springframework.stereotype.Repository;

@Repository
public class TitleTypeHibernateDao extends GenericHibernateDAO<TitleType, Integer> implements TitleTypeDao {
    
}
