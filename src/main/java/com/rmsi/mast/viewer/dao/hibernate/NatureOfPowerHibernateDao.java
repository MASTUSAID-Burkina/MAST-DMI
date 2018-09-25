package com.rmsi.mast.viewer.dao.hibernate;


import org.springframework.stereotype.Repository;

import com.rmsi.mast.studio.dao.hibernate.GenericHibernateDAO;
import com.rmsi.mast.studio.domain.NatureOfPower;
import com.rmsi.mast.viewer.dao.NatureOfPowerDao;

@Repository
public class NatureOfPowerHibernateDao extends GenericHibernateDAO<NatureOfPower, Integer> implements NatureOfPowerDao {

}
