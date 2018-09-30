package com.rmsi.mast.studio.mobile.service.impl;

import com.rmsi.mast.studio.dao.AcquisitionTypeDao;
import com.rmsi.mast.studio.dao.AttributeMasterDAO;
import com.rmsi.mast.studio.dao.ClaimTypeDao;
import com.rmsi.mast.studio.dao.DisputeDao;
import com.rmsi.mast.studio.dao.DisputeStatusDao;
import com.rmsi.mast.studio.dao.DisputeTypeDao;
import com.rmsi.mast.studio.dao.DocumentTypeDao;
import com.rmsi.mast.studio.dao.IdTypeDao;
import com.rmsi.mast.studio.dao.LaExtDisputeDAO;
import com.rmsi.mast.studio.dao.LaExtDisputelandmappingDAO;
import com.rmsi.mast.studio.dao.ProjectDAO;
import com.rmsi.mast.studio.dao.ResourceLandClassificationMappingDAO;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import com.rmsi.mast.studio.dao.RelationshipTypeDao;
import com.rmsi.mast.studio.dao.UserDAO;
import com.rmsi.mast.studio.dao.WorkflowDAO;
import com.rmsi.mast.studio.dao.WorkflowStatusHistoryDAO;
import com.rmsi.mast.studio.domain.AcquisitionType;
import com.rmsi.mast.studio.domain.AttributeMaster;
import com.rmsi.mast.studio.domain.AttributeMasterResourcePOI;
import com.rmsi.mast.studio.domain.AttributeOptions;
import com.rmsi.mast.studio.domain.AttributeValues;
import com.rmsi.mast.studio.domain.CustomAttributes;
import com.rmsi.mast.studio.domain.DisputeType;
import com.rmsi.mast.studio.domain.ExistingClaim;
import com.rmsi.mast.studio.domain.GroupType;
import com.rmsi.mast.studio.domain.IdType;
import com.rmsi.mast.studio.domain.LaExtDispute;
import com.rmsi.mast.studio.domain.LaExtDisputelandmapping;
import com.rmsi.mast.studio.domain.LaExtTransactiondetail;
import com.rmsi.mast.studio.domain.NaturalPerson;
import com.rmsi.mast.studio.domain.NonNaturalPerson;
import com.rmsi.mast.studio.domain.PersonType;
import com.rmsi.mast.studio.domain.Project;
import com.rmsi.mast.studio.domain.ProjectArea;
import com.rmsi.mast.studio.domain.ResourceCustomAttributes;
import com.rmsi.mast.studio.domain.ResourceAttributeValues;
import com.rmsi.mast.studio.domain.ResourceClassification;
import com.rmsi.mast.studio.domain.ResourceLandClassificationMapping;
import com.rmsi.mast.studio.domain.ResourcePOIAttributeValues;
import com.rmsi.mast.studio.domain.ResourceSourceDocument;
import com.rmsi.mast.studio.domain.ResourceSubClassification;
import com.rmsi.mast.studio.domain.ShareType;
import com.rmsi.mast.studio.domain.SocialTenureRelationship;
import com.rmsi.mast.studio.domain.SourceDocument;
import com.rmsi.mast.studio.domain.SpatialUnit;
import com.rmsi.mast.studio.domain.SpatialUnitPersonWithInterest;
import com.rmsi.mast.studio.domain.SpatialUnitResourceLine;
import com.rmsi.mast.studio.domain.SpatialUnitResourcePoint;
import com.rmsi.mast.studio.domain.SpatialUnitResourcePolygon;
import com.rmsi.mast.studio.domain.Status;
import com.rmsi.mast.studio.domain.Surveyprojectattribute;
import com.rmsi.mast.studio.domain.Unit;
import com.rmsi.mast.studio.domain.User;
import com.rmsi.mast.studio.domain.WorkflowStatusHistory;
import com.rmsi.mast.studio.domain.fetch.SpatialUnitTable;
import com.rmsi.mast.studio.domain.fetch.SpatialunitDeceasedPerson;
import com.rmsi.mast.studio.mobile.dao.AttributeMasterResourcePoiDAO;
import com.rmsi.mast.studio.mobile.dao.AttributeOptionsDao;
import com.rmsi.mast.studio.mobile.dao.AttributeValuesDao;
import com.rmsi.mast.studio.mobile.dao.CitizenshipDao;
import com.rmsi.mast.studio.mobile.dao.EducationLevelDao;
import com.rmsi.mast.studio.mobile.dao.ExistingClaimDao;
import com.rmsi.mast.studio.mobile.dao.GenderDao;
import com.rmsi.mast.studio.mobile.dao.GroupTypeDao;
import com.rmsi.mast.studio.mobile.dao.LaPartygroupOccupationDAO;
import com.rmsi.mast.studio.mobile.dao.LandTypeDao;
import com.rmsi.mast.studio.mobile.dao.LandUseTypeDao;
import com.rmsi.mast.studio.mobile.dao.MaritalStatusDao;
import com.rmsi.mast.studio.mobile.dao.NaturalPersonDao;
import com.rmsi.mast.studio.mobile.dao.NonNaturalPersonDao;
import com.rmsi.mast.studio.mobile.dao.OccupancyTypeDao;
import com.rmsi.mast.studio.mobile.dao.PersonDao;
import com.rmsi.mast.studio.mobile.dao.PersonTypeDao;
import com.rmsi.mast.studio.mobile.dao.ResourceAttributeValuesDAO;
import com.rmsi.mast.studio.mobile.dao.ResourcePOIAttributeValuesDAO;
import com.rmsi.mast.studio.mobile.dao.ResourceSourceDocumentDao;
import com.rmsi.mast.studio.mobile.dao.ShareTypeDao;
import com.rmsi.mast.studio.mobile.dao.SlopeValuesDao;
import com.rmsi.mast.studio.mobile.dao.SocialTenureDao;
import com.rmsi.mast.studio.mobile.dao.SoilQualityValuesDao;
import com.rmsi.mast.studio.mobile.dao.SourceDocumentDao;
import com.rmsi.mast.studio.mobile.dao.SpatialUnitDao;
import com.rmsi.mast.studio.mobile.dao.SpatialUnitPersonWithInterestDao;
import com.rmsi.mast.studio.mobile.dao.SpatialUnitResourceLineDao;
import com.rmsi.mast.studio.mobile.dao.SpatialUnitResourcePointDao;
import com.rmsi.mast.studio.mobile.dao.SpatialUnitResourcePolygonDao;
import com.rmsi.mast.studio.mobile.dao.StatusDao;
import com.rmsi.mast.studio.mobile.dao.TenureClassDao;
import com.rmsi.mast.studio.mobile.dao.UserDataDao;
import com.rmsi.mast.studio.mobile.dao.WorkflowStatusHistoryDao;
import com.rmsi.mast.studio.mobile.dao.hibernate.CustomAttributesHibernateDAO;
import com.rmsi.mast.studio.mobile.service.ProjectService;
import com.rmsi.mast.studio.mobile.service.ResourceCustomAttributesService;
import com.rmsi.mast.studio.mobile.service.ResourceClassificationServise;
import com.rmsi.mast.studio.mobile.service.ResourceSubClassificationService;
import com.rmsi.mast.studio.mobile.service.SurveyProjectAttributeService;
import com.rmsi.mast.studio.mobile.service.UserDataService;
import com.rmsi.mast.studio.mobile.transferobjects.Attribute;
import com.rmsi.mast.studio.mobile.transferobjects.Person;
import com.rmsi.mast.studio.mobile.transferobjects.PersonOfInterest;
import com.rmsi.mast.studio.mobile.transferobjects.ResourcePersonOfInterest;
import com.rmsi.mast.studio.mobile.transferobjects.Property;
import com.rmsi.mast.studio.mobile.transferobjects.Right;
import com.rmsi.mast.studio.util.GeometryConversion;
import com.rmsi.mast.studio.util.StringUtils;
import com.rmsi.mast.viewer.dao.LaExtPersonLandMappingsDao;
import com.rmsi.mast.viewer.dao.LaExtTransactiondetailDao;
import com.rmsi.mast.viewer.dao.LaPartyDao;
import com.rmsi.mast.viewer.dao.ProjectRegionDao;
import com.rmsi.mast.viewer.dao.SpatialUnitDeceasedPersonDao;
import com.rmsi.mast.viewer.dao.StatusDAO;
import com.rmsi.mast.viewer.service.RegistrationRecordsService;
import com.vividsolutions.jts.io.WKTReader;

import java.util.IdentityHashMap;

@Service
public class UserDataServiceImpl implements UserDataService {

    @Autowired
    UserDAO userdao;

    @Autowired
    UserDataDao userData;

    @Autowired
    SpatialUnitDao spatialUnitDao;

    @Autowired
    SpatialUnitResourcePolygonDao spatialUnitResourcePolygondao;

    @Autowired
    SpatialUnitResourcePointDao spatialUnitResourcePointdao;

    @Autowired
    SpatialUnitResourceLineDao spatialUnitResourceLinedao;

    @Autowired
    NaturalPersonDao naturalPersonDao;

    @Autowired
    NonNaturalPersonDao nonNaturalPersonDao;

    @Autowired
    SocialTenureDao socialTenureDao;

    @Autowired
    PersonDao personDao;

    @Autowired
    GenderDao genderDao;

    @Autowired
    CitizenshipDao citizenshipDao;

    @Autowired
    DisputeTypeDao disputeTypeDao;

    @Autowired
    AcquisitionTypeDao acquisitionTypeDao;

    @Autowired
    DocumentTypeDao documentTypeDao;

    @Autowired
    MaritalStatusDao maritalStatusDao;

    @Autowired
    OccupancyTypeDao occupancyTypeDao;

    @Autowired
    TenureClassDao tenureClassDao;

    @Autowired
    ShareTypeDao tenureRelationTypeDao;

    @Autowired
    ClaimTypeDao claimTypeDAO;

    @Autowired
    LandTypeDao landTypeDao;

    @Autowired
    SlopeValuesDao slopeValuesDao;

    @Autowired
    SoilQualityValuesDao soilQualityValuesDao;

    @Autowired
    LandUseTypeDao landUseTypeDao;

    @Autowired
    PersonTypeDao personTypeDao;

    @Autowired
    EducationLevelDao educationLevelDao;

    @Autowired
    IdTypeDao idTypeDao;

    @Autowired
    GroupTypeDao groupTypeDao;

    @Autowired
    ShareTypeDao shareTypeDao;

    @Autowired
    RelationshipTypeDao relationshipTypeDao;

    @Autowired
    StatusDao status;

    @Autowired
    AttributeValuesDao attributeValuesDao;

    @Autowired
    WorkflowStatusHistoryDao workflowStatusHistoryDao;

    @Autowired
    SurveyProjectAttributeService surveyProjectAttribute;

    @Autowired
    AttributeOptionsDao attributeOptionsDao;

    @Autowired
    SourceDocumentDao sourceDocumentDao;

    @Autowired
    SpatialUnitPersonWithInterestDao spatialUnitPersonWithInterestDao;

    @Autowired
    SpatialUnitDeceasedPersonDao spatialUnitDeceasedPersonDao;

    @Autowired
    DisputeDao disputeDao;

    @Autowired
    DisputeStatusDao disputeStatusDao;

    @Autowired
    ProjectDAO projectDao;

    @Autowired
    ProjectService projectService;

    String Projectname = null;

    @Autowired
    RegistrationRecordsService registrationRecordsService;

    @Autowired
    ResourceAttributeValuesDAO resourceattributeValuesdao;

    @Autowired
    ResourceLandClassificationMappingDAO resourceLandClassificationMappingdao;

    @Autowired
    AttributeMasterDAO attributeMasterdao;

    @Autowired
    ResourceClassificationServise resourceClassificationServise;

    @Autowired
    ResourceSubClassificationService resourceSubClassificationService;

    @Autowired
    LaPartyDao laPartydao;

    @Autowired
    CustomAttributesHibernateDAO customAttributesHibernatedao;

    @Autowired
    ResourceCustomAttributesService resouceCustomAttributesService;

    @Autowired
    AttributeMasterResourcePoiDAO attributeMasterResourcePoiDao;

    @Autowired
    ResourcePOIAttributeValuesDAO resourcePOIAttributeValuesdao;

    @Autowired
    DisputeTypeDao disputeTypedao;

    @Autowired
    LaExtDisputelandmappingDAO laExtDisputelandmappingDao;

    @Autowired
    LaExtPersonLandMappingsDao laExtPersonLandMappingsDao;

    @Autowired
    LaExtTransactiondetailDao laExtTransactiondetailDao;

    @Autowired
    LaPartygroupOccupationDAO laPartygroupOccupationdao;

    @Autowired
    LaExtDisputeDAO laExtDisputeDAO;

    @Autowired
    ExistingClaimDao existingClaimDao;

    @Autowired
    ResourceSourceDocumentDao resourceSourceDocumentdao;

    @Autowired
    ProjectRegionDao projRegionDao;

    @Autowired
    WorkflowStatusHistoryDAO workflowStatusHistoryDAO;

    @Autowired
    StatusDAO statusDAO;

    @Autowired
    WorkflowDAO workflowDAO;
//    @Autowired
//    DisputeBasicDAO disputeBasicDao;
    private static final Logger logger = Logger.getLogger(UserDataServiceImpl.class.getName());

    @Override
    public User authenticateByEmail(String email, String passwd) {

        User user = userdao.findByUniqueName(email);

        if (user != null) {
            String decryptedPass = decryptPassword(user.getPassword());
            if (decryptedPass.equals(passwd)) {
                user.setPassword(decryptedPass);
                return user;
            } else {
                System.out.println("Incorrect Password");
            }
        } else {
            System.out.println("Authentication Failed, username doesn't exist");
        }
        return null;
    }

    @Override
    public String getDefaultProjectByUserId(int userId) {

        User user = userData.getUserByUserId(userId);
//        Project project = projectDao.getAllUserProjects().iterator().next();

        if (user != null) {
            Projectname = user.getDefaultproject();
            return Projectname;
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public Map<String, String> saveClaims(List<Property> properties, String projectName, int userId) throws Exception {
        Long featureId = 0L;
        Long serverPropId = 0L;
        Long disputePersonid = 0L;
        Map<String, String> result = new IdentityHashMap<String, String>();

        if (properties == null || properties.size() < 1 || projectName == null || projectName.equals("") || userId < 1) {
            return null;
        }

        try {
            // Get list of all attributes defined for the project
            List<Surveyprojectattribute> projectAttributes = surveyProjectAttribute.getSurveyProjectAttributes(projectName);

            for (Property prop : properties) {
                featureId = prop.getId();
                Date creationDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a").parse(prop.getCompletionDate());
                SpatialUnit su = new SpatialUnit(); //spatialUnitDao.findByImeiandTimeStamp(prop.getImei(), creationDate);

//                if (spatialUnit != null) {
//                    result.put(featureId.toString(), Long.toString(spatialUnit.getLandid()));
//                    continue;
//                }
                Project project = projectDao.findByProjectId(Integer.parseInt(projectName));
                ProjectArea projectArea = projectService.getProjectArea(project.getName()).get(0);

                su.setClaimtypeid(Integer.parseInt(prop.getClaimTypeCode()));

                if (!StringUtils.isEmpty(prop.getUkaNumber())) {
                    su.setLandno(prop.getUkaNumber());
                }

                DateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
                Date date = dateformat.parse(prop.getCompletionDate());
                long time = date.getTime();
                su.setCreateddate(new Timestamp(time));
                su.setSurveydate(creationDate);
                su.setProjectnameid(Integer.parseInt(projectName));
                su.setCreatedby(userId);

//                spatialUnit.setTenureclassid(prop.getRight().getId().intValue());
                su.setLandno(prop.getId().toString());
                if (prop.getRight() != null) {
                    ShareType sharetype = shareTypeDao.getTenureRelationshipTypeById(prop.getRight().getShareTypeId());
                    su.setLaRightLandsharetype(sharetype);
                }

                GeometryConversion geomConverter = new GeometryConversion();
                su.setGeometrytype(prop.getGeomType());

                if (su.getGeometrytype().equalsIgnoreCase("polygon")) {
//                    spatialUnit.setGeometryformula(geomConverter.convertWktToPolygon(prop.getCoordinates()).toString());
                    WKTReader reader = new WKTReader();
                    try {
                        su.setGeometry(reader.read(geomConverter.convertWktToPolygon(prop.getCoordinates()).toString()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

//                spatialUnit.getTheGeom().setSRID(4326);
                su.setLaSpatialunitgroup1(projectArea.getLaSpatialunitgroup1());
                su.setLaSpatialunitgroup2(projectArea.getLaSpatialunitgroup2());
                su.setLaSpatialunitgroup3(projectArea.getLaSpatialunitgroup3());
                su.setLaSpatialunitgroup4(projectArea.getLaSpatialunitgroup4());
                su.setLaSpatialunitgroup5(projectArea.getLaSpatialunitgroup5());
                su.setLaSpatialunitgroupHierarchy1(projectArea.getLaSpatialunitgroupHierarchy1());
                su.setLaSpatialunitgroupHierarchy2(projectArea.getLaSpatialunitgroupHierarchy2());
                su.setLaSpatialunitgroupHierarchy3(projectArea.getLaSpatialunitgroupHierarchy3());
                su.setLaSpatialunitgroupHierarchy4(projectArea.getLaSpatialunitgroupHierarchy4());

                if (prop.getVillageId() != null && prop.getVillageId() > 0) {
                    su.setLaSpatialunitgroupHierarchy5(projRegionDao.findById(prop.getVillageId(), false));
                }

                Unit unit = new Unit();
                unit.setUnitid(1);
                su.setLaExtUnit(unit);

                su.setIsactive(true);
                su.setApplicationstatusid(1);
                if (!StringUtils.isEmpty(prop.getPolygonNumber())) {
                    su.setClaimno(Integer.parseInt(prop.getPolygonNumber()));
                }
                if (su.getClaimtypeid() == 1) {
                    su.setWorkflowstatusid(1);
                } else {
                    su.setWorkflowstatusid(10);
                }
                su.setModifiedby(userId);
                su.setModifieddate(new Date());
                su.setOther_use(prop.getOtherUse());

                AcquisitionType aqobj = new AcquisitionType();

                if (null != prop.getRight() && prop.getRight().getAcquisitionTypeId() != 0) {
                    aqobj.setAcquisitiontypeid(prop.getRight().getAcquisitionTypeId());
                    su.setLaRightAcquisitiontype(aqobj);
                }

                su.setImei(prop.getImei());
                setPropAttibutes(su, prop);

                su.setArea(su.getGeometry().getArea());
                serverPropId = spatialUnitDao.addSpatialUnit(su).getLandid();
                spatialUnitDao.clear();

                LaExtTransactiondetail transaction = new LaExtTransactiondetail();
                transaction.setCreatedby(userId);
                transaction.setCreateddate(new Date());
                transaction.setIsactive(true);

                Status status = registrationRecordsService.getStatusById(1);

                transaction.setLaExtApplicationstatus(status);
                transaction.setModuletransid(1);
                transaction.setRemarks("");
                transaction.setProcessid(1l);
                LaExtTransactiondetail LaExtTransactionObj = laExtTransactiondetailDao.addLaExtTransactiondetail(transaction);

                // Add history
                WorkflowStatusHistory sunitHistory = new WorkflowStatusHistory();
                sunitHistory.setComments("");
                sunitHistory.setCreatedby((int) userId);
                sunitHistory.setIsactive(true);
                sunitHistory.setLandid(serverPropId);
                sunitHistory.setCreateddate(new Date());
                sunitHistory.setUserid((int) userId);
                sunitHistory.setStatuschangedate(new Date());
                sunitHistory.setStatus(statusDAO.getStatusById(1));
                if (su.getClaimtypeid() == 1) {
                    sunitHistory.setWorkflow(workflowDAO.getWorkflowByid(1));
                } else {
                    sunitHistory.setWorkflow(workflowDAO.getWorkflowByid(10));
                }

                workflowStatusHistoryDAO.addWorkflowStatusHistory(sunitHistory);

                // Save property attributes
                List<AttributeValues> attributes = createAttributesList(projectAttributes, prop.getAttributes());
                attributeValuesDao.addAttributeValues(attributes, serverPropId);

                if (prop.getDocument() != null && prop.getDocument().equalsIgnoreCase("Yes")) {
                    ExistingClaim existingclaim = new ExistingClaim();
                    existingclaim.setDocumentrefno(Integer.parseInt(prop.getDocumentRefNo()));
                    existingclaim.setDocumenttype(prop.getDocumentType());
                    existingclaim.setLandid(serverPropId.intValue());
                    existingclaim.setPlotno(Integer.parseInt(prop.getPlotNo()));
                    existingclaim.setCreatedby(userId);
                    existingclaim.setCreateddate(new Date());
                    existingclaim.setDocumentdate(new Date());
                    existingclaim.setIsactive(true);
                    existingclaim.setModifiedby(userId);
                    existingclaim.setModifieddate(new Date());
                    existingClaimDao.addExistingClaim(existingclaim);
                }

                // Save Natural persons
                if (prop.getRight() != null) {
                    for (Person propPerson : prop.getRight().getNaturalPersons()) {
                        // Save natural person
                        propPerson.setIsNatural(1);
                        NaturalPerson person = new NaturalPerson();
                        person.setLaSpatialunitgroup1(projectArea.getLaSpatialunitgroup1());
                        person.setLaSpatialunitgroup3(projectArea.getLaSpatialunitgroup3());
                        person.setLaSpatialunitgroup2(projectArea.getLaSpatialunitgroup2());
                        person.setLaSpatialunitgroup4(projectArea.getLaSpatialunitgroup4());
                        person.setLaSpatialunitgroup5(projectArea.getLaSpatialunitgroup5());
                        person.setLaSpatialunitgroupHierarchy1(projectArea.getLaSpatialunitgroupHierarchy1());
                        person.setLaSpatialunitgroupHierarchy2(projectArea.getLaSpatialunitgroupHierarchy2());
                        person.setLaSpatialunitgroupHierarchy3(projectArea.getLaSpatialunitgroupHierarchy3());
                        person.setLaSpatialunitgroupHierarchy4(projectArea.getLaSpatialunitgroupHierarchy4());
                        if (su.getLaSpatialunitgroupHierarchy5() != null) {
                            person.setLaSpatialunitgroupHierarchy5(su.getLaSpatialunitgroupHierarchy5());
                        }

                        person.setCreatedby(userId);
                        person.setCreateddate(new Timestamp(time));

                        setNaturalPersonAttributes(person, propPerson, userId);

                        person = naturalPersonDao.addNaturalPerson(person);
                        attributes = createAttributesList(projectAttributes, propPerson.getAttributes());
                        attributeValuesDao.addAttributeValues(attributes, person.getPartyid());

                        // Save right
                        SocialTenureRelationship right = new SocialTenureRelationship();
                        right.setCreatedby(userId);
                        right.setCreateddate(new Timestamp(time));
                        right.setLandid(serverPropId);
                        right.setPartyid(person.getPartyid());
                        right.setLaPartygroupPersontype(person.getLaPartygroupPersontype());
                        right.setLaExtTransactiondetail(LaExtTransactionObj);
                        right.setIsactive(true);

                        setRightAttributes(right, prop.getRight());

                        SocialTenureRelationship socialTenurerelationship = socialTenureDao.addSocialTenure(right);
                        attributes = createAttributesList(projectAttributes, prop.getRight().getAttributes());
                        attributeValuesDao.addAttributeValues(attributes, socialTenurerelationship.getPersonlandid());
                    }
                }

                // Save Non-natural person
                if (prop.getRight() != null && prop.getRight().getNonNaturalPerson() != null) {

                    // Save non natural person
                    NonNaturalPerson nonPerson = new NonNaturalPerson();
                    nonPerson.setLaSpatialunitgroup1(projectArea.getLaSpatialunitgroup1());
                    nonPerson.setLaSpatialunitgroup2(projectArea.getLaSpatialunitgroup2());
                    nonPerson.setLaSpatialunitgroup3(projectArea.getLaSpatialunitgroup3());
                    nonPerson.setLaSpatialunitgroupHierarchy1(projectArea.getLaSpatialunitgroupHierarchy1());
                    nonPerson.setLaSpatialunitgroupHierarchy2(projectArea.getLaSpatialunitgroupHierarchy2());
                    nonPerson.setLaSpatialunitgroupHierarchy3(projectArea.getLaSpatialunitgroupHierarchy3());
                    PersonType persontype = new PersonType();
                    persontype.setPersontypeid(prop.getRight().getNonNaturalPerson().getIsNatural());
                    nonPerson.setLaPartygroupPersontype(persontype);
                    nonPerson.setCreatedby(userId);
                    nonPerson.setIsactive(true);
                    nonPerson.setCreateddate(new Timestamp(time));
                    setNonNaturalPersonAttributes(nonPerson, prop.getRight().getNonNaturalPerson());
                    nonPerson = nonNaturalPersonDao.addNonNaturalPerson(nonPerson);
                    attributes = createAttributesList(projectAttributes, prop.getRight().getNonNaturalPerson().getAttributes());
                    attributeValuesDao.addAttributeValues(attributes, nonPerson.getPartyid());

                    // Save right
                    SocialTenureRelationship right = new SocialTenureRelationship();
                    right.setCreatedby(userId);
                    setRightAttributes(right, prop.getRight());
                    right.setCertIssueDate(new Date());
                    right.setLandid(serverPropId);
                    right.setPartyid(nonPerson.getPartyid());
                    right.setLaPartygroupPersontype(nonPerson.getLaPartygroupPersontype());
                    right.setIsactive(true);

                    right.setCreateddate(new Timestamp(time));
                    right.setModifieddate(new Timestamp(time));
                    right.setModifiedby(userId);
                    right.setLaExtTransactiondetail(LaExtTransactionObj);

                    SocialTenureRelationship socialTenurerelationship = socialTenureDao.addSocialTenure(right);

                    attributes = createAttributesList(projectAttributes, prop.getRight().getAttributes());
                    attributeValuesDao.addAttributeValues(attributes, socialTenurerelationship.getPersonlandid());
                }

                //DisputePersonCase
                if (prop.getDispute() != null && prop.getDispute().getDisputingPersons() != null) {
                    LaExtDispute disputeobj = null;
                    int counts = 0;
                    NaturalPerson obj = null;
                    for (Person propPerson : prop.getDispute().getDisputingPersons()) {

                        NaturalPerson person = new NaturalPerson();
                        person.setLaSpatialunitgroup1(projectArea.getLaSpatialunitgroup1());
                        person.setLaSpatialunitgroup3(projectArea.getLaSpatialunitgroup3());
                        person.setLaSpatialunitgroup2(projectArea.getLaSpatialunitgroup2());
                        person.setLaSpatialunitgroup4(projectArea.getLaSpatialunitgroup4());
                        person.setLaSpatialunitgroup5(projectArea.getLaSpatialunitgroup5());
                        person.setLaSpatialunitgroupHierarchy1(projectArea.getLaSpatialunitgroupHierarchy1());
                        person.setLaSpatialunitgroupHierarchy2(projectArea.getLaSpatialunitgroupHierarchy2());
                        person.setLaSpatialunitgroupHierarchy3(projectArea.getLaSpatialunitgroupHierarchy3());
                        person.setLaSpatialunitgroupHierarchy4(projectArea.getLaSpatialunitgroupHierarchy4());
                        person.setLaSpatialunitgroupHierarchy5(projectArea.getLaSpatialunitgroupHierarchy5());

                        person.setIdentityno("13424345");
                        IdType idtype = new IdType();
                        idtype.setIdentitytypeid(1);
                        person.setLaPartygroupIdentitytype(idtype);
                        person.setCreatedby(userId);
                        person.setCreateddate(new Timestamp(time));

                        setNaturalPersonAttributes(person, propPerson, userId);
                        obj = person;
                        disputePersonid = naturalPersonDao.addNaturalPerson(person).getPartyid();
                        attributes = createAttributesList(projectAttributes, propPerson.getAttributes());
                        //Vishal(10-1-2018)
                        attributeValuesDao.addAttributeValues(attributes, person.getPartyid());
                        LaExtDispute disputes = new LaExtDispute();
                        if (prop.getDispute() != null && counts == 0) {
                            counts++;

                            disputes.setComment(prop.getDispute().getDescription());
                            disputes.setCreatedby(userId);
                            Date date1 = dateformat.parse(prop.getDispute().getRegDate());
                            disputes.setCreateddate(date1);

                            disputes.setIsactive(true);
                            disputes.setLandid(serverPropId);
                            disputes.setStatus(1);
                            disputes.setDisputetypeid(prop.getDispute().getDisputeTypeId());
                            disputeobj = laExtDisputeDAO.saveLaExtDispute(disputes);

                        }

                        LaExtDisputelandmapping dispute = new LaExtDisputelandmapping();
                        dispute.setLandid(serverPropId);
                        dispute.setPersontypeid(obj.getLaPartygroupPersontype().getPersontypeid());
                        dispute.setIsactive(true);
                        dispute.setCreatedby(userId);
                        Date date2 = dateformat.parse(prop.getDispute().getRegDate());
                        dispute.setCreateddate(date2);
                        DisputeType Disputetype = disputeTypedao.findLaExtDisputeTypeByid(prop.getDispute().getDisputeTypeId());
                        dispute.setLaExtDisputetype(Disputetype);
                        dispute.setLaExtDispute(disputeobj);
                        dispute.setLaExtTransactiondetail(LaExtTransactionObj);
                        dispute.setPartyid(disputePersonid.intValue());
                        dispute.setComment(prop.getDispute().getDescription());

                        laExtDisputelandmappingDao.saveLaExtDisputelandmapping(dispute);
                    }
                }

                // Save person of interests
                List<SpatialUnitPersonWithInterest> pois = new ArrayList<>();

                for (PersonOfInterest propPoi : prop.getPersonOfInterests()) {
                    SpatialUnitPersonWithInterest poi = new SpatialUnitPersonWithInterest();
                    if (!StringUtils.isEmpty(propPoi.getDob())) {
                        poi.setDob(new SimpleDateFormat("yyyy-MM-dd").parse(propPoi.getDob()));
                    }
                    if (propPoi.getRelationshipId() > 0) {
                        poi.setRelation(propPoi.getRelationshipId());
                    }
                    if (propPoi.getGenderId() > 0) {
                        poi.setGender(propPoi.getGenderId());
                    }

                    poi.setFirstName(propPoi.getFirstName());
                    poi.setMiddleName(propPoi.getMiddleName());
                    poi.setLastName(propPoi.getLastName());
                    poi.setAddress(propPoi.getAddress());
                    poi.setIdentityno(propPoi.getIdNumber());

                    poi.setTransactionid(LaExtTransactionObj.getTransactionid());

                    poi.setCreatedby(userId);
                    poi.setCreateddate(new Date());
                    poi.setLandid(serverPropId);
                    poi.setId(propPoi.getId());
                    poi.setIsactive(true);
                    pois.add(poi);
                }

                if (pois.size() > 0) {
                    spatialUnitPersonWithInterestDao.addNextOfKin(pois, serverPropId);
                }

                // Save decesed person
                if (prop.getDeceasedPerson() != null) {
                    SpatialunitDeceasedPerson deadPerson = new SpatialunitDeceasedPerson();
                    deadPerson.setFirstname(prop.getDeceasedPerson().getFirstName());
                    deadPerson.setLastname(prop.getDeceasedPerson().getLastName());
                    deadPerson.setMiddlename(prop.getDeceasedPerson().getMiddleName());
                    deadPerson.setPartyid(serverPropId);

                    List<SpatialunitDeceasedPerson> deadPersons = new ArrayList();
                    deadPersons.add(deadPerson);

                    spatialUnitDeceasedPersonDao.addDeceasedPerson(deadPersons, serverPropId);
                }

                result.put(featureId.toString(), Long.toString(serverPropId));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to save property: ID " + featureId.toString(), e);
            throw e;
        }
    }

    private void setRightAttributes(SocialTenureRelationship right, Right propRight) throws ParseException {
        if (right == null || propRight == null) {
            return;
        }

        if (!StringUtils.isEmpty(propRight.getCertDate())) {
            right.setCertIssueDate(new SimpleDateFormat("yyyy-MM-dd").parse(propRight.getCertDate()));
        }

        right.setCertNumber(propRight.getCertNumber());

        if (propRight.getCertTypeId() != null && propRight.getCertTypeId() > 0) {
            right.setCertificatetypeid(propRight.getCertTypeId());
        }

        right.setIsactive(true);

        if (propRight.getAttributes() == null || propRight.getAttributes().size() < 1) {
            for (Attribute attribute : propRight.getAttributes()) {
                String value = attribute.getValue();
                Long id = attribute.getId();

                if (id == 12) {
                    right.setSharepercentage(value);
                }
            }
        }
    }

    private void setNonNaturalPersonAttributes(NonNaturalPerson person, Person propPerson) throws ParseException {
        if (person == null || propPerson == null || propPerson.getAttributes() == null || propPerson.getAttributes().size() < 1) {
            return;
        }

        // person.getLaParty().setLaPartygroupPersontype(personTypeDao.getPersonTypeById(2));
//        person.setMobileGroupId(propPerson.getId().toString());
//        person.setResident(propPerson.getResident() == 1);
//        person.setMobileGroupId(propPerson.getId().toString());
        person.setIsactive(true);

        for (Attribute attribute : propPerson.getAttributes()) {
            String value = attribute.getValue();
            Long id = attribute.getId();

            if (id == 6) {
                person.setOrganizationname(value);
            }
            if (id == 1136) {
                person.setFirstname(value);

            }
            if (id == 1138) {
                person.setMiddlename(value);

            }
            if (id == 1137) {
                person.setLastname(value);

            }
            if (id == 7) {
                person.setAddress(value);
            }
            if (id == 8) {
                person.setContactno(value);
            }
            if (id == 1154) {
                AttributeOptions attOptions = attributeOptionsDao.getAttributeOptionsId(Integer.parseInt(attribute.getValue()));
                GroupType grouptype = new GroupType();
                grouptype.setGrouptypeid(attOptions.getParentid());
                person.setGroupType(grouptype);
            }
//            else if (id == 7) {
//                person.setAddress(value);
//            }
//            else if (id == 8) {
//                person.setPhoneNumber(value);
//            } else if (id == 52) {
//                person.setGroupType(groupTypeDao.getGroupTypeById(Integer.parseInt(value)));
//            }
        }
    }

    private void setNaturalPersonAttributes(NaturalPerson naturalPerson, Person propPerson, int userId) throws ParseException {

        if (naturalPerson == null || propPerson == null || propPerson.getAttributes() == null || propPerson.getAttributes().size() < 1) {
            return;
        }

        naturalPerson.setLaPartygroupPersontype(personTypeDao.getPersonTypeById(propPerson.getIsNatural()));

        naturalPerson.setIsactive(true);

        for (Attribute attribute : propPerson.getAttributes()) {
            String value = attribute.getValue();
            Long id = attribute.getId();

            if (id == 1) {
                naturalPerson.setFirstname(value);
            } else if (id == 2) {
                naturalPerson.setLastname(value);
            } else if (id == 3) {
                naturalPerson.setMiddlename(value);
            } else if (id == 4) {
                AttributeOptions attOptions = attributeOptionsDao.getAttributeOptionsId(Integer.parseInt(attribute.getValue()));
                naturalPerson.setGenderid(attOptions.getParentid());
            } else if (id == 5) {
                naturalPerson.setContactno(value);
            } else if (id == 19 || id == 265) {
                naturalPerson.setProfession(value);
            } else if (id == 1129 || id == 264) {
                Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(value);
                naturalPerson.setDateofbirth(date1);
            } else if (id == 1135) {
                AttributeOptions attOptions = attributeOptionsDao.getAttributeOptionsId(Integer.parseInt(attribute.getValue()));
                naturalPerson.setLaPartygroupOccupation(laPartygroupOccupationdao.getOccupation(attOptions.getParentid()));
            } else if (id == 20) {
                naturalPerson.setLaPartygroupEducationlevel(educationLevelDao.getEducationLevelBypk(Integer.parseInt(value)));
            } else if (id == 22) {
                AttributeOptions attOptions = attributeOptionsDao.getAttributeOptionsId(Integer.parseInt(attribute.getValue()));
                naturalPerson.setLaPartygroupMaritalstatus(maritalStatusDao.getMaritalStatusById(attOptions.getAttributeoptionsid()));
            }
            if (id == 1155) {
                if (!value.equalsIgnoreCase("")) {
                    AttributeOptions attOptions = attributeOptionsDao.getAttributeOptionsId(Integer.parseInt(attribute.getValue()));
                    naturalPerson.setLaPartygroupPersontype(personTypeDao.getPersonTypeById(attOptions.getParentid()));
                }
            } else if (id == 1134) {
                AttributeOptions attOptions = attributeOptionsDao.getAttributeOptionsId(Integer.parseInt(attribute.getValue()));
                naturalPerson.setLaPartygroupEducationlevel(educationLevelDao.getEducationLevelBypk(attOptions.getParentid()));
            } else if (id == 1151) {
                naturalPerson.setAddress(value);
            } else if (id == 42) {
                AttributeOptions attOptions = attributeOptionsDao.getAttributeOptionsId(Integer.parseInt(attribute.getValue()));
                naturalPerson.setCitizenship(attOptions.getParentid());
            } else if (id == 43) {
                naturalPerson.setResident(attribute.getValue());
            } else if (id == 1059) {
                AttributeOptions attOptions = attributeOptionsDao.getAttributeOptionsId(Integer.parseInt(attribute.getValue()));
                naturalPerson.setEthnicity(attOptions.getParentid());
            } else if (id == 1153 || id == 266) {
                naturalPerson.setIdentityno(value);
            } else if (id == 1152) {
                AttributeOptions attOptions = attributeOptionsDao.getAttributeOptionsId(Integer.parseInt(attribute.getValue()));
                naturalPerson.setLaPartygroupIdentitytype(idTypeDao.getTypeByAttributeOptionId(attOptions.getParentid()));
            } else if (id == 1156) {
                if (!value.equalsIgnoreCase("")) {
                    AttributeOptions attOptions = attributeOptionsDao.getAttributeOptionsId(Integer.parseInt(attribute.getValue()));
                    naturalPerson.setOwnertype(attOptions.getParentid());
                }
            } else if (id == 267) {
                naturalPerson.setFathername(attribute.getValue());
            } else if (id == 268) {
                naturalPerson.setMothername(attribute.getValue());
            } else if (id == 270) {
                naturalPerson.setBirthPlace(attribute.getValue());
            } else if (id == 269) {
                if (!StringUtils.isEmpty(attribute.getValue()) && !attribute.getValue().equals("0")) {
                    AttributeOptions attOptions = attributeOptionsDao.getAttributeOptionsId(Integer.parseInt(attribute.getValue()));
                    naturalPerson.setNopId(attOptions.getParentid());
                }
            } else if (id == 271) {
                if (!StringUtils.isEmpty(attribute.getValue())) {
                    Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(value);
                    naturalPerson.setIdCardDate(date1);
                }
            } else if (id == 294) {
                if (!StringUtils.isEmpty(attribute.getValue())) {
                    Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(value);
                    naturalPerson.setMandateDate(date1);
                }
            }
        }
    }

    private List<AttributeValues> createAttributesList(List<Surveyprojectattribute> projectAttributes, List<Attribute> propAttributes) {
        List<AttributeValues> attributes = new ArrayList<>();

        if (propAttributes == null || propAttributes.size() < 1 || projectAttributes == null || projectAttributes.size() < 1) {
            return attributes;
        }

        for (Attribute propAttribute : propAttributes) {
            if (propAttribute.getValue() != null && !propAttribute.getValue().equalsIgnoreCase("null")) {
                AttributeValues attribute = new AttributeValues();
                attribute.setAttributevalue(propAttribute.getValue());
                for (Surveyprojectattribute projectAttribute : projectAttributes) {
                    if (projectAttribute.getAttributeMaster().getAttributemasterid().longValue() == propAttribute.getId().longValue()) {
                        attribute.setParentuid(projectAttribute.getUid());
                        attribute.setLaExtAttributemaster(projectAttribute.getAttributeMaster().getAttributemasterid().intValue());
                        break;
                    }

                }
                attributes.add(attribute);
            }
        }
        return attributes;
    }

    /**
     * Sets Spatial unit object attributes based on property object
     */
    private void setPropAttibutes(SpatialUnit parcel, Property prop) {

        if (parcel == null || prop == null || prop.getAttributes() == null || prop.getAttributes().size() < 1) {
            return;
        }

        try {
            for (Attribute attribute : prop.getAttributes()) {
                if (attribute.getId() == 9) {
                    AttributeOptions attOptions = attributeOptionsDao.getAttributeOptionsId(Integer.parseInt(attribute.getValue()));
                    parcel.setProposedused(attOptions.getParentid());
                } else if (attribute.getId() == 13) {
                    parcel.setOccupancylength(Integer.parseInt(attribute.getValue()));
                } else if (attribute.getId() == 16) {
                    if (!StringUtils.isEmpty(attribute.getValue())) {
                        String[] landUses = attribute.getValue().split(",");
                        String landUseCodes = "";

                        for (String landUse : landUses) {
                            AttributeOptions attrOption = attributeOptionsDao.getAttributeOptionsId(Integer.parseInt(landUse.trim()));
                            if (landUseCodes.length() > 0) {
                                landUseCodes += "," + attrOption.getParentid().toString();
                            } else {
                                landUseCodes = attrOption.getParentid().toString();
                            }
                        }
                        parcel.setLandusetypeid(landUseCodes);
                    }
                } else if (attribute.getId() == 37) {
                    AttributeOptions attOptions = attributeOptionsDao.getAttributeOptionsId(Integer.parseInt(attribute.getValue()));
                    parcel.setLaBaunitLandtype(landTypeDao.getLandTypeBylandtypeId(attOptions.getParentid()));
                } else if (attribute.getId() == 39) {
                    parcel.setLaExtSlopevalue(slopeValuesDao.getSlopeValuesById(Integer.parseInt(attribute.getValue())));
                } else if (attribute.getId() == 44) {
                    parcel.setNeighborNorth(attribute.getValue());
                } else if (attribute.getId() == 45) {
                    parcel.setNeighborSouth(attribute.getValue());
                } else if (attribute.getId() == 46) {
                    parcel.setNeighborEast(attribute.getValue());
                } else if (attribute.getId() == 47) {
                    parcel.setNeighborWest(attribute.getValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setReourcePolygonPropAttibutes(SpatialUnitResourcePolygon parcel, Property prop) {
        if (parcel == null || prop == null || prop.getAttributes() == null || prop.getAttributes().size() < 1) {
            return;
        }

        // Set proposed land use from right
        if (prop.getRight() != null) {
            for (Attribute attribute : prop.getRight().getAttributes()) {
                if (attribute.getId() == 9) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(Integer.parseInt(attribute.getValue())));
                    break;
                }
            }
        }

        for (Attribute attribute : prop.getAttributes()) {
            if (attribute.getId() == 9) {
                if (Integer.parseInt(attribute.getValue()) == 26) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(1));
                } else if (Integer.parseInt(attribute.getValue()) == 27) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(2));
                } else if (Integer.parseInt(attribute.getValue()) == 28) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(3));
                } else if (Integer.parseInt(attribute.getValue()) == 29) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(4));
                } else if (Integer.parseInt(attribute.getValue()) == 30) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(5));
                } else if (Integer.parseInt(attribute.getValue()) == 31) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(6));
                } else if (Integer.parseInt(attribute.getValue()) == 32) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(7));
                } else if (Integer.parseInt(attribute.getValue()) == 33) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(8));
                } else if (Integer.parseInt(attribute.getValue()) == 34) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(9));
                }

            } //               else if (attribute.getId() == 15) {
            //                   parcel.setHousehidno(Integer.parseInt(attribute.getValue()));
            //               }
            else if (attribute.getId() == 16) {
                if (Integer.parseInt(attribute.getValue()) == 16) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(1));
                } else if (Integer.parseInt(attribute.getValue()) == 17) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(2));
                } else if (Integer.parseInt(attribute.getValue()) == 18) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(3));
                } else if (Integer.parseInt(attribute.getValue()) == 19) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(4));
                } else if (Integer.parseInt(attribute.getValue()) == 20) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(5));
                } else if (Integer.parseInt(attribute.getValue()) == 21) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(6));
                } else if (Integer.parseInt(attribute.getValue()) == 22) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(7));
                } else if (Integer.parseInt(attribute.getValue()) == 23) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(8));
                } else if (Integer.parseInt(attribute.getValue()) == 24) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(9));
                } else if (Integer.parseInt(attribute.getValue()) == 25) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(10));
                }
            } else if (attribute.getId() == 17) {
//                   parcel.getLaExtPersonlandmappings().get(0).getLaExtTransactiondetail().setRemarks(attribute.getValue());
            } //               else if (attribute.getId() == 28) {
            //                   parcel.setLandOwner(attribute.getValue());
            //               }
            //               else if (attribute.getId() == 34) {
            //                   parcel.setAddress1(attribute.getValue());
            //               } 
            else if (attribute.getId() == 37) {
                if (Integer.parseInt(attribute.getValue()) == 35) {
                    parcel.setLaBaunitLandtype(landTypeDao.getLandTypeById(1));
                } else if (Integer.parseInt(attribute.getValue()) == 36) {
                    parcel.setLaBaunitLandtype(landTypeDao.getLandTypeById(2));
                } else if (Integer.parseInt(attribute.getValue()) == 37) {
                    parcel.setLaBaunitLandtype(landTypeDao.getLandTypeById(4));
                } else if (Integer.parseInt(attribute.getValue()) == 38) {
                    parcel.setLaBaunitLandtype(landTypeDao.getLandTypeById(3));
                }
            } else if (attribute.getId() == 39) {
                parcel.setLaExtSlopevalue(slopeValuesDao.getSlopeValuesById(Integer.parseInt(attribute.getValue())));
            } else if (attribute.getId() == 44) {
                parcel.setNeighborNorth(attribute.getValue());
            } else if (attribute.getId() == 45) {
                parcel.setNeighborSouth(attribute.getValue());
            } else if (attribute.getId() == 46) {
                parcel.setNeighborEast(attribute.getValue());
            } else if (attribute.getId() == 47) {
                parcel.setNeighborWest(attribute.getValue());
            }
//               else if (attribute.getId() == 53) {
//                   parcel.getLaBaunitLandusetype().setLandusetypeEn(attribute.getValue());
//               }
        }
    }

    private void setReourceLinePropAttibutes(SpatialUnitResourceLine parcel, Property prop) {
        if (parcel == null || prop == null || prop.getAttributes() == null || prop.getAttributes().size() < 1) {
            return;
        }

        // Set proposed land use from right
        if (prop.getRight() != null) {
            for (Attribute attribute : prop.getRight().getAttributes()) {
                if (attribute.getId() == 9) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(Integer.parseInt(attribute.getValue())));
                    break;
                }
            }
        }

        for (Attribute attribute : prop.getAttributes()) {
            if (attribute.getId() == 9) {
                if (Integer.parseInt(attribute.getValue()) == 26) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(1));
                } else if (Integer.parseInt(attribute.getValue()) == 27) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(2));
                } else if (Integer.parseInt(attribute.getValue()) == 28) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(3));
                } else if (Integer.parseInt(attribute.getValue()) == 29) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(4));
                } else if (Integer.parseInt(attribute.getValue()) == 30) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(5));
                } else if (Integer.parseInt(attribute.getValue()) == 31) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(6));
                } else if (Integer.parseInt(attribute.getValue()) == 32) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(7));
                } else if (Integer.parseInt(attribute.getValue()) == 33) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(8));
                } else if (Integer.parseInt(attribute.getValue()) == 34) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(9));
                }

            } //               else if (attribute.getId() == 15) {
            //                   parcel.setHousehidno(Integer.parseInt(attribute.getValue()));
            //               }
            else if (attribute.getId() == 16) {
                if (Integer.parseInt(attribute.getValue()) == 16) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(1));
                } else if (Integer.parseInt(attribute.getValue()) == 17) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(2));
                } else if (Integer.parseInt(attribute.getValue()) == 18) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(3));
                } else if (Integer.parseInt(attribute.getValue()) == 19) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(4));
                } else if (Integer.parseInt(attribute.getValue()) == 20) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(5));
                } else if (Integer.parseInt(attribute.getValue()) == 21) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(6));
                } else if (Integer.parseInt(attribute.getValue()) == 22) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(7));
                } else if (Integer.parseInt(attribute.getValue()) == 23) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(8));
                } else if (Integer.parseInt(attribute.getValue()) == 24) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(9));
                } else if (Integer.parseInt(attribute.getValue()) == 25) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(10));
                }
            } else if (attribute.getId() == 17) {
//                   parcel.getLaExtPersonlandmappings().get(0).getLaExtTransactiondetail().setRemarks(attribute.getValue());
            } //               else if (attribute.getId() == 28) {
            //                   parcel.setLandOwner(attribute.getValue());
            //               }
            //               else if (attribute.getId() == 34) {
            //                   parcel.setAddress1(attribute.getValue());
            //               } 
            else if (attribute.getId() == 37) {
                if (Integer.parseInt(attribute.getValue()) == 35) {
                    parcel.setLaBaunitLandtype(landTypeDao.getLandTypeById(1));
                } else if (Integer.parseInt(attribute.getValue()) == 36) {
                    parcel.setLaBaunitLandtype(landTypeDao.getLandTypeById(2));
                } else if (Integer.parseInt(attribute.getValue()) == 37) {
                    parcel.setLaBaunitLandtype(landTypeDao.getLandTypeById(4));
                } else if (Integer.parseInt(attribute.getValue()) == 38) {
                    parcel.setLaBaunitLandtype(landTypeDao.getLandTypeById(3));
                }
            } else if (attribute.getId() == 39) {
                parcel.setLaExtSlopevalue(slopeValuesDao.getSlopeValuesById(Integer.parseInt(attribute.getValue())));
            } else if (attribute.getId() == 44) {
                parcel.setNeighborNorth(attribute.getValue());
            } else if (attribute.getId() == 45) {
                parcel.setNeighborSouth(attribute.getValue());
            } else if (attribute.getId() == 46) {
                parcel.setNeighborEast(attribute.getValue());
            } else if (attribute.getId() == 47) {
                parcel.setNeighborWest(attribute.getValue());
            }
//               else if (attribute.getId() == 53) {
//                   parcel.getLaBaunitLandusetype().setLandusetypeEn(attribute.getValue());
//               }
        }
    }

    private void setReourcePointPropAttibutes(SpatialUnitResourcePoint parcel, Property prop) {
        if (parcel == null || prop == null || prop.getAttributes() == null || prop.getAttributes().size() < 1) {
            return;
        }

        // Set proposed land use from right
        if (prop.getRight() != null) {
            for (Attribute attribute : prop.getRight().getAttributes()) {
                if (attribute.getId() == 9) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(Integer.parseInt(attribute.getValue())));
                    break;
                }
            }
        }

        for (Attribute attribute : prop.getAttributes()) {
            if (attribute.getId() == 9) {
                if (Integer.parseInt(attribute.getValue()) == 26) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(1));
                } else if (Integer.parseInt(attribute.getValue()) == 27) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(2));
                } else if (Integer.parseInt(attribute.getValue()) == 28) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(3));
                } else if (Integer.parseInt(attribute.getValue()) == 29) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(4));
                } else if (Integer.parseInt(attribute.getValue()) == 30) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(5));
                } else if (Integer.parseInt(attribute.getValue()) == 31) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(6));
                } else if (Integer.parseInt(attribute.getValue()) == 32) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(7));
                } else if (Integer.parseInt(attribute.getValue()) == 33) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(8));
                } else if (Integer.parseInt(attribute.getValue()) == 34) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(9));
                }

            } //               else if (attribute.getId() == 15) {
            //                   parcel.setHousehidno(Integer.parseInt(attribute.getValue()));
            //               }
            else if (attribute.getId() == 16) {
                if (Integer.parseInt(attribute.getValue()) == 16) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(1));
                } else if (Integer.parseInt(attribute.getValue()) == 17) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(2));
                } else if (Integer.parseInt(attribute.getValue()) == 18) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(3));
                } else if (Integer.parseInt(attribute.getValue()) == 19) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(4));
                } else if (Integer.parseInt(attribute.getValue()) == 20) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(5));
                } else if (Integer.parseInt(attribute.getValue()) == 21) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(6));
                } else if (Integer.parseInt(attribute.getValue()) == 22) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(7));
                } else if (Integer.parseInt(attribute.getValue()) == 23) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(8));
                } else if (Integer.parseInt(attribute.getValue()) == 24) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(9));
                } else if (Integer.parseInt(attribute.getValue()) == 25) {
                    parcel.setLaBaunitLandusetype(landUseTypeDao.getLandUseTypeById(10));
                }
            } else if (attribute.getId() == 17) {
//                   parcel.getLaExtPersonlandmappings().get(0).getLaExtTransactiondetail().setRemarks(attribute.getValue());
            } //               else if (attribute.getId() == 28) {
            //                   parcel.setLandOwner(attribute.getValue());
            //               }
            //               else if (attribute.getId() == 34) {
            //                   parcel.setAddress1(attribute.getValue());
            //               } 
            else if (attribute.getId() == 37) {
                if (Integer.parseInt(attribute.getValue()) == 35) {
                    parcel.setLaBaunitLandtype(landTypeDao.getLandTypeById(1));
                } else if (Integer.parseInt(attribute.getValue()) == 36) {
                    parcel.setLaBaunitLandtype(landTypeDao.getLandTypeById(2));
                } else if (Integer.parseInt(attribute.getValue()) == 37) {
                    parcel.setLaBaunitLandtype(landTypeDao.getLandTypeById(4));
                } else if (Integer.parseInt(attribute.getValue()) == 38) {
                    parcel.setLaBaunitLandtype(landTypeDao.getLandTypeById(3));
                }
            } else if (attribute.getId() == 39) {
                parcel.setLaExtSlopevalue(slopeValuesDao.getSlopeValuesById(Integer.parseInt(attribute.getValue())));
            } else if (attribute.getId() == 44) {
                parcel.setNeighborNorth(attribute.getValue());
            } else if (attribute.getId() == 45) {
                parcel.setNeighborSouth(attribute.getValue());
            } else if (attribute.getId() == 46) {
                parcel.setNeighborEast(attribute.getValue());
            } else if (attribute.getId() == 47) {
                parcel.setNeighborWest(attribute.getValue());
            }
//               else if (attribute.getId() == 53) {
//                   parcel.getLaBaunitLandusetype().setLandusetypeEn(attribute.getValue());
//               }
        }
    }

    @Override
    @Transactional(noRollbackFor = Exception.class)
    public SourceDocument uploadMultimedia(SourceDocument sourceDocument, MultipartFile mpFile, File documentsDir) {

        /**
         * 1) Insert source document
         */
        sourceDocument = sourceDocumentDao.addSourceDocument(sourceDocument);

        /**
         * 2) Insert values in AttributeValues
         */
        AttributeValues attributeValues;

        List<AttributeValues> attributeValuesList = new ArrayList<AttributeValues>();
//        String projectName = spatialUnitDao.getSpatialUnitByUsin(sourceDocument.getLaSpatialunitLand().getLandid()).getProjectnameid().getName();

        if ((sourceDocument.getLaExtTransactiondetail().getRemarks() != null)) {

            attributeValues = new AttributeValues();

            attributeValues.setParentuid(surveyProjectAttribute
                    .getSurveyProjectAttributeId(10, spatialUnitDao
                            .getSpatialUnitByUsin(sourceDocument.getLaSpatialunitLand())
                            .getProjectnameid().toString()));

            attributeValues.setLaExtAttributemaster(10);
            attributeValues.setAttributevalue(sourceDocument.getRemarks());
            attributeValuesList.add(attributeValues);
        }

        if ((sourceDocument.getDocumentname() != null)) {
            attributeValues = new AttributeValues();
            attributeValues.setParentuid(surveyProjectAttribute
                    .getSurveyProjectAttributeId(11, spatialUnitDao
                            .getSpatialUnitByUsin(sourceDocument.getLaSpatialunitLand())
                            .getProjectnameid().toString()));
            attributeValues.setLaExtAttributemaster(10);
            attributeValues.setAttributevalue(sourceDocument.getDocumentname());
            attributeValuesList.add(attributeValues);
        }

//        if ((sourceDocument.getDocumentType() != null)) {
//            attributeValues = new AttributeValues();
//            attributeValues.setUid(surveyProjectAttribute
//                    .getSurveyProjectAttributeId(340, projectName));
//
//            attributeValues.setValue(attributeOptionsDao.getAttributeOptionsId(340, sourceDocument.getDocumentType().getCode().intValue()));
//            attributeValuesList.add(attributeValues);
//        }
        attributeValuesDao.addAttributeValues(attributeValuesList, Long.valueOf(sourceDocument.getDocumentid()));

        /**
         * 3) Save file on server *
         */
        try {
            byte[] document = mpFile.getBytes();

            if (sourceDocument.getDocumentid() != 0) {
                /**
                 * Create the file on Server + "." +
                 * FileUtils.getFileExtension(sourceDocument.getDocumentname()
                 */
                /*String Docsextention= sourceDocument.getLaExtDocumentformat().getDocumentformatEn();
            	String[] Docsextentionarray=Docsextention.split("/");*/
                File serverFile = new File(documentsDir + File.separator
                        + sourceDocument.getDocumentname());

                try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(serverFile))) {
                    outputStream.write(document);
                    outputStream.flush();
                    outputStream.close();
                }
            }
        } catch (MultipartException | IOException ex) {
            logger.error("Exception", ex);
        }
        return sourceDocument;
    }

    @Override
    @Transactional(noRollbackFor = Exception.class)
    public ResourceSourceDocument uploadResourceMultimedia(ResourceSourceDocument sourceDocument, MultipartFile mpFile, File documentsDir) {

        /**
         * 1) Insert source document
         */
        sourceDocument = resourceSourceDocumentdao.addResourceDocument(sourceDocument);

        /**
         * 2) Insert values in AttributeValues
         */
//        AttributeValues attributeValues;
//
//        List<AttributeValues> attributeValuesList = new ArrayList<AttributeValues>();
////        String projectName = spatialUnitDao.getSpatialUnitByUsin(sourceDocument.getLaSpatialunitLand().getLandid()).getProjectnameid().getName();
//
//        if ((sourceDocument.getLaExtTransactiondetail().getRemarks() != null)) {
//
//            attributeValues = new AttributeValues();
//
//            attributeValues.setParentuid(surveyProjectAttribute
//                    .getSurveyProjectAttributeId(10,spatialUnitDao
//							.getSpatialUnitByUsin(sourceDocument.getLaSpatialunitLand())
//							.getProjectnameid().toString()));
//            
//            attributeValues.setLaExtAttributemaster(10);
//            attributeValues.setAttributevalue(sourceDocument.getRemarks());
//            attributeValuesList.add(attributeValues);
//        }
//
//        if ((sourceDocument.getDocumentname() != null)) {
//            attributeValues = new AttributeValues();
//            attributeValues.setParentuid(surveyProjectAttribute
//                    .getSurveyProjectAttributeId(11,spatialUnitDao
//							.getSpatialUnitByUsin(sourceDocument.getLaSpatialunitLand())
//							.getProjectnameid().toString()));
//            attributeValues.setLaExtAttributemaster(10);
//            attributeValues.setAttributevalue(sourceDocument.getDocumentname());
//            attributeValuesList.add(attributeValues);
//        }
//        if ((sourceDocument.getDocumentType() != null)) {
//            attributeValues = new AttributeValues();
//            attributeValues.setUid(surveyProjectAttribute
//                    .getSurveyProjectAttributeId(340, projectName));
//
//            attributeValues.setValue(attributeOptionsDao.getAttributeOptionsId(340, sourceDocument.getDocumentType().getCode().intValue()));
//            attributeValuesList.add(attributeValues);
//        }
//        attributeValuesDao.addAttributeValues(attributeValuesList, Long.valueOf(sourceDocument.getDocumentid()));
        /**
         * 3) Save file on server *
         */
        try {
            byte[] document = mpFile.getBytes();

            if (sourceDocument.getDocumentid() != 0) {
                /**
                 * Create the file on Server + "." +
                 * FileUtils.getFileExtension(sourceDocument.getDocumentname()
                 */
                File serverFile = new File(documentsDir + File.separator
                        + sourceDocument.getDocumentname());

                try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(serverFile))) {
                    outputStream.write(document);
                }
            }
        } catch (MultipartException | IOException ex) {
            logger.error("Exception", ex);
        }
        return sourceDocument;
    }

    @Override
    public SourceDocument findMultimedia(String fileName, Long usin) {

        return sourceDocumentDao.findByUsinandFile(fileName, usin);

    }

    @Override
    public Long findPersonByMobileGroupId(String mobileGroupId, Long usin) {
        try {
            return personDao.findPersonIdClientId(mobileGroupId, usin);
        } catch (Exception ex) {
            logger.error("Exception", ex);
            System.out.println("Exception while finding PERSON:: " + ex);
            throw ex;
        }
    }

    /**
     * This methods decrypts the password
     *
     * @param enycPasswd : Encrypted Password
     * @return: Returns the decrypted password
     */
    private String decryptPassword(String enycPasswd) {

        final String ENCRYPT_KEY = "HG58YZ3CR9";
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(ENCRYPT_KEY);
        encryptor.setAlgorithm("PBEWithMD5AndTripleDES");
        String decPasswd = encryptor.decrypt(enycPasswd);

        return decPasswd;
    }

//    @Override
//    public boolean updateNaturalPersonAttribValues(NaturalPerson naturalPerson, String project) {
////        try {
////            List<AttributeValues> attribsList = new ArrayList<>();
////            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
////            long parentUid = naturalPerson.getPartyid();
////
////            if (!StringUtils.isEmpty(naturalPerson.getFirstname())) {
////                addAttribute(1, project, parentUid, naturalPerson.getFirstname(), attribsList);
////            }
////            if (!StringUtils.isEmpty(naturalPerson.getLastname())) {
////                addAttribute(2, project, parentUid, naturalPerson.getLastname(), attribsList);
////            }
////            if (!StringUtils.isEmpty(naturalPerson.getMiddlename())) {
////                addAttribute(3, project, parentUid, naturalPerson.getMiddlename(), attribsList);
////            }
////            if (naturalPerson.getGenderid() != null) {
////                addAttribute(4, project, parentUid,
////                        attributeOptionsDao.getAttributeOptionsId(4, (int) naturalPerson.getGenderid()),
////                        attribsList);
////            }
//////            if (!StringUtils.isEmpty(naturalPerson.getMobile())) {
//////                addAttribute(5, project, parentUid, naturalPerson.getMobile(), attribsList);
//////            }
////            if (!StringUtils.isEmpty(naturalPerson.getIdentityno())) {
////                addAttribute(30, project, parentUid, naturalPerson.getIdentityno(), attribsList);
////            }
//////            if (naturalPerson.getAge() != 0) {
//////                addAttribute(21, project, parentUid, Integer.toString(naturalPerson.getAge()), attribsList);
//////            }
////            if (!StringUtils.isEmpty(naturalPerson.getLaPartygroupOccupation().getOccupationEn())) {
////                addAttribute(19, project, parentUid, naturalPerson.getLaPartygroupOccupation().getOccupationEn(), attribsList);
////            }
////            if (naturalPerson.getLaPartygroupEducationlevel() != null) {
////                addAttribute(20, project, parentUid,
////                        attributeOptionsDao.getAttributeOptionsId(20, (int) naturalPerson.getLaPartygroupEducationlevel().getEducationlevelid()),
////                        attribsList);
////            }
//////            if (!StringUtils.isEmpty(naturalPerson.getTenure_Relation())) {
//////                addAttribute(25, project, parentUid, naturalPerson.getTenure_Relation(), attribsList);
//////            }
//////            if (!StringUtils.isEmpty(naturalPerson.getHouseholdRelation())) {
//////                addAttribute(26, project, parentUid, naturalPerson.getHouseholdRelation(), attribsList);
//////            }
//////            if (!StringUtils.isEmpty(naturalPerson.getWitness())) {
//////                addAttribute(27, project, parentUid, naturalPerson.getWitness(), attribsList);
//////            }
////            if (naturalPerson.getLaPartygroupMaritalstatus() != null) {
////                addAttribute(22, project, parentUid,
////                        attributeOptionsDao.getAttributeOptionsId(22, (int) naturalPerson.getLaPartygroupMaritalstatus().getMaritalstatusid()),
////                        attribsList);
////            }
//////            if (naturalPerson.getOwner() != null) {
//////                addAttribute(40, project, parentUid, naturalPerson.getOwner().toString(), attribsList);
//////            }
//////            if (!StringUtils.isEmpty(naturalPerson.getAdministator())) {
//////                addAttribute(41, project, parentUid, naturalPerson.getAdministator(), attribsList);
//////            }
//////            if (naturalPerson.getCitizenship_id() != null) {
//////                addAttribute(42, project, parentUid,
//////                        attributeOptionsDao.getAttributeOptionsId(42, (int) naturalPerson.getCitizenship_id().getId()),
//////                        attribsList);
//////            }
//////            if (naturalPerson.getResident_of_village() != null) {
//////                addAttribute(43, project, parentUid, naturalPerson.getResident_of_village().toString(), attribsList);
//////            }
////            if (naturalPerson.getLaPartygroupPersontype() != null) {
////                addAttribute(54, project, parentUid,
////                        attributeOptionsDao.getAttributeOptionsId(54,  naturalPerson.getLaPartygroupPersontype().getPersontypeid().intValue()),
////                        attribsList);
////            }
////            if (!StringUtils.isEmpty(naturalPerson.getIdentityno())) {
////                addAttribute(310, project, parentUid, naturalPerson.getIdentityno(), attribsList);
////            }
////            if (naturalPerson.getDateofbirth() != null) {
////                addAttribute(330, project, parentUid, dateFormat.format(naturalPerson.getDateofbirth()), attribsList);
////            }
////            if (naturalPerson.getLaPartygroupIdentitytype() != null) {
////                addAttribute(320, project, parentUid,
////                        attributeOptionsDao.getAttributeOptionsId(320, (int) naturalPerson.getLaPartygroupIdentitytype().getIdentitytypeid()),
////                        attribsList);
////            }
////            attributeValuesDao.updateAttributeValues(attribsList);
////        } catch (Exception e) {
//            logger.error("Exception", e);
//            e.printStackTrace();
//            return false;
//        }
//        return null;
//    }
//
//    private void addAttribute(long attributeId, String project, Long parentUid, String value, List<AttributeValues> list) {
//        Long attributeProjectId = surveyProjectAttribute.getSurveyProjectAttributeId(attributeId, project);
//        if (attributeProjectId != null && parentUid != null) {
//            AttributeValues attributeValues = new AttributeValues();
//            attributeValues.setParentuid(parentUid);
//            attributeValues.setAttributevalue(value);
////            attributeValues.setUid(attributeProjectId);
//            list.add(attributeValues);
//        }
//    }
//
//    @Override
//    public boolean updateTenureAttribValues(SocialTenureRelationship socialTenure, String project) {
//        try {
//            List<AttributeValues> attribsList = new ArrayList<AttributeValues>();
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            long parentUid = socialTenure.getPersonlandid();
//
//            if (socialTenure.getCreateddate() != null) {
//                addAttribute(32, project, parentUid, dateFormat.format(socialTenure.getCreateddate()), attribsList);
//            }
////            if (socialTenure.getSocial_tenure_enddate() != null) {
////                addAttribute(33, project, parentUid, dateFormat.format(socialTenure.getSocial_tenure_enddate()), attribsList);
////            }
////            if (socialTenure.getTenureDuration() != 0) {
////                addAttribute(13, project, parentUid, socialTenure.getTenureDuration() + "", attribsList);
////            }
////            if (socialTenure.getLaSpatialunitLand().getLaRightLandsharetype() != null) {
////                addAttribute(31, project, parentUid,
////                        attributeOptionsDao.getAttributeOptionsId(31, socialTenure.getLaSpatialunitLand().getLaRightLandsharetype().getLandsharetypeid()),
////                        attribsList);
////            }
////            if (socialTenure.getOccupancyTypeId() != null) {
////                addAttribute(24, project, parentUid,
////                        attributeOptionsDao.getAttributeOptionsId(24, socialTenure.getOccupancyTypeId().getOccId()),
////                        attribsList);
////            }
////            if (socialTenure.getLaParty().getLaPartyPerson().getLaRightTenureclass() != null) {
////                addAttribute(23, project, parentUid,
////                        attributeOptionsDao.getAttributeOptionsId(23, socialTenure.getLaParty().getLaPartyPerson().getLaRightTenureclass().getTenureclassid()),
////                        attribsList);
////            }
////            if (socialTenure.getLaSpatialunitLand().getLaRightAcquisitiontype() != null) {
////                addAttribute(300, project, parentUid,
////                        attributeOptionsDao.getAttributeOptionsId(300, socialTenure.getLaSpatialunitLand().getLaRightAcquisitiontype().getAcquisitiontypeid()),
////                        attribsList);
////            }
//            attributeValuesDao.updateAttributeValues(attribsList);
//        } catch (Exception e) {
//            logger.error("Exception", e);
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//  
//    
//    @Override
//    public boolean updateNonNaturalPersonAttribValues(
//            NonNaturalPerson nonnaturalPerson, String project) {
////        try {
////            List<AttributeValues> attribsList = new ArrayList<>();
////            long parentUid = nonnaturalPerson.getOrganizationid();
////
//////          if (StringUtils.isNotEmpty(nonnaturalPerson.getAddress())) {
//////              addAttribute(7, project, parentUid, nonnaturalPerson.getAddress(), attribsList);
//////          }
////          if (StringUtils.isNotEmpty(nonnaturalPerson.getOrganizationname())) {
////              addAttribute(6, project, parentUid, nonnaturalPerson.getOrganizationname(), attribsList);
////          }
////          if (StringUtils.isNotEmpty(nonnaturalPerson.getContactno())) {
////              addAttribute(8, project, parentUid, nonnaturalPerson.getContactno(), attribsList);
////          }
////          if (nonnaturalPerson.getGroupType() != null) {
////              addAttribute(52, project, parentUid,
////                      attributeOptionsDao.getAttributeOptionsId(52, (int) nonnaturalPerson.getGroupType().getGrouptypeid()), 
////                      attribsList);
////          }
////            attributeValuesDao.updateAttributeValues(attribsList);
////        } catch (Exception e) {
////            logger.error("Exception", e);
////            e.printStackTrace();
////            return false;
////        }
//        return true;
//    }
//
//    @Override
//    public boolean updateGeneralAttribValues(SpatialUnitTable spatialunit, String project) {
//        try {
//            List<SocialTenureRelationship> rights = socialTenureDao.findSocailTenureByUsin(spatialunit.getUsin());
//            List<AttributeValues> attribsList = new ArrayList<AttributeValues>();
//            long parentUid = spatialunit.getUsin();
//
//            if (spatialunit.getHousehidno() != 0) {
//                addAttribute(15, project, parentUid, spatialunit.getHousehidno() + "", attribsList);
//            }
//            if (StringUtils.isNotEmpty(spatialunit.getComments())) {
//                addAttribute(17, project, parentUid, spatialunit.getComments(), attribsList);
//            }
//            if (StringUtils.isNotEmpty(spatialunit.getAddress1())) {
//                addAttribute(34, project, parentUid, spatialunit.getAddress1(), attribsList);
//            }
//            if (StringUtils.isNotEmpty(spatialunit.getAddress2())) {
//                addAttribute(35, project, parentUid, spatialunit.getAddress2(), attribsList);
//            }
//            if (StringUtils.isNotEmpty(spatialunit.getPostal_code())) {
//                addAttribute(36, project, parentUid, spatialunit.getPostal_code(), attribsList);
//            }
//            if (spatialunit.getProposedUse() != null) {
//                String value = attributeOptionsDao.getAttributeOptionsId(9, (int) spatialunit.getProposedUse().getLandusetypeid());
//                addAttribute(9, project, parentUid, value, attribsList);
//
//                if (rights != null) {
//                    for (SocialTenureRelationship right : rights) {
//                        addAttribute(9, project, (long) right.getCreatedby(), value, attribsList);
//                    }
//                }
//            }
//            if (spatialunit.getExistingUse() != null) {
//                String value = attributeOptionsDao.getAttributeOptionsId(16, (int) spatialunit.getExistingUse().getLandusetypeid());
//                addAttribute(16, project, parentUid, value, attribsList);
//            }
//            if (spatialunit.getLandType() != null) {
//                String value = attributeOptionsDao.getAttributeOptionsId(37, (int) spatialunit.getLandType().getLandtypeid());
//                addAttribute(37, project, parentUid, value, attribsList);
//            }
//            if (spatialunit.getSoilQualityValues() != null) {
//                String value = attributeOptionsDao.getAttributeOptionsId(38, (int) spatialunit.getSoilQualityValues().getLandsoilqualityid());
//                addAttribute(38, project, parentUid, value, attribsList);
//            }
//            if (spatialunit.getSlopeValues() != null) {
//                String value = attributeOptionsDao.getAttributeOptionsId(39, (int) spatialunit.getSlopeValues().getSlopevalueid());
//                addAttribute(39, project, parentUid, value, attribsList);
//            }
//            if (StringUtils.isNotEmpty(spatialunit.getNeighbor_north())) {
//                addAttribute(44, project, parentUid, spatialunit.getNeighbor_north(), attribsList);
//            }
//            if (StringUtils.isNotEmpty(spatialunit.getNeighbor_south())) {
//                addAttribute(45, project, parentUid, spatialunit.getNeighbor_south(), attribsList);
//            }
//            if (StringUtils.isNotEmpty(spatialunit.getNeighbor_east())) {
//                addAttribute(46, project, parentUid, spatialunit.getNeighbor_east(), attribsList);
//            }
//            if (StringUtils.isNotEmpty(spatialunit.getNeighbor_west())) {
//                addAttribute(47, project, parentUid, spatialunit.getNeighbor_west(), attribsList);
//            }
//            if (StringUtils.isNotEmpty(spatialunit.getOtherUseType())) {
//                addAttribute(53, project, parentUid, spatialunit.getOtherUseType(), attribsList);
//            }
//            attributeValuesDao.updateAttributeValues(attribsList);
//        } catch (Exception e) {
//            logger.error("Exception", e);
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public boolean updateMultimediaAttribValues(SourceDocument sourcedocument,
//            String project) {
//        try {
//            List<AttributeValues> attribsList = new ArrayList<>();
//            long parentUid = Long.parseLong(sourcedocument.getDocumentid() + "");
//
//            if (StringUtils.isNotEmpty(sourcedocument.getDocumentname())) {
//                addAttribute(10, project, parentUid, sourcedocument.getDocumentname(), attribsList);
//            }
//            if (StringUtils.isNotEmpty(sourcedocument.getLaExtTransactiondetail().getRemarks())) {
//                addAttribute(11, project, parentUid, sourcedocument.getLaExtTransactiondetail().getRemarks(), attribsList);
//            }
//
////            if (sourcedocument.getDocumentType() != null) {
////                addAttribute(340, project, parentUid,
////                        attributeOptionsDao.getAttributeOptionsId(340, sourcedocument.getDocumentType().getCode().intValue()),
////                        attribsList);
////            }
//            attributeValuesDao.updateAttributeValues(attribsList);
//        } catch (Exception e) {
//            logger.error("Exception", e);
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    @Transactional(noRollbackFor = Exception.class)
//    public List<Long> updateAdjudicatedData(Long userId, List<Long> usinList) {
//
//        try {
//
//            List<Long> sucessfulUpdateList = new ArrayList<Long>();
//
//            WorkflowStatusHistory statusHistory;
//
//            Iterator<Long> usinIter = usinList.iterator();
//
//            // Get Spatial Unit by usin
//            while (usinIter.hasNext()) {
//
//                /**
//                 * 1) Updating Status and Stautsv update time in Spatial Unit
//                 */
//                Long usin = usinIter.next();
//                Status statusId = status.getStatusById(2);
//
////                SpatialUnit spatialUnit = spatialUnitDao
////                        .getSpatialUnitByUsin(usin);
//
//                Date statusUpdateTime = new SimpleDateFormat(
//                        "dd/MM/yyyy HH:mm:ss").parse(new SimpleDateFormat(
//                        "dd/MM/yyyy HH:mm:ss").format(new Date()));
////                spatialUnit.getLaExtPersonlandmappings().get(0).getLaExtTransactiondetail().setLaExtApplicationstatus(statusId);
////                spatialUnit.setModifieddate(new Timestamp(statusUpdateTime.getTime()));
//
//                /**
//                 * 2) Updating Status History in Workflow Status History
//                 */
//                statusHistory = new WorkflowStatusHistory();
//
//               // statusHistory.setStatus_change_time(statusUpdateTime);
//              //  statusHistory.setUserid(userId);
//               // statusHistory.setUsin(usin);
//               // statusHistory.setWorkflow_status_id(statusId
//                 //       .getWorkflowStatusId());
//
////                sucessfulUpdateList.add(spatialUnitDao.addSpatialUnit(
////                        spatialUnit).getLandid());
//                workflowStatusHistoryDao
//                        .addWorkflowStatusHistory(statusHistory);
//            }
//            return sucessfulUpdateList;
//        } catch (Exception e) {
//            logger.error("Exception in saving STATUS UPDATE in SPATIAL UNIT and STATUS HISTORY::: "
//                    + e);
//            e.printStackTrace();
//            return null;
//        }
//    }
    @Override
    @Transactional
    public Map<String, String> saveResource(List<Property> resources, String projectName, int userId) throws Exception {
        Long featureId = 0L;
        Long serverPropId = 0L;
        Map<String, String> result = new IdentityHashMap<String, String>();

        if (resources == null || resources.size() < 1 || projectName == null || projectName.equals("") || userId < 1) {
            return null;
        }

        try {
            // Get list of all attributes defined for the project
            List<Surveyprojectattribute> projectAttributes = surveyProjectAttribute.getSurveyProjectAttributes(projectName);
            for (Property prop : resources) {
                if (prop.getGeomType().equalsIgnoreCase("Polygon")) {
                    featureId = prop.getId();
                    Date creationDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a").parse(prop.getCompletionDate());
//                SpatialUnit spatialUnit = spatialUnitDao.findByImeiandTimeStamp(projectName, creationDate);
                    Project project = projectDao.findByProjectId(Integer.parseInt(projectName));
                    ProjectArea projectArea = projectService.getProjectArea(project.getName()).get(0);

//                if (spatialUnit != null) {
//                    result.put(featureId.toString(), Long.toString(spatialUnit.getLandid()));
//                    continue;
//                }
                    SpatialUnitResourcePolygon spatialUnit = new SpatialUnitResourcePolygon();
                    spatialUnit.setClaimtypeid(5);   // for resources
                    //spatialUnit.setLaRightClaimtype(claimTypeDAO.findById(prop.getClaimTypeCode(), false));

                    if (!StringUtils.isEmpty(prop.getUkaNumber())) {
                        spatialUnit.setLandno(prop.getUkaNumber());
                    }
                    spatialUnit.setGeometrytype(prop.getPolygonNumber());
                    DateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
                    Date date = dateformat.parse(prop.getCompletionDate());
                    long time = date.getTime();
                    spatialUnit.setCreateddate(dateformat.parse(prop.getCreationDate()));
//                spatialUnit.setSurveydate(new SimpleDateFormat("yyyy-MM-dd").parse(prop.getSurveyDate()));
                    spatialUnit.setProjectnameid(Integer.parseInt(projectName));
                    spatialUnit.setCreatedby(userId);
                    spatialUnit.setLandno(featureId.toString());
//                ShareType sharetype =  shareTypeDao.getTenureRelationshipTypeById(prop.getRight().getShareTypeId());
//                spatialUnit.setLaRightLandsharetype(sharetype);
//                spatialUnit.setHamletId(prop.getHamletId());
//                spatialUnit.setWitness1(prop.getAdjudicator1());
//                spatialUnit.setWitness2(prop.getAdjudicator2());

                    GeometryConversion geomConverter = new GeometryConversion();

                    spatialUnit.setGeometrytype(prop.getGeomType());

                    if (spatialUnit.getGeometrytype().equalsIgnoreCase("polygon")) {
                        WKTReader reader = new WKTReader();
                        try {
                            spatialUnit.setGeometry(reader.read(geomConverter.convertWktToPolygon(prop.getCoordinates()).toString()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

//                spatialUnit.getTheGeom().setSRID(4326);
                    spatialUnit.setLaSpatialunitgroup1(projectArea.getLaSpatialunitgroup1());
                    spatialUnit.setLaSpatialunitgroup2(projectArea.getLaSpatialunitgroup2());
                    spatialUnit.setLaSpatialunitgroup3(projectArea.getLaSpatialunitgroup3());
                    spatialUnit.setLaSpatialunitgroup4(projectArea.getLaSpatialunitgroup4());
                    spatialUnit.setLaSpatialunitgroup5(projectArea.getLaSpatialunitgroup5());
                    spatialUnit.setLaSpatialunitgroupHierarchy1(projectArea.getLaSpatialunitgroupHierarchy1());
                    spatialUnit.setLaSpatialunitgroupHierarchy2(projectArea.getLaSpatialunitgroupHierarchy2());
                    spatialUnit.setLaSpatialunitgroupHierarchy3(projectArea.getLaSpatialunitgroupHierarchy3());
                    spatialUnit.setLaSpatialunitgroupHierarchy4(projectArea.getLaSpatialunitgroupHierarchy4());
                    spatialUnit.setLaSpatialunitgroupHierarchy5(projectArea.getLaSpatialunitgroupHierarchy5());
                    Unit unit = new Unit();
                    unit.setUnitid(1);
                    spatialUnit.setLaExtUnit(unit);
                    spatialUnit.setIsactive(true);
                    spatialUnit.setApplicationstatusid(1);
                    spatialUnit.setWorkflowstatusid(1);
                    spatialUnit.setModifiedby(userId);
                    spatialUnit.setModifieddate(new Date());
//                spatialUnit.setImei(prop.getImei());

                    setReourcePolygonPropAttibutes(spatialUnit, prop);
                    try {
                        spatialUnit.setArea(geomConverter.getArea(prop.getCoordinates()));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    serverPropId = spatialUnitResourcePolygondao.addSpatialUnitResourcePolygon(spatialUnit).getLandid();
                    spatialUnitDao.clear();

                } else if (prop.getGeomType().equalsIgnoreCase("Point")) {
                    featureId = prop.getId();
                    Date creationDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a").parse(prop.getCompletionDate());
//                    SpatialUnit spatialUnit = spatialUnitDao.findByImeiandTimeStamp(projectName, creationDate);
                    Project project = projectDao.findByProjectId(Integer.parseInt(projectName));
                    ProjectArea projectArea = projectService.getProjectArea(project.getName()).get(0);

//                    if (spatialUnit != null) {
//                        result.put(featureId.toString(), Long.toString(spatialUnit.getLandid()));
//                        continue;
//                    }
                    SpatialUnitResourcePoint spatialUnit = new SpatialUnitResourcePoint();
                    spatialUnit.setClaimtypeid(5);   // for resources
                    //spatialUnit.setLaRightClaimtype(claimTypeDAO.findById(prop.getClaimTypeCode(), false));

                    if (!StringUtils.isEmpty(prop.getUkaNumber())) {
                        spatialUnit.setLandno(prop.getUkaNumber());
                    }
                    spatialUnit.setGeometrytype(prop.getPolygonNumber());
                    DateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
                    Date date = dateformat.parse(prop.getCompletionDate());
                    long time = date.getTime();
                    spatialUnit.setCreateddate(dateformat.parse(prop.getCreationDate()));
//                    spatialUnit.setSurveydate(new SimpleDateFormat("yyyy-MM-dd").parse(prop.getSurveyDate()));
                    spatialUnit.setProjectnameid(Integer.parseInt(projectName));
                    spatialUnit.setCreatedby(userId);
                    spatialUnit.setLandno(featureId.toString());
//                    ShareType sharetype =  shareTypeDao.getTenureRelationshipTypeById(prop.getRight().getShareTypeId());
//                    spatialUnit.setLaRightLandsharetype(sharetype);
//                    spatialUnit.setHamletId(prop.getHamletId());
//                    spatialUnit.setWitness1(prop.getAdjudicator1());
//                    spatialUnit.setWitness2(prop.getAdjudicator2());

                    GeometryConversion geomConverter = new GeometryConversion();

                    spatialUnit.setGeometrytype(prop.getGeomType());

                    if (spatialUnit.getGeometrytype().equalsIgnoreCase("Point")) {
                        WKTReader reader = new WKTReader();
                        try {
                            spatialUnit.setGeometry(reader.read(geomConverter.convertWktToPoint(prop.getCoordinates()).toString()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

//                    spatialUnit.getTheGeom().setSRID(4326);
                    spatialUnit.setLaSpatialunitgroup1(projectArea.getLaSpatialunitgroup1());
                    spatialUnit.setLaSpatialunitgroup2(projectArea.getLaSpatialunitgroup2());
                    spatialUnit.setLaSpatialunitgroup3(projectArea.getLaSpatialunitgroup3());
                    spatialUnit.setLaSpatialunitgroup4(projectArea.getLaSpatialunitgroup4());
                    spatialUnit.setLaSpatialunitgroup5(projectArea.getLaSpatialunitgroup5());
                    spatialUnit.setLaSpatialunitgroupHierarchy1(projectArea.getLaSpatialunitgroupHierarchy1());
                    spatialUnit.setLaSpatialunitgroupHierarchy2(projectArea.getLaSpatialunitgroupHierarchy2());
                    spatialUnit.setLaSpatialunitgroupHierarchy3(projectArea.getLaSpatialunitgroupHierarchy3());
                    spatialUnit.setLaSpatialunitgroupHierarchy4(projectArea.getLaSpatialunitgroupHierarchy4());
                    spatialUnit.setLaSpatialunitgroupHierarchy5(projectArea.getLaSpatialunitgroupHierarchy5());
                    Unit unit = new Unit();
                    unit.setUnitid(1);
                    spatialUnit.setLaExtUnit(unit);
                    spatialUnit.setIsactive(true);
                    spatialUnit.setApplicationstatusid(1);
                    spatialUnit.setWorkflowstatusid(1);
                    spatialUnit.setModifiedby(userId);
                    spatialUnit.setModifieddate(new Date());
//                    spatialUnit.setImei(prop.getImei());

                    setReourcePointPropAttibutes(spatialUnit, prop);

                    serverPropId = spatialUnitResourcePointdao.addSpatialUnitResourcePoint(spatialUnit).getLandid();
                    spatialUnitDao.clear();

                } else if (prop.getGeomType().equalsIgnoreCase("Line")) {
                    featureId = prop.getId();
                    Date creationDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a").parse(prop.getCompletionDate());
//                    SpatialUnit spatialUnit = spatialUnitDao.findByImeiandTimeStamp(projectName, creationDate);
                    Project project = projectDao.findByProjectId(Integer.parseInt(projectName));
                    ProjectArea projectArea = projectService.getProjectArea(project.getName()).get(0);

//                    if (spatialUnit != null) {
//                        result.put(featureId.toString(), Long.toString(spatialUnit.getLandid()));
//                        continue;
//                    }
                    SpatialUnitResourceLine spatialUnit = new SpatialUnitResourceLine();
                    spatialUnit.setClaimtypeid(5);   // for resources
                    //spatialUnit.setLaRightClaimtype(claimTypeDAO.findById(prop.getClaimTypeCode(), false));

                    if (!StringUtils.isEmpty(prop.getUkaNumber())) {
                        spatialUnit.setLandno(prop.getUkaNumber());
                    }
                    spatialUnit.setGeometrytype(prop.getPolygonNumber());
                    DateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
                    Date date = dateformat.parse(prop.getCompletionDate());
                    long time = date.getTime();
                    spatialUnit.setCreateddate(dateformat.parse(prop.getCreationDate()));
//                    spatialUnit.setSurveydate(new SimpleDateFormat("yyyy-MM-dd").parse(prop.getSurveyDate()));
                    spatialUnit.setProjectnameid(Integer.parseInt(projectName));
                    spatialUnit.setCreatedby(userId);
                    spatialUnit.setLandno(featureId.toString());
//                    ShareType sharetype =  shareTypeDao.getTenureRelationshipTypeById(prop.getRight().getShareTypeId());
//                    spatialUnit.setLaRightLandsharetype(sharetype);
//                    spatialUnit.setHamletId(prop.getHamletId());
//                    spatialUnit.setWitness1(prop.getAdjudicator1());
//                    spatialUnit.setWitness2(prop.getAdjudicator2());

                    GeometryConversion geomConverter = new GeometryConversion();

                    spatialUnit.setGeometrytype(prop.getGeomType());

                    if (spatialUnit.getGeometrytype().equalsIgnoreCase("Line")) {
                        WKTReader reader = new WKTReader();
                        try {
                            spatialUnit.setGeometry(reader.read(geomConverter.convertWktToLineString(prop.getCoordinates()).toString()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

//                    spatialUnit.getTheGeom().setSRID(4326);
                    spatialUnit.setLaSpatialunitgroup1(projectArea.getLaSpatialunitgroup1());
                    spatialUnit.setLaSpatialunitgroup2(projectArea.getLaSpatialunitgroup2());
                    spatialUnit.setLaSpatialunitgroup3(projectArea.getLaSpatialunitgroup3());
                    spatialUnit.setLaSpatialunitgroup4(projectArea.getLaSpatialunitgroup4());
                    spatialUnit.setLaSpatialunitgroup5(projectArea.getLaSpatialunitgroup5());
                    spatialUnit.setLaSpatialunitgroupHierarchy1(projectArea.getLaSpatialunitgroupHierarchy1());
                    spatialUnit.setLaSpatialunitgroupHierarchy2(projectArea.getLaSpatialunitgroupHierarchy2());
                    spatialUnit.setLaSpatialunitgroupHierarchy3(projectArea.getLaSpatialunitgroupHierarchy3());
                    spatialUnit.setLaSpatialunitgroupHierarchy4(projectArea.getLaSpatialunitgroupHierarchy4());
                    spatialUnit.setLaSpatialunitgroupHierarchy5(projectArea.getLaSpatialunitgroupHierarchy5());
                    Unit unit = new Unit();
                    unit.setUnitid(1);
                    spatialUnit.setLaExtUnit(unit);
                    spatialUnit.setIsactive(true);
                    spatialUnit.setApplicationstatusid(1);
                    spatialUnit.setWorkflowstatusid(1);
                    spatialUnit.setModifiedby(userId);
                    spatialUnit.setModifieddate(new Date());
//                    spatialUnit.setImei(prop.getImei());

                    setReourceLinePropAttibutes(spatialUnit, prop);

                    serverPropId = spatialUnitResourceLinedao.addSpatialUnitResourceLine(spatialUnit).getLandid();
                    spatialUnitDao.clear();

                }

//                for (ClassificationAttributes attribute : prop.getClassificationAttributes()) {
//                   
//                	 ResourceAttributeValues resourceattribvalues = new ResourceAttributeValues();
//                    if (attribute.getAttribID() == 1017) {
//                 	   AttributeMaster attributemaster = attributeMasterdao.findByAttributeId(attribute.getAttribID().longValue());
//                        resourceattribvalues.setAttributemaster(attributemaster);
//                        resourceattribvalues.setAttributevalue(attribute.getAttribValue());
//                        resourceattribvalues.setLandid(serverPropId.intValue());
//                        resourceattribvalues.setProjectid(Integer.parseInt(projectName));
//                        resourceattributeValuesdao.addResourceAttributeValues(resourceattribvalues);
//                        } 
//                    
//                    else if (attribute.getAttribID() == 1018) {
//                 	   AttributeMaster attributemaster = attributeMasterdao.findByAttributeId(attribute.getAttribID().longValue());
//                        resourceattribvalues.setAttributemaster(attributemaster);
//                        resourceattribvalues.setAttributevalue(attribute.getAttribValue());
//                        resourceattribvalues.setLandid(serverPropId.intValue());
//                        resourceattribvalues.setProjectid(Integer.parseInt(projectName));
//                        resourceattributeValuesdao.addResourceAttributeValues(resourceattribvalues);
//                        } 
//                    
//                    else if (attribute.getAttribID() == 1019) {
//                 	   AttributeMaster attributemaster = attributeMasterdao.findByAttributeId(attribute.getAttribID().longValue());
//                        resourceattribvalues.setAttributemaster(attributemaster);
//                        resourceattribvalues.setAttributevalue(attribute.getAttribValue()); 
//                        resourceattribvalues.setLandid(serverPropId.intValue());
//                        resourceattribvalues.setProjectid(Integer.parseInt(projectName));
//                        resourceattributeValuesdao.addResourceAttributeValues(resourceattribvalues);
//                        } 
//                    
//                    else if (attribute.getAttribID() == 1026) {
//                 	   AttributeMaster attributemaster = attributeMasterdao.findByAttributeId(attribute.getAttribID().longValue());
//                        resourceattribvalues.setAttributemaster(attributemaster);
//                        resourceattribvalues.setAttributevalue(attribute.getAttribValue()); 
//                        resourceattribvalues.setLandid(serverPropId.intValue());
//                        resourceattribvalues.setProjectid(Integer.parseInt(projectName));
//                        resourceattributeValuesdao.addResourceAttributeValues(resourceattribvalues);
//                        } 
//                    
//                    else if (attribute.getAttribID() == 1028) {
//                 	   AttributeMaster attributemaster = attributeMasterdao.findByAttributeId(attribute.getAttribID().longValue());
//                        resourceattribvalues.setAttributemaster(attributemaster);
//                        resourceattribvalues.setAttributevalue(attribute.getAttribValue()); 
//                        resourceattribvalues.setLandid(serverPropId.intValue());
//                        resourceattribvalues.setProjectid(Integer.parseInt(projectName));  
//                        resourceattributeValuesdao.addResourceAttributeValues(resourceattribvalues);
//                        } 
//                    
//                    else if (attribute.getAttribID() == 1029) {
//                 	   AttributeMaster attributemaster = attributeMasterdao.findByAttributeId(attribute.getAttribID().longValue());
//                        resourceattribvalues.setAttributemaster(attributemaster);
//                        resourceattribvalues.setAttributevalue(attribute.getAttribValue()); 
//                        resourceattribvalues.setLandid(serverPropId.intValue());
//                        resourceattribvalues.setProjectid(Integer.parseInt(projectName));
//                        resourceattributeValuesdao.addResourceAttributeValues(resourceattribvalues);
//                        } 
//                    
//                   
//                              
//                         
//                }
                // Resource Code
                for (Attribute attribute : prop.getAttributes()) {

                    ResourceAttributeValues resourceattribvalues = new ResourceAttributeValues();
                    AttributeMaster attributemaster = attributeMasterdao.findByAttributeId(attribute.getId().longValue());
                    resourceattribvalues.setAttributemaster(attributemaster);
                    resourceattribvalues.setAttributevalue(attribute.getValue());
                    resourceattribvalues.setGroupid(attribute.getGroupId());
                    resourceattribvalues.setLandid(serverPropId.intValue());
                    resourceattribvalues.setGeomtype(prop.getGeomType());
                    resourceattribvalues.setProjectid(Integer.parseInt(projectName));
                    resourceattributeValuesdao.addResourceAttributeValues(resourceattribvalues);

                }

                for (ResourcePersonOfInterest attributes : prop.getPersonOfInterestsRes()) {

                    ResourcePOIAttributeValues resourcePoiAttribvalues = new ResourcePOIAttributeValues();
                    AttributeMasterResourcePOI Poiattributemaster = attributeMasterResourcePoiDao.getPOIAttributteMasterById(attributes.getId().intValue());
                    resourcePoiAttribvalues.setAttributemaster(Poiattributemaster);
                    resourcePoiAttribvalues.setAttributevalue(attributes.getValue());
                    resourcePoiAttribvalues.setLandid(serverPropId.intValue());
                    resourcePoiAttribvalues.setGeomtype(prop.getGeomType());
                    resourcePoiAttribvalues.setProjectid(Integer.parseInt(projectName));
                    if (null != attributes.getGroupId()) {
                        resourcePoiAttribvalues.setGroupid(attributes.getGroupId());
                    }
                    resourcePOIAttributeValuesdao.addResourcePOIAttributeValues(resourcePoiAttribvalues);

                }

                ResourceLandClassificationMapping resourcelandClassification = new ResourceLandClassificationMapping();
                ResourceClassification resourceClassification = resourceClassificationServise.getById(prop.getClassificationAttributes().get(0).getAttribID());
                resourcelandClassification.setClassificationid(resourceClassification);
                resourcelandClassification.setLandid(serverPropId.intValue());
                resourcelandClassification.setGeomtype(prop.getGeomType());
                resourcelandClassification.setCategoryid(prop.getClassificationAttributes().get(2).getAttribID());
                resourcelandClassification.setProjectid(Integer.parseInt(projectName));
                ResourceSubClassification resourcesubClassification = resourceSubClassificationService.getById(prop.getClassificationAttributes().get(1).getAttribID());
                resourcelandClassification.setSubclassificationid(resourcesubClassification);
                resourceLandClassificationMappingdao.addResourceLandClassifications(resourcelandClassification);

                for (com.rmsi.mast.studio.mobile.transferobjects.ResourceCustomAttributes rescustomAttribute : prop.getCustomAttributes()) {

                    CustomAttributes customAttributes = new CustomAttributes();
                    ResourceCustomAttributes resouceCustomAttributes = resouceCustomAttributesService.getByCustomattributeId(rescustomAttribute.getAttribId());
                    customAttributes.setCustomattributeid(resouceCustomAttributes);
                    customAttributes.setAttributevalue(rescustomAttribute.getAttribValue());
                    customAttributes.setLandid(serverPropId.intValue());
                    customAttributes.setAttributeoptionsid(rescustomAttribute.getResID());
                    customAttributes.setGeomtype(prop.getGeomType());
                    customAttributes.setProjectid(Integer.parseInt(projectName));
                    if (rescustomAttribute.getSubclassificationid().equalsIgnoreCase("null")) {
                    } else {
                        customAttributes.setSubclassificationid(Integer.parseInt(rescustomAttribute.getSubclassificationid()));
                    }
                    customAttributesHibernatedao.addResourceCustomAttributeValues(customAttributes);
                }

                // Save property attributes
//                List<AttributeValues> attributes = createAttributesList(projectAttributes, prop.getAttributes());
//                attributeValuesDao.addAttributeValues(attributes, serverPropId);

                /*  COMMENTED BY RAHUL START                
                
                // Save Natural persons
                if (prop.getRight() != null && prop.getRight().getNonNaturalPerson() == null) {
                    for (Person propPerson : prop.getRight().getNaturalPersons()) {
                        // Save natural person
                        NaturalPerson person = new NaturalPerson();
                        person.setLaSpatialunitgroup1(projectArea.getLaSpatialunitgroup1());
                        person.setLaSpatialunitgroup2(projectArea.getLaSpatialunitgroup2());
                        person.setLaSpatialunitgroup3(projectArea.getLaSpatialunitgroup3());
                        person.setLaSpatialunitgroupHierarchy1(projectArea.getLaSpatialunitgroupHierarchy1());
                        person.setLaSpatialunitgroupHierarchy2(projectArea.getLaSpatialunitgroupHierarchy2());
                        person.setLaSpatialunitgroupHierarchy3(projectArea.getLaSpatialunitgroupHierarchy3());
                        person.setIdentityno("13424345");
                        IdType idtype = new IdType();
                        idtype.setIdentitytypeid(1);
                        person.setLaPartygroupIdentitytype(idtype);
                        person.setCreatedby(userId);
                        person.setCreateddate(new Timestamp(time));

                        setNaturalPersonAttributes(person, propPerson);
                        person = naturalPersonDao.addNaturalPerson(person);
                        attributes = createAttributesList(projectAttributes, propPerson.getAttributes());
                        attributeValuesDao.addAttributeValues(attributes, person.getPartyid());

                        // Save right
                        SocialTenureRelationship right = new SocialTenureRelationship();
                        right.setCreatedby(userId);
                        setRightAttributes(right, prop.getRight());
                        right.setLandid(serverPropId);
                        right.setPartyid(person.getPartyid());
                        right.setLaPartygroupPersontype(person.getLaPartygroupPersontype());
                        
                        LaExtTransactiondetail laExtTransactiondetail = new LaExtTransactiondetail();
            			laExtTransactiondetail.setCreatedby(userId);
            			laExtTransactiondetail.setCreateddate(new Timestamp(time));
            			laExtTransactiondetail.setIsactive(true);
            			
            			Status status = registrationRecordsService.getStatusById(1);
            			
            			laExtTransactiondetail.setLaExtApplicationstatus(status);
            			laExtTransactiondetail.setModuletransid(1);
            			laExtTransactiondetail.setRemarks("");
            			laExtTransactiondetail.setProcessid(1l);
                        
                        right.setLaExtTransactiondetail(laExtTransactiondetail);
                        right.setCreateddate(new Timestamp(time));
                        
                        SocialTenureRelationship  socialTenurerelationship  = socialTenureDao.addSocialTenure(right);
                        attributes = createAttributesList(projectAttributes, prop.getRight().getAttributes());
                        attributeValuesDao.addAttributeValues(attributes, socialTenurerelationship.getPersonlandid());
                        
                    }
                }
  COMMENTED BY RAHUL CLOSE */
                result.put(featureId.toString(), Long.toString(serverPropId));
            }

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to save property: ID " + featureId.toString(), e);
            throw e;
        }
    }

    @Override
    public boolean updateNaturalPersonAttribValues(NaturalPerson naturalPerson,
            String project) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean updateTenureAttribValues(
            SocialTenureRelationship socialTenure, String project) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean updateNonNaturalPersonAttribValues(
            NonNaturalPerson nonnaturalPerson, String project) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean updateGeneralAttribValues(SpatialUnitTable spatialUnit,
            String project) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean updateMultimediaAttribValues(SourceDocument sourcedocument,
            String project) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<Long> updateAdjudicatedData(Long userId, List<Long> usin) {
        // TODO Auto-generated method stub
        return null;
    }

}
