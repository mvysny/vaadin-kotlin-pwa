package com.vaadin.pwademo.tasks

import com.github.mvysny.ktormvaadin.ActiveEntity
import com.github.mvysny.ktormvaadin.ActiveKtorm.database
import com.github.mvysny.ktormvaadin.db
import com.github.mvysny.ktormvaadin.deleteAll
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length
import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.isEmpty
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDate
import kotlin.random.Random

object Tasks : Table<Task>("task") {
   val id = long("id").primaryKey().bindTo { it.id }
    val title = varchar("title").bindTo { it.title }
    val completed = boolean("completed").bindTo { it.completed }
    val created = date("created").bindTo { it.created }
    @JvmStatic
    private val log: Logger = LoggerFactory.getLogger(Tasks::class.java)
    fun generateSampleData() {
        if (database.tasks.isEmpty()) {
            log.info("Task table is empty, populating with test data")
            db {
                sampleData.forEach {
                    Task {
                        title = it
                        completed = Random.nextBoolean()
                        created = LocalDate.now()
                    }.save()
                }
            }
        }
    }
    fun regenerateSampleData() {
        db {
            Tasks.deleteAll()
            generateSampleData()
        }
    }
}

val Database.tasks get() = this.sequenceOf(Tasks)

interface Task : ActiveEntity<Task> {
    var id: Long?
    @get:NotNull
    @get:Length(min = 2)
    var title: String
    @get:NotNull
    var completed: Boolean
    @get:NotNull
    var created: LocalDate
    override val table: Table<Task>
        get() = Tasks

    companion object : Entity.Factory<Task>() {
        fun newEmpty() = Task { title = ""; completed = false; created = LocalDate.now() }
    }
}

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

