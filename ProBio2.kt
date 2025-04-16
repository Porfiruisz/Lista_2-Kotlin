package com.example.probio2

/**
 * Klasa reprezentująca wielomian o współczynnikach rzeczywistych.
 *
 * @property wspolczynniki Lista współczynników wielomianu, od wyrazu wolnego do najwyższej potęgi.
 * @throws IllegalArgumentException jeśli lista współczynników jest pusta.
 */
class Wielomian(private val wspolczynniki: List<Double>) {

    init {
        if (wspolczynniki.isEmpty()) {
            throw IllegalArgumentException("Lista współczynników nie może być pusta.")
        }
    }

    /**
     * Wyświetla współczynniki wielomianu w kolejności od wyrazu wolnego do najwyższej potęgi.
     */
    fun wyswietlWspolczynniki() {
        println(wspolczynniki.joinToString(", "))
    }

    /**
     * Zwraca stopień wielomianu, czyli najwyższą potęgę zmiennej z niezerowym współczynnikiem.
     *
     * @return Stopień wielomianu.
     */
    fun stopien(): Int {
        for (i in wspolczynniki.indices.reversed()) {
            if (wspolczynniki[i] != 0.0) {
                return i
            }
        }
        return 0
    }

    /**
     * Zwraca tekstową reprezentację wielomianu w postaci:
     * W(x) = anx^n + ... + a1x + a0
     *
     * @return Reprezentacja tekstowa wielomianu.
     */
    fun reprezentacjaTekstowa(): String {
        val terms = mutableListOf<String>()

        for (i in wspolczynniki.indices.reversed()) {
            val wspolczynnik = wspolczynniki[i]
            if (wspolczynnik != 0.0) {
                val term = when {
                    i == 0 -> "$wspolczynnik"
                    i == 1 -> "${wspolczynnik}x"
                    else -> "${wspolczynnik}x^$i"
                }
                terms.add(term)
            }
        }

        return "W(x) = " + terms.joinToString(" + ")
    }

    /**
     * Oblicza wartość wielomianu dla zadanej wartości x.
     *
     * @param x Punkt, w którym należy obliczyć wartość wielomianu.
     * @return Wartość wielomianu w punkcie x.
     */
    operator fun invoke(x: Double): Double {
        var wynik = 0.0
        for (i in wspolczynniki.indices) {
            wynik += wspolczynniki[i] * Math.pow(x, i.toDouble())
        }
        return wynik
    }
}


fun main() {
    // Tworzenie wielomianu W(x) = 3x^3 + 0x^2 - 2x + 5
    val wielomian = Wielomian(listOf(5.0, -2.0, 0.0, 3.0))

    // Wyświetlenie współczynników
    println("Wspolczynniki wielomianu:")
    wielomian.wyswietlWspolczynniki()

    println("\n")

    // Obliczenie i wyświetlenie stopnia wielomianu
    val stopien = wielomian.stopien()
    println("Stopien wielomianu: $stopien")

    println("\n")

    // Reprezentacja tekstowa wielomianu
    val tekst = wielomian.reprezentacjaTekstowa()
    println("Reprezentacja tekstowa: $tekst")

    println("\n")

    // Obliczenie wartości wielomianu dla różnych x
    val punkty = listOf(-2.0, 0.0, 1.0, 2.0)
    for (x in punkty) {
        val wartosc = wielomian(x)
        println("W($x) = $wartosc")
    }

    println("\n")

    // Test dla wielomianu zerowego oprócz wyrazu wolnego: W(x) = 4
    val stały = Wielomian(listOf(4.0))
    println("\nReprezentacja wielomianu stalego:")
    println(stały.reprezentacjaTekstowa())
    println("W(10) = ${stały(10.0)}")
}

