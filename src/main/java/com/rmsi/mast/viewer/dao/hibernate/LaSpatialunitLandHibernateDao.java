package com.rmsi.mast.viewer.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.rmsi.mast.studio.dao.hibernate.GenericHibernateDAO;
import com.rmsi.mast.studio.domain.LaSpatialunitLand;
import com.rmsi.mast.viewer.dao.LaSpatialunitLandDao;

@Repository
public class LaSpatialunitLandHibernateDao extends GenericHibernateDAO<LaSpatialunitLand, Integer>
        implements LaSpatialunitLandDao {

    Logger logger = Logger.getLogger(SpatialUnitHibernateDAO.class);

    @SuppressWarnings("unchecked")
    @Override
    public LaSpatialunitLand getLaSpatialunitLandDetails(Long landid) {
        try {
            return getEntityManager().find(LaSpatialunitLand.class, landid);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LaSpatialunitLand> getLaSpatialunitLandDetailsQ(Integer landid) {
        Query query = getEntityManager().createQuery("select LU from LaSpatialunitLand LU where LU.landid =:landId");
        try {
            List<LaSpatialunitLand> lstLaSpatialunitLands = query.setParameter("landId", Long.valueOf(landid + "")).getResultList();
            return lstLaSpatialunitLands;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            throw e;
        }
    }

    @Override
    public boolean updateLaSpatialunitLand(LaSpatialunitLand laSpatialunitLand) {
        String query = "update la_spatialunit_land set isactive = true where landid = " + laSpatialunitLand.getLandid() + " and isactive = true";
        int update = getEntityManager().createNativeQuery(query).executeUpdate();
        if (update > 0) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public boolean addLaSpatialunitLand(LaSpatialunitLand laSpatialunitLand) {

        return false;
    }

}
