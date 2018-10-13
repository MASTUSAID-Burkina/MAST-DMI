package com.rmsi.mast.studio.dao.hibernate;

import com.rmsi.mast.studio.dao.MutationTypeDao;
import com.rmsi.mast.studio.domain.MutationType;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class MutationTypeHibernateDao extends GenericHibernateDAO<MutationType, Integer> implements MutationTypeDao {
    private static final Logger logger = Logger.getLogger(MutationTypeHibernateDao.class);

    @Override
    public MutationType getTypeById(int id) {
        try {
            String query = "select t from MutationType t where  t.id =" + id;
            List<MutationType> result = getEntityManager().createQuery(query).getResultList();

            if (result != null && result.size() > 0) {
                return result.get(0);
            }
        } catch (Exception ex) {
            logger.error(ex);
            throw ex;
        }
        return null;
    }
}
