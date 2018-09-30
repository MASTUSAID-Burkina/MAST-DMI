package com.rmsi.mast.studio.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.rmsi.mast.studio.dao.ProjectRegionDAO;
import com.rmsi.mast.studio.domain.ProjectRegion;
import com.rmsi.mast.studio.domain.VillageSearchResult;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ProjectRegionHibernateDAO extends GenericHibernateDAO<ProjectRegion, Integer>
        implements ProjectRegionDAO {

    @SuppressWarnings("unchecked")
    public List<ProjectRegion> findAllCountry() {

        List<ProjectRegion> project = new ArrayList<ProjectRegion>();
        String hqlstr = "Select p from ProjectRegion p where p.laSpatialunitgroup.spatialunitgroupid=1 ";

        try {
            project = getEntityManager().createQuery(hqlstr).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return project;
    }

    @SuppressWarnings("unchecked")
    public List<ProjectRegion> findRegionByCountry(Integer Id) {

        List<ProjectRegion> project = new ArrayList<ProjectRegion>();

        try {
            project = getEntityManager().createQuery("Select p from ProjectRegion p where  p.laSpatialunitgroup.spatialunitgroupid=2 and  p.uperhierarchyid = :Id").setParameter("Id", Id).getResultList();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return project;
    }

    @SuppressWarnings("unchecked")
    public List<ProjectRegion> findDistrictByRegion(Integer Id) {
        List<ProjectRegion> project = new ArrayList<ProjectRegion>();

        try {
            project = getEntityManager().createQuery("Select p from ProjectRegion p where  p.laSpatialunitgroup.spatialunitgroupid=3 and  p.uperhierarchyid = :Id").setParameter("Id", Id).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return project;
    }

    @SuppressWarnings("unchecked")
    public List<ProjectRegion> findVillageByDistrict(Integer Id) {
        List<ProjectRegion> project = new ArrayList<ProjectRegion>();

        try {
            project = getEntityManager().createQuery("Select p from ProjectRegion p where  p.laSpatialunitgroup.spatialunitgroupid=4 and  p.uperhierarchyid = :Id").setParameter("Id", Id).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return project;
    }

    @SuppressWarnings("unchecked")
    public List<ProjectRegion> findPlaceByVillage(Integer Id) {
        List<ProjectRegion> project = new ArrayList<ProjectRegion>();

        try {
            project = getEntityManager().createQuery("Select p from ProjectRegion p where  p.laSpatialunitgroup.spatialunitgroupid=5 and  p.uperhierarchyid = :Id").setParameter("Id", Id).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return project;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ProjectRegion findProjectRegionById(Integer id) {

        List<ProjectRegion> project = new ArrayList<ProjectRegion>();

        try {
            project = getEntityManager().createQuery("Select p from ProjectRegion p where p.isactive=true and  p.hierarchyid = :Id").setParameter("Id", id).getResultList();
            if (project.size() > 0) {
                return project.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public List<ProjectRegion> findAllProjectRegion() {

        List<ProjectRegion> project = new ArrayList<ProjectRegion>();

        try {
            project = getEntityManager().createQuery("Select p from ProjectRegion p where p.isactive=true").getResultList();
            if (project.size() > 0) {
                return project;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public Integer getVillageCount(String villageName) {
        Integer count = 0;
        try {
            StringBuilder queryStr = new StringBuilder("Select v from ProjectRegion v join v.laSpatialunitgroup g where v.isactive = true and g.spatialunitgroupid = 5 ");

            if (!"".equals(villageName)) {
                queryStr.append("and (lower(v.name) like :villageName or lower(v.nameEn) like :villageName) ");
            }

            queryStr.append("order by v.name");

            Query query = getEntityManager().createQuery(queryStr.toString());

            if (!"".equals(villageName)) {
                query.setParameter("villageName", "%" + villageName.trim().toLowerCase() + "%");
            }

            @SuppressWarnings("unchecked")
            List<?> villages = query.getResultList();
            if (villages.size() > 0) {
                count = Integer.valueOf(villages.size());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    @Override
    public List<VillageSearchResult> searchVillage(String villageName, Integer startpos) {
        try {
            StringBuilder q = new StringBuilder(
                    "select v.hierarchyid, v.isactive, v.name, v.name_en, v.code, v.cfv_agent, "
                    + "c.name as commune_name, c.name_en as commune_name_en, "
                    + "p.name as province_name, p.name_en as province_name_en, v.uperhierarchyid, c.uperhierarchyid as province_id "
                    + "from la_spatialunitgroup_hierarchy v "
                    + "inner join la_spatialunitgroup_hierarchy c on v.uperhierarchyid = c.hierarchyid "
                    + "inner join la_spatialunitgroup_hierarchy p on c.uperhierarchyid = p.hierarchyid "
                    + "where v.isactive = true and v.spatialunitgroupid = 5 ");

            if (!"".equals(villageName)) {
                q.append("and (lower(v.name) like :villageName or lower(v.name_en) like :villageName) ");
            }

            q.append("order by v.name");

            Query query = getEntityManager().createNativeQuery(q.toString(), VillageSearchResult.class);

            if (!"".equals(villageName)) {
                query.setParameter("villageName", "%" + villageName.trim().toLowerCase() + "%");
            }

            return query.setFirstResult(startpos).setMaxResults(10).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<ProjectRegion> getVillagesByProject(int id){
        try {
            String q = "select v.hierarchyid, v.uperhierarchyid, v.isactive, v.name, v.name_en, v.code, v.cfv_agent, null as spatialunitgroupid "
                    + "from la_spatialunitgroup_hierarchy v inner join la_ext_projectarea p on v.uperhierarchyid = p.hierarchyid4 "
                    + "where p.projectnameid = :projectId and v.isactive = true and v.spatialunitgroupid = 5 order by v.name";

            Query query = getEntityManager().createNativeQuery(q, ProjectRegion.class);
            query.setParameter("projectId", id);

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<ProjectRegion> getVillagesByProjectName(String name){
        try {
            String q = "select v.hierarchyid, v.uperhierarchyid, v.isactive, v.name, v.name_en, v.code, v.cfv_agent, null as spatialunitgroupid "
                    + "from la_spatialunitgroup_hierarchy v inner join (la_ext_projectarea p inner join la_spatialsource_projectname pn on p.projectnameid = pn.projectnameid) on v.uperhierarchyid = p.hierarchyid4 "
                    + "where pn.projectname = :projectName and v.isactive = true and v.spatialunitgroupid = 5 order by v.name";

            Query query = getEntityManager().createNativeQuery(q, ProjectRegion.class);
            query.setParameter("projectName", name);

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public VillageSearchResult getVillage(Integer id) {
        try {
            StringBuilder q = new StringBuilder(
                    "select v.hierarchyid, v.isactive, v.name, v.name_en, v.code, v.cfv_agent, "
                    + "c.name as commune_name, c.name_en as commune_name_en, "
                    + "p.name as province_name, p.name_en as province_name_en, v.uperhierarchyid, c.uperhierarchyid as province_id "
                    + "from la_spatialunitgroup_hierarchy v "
                    + "inner join la_spatialunitgroup_hierarchy c on v.uperhierarchyid = c.hierarchyid "
                    + "inner join la_spatialunitgroup_hierarchy p on c.uperhierarchyid = p.hierarchyid "
                    + "where v.spatialunitgroupid = 5 and v.hierarchyid = :villageId ");

            Query query = getEntityManager().createNativeQuery(q.toString(), VillageSearchResult.class);
            query.setParameter("villageId", id);

            return (VillageSearchResult) query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<ProjectRegion> getAllCommunes() {
        try {
            Query query = getEntityManager().createQuery("Select v from ProjectRegion v join v.laSpatialunitgroup g where v.isactive = true and g.spatialunitgroupid = 4 order by v.name");
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<ProjectRegion> getCommunesByProvince(int provinceId) {
        try {
            Query query = getEntityManager().createQuery("Select p from ProjectRegion p join p.laSpatialunitgroup g where p.uperhierarchyid = :provinceId and p.isactive = true and g.spatialunitgroupid = 4 order by p.name");
            return query.setParameter("provinceId", provinceId).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    @Transactional
    public Boolean deleteVillage(Integer id) {
        try {
            ProjectRegion village = findById(id, false);
            if (village != null) {
                getEntityManager().remove(village);
                getEntityManager().flush();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    @Transactional
    public ProjectRegion createVillage(ProjectRegion village) {
        return makePersistent(village);
    }

    @Override
    public List<ProjectRegion> getAllProvinces() {
        try {
            Query query = getEntityManager().createQuery("Select v from ProjectRegion v join v.laSpatialunitgroup g where v.isactive = true and g.spatialunitgroupid = 3 order by v.name");
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
