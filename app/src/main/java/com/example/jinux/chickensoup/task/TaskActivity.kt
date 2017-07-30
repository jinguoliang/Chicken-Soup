package com.example.jinux.chickensoup.task

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.example.jinux.chickensoup.BaseActivity
import com.example.jinux.chickensoup.R
import com.example.jinux.chickensoup.database.HttpDataBase
import com.example.jinux.chickensoup.main.TaskFragment
import com.ohmerhe.kolley.request.Http
import org.jetbrains.anko.setContentView

/**
 * Created by jingu on 2017/7/30.
 *
 * Activity 负责整合
 */

class TaskActivity() : BaseActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_act)

        // Set up the toolbar.
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setHomeAsUpIndicator(R.mipmap.ic_launcher)
            it.setDisplayHomeAsUpEnabled(true)
        }

        // Set up the navigation drawer.
        drawerLayout = (findViewById<DrawerLayout>(R.id.drawer_layout)).apply {
            setStatusBarBackground(R.color.colorPrimaryDark)
        }
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        setupDrawerContent(navigationView)

        val tasksFragment = supportFragmentManager.findFragmentById(R.id.contentFrame)
                as TaskFragment? ?: TaskFragment.newInstance().also {
            ActivityUtils.addFragmentToActivity(
                    supportFragmentManager, it, R.id.contentFrame)
        }

        Http.init(this)
        TaskPresenter(HttpDataBase(this), tasksFragment, "俯卧撑")
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
//            if (menuItem.itemId == R.id.statistics_navigation_menu_item) {
//
//            }
            // Close the navigation drawer when an item is selected.
            menuItem.isChecked = true
            drawerLayout.closeDrawers()
            true
        }
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
            transaction.add(frameId, fragment)
            transaction.commit()
        }

    }

