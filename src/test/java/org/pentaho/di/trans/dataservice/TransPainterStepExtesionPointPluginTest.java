/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2015 by Pentaho : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.pentaho.di.trans.dataservice;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.pentaho.di.core.gui.GCInterface;
import org.pentaho.di.core.gui.Point;
import org.pentaho.di.core.logging.LogChannelInterface;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.TransPainterExtension;
import org.pentaho.di.trans.dataservice.serialization.DataServiceMetaStoreUtil;
import org.pentaho.di.trans.step.StepMeta;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by bmorrise on 10/26/15.
 */
@RunWith( MockitoJUnitRunner.class )
public class TransPainterStepExtesionPointPluginTest {

  private static String STEP_NAME = "Step Name";

  @Mock
  private DataServiceMetaStoreUtil metaStoreUtil;

  @Mock
  private DataServiceContext context;

  @Mock
  private TransPainterExtension extension;

  @Mock
  private TransMeta transMeta;

  @Mock
  private StepMeta stepMeta;

  @Mock
  private DataServiceMeta dataServiceMeta;

  @Mock
  private LogChannelInterface log;

  @Mock
  private GCInterface gc;

  private TransPainterStepExtensionPointPlugin plugin;

  @Before
  public void setUp() throws Exception {
    when( context.getMetaStoreUtil() ).thenReturn( metaStoreUtil );
    plugin = new TransPainterStepExtensionPointPlugin( context );
  }

  @Test
  public void testCallExtensionPoint() throws Exception {
    extension.transMeta = transMeta;
    extension.stepMeta = stepMeta;
    extension.gc = gc;
    extension.x1 = 0;
    extension.y1 = 0;
    extension.iconsize = 32;
    extension.offset = new Point( 0, 0 );
    extension.areaOwners = new ArrayList<>();
    when( stepMeta.getName() ).thenReturn( STEP_NAME );
    when( metaStoreUtil.getDataServiceByStepName( transMeta, STEP_NAME ) ).thenReturn( dataServiceMeta );

    plugin.callExtensionPoint( log, extension );

    verify( metaStoreUtil ).getDataServiceByStepName( transMeta, STEP_NAME );
    verify( gc ).drawImage( anyString(), any( ClassLoader.class ), anyInt(), anyInt() );
  }


}
