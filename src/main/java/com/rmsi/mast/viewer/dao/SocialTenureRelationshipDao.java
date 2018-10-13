package com.rmsi.mast.viewer.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.rmsi.mast.studio.dao.GenericDAO;
import com.rmsi.mast.studio.domain.SocialTenureRelationship;
import java.util.Date;

public interface SocialTenureRelationshipDao extends GenericDAO<SocialTenureRelationship, Long>{

	@Transactional
	SocialTenureRelationship saveSocialTenureRelationship(SocialTenureRelationship socialTenureRelationship);
	
	SocialTenureRelationship getSocialTenureRelationshipByLandId(Long landId);
	
	SocialTenureRelationship getSocialTenureRelationshipByTransactionId(Long transid);
	
	SocialTenureRelationship getAllSocialTenureRelationshipByTransactionId(Long transid);

	SocialTenureRelationship getSocialTenureRelationshipByLandIdForBuyer(Long landId,Long processid);
	List<SocialTenureRelationship> getSocialTenureRelationshipListByLandIdForBuyer(Long landId,Long processid);
	SocialTenureRelationship getSocialTenureRelationshipByLandIdPartyIdandPersonTypeID(Long landId, Long partyid,Integer persontypeid);
	
	boolean updateSocialTenureRelationshipByPartyId(Long partyId, Long landid);
	boolean updateSocialTenureRelationshipByPartytypeId(Long partyId,Long landid, int mutationTypeId, String contractName, 
                String contractNumber, Date contractDate, String apfrNum, int ownershipTypeId);
	List<SocialTenureRelationship> getSocialTenureRelationshipBylandID(Long landId);
	
	List<SocialTenureRelationship>getSocialTenureRelationshipBylandIDAndPartyID(Long landId);

	SocialTenureRelationship getSocialTenureRelationshipForSellerByLandId(Long landId);
	
	List<SocialTenureRelationship> getSocialTenureRelationshipListForSellerByLandId(Long landId);
	
	SocialTenureRelationship getSocialTenureRelationshipByLandIdandTypeId(Long landId,Long processid,Integer persontype);
	
}
