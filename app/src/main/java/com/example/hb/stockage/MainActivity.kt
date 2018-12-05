package com.example.hb.stockage

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity(), AnkoLogger {

    val courseDb by lazy { CourseDb() }
    var list = listOf<MobileCourse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Insert in DB
        doAsync {
            val course1 = MobileCourse("ABC Android",120)
            courseDb.saveCourse(course1)
        }

        // Read in DB
        doAsync {
            list = courseDb.requestCourse()

            uiThread {
                showList()
            }
        }
    }

    private fun showList() {
        info("NB COURSES : ${list.size}")
        for (c in list)
            info("Voici un course ${c.title}")
    }
}
