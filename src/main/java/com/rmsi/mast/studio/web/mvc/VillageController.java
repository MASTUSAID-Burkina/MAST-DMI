package com.rmsi.mast.studio.web.mvc;

import com.rmsi.mast.studio.domain.ProjectRegion;
import com.rmsi.mast.studio.domain.VillageSearchResult;
import com.rmsi.mast.studio.service.LaSpatialunitgroupService;
import com.rmsi.mast.studio.service.ProjectRegionService;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.log4j.Logger;

@Controller
public class VillageController {

    private static final Logger logger = Logger.getLogger(VillageController.class);

    @Autowired
    ProjectRegionService projRegService;

    @Autowired
    LaSpatialunitgroupService spatialGroupService;

    @RequestMapping(value = "/studio/village/search/count/", method = RequestMethod.POST)
    @ResponseBody
    public Integer searchListSize(HttpServletRequest request, HttpServletResponse response) {
        String villageName = "";
        try {
            villageName = ServletRequestUtils.getStringParameter(request, "village_txtSearch", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            return projRegService.getVillageCount(villageName);
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @RequestMapping(value = "/studio/village/search/{startpos}", method = RequestMethod.POST)
    @ResponseBody
    public List<VillageSearchResult> searchVillage(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer startpos) {
        String villageName = "";
        List<VillageSearchResult> villagelst = new ArrayList<>();
        try {
            villageName = ServletRequestUtils.getStringParameter(request, "village_txtSearch", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            villagelst = projRegService.searchVillage(villageName, startpos);

        } catch (Exception e) {

            logger.error(e);
            return null;
        }
        return villagelst;

    }

    @RequestMapping(value = "/studio/village/getbyproject/{projectid}", method = RequestMethod.GET)
    @ResponseBody
    public List<ProjectRegion> getVillagesByProject(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer projectid) {
        try {
            return projRegService.getVillagesByProject(projectid);
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    @RequestMapping(value = "/studio/Commune/all/", method = RequestMethod.GET)
    @ResponseBody
    public List<ProjectRegion> getAllCommunes() {
        return projRegService.getAllCommunes();
    }

    @RequestMapping(value = "/studio/communesbyprovince/{provinceid}", method = RequestMethod.GET)
    @ResponseBody
    public List<ProjectRegion> getAllCommunesByProvince(@PathVariable Integer provinceid) {
        return projRegService.getCommunesByProvince(provinceid);
    }

    @RequestMapping(value = "/studio/village/{villageId}", method = RequestMethod.GET)
    @ResponseBody
    public VillageSearchResult getVillageById(@PathVariable Integer villageId) {
        return projRegService.getVillage(villageId);
    }

    @RequestMapping(value = "/studio/village/delete/{id}", method = RequestMethod.GET)
    @ResponseBody
    public boolean deleteVillage(@PathVariable Integer id) {
        return projRegService.deleteVillage(id);
    }

    @RequestMapping(value = "/studio/village/create", method = RequestMethod.POST)
    @ResponseBody
    public String createVillage(HttpServletRequest request, HttpServletResponse response) {

        String villageNameEn;
        String villageNameFr;
        String cfvAgent;
        String VillageCode;
        int communeId;
        String villageId;

        try {
            villageNameEn = ServletRequestUtils.getRequiredStringParameter(request, "nameEn");
            villageNameFr = ServletRequestUtils.getRequiredStringParameter(request, "nameFR");
            cfvAgent = ServletRequestUtils.getRequiredStringParameter(request, "cfV_agent");
            VillageCode = ServletRequestUtils.getRequiredStringParameter(request, "VillageCode");
            communeId = ServletRequestUtils.getRequiredIntParameter(request, "commmune_id");
            villageId = ServletRequestUtils.getRequiredStringParameter(request, "hid_villageid");

            ProjectRegion village = new ProjectRegion();

            if (!"".equals(villageId)) {
                village = projRegService.getFullVillage(Integer.parseInt(villageId));
            }

            village.setIsactive(true);
            village.setNameEn(villageNameEn);
            village.setName(villageNameFr);
            village.setAreaCode(VillageCode);
            village.setCfvAgent(cfvAgent);
            village.setUperhierarchyid(communeId);
            village.setLaSpatialunitgroup(spatialGroupService.findLaSpatialunitgroupById(5));

            projRegService.createVillage(village);
            return "true";

        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }

    }

    @RequestMapping(value = "/studio/AllProvince", method = RequestMethod.GET)
    @ResponseBody
    public List<ProjectRegion> getAllProvince() {
        return projRegService.getAllProvinces();
    }
}
