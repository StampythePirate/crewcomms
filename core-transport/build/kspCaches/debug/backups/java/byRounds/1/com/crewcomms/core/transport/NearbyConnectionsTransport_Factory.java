package com.crewcomms.core.transport;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class NearbyConnectionsTransport_Factory implements Factory<NearbyConnectionsTransport> {
  private final Provider<Context> contextProvider;

  public NearbyConnectionsTransport_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public NearbyConnectionsTransport get() {
    return newInstance(contextProvider.get());
  }

  public static NearbyConnectionsTransport_Factory create(Provider<Context> contextProvider) {
    return new NearbyConnectionsTransport_Factory(contextProvider);
  }

  public static NearbyConnectionsTransport newInstance(Context context) {
    return new NearbyConnectionsTransport(context);
  }
}
