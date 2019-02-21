package com.rmsi.mast.viewer.dao.hibernate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.rmsi.mast.studio.dao.hibernate.GenericHibernateDAO;
import com.rmsi.mast.studio.domain.LaPartyPerson;
import com.rmsi.mast.studio.domain.NonNaturalPerson;
import com.rmsi.mast.viewer.dao.LaPartyPersonDao;
import javax.persistence.Query;

/**
 *
 * @author Abhay.Pandey
 *
 */
@Repository
public class LaPartyPersonHibernateDao extends GenericHibernateDAO<LaPartyPerson, Long>
        implements LaPartyPersonDao {

    @SuppressWarnings("unchecked")
    @Override
    public LaPartyPerson getPartyPersonDetails(Integer landid) {

        String strQuery = "select LP.partyid,LP.persontypeid from la_ext_personlandmapping LP where landid = " + landid + " and isactive = true";
        List<Object[]> lst = getEntityManager().createNativeQuery(strQuery).getResultList();
        if (lst.size() > 0) {
            Object[] arrObj = lst.get(0);
            int partyId = Integer.parseInt(arrObj[0].toString());
            int persontypeId = Integer.parseInt(arrObj[1].toString());
            if (persontypeId == 1) {
                strQuery = "select LP.firstname, LP.middlename, LP.lastname, LG.gender, LM.maritalstatus, LP.identityno, LI.identitytypeid, dateofbirth, LP.contactno, LP.address, LM.maritalstatusid, LG.genderid "
                        + "from la_party_person LP left join la_partygroup_gender LG on LP.genderid = LG.genderid left join la_partygroup_maritalstatus LM on "
                        + "LP.maritalstatusid = LM.maritalstatusid left join la_partygroup_identitytype LI on LP.identitytypeid = Li.identitytypeid "
                        + "where LP.personid = " + partyId;

                List<Object[]> lstParty = getEntityManager().createNativeQuery(strQuery).getResultList();
                for (Object[] obj : lstParty) {
                    LaPartyPerson laPartyPerson = new LaPartyPerson();
                    laPartyPerson.setFirstname(obj[0] + "");
                    laPartyPerson.setMiddlename(obj[1] + "");
                    laPartyPerson.setLastname(obj[2] + "");
                    laPartyPerson.setGendername(obj[3] + "");
                    //laPartyPerson.setMaritalstatusid(Integer.parseInt(obj[4] + ""));
                    laPartyPerson.setIdentityno(obj[5] + "");
                    laPartyPerson.setIdentitytypeid(Integer.parseInt(obj[6].toString()));
                    //laPartyPerson.setDateofbirth(dateofbirth);
                    DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        if (null != obj[7] && !obj[7].toString().isEmpty()) {
                            Date dateofbirth = dateformat.parse(obj[7].toString());
                            laPartyPerson.setDateofbirth(dateofbirth);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    laPartyPerson.setContactno(obj[8] + "");
                    if (null != obj[9] && !obj[9].toString().isEmpty()) {
                        laPartyPerson.setAddress(obj[9] + "");
                    } else {
                        laPartyPerson.setAddress("");
                    }
                    //laPartyPerson.setMaritalstatusid(Integer.parseInt(obj[10].toString()));
                    try {
                        if (null != obj[10] && !obj[10].toString().isEmpty()) {
                            laPartyPerson.setMaritalstatusid(Integer.parseInt(obj[10].toString()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        if (null != obj[11] && !obj[11].toString().isEmpty()) {
                            laPartyPerson.setGenderid(Integer.parseInt(obj[11].toString()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (null != obj[7] && !obj[7].toString().isEmpty()) {
                        laPartyPerson.setDob(obj[7].toString());
                    }
                    return laPartyPerson;
                }
            } else {

            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getAllPartyPersonDetails(Integer landid) {
        List<Object> lstpartyperson = new ArrayList<>();
        try {
            String strQuery = "select LP.partyid,LP.persontypeid "
                    + "from la_ext_personlandmapping LP left join la_party_person psn on psn.personid=LP.partyid"
                    + " where landid = " + landid + " and LP.isactive = true order by psn.ownertype asc";
            List<Object[]> lst = getEntityManager().createNativeQuery(strQuery).getResultList();

            if (lst != null && lst.size() > 0) {
                for (Object[] arrObj : lst) {
                    int partyId = Integer.parseInt(arrObj[0].toString());
                    int persontypeId = Integer.parseInt(arrObj[1].toString());
                    if (persontypeId == 1 || persontypeId == 11) {
                        List<LaPartyPerson> persons = getParties(partyId, "" + persontypeId);
                        if (persons != null) {
                            lstpartyperson.addAll(persons);
                        }
                    }
                }
            }

            // Add organizations if any
            strQuery = "select LP.partyid,LP.persontypeid "
                    + "from la_ext_personlandmapping LP left join la_party_organization psn on psn.organizationid=LP.partyid"
                    + " where landid = " + landid + " and LP.isactive = true";
            lst = getEntityManager().createNativeQuery(strQuery).getResultList();
            if (lst != null && lst.size() > 0) {
                for (Object[] arrObj : lst) {
                    long partyId = Long.parseLong(arrObj[0].toString());
                    int persontypeId = Integer.parseInt(arrObj[1].toString());
                    NonNaturalPerson org = getOrganization(partyId, "" + persontypeId);
                    if (org != null) {
                        lstpartyperson.add(org);
                    }
                }
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return lstpartyperson;
    }

    private NonNaturalPerson getOrganization(long partyId, String personType) {
        NonNaturalPerson org = getEntityManager().find(NonNaturalPerson.class, partyId);
        if (org != null) {
            org.setPersontype(personType);
        }
        return org;
    }

    private List<LaPartyPerson> getParties(int partyId, String personType) {
        List<LaPartyPerson> persons = new ArrayList<LaPartyPerson>();

        String strQuery = "select LP.firstname, LP.middlename, LP.lastname, LG.gender, LM.maritalstatus, LP.identityno, LI.identitytypeid, dateofbirth, LP.contactno, "
                + " LP.address, LM.maritalstatusid, LG.genderid,LP.personid, LI.identitytype, "
                + "LP.birthplace, LP.profession, LP.idcard_establishment_date, LP.idcard_origin, LP.mandate_issuance_date, LP.mandate_location, "
                + "LP.nop_id, NP.nature_of_power_fr, LP.fathername, LP.mothername "
                + "from la_party_person LP left join la_partygroup_gender LG on LP.genderid = LG.genderid left join la_partygroup_maritalstatus LM on "
                + "LP.maritalstatusid = LM.maritalstatusid  left join la_partygroup_identitytype LI on LP.identitytypeid = Li.identitytypeid "
                + "left join nature_of_power NP on LP.nop_id = NP.nop_id "
                + "where LP.personid = " + partyId;

        List<Object[]> lstParty = getEntityManager().createNativeQuery(strQuery).getResultList();

        for (Object[] obj : lstParty) {
            LaPartyPerson laPartyPerson = new LaPartyPerson();
            laPartyPerson.setFirstname(obj[0] + "");
            if (obj[1] != null) {
                laPartyPerson.setMiddlename(obj[1].toString());
            }
            laPartyPerson.setLastname(obj[2] + "");
            if (obj[3] != null) {
                laPartyPerson.setGendername(obj[3].toString());
            }

            if (obj[14] != null) {
                laPartyPerson.setBirthplace(obj[14].toString());
            }
            if (obj[15] != null) {
                laPartyPerson.setProfession(obj[15].toString());
            }
            if (obj[16] != null) {
                laPartyPerson.setIdDate((Date) obj[16]);
            }
            if (obj[17] != null) {
                laPartyPerson.setIdOrigin(obj[17].toString());
            }
            if (obj[18] != null) {
                laPartyPerson.setMandateDate((Date) obj[18]);
            }
            if (obj[19] != null) {
                laPartyPerson.setMandateLocation(obj[19].toString());
            }
            if (obj[20] != null) {
                laPartyPerson.setNopId(Integer.parseInt(obj[20].toString()));
            }
            if (obj[21] != null) {
                laPartyPerson.setNatureOfPowerName(obj[21].toString());
            }
            if (obj[22] != null) {
                laPartyPerson.setFathername(obj[22].toString());
            }
            if (obj[23] != null) {
                laPartyPerson.setMothername(obj[23].toString());
            }
            laPartyPerson.setPersontype(personType);

            if (null != obj[4] && !obj[4].toString().isEmpty()) {
                laPartyPerson.setMaritalstatus(obj[4].toString());
            }
            if (null != obj[13] && !obj[13].toString().isEmpty()) {
                laPartyPerson.setIdentitytype(obj[13].toString());
            }
            laPartyPerson.setIdentityno(obj[5] + "");
            if (null != obj[6]) {
                laPartyPerson.setIdentitytypeid(Integer.parseInt(obj[6].toString()));
            }
            if (null != obj[7]) {
                laPartyPerson.setDateofbirth((Date) obj[7]);
            }

            laPartyPerson.setContactno(obj[8] + "");
            if (null != obj[9] && !obj[9].toString().isEmpty()) {
                laPartyPerson.setAddress(obj[9] + "");
            } else {
                laPartyPerson.setAddress("");
            }
            try {
                if (null != obj[10] && !obj[10].toString().isEmpty()) {
                    laPartyPerson.setMaritalstatusid(Integer.parseInt(obj[10].toString()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (null != obj[11] && !obj[11].toString().isEmpty()) {
                    laPartyPerson.setGenderid(Integer.parseInt(obj[11].toString()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            laPartyPerson.setPersonid(Long.parseLong(obj[12].toString()));

            if (null != obj[7] && !obj[7].toString().isEmpty()) {
                laPartyPerson.setDob(obj[7].toString());
            }
            persons.add(laPartyPerson);
        }
        return persons;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LaPartyPerson> fillAllPartyPersonDetails(Integer landid, Integer processid) {

        List<LaPartyPerson> lstpartyperson = new ArrayList<LaPartyPerson>();
        try {
            String strQuery = "select plm.partyid,plm.persontypeid from la_ext_personlandmapping plm left join la_party_person psn on psn.personid=plm.partyid left join la_ext_transactiondetails td on td.transactionid=plm.transactionid"
                    + " where plm.landid = " + landid + " and plm.isactive = true and plm.persontypeid=11 and td.processid=" + processid + " order by psn.ownertype asc";
            List<Object[]> lst = getEntityManager().createNativeQuery(strQuery).getResultList();
            if (lst.size() > 0) {
                for (Object[] arrObj : lst) {
                    int partyId = Integer.parseInt(arrObj[0].toString());
                    int persontypeId = Integer.parseInt(arrObj[1].toString());
                    if (persontypeId == 1 || persontypeId == 11) {
                        List<LaPartyPerson> persons = getParties(partyId, "" + persontypeId);
                        if (persons != null) {
                            lstpartyperson.addAll(persons);
                        }
                    }

                }
                return lstpartyperson;
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return lstpartyperson;
    }

    @SuppressWarnings("unchecked")
    @Override
    public LaPartyPerson getPartyPersonDetailssurrenderlease(Integer landid) {

        try {
            String strQuery = "select LP.personid,LP.monthid from la_lease LP where landid = " + landid + " and isactive = true";
            List<Object[]> lst = getEntityManager().createNativeQuery(strQuery).getResultList();
            if (lst.size() > 0) {
                Object[] arrObj = lst.get(0);
                int partyId = Integer.parseInt(arrObj[0].toString());
                int persontypeId = 1;
                if (persontypeId == 1) {

                    strQuery = "select LP.firstname, LP.middlename, LP.lastname, LG.gender, LM.maritalstatus, LP.identityno, LI.identitytypeid, dateofbirth, LP.contactno, LP.address, LM.maritalstatusid, LG.genderid ,"
                            + " lea.leaseamount,lea.leaseyear,lam.month,lea.leasestartdate,lea.leaseenddate  from la_party_person LP"
                            + " left join la_partygroup_gender LG on LP.genderid = LG.genderid"
                            + " left join la_partygroup_maritalstatus LM on  LP.maritalstatusid = LM.maritalstatusid"
                            + " left join la_partygroup_identitytype LI on LP.identitytypeid = Li.identitytypeid"
                            + " left join la_lease lea on  LP.personid=lea.personid"
                            + " left join la_ext_month lam on lam.monthid = lea.monthid"
                            + " where LP.personid =  " + partyId + " and lea.landid= " + landid;

                    List<Object[]> lstParty = getEntityManager().createNativeQuery(strQuery).getResultList();
                    for (Object[] obj : lstParty) {
                        LaPartyPerson laPartyPerson = new LaPartyPerson();
                        laPartyPerson.setFirstname(obj[0] + "");
                        laPartyPerson.setMiddlename(obj[1] + "");
                        laPartyPerson.setLastname(obj[2] + "");
                        laPartyPerson.setGendername(obj[3] + "");
                        laPartyPerson.setIdentityno(obj[5] + "");
                        laPartyPerson.setIdentitytypeid(Integer.parseInt(obj[6].toString()));
                        laPartyPerson.setPersonid(Long.parseLong(arrObj[0].toString()));

                        Integer intvalue;
                        try {
                            Double d = (Double) obj[12];
                            intvalue = d.intValue();
                            laPartyPerson.setHierarchyid1(Integer.parseInt(intvalue.toString()));	// Lease Amount	
                            Integer yearvalue = (Integer) obj[13];
                            Integer monthvalue = Integer.parseInt(obj[14].toString());
                            Integer totalmonthvalue = ((yearvalue) * 12) + monthvalue;
                            laPartyPerson.setHierarchyid2(Integer.parseInt(totalmonthvalue.toString()));
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                        // Lease Year					
                        DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            if (null != obj[7] && !obj[7].toString().isEmpty()) {
                                laPartyPerson.setDateofbirth((Date) obj[7]);
                            }

                            if (null != obj[15] && !obj[15].toString().isEmpty()) {
                                laPartyPerson.setLeaseStartdate((Date) obj[15]);
                            }

                            if (null != obj[16] && !obj[16].toString().isEmpty()) {
                                laPartyPerson.setLeaseEnddate((Date) obj[16]);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        laPartyPerson.setContactno(obj[8] + "");
                        if (null != obj[9] && !obj[9].toString().isEmpty()) {
                            laPartyPerson.setAddress(obj[9] + "");
                        } else {
                            laPartyPerson.setAddress("");
                        }

                        try {
                            if (null != obj[10] && !obj[10].toString().isEmpty()) {
                                laPartyPerson.setMaritalstatusid(Integer.parseInt(obj[10].toString()));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            if (null != obj[11] && !obj[11].toString().isEmpty()) {
                                laPartyPerson.setGenderid(Integer.parseInt(obj[11].toString()));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (null != obj[7] && !obj[7].toString().isEmpty()) {
                            laPartyPerson.setDob(obj[7].toString());
                        }
                        return laPartyPerson;
                    }
                }
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    public List<LaPartyPerson> getAllPartyPersonDetailsByTransactionId(
            Integer transid) {

        List<LaPartyPerson> lstpartyperson = new ArrayList<LaPartyPerson>();
        try {
            String strQuery = "select LP.partyid,LP.persontypeid from la_ext_personlandmapping LP left join la_party_person psn on"
                    + " psn.personid=LP.partyid  where transactionid = " + transid + " order by psn.ownertype asc";
            List<Object[]> lst = getEntityManager().createNativeQuery(strQuery).getResultList();
            if (lst.size() > 0) {
                for (Object[] arrObj : lst) {
                    //Object[] arrObj = lst.get(0);
                    int partyId = Integer.parseInt(arrObj[0].toString());
                    int persontypeId = Integer.parseInt(arrObj[1].toString());
                    if (persontypeId == 1 || persontypeId == 11) {
                        strQuery = "select LP.firstname, LP.middlename, LP.lastname, LG.gender, LM.maritalstatus, LP.identityno, LI.identitytypeid, dateofbirth, LP.contactno, "
                                + " LP.address, LM.maritalstatusid, LG.genderid,LP.personid, LI.identitytype, "
                                + "LP.birthplace, LP.profession, LP.idcard_establishment_date, LP.idcard_origin, LP.mandate_issuance_date, LP.mandate_location, "
                                + "LP.nop_id, NP.nature_of_power_fr, LP.fathername, LP.mothername "
                                + "from la_party_person LP left join la_partygroup_gender LG on LP.genderid = LG.genderid left join la_partygroup_maritalstatus LM on "
                                + "LP.maritalstatusid = LM.maritalstatusid  left join la_partygroup_identitytype LI on LP.identitytypeid = Li.identitytypeid "
                                + "left join nature_of_power NP on LP.nop_id = NP.nop_id "
                                + "where LP.personid = " + partyId;

                        List<Object[]> lstParty = getEntityManager().createNativeQuery(strQuery).getResultList();

                        for (Object[] obj : lstParty) {
                            LaPartyPerson laPartyPerson = new LaPartyPerson();
                            laPartyPerson.setFirstname(obj[0] + "");
                            laPartyPerson.setMiddlename(obj[1] + "");
                            laPartyPerson.setLastname(obj[2] + "");
                            laPartyPerson.setGendername(obj[3] + "");

                            if (obj[14] != null) {
                                laPartyPerson.setBirthplace(obj[14].toString());
                            }
                            if (obj[15] != null) {
                                laPartyPerson.setProfession(obj[15].toString());
                            }
                            if (obj[16] != null) {
                                laPartyPerson.setIdDate((Date) obj[16]);
                            }
                            if (obj[17] != null) {
                                laPartyPerson.setIdOrigin(obj[17].toString());
                            }
                            if (obj[18] != null) {
                                laPartyPerson.setMandateDate((Date) obj[18]);
                            }
                            if (obj[19] != null) {
                                laPartyPerson.setMandateLocation(obj[19].toString());
                            }
                            if (obj[20] != null) {
                                laPartyPerson.setNopId(Integer.parseInt(obj[20].toString()));
                            }
                            if (obj[21] != null) {
                                laPartyPerson.setNatureOfPowerName(obj[21].toString());
                            }
                            if (obj[22] != null) {
                                laPartyPerson.setFathername(obj[22].toString());
                            }
                            if (obj[23] != null) {
                                laPartyPerson.setMothername(obj[23].toString());
                            }

                            if (null != obj[4] && !obj[4].toString().isEmpty()) {
                                laPartyPerson.setMaritalstatus(obj[4].toString());
                            }
                            if (null != obj[13] && !obj[13].toString().isEmpty()) {
                                laPartyPerson.setIdentitytype(obj[13].toString());
                            }
                            laPartyPerson.setIdentityno(obj[5] + "");
                            if (null != obj[6]) {
                                laPartyPerson.setIdentitytypeid(Integer.parseInt(obj[6].toString()));
                            }
                            if (null != obj[7]) {
                                laPartyPerson.setDateofbirth((Date) obj[7]);
                            }

                            laPartyPerson.setContactno(obj[8] + "");
                            if (null != obj[9] && !obj[9].toString().isEmpty()) {
                                laPartyPerson.setAddress(obj[9] + "");
                            } else {
                                laPartyPerson.setAddress("");
                            }
                            try {
                                if (null != obj[10] && !obj[10].toString().isEmpty()) {
                                    laPartyPerson.setMaritalstatusid(Integer.parseInt(obj[10].toString()));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                if (null != obj[11] && !obj[11].toString().isEmpty()) {
                                    laPartyPerson.setGenderid(Integer.parseInt(obj[11].toString()));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            laPartyPerson.setPersonid(Long.parseLong(obj[12].toString()));

                            if (null != obj[7] && !obj[7].toString().isEmpty()) {
                                laPartyPerson.setDob(obj[7].toString());
                            }
                            laPartyPerson.setPersontype(persontypeId + "");
                            lstpartyperson.add(laPartyPerson);
                        }
                    }

                }
                return lstpartyperson;

            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return lstpartyperson;
    }
}
