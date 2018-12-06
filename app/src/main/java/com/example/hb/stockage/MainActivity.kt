package com.example.hb.stockage

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.hb.stockage.app.src.main.Course
import com.example.hb.stockage.app.src.main.CoursesService
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity(), AnkoLogger {

    val courseDb by lazy { CourseDb() }
    var list = listOf<MobileCourse>()

    private val url = "http://mobile-courses-server.herokuapp.com/"


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


        //=== Sample of HTTP GET

        // Retrofit client
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        // service
        val service = retrofit.create(CoursesService::class.java)

        // GET
        val courseRequest = service.listCourses()

        // enqueue attend un objet de type Callback qu'on implémente à la volée entre les deux {}
        courseRequest.enqueue(object: Callback<List<Course>> {

            override fun onResponse(call: Call<List<Course>>, response: Response<List<Course>>) {
                val allCourses = response.body()
                if (allCourses != null) {
                    info("MyApp >> HERE is ALL COURSES FROM HEROKU SERVER:")
                    for (c in allCourses)
                        info("MyApp >> one course : ${c.title} : ${c.img} ")
                }
            }

            override fun onFailure(call: Call<List<Course>>, t: Throwable) {
                error("KO")
            }
        })
    }

    private fun showList() {
        info("MyApp >> NB COURSES : ${list.size}")
        for (c in list)
            info("MyApp >> Voici un course ${c.title}")
    }
}
