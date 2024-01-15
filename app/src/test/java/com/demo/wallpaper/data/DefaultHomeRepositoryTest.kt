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

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import com.demo.wallpaper.data.local.database.Home
import com.demo.wallpaper.data.local.database.HomeDao

/**
 * Unit tests for [DefaultHomeRepository].
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class DefaultHomeRepositoryTest {

    @Test
    fun homes_newItemSaved_itemIsReturned() = runTest {
        val repository = DefaultHomeRepository(FakeHomeDao())

        repository.add("Repository")

        assertEquals(repository.homes.first().size, 1)
    }

}

private class FakeHomeDao : HomeDao {

    private val data = mutableListOf<Home>()

    override fun getHomes(): Flow<List<Home>> = flow {
        emit(data)
    }

    override suspend fun insertHome(item: Home) {
        data.add(0, item)
    }
}
