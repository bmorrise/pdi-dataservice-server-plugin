package org.pentaho.di.trans.dataservice.clients;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.trans.dataservice.data.ResultData;
import org.pentaho.di.trans.step.RowAdapter;

/**
 * Created by bmorrise on 6/20/16.
 */
public interface IDataServiceService {
  ResultData query( String transPath, String stepName, String sql, int maxRows ) throws KettleException;

  void ingest( String transPath, String stepName, String sql, int maxRows, RowAdapter rowAdapter )
      throws KettleException;
}
