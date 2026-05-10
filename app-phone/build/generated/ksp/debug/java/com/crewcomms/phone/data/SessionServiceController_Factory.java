package com.crewcomms.phone.data;

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
public final class SessionServiceController_Factory implements Factory<SessionServiceController> {
  private final Provider<Context> contextProvider;

  public SessionServiceController_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public SessionServiceController get() {
    return newInstance(contextProvider.get());
  }

  public static SessionServiceController_Factory create(Provider<Context> contextProvider) {
    return new SessionServiceController_Factory(contextProvider);
  }

  public static SessionServiceController newInstance(Context context) {
    return new SessionServiceController(context);
  }
}
