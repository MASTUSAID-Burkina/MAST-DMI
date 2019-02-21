package com.rmsi.mast.viewer.web.mvc;

import com.ibm.icu.util.Calendar;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.rmsi.mast.studio.dao.DocumentTypeDao;
import com.rmsi.mast.studio.dao.GroupTypeDAO;
import com.rmsi.mast.studio.dao.OutputformatDAO;
import com.rmsi.mast.studio.dao.ProjectDAO;
import com.rmsi.mast.studio.dao.SocialTenureRelationshipDAO;
import com.rmsi.mast.studio.dao.SourceDocumentDAO;
import com.rmsi.mast.studio.domain.DocumentType;
import com.rmsi.mast.studio.domain.Gender;
import com.rmsi.mast.studio.domain.GroupType;
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
import com.rmsi.mast.studio.domain.LaSurrenderLease;
import com.rmsi.mast.studio.domain.LaSurrenderMortgage;
import com.rmsi.mast.studio.domain.La_Month;
import com.rmsi.mast.studio.domain.LandType;
import com.rmsi.mast.studio.domain.LandUseType;
import com.rmsi.mast.studio.domain.MaritalStatus;
import com.rmsi.mast.studio.domain.NaturalPerson;
import com.rmsi.mast.studio.domain.NonNaturalPerson;
import com.rmsi.mast.studio.domain.Permission;
import com.rmsi.mast.studio.domain.PersonType;
import com.rmsi.mast.studio.domain.Project;
import com.rmsi.mast.studio.domain.ProjectRegion;
import com.rmsi.mast.studio.domain.RelationshipType;
import com.rmsi.mast.studio.domain.ShareType;
import com.rmsi.mast.studio.domain.SocialTenureRelationship;
import com.rmsi.mast.studio.domain.SourceDocument;
import com.rmsi.mast.studio.domain.SpatialUnitPersonWithInterest;
import com.rmsi.mast.studio.domain.Status;
import com.rmsi.mast.studio.domain.User;
import com.rmsi.mast.studio.domain.fetch.PoiReport;
import com.rmsi.mast.studio.mobile.dao.LandUseTypeDao;
import com.rmsi.mast.studio.mobile.dao.SpatialUnitPersonWithInterestDao;
import com.rmsi.mast.studio.service.UserService;
import com.rmsi.mast.studio.util.FileUtils;
import com.rmsi.mast.studio.util.StringUtils;
import com.rmsi.mast.viewer.dao.LaExtTransactiondetailDao;
import com.rmsi.mast.viewer.dao.LaLeaseDao;
import com.rmsi.mast.viewer.dao.LaMortgageDao;
import com.rmsi.mast.viewer.dao.LaMortgageSurrenderDao;
import com.rmsi.mast.viewer.dao.LaPartyDao;
import com.rmsi.mast.viewer.dao.LaSurrenderLeaseDao;
import com.rmsi.mast.viewer.dao.SourceDocumentsDao;
import com.rmsi.mast.viewer.service.LaExtRegistrationLandShareTypeService;
import com.rmsi.mast.viewer.service.LandRecordsService;
import com.rmsi.mast.viewer.service.RegistrationRecordsService;

@Controller
public class RegistrationRecordsController {

    Logger logger = Logger.getLogger(RegistrationRecordsController.class);

    @Autowired
    RegistrationRecordsService regRecordsService;

    @Autowired
    UserService userService;

    @Autowired
    ProjectDAO projectDAO;

    @Autowired
    DocumentTypeDao documentTypeDao;

    @Autowired
    LaLeaseDao laLeaseDao;

    @Autowired
    private LandRecordsService landRecordsService;

    @Autowired
    LaPartyDao laPartyDao;

    @Autowired
    LaMortgageDao laMortgagedao;

    @Autowired
    LaMortgageSurrenderDao lasurrenderMortgagedao;

    @Autowired
    LaExtTransactiondetailDao transactionDao;

    @Autowired
    SourceDocumentDAO sourceDocumentDAO;

    @Autowired
    SourceDocumentsDao sourceDocumentsDao;

    @Autowired
    SocialTenureRelationshipDAO socialTenureRelationshipDAO;

    @Autowired
    OutputformatDAO Outputformatdao;

    @Autowired
    LaSurrenderLeaseDao laSurrenderLeaseDao;

    @Autowired
    LandUseTypeDao landusetypedao;

    @Autowired
    LaMortgageSurrenderDao laMortgageSurrenderDao;

    @Autowired
    SpatialUnitPersonWithInterestDao spatialUnitPersonWithInterestDao;

    @Autowired
    LaExtRegistrationLandShareTypeService laExtRegistrationLandShareTypeservice;

    @Autowired
    private GroupTypeDAO groupTypeDAO;
    
    @RequestMapping(value = "/viewer/registryrecords/spatialunit/{project}/{startfrom}/{lang}", method = RequestMethod.GET)
    @ResponseBody
    public List<LaSpatialunitLand> spatialUnitList(@PathVariable String project, @PathVariable String lang, @PathVariable Integer startfrom) {
        if (project.equalsIgnoreCase("default")) {
            return null;
        } else {
            Project objproject = projectDAO.findByName(project);
            Integer projectId = objproject.getProjectnameid();
            int workflowId = -1;

            return landRecordsService.search(lang, 0, projectId, "", 0, "", "", "", "", 0, 7, workflowId, startfrom);
        }
    }

    @RequestMapping(value = "/viewer/registryrecords/spatialunitcount/{project}", method = RequestMethod.GET)
    @ResponseBody
    public Integer spatialUnitCount(@PathVariable String project) {
        if (project.equalsIgnoreCase("default")) {
            return null;
        } else {
            Project objproject = projectDAO.findByName(project);
            Integer projectId = objproject.getProjectnameid();
            int workflowId = -1;

            return landRecordsService.searchCount(0, projectId, "", 0, "", "", "", "", 0, workflowId, 7);
        }
    }

    @RequestMapping(value = "/viewer/registration/partydetails/lease/{landid}", method = RequestMethod.GET)
    @ResponseBody
    public LaExtPersonLandMapping getPersonLandMapDetails(
            @PathVariable Integer landid) {

        return regRecordsService.getPersonLandMapDetails(landid);
    }

    @RequestMapping(value = "/viewer/registration/partydetails/sale/{landid}", method = RequestMethod.GET)
    @ResponseBody
    public Object getAllPartyPersonDetails(@PathVariable Integer landid) {

        return regRecordsService.getAllPartyPersonDetails(landid);
    }

    @RequestMapping(value = "/viewer/registration/editpartydetails/{transid}", method = RequestMethod.GET)
    @ResponseBody
    public List<LaPartyPerson> getAllPartyPersonDetailsByTransactionId(@PathVariable Integer transid) {

        return regRecordsService.getAllPartyPersonDetailsByTransactionId(transid);
    }

    @RequestMapping(value = "/viewer/registration/partydetails/filldetails/{landid}/{processid}", method = RequestMethod.GET)
    @ResponseBody
    public List<LaPartyPerson> fillAllPartyPersonDetails(@PathVariable Integer landid, @PathVariable Integer processid) {

        return regRecordsService.fillAllPartyPersonDetails(landid, processid);
    }

    @RequestMapping(value = "/viewer/registration/partypersondetailssurrenderlease/{landid}", method = RequestMethod.GET)
    @ResponseBody
    public LaPartyPerson getPartyPersonDetailssurrenderlease(@PathVariable Integer landid) {

        return regRecordsService.getPartyPersonDetailssurrenderlease(landid);
    }

    @RequestMapping(value = "/viewer/registration/getleasebylandid/{landid}", method = RequestMethod.GET)
    @ResponseBody
    public LaLease getLeaseByLandId(@PathVariable Integer landid) {
        return regRecordsService.getLeaseByLandId(landid);
    }

    @RequestMapping(value = "/viewer/registration/checkforactivelease/{landid}/{processid}", method = RequestMethod.GET)
    @ResponseBody
    public boolean checkForActiveLease(@PathVariable Integer landid, @PathVariable Integer processid) {
        return regRecordsService.checkForActiveLease(landid, processid);
    }

    @RequestMapping(value = "/viewer/registration/getleasebytransactionid/{transid}", method = RequestMethod.GET)
    @ResponseBody
    public LaLease getLeaseByTransactionId(@PathVariable Integer landid, @PathVariable Integer transid) {
        return regRecordsService.getLeaseByTransactionId(transid);
    }

    // MaritalStatusDao
    @RequestMapping(value = "/viewer/registration/maritalstatus/", method = RequestMethod.GET)
    @ResponseBody
    public List<MaritalStatus> getMaritalStatusDetails() {

        return regRecordsService.getMaritalStatusDetails();
    }

    @RequestMapping(value = "/viewer/registration/genderstatus/", method = RequestMethod.GET)
    @ResponseBody
    public List<Gender> getGenderDetails() {

        return regRecordsService.getGenderDetails();
    }

    @RequestMapping(value = "/viewer/registration/idtype/", method = RequestMethod.GET)
    @ResponseBody
    public List<IdType> getIDTypeDetails() {

        return regRecordsService.getIDTypeDetails();
    }

    @RequestMapping(value = "/viewer/registration/laspatialunitland/{landid}", method = RequestMethod.GET)
    @ResponseBody
    public LaSpatialunitLand getLaSpatialunitLandDetails(@PathVariable Long landid) {
        LaSpatialunitLand su = regRecordsService.getLaSpatialunitLandDetails(landid);
        if (su != null) {
            if (StringUtils.isNotEmpty(su.getLandusetypeid())) {
                List<LandUseType> existingList = landRecordsService.getExistingUseName(su.getLandusetypeid());
                if (existingList != null && existingList.size() > 0) {
                    String landUseName = "";
                    for (LandUseType lut : existingList) {
                        if (landUseName.equals("")) {
                            landUseName = lut.getLandusetype();
                        } else {
                            landUseName += ", " + lut.getLandusetype();
                        }
                    }
                    su.setFirstname(landUseName);
                    su.setLandusetype_en(landUseName);
                }
            }
        }
        return su;
    }

    @RequestMapping(value = "/viewer/registration/laMortgage/{landid}", method = RequestMethod.GET)
    @ResponseBody
    public LaMortgage getLaMortgage(@PathVariable Long landid) {

        return laMortgagedao.getMortgageByLandId(landid);
    }

    @RequestMapping(value = "/viewer/registration/editlaMortgage/{landid}/{transid}", method = RequestMethod.GET)
    @ResponseBody
    public LaMortgage editLaMortgage(@PathVariable Long landid, @PathVariable Long transid) {

        return laMortgagedao.getMortgageByLandandTransactionId(landid, transid.intValue());
    }

    @RequestMapping(value = "/viewer/registration/relationshiptypes/", method = RequestMethod.GET)
    @ResponseBody
    public List<RelationshipType> getAllRelationshipTypes() {
        return landRecordsService.findAllRelationshipTypes();
    }

    @RequestMapping(value = "/viewer/registration/landtype/", method = RequestMethod.GET)
    @ResponseBody
    public List<LandType> getLandTypeDetails() {

        return regRecordsService.getAllLandType();
    }

    @RequestMapping(value = "/viewer/registration/allcountry/", method = RequestMethod.GET)
    @ResponseBody
    public List<ProjectRegion> getAllCountry() {

        return regRecordsService.getAllCountry();
    }

    @RequestMapping(value = "/viewer/registration/allregion/{country_r_id}", method = RequestMethod.GET)
    @ResponseBody
    public List<ProjectRegion> getAllRegion(@PathVariable Integer country_r_id) {

        return regRecordsService.getAllRegion(country_r_id);
    }

    @RequestMapping(value = "/viewer/registration/allprovince/{region_r_id}", method = RequestMethod.GET)
    @ResponseBody
    public List<ProjectRegion> getAllProvience(@PathVariable Integer region_r_id) {

        return regRecordsService.getAllProvience(region_r_id);
    }

    //
    @RequestMapping(value = "/viewer/registration/landsharetypes/", method = RequestMethod.GET)
    @ResponseBody
    public List<ShareType> getAlllandsharetypes() {

        return regRecordsService.getAlllandsharetype();
    }

    @RequestMapping(value = "/viewer/registration/land/sharetype/", method = RequestMethod.GET)
    @ResponseBody
    public List<ShareType> getAlllandsharetype() {

        return regRecordsService.getAlllandsharetype();
    }

    @RequestMapping(value = "/viewer/registration/processdetails/", method = RequestMethod.GET)
    @ResponseBody
    public List<LaExtProcess> getAllProcessDetails() {

        return regRecordsService.getAllProcessDetails();
    }

    @RequestMapping(value = "/viewer/registration/monthoflease/", method = RequestMethod.GET)
    @ResponseBody
    public List<La_Month> getmonthofleaseDetails() {

        return regRecordsService.getmonthofleaseDetails();
    }

    @RequestMapping(value = "/viewer/registration/financialagency/", method = RequestMethod.GET)
    @ResponseBody
    public List<LaExtFinancialagency> getFinancialagencyDetails() {

        return regRecordsService.getFinancialagencyDetails();
    }

    @RequestMapping(value = "/viewer/registration/savefinalsaledata", method = RequestMethod.POST)
    @ResponseBody
    public String savefinalsaledata(HttpServletRequest request, HttpServletResponse response, Principal principal) {
        Long landId = 0L;
        String username = principal.getName();
        User userObj = userService.findByUniqueName(username);

        Long user_id = userObj.getId();
        String sellerpartyids = "";
        String buyerpartyids = "";

        try {
            Long processid = ServletRequestUtils.getRequiredLongParameter(request, "registration_process");
            landId = ServletRequestUtils.getRequiredLongParameter(request, "landidhide");
            Integer editflag = ServletRequestUtils.getRequiredIntParameter(request, "editflag");
            int mutationType = ServletRequestUtils.getIntParameter(request, "sale_transfer_type", 0);
            String contractName = ServletRequestUtils.getStringParameter(request, "sale_contract_name", null);
            String contractDate = ServletRequestUtils.getStringParameter(request, "sale_contract_date", null);
            int ownershipType = ServletRequestUtils.getIntParameter(request, "sale_ownership_type", 0);

            Date dtContractDate = null;
            String contractNum = ServletRequestUtils.getStringParameter(request, "sale_contract_num", null);

            if (StringUtils.isNotEmpty(contractDate)) {
                DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    dtContractDate = dateformat.parse(contractDate);
                } catch (ParseException ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }

            List<SocialTenureRelationship> sellerRights = regRecordsService.getSocialTenureRelationshipListForSellerByLandId(landId);
            Long sellerPartyId = 0L;

            if (sellerRights.size() > 0 && (processid == 2 || processid == 4 || processid == 6)) {
                for (SocialTenureRelationship right : sellerRights) {
                    sellerPartyId = right.getPartyid();

                    if (sellerpartyids.equalsIgnoreCase("")) {
                        sellerpartyids = sellerPartyId.toString();
                    } else {
                        sellerpartyids = sellerpartyids + "," + sellerPartyId.toString();
                    }
                    if (editflag == 0) {
                        regRecordsService.updateSocialTenureRelationshipByPartyId(sellerPartyId, landId);
                    }
                }
            }

            List<SocialTenureRelationship> buyerRights = regRecordsService.getSocialTenureRelationshipListByLandIdForBuyer(landId, processid);
            Long buyerPartyId = 0L;
            if (null != buyerRights && buyerRights.size() > 0) {
                for (SocialTenureRelationship right : buyerRights) {
                    buyerPartyId = right.getPartyid();

                    if (buyerpartyids.equalsIgnoreCase("")) {
                        buyerpartyids = buyerPartyId.toString();
                    } else {
                        buyerpartyids = buyerpartyids + "," + buyerPartyId.toString();
                    }
                    if (editflag == 0) {
                        regRecordsService.updateSocialTenureRelationshipByPartytypeId(
                                buyerPartyId, landId, mutationType, contractName, contractNum, dtContractDate, ownershipType);
                    }
                }
            }

            if (null != buyerRights && editflag == 0) {
                LaExtTransactionHistory latranshist = new LaExtTransactionHistory();
                latranshist.setOldownerid(sellerpartyids);
                latranshist.setNewownerid(buyerpartyids);
                latranshist.setLandid(landId);
                latranshist.setIsactive(true);
                latranshist.setTransactionid(buyerRights.get(0).getLaExtTransactiondetail().getTransactionid());
                latranshist.setCreatedby(user_id.intValue());
                latranshist.setCreateddate(new Date());
                latranshist = regRecordsService.saveTransactionHistory(latranshist);
            }

            return "Success";
        } catch (ServletRequestBindingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/viewer/registration/saveprocessdata", method = RequestMethod.POST)
    @ResponseBody
    public String saveProcessData(HttpServletRequest request, HttpServletResponse response, Principal principal) {
        Long landId = 0L;
        Long buyerRelationShipId = 0L;
        String username = principal.getName();
        User userObj = userService.findByUniqueName(username);

        Long user_id = userObj.getId();
        /*if(landId == 0){
			return true;
		}*/
        try {
            String buyerFirstName = ServletRequestUtils.getRequiredStringParameter(request, "firstname_r_sale1");
            String buyerMiddleName = ServletRequestUtils.getRequiredStringParameter(request, "middlename_r_sale1");
            String buyerLastName = ServletRequestUtils.getRequiredStringParameter(request, "lastname_r_sale1");
            int buyerIdTypeid = ServletRequestUtils.getRequiredIntParameter(request, "sale_idtype_buyer");
            String buyerId = ServletRequestUtils.getRequiredStringParameter(request, "id_r1");
            String buyerContact_No = ServletRequestUtils.getRequiredStringParameter(request, "contact_no1");
            int buyerGenderId = ServletRequestUtils.getRequiredIntParameter(request, "sale_gender_buyer");
            String buyerAddress = ServletRequestUtils.getRequiredStringParameter(request, "address_sale1");
            String buyerDOBstr = ServletRequestUtils.getRequiredStringParameter(request, "date_Of_birth_sale1");
            int buyerMaritalStatusId = ServletRequestUtils.getRequiredIntParameter(request, "sale_marital_buyer");
            String buyerRemarks = ServletRequestUtils.getRequiredStringParameter(request, "remrks_sale");

            Long processid = ServletRequestUtils.getRequiredLongParameter(request, "registration_process");

            if (processid == 6) {
                buyerRelationShipId = ServletRequestUtils.getRequiredLongParameter(request, "sale_relation_buyer");
            }

            landId = ServletRequestUtils.getRequiredLongParameter(request, "landidhide");
            Status status = regRecordsService.getStatusById(2);

            PersonType personType = regRecordsService.getPersonTypeById(1);

            DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
            /*Date date = null;
			long time = 0;*/
            Date buyerDOB = null;
            try {
                buyerDOB = dateformat.parse(buyerDOBstr);
                /*date = dateformat.parse(new Date().toString());
				time = date.getTime();*/

            } catch (ParseException e) {
                e.printStackTrace();
            }
            MaritalStatus maritalStatus = regRecordsService.getMaritalStatusByID(buyerMaritalStatusId);
            IdType idType = regRecordsService.getIDTypeDetailsByID(buyerIdTypeid);
            NaturalPerson naturalPerson = new NaturalPerson();
            naturalPerson.setContactno(buyerContact_No);
            naturalPerson.setCreatedby(user_id.intValue());
            naturalPerson.setCreateddate(new Date());
            naturalPerson.setDateofbirth(buyerDOB);
            naturalPerson.setFirstname(buyerFirstName);
            naturalPerson.setMiddlename(buyerMiddleName);
            naturalPerson.setLastname(buyerLastName);
            naturalPerson.setIsactive(true);
            naturalPerson.setGenderid(buyerGenderId);
            naturalPerson.setLaPartygroupMaritalstatus(maritalStatus);
            naturalPerson.setAddress(buyerAddress);
            naturalPerson.setLaPartygroupIdentitytype(idType);
            naturalPerson.setIdentityno(buyerId);

            if (processid == 6) {
                RelationshipType obj = new RelationshipType();
                obj.setRelationshiptypeid(buyerRelationShipId);
                naturalPerson.setLaPartygroupRelationshiptype(obj);
            }

            naturalPerson.setLaSpatialunitgroup1(regRecordsService.findLaSpatialunitgroupById(1));
            naturalPerson.setLaSpatialunitgroup2(regRecordsService.findLaSpatialunitgroupById(2));
            naturalPerson.setLaSpatialunitgroup3(regRecordsService.findLaSpatialunitgroupById(3));
            naturalPerson.setLaSpatialunitgroupHierarchy1(regRecordsService.findProjectRegionById(1));
            naturalPerson.setLaSpatialunitgroupHierarchy2(regRecordsService.findProjectRegionById(2));
            naturalPerson.setLaSpatialunitgroupHierarchy3(regRecordsService.findProjectRegionById(3));

            LaParty laParty = new LaParty();
            laParty.setCreatedby(user_id.intValue());
            laParty.setCreateddate(new Date());
            laParty.setLaPartygroupPersontype(personType);

            Long partyId = 0l;
            SocialTenureRelationship socialTenureRelationshipSellerDetails = regRecordsService.getSocialTenureRelationshipByLandId(landId);
            Long sellerPartyId = 0L;
            if (socialTenureRelationshipSellerDetails != null) {
                sellerPartyId = socialTenureRelationshipSellerDetails.getPartyid();
            } else {
                return null;
            }

            try {
                naturalPerson.setLaPartygroupPersontype(personType);
                NaturalPerson naturalPerson2 = regRecordsService.saveNaturalPerson(naturalPerson);
                partyId = naturalPerson2.getPartyid();
            } catch (Exception er) {
                er.printStackTrace();
                return null;
            }
            SocialTenureRelationship socialTenureRelationship = new SocialTenureRelationship();
            socialTenureRelationship.setCreatedby(user_id.intValue());
            socialTenureRelationship.setPartyid(partyId);
            socialTenureRelationship.setLaPartygroupPersontype(personType);
            socialTenureRelationship.setCreateddate(new Date());
            socialTenureRelationship.setIsactive(true);
            socialTenureRelationship.setLandid(landId);

            LaExtTransactiondetail laExtTransactiondetail = new LaExtTransactiondetail();
            laExtTransactiondetail.setCreatedby(user_id.intValue());
            laExtTransactiondetail.setCreateddate(new Date());
            laExtTransactiondetail.setIsactive(true);
            laExtTransactiondetail.setLaExtApplicationstatus(status);
            laExtTransactiondetail.setModuletransid(partyId.intValue());
            laExtTransactiondetail.setRemarks(buyerRemarks);
            //laExtTransactiondetail.setProcessid(2l);
            laExtTransactiondetail.setProcessid(processid);
            // laExtTransactiondetail.setLaExtPersonlandmappings(lstSocialTenureRelationships);

            socialTenureRelationship.setLaExtTransactiondetail(laExtTransactiondetail);

            try {
                // registrationRecordsService.saveTransaction(laExtTransactiondetail);
                /*
				 * List<LaSpatialunitLand> lstLaSpatialunitLand =
				 * registrationRecordsService
				 * .getLaSpatialunitLandDetails(landId);
                 */
                socialTenureRelationship = regRecordsService.saveSocialTenureRelationship(socialTenureRelationship);
                regRecordsService.updateSocialTenureRelationshipByPartyId(sellerPartyId, landId);
                /*
				 * registrationRecordsService.updateLaSpatialunitLand(
				 * lstLaSpatialunitLand.get(0));
				 * registrationRecordsService.addLaSpatialunitLand
				 * (lstLaSpatialunitLand.get(0));
                 */

            } catch (Exception er) {
                er.printStackTrace();
            }
            return socialTenureRelationship.getLaExtTransactiondetail().getTransactionid() + "";
        } catch (ServletRequestBindingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/viewer/registration/savebuyerdata", method = RequestMethod.POST)
    @ResponseBody
    public String saveBuyerdata(HttpServletRequest request, HttpServletResponse response, Principal principal) {
        Long landId = 0L;
        String username = principal.getName();
        User userObj = userService.findByUniqueName(username);

        Long user_id = userObj.getId();

        try {
            landId = ServletRequestUtils.getRequiredLongParameter(request, "landidhide");
            int buyerId = ServletRequestUtils.getRequiredIntParameter(request, "personid");
            Long processid = ServletRequestUtils.getRequiredLongParameter(request, "registration_process");
            String buyerFirstName = ServletRequestUtils.getRequiredStringParameter(request, "firstname_r_sale1");
            String buyerMiddleName = ServletRequestUtils.getRequiredStringParameter(request, "middlename_r_sale1");
            String buyerLastName = ServletRequestUtils.getRequiredStringParameter(request, "lastname_r_sale1");
            String buyerPlaceOfBirth = ServletRequestUtils.getRequiredStringParameter(request, "place_of_birth_sale1");
            String buyerProfession = ServletRequestUtils.getRequiredStringParameter(request, "profession_sale1");
            String buyerIdDate = ServletRequestUtils.getRequiredStringParameter(request, "id_date_sale1");
            String buyerFather = ServletRequestUtils.getRequiredStringParameter(request, "father_name_sale1");
            String buyerMother = ServletRequestUtils.getRequiredStringParameter(request, "mother_name_sale1");
            int buyerNopId = ServletRequestUtils.getRequiredIntParameter(request, "nop_sale1");
            String buyerMandateDate = ServletRequestUtils.getRequiredStringParameter(request, "mandate_date_sale1");
            String buyerMandateLoc = ServletRequestUtils.getRequiredStringParameter(request, "mandate_loc_sale1");
            String buyerIdNumber = ServletRequestUtils.getRequiredStringParameter(request, "id_r1");
            int buyerGenderId = ServletRequestUtils.getRequiredIntParameter(request, "sale_gender_buyer");
            String buyerAddress = ServletRequestUtils.getRequiredStringParameter(request, "address_sale1");
            String buyerDOBstr = ServletRequestUtils.getRequiredStringParameter(request, "date_Of_birth_sale1");
            int buyerMaritalStatusId = ServletRequestUtils.getRequiredIntParameter(request, "sale_marital_buyer");

            Status status = regRecordsService.getStatusById(7); // Final
            PersonType personType = regRecordsService.getPersonTypeById(11);
            DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");

            Date buyerDOB = null;
            Date dtBuyerIdDate = null;
            Date dtBuyerMandateDate = null;

            try {
                if (StringUtils.isNotEmpty(buyerDOBstr)) {
                    buyerDOB = dateformat.parse(buyerDOBstr);
                }
                if (StringUtils.isNotEmpty(buyerIdDate)) {
                    dtBuyerIdDate = dateformat.parse(buyerIdDate);
                }
                if (StringUtils.isNotEmpty(buyerMandateDate)) {
                    dtBuyerMandateDate = dateformat.parse(buyerMandateDate);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            MaritalStatus maritalStatus = regRecordsService.getMaritalStatusByID(buyerMaritalStatusId);
            SocialTenureRelationship rightBuyer = regRecordsService.getSocialTenureRelationshipByLandIdForBuyer(landId, processid);
            Long buyerPartyId = ((Integer) buyerId).longValue();
            NaturalPerson person = new NaturalPerson();

            if (buyerPartyId > 0) {
                person = (NaturalPerson) laPartyDao.getPartyIdByID(buyerPartyId);
            }

            person.setDateofbirth(buyerDOB);
            person.setFirstname(buyerFirstName);
            person.setMiddlename(buyerMiddleName);
            person.setLastname(buyerLastName);
            person.setIsactive(true);
            person.setGenderid(buyerGenderId);
            person.setLaPartygroupMaritalstatus(maritalStatus);
            person.setAddress(buyerAddress);
            person.setIdentityno(buyerIdNumber);
            person.setOwnertype(2);
            if (null != rightBuyer && (processid == 4 || processid == 2)) {
                person.setOwnertype(2);
            } else {
                person.setOwnertype(1);
            }
            person.setLaSpatialunitgroup1(regRecordsService.findLaSpatialunitgroupById(1));
            person.setLaSpatialunitgroup2(regRecordsService.findLaSpatialunitgroupById(2));
            person.setLaSpatialunitgroup3(regRecordsService.findLaSpatialunitgroupById(3));
            person.setLaSpatialunitgroupHierarchy1(regRecordsService.findProjectRegionById(1));
            person.setLaSpatialunitgroupHierarchy2(regRecordsService.findProjectRegionById(2));
            person.setLaSpatialunitgroupHierarchy3(regRecordsService.findProjectRegionById(3));
            person.setLaPartygroupPersontype(personType);
            if (buyerNopId > 0) {
                person.setNopId(buyerNopId);
            }
            person.setIdCardDate(dtBuyerIdDate);
            person.setFathername(buyerFather);
            person.setMothername(buyerMother);
            person.setMandateDate(dtBuyerMandateDate);
            person.setMandateLocation(buyerMandateLoc);
            person.setBirthPlace(buyerPlaceOfBirth);
            person.setProfession(buyerProfession);

            if (buyerPartyId == 0L) {
                person.setCreatedby(user_id.intValue());
                person.setCreateddate(new Date());

                Long partyId = 0l;
                SocialTenureRelationship sellerRight = regRecordsService.getSocialTenureRelationshipByLandId(landId);
                if (sellerRight == null) {
                    return null;
                }

                NaturalPerson person2 = regRecordsService.saveNaturalPerson(person);
                partyId = person2.getPartyid();

                SocialTenureRelationship newRight = new SocialTenureRelationship();
                newRight.setShareTypeId(sellerRight.getShareTypeId());
                newRight.setCreatedby(user_id.intValue());
                newRight.setPartyid(partyId);
                newRight.setLaPartygroupPersontype(personType);
                newRight.setCreateddate(new Date());
                newRight.setIsactive(true);
                newRight.setLandid(landId);

                LaExtTransactiondetail laExtTransactiondetail = new LaExtTransactiondetail();
                laExtTransactiondetail.setCreatedby(user_id.intValue());
                laExtTransactiondetail.setCreateddate(new Date());
                laExtTransactiondetail.setIsactive(true);
                laExtTransactiondetail.setLaExtApplicationstatus(status);
                laExtTransactiondetail.setModuletransid(partyId.intValue());
                laExtTransactiondetail.setProcessid(processid);

                newRight.setLaExtTransactiondetail(laExtTransactiondetail);

                newRight = regRecordsService.saveSocialTenureRelationship(newRight);
                return newRight.getLaExtTransactiondetail().getTransactionid() + "";

            } else {

                person.setModifiedby(user_id.intValue());
                person.setModifieddate(new Date());

                regRecordsService.getSocialTenureRelationshipByLandId(landId);
                regRecordsService.saveNaturalPerson(person);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/viewer/registration/savelegalbuyer", method = RequestMethod.POST)
    @ResponseBody
    public String saveLegalBuyer(HttpServletRequest request, HttpServletResponse response, Principal principal) {
        Long landId = 0L;
        String username = principal.getName();
        User userObj = userService.findByUniqueName(username);

        Long user_id = userObj.getId();

        try {
            landId = ServletRequestUtils.getRequiredLongParameter(request, "landidhide");
            int buyerId = ServletRequestUtils.getRequiredIntParameter(request, "personid");
            Long processid = ServletRequestUtils.getRequiredLongParameter(request, "registration_process");
            
            String leName = ServletRequestUtils.getRequiredStringParameter(request, "txtLegalBuyerName");
            int leType = ServletRequestUtils.getRequiredIntParameter(request, "cbxLegalBuyerType");
            String leRegNum = ServletRequestUtils.getRequiredStringParameter(request, "txtLegalBuyerRegNum");
            String leEstDate = ServletRequestUtils.getRequiredStringParameter(request, "txtLegalBuyerEstDate");
            String leAddress = ServletRequestUtils.getRequiredStringParameter(request, "txtLegalBuyerAddress");
            String leRepName = ServletRequestUtils.getRequiredStringParameter(request, "txtLegalBuyerRepName");
            String leRepPhone = ServletRequestUtils.getRequiredStringParameter(request, "txtLegalBuyerRepPhone");

            Status status = regRecordsService.getStatusById(7); // Final
            PersonType personType = regRecordsService.getPersonTypeById(11);
            GroupType gt = groupTypeDAO.getGroupType(leType);
            
            DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");

            Date dtEstDate = null;

            try {
                if (StringUtils.isNotEmpty(leEstDate)) {
                    dtEstDate = dateformat.parse(leEstDate);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            SocialTenureRelationship rightBuyer = regRecordsService.getSocialTenureRelationshipByLandIdForBuyer(landId, processid);
            Long buyerPartyId = ((Integer) buyerId).longValue();
            NonNaturalPerson nonPerson = new NonNaturalPerson();

            if (buyerPartyId > 0) {
                nonPerson = (NonNaturalPerson) laPartyDao.getPartyIdByID(buyerPartyId);
            }

            nonPerson.setAddress(leAddress);
            nonPerson.setContactno(leRepPhone);
            nonPerson.setIdentityregistrationno(leRegNum);
            nonPerson.setGroupType(gt);
            nonPerson.setIsactive(Boolean.TRUE);
            nonPerson.setOrganizationname(leName);
            nonPerson.setLaPartygroupPersontype(personType);
            nonPerson.setRegdate(dtEstDate);
            nonPerson.setRepname(leRepName);
            
            nonPerson.setLaSpatialunitgroup1(regRecordsService.findLaSpatialunitgroupById(1));
            nonPerson.setLaSpatialunitgroup2(regRecordsService.findLaSpatialunitgroupById(2));
            nonPerson.setLaSpatialunitgroup3(regRecordsService.findLaSpatialunitgroupById(3));
            nonPerson.setLaSpatialunitgroupHierarchy1(regRecordsService.findProjectRegionById(1));
            nonPerson.setLaSpatialunitgroupHierarchy2(regRecordsService.findProjectRegionById(2));
            nonPerson.setLaSpatialunitgroupHierarchy3(regRecordsService.findProjectRegionById(3));
            
            if (buyerPartyId == 0L) {
                nonPerson.setCreatedby(user_id.intValue());
                nonPerson.setCreateddate(new Date());

                Long partyId = 0l;
                SocialTenureRelationship sellerRight = regRecordsService.getSocialTenureRelationshipByLandId(landId);
                if (sellerRight == null) {
                    return null;
                }

                NonNaturalPerson nonPerson2 = regRecordsService.saveNonNaturalPerson(nonPerson);
                partyId = nonPerson2.getPartyid();

                SocialTenureRelationship newRight = new SocialTenureRelationship();
                newRight.setShareTypeId(sellerRight.getShareTypeId());
                newRight.setCreatedby(user_id.intValue());
                newRight.setPartyid(partyId);
                newRight.setLaPartygroupPersontype(personType);
                newRight.setCreateddate(new Date());
                newRight.setIsactive(true);
                newRight.setLandid(landId);

                LaExtTransactiondetail laExtTransactiondetail = new LaExtTransactiondetail();
                laExtTransactiondetail.setCreatedby(user_id.intValue());
                laExtTransactiondetail.setCreateddate(new Date());
                laExtTransactiondetail.setIsactive(true);
                laExtTransactiondetail.setLaExtApplicationstatus(status);
                laExtTransactiondetail.setModuletransid(partyId.intValue());
                laExtTransactiondetail.setProcessid(processid);

                newRight.setLaExtTransactiondetail(laExtTransactiondetail);

                newRight = regRecordsService.saveSocialTenureRelationship(newRight);
                return newRight.getLaExtTransactiondetail().getTransactionid() + "";

            } else {
                nonPerson.setModifiedby(user_id.intValue());
                nonPerson.setModifieddate(new Date());
                regRecordsService.saveNonNaturalPerson(nonPerson);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @RequestMapping(value = "/viewer/registration/saveLeaseeDetails/{processId}", method = RequestMethod.POST)
    @ResponseBody
    public String saveLeaseeDetails(HttpServletRequest request, @PathVariable long processId, HttpServletResponse response, Principal principal) {

        try {
            Long landId;
            LaLease lease = null;
            NaturalPerson person = new NaturalPerson();
            List<LaLease> leaseeobjList = null;
            LaExtTransactiondetail laExtTransactiondetail = null;

            String firstName = ServletRequestUtils.getRequiredStringParameter(request, "firstname_r_lease");
            String middleName = ServletRequestUtils.getRequiredStringParameter(request, "middlename_r_lease");
            String lastName = ServletRequestUtils.getRequiredStringParameter(request, "lastname_r_lease");
            String placeOfBirth = ServletRequestUtils.getRequiredStringParameter(request, "place_of_birth_lease");
            String profession = ServletRequestUtils.getRequiredStringParameter(request, "profession_lease");
            String idDate = ServletRequestUtils.getRequiredStringParameter(request, "id_date_lease");
            String father = ServletRequestUtils.getRequiredStringParameter(request, "father_name_lease");
            String mother = ServletRequestUtils.getRequiredStringParameter(request, "mother_name_lease");
            int nopId = ServletRequestUtils.getRequiredIntParameter(request, "nop_lease");
            String mandateDate = ServletRequestUtils.getRequiredStringParameter(request, "mandate_date_lease");
            String mandateLoc = ServletRequestUtils.getRequiredStringParameter(request, "mandate_loc_lease");
            String idNumber = ServletRequestUtils.getRequiredStringParameter(request, "id_lease");
            int genderId = ServletRequestUtils.getRequiredIntParameter(request, "gender_lease");
            String address = ServletRequestUtils.getRequiredStringParameter(request, "address_lease");
            String dobstr = ServletRequestUtils.getRequiredStringParameter(request, "date_Of_birth_lease");
            int maritalStatusId = ServletRequestUtils.getRequiredIntParameter(request, "marital_lease");
            Integer personId = ServletRequestUtils.getIntParameter(request, "leaseeperson", 0);
            Integer editflag = ServletRequestUtils.getRequiredIntParameter(request, "editflag");

            landId = ServletRequestUtils.getRequiredLongParameter(request, "landidhide");
            Status status = regRecordsService.getStatusById(1);
            PersonType personType = regRecordsService.getPersonTypeById(1);

            DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateOfBirth = null;
            Date dtIdDate = null;
            Date dtMandateDate = null;

            try {
                if (StringUtils.isNotEmpty(idDate)) {
                    dateOfBirth = dateformat.parse(dobstr);
                }
                if (StringUtils.isNotEmpty(idDate)) {
                    dtIdDate = dateformat.parse(idDate);
                }
                if (StringUtils.isNotEmpty(mandateDate)) {
                    dtMandateDate = dateformat.parse(mandateDate);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            User userObj = userService.findByUniqueName(principal.getName());
            Long user_id = userObj.getId();

            MaritalStatus maritalStatus = regRecordsService.getMaritalStatusByID(maritalStatusId);

            if (editflag == 1 || personId != 0) {
                leaseeobjList = laLeaseDao.getleaseeListByLandandPersonId(landId, (long) personId);
            } else if (editflag == 0 && personId == 0) {
                leaseeobjList = new ArrayList<>();
                LaLease l = laLeaseDao.getLeaseByLandId(landId);
                if (l != null) {
                    leaseeobjList.add(l);
                }
            }

            if (personId > 0) {
                person = (NaturalPerson) laPartyDao.getPartyIdByID((long) personId);
            }

            person.setDateofbirth(dateOfBirth);
            person.setFirstname(firstName);
            person.setMiddlename(middleName);
            person.setLastname(lastName);
            person.setIsactive(true);
            person.setGenderid(genderId);
            person.setLaPartygroupMaritalstatus(maritalStatus);
            person.setAddress(address);
            person.setIdentityno(idNumber);
            person.setOwnertype(2);
            person.setLaSpatialunitgroup1(regRecordsService.findLaSpatialunitgroupById(1));
            person.setLaSpatialunitgroup2(regRecordsService.findLaSpatialunitgroupById(2));
            person.setLaSpatialunitgroup3(regRecordsService.findLaSpatialunitgroupById(3));
            person.setLaSpatialunitgroupHierarchy1(regRecordsService.findProjectRegionById(1));
            person.setLaSpatialunitgroupHierarchy2(regRecordsService.findProjectRegionById(2));
            person.setLaSpatialunitgroupHierarchy3(regRecordsService.findProjectRegionById(3));
            person.setLaPartygroupPersontype(personType);
            if (nopId > 0) {
                person.setNopId(nopId);
            }
            person.setIdCardDate(dtIdDate);
            person.setFathername(father);
            person.setMothername(mother);
            person.setMandateDate(dtMandateDate);
            person.setMandateLocation(mandateLoc);
            person.setBirthPlace(placeOfBirth);
            person.setProfession(profession);

            if (leaseeobjList.size() > 0 && personId != 0) {
                lease = leaseeobjList.get(0);
                person.setModifiedby(user_id.intValue());
                person.setModifieddate(Calendar.getInstance().getTime());
                regRecordsService.saveNaturalPerson(person);
                transactionDao.getLaExtTransactionByLeaseeid(lease.getLeaseid().longValue());
            } else if (leaseeobjList.size() < 1 && personId == 0) {
                person.setCreatedby(user_id.intValue());
                person.setCreateddate(new Date());
                person = regRecordsService.saveNaturalPerson(person);

                Long ownerid = 0L;
                SocialTenureRelationship ownerRight = regRecordsService.getSocialTenureRelationshipByLandId(landId);

                if (ownerRight != null) {
                    ownerid = ownerRight.getPartyid();
                } else {
                    return null;
                }

                lease = new LaLease();
                lease.setCreatedby(user_id.intValue());
                lease.setCreateddate(new Date());
                lease.setIsactive(true);
                lease.setPersonid(person.getPartyid());
                lease.setLandid(landId);
                lease.setOwnerid(ownerid);
                lease.setIndividual(Boolean.TRUE);
                lease = regRecordsService.saveLease(lease);

                laExtTransactiondetail = new LaExtTransactiondetail();
                laExtTransactiondetail.setCreatedby(user_id.intValue());
                laExtTransactiondetail.setCreateddate(new Date());
                laExtTransactiondetail.setIsactive(true);
                laExtTransactiondetail.setLaExtApplicationstatus(status);
                laExtTransactiondetail.setProcessid(processId);
                laExtTransactiondetail.setModuletransid(lease.getLeaseid());

                regRecordsService.saveTransaction(laExtTransactiondetail);
            }

            if (lease != null) {
                return lease.getLeaseid().toString();
            } else {
                return null;
            }

        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @RequestMapping(value = "/viewer/registration/getsurrenderlease/{landId}", method = RequestMethod.GET)
    @ResponseBody
    public LaSurrenderLease getSurrenderLease(HttpServletRequest request, @PathVariable int landId, HttpServletResponse response, Principal principal) {
        try {
            return laSurrenderLeaseDao.getSurrenderleaseByLandandProcessId((long) landId, 5L);
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }

    @RequestMapping(value = "/viewer/registration/savesurrenderleasedata", method = RequestMethod.POST)
    @ResponseBody
    public String saveSurrenderLeasedata(HttpServletRequest request, HttpServletResponse response, Principal principal) {
        LaExtTransactiondetail laExtTransactiondetail = null;
        LaSurrenderLease surrenderLease = null;
        Integer transactionId = 0;
        try {
            Integer leaseId = ServletRequestUtils.getIntParameter(request, "leaseid", 0);
            Integer editflag = ServletRequestUtils.getRequiredIntParameter(request, "editflag");
            if (editflag == 1) {
                transactionId = ServletRequestUtils.getRequiredIntParameter(request, "transactionId");
            }

            LaLease lease = laLeaseDao.getLeaseById(leaseId);

            if (lease == null) {
                return null;
            }

            Status status = regRecordsService.getStatusById(1);
            String username = principal.getName();
            User userObj = userService.findByUniqueName(username);
            Long user_id = userObj.getId();

            if (editflag == 0) {
                surrenderLease = laSurrenderLeaseDao.getSurrenderleaseByLandandProcessId(lease.getLandid(), 5L);
            } else if (editflag == 1) {
                surrenderLease = laSurrenderLeaseDao.getSurrenderleaseByLandandTransId(lease.getLandid(), transactionId);
            }

            if (surrenderLease == null) {
                surrenderLease = new LaSurrenderLease();
                surrenderLease.setCreatedby(user_id.intValue());
                surrenderLease.setCreateddate(new Date());
                surrenderLease.setIsactive(true);
                surrenderLease.setMonths(lease.getMonths());
                surrenderLease.setLeaseamount(lease.getLeaseamount());
                surrenderLease.setYears(lease.getYears());
                surrenderLease.setPersonid(lease.getPersonid());
                surrenderLease.setLandid(lease.getLandid());
                surrenderLease.setOwnerid(lease.getOwnerid());;
                surrenderLease.setLeasestartdate(lease.getLeasestartdate());
                surrenderLease.setLeaseenddate(lease.getLeaseenddate());

                surrenderLease = regRecordsService.savesurrenderLease(surrenderLease);

                laExtTransactiondetail = new LaExtTransactiondetail();
                laExtTransactiondetail.setCreatedby(user_id.intValue());
                laExtTransactiondetail.setCreateddate(new Date());
                laExtTransactiondetail.setIsactive(true);
                laExtTransactiondetail.setLaExtApplicationstatus(status);
                laExtTransactiondetail.setProcessid(5L);
                laExtTransactiondetail.setModuletransid(surrenderLease.getLeaseid());

                laExtTransactiondetail = regRecordsService.saveTransaction(laExtTransactiondetail);
            }

            return surrenderLease.getLeaseid().toString();
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }

    @RequestMapping(value = "/viewer/registration/savemortgagedata", method = RequestMethod.POST)
    @ResponseBody
    public String saveMortgageData(HttpServletRequest request, HttpServletResponse response, Principal principal) {

        try {
            Long landId = 0L;
            LaMortgage laMortgage = null;
            LaExtFinancialagency laExtFinancialagency = null;
            LaExtTransactiondetail laExtTransactiondetail = null;
            String username = principal.getName();
            User userObj = userService.findByUniqueName(username);
            Integer transactionId = 0;
            Long user_id = userObj.getId();

            int financial_AgenciesID = ServletRequestUtils.getRequiredIntParameter(request, "mortgage_Financial_Agencies");
            String mortgage_from = ServletRequestUtils.getRequiredStringParameter(request, "mortgage_from");
            String mortgage_to = ServletRequestUtils.getRequiredStringParameter(request, "mortgage_to");
            Double amount_mortgage = ServletRequestUtils.getRequiredDoubleParameter(request, "amount_mortgage");
            String remrks_mortgage = ServletRequestUtils.getRequiredStringParameter(request, "remrks_mortgage");
            Integer editflag = ServletRequestUtils.getRequiredIntParameter(request, "editflag");
            if (editflag == 1) {
                transactionId = ServletRequestUtils.getRequiredIntParameter(request, "transactionId");
            }

            landId = ServletRequestUtils.getRequiredLongParameter(request, "landidhide");

            Status status = regRecordsService.getStatusById(1);

//			PersonType personType = registrationRecordsService.getPersonTypeById(1);
            DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
            //Date date = null;
            Date mortgage_fromDate = null;
            Date mortgage_toDate = null;
            //long time = 0;
            try {
                mortgage_fromDate = dateformat.parse(mortgage_from);
                mortgage_toDate = dateformat.parse(mortgage_to);

                //date = dateformat.parse(new Date().toString());
                //time = date.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (editflag == 0) {
                laMortgage = laMortgagedao.getMortgageByLandId(landId);
            } else if (editflag == 1) {
                laMortgage = laMortgagedao.getMortgageByLandandTransactionId(landId, transactionId);

            }

            if (null != laMortgage && editflag == 0) {
                laMortgage.setMortgagefrom(mortgage_fromDate);
                laMortgage.setMortgageto(mortgage_toDate);
                laMortgage.setMortgageamount(amount_mortgage);
                laExtFinancialagency = regRecordsService.getFinancialagencyByID(financial_AgenciesID);
                laMortgage.setLaExtFinancialagency(laExtFinancialagency);
                laMortgage = regRecordsService.saveMortgage(laMortgage);
                laExtTransactiondetail = transactionDao.getLaExtTransactionByLeaseeid(laMortgage.getMortgageid().longValue());
                laExtTransactiondetail.setLaExtApplicationstatus(status);
                laExtTransactiondetail = regRecordsService.saveTransaction(laExtTransactiondetail);

            } else if (null != laMortgage && editflag == 1) {

                laMortgage.setMortgagefrom(mortgage_fromDate);
                laMortgage.setMortgageto(mortgage_toDate);
                laMortgage.setMortgageamount(amount_mortgage);
                laExtFinancialagency = regRecordsService.getFinancialagencyByID(financial_AgenciesID);
                laMortgage.setLaExtFinancialagency(laExtFinancialagency);
                laMortgage = regRecordsService.saveMortgage(laMortgage);
                /*laExtTransactiondetail = transactionDao.getpoiByLeaseeid(laMortgage.getMortgageid().longValue());
				 laExtTransactiondetail.setLaExtApplicationstatus(status);
				 laExtTransactiondetail = registrationRecordsService.saveTransaction(laExtTransactiondetail);*/

            } else if (null == laMortgage) {
                Long ownerid = 0L;
                try {
                    SocialTenureRelationship socialTenureRelationshipSellerDetails = regRecordsService.getSocialTenureRelationshipByLandId(landId);

                    if (socialTenureRelationshipSellerDetails != null) {
                        ownerid = socialTenureRelationshipSellerDetails.getPartyid();
                    } else {
                        return null;
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

//			SocialTenureRelationship socialTenureRelationshipSellerDetails = registrationRecordsService.getSocialTenureRelationshipByLandId(landId);
                laExtFinancialagency = regRecordsService.getFinancialagencyByID(financial_AgenciesID);

                laExtTransactiondetail = new LaExtTransactiondetail();
                laExtTransactiondetail.setCreatedby(user_id.intValue());
                laExtTransactiondetail.setCreateddate(new Date());//new Timestamp(time)
                laExtTransactiondetail.setIsactive(true);
                laExtTransactiondetail.setLaExtApplicationstatus(status);

                laExtTransactiondetail.setRemarks(remrks_mortgage);
                laExtTransactiondetail.setProcessid(3L);// process Id 3 for Mortgage

                laMortgage = new LaMortgage();
                laMortgage.setCreatedby(user_id.intValue());
                laMortgage.setCreateddate(new Date());
                laMortgage.setIsactive(true);
                //laMortgage.setLaExtTransactiondetail(laExtTransactiondetail);
                laMortgage.setMortgageamount(amount_mortgage);
                laMortgage.setMortgagefrom(mortgage_fromDate);
                laMortgage.setMortgageto(mortgage_toDate);
                laMortgage.setLaExtFinancialagency(laExtFinancialagency);
                laMortgage.setLandid(landId);
                laMortgage.setOwnerid(ownerid);

                laMortgage = regRecordsService.saveMortgage(laMortgage);

                laExtTransactiondetail.setModuletransid(laMortgage.getMortgageid());

                laExtTransactiondetail = regRecordsService.saveTransaction(laExtTransactiondetail);
            }
            if (null != laExtTransactiondetail) {

                return laExtTransactiondetail.getTransactionid().toString();
            } else {
                return "Success";
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return null;

    }

    @RequestMapping(value = "/viewer/registration/savedocument", method = RequestMethod.POST)
    @ResponseBody
    public boolean savedocument(MultipartHttpServletRequest request, HttpServletResponse response, Principal principal) {
        try {
            Long landId = 0L;

            String username = principal.getName();
            User userObj = userService.findByUniqueName(username);
            Long user_id = userObj.getId();

            Iterator<String> file = request.getFileNames();

            landId = ServletRequestUtils.getRequiredLongParameter(request, "selectedlandid");

            String doc_name_sale = ServletRequestUtils.getRequiredStringParameter(request, "doc_name_sale");
            String doc_desc_sale = ServletRequestUtils.getRequiredStringParameter(request, "doc_desc_sale");
            String doc_date_sale = ServletRequestUtils.getRequiredStringParameter(request, "doc_date_sale");
            Long doc_typeid = ServletRequestUtils.getRequiredLongParameter(request, "doc_Type_Sale");
            String transactionid = ServletRequestUtils.getRequiredStringParameter(request, "transactionid");

            byte[] document = null;
            while (file.hasNext()) {
                String fileName = file.next();
                MultipartFile mpFile = request.getFile(fileName);
                String originalFileName = mpFile.getOriginalFilename();
                //String fileExtension = originalFileName.substring(originalFileName.indexOf(".") + 1, originalFileName.length()).toLowerCase();

                if (!"".equals(originalFileName)) {
                    document = mpFile.getBytes();
                    originalFileName = originalFileName.substring(0, originalFileName.indexOf(".")) + "_" + transactionid + originalFileName.substring(originalFileName.indexOf("."), originalFileName.length());
                }
                DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                Date uploadDate = null;
                try {
                    uploadDate = dateformat.parse(doc_date_sale);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String outDirPath = FileUtils.getFielsFolder(request) + "resources" + File.separator + "documents" + File.separator + "Registry" + File.separator + "webupload";
                File outDir = new File(outDirPath);
                boolean exists = outDir.exists();

                if (!exists) {
                    (new File(outDirPath)).mkdirs();
                }

                String uploadFileName = ("resources/documents/" + "Registry" + "/webupload");

                SourceDocument sourceDocument = new SourceDocument();
                sourceDocument.setCreatedby(user_id.intValue());
                sourceDocument.setCreateddate(new Date());
                sourceDocument.setDocumentlocation(uploadFileName + File.separator + originalFileName);
//				sourceDocument.setDocumentname(originalFileName);
                sourceDocument.setDocumentname(doc_name_sale);
                sourceDocument.setIsactive(true);
                sourceDocument.setRemarks(doc_desc_sale);
                sourceDocument.setRecordationdate(uploadDate);
                sourceDocument.setDocumenttypeid(doc_typeid.intValue());
                try {
                    //List<SpatialUnit> lstSpatialUnit = registrationRecordsService.getSpatialUnitLandMappingDetails(landId);
                    SocialTenureRelationship socialTenureRelationship = regRecordsService.getSocialTenureRelationshipByLandId(landId);
                    Long partyId = socialTenureRelationship.getPartyid();
                    //Integer transactionid1 = socialTenureRelationship.getLaExtTransactiondetail().getTransactionid();
                    LaParty laParty = regRecordsService.getLaPartyById(partyId);
                    LaExtTransactiondetail laExtTransactiondetail = regRecordsService.getLaExtTransactiondetail(Integer.parseInt(transactionid));
                    sourceDocument.setLaParty(laParty);
                    sourceDocument.setLaExtTransactiondetail(laExtTransactiondetail);
                    sourceDocument.setLaSpatialunitLand(landId);
                    sourceDocument = regRecordsService.saveUploadedDocuments(sourceDocument);

                } catch (Exception e) {
                    logger.error(e);
                    return false;
                }
                try {
                    FileOutputStream uploadfile = new FileOutputStream(outDirPath + File.separator + originalFileName);
                    uploadfile.write(document);
                    uploadfile.flush();
                    uploadfile.close();
                } catch (Exception e) {
                    logger.error(e);
                    return false;
                }

            }
        } catch (Exception e) {
            logger.error(e);
            return false;
        }
        return false;
    }

    @RequestMapping(value = "/viewer/registration/savedocumentmortgage", method = RequestMethod.POST)
    @ResponseBody
    public boolean savedocumentMortgage(MultipartHttpServletRequest request, HttpServletResponse response, Principal principal) {

        try {
            Long landId = 0L;
            String username = principal.getName();
            User userObj = userService.findByUniqueName(username);
            Long user_id = userObj.getId();
            Iterator<String> file = request.getFileNames();

            landId = ServletRequestUtils.getRequiredLongParameter(request, "selectedlandid");

            String doc_name_mortgage = ServletRequestUtils.getRequiredStringParameter(request, "doc_name_mortgage");
            String doc_desc_mortgage = ServletRequestUtils.getRequiredStringParameter(request, "doc_desc_mortgage");
            String doc_date_mortgage = ServletRequestUtils.getRequiredStringParameter(request, "doc_date_mortgage");
            Long doc_typeid = ServletRequestUtils.getRequiredLongParameter(request, "doc_Type_Mortgage");

            String transactionid = ServletRequestUtils.getRequiredStringParameter(request, "transactionid");

            byte[] document = null;
            while (file.hasNext()) {
                String fileName = file.next();
                MultipartFile mpFile = request.getFile(fileName);
                String originalFileName = mpFile.getOriginalFilename();
//				String fileExtension = originalFileName.substring(originalFileName.indexOf(".") + 1, originalFileName.length()).toLowerCase();

                if (!"".equals(originalFileName)) {
                    document = mpFile.getBytes();
                    originalFileName = originalFileName.substring(0, originalFileName.indexOf(".")) + "_" + transactionid + originalFileName.substring(originalFileName.indexOf("."), originalFileName.length());
                }
                DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                Date uploadDate = null;
                try {
                    uploadDate = dateformat.parse(doc_date_mortgage);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String outDirPath = FileUtils.getFielsFolder(request) + "resources" + File.separator + "documents" + File.separator + "Registry" + File.separator + "webupload";
                File outDir = new File(outDirPath);
                boolean exists = outDir.exists();

                if (!exists) {
                    (new File(outDirPath)).mkdirs();
                }

                String uploadFileName = ("resources/documents/" + "Registry" + "/webupload");

                SourceDocument sourceDocument = new SourceDocument();
                sourceDocument.setCreatedby(user_id.intValue());
                sourceDocument.setCreateddate(new Date());
                sourceDocument.setDocumentlocation(uploadFileName + File.separator + originalFileName);
//				sourceDocument.setDocumentname(originalFileName);
                sourceDocument.setDocumentname(doc_name_mortgage);
                sourceDocument.setIsactive(true);
                sourceDocument.setRemarks(doc_desc_mortgage);
                sourceDocument.setRecordationdate(uploadDate);
                sourceDocument.setDocumenttypeid(doc_typeid.intValue());
                try {
//					List<SpatialUnit> lstSpatialUnit = registrationRecordsService.getSpatialUnitLandMappingDetails(landId);
                    SocialTenureRelationship socialTenureRelationship = regRecordsService.getSocialTenureRelationshipByLandId(landId);
                    Long partyId = socialTenureRelationship.getPartyid();
                    //Integer transactionid = socialTenureRelationship.getLaExtTransactiondetail().getTransactionid();
                    LaParty laParty = regRecordsService.getLaPartyById(partyId);
                    LaExtTransactiondetail laExtTransactiondetail = regRecordsService.getLaExtTransactiondetail(Integer.parseInt(transactionid));
                    sourceDocument.setLaParty(laParty);
                    sourceDocument.setLaExtTransactiondetail(laExtTransactiondetail);
                    sourceDocument.setLaSpatialunitLand(landId);
                    //sourceDocument.setLaSpatialunitLand(lstSpatialUnit.get(0).getLandid());
                    sourceDocument = regRecordsService.saveUploadedDocuments(sourceDocument);

                } catch (Exception e) {
                    logger.error(e);
                    return false;
                }
                try {
                    FileOutputStream uploadfile = new FileOutputStream(outDirPath + File.separator + originalFileName);
                    uploadfile.write(document);
                    uploadfile.flush();
                    uploadfile.close();
                } catch (Exception e) {
                    logger.error(e);
                    return false;
                }

            }
        } catch (Exception e) {
            logger.error(e);
            return false;
        }
        return false;
    }

    @RequestMapping(value = "/viewer/registration/savedocumentlease", method = RequestMethod.POST)
    @ResponseBody
    public boolean savedocumentLease(MultipartHttpServletRequest request, HttpServletResponse response, Principal principal) {

        try {
            Long landId = 0L;
            String username = principal.getName();
            User userObj = userService.findByUniqueName(username);
            Long user_id = userObj.getId();
            Iterator<String> file = request.getFileNames();

            landId = ServletRequestUtils.getRequiredLongParameter(request, "selectedlandid");

            String doc_name_Lease = ServletRequestUtils.getRequiredStringParameter(request, "doc_name_Lease");
            String doc_desc_Lease = ServletRequestUtils.getRequiredStringParameter(request, "doc_desc_Lease");
            String doc_date = ServletRequestUtils.getRequiredStringParameter(request, "doc_date_Lease");
            Long doc_typeid = ServletRequestUtils.getRequiredLongParameter(request, "doc_Type_Lease");
            String transactionid = ServletRequestUtils.getRequiredStringParameter(request, "transactionid");

            byte[] document = null;
            while (file.hasNext()) {
                String fileName = file.next();
                MultipartFile mpFile = request.getFile(fileName);
                String originalFileName = mpFile.getOriginalFilename();
//				String fileExtension = originalFileName.substring(originalFileName.indexOf(".") + 1, originalFileName.length()).toLowerCase();

                if (!"".equals(originalFileName)) {
                    document = mpFile.getBytes();
                    originalFileName = originalFileName.substring(0, originalFileName.indexOf(".")) + "_" + transactionid + originalFileName.substring(originalFileName.indexOf("."), originalFileName.length());
                }

                String outDirPath = FileUtils.getFielsFolder(request) + "resources" + File.separator + "documents" + File.separator + "Registry" + File.separator + "webupload";
                File outDir = new File(outDirPath);
                boolean exists = outDir.exists();

                if (!exists) {
                    (new File(outDirPath)).mkdirs();
                }

                DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                Date uploadDate = null;
                try {
                    uploadDate = dateformat.parse(doc_date);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String uploadFileName = ("resources/documents/" + "Registry" + "/webupload");

                SourceDocument sourceDocument = new SourceDocument();
                sourceDocument.setCreatedby(user_id.intValue());
                sourceDocument.setCreateddate(new Date());
                sourceDocument.setDocumentlocation(uploadFileName + File.separator + originalFileName);
//				sourceDocument.setDocumentname(originalFileName);
                sourceDocument.setDocumentname(doc_name_Lease);
                sourceDocument.setIsactive(true);
                sourceDocument.setRemarks(doc_desc_Lease);
                sourceDocument.setRecordationdate(uploadDate);
                sourceDocument.setDocumenttypeid(doc_typeid.intValue());
                try {
//					List<SpatialUnit> lstSpatialUnit = registrationRecordsService.getSpatialUnitLandMappingDetails(landId);
                    /*SocialTenureRelationship socialTenureRelationship = registrationRecordsService.getSocialTenureRelationshipByLandId(landId);
					Long partyId = socialTenureRelationship.getPartyid();*/
//					Integer transactionid = socialTenureRelationship.getLaExtTransactiondetail().getTransactionid();

                    LaExtTransactiondetail laExtTransactiondetail = regRecordsService.getLaExtTransactiondetail(Integer.parseInt(transactionid));
                    Integer leaseid = laExtTransactiondetail.getModuletransid();
                    LaLease laLease = laLeaseDao.getLeaseById(leaseid);
                    LaParty laParty = regRecordsService.getLaPartyById(laLease.getPersonid());
                    sourceDocument.setLaParty(laParty);
                    sourceDocument.setLaExtTransactiondetail(laExtTransactiondetail);
                    sourceDocument.setLaSpatialunitLand(landId);
                    //sourceDocument.setLaSpatialunitLand(lstSpatialUnit.get(0).getLandid());
                    sourceDocument = regRecordsService.saveUploadedDocuments(sourceDocument);

                } catch (Exception e) {
                    logger.error(e);
                    return false;
                }
                try {
                    FileOutputStream uploadfile = new FileOutputStream(outDirPath + File.separator + originalFileName);
                    uploadfile.write(document);
                    uploadfile.flush();
                    uploadfile.close();
                } catch (Exception e) {
                    logger.error(e);
                    return false;
                }

            }
        } catch (Exception e) {
            logger.error(e);
            return false;
        }
        return false;
    }

    @RequestMapping(value = "/viewer/registration/savedocumentsurrenderlease", method = RequestMethod.POST)
    @ResponseBody
    public boolean savedocumentSurrenderLease(MultipartHttpServletRequest request, HttpServletResponse response, Principal principal) {

        try {
            Long landId = 0L;
            String username = principal.getName();
            User userObj = userService.findByUniqueName(username);
            Long user_id = userObj.getId();
            Iterator<String> file = request.getFileNames();

            landId = ServletRequestUtils.getRequiredLongParameter(request, "selectedlandid");

            String doc_name_Lease = ServletRequestUtils.getRequiredStringParameter(request, "doc_name_Lease");
            String doc_desc_Lease = ServletRequestUtils.getRequiredStringParameter(request, "doc_desc_Lease");
            String doc_date = ServletRequestUtils.getRequiredStringParameter(request, "doc_date_Lease");
            Long doc_typeid = ServletRequestUtils.getRequiredLongParameter(request, "doc_Type_Lease");
            String transactionid = ServletRequestUtils.getRequiredStringParameter(request, "transactionid");

            byte[] document = null;
            while (file.hasNext()) {
                String fileName = file.next();
                MultipartFile mpFile = request.getFile(fileName);
                String originalFileName = mpFile.getOriginalFilename();
//				String fileExtension = originalFileName.substring(originalFileName.indexOf(".") + 1, originalFileName.length()).toLowerCase();

                if (!"".equals(originalFileName)) {
                    document = mpFile.getBytes();
                    originalFileName = originalFileName.substring(0, originalFileName.indexOf(".")) + "_" + transactionid + originalFileName.substring(originalFileName.indexOf("."), originalFileName.length());
                }

                String outDirPath = FileUtils.getFielsFolder(request) + "resources" + File.separator + "documents" + File.separator + "Registry" + File.separator + "webupload";
                File outDir = new File(outDirPath);
                boolean exists = outDir.exists();

                if (!exists) {
                    (new File(outDirPath)).mkdirs();
                }

                DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                Date uploadDate = null;
                try {
                    uploadDate = dateformat.parse(doc_date);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String uploadFileName = ("resources/documents/" + "Registry" + "/webupload");

                SourceDocument sourceDocument = new SourceDocument();
                sourceDocument.setCreatedby(user_id.intValue());
                sourceDocument.setCreateddate(new Date());
                sourceDocument.setDocumentlocation(uploadFileName + File.separator + originalFileName);
//				sourceDocument.setDocumentname(originalFileName);
                sourceDocument.setDocumentname(doc_name_Lease);
                sourceDocument.setIsactive(true);
                sourceDocument.setRemarks(doc_desc_Lease);
                sourceDocument.setRecordationdate(uploadDate);
                sourceDocument.setDocumenttypeid(doc_typeid.intValue());
                try {
//					List<SpatialUnit> lstSpatialUnit = registrationRecordsService.getSpatialUnitLandMappingDetails(landId);
                    /*SocialTenureRelationship socialTenureRelationship = registrationRecordsService.getSocialTenureRelationshipByLandId(landId);
					Long partyId = socialTenureRelationship.getPartyid();*/
//					Integer transactionid = socialTenureRelationship.getLaExtTransactiondetail().getTransactionid();

                    LaExtTransactiondetail laExtTransactiondetail = regRecordsService.getLaExtTransactiondetail(Integer.parseInt(transactionid));
                    Integer leaseid = laExtTransactiondetail.getModuletransid();
                    LaSurrenderLease laLease = laLeaseDao.getSurrenderLeaseById(leaseid);
                    LaParty laParty = regRecordsService.getLaPartyById(laLease.getPersonid());
                    sourceDocument.setLaParty(laParty);
                    sourceDocument.setLaExtTransactiondetail(laExtTransactiondetail);
                    sourceDocument.setLaSpatialunitLand(landId);
                    //sourceDocument.setLaSpatialunitLand(lstSpatialUnit.get(0).getLandid());
                    sourceDocument = regRecordsService.saveUploadedDocuments(sourceDocument);

                } catch (Exception e) {
                    logger.error(e);
                    return false;
                }
                try {
                    FileOutputStream uploadfile = new FileOutputStream(outDirPath + File.separator + originalFileName);
                    uploadfile.write(document);
                    uploadfile.flush();
                    uploadfile.close();
                } catch (Exception e) {
                    logger.error(e);
                    return false;
                }

            }
        } catch (Exception e) {
            logger.error(e);
            return false;
        }
        return false;
    }

    @RequestMapping(value = "/viewer/registration/search1/{project}/{startfrom}", method = RequestMethod.POST)
    @ResponseBody
    public List<LaSpatialunitLand> searchUnitList(HttpServletRequest request, @PathVariable String project, @PathVariable Integer startfrom) {
        String parcelId = ServletRequestUtils.getStringParameter(request, "txt_parcel_id", "");
        int villageId = ServletRequestUtils.getIntParameter(request, "cbxRegVillage", 0);
        String apfrNum = ServletRequestUtils.getStringParameter(request, "txt_apfr_num", "");
        String firstName = ServletRequestUtils.getStringParameter(request, "txt_first_name", "");
        int appType = ServletRequestUtils.getIntParameter(request, "cbx_app_type", 0);
        String lang = ServletRequestUtils.getStringParameter(request, "lang_search_registered", "en");
        int appStatus = 7; // Final

        Integer projectId;
        int workflowId = -1;

        try {
            Project objproject = projectDAO.findByName(project);
            projectId = objproject.getProjectnameid();

            return landRecordsService.search(lang, 0, projectId, parcelId, villageId, "", "", apfrNum, firstName, appType, appStatus, workflowId, startfrom);

        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @RequestMapping(value = "/viewer/registration/search1Count/{project}", method = RequestMethod.POST)
    @ResponseBody
    public Integer searchUnitCount(HttpServletRequest request, HttpServletResponse response, @PathVariable String project) {
        String parcelId = ServletRequestUtils.getStringParameter(request, "txt_parcel_id", "");
        int villageId = ServletRequestUtils.getIntParameter(request, "cbxRegVillage", 0);
        String apfrNum = ServletRequestUtils.getStringParameter(request, "txt_apfr_num", "");
        String firstName = ServletRequestUtils.getStringParameter(request, "txt_first_name", "");
        int appType = ServletRequestUtils.getIntParameter(request, "cbx_app_type", 0);
        int appStatus = 7; // Final

        Integer projectId;
        int workflowId = -1;

        try {
            Project objproject = projectDAO.findByName(project);
            projectId = objproject.getProjectnameid();

            return landRecordsService.searchCount(0, projectId, parcelId, villageId, "", "", apfrNum, firstName, appType, workflowId, appStatus);

        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @RequestMapping(value = "/viewer/registration/doctype", method = RequestMethod.GET)
    @ResponseBody
    public List<DocumentType> getDocumentType() {

        return documentTypeDao.getAllDocumentTypes();
    }

    @RequestMapping(value = "/viewer/registration/saveleasePersondata", method = RequestMethod.POST)
    @ResponseBody
    public String saveLeasePersonData(HttpServletRequest request, HttpServletResponse response, Principal principal) {

        try {
            Long landId = 0L;
            String firstName = ServletRequestUtils.getRequiredStringParameter(request, "firstname_r_applicant");
            String middlename = ServletRequestUtils.getRequiredStringParameter(request, "middlename_r_applicant");
            String lastname = ServletRequestUtils.getRequiredStringParameter(request, "lastname_r_applicant");
            String id = ServletRequestUtils.getRequiredStringParameter(request, "id_r_applicant");
            int id_type = ServletRequestUtils.getRequiredIntParameter(request, "id_type_applicant");
            String contact_no = ServletRequestUtils.getRequiredStringParameter(request, "contact_no_applicant");
            int genderId = ServletRequestUtils.getRequiredIntParameter(request, "gender_type_applicant");
            String address = ServletRequestUtils.getRequiredStringParameter(request, "address_lease_applicant");
            String date_Of_birth = ServletRequestUtils.getRequiredStringParameter(request, "date_Of_birth_applicant");
            int martialId = ServletRequestUtils.getRequiredIntParameter(request, "martial_sts_applicant");

            String remrks_lease = ServletRequestUtils.getRequiredStringParameter(request, "remrks_lease");

            int no_Of_month_Lease = ServletRequestUtils.getRequiredIntParameter(request, "no_Of_month_Lease");
            Double lease_Amount = ServletRequestUtils.getRequiredDoubleParameter(request, "lease_Amount");
            //int no_Of_years_Lease = ServletRequestUtils.getRequiredIntParameter(request,"no_Of_years_Lease");

            landId = ServletRequestUtils.getRequiredLongParameter(request, "landidhide");
            Status status = regRecordsService.getStatusById(2);

            PersonType personType = regRecordsService.getPersonTypeById(1);

            DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateOfBirth = null;
            try {
                dateOfBirth = dateformat.parse(date_Of_birth);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String username = principal.getName();
            User userObj = userService.findByUniqueName(username);
            //int year = no_Of_month_Lease / 12;

            Long user_id = userObj.getId();

            MaritalStatus maritalStatus = regRecordsService.getMaritalStatusByID(martialId);
            IdType idType = regRecordsService.getIDTypeDetailsByID(id_type);
            NaturalPerson naturalPerson = new NaturalPerson();
            naturalPerson.setContactno(contact_no);
            naturalPerson.setCreatedby(user_id.intValue());
            naturalPerson.setCreateddate(new Date());
            naturalPerson.setDateofbirth(dateOfBirth);
            naturalPerson.setFirstname(firstName);
            naturalPerson.setMiddlename(middlename);
            naturalPerson.setLastname(lastname);
            naturalPerson.setIsactive(true);
            naturalPerson.setGenderid(genderId);
            naturalPerson.setLaPartygroupMaritalstatus(maritalStatus);
            naturalPerson.setAddress(address);
            naturalPerson.setLaPartygroupIdentitytype(idType);
            naturalPerson.setIdentityno(id);
            naturalPerson.setLaSpatialunitgroup1(regRecordsService.findLaSpatialunitgroupById(1));
            naturalPerson.setLaSpatialunitgroup2(regRecordsService.findLaSpatialunitgroupById(2));
            naturalPerson.setLaSpatialunitgroup3(regRecordsService.findLaSpatialunitgroupById(3));
            naturalPerson.setLaSpatialunitgroupHierarchy1(regRecordsService.findProjectRegionById(1));
            naturalPerson.setLaSpatialunitgroupHierarchy2(regRecordsService.findProjectRegionById(2));
            naturalPerson.setLaSpatialunitgroupHierarchy3(regRecordsService.findProjectRegionById(3));

            try {
                naturalPerson.setLaPartygroupPersontype(personType);
                naturalPerson = regRecordsService.saveNaturalPerson(naturalPerson);
            } catch (Exception er) {
                er.printStackTrace();
                return null;
            }

            Long ownerid = 0L;
            try {
                SocialTenureRelationship socialTenureRelationshipSellerDetails = regRecordsService.getSocialTenureRelationshipByLandId(landId);

                if (socialTenureRelationshipSellerDetails != null) {
                    ownerid = socialTenureRelationshipSellerDetails.getPartyid();
                } else {
                    return null;
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            LaLease laLease = new LaLease();
            laLease.setCreatedby(user_id.intValue());
            laLease.setCreateddate(new Date());
            laLease.setIsactive(true);
            laLease.setMonths(no_Of_month_Lease);
            laLease.setLeaseamount(lease_Amount);
            laLease.setYears(0);
            laLease.setPersonid(naturalPerson.getPartyid());
            laLease.setLandid(landId);
            laLease.setOwnerid(ownerid);

//			LaExtFinancialagency laExtFinancialagency = registrationRecordsService.getFinancialagencyByID(financial_AgenciesID);
            LaExtTransactiondetail laExtTransactiondetail = new LaExtTransactiondetail();
            laExtTransactiondetail.setCreatedby(user_id.intValue());
            laExtTransactiondetail.setCreateddate(new Date());//new Timestamp(time)
            laExtTransactiondetail.setIsactive(true);
            laExtTransactiondetail.setLaExtApplicationstatus(status);

            laExtTransactiondetail.setRemarks(remrks_lease);
            laExtTransactiondetail.setProcessid(1L);// process Id 1 for Lease

            laLease = regRecordsService.saveLease(laLease);

            laExtTransactiondetail.setModuletransid(laLease.getLeaseid());

            laExtTransactiondetail = regRecordsService.saveTransaction(laExtTransactiondetail);

            return laExtTransactiondetail.getTransactionid().toString();
        } catch (Exception e) {
            logger.error(e);
        }
        return null;

    }

    @RequestMapping(value = "/viewer/registration/savedocumentInfo/Registration", method = RequestMethod.POST)
    @ResponseBody
    public Integer savedocumentInfoRegistration(HttpServletRequest request, HttpServletResponse response, Principal principal) {

        SocialTenureRelationship socialTenureRelationshipBuyerDetails = null;
        List<LaLease> leaseeobjList = new ArrayList<>();
        LaExtTransactiondetail tran = null;
        LaParty laParty = null;
        LaMortgage laMortgage = null;
        LaSurrenderMortgage lasurrenderMortgage = null;
        LaSurrenderLease surenderLease = null;
        Integer transactionId = 0;
        Integer persontypeId = 0;
        Permission permission = null;

        try {
            String landId = "";
            String doc_name = "";
            String doc_desc = "";
            String doc_date = "";
            Integer processid = 0;
            Long buyertransid = 0L;

            String username = principal.getName();
            User userObj = userService.findByUniqueName(username);
            Long user_id = userObj.getId();
            landId = ServletRequestUtils.getRequiredStringParameter(request, "landidhide");
            int surrenderLeaseId = ServletRequestUtils.getIntParameter(request, "surrenderLeaseId", 0);
            processid = ServletRequestUtils.getRequiredIntParameter(request, "processidhide");
            Integer editflag = ServletRequestUtils.getRequiredIntParameter(request, "editflag");
            if (editflag == 1) {
                transactionId = ServletRequestUtils.getRequiredIntParameter(request, "transactionId");
            }

            if (processid == 1 || processid == 10) {
                persontypeId = 1;
                doc_name = ServletRequestUtils.getRequiredStringParameter(request, "doc_name_Lease");
                doc_desc = ServletRequestUtils.getRequiredStringParameter(request, "doc_desc_Lease");
                doc_date = ServletRequestUtils.getRequiredStringParameter(request, "doc_date_Lease");

                if (editflag == 0) {
                    LaLease l = laLeaseDao.getLeaseByLandId(Long.parseLong(landId));
                    if (l != null) {
                        leaseeobjList.add(l);
                    }
                } else if (editflag == 1) {
                    tran = transactionDao.getLaExtTransactiondetail(transactionId);
                    LaLease l = laLeaseDao.getLeaseById(tran.getModuletransid());
                    if (l != null) {
                        leaseeobjList.add(l);
                    }
                }

            } else if (processid == 11 || processid == 12) {
                persontypeId = 1;
                doc_name = ServletRequestUtils.getRequiredStringParameter(request, "perm_doc_name");
                doc_desc = ServletRequestUtils.getRequiredStringParameter(request, "perm_doc_desc");
                doc_date = ServletRequestUtils.getRequiredStringParameter(request, "perm_doc_date");

                int permissionId = 0;
                if (processid == 11) {
                    permissionId = ServletRequestUtils.getRequiredIntParameter(request, "permissionId");
                } else {
                    permissionId = ServletRequestUtils.getRequiredIntParameter(request, "surrenderPermissionId");
                }

                permission = regRecordsService.getPermissionById(permissionId);
            } else if (processid == 2) {
                persontypeId = 11;
                doc_name = ServletRequestUtils.getRequiredStringParameter(request, "doc_name_sale");
                doc_desc = ServletRequestUtils.getRequiredStringParameter(request, "doc_desc_sale");
                doc_date = ServletRequestUtils.getRequiredStringParameter(request, "doc_date_sale");
                if (editflag == 0) {
                    socialTenureRelationshipBuyerDetails = regRecordsService.getSocialTenureRelationshipByLandIdandTypeId(Long.parseLong(landId), processid.longValue(), persontypeId);
                } else if (editflag == 1) {
                    socialTenureRelationshipBuyerDetails = regRecordsService.getSocialTenureRelationshipByTransactionId(transactionId.longValue());
                }
            } else if (processid == 3) {
                persontypeId = 1;
                doc_name = ServletRequestUtils.getRequiredStringParameter(request, "doc_name_mortgage");
                doc_desc = ServletRequestUtils.getRequiredStringParameter(request, "doc_desc_mortgage");
                doc_date = ServletRequestUtils.getRequiredStringParameter(request, "doc_date_mortgage");
                if (editflag == 0) {
                    laMortgage = laMortgagedao.getMortgageByLandId(Long.parseLong(landId));
                } else if (editflag == 1) {
                    tran = transactionDao.getLaExtTransactiondetail(transactionId);
                    laMortgage = laMortgagedao.getMortgageByMotgageId(tran.getModuletransid());
                }

            } else if (processid == 4) {
                persontypeId = 11;
                doc_name = ServletRequestUtils.getRequiredStringParameter(request, "doc_name_sale");
                doc_desc = ServletRequestUtils.getRequiredStringParameter(request, "doc_desc_sale");
                doc_date = ServletRequestUtils.getRequiredStringParameter(request, "doc_date_sale");
                if (editflag == 0) {
                    socialTenureRelationshipBuyerDetails = regRecordsService.getSocialTenureRelationshipByLandIdandTypeId(Long.parseLong(landId), processid.longValue(), persontypeId);
                } else if (editflag == 1) {
                    socialTenureRelationshipBuyerDetails = regRecordsService.getSocialTenureRelationshipByTransactionId(transactionId.longValue());
                }
            } else if (processid == 5) {
                persontypeId = 1;
                doc_name = ServletRequestUtils.getRequiredStringParameter(request, "doc_name_Lease");
                doc_desc = ServletRequestUtils.getRequiredStringParameter(request, "doc_desc_Lease");
                doc_date = ServletRequestUtils.getRequiredStringParameter(request, "doc_date_Lease");
                surenderLease = laSurrenderLeaseDao.findById(surrenderLeaseId, false);
            } else if (processid == 6) {
                persontypeId = 11;
                doc_name = ServletRequestUtils.getRequiredStringParameter(request, "doc_name_sale");
                doc_desc = ServletRequestUtils.getRequiredStringParameter(request, "doc_desc_sale");
                doc_date = ServletRequestUtils.getRequiredStringParameter(request, "doc_date_sale");
                if (editflag == 0) {
                    socialTenureRelationshipBuyerDetails = regRecordsService.getSocialTenureRelationshipByLandIdandTypeId(Long.parseLong(landId), processid.longValue(), persontypeId);
                } else if (editflag == 1) {
                    socialTenureRelationshipBuyerDetails = regRecordsService.getSocialTenureRelationshipByTransactionId(transactionId.longValue());
                }
            } else if (processid == 7) {
                persontypeId = 11;
                doc_name = ServletRequestUtils.getRequiredStringParameter(request, "doc_name_sale");
                doc_desc = ServletRequestUtils.getRequiredStringParameter(request, "doc_desc_sale");
                doc_date = ServletRequestUtils.getRequiredStringParameter(request, "doc_date_sale");
                if (editflag == 0) {
                    socialTenureRelationshipBuyerDetails = regRecordsService.getSocialTenureRelationshipByLandIdandTypeId(Long.parseLong(landId), processid.longValue(), persontypeId);
                } else if (editflag == 1) {
                    socialTenureRelationshipBuyerDetails = regRecordsService.getSocialTenureRelationshipByTransactionId(transactionId.longValue());
                }
            } else if (processid == 9) {
                persontypeId = 1;
                doc_name = ServletRequestUtils.getRequiredStringParameter(request, "doc_name_mortgage");
                doc_desc = ServletRequestUtils.getRequiredStringParameter(request, "doc_desc_mortgage");
                doc_date = ServletRequestUtils.getRequiredStringParameter(request, "doc_date_mortgage");
                if (editflag == 0) {
                    lasurrenderMortgage = lasurrenderMortgagedao.getMortgageByLandIdandprocessId(Long.parseLong(landId), processid.longValue());
                } else if (editflag == 1) {
                    tran = transactionDao.getLaExtTransactiondetail(transactionId);
                    lasurrenderMortgage = lasurrenderMortgagedao.getMortgageBysurMortgageId(tran.getModuletransid());

                }
            }

            Date uploadDate = null;
            DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");

            try {
                uploadDate = dateformat.parse(doc_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            SourceDocument sourceDocument = new SourceDocument();
            sourceDocument.setCreatedby(user_id.intValue());
            sourceDocument.setCreateddate(new Date());
            sourceDocument.setDocumentlocation("");
            sourceDocument.setDocumentname(doc_name);
            sourceDocument.setIsactive(true);
            sourceDocument.setRemarks(doc_desc);
            sourceDocument.setRecordationdate(uploadDate);
            sourceDocument.setDocumenttypeid(1);

            try {
                Long buyerPartyId = 0L;
                if (socialTenureRelationshipBuyerDetails != null) {
                    buyerPartyId = socialTenureRelationshipBuyerDetails.getPartyid();
                    buyertransid = socialTenureRelationshipBuyerDetails.getLaExtTransactiondetail().getTransactionid().longValue();
                    Long partyId = buyerPartyId;
                    laParty = regRecordsService.getLaPartyById(partyId);
                    tran = regRecordsService.getLaExtTransactiondetail(buyertransid.intValue());
                    sourceDocument.setLaParty(laParty);
                    sourceDocument.setLaExtTransactiondetail(tran);
                    sourceDocument.setLaSpatialunitLand(Long.parseLong(landId));
                    regRecordsService.saveUploadedDocuments(sourceDocument);
                } else if (null != leaseeobjList && leaseeobjList.size() > 0) {
                    tran = transactionDao.getLaExtTransactionByLeaseeid(leaseeobjList.get(0).getLeaseid().longValue());
                    laParty = regRecordsService.getLaPartyById(leaseeobjList.get(0).getPersonid());
                    sourceDocument.setLaParty(laParty);
                    sourceDocument.setLaExtTransactiondetail(tran);
                    sourceDocument.setLaSpatialunitLand(Long.parseLong(landId));
                    regRecordsService.saveUploadedDocuments(sourceDocument);
                } else if (null != laMortgage) {
                    tran = transactionDao.getLaExtTransactionforMortgage(laMortgage.getMortgageid().longValue());
                    laParty = regRecordsService.getLaPartyById(laMortgage.getOwnerid());
                    sourceDocument.setLaParty(laParty);
                    sourceDocument.setLaExtTransactiondetail(tran);
                    sourceDocument.setLaSpatialunitLand(Long.parseLong(landId));
                    regRecordsService.saveUploadedDocuments(sourceDocument);
                } else if (null != surenderLease) {
                    tran = transactionDao.getLaExtTransactionByLeaseeidForSurrenderLease(surenderLease.getLeaseid().longValue());
                    sourceDocument.setLaExtTransactiondetail(tran);
                    sourceDocument.setLaSpatialunitLand(Long.parseLong(landId));
                    regRecordsService.saveUploadedDocuments(sourceDocument);
                } else if (null != lasurrenderMortgage) {
                    tran = transactionDao.getLaExtTransactionforSurrenderMortgage(lasurrenderMortgage.getMortgageid().longValue());
                    laParty = regRecordsService.getLaPartyById(lasurrenderMortgage.getOwnerid());
                    sourceDocument.setLaParty(laParty);
                    sourceDocument.setLaExtTransactiondetail(tran);
                    sourceDocument.setLaSpatialunitLand(Long.parseLong(landId));
                    regRecordsService.saveUploadedDocuments(sourceDocument);
                } else if (permission != null) {
                    tran = transactionDao.getLaExtTransactiondetail(permission.getTransactionid());
                    sourceDocument.setLaExtTransactiondetail(tran);
                    sourceDocument.setLaSpatialunitLand(Long.parseLong(landId));
                    regRecordsService.saveUploadedDocuments(sourceDocument);
                }

            } catch (Exception e) {
                logger.error(e);
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return persontypeId;
    }

    @RequestMapping(value = "/viewer/registration/savedocumentInfo/split", method = RequestMethod.POST)
    @ResponseBody
    public Boolean savedocumentInfoSplits(HttpServletRequest request, HttpServletResponse response, Principal principal) {

        try {
            String landId = "";
            LaExtTransactiondetail laExtTransactiondetail = new LaExtTransactiondetail();
            String username = principal.getName();
            User userObj = userService.findByUniqueName(username);
            Long user_id = userObj.getId();
            landId = ServletRequestUtils.getRequiredStringParameter(request, "split_selectedlandid");
            String doc_name_sale = ServletRequestUtils.getRequiredStringParameter(request, "doc_name_split");
            String doc_desc_sale = ServletRequestUtils.getRequiredStringParameter(request, "doc_desc_split");
            String doc_date_sale = ServletRequestUtils.getRequiredStringParameter(request, "doc_date_split");

            String outDirPath = FileUtils.getFielsFolder(request) + "resources" + File.separator + "documents" + File.separator + "Registry" + File.separator + "webupload";
            File outDir = new File(outDirPath);
            boolean exists = outDir.exists();
            Date uploadDate = null;
            DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
            if (!exists) {
                (new File(outDirPath)).mkdirs();
            }

            try {
                uploadDate = dateformat.parse(doc_date_sale);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            SourceDocument sourceDocument = new SourceDocument();
            sourceDocument.setCreatedby(user_id.intValue());
            sourceDocument.setCreateddate(new Date());
            sourceDocument.setDocumentlocation("");
            sourceDocument.setDocumentname(doc_name_sale);
            sourceDocument.setIsactive(true);
            sourceDocument.setRemarks(doc_desc_sale);
            sourceDocument.setRecordationdate(uploadDate);
            sourceDocument.setDocumenttypeid(1);
            try {
                SocialTenureRelationship socialTenureRelationship = regRecordsService.getSocialTenureRelationshipByLandId(Long.parseLong(landId));
                Long partyId = socialTenureRelationship.getPartyid();
                LaParty laParty = regRecordsService.getLaPartyById(partyId);
                List<SourceDocument> lstsourceDocument = sourceDocumentDAO.findSourceDocumentByLandIdAndProessId(Long.parseLong(landId), 8l);
                if (lstsourceDocument.size() > 0) {

                    laExtTransactiondetail = regRecordsService.getLaExtTransactiondetail(lstsourceDocument.get(0).getLaExtTransactiondetail().getTransactionid());
                } else {
                    laExtTransactiondetail.setCreatedby(user_id.intValue());
                    laExtTransactiondetail.setCreateddate(new Date());
                    laExtTransactiondetail.setIsactive(true);
                    laExtTransactiondetail.setLaExtApplicationstatus(regRecordsService.getStatusById(1));
                    laExtTransactiondetail.setModuletransid(partyId.intValue());
                    laExtTransactiondetail.setRemarks("");
                    laExtTransactiondetail.setProcessid(8l);
                }

                sourceDocument.setLaParty(laParty);
                sourceDocument.setLaExtTransactiondetail(laExtTransactiondetail);
                sourceDocument.setLaSpatialunitLand(Long.parseLong(landId));
                sourceDocument = regRecordsService.saveUploadedDocuments(sourceDocument);

            } catch (Exception e) {
                logger.error(e);
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;

    }

    @RequestMapping(value = "/viewer/registryrecords/getprocessDocument/{landid}/{persontypeId}/{processId}", method = RequestMethod.GET)
    @ResponseBody
    public List<SourceDocument> getprocessDocument(@PathVariable Long landid, @PathVariable Integer persontypeId, @PathVariable Long processId) {
        List<SourceDocument> lstSourceDocument = new ArrayList<SourceDocument>();

        try {

            SocialTenureRelationship socialTenureRelationshipBuyerDetails = regRecordsService.getSocialTenureRelationshipByLandIdandTypeId(landid, processId, persontypeId); //landid,2l,11
            List<LaLease> leaseeobjList = laLeaseDao.getleaseobjbylandandprocessidList(landid, processId);
            LaMortgage laMortgageobj = laMortgagedao.getMortgageByLandIdandprocessId(landid, processId);
            LaSurrenderMortgage lasurrenderMortgageobj = lasurrenderMortgagedao.getMortgageByLandIdandprocessId(landid, processId);
            LaSurrenderLease surenderLease = laSurrenderLeaseDao.getSurrenderleaseByLandandProcessId(landid, processId);
            Integer transId = 0;
            if (processId == 2 || processId == 4 || processId == 6 || processId == 7) {
                if (socialTenureRelationshipBuyerDetails != null) {
                    transId = socialTenureRelationshipBuyerDetails.getLaExtTransactiondetail().getTransactionid();

                    lstSourceDocument = sourceDocumentDAO.findSourceDocumentByLandIdandTransactionid(landid, transId);
                    return lstSourceDocument;
                }
            }

            if (processId == 1 || processId == 10) {
                if (null != leaseeobjList) {
                    for (LaLease obj : leaseeobjList) {
                        LaExtTransactiondetail laExtTransactiondetail = transactionDao.getLaExtTransactionByLeaseeid(obj.getLeaseid().longValue());
                        if (null != laExtTransactiondetail) {
                            transId = laExtTransactiondetail.getTransactionid();
                            lstSourceDocument = sourceDocumentDAO.findSourceDocumentByLandIdandTransactionid(landid, transId);
                            if (lstSourceDocument.size() > 0) {
                                return lstSourceDocument;
                            }
                        }
                    }
                    if (lstSourceDocument.size() == 0) {
                        return null;
                    }

                }
            }

            if (processId == 5) {
                if (null != surenderLease) {
                    LaExtTransactiondetail laExtTransactiondetail = transactionDao.getLaExtTransactionByLeaseeidForSurrenderLease(surenderLease.getLeaseid().longValue());
                    transId = laExtTransactiondetail.getTransactionid();
                    lstSourceDocument = sourceDocumentDAO.findSourceDocumentByLandIdandTransactionid(landid, transId);
                    return lstSourceDocument;
                }
            }

            if (processId == 3) {
                if (null != laMortgageobj) {
                    LaExtTransactiondetail laExtTransactiondetail = transactionDao.getLaExtTransactionforMortgage(laMortgageobj.getMortgageid().longValue());
                    transId = laExtTransactiondetail.getTransactionid();
                    lstSourceDocument = sourceDocumentDAO.findSourceDocumentByLandIdandTransactionid(landid, transId);
                    return lstSourceDocument;
                }
            }

            if (processId == 9) {
                if (null != lasurrenderMortgageobj) {
                    LaExtTransactiondetail laExtTransactiondetail = transactionDao.getLaExtTransactionforSurrenderMortgage(lasurrenderMortgageobj.getMortgageid().longValue());
                    transId = laExtTransactiondetail.getTransactionid();
                    lstSourceDocument = sourceDocumentDAO.findSourceDocumentByLandIdandTransactionid(landid, transId);
                    return lstSourceDocument;
                }
            }

            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @RequestMapping(value = "/viewer/registryrecords/editDocuments/{landid}/{transid}", method = RequestMethod.GET)
    @ResponseBody
    public List<SourceDocument> editDocument(@PathVariable Long landid, @PathVariable Integer transid) {
        List<SourceDocument> lstSourceDocument = new ArrayList<SourceDocument>();
        try {
            lstSourceDocument = sourceDocumentDAO.findSourceDocumentByLandIdandTransactionid(landid, transid);
            if (lstSourceDocument.size() > 0) {
                return lstSourceDocument;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/viewer/registryrecords/getsplitDocument/{landid}", method = RequestMethod.GET)
    @ResponseBody
    public List<SourceDocument> getsplitDocument(@PathVariable Long landid) {
        List<SourceDocument> lstSourceDocument = new ArrayList<SourceDocument>();

        try {

            lstSourceDocument = sourceDocumentDAO.findSourceDocumentByLandIdAndProessId(landid, 8l);

            if (lstSourceDocument != null) {

                return lstSourceDocument;
            } else {

                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @RequestMapping(value = "/viewer/registration/updateLeaseeDetails", method = RequestMethod.POST)
    @ResponseBody
    public String updateLeaseeDetails(HttpServletRequest request, HttpServletResponse response, Principal principal) {

        try {
            Long landId = 0L;
            LaLease leaseeobj = null;
            LaExtTransactiondetail laExtTransactiondetail = null;
            List<LaLease> leaseeobjList = new ArrayList<LaLease>();
            Integer transactionId = 0;
            Integer leaseepersonid = 0;

            int months = ServletRequestUtils.getRequiredIntParameter(request, "no_Of_month_Lease");
            Double lease_Amount = ServletRequestUtils.getRequiredDoubleParameter(request, "lease_Amount");
            landId = ServletRequestUtils.getRequiredLongParameter(request, "landidhide");
            String Start_date_Lease = ServletRequestUtils.getRequiredStringParameter(request, "Start_date_Lease");
            String End_date_Lease = ServletRequestUtils.getRequiredStringParameter(request, "End_date_Lease");
            Integer personId = ServletRequestUtils.getRequiredIntParameter(request, "leaseeperson");

            Integer editflag = ServletRequestUtils.getRequiredIntParameter(request, "editflag");
            if (editflag == 1) {
                leaseepersonid = ServletRequestUtils.getRequiredIntParameter(request, "leaseeperson");
                transactionId = ServletRequestUtils.getRequiredIntParameter(request, "transactionId");
            }

            DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = null;
            try {
                startDate = dateformat.parse(Start_date_Lease);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            Date endDate = null;
            try {
                endDate = dateformat.parse(End_date_Lease);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            int year = months / 12;

            if (editflag == 1) {
                leaseeobjList = laLeaseDao.getleaseeListByLandandPersonId(landId, leaseepersonid.longValue());
            } else if (personId != 0) {
                leaseeobjList = laLeaseDao.getleaseeListByLandandPersonId(landId, personId.longValue());
            } else if (editflag == 0 && personId == 0 && leaseepersonid == 0) {
                LaLease l = laLeaseDao.getLeaseByLandId(landId);
                if (l != null) {
                    leaseeobjList.add(l);
                }
            }
            if (leaseeobjList.size() > 0 && leaseepersonid != 0) {
                leaseeobj = leaseeobjList.get(0);
                leaseeobj.setMonths(months);
                leaseeobj.setLeaseamount(lease_Amount);
                leaseeobj.setYears(0);
                leaseeobj.setLeasestartdate(startDate);
                leaseeobj.setLeaseenddate(endDate);
                leaseeobj = regRecordsService.saveLease(leaseeobj);

                laExtTransactiondetail = transactionDao.getLaExtTransactionByLeaseeid(leaseeobj.getLeaseid().longValue());
            } else if (leaseeobjList.size() > 0 && personId != 0) {
                leaseeobj = leaseeobjList.get(0);
                leaseeobj.setMonths(months);
                leaseeobj.setLeaseamount(lease_Amount);
                leaseeobj.setYears(0);
                leaseeobj.setLeasestartdate(startDate);
                leaseeobj.setLeaseenddate(endDate);
                leaseeobj = regRecordsService.saveLease(leaseeobj);

                laExtTransactiondetail = transactionDao.getLaExtTransactionByLeaseeid(leaseeobj.getLeaseid().longValue());
            } else if (leaseeobjList.size() > 0 && personId == 0 && leaseepersonid == 0) {
                for (LaLease leaseeobjall : leaseeobjList) {
                    leaseeobj.setMonths(months);
                    leaseeobjall.setLeaseamount(lease_Amount);
                    leaseeobj.setYears(0);
                    leaseeobjall.setLeasestartdate(startDate);
                    leaseeobjall.setLeaseenddate(endDate);
                    leaseeobjall = regRecordsService.saveLease(leaseeobjall);
                }

                laExtTransactiondetail = transactionDao.getLaExtTransactionByLeaseeid(leaseeobjList.get(0).getLeaseid().longValue());

            } else {

                return "success";
            }
            if (null != laExtTransactiondetail) {
                return laExtTransactiondetail.getTransactionid().toString();
            } else {
                return "Success";
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return null;

    }

    @RequestMapping(value = "/viewer/registration/saveleasedata", method = RequestMethod.POST)
    @ResponseBody
    public boolean saveLeaseData(HttpServletRequest request, HttpServletResponse response, Principal principal) {
        try {
            Integer leaseId = ServletRequestUtils.getIntParameter(request, "leaseid", 0);
            Integer years = ServletRequestUtils.getIntParameter(request, "leaseYears", 0);
            Integer months = ServletRequestUtils.getIntParameter(request, "leaseMonths", 0);
            Integer amount = ServletRequestUtils.getIntParameter(request, "lease_Amount", 0);
            String strDateFrom = ServletRequestUtils.getStringParameter(request, "leaseStartDate", "");
            String strDateTo = ServletRequestUtils.getStringParameter(request, "leaseEndDate", "");
            Integer usageType = ServletRequestUtils.getIntParameter(request, "radio_usage", 1);
            String leaseConditions = ServletRequestUtils.getStringParameter(request, "leaseConditions", "");

            DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateFrom = null;
            Date dateTo = null;
            try {
                if (StringUtils.isNotEmpty(strDateFrom)) {
                    dateFrom = dateformat.parse(strDateFrom);
                }
                if (StringUtils.isNotEmpty(strDateTo)) {
                    dateTo = dateformat.parse(strDateTo);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            LaLease lease = laLeaseDao.getLeaseById(leaseId);

            if (lease == null) {
                return false;
            }

            lease.setYears(years);
            lease.setMonths(months);
            lease.setLeaseamount((double) amount);
            lease.setLeasestartdate(dateFrom);
            lease.setLeaseenddate(dateTo);
            if (usageType == 1) {
                lease.setIndividual(Boolean.TRUE);
            } else {
                lease.setIndividual(Boolean.FALSE);
            }
            lease.setConditions(leaseConditions);

            return regRecordsService.registerLease(lease);
        } catch (Exception e) {
            logger.error(e);
        }
        return false;
    }

    @RequestMapping(value = "/viewer/registration/updateMortgageData", method = RequestMethod.POST)
    @ResponseBody
    public String updateMortgageData(HttpServletRequest request, HttpServletResponse response, Principal principal) {

        try {
            Long landId = 0L;
            LaMortgage laMortgage = null;
            LaExtTransactiondetail laExtTransactiondetail = null;

            landId = ServletRequestUtils.getRequiredLongParameter(request, "landidhide");

            laMortgage = laMortgagedao.getMortgageByLandId(landId);
            if (null != laMortgage) {

                laExtTransactiondetail = transactionDao.getLaExtTransactionByLeaseeid(laMortgage.getMortgageid().longValue());
                Status status = regRecordsService.getStatusById(2);
                laExtTransactiondetail.setLaExtApplicationstatus(status);
                laExtTransactiondetail = regRecordsService.saveTransaction(laExtTransactiondetail);
            } else {

            }

            return laExtTransactiondetail.getTransactionid().toString();
        } catch (Exception e) {
            logger.error(e);
        }
        return null;

    }

    @RequestMapping(value = "/viewer/upload/media_sale/", method = RequestMethod.POST)
    @ResponseBody
    public String upload(MultipartHttpServletRequest request, HttpServletResponse response, Principal principal) {
        SourceDocument doc = null;
        try {
            Iterator<String> file = request.getFileNames();

            Integer documentId = null;
            documentId = ServletRequestUtils.getRequiredIntParameter(request, "documentId");

            byte[] document = null;
            while (file.hasNext()) {
                String fileName = file.next();
                MultipartFile mpFile = request.getFile(fileName);
                String originalFileName = mpFile.getOriginalFilename();

                if (originalFileName != "") {
                    document = mpFile.getBytes();
                }

                doc = sourceDocumentDAO.findDocumentByDocumentId(documentId.longValue());
                String newFileName = doc.getDocumentid().toString() + "." + FileUtils.getFileExtension(originalFileName);
                String folderLocation = "/multimedia";
                String fileLocation = folderLocation + File.separator + newFileName;
                doc.setDocumentlocation(fileLocation);

                String filepath = FileUtils.getFielsFolder(request) + folderLocation;
                filepath = filepath.replace("\\mast\\..", "");
                File existingdr = new File(filepath);

                boolean exist = existingdr.exists();
                if (!exist) {
                    (new File(filepath)).mkdirs();
                }

                try {
                    File serverFile = new File(existingdr + File.separator + newFileName);
                    FileOutputStream uploadfile = new FileOutputStream(serverFile, false);
                    uploadfile.write(document);
                    uploadfile.flush();
                    uploadfile.close();
                    sourceDocumentsDao.saveUploadedDocuments(doc);
                } catch (Exception e) {
                    logger.error(e);
                }

                return doc.getLaSpatialunitLand().toString();
            }

        } catch (Exception e) {
            logger.error(e);
        }
        return doc.getLaSpatialunitLand().toString();
    }

    @RequestMapping(value = "/viewer/registration/mediaavail/{documentId}", method = RequestMethod.GET)
    @ResponseBody
    public boolean isPersonMultimediaexist(@PathVariable Long documentId, HttpServletRequest request, HttpServletResponse response) {

        SourceDocument doc = sourceDocumentDAO.findDocumentByDocumentId(documentId.longValue());
        if (doc == null) {
            return false;
        }

        return true;
    }

    @RequestMapping(value = "/viewer/registration/download/{documentId}", method = RequestMethod.GET)
    @ResponseBody
    public void PersonMultimediaShowSale(@PathVariable Long documentId, HttpServletRequest request, HttpServletResponse response) {
        byte[] data = null;
        SourceDocument doc = sourceDocumentDAO.findDocumentByDocumentId(documentId.longValue());

        if (doc == null) {
            response.setContentLength(data.length);
            return;
        }

        String filepath = FileUtils.getFielsFolder(request) + doc.getDocumentlocation();
        filepath = filepath.replace("\\mast\\..", "");
        Path path = Paths.get(filepath);
        try {
            data = Files.readAllBytes(path);
            response.setContentLength(data.length);
            if (FileUtils.getFileExtension(doc.getDocumentlocation()).equalsIgnoreCase("xlsx")
                    || FileUtils.getFileExtension(doc.getDocumentlocation()).equalsIgnoreCase("xls")) {
                response.setContentType("application/vnd.ms-excel");
            } else if (FileUtils.getFileExtension(doc.getDocumentlocation()).equalsIgnoreCase("docx")
                    || FileUtils.getFileExtension(doc.getDocumentlocation()).equalsIgnoreCase("rtf")
                    || FileUtils.getFileExtension(doc.getDocumentlocation()).equalsIgnoreCase("doc")) {
                response.setContentType("application/msword");
            } else if (FileUtils.getFileExtension(doc.getDocumentlocation()).equalsIgnoreCase("pdf")) {
                response.setContentType("application/pdf");
            }
            OutputStream out = response.getOutputStream();
            out.write(data);
            out.flush();
            out.close();

        } catch (Exception e) {
            logger.error(e);
        }
    }

    @RequestMapping(value = "/viewer/delete/media_sale/", method = RequestMethod.POST)
    @ResponseBody
    public String delete_sale(HttpServletRequest request, HttpServletResponse response, Principal principal) {
        SourceDocument doc = null;

        try {

            String userId[] = null;
            String project = "";
            String username = principal.getName();
            User userObj = userService.findByUniqueName(username);

            Long created_by = userObj.getId();
            Long modifiedby = created_by;// As discussed with rajendra sir modified by also same as created by

            Integer documentId = null;
            documentId = ServletRequestUtils.getRequiredIntParameter(request, "documentId");

//				Integer transid =null;
//				transid= ServletRequestUtils.getRequiredIntParameter(request, "transid");
//				
//				Long landid = 0L;
//				landid= ServletRequestUtils.getRequiredLongParameter(request, "landid");
            doc = sourceDocumentDAO.findDocumentByDocumentId(documentId.longValue());
            doc.setIsactive(false);

            sourceDocumentsDao.saveUploadedDocuments(doc);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc.getLaSpatialunitLand().toString();

    }

    @RequestMapping(value = "/viewer/registration/approvesurrenderleasedata", method = RequestMethod.POST)
    @ResponseBody
    public boolean approveSurrenderLeasedata(HttpServletRequest request, HttpServletResponse response, Principal principal) {

        LaExtTransactiondetail laExtTransactiondetail = null;
        try {
            int surrenderLeaseId = ServletRequestUtils.getRequiredIntParameter(request, "surrenderLeaseId");
            int leaseId = ServletRequestUtils.getRequiredIntParameter(request, "leaseid");

            LaSurrenderLease surrenderleaseobj = laSurrenderLeaseDao.findById(surrenderLeaseId, false);

            if (null != surrenderleaseobj) {
                laExtTransactiondetail = transactionDao.getLaExtTransactionByLeaseeid(surrenderleaseobj.getLeaseid().longValue());
                Status status = regRecordsService.getStatusById(7); // Final
                laExtTransactiondetail.setLaExtApplicationstatus(status);
                regRecordsService.saveTransaction(laExtTransactiondetail);
                regRecordsService.disablelease(leaseId);
            }

            return true;
        } catch (Exception e) {
            logger.error(e);
        }
        return false;
    }

    @RequestMapping(value = "/viewer/registration/savesurrenderMortgagedata", method = RequestMethod.POST)
    @ResponseBody
    public String saveSurrenderMortgagedata(HttpServletRequest request, HttpServletResponse response, Principal principal) {

        try {
            Long landId = 0L;
            LaMortgage mortgageobj = null;
            LaSurrenderMortgage laMortgage = null;
            LaExtFinancialagency laExtFinancialagency = null;
            LaExtTransactiondetail laExtTransactiondetail = null;
            String username = principal.getName();
            User userObj = userService.findByUniqueName(username);
            Integer transactionId = 0;
            Long user_id = userObj.getId();

            int financial_AgenciesID = ServletRequestUtils.getRequiredIntParameter(request, "mortgage_Financial_Agencies");
            String mortgage_from = ServletRequestUtils.getRequiredStringParameter(request, "mortgage_from");
            String mortgage_to = ServletRequestUtils.getRequiredStringParameter(request, "mortgage_to");
            Double amount_mortgage = ServletRequestUtils.getRequiredDoubleParameter(request, "amount_mortgage");
            String mortgagesurrender_reason = ServletRequestUtils.getRequiredStringParameter(request, "mortgagesurrender_reason");
            Integer editflag = ServletRequestUtils.getRequiredIntParameter(request, "editflag");
            if (editflag == 1) {
                transactionId = ServletRequestUtils.getRequiredIntParameter(request, "transactionId");
            }

            landId = ServletRequestUtils.getRequiredLongParameter(request, "landidhide");

            Status status = regRecordsService.getStatusById(1);

//				PersonType personType = registrationRecordsService.getPersonTypeById(1);
            DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
            //Date date = null;
            Date mortgage_fromDate = null;
            Date mortgage_toDate = null;
            //long time = 0;
            try {
                mortgage_fromDate = dateformat.parse(mortgage_from);
                mortgage_toDate = dateformat.parse(mortgage_to);

                //date = dateformat.parse(new Date().toString());
                //time = date.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (editflag == 1) {
                laMortgage = laMortgageSurrenderDao.getMortgageByLandIdandTransId(landId, transactionId);
            } else if (editflag == 0) {
                mortgageobj = laMortgagedao.getMortgageByLandId(landId);
                laMortgage = laMortgageSurrenderDao.getMortgageByLandIdandprocessId(landId, 9L);

            }

            if (null != laMortgage) {
                laMortgage.setMortgagefrom(mortgage_fromDate);
                laMortgage.setMortgageto(mortgage_toDate);
                laMortgage.setMortgageamount(amount_mortgage);
                laExtFinancialagency = regRecordsService.getFinancialagencyByID(financial_AgenciesID);
                laMortgage.setLaExtFinancialagency(laExtFinancialagency);
                laMortgage.setSurrenderreason(mortgagesurrender_reason);
                laMortgage = regRecordsService.saveSurrenderMortgage(laMortgage);
                /* laExtTransactiondetail = transactionDao.getLaExtTransactionByLeaseeid(laMortgage.getMortgageid().longValue());
					 laExtTransactiondetail.setLaExtApplicationstatus(status);
					 laExtTransactiondetail = registrationRecordsService.saveTransaction(laExtTransactiondetail);*/

            } else if (null == laMortgage && null != mortgageobj) {
                Long ownerid = 0L;
                try {
                    SocialTenureRelationship socialTenureRelationshipSellerDetails = regRecordsService.getSocialTenureRelationshipByLandId(landId);

                    if (socialTenureRelationshipSellerDetails != null) {
                        ownerid = socialTenureRelationshipSellerDetails.getPartyid();
                    } else {
                        return null;
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

//				SocialTenureRelationship socialTenureRelationshipSellerDetails = registrationRecordsService.getSocialTenureRelationshipByLandId(landId);
                laExtFinancialagency = regRecordsService.getFinancialagencyByID(financial_AgenciesID);

                laExtTransactiondetail = new LaExtTransactiondetail();
                laExtTransactiondetail.setCreatedby(user_id.intValue());
                laExtTransactiondetail.setCreateddate(new Date());//new Timestamp(time)
                laExtTransactiondetail.setIsactive(true);
                laExtTransactiondetail.setLaExtApplicationstatus(status);

                /*laExtTransactiondetail.setRemarks(remrks_mortgage);*/
                laExtTransactiondetail.setProcessid(9L);// process Id 3 for Mortgage

                laMortgage = new LaSurrenderMortgage();
                laMortgage.setCreatedby(user_id.intValue());
                laMortgage.setCreateddate(new Date());
                laMortgage.setIsactive(true);
                //laMortgage.setLaExtTransactiondetail(laExtTransactiondetail);
                laMortgage.setMortgageamount(amount_mortgage);
                laMortgage.setMortgagefrom(mortgage_fromDate);
                laMortgage.setMortgageto(mortgage_toDate);
                laMortgage.setLaExtFinancialagency(laExtFinancialagency);
                laMortgage.setLandid(landId);
                laMortgage.setOwnerid(ownerid);
                laMortgage.setSurrenderreason(mortgagesurrender_reason);

                laMortgage = regRecordsService.saveSurrenderMortgage(laMortgage);

                laExtTransactiondetail.setModuletransid(laMortgage.getMortgageid());

                laExtTransactiondetail = regRecordsService.saveTransaction(laExtTransactiondetail);
            }

            return laExtTransactiondetail.getTransactionid().toString();
        } catch (Exception e) {
            logger.error(e);
        }
        return null;

    }

    @RequestMapping(value = "/viewer/registration/approveSurenderMortgageData", method = RequestMethod.POST)
    @ResponseBody
    public String approveSurrenderMortagagedata(HttpServletRequest request, HttpServletResponse response, Principal principal) {

        LaExtTransactiondetail laExtTransactiondetail = null;
        try {
            Long landId = 0L;
            landId = ServletRequestUtils.getRequiredLongParameter(request, "landidhide");

//					LaPartyPerson lapartydetail= getPartyPersonDetailssurrenderlease(Integer.parseInt(landId.toString()));
            LaSurrenderMortgage laMortgage = laMortgageSurrenderDao.getMortgageByLandIdandprocessId(landId, 9L);

            if (null != laMortgage) {
                laExtTransactiondetail = transactionDao.getLaExtTransactionforSurrenderMortgage(laMortgage.getMortgageid().longValue());
                Status status = regRecordsService.getStatusById(2);
                laExtTransactiondetail.setLaExtApplicationstatus(status);
                laExtTransactiondetail = regRecordsService.saveTransaction(laExtTransactiondetail);
                regRecordsService.disableMortagage(laMortgage.getOwnerid(), landId);
            }

            return laExtTransactiondetail.getTransactionid().toString();
        } catch (Exception e) {
            logger.error(e);
        }
        return null;

    }

    @RequestMapping(value = "/viewer/registryrecords/SurrenderMortagagedata/{landid}/{processId}", method = RequestMethod.GET)
    @ResponseBody
    public LaSurrenderMortgage getSurrendermortgagedata(@PathVariable Long landid, @PathVariable Long processId) {

        LaSurrenderMortgage lasurrenderMortgage = null;
        lasurrenderMortgage = laMortgageSurrenderDao.getMortgageByLandIdandprocessId(landid, processId);
        if (null != lasurrenderMortgage) {
            return lasurrenderMortgage;
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/viewer/registrion/addLeaseePoi/{landid}", method = RequestMethod.GET)
    @ResponseBody
    public String getLeaseeObject(@PathVariable Long landid) {

        List<LaLease> leaseeobjList = new ArrayList<>();
        LaLease l = laLeaseDao.getLeaseByLandId(landid);
        if (l != null) {
            leaseeobjList.add(l);
        }
        if (null != leaseeobjList) {
            return "true";
        } else {
            return "Save the leasee details first to add Poi's";
        }
    }

    @RequestMapping(value = "/viewer/landrecords/landPOILeasee/{landid}", method = RequestMethod.GET)
    @ResponseBody
    public List<SpatialUnitPersonWithInterest> getLeaseeObjectJsGrid(@PathVariable Long landid) {

        LaExtTransactiondetail laExtTransactiondetail = null;
        List<SpatialUnitPersonWithInterest> poiObject = null;

        List<LaLease> leaseeobjList = new ArrayList<>();
        LaLease l = laLeaseDao.getLeaseByLandId(landid);
        if (l != null) {
            leaseeobjList.add(l);
        }

        if (leaseeobjList.size() > 0) {
            laExtTransactiondetail = transactionDao.getLaExtTransactionByLeaseeid(leaseeobjList.get(0).getLeaseid().longValue());

            if (null != laExtTransactiondetail && laExtTransactiondetail.getLaExtApplicationstatus().getWorkflowStatusId() == 1) {

                poiObject = spatialUnitPersonWithInterestDao.findByUsinandTransid(landid, laExtTransactiondetail.getTransactionid().longValue());

                return poiObject;
            }

        }

        return null;

    }

    @RequestMapping(value = "/viewer/registration/saveRegPersonOfInterestForLeasee/{landId}", method = RequestMethod.POST)
    @ResponseBody
    public SpatialUnitPersonWithInterest saveRegPersonOfInterestForEditing(HttpServletRequest request, @PathVariable Long landId, Principal principal) {

        SpatialUnitPersonWithInterest personinterest = null;
        Date date1 = null;
        try {
            Integer PoiId = 0;
            Integer genderid = 0;
            Integer realtionid = 0;
            String dateofbirth = "";
            String firstname = "";
            String middlename = "";
            String lastname = "";
            Integer genderidlease = 0;
            Integer realtionidlease = 0;
            String dateofbirthlease = "";
            String firstnamelease = "";
            String middlenamelease = "";
            String lastnamelease = "";
            Integer transactionId = 0;
            Integer editflag = 0;

            try {
                firstnamelease = ServletRequestUtils.getRequiredStringParameter(request, "firstname_r_poi");
            } catch (Exception e) {
            }
            try {
                middlenamelease = ServletRequestUtils.getRequiredStringParameter(request, "middlename_r_poi");
            } catch (Exception e) {
            }
            try {
                lastnamelease = ServletRequestUtils.getRequiredStringParameter(request, "lastname_r_poi");
            } catch (Exception e) {
            }
            try {
                genderidlease = ServletRequestUtils.getRequiredIntParameter(request, "gender_type_POI");
            } catch (Exception e) {
            }
            try {
                realtionidlease = ServletRequestUtils.getRequiredIntParameter(request, "Relationship_POI");
            } catch (Exception e) {
            }
            try {
                dateofbirthlease = ServletRequestUtils.getRequiredStringParameter(request, "date_Of_birthPOI");
            } catch (Exception e) {
            }
            try {
                firstname = ServletRequestUtils.getRequiredStringParameter(request, "firstname_sale_poi");
            } catch (Exception e) {
            }
            try {
                middlename = ServletRequestUtils.getRequiredStringParameter(request, "middlename_sale_poi");
            } catch (Exception e) {
            }
            try {
                lastname = ServletRequestUtils.getRequiredStringParameter(request, "lastname_sale_poi");
            } catch (Exception e) {
            }
            try {
                genderid = ServletRequestUtils.getRequiredIntParameter(request, "gender_sale_POI");
            } catch (Exception e) {
            }
            try {
                realtionid = ServletRequestUtils.getRequiredIntParameter(request, "Relationship_POI_sale");
            } catch (Exception e) {
            }
            try {
                dateofbirth = ServletRequestUtils.getRequiredStringParameter(request, "date_Of_birthPOI_sale");
            } catch (Exception e) {
            }
            try {
                PoiId = ServletRequestUtils.getRequiredIntParameter(request, "leaseepoiid");
            } catch (Exception e) {
            }
            editflag = ServletRequestUtils.getRequiredIntParameter(request, "editflag");
            if (dateofbirth != "") {

                date1 = new SimpleDateFormat("YYYY-MM-DD").parse(dateofbirth);

//				 date1=new SimpleDateFormat("yyyy-MM-dd").parse(finaldob);
            }

            if (dateofbirthlease != "") {

                date1 = new SimpleDateFormat("YYYY-MM-DD").parse(dateofbirthlease);

//				 date1=new SimpleDateFormat("yyyy-MM-dd").parse(finaldob);
            }
            List<SpatialUnitPersonWithInterest> poiList = new ArrayList<SpatialUnitPersonWithInterest>();
            LaExtTransactiondetail laExtTransactiondetail = null;
            List<SpatialUnitPersonWithInterest> poiObject = null;
            if (editflag == 0) {
                LaLease leaseeobjList = laLeaseDao.getLeaseByLandId(landId);
                if (null != leaseeobjList) {
                    laExtTransactiondetail = transactionDao.getLaExtTransactionByLeaseeid(leaseeobjList.getLeaseid().longValue());

                }
            }

            if (editflag == 1) {
                transactionId = landId.intValue();
            }

            personinterest = spatialUnitPersonWithInterestDao.findSpatialUnitPersonWithInterestById(PoiId.longValue());

            if (null == personinterest) {
                personinterest = new SpatialUnitPersonWithInterest();
                if (!firstname.equalsIgnoreCase("")) {
                    personinterest.setFirstName(firstname);
                }
                if (!middlename.equalsIgnoreCase("")) {
                    personinterest.setMiddleName(middlename);
                }
                if (!lastname.equalsIgnoreCase("")) {
                    personinterest.setLastName(lastname);
                }
                if (null != date1) {
                    personinterest.setDob(date1);
                }
                if (genderid != 0) {
                    personinterest.setGender(genderid);
                }
                if (realtionid != 0) {
                    personinterest.setRelation(realtionid);
                }

                if (!firstnamelease.equalsIgnoreCase("")) {
                    personinterest.setFirstName(firstnamelease);
                }
                if (!middlenamelease.equalsIgnoreCase("")) {
                    personinterest.setMiddleName(middlenamelease);
                }
                if (!lastnamelease.equalsIgnoreCase("")) {
                    personinterest.setLastName(lastnamelease);
                }
                if (genderidlease != 0) {
                    personinterest.setGender(genderidlease);
                }
                if (realtionidlease != 0) {
                    personinterest.setRelation(realtionidlease);
                }
                if (null != date1) {
                    personinterest.setDob(date1);
                }

                personinterest.setLandid(landId);
                personinterest.setCreatedby(1);
                personinterest.setCreateddate(new Date());
                personinterest.setIsactive(true);
                if (editflag == 1) {

                    personinterest.setTransactionid(transactionId);

                } else {
                    personinterest.setTransactionid(laExtTransactiondetail.getTransactionid());
                }
            } else {
                if (!firstname.equalsIgnoreCase("")) {
                    personinterest.setFirstName(firstname);
                }
                if (!middlename.equalsIgnoreCase("")) {
                    personinterest.setMiddleName(middlename);
                }
                if (!lastname.equalsIgnoreCase("")) {
                    personinterest.setLastName(lastname);
                }
                if (null != date1) {
                    personinterest.setDob(date1);
                }
                if (genderid != 0) {
                    personinterest.setGender(genderid);
                }
                if (realtionid != 0) {
                    personinterest.setRelation(realtionid);
                }

                if (!firstnamelease.equalsIgnoreCase("")) {
                    personinterest.setFirstName(firstnamelease);
                }
                if (!middlenamelease.equalsIgnoreCase("")) {
                    personinterest.setMiddleName(middlenamelease);
                }
                if (!lastnamelease.equalsIgnoreCase("")) {
                    personinterest.setLastName(lastnamelease);
                }
                if (genderidlease != 0) {
                    personinterest.setGender(genderidlease);
                }
                if (realtionidlease != 0) {
                    personinterest.setRelation(realtionidlease);
                }
                if (null != date1) {
                    personinterest.setDob(date1);
                }
                if (editflag == 1) {

                    personinterest.setTransactionid(transactionId);

                } else {
                    personinterest.setTransactionid(laExtTransactiondetail.getTransactionid());
                }

            }
            poiList.add(personinterest);
            spatialUnitPersonWithInterestDao.addNextOfKin(poiList, landId);

            return personinterest;
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @RequestMapping(value = "/viewer/registration/landLeaseePOI/{landid}", method = RequestMethod.GET)
    @ResponseBody
    public List<PoiReport> getLeaseePoi(@PathVariable Long landid) {

        LaExtTransactiondetail laExtTransactiondetail = null;
        List<PoiReport> poiObject = null;

        List<LaLease> leaseeobjList = new ArrayList<>();
        LaLease l = laLeaseDao.getLeaseByLandId(landid);
        if (l != null) {
            leaseeobjList.add(l);
        }

        if (leaseeobjList.size() > 0) {
            for (LaLease Obj : leaseeobjList) {

                laExtTransactiondetail = transactionDao.getpoiByLeaseeid(Obj.getLeaseid().longValue());

                if (null != laExtTransactiondetail && laExtTransactiondetail.getLaExtApplicationstatus().getWorkflowStatusId() == 2) {

                    poiObject = landRecordsService.getDataCorrectionReportPOI(laExtTransactiondetail.getTransactionid().longValue(), landid);
                    if (null != poiObject) {
                        return poiObject;
                    }

                }
            }

        }

        return null;

    }

    @RequestMapping(value = "/viewer/registration/processid/{transid}", method = RequestMethod.GET)
    @ResponseBody
    public LaExtTransactiondetail getTransactiondetails(@PathVariable Long transid) {

        LaExtTransactiondetail laExtTransactiondetail = null;
        List<SpatialUnitPersonWithInterest> poiObject = null;

        laExtTransactiondetail = transactionDao.getLaExtTransactiondetail(transid.intValue());

        return laExtTransactiondetail;

    }

    @RequestMapping(value = "/viewer/registration/getPOIbyId/{Id}", method = RequestMethod.GET)
    @ResponseBody
    public SpatialUnitPersonWithInterest getPOIbyId(HttpServletRequest request, @PathVariable Long Id, Principal principal) {

        SpatialUnitPersonWithInterest personinterest = null;

        personinterest = spatialUnitPersonWithInterestDao.findSpatialUnitPersonWithInterestById(Id.longValue());

        if (null != personinterest) {
            return personinterest;
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/viewer/registration/salebuyerdetails/{personid}/{landid}", method = RequestMethod.GET)
    @ResponseBody
    public Long getBuyerbyPersonId(HttpServletRequest request, @PathVariable Long personid, @PathVariable Long landid, Principal principal) {

        SocialTenureRelationship plmobj = null;

        plmobj = socialTenureRelationshipDAO.getSocialTenureObj(personid, landid);

        if (null != plmobj) {
            LaExtTransactiondetail transobj = transactionDao.getLaExtTransactiondetail(plmobj.getLaExtTransactiondetail().getTransactionid());
            if (null != transobj) {
                return transobj.getProcessid();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/viewer/registration/checkForRegisteredPermission/{landid}", method = RequestMethod.GET)
    @ResponseBody
    public boolean checkForRegisteredPermission(HttpServletRequest request, @PathVariable Long landid) {
        try {
            return regRecordsService.checkForRegisteredPermission(landid);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return false;
        }
    }

    @RequestMapping(value = "/viewer/registration/getPendingPermissionSurrender/{landid}", method = RequestMethod.GET)
    @ResponseBody
    public Permission getPendingPermissionSurrender(HttpServletRequest request, @PathVariable Long landid) {
        try {
            return regRecordsService.getPendingTerminationPermissionByPropId(landid);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        }
    }

    @RequestMapping(value = "/viewer/registration/getPendingPermission/{landid}", method = RequestMethod.GET)
    @ResponseBody
    public Permission getPendingPermission(HttpServletRequest request, @PathVariable Long landid) {
        try {
            return regRecordsService.getPendingPermissionByPropId(landid);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        }
    }

    @RequestMapping(value = "/viewer/registration/getRegisteredPermission/{landid}", method = RequestMethod.GET)
    @ResponseBody
    public Permission getRegisteredPermission(HttpServletRequest request, @PathVariable Long landid) {
        try {
            return regRecordsService.getRegisteredPermissionByPropId(landid);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        }
    }

    @RequestMapping(value = "/viewer/registration/getPermissionByTransaction/{transactionId}", method = RequestMethod.GET)
    @ResponseBody
    public Permission getPermissionByTransactionId(HttpServletRequest request, @PathVariable int transactionId) {
        try {
            return regRecordsService.getPermissionByTransactionId(transactionId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        }
    }

    @RequestMapping(value = "/viewer/registration/getPermissionDocs/{permissionId}", method = RequestMethod.GET)
    @ResponseBody
    public List<SourceDocument> getPermissionDocs(HttpServletRequest request, @PathVariable int permissionId) {
        try {
            Permission p = regRecordsService.getPermissionById(permissionId);
            if (p != null) {
                return sourceDocumentDAO.getDocumentsByTransactionId((long) p.getTransactionid());
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        }
    }

    @RequestMapping(value = "/viewer/registration/saveNewPermission", method = RequestMethod.POST)
    @ResponseBody
    public Permission saveNewPermission(HttpServletRequest request, HttpServletResponse response, Principal principal) {
        return savePermission(request, principal, true);
    }

    @RequestMapping(value = "/viewer/registration/saveSurrenderPermission", method = RequestMethod.POST)
    @ResponseBody
    public Permission saveSurrenderPermission(HttpServletRequest request, HttpServletResponse response, Principal principal) {
        return savePermission(request, principal, false);
    }

    @RequestMapping(value = "/viewer/registration/registerNewPermission", method = RequestMethod.POST)
    @ResponseBody
    public boolean registerNewPermission(HttpServletRequest request, Principal principal) {
        try {
            Integer permissionId = ServletRequestUtils.getIntParameter(request, "permissionId", 0);
            Long userId = userService.findByUniqueName(principal.getName()).getId();
            String regNum = ServletRequestUtils.getStringParameter(request, "permRegNum", "");
            String appNum = ServletRequestUtils.getStringParameter(request, "permAppNum", "");
            String appDateStr = ServletRequestUtils.getStringParameter(request, "permAppDate", "");
            String startDateStr = ServletRequestUtils.getStringParameter(request, "permStartDate", "");
            String endDateStr = ServletRequestUtils.getStringParameter(request, "permEndDate", "");
            String usage = ServletRequestUtils.getStringParameter(request, "permUsage", "");

            DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
            Date appDate = null;
            Date startDate = null;
            Date endDate = null;

            try {
                if (StringUtils.isNotEmpty(appDateStr)) {
                    appDate = dateformat.parse(appDateStr);
                }
                if (StringUtils.isNotEmpty(startDateStr)) {
                    startDate = dateformat.parse(startDateStr);
                }
                if (StringUtils.isNotEmpty(endDateStr)) {
                    endDate = dateformat.parse(endDateStr);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Permission p = regRecordsService.getPermissionById(permissionId);
            p.setRegnum(regNum);
            p.setAppnum(appNum);
            p.setAppdate(appDate);
            p.setStartdate(startDate);
            p.setEnddate(endDate);
            p.setUsage(usage);
            p.setModifiedby(userId.intValue());
            p.setModifieddate(Calendar.getInstance().getTime());

            return regRecordsService.registerPermission(p);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping(value = "/viewer/registration/registerSurrenderPermission", method = RequestMethod.POST)
    @ResponseBody
    public boolean registerSurrenderPermission(HttpServletRequest request, Principal principal) {
        try {
            Integer permissionId = ServletRequestUtils.getIntParameter(request, "surrenderPermissionId", 0);
            Long userId = userService.findByUniqueName(principal.getName()).getId();

            Permission p = regRecordsService.getPermissionById(permissionId);
            p.setModifiedby(userId.intValue());

            return regRecordsService.registerPermission(p);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            return false;
        }
    }

    private Permission savePermission(HttpServletRequest request, Principal principal, boolean isNew) {
        try {
            Integer permissionId = ServletRequestUtils.getIntParameter(request, "permissionId", 0);
            Long userId = userService.findByUniqueName(principal.getName()).getId();

            if (!isNew) {
                Integer permissionSurrenderId = ServletRequestUtils.getIntParameter(request, "surrenderPermissionId", 0);
                if (permissionSurrenderId > 0) {
                    return regRecordsService.getPermissionById(permissionSurrenderId);
                }

                // Create new surrender permission
                Permission p = regRecordsService.getPermissionById(permissionId);

                Permission surrenderPermission = new Permission();
                surrenderPermission.setAppdate(p.getAppdate());
                surrenderPermission.setApplicantid(p.getApplicantid());
                surrenderPermission.setAppnum(p.getAppnum());
                surrenderPermission.setCreatedby(userId.intValue());
                surrenderPermission.setCreateddate(Calendar.getInstance().getTime());
                surrenderPermission.setEnddate(p.getEnddate());
                surrenderPermission.setLandid(p.getLandid());
                surrenderPermission.setOwnerid(p.getOwnerid());
                surrenderPermission.setRegnum(p.getRegnum());
                surrenderPermission.setStartdate(p.getStartdate());
                surrenderPermission.setTerminatedid(p.getId());
                surrenderPermission.setTransactionid(null);
                surrenderPermission.setUsage(p.getUsage());
                surrenderPermission.setActive(true);

                surrenderPermission = regRecordsService.savePermission(surrenderPermission);
                return regRecordsService.getPermissionById(surrenderPermission.getId());
            }

            NaturalPerson applicant = new NaturalPerson();
            Permission permission = null;

            String firstName = ServletRequestUtils.getRequiredStringParameter(request, "perm_firstname");
            String middleName = ServletRequestUtils.getRequiredStringParameter(request, "perm_middlename");
            String lastName = ServletRequestUtils.getRequiredStringParameter(request, "perm_lastname");
            String placeOfBirth = ServletRequestUtils.getRequiredStringParameter(request, "perm_place_of_birth");
            String profession = ServletRequestUtils.getRequiredStringParameter(request, "perm_profession");
            String idDate = ServletRequestUtils.getRequiredStringParameter(request, "perm_id_date");
            String father = ServletRequestUtils.getRequiredStringParameter(request, "perm_father_name");
            String mother = ServletRequestUtils.getRequiredStringParameter(request, "perm_mother_name");
            int nopId = ServletRequestUtils.getRequiredIntParameter(request, "perm_nop");
            String mandateDate = ServletRequestUtils.getRequiredStringParameter(request, "perm_mandate_date");
            String mandateLoc = ServletRequestUtils.getRequiredStringParameter(request, "perm_mandate_loc");
            String idNumber = ServletRequestUtils.getRequiredStringParameter(request, "perm_id_num");
            int genderId = ServletRequestUtils.getRequiredIntParameter(request, "perm_gender");
            String address = ServletRequestUtils.getRequiredStringParameter(request, "perm_address");
            String dobstr = ServletRequestUtils.getRequiredStringParameter(request, "perm_dob");
            int maritalStatusId = ServletRequestUtils.getRequiredIntParameter(request, "perm_marital_status");
            Long landId = ServletRequestUtils.getRequiredLongParameter(request, "landidhide");

            PersonType personType = regRecordsService.getPersonTypeById(1);
            MaritalStatus maritalStatus = regRecordsService.getMaritalStatusByID(maritalStatusId);

            DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateOfBirth = null;
            Date dtIdDate = null;
            Date dtMandateDate = null;

            try {
                if (StringUtils.isNotEmpty(dobstr)) {
                    dateOfBirth = dateformat.parse(dobstr);
                }
                if (StringUtils.isNotEmpty(idDate)) {
                    dtIdDate = dateformat.parse(idDate);
                }
                if (StringUtils.isNotEmpty(mandateDate)) {
                    dtMandateDate = dateformat.parse(mandateDate);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (permissionId > 0) {
                permission = regRecordsService.getPermissionById(permissionId);
                applicant = (NaturalPerson) laPartyDao.getPartyIdByID((long) permission.getApplicantid());
            }

            applicant.setDateofbirth(dateOfBirth);
            applicant.setFirstname(firstName);
            applicant.setMiddlename(middleName);
            applicant.setLastname(lastName);
            applicant.setIsactive(true);
            applicant.setGenderid(genderId);
            applicant.setLaPartygroupMaritalstatus(maritalStatus);
            applicant.setAddress(address);
            applicant.setIdentityno(idNumber);
            applicant.setOwnertype(2);
            applicant.setLaSpatialunitgroup1(regRecordsService.findLaSpatialunitgroupById(1));
            applicant.setLaSpatialunitgroup2(regRecordsService.findLaSpatialunitgroupById(2));
            applicant.setLaSpatialunitgroup3(regRecordsService.findLaSpatialunitgroupById(3));
            applicant.setLaSpatialunitgroupHierarchy1(regRecordsService.findProjectRegionById(1));
            applicant.setLaSpatialunitgroupHierarchy2(regRecordsService.findProjectRegionById(2));
            applicant.setLaSpatialunitgroupHierarchy3(regRecordsService.findProjectRegionById(3));
            applicant.setLaPartygroupPersontype(personType);
            if (nopId > 0) {
                applicant.setNopId(nopId);
            }
            applicant.setIdCardDate(dtIdDate);
            applicant.setFathername(father);
            applicant.setMothername(mother);
            applicant.setMandateDate(dtMandateDate);
            applicant.setMandateLocation(mandateLoc);
            applicant.setBirthPlace(placeOfBirth);
            applicant.setProfession(profession);

            if (permission != null) {
                applicant.setModifiedby(userId.intValue());
                applicant.setModifieddate(Calendar.getInstance().getTime());
                regRecordsService.saveNaturalPerson(applicant);
            } else {
                Long ownerid;
                SocialTenureRelationship ownerRight = regRecordsService.getSocialTenureRelationshipByLandId(landId);

                if (ownerRight != null) {
                    ownerid = ownerRight.getPartyid();
                } else {
                    return null;
                }

                applicant.setCreatedby(userId.intValue());
                applicant.setCreateddate(new Date());
                applicant = regRecordsService.saveNaturalPerson(applicant);

                permission = new Permission();
                permission.setApplicantid(applicant.getPartyid());
                permission.setCreatedby(userId.intValue());
                permission.setCreateddate(Calendar.getInstance().getTime());
                permission.setLandid(landId);
                permission.setOwnerid(ownerid);
                permission.setRegnum("");
                permission.setTransactionid(null);
                permission.setActive(true);

                permission = regRecordsService.savePermission(permission);
            }

            // Re-write permission object with the one containg application object
            return regRecordsService.getPermissionById(permission.getId());

        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }
}
