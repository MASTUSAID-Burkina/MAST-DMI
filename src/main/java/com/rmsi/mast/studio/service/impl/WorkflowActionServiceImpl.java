package com.rmsi.mast.studio.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rmsi.mast.studio.dao.LASpatialUnitDAO;
import com.rmsi.mast.studio.dao.ProjectDAO;
import com.rmsi.mast.studio.dao.WorkflowActionDAO;
import com.rmsi.mast.studio.dao.WorkflowDAO;
import com.rmsi.mast.studio.dao.WorkflowStatusHistoryDAO;
import com.rmsi.mast.studio.domain.ParcelCount;
import com.rmsi.mast.studio.domain.Project;
import com.rmsi.mast.studio.domain.WorkflowActionmapping;
import com.rmsi.mast.studio.domain.WorkflowStatusHistory;
import com.rmsi.mast.studio.domain.fetch.ClaimBasic;
import com.rmsi.mast.studio.mobile.dao.SpatialUnitDao;
import com.rmsi.mast.studio.mobile.service.SpatialUnitService;
import com.rmsi.mast.studio.service.ClaimBasicService;
import com.rmsi.mast.studio.service.WorkflowActionService;
import com.rmsi.mast.studio.util.ConstantUtil;
import com.rmsi.mast.studio.util.StringUtils;
import com.rmsi.mast.viewer.dao.ParcelCountDao;
import com.rmsi.mast.viewer.dao.StatusDAO;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkflowActionServiceImpl implements WorkflowActionService {

    @Autowired
    WorkflowActionDAO WorkflowActionDAO;

    @Autowired
    WorkflowStatusHistoryDAO workflowStatusHistoryDAO;

    @Autowired
    StatusDAO statusDAO;

    @Autowired
    WorkflowDAO workflowDAO;

    @Autowired
    LASpatialUnitDAO suStatusUpdateDao;
    
    @Autowired
    SpatialUnitDao spatialUnitDao;
    
    @Autowired
    private SpatialUnitService spatialUnitService;

    @Autowired
    private ParcelCountDao parcelCountDao;
    
    @Autowired
    private ClaimBasicService claimBasicService;
    
    @Autowired
    ProjectDAO projectDAO;
    
    private static final Logger logger = Logger.getLogger(WorkflowActionServiceImpl.class);
    
    @Override
    public List<WorkflowActionmapping> getWorkflowActionmapping(Integer workflowid, Integer roleid) {
        return WorkflowActionDAO.getWorkflowActionmapping(workflowid, roleid);
    }

    @Override
    public Integer actionApprove(Long id, long userid, Integer workflowId, String comments) {

        boolean historyUpdate = true;
        int newWorkflowId = 0;
        
        try {
            WorkflowStatusHistory sunitHistory = new WorkflowStatusHistory();
            sunitHistory.setComments(comments);
            sunitHistory.setCreatedby((int) userid);
            sunitHistory.setIsactive(true);
            sunitHistory.setLandid(id);
            sunitHistory.setCreateddate(new Date());
            sunitHistory.setUserid((int) userid);
            sunitHistory.setStatuschangedate(new Date());
            sunitHistory.setStatus(statusDAO.getStatusById(4));
            sunitHistory.setWorkflow(workflowDAO.getWorkflowByid(workflowId));

            workflowStatusHistoryDAO.addWorkflowStatusHistory(sunitHistory);
        } catch (Exception e) {
            historyUpdate = false;
        }

        if (historyUpdate) {
            try {
                ClaimBasic su = spatialUnitService.getClaimsBasicByLandId(id).get(0);
                newWorkflowId = workflowId;
                
                if (workflowId != 9 && workflowId != 14) {
                    newWorkflowId += 1;
                    su.setWorkflowstatusid(newWorkflowId);
                }
                if (workflowId == 8 || workflowId == 13) {
                    su.setApplicationstatusid(7); // Final
                } else {
                    su.setApplicationstatusid(8); // Pending
                }
                
                String countval = "";
                String villageCode = su.getLaSpatialunitgroupHierarchy5().getAreaCode() + "-";

                // check for print notice
                if (workflowId == 3) {
                    su.setPublicNoticeStartDate(new Date());
                }

                //case for application no
                Project project = projectDAO.findById(su.getProjectnameid(), false);
                
                if ((workflowId == 1 || workflowId == 10) && StringUtils.isEmpty(su.getAppNum())) {
                    ParcelCount parcelCount = parcelCountDao.findParcelCountByTypeAndProjectName(ConstantUtil.APPLICATION, project.getName());
                    if(parcelCount == null){
                        parcelCount = new ParcelCount();
                        parcelCount.setCount(0);
                        parcelCount.setPname(project.getName());
                        parcelCount.setType(ConstantUtil.APPLICATION);
                    }
                    
                    long count = parcelCount.getCount() + 1;
                    parcelCount.setCount(count);

                    if (count < 10) {
                        countval = "0000" + String.valueOf(count);
                    } else if (count >= 10 && count <= 99) {
                        countval = "000" + String.valueOf(count);
                    } else if (count >= 100 && count <= 999) {
                        countval = "00" + String.valueOf(count);
                    } else if (count >= 1000 && count <= 9999) {
                        countval = "0" + String.valueOf(count);
                    } else if (count >= 10000 && count <= 99999) {
                        countval = String.valueOf(count);
                    }
                    su.setAppNum(villageCode + countval);
                    su.setApplicationdate(new Date());
                    
                    parcelCountDao.makePersistent(parcelCount);
                } // PV no case
                else if (workflowId == 5) {
                    ParcelCount parcelCount = parcelCountDao.findParcelCountByTypeAndProjectName(ConstantUtil.PV, project.getName());
                    if(parcelCount == null){
                        parcelCount = new ParcelCount();
                        parcelCount.setCount(0);
                        parcelCount.setPname(project.getName());
                        parcelCount.setType(ConstantUtil.PV);
                    }
                    
                    long count = parcelCount.getCount() + 1;
                    parcelCount.setCount(count);
                    
                    if (count < 10) {
                        countval = "0000" + String.valueOf(count);
                    } else if (count >= 10 && count <= 99) {
                        countval = "000" + String.valueOf(count);
                    } else if (count >= 100 && count <= 999) {
                        countval = "00" + String.valueOf(count);
                    } else if (count >= 1000 && count <= 9999) {
                        countval = "0" + String.valueOf(count);
                    } else if (count >= 10000 && count <= 99999) {
                        countval = String.valueOf(count);
                    }
                    
                    su.setPvNum(villageCode + countval);
                    parcelCountDao.makePersistent(parcelCount);
                } // APFR no case
                else if (workflowId == 6 || workflowId == 12) {
                    ParcelCount parcelCount = parcelCountDao.findParcelCountByTypeAndProjectName(ConstantUtil.APFR, project.getName());
                    if(parcelCount == null){
                        parcelCount = new ParcelCount();
                        parcelCount.setCount(0);
                        parcelCount.setPname(project.getName());
                        parcelCount.setType(ConstantUtil.APFR);
                    }
                    
                    long count = parcelCount.getCount() + 1;
                    parcelCount.setCount(count);
                    
                    if (count < 10) {
                        countval = "0000" + String.valueOf(count);
                    } else if (count >= 10 && count <= 99) {
                        countval = "000" + String.valueOf(count);
                    } else if (count >= 100 && count <= 999) {
                        countval = "00" + String.valueOf(count);
                    } else if (count >= 1000 && count <= 9999) {
                        countval = "0" + String.valueOf(count);
                    } else if (count >= 10000 && count <= 99999) {
                        countval = String.valueOf(count);
                    }
                    su.setApfrNum(villageCode + countval);
                    parcelCountDao.makePersistent(parcelCount);
                }
                
                claimBasicService.saveClaimBasicDAO(su);
            } catch (Exception e) {
                logger.error(e);
                return 0;
            }
            
            return newWorkflowId;
        }
        return 0;
    }

    @Override
    public Integer actionReject(Long id, long userid, Integer workflowId, String comments) {
        boolean historyUpdate = true;
        try {
            WorkflowStatusHistory sunitHistory = new WorkflowStatusHistory();
            sunitHistory.setComments(comments);
            sunitHistory.setCreatedby((int) userid);
            sunitHistory.setIsactive(true);
            sunitHistory.setLandid(id);
            sunitHistory.setCreateddate(new Date());
            sunitHistory.setUserid((int) userid);
            sunitHistory.setStatuschangedate(new Date());
            sunitHistory.setStatus(statusDAO.getStatusById(5));
            sunitHistory.setWorkflow(workflowDAO.getWorkflowByid(workflowId));
            
            workflowStatusHistoryDAO.addWorkflowStatusHistory(sunitHistory);

        } catch (Exception e) {
            historyUpdate = false;
        }

        if (historyUpdate) {
            if (workflowId != 1 && workflowId != 10) {
                workflowId -=1;
            }
            boolean flag = suStatusUpdateDao.updateSpatialUnit(id, 5, workflowId);
            if (flag) {
                return 1;
            }
        }
        return 0;
    }

    @Override
    public Integer actionRegister(Long id, long userid, Integer workflowId, String comments) {

        boolean historyUpdate = true;
        int appstatus = 0;
        int workstatus = 0;
        try {

            WorkflowStatusHistory sunitHistory = new WorkflowStatusHistory();
            sunitHistory.setComments(comments);
            sunitHistory.setCreatedby((int) userid);
            sunitHistory.setIsactive(true);
            sunitHistory.setLandid(id);
            sunitHistory.setCreateddate(new Date());
            sunitHistory.setUserid((int) userid);
            sunitHistory.setStatuschangedate(new Date());

            if (workflowId == 4) {
                sunitHistory.setStatus(statusDAO.getStatusById(2));
                sunitHistory.setWorkflow(workflowDAO.getWorkflowByid(4));
                appstatus = 5;
                workstatus = 6;

            }
            workflowStatusHistoryDAO.addWorkflowStatusHistory(sunitHistory);

        } catch (Exception e) {
            historyUpdate = false;
        }

        if (historyUpdate) {

            boolean flag = suStatusUpdateDao.updateSpatialUnit(id, appstatus, workstatus);
            if (flag) {
                return 1;
            }

        }

        return null;

    }

    @Override
    public Integer actionVerification(Long id, long userid, Integer workflowId, String comments) {

        boolean historyUpdate = true;
        int appstatus = 0;
        int workstatus = 0;
        try {

            WorkflowStatusHistory sunitHistory = new WorkflowStatusHistory();
            sunitHistory.setComments(comments);
            sunitHistory.setCreatedby((int) userid);
            sunitHistory.setIsactive(true);
            sunitHistory.setLandid(id);
            sunitHistory.setCreateddate(new Date());
            sunitHistory.setUserid((int) userid);
            sunitHistory.setStatuschangedate(new Date());

            if (workflowId == 3) {
                sunitHistory.setStatus(statusDAO.getStatusById(4));
                sunitHistory.setWorkflow(workflowDAO.getWorkflowByid(3));
                appstatus = 4;
                workstatus = 4;
            }
            workflowStatusHistoryDAO.addWorkflowStatusHistory(sunitHistory);

        } catch (Exception e) {
            historyUpdate = false;
        }

        if (historyUpdate) {

            boolean flag = suStatusUpdateDao.updateSpatialUnit(id, appstatus, workstatus);
            if (flag) {
                return 1;
            }

        }

        return null;

    }

    @Override
    public Integer actiondelete(Long id, long userid, Integer workflowId, String comments) {
        boolean historyUpdate = true;
        try {

            WorkflowStatusHistory sunitHistory = new WorkflowStatusHistory();
            sunitHistory.setComments(comments);
            sunitHistory.setCreatedby((int) userid);
            sunitHistory.setIsactive(true);
            sunitHistory.setLandid(id);
            sunitHistory.setCreateddate(new Date());
            sunitHistory.setUserid((int) userid);
            sunitHistory.setStatuschangedate(new Date());
            sunitHistory.setStatus(statusDAO.getStatusById(3));
            sunitHistory.setWorkflow(workflowDAO.getWorkflowByid(5));

            workflowStatusHistoryDAO.addWorkflowStatusHistory(sunitHistory);

        } catch (Exception e) {
            historyUpdate = false;
        }

        if (historyUpdate) {
            boolean flag = suStatusUpdateDao.deleteSpatialUnit(id, 3, 5);
            if (flag) {
                return 1;
            }
        }
        return null;
    }
    
    @Override
    public long findSFRname(Long id, int workflowId, int workflowStatus) {
        return workflowStatusHistoryDAO.findSFRnameByUsin(id, workflowId, workflowStatus);
    }
}
