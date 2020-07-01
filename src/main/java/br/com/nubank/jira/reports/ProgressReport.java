package br.com.nubank.jira.reports;

import com.atlassian.jira.issue.search.SearchException;
import com.atlassian.jira.issue.search.SearchProviderFactoryImpl;
import com.atlassian.jira.jql.builder.JqlQueryBuilder;
import com.atlassian.jira.plugin.report.impl.AbstractReport;
import com.atlassian.jira.issue.search.SearchProvider;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.util.ParameterUtils;
import com.atlassian.jira.web.action.ProjectActionSupport;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.atlassian.query.Query;
import org.apache.log4j.Logger;

public class ProgressReport extends AbstractReport {

    private static final Logger log = Logger.getLogger(ProgressReport.class);
    @JiraImport
    private final ProjectManager projectManager;
    @JiraImport
    private final SearchProvider searchProvider;
    private Long projectId;

    public ProgressReport(ProjectManager projectManager, SearchProvider searchProvider) {
        this.projectManager = projectManager;
        this.searchProvider = searchProvider;
    }

    public String generateReportHtml(ProjectActionSupport projectActionSupport, Map map) throws Exception {
        Map<String, Object> velocityParams = new HashMap<>();
        velocityParams.put("projectName", projectManager.getProjectObj(projectId).getName());
        velocityParams.put("counter", getOpenIssueCount(projectActionSupport.getLoggedInUser(),projectId));

        return descriptor.getHtml("view", velocityParams);
    }

    public void validate(ProjectActionSupport action, Map params) {

        projectId = ParameterUtils.getLongParam(params, "selectedProjectId");

        if (projectId == null || projectManager.getProjectObj(projectId) == null){
            action.addError("selectedProjectId", action.getText("report.issuecreation.projectid.invalid"));
            log.error("Invalid projectId");
        }
    }

    private long getOpenIssueCount(ApplicationUser user, Long projectId) throws SearchException {
        JqlQueryBuilder queryBuilder = JqlQueryBuilder.newBuilder();
        Query query = queryBuilder.where().project(projectId).buildQuery();
        return searchProvider.searchCount(query, user);
    }
}
