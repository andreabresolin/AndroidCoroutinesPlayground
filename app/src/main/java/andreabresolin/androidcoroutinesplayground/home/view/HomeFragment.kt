package andreabresolin.androidcoroutinesplayground.home.view

import andreabresolin.androidcoroutinesplayground.R
import andreabresolin.androidcoroutinesplayground.app.view.BaseFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpListeners()
    }

    private fun setUpListeners() {
        mvpFragmentBtn.setOnClickListener { view -> view.findNavController().navigate(R.id.homeToMVPAction) }
        mvvmFragmentBtn.setOnClickListener { view -> view.findNavController().navigate(R.id.homeToMVVMAction) }
    }
}
