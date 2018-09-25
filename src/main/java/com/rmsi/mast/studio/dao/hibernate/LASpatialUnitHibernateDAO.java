package com.rmsi.mast.studio.dao.hibernate;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rmsi.mast.studio.dao.LASpatialUnitDAO;
import com.rmsi.mast.studio.domain.SpatialUnit;
import java.util.Date;

@Repository
public class LASpatialUnitHibernateDAO extends GenericHibernateDAO<SpatialUnit, Long> implements LASpatialUnitDAO {

    @Override
    public boolean updateSpatialUnit(long landid, Integer applicationstatusid, Integer workflowstatusid) {
        try {
            Query query = getEntityManager().createQuery("UPDATE SpatialUnit sp SET  sp.applicationstatusid = :statusid , sp.workflowstatusid = :workflowid  where  sp.landid = :landid");

            query.setParameter("statusid", applicationstatusid);
            query.setParameter("workflowid", workflowstatusid);
            query.setParameter("landid", landid);

            int rows = query.executeUpdate();

            if (rows > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateSpatialUnitAppNum(long landid, String appNum, Date appDate, Integer appStatusId, Integer workflowId){
        try {
            Query query = getEntityManager().createQuery("UPDATE SpatialUnit sp SET sp.appNum = :appNum, sp.applicationdate = :appDate, sp.applicationstatusid = :statusid, sp.workflowstatusid = :workflowid where sp.landid = :landid");

            query.setParameter("statusid", appStatusId);
            query.setParameter("workflowid", workflowId);
            query.setParameter("appNum", appNum);
            query.setParameter("appDate", appDate);
            query.setParameter("landid", landid);

            int rows = query.executeUpdate();

            if (rows > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
            
    @Override
    public boolean deleteSpatialUnit(long landid, Integer applicationstatusid, Integer workflowstatusid) {

        try {
            Query query = getEntityManager().createQuery("UPDATE SpatialUnit sp SET sp.isactive =false, sp.applicationstatusid = :statusid , sp.workflowstatusid = :workflowid  where  sp.landid = :landid");

            query.setParameter("statusid", applicationstatusid);
            query.setParameter("workflowid", workflowstatusid);
            query.setParameter("landid", landid);

            int rows = query.executeUpdate();

            if (rows > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {

            return false;
        }
    }

}
