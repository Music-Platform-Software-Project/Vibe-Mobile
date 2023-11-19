package view.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.cs308_00.R

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment.
        return when (position) {
            0 -> IncomingRequests.newInstance("s", "v")
            else -> MyRequests.newInstance("s", "v")
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val tabTitles = arrayOf("Incoming Requests", "My Requests")
        return tabTitles[position]
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 2
    }
}