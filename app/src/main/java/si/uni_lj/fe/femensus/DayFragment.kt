package si.uni_lj.fe.femensus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DayFragment(var day: WeekDay) : Fragment() {

    private lateinit var menuAdapter: MenuAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_day, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)

        val menuList: List<MenuItem> = parsedMenu!!.getDayMenuItems(day)

        menuAdapter = MenuAdapter(requireContext(), menuList)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = menuAdapter

        return view
    }
}
