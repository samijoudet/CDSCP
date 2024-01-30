package fr.unice.polytech.cdscp_android.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import fr.unice.polytech.cdscp_android.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DataDisplaySingleValueFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DataDisplaySingleValueFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var titleTextView: TextView
    private lateinit var dataTextView: TextView
    private lateinit var actionButton: Button


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_data_display_single_value, container, false)

        titleTextView = view.findViewById(R.id.titleTextView)
        dataTextView = view.findViewById(R.id.dataTextView)
        actionButton = view.findViewById(R.id.actionButton)

        // Example of setting data, you can customize this
        titleTextView.text = "CO2 Level"
        dataTextView.text = "-- ppm"
        actionButton.text = ""

        // Set up any listeners
        actionButton.setOnClickListener {
            // Handle button click
        }

        return view
    }

    fun getTextView(): TextView {
        return dataTextView
    }

    fun setData(title: String, data: String, actionListener: View.OnClickListener) {
        titleTextView.text = title
        dataTextView.text = data
        actionButton.setOnClickListener(actionListener)
    }
}