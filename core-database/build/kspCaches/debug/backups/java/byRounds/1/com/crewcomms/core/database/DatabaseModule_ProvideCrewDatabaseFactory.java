package com.crewcomms.core.database;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideCrewDatabaseFactory implements Factory<CrewDatabase> {
  private final Provider<Context> contextProvider;

  public DatabaseModule_ProvideCrewDatabaseFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public CrewDatabase get() {
    return provideCrewDatabase(contextProvider.get());
  }

  public static DatabaseModule_ProvideCrewDatabaseFactory create(
      Provider<Context> contextProvider) {
    return new DatabaseModule_ProvideCrewDatabaseFactory(contextProvider);
  }

  public static CrewDatabase provideCrewDatabase(Context context) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideCrewDatabase(context));
  }
}
