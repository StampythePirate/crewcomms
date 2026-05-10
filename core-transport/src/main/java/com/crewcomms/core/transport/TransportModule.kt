package com.crewcomms.core.transport

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TransportModule {
    @Provides
    @Singleton
    @Named("mock")
    fun provideMockTransport(transport: MockTransport): CrewTransport = transport

    @Provides
    @Singleton
    @Named("nearby")
    fun provideNearbyTransport(transport: NearbyConnectionsTransport): CrewTransport = transport

    @Provides
    @Singleton
    @Named("local")
    fun provideLocalTransport(transport: LocalLanTransport): CrewTransport = transport
}
