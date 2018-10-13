/**
 *
 */
package com.rmsi.mast.studio.mobile.dao.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rmsi.mast.studio.dao.hibernate.GenericHibernateDAO;
import com.rmsi.mast.studio.domain.SpatialUnitPersonWithInterest;
import com.rmsi.mast.studio.mobile.dao.SpatialUnitPersonWithInterestDao;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * @author Shruti.Thakur
 *
 */
@Repository
public class SpatialUnitPersonWithInterestHibernateDao extends GenericHibernateDAO<SpatialUnitPersonWithInterest, Long> implements SpatialUnitPersonWithInterestDao {

    private static final Logger logger = Logger
            .getLogger(PersonHiberanteDao.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public SpatialUnitPersonWithInterest addNextOfKin(
            List<SpatialUnitPersonWithInterest> nextOfKinList, Long usin) {
        try {
            Iterator<SpatialUnitPersonWithInterest> nextOfKinIter = nextOfKinList
                    .iterator();

            SpatialUnitPersonWithInterest nextOfKin = null;

            while (nextOfKinIter.hasNext()) {

                nextOfKin = nextOfKinIter.next();
                // nextOfKin.setUsin(usin);

                makePersistent(nextOfKin);

            }
        } catch (Exception ex) {
            logger.error(ex);
            throw ex;
        }
        return null;
    }

    @Override
    public List<SpatialUnitPersonWithInterest> findByUsin(Long usin) {
        try {
            Query q = getEntityManager().createNativeQuery("select transactionid from la_ext_personlandmapping where isactive and persontypeid = 1 and landid=:usin");
            Integer tranId = (Integer)q.setParameter("usin", usin).getSingleResult();

            if (tranId != null) {
                Query query = getEntityManager().createQuery("Select sp from SpatialUnitPersonWithInterest sp where sp.landid = :usin and sp.isactive = true and sp.transactionid=:tranId order by sp.id asc ");
                List<SpatialUnitPersonWithInterest> pois = query.setParameter("usin", usin).setParameter("tranId", tranId).getResultList();

                if (pois.size() > 0) {
                    return pois;
                } 
            }
            return new ArrayList<SpatialUnitPersonWithInterest>();
        } catch (Exception e) {
            logger.error(e);
            return new ArrayList<SpatialUnitPersonWithInterest>();
        }
    }

    @Override
    public SpatialUnitPersonWithInterest findSpatialUnitPersonWithInterestById(Long id) {

        try {
            Query query = getEntityManager().createQuery("Select sp from SpatialUnitPersonWithInterest sp where sp.id = :usin order by sp.id asc ");
            SpatialUnitPersonWithInterest personinterest = (SpatialUnitPersonWithInterest) query.setParameter("usin", id).getSingleResult();

            return personinterest;
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    @Transactional
    public SpatialUnitPersonWithInterest findSpatialUnitPersonWithInterestByObj(
            SpatialUnitPersonWithInterest obj, Long landId) {
        SpatialUnitPersonWithInterest personinterest = null;
        try {
            Query query = getEntityManager().createQuery("Select sp from SpatialUnitPersonWithInterest sp where sp.id = :usin order by sp.id asc ");
            personinterest = (SpatialUnitPersonWithInterest) query.setParameter("usin", obj.getId()).getSingleResult();
            if (null == personinterest) {
                personinterest = new SpatialUnitPersonWithInterest();
                personinterest.setFirstName(obj.getFirstName());
                personinterest.setMiddleName(obj.getMiddleName());
                personinterest.setLastName(obj.getLastName());
                personinterest.setDob(obj.getDob());
                personinterest.setGender(obj.getGender());
                personinterest.setRelation(obj.getRelation());
                personinterest.setLandid(landId);
                personinterest.setCreatedby(1);
                personinterest.setCreateddate(new Date());
                personinterest.setIsactive(true);
            } else {
                personinterest.setFirstName(obj.getFirstName());
                personinterest.setMiddleName(obj.getMiddleName());
                personinterest.setLastName(obj.getLastName());
                personinterest.setDob(obj.getDob());
                personinterest.setGender(obj.getGender());
                personinterest.setRelation(obj.getRelation());
            }

            em.merge(personinterest);

            return obj;
        } catch (Exception e) {
            logger.error(e);
            return null;
        }

    }

    @Override
    public List<SpatialUnitPersonWithInterest> findByUsinandTransid(Long usin,
            Long transid) {
        try {
            Query query = getEntityManager().createQuery("Select sp from SpatialUnitPersonWithInterest sp where sp.landid = :usin and sp.transactionid = :transid and sp.isactive=true order by sp.id asc ");
            List<SpatialUnitPersonWithInterest> personinterest = query.setParameter("usin", usin).setParameter("transid", transid.intValue()).getResultList();

            if (personinterest.size() > 0) {
                return personinterest;
            } else {
                return new ArrayList<SpatialUnitPersonWithInterest>();
            }
        } catch (Exception e) {
            logger.error(e);
            return new ArrayList<SpatialUnitPersonWithInterest>();
        }
    }

}
