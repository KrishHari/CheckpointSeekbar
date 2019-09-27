package com.example.checkpointseekbar

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var checkMarks = ArrayList<CheckPoint>()

        checkMarks.add(
            CheckPoint("200", "22", 22, "30")
        )
        checkMarks.add(CheckPoint("400", "30", 30, "40"))
        checkMarks.add(CheckPoint("600", "36", 36, "60"))
        checkMarks.add(CheckPoint("800", "44", 44, "80"))
        customProgressBar.setCheckPoints(checkMarks)


        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                customProgressBar.progress = progress
                progress_text.setText(progress.toString())
            }

        })
        progress_button.setOnClickListener {
            try {
                seekbar.progress = Integer.parseInt(progress_text.text.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
