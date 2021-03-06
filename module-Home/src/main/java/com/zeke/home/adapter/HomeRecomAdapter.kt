package com.zeke.home.adapter

import android.view.ViewGroup
import com.kingz.module.common.adapter.IDelegateAdapter
import com.zeke.home.entity.HomeRecomData
import com.zeke.kangaroo.adapter.CommonRecyclerAdapter


/**
 * author: King.Z <br>
 * date:  2020/5/24 13:05 <br>
 * description: 支持多种viewType的首页推荐Adapter <br>
 */
class HomeRecomAdapter : CommonRecyclerAdapter<HomeRecomData>() {

    var delegateAdapters: MutableList<IDelegateAdapter<HomeRecomData>> = ArrayList()

    fun addDelegate(delegateAdapter: IDelegateAdapter<HomeRecomData>) {
        delegateAdapters.add(delegateAdapter)
    }

    override fun getItemLayout(type: Int): Int { return 0 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // 找到对应的委托Adapter 把onCreateViewHolder交给委托Adapter去处理
        return delegateAdapters[viewType].onCreateViewHolderWithCommon(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        val recomData = getItem(position)
        // 遍历所有的代理，查询谁能处理
        delegateAdapters.forEach {
            if(it.isForViewType(recomData)){
                // 谁能处理就返回他的index
                return delegateAdapters.indexOf(it)
            }
        }
        return 0
        // throw RuntimeException("没有找到可以处理的委托Adapter")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 找到当前ViewHolder的ViewType，也就是委托Adapter在集合中的index
        val viewType = holder.itemViewType
        // 找到对应的委托Adapter
        val delegateAdapter = delegateAdapters[viewType]
        // 把onBindViewHolder交给委托Adapter去处理
        delegateAdapter.onBindViewHolder(holder, position, getItem(position))
    }
}