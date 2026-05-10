package com.crewcomms.core.database.repository;

import com.crewcomms.core.database.dao.CrewDao;
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
public final class HistoryRepository_Factory implements Factory<HistoryRepository> {
  private final Provider<CrewDao> crewDaoProvider;

  public HistoryRepository_Factory(Provider<CrewDao> crewDaoProvider) {
    this.crewDaoProvider = crewDaoProvider;
  }

  @Override
  public HistoryRepository get() {
    return newInstance(crewDaoProvider.get());
  }

  public static HistoryRepository_Factory create(Provider<CrewDao> crewDaoProvider) {
    return new HistoryRepository_Factory(crewDaoProvider);
  }

  public static HistoryRepository newInstance(CrewDao crewDao) {
    return new HistoryRepository(crewDao);
  }
}
