package com.crewcomms.core.database;

import com.crewcomms.core.database.dao.CrewDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideCrewDaoFactory implements Factory<CrewDao> {
  private final Provider<CrewDatabase> databaseProvider;

  public DatabaseModule_ProvideCrewDaoFactory(Provider<CrewDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public CrewDao get() {
    return provideCrewDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideCrewDaoFactory create(
      Provider<CrewDatabase> databaseProvider) {
    return new DatabaseModule_ProvideCrewDaoFactory(databaseProvider);
  }

  public static CrewDao provideCrewDao(CrewDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideCrewDao(database));
  }
}
