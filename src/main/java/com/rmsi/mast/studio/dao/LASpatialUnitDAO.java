package com.rmsi.mast.studio.dao;

import java.util.Date;

public interface LASpatialUnitDAO {

    boolean updateSpatialUnit(long landid, Integer applicationstatusid, Integer workflowstatusid);

    boolean updateSpatialUnitAppNum(long landid, String appNum, Date appDate, Integer appStatusId, Integer workflowId);
    
    boolean deleteSpatialUnit(long landid, Integer applicationstatusid, Integer workflowstatusid);

}
