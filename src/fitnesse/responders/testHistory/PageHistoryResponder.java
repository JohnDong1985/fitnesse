package fitnesse.responders.testHistory;

import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.wiki.PathParser;
import fitnesse.responders.templateUtilities.PageTitle;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import java.io.File;
import java.io.StringWriter;

public class PageHistoryResponder implements Responder {
  private File resultsDirectory;

  public Response makeResponse(FitNesseContext context, Request request) throws Exception {
    if (resultsDirectory == null)
      resultsDirectory = context.getTestHistoryDirectory();
    SimpleResponse response = new SimpleResponse();
    TestHistory history = new TestHistory();
    history.readHistoryDirectory(resultsDirectory);
    String pageName = request.getResource();
    PageHistory pageHistory= history.getPageHistory(pageName);
    VelocityContext velocityContext = new VelocityContext();
    velocityContext.put("pageTitle", makePageTitle(request.getResource()));
    velocityContext.put("pageHistory", pageHistory);
    Template template = context.getVelocityEngine().getTemplate("pageHistory.vm");
    StringWriter writer = new StringWriter();

    template.merge(velocityContext, writer);
    response.setContent(writer.toString());
    return response;

  }

  private PageTitle makePageTitle(String resource) {
    return new PageTitle("Test History", PathParser.parse(resource));
  }

  public void setResultsDirectory(File resultsDirectory) {
    this.resultsDirectory = resultsDirectory;
  }

}
