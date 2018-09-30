package com.rmsi.mast.studio.dao.hibernate;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.rmsi.mast.studio.dao.PaymentInfoDAO;
import com.rmsi.mast.studio.domain.PaymentInfo;

@Repository
public class PaymentInfoHibernateDAO extends GenericHibernateDAO<PaymentInfo, Long>
        implements PaymentInfoDAO {

    private static final Logger logger = Logger.getLogger(PaymentInfoHibernateDAO.class);

    @Override
    public Date findLetterDate(Long usin) {

        try {
            Query query = getEntityManager().createQuery("Select pi from PaymentInfo pi where pi.usin = :usin");
            @SuppressWarnings("unchecked")
            List<PaymentInfo> paymentList = query.setParameter("usin", usin).getResultList();

            if (paymentList.size() > 0) {
                return paymentList.get(0).getLetter_generation_date();
            } else {
                return null;
            }
        } catch (Exception e) {

            logger.error(e);
            return null;
        }

    }

    @Override
    public boolean updateDate(Long usin, Date letterdate) {

        try {

            Query query = getEntityManager().createQuery("UPDATE PaymentInfo pi SET pi.letter_generation_date = :letter_generation_date  where pi.usin = :usin");
            int updaterec = query.setParameter("letter_generation_date", letterdate).setParameter("usin", usin).executeUpdate();

            if (updaterec > 0) {
                return true;
            }

        } catch (Exception e) {

            logger.error(e);
            return false;
        }

        return false;

    }

}
