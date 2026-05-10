package com.crewcomms.phone.ui;

import com.crewcomms.phone.data.CrewSessionRepository;
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
public final class PhoneViewModel_Factory implements Factory<PhoneViewModel> {
  private final Provider<CrewSessionRepository> sessionRepositoryProvider;

  public PhoneViewModel_Factory(Provider<CrewSessionRepository> sessionRepositoryProvider) {
    this.sessionRepositoryProvider = sessionRepositoryProvider;
  }

  @Override
  public PhoneViewModel get() {
    return newInstance(sessionRepositoryProvider.get());
  }

  public static PhoneViewModel_Factory create(
      Provider<CrewSessionRepository> sessionRepositoryProvider) {
    return new PhoneViewModel_Factory(sessionRepositoryProvider);
  }

  public static PhoneViewModel newInstance(CrewSessionRepository sessionRepository) {
    return new PhoneViewModel(sessionRepository);
  }
}
