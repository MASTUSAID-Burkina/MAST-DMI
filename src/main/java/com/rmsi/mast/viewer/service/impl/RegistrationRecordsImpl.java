package com.rmsi.mast.viewer.service.impl;

import com.ibm.icu.util.Calendar;
import com.rmsi.mast.studio.dao.ProjectDAO;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
import com.rmsi.mast.studio.domain.NonNaturalPerson;
import com.rmsi.mast.studio.domain.ParcelCount;
import com.rmsi.mast.studio.domain.Permission;
import com.rmsi.mast.studio.domain.PersonType;
import com.rmsi.mast.studio.domain.Project;
import com.rmsi.mast.studio.domain.ProjectRegion;
import com.rmsi.mast.studio.domain.ShareType;
import com.rmsi.mast.studio.domain.SocialTenureRelationship;
import com.rmsi.mast.studio.domain.SourceDocument;
import com.rmsi.mast.studio.domain.SpatialUnit;
import com.rmsi.mast.studio.domain.Status;
import com.rmsi.mast.studio.domain.fetch.ClaimBasic;
import com.rmsi.mast.studio.domain.fetch.ClaimBasicLand;
import com.rmsi.mast.studio.domain.fetch.RightBasic;
import com.rmsi.mast.studio.mobile.dao.NonNaturalPersonDao;
import com.rmsi.mast.studio.mobile.dao.hibernate.SpatialUnitHibernateDao;
import com.rmsi.mast.studio.util.ConstantUtil;
import com.rmsi.mast.viewer.dao.ClaimBasicLandDao;
import com.rmsi.mast.viewer.dao.GenderLDao;
import com.rmsi.mast.viewer.dao.IDTypeDAO;
import com.rmsi.mast.viewer.dao.LaExtPersonLandMappingsDao;
import com.rmsi.mast.viewer.dao.LaExtProcessDao;
import com.rmsi.mast.viewer.dao.LaExtTransactionHistoryDao;
import com.rmsi.mast.viewer.dao.LaExtTransactiondetailDao;
import com.rmsi.mast.viewer.dao.LaLeaseDao;
import com.rmsi.mast.viewer.dao.LaMortgageDao;
import com.rmsi.mast.viewer.dao.LaMortgageSurrenderDao;
import com.rmsi.mast.viewer.dao.LaPartyDao;
import com.rmsi.mast.viewer.dao.LaPartyPersonDao;
import com.rmsi.mast.viewer.dao.LaSpatialunitLandDao;
import com.rmsi.mast.viewer.dao.LaSpatialunitgroupDAO;
import com.rmsi.mast.viewer.dao.LaSpatialunitgroupHierarchyDao;
import com.rmsi.mast.viewer.dao.LaSurrenderLeaseDao;
import com.rmsi.mast.viewer.dao.LandShareTypeDao;
import com.rmsi.mast.viewer.dao.LandTypeLadmDAO;
import com.rmsi.mast.viewer.dao.MaritalStatusaDao;
import com.rmsi.mast.viewer.dao.NaturalPersonDAO;
import com.rmsi.mast.viewer.dao.ParcelCountDao;
import com.rmsi.mast.viewer.dao.PermissionDao;
import com.rmsi.mast.viewer.dao.PersonTypeLDao;
import com.rmsi.mast.viewer.dao.ProjectRegionDao;
import com.rmsi.mast.viewer.dao.RegistrationRecordsDao;
import com.rmsi.mast.viewer.dao.SocialTenureRelationshipDao;
import com.rmsi.mast.viewer.dao.SourceDocumentsDao;
import com.rmsi.mast.viewer.dao.SpatialUnitDAO;
import com.rmsi.mast.viewer.dao.StatusDAO;
import com.rmsi.mast.viewer.service.RegistrationRecordsService;
import java.util.Date;

@Service
public class RegistrationRecordsImpl implements RegistrationRecordsService {

    private static final Logger logger = Logger.getLogger(RegistrationRecordsImpl.class);

    @Autowired
    RegistrationRecordsDao registrationRecordsDao;

    @Autowired
    LaExtPersonLandMappingsDao laExtPersonLandMappingsDao;

    @Autowired
    LaPartyPersonDao laPartyPersonDao;

    @Autowired
    MaritalStatusaDao maritalStatusDao;

    @Autowired
    GenderLDao genderLDao;

    @Autowired
    IDTypeDAO idTypeDAO;

    @Autowired
    SpatialUnitDAO spatialUnitDAO;

    @Autowired
    LaSpatialunitLandDao laSpatialunitLandDao;

    @Autowired
    LandTypeLadmDAO landTypeLadmDAO;

    @Autowired
    LaSpatialunitgroupHierarchyDao laSpatialunitgroupHierarchyDao;

    @Autowired
    LandShareTypeDao landShareTypeDao;

    @Autowired
    LaExtProcessDao laExtProcessDao;

    @Autowired
    StatusDAO statusDAO;

    @Autowired
    PersonTypeLDao personTypeLDao;

    @Autowired
    LaExtTransactiondetailDao transactionDao;

    @Autowired
    LaPartyDao laPartyDao;

    @Autowired
    NaturalPersonDAO naturalPersonDAO;

    @Autowired
    LaSpatialunitgroupDAO laSpatialunitgroupDAO;

    @Autowired
    ProjectRegionDao projectRegionDao;

    @Autowired
    SocialTenureRelationshipDao socialTenureRelationshipDao;

    @Autowired
    ClaimBasicLandDao claimBasicLandDao;

    @Autowired
    SourceDocumentsDao sourceDocumentsDao;

    @Autowired
    LaMortgageDao laMortgageDao;

    @Autowired
    LaLeaseDao laLeaseDao;

    @Autowired
    LaSurrenderLeaseDao lasurrenderleaseDao;

    @Autowired
    LaExtTransactionHistoryDao laexttransactionhistoryDao;

    @Autowired
    LaMortgageSurrenderDao laMortgageSurrenderDao;

    @Autowired
    private ParcelCountDao parcelCountDao;

    @Autowired
    ProjectDAO projectDAO;

    @Autowired
    SpatialUnitHibernateDao spatialUnitDao;

    @Autowired
    PermissionDao permissionDao;
    
    @Autowired
    NonNaturalPersonDao nonPersonDao;

    @Override
    public List<LaSpatialunitLand> findAllSpatialUnitTemp(String defaultProject,
            int startfrom) {
        return registrationRecordsDao.findOrderedSpatialUnitRegistry(defaultProject, startfrom);
    }

    @Override
    public LaExtPersonLandMapping getPersonLandMapDetails(Integer landid) {
        return laExtPersonLandMappingsDao.getPersonLandMapDetails(landid);
//		return null;
    }

    @Override
    public LaPartyPerson getPartyPersonDetails(Integer landid) {
        return laPartyPersonDao.getPartyPersonDetails(landid);
    }

    @Override
    public Object getAllPartyPersonDetails(Integer landid) {
        return laPartyPersonDao.getAllPartyPersonDetails(landid);
    }

    @Override
    public List<LaPartyPerson> fillAllPartyPersonDetails(Integer landid, Integer processid) {
        return laPartyPersonDao.fillAllPartyPersonDetails(landid, processid);
    }

    @Override
    public LaPartyPerson getPartyPersonDetailssurrenderlease(Integer landid) {
        return laPartyPersonDao.getPartyPersonDetailssurrenderlease(landid);
    }

    @Override
    public List<MaritalStatus> getMaritalStatusDetails() {
        return maritalStatusDao.getMaritalStatus();
    }

    @Override
    public List<Gender> getGenderDetails() {
        return genderLDao.getGenderDetails();
    }

    @Override
    public List<IdType> getIDTypeDetails() {
        return idTypeDAO.getIDTypeDetails();
    }

    @Override
    public List<SpatialUnit> getSpatialUnitLandMappingDetails(Long landid) {
        return spatialUnitDAO.getSpatialUnitLandMappingDetails(landid);
    }

    @Override
    public LaSpatialunitLand getLaSpatialunitLandDetails(Long landid) {
        return laSpatialunitLandDao.getLaSpatialunitLandDetails(landid);
    }

    @Override
    public List<LandType> getAllLandType() {
        return landTypeLadmDAO.getAllLandType();
    }

    @Override
    public List<ProjectRegion> getAllCountry() {
        return laSpatialunitgroupHierarchyDao.getAllCountry();
    }

    @Override
    public List<ProjectRegion> getAllRegion(Integer country_r_id) {
        return laSpatialunitgroupHierarchyDao.getAllRegion(country_r_id);
    }

    @Override
    public List<ShareType> getAlllandsharetype() {
        return landShareTypeDao.getAlllandsharetype();
    }

    @Override
    public List<ProjectRegion> getAllProvience(Integer region_r_id) {
        return laSpatialunitgroupHierarchyDao.getAllProvience(region_r_id);
    }

    @Override
    public List<LaExtProcess> getAllProcessDetails() {
        return laExtProcessDao.getAllProcessDetails();
    }

    @Override
    public Status getStatusById(int statusId) {
        return statusDAO.getStatusById(statusId);
    }

    @Override
    public PersonType getPersonTypeById(int personTypeGid) {
        return personTypeLDao.getPersonTypeById(personTypeGid);
    }

    @Override
    public MaritalStatus getMaritalStatusByID(Integer id) {
        return maritalStatusDao.getMaritalStatusByID(id);
    }

    @Override
    public IdType getIDTypeDetailsByID(Integer id) {
        return idTypeDAO.getIDTypeDetailsByID(id);
    }

    @Override
    public LaExtTransactiondetail saveTransaction(LaExtTransactiondetail laExtTransactiondetail) {
        try {
            return transactionDao.makePersistent(laExtTransactiondetail);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        }
    }

    @Override
    public boolean registerLease(LaLease lease) {
        laLeaseDao.saveLease(lease);

        LaExtTransactiondetail laExtTransactiondetail = transactionDao.getLaExtTransactionByLeaseeid(lease.getLeaseid().longValue());
        Status status = getStatusById(7); // Final
        laExtTransactiondetail.setLaExtApplicationstatus(status);
        transactionDao.makePersistent(laExtTransactiondetail);
        return true;
    }

    @Override
    public LaParty saveParty(LaParty laParty) {
        return laPartyDao.saveParty(laParty);
    }

    @Override
    @Transactional
    public NaturalPerson saveNaturalPerson(NaturalPerson naturalPerson) {
        try {
            return naturalPersonDAO.saveNaturalPerson(naturalPerson);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            throw e;
        }
    }
    
    @Override
    @Transactional
    public NonNaturalPerson saveNonNaturalPerson(NonNaturalPerson nonPerson){
        try {
            return nonPersonDao.addNonNaturalPerson(nonPerson);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            throw e;
        }
    }

    @Override
    public LaSpatialunitgroup findLaSpatialunitgroupById(Integer id) {
        return laSpatialunitgroupDAO.findLaSpatialunitgroupById(id);
    }

    @Override
    public ProjectRegion findProjectRegionById(Integer id) {
        return projectRegionDao.findProjectRegionById(id);
    }

    @Override
    @Transactional
    public SocialTenureRelationship saveSocialTenureRelationship(
            SocialTenureRelationship socialTenureRelationship) {

        return socialTenureRelationshipDao.saveSocialTenureRelationship(socialTenureRelationship);
    }

    @Override
    public ClaimBasicLand getClaimBasicLandById(Long id) {
        return claimBasicLandDao.getClaimBasicLandById(id);
    }

    @Override
    public List<LaSpatialunitLand> getLaSpatialunitLandDetailsQ(Integer landid) {
        return laSpatialunitLandDao.getLaSpatialunitLandDetailsQ(landid);
    }

    @Override
    public boolean updateLaSpatialunitLand(LaSpatialunitLand laSpatialunitLand) {
        return laSpatialunitLandDao.updateLaSpatialunitLand(laSpatialunitLand);
    }

    @Override
    public boolean addLaSpatialunitLand(LaSpatialunitLand laSpatialunitLand) {
        return laSpatialunitLandDao.addLaSpatialunitLand(laSpatialunitLand);
    }

    @Override
    public List<LaSpatialunitLand> search(Long transactionid, Integer startfrom, String project, String villageId, String parcelId) {
        return registrationRecordsDao.search(transactionid, startfrom, project, villageId, parcelId);
    }

    @Override
    public SocialTenureRelationship getSocialTenureRelationshipByLandId(
            Long landId) {
        return socialTenureRelationshipDao.getSocialTenureRelationshipByLandId(landId);
    }

    @Override
    public SocialTenureRelationship getSocialTenureRelationshipByLandIdForBuyer(Long landId, Long processid) {
        return socialTenureRelationshipDao.getSocialTenureRelationshipByLandIdForBuyer(landId, processid);
    }

    @Override
    public SocialTenureRelationship getSocialTenureRelationshipForSellerByLandId(Long landId) {
        return socialTenureRelationshipDao.getSocialTenureRelationshipForSellerByLandId(landId);
    }

    @Override
    public boolean updateSocialTenureRelationshipByPartyId(Long partyId, Long landid) {
        return socialTenureRelationshipDao.updateSocialTenureRelationshipByPartyId(partyId, landid);
    }

    @Override
    public boolean updateSocialTenureRelationshipByPartytypeId(Long partyId, Long landid, int mutationTypeId,
            String contractName, String contractNumber, Date contractDate, int ownershipTypeId) {
        ClaimBasic su = spatialUnitDao.getClaimsBasicByLandId(landid).get(0);
        Project project = projectDAO.findById(su.getProjectnameid(), false);

        ParcelCount parcelCount = parcelCountDao.findParcelCountByTypeAndProjectName(ConstantUtil.APFR, project.getName());
        if (parcelCount == null) {
            parcelCount = new ParcelCount();
            parcelCount.setCount(0);
            parcelCount.setPname(project.getName());
            parcelCount.setType(ConstantUtil.APFR);
        }

        long count = parcelCount.getCount() + 1;
        parcelCount.setCount(count);
        String countval = "";

        if (count < 10) {
            countval = "0000" + String.valueOf(count);
        } else if (count >= 10 && count <= 99) {
            countval = "000" + String.valueOf(count);
        } else if (count >= 100 && count <= 999) {
            countval = "00" + String.valueOf(count);
        } else if (count >= 1000 && count <= 9999) {
            countval = "0" + String.valueOf(count);
        } else if (count >= 10000 && count <= 99999) {
            countval = String.valueOf(count);
        }

        String villageCode = su.getLaSpatialunitgroupHierarchy5().getAreaCode() + "-";
        String apfrNum = villageCode + countval;

        parcelCountDao.makePersistent(parcelCount);
        return socialTenureRelationshipDao.updateSocialTenureRelationshipByPartytypeId(partyId, landid, mutationTypeId,
                contractName, contractNumber, contractDate, apfrNum, ownershipTypeId);
    }

    @Override
    public LaParty getLaPartyById(Long partyId) {
        return laPartyDao.getPartyIdByID(partyId);
    }

    @Override
    public LaExtTransactiondetail getLaExtTransactiondetail(Integer id) {
        return transactionDao.getLaExtTransactiondetail(id);
    }

    @Override
    //@Transactional
    public SourceDocument saveUploadedDocuments(SourceDocument sourceDocument) {
        return sourceDocumentsDao.saveUploadedDocuments(sourceDocument);
    }

    @Override
    public List<LaExtFinancialagency> getFinancialagencyDetails() {
        return registrationRecordsDao.getFinancialagencyDetails();
    }

    @Override
    public LaExtFinancialagency getFinancialagencyByID(int financial_AgenciesID) {
        return registrationRecordsDao.getFinancialagencyByID(financial_AgenciesID);
    }

    @Override
    public List<La_Month> getmonthofleaseDetails() {
        return registrationRecordsDao.getmonthofleaseDetails();
    }

    @Override
    public La_Month getLaMonthById(int no_Of_month_Lease) {
        return registrationRecordsDao.getLaMonthById(no_Of_month_Lease);
    }

    @Override
    //@Transactional
    public LaLease saveLease(LaLease laLease) {
        return laLeaseDao.saveLease(laLease);
    }

    @Override
    //@Transactional
    public LaSurrenderLease savesurrenderLease(LaSurrenderLease laLease) {
        return lasurrenderleaseDao.savesurrenderLease(laLease);
    }

    @Override
    //@Transactional
    public LaExtTransactionHistory saveTransactionHistory(LaExtTransactionHistory latranshist) {
        return laexttransactionhistoryDao.saveTransactionHistory(latranshist);
    }

    @Override
    public Integer findSpatialUnitTempCount(String project, Integer startfrom) {
        return registrationRecordsDao.findSpatialUnitTempCount(project, startfrom);
    }

    @Override
    public Integer searchCount(Long transactionid, Integer startfrom, String project, String villageId, String parcelId) {
        return registrationRecordsDao.searchCount(transactionid, startfrom, project, villageId, parcelId);
    }

    @Override
    public boolean checkForActiveLease(int landid, int processId) {
        return laLeaseDao.checkForActiveLease(landid, processId);

    }

    @Override
    public SocialTenureRelationship getSocialTenureRelationshipByLandIdandTypeId(Long landId, Long processid, Integer persontype) {

        return socialTenureRelationshipDao.getSocialTenureRelationshipByLandIdandTypeId(landId, processid, persontype);
    }

    @Override
    //@Transactional
    public LaMortgage saveMortgage(LaMortgage laMortgage) {
        return laMortgageDao.saveMortgage(laMortgage);
    }

    @Override
    public LaSurrenderMortgage saveSurrenderMortgage(
            LaSurrenderMortgage laMortgage) {
        return laMortgageSurrenderDao.saveMortgage(laMortgage);
    }

    @Override
    @Transactional
    public boolean disablelease(int leaseId) {
        return laLeaseDao.disablelease(leaseId);
    }

    @Override
    @Transactional
    public boolean disableMortagage(Long personid, Long landid) {
        // TODO Auto-generated method stub
        return laMortgageDao.disablelease(personid, landid);
    }

    @Override
    public List<SocialTenureRelationship> getSocialTenureRelationshipListForSellerByLandId(
            Long landId) {

        return socialTenureRelationshipDao.getSocialTenureRelationshipListForSellerByLandId(landId);

    }

    @Override
    public List<SocialTenureRelationship> getSocialTenureRelationshipListByLandIdForBuyer(
            Long landId, Long processid) {

        return socialTenureRelationshipDao.getSocialTenureRelationshipListByLandIdForBuyer(landId, processid);

    }

    @Override
    public List<LaPartyPerson> getAllPartyPersonDetailsByTransactionId(
            Integer transid) {
        return laPartyPersonDao.getAllPartyPersonDetailsByTransactionId(transid);
    }

    @Override
    public LaLease getLeaseByTransactionId(Integer transid) {
        return laLeaseDao.getLeaseByTransactionId((long) transid);
    }

    @Override
    public SocialTenureRelationship getSocialTenureRelationshipByTransactionId(Long transactionId) {
        return socialTenureRelationshipDao.getSocialTenureRelationshipByTransactionId(transactionId);
    }

    @Override
    public LaLease getLeaseByLandId(Integer landid) {
        return laLeaseDao.getLeaseByLandId((long) landid);
    }

    @Override
    public SocialTenureRelationship getAllSocialTenureRelationshipByTransactionId(
            Long transactionId) {
        return socialTenureRelationshipDao.getAllSocialTenureRelationshipByTransactionId(transactionId);
    }

    @Override
    public Permission getPermissionById(int id) {
        return permissionDao.findById(id);
    }

    @Override
    public Permission getRegisteredPermissionByPropId(long usin) {
        return permissionDao.findRegisteredPermissionByPropId(usin);
    }
    
    @Override
    public Permission getPermissionByTransactionId(int transactionId){
        return permissionDao.findPermissionByTransactionId(transactionId);
    }

    @Override
    public Permission getPendingTerminationPermissionByPropId(long usin) {
        return permissionDao.findPendingTerminationPermissionByPropId(usin);
    }

    @Override
    public Permission getPendingPermissionByPropId(long usin) {
        return permissionDao.findPendingPermissionByPropId(usin);
    }

    @Override
    public Permission savePermission(Permission permission) {
        Permission p;
        if (permission.getTransactionid() == null) {
            // Create new transaction
            long processId = 11;
            if (permission.getTerminatedid() != null && permission.getTerminatedid() > 0) {
                processId = 12;
            }
            LaExtTransactiondetail tran = new LaExtTransactiondetail();
            tran.setCreatedby(permission.getCreatedby());
            tran.setCreateddate(permission.getCreateddate());
            tran.setIsactive(true);
            tran.setLaExtApplicationstatus(getStatusById(1));
            tran.setProcessid(processId);
            tran = transactionDao.makePersistent(tran);
            
            permission.setTransactionid(tran.getTransactionid());
            p = permissionDao.save(permission);
            
            tran.setModuletransid(p.getId());
            transactionDao.makePersistent(tran);
        } else {
            p = permissionDao.save(permission);
        }
        return p;
    }

    @Override
    public boolean registerPermission(Permission permission) {
        LaExtTransactiondetail tran = transactionDao.getLaExtTransactiondetail(permission.getTransactionid());
        tran.setLaExtApplicationstatus(getStatusById(7));
        transactionDao.makePersistent(tran);
        
        // If termination, make original one disaplbed
        if (permission.getTerminatedid() != null && permission.getTerminatedid() > 0) {
            Permission p = permissionDao.findById(permission.getTerminatedid());
            p.setActive(Boolean.FALSE);
            p.setModifiedby(permission.getModifiedby());
            p.setModifieddate(Calendar.getInstance().getTime());
            permissionDao.save(p);
        } else {
            permissionDao.save(permission);
        }
        return true;
    }

    @Override
    public boolean checkForRegisteredPermission(long usin) {
        return permissionDao.checkForRegisteredPermission(usin);
    }
}
