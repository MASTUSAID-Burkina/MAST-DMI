package com.rmsi.mast.viewer.dao.hibernate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.rmsi.mast.studio.dao.hibernate.GenericHibernateDAO;
import com.rmsi.mast.studio.domain.LaExtFinancialagency;
import com.rmsi.mast.studio.domain.LaSpatialunitLand;
import com.rmsi.mast.studio.domain.La_Month;
import com.rmsi.mast.viewer.dao.RegistrationRecordsDao;

@Repository
public class RegistrationRecordsHibernateDAO extends GenericHibernateDAO<LaSpatialunitLand, Long>
        implements RegistrationRecordsDao {

    private static final Logger logger = Logger.getLogger(RegistrationRecordsHibernateDAO.class);

    @SuppressWarnings("unchecked")
    @Override
    public List<LaSpatialunitLand> findOrderedSpatialUnitRegistry(
            String defaultProject, int startfrom) {
        List<LaSpatialunitLand> lstLaSpatialunitLand = new ArrayList<LaSpatialunitLand>();
        try {
            String hql = "Select Distinct on (LD.landid,LD.modifieddate) LD.landid, LD.landno, LC.claimtypeid, LC.claimtype_en, LD.area, la.applicationstatus_en,TR.transactionid, LP.firstname, LP.middlename, LP.lastname, LH.name_en ,ST.landsharetype "
                    + "from la_spatialunit_land LD  "
                    + "Inner join la_ext_personlandmapping PL on LD.landid = PL.landid inner join la_ext_transactiondetails TR  "
                    + "on PL.transactionid = TR.transactionid inner Join la_right_claimtype LC on LD.claimtypeid=LC.claimtypeid  "
                    + "inner Join la_ext_applicationstatus la on la.applicationstatusid = LD.applicationstatusid  "
                    + "inner Join la_party_person LP on PL.partyid = LP.personid 	and LP.ownertype=1"
                    + "inner join la_spatialunitgroup_hierarchy LH on LH.hierarchyid = LD.hierarchyid5 "
                    + "inner join la_ext_registrationsharetype LS on LS.landid = LD.landid "
                    + "inner Join la_right_landsharetype ST on ST.landsharetypeid =  LS.landsharetypeid "
                    + "where PL.persontypeid=1 and workflowstatusid in (9,14) and LD.projectnameid = " + defaultProject + " and LD.isactive = true and PL.isactive = true order by LD.modifieddate ";

            List<Object[]> arrObject = getEntityManager().createNativeQuery(hql).setFirstResult(startfrom).setMaxResults(10).getResultList();

            for (Object[] object : arrObject) {
                LaSpatialunitLand laSpatialunitLand = new LaSpatialunitLand();
                laSpatialunitLand.setLandid(Long.valueOf(object[0].toString()));
                laSpatialunitLand.setLandno((String) object[1]);
                laSpatialunitLand.setClaimtypeid(Integer.valueOf(object[2].toString()));
                laSpatialunitLand.setClaimtype_en(object[3].toString());
                laSpatialunitLand.setArea(Double.parseDouble(object[4].toString()));
                laSpatialunitLand.setApplicationstatus_en(object[5].toString());
                laSpatialunitLand.setTransactionid(Integer.valueOf(object[6].toString()));
                laSpatialunitLand.setFirstname((String) object[7]);
                laSpatialunitLand.setLastname((String) object[9]);
                if (null != object[10] && !((String) object[10]).isEmpty() && ((String) object[10]).length() > 2) {
                    String communityID = ((String) object[10]).substring(0, 2).toUpperCase();
                    laSpatialunitLand.setRegistrationNo(addRegistrationNo(communityID, (String) object[1]));
                }
                laSpatialunitLand.setLandnostrwithzero(addZeroinLandNo((String) object[0].toString()));
                if (null != object[11]) {
                    laSpatialunitLand.setTenancyType((String) object[11]);
                } else {
                    laSpatialunitLand.setTenancyType("");
                }
                lstLaSpatialunitLand.add(laSpatialunitLand);
            }
            return lstLaSpatialunitLand;
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LaSpatialunitLand> search(Long transactionid, Integer startfrom, String project, String villageId, String parcelId) {

        List<LaSpatialunitLand> lstLaSpatialunitLand = new ArrayList<LaSpatialunitLand>();

        String hql = "";
        String strWhere = "";

        if (transactionid != null && transactionid != 0) {
            strWhere = strWhere + " and TR.transactionid = " + transactionid;
        }

        if (!"".equals(villageId) && !villageId.equals("0")) {
            strWhere = strWhere + " and LD.hierarchyid5 = " + villageId;
        }

        if (!"".equals(parcelId)) {
            String strPattern = "^0+";
            parcelId = parcelId.replaceAll(strPattern, "");
            strWhere = strWhere + " and  LD.landid = '" + parcelId + "'";
        }

        hql = "Select Distinct on (LD.landid) LD.landid, LD.landno, LC.claimtypeid, LC.claimtype_en, LD.area, la.applicationstatus_en, TR.transactionid, "
                + "LP.firstname, LP.lastname, LP.address,la.applicationstatusid, LH.name_en ,ST.landsharetype "
                + "from la_spatialunit_land LD "
                + "Inner join la_ext_personlandmapping PL on LD.landid = PL.landid "
                + "inner join la_ext_transactiondetails TR on PL.transactionid = TR.transactionid "
                + "inner Join la_right_claimtype LC on LD.claimtypeid=LC.claimtypeid "
                + "inner Join la_ext_applicationstatus la on la.applicationstatusid = TR.applicationstatusid "
                + "inner join la_spatialunitgroup_hierarchy LH on LH.hierarchyid = LD.hierarchyid5 "
                + "inner Join  la_party_person LP on PL.partyid = LP.personid "
                + "inner join la_ext_registrationsharetype LS on LS.landid = LD.landid "
                + "inner Join la_right_landsharetype ST on ST.landsharetypeid =  LS.landsharetypeid "
                + "where LD.projectnameid = " + project + strWhere + " and workflowstatusid in (9,14) and LD.isactive = true and PL.isactive = true "; 

        try {
            List<Object[]> arrObject = getEntityManager().createNativeQuery(hql).getResultList();

            for (Object[] object : arrObject) {
                LaSpatialunitLand laSpatialunitLand = new LaSpatialunitLand();
                laSpatialunitLand.setLandid(Long.valueOf(object[0].toString()));
                laSpatialunitLand.setLandno((String) object[1]);
                laSpatialunitLand.setClaimtypeid(Integer.valueOf(object[2].toString()));
                laSpatialunitLand.setClaimtype_en(object[3].toString());
                laSpatialunitLand.setArea(4);
                laSpatialunitLand.setApplicationstatus_en(object[5].toString());
                laSpatialunitLand.setTransactionid(Integer.valueOf(object[6].toString()));
                laSpatialunitLand.setFirstname(object[7].toString());
                laSpatialunitLand.setLastname(object[8].toString());
                if (null != object[9]) {
                    laSpatialunitLand.setAddress(object[9].toString());
                }
                laSpatialunitLand.setApplicationstatusid(Integer.valueOf(object[10].toString()));

                if (null != object[11] && !((String) object[11]).isEmpty() && ((String) object[11]).length() > 2) {
                    String communityID = ((String) object[11]).substring(0, 2).toUpperCase();
                    laSpatialunitLand.setRegistrationNo(addRegistrationNo(communityID, (String) object[1]));
                }
                if (null != object[12] && !((String) object[12]).isEmpty()) {
                    laSpatialunitLand.setTenancyType(object[12].toString());
                }
                laSpatialunitLand.setLandnostrwithzero(addZeroinLandNo((String) object[0].toString()));

                lstLaSpatialunitLand.add(laSpatialunitLand);

            }
            return lstLaSpatialunitLand;

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Integer searchCount(Long transactionid, Integer startfrom, String project, String villageId, String parcelId) {
        String strWhere = "";
        String hql;
        if (transactionid != null && transactionid != 0) {
            strWhere = strWhere + " and TR.transactionid = " + transactionid;
        }

        if (!"".equals(villageId) && !villageId.equals("0")) {
            strWhere = strWhere + " and LD.hierarchyid5 = " + villageId;
        }

        if (!"".equals(parcelId)) {

            String strPattern = "^0+";
            parcelId = parcelId.replaceAll(strPattern, "");
            strWhere = strWhere + " and LD.landid = '" + parcelId + "'";
        }

        hql = "Select count(LD.landid) from la_spatialunit_land LD "
                + "Inner join la_ext_personlandmapping PL on LD.landid = PL.landid "
                + "inner join la_ext_transactiondetails TR on PL.transactionid = TR.transactionid "
                + "inner Join la_right_claimtype LC on LD.claimtypeid=LC.claimtypeid "
                + "inner Join la_ext_applicationstatus la on la.applicationstatusid = TR.applicationstatusid "
                + "inner Join  la_party_person LP on PL.partyid = LP.personid "
                + "inner join la_ext_registrationsharetype LS on LS.landid = LD.landid "
                + "inner Join la_right_landsharetype ST on ST.landsharetypeid =  LS .landsharetypeid "
                + "where workflowstatusid in (9,14) and LD.projectnameid = " + project + strWhere + " and LD.isactive = true and PL.isactive = true ";

        List<BigInteger> arrObject = getEntityManager().createNativeQuery(hql).getResultList();

        if (arrObject.size() > 0) {
            return Integer.parseInt(arrObject.get(0).toString());
        } else {
            return 0;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LaExtFinancialagency> getFinancialagencyDetails() {
        try {
            Query query = getEntityManager().createQuery("select lpm from LaExtFinancialagency lpm where isactive = true");
            List<LaExtFinancialagency> lstLaExtFinancialagency = query.getResultList();

            return lstLaExtFinancialagency;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public LaExtFinancialagency getFinancialagencyByID(int financial_AgenciesID) {
        try {
            Query query = getEntityManager().createQuery("select lpm from LaExtFinancialagency lpm where isactive = true and lpm.financialagencyid = :financialagencyId");
            List<LaExtFinancialagency> lstLaExtFinancialagency = query.setParameter("financialagencyId", financial_AgenciesID).getResultList();

            if (lstLaExtFinancialagency.size() > 0) {
                return lstLaExtFinancialagency.get(0);
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<La_Month> getmonthofleaseDetails() {
        try {
            Query query = getEntityManager().createQuery("select lpm from La_Month lpm where lpm.isactive = true");
            List<La_Month> lstLa_Month = query.getResultList();

            return lstLa_Month;

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public La_Month getLaMonthById(int no_Of_month_Lease) {

        try {
            String month = "" + no_Of_month_Lease;
            Query query = getEntityManager().createQuery("select lpm from La_Month lpm where lpm.isactive = true and lpm.month = :monthId");
            List<La_Month> lstLa_Month = query.setParameter("monthId", month).getResultList();

            if (lstLa_Month.size() > 0) {
                return lstLa_Month.get(0);
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Integer findSpatialUnitTempCount(String project, Integer startfrom) {

        try {
            String hql = "Select count( Distinct LD.landid) from la_spatialunit_land LD Inner join la_ext_personlandmapping PL on "
                    + "LD.landid = PL.landid inner join la_ext_transactiondetails TR "
                    + "on PL.transactionid = TR.transactionid inner Join la_right_claimtype LC on LD.claimtypeid=LC.claimtypeid "
                    + "inner Join la_ext_applicationstatus la on la.applicationstatusid = LD.applicationstatusid "
                    + "inner join la_ext_registrationsharetype LS on LS.landid = LD.landid "
                    + "inner Join la_party_person LP on PL.partyid = LP.personid and LP.ownertype=1"
                    + "inner Join la_right_landsharetype ST on ST.landsharetypeid =  LS.landsharetypeid "
                    + "where PL.persontypeid=1 and workflowstatusid in (9,14) and LD.projectnameid = " + project + " and LD.isactive = true and PL.isactive = true  ";// PL.isactive = true and in where clause --TR.applicationstatusid=5 and //--and TR.applicationstatusid = 5 --As discussed with Kamal And gaurav
            List<BigInteger> arrObject = getEntityManager().createNativeQuery(hql).getResultList();

            if (arrObject.size() > 0) {
                return Integer.parseInt(arrObject.get(0).toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
}
