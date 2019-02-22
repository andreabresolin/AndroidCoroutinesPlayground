package andreabresolin.androidcoroutinesplayground.app.exception

class CustomTaskException : Exception {
    constructor() : super()
    constructor(message: String) : super(message)
}