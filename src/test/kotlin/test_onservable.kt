import kotlin.properties.Delegates.observable
import kotlin.reflect.KProperty

class Entity {

    lateinit var onTodosChange: (property: KProperty<*>,
                        oldValue: List<String>,
                        newValue: List<String>) -> Unit

    var data by observable(listOf<String>()){
        property, oldValue, newValue ->
        onTodosChange(property, oldValue, newValue)
    }
}


fun main() {
    val entity = Entity()
    entity.onTodosChange = { property, oldValue, newValue -> println("${property.name}: $oldValue -> $newValue") }
    entity.data = listOf<String>("a", "b", "c")
}