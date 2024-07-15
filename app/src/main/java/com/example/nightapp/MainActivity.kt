package com.example.nightapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nightapp.TestTileService.GlobalVariable
import java.io.File


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val seekBar: SeekBar = findViewById(R.id.seekBar)
        // 设置SeekBar的最大值
        seekBar.max = 255

        // 设置进度改变监听器
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // 当SeekBar的进度改变时调用
                setBrightness(progress.toFloat() / 255f)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // 当用户开始触摸SeekBar时调用
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // 当用户停止触摸SeekBar时调用
            }
        })





        val seekBar_2: SeekBar = findViewById(R.id.seekBar_2)
        seekBar_2.max = 100
        // 设置进度改变监听器
        seekBar_2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar_2: SeekBar?, progress: Int, fromUser: Boolean) {
                TestTileService.GlobalVariable.PMf = progress / 100f
                TestTileService.GlobalVariable.floatingView?.let { it.findViewById<View>(R.id.view_overlay).alpha = TestTileService.GlobalVariable.PMf }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // 当用户开始触摸SeekBar时调用
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // 当用户停止触摸SeekBar时调用
            }
        })






        val myButton: Button = findViewById(R.id.my_ButtonView)

        myButton.setOnClickListener {
            TestTileService.GlobalVariable.VariableBool = !TestTileService.GlobalVariable.VariableBool

            if(TestTileService.GlobalVariable.VariableBool){
                TestTileService.GlobalVariable.TileService?.create()
                myButton.text = "关闭"
            } else{
                TestTileService.GlobalVariable.TileService?.remove()
                myButton.text = "开启"
            }

            //弹出文字提醒
            //Toast.makeText(this, "开启悬浮窗", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setBrightness(level: Float) {
        val layoutParams = window.attributes
        layoutParams.screenBrightness = level
        window.attributes = layoutParams
    }

    private fun getBrightness(): Float {
        return window.attributes.screenBrightness
    }

    /*
     // 创建悬浮窗
            if (!Settings.canDrawOverlays(this)) {
                // 没有权限，提示用户去开启
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + this.packageName))
                this.startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE)
            } else {
                // 已经有权限，可以创建悬浮窗
                createFloatingView()
            }
    */
}
