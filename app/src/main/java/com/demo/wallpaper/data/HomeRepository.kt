/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.demo.wallpaper.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.demo.wallpaper.data.local.database.Home
import com.demo.wallpaper.data.local.database.HomeDao
import javax.inject.Inject

interface HomeRepository {
    val homes: Flow<List<String>>

    suspend fun add(name: String)
}

class DefaultHomeRepository @Inject constructor(
    private val homeDao: HomeDao
) : HomeRepository {

    override val homes: Flow<List<String>> =
        homeDao.getHomes().map { items -> items.map { it.name } }

    override suspend fun add(name: String) {
        homeDao.insertHome(Home(name = name))
    }
}
