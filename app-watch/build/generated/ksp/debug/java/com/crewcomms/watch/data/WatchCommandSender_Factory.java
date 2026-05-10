package com.crewcomms.watch.data;

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
public final class WatchCommandSender_Factory implements Factory<WatchCommandSender> {
  private final Provider<Context> contextProvider;

  public WatchCommandSender_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public WatchCommandSender get() {
    return newInstance(contextProvider.get());
  }

  public static WatchCommandSender_Factory create(Provider<Context> contextProvider) {
    return new WatchCommandSender_Factory(contextProvider);
  }

  public static WatchCommandSender newInstance(Context context) {
    return new WatchCommandSender(context);
  }
}
