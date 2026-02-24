package app.xl.androidapp.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import app.xl.androidapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailInfoFragment : Fragment() {

    private val args: DetailInfoFragmentArgs by navArgs()

    private val repositoryFullName: String
        get() = args.repositoryFullName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_info, container, false)
    }
}