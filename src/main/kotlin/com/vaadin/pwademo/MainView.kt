/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.pwademo

import com.github.vok.framework.sql2o.findAll
import com.github.vok.framework.sql2o.vaadin.dataProvider
import com.github.vok.karibudsl.flow.*
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.router.Route
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.page.BodySize
import com.vaadin.flow.component.page.Viewport
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo

@BodySize(width = "100vw", height = "100vh")
@HtmlImport("frontend://styles.html")
@Route("")
@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes")
@Theme(Lumo::class)
class MainView : VerticalLayout() {
    private lateinit var label: Label
    private val grid: Grid<Task>
    init {
        setSizeFull()

        button("Click me") {
            onLeftClick {
                label.text = "Clicked!"
            }
        }
        label = label("Not Clicked")

        grid = grid {
            dataProvider = Task.dataProvider
            addColumn(ComponentRenderer<Checkbox, Task> { it -> Checkbox(it.completed )}).apply {
                flexGrow = 0
            }
            addColumn(Task::title)
        }

        // todo move into the grid when Karibu-DSL is fixed
        setFlexGrow(1.0, grid)
    }
}
