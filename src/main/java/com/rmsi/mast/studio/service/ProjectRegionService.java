package com.rmsi.mast.studio.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rmsi.mast.studio.domain.ProjectRegion;
import com.rmsi.mast.studio.domain.VillageSearchResult;

public interface ProjectRegionService {

    ProjectRegion findProjectRegionById(Integer id);
    Integer getVillageCount(String villageName);
    ProjectRegion getVillageById(Integer id);
    List<ProjectRegion> findAllProjectRegion();
    List<VillageSearchResult> searchVillage(String villageName, Integer startpos);
    List<ProjectRegion> getVillagesByProject(int id);
    VillageSearchResult getVillage(Integer id);
    ProjectRegion getFullVillage(Integer id);
    List<ProjectRegion> getAllCommunes();
    Boolean deleteVillage(Integer id);
    ProjectRegion createVillage(ProjectRegion village);
    List<ProjectRegion> getAllProvinces();
    List<ProjectRegion> getCommunesByProvince(int provinceId);
}
