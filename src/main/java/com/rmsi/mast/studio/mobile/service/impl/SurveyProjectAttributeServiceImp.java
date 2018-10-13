package com.rmsi.mast.studio.mobile.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rmsi.mast.studio.dao.AttributeMasterDAO;
import com.rmsi.mast.studio.dao.DisputeDao;
import com.rmsi.mast.studio.dao.ProjectAdjudicatorDAO;
import com.rmsi.mast.studio.dao.ProjectDAO;
import com.rmsi.mast.studio.dao.ProjectHamletDAO;
import com.rmsi.mast.studio.domain.AttributeMaster;
import com.rmsi.mast.studio.domain.NaturalPerson;
import com.rmsi.mast.studio.domain.PersonType;
//import com.rmsi.mast.studio.domain.LaExtDisputelandmapping;
//import com.rmsi.mast.studio.domain.PersonType;
import com.rmsi.mast.studio.domain.Project;
import com.rmsi.mast.studio.domain.ProjectAdjudicator;
import com.rmsi.mast.studio.domain.ProjectHamlet;
//import com.rmsi.mast.studio.domain.SocialTenureRelationship;
//import com.rmsi.mast.studio.domain.SourceDocument;
import com.rmsi.mast.studio.mobile.dao.AttributeOptionsDao;
import com.rmsi.mast.studio.mobile.dao.AttributeValuesDao;
import com.rmsi.mast.studio.mobile.dao.NaturalPersonDao;
import com.rmsi.mast.studio.mobile.dao.NonNaturalPersonDao;
import com.rmsi.mast.studio.mobile.dao.PersonDao;
import com.rmsi.mast.studio.mobile.dao.SocialTenureDao;
import com.rmsi.mast.studio.mobile.dao.SourceDocumentDao;
import com.rmsi.mast.studio.mobile.dao.SurveyProjectAttributeDao;
import com.rmsi.mast.studio.mobile.dao.hibernate.SocialTenureHibernateDao;
import com.rmsi.mast.studio.mobile.dao.hibernate.SpatialUnitHibernateDao;
import com.rmsi.mast.studio.mobile.service.SurveyProjectAttributeService;
import com.rmsi.mast.viewer.dao.SpatialUnitDeceasedPersonDao;
import com.rmsi.mast.studio.domain.Surveyprojectattribute;
import com.rmsi.mast.studio.domain.fetch.AttributeValue;
import com.rmsi.mast.studio.domain.fetch.ClaimBasic;
import com.rmsi.mast.studio.domain.fetch.DisputeBasic;
import com.rmsi.mast.studio.domain.fetch.MediaBasic;
import com.rmsi.mast.studio.domain.fetch.NaturalPersonBasic;
import com.rmsi.mast.studio.domain.fetch.NonNaturalPersonBasic;
//import com.rmsi.mast.studio.domain.fetch.PersonBasic;
import com.rmsi.mast.studio.domain.fetch.PersonTypeBasic;
import com.rmsi.mast.studio.domain.fetch.PoiBasic;
//import com.rmsi.mast.studio.domain.fetch.PoiBasic;
import com.rmsi.mast.studio.domain.fetch.RightBasic;
import com.rmsi.mast.studio.mobile.dao.SpatialUnitPersonWithInterestDao;
import com.rmsi.mast.studio.mobile.transferobjects.Attribute;
import com.rmsi.mast.studio.mobile.transferobjects.DeceasedPerson;
//import com.rmsi.mast.studio.mobile.transferobjects.DeceasedPerson;
import com.rmsi.mast.studio.mobile.transferobjects.Media;
import com.rmsi.mast.studio.mobile.transferobjects.PersonOfInterest;
//import com.rmsi.mast.studio.mobile.transferobjects.PersonOfInterest;
import com.rmsi.mast.studio.mobile.transferobjects.Property;
import com.rmsi.mast.studio.mobile.transferobjects.Right;
import com.rmsi.mast.studio.util.StringUtils;

import java.text.SimpleDateFormat;
//import java.util.Calendar;

@Service
public class SurveyProjectAttributeServiceImp implements
        SurveyProjectAttributeService {

    @Autowired
    SurveyProjectAttributeDao attributes;

    @Autowired
    AttributeOptionsDao attributeOptions;

    @Autowired
    NaturalPersonDao naturalPersonDao;

//    @Autowired
//    SocialTenureDao socailTenure;
    @Autowired
    AttributeMasterDAO attributeMasterDao;

    @Autowired
    SpatialUnitHibernateDao spatialUnitiHibernateDao;

//    @Autowired
//    SocialTenureHibernateDao tenureDao;
    @Autowired
    PersonDao personDao;

    @Autowired
    AttributeValuesDao attributeValuesDao;

    @Autowired
    NonNaturalPersonDao nonNaturalPersonDao;

    @Autowired
    SourceDocumentDao sourceDocumentDao;

    @Autowired
    ProjectAdjudicatorDAO projectAdjudicatorDAO;

    @Autowired
    ProjectHamletDAO projectHamletDAO;

    @Autowired
    SpatialUnitPersonWithInterestDao spatialUnitPersonWithInterestDao;

    @Autowired
    SpatialUnitDeceasedPersonDao spatialUnitDeceasedPersonDao;

    @Autowired
    DisputeDao disputeDao;

    @Autowired
    ProjectDAO projectDao;

    private static final Logger logger = Logger
            .getLogger(SurveyProjectAttributeServiceImp.class.getName());

    @Override
    public List<AttributeMaster> getSurveyAttributesByProjectId(Project projectId) {
        List<AttributeMaster> attributeMasterList = attributes.getSurveyAttributes(projectId);
        try {
            Iterator<AttributeMaster> surveyProjectAttribItr = attributeMasterList.iterator();
            while (surveyProjectAttribItr.hasNext()) {
                AttributeMaster attributeMaster = surveyProjectAttribItr.next();
                if (attributeMaster.getLaExtAttributedatatype().getDatatype().equalsIgnoreCase("dropdown")) {
                    attributeMaster.setOptions(attributeOptions.getAttributeOptions(attributeMaster.getAttributemasterid()));
                }
            }
        } catch (Exception ex) {
            logger.error("Exception", ex);
            System.out.println("Exception ::: " + ex);
        }
        return attributeMasterList;
    }

    @Override
    public List<Property> getProperties(String projectId, int statusId) {
        List<Property> props = new ArrayList<>();
        List<ClaimBasic> claims;
        Project project_id = projectDao.findByName(projectId);

        if (statusId > 0) {
            claims = spatialUnitiHibernateDao.getClaimsBasicByStatus(project_id.getProjectnameid(), statusId);
        } else {
            claims = spatialUnitiHibernateDao.getClaimsBasicByProject(project_id.getProjectnameid());
        }

        try {
            if (claims != null && claims.size() > 0) {
                SimpleDateFormat dfDateAndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
                SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");

                for (ClaimBasic claim : claims) {
                    long usin = claim.getLandid();

                    Property prop = new Property();
                    
                    prop.setCoordinates(StringUtils.empty(claim.getCoordinates()));
                    
                    if (claim.getModifieddate() != null) {
                        prop.setCompletionDate(dfDateAndTime.format(claim.getModifieddate()));
                    }
                    if(claim.getLaSpatialunitgroupHierarchy5() != null){
                        prop.setVillageId(claim.getLaSpatialunitgroupHierarchy5().getHierarchyid());
                    } else {
                        prop.setVillageId(null);
                    }
                    prop.setGeomType(claim.getGeometrytype());
                    prop.setId(usin);
                    prop.setUkaNumber(StringUtils.empty(claim.getLandno()));
                    prop.setServerId(usin);
                    prop.setOtherUse(claim.getOther_use());
                    //prop.setStatus(claim.get.getRights().get(0).getLaExtTransactiondetail().getLaExtApplicationstatus().getApplicationstatus_en());
                    prop.setUserId(claim.getCreatedby());
                    if (claim.getCreateddate() != null) {
                        prop.setSurveyDate(dfDate.format(claim.getCreateddate()));
                    }

                    // Property attributes
                    prop.setAttributes(new ArrayList<Attribute>());
                    fillAttributes(prop.getAttributes(), claim.getAttributes());

                    // Right
                    Right propRight = null;

                    if (claim.getRights() != null && claim.getRights().size() > 0) {
                        for (RightBasic right : claim.getRights()) {
                            if (!right.getIsactive()) {
                                continue;
                            }

                            if (propRight == null) {
                                propRight = new Right();
                                if (right.getCertIssueDate() != null) {
                                    propRight.setCertDate(dfDate.format(right.getCertIssueDate()));
                                }
                                if(right.getShareTypeId() != null){
                                    propRight.setShareTypeId(right.getShareTypeId());
                                }
                                propRight.setCertNumber(StringUtils.empty(right.getCertNumber()));
                                propRight.setCertTypeId(right.getCertTypeid());
                                propRight.setId((long) right.getPersonlandid());
                                propRight.setAttributes(new ArrayList<Attribute>());
                                propRight.setNaturalPersons(new ArrayList<com.rmsi.mast.studio.mobile.transferobjects.Person>());
                                fillAttributes(propRight.getAttributes(), right.getAttributes());

                                prop.setRight(propRight);
                            }

                            // Persons
                            PersonType person = right.getLaParty().getLaPartygroupPersontype();
                            NaturalPerson naturalPerson = null;

                            // Non natural person
                            if (person.getPersontypeid() == 2) {
                                NonNaturalPersonBasic nonPerson = new NonNaturalPersonBasic();
                                com.rmsi.mast.studio.mobile.transferobjects.Person propNonPerson = new com.rmsi.mast.studio.mobile.transferobjects.Person();
                                propNonPerson.setIsNatural(0);
                                if (!StringUtils.isEmpty(person.getPersontypeEn())) {
//                                propNonPerson.setId(Long.parseLong(person.getMobileGroupId()));
                                } else {
                                    propNonPerson.setId(person.getPersontypeid().longValue());
                                }
                                propNonPerson.setAttributes(new ArrayList<Attribute>());
                                fillAttributes(propNonPerson.getAttributes(), nonPerson.getAttributes());
                                propRight.setNonNaturalPerson(propNonPerson);

                            } else if (person.getPersontypeid() == 1) {
                                try {
                                    naturalPerson = new NaturalPerson();
                                } catch (Exception e) {
                                    logger.error(e);
                                }
                            }

                            if (naturalPerson != null) {
                                propRight.getNaturalPersons().add(createPropPerson(naturalPerson));
                            }
                        }
                    }

                    // POIs
                    if (claim.getPois() != null && claim.getPois().size() > 0) {
                        prop.setPersonOfInterests(new ArrayList<PersonOfInterest>());

                        for (PoiBasic poi : claim.getPois()) {
                            PersonOfInterest propPoi = new PersonOfInterest();
                            if (poi.getDob() != null) {
                                propPoi.setDob(dfDate.format(poi.getDob()));
                            }
                            if (poi.getGenderId() != null) {
                                propPoi.setGenderId(poi.getGenderId());
                            }
                            propPoi.setId(poi.getId());
                            propPoi.setFirstName(StringUtils.empty(poi.getFirstName()));
                            propPoi.setMiddleName(StringUtils.empty(poi.getMiddleName()));
                            propPoi.setLastName(StringUtils.empty(poi.getLastName()));
                            propPoi.setAddress(StringUtils.empty(poi.getAddress()));
                            propPoi.setIdNumber(StringUtils.empty(poi.getIdNumber()));
                            if (poi.getRelationshipTypeId() != null) {
                                propPoi.setRelationshipId(poi.getRelationshipTypeId());
                            }
                            prop.getPersonOfInterests().add(propPoi);
                        }
                    }

                    // Deceased person
                    if (claim.getDeceased() != null && claim.getDeceased().size() > 0) {
                        DeceasedPerson deceasedPerson = new DeceasedPerson();
                        deceasedPerson.setId(claim.getDeceased().get(0).getPartyid());
                        deceasedPerson.setFirstName(StringUtils.empty(claim.getDeceased().get(0).getFirstname()));
                        deceasedPerson.setLastName(StringUtils.empty(claim.getDeceased().get(0).getLastname()));
                        deceasedPerson.setMiddleName(StringUtils.empty(claim.getDeceased().get(0).getMiddlename()));
                        prop.setDeceasedPerson(deceasedPerson);
                    }

                    // Property media
                    if (claim.getMedia() != null && claim.getMedia().size() > 0) {
                        prop.setMedia(new ArrayList<Media>());

                        for (MediaBasic doc : claim.getMedia()) {
                            if (null != doc.getLaParty()) {
                                if (doc.getLaParty().getLaPartygroupPersontype().getPersontypeid() != null) {
                                    Media media = new Media();
                                    media.setId((long) doc.getDocumentid());
                                    media.setAttributes(new ArrayList<Attribute>());
                                    fillAttributes(media.getAttributes(), doc.getAttributes());
                                    prop.getMedia().add(media);
                                }
                            }
                        }
                    }

                    // Dispute. If dispue found set claim type to disputed
                    if (claim.getDisputes() != null && claim.getDisputes().size() > 0) {
                        for (DisputeBasic dispute : claim.getDisputes()) {
                            if (dispute.getIsactive()) {
                                // Make sure property is disputed. set dispute type
                                prop.setClaimTypeCode("dispute");

                                // Add dispute information
                                com.rmsi.mast.studio.mobile.transferobjects.Dispute propDispute = new com.rmsi.mast.studio.mobile.transferobjects.Dispute();

                                if (null != dispute.getLaExtTransactiondetail()) {
                                    propDispute.setDescription(StringUtils.empty(dispute.getLaExtTransactiondetail().getRemarks()));
                                    propDispute.setRegDate(dfDate.format(dispute.getLaExtTransactiondetail().getCreateddate()));
                                }

                                if (null != dispute.getLaExtDisputetype().getDisputetypeid()) {
                                    propDispute.setDisputeTypeId(dispute.getLaExtDisputetype().getDisputetypeid().intValue());
                                }
                                propDispute.setId(dispute.getDisputelandid().longValue());

                                // Add media
                                if (claim.getMedia() != null && claim.getMedia().size() > 0) {
                                    propDispute.setMedia(new ArrayList<Media>());

                                    for (MediaBasic doc : claim.getMedia()) {
                                        if (null != doc.getLaParty()) {
                                            if (doc.getIsactive() && doc.getLaParty().getLaPartygroupPersontype().getPersontypeid() != null) {
                                                Media media = new Media();
                                                media.setId((long) doc.getDocumentid());
                                                media.setAttributes(new ArrayList<Attribute>());
                                                fillAttributes(media.getAttributes(), doc.getAttributes());
                                                propDispute.getMedia().add(media);
                                            }
                                        }
                                    }
                                }

                                prop.setDispute(propDispute);
                                break;
                            }
                        }
                    }

                    props.add(prop);
                }
            }
            return props;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    private com.rmsi.mast.studio.mobile.transferobjects.Person createPropPerson(NaturalPerson naturalPerson) {
        com.rmsi.mast.studio.mobile.transferobjects.Person propPerson = new com.rmsi.mast.studio.mobile.transferobjects.Person();
        propPerson.setIsNatural(1);
        if (!StringUtils.isEmpty(naturalPerson.getContactno())) {
            propPerson.setId(Long.parseLong(naturalPerson.getContactno()));
        } else {
            propPerson.setId(naturalPerson.getPartyid());
        }
//        if (naturalPerson.getLaParty().getLaExtPersonlandmappings().get(0).getLaSpatialunitLand().getLaRightAcquisitiontype() != null) {
//            propPerson.setAcquisitionTypeId(naturalPerson.getLaParty().getLaExtPersonlandmappings().get(0).getLaSpatialunitLand().getLaRightAcquisitiontype().getAcquisitiontypeid());
//        }
//        propPerson.setShare(StringUtils.empty(naturalPerson.getLaParty().getLaExtPersonlandmappings().get(0).getLaSpatialunitLand().getLaRightLandsharetype().getLandsharetype()));
//        if (naturalPerson.getLaParty().getLaPartygroupPersontype() != null) {
//            propPerson.setSubTypeId(naturalPerson.getLaParty().getLaPartygroupPersontype().getPersontypeid().intValue());
//        }
//        propPerson.setResident(naturalPerson.isResident() ? 1 : 0);
        propPerson.setAttributes(new ArrayList<Attribute>());
//        fillAttributes(propPerson.getAttributes(), naturalPerson.getAttributes());
        return propPerson;
    }

    private void fillAttributes(List<Attribute> attributes, List<? extends AttributeValue> sourceAttributes) {
        if (attributes == null || sourceAttributes == null || sourceAttributes.size() < 1) {
            return;
        }
        for (AttributeValue attribute : sourceAttributes) {
            Attribute attr = new Attribute();
            attr.setId(attribute.getSurveyprojectattributesid().longValue());
            attr.setValue(attribute.getAttributevalue());
            attributes.add(attr);
        }
    }

    @Override
    public Long getSurveyProjectAttributeId(long attributeId, String projectId) {
        Surveyprojectattribute attribute = attributes.getSurveyProjectAttributeId(attributeId, projectId);
        if (attribute != null) {
            return attribute.getUid();
        } else {
            return null;
        }
    }

    @Override
    public List<Surveyprojectattribute> getSurveyProjectAttributes(String projectId) {
        return attributes.getSurveyProjectAttributes(projectId);
    }

    @Override
    public List<ProjectAdjudicator> getProjectAdjudicatorByProjectId(String projectId) {
        return projectAdjudicatorDAO.findByProject(projectId);
    }

    @Override
    public List<ProjectHamlet> getProjectHamletsByProjectId(String projectId) {
        return projectHamletDAO.findHamlets(projectId);
    }
}
