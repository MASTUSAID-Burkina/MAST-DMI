package com.rmsi.mast.studio.dao;

import java.util.Date;

import com.rmsi.mast.studio.domain.PaymentInfo;

public interface PaymentInfoDAO extends GenericDAO<PaymentInfo, Long> {

    Date findLetterDate(Long usin);

    boolean updateDate(Long usin, Date letterdate);

}
