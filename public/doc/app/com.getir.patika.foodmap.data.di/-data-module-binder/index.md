//[app](../../../index.md)/[com.getir.patika.foodmap.data.di](../index.md)/[DataModuleBinder](index.md)

# DataModuleBinder

[androidJvm]\
@Module

interface [DataModuleBinder](index.md)

A Hilt module that binds data source implementations to their respective interfaces.

## Functions

| Name | Summary |
|---|---|
| [bindLocationRepository](bind-location-repository.md) | [androidJvm]<br>@Binds<br>abstract fun [bindLocationRepository](bind-location-repository.md)(impl: [LocationDataSource](../../com.getir.patika.foodmap.data.impl/-location-data-source/index.md)): [LocationRepository](../../com.getir.patika.foodmap.data/-location-repository/index.md) |
| [bindPreferencesRepository](bind-preferences-repository.md) | [androidJvm]<br>@Binds<br>abstract fun [bindPreferencesRepository](bind-preferences-repository.md)(impl: [PreferencesDataSource](../../com.getir.patika.foodmap.data.impl/-preferences-data-source/index.md)): [PreferencesRepository](../../com.getir.patika.foodmap.data/-preferences-repository/index.md) |
