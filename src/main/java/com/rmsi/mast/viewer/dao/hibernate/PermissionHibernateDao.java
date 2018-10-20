package com.rmsi.mast.viewer.dao.hibernate;

import java.util.List;


import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.rmsi.mast.studio.dao.hibernate.GenericHibernateDAO;
import com.rmsi.mast.studio.domain.LaPartyPerson;
import com.rmsi.mast.studio.domain.Permission;
import com.rmsi.mast.viewer.dao.PermissionDao;
import java.util.Date;

@Repository
public class PermissionHibernateDao extends GenericHibernateDAO<Permission, Integer> implements PermissionDao {

    Logger logger = Logger.getLogger(PermissionHibernateDao.class);
    
    @Override
    public Permission findById(int id) {
        Permission p = super.findById(id, false);
        setApplicant(p);
        return p;
    }

    @Override
    public Permission findRegisteredPermissionByPropId(long usin) {
        String strQuery = "select p.* "
                + "from la_ext_permission p inner join la_ext_transactiondetails t on p.transactionid = t.transactionid "
                + "where p.active and p.terminatedid is null and t.applicationstatusid=7 and p.landid=" + usin;
        List<Permission> permissions = getEntityManager().createNativeQuery(strQuery, Permission.class).getResultList();
        if (permissions.size() > 0) {
            setApplicant(permissions.get(0));
            return permissions.get(0);
        }
        return null;
    }

    @Override
    public Permission findPendingPermissionByPropId(long usin){
        String strQuery = "select p.* "
                + "from la_ext_permission p inner join la_ext_transactiondetails t on p.transactionid = t.transactionid "
                + "where p.active and p.terminatedid is null and t.applicationstatusid=1 and p.landid=" + usin;
        List<Permission> permissions = getEntityManager().createNativeQuery(strQuery, Permission.class).getResultList();
        if (permissions.size() > 0) {
            setApplicant(permissions.get(0));
            return permissions.get(0);
        }
        return null;
    }
    
    @Override
    public Permission findPermissionByTransactionId(int transactionId){
        String strQuery = "select p.* "
                + "from la_ext_permission p where p.transactionid=" + transactionId;
        List<Permission> permissions = getEntityManager().createNativeQuery(strQuery, Permission.class).getResultList();
        if (permissions.size() > 0) {
            setApplicant(permissions.get(0));
            return permissions.get(0);
        }
        return null;
    }
    
    @Override
    public Permission findPendingTerminationPermissionByPropId(long usin) {
        String strQuery = "select p.* "
                + "from la_ext_permission p inner join la_ext_transactiondetails t on p.transactionid = t.transactionid "
                + "where p.active and p.terminatedid is not null and t.applicationstatusid=1 and p.landid=" + usin;
        List<Permission> permissions = getEntityManager().createNativeQuery(strQuery, Permission.class).getResultList();
        if (permissions.size() > 0) {
            setApplicant(permissions.get(0));
            return permissions.get(0);
        }
        return null;
    }
    
    @Override
    public boolean checkForRegisteredPermission(long usin){
        return findRegisteredPermissionByPropId(usin) != null;
    }

    @Override
    public Permission save(Permission permission) {
        return makePersistent(permission);
    }
    
    private void setApplicant(Permission permission) {
        String q = "select LP.firstname, LP.middlename, LP.lastname, LG.gender, LM.maritalstatus, LP.identityno, LI.identitytypeid, dateofbirth, LP.contactno, "
                + " LP.address, LM.maritalstatusid, LG.genderid,LP.personid, LI.identitytype, "
                + "LP.birthplace, LP.profession, LP.idcard_establishment_date, LP.idcard_origin, LP.mandate_issuance_date, LP.mandate_location, "
                + "LP.nop_id, NP.nature_of_power_fr, LP.fathername, LP.mothername "
                + "from la_party_person LP left join la_partygroup_gender LG on LP.genderid = LG.genderid left join la_partygroup_maritalstatus LM on "
                + "LP.maritalstatusid = LM.maritalstatusid  left join la_partygroup_identitytype LI on LP.identitytypeid = Li.identitytypeid "
                + "left join nature_of_power NP on LP.nop_id = NP.nop_id "
                + "where LP.personid = " + permission.getApplicantid();

        List<Object[]> lstParty = getEntityManager().createNativeQuery(q).getResultList();
        Object[] party = lstParty.get(0);

        LaPartyPerson person = new LaPartyPerson();
        person.setFirstname(party[0] + "");
        person.setMiddlename(party[1] + "");
        person.setLastname(party[2] + "");
        person.setGendername(party[3] + "");

        if (party[14] != null) {
            person.setBirthplace(party[14].toString());
        }
        if (party[15] != null) {
            person.setProfession(party[15].toString());
        }
        if (party[16] != null) {
            person.setIdDate((Date) party[16]);
        }
        if (party[17] != null) {
            person.setIdOrigin(party[17].toString());
        }
        if (party[18] != null) {
            person.setMandateDate((Date) party[18]);
        }
        if (party[19] != null) {
            person.setMandateLocation(party[19].toString());
        }
        if (party[20] != null) {
            person.setNopId(Integer.parseInt(party[20].toString()));
        }
        if (party[21] != null) {
            person.setNatureOfPowerName(party[21].toString());
        }
        if (party[22] != null) {
            person.setFathername(party[22].toString());
        }
        if (party[23] != null) {
            person.setMothername(party[23].toString());
        }

        if (null != party[4] && !party[4].toString().isEmpty()) {
            person.setMaritalstatus(party[4].toString());
        }
        if (null != party[13] && !party[13].toString().isEmpty()) {
            person.setIdentitytype(party[13].toString());
        }
        person.setIdentityno(party[5] + "");
        if (null != party[6]) {
            person.setIdentitytypeid(Integer.parseInt(party[6].toString()));
        }
        if (null != party[7]) {
            person.setDateofbirth((Date) party[7]);
        }

        person.setContactno(party[8] + "");
        if (null != party[9] && !party[9].toString().isEmpty()) {
            person.setAddress(party[9] + "");
        } else {
            person.setAddress("");
        }
        try {
            if (null != party[10] && !party[10].toString().isEmpty()) {
                person.setMaritalstatusid(Integer.parseInt(party[10].toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (null != party[11] && !party[11].toString().isEmpty()) {
                person.setGenderid(Integer.parseInt(party[11].toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        person.setPersonid(Long.parseLong(party[12].toString()));

        if (null != party[7] && !party[7].toString().isEmpty()) {
            person.setDob(party[7].toString());
        }
        person.setPersontype("1");
        permission.setApplicant(person);
    }
}
