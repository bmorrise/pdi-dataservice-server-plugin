package org.pentaho.di.trans.dataservice.clients;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.eclipse.swt.widgets.Display;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.dataservice.DataServiceMeta;
import org.pentaho.di.trans.dataservice.data.ResultData;
import org.pentaho.di.trans.dataservice.serialization.DataServiceFactory;
import org.pentaho.di.trans.step.RowAdapter;
import org.pentaho.di.ui.spoon.Spoon;
import org.pentaho.metastore.api.exceptions.MetaStoreException;

/**
 * Created by bmorrise on 6/20/16.
 */
public class DataServiceService implements IDataServiceService {

  private final ExecutorQueryService executorQueryService;
  private final DataServiceFactory dataServiceFactory;

  public DataServiceService( DataServiceFactory dataServiceFactory ) {
    this.executorQueryService = new ExecutorQueryService( dataServiceFactory );
    this.dataServiceFactory = dataServiceFactory;
  }

  @Override public ResultData query( String transPath, String stepName, String sql, int maxRows )
      throws KettleException {

    TransMeta transMeta = Spoon.getInstance().getActiveTransformation();

    //TransMeta transMeta = new TransMeta( transPath, "DET Trans" );

    DataServiceMeta dataServiceMeta = new DataServiceMeta( transMeta );
    dataServiceMeta.setUserDefined( false );
    dataServiceMeta.setStepname( stepName );
    dataServiceMeta.setName( "det_service" );

    final ResultData resultData = new ResultData();
    Query query = executorQueryService.prepareQuery( sql, maxRows, ImmutableMap.<String, String>of(), dataServiceMeta );
    query.execute( new RowAdapter() {
      @Override public void rowReadEvent( RowMetaInterface rowMeta, Object[] row ) throws KettleStepException {
        if ( resultData.getRowMeta() == null ) {
          resultData.setRowMeta( rowMeta );
        }
        resultData.addRow( row );
      }
    } );

    return resultData;
  }

}
