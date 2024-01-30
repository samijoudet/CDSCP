package fr.unice.polytech.cdscp_android.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import fr.unice.polytech.cdscp_android.R
import fr.unice.polytech.cdscp_android.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val buttonBreath : Button = binding.buttonBreath
        val buttonPollen : Button = binding.buttonPollen
        buttonBreath.setOnClickListener {
            (buttonBreath as MaterialButton).iconTint = ContextCompat.getColorStateList(requireContext(), R.color.green)
            (buttonPollen as MaterialButton).iconTint = ContextCompat.getColorStateList(requireContext(), R.color.white)
        }
        buttonPollen.setOnClickListener {
            (buttonPollen as MaterialButton).iconTint  = ContextCompat.getColorStateList(requireContext(), R.color.green)
            (buttonBreath as MaterialButton).iconTint  = ContextCompat.getColorStateList(requireContext(), R.color.white)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}