package com.rmsi.mast.studio.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rmsi.mast.studio.dao.ProjectRegionDAO;
import com.rmsi.mast.studio.domain.ProjectRegion;
import com.rmsi.mast.studio.domain.VillageSearchResult;
import com.rmsi.mast.studio.service.ProjectRegionService;

@Service
public class ProjectRegionServiceImpl implements ProjectRegionService {

    @Autowired
    ProjectRegionDAO projectRegionDAO;

    @Override
    public ProjectRegion findProjectRegionById(Integer id) {
        // TODO Auto-generated method stub
        return projectRegionDAO.findProjectRegionById(id);
    }

    public Integer getVillageCount(String villageName) {
        return projectRegionDAO.getVillageCount(villageName);
    }

    @Override
    public List<ProjectRegion> findAllProjectRegion() {
        // TODO Auto-generated method stub
        return projectRegionDAO.findAllProjectRegion();
    }

    @Override
    public List<VillageSearchResult> searchVillage(String villageName, Integer startpos){
        return projectRegionDAO.searchVillage(villageName, startpos);
    }
    
    @Override
    public List<ProjectRegion> getVillagesByProject(int id){
        return projectRegionDAO.getVillagesByProject(id);
    }
    
    @Override
    public VillageSearchResult getVillage(Integer id){
        return projectRegionDAO.getVillage(id);
    }
    
    @Override
    public ProjectRegion getFullVillage(Integer id){
        return projectRegionDAO.findById(id, false);
    }
    
    @Override
    public List<ProjectRegion> getAllCommunes(){
        return projectRegionDAO.getAllCommunes();
    }
    
    @Override
    public List<ProjectRegion> getCommunesByProvince(int provinceId){
        return projectRegionDAO.getCommunesByProvince(provinceId);
    }
    
    @Override
    public ProjectRegion getVillageById(Integer id){
        return projectRegionDAO.findById(id, false);
    }

    @Override
    public Boolean deleteVillage(Integer id) {
        return projectRegionDAO.deleteVillage(id);
    }

    @Override
    public ProjectRegion createVillage(ProjectRegion village) {
        return projectRegionDAO.createVillage(village);
    }

    @Override
    public List<ProjectRegion> getAllProvinces() {
        return projectRegionDAO.getAllProvinces();
    }
}
