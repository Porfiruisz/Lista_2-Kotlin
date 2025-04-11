package dev.k.myapplication

class Wielomian{
    val wspolczynniki: List<Int>

    constructor(n: List<Int>){
        val trimmed = n.dropLastWhile { it == 0 }
        wspolczynniki = if (trimmed.isEmpty()) listOf(0) else trimmed
    }

    fun stopien(): Int {
        return wspolczynniki.size - 1
    }


    // Zweryfikować
    fun toTekst(): String {
        if (wspolczynniki.all { it == 0 }) return "W(x) = 0"

        val n = wspolczynniki.size - 1
        val czesci = mutableListOf<String>()

        for (i in n downTo 0) {
            val a = wspolczynniki[i]
            if (a == 0) continue

            val znak = if (czesci.isEmpty()) {
                if (a < 0) "-" else ""
            } else {
                if (a < 0) " - " else " + "
            }

            val wartosc = kotlin.math.abs(a)

            val czesc = when (i) {
                0 -> "$wartosc"
                1 -> if (wartosc == 1) "x" else "${wartosc}x"
                else -> if (wartosc == 1) "x^$i" else "${wartosc}x^$i"
            }

            czesci.add(znak + czesc)
        }

        return "W(x) =" + czesci.joinToString("")
    }
}

fun main(){

    val wielomian = Wielomian(listOf(2, -1, 0, 3))  // 2 - x + 3x^3
    println(wielomian.toTekst())  // W(x) = 3x^3 - x + 2

}
