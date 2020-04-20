package com.zeke.ktx.api

import android.content.Context
import android.os.AsyncTask
import android.util.JsonReader
import android.widget.Toast
import com.starcor.ui.performance.utils.ZLog
import com.zeke.ktx.api.callback.IDataResponse
import com.zeke.ktx.modules.player.entity.HomeRecomData
import com.zeke.ktx.modules.player.entity.PageContent
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

/**
 * author：KingZ
 * date：2020/2/21
 * description：首页推荐数据实现类
 */
class RecomDataServiceImpl constructor() : DataApiService<MutableList<HomeRecomData>> {
    companion object {
        private val TAG = RecomDataServiceImpl::class.java.simpleName
        private lateinit var mCallBack: IDataResponse<MutableList<HomeRecomData>>

    }

    override fun requestData(context: Context,
                             callback: IDataResponse<MutableList<HomeRecomData>>) {
        mCallBack = callback
        val assetManager = context.assets
        val uris: Array<String>
        val uriList = ArrayList<String>()

        try {
            for (asset in assetManager.list("")) {
                if (asset.endsWith("_data.json")) {
                    uriList.add(asset)
                }
            }
        } catch (e: IOException) {
            Toast.makeText(context, "No home_data.json file to load !!!",
                    Toast.LENGTH_LONG).show()
        }
        uris = uriList.toTypedArray()
        uris.sort()

        // 异步加载本地数据，模拟网络请求
        HomeDataLoader(context).execute(uris[0])
    }


    class HomeDataLoader(var ctx: Context)
        : AsyncTask<String, Void, MutableList<HomeRecomData>>() {
        private var sawError: Boolean = false

        override fun doInBackground(vararg parms: String?): MutableList<HomeRecomData> {
            val homeRecomData = ArrayList<HomeRecomData>()

            for (uri in parms) {
                try {
                    val inputStream = ctx.assets.open(uri)
                    praseLocalJsonData(JsonReader(
                            InputStreamReader(inputStream, "UTF-8")),
                            homeRecomData)
                } catch (e: Exception) {
                    ZLog.e(TAG, "Error loading sample list: $uri", e)
                    sawError = true
                }

            }
            return homeRecomData
        }

        // 等同于Java的 throws IOException
        @Throws(IOException::class)
        private fun praseLocalJsonData(
                reader: JsonReader,
                pages: MutableList<HomeRecomData>) {
            reader.beginArray()
            while (reader.hasNext()) {
                parsePage(reader, pages)
            }
            reader.endArray()
        }

        @Throws(IOException::class)
        private fun parsePage(
                reader: JsonReader,
                pages: MutableList<HomeRecomData>) {
            var pageId = ""
            var pageName = ""
            var pageType = ""
            var pageContent: MutableList<PageContent>? = null
            val pageLIst = ArrayList<HomeRecomData>()
            reader.beginObject()
            while (reader.hasNext()) {
                val node = reader.nextName()
                // switch-case
                when (node) {
                    "id" -> pageId = reader.nextString()
                    "name" -> pageName = reader.nextString()
                    "type" -> pageType = reader.nextString()
                    "page_content" -> {
                        reader.beginArray()
                        while (reader.hasNext()) {
                            pageContent = readPageContent(reader)
                        }
                        reader.endArray()
                    }
                    else -> reader.nextString()
                }
            }
            reader.endObject()
            pageLIst.add(HomeRecomData(pageId, pageName, pageType, pageContent))
            pages.addAll(pageLIst)
        }

        @Throws(IOException::class)
        private fun readPageContent(reader: JsonReader): MutableList<PageContent> {
            var content_id: String? = ""
            var type: String? = ""
            var content_name: String? = ""
            var pageContentList = ArrayList<PageContent>()
            reader.beginObject()
            while (reader.hasNext()) {
                val name = reader.nextName()
                when (name) {
                    "id" -> content_id = reader.nextString()
                    "type" -> type = reader.nextString()
                    "name" -> content_name = reader.nextString()
                    else -> reader.nextString()
                }
                pageContentList.add(PageContent(content_id!!, type!!))
            }
            reader.endObject()
            return pageContentList
        }

        override fun onPostExecute(result: MutableList<HomeRecomData>) {
            super.onPostExecute(result)
            //TODO 处理完毕返回数据

            mCallBack.onSuccess(result)
        }

    }
}
