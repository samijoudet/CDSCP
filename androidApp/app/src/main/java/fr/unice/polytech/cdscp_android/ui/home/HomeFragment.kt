package fr.unice.polytech.cdscp_android.ui.home

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fr.unice.polytech.cdscp_android.R
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

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    private val CHANNEL_ID = "CO2HighNotification"
    private val notificationId = 1
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "CO2HighNotification"
            val descriptionText = "The CO2 level getting high. You may want to open the window"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }

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

        val callCO2 = apiService.getCo2()
        var co2Value = 0
        callCO2.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    println(result)
                    textView.text = result
                    if (result != null) {
                        co2Value = result.split(" ppm")[0].toInt()
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                textView.text = "Error: ${t.message}"

            }
        })
        val callOpenState = apiService.getOpenState()
        callOpenState.enqueue(object : Callback<String> {
            @RequiresApi(Build.VERSION_CODES.TIRAMISU)
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    println(result)
                    if (result == "CLOSED" && co2Value > 800) {
                        //send notification to open the window
                        var builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                            .setContentTitle("Open the window")
                            .setContentText("The CO2 level getting high. You may want to open the window")
                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                        with(NotificationManagerCompat.from(requireContext())) {
                            if (ActivityCompat.checkSelfPermission(
                                    requireContext(),
                                    Manifest.permission.POST_NOTIFICATIONS
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                ActivityCompat.requestPermissions(
                                    requireActivity(),
                                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                                    1
                                )
                                return
                            }
                            notify(notificationId, builder.build())
                        }
                    }
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

        createNotificationChannel()

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

        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                getCo2(co2TextView)
                handler.postDelayed(this, 300000)
            }
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        handler.post(runnable)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}