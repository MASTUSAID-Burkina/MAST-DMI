package com.rmsi.mast.studio.mobile.dao.hibernate;


import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.rmsi.mast.studio.dao.hibernate.GenericHibernateDAO;
import com.rmsi.mast.studio.domain.ShareType;
import com.rmsi.mast.studio.mobile.dao.ShareTypeDao;
import java.util.List;

/**
 * @author Shruti.Thakur
 *
 */
@Repository
public class ShareTypeHibernateDao extends
        GenericHibernateDAO<ShareType, Integer> implements ShareTypeDao {

    private static final Logger logger = Logger.getLogger(ShareTypeHibernateDao.class);

    @Override
    public ShareType getTenureRelationshipTypeById(int landsharetypeid) {
        try {

            String query = "select s from ShareType s where s.landsharetypeid = :landsharetypeid";

            @SuppressWarnings("unchecked")
            ShareType surveyprojectattributes = (ShareType) getEntityManager()
                    .createQuery(query).setParameter("landsharetypeid", landsharetypeid).getSingleResult();

            if (surveyprojectattributes != null) {
                return surveyprojectattributes;
            }
        } catch (Exception ex) {
            logger.error(ex);
            throw ex;
        }
        return null;
    }

    @Override
    public List<ShareType> getShareTypesForMobile() {
        return getEntityManager().createQuery("select s from ShareType s where s.landsharetypeid=7 or s.landsharetypeid=8").getResultList();
    }
}
