package com.rmsi.mast.viewer.dao.hibernate;

import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.rmsi.mast.studio.dao.hibernate.GenericHibernateDAO;
import com.rmsi.mast.studio.domain.LaExtProcess;
import com.rmsi.mast.viewer.dao.LaExtProcessDao;

@Repository
public class LaExtProcessHibernateDao extends GenericHibernateDAO<LaExtProcess, Integer>
        implements LaExtProcessDao {

    Logger logger = Logger.getLogger(LaExtProcessHibernateDao.class);

    @Override
    public LaExtProcess makePersistent(LaExtProcess entity) {
        return null;
    }

    @Override
    public void makeTransient(LaExtProcess entity) {

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LaExtProcess> getAllProcessDetails() {
        try {
            Query query = getEntityManager().createQuery("Select lp from LaExtProcess lp where lp.isactive = true order by lp.processname");
            List<LaExtProcess> lstExtProcesses = query.getResultList();
            return lstExtProcesses;
        } catch (Exception e) {
        }
        return null;
    }

}
