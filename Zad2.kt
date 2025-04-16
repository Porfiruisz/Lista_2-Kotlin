package com.example.probio2

/**
 * Klasa bazowa dla sekwencji nukleotydowych.
 *
 * Źródła:
 *
 * https://pl.wikipedia.org/wiki/FASTA_format
 * https://en.wikipedia.org/wiki/DNA_and_RNA_codon_tables
 * https://stackoverflow.com/questions/68437968/how-to-turn-dna-list-sequences-into-protein-sequences-in-python
 *
 * @property identifier Identyfikator sekwencji
 * @property data Sekwencja zasad nukleotydowych
 */
open class Baza(
    val identifier: String,
    var data: String
) {
    /**
     * Zbiór dozwolonych znaków w sekwencji.
     */
    open val VALID_CHARS: Set<Char> = emptySet()

    /**
     * Długość sekwencji.
     */
    val length: Int
        get() = data.length

    /**
     * Zmienia zasadę na określonej pozycji w sekwencji.
     *
     * @param position Pozycja w sekwencji
     * @param value Nowa wartość zasady
     * @throws IndexOutOfBoundsException jeśli pozycja jest poza zakresem
     * @throws IllegalArgumentException jeśli wartość nie jest dozwolona
     */
    open fun mutate(position: Int, value: Char) {
        if (position !in data.indices) {
            throw IndexOutOfBoundsException("Pozycja $position jest poza zakresem.")
        }
        if (value !in VALID_CHARS) {
            throw IllegalArgumentException("Znak '$value' nie jest dozwolony.")
        }

        val chars = data.toCharArray()
        chars[position] = value
        data = String(chars)
    }

    /**
     * Zwraca pozycję pierwszego wystąpienia motywu w sekwencji.
     *
     * @param motif Szukany motyw
     * @return Indeks pierwszego wystąpienia lub -1, jeśli nie znaleziono
     */
    fun findMotif(motif: String): Int {
        return data.indexOf(motif)
    }

    /**
     * Zwraca reprezentację sekwencji w formacie FASTA.
     */
    override fun toString(): String {
        return ">$identifier\n$data"
    }
}

/**
 * Klasa reprezentująca sekwencję DNA.
 */
class DNASequence(identifier: String, data: String) : Baza(identifier, data) {

    override val VALID_CHARS = setOf('A', 'T', 'C', 'G')

    /**
     * Zwraca nić komplementarną dla sekwencji DNA.
     */
    fun complement(): String {
        return data.map {
            when (it) {
                'A' -> 'T'
                'T' -> 'A'
                'C' -> 'G'
                'G' -> 'C'
                else -> error("Nieznana zasada: $it")
            }
        }.joinToString("")
    }

    /**
     * Transkrybuje sekwencję DNA do RNA.
     *
     * @return Obiekt RNASequence
     */
    fun transcribe(): RNASequence {
        val rnaData = data.replace('T', 'U')
        return RNASequence(identifier, rnaData)
    }
}

/**
 * Klasa reprezentująca sekwencję RNA.
 */
class RNASequence(identifier: String, data: String) : Baza(identifier, data) {

    override val VALID_CHARS = setOf('A', 'U', 'C', 'G')

    /**
     * Translacja sekwencji RNA do sekwencji białkowej.
     *
     * @return Obiekt ProteinSequence
     */
    fun transcribe(): ProteinSequence {
        val codonTable = mapOf(
            "UUU" to 'F', "UUC" to 'F', "UUA" to 'L', "UUG" to 'L',
            "CUU" to 'L', "CUC" to 'L', "CUA" to 'L', "CUG" to 'L',
            "AUU" to 'I', "AUC" to 'I', "AUA" to 'I', "AUG" to 'M',
            "GUU" to 'V', "GUC" to 'V', "GUA" to 'V', "GUG" to 'V',
            "UCU" to 'S', "UCC" to 'S', "UCA" to 'S', "UCG" to 'S',
            "CCU" to 'P', "CCC" to 'P', "CCA" to 'P', "CCG" to 'P',
            "ACU" to 'T', "ACC" to 'T', "ACA" to 'T', "ACG" to 'T',
            "GCU" to 'A', "GCC" to 'A', "GCA" to 'A', "GCG" to 'A',
            "UAU" to 'Y', "UAC" to 'Y', "UAA" to '*', "UAG" to '*',
            "CAU" to 'H', "CAC" to 'H', "CAA" to 'Q', "CAG" to 'Q',
            "AAU" to 'N', "AAC" to 'N', "AAA" to 'K', "AAG" to 'K',
            "GAU" to 'D', "GAC" to 'D', "GAA" to 'E', "GAG" to 'E',
            "UGU" to 'C', "UGC" to 'C', "UGA" to '*', "UGG" to 'W',
            "CGU" to 'R', "CGC" to 'R', "CGA" to 'R', "CGG" to 'R',
            "AGU" to 'S', "AGC" to 'S', "AGA" to 'R', "AGG" to 'R',
            "GGU" to 'G', "GGC" to 'G', "GGA" to 'G', "GGG" to 'G'
        )

        val proteinBuilder = StringBuilder()

        for (i in 0 until data.length step 3) {
            if (i + 2 >= data.length) break
            val codon = data.substring(i, i + 3)
            val aminoAcid = codonTable[codon] ?: error("Nieznany kodon: $codon")
            if (aminoAcid == '*') break
            proteinBuilder.append(aminoAcid)
        }

        return ProteinSequence(identifier, proteinBuilder.toString())
    }
}

/**
 * Klasa reprezentująca sekwencję białkową (aminokwasową).
 */
class ProteinSequence(identifier: String, data: String) : Baza(identifier, data) {

    override val VALID_CHARS = setOf(
        'A', 'R', 'N', 'D', 'C', 'E', 'Q', 'G', 'H', 'I',
        'L', 'K', 'M', 'F', 'P', 'S', 'T', 'W', 'Y', 'V',
        'X', '*'
    )
}

/**
 * Testy klas
 */
fun main() {
    // DNA
    val dna = DNASequence("Sample_001", "ATGGCCATTGTAATGGGCCGCTGAAAGGGTGCCCGATAG")
    println("DNA (FASTA): $dna")
    println("Komplementarna nic DNA: ${dna.complement()}")

    println("\n")

    // Transkrypcja
    val rna = dna.transcribe()
    println("RNA (FASTA): $rna")

    println("\n")

    // Mutacja RNA
    println("RNA przed mutacja: ${rna.data}")
    rna.mutate(2, 'A')
    println("RNA po mutacji: ${rna.data}")

    println("\n")

    // Szukanie motywu w RNA
    val motif = "GCG"
    val pos = rna.findMotif(motif)
    if (pos != -1) {
        println("Motyw '$motif' znaleziony na pozycji: $pos")
    } else {
        println("Motyw '$motif' nie zostal znaleziony.")
    }

    println("\n")

    // Translacja RNA do białka
    val protein = rna.transcribe()
    println("Bialko (FASTA): $protein")
    println("Dlugosc sekwencji bialka: ${protein.length}")

    println("\n")

    // Mutacja białka
    println("Bialko przed mutacja: ${protein.data}")
    if (protein.length > 0) {
        protein.mutate(0, 'G')
        println("Bialko po mutacji: ${protein.data}")
    } else {
        println("Nie mozna przeprowadzic mutacji – bialko jest puste.")
    }

    println("\n")

    // Szukanie motywu w białku
    val proteinMotif = "GA"
    val proteinPos = protein.findMotif(proteinMotif)
    if (proteinPos != -1) {
        println("Motyw '$proteinMotif' znaleziony w bialku na pozycji: $proteinPos")
    } else {
        println("Motyw '$proteinMotif' nie zostal znaleziony w bialku.")
    }
}
