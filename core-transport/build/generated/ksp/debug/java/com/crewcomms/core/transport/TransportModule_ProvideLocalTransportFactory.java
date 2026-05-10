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
public final class TransportModule_ProvideLocalTransportFactory implements Factory<CrewTransport> {
  private final Provider<LocalLanTransport> transportProvider;

  public TransportModule_ProvideLocalTransportFactory(
      Provider<LocalLanTransport> transportProvider) {
    this.transportProvider = transportProvider;
  }

  @Override
  public CrewTransport get() {
    return provideLocalTransport(transportProvider.get());
  }

  public static TransportModule_ProvideLocalTransportFactory create(
      Provider<LocalLanTransport> transportProvider) {
    return new TransportModule_ProvideLocalTransportFactory(transportProvider);
  }

  public static CrewTransport provideLocalTransport(LocalLanTransport transport) {
    return Preconditions.checkNotNullFromProvides(TransportModule.INSTANCE.provideLocalTransport(transport));
  }
}
