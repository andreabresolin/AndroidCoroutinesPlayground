package andreabresolin.androidcoroutinesplayground

import andreabresolin.androidcoroutinesplayground.app.view.BaseActivity
import android.os.Bundle

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
