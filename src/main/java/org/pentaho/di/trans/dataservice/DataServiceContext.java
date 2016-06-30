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

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Supplier;
import org.pentaho.caching.api.PentahoCacheManager;
import org.pentaho.di.core.logging.LogChannelInterface;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.dataservice.clients.DataServiceClient;
import org.pentaho.di.trans.dataservice.clients.DataServiceService;
import org.pentaho.di.trans.dataservice.optimization.AutoOptimizationService;
import org.pentaho.di.trans.dataservice.optimization.PushDownFactory;
import org.pentaho.di.trans.dataservice.serialization.DataServiceFactory;
import org.pentaho.di.trans.dataservice.serialization.DataServiceMetaStoreUtil;
import org.pentaho.di.trans.dataservice.ui.DataServiceDelegate;
import org.pentaho.di.trans.dataservice.ui.UIFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DataServiceContext {
  private final DataServiceMetaStoreUtil metaStoreUtil;
  private final List<AutoOptimizationService> autoOptimizationServices;
  private final PentahoCacheManager cacheManager;
  private final List<PushDownFactory> pushDownFactories;
  private final LogChannelInterface logChannel;
  private final UIFactory uiFactory;
  private final ConcurrentMap<String, DataServiceExecutor> executors = new ConcurrentHashMap<>();

  public DataServiceContext( List<PushDownFactory> pushDownFactories,
                             List<AutoOptimizationService> autoOptimizationServices,
                             PentahoCacheManager cacheManager, UIFactory uiFactory, LogChannelInterface logChannel ) {
    this.pushDownFactories = pushDownFactories;
    this.autoOptimizationServices = autoOptimizationServices;
    this.cacheManager = cacheManager;
    this.metaStoreUtil = DataServiceMetaStoreUtil.create( this );
    this.logChannel = logChannel;
    this.uiFactory = uiFactory;
  }

  @VisibleForTesting
  protected DataServiceContext( List<PushDownFactory> pushDownFactories,
                                List<AutoOptimizationService> autoOptimizationServices,
                                PentahoCacheManager cacheManager, DataServiceMetaStoreUtil metaStoreUtil,
                                UIFactory uiFactory, LogChannelInterface logChannel ) {
    this.pushDownFactories = pushDownFactories;
    this.autoOptimizationServices = autoOptimizationServices;
    this.cacheManager = cacheManager;
    this.metaStoreUtil = metaStoreUtil;
    this.logChannel = logChannel;
    this.uiFactory = uiFactory;
  }

  public PentahoCacheManager getCacheManager() {
    return cacheManager;
  }

  public DataServiceMetaStoreUtil getMetaStoreUtil() {
    return metaStoreUtil;
  }

  public List<AutoOptimizationService> getAutoOptimizationServices() {
    return autoOptimizationServices;
  }

  public List<PushDownFactory> getPushDownFactories() {
    return pushDownFactories;
  }

  public LogChannelInterface getLogChannel() {
    return logChannel;
  }

  public UIFactory getUIFactory() {
    return this.uiFactory;
  }

  public DataServiceDelegate getDataServiceDelegate() {
    return DataServiceDelegate.withDefaultSpoonInstance( this );
  }

  public DataServiceClient createClient( final Supplier<Repository> supplier ) {
    return new DataServiceClient( new DataServiceFactory( this ) {
      @Override public Repository getRepository() {
        return supplier.get();
      }
    } );
  }

  public DataServiceClient createLocalClient() {
    return new DataServiceClient( getDataServiceDelegate() );
  }

  public DataServiceClient createServiceClient( final Supplier<Repository> supplier ) {
    return new DataServiceClient( new DataServiceFactory( this ) {
      @Override public Repository getRepository() {
        return supplier.get();
      }
    } );
  }

  public DataServiceService createServiceClient() {
    return new DataServiceService( getDataServiceDelegate() );
  }

  public void addExecutor( DataServiceExecutor executor ) {
    executors.putIfAbsent( executor.getId(), executor );
  }

  public DataServiceExecutor getExecutor( String id ) {
    return executors.get( id );
  }

  public void removeExecutor( String id ) {
    executors.remove( id );
  }
}
