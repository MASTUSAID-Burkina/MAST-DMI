package com.rmsi.mast.viewer.dao;

import com.rmsi.mast.studio.dao.GenericDAO;
import com.rmsi.mast.studio.domain.LaSurrenderLease;

public interface LaSurrenderLeaseDao extends GenericDAO<LaSurrenderLease, Integer> {

    LaSurrenderLease savesurrenderLease(LaSurrenderLease laLease);

    LaSurrenderLease getSurrenderleaseByLandandProcessId(Long landId, Long processId);

    LaSurrenderLease getObjbylandId(Long landId);

    LaSurrenderLease getObjbySurrenderLeaseeId(Integer leaseeId);

    LaSurrenderLease getSurrenderleaseByLandandTransId(Long landId, Integer transactionid);

}
