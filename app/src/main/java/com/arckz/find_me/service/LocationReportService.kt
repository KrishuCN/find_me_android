package com.arckz.find_me.service

import android.util.Log
import com.arckz.find_me.base.BaseApplication
import com.arckz.find_me.okhttp.OkHttpUtil
import com.arckz.find_me.util.CommonUtil
import com.arckz.find_me.util.LocationUtils
import com.arckz.find_me.util.Url
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.Poi
import com.blankj.utilcode.util.LogUtils
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response

/**
 * <pre>
 *
 *     author: Hy
 *     time  : 2019/05/07  下午 5:19
 *     desc  : Start locate and report the result
 *
 * </pre>
 */
class LocationReportService {
    var locationUtils: LocationUtils? = null
    var bdAbstractLocationListener: MyLocationListener? = null

    init {
        locationUtils = LocationUtils(BaseApplication.INSTANCE?.applicationContext)
        bdAbstractLocationListener = MyLocationListener()
    }

    fun startLoc(){
        if (locationUtils!=null && bdAbstractLocationListener != null){
            locationUtils!!.registerListener(bdAbstractLocationListener)
        }

        locationUtils!!.setLocationOption(locationUtils!!.defaultLocationClientOption)
        locationUtils!!.start()
    }

    inner class MyLocationListener:BDAbstractLocationListener(){
        override fun onReceiveLocation(location: BDLocation?) {
            location?.run {
                if (location.locType != BDLocation.TypeServerError) {
                    val sb = StringBuffer(256)
                    sb.append("time : ")
                    /**
                     * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                     * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                     */
                    sb.append(time)
                    sb.append("\nlocType : ")// 定位类型
                    sb.append(locType)
                    sb.append("\nlocType description : ")// *****对应的定位类型说明*****
                    sb.append(locTypeDescription)
                    sb.append("\nlatitude : ")// 纬度
                    sb.append(latitude)
                    sb.append("\nlontitude : ")// 经度
                    sb.append(longitude)
                    sb.append("\nradius : ")// 半径
                    sb.append(radius)
                    sb.append("\nCountryCode : ")// 国家码
                    sb.append(countryCode)
                    sb.append("\nCountry : ")// 国家名称
                    sb.append(country)
                    sb.append("\ncitycode : ")// 城市编码
                    sb.append(cityCode)
                    sb.append("\ncity : ")// 城市
                    sb.append(city)
                    sb.append("\nDistrict : ")// 区
                    sb.append(district)
                    sb.append("\nStreet : ")// 街道
                    sb.append(street)
                    sb.append("\naddr : ")// 地址信息
                    sb.append(addrStr)
                    sb.append("\nUserIndoorState: ")// *****返回用户室内外判断结果*****
                    sb.append(userIndoorState)
                    sb.append("\nDirection(not all devices have value): ")
                    sb.append(direction)// 方向
                    sb.append("\nlocationdescribe: ")
                    sb.append(locationDescribe)// 位置语义化信息
                    sb.append("\nPoi: ")// POI信息
                    if (poiList != null && !poiList.isEmpty()) {
                        for (i in 0 until poiList.size) {
                            val poi = poiList[i] as Poi
                            sb.append(poi.name + ";")
                        }
                    }
                    when (locType) {
                        BDLocation.TypeGpsLocation -> { //GPS定位结果
                            sb.append("\nspeed : ")
                            sb.append(speed)// 速度 单位：km/h
                            sb.append("\nsatellite : ")
                            sb.append(satelliteNumber)// 卫星数目
                            sb.append("\nheight : ")
                            sb.append(altitude)// 海拔高度 单位：米
                            sb.append("\ngps status : ")
                            sb.append(gpsAccuracyStatus)// *****gps质量判断*****
                            sb.append("\ndescribe : ")
                            sb.append("gps定位成功")
                        }
                        BDLocation.TypeNetWorkLocation -> {//网络定位结果
                            // 运营商信息
                            if (hasAltitude()) {// *****如果有海拔高度*****
                                sb.append("\nheight : ")
                                sb.append(altitude)// 单位：米
                            }
                            sb.append("\noperationers : ")// 运营商信息
                            sb.append(operators)
                            sb.append("\ndescribe : ")
                            sb.append("网络定位成功")
                        }
                        BDLocation.TypeOffLineLocation -> {// 离线定位结果
                            sb.append("\ndescribe : ")
                            sb.append("离线定位成功，离线定位结果也是有效的")
                        }
                        BDLocation.TypeServerError -> {
                            sb.append("\ndescribe : ")
                            sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因")
                        }
                        BDLocation.TypeNetWorkException -> {
                            sb.append("\ndescribe : ")
                            sb.append("网络不同导致定位失败，请检查网络是否通畅")
                        }
                        BDLocation.TypeCriteriaException -> {
                            sb.append("\ndescribe : ")
                            sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机")
                        }
                    }
                    Log.d("MYTAG", sb.toString())

                    val map = OkHttpUtil.getBaseData()
                    map["lng"] = longitude
                    map["lat"] = latitude
                    map["loc"] = "你老公在这里"
                    map["con"] = addrStr

                    //上传服务器
                    OkGo.post<String>(Url.SERVER_LOCATION_REPORT)
                        .upJson(CommonUtil.getJSONObject(map))
                        .execute(object :StringCallback(){
                            override fun onSuccess(response: Response<String>?) {
                                //上传成功
                                LogUtils.d("上传位置信息成功")
                            }

                            override fun onError(response: Response<String>?) {
                                super.onError(response)
                                LogUtils.d("上传位置信息失败:\n $response")
                            }
                        })

                    //释放
                    locationUtils?.stop()
                    locationUtils?.unregisterListener(bdAbstractLocationListener)
                }
            }
        }
    }

}