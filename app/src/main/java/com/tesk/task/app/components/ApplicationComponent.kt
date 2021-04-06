package com.tesk.task.app.components

import com.tesk.task.app.modules.ModuleViewModelFactory
import com.tesk.task.app.ui.fragments.HubFragment
import com.tesk.task.app.ui.fragments.SearchFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ModuleViewModelFactory::class))
interface ApplicationComponent {

    fun inject(repositoryFragment: HubFragment)

    fun inject(searchFragment: SearchFragment)
}