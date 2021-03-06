package com.rmsi.mast.viewer.dao.hibernate;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rmsi.mast.studio.dao.hibernate.GenericHibernateDAO;
import com.rmsi.mast.studio.domain.LaExtTransactiondetail;
import com.rmsi.mast.viewer.dao.LaExtTransactiondetailDao;

@Repository
public class LaExtTransactiondetailHibernateDao extends GenericHibernateDAO<LaExtTransactiondetail, Integer>
implements LaExtTransactiondetailDao{

	@SuppressWarnings("unchecked")
	@Override
	public LaExtTransactiondetail getLaExtTransactiondetail(Integer id) {
		try {
		     
        	String query = "select s from LaExtTransactiondetail  s where s.transactionid = :id";
        	List<LaExtTransactiondetail>lstLaExtTransactiondetail = getEntityManager().createQuery(query).setParameter("id", id).getResultList();

            if (lstLaExtTransactiondetail.size()>0) {
                return lstLaExtTransactiondetail.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
	return null;
	}

	@Override
	public LaExtTransactiondetail getLaExtTransactiondetailByLandid(long id) {
		try {
		     Long Id = new Long(id);
        	String query = "select s from LaExtTransactiondetail  s where s.transactionid = :id";
        	List<LaExtTransactiondetail>lstLaExtTransactiondetail = getEntityManager().createQuery(query).setParameter("id", Id.intValue()).getResultList();

            if (lstLaExtTransactiondetail.size()>0) {
                return lstLaExtTransactiondetail.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
	return null;
	}

	@Override
	public LaExtTransactiondetail addLaExtTransactiondetail(
			LaExtTransactiondetail laExtTransaction) {

        try {

            return makePersistent(laExtTransaction);

        } catch (Exception ex) {
            throw ex;
        }
    }

	@Override
	public LaExtTransactiondetail getLaExtTransactionByLeaseeid(Long id) {
		try {
		     
        	String query = "select s from LaExtTransactiondetail  s where s.moduletransid = :id and s.isactive=true and s.laExtApplicationstatus.workflowStatusId=1";
        	List<LaExtTransactiondetail> lstLaExtTransactiondetail = getEntityManager().createQuery(query).setParameter("id", id.intValue()).getResultList();

            if (lstLaExtTransactiondetail.size()>0) {
                return lstLaExtTransactiondetail.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
	return null;
	}

	@Override
	public LaExtTransactiondetail getLaExtTransactionByLeaseeidForSurrenderLease(Long id) {
		try {
		     
        	String query = "select s from LaExtTransactiondetail  s where s.moduletransid = :id and s.isactive=true and s.processid=5";
        	List<LaExtTransactiondetail> lstLaExtTransactiondetail = getEntityManager().createQuery(query).setParameter("id", id.intValue()).getResultList();

            if (lstLaExtTransactiondetail.size()>0) {
                return lstLaExtTransactiondetail.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
	return null;
	}
	
	@Override
	public LaExtTransactiondetail getLaExtTransactionforMortgage(Long id) {
		try {
		     
        	String query = "select s from LaExtTransactiondetail  s where s.moduletransid = :id and s.isactive=true and s.processid=3";
        	List<LaExtTransactiondetail> lstLaExtTransactiondetail = getEntityManager().createQuery(query).setParameter("id", id.intValue()).getResultList();

            if (lstLaExtTransactiondetail.size()>0) {
                return lstLaExtTransactiondetail.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
	return null;
	}

	@Override
	public LaExtTransactiondetail getLaExtTransactionforSurrenderMortgage(Long id) {
		try {
		     
        	String query = "select s from LaExtTransactiondetail  s where s.moduletransid = :id and s.isactive=true and s.processid=9";
        	List<LaExtTransactiondetail> lstLaExtTransactiondetail = getEntityManager().createQuery(query).setParameter("id", id.intValue()).getResultList();

            if (lstLaExtTransactiondetail.size()>0) {
                return lstLaExtTransactiondetail.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
	return null;
	}

	@Override
	public LaExtTransactiondetail getpoiByLeaseeid(Long id) {
		try {
		     
        	String query = "select s from LaExtTransactiondetail  s where s.moduletransid = :id and s.isactive=true and s.laExtApplicationstatus.workflowStatusId=2";
        	List<LaExtTransactiondetail> lstLaExtTransactiondetail = getEntityManager().createQuery(query).setParameter("id", id.intValue()).getResultList();

            if (lstLaExtTransactiondetail.size()>0) {
                return lstLaExtTransactiondetail.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
	return null;
	}
	
}
