package com.crewcomms.watch.data;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class WatchRepository_Factory implements Factory<WatchRepository> {
  private final Provider<WatchCommandSender> commandSenderProvider;

  public WatchRepository_Factory(Provider<WatchCommandSender> commandSenderProvider) {
    this.commandSenderProvider = commandSenderProvider;
  }

  @Override
  public WatchRepository get() {
    return newInstance(commandSenderProvider.get());
  }

  public static WatchRepository_Factory create(Provider<WatchCommandSender> commandSenderProvider) {
    return new WatchRepository_Factory(commandSenderProvider);
  }

  public static WatchRepository newInstance(WatchCommandSender commandSender) {
    return new WatchRepository(commandSender);
  }
}
