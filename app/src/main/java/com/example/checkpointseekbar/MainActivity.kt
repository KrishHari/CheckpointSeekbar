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
            CheckPoint("200", "22", 22, "30", true)
        )
        checkMarks.add(CheckPoint("400", "30", 30, "40", true))
        checkMarks.add(CheckPoint("600", "36", 36, "60", false))
        checkMarks.add(CheckPoint("800", "44", 44, "80", false))
        customProgressBar.setCheckPoints(checkMarks)
        val areAllCheckPointsNotMet = areAllCheckPointsNotMet(checkMarks)

        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (areAllCheckPointsNotMet) {
                    if (progress >= checkMarks[0].progress) {
                        customProgressBar.progress = checkMarks[0].progress - 1
                        progress_text.setText(customProgressBar.progress.toString())
                    } else {
                        customProgressBar.progress = progress
                        progress_text.setText(progress.toString())
                    }
                } else {
                    for (checkPoint in checkMarks) {
                        if (checkPoint.someCondition) {
                            if (checkPoint.progress >= progress) {
                                customProgressBar.progress = progress
                                progress_text.setText(progress.toString())
                            } else {
                                customProgressBar.progress = checkPoint.progress
                                progress_text.setText(progress.toString())
                            }
                        }
                    }
                }
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

    fun areAllCheckPointsNotMet(checkPoints: List<CheckPoint>): Boolean {
        for (checkPoint in checkPoints) {
            if (checkPoint.someCondition) return false
        }
        return true
    }
}
