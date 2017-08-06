package com.example.jinux.chickensoup

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import com.example.jinux.chickensoup.about.AboutFragment
import com.example.jinux.chickensoup.task.TaskFragment
import kotlinx.android.synthetic.main.task_act.*

/**
 * Created by jingu on 2017/7/30.
 *
 * Activity 负责整合
 */

class TaskActivity : BaseActivity() {
    val PAGE_RECORD = 0
    val PAGE_ABOUT = 1

    private val drawerLayout by lazy {
        drawer_layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_act)

        // Set up the toolbar.
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setHomeAsUpIndicator(R.mipmap.ic_launcher)
            it.setDisplayHomeAsUpEnabled(true)
        }

        // Set up the navigation drawer.
        drawerLayout.setStatusBarBackground(R.color.colorPrimaryDark)

        setupDrawerContent(nav_view)

        showPage(PAGE_RECORD)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            // Open the navigation drawer when the home icon is selected from the toolbar.
            drawerLayout.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

                R.id.list_navigation_menu_item_record -> {
                    showPage(PAGE_RECORD)
                }

                R.id.list_navigation_menu_item_about -> {
                    showPage(PAGE_ABOUT)
                }

            }

            // Close the navigation drawer when an item is selected.
            drawerLayout.closeDrawers()
            true
        }
    }

    private fun showPage(pageId: Int) {
        val fragment = when (pageId) {
            0 -> {
                TaskFragment.newInstance()
            }
            else -> {
                AboutFragment()
            }
        }
        ActivityUtils.addFragmentToActivity(supportFragmentManager, fragment, R.id.contentFrame)
    }
}

/**
 * This provides methods to help Activities load their UI.
 */
object ActivityUtils {

    /**
     * The `fragment` is added to the container view with id `frameId`. The operation is
     * performed by the `fragmentManager`.
     */
    fun addFragmentToActivity(fragmentManager: FragmentManager,
                              fragment: Fragment, frameId: Int) {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(frameId, fragment)
        transaction.commit()
    }

}

