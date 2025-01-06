package com.example.kidseducation

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.kidseducation.client.RetrofitClient
import com.example.kidseducation.response.collection.CollectionCategoryResponse
import com.example.kidseducation.response.quizcategory.QuizCategoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CollectionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CollectionFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

//    private val listCollection = ArrayList<CollectionCategoryResponse>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val RVKoleksi: RecyclerView = view.findViewById(R.id.recyclerViewKoleksi)
        RVKoleksi.layoutManager = GridLayoutManager(activity, 2)

        RVKoleksi.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val position = parent.getChildAdapterPosition(view)
                val spanCount = 2
                val column = position % spanCount

                if (column == 0) {
                    outRect.bottom = 56
                } else {
                    outRect.top = 56
                }
            }
        })

        RetrofitClient.instance.getCollectionCategory().enqueue(
            object : Callback<ArrayList<CollectionCategoryResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<CollectionCategoryResponse>>,
                    response: Response<ArrayList<CollectionCategoryResponse>>
                ) {

                    val listCollection = response.body() ?: arrayListOf()

                    var adapter = AdapterCollectionCategory(listCollection)
                    RVKoleksi.adapter = adapter
                }

                override fun onFailure(p0: Call<ArrayList<CollectionCategoryResponse>>, p1: Throwable) {
                    TODO("Not yet implemented")
                }
            }
        )
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collection, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CollectionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CollectionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}