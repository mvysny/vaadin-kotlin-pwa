package com.vaadin.pwademo

import com.github.vok.framework.sql2o.Dao
import com.github.vok.framework.sql2o.Entity
import com.github.vok.framework.sql2o.db
import java.util.*

private val sampleData = listOf(
    "Evian",
    "Voss",
    "Veen",
    "San Pellegrino",
    "Perrier", "Coca-Cola",
    "Fanta",
    "Sprite", "Maxwell Ready-to-Drink Coffee",
    "Nescafé Gold",
    "Starbucks East Timor Tatamailau", "Prince Of Peace Organic White Tea",
    "Pai Mu Tan White Peony Tea",
    "Tazo Zen Green Tea",
    "Dilmah Sencha Green Tea",
    "Twinings Earl Grey",
    "Twinings Lady Grey",
    "Classic Indian Chai", "Cow's Milk",
    "Goat's Milk",
    "Unicorn's Milk",
    "Salt Lassi",
    "Mango Lassi",
    "Airag", "Crowmoor Extra Dry Apple",
    "Golden Cap Perry",
    "Somersby Blueberry",
    "Kopparbergs Naked Apple Cider",
    "Kopparbergs Raspberry",
    "Kingstone Press Wild Berry Flavoured Cider",
    "Crumpton Oaks Apple",
    "Frosty Jack's",
    "Ciderboys Mad Bark",
    "Angry Orchard Stone Dry",
    "Walden Hollow",
    "Fox Barrel Wit Pear", "Budweiser",
    "Miller",
    "Heineken",
    "Holsten Pilsener",
    "Krombacher",
    "Weihenstephaner Hefeweissbier",
    "Ayinger Kellerbier",
    "Guinness Draught",
    "Kilkenny Irish Cream Ale",
    "Hoegaarden White",
    "Barbar",
    "Corsendonk Agnus Dei",
    "Leffe Blonde",
    "Chimay Tripel",
    "Duvel",
    "Pilsner Urquell",
    "Kozel",
    "Staropramen",
    "Lapin Kulta IVA",
    "Kukko Pils III",
    "Finlandia Sahti", "Jacob's Creek Classic Shiraz",
    "Chateau d’Yquem Sauternes",
    "Oremus Tokaji Aszú 5 Puttonyos", "Pan Galactic Gargle Blaster",
    "Mead",
    "Soma"
)

open class Task(override var id: Long? = null, open var title: String = "", open var completed: Boolean = false) : Entity<Long> {
    companion object : Dao<Task> {
        fun generateSampleData() {
            db {
                sampleData.forEach { Task(title = it, completed = Random().nextBoolean()).save() }
            }
        }
    }
}
