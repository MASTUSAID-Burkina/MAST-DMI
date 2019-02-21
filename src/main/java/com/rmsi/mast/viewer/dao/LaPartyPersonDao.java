package com.rmsi.mast.viewer.dao;

import java.util.List;

import com.rmsi.mast.studio.dao.GenericDAO;
import com.rmsi.mast.studio.domain.LaPartyPerson;

public interface LaPartyPersonDao extends GenericDAO<LaPartyPerson, Long> {

    LaPartyPerson getPartyPersonDetails(Integer landid);

    Object getAllPartyPersonDetails(Integer landid);

    List<LaPartyPerson> getAllPartyPersonDetailsByTransactionId(Integer transid);

    List<LaPartyPerson> fillAllPartyPersonDetails(Integer landid, Integer processid);

    LaPartyPerson getPartyPersonDetailssurrenderlease(Integer landid);
}
