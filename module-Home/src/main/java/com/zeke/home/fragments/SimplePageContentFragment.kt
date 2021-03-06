package com.zeke.home.fragments

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kingz.module.common.IView
import com.kingz.module.common.base.BaseFragment
import com.kingz.module.home.R
import com.zeke.home.entity.Live
import com.zeke.kangaroo.adapter.CommonRecyclerAdapter

/**
 * author：KingZ
 * date：2019/12/29
 * description：首页/点播/每个tab页
 * //可以 将Recycle替换为DragRecyclerView
 */
class SimplePageContentFragment : BaseFragment(), IView, View.OnClickListener {

    private lateinit var mRecycleView: RecyclerView
    var mRV: RVAdapter = RVAdapter()

    override val isShown: Boolean
        get() = false

    override fun getLayoutId(): Int {
        return R.layout.single_recyclerview
    }

    override fun onViewCreated() {
        mRecycleView = rootView.findViewById(R.id.content_recycler)
        mRecycleView.setHasFixedSize(true)
        mRecycleView.layoutManager = GridLayoutManager(activity!!,2)
        mRecycleView.adapter = mRV
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError() {
    }

    override fun showEmpty() {

    }

    override fun showMessage(tips: String) {

    }

    override fun onClick(v: View) {
    }

    inner class RVAdapter : CommonRecyclerAdapter<Live>(){
        init { // Initializer Block
            //为Primary Constructor服务
        }

        override fun getItemLayout(type: Int): Int {
            return R.layout.live_channel_item
        }

        override fun onBindViewHolder(holder: CommonRecyclerAdapter.ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val data = getItem(position)
            holder.getView<TextView>(R.id.channel_id).text = (data as Live).name
        }
    }
}
