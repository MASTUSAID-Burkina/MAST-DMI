package com.rmsi.mast.viewer.dao.hibernate;

import java.util.List;


import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.rmsi.mast.studio.dao.hibernate.GenericHibernateDAO;
import com.rmsi.mast.studio.domain.ParcelCount;
import com.rmsi.mast.viewer.dao.ParcelCountDao;

@Repository
public class ParcelCountHibernateDAO extends GenericHibernateDAO<ParcelCount, Integer>
        implements ParcelCountDao {

    private static final Logger logger = Logger.getLogger(ParcelCountHibernateDAO.class);

    @Override
    public ParcelCount findParcelCountByTypeAndProjectName(String Type, String projectName) {

        try {
            @SuppressWarnings("unchecked")
            List<ParcelCount> parcelCount = 
                    getEntityManager()
                            .createQuery("Select pc from ParcelCount pc where pc.type = :type and pc.pname = :pname ")
                            .setParameter("type", Type)
                            .setParameter("pname", projectName)
                            .getResultList();

            if (parcelCount.size() > 0) {
                return parcelCount.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
