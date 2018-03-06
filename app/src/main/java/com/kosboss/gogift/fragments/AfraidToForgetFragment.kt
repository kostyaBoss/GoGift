package com.kosboss.gogift.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import com.kosboss.gogift.Constants
import com.kosboss.gogift.R
import com.kosboss.gogift.events.DataPassingEvent
import com.kosboss.gogift.events.PagerPasserEvent
import kotlinx.android.synthetic.main.fragment_afraid_to_forget.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.joda.time.DateTime
import java.util.*

class AfraidToForgetFragment : Fragment(), View.OnClickListener {

    // handling passing here viewPager via eventbus
    var bus = EventBus.getDefault()

    @Subscribe
    public fun setUpViewPager(event: PagerPasserEvent) {
        viewPager = event.viewPager
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bus.register(this)
    }

    override fun onClick(p0: View?) {
        if (p0 != null) {
            when (p0.id) {
                R.id.button_done_afraid_to_forget -> {
                    // calendar instance to put data into android native calendar

                    // default timezone and calendar
                    val calendar = Calendar.getInstance(TimeZone.getDefault())
                    calendar.set(Calendar.YEAR, DateTime.now().year)
                    calendar.set(Calendar.MONTH, view!!.month_picker.value - 1)
                    calendar.set(Calendar.DAY_OF_MONTH, view!!.day_picker.value)

                    // date, selected in application
                    val selectedDateMillis = DateTime(calendar.timeInMillis)

                    // post object using eventbus
                    bus.post(DataPassingEvent(selectedDateMillis.millis))

                    preferences = context.getSharedPreferences(constants.SHARED_PREFS, Context.MODE_PRIVATE)
                    editor = preferences.edit();
                    editor.putLong(constants.TIME, selectedDateMillis.millis)
                    editor.commit()
                    viewPager.currentItem = viewPager.currentItem + 1
                }
            }
        }
    }

    lateinit var viewPager: ViewPager
    lateinit var preferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var constants: Constants
    lateinit var viewFrag: View

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_afraid_to_forget, container, false)

        constants = Constants()

        // formatter for printing months instead of days
        val formatter = NumberPicker.Formatter {
            val months = context.resources.getStringArray(R.array.months)
            when (it) {
                1 -> return@Formatter months[0]
                2 -> return@Formatter months[1]
                3 -> return@Formatter months[2]
                4 -> return@Formatter months[3]
                5 -> return@Formatter months[4]
                6 -> return@Formatter months[5]
                7 -> return@Formatter months[6]
                8 -> return@Formatter months[7]
                9 -> return@Formatter months[8]
                10 -> return@Formatter months[9]
                11 -> return@Formatter months[10]
                12 -> return@Formatter months[11]
                else -> return@Formatter null
            }
        }
        view.month_picker.setFormatter(formatter)
        // default for months
        view.month_picker.value = DateTime.now().monthOfYear
        // default for days
        view.day_picker.value = DateTime.now().dayOfMonth

        view.button_done_afraid_to_forget.setOnClickListener(this)


        return view
    }


    companion object {
        fun newInstance(viewPager: ViewPager, constants: Constants): AfraidToForgetFragment {
            val fragment = AfraidToForgetFragment()
            val args = Bundle()
            fragment.constants = constants
            fragment.viewPager = viewPager
            return fragment
        }
    }
}// Required empty public constructor
