package com.example.nightapp

import android.content.Context
import android.graphics.PixelFormat
import android.provider.Settings
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import java.io.File


class TestTileService : TileService() {
    private lateinit var windowManager: WindowManager

    object GlobalVariable {
        var VariableBool:Boolean = false
        var PMf:Float = 0.5f
        var floatingView: LinearLayout? = null
        var TileService: TestTileService? = null
    }

    override fun onCreate() {
        super.onCreate()

        GlobalVariable.TileService = this

    }

    override fun onClick() {
        GlobalVariable.VariableBool = !GlobalVariable.VariableBool

        if(GlobalVariable.VariableBool){
            create()
            qsTile.state = Tile.STATE_ACTIVE
        }else{
            remove()
            qsTile.state = Tile.STATE_INACTIVE
        }
        qsTile.updateTile()
        //Toast.makeText(this, "点我干嘛！", Toast.LENGTH_SHORT).show()
    }

    //创建悬浮窗口
    fun create(){
        // 创建悬浮窗
        if (Settings.canDrawOverlays(this) or (GlobalVariable.floatingView == null)) {
            // 已经有权限，可以创建悬浮窗
            createFloatingView()
        }
        qsTile.state = Tile.STATE_ACTIVE
        qsTile.updateTile()
    }

    //销毁悬浮窗口
    fun remove(){
        GlobalVariable.floatingView?.let {
            windowManager.removeView(it)
            GlobalVariable.floatingView = null
        }
        qsTile.state = Tile.STATE_INACTIVE
        qsTile.updateTile()
    }



    //创建悬浮窗口，覆盖应用形成遮罩效果（点击穿透，透明，无事件）
    private fun createFloatingView() {
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val inflater = LayoutInflater.from(this)
        GlobalVariable.floatingView = inflater.inflate(R.layout.floating_window_layout, null) as LinearLayout

        // 设置悬浮窗参数
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,// 设置宽度为设备屏幕宽度
            WindowManager.LayoutParams.MATCH_PARENT,// 设置宽度为设备屏幕高度
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or //可以超出屏幕边界
                    WindowManager.LayoutParams.FLAG_SECURE, // 截图录屏不会被遮挡
            PixelFormat.TRANSLUCENT
        )

        //设置透明度
        GlobalVariable.floatingView?.let { it.findViewById<View>(R.id.view_overlay).alpha = GlobalVariable.PMf }

        // 添加悬浮窗到屏幕
        windowManager.addView(GlobalVariable.floatingView, params)
    }
}