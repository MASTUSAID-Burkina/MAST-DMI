package com.rmsi.mast.viewer.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.rmsi.mast.studio.domain.Gender;
import com.rmsi.mast.studio.domain.IdType;
import com.rmsi.mast.studio.domain.LaExtFinancialagency;
import com.rmsi.mast.studio.domain.LaExtPersonLandMapping;
import com.rmsi.mast.studio.domain.LaExtProcess;
import com.rmsi.mast.studio.domain.LaExtTransactionHistory;
import com.rmsi.mast.studio.domain.LaExtTransactiondetail;
import com.rmsi.mast.studio.domain.LaLease;
import com.rmsi.mast.studio.domain.LaMortgage;
import com.rmsi.mast.studio.domain.LaParty;
import com.rmsi.mast.studio.domain.LaPartyPerson;
import com.rmsi.mast.studio.domain.LaSpatialunitLand;
import com.rmsi.mast.studio.domain.LaSpatialunitgroup;
import com.rmsi.mast.studio.domain.LaSurrenderLease;
import com.rmsi.mast.studio.domain.LaSurrenderMortgage;
import com.rmsi.mast.studio.domain.La_Month;
import com.rmsi.mast.studio.domain.LandType;
import com.rmsi.mast.studio.domain.MaritalStatus;
import com.rmsi.mast.studio.domain.NaturalPerson;
import com.rmsi.mast.studio.domain.PersonType;
import com.rmsi.mast.studio.domain.ProjectRegion;
import com.rmsi.mast.studio.domain.ShareType;
import com.rmsi.mast.studio.domain.SocialTenureRelationship;
import com.rmsi.mast.studio.domain.SourceDocument;
import com.rmsi.mast.studio.domain.SpatialUnit;
import com.rmsi.mast.studio.domain.Status;
import com.rmsi.mast.studio.domain.fetch.ClaimBasicLand;
import java.util.Date;

public interface RegistrationRecordsService {

    List<LaSpatialunitLand> findAllSpatialUnitTemp(String defaultProject, int startfrom);

    public LaExtPersonLandMapping getPersonLandMapDetails(Integer landid);

    LaPartyPerson getPartyPersonDetails(Integer landid);

    List<LaPartyPerson> getAllPartyPersonDetails(Integer landid);

    List<LaPartyPerson> getAllPartyPersonDetailsByTransactionId(Integer transid);

    List<LaPartyPerson> fillAllPartyPersonDetails(Integer landid, Integer processid);

    LaPartyPerson getPartyPersonDetailssurrenderlease(Integer landid);

    LaLease getLeaseByLandId(Integer landid);

    LaLease getLeaseByTransactionId(Integer transid);

    List<MaritalStatus> getMaritalStatusDetails();

    List<Gender> getGenderDetails();

    List<IdType> getIDTypeDetails();

    List<SpatialUnit> getSpatialUnitLandMappingDetails(Long landid);

    LaSpatialunitLand getLaSpatialunitLandDetails(Long landid);

    List<LaSpatialunitLand> getLaSpatialunitLandDetailsQ(Integer landid);

    boolean updateLaSpatialunitLand(LaSpatialunitLand laSpatialunitLand);

    boolean addLaSpatialunitLand(LaSpatialunitLand laSpatialunitLand);

    List<LandType> getAllLandType();

    List<ProjectRegion> getAllCountry();

    List<ProjectRegion> getAllRegion(Integer country_r_id);

    List<ShareType> getAlllandsharetype();

    List<ProjectRegion> getAllProvience(Integer region_r_id);

    List<LaExtProcess> getAllProcessDetails();

    Status getStatusById(int statusId);

    PersonType getPersonTypeById(int personTypeGid);

    MaritalStatus getMaritalStatusByID(Integer id);

    IdType getIDTypeDetailsByID(Integer id);

    @Transactional
    LaExtTransactiondetail saveTransaction(LaExtTransactiondetail laExtTransactiondetail);

    @Transactional
    boolean registerLease(LaLease lease);
    
    @Transactional
    NaturalPerson saveNaturalPerson(NaturalPerson naturalPerson);

    @Transactional
    LaParty saveParty(LaParty laParty);

    @Transactional
    SocialTenureRelationship saveSocialTenureRelationship(SocialTenureRelationship socialTenureRelationship);

    @Transactional
    boolean updateSocialTenureRelationshipByPartyId(Long partyId, Long landid);

    @Transactional
    boolean updateSocialTenureRelationshipByPartytypeId(Long partyId, Long landid, int mutationTypeId, 
            String contractName, String contractNumber, Date contractDate, int ownershipTypeId);

    public LaSpatialunitgroup findLaSpatialunitgroupById(Integer id);

    ProjectRegion findProjectRegionById(Integer id);

    ClaimBasicLand getClaimBasicLandById(Long id);

    List<LaSpatialunitLand> search(Long transactionid, Integer startfrom, String project, String villageId, String parcelId);

    SocialTenureRelationship getSocialTenureRelationshipByLandId(Long landId);

    SocialTenureRelationship getSocialTenureRelationshipByTransactionId(Long transactionId);

    SocialTenureRelationship getAllSocialTenureRelationshipByTransactionId(Long transactionId);

    LaParty getLaPartyById(Long partyId);

    LaExtTransactiondetail getLaExtTransactiondetail(Integer id);

    @Transactional
    SourceDocument saveUploadedDocuments(SourceDocument sourceDocument);

    List<LaExtFinancialagency> getFinancialagencyDetails();

    LaExtFinancialagency getFinancialagencyByID(int financial_AgenciesID);

    @Transactional
    LaMortgage saveMortgage(LaMortgage laMortgage);

    @Transactional
    LaSurrenderMortgage saveSurrenderMortgage(LaSurrenderMortgage laMortgage);

    List<La_Month> getmonthofleaseDetails();

    La_Month getLaMonthById(int no_Of_month_Lease);

    @Transactional
    LaLease saveLease(LaLease laLease);

    @Transactional
    LaSurrenderLease savesurrenderLease(LaSurrenderLease laLease);

    Integer findSpatialUnitTempCount(String project, Integer startfrom);

    Integer searchCount(Long transactionid, Integer startfrom, String project, String villageId, String parcelId);

    boolean disablelease(int leaseId);

    boolean disableMortagage(Long personid, Long landid);

    boolean checkForActiveLease(int landid, int processId);

    SocialTenureRelationship getSocialTenureRelationshipByLandIdForBuyer(Long landId, Long processid);

    List<SocialTenureRelationship> getSocialTenureRelationshipListByLandIdForBuyer(Long landId, Long processid);

    SocialTenureRelationship getSocialTenureRelationshipForSellerByLandId(Long landId);

    List<SocialTenureRelationship> getSocialTenureRelationshipListForSellerByLandId(Long landId);

    @Transactional
    LaExtTransactionHistory saveTransactionHistory(LaExtTransactionHistory latranshist);

    SocialTenureRelationship getSocialTenureRelationshipByLandIdandTypeId(Long landId, Long processid, Integer persontype);
}
