package andreabresolin.androidcoroutinesplayground.app.domain

import andreabresolin.androidcoroutinesplayground.app.coroutines.AppCoroutineScope

abstract class BaseCoroutineUseCase
constructor(appCoroutineScope: AppCoroutineScope) : BaseUseCase(), AppCoroutineScope by appCoroutineScope