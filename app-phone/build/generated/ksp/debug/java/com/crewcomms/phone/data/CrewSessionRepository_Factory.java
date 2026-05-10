package com.crewcomms.phone.data;

import com.crewcomms.core.database.repository.HistoryRepository;
import com.crewcomms.core.transport.CrewTransport;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("dagger.hilt.android.scopes.ViewModelScoped")
@QualifierMetadata("javax.inject.Named")
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
public final class CrewSessionRepository_Factory implements Factory<CrewSessionRepository> {
  private final Provider<HistoryRepository> historyRepositoryProvider;

  private final Provider<PhonePreferencesStore> preferencesStoreProvider;

  private final Provider<WearSyncManager> wearSyncManagerProvider;

  private final Provider<SessionServiceController> sessionServiceControllerProvider;

  private final Provider<CrewTransport> mockTransportProvider;

  private final Provider<CrewTransport> nearbyTransportProvider;

  public CrewSessionRepository_Factory(Provider<HistoryRepository> historyRepositoryProvider,
      Provider<PhonePreferencesStore> preferencesStoreProvider,
      Provider<WearSyncManager> wearSyncManagerProvider,
      Provider<SessionServiceController> sessionServiceControllerProvider,
      Provider<CrewTransport> mockTransportProvider,
      Provider<CrewTransport> nearbyTransportProvider) {
    this.historyRepositoryProvider = historyRepositoryProvider;
    this.preferencesStoreProvider = preferencesStoreProvider;
    this.wearSyncManagerProvider = wearSyncManagerProvider;
    this.sessionServiceControllerProvider = sessionServiceControllerProvider;
    this.mockTransportProvider = mockTransportProvider;
    this.nearbyTransportProvider = nearbyTransportProvider;
  }

  @Override
  public CrewSessionRepository get() {
    return newInstance(historyRepositoryProvider.get(), preferencesStoreProvider.get(), wearSyncManagerProvider.get(), sessionServiceControllerProvider.get(), mockTransportProvider.get(), nearbyTransportProvider.get());
  }

  public static CrewSessionRepository_Factory create(
      Provider<HistoryRepository> historyRepositoryProvider,
      Provider<PhonePreferencesStore> preferencesStoreProvider,
      Provider<WearSyncManager> wearSyncManagerProvider,
      Provider<SessionServiceController> sessionServiceControllerProvider,
      Provider<CrewTransport> mockTransportProvider,
      Provider<CrewTransport> nearbyTransportProvider) {
    return new CrewSessionRepository_Factory(historyRepositoryProvider, preferencesStoreProvider, wearSyncManagerProvider, sessionServiceControllerProvider, mockTransportProvider, nearbyTransportProvider);
  }

  public static CrewSessionRepository newInstance(HistoryRepository historyRepository,
      PhonePreferencesStore preferencesStore, WearSyncManager wearSyncManager,
      SessionServiceController sessionServiceController, CrewTransport mockTransport,
      CrewTransport nearbyTransport) {
    return new CrewSessionRepository(historyRepository, preferencesStore, wearSyncManager, sessionServiceController, mockTransport, nearbyTransport);
  }
}
