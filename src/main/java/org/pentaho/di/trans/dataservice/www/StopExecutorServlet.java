package org.pentaho.di.trans.dataservice.www;

import com.google.common.base.Strings;
import org.pentaho.di.core.annotations.CarteServlet;
import org.pentaho.di.trans.dataservice.DataServiceContext;
import org.pentaho.di.trans.dataservice.DataServiceExecutor;
import org.pentaho.di.www.BaseHttpServlet;
import org.pentaho.di.www.CartePluginInterface;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@CarteServlet(
    id = "stopExecutor",
    name = "Stop data service executor",
    description = "Stop the associated transformations and clean up the data service"
)
public class StopExecutorServlet extends BaseHttpServlet implements CartePluginInterface {

  private static final long serialVersionUID = 5627050480135785258L;

  public static final String CONTEXT_PATH = "/stopExecutor";

  private final DataServiceContext context;

  public StopExecutorServlet( DataServiceContext context ) {
    this.context = context;
  }

  @Override public void doPut( HttpServletRequest request, HttpServletResponse response )
      throws ServletException, IOException {
    doGet( request, response );
  }

  @Override public void doGet( HttpServletRequest request, HttpServletResponse response )
      throws ServletException, IOException {
    if ( isJettyMode() && !request.getContextPath().startsWith( CONTEXT_PATH ) ) {
      return;
    }

    String executorId = request.getParameter( "executorId" );
    if ( Strings.isNullOrEmpty( executorId ) ) {
      response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
    } else {
      DataServiceExecutor executor = context.getDataServiceExecutor( executorId );
      if ( executor != null ) {
        executor.stop();
        context.remoteDataServiceExecutor( executor );
      }
      response.setStatus( HttpServletResponse.SC_OK );
    }
  }

  public String toString() {
    return "Transformation data service";
  }

  public String getService() {
    return CONTEXT_PATH + " (" + toString() + ")";
  }

  public String getContextPath() {
    return CONTEXT_PATH;
  }
}
