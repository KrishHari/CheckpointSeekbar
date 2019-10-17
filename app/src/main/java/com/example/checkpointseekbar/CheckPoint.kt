package com.example.checkpointseekbar

class CheckPoint(
    val topText: String?,
    val bottomText: String?,
    val progress: Int,
    val secondBottomText: String?,
    // Condition calculated from backend
    val someCondition:Boolean)