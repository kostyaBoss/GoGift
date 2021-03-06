package com.blacksmithdreamapps.presentgo.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blacksmithdreamapps.presentgo.R
import com.blacksmithdreamapps.presentgo.events.ImageChangeEvent
import com.blacksmithdreamapps.presentgo.events.PagerPasserEvent
import kotlinx.android.synthetic.main.fragment_gender.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


/**
 * Developed and designed by Dream Apps Blacksmith
 * Code author is Boskin Kostya
 * On 027 27.02.2018.
 */

class GenderFragment : Fragment(), View.OnClickListener {

    lateinit var viewPager: ViewPager
    lateinit var preferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

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
                R.id.male_cv -> {
                    preferences = context.getSharedPreferences(com.blacksmithdreamapps.presentgo.Constants.SHARED_PREFS, Context.MODE_PRIVATE)
                    editor = preferences.edit();
                    editor.putString(com.blacksmithdreamapps.presentgo.Constants.SEX, com.blacksmithdreamapps.presentgo.Constants.SEX_MALE)
                    editor.commit()
                    // event bus to track changes
                    val bus = EventBus.getDefault()
                    bus.post(object : ImageChangeEvent(1) {})

                    viewPager.currentItem = viewPager.currentItem + 1
                }
                R.id.female_cv -> {
                    preferences = context.getSharedPreferences(com.blacksmithdreamapps.presentgo.Constants.SHARED_PREFS, Context.MODE_PRIVATE)
                    editor = preferences.edit();
                    editor.putString(com.blacksmithdreamapps.presentgo.Constants.SEX, com.blacksmithdreamapps.presentgo.Constants.SEX_FEMALE)
                    // event bus to track changes
                    val bus = EventBus.getDefault()
                    bus.post(object : ImageChangeEvent(0) {})

                    editor.commit()
                    viewPager.currentItem = viewPager.currentItem + 1
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_gender, container, false)

        view.female_cv.setOnClickListener(this)
        view.male_cv.setOnClickListener(this)

        return view
    }


    companion object {
        fun newInstance(viewPager: ViewPager): GenderFragment {
            val fragment = GenderFragment()
            val args = Bundle()

            fragment.viewPager = viewPager
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
