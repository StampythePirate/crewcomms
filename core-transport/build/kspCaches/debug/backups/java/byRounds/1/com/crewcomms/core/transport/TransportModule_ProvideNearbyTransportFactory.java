package com.crewcomms.core.transport;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("javax.inject.Named")
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
public final class TransportModule_ProvideNearbyTransportFactory implements Factory<CrewTransport> {
  private final Provider<NearbyConnectionsTransport> transportProvider;

  public TransportModule_ProvideNearbyTransportFactory(
      Provider<NearbyConnectionsTransport> transportProvider) {
    this.transportProvider = transportProvider;
  }

  @Override
  public CrewTransport get() {
    return provideNearbyTransport(transportProvider.get());
  }

  public static TransportModule_ProvideNearbyTransportFactory create(
      Provider<NearbyConnectionsTransport> transportProvider) {
    return new TransportModule_ProvideNearbyTransportFactory(transportProvider);
  }

  public static CrewTransport provideNearbyTransport(NearbyConnectionsTransport transport) {
    return Preconditions.checkNotNullFromProvides(TransportModule.INSTANCE.provideNearbyTransport(transport));
  }
}
