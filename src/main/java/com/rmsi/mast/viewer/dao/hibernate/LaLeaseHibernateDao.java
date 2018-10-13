package com.rmsi.mast.viewer.dao.hibernate;

import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.rmsi.mast.studio.dao.hibernate.GenericHibernateDAO;
import com.rmsi.mast.studio.domain.LaLease;
import com.rmsi.mast.studio.domain.LaPartyPerson;
import com.rmsi.mast.studio.domain.LaSurrenderLease;
import com.rmsi.mast.studio.domain.SocialTenureRelationship;
import com.rmsi.mast.studio.domain.Status;
import com.rmsi.mast.viewer.dao.LaLeaseDao;
import java.util.Date;

@Repository
public class LaLeaseHibernateDao extends GenericHibernateDAO<LaLease, Integer> implements LaLeaseDao {

    Logger logger = Logger.getLogger(LaLeaseHibernateDao.class);

    @Override
    public LaLease saveLease(LaLease laLease) {
        try {
            return makePersistent(laLease);

        } catch (Exception ex) {
            logger.error(ex);
            throw ex;
        }
    }

    @Override
    public LaLease getLeaseById(Integer leaseId) {

        try {
            Query query = getEntityManager().createQuery("select la from LaLease la where la.leaseid = :leaseid");
            List<LaLease> lstLaExtFinancialagency = query.setParameter("leaseid", leaseId).getResultList();
            setLessee(lstLaExtFinancialagency.get(0));
            return lstLaExtFinancialagency.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public LaSurrenderLease getSurrenderLeaseById(Integer leaseId) {

        try {
            Query query = getEntityManager().createQuery("select la from LaSurrenderLease la where la.leaseid = :leaseid");
            @SuppressWarnings("unchecked")
            List<LaSurrenderLease> lstLaExtFinancialagency = query.setParameter("leaseid", leaseId).getResultList();

            return lstLaExtFinancialagency.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean disablelease(int leaseId) {
        try {
            Query query = getEntityManager().createQuery("UPDATE LaLease SET isactive = false  where leaseid = :leaseid");
            int updateFinal = query.setParameter("leaseid", leaseId).executeUpdate();

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
    public boolean checkForActiveLease(int landid, int processId) {
        try {
            String leaseRegCheck = "";
            if(processId != 5){
                leaseRegCheck = " or (t.applicationstatusid=1 and t.processid !=" + processId + ")";
            }
            Query query = getEntityManager().createNativeQuery("select l.* from la_lease l "
                    + "inner join la_ext_transactiondetails t on l.leaseid = t.moduletransid "
                    + "where l.landid = " + landid + " and l.isactive and t.processid in (1,10) and "
                    + "(t.applicationstatusid = 7" + leaseRegCheck + ")", LaLease.class);
            List<LaLease> leases = query.getResultList();

            if (leases.size() > 0) {
                return true;
            }
        } catch (Exception e) {
            logger.error(e);
            return false;
        }
        return false;
    }

    @Override
    public LaLease getLeaseByLandId(Long landId) {
        try {
            Query query = getEntityManager().createQuery("select la from LaLease la where la.landid = :landid and isactive=true");
            List<LaLease> leases = query.setParameter("landid", landId).getResultList();
            if (leases != null && leases.size() > 0) {
                setLessee(leases.get(0));
                return leases.get(0);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public LaLease getLeaseByTransactionId(Long transid){
        try {
            String strQuery = "select * from la_lease l inner join la_ext_transactiondetails trans on l.leaseid = trans.moduletransid "
                    + "where trans.processid in (1,10) and trans.transactionid = " + transid;
            List<LaLease> leases = getEntityManager().createNativeQuery(strQuery, LaLease.class).getResultList();
            if (leases.size() > 0) {
                setLessee(leases.get(0));
                return leases.get(0);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public LaLease getleaseobjbylandandprocessid(Long landId, Long processId) {
        try {
            String strQuery = "select * from la_lease l left join la_ext_transactiondetails trans on l.leaseid = trans.moduletransid where l.isactive= true and trans.applicationstatusid=1 and l.landid= " + landId + " and trans.processid=" + processId;
            List<LaLease> laLease = getEntityManager().createNativeQuery(strQuery, LaLease.class).getResultList();
            if (laLease.size() > 0) {
                return laLease.get(0);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<LaLease> getleaseeListByLandandPersonId(Long landId,
            Long personid) {

        try {
            Query query = getEntityManager().createQuery("select la from LaLease la where la.landid = :landid and la.personid = :personid");
            @SuppressWarnings("unchecked")
            List<LaLease> lstLaExtFinancialagency = query.setParameter("landid", landId).setParameter("personid", personid).getResultList();

            return lstLaExtFinancialagency;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<LaLease> getleaseobjbylandandprocessidList(Long landId,
            Long processId) {
        try {
            String strQuery = "select * from la_lease l left join la_ext_transactiondetails trans on l.leaseid = trans.moduletransid where l.isactive= true and trans.applicationstatusid=1 and l.landid= " + landId + " and trans.processid=" + processId;
            List<LaLease> laLease = getEntityManager().createNativeQuery(strQuery, LaLease.class).getResultList();
            if (laLease.size() > 0) {
                return laLease;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private void setLessee(LaLease lease) {
        String q = "select LP.firstname, LP.middlename, LP.lastname, LG.gender, LM.maritalstatus, LP.identityno, LI.identitytypeid, dateofbirth, LP.contactno, "
                + " LP.address, LM.maritalstatusid, LG.genderid,LP.personid, LI.identitytype, "
                + "LP.birthplace, LP.profession, LP.idcard_establishment_date, LP.idcard_origin, LP.mandate_issuance_date, LP.mandate_location, "
                + "LP.nop_id, NP.nature_of_power_fr, LP.fathername, LP.mothername "
                + "from la_party_person LP left join la_partygroup_gender LG on LP.genderid = LG.genderid left join la_partygroup_maritalstatus LM on "
                + "LP.maritalstatusid = LM.maritalstatusid  left join la_partygroup_identitytype LI on LP.identitytypeid = Li.identitytypeid "
                + "left join nature_of_power NP on LP.nop_id = NP.nop_id "
                + "where LP.personid = " + lease.getPersonid();

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
            lease.setBorrower(person);
    }
}
