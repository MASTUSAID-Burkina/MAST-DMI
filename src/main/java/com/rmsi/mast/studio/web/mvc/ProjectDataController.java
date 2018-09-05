package com.rmsi.mast.studio.web.mvc;

import java.io.File;
import java.io.FileOutputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.rmsi.mast.studio.dao.ProjectDAO;
import com.rmsi.mast.studio.dao.ProjectNameDAO;
import com.rmsi.mast.studio.domain.Project;
import com.rmsi.mast.studio.domain.ProjectName;
import com.rmsi.mast.studio.domain.ProjectSpatialData;
import com.rmsi.mast.studio.domain.User;
import com.rmsi.mast.studio.domain.UserProject;
import com.rmsi.mast.studio.service.ProjectAttributeService;
import com.rmsi.mast.studio.service.ProjectDataService;
import com.rmsi.mast.studio.service.UserService;

@Controller
public class ProjectDataController {

    private static final Logger logger = Logger.getLogger(ProjectDataController.class);

    @Autowired
    ProjectDataService projectDataService;

    @Autowired
    UserService userService;

    @Autowired
    ProjectAttributeService projectAttributeService;

    @Autowired
    ProjectDAO projectDao;

    @Autowired
    ProjectNameDAO projectNameDao;

    @RequestMapping(value = "/mobileconfig/upload/", method = RequestMethod.POST)
    @ResponseBody
    public String uploadSpatialData(MultipartHttpServletRequest request, HttpServletResponse response, Principal principal) {
        //List<ProjectSpatialData> uploadDocuments = new ArrayList<ProjectSpatialData>();
        try {

            List<String> validExtension = new ArrayList<String>();
            validExtension.add("mbtiles");

            Iterator<String> file = request.getFileNames();
            byte[] document = null;
            String username = principal.getName();
            User user = userService.findByUniqueName(username);
            Long userid = user.getId();

            while (file.hasNext()) {
                String fileName = file.next();
                MultipartFile mpFile = request.getFile(fileName);
                long size = mpFile.getSize();
                String originalFileName = mpFile.getOriginalFilename();
                ProjectSpatialData objDocument = new ProjectSpatialData();

                Long projId = ServletRequestUtils.getRequiredLongParameter(request, "ProjectID");
                String alias = ServletRequestUtils.getRequiredStringParameter(request, "alias");
			
                ProjectName projObj = projectNameDao.findById(projId, false);

                String fileExtension = originalFileName.substring(originalFileName.indexOf(".") + 1, originalFileName.length()).toLowerCase();
                
                if (!validExtension.contains(fileExtension.toLowerCase())) {
                    return "Invalid";
                }
                
                if (!"".equals(originalFileName)) {
                    document = mpFile.getBytes();
                }
                String uploadFileName;
                String outDirPath = request.getSession().getServletContext().getRealPath(File.separator) + "resources/documents/" + projObj.getName() + "/mbtiles";

                File outDir = new File(outDirPath);
                boolean exists = outDir.exists();
                if (!exists) {
                    boolean success = (new File(outDirPath)).mkdirs();
                }

                objDocument.setFileName(originalFileName);
                objDocument.setProject(projObj);
                objDocument.setSize(size / 1024);
                uploadFileName = ("resources/documents/" + projId + "/mbtiles");
                objDocument.setFileLocation(uploadFileName);
                objDocument.setDescription(alias);
                objDocument.setCreatedby(userid);
                objDocument.setCreateddate(new Date());
                objDocument.setDocumentformatid(1);
                objDocument.setIsactive(true);
                objDocument.setModifiedby(userid);
                objDocument.setModifieddate(new Date());
                
                ProjectSpatialData savedDocument = projectDataService.saveUploadedDocuments(objDocument);

                try {
                    FileOutputStream uploadfile = new FileOutputStream(outDirPath + "/" + savedDocument.getId() + "." + fileExtension);
                    uploadfile.write(document);
                    uploadfile.flush();
                    uploadfile.close();
                } catch (Exception e) {
                    logger.error(e);
                }
            }

        } catch (Exception e) {
            logger.error(e);
        }
        return "Success";
    }

    @RequestMapping(value = "/mobileconfig/projectdata/", method = RequestMethod.GET)
    @ResponseBody
    public List<ProjectSpatialData> list() {
        return projectDataService.findAllProjectdata();

    }

    @RequestMapping(value = "/mobileconfig/projectdata/delete/{id}", method = RequestMethod.GET)
    @ResponseBody
    public void deleteProjectData(@PathVariable Long id) {

        projectDataService.deleteProjectData(id);

    }

    @RequestMapping(value = "/mobileconfig/projectname/", method = RequestMethod.GET)
    @ResponseBody
    public List<Project> listproject(Principal principal) {
        String username = principal.getName();
        User user = userService.findByUniqueName(username);
        //@Integer id = user.getId();

        //@Set<Role> role = user.getRoles();
        //@String rolename = role.iterator().next().getName();
        //role.setName("ROLE_ADMIN");
        List<Project> Projectlst = new ArrayList<Project>();
        List<UserProject> UserProjectlst = new ArrayList<UserProject>();

        try {
            //@	if(rolename.equals("ROLE_ADMIN"))
            {
                Projectlst = projectAttributeService.findallProjects();
                return Projectlst;
            }
            /* @*/
        } catch (Exception e) {

            logger.error(e);
            return Projectlst;
        }

    }

    @RequestMapping(value = "/mobileconfig/projectdata/display/{name}", method = RequestMethod.GET)
    @ResponseBody
    public List<ProjectSpatialData> displaySelectedProject(@PathVariable String name) {

        return projectDataService.displaySelectedProject(name);

    }

}
