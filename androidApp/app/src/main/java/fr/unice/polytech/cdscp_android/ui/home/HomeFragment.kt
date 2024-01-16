package fr.unice.polytech.cdscp_android.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fr.unice.polytech.cdscp_android.api.ApiClient
import fr.unice.polytech.cdscp_android.api.ApiService
import fr.unice.polytech.cdscp_android.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!



    private fun getDataFromApi(textView:TextView) {
        val retrofit = ApiClient.apiClient
        val apiService = retrofit.create(ApiService::class.java)

        val call = apiService.getHelloWorld()
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    textView.text = result
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                textView.text = "Error: ${t.message}"

            }
        })
    }

    private fun getCo2(textView:TextView) {
        val retrofit = ApiClient.apiClient
        val apiService = retrofit.create(ApiService::class.java)

        val call = apiService.getCo2()
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    println(result)
                    textView.text = result
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                textView.text = "Error: ${t.message}"

            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHelloWorld
        val button: TextView = binding.button
        button.setOnClickListener {
            getDataFromApi(textView)
        }

        val co2TextView: TextView = binding.co2TextView
        val co2Button: TextView = binding.co2Button
        co2Button.setOnClickListener {
            getCo2(co2TextView)
        }

        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}