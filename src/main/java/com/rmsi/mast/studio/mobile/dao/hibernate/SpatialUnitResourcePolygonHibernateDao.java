/**
 *
 */
package com.rmsi.mast.studio.mobile.dao.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.rmsi.mast.studio.dao.hibernate.GenericHibernateDAO;
import com.rmsi.mast.studio.domain.BasicResourceLine;
import com.rmsi.mast.studio.domain.BasicResourcePolygon;
import com.rmsi.mast.studio.domain.SpatialUnit;
import com.rmsi.mast.studio.domain.SpatialUnitResourcePolygon;
import com.rmsi.mast.studio.domain.fetch.ClaimBasic;
import com.rmsi.mast.studio.domain.fetch.DisputeBasic;
import com.rmsi.mast.studio.domain.fetch.MediaBasic;
import com.rmsi.mast.studio.domain.fetch.RightBasic;
import com.rmsi.mast.studio.domain.fetch.SpatialUnitTable;
import com.rmsi.mast.studio.mobile.dao.SpatialUnitDao;
import com.rmsi.mast.studio.mobile.dao.SpatialUnitResourcePolygonDao;

/**
 * @author Shruti.Thakur
 *
 */
@Repository
public class SpatialUnitResourcePolygonHibernateDao extends
        GenericHibernateDAO<SpatialUnitResourcePolygon, Long> implements SpatialUnitResourcePolygonDao {

    private static final Logger logger = Logger
            .getLogger(SpatialUnitResourcePolygonHibernateDao.class);

    @Override
    public SpatialUnitResourcePolygon addSpatialUnitResourcePolygon(
            SpatialUnitResourcePolygon spatialUnit) {
        try {
            return makePersistent(spatialUnit);

        } catch (Exception ex) {
            System.out.println("Exception while adding data...." + ex);
            logger.error(ex);
            throw ex;
        }
    }

    @Override
    public List<SpatialUnit> getSpatialUnitByProject(String projectId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SpatialUnit findByImeiandTimeStamp(String imeiNumber, Date surveyDate) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ClaimBasic getSpatialUnitByUsin(long usin) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<SpatialUnit> findSpatialUnitByStatusId(String projectId,
            int statusId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ClaimBasic> getClaimsBasicByStatus(Integer projectId,
            int statusId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ClaimBasic> getClaimsBasicByProject(Integer projectId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ClaimBasic> getClaimsBasicByLandId(Long landid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BasicResourcePolygon getBasicResourcePolygon(Long landid){
        return getEntityManager().find(BasicResourcePolygon.class, landid);
    }
    
    @Override 
    public BasicResourcePolygon saveBasicResourcePolygon(BasicResourcePolygon poly){
        BasicResourcePolygon obj = getEntityManager().merge(poly);
        flush();
        return obj;
    }
}
