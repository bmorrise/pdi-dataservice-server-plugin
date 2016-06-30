package org.pentaho.di.trans.dataservice.data;

import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.row.RowMetaInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bmorrise on 6/20/16.
 */
public class ResultData {

  private RowMetaInterface rowMeta;
  private List<Object[]> rows = new ArrayList<>();

  public RowMetaInterface getRowMeta() {
    return rowMeta;
  }

  public void setRowMeta( RowMetaInterface rowMeta ) {
    this.rowMeta = rowMeta;
  }

  public void addRow( Object[] row ) {
    rows.add( row );
  }

  public List<Object[]> getRows() {
    return rows;
  }
}
