/**
 *
 */
package com.rmsi.mast.studio.mobile.dao.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.rmsi.mast.studio.dao.hibernate.GenericHibernateDAO;
import com.rmsi.mast.studio.domain.LandUseType;
import com.rmsi.mast.studio.mobile.dao.LandUseTypeDao;
import javax.persistence.Query;

/**
 * @author shruti.thakur
 *
 */
@Repository
public class LandUseTypeHibernateDao extends
        GenericHibernateDAO<LandUseType, Integer> implements LandUseTypeDao {

    private static final Logger logger = Logger.getLogger(GenderHibernateDao.class);

    @Override
    public LandUseType getLandUseTypeById(int landUseTypeId) {

        try {
            String query = "select lu.* from la_baunit_landusetype lu inner join "
                    + "la_ext_attributeoptions ao on ao.parentid = lu.landusetypeid where "
                    + "ao.attributeoptionsid = " + landUseTypeId;

            @SuppressWarnings("unchecked")
            List<LandUseType> landUseType = getEntityManager()
                    .createNativeQuery(query, LandUseType.class)
                    .getResultList();

            if (landUseType != null && landUseType.size() > 0) {
                return landUseType.get(0);
            }
        } catch (Exception ex) {
            logger.error(ex);
            throw ex;
        }
        return null;
    }

    @Override
    public LandUseType getLandUseTypeBylandusetypeId(int landUseTypeId) {

        try {
            String query = "select lu from LandUseType lu where " + "lu.landusetypeid = " + landUseTypeId;

            @SuppressWarnings("unchecked")
            List<LandUseType> landUseType = getEntityManager()
                    .createQuery(query)
                    .getResultList();

            if (landUseType != null && landUseType.size() > 0) {
                return landUseType.get(0);
            }
        } catch (Exception ex) {
            logger.error(ex);
            throw ex;
        }
        return null;
    }

    @Override
    public List<LandUseType> findEntriesById(String existingUse) {
        try {
            String query = "Select * from la_baunit_landusetype where landusetypeid in (" + existingUse + ")";
            Query executeQuery = getEntityManager().createNativeQuery(query, LandUseType.class);
            @SuppressWarnings("unchecked")
            List<LandUseType> landtypeList = executeQuery.getResultList();

            if (landtypeList.size() > 0) {
                return landtypeList;
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }
}
