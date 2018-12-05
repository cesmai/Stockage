package com.example.hb.stockage

import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.insert

class CourseDb(private val dbHelper: CourseDbHelper = CourseDbHelper.instance) {

    fun requestCourse() = dbHelper.use {

        select(
            MobileCourseTable.NAME,
            MobileCourseTable.TITLE,
            MobileCourseTable.TIME
        ).parseList(classParser<MobileCourse>())
    }


    fun saveCourse(course: MobileCourse) = dbHelper.use {

        insert(
            MobileCourseTable.NAME,
            MobileCourseTable.TITLE to course.title,
            MobileCourseTable.TIME to course.time
        )
    }


    fun saveCourses(courseList: List<MobileCourse>) {
        for (c in courseList)
            saveCourse(c)
    }
}