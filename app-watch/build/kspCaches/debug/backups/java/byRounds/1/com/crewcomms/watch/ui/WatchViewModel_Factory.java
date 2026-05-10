package com.crewcomms.watch.ui;

import com.crewcomms.watch.data.WatchRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class WatchViewModel_Factory implements Factory<WatchViewModel> {
  private final Provider<WatchRepository> repositoryProvider;

  public WatchViewModel_Factory(Provider<WatchRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public WatchViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static WatchViewModel_Factory create(Provider<WatchRepository> repositoryProvider) {
    return new WatchViewModel_Factory(repositoryProvider);
  }

  public static WatchViewModel newInstance(WatchRepository repository) {
    return new WatchViewModel(repository);
  }
}
