
/* ************@RMSI/NK add for region to district*******************************/
package com.rmsi.mast.studio.dao;

import java.util.List;

import com.rmsi.mast.studio.domain.ProjectRegion;
import com.rmsi.mast.studio.domain.VillageSearchResult;

public interface ProjectRegionDAO extends GenericDAO<ProjectRegion, Integer> {

    Integer getVillageCount(String villageName);
    
    List<VillageSearchResult> searchVillage(String villageName, Integer startpos);
    
    List<ProjectRegion> getVillagesByProject(int id);
    
    VillageSearchResult getVillage(Integer id);
            
    List<ProjectRegion> getAllCommunes();
    
    List<ProjectRegion> getCommunesByProvince(int provinceId);
    
    List<ProjectRegion> findAllCountry();

    List<ProjectRegion> findRegionByCountry(Integer countryname);

    List<ProjectRegion> findDistrictByRegion(Integer countryname);

    List<ProjectRegion> findVillageByDistrict(Integer countryname);

    List<ProjectRegion> findPlaceByVillage(Integer countryname);

    ProjectRegion findProjectRegionById(Integer id);

    List<ProjectRegion> findAllProjectRegion();
    
    Boolean deleteVillage(Integer id);
    
    ProjectRegion createVillage(ProjectRegion village);
    
    List<ProjectRegion> getAllProvinces();
}
