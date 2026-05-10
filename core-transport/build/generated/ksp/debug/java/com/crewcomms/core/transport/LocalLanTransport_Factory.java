package com.crewcomms.core.transport;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
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
public final class LocalLanTransport_Factory implements Factory<LocalLanTransport> {
  @Override
  public LocalLanTransport get() {
    return newInstance();
  }

  public static LocalLanTransport_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static LocalLanTransport newInstance() {
    return new LocalLanTransport();
  }

  private static final class InstanceHolder {
    private static final LocalLanTransport_Factory INSTANCE = new LocalLanTransport_Factory();
  }
}
