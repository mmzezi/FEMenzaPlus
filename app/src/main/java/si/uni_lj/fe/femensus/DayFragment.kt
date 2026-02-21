package si.uni_lj.fe.femensus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DayFragment : Fragment() {

    private lateinit var menuAdapter: MenuAdapter
    private lateinit var recyclerView: RecyclerView
    private var day: WeekDay = WeekDay.Monday

    companion object {
        private const val ARG_DAY = "arg_day"

        fun newInstance(day: WeekDay): DayFragment {
            val fragment = DayFragment()
            val args = Bundle()
            args.putInt(ARG_DAY, day.value)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val dayValue = it.getInt(ARG_DAY)
            day = WeekDay.values().firstOrNull { d -> d.value == dayValue } ?: WeekDay.Monday
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_day, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)

        val menuList: List<MenuItem> = parsedMenu?.getDayMenuItems(day) ?: emptyList()

        menuAdapter = MenuAdapter(requireContext(), menuList)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = menuAdapter

        return view
    }
}
