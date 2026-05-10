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
public final class MockTransport_Factory implements Factory<MockTransport> {
  @Override
  public MockTransport get() {
    return newInstance();
  }

  public static MockTransport_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static MockTransport newInstance() {
    return new MockTransport();
  }

  private static final class InstanceHolder {
    private static final MockTransport_Factory INSTANCE = new MockTransport_Factory();
  }
}
