package com.example.kidseducation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kidseducation.client.RetrofitClient
import com.example.kidseducation.response.quizcategory.QuizCategoryResponse
import com.example.kidseducation.response.quizcategory.QuizProgressResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val listQuiz = ArrayList<QuizCategoryResponse>()

    private fun getDeviceUptimeMillis(): Long {
        return android.os.SystemClock.elapsedRealtime()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idUser = arguments?.getString("ID_USER")
        val txtWelcome: TextView = view.findViewById(R.id.textWelcome)
        val username = arguments?.getString("USERNAME")
        txtWelcome.text = "Hallo, $username"

        val RVQuiz: RecyclerView = view.findViewById(R.id.recyclerViewQuiz)
        RVQuiz.layoutManager = GridLayoutManager(activity, 4)


        val sharedPreferences = requireActivity().getSharedPreferences("PREFS", 0)
        val lastShownTime = sharedPreferences.getLong("POPUP_LAST_SHOWN", 0)
        val currentTime = System.currentTimeMillis()

        if (lastShownTime == 0L || currentTime - lastShownTime > getDeviceUptimeMillis()) {
            showWelcomePopup()
        }

        RetrofitClient.instance.getQuizCategory().enqueue(
            object : Callback<ArrayList<QuizCategoryResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<QuizCategoryResponse>>,
                    response: Response<ArrayList<QuizCategoryResponse>>
                ) {
                    listQuiz.clear()
                response.body().let {
                    if (it != null) {
                        listQuiz.addAll(it)
                    }
                }
                    val adapter = AdapterQuizCategory(listQuiz, object :
                        AdapterQuizCategory.OnQuizCategoryClickListener {
                        override fun onCategoryClick(idKategori: String) {
                            val intent = Intent(activity, ProgressActivity::class.java)
                            intent.putExtra("id_kategori", idKategori)
                            intent.putExtra("id_user", idUser)
                            startActivity(intent)
                        }
                    })
                    RVQuiz.adapter = adapter
                }

                override fun onFailure(p0: Call<ArrayList<QuizCategoryResponse>>, p1: Throwable) {
                    TODO("Not yet implemented")
                }
            }
        )

        val textNoProgress: TextView = view.findViewById(R.id.textNoProgress)
        val RVProgress: RecyclerView = view.findViewById(R.id.recyclerViewProgress)
        RVProgress.layoutManager = GridLayoutManager(activity, 1)

        if (idUser != null) {
            RetrofitClient.instance.getQuizProgress(idUser).enqueue(
                object : Callback<ArrayList<QuizProgressResponse>> {
                    override fun onResponse(
                        call: Call<ArrayList<QuizProgressResponse>>,
                        response: Response<ArrayList<QuizProgressResponse>>
                    ) {
                        val listProgress = response.body() ?: arrayListOf()

                        if (listProgress.isEmpty()) {
                            RVProgress.visibility = View.GONE
                            textNoProgress.visibility = View.VISIBLE
                        } else {
                            RVProgress.visibility = View.VISIBLE
                            textNoProgress.visibility = View.GONE

                            val adapterProgress = AdapterQuizProgress(listProgress)
                            RVProgress.adapter = adapterProgress
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<QuizProgressResponse>>, t: Throwable) {
                        Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }

    private fun showWelcomePopup() {
        val dialog = android.app.Dialog(requireContext())
        dialog.setContentView(R.layout.welcome_popup)
        dialog.setCancelable(false)

        val window = dialog.window
        window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawableResource(android.R.color.transparent)

        val closeButton: Button = dialog.findViewById(R.id.close_button)

        closeButton.setOnClickListener {
            val sharedPreferences = requireActivity().getSharedPreferences("PREFS", 0)
            sharedPreferences.edit()
                .putLong("POPUP_LAST_SHOWN", System.currentTimeMillis())
                .apply()
            dialog.dismiss()
        }

        dialog.show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}