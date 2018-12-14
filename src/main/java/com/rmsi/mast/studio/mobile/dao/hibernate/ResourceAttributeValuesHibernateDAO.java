package com.rmsi.mast.studio.mobile.dao.hibernate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.rmsi.mast.studio.dao.hibernate.GenericHibernateDAO;
import com.rmsi.mast.studio.domain.ResourceAttributeValues;
import com.rmsi.mast.studio.domain.fetch.ResourceDetails;
import com.rmsi.mast.studio.mobile.dao.ResourceAttributeValuesDAO;
import com.rmsi.mast.studio.mobile.transferobjects.SearchResult;
import com.rmsi.mast.studio.util.StringUtils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Repository
public class ResourceAttributeValuesHibernateDAO extends GenericHibernateDAO<ResourceAttributeValues, Integer> implements ResourceAttributeValuesDAO {

    private static final Logger logger = Logger.getLogger(ResourceAttributeValuesHibernateDAO.class);

    @Override
    public ResourceAttributeValues addResourceAttributeValues(ResourceAttributeValues resourceAttributevalues) {

        try {
            return makePersistent(resourceAttributevalues);

        } catch (Exception ex) {
            System.out.println("Exception while adding data...." + ex);
            logger.error(ex);
            throw ex;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ResourceAttributeValues> getResourceAttributeValuesBylandId(Integer projectId, Integer Id) {
        try {
            List<ResourceAttributeValues> lstResourceDetails = new ArrayList<ResourceAttributeValues>();

            String query = "Select Distinct RA.LandID,RA.groupID,RA.AttributeValueID,RA.AttributeValue,AM.FieldName,AM.attributemasterid,AM.FieldAliasName "
                    + "from la_ext_resourceattributevalue RA,la_ext_attributemaster AM,la_ext_attributecategory AC "
                    + "Where RA.AttributeMasterID=AM.AttributeMasterID And AM.AttributeCategoryID=AC.AttributeCategoryID "
                    + "and RA.projectid =" + projectId + " And RA.LandID=" + Id + " order by groupid";

            List<Object[]> arrObject = getEntityManager().createNativeQuery(query).getResultList();

            for (Object[] object : arrObject) {
                ResourceAttributeValues resourceattributes = new ResourceAttributeValues();
                resourceattributes.setLandid(Integer.valueOf(object[0].toString()));
                resourceattributes.setGroupid(Integer.valueOf(object[1].toString()));
                resourceattributes.setAttributevalueid(Integer.valueOf(object[5].toString()));
                resourceattributes.setAttributevalue(object[3].toString());
                resourceattributes.setFieldname(object[4].toString());
                resourceattributes.setFieldAliasName(object[6].toString());

                lstResourceDetails.add(resourceattributes);
            }
            return lstResourceDetails;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public SearchResult searchResources(String project, int chartered, int tenureType, int classType, String owner, Integer startfrom) {
        
        SearchResult searchResult = new SearchResult();
        
        try {
            String where = "";
            if (chartered > -1) {
                where = "res.chartered = '" + Boolean.toString(chartered > 0) + "'";
            }

            if (tenureType > 0) {
                if (!where.equals("")) {
                    where += " and ";
                }
                where += "AC.attributecategoryid = " + Integer.toString(tenureType);
            }

            if (classType > 0) {
                if (!where.equals("")) {
                    where += " and ";
                }
                where += "RC.classificationid = " + Integer.toString(classType);
            }

            if (StringUtils.isNotEmpty(owner)) {
                if (!where.equals("")) {
                    where += " and ";
                }
                where += "lower(coalesce(Temp_table.OwnerName, '')) like '%" + owner.toLowerCase().replace("'", "''") + "%'";
            }

            if (StringUtils.isNotEmpty(where)) {
                where = " where " + where;
            }

            String query = "select distinct rlm.LandID,Pr.ProjectName,RC.ClassificationName,RSC.SubClassificationName,coalesce(Temp_table.CategoryName,AC.CategoryName) as CategoryName, \n"
                    + "rlm.GeomType as GeometryName, Temp_table.OwnerName, res.chartered, res.comment, res.validated_by_council, res.validation_date, res.in_exploitation, RC.classificationid, AC.attributecategoryid \n"
                    + "from la_ext_resourcelandclassificationmapping rlm\n"
                    + "inner join (select landid, chartered, comment, validated_by_council, validation_date, in_exploitation from la_spatialunit_resource_land where isactive=true \n"
                    + "  union select landid, chartered, comment, validated_by_council, validation_date, in_exploitation from la_spatialunit_resource_line where isactive=true \n"
                    + "  union select landid, chartered, comment, validated_by_council, validation_date, in_exploitation from la_spatialunit_resource_point where isactive=true) res on rlm.landid = res.landid\n"
                    + "left join (Select Distinct RA.LandID,RC.ProjectName,RC.ClassificationName,RC.SubClassificationName,AC.CategoryName,RA.GeomType as GeometryName, \n"
                    + "coalesce((Select attributevalue from la_ext_resourceattributevalue Where landid=RA.landID and attributemasterid in (1063,1017,1035,1079,1088,1097,1108) and groupid=RA.groupid), '') ||' '|| \n"
                    + "coalesce((Select attributevalue from la_ext_resourceattributevalue Where landid=RA.landID and attributemasterid in (1065,1018,1036,1080,1089,1109,1098) and groupid=RA.groupid), '') ||' '|| \n"
                    + "coalesce((Select attributevalue from la_ext_resourceattributevalue Where landid=RA.landID and attributemasterid in (1066,1019,1037,1081,1090,1099,1110) and groupid=RA.groupid), '') as OwnerName \n"
                    + "from la_ext_resourceattributevalue RA, (Select RLM.LandID,Pr.ProjectName,RC.ClassificationName,RSC.SubClassificationName,GT.GeometryName,RLM.GeomType \n"
                    + "from la_ext_resourceclassification RC, la_ext_resourcesubclassification RSC,la_ext_resourcelandclassificationmapping RLM ,la_spatialsource_projectname Pr,la_ext_GeometryType GT \n"
                    + "Where RC.ClassificationID=RSC.ClassificationID And RSC.SubClassificationID=RLM.SubClassificationID And RLM.ProjectID=Pr.ProjectNameID) RC \n"
                    + ",la_ext_attributemaster AM,la_ext_attributecategory AC ,la_ext_GeometryType GT \n"
                    + "Where RA.landID = RC.LandID And GT.GeometryName=RC.GeomType AND RA.AttributeMasterID=AM.AttributeMasterID And AM.AttributeCategoryID=AC.AttributeCategoryID \n"
                    + "and RA.attributevalue='Primary occupant /Point of contact' and RA.projectid  = " + project + ") as Temp_table on Temp_table.landid=rlm.landid \n"
                    + "left join la_spatialsource_projectname Pr on rlm.projectid=Pr.ProjectNameID \n"
                    + "left join la_ext_resourceclassification RC on RC.ClassificationID=rlm.ClassificationID \n"
                    + "left join la_ext_resourcesubclassification RSC on RSC.SubClassificationID=rlm.SubClassificationID \n"
                    + "left join la_ext_attributecategory AC on AC.AttributeCategoryID=rlm.CategoryID \n"
                    + where
                    + " order by rlm.landid";

            List<Object[]> allRecords = getEntityManager().createNativeQuery(query).getResultList();
                        
            if (allRecords != null && allRecords.size() > startfrom) {
                int endIndex = startfrom + 10;
                if (endIndex > allRecords.size()) {
                    endIndex = allRecords.size();
                }
                
                List<Object[]> pageRecords = allRecords.subList(startfrom, endIndex);
                List<ResourceDetails> lstResourceDetails = new ArrayList<>();
                
                for (Object[] object : pageRecords) {
                    ResourceDetails resourceDetails = new ResourceDetails();
                    resourceDetails.setLandid(Integer.valueOf(object[0].toString()));
                    resourceDetails.setProjectName(object[1].toString());
                    if (null != object[2]) {
                        resourceDetails.setClassificationName(object[2].toString());
                    }
                    if (null != object[3]) {
                        resourceDetails.setSubclassificationName(object[3].toString());
                    }
                    if (null != object[4]) {
                        resourceDetails.setCategoryName(object[4].toString());
                    }
                    resourceDetails.setGeometryName(object[5].toString());
                    if (null != object[6]) {
                        resourceDetails.setPersonName(object[6].toString());
                    }
                    if (null != object[7]) {
                        resourceDetails.setChartered(Boolean.valueOf(object[7].toString()));
                    }
                    if (null != object[8]) {
                        resourceDetails.setComment(object[8].toString());
                    }
                    if (null != object[9]) {
                        resourceDetails.setValidatedByCouncil(Boolean.valueOf(object[9].toString()));
                    }
                    if (null != object[10]) {
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        resourceDetails.setValidationDate(df.parse(object[10].toString()));
                    }
                    if (null != object[11]) {
                        resourceDetails.setInExploitation(Boolean.valueOf(object[11].toString()));
                    }
                    resourceDetails.setProjectId(Integer.parseInt(project));

                    lstResourceDetails.add(resourceDetails);
                }
                
                searchResult.setCount(allRecords.size());
                searchResult.setResult(lstResourceDetails);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return searchResult;
    }

    @Override
    public Integer getAllresouceCountByproject(String project) {

        try {

            /*String query = "select  count(*) from  la_ext_resourcelandclassificationmapping RLM where RLM.projectid=" +project;*/
            String query = "select  count(*) from  la_ext_resourcelandclassificationmapping RLM where RLM.projectid=" + project + " and landid in (select landid from la_spatialunit_resource_land where isactive=true "
                    + "union select landid from la_spatialunit_resource_line where isactive=true union select landid from la_spatialunit_resource_point where isactive=true) ";

            List<BigInteger> arrObject = getEntityManager().createNativeQuery(query).getResultList();

            if (arrObject.size() > 0) {
                return Integer.parseInt(arrObject.get(0).toString());
            } else {
                return 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    @Override
    public List<ResourceAttributeValues> getResourceAttributeValuesByMasterlandid(
            Integer Id) {

        Query query = getEntityManager().createQuery("select re from ResourceAttributeValues re where re.landid =:landId order by re.groupid");
        try {
            List<ResourceAttributeValues> lstResourceAttributeValues = query.setParameter("landId", Id).getResultList();
            return lstResourceAttributeValues;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        }
    }

    @Override
    public List<Object[]> getResourceAttributeValuesAndDatatypeBylandId(Integer projectId,
            Integer Id) {
        try {

            String query = "Select AM.FieldName, RA.AttributeMasterID, RA.groupid, AM.FieldAliasName "
                    + "from la_ext_resourceattributevalue RA,la_ext_attributemaster AM,la_ext_attributecategory AC "
                    + "Where RA.AttributeMasterID=AM.AttributeMasterID And AM.AttributeCategoryID=AC.AttributeCategoryID "
                    + "and RA.projectid =" + projectId + " And RA.LandID=" + Id + " order by AM.listing \\:\\: int";

            List<Object[]> arrObject = getEntityManager().createNativeQuery(query).getResultList();
            if (arrObject.size() > 0) {

                return arrObject;
            }

            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
