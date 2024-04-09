package com.example.infinitehorizontalscroll

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.infinitehorizontalscroll.ui.theme.InfiniteHorizontalScrollTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var adapter: CarouselAdapter
    private val items: List<ListItem> = listOf(ListItem(title = "1"), ListItem(title = "2"), ListItem(title = "3"), ListItem(title = "4"), ListItem(title = "5"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        adapter = CarouselAdapter(items)

//        val startOfRealItemIndex = 0
//        val endOfRealItemIndex = items.size
//        val somePositiveXValue = 0
//        val someNegativeXValue = 0
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        lifecycleScope.launch { autoScrollFeaturesList() }

//        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//
//                val firstVisiblePos = layoutManager.findFirstVisibleItemPosition()
//                val lastVisiblePos = layoutManager.findLastVisibleItemPosition()
//
//                if (firstVisiblePos < startOfRealItemIndex) {
//                    recyclerView.scrollBy(somePositiveXValue, 0)
//                } else if (lastVisiblePos > endOfRealItemIndex) {
//                    recyclerView.scrollBy(someNegativeXValue, 0)
//                }
//            }
//        })
    }

//    private fun setupFeatureTiles(featuresList: List<ListItem>) {
//        with(items) {
//            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
//            adapter = featuresAdapter
//        }
//        featuresAdapter.submitList(featuresList)

//        lifecycleScope.launch { autoScrollFeaturesList() }
//    }

    private tailrec suspend fun autoScrollFeaturesList() {
        val recyclerFeatures: RecyclerView = findViewById(R.id.recyclerView)
        val featuresAdapter = adapter

        if (recyclerFeatures.canScrollHorizontally(DIRECTION_RIGHT)) {
            recyclerFeatures.smoothScrollBy(SCROLL_DX, 0)
        } else {
            val firstPosition =
                (recyclerFeatures.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            if (firstPosition != RecyclerView.NO_POSITION) {
                val currentList = featuresAdapter.currentList
                val secondPart = currentList.subList(0, firstPosition)
                val firstPart = currentList.subList(firstPosition, currentList.size)
                featuresAdapter.submitList(firstPart + secondPart)
            }
        }
        delay(DELAY_BETWEEN_SCROLL_MS)
        autoScrollFeaturesList()
    }

    private const val DELAY_BETWEEN_SCROLL_MS = 25L
    private const val SCROLL_DX = 5
    private const val DIRECTION_RIGHT = 1
}