package com.vaadin.pwademo

import com.github.vok.framework.sql2o.Dao
import com.github.vok.framework.sql2o.Entity

open class Task(override var id: Long? = null, open var title: String = "", open var completed: Boolean = false) : Entity<Long> {
    companion object : Dao<Task>
}
