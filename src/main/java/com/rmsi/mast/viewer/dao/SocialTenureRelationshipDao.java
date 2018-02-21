package com.rmsi.mast.viewer.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.rmsi.mast.studio.dao.GenericDAO;
import com.rmsi.mast.studio.domain.SocialTenureRelationship;

public interface SocialTenureRelationshipDao extends GenericDAO<SocialTenureRelationship, Long>{

	@Transactional
	SocialTenureRelationship saveSocialTenureRelationship(SocialTenureRelationship socialTenureRelationship);
	
	SocialTenureRelationship getSocialTenureRelationshipByLandId(Long landId);
	SocialTenureRelationship getSocialTenureRelationshipByLandIdPartyIdandPersonTypeID(Long landId, Long partyid,Integer persontypeid);
	
	boolean updateSocialTenureRelationshipByPartyId(Long partyId, Long landid);
	
	List<SocialTenureRelationship> getSocialTenureRelationshipBylandID(Long landId);
	
	List<SocialTenureRelationship>getSocialTenureRelationshipBylandIDAndPartyID(Long landId);
}