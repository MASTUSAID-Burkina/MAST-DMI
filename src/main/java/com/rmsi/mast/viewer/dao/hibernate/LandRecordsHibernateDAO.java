package com.rmsi.mast.viewer.dao.hibernate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rmsi.mast.studio.dao.hibernate.GenericHibernateDAO;
import com.rmsi.mast.studio.domain.LaSpatialunitLand;
import com.rmsi.mast.studio.domain.NaturalPerson;
import com.rmsi.mast.studio.domain.SpatialUnit;
import com.rmsi.mast.studio.domain.Status;
import com.rmsi.mast.studio.domain.fetch.CcroOccurrenceStat;
import com.rmsi.mast.studio.domain.fetch.ClaimProfile;
import com.rmsi.mast.studio.domain.fetch.ClaimSummary;
import com.rmsi.mast.studio.domain.fetch.ClaimantAgeStat;
import com.rmsi.mast.studio.domain.fetch.ClaimsStat;
import com.rmsi.mast.studio.domain.fetch.DataCorrectionReport;
import com.rmsi.mast.studio.domain.fetch.DisputeStat;
import com.rmsi.mast.studio.domain.fetch.FarmReport;
import com.rmsi.mast.studio.domain.fetch.GeometryPoint;
import com.rmsi.mast.studio.domain.fetch.LeaseHistoryForFetch;
import com.rmsi.mast.studio.domain.fetch.MortageHistoryForFetch;
import com.rmsi.mast.studio.domain.fetch.NaturalPersonBasic;
import com.rmsi.mast.studio.domain.fetch.OwnerHistoryForFetch;
import com.rmsi.mast.studio.domain.fetch.OwnershipTypeStat;
import com.rmsi.mast.studio.domain.fetch.PersonForEditing;
import com.rmsi.mast.studio.domain.fetch.PersonsReport;
import com.rmsi.mast.studio.domain.fetch.PoiReport;
import com.rmsi.mast.studio.domain.fetch.ProjectDetails;
import com.rmsi.mast.studio.domain.fetch.RegistryBook;
import com.rmsi.mast.studio.domain.fetch.ReportCertificateFetch;
import com.rmsi.mast.studio.domain.fetch.SpatialUnitBasic;
import com.rmsi.mast.studio.domain.fetch.SpatialUnitTable;
import com.rmsi.mast.studio.domain.fetch.TransactionHistoryForFetch;
import com.rmsi.mast.studio.domain.fetch.UploadedDocumentDetailsForFetch;
import com.rmsi.mast.studio.domain.fetch.ApfrStatSummary;
import com.rmsi.mast.studio.domain.fetch.ResourcesStatSummary;
import com.rmsi.mast.studio.domain.fetch.TransactionsStatSummary;
import com.rmsi.mast.studio.util.ClaimsSorter;
import com.rmsi.mast.studio.util.StringUtils;
import com.rmsi.mast.viewer.dao.LaPartyDao;
import com.rmsi.mast.viewer.dao.LandRecordsDao;

@Repository
public class LandRecordsHibernateDAO extends GenericHibernateDAO<SpatialUnitTable, Long>
        implements LandRecordsDao {

    @Autowired
    LaPartyDao lapartydao;

    private static final Logger logger = Logger.getLogger(LandRecordsHibernateDAO.class);

    @SuppressWarnings("unchecked")
    @Override
    public List<SpatialUnitTable> findallspatialUnit(String defaultProject) {

        try {
            Query query = getEntityManager().createQuery("Select su from SpatialUnitTable su where su.project = :project_name and su.active=true order by su.usin desc");
            List<SpatialUnitTable> spatialUnit = query.setParameter("project_name", defaultProject).getResultList();

            if (spatialUnit.size() > 0) {
                return spatialUnit;
            } else {
                return null;
            }
        } catch (Exception e) {

            logger.error(e);
            return null;
        }

    }

    @Override
    public boolean updateApprove(Long id) {

        try {
            Query query = getEntityManager().createQuery("UPDATE SpatialUnitTable su SET su.status.workflowStatusId = :statusId  where su.usin = :usin");
            int updateFinal = query.setParameter("statusId", Status.STATUS_APPROVED).setParameter("usin", id).executeUpdate();

            if (updateFinal > 0) {
                return true;
            }

        } catch (Exception e) {

            logger.error(e);
            return false;
        }

        return false;

    }

    @Override
    public PersonForEditing updatePersonForEditing(PersonForEditing pfe) throws Exception {
        // Check for claim to be editable
        SpatialUnitBasic su = getSpatialUnitBasic(pfe.getLandid());
        NaturalPersonBasic person = getNaturalPersonBasic(pfe.getPersonId());

//      if (su == null) {
//          throw new Exception("Claim was not found");
//      }
//      if (person == null) {
//          throw new Exception("Person was not found");
//      }
//      if (su.getStatus() != Status.STATUS_NEW && su.getStatus() != Status.STATUS_VALIDATED && su.getStatus() != Status.STATUS_REFERRED) {
//          throw new Exception("Record cannot be modified because of underlaying claim status");
//      }
        // Calculate age
        if (pfe.getDob() != null) {
//          pfe.setAge(DateUtils.getAge(pfe.getDob()));
        } else {
//          pfe.setAge(null);
        }

        // Update claim
//      if (StringUtils.isNotEmpty(su.getPropertyno())) {
//          pfe.setHamletId(su.getHamletId());
//      } else {
//          su.setHamletId(pfe.getHamletId());
//      }
//      su.setSurveyDate(pfe.get);
        su.setNeighborNorth(pfe.getNeighborNorth());
        su.setNeighborSouth(pfe.getNeighborSouth());
        su.setNeighborEast(pfe.getNeighborEast());
        su.setNeighborWest(pfe.getNeighborWest());

        getEntityManager().merge(su);

        // Update person
        person.setFirstname(pfe.getFirstName());
        person.setLastname(pfe.getLastName());
        person.setMiddlename(pfe.getMiddleName());
//        person.set(pfe.getIdType());
        person.setIdentityno(pfe.getIdNumber());
        person.setDateofbirth(pfe.getDob());
//        person.setAge(pfe.getAge());
        person.setGenderid(pfe.getGender());
//        person.setMaritalStatus(pfe.getMaritalStatus());

        getEntityManager().merge(person);

        return pfe;
    }

    @Override
    public boolean rejectStatus(Long id) {

        try {
            int finalstatus = 5;
            Query query = getEntityManager().createQuery("UPDATE SpatialUnitTable su SET su.status.workflowStatusId = :statusId  where su.usin = :usin");
            int rejectStatus = query.setParameter("statusId", finalstatus).setParameter("usin", id).executeUpdate();

            if (rejectStatus > 0) {
                return true;
            }
        } catch (Exception e) {

            logger.error(e);
            return false;
        }

        return false;

    }

    @Override
    public List<SpatialUnitTable> search(String usinStr, String ukaNumber,
            String projname, String dateto, String datefrom,
            Long status, String claimType, Integer startpos) {

        ArrayList<Long> newUsin = new ArrayList<>();
        try {
            StringBuilder queryStr = new StringBuilder("Select su from SpatialUnitTable su where su.project = :project_name and su.active=true ");
            if (!"".equals(ukaNumber)) {
                queryStr.append("and su.propertyno like :propertyno ");
            }
            if (!"".equals(usinStr)) {
                queryStr.append("and su.usin in :usin ");
            }
            if (!dateto.isEmpty() || !datefrom.isEmpty()) {
                queryStr.append("and (str(su.surveyDate) BETWEEN :stDate AND :edDate) ");
            }
            if (claimType != null && claimType.length() > 0) {
                queryStr.append("and su.claimType.code=:claimType ");
            }
            if (status != 0) {
                queryStr.append("and su.status.workflowStatusId=:workflowStatusId ");
            }

            queryStr.append("order by su.usin desc ");

            Query query = getEntityManager().createQuery(queryStr.toString());
            query.setParameter("project_name", projname);

            if (!"".equals(ukaNumber)) {
                query.setParameter("propertyno", "%" + ukaNumber.trim() + "%");
            }
            if (!"".equals(usinStr)) {
                for (String retval : usinStr.split(",")) {
                    newUsin.add(Long.parseLong(retval.trim()));
                }
                query.setParameter("usin", newUsin);
            }
            if (!dateto.isEmpty() || !datefrom.isEmpty()) {
                query.setParameter("stDate", datefrom).setParameter("edDate", dateto);
            }
            if (status != 0) {
                query.setParameter("workflowStatusId", status.intValue());
            }
            if (claimType != null && claimType.length() > 0) {
                query.setParameter("claimType", claimType);
            }

            @SuppressWarnings("unchecked")
            List<SpatialUnitTable> spatialUnit = query.setFirstResult(startpos).setMaxResults(10).getResultList();
            if (spatialUnit.size() > 0) {
                return spatialUnit;
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public List<SpatialUnitTable> findSpatialUnitById(Long id) {
        try {
            Query query = getEntityManager().createQuery("Select su from SpatialUnitTable su where su.usin = :usin and su.active = true");
            @SuppressWarnings("unchecked")
            List<SpatialUnitTable> spatialUnitlst = query.setParameter("usin", id).getResultList();

            if (spatialUnitlst.size() > 0) {
                return spatialUnitlst;
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public List<ClaimSummary> getClaimsSummary(Long usin, int startRecord, int endRecord, String projectName, int statusId, String claimType) {
        try {
            Query query = getEntityManager().createQuery("Select cs from ClaimSummary cs where "
                    + "(cs.usin = :usin or :usin = 0) "
                    + "and cs.projectName = :projectName "
                    + "and (:statusId = 0 or cs.statusId = :statusId) "
                    + "and cs.claimType = :claimType");

            List<ClaimSummary> claims
                    = query.setParameter("usin", usin)
                    .setParameter("projectName", projectName)
                    .setParameter("claimType", claimType)
                    .setParameter("statusId", statusId)
                    .getResultList();

            if (claims != null && claims.size() > 0) {
                if (claims.size() > 1) {
                    // Sort by owner name, assuming that list already sorted by hamlets
                    Collections.sort(claims, new ClaimsSorter());
                }

                if (startRecord > endRecord) {
                    int tmpNumber = endRecord;
                    endRecord = startRecord;
                    startRecord = tmpNumber;
                }

                if (startRecord > 1 || endRecord < claims.size()) {
                    // return requested range
                    if (startRecord > claims.size()) {
                        return null;
                    }

                    startRecord = startRecord - 1;

                    if (startRecord < 0) {
                        startRecord = 0;
                    }

                    if (endRecord > claims.size()) {
                        endRecord = claims.size();
                    }

                    return claims.subList(startRecord, endRecord);
                }
            }
            return claims;
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public List<RegistryBook> getRegistryBook(String projectName, long usin) {
        try {
            Query query = getEntityManager().createQuery("Select rb from RegistryBook rb where rb.projectName = :projectName and (:usin = 0L or rb.usin = :usin)");
            return query.setParameter("projectName", projectName).setParameter("usin", usin).getResultList();
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public List<PersonForEditing> getPersonsForEditing(String projectName,
            long landid,
            String firstName,
            String lastName,
            String middleName,
            String idNumber,
            Integer claimNumber,
            String neighbourN,
            String neighbourS,
            String neighbourE,
            String neighbourW) {
        try {
            Query query = getEntityManager().createQuery("Select p from PersonForEditing p where p.projectName = :projectName and "
                    + "(p.landid = :landid or :landid = 0L) and "
                    + "(lower(trim(p.firstName)) like :firstName or p.firstName is null) and "
                    + "(lower(trim(p.lastName)) like :lastName or p.lastName is null) and "
                    + "(lower(trim(p.middleName)) like :middleName or p.middleName is null) and "
                    + "(lower(trim(p.idNumber)) like :idNumber or p.idNumber is null) and "
                    + "(p.claimNumber = :claimNumber or :claimNumber = 0) and "
                    + "(lower(trim(p.neighborNorth)) like :neighbourN or p.neighborNorth is null) and "
                    + "(lower(trim(p.neighborSouth)) like :neighbourS or p.neighborSouth is null) and "
                    + "(lower(trim(p.neighborEast)) like :neighbourE or p.neighborEast is null) and "
                    + "(lower(trim(p.neighborWest)) like :neighbourW or p.neighborWest is null)");
            //setParameter("projectName", CB_Proj)
            String proj = "CB_Proj";
            List<PersonForEditing> result = query
                    .setParameter("projectName", proj)
                    .setParameter("landid", landid)
                    .setParameter("firstName", StringUtils.empty(firstName).toLowerCase() + "%")
                    .setParameter("lastName", StringUtils.empty(lastName).toLowerCase() + "%")
                    .setParameter("middleName", StringUtils.empty(middleName).toLowerCase() + "%")
                    .setParameter("idNumber", StringUtils.empty(idNumber).toLowerCase() + "%")
                    .setParameter("claimNumber", claimNumber)
                    .setParameter("neighbourN", StringUtils.empty(neighbourN).toLowerCase() + "%")
                    .setParameter("neighbourS", StringUtils.empty(neighbourS).toLowerCase() + "%")
                    .setParameter("neighbourE", StringUtils.empty(neighbourE).toLowerCase() + "%")
                    .setParameter("neighbourW", StringUtils.empty(neighbourW).toLowerCase() + "%")
                    .getResultList();
            return result;
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public ProjectDetails getProjectDetails(String projectName) {
        try {
            Query query = getEntityManager().createQuery("Select p from ProjectDetails p where p.name = :projectName");
            return (ProjectDetails) query.setParameter("projectName", projectName).getSingleResult();
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public ClaimProfile getClaimsProfile(String projectName) {
        try {
            ClaimProfile profile = new ClaimProfile();
            if (StringUtils.empty(projectName).equalsIgnoreCase("ALL")) {
                projectName = "";
            }

            // Get claims stat
            StoredProcedureQuery query = getEntityManager().createStoredProcedureQuery("get_claims_stat", ClaimsStat.class)
                    .registerStoredProcedureParameter("projectName", String.class, ParameterMode.IN)
                    .setParameter("projectName", projectName);
            profile.setClaimsStatList(query.getResultList());

            // Get unique claimants
            query = getEntityManager().createStoredProcedureQuery("get_unique_claimants")
                    .registerStoredProcedureParameter("projectName", String.class, ParameterMode.IN)
                    .setParameter("projectName", projectName);
            Object[] result = (Object[]) query.getSingleResult();

            if (result != null && result[0] != null) {
                profile.setUniqueMales((int) result[0]);
            }
            if (result != null && result[1] != null) {
                profile.setUniqueFemales((int) result[1]);
            }

            // Get unique claimants with denied CCRO
            query = getEntityManager().createStoredProcedureQuery("get_unique_claimants_denied")
                    .registerStoredProcedureParameter("projectName", String.class, ParameterMode.IN)
                    .setParameter("projectName", projectName);
            result = (Object[]) query.getSingleResult();

            if (result != null && result[0] != null) {
                profile.setUniqueMalesDenied((int) result[0]);
            }
            if (result != null && result[1] != null) {
                profile.setUniqueFemalesDenied((int) result[1]);
            }

            // Get ownership types stat
            query = getEntityManager().createStoredProcedureQuery("get_ownership_types_approved", OwnershipTypeStat.class)
                    .registerStoredProcedureParameter("projectName", String.class, ParameterMode.IN)
                    .setParameter("projectName", projectName);
            profile.setOwnershipTypeStatList(query.getResultList());

            // Get ccro occurrence stat
            query = getEntityManager().createStoredProcedureQuery("get_ccro_occurrence", CcroOccurrenceStat.class)
                    .registerStoredProcedureParameter("projectName", String.class, ParameterMode.IN)
                    .setParameter("projectName", projectName);
            profile.setCcroOccurrenceStatList(query.getResultList());

            // Get claimants age stat
            query = getEntityManager().createStoredProcedureQuery("get_unique_claimants_with_age", ClaimantAgeStat.class)
                    .registerStoredProcedureParameter("projectName", String.class, ParameterMode.IN)
                    .setParameter("projectName", projectName);
            profile.setClaimantsAgeList(query.getResultList());

            // Get disputes stat
            query = getEntityManager().createStoredProcedureQuery("get_disputes_stat", DisputeStat.class)
                    .registerStoredProcedureParameter("projectName", String.class, ParameterMode.IN)
                    .setParameter("projectName", projectName);
            profile.setDisputes(query.getResultList());

            return profile;
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public String findBiggestUkaNumber(String ukaPrefix) {
        try {
            Query query = getEntityManager().createQuery("Select max(su.propertyno) from SpatialUnitTable su where su.propertyno like :ukaPrefix");
            return (String) query.setParameter("ukaPrefix", ukaPrefix + "%").getSingleResult();
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public SpatialUnit getParcelGeometry(long landid) {
        try {
            //String hql = "SELECT  ST_AsText(geometry),landno FROM la_spatialunit_land where landid = " + landid;

            String hql = "Select ST_AsText(slu.geometry),slu.landno from la_ext_transactiondetails td, la_ext_personlandmapping plm,la_spatialunit_land slu"
                    + "   Where td.transactionID = " + landid + " and  td.transactionID=plm.transactionID And plm.landid=slu.landid";

            List<Object[]> lstObject = getEntityManager().createNativeQuery(hql).getResultList();
            SpatialUnit spatialUnit = new SpatialUnit();
            for (Object[] arrObj : lstObject) {
                spatialUnit.setGeomStr(arrObj[0].toString());
                spatialUnit.setLandno(arrObj[1].toString());
            }
            return spatialUnit;
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    /*@Override
    public SpatialUnit getParcelGeometry(long landid) {
        try {
            Query query = getEntityManager().createQuery("Select su from SpatialUnit su where su.landid = :landid");
            return (SpatialUnit) query.setParameter("landid", landid).getSingleResult();
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }*/
    @Override
    public SpatialUnitBasic getSpatialUnitBasic(Long usin) {
        try {
            Query query = getEntityManager().createQuery("Select su from SpatialUnitBasic su where su.landid = :usin");
            @SuppressWarnings("unchecked")
            SpatialUnitBasic result = (SpatialUnitBasic) query.setParameter("usin", usin).getSingleResult();
            return result;
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public NaturalPersonBasic getNaturalPersonBasic(Long id) {
        try {
            Query query = getEntityManager().createQuery("Select p from NaturalPersonBasic p where p.personid = :id");
            @SuppressWarnings("unchecked")
            NaturalPersonBasic result = (NaturalPersonBasic) query.setParameter("id", id).getSingleResult();
            return result;
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public SpatialUnitTable getSpatialUnit(Long id) {
        try {
            Query query = getEntityManager().createQuery("Select su from SpatialUnitTable su where su.usin = :usin and su.active = true");
            @SuppressWarnings("unchecked")
            List<SpatialUnitTable> spatialUnitlst = query.setParameter("usin", id).getResultList();

            if (spatialUnitlst.size() > 0) {
                return spatialUnitlst.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public String findukaNumberByUsin(Long id) {

        try {
            Query query = getEntityManager().createQuery("Select su from SpatialUnitTable su where su.usin = :usin and su.active = true");
            @SuppressWarnings("unchecked")
            List<SpatialUnitTable> spatialUnitlst = query.setParameter("usin", id).getResultList();
            //String uka = spatialUnitlst.get(0).getPropertyno();

            if (spatialUnitlst.size() > 0) {
                //@@return uka;
                return null;
            } else {
                return "";
            }
        } catch (Exception e) {

            logger.error(e);
            return "";
        }

    }

    @Override
    public boolean updateFinal(Long id) {

        try {
            int finalstatus = 7;
            Query query = getEntityManager().createQuery("UPDATE SpatialUnitTable su SET su.status.workflowStatusId = :statusId  where su.usin = :usin");
            int updateFinal = query.setParameter("statusId", finalstatus).setParameter("usin", id).executeUpdate();

            if (updateFinal > 0) {
                return true;
            }
        } catch (Exception e) {

            logger.error(e);
            return false;
        }

        return false;

    }

    @Override
    public boolean updateAdjudicated(Long id) {

        try {
            int finalstatus = 2;
            Query query = getEntityManager().createQuery("UPDATE SpatialUnitTable su SET su.status.workflowStatusId = :statusId  where su.usin = :usin");
            int updateFinal = query.setParameter("statusId", finalstatus).setParameter("usin", id).executeUpdate();

            if (updateFinal > 0) {
                return true;
            }
        } catch (Exception e) {

            logger.error(e);
            return false;
        }

        return false;

    }

    @Override
    public boolean deleteSpatial(Long id) {

        try {
            Query query = getEntityManager().createQuery("UPDATE SpatialUnitTable su SET su.active = false  where su.usin = :usin");
            int updateFinal = query.setParameter("usin", id).executeUpdate();

            if (updateFinal > 0) {
                return true;
            }
        } catch (Exception e) {

            logger.error(e);
            return false;
        }
        return false;
    }

    @Override
    public Integer searchSize(String usinStr, String ukaNumber,
            String projname, String dateto, String datefrom, Long status, String claimType) {

        Integer count = 0;

        ArrayList<Long> newUsin = new ArrayList<Long>();
        try {
            // and (str(su.surveyDate) BETWEEN :stDate AND :edDate) and su.project = :project_name ")
            StringBuffer queryStr = new StringBuffer("Select count(*) from SpatialUnitTable su where su.project = :project_name and su.active=true ");
            /* if (ukaNumber != "") {
                queryStr.append("and su.propertyno like :propertyno ");
            }
            if (usinStr != "") {

                queryStr.append("and su.usin in :usin ");

            }
            if (!dateto.isEmpty() || !datefrom.isEmpty()) {
                queryStr.append("and (str(su.surveyDate) BETWEEN :stDate AND :edDate) ");
            }*/
            if (status != 0) {
                queryStr.append("and su.status.workflowStatusId=:workflowStatusId ");
            }
            if (claimType != null && claimType.length() > 0) {
                queryStr.append("and su.claimType.code=:claimType ");
            }

            Query query = getEntityManager().createQuery(queryStr.toString());
            // query.setParameter("project_name", projname);

            /*  if (ukaNumber != "") {
                query.setParameter("propertyno", "%" + ukaNumber + "%");
            }
            if (usinStr != "") {
                for (String retval : usinStr.split(",")) {
                    newUsin.add(Long.parseLong(retval));
                }
                query.setParameter("usin", newUsin);
            }
            if (!dateto.isEmpty() || !datefrom.isEmpty()) {
                query.setParameter("stDate", datefrom).setParameter("edDate", dateto);
            }*/
            if (status != 0) {
                query.setParameter("workflowStatusId", status.intValue());
            }
            if (claimType != null && claimType.length() > 0) {
                query.setParameter("claimType", claimType);
            }

            @SuppressWarnings("unchecked")
            List<?> spatialUnit = query.getResultList();
            if (spatialUnit.size() > 0) {
                count = Integer.valueOf(spatialUnit.get(0).toString());
            }

        } catch (Exception e) {

            logger.error(e);

        }

        return count;

    }

    @Override
    public List<SpatialUnitTable> getSpatialUnitByBbox(String bbox, String project_name) {

        List<SpatialUnitTable> spatialUnit = new ArrayList<SpatialUnitTable>();
        try {

            Query query = getEntityManager().createNativeQuery("SELECT * from spatial_unit where ST_WITHIN(the_geom, ST_MakeEnvelope(" + bbox + ",4326)) and (project_name=" + "'" + project_name + "'" + " and active=true) ", SpatialUnitTable.class);
            spatialUnit = query.getResultList();
        } catch (Exception e) {
            logger.error(e);
        }

        return spatialUnit;

    }

    @Override
    public boolean findExistingHamlet(long hamlet_id) {
        try {
            Query query = getEntityManager().createQuery("Select su from SpatialUnitTable su where su.hamlet_Id.id = :hamlet_id");
            @SuppressWarnings("unchecked")
            List<SpatialUnitTable> spatialUnitlst = query.setParameter("hamlet_id", hamlet_id).getResultList();
            if (spatialUnitlst.size() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {

            logger.error(e);
            return false;
        }
    }

    @Override
    public boolean deleteAllVertexLabel() {

        try {
            Query query = getEntityManager().createNativeQuery("DELETE FROM vertexlabel");
            int spatialUnit = query.executeUpdate();

            if (spatialUnit > 0) {
                return true;
            } else {
                return true;
            }
        } catch (Exception e) {

            logger.error(e);
            return false;
        }
    }

    @Override
    public boolean addAllVertexLabel(int k, String lat, String lon) {
        try {
            Query query = getEntityManager().createNativeQuery("insert into vertexlabel(gid,the_geom) values(" + k + ",ST_SetSRID(ST_MakePoint(" + lon + "," + lat + "), 4326));");
            int spatialUnit = query.executeUpdate();

            if (spatialUnit > 0) {
                return true;
            } else {
                return true;
            }
        } catch (Exception e) {

            logger.error(e);
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LaSpatialunitLand> search(
            String lang,
            Integer claimType,
            int project,
            String parcelId,
            int villageId,
            String appNum,
            String pvNum,
            String apfrNum,
            String firstName,
            int appType,
            int appStatus,
            int workflowId,
            Integer startpos) {

        String sql;
        String strWhere = "";

        if (claimType > 0) {
            strWhere = strWhere + " and LC.claimtypeid = " + claimType;
        }

        if (appStatus > 0) {
            strWhere = strWhere + " and LD.applicationstatusid = " + appStatus;
        }

        if (workflowId > 0) {
            strWhere = strWhere + " and LD.workflowstatusid = " + workflowId;
        }

        if (villageId > 0) {
            strWhere = strWhere + " and LD.hierarchyid5 = :villageId";
        }

        if (!"".equals(parcelId)) {
            String strPattern = "^0+";
            parcelId = parcelId.replaceAll(strPattern, "");
            strWhere = strWhere + " and LD.landid = " + parcelId;
        }

        if (!StringUtils.isEmpty(appNum)) {
            strWhere = strWhere + " and coalesce(LD.application_no, '') like :appNum";
        }

        if (!StringUtils.isEmpty(pvNum)) {
            strWhere = strWhere + " and coalesce(LD.pv_no, '') like :pvNum";
        }

        if (!StringUtils.isEmpty(apfrNum)) {
            strWhere = strWhere + " and coalesce(PL.certificateno, '') like :apfrNum";
        }

        if (!StringUtils.isEmpty(firstName)) {
            strWhere = strWhere + " and (coalesce(LP.firstname, '') like :firstName or coalesce(LE.organizationname, '') like :firstName)";
        }

        if (appType > 0) {
            strWhere = strWhere + " and PL.share_type_id = " + appType;
        }

        sql = "select LD.landid, LD.landno, LC.claimtypeid, (case when :lang = 'en' then LC.claimtype_en else LC.claimtype end) as claimtype, LD.area, "
                + "  (case when :lang = 'en' then la.applicationstatus_en else la.applicationstatus end) as applicationstatus,"
                + "  trim(coalesce(LP.firstname, '') ||' '|| coalesce(LP.lastname, '') || ' ' || coalesce(LE.organizationname, '')) as fullname, "
                + "  trim(coalesce(LP.address,'') || ' ' || coalesce(LE.address,'')) as address, la.applicationstatusid ,LD.workflowstatusid,"
                + "  (case when :lang = 'en' then lf.workflow_en else lf.workflow end) as workflow_status,"
                + "  TR.transactionid, (case when :lang = 'en' then ST.landsharetype_en else ST.landsharetype end) as apptype, PL.share_type_id as apptypeid,"
                + "  LD.application_no, LD.pv_no, PL.certificateno, LD.section, LD.parcel_no_in_section, "
                + "  (case when :lang = 'en' then v.name_en else v.name end) as villagename "
                + "from la_spatialunit_land LD"
                + "  inner join la_ext_personlandmapping PL on LD.landid = PL.landid"
                + "  inner Join la_right_claimtype LC on LD.claimtypeid=LC.claimtypeid"
                + "  inner Join la_ext_applicationstatus la on la.applicationstatusid =  LD.applicationstatusid"
                + "  inner Join la_ext_workflow lf on lf.workflowid =  LD.workflowstatusid"
                + "  inner Join la_right_landsharetype ST on PL.share_type_id =  ST.landsharetypeid"
                + "  left Join  la_party_person LP on PL.partyid = LP.personid"
                + "  left Join  la_party_organization LE on PL.partyid = LE.organizationid"
                + "  inner join la_ext_transactiondetails TR on PL.transactionid = TR.transactionid "
                + "  left join public.la_spatialunitgroup_hierarchy v on LD.hierarchyid5 = v.hierarchyid "
                + "where PL.isactive=true and PL.persontypeid=1 and LD.isactive=true and LD.projectnameid = :projectId"
                + strWhere
                + " order by LD.landid desc";

        Query q = getEntityManager().createNativeQuery(sql);
        q.setParameter("projectId", project);

        if (!StringUtils.isEmpty(appNum)) {
            q.setParameter("appNum", appNum + "%");
        }

        if (villageId > 0) {
            q.setParameter("villageId", villageId);
        }

        if (!StringUtils.isEmpty(pvNum)) {
            q.setParameter("pvNum", pvNum + "%");
        }

        if (!StringUtils.isEmpty(apfrNum)) {
            q.setParameter("apfrNum", apfrNum + "%");
        }

        if (!StringUtils.isEmpty(firstName)) {
            q.setParameter("firstName", "%" + firstName + "%");
        }

        if (!StringUtils.isEmpty(lang)) {
            q.setParameter("lang", lang);
        } else {
            q.setParameter("lang", "en");
        }

        try {
            List<Object[]> arrObject = q.setFirstResult(startpos).setMaxResults(10).getResultList();
            List<LaSpatialunitLand> lstLaSpatialunitLand = new ArrayList<>();

            for (Object[] object : arrObject) {
                LaSpatialunitLand laSpatialunitLand = new LaSpatialunitLand();
                laSpatialunitLand.setLandid(Long.valueOf(object[0].toString()));
                laSpatialunitLand.setLandno((String) object[1]);
                laSpatialunitLand.setClaimtypeid(Integer.valueOf(object[2].toString()));
                laSpatialunitLand.setClaimtype_en(object[3].toString());
                laSpatialunitLand.setArea(4);
                laSpatialunitLand.setApplicationstatus_en(object[5].toString());
                //laSpatialunitLand.setTransactionid(Integer.valueOf(object[6].toString()));
                if (null != object[6]) {
                    laSpatialunitLand.setFirstname(object[6].toString());
                } else {
                    laSpatialunitLand.setFirstname("");
                }

                if (null != object[7]) {
                    laSpatialunitLand.setAddress(object[7].toString());
                } else {
                    laSpatialunitLand.setAddress("");
                }

                laSpatialunitLand.setApplicationstatusid(Integer.valueOf(object[8].toString()));
                laSpatialunitLand.setWorkflowstatusid(Integer.valueOf(object[9].toString()));
                laSpatialunitLand.setWorkflowstatus(object[10].toString());
                laSpatialunitLand.setTransactionid(Integer.valueOf(object[11].toString()));
                laSpatialunitLand.setLandnostrwithzero(addZeroinLandNo((String) object[0].toString()));
                laSpatialunitLand.setRightShareType(object[12].toString());
                laSpatialunitLand.setRightShareTypeId(Integer.valueOf(object[13].toString()));
                if (object[14] != null) {
                    laSpatialunitLand.setAppNum(object[14].toString());
                }
                if (object[15] != null) {
                    laSpatialunitLand.setPvNum(object[15].toString());
                }
                if (object[16] != null) {
                    laSpatialunitLand.setApfrNum(object[16].toString());
                }
                if (object[17] != null) {
                    laSpatialunitLand.setSection(Integer.valueOf(object[17].toString()));
                }
                if (object[18] != null) {
                    laSpatialunitLand.setParcelNoInSection(Long.valueOf(object[18].toString()));
                }
                if (object[19] != null) {
                    laSpatialunitLand.setVillageName(object[19].toString());
                }
                lstLaSpatialunitLand.add(laSpatialunitLand);

            }
            return lstLaSpatialunitLand;

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        }
    }

    @Override
    public List<ReportCertificateFetch> getCertificatedetailsbytransactionid(Long usin) {
        String sql;

        try {
            sql = " select row_number() OVER () as rnum, td.transactionid as usin,plm.certificateno,ld.landno,CURRENT_DATE as Date,ld.area,lut.landusetype,ld.neighbor_east,ld.neighbor_west,ld.neighbor_north,"
                    + " ld.neighbor_south,plm.sharepercentage,ps.firstname,ps.middlename,ps.lastname,ps.address, parea.landofficersignature, ps.personid as partyid,	ld.createddate as capture_date, "
                    + " lst.landsharetype_en as landsharetype,g.gender,EXTRACT(YEAR from AGE(CURRENT_DATE, ps.dateofbirth)) as age,ms.maritalstatus,"
                    + " id.identitytype,ps.identityno,CURRENT_DATE as dateofregistration, 0 as duration,ps.contactno,lt.landtype,hie1.name as country,"
                    + " hie2.name as region,hie3.name as province,hie4.name as commune,hie5.name as place,ld.udparcelno"
                    + " from la_ext_transactiondetails td"
                    + " inner join la_ext_personlandmapping plm on plm.transactionid=td.transactionid"
                    + " inner join la_spatialunit_land ld on ld.landid=plm.landid"
                    + " inner join la_Party_person ps on ps.personid=plm.partyid"
                    + " inner join la_ext_projectarea parea on parea.projectnameid= ld.projectnameid"
                    + " left join la_baunit_landusetype lut on lut.landusetypeid=ld.landusetypeid"
                    + " left join la_right_landsharetype lst on lst.landsharetypeid=plm.share_type_id"
                    + " left join la_partygroup_gender g on g.genderid=ps.genderid"
                    + " left join la_partygroup_maritalstatus ms on ms.maritalstatusid=ps.maritalstatusid"
                    + " left join la_partygroup_identitytype id on id.identitytypeid=ps.identitytypeid"
                    + " left join la_baunit_landtype lt on lt.landtypeid=ld.landtypeid"
                    + " left join la_spatialunitgroup_hierarchy hie1 on hie1.hierarchyid=ld.hierarchyid1 and ld.spatialunitgroupid1=1"
                    + " left join la_spatialunitgroup_hierarchy hie2 on hie2.hierarchyid=ld.hierarchyid2 and ld.spatialunitgroupid2=2"
                    + " left join la_spatialunitgroup_hierarchy hie3 on hie3.hierarchyid=ld.hierarchyid3 and ld.spatialunitgroupid3=3"
                    + " left join la_spatialunitgroup_hierarchy hie4 on hie4.hierarchyid=ld.hierarchyid4 and ld.spatialunitgroupid4=4"
                    + " left join la_spatialunitgroup_hierarchy hie5 on hie5.hierarchyid=ld.hierarchyid5 and ld.spatialunitgroupid5=5"
                    + " where td.transactionid=" + usin;

            Query query = getEntityManager().createNativeQuery(sql, ReportCertificateFetch.class);
            List<ReportCertificateFetch> attribValues = query.getResultList();

            if (attribValues.size() > 0) {
                return attribValues;
            } else {
                return null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.error(e);
            return null;
        }
    }

    @Override
    public List<ReportCertificateFetch> getCertificatedetailsinbatch(Long startRecord, Long endRecord) {
        String sql;

        try {
            sql = " select row_number() OVER () as rnum,td.transactionid as usin,plm.certificateno,ld.landno,CURRENT_DATE as Date,ld.area,lut.landusetype,ld.neighbor_east,ld.neighbor_west,ld.neighbor_north,"
                    + " ld.neighbor_south,plm.sharepercentage,ps.firstname,ps.middlename,ps.lastname,ps.address, parea.landofficersignature, ps.personid as partyid,	ld.createddate as capture_date, "
                    + " lst.landsharetype_en as landsharetype,g.gender,EXTRACT(YEAR from AGE(CURRENT_DATE, ps.dateofbirth)) as age,ms.maritalstatus,"
                    + " id.identitytype,ps.identityno,CURRENT_DATE as dateofregistration, 0 as duration,ps.contactno,lt.landtype,hie1.name as country,"
                    + " hie2.name as region,hie3.name as province,hie4.name as commune,hie5.name as place,ld.udparcelno"
                    + " from la_ext_transactiondetails td"
                    + " inner join la_ext_personlandmapping plm on plm.transactionid=td.transactionid"
                    + " inner join la_spatialunit_land ld on ld.landid=plm.landid"
                    + " inner join la_Party_person ps on ps.personid=plm.partyid"
                    + " inner join la_ext_projectarea parea on parea.projectnameid= ld.projectnameid"
                    + " left join la_baunit_landusetype lut on lut.landusetypeid=ld.landusetypeid"
                    + " left join la_right_landsharetype lst on lst.landsharetypeid=plm.share_type_id"
                    + " left join la_partygroup_gender g on g.genderid=ps.genderid"
                    + " left join la_partygroup_maritalstatus ms on ms.maritalstatusid=ps.maritalstatusid"
                    + " left join la_partygroup_identitytype id on id.identitytypeid=ps.identitytypeid"
                    + " left join la_baunit_landtype lt on lt.landtypeid=ld.landtypeid"
                    + " left join la_spatialunitgroup_hierarchy hie1 on hie1.hierarchyid=ld.hierarchyid1 and ld.spatialunitgroupid1=1"
                    + " left join la_spatialunitgroup_hierarchy hie2 on hie2.hierarchyid=ld.hierarchyid2 and ld.spatialunitgroupid2=2"
                    + " left join la_spatialunitgroup_hierarchy hie3 on hie3.hierarchyid=ld.hierarchyid3 and ld.spatialunitgroupid3=3"
                    + " left join la_spatialunitgroup_hierarchy hie4 on hie4.hierarchyid=ld.hierarchyid4 and ld.spatialunitgroupid4=4"
                    + " left join la_spatialunitgroup_hierarchy hie5 on hie5.hierarchyid=ld.hierarchyid5 and ld.spatialunitgroupid5=5"
                    + " where td.transactionid between " + startRecord + " and " + endRecord;

            Query query = getEntityManager().createNativeQuery(sql, ReportCertificateFetch.class);
            List<ReportCertificateFetch> attribValues = query.getResultList();

            if (attribValues.size() > 0) {
                return attribValues;
            } else {
                return null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.error(e);
            return null;
        }
    }

    @Override
    public List<Object> findsummaryreport(String projectnameid) {
        try {
            List<Object> spatialUnit;

            String query = "select hr.name as Commune_Name,count(*) as parcels, "
                    + " round(((count(*)*100)/(select count(*) from la_spatialunit_land where isactive='1' and projectnameid= :projectnameid)\\:\\:numeric),2) as total_percentage,"
                    + " sum(ld.area) as Total_Area_Mapped, sum(ld.area)/count(*) as Parcel_Average_Size, "
                    + " (select count(*) from la_spatialunit_land where applicationstatusid=2 and isactive='1') as APFR_Issued "
                    + " from la_spatialunit_land ld inner join la_spatialunitgroup_hierarchy hr on hr.hierarchyid=ld.hierarchyid4"
                    + " where ld.isactive='1' and hr.isactive='1' and ld.projectnameid= :projectnameid"
                    + " group by hr.name";

            //spatialUnit = getEntityManager().createQuery(query).getResultList();
            Query executeQuery = getEntityManager().createNativeQuery(query);
            executeQuery.setParameter("projectnameid", Integer.parseInt(projectnameid));
            spatialUnit = executeQuery.getResultList();
            if (spatialUnit.size() > 0) {

                return spatialUnit;
            } else {

                return null;
            }
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public List<Object> findprojectdetailedsummaryreport(String projectnameid) {
        try {
            List<Object> spatialUnit;

            String query = "select lst.landsharetype_en Tenure_Type,gd.gender_en,count(*) parcels, "
                    + " round(((count(*)*100)/(select count(*) from la_spatialunit_land where isactive='1' and projectnameid= :projectnameid)\\:\\:numeric),2) as total_percentage,"
                    + " sum(ld.area) Total_Area_Mapped, sum(ld.area)/count(*) Parcel_Average_Size, (select count(*) from la_spatialunit_land where applicationstatusid=2 and isactive='1') APFR_Issued"
                    + " from la_spatialunit_land ld inner join la_ext_personlandmapping plm on plm.landid=ld.landid inner join la_right_landsharetype lst on lst.landsharetypeid=plm.share_type_id"
                    + " inner join la_party_person ps on ps.personid=plm.partyid inner join la_partygroup_gender gd on gd.genderid=ps.genderid "
                    + " where plm.persontypeid=1 and plm.isactive='1' and ld.isactive='1' and lst.isactive='1' and ld.projectnameid= :projectnameid group by lst.landsharetype_en,gd.gender_en;";

            Query executeQuery = getEntityManager().createNativeQuery(query);
            executeQuery.setParameter("projectnameid", Integer.parseInt(projectnameid));
            spatialUnit = executeQuery.getResultList();
            if (spatialUnit.size() > 0) {

                return spatialUnit;
            } else {

                return null;
            }
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public List<Object> findprojectapplicationstatussummaryreport(String projectnameid) {
        try {
            List<Object> spatialUnit;

            String query = "select distinct (select count(applicationstatusid) from la_spatialunit_land where applicationstatusid=1) New_application,(select count(applicationstatusid) from la_spatialunit_land where applicationstatusid=2) Approved_application,"
                    + " (select count(applicationstatusid) from la_spatialunit_land where applicationstatusid=3) reject_application,(select count(applicationstatusid) from la_spatialunit_land where applicationstatusid=4) Pending_application,"
                    + " (select count(applicationstatusid) from la_spatialunit_land where applicationstatusid=5) registred_application from la_spatialunit_land ld"
                    + " where ld.isactive='1' and ld.projectnameid=" + projectnameid;

            Query executeQuery = getEntityManager().createNativeQuery(query);
            //executeQuery.setParameter("projectnameid", Integer.parseInt(projectnameid));
            spatialUnit = executeQuery.getResultList();
            if (spatialUnit.size() > 0) {

                return spatialUnit;
            } else {

                return null;
            }
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public List<Object> findprojectapplicationtypesummaryreport(String projectnameid) {
        try {
            List<Object> spatialUnit;

            String query = "select distinct (select count(claimtypeid) from la_spatialunit_land where claimtypeid=1) Disputedclaim,(select count(claimtypeid) from la_spatialunit_land where claimtypeid=2) Existingright,"
                    + " (select count(claimtypeid) from la_spatialunit_land where claimtypeid=3) Newclaim,(select count(claimtypeid) from la_spatialunit_land where claimtypeid=4) Unclaimed,"
                    + " (select count(claimtypeid) from la_spatialunit_land where claimtypeid=5) NoClaim from la_spatialunit_land ld where ld.isactive='1' and ld.projectnameid=" + projectnameid;

            Query executeQuery = getEntityManager().createNativeQuery(query);
            //executeQuery.setParameter("projectnameid", Integer.parseInt(projectnameid));
            spatialUnit = executeQuery.getResultList();
            if (spatialUnit.size() > 0) {

                return spatialUnit;
            } else {

                return null;
            }
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public List<Object> findprojectworkflowsummaryreport(String projectnameid) {
        try {
            List<Object> spatialUnit;

            String query = "select ld.landno,ld.createddate Capture_Data, lw1.statuschangedate Processing_Date, lw2.statuschangedate Approval_Date, plm.certificateissuedate certificate_date  from la_spatialunit_land ld"
                    + " inner join la_ext_personlandmapping plm on plm.landid=ld.landid and plm.isactive='1' inner join la_ext_landworkflowhistory lw1 on lw1.landid=ld.landid and lw1.workflowid=2"
                    + " inner join la_ext_landworkflowhistory lw2 on lw2.landid=ld.landid and lw2.applicationstatusid=2 where ld.isactive='1' and ld.projectnameid=" + projectnameid;

            Query executeQuery = getEntityManager().createNativeQuery(query);
            //executeQuery.setParameter("projectnameid", Integer.parseInt(projectnameid));
            spatialUnit = executeQuery.getResultList();
            if (spatialUnit.size() > 0) {

                return spatialUnit;
            } else {

                return null;
            }
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public List<Object> findprojectTenureTypesLandUnitsummaryreport(String projectnameid) {
        try {
            List<Object> spatialUnit;

            String query = "select ld.landno,Concat(ps.firstname,' ',ps.middlename,' ',ps.lastname) AS name,g.gender,lst.landsharetype_en as landsharetype,EXTRACT(YEAR from AGE(CURRENT_DATE, ps.dateofbirth)) as age"
                    + " from la_spatialunit_land ld inner join la_ext_personlandmapping plm on plm.landid=ld.landid inner join la_Party_person ps on ps.personid=plm.partyid"
                    + " left join la_right_landsharetype lst on lst.landsharetypeid=plm.share_type_id left join la_partygroup_gender g on g.genderid=ps.genderid"
                    + " where ld.projectnameid=" + projectnameid;

            Query executeQuery = getEntityManager().createNativeQuery(query);
            spatialUnit = executeQuery.getResultList();
            if (spatialUnit.size() > 0) {

                return spatialUnit;
            } else {

                return null;
            }
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public List<Object> findLiberiaFarmummaryreport(String projectnameid) {
        try {
            List<Object> spatialUnit;

            String query = "Select Distinct RA.landID,cast(srl.createddate as date),u.username, (Select attributevalue from la_ext_resource_custom_attributevalue where landid=RA.landID and attributeoptionsid in (100,121,130,139,148,157)) as EnterpriseFarmName,"
                    + " h1.name as county,h2.name as District,h3.name as Clanname,h4.name as community,h5.name as town,rc.classificationname,rsc.subclassificationname,area,ac.categoryname,"
                    + " case when ac.attributecategoryid in (10,17) then 'Natural' when ac.attributecategoryid in (14,18) then 'Non-Natural' end as persontype,"
                    + " (Select attributevalue from la_ext_resourceattributevalue Where landid=RA.landID and attributemasterid in (1063,1017,1035,1079,1088,1097,1108) and groupid=RA.groupid) ||' '||"
                    + " (Select attributevalue from la_ext_resourceattributevalue Where landid=RA.landID and attributemasterid in (1065,1018,1036,1080,1089,1109,1098) and groupid=RA.groupid) ||' '||"
                    + " (Select attributevalue from la_ext_resourceattributevalue Where landid=RA.landID and attributemasterid in (1066,1019,1037,1081,1090,1099,1110) and groupid=RA.groupid) as OwnerName,"
                    + " (Select case when count(*)=0 then 'No' else 'Yes' end as ispoi from la_ext_resourcepoiattributevalue where landID=RA.LandID ) as IsPOI,"
                    + " (Select attributevalue from la_ext_resourceattributevalue Where landid=RA.landID and attributemasterid in (1022,1064,1116,22) and groupid=RA.groupid) as MaritalStatus, "
                    + " (Select count(distinct groupid) from la_ext_resourcepoiattributevalue where landID=RA.LandID ) as POICount,"
                    + " (Select attributevalue from la_ext_resourceattributevalue Where landid=RA.landID and attributemasterid in (4,1020,1067,1119) and groupid=RA.groupid) as Gender,"
                    + " (Select attributevalue from la_ext_resourceattributevalue Where landid=RA.landID and attributemasterid in (1024,1059,1070,1122) and groupid=RA.groupid) as Etnicity,"
                    + " (Select attributevalue from la_ext_resourceattributevalue Where landid=RA.landID and attributemasterid in (43,1025,1071,1123) and groupid=RA.groupid) as Resisdent,"
                    + " (Select attributevalue from la_ext_resourceattributevalue Where landid=RA.landID and attributemasterid in (1021,1068,1120,1129) and groupid=RA.groupid) as DOB,"
                    + " (Select attributevalue from la_ext_resourceattributevalue Where landid=RA.landID and attributemasterid in (5,8,1030,1042,1051,1073,1086,1095,1105,1125) and groupid=RA.groupid) as Mobile,"
                    + " (Select attributevalue from la_ext_resource_custom_attributevalue where landid=RA.landID and attributeoptionsid in (103,122,131,140,149,158)) as primarycrop,"
                    + " (Select attributevalue from la_ext_resource_custom_attributevalue where landid=RA.landID and attributeoptionsid in (104,123,132,141,150,159)) as primarycropdate,"
                    + " (Select attributevalue from la_ext_resource_custom_attributevalue where landid=RA.landID and attributeoptionsid in (105,124,133,142,151,160)) as primarycropduration,"
                    + " (Select attributevalue from la_ext_resource_custom_attributevalue where landid=RA.landID and attributeoptionsid in (116,125,134,143,152,161)) as seccrop,"
                    + " (Select attributevalue from la_ext_resource_custom_attributevalue where landid=RA.landID and attributeoptionsid in (117,126,135,144,153,162)) as seccropdate,"
                    + " (Select attributevalue from la_ext_resource_custom_attributevalue where landid=RA.landID and attributeoptionsid in (118,127,136,145,154,163)) as seccropduration, "
                    + " (Select attributevalue from la_ext_resource_custom_attributevalue where landid=RA.landID and attributeoptionsid in (119,128,137,146,155,164)) as TotalExpenditure,"
                    + " (Select attributevalue from la_ext_resource_custom_attributevalue where landid=RA.landID and attributeoptionsid in (120,129,138,147,156,165)) as TotalSale"
                    + " from la_spatialunit_resource_land srl,la_ext_resourceattributevalue RA,la_ext_attributemaster AM,la_ext_attributecategory ac"
                    + " ,la_ext_user u,la_spatialunitgroup_hierarchy h1 ,la_spatialunitgroup_hierarchy h2,la_spatialunitgroup_hierarchy h3,la_spatialunitgroup_hierarchy h4,la_spatialunitgroup_hierarchy h5 ,la_ext_resourcelandclassificationmapping rlcm"
                    + " ,la_ext_resourceclassification rc ,la_ext_resourcesubclassification rsc "
                    + " Where srl.LandID=RA.landID and rlcm.landid=srl.landid and rc.classificationid=rlcm.classificationid and rsc.subclassificationid=rlcm.subclassificationid"
                    + " And u.userid=srl.createdby And h1.hierarchyid=srl.hierarchyid1 and h1.spatialunitgroupid=1 And h2.hierarchyid=srl.hierarchyid2 and h2.spatialunitgroupid=2"
                    + " And h3.hierarchyid=srl.hierarchyid3 and h3.spatialunitgroupid=3 And h4.hierarchyid=srl.hierarchyid4 and h4.spatialunitgroupid=4 And h5.hierarchyid=srl.hierarchyid5 and h5.spatialunitgroupid=5"
                    + " AND RA.AttributeMasterID=AM.AttributeMasterID AND AM.AttributeCategoryID=ac.AttributeCategoryID AND srl.isactive = true and RA.projectid = " + projectnameid + ""
                    + " Order by Ra.Landid";

            Query executeQuery = getEntityManager().createNativeQuery(query);
            spatialUnit = executeQuery.getResultList();
            if (spatialUnit.size() > 0) {

                return spatialUnit;
            } else {

                return null;
            }
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public List<Object> findprojectdetailedsummaryreportForCommune(String hierarchyid) {
        try {
            List<Object> spatialUnit;

            String query = "select lst.landsharetype_en Tenure_Type,gd.gender_en,hr.name Commune_Name,count(*) parcels,"
                    + " round(((count(*)*100)/(select count(*) from la_spatialunit_land where isactive='1' and hierarchyid= :hierarchyid)\\:\\:numeric),2) as total_percentage,"
                    + " sum(ld.area) Total_Area_Mapped, sum(ld.area)/count(*) Parcel_Average_Size, (select count(*) from la_spatialunit_land where applicationstatusid=2 and isactive='1') APFR_Issued from la_spatialunit_land ld"
                    + " inner join la_ext_personlandmapping plm on plm.landid=ld.landid inner join la_right_landsharetype lst on lst.landsharetypeid=plm.share_type_id "
                    + " inner join la_party_person ps on ps.personid=plm.partyid inner join la_partygroup_gender gd on gd.genderid=ps.genderid "
                    + " inner join la_spatialunitgroup_hierarchy hr on hr.hierarchyid=ld.hierarchyid4 "
                    + " where plm.persontypeid=1 and plm.isactive='1' and ld.isactive='1' and lst.isactive='1' and hr.isactive='1' and hr.hierarchyid= :hierarchyid"
                    + " group by lst.landsharetype_en,gd.gender_en,hr.name,hr.hierarchyid;";

            Query executeQuery = getEntityManager().createNativeQuery(query);
            executeQuery.setParameter("hierarchyid", Integer.parseInt(hierarchyid));
            spatialUnit = executeQuery.getResultList();
            if (spatialUnit.size() > 0) {

                return spatialUnit;
            } else {

                return null;
            }
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public Integer getTotalrecordByProject(int project, int workflowId) {
        try {
            String strWhere = "";
            if (workflowId > 0) {
                strWhere = " and LD.workflowstatusid = " + workflowId;
            }

            String sql = "select count(*) from "
                    + "("
                    + "  select LD.landid"
                    + "  from la_spatialunit_land LD"
                    + "    inner join la_ext_personlandmapping PL on LD.landid = PL.landid"
                    + "    inner Join la_right_claimtype LC on LD.claimtypeid=LC.claimtypeid"
                    + "    inner Join la_ext_applicationstatus la on la.applicationstatusid =  LD.applicationstatusid"
                    + "    inner Join la_ext_workflow lf on lf.workflowid =  LD.workflowstatusid"
                    + "    inner Join la_right_landsharetype ST on PL.share_type_id =  ST.landsharetypeid"
                    + "    inner Join  la_party_person LP on PL.partyid = LP.personid"
                    + "    inner join la_ext_transactiondetails TR on PL.transactionid = TR.transactionid"
                    + "  where PL.isactive=true and PL.persontypeid=1 and LD.isactive=true and LD.projectnameid = "
                    + project + strWhere + ") as t1";

            List<BigInteger> arrObject = getEntityManager().createNativeQuery(sql).getResultList();

            if (arrObject.size() > 0) {
                return arrObject.get(0).intValue();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;

    }

    @Override
    public Integer searchCount(
            Integer claimType,
            int project,
            String parcelId,
            int villageId,
            String appNum,
            String pvNum,
            String apfrNum,
            String firstName,
            int appType,
            int workflowId,
            int appStatus) {

        String sql;
        String strWhere = "";

        if (claimType > 0) {
            strWhere = strWhere + " and LC.claimtypeid = " + claimType;
        }

        if (appStatus > 0) {
            strWhere = strWhere + " and LD.applicationstatusid = " + appStatus;
        }

        if (workflowId > 0) {
            strWhere = strWhere + " and LD.workflowstatusid = " + workflowId;
        }

        if (villageId > 0) {
            strWhere = strWhere + " and LD.hierarchyid5 = :villageId";
        }

        if (!"".equals(parcelId)) {
            String strPattern = "^0+";
            parcelId = parcelId.replaceAll(strPattern, "");
            strWhere = strWhere + " and LD.landid = " + parcelId;
        }

        if (!StringUtils.isEmpty(appNum)) {
            strWhere = strWhere + " and coalesce(LD.application_no, '') like :appNum";
        }

        if (!StringUtils.isEmpty(pvNum)) {
            strWhere = strWhere + " and coalesce(LD.pv_no, '') like :pvNum";
        }

        if (!StringUtils.isEmpty(apfrNum)) {
            strWhere = strWhere + " and coalesce(PL.certificateno, '') like :apfrNum";
        }

        if (!StringUtils.isEmpty(firstName)) {
            strWhere = strWhere + " and (coalesce(LP.firstname, '') like :firstName or coalesce(LE.organizationname, '') like :firstName)";
        }

        if (appType > 0) {
            strWhere = strWhere + " and PL.share_type_id = " + appType;
        }

        sql = "select count(*) from "
                + "("
                + "  select LD.landid"
                + "  from la_spatialunit_land LD"
                + "    inner join la_ext_personlandmapping PL on LD.landid = PL.landid"
                + "    inner Join la_right_claimtype LC on LD.claimtypeid=LC.claimtypeid"
                + "    inner Join la_ext_applicationstatus la on la.applicationstatusid =  LD.applicationstatusid"
                + "    inner Join la_ext_workflow lf on lf.workflowid =  LD.workflowstatusid"
                + "    inner Join la_right_landsharetype ST on PL.share_type_id =  ST.landsharetypeid"
                + "    left Join  la_party_person LP on PL.partyid = LP.personid"
                + "    left Join  la_party_organization LE on PL.partyid = LE.organizationid"
                + "    inner join la_ext_transactiondetails TR on PL.transactionid = TR.transactionid"
                + "  where PL.isactive=true and PL.persontypeid=1 and LD.isactive=true and LD.projectnameid = :projectId"
                + strWhere + ") as t1";

        try {
            Query q = getEntityManager().createNativeQuery(sql);
            q.setParameter("projectId", project);

            if (!StringUtils.isEmpty(appNum)) {
                q.setParameter("appNum", appNum + "%");
            }

            if (villageId > 0) {
                q.setParameter("villageId", villageId);
            }

            if (!StringUtils.isEmpty(pvNum)) {
                q.setParameter("pvNum", pvNum + "%");
            }

            if (!StringUtils.isEmpty(apfrNum)) {
                q.setParameter("apfrNum", apfrNum + "%");
            }

            if (!StringUtils.isEmpty(firstName)) {
                q.setParameter("firstName", "%" + firstName + "%");
            }

            List<BigInteger> arrObject = q.getResultList();

            if (arrObject.size() > 0) {
                return arrObject.get(0).intValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;

    }

    @Override
    public Integer spatialUnitWorkflowCount(int[] workflow_ids, int project) {
        String resultwrk = "";
        if (workflow_ids != null && workflow_ids.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (Integer s : workflow_ids) {
                sb.append(s).append(",");
            }
            resultwrk = sb.deleteCharAt(sb.length() - 1).toString();
        }

        String strWhereCls = "";

        if (!resultwrk.isEmpty()) {
            strWhereCls = " and LD.workflowstatusid in (" + resultwrk + ")";
        }

        String sql = "select count(1) "
                + "from la_spatialunit_land LD"
                + "  inner join la_ext_personlandmapping PL on LD.landid = PL.landid"
                + "  inner Join la_right_claimtype LC on LD.claimtypeid=LC.claimtypeid"
                + "  inner Join la_ext_applicationstatus la on la.applicationstatusid =  LD.applicationstatusid"
                + "  inner Join la_ext_workflow lf on lf.workflowid =  LD.workflowstatusid"
                + "  inner Join la_right_landsharetype ST on PL.share_type_id =  ST.landsharetypeid"
                + "  inner Join  la_party_person LP on PL.partyid = LP.personid"
                + "  inner join la_ext_transactiondetails TR on PL.transactionid = TR.transactionid "
                + "where PL.isactive=true and PL.persontypeid=1 and LD.isactive=true and LD.projectnameid = :projectId "
                + strWhereCls;

        try {
            Query q = getEntityManager().createNativeQuery(sql);
            q.setParameter("projectId", project);
            List<BigInteger> arrObject = q.getResultList();

            if (arrObject.size() > 0) {
                return arrObject.get(0).intValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    public List<LaSpatialunitLand> getspatialUnitWorkFlowResult(String lang, int[] workflow_ids, Integer startfrom, int project) {

        String resultwrk = "";
        if (workflow_ids != null && workflow_ids.length > 0) {
            StringBuilder sb = new StringBuilder();

            for (Integer s : workflow_ids) {
                sb.append(s).append(",");
            }
            resultwrk = sb.deleteCharAt(sb.length() - 1).toString();
        }

        String strWhereCls = "";

        if (!resultwrk.isEmpty()) {
            strWhereCls = " and LD.workflowstatusid in (" + resultwrk + ")";
        }

        try {

            String sql = "select LD.landid, LD.landno, LC.claimtypeid, (case when :lang = 'en' then LC.claimtype_en else LC.claimtype end) as claimtype, LD.area, "
                    + "  (case when :lang = 'en' then la.applicationstatus_en else la.applicationstatus end) as applicationstatus,"
                    + "  LP.firstname||' '|| LP.lastname as fullname, LP.address,la.applicationstatusid ,LD.workflowstatusid,"
                    + "  (case when :lang = 'en' then lf.workflow_en else lf.workflow end) as workflow_status,"
                    + "  TR.transactionid, (case when :lang = 'en' then ST.landsharetype_en else ST.landsharetype end) as apptype, PL.share_type_id as apptypeid,"
                    + "  LD.application_no, LD.pv_no, PL.certificateno, LD.section, LD.parcel_no_in_section "
                    + "from la_spatialunit_land LD"
                    + "  inner join la_ext_personlandmapping PL on LD.landid = PL.landid"
                    + "  inner Join la_right_claimtype LC on LD.claimtypeid=LC.claimtypeid"
                    + "  inner Join la_ext_applicationstatus la on la.applicationstatusid =  LD.applicationstatusid"
                    + "  inner Join la_ext_workflow lf on lf.workflowid =  LD.workflowstatusid"
                    + "  inner Join la_right_landsharetype ST on PL.share_type_id = ST.landsharetypeid"
                    + "  inner Join  la_party_person LP on PL.partyid = LP.personid"
                    + "  inner join la_ext_transactiondetails TR on PL.transactionid = TR.transactionid "
                    + "where PL.isactive=true and PL.persontypeid=1 and LD.isactive=true and LD.projectnameid = :projectId "
                    + strWhereCls
                    + " order by LD.landid desc";

            Query q = getEntityManager().createNativeQuery(sql);
            q.setParameter("projectId", project);

            if (!StringUtils.isEmpty(lang)) {
                q.setParameter("lang", lang);
            } else {
                q.setParameter("lang", "en");
            }

            List<Object[]> arrObject = q.setFirstResult(startfrom).setMaxResults(10).getResultList();
            List<LaSpatialunitLand> lstLaSpatialunitLand = new ArrayList<>();

            for (Object[] object : arrObject) {
                LaSpatialunitLand laSpatialunitLand = new LaSpatialunitLand();
                laSpatialunitLand.setLandid(Long.valueOf(object[0].toString()));
                laSpatialunitLand.setLandno((String) object[1]);
                laSpatialunitLand.setClaimtypeid(Integer.valueOf(object[2].toString()));
                laSpatialunitLand.setClaimtype_en(object[3].toString());
                laSpatialunitLand.setArea(4);
                laSpatialunitLand.setApplicationstatus_en(object[5].toString());

                if (null != object[6]) {
                    laSpatialunitLand.setFirstname(object[6].toString());
                } else {
                    laSpatialunitLand.setFirstname("");
                }

                if (null != object[7]) {
                    laSpatialunitLand.setAddress(object[7].toString());
                } else {
                    laSpatialunitLand.setAddress("");
                }

                laSpatialunitLand.setApplicationstatusid(Integer.valueOf(object[8].toString()));
                laSpatialunitLand.setWorkflowstatusid(Integer.valueOf(object[9].toString()));
                laSpatialunitLand.setWorkflowstatus(object[10].toString());
                laSpatialunitLand.setTransactionid(Integer.valueOf(object[11].toString()));
                laSpatialunitLand.setLandnostrwithzero(addZeroinLandNo((String) object[0].toString()));
                laSpatialunitLand.setRightShareType(object[12].toString());
                laSpatialunitLand.setRightShareTypeId(Integer.valueOf(object[13].toString()));

                if (object[14] != null) {
                    laSpatialunitLand.setAppNum(object[14].toString());
                }
                if (object[15] != null) {
                    laSpatialunitLand.setPvNum(object[15].toString());
                }
                if (object[16] != null) {
                    laSpatialunitLand.setApfrNum(object[16].toString());
                }
                if (object[17] != null) {
                    laSpatialunitLand.setSection(Integer.valueOf(object[17].toString()));
                }
                if (object[18] != null) {
                    laSpatialunitLand.setParcelNoInSection(Long.valueOf(object[18].toString()));
                }

                lstLaSpatialunitLand.add(laSpatialunitLand);

            }
            return lstLaSpatialunitLand;

        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public List<OwnerHistoryForFetch> getownerhistorydetails(Long landid) {
        try {
            String sql = "select row_number() OVER () as rnum,ld.landid, "
                    + " trim(coalesce(ps.firstname,'') || coalesce(le.organizationname, '')) as firstname, "
                    + " ps.middlename, "
                    + " ps.lastname, "
                    + " trim(coalesce(ps.address,'') || coalesce(le.address, '')) as address, "
                    + " trim(coalesce(ps.identityno,'') || coalesce(le.identityregistrationno, '')) as identityno, "
                    + " plm.createddate, "
                    + " plm.transactionid  "
                    + "from la_spatialunit_land ld inner join la_ext_personlandmapping plm on plm.landid=ld.landid "
                    + "left join la_Party_person ps on ps.personid=plm.partyid  "
                    + "left join la_party_organization le on le.organizationid=plm.partyid  "
                    + "where plm.persontypeid=1 and plm.isactive=true and ld.landid=" + landid
                    + " order by ps.ownertype asc";

            Query query = getEntityManager().createNativeQuery(sql, OwnerHistoryForFetch.class);
            List<OwnerHistoryForFetch> attribValues = query.getResultList();

            if (attribValues.size() > 0) {
                return attribValues;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        }
    }

    @Override
    public List<LeaseHistoryForFetch> getleasehistorydetails(Long landid) {
        String sql = null;
        try {
            sql = "select row_number() OVER () as rnum, lea.landid, ps.firstname,ps.middlename,ps.lastname,ps.address,ps.identityno, "
                    + "lea.years, lea.months,lea.leaseamount,lea.createddate,lea.leasestartdate, lea.leaseenddate, lea.conditions "
                    + " from la_lease lea inner join la_Party_person ps on ps.personid=lea.personid "
                    + " left join la_ext_transactiondetails td on td.moduletransid=lea.leaseid "
                    + " where lea.landid=" + landid + " order by lea.createddate desc;";

            Query query = getEntityManager().createNativeQuery(sql, LeaseHistoryForFetch.class);
            List<LeaseHistoryForFetch> attribValues = query.getResultList();

            if (attribValues.size() > 0) {
                return attribValues;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        }
    }

    @Override
    public List<LeaseHistoryForFetch> findleasedetailbylandid(Long transactionid, Long landid) {

        String sql = null;

        try {
            sql = "select row_number() OVER () as rnum, lea.landid, ps.firstname,ps.middlename,ps.lastname,ps.address,ps.identityno, "
                    + "lea.years, lea.months,lea.leaseamount,lea.createddate,lea.leasestartdate, lea.leaseenddate, lea.conditions "
                    + " from la_lease lea inner join la_Party_person ps on ps.personid=lea.personid "
                    + " left join la_ext_transactiondetails td on td.moduletransid=lea.leaseid "
                    + " where lea.landid=" + landid + " and td.transactionid=" + transactionid;

            Query query = getEntityManager().createNativeQuery(sql, LeaseHistoryForFetch.class);
            List<LeaseHistoryForFetch> attribValues = query.getResultList();

            if (attribValues.size() > 0) {
                return attribValues;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        }

    }

    @Override
    public List<LeaseHistoryForFetch> findsurrenderleasedetailbylandid(Long transactionid, Long landid) {
        String sql = null;
        try {
            sql = " select row_number() OVER () as rnum,lea.landid,ps.firstname,ps.middlename,ps.lastname,"
                    + "ps.address,ps.identityno,lea.years, lea.months,lea.leaseamount,lea.createddate,lea.leasestartdate, "
                    + "lea.leaseenddate, '' as conditions "
                    + " from la_surrenderlease lea inner join la_Party_person ps on ps.personid=lea.personid "
                    + " left join la_ext_transactiondetails td on td.moduletransid=lea.leaseid "
                    + " where lea.landid=" + landid + " and td.transactionid=" + transactionid;

            Query query = getEntityManager().createNativeQuery(sql, LeaseHistoryForFetch.class);
            List<LeaseHistoryForFetch> attribValues = query.getResultList();

            if (attribValues.size() > 0) {
                return attribValues;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        }
    }

    @Override
    public List<MortageHistoryForFetch> findmortagagedetailbylandid(Long transactionid, Long landid) {

        String sql = null;

        try {
            sql = " select row_number() OVER () as rnum,ld.landid,fin.financialagency,mor.mortgagefrom,mor.mortgageto,mor.mortgageamount"
                    + " from la_mortgage mor inner join la_spatialunit_land ld on ld.landid=mor.landid "
                    + " inner join la_ext_financialagency fin on fin.financialagencyid=mor.financialagencyid "
                    + " left join la_ext_transactiondetails td on td.moduletransid=mor.mortgageid"
                    + " where ld.landid=" + landid + "and td.transactionid=" + transactionid;

            Query query = getEntityManager().createNativeQuery(sql, MortageHistoryForFetch.class);
            List<MortageHistoryForFetch> attribValues = query.getResultList();

            if (attribValues.size() > 0) {
                return attribValues;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        }

    }

    @Override
    public List<MortageHistoryForFetch> findSurrendermortagagedetailbylandid(
            Long transactionid, Long landid) {

        String sql = null;

        try {
            sql = " select row_number() OVER () as rnum,ld.landid,fin.financialagency,mor.mortgagefrom,mor.mortgageto,mor.mortgageamount"
                    + " from la_surrendermortgage mor inner join la_spatialunit_land ld on ld.landid=mor.landid "
                    + " inner join la_ext_financialagency fin on fin.financialagencyid=mor.financialagencyid "
                    + " left join la_ext_transactiondetails td on td.moduletransid=mor.mortgageid"
                    + " where ld.landid=" + landid + "and td.transactionid=" + transactionid;

            Query query = getEntityManager().createNativeQuery(sql, MortageHistoryForFetch.class);
            List<MortageHistoryForFetch> attribValues = query.getResultList();

            if (attribValues.size() > 0) {
                return attribValues;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        }

    }

    @Override
    public List<MortageHistoryForFetch> getmortagagedetails(Long landid) {

        String sql = null;

        try {
            sql = " select row_number() OVER () as rnum,ld.landid,fin.financialagency,mor.mortgagefrom,mor.mortgageto,mor.mortgageamount"
                    + " from la_mortgage mor inner join la_spatialunit_land ld on ld.landid=mor.landid "
                    + " inner join la_ext_financialagency fin on fin.financialagencyid=mor.financialagencyid "
                    + " where ld.landid=" + landid + " order by mor.createddate desc;";

            Query query = getEntityManager().createNativeQuery(sql, MortageHistoryForFetch.class);
            List<MortageHistoryForFetch> attribValues = query.getResultList();

            if (attribValues.size() > 0) {
                return attribValues;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        }

    }

    @Override
    public List<UploadedDocumentDetailsForFetch> viewdocumentdetailbytransactioid(Long transactionid) {

        String sql = null;

        try {
            sql = " select row_number() OVER () as rnum,doc.documentname, doc.recordationdate as docdate,doc.remarks as description,doctype.documenttype,doc.transactionid,doc.partyid,doc.documentid "
                    + " from la_ext_documentdetails doc "
                    + " left join la_ext_documenttype doctype on doctype.documenttypeid=doc.documenttypeid"
                    + " where doc.transactionid=" + transactionid;

            Query query = getEntityManager().createNativeQuery(sql, UploadedDocumentDetailsForFetch.class);
            List<UploadedDocumentDetailsForFetch> attribValues = query.getResultList();

            if (attribValues.size() > 0) {
                return attribValues;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        }

    }

    @Override
    public List<TransactionHistoryForFetch> gettransactiondetails(Long landid, String lang) {

        String sql = null;

        try {
            sql = " select row_number() OVER () as rnum,transactiontype,landid,applicantname,ownername,createddate,"
                    + "transactionid,personid, processid, certificateno, sharetype from "
                    + "(select distinct on (lea.personid) (case when '" + lang + "' = 'en' then p.processname_en else p.processname end) as transactiontype, ld.landid,"
                    + "trim(coalesce(ps1.firstname, '') ||' '|| coalesce(ps1.lastname, '') || ' ' || coalesce(le2.organizationname, '')) as applicantname,"
                    + "trim(coalesce(ps2.firstname, '') ||' '|| coalesce(ps2.lastname, '') || ' ' || coalesce(le.organizationname, '')) as ownername,"
                    + "lea.createddate,td.transactionid,lea.personid, td.processid, plm.certificateno, (case when '" + lang + "' = 'en' then lsh.landsharetype_en else lsh.landsharetype end) as sharetype "
                    + "from la_lease lea inner join la_spatialunit_land ld on ld.landid=lea.landid "
                    + "left join la_Party_person ps1 on ps1.personid=lea.personid "
                    + "left join la_ext_personlandmapping plm on plm.partyid=lea.ownerid "
                    + "inner join la_right_landsharetype lsh on plm.share_type_id = lsh.landsharetypeid "
                    + "left join la_ext_transactiondetails td on td.moduletransid=lea.leaseid "
                    + "inner join la_ext_process p on td.processid = p.processid "
                    + "left join la_Party_person ps2 on ps2.personid=lea.ownerid "
                    + "left join la_party_organization le on le.organizationid=lea.ownerid "
                    + "left join la_party_organization le2 on le.organizationid=lea.personid "
                    + "where ld.landid=" + landid + " and td.processid in (1,10) and td.isactive=true "
                    + "	union "
                    + "select distinct on (lea.transactionid) (case when '" + lang + "' = 'en' then p.processname_en else p.processname end) as transactiontype, lea.landid,"
                    + "trim(coalesce(ps1.firstname, '') ||' '|| coalesce(ps1.lastname, '') || ' ' || coalesce(le2.organizationname, '')) as applicantname,"
                    + "trim(coalesce(ps2.firstname, '') ||' '|| coalesce(ps2.lastname, '') || ' ' || coalesce(le.organizationname, '')) as ownername,"
                    + " lea.createddate,td.transactionid,lea.applicantid as personid, td.processid, plm.certificateno, (case when '" + lang + "' = 'en' then lsh.landsharetype_en else lsh.landsharetype end) as sharetype "
                    + " from la_ext_permission lea inner join la_ext_personlandmapping plm on lea.ownerid=plm.partyid "
                    + " left join la_Party_person ps1 on ps1.personid=lea.applicantid "
                    + " inner join la_right_landsharetype lsh on plm.share_type_id = lsh.landsharetypeid "
                    + " left join la_ext_transactiondetails td on td.transactionid=lea.transactionid "
                    + " inner join la_ext_process p on td.processid = p.processid "
                    + " left join la_Party_person ps2 on ps2.personid=lea.ownerid "
                    + " left join la_party_organization le on le.organizationid=lea.ownerid "
                    + " left join la_party_organization le2 on le.organizationid=lea.applicantid "
                    + " where lea.landid=" + landid + " and td.processid in (11,12) and td.isactive=true "
                    + " Union"
                    + "	select distinct on (personid) 'Mortgage' as transactiontype,ld.landid,fin.financialagency as applicantname,ps2.firstname||' '||coalesce(ps2.middlename, '')||' '||ps2.lastname as ownername,mor.createddate,td.transactionid,"
                    + " mor.financialagencyid as personid, td.processid, '' as certificateno, '' as sharetype"
                    + "	from la_mortgage mor left join la_spatialunit_land ld on ld.landid=mor.landid"
                    + " left join la_ext_financialagency fin on fin.financialagencyid=mor.financialagencyid"
                    + " left join la_ext_personlandmapping plm on plm.landid=ld.landid"
                    + " left join la_ext_transactiondetails td on td.moduletransid=mor.mortgageid"
                    + " left join la_Party_person ps2 on ps2.personid=mor.ownerid "
                    + "	where ld.landid=" + landid + " and td.processid=3"
                    + " Union"
                    + "	select distinct on (personid) 'Surrender Mortgage' as transactiontype,ld.landid,fin.financialagency as applicantname,ps2.firstname||' '||coalesce(ps2.middlename, '')||' '||ps2.lastname as ownername,mor.createddate,td.transactionid,"
                    + " mor.financialagencyid as personid, td.processid, '' as certificateno, '' as sharetype"
                    + "	from la_surrendermortgage mor left join la_spatialunit_land ld on ld.landid=mor.landid"
                    + " left join la_ext_financialagency fin on fin.financialagencyid=mor.financialagencyid"
                    + " left join la_ext_personlandmapping plm on plm.landid=ld.landid"
                    + " left join la_ext_transactiondetails td on td.moduletransid=mor.mortgageid"
                    + " left join la_Party_person ps2 on ps2.personid=mor.ownerid "
                    + "	where ld.landid=" + landid + " and td.processid=9"
                    + " Union"
                    + " (select string_agg(transactiontype, '') as transactiontype, landid,string_agg(applicantname,' ' order by applicantname)as applicantname,string_agg(ownename,' ' order by ownename)as ownename,"
                    + "createddate,transactionid,null as personid, processid, string_agg(certificateno, '') as certificateno, string_agg(sharetype, '') as sharetype "
                    + "from ("
                    + "select distinct '' as transactiontype, th.landid,null as applicantname, "
                    + "  string_agg(ps.firstname||' '||ps.lastname,' / ' order by ps.ownertype)as ownename, "
                    + "  th.createddate,th.transactionid,null as personid, td.processid, pm.certificateno,  "
                    + "  (case when '" + lang + "' = 'en' then lsh.landsharetype_en else lsh.landsharetype end) as sharetype "
                    + "from la_ext_transactionhistory th "
                    + "  inner join (la_Party_person ps inner join la_ext_personlandmapping pm on ps.personid = pm.partyid inner join la_right_landsharetype lsh on pm.share_type_id = lsh.landsharetypeid)  "
                    + "   on cast(ps.personid as character varying) in (SELECT  regexp_split_to_table(th.oldownerid, E',')) "
                    + "  inner join la_ext_transactiondetails td on td.transactionid=th.transactionid "
                    + "where th.landid= " + landid + " and td.processid=2  "
                    + "group by th.landid,th.createddate,th.transactionid, td.processid, pm.certificateno, lsh.landsharetypeid "
                    + "union "
                    + "select distinct '' as transactiontype, th.landid,null as applicantname, "
                    + "  string_agg(le.organizationname,' / ' order by le.organizationname)as ownename, "
                    + "  th.createddate,th.transactionid,null as personid, td.processid, pm.certificateno,  "
                    + "  (case when '" + lang + "' = 'en' then lsh.landsharetype_en else lsh.landsharetype end) as sharetype "
                    + "from la_ext_transactionhistory th "
                    + "  inner join (la_party_organization le inner join la_ext_personlandmapping pm on le.organizationid = pm.partyid inner join la_right_landsharetype lsh on pm.share_type_id = lsh.landsharetypeid)  "
                    + "   on cast(le.organizationid as character varying) in (SELECT  regexp_split_to_table(th.oldownerid, E',')) "
                    + "  inner join la_ext_transactiondetails td on td.transactionid=th.transactionid "
                    + "where th.landid= " + landid + " and td.processid=2  "
                    + "group by th.landid,th.createddate,th.transactionid, td.processid, pm.certificateno, lsh.landsharetypeid "
                    + "union "
                    + "select distinct (case when '" + lang + "' = 'en' then m.name_en else m.name end) as transactiontype, th.landid, "
                    + "  string_agg(ps2.firstname||' '||ps2.lastname,' / ' order by ps2.ownertype)as applicantname, null as ownename, "
                    + "  th.createddate,th.transactionid,null as personid, td.processid, '' as certificateno, '' as sharetype "
                    + "from la_ext_transactionhistory th "
                    + "  inner join (la_Party_person ps2 inner join la_ext_personlandmapping pm on ps2.personid = pm.partyid left join mutation_type m on pm.mutation_id = m.id)  "
                    + "  on cast(ps2.personid as character varying) in (SELECT  regexp_split_to_table(th.newownerid, E',')) "
                    + "  inner join la_ext_transactiondetails td on td.transactionid=th.transactionid "
                    + "where th.landid= " + landid + " and td.processid=2  "
                    + "group by th.landid,th.createddate,th.transactionid, m.id, td.processid "
                    + "union "
                    + "select distinct (case when '" + lang + "' = 'en' then m.name_en else m.name end) as transactiontype, th.landid, "
                    + "  string_agg(le.organizationname,' / ' order by le.organizationname)as applicantname, null as ownename, "
                    + "  th.createddate,th.transactionid,null as personid, td.processid, '' as certificateno, '' as sharetype "
                    + "from la_ext_transactionhistory th "
                    + "  inner join (la_party_organization le inner join la_ext_personlandmapping pm on le.organizationid = pm.partyid left join mutation_type m on pm.mutation_id = m.id)  "
                    + "  on cast(le.organizationid as character varying) in (SELECT  regexp_split_to_table(th.newownerid, E',')) "
                    + "  inner join la_ext_transactiondetails td on td.transactionid=th.transactionid "
                    + "where th.landid= " + landid + " and td.processid=2 "
                    + "group by th.landid,th.createddate,th.transactionid, m.id, td.processid "
                    + ") as tmp "
                    + "group by landid,createddate,transactionid,personid, processid)"
                    + " Union "
                    + " Select distinct on (lea.personid) (case when '" + lang + "' = 'en' then p.processname_en else p.processname end) as transactiontype, ld.landid,"
                    + " trim(coalesce(ps1.firstname, '') ||' '|| coalesce(ps1.lastname, '') || ' ' || coalesce(le2.organizationname, '')) as applicantname,"
                    + " trim(coalesce(ps2.firstname, '') ||' '|| coalesce(ps2.lastname, '') || ' ' || coalesce(le.organizationname, '')) as ownername,"
                    + " lea.createddate,td.transactionid,lea.personid, td.processid, plm.certificateno, (case when '" + lang + "' = 'en' then lsh.landsharetype_en else lsh.landsharetype end) as sharetype "
                    + " from la_surrenderlease lea inner join la_spatialunit_land ld on ld.landid=lea.landid "
                    + " left join la_Party_person ps1 on ps1.personid=lea.personid "
                    + " left join la_ext_personlandmapping plm on plm.partyid=lea.ownerid "
                    + " inner join la_right_landsharetype lsh on plm.share_type_id = lsh.landsharetypeid "
                    + " left join la_ext_transactiondetails td "
                    + " inner join la_ext_process p on td.processid = p.processid "
                    + " on td.moduletransid=lea.leaseid "
                    + " left join la_Party_person ps2 on ps2.personid=lea.ownerid "
                    + " left join la_party_organization le on le.organizationid=lea.ownerid "
                    + " left join la_party_organization le2 on le.organizationid=lea.personid "
                    + " where ld.landid=" + landid + " and td.processid=5)t order by transactionid desc;";

            Query query = getEntityManager().createNativeQuery(sql, TransactionHistoryForFetch.class);
            List<TransactionHistoryForFetch> attribValues = query.getResultList();

            if (attribValues.size() > 0) {
                return attribValues;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        }

    }

    @Override
    public String checkruntopologychecks(String projectName) {
        try {
            /*Query query = getEntityManager().createQuery("select ins_toplology_error");
	            return (ProjectDetails) query.getSingleResult();*/

 /* Query query = getEntityManager().createQuery("select ins_toplology_error()");
	            query.getResultList();*/
 /*StoredProcedureQuery query = getEntityManager().createStoredProcedureQuery("ins_toplology_error")
	                    .registerStoredProcedureParameter("projectName", String.class, ParameterMode.IN)
	                    .setParameter("projectName", projectName);
	            query.getResultList();*/
            // List<Object[]> o = getEntityManager().createQuery("SELECT ins_toplology_error() ").getResultList();  
            //String queryStrgetparcel = "select ins_toplology_error()";
            String qq = "INSERT INTO public.topology_checks_error_log(geometry, error_message, layer_name, landid)"
                    + " select  a.geometry,'invalid geometry','la_spatialunit_land',a.landid from la_spatialunit_land a where st_isvalid(a.geometry)=false union all"
                    + " select a.geometry,'intersect','la_spatialunit_land',a.landid FROM la_spatialunit_land a INNER JOIN la_spatialunit_land b ON ST_Intersects(a.geometry,b.geometry) where a.geometry<b.geometry union all"
                    + " select a.geometry, 'small area','la_spatialunit_land',a.landid from la_spatialunit_land a WHERE  ST_Area(a.geometry) < 0.00001 union all"
                    + " SELECT a.geometry,'invalid geometry','la_spatialunit_land',a.landid from la_spatialunit_land a where  ST_IsEmpty(ST_GeomFromText('GEOMETRYCOLLECTION EMPTY'))=false";

            int temp = getEntityManager().createNativeQuery(qq).executeUpdate();

            /*if(temp.get(0) != null && temp.size()>0)
				{
					int i =0;
				}*/
            //StoredProcedureQuery query = getEntityManager().createStoredProcedureQuery("calltopologychecks", Review.class);
            // List<Object> lstobj = query.getResultList();
            return "Success";
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public NaturalPerson updateNaturalPersonDataForEdit(NaturalPerson editednaturalperosn) throws Exception {
        getEntityManager().merge(editednaturalperosn);
        return editednaturalperosn;
    }

    private String addZeroinLandNo(String landNo) {

        int length = 9;
        String str = "";
        for (int i = 0; i < length - landNo.length(); i++) {
            str = str + "0";
        }
        return str + "" + landNo;

    }

    private String addRegistrationNo(String communityID, String landNo) {

        int length = 6;
        String str = "";
        for (int i = 0; i < length - landNo.length(); i++) {
            str = str + "0";
        }
        return communityID + "- " + str + "" + landNo;

    }

    public List<DataCorrectionReport> getDataCorrectionReport(Long transactionid, Long landId) {

        String sql = null;

        try {
            sql = " Select Distinct on(LD.landid) LD.landid, LD.landno, TR.transactionid ,ST.landsharetype as landsharetype , LC.claimtype_en as claimtype, LT.landusetype_en as landusetype ,PU.landusetype_en as proposedused, LU.landtype_en as landtype, "
                    + "TC.tenureclass_en as tenureclasstype ,LD.area,LD.neighbor_east,LD.neighbor_west,LD.neighbor_north,LD.neighbor_south,LD.occupancylength, LD.claimno, PR.projectname,LD.createddate as claimdate,"
                    + " hie1.name as  county , hie2.name as region,hie3.name as province,hie4.name as commune,hie5.name as place, PL.partyid, case when LD.other_use  is null then ' ' else LD.other_use  end as other_use from la_spatialunit_land LD"
                    + " Inner join la_ext_personlandmapping PL on LD.landid = PL.landid"
                    + " inner Join la_right_claimtype LC on LD.claimtypeid=LC.claimtypeid "
                    + " inner Join la_right_landsharetype ST on PL.share_type_id =  ST.landsharetypeid "
                    + " inner join la_baunit_landusetype LT on LD.landusetypeid=LT.landusetypeid"
                    + " inner join la_baunit_landusetype PU on LD.proposedused=PU.landusetypeid"
                    + " inner join la_baunit_landtype LU on LD.landtypeid=LU.landtypeid"
                    + " inner join la_right_tenureclass TC on LD.tenureclassid=TC.tenureclassid"
                    + " inner join la_spatialsource_projectname PR on  LD.projectnameid= PR.projectnameid"
                    + " left join la_spatialunitgroup_hierarchy hie1 on hie1.hierarchyid=ld.hierarchyid1 and LD.spatialunitgroupid1=1"
                    + " left join la_spatialunitgroup_hierarchy hie2 on hie2.hierarchyid=ld.hierarchyid2 and LD.spatialunitgroupid2=2 "
                    + " left join la_spatialunitgroup_hierarchy hie3 on hie3.hierarchyid=ld.hierarchyid3 and LD.spatialunitgroupid3=3"
                    + " left join la_spatialunitgroup_hierarchy hie4 on hie4.hierarchyid=ld.hierarchyid4 and LD.spatialunitgroupid4=4"
                    + " left join la_spatialunitgroup_hierarchy hie5 on hie5.hierarchyid=ld.hierarchyid5 and LD.spatialunitgroupid5=5"
                    + " inner join la_ext_transactiondetails TR on PL.transactionid = TR.transactionid  where TR.transactionid=" + transactionid + "  order by landid";

            Query query = getEntityManager().createNativeQuery(sql, DataCorrectionReport.class);
            List<DataCorrectionReport> datacorrectionlst = query.getResultList();

            if (datacorrectionlst.size() > 0) {
                for (DataCorrectionReport objDataCorrectionReport : datacorrectionlst) {
                    long landID = objDataCorrectionReport.getLandId();
                    String landnumber = addZeroinLandNo(landID + "");
                    objDataCorrectionReport.setLandno(landnumber);
                }
                return datacorrectionlst;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        }

    }

    @Override
    public List<PoiReport> getDataCorrectionReportPOI(Long transactionid, Long landId) {

        String sql = null;
        try {

            sql = " select SP.id as Id,SP.first_name as firstName , SP.middle_name as middleName ,SP.last_name as lastName , GN.gender_en as gender ,RL.relationshiptype_en as relationship, SP.dob from la_ext_spatialunit_personwithinterest SP "
                    + " inner join la_partygroup_gender GN on SP.gender= GN.genderid "
                    + " inner join la_partygroup_relationshiptype RL on  SP.relation=RL.relationshiptypeid where   SP.landid=" + landId + " and SP.isactive= true and SP.transactionid=" + transactionid;

            Query query = getEntityManager().createNativeQuery(sql, PoiReport.class);
            List<PoiReport> PoiReportlst = query.getResultList();

            if (PoiReportlst.size() > 0) {
                return PoiReportlst;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        }

    }

    @Override
    public List<PersonsReport> getDataCorrectionPersonsReport(Long transactionid, Long landId) {

        String sql = null;
        try {

            sql = "  select ps.personid as id,  ps.firstname as firstname ,ps.middlename as middlename ,ps.lastname as lastname ,ps.address as address ,ps.dateofbirth as dateofbirth, g.gender as gender ,ms.maritalstatus as maritalstatus ,id.identitytype as identitytype ,ps.identityno as identityno , "
                    + " ps.contactno as contact ,oc.occupation_en as occupation,eu.educationlevel_en as educationlevel, aopt.optiontext as ownertype "
                    + " from la_ext_transactiondetails td "
                    + " inner join la_ext_personlandmapping plm on plm.transactionid=td.transactionid "
                    + " inner join la_spatialunit_land ld on ld.landid=plm.landid "
                    + " inner join la_Party_person ps on ps.personid=plm.partyid "
                    + " left join la_partygroup_gender g on g.genderid=ps.genderid "
                    + " left join la_partygroup_maritalstatus ms on ms.maritalstatusid=ps.maritalstatusid "
                    + " left join la_partygroup_identitytype id on id.identitytypeid=ps.identitytypeid "
                    + " left join la_partygroup_occupation oc on ps.occupationid=oc.occupationid "
                    + " left join la_partygroup_educationlevel eu on ps.educationlevelid=eu.educationlevelid "
                    + " left join la_ext_attributeoptions aopt on aopt.parentid=ps.ownertype and aopt.attributemasterid=1156 "
                    + " where td.transactionid=" + transactionid + " and  ps.isactive='true' order by ownertype desc";

            Query query = getEntityManager().createNativeQuery(sql, PersonsReport.class);
            List<PersonsReport> PersonsReportlst = query.getResultList();

            if (PersonsReportlst.size() > 0) {
                return PersonsReportlst;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        }

    }

    @Override
    public List<FarmReport> getFarmReportByLandId(Long landId) {

        try {
            String sql = "Select Distinct on (RA.landID) RA.landID,cast(srl.createddate as date),u.username, (Select attributevalue from la_ext_resource_custom_attributevalue where landid=RA.landID "
                    + " and attributeoptionsid in (100,121,130,139,148,157)) as EnterpriseGroupname, h1.name as county,h2.name as District,h3.name as Clanname,h4.name as community,h5.name as town,"
                    + "rc.classificationname,rsc.subclassificationname,area,ac.categoryname, case when ac.attributecategoryid in (10,17,11,12) then 'Natural' when ac.attributecategoryid "
                    + "in (14,18) then 'Non-Natural' end as persontype, "
                    + " (Select attributevalue from la_ext_resourceattributevalue Where landid=RA.landID and attributemasterid in (1063,1017,1035,1079,1088,1097,1108) and groupid=RA.groupid) ||' '|| "
                    + " (Select attributevalue from la_ext_resourceattributevalue Where landid=RA.landID and attributemasterid in (1065,1018,1036,1080,1089,1109,1098) and groupid=RA.groupid) ||' '|| "
                    + " (Select attributevalue from la_ext_resourceattributevalue Where landid=RA.landID and attributemasterid in (1066,1019,1037,1081,1090,1099,1110) and groupid=RA.groupid) as Name, "
                    + " (Select case when count(*)=0 then 'No' else 'Yes' end as ispoi from la_ext_resourcepoiattributevalue where landID=RA.LandID ) as ispoi, "
                    + " (Select attributevalue from la_ext_resourceattributevalue Where landid=RA.landID and attributemasterid in (1022,1064,1116,22) and groupid=RA.groupid) as MaritalStatus, "
                    + " (Select count(distinct groupid) from la_ext_resourcepoiattributevalue where landID=RA.LandID ) as POICount, "
                    + " (Select attributevalue from la_ext_resourceattributevalue Where landid=RA.landID and attributemasterid in (4,1020,1067,1119) and groupid=RA.groupid) as Gender, "
                    + " (Select attributevalue from la_ext_resourceattributevalue Where landid=RA.landID and attributemasterid in (1024,1059,1070,1122) and groupid=RA.groupid) as Ethnicity, "
                    + " (Select attributevalue from la_ext_resourceattributevalue Where landid=RA.landID and attributemasterid in (43,1025,1071,1123) and groupid=RA.groupid) as Resident, "
                    + " (Select attributevalue from la_ext_resourceattributevalue Where landid=RA.landID and attributemasterid in (1021,1068,1120,1129) and groupid=RA.groupid) as DOB, "
                    + " (Select attributevalue from la_ext_resourceattributevalue Where landid=RA.landID and attributemasterid in (5,8,1030,1042,1051,1073,1086,1095,1105,1125) and groupid=RA.groupid) as MobileNo, "
                    + " (Select attributevalue from la_ext_resource_custom_attributevalue where landid=RA.landID and attributeoptionsid in (103,122,131,140,149,158)) as primarycrop, "
                    + " (Select attributevalue from la_ext_resource_custom_attributevalue where landid=RA.landID and attributeoptionsid in (104,123,132,141,150,159)) as primarycropdate, "
                    + " (Select attributevalue from la_ext_resource_custom_attributevalue where landid=RA.landID and attributeoptionsid in (105,124,133,142,151,160)) as primarycropduration, "
                    + " (Select attributevalue from la_ext_resource_custom_attributevalue where landid=RA.landID and attributeoptionsid in (116,125,134,143,152,161)) as seccrop, "
                    + " (Select attributevalue from la_ext_resource_custom_attributevalue where landid=RA.landID and attributeoptionsid in (117,126,135,144,153,162)) as seccropdate, "
                    + " (Select attributevalue from la_ext_resource_custom_attributevalue where landid=RA.landID and attributeoptionsid in (118,127,136,145,154,163)) as seccropduration, "
                    + " (Select attributevalue from la_ext_resource_custom_attributevalue where landid=RA.landID and attributeoptionsid in (119,128,137,146,155,164)) as TotalExpenditure, "
                    + " (Select attributevalue from la_ext_resource_custom_attributevalue where landid=RA.landID and attributeoptionsid in (120,129,138,147,156,165)) as TotalSale, "
                    + " prj.projectname as project"
                    + " from la_spatialunit_resource_land srl,la_ext_resourceattributevalue RA,la_ext_attributemaster AM,la_ext_attributecategory ac "
                    + " ,la_ext_user u,la_spatialunitgroup_hierarchy h1 ,la_spatialunitgroup_hierarchy h2,la_spatialunitgroup_hierarchy h3,la_spatialunitgroup_hierarchy h4,la_spatialunitgroup_hierarchy h5 , "
                    + "	la_ext_resourcelandclassificationmapping rlcm ,la_ext_resourceclassification rc ,la_ext_resourcesubclassification rsc, la_spatialsource_projectname prj "
                    + "  Where srl.LandID=RA.landID and rlcm.projectid=prj.projectnameid and rlcm.landid=srl.landid and rc.classificationid=rlcm.classificationid and rsc.subclassificationid=rlcm.subclassificationid "
                    + "  And u.userid=srl.createdby And h1.hierarchyid=srl.hierarchyid1 and h1.spatialunitgroupid=1 And h2.hierarchyid=srl.hierarchyid2 and h2.spatialunitgroupid=2 "
                    + " And h3.hierarchyid=srl.hierarchyid3 and h3.spatialunitgroupid=3 And h4.hierarchyid=srl.hierarchyid4 and h4.spatialunitgroupid=4 And h5.hierarchyid=srl.hierarchyid5 and h5.spatialunitgroupid=5 "
                    + "  AND RA.AttributeMasterID=AM.AttributeMasterID AND AM.AttributeCategoryID=ac.AttributeCategoryID AND srl.isactive = true and "
                    + " srl.landid = " + landId
                    + " Order by Ra.Landid";

            Query query = getEntityManager().createNativeQuery(sql, FarmReport.class);
            List<FarmReport> FarmReportlst = query.getResultList();
            if (FarmReportlst.size() > 0) {

                return FarmReportlst;
            }

            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public List<DataCorrectionReport> getBatchDataCorrectionReport(
            Long transactionid) {

        String sql = null;

        try {
            sql = " Select LD.landid, LD.landno, TR.transactionid ,ST.landsharetype as landsharetype , LC.claimtype_en as claimtype, LT.landusetype_en as landusetype ,PU.landusetype_en as proposedused, LU.landtype_en as landtype, "
                    + "TC.tenureclass_en as tenureclasstype ,LD.area,LD.neighbor_east,LD.neighbor_west,LD.neighbor_north,LD.neighbor_south,LD.occupancylength, LD.claimno, PR.projectname,LD.createddate as claimdate,"
                    + " hie1.name as  county , hie2.name as region,hie3.name as province,hie4.name as commune,hie5.name as place, PL.partyid from la_spatialunit_land LD"
                    + " Inner join la_ext_personlandmapping PL on LD.landid = PL.landid"
                    + " inner Join la_right_claimtype LC on LD.claimtypeid=LC.claimtypeid "
                    + " inner Join la_right_landsharetype ST on PL.share_type_id =  ST.landsharetypeid "
                    + " inner join la_baunit_landusetype LT on LD.landusetypeid=LT.landusetypeid"
                    + " inner join la_baunit_landusetype PU on LD.proposedused=PU.landusetypeid"
                    + " inner join la_baunit_landtype LU on LD.landtypeid=LU.landtypeid"
                    + " inner join la_right_tenureclass TC on LD.tenureclassid=TC.tenureclassid"
                    + " inner join la_spatialsource_projectname PR on  LD.projectnameid= PR.projectnameid"
                    + " left join la_spatialunitgroup_hierarchy hie1 on hie1.hierarchyid=ld.hierarchyid1 and LD.spatialunitgroupid1=1"
                    + " left join la_spatialunitgroup_hierarchy hie2 on hie2.hierarchyid=ld.hierarchyid2 and LD.spatialunitgroupid2=2 "
                    + " left join la_spatialunitgroup_hierarchy hie3 on hie3.hierarchyid=ld.hierarchyid3 and LD.spatialunitgroupid3=3"
                    + " left join la_spatialunitgroup_hierarchy hie4 on hie4.hierarchyid=ld.hierarchyid4 and LD.spatialunitgroupid4=4"
                    + " left join la_spatialunitgroup_hierarchy hie5 on hie5.hierarchyid=ld.hierarchyid5 and LD.spatialunitgroupid5=5"
                    + " inner join la_ext_transactiondetails TR on PL.transactionid = TR.transactionid  where TR.transactionid =" + transactionid + " order by landid";

            Query query = getEntityManager().createNativeQuery(sql, DataCorrectionReport.class);
            List<DataCorrectionReport> datacorrectionlst = query.getResultList();

            if (datacorrectionlst.size() > 0) {
                return datacorrectionlst;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        }

    }

    @Override
    public List<PoiReport> getBatchDataCorrectionReportPOI(
            Long transactionid) {

        String sql = null;
        try {

            sql = " select SP.id as Id,SP.first_name as firstName , SP.middle_name as middleName ,SP.last_name as lastName , GN.gender_en as gender ,RL.relationshiptype_en as relationship, SP.dob from la_ext_spatialunit_personwithinterest SP "
                    + " inner join la_partygroup_gender GN on SP.gender= GN.genderid "
                    + " inner join la_partygroup_relationshiptype RL on  SP.relation=RL.relationshiptypeid where SP.isactive= true and SP.transactionid=" + transactionid;

            Query query = getEntityManager().createNativeQuery(sql, PoiReport.class);
            List<PoiReport> PoiReportlst = query.getResultList();

            if (PoiReportlst.size() > 0) {
                return PoiReportlst;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        }

    }

    @Override
    public List<PersonsReport> getBatchDataCorrectionPersonsReport(
            Long transactionidstart, Long transactionidend) {

        Long startid = transactionidstart;

        Long endid = transactionidend;

        Long transiddiffr = transactionidend - transactionidstart;
        String ids = startid + ",";
        for (int i = 0; i < transiddiffr; i++) {
            transactionidstart = transactionidstart + 1;
            ids = ids + transactionidstart + ",";
        }

        ids = ids.substring(0, ids.length() - 1);

        String sql = null;
        try {

            sql = "  select ps.personid as id,  ps.firstname as firstname ,ps.middlename as middlename ,ps.lastname as lastname ,ps.address as address ,ps.dateofbirth as dateofbirth, g.gender as gender ,ms.maritalstatus as maritalstatus ,id.identitytype as identitytype ,ps.identityno as identityno , "
                    + " ps.contactno as contact ,oc.occupation_en as occupation,eu.educationlevel_en as educationlevel, aopt.optiontext as ownertype "
                    + " from la_ext_transactiondetails td "
                    + " inner join la_ext_personlandmapping plm on plm.transactionid=td.transactionid "
                    + " inner join la_spatialunit_land ld on ld.landid=plm.landid "
                    + " inner join la_Party_person ps on ps.personid=plm.partyid "
                    + " left join la_partygroup_gender g on g.genderid=ps.genderid "
                    + " left join la_partygroup_maritalstatus ms on ms.maritalstatusid=ps.maritalstatusid "
                    + " left join la_partygroup_identitytype id on id.identitytypeid=ps.identitytypeid "
                    + " left join la_partygroup_occupation oc on ps.occupationid=oc.occupationid "
                    + " left join la_partygroup_educationlevel eu on ps.educationlevelid=eu.educationlevelid "
                    + " left join la_ext_attributeoptions aopt on aopt.parentid=ps.ownertype and aopt.attributemasterid=1156 "
                    + " where td.transactionid in (" + ids + ")";

            Query query = getEntityManager().createNativeQuery(sql, PersonsReport.class);
            List<PersonsReport> PersonsReportlst = query.getResultList();

            if (PersonsReportlst.size() > 0) {
                return PersonsReportlst;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        }

    }

    @Override
    public List<Object> findregparcelcountbyTenure(int project, String tag, Integer villageId) {
        try {
            List<Object> spatialUnit;
            int existingworkflow = 0;
            int newworkflow = 0;

            if (tag.equalsIgnoreCase("NEW")) {
                existingworkflow = 10;
                newworkflow = 1;
            } else if (tag.equalsIgnoreCase("REGISTERED")) {
                existingworkflow = 11;
                newworkflow = 3;
            } else if (tag.equalsIgnoreCase("APFR")) {
                existingworkflow = 13;
                newworkflow = 7;
            }

            String sql = "select count(sp.landid) as count, pt.claimtype, st.landsharetype\n "
                    + "from la_spatialunit_land sp inner join la_ext_personlandmapping pl on sp.landid = pl.landid inner join la_right_claimtype pt on sp.claimtypeid=pt.claimtypeid\n "
                    + "  inner join la_right_landsharetype st on pl.share_type_id = st.landsharetypeid\n "
                    + "where sp.projectnameid = :project_name_id and sp.isactive and pl.isactive and "
                    + "((sp.workflowstatusid >= :newworkflow and sp.workflowstatusid < 10) or sp.workflowstatusid >= :existingworkflow)\n "
                    + " and sp.applicationstatusid != 5 and sp.hierarchyid5 = :villageId \n "
                    + "group by pt.claimtypeid, st.landsharetypeid \n "
                    + "order by pt.claimtypeid, st.landsharetypeid";

            Query executeQuery = getEntityManager().createNativeQuery(sql);
            executeQuery.setParameter("project_name_id", project)
                    .setParameter("existingworkflow", existingworkflow)
                    .setParameter("newworkflow", newworkflow)
                    .setParameter("villageId", villageId);
            spatialUnit = executeQuery.getResultList();
            if (spatialUnit.size() > 0) {
                return spatialUnit;
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public List<ApfrStatSummary> getApfrStatSummary(int projectId, int villageId) {
        try {
            String sql = "select 'Total APFR' as name, 1 as ord,\n"
                    + "  coalesce(sum(case when p.genderid = 1 and sup.share_type_id = 7 then 1 else 0 end), 0) as males, \n"
                    + "  coalesce(sum(case when p.genderid = 2 and sup.share_type_id = 7 then 1 else 0 end), 0) as females,\n"
                    + "  coalesce(sum(case when sup.share_type_id = 8 then 1 else 0 end), 0) as collective \n"
                    + "from la_spatialunit_land su inner join la_ext_personlandmapping sup on su.landid = sup.landid inner join la_party_person p on sup.partyid = p.personid\n"
                    + "where su.claimtypeid = 1 and su.projectnameid = :projectId and su.isactive and (su.hierarchyid5 = :villageId or 0 = :villageId)\n"
                    + "\n"
                    + "union\n"
                    + "\n"
                    + "select 'Total APFR applications' as name, 2 as ord,\n"
                    + "  coalesce(sum(case when p.genderid = 1 and sup.share_type_id = 7 then 1 else 0 end), 0) as males, \n"
                    + "  coalesce(sum(case when p.genderid = 2 and sup.share_type_id = 7 then 1 else 0 end), 0) as females,\n"
                    + "  coalesce(sum(case when sup.share_type_id = 8 then 1 else 0 end), 0) as collective \n"
                    + "from la_spatialunit_land su inner join la_ext_personlandmapping sup on su.landid = sup.landid inner join la_party_person p on sup.partyid = p.personid\n"
                    + "where sup.transactionid not in (select transactionid from la_ext_transactionhistory) and \n"
                    + "  su.claimtypeid = 1 and su.projectnameid = :projectId and su.isactive and (su.hierarchyid5 = :villageId or 0 = :villageId)\n"
                    + "\n"
                    + "union\n"
                    + "\n"
                    + "select 'Total signed PV' as name, 3 as ord, \n"
                    + "  coalesce(sum(case when p.genderid = 1 and sup.share_type_id = 7 then 1 else 0 end), 0) as males, \n"
                    + "  coalesce(sum(case when p.genderid = 2 and sup.share_type_id = 7 then 1 else 0 end), 0) as females,\n"
                    + "  coalesce(sum(case when sup.share_type_id = 8 then 1 else 0 end), 0) as collective \n"
                    + "from la_spatialunit_land su inner join la_ext_personlandmapping sup on su.landid = sup.landid inner join la_party_person p on sup.partyid = p.personid\n"
                    + "where su.landid in (select landid from la_ext_landworkflowhistory where workflowid = 7) and sup.transactionid not in (select transactionid from la_ext_transactionhistory) and \n"
                    + "  su.claimtypeid = 1 and su.projectnameid = :projectId and su.isactive and (su.hierarchyid5 = :villageId or 0 = :villageId)\n"
                    + "\n"
                    + "union \n"
                    + "\n"
                    + "select 'Total APFR deliverd to applicants' as name, 4 as ord, \n"
                    + "  coalesce(sum(case when p.genderid = 1 and sup.share_type_id = 7 then 1 else 0 end), 0) as males, \n"
                    + "  coalesce(sum(case when p.genderid = 2 and sup.share_type_id = 7 then 1 else 0 end), 0) as females,\n"
                    + "  coalesce(sum(case when sup.share_type_id = 8 then 1 else 0 end), 0) as collective \n"
                    + "from la_spatialunit_land su inner join la_ext_personlandmapping sup on su.landid = sup.landid inner join la_party_person p on sup.partyid = p.personid\n"
                    + "where su.workflowstatusid = 9 and sup.transactionid not in (select transactionid from la_ext_transactionhistory) and \n"
                    + "  su.claimtypeid = 1 and su.projectnameid = :projectId and su.isactive and (su.hierarchyid5 = :villageId or 0 = :villageId)\n"
                    + "\n"
                    + "union\n"
                    + "\n"
                    + "select 'Total APFR area (ha)' as name, 5 as ord,\n"
                    + "  coalesce(sum(case when p.genderid = 1 and sup.share_type_id = 7 then (round((st_area(st_transform(geometry, 32630))/10000)\\:\\:numeric, 2)) else 0 end), 0) as males, \n"
                    + "  coalesce(sum(case when p.genderid = 2 and sup.share_type_id = 7 then (round((st_area(st_transform(geometry, 32630))/10000)\\:\\:numeric, 2)) else 0 end), 0) as females,\n"
                    + "  coalesce(sum(case when sup.share_type_id = 8 then (round((st_area(st_transform(geometry, 32630))/10000)\\:\\:numeric, 2)) else 0 end), 0) as collective \n"
                    + "from la_spatialunit_land su inner join la_ext_personlandmapping sup on su.landid = sup.landid inner join la_party_person p on sup.partyid = p.personid\n"
                    + "where sup.transactionid not in (select transactionid from la_ext_transactionhistory) and \n"
                    + "  su.claimtypeid = 1 and su.projectnameid = :projectId and su.isactive and (su.hierarchyid5 = :villageId or 0 = :villageId)\n"
                    + "\n"
                    + "union\n"
                    + "\n"
                    + "select 'Total APFR from mutation' as name, 6 as ord,\n"
                    + "  coalesce(sum(case when p.genderid = 1 and sup.share_type_id = 7 then 1 else 0 end), 0) as males, \n"
                    + "  coalesce(sum(case when p.genderid = 2 and sup.share_type_id = 7 then 1 else 0 end), 0) as females,\n"
                    + "  coalesce(sum(case when sup.share_type_id = 8 then 1 else 0 end), 0) as collective \n"
                    + "from la_spatialunit_land su inner join la_ext_personlandmapping sup on su.landid = sup.landid inner join la_party_person p on sup.partyid = p.personid\n"
                    + "where sup.transactionid in (select transactionid from la_ext_transactionhistory) and \n"
                    + "  su.claimtypeid = 1 and su.projectnameid = :projectId and su.isactive and (su.hierarchyid5 = :villageId or 0 = :villageId)\n"
                    + "\n"
                    + "union\n"
                    + "\n"
                    + "select 'Total APFR from mutation area (ha)' as name, 7 as ord,\n"
                    + "  coalesce(sum(case when p.genderid = 1 and sup.share_type_id = 7 then (round((st_area(st_transform(geometry, 32630))/10000)\\:\\:numeric, 2)) else 0 end), 0) as males, \n"
                    + "  coalesce(sum(case when p.genderid = 2 and sup.share_type_id = 7 then (round((st_area(st_transform(geometry, 32630))/10000)\\:\\:numeric, 2)) else 0 end), 0) as females,\n"
                    + "  coalesce(sum(case when sup.share_type_id = 8 then (round((st_area(st_transform(geometry, 32630))/10000)\\:\\:numeric, 2)) else 0 end), 0) as collective \n"
                    + "from la_spatialunit_land su inner join la_ext_personlandmapping sup on su.landid = sup.landid inner join la_party_person p on sup.partyid = p.personid\n"
                    + "where sup.transactionid in (select transactionid from la_ext_transactionhistory) and \n"
                    + "  su.claimtypeid = 1 and su.projectnameid = :projectId and su.isactive and (su.hierarchyid5 = :villageId or 0 = :villageId)\n"
                    + "\n"
                    + "order by ord";

            Query q = getEntityManager().createNativeQuery(sql, ApfrStatSummary.class);
            q.setParameter("projectId", projectId)
                    .setParameter("villageId", villageId);
            return q.getResultList();

        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public List<TransactionsStatSummary> getTransactionStatSummary(int projectId, int villageId) {
        try {
            String sql = "select mu.name, 1 as ord,\n"
                    + "  coalesce(sum(case when p.genderid = 1 and sup.share_type_id = 7 then 1 else 0 end), 0) as males, \n"
                    + "  coalesce(sum(case when p.genderid = 1 and sup.share_type_id = 7 then (round((st_area(st_transform(geometry, 32630))/10000)\\:\\:numeric, 2)) else 0 end), 0) as males_area, \n"
                    + "  coalesce(sum(case when p.genderid = 2 and sup.share_type_id = 7 then 1 else 0 end), 0) as females,\n"
                    + "  coalesce(sum(case when p.genderid = 2 and sup.share_type_id = 7 then (round((st_area(st_transform(geometry, 32630))/10000)\\:\\:numeric, 2)) else 0 end), 0) as females_area,\n"
                    + "  coalesce(sum(case when sup.share_type_id = 8 then 1 else 0 end), 0) as collective,\n"
                    + "  coalesce(sum(case when sup.share_type_id = 8 then (round((st_area(st_transform(geometry, 32630))/10000)\\:\\:numeric, 2)) else 0 end), 0) as collective_area \n"
                    + "from la_spatialunit_land su inner join (la_ext_personlandmapping sup inner join mutation_type mu on sup.mutation_id = mu.id) \n"
                    + "  on su.landid = sup.landid inner join la_party_person p on sup.partyid = p.personid\n"
                    + "where sup.transactionid in (select transactionid from la_ext_transactionhistory) and \n"
                    + "  su.claimtypeid in (1,2) and su.projectnameid = :projectId and su.isactive and (su.hierarchyid5 = :villageId or 0 = :villageId)\n"
                    + "group by mu.name\n"
                    + "\n"
                    + "union\n"
                    + "\n"
                    + "select 'Autorisation de Mise en Valeur' as name, 2 as ord,\n"
                    + "  coalesce(sum(case when p.genderid = 1 and p.nop_id is null then 1 else 0 end), 0) as males, \n"
                    + "  coalesce(sum(case when p.genderid = 1 and p.nop_id is null then (round((st_area(st_transform(geometry, 32630))/10000)\\:\\:numeric, 2)) else 0 end), 0) as males_area, \n"
                    + "  coalesce(sum(case when p.genderid = 2 and p.nop_id is null then 1 else 0 end), 0) as females,\n"
                    + "  coalesce(sum(case when p.genderid = 2 and p.nop_id is null then (round((st_area(st_transform(geometry, 32630))/10000)\\:\\:numeric, 2)) else 0 end), 0) as females_area,\n"
                    + "  coalesce(sum(case when p.nop_id is not null then 1 else 0 end), 0) as collective,\n"
                    + "  coalesce(sum(case when p.nop_id is not null then (round((st_area(st_transform(geometry, 32630))/10000)\\:\\:numeric, 2)) else 0 end), 0) as collective_area \n"
                    + "from (la_spatialunit_land su inner join la_ext_permission per on su.landid = per.landid) inner join la_party_person p on per.applicantid = p.personid\n"
                    + "where per.terminatedid is null and \n"
                    + "  su.claimtypeid in (1,2) and su.projectnameid = :projectId and su.isactive and (su.hierarchyid5 = :villageId or 0 = :villageId)\n"
                    + "\n"
                    + "union\n"
                    + "\n"
                    + "select pr.processname as name, 3 as ord,\n"
                    + "  coalesce(sum(case when p.genderid = 1 and ((td.processid = 1 and individual) or (td.processid = 10 and p.nop_id is null)) then 1 else 0 end), 0) as males, \n"
                    + "  coalesce(sum(case when p.genderid = 1 and ((td.processid = 1 and individual) or (td.processid = 10 and p.nop_id is null)) then (round((st_area(st_transform(geometry, 32630))/10000)\\:\\:numeric, 2)) else 0 end), 0) as males_area, \n"
                    + "  coalesce(sum(case when p.genderid = 2 and ((td.processid = 1 and individual) or (td.processid = 10 and p.nop_id is null)) then 1 else 0 end), 0) as females,\n"
                    + "  coalesce(sum(case when p.genderid = 2 and ((td.processid = 1 and individual) or (td.processid = 10 and p.nop_id is null)) then (round((st_area(st_transform(geometry, 32630))/10000)\\:\\:numeric, 2)) else 0 end), 0) as females_area,\n"
                    + "  coalesce(sum(case when (td.processid = 1 and not individual) or (td.processid = 10 and p.nop_id is not null) then 1 else 0 end), 0) as collective,\n"
                    + "  coalesce(sum(case when (td.processid = 1 and not individual) or (td.processid = 10 and p.nop_id is not null) then (round((st_area(st_transform(geometry, 32630))/10000)\\:\\:numeric, 2)) else 0 end), 0) as collective_area \n"
                    + "from (la_spatialunit_land su inner join (la_lease l inner join (la_ext_transactiondetails td inner join la_ext_process pr on td.processid = pr.processid) on l.leaseid = td.moduletransid) \n"
                    + "  on su.landid = l.landid) inner join la_party_person p on l.personid = p.personid\n"
                    + "where td.processid in (1,10) and \n"
                    + "  su.claimtypeid in (1,2) and su.projectnameid = :projectId and su.isactive and (su.hierarchyid5 = :villageId or 0 = :villageId)\n"
                    + "group by pr.processname\n"
                    + "\n"
                    + "order by ord, name";

            Query q = getEntityManager().createNativeQuery(sql, TransactionsStatSummary.class);
            q.setParameter("projectId", projectId)
                    .setParameter("villageId", villageId);
            return q.getResultList();

        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public List<ResourcesStatSummary> getResourcesStatSummary(int projectId) {
        try {
            String sql = "select sum(total) as total, sum(chartered) as chartered, sum(adopted) as adopted\n"
                    + "from\n"
                    + "(select count(1) as total, sum(case when chartered then 1 else 0 end) as chartered, sum(case when validated_by_council then 1 else 0 end) as adopted\n"
                    + "from la_spatialunit_resource_land\n"
                    + "where isactive and projectnameid = :projectId\n"
                    + "\n"
                    + "union \n"
                    + "\n"
                    + "select count(1) as total, sum(case when chartered then 1 else 0 end) as chartered, sum(case when validated_by_council then 1 else 0 end) as adopted\n"
                    + "from la_spatialunit_resource_line\n"
                    + "where isactive and projectnameid = :projectId\n"
                    + "\n"
                    + "union \n"
                    + "\n"
                    + "select count(1) as total, sum(case when chartered then 1 else 0 end) as chartered, sum(case when validated_by_council then 1 else 0 end) as adopted\n"
                    + "from la_spatialunit_resource_point\n"
                    + "where isactive and projectnameid = :projectId) res";

            Query q = getEntityManager().createNativeQuery(sql, ResourcesStatSummary.class);
            q.setParameter("projectId", projectId);
            return q.getResultList();

        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public List<Object> findparcelcountbygender(int project, String tag, Integer villageId) {
        int newworkflow_id = 0;
        int existingworkflow_id = 0;

        if (tag.equalsIgnoreCase("NEW")) {
            newworkflow_id = 1;
            existingworkflow_id = 10;
        } else if (tag.equalsIgnoreCase("REGISTERED")) {
            newworkflow_id = 2;
            existingworkflow_id = 11;
        } else if (tag.equalsIgnoreCase("APFR")) {
            newworkflow_id = 7;
            existingworkflow_id = 13;
        }

        try {
            List<Object> spatialUnit;
            String query = "select g.gender, sum(case when p.genderid is null then 0 else 1 end) as cnt \n"
                    + "from la_partygroup_gender g left join (select p.genderid from la_party_person p \n"
                    + "  inner join la_ext_personlandmapping r on p.personid = r.partyid \n"
                    + "  inner join la_spatialunit_land sp on r.landid = sp.landid \n"
                    + "  where sp.projectnameid = :project_name_id and sp.hierarchyid5 = :villageId and sp.isactive "
                    + "  and ((sp.workflowstatusid >= :newworkflow_id and sp.workflowstatusid < 10) or sp.workflowstatusid >= :existingworkflow_id)) p\n"
                    + "  on g.genderid = p.genderid \n"
                    + "group by g.genderid \n"
                    + "order by g.genderid";

            Query executeQuery = getEntityManager().createNativeQuery(query);
            executeQuery.setParameter("project_name_id", project)
                    .setParameter("newworkflow_id", newworkflow_id)
                    .setParameter("existingworkflow_id", existingworkflow_id)
                    .setParameter("villageId", villageId);
            spatialUnit = executeQuery.getResultList();
            if (spatialUnit.size() > 0) {
                return spatialUnit;
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @Override
    public List<Object> findRegistrytable(int project, String tag, Integer villageId) {
        try {
            int workflowId = 0;
            if (tag.equalsIgnoreCase("PROCESSEDAPPLICATION")) {
                workflowId = 3;
            } else if (tag.equalsIgnoreCase("PUBLISHEDAPPLICATION")) {
                workflowId = 6;
            } else if (tag.equalsIgnoreCase("PROCESSEDAPFR")) {
                workflowId = 7;
            }

            String query = "select p.firstname, p.lastname, sp.application_no, sp.applicationdate \n"
                    + "from la_party_person p inner join la_ext_personlandmapping r on p.personid = r.partyid \n"
                    + "  inner join la_spatialunit_land sp on r.landid = sp.landid \n"
                    + "where sp.projectnameid = :project_name_id and sp.hierarchyid5 = :villageId and sp.isactive and sp.workflowstatusid = :workId \n"
                    + "order by p.firstname, p.lastname";

            Query executeQuery = getEntityManager().createNativeQuery(query);
            executeQuery.setParameter("project_name_id", project)
                    .setParameter("workId", workflowId)
                    .setParameter("villageId", villageId);
            List<Object> spatialUnit = executeQuery.getResultList();
            if (spatialUnit.size() > 0) {
                return spatialUnit;
            }

        } catch (Exception e) {
            logger.error(e);
            return null;
        }
        return null;
    }

    @Override
    public List<GeometryPoint> getGeometryPoints(int landid) {
        Query q = getEntityManager().createNativeQuery("select * from get_geometry_points(" + landid + ")", GeometryPoint.class);
        return q.getResultList();
    }
}
