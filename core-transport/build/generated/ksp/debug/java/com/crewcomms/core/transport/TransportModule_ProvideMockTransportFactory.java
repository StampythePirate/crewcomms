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
public final class TransportModule_ProvideMockTransportFactory implements Factory<CrewTransport> {
  private final Provider<MockTransport> transportProvider;

  public TransportModule_ProvideMockTransportFactory(Provider<MockTransport> transportProvider) {
    this.transportProvider = transportProvider;
  }

  @Override
  public CrewTransport get() {
    return provideMockTransport(transportProvider.get());
  }

  public static TransportModule_ProvideMockTransportFactory create(
      Provider<MockTransport> transportProvider) {
    return new TransportModule_ProvideMockTransportFactory(transportProvider);
  }

  public static CrewTransport provideMockTransport(MockTransport transport) {
    return Preconditions.checkNotNullFromProvides(TransportModule.INSTANCE.provideMockTransport(transport));
  }
}
